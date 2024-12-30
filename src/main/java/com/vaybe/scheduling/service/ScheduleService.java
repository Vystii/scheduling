package com.vaybe.scheduling.service;

import com.vaybe.scheduling.dto.ScheduleRequestDTO;
import com.vaybe.scheduling.dto.ScheduleResponseDTO;
import com.vaybe.scheduling.model.Course;
import com.vaybe.scheduling.model.Room;
import com.vaybe.scheduling.model.Schedule;
import com.vaybe.scheduling.model.SchoolClass;
import com.vaybe.scheduling.model.Settings;
import com.vaybe.scheduling.model.TimeSlot;
import com.vaybe.scheduling.repository.ScheduleRepository;
import com.vaybe.scheduling.repository.SchoolClassRepository;
import com.vaybe.scheduling.repository.TimeSlotRepository;
import com.vaybe.scheduling.util.TimeSlotData;
import com.vaybe.scheduling.repository.RoomRepository;
import com.vaybe.scheduling.repository.SettingsRepository;
import com.vaybe.scheduling.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private SchoolClassRepository schoolClassRepository;

    @Autowired
    private SchoolClassService schoolClassService;

    @Autowired
    private CourseRepository courseRepository;

    @Transactional
    public ScheduleResponseDTO generateSchedule(ScheduleRequestDTO request, boolean deleteExistingSchedules) {
        if (deleteExistingSchedules) {
            scheduleRepository.deleteAll();
            timeSlotRepository.deleteAll();
        }

        List<Schedule> generatedSchedules = new ArrayList<>();
        List<Course> allCourses = courseRepository.findAll(); // Retrieve courses from repository
        Set<String> scheduledCourseIds = new HashSet<>();

        Settings settings = getSettings();
        int granularity = settings != null ? settings.getGranularity()
                : (request.getGranularity() > 0 ? request.getGranularity() : 180);
        int pauseDuration = settings != null ? settings.getPauseDuration() : 15;
        if (settings == null)
            saveSettings(granularity, pauseDuration);

        List<Integer> weekdays = request.getWeekdays();

        List<List<TimeSlotData>> weekTimeSlots = initializeTimeSlots(weekdays, granularity, pauseDuration);

        // Retrieve rooms from repository
        List<Room> rooms = roomRepository.findAll();
        for (Course course : allCourses) {
            if (scheduledCourseIds.contains(course.getId()))
                continue;
            boolean scheduled = false;
            for (int day = 0; day < weekTimeSlots.size(); day++) {
                List<TimeSlotData> dayTimeSlots = weekTimeSlots.get(day);
                for (TimeSlotData timeSlotData : dayTimeSlots) {
                    if (timeSlotData.isAvailable() && timeSlotData.canAccommodate(granularity)) {
                        for (Room room : rooms) {
                            SchoolClass schoolClass = course.getSchoolClass(); // Retrieve school class from course
                            if (schoolClass != null && room.getCapacity() >= schoolClass.getNumberOfStudents() &&
                                    timeSlotData.assignCourse(course, room, granularity)) {
                                if (isRoomAndClassAvailable(room, schoolClass.getId(),
                                        new TimeSlot(timeSlotData.getStartTime(), timeSlotData.getEndTime(), day + 1),
                                        day + 1, generatedSchedules)) {
                                    Schedule schedule = createScheduleWithoutTimeSlot(course, room, schoolClass,
                                            day + 1);
                                    generatedSchedules.add(schedule);
                                    saveTimeSlotForSchedule(timeSlotData, schedule, day + 1);
                                    scheduledCourseIds.add(course.getId());
                                    scheduled = true;
                                    break;
                                }
                            }
                        }
                        if (scheduled)
                            break;
                    }
                }
                if (scheduled)
                    break;
            }

            if (!scheduled) {
                // Handle unscheduled courses if needed
            }
        }

        ScheduleResponseDTO response = new ScheduleResponseDTO();
        response.setSchedule(generatedSchedules);
        // Add unscheduled courses to response if applicable
        return response;
    }

    public List<Schedule> getSchedulesByCourseIds(List<String> courseIds) {
        return scheduleRepository.findByCourseIdIn(courseIds);
    }

    public Schedule addSchedule(Schedule schedule, String roomId) {
        Optional<SchoolClass> classe = schoolClassRepository.findById(schedule.getClassId());
        Optional<Room> room = roomRepository.findById(roomId);
        if (classe.get() == null || room.get() == null) {
            return null;
        }
        schedule.setRoom(room.get());
        return scheduleRepository.save(schedule);
    }

    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public void deleteScheduleById(Long id) {
        scheduleRepository.deleteById(id);
    }

    public Schedule updateSchedule(Long id, Schedule schedule) {
        if (!scheduleRepository.existsById(id)) {
            throw new RuntimeException("Schedule not found");
        }
        schedule.setId(id);
        return scheduleRepository.save(schedule);
    }

    private Schedule createScheduleWithoutTimeSlot(Course course, Room room, SchoolClass schoolClass, int dayOfWeek) {
        Schedule schedule = new Schedule();
        schedule.setTitle("Course " + course.getId());
        schedule.setDescription("Level: " + course.getLevel());
        schedule.setClassId(course.getSchoolClass().getId());
        schedule.setCourseId(course.getId());
        schedule.setRoom(room);
        schedule.setSchoolClass(schoolClass);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        return savedSchedule;
    }

    private void saveTimeSlotForSchedule(TimeSlotData timeSlotData, Schedule schedule, int dayOfWeek) {
        Optional<TimeSlot> existingTimeSlot = timeSlotRepository.findByStartTimeAndEndTimeAndDayOfWeek(
                timeSlotData.getStartTime(), timeSlotData.getEndTime(), dayOfWeek);

        TimeSlot timeSlot;
        if (existingTimeSlot.isPresent()) {
            timeSlot = existingTimeSlot.get();
        } else {
            timeSlot = new TimeSlot();
            timeSlot.setStartTime(timeSlotData.getStartTime());
            timeSlot.setEndTime(timeSlotData.getEndTime());
            timeSlot.setDayOfWeek(dayOfWeek);
            timeSlot = timeSlotRepository.save(timeSlot); // Save and get ID
        }

        schedule.setTimeSlot(timeSlot);
        scheduleRepository.save(schedule);
    }

    private boolean isRoomAndClassAvailable(Room room, String classId, TimeSlot timeSlot, int dayOfWeek,
            List<Schedule> generatedSchedules) {
        if (room == null) {
            return false;
        }

        Optional<SchoolClass> schoolClassOpt = schoolClassService.getClassById(classId);
        if (schoolClassOpt.isEmpty()) {
            return false; // School class not found
        }

        SchoolClass schoolClass = schoolClassOpt.get();
        if (room.getCapacity() < schoolClass.getNumberOfStudents()) {
            return false; // Room capacity is less than the number of students in the class
        }

        for (Schedule schedule : generatedSchedules) {
            if (!schedule.getId().equals(timeSlot.getId()) &&
                    schedule.getRoom().getId().equals(room.getId()) &&
                    schedule.getTimeSlot().getDayOfWeek() == dayOfWeek &&
                    timeSlotsOverlap(schedule.getTimeSlot(), timeSlot)) {
                return false; // Room is already booked
            }
            if (!schedule.getId().equals(timeSlot.getId()) &&
                    schedule.getSchoolClass().getId().equals(classId) &&
                    schedule.getTimeSlot().getDayOfWeek() == dayOfWeek &&
                    timeSlotsOverlap(schedule.getTimeSlot(), timeSlot)) {
                return false; // Class is already following another course
            }
        }
        return true;
    }

    private List<List<TimeSlotData>> initializeTimeSlots(List<Integer> weekdays, int granularity, int pauseDuration) {
        List<List<TimeSlotData>> weekTimeSlots = new ArrayList<>();
        for (int weekday : weekdays) {
            List<TimeSlotData> dayTimeSlots = new ArrayList<>();
            LocalDateTime startTime = LocalDateTime.now().withHour(8).withMinute(0).withSecond(0).withNano(0); // Start
                                                                                                               // at 8
                                                                                                               // AM
            for (int i = 0; i < weekday; i++) {
                LocalDateTime endTime = startTime.plusMinutes(granularity);
                dayTimeSlots.add(new TimeSlotData(startTime, endTime, pauseDuration));
                startTime = endTime.plusMinutes(pauseDuration);
            }
            weekTimeSlots.add(dayTimeSlots);
        }
        return weekTimeSlots;
    }

    private Settings getSettings() {
        return settingsRepository.findById(1L).orElse(null);
    }

    private void saveSettings(int granularity, int pauseDuration) {
        Settings settings = new Settings();
        settings.setId(1L);
        settings.setGranularity(granularity);
        settings.setPauseDuration(pauseDuration);
        settingsRepository.save(settings);
    }

    private boolean timeSlotsOverlap(TimeSlot existingSlot, TimeSlot newSlot) {
        return (newSlot.getStartTime().isBefore(existingSlot.getEndTime()) &&
                newSlot.getEndTime().isAfter(existingSlot.getStartTime()));
    }
}
