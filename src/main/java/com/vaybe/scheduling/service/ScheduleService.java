package com.vaybe.scheduling.service;

import com.vaybe.scheduling.dto.CourseDTO;
import com.vaybe.scheduling.dto.RoomDTO;
import com.vaybe.scheduling.dto.ScheduleRequestDTO;
import com.vaybe.scheduling.dto.ScheduleResponseDTO;
import com.vaybe.scheduling.model.Schedule;
import com.vaybe.scheduling.model.TimeSlot;
import com.vaybe.scheduling.model.Settings;
import com.vaybe.scheduling.repository.ScheduleRepository;
import com.vaybe.scheduling.repository.TimeSlotRepository;
import com.vaybe.scheduling.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule updateSchedule(Long id, Schedule updatedSchedule) {
        Optional<Schedule> existingScheduleOptional = scheduleRepository.findById(id);
        if (existingScheduleOptional.isPresent()) {
            Schedule existingSchedule = existingScheduleOptional.get();
            existingSchedule.setTitle(updatedSchedule.getTitle());
            existingSchedule.setDescription(updatedSchedule.getDescription());
            existingSchedule.setRoomId(updatedSchedule.getRoomId());
            existingSchedule.setClassId(updatedSchedule.getClassId());
            existingSchedule.setCourseId(updatedSchedule.getCourseId());
            existingSchedule.setTimeSlot(updatedSchedule.getTimeSlot());
            return scheduleRepository.save(existingSchedule);
        } else {
            throw new RuntimeException("Schedule not found");
        }
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public ScheduleResponseDTO generateSchedule(ScheduleRequestDTO request, boolean deleteExistingSchedules) {
        // Step 1: Retrieve and break dependencies
        if (deleteExistingSchedules) {
            List<Schedule> existingSchedules = scheduleRepository.findAll();
            for (Schedule schedule : existingSchedules) {
                if (schedule.getTimeSlot() != null) {
                    schedule.setTimeSlot(null);
                    scheduleRepository.save(schedule);
                }
            }

            // Step 2: Delete TimeSlots
            timeSlotRepository.deleteAll();

            // Step 3: Delete Schedules
            scheduleRepository.deleteAll();
        }

        List<Schedule> generatedSchedules = new ArrayList<>();
        List<CourseDTO> unscheduledCourses = new ArrayList<>(request.getCourses());
        Set<Long> scheduledCourseIds = new HashSet<>();

        int granularity = request.getGranularity() > 0 ? request.getGranularity() : 180; // Default to 3 hours if not
                                                                                         // provided
        int pauseDuration = 15; // Default pause duration in minutes
        List<Integer> weekdays = request.getWeekdays();
        List<RoomDTO> rooms = request.getRooms();

        // Initialize time slots for each day
        List<List<TimeSlotData>> weekTimeSlots = initializeTimeSlots(weekdays, granularity, pauseDuration);

        // Implement the scheduling logic
        for (CourseDTO course : request.getCourses()) {
            if (scheduledCourseIds.contains(course.getId())) {
                continue; // Skip already scheduled courses
            }

            boolean scheduled = false;
            for (int day = 0; day < weekTimeSlots.size(); day++) {
                List<TimeSlotData> dayTimeSlots = weekTimeSlots.get(day);
                for (TimeSlotData timeSlotData : dayTimeSlots) {
                    if (timeSlotData.isAvailable() && timeSlotData.canAccommodate(granularity)) {
                        for (RoomDTO room : rooms) {
                            if (room.getCapacity() >= course.getExpectedStudents()
                                    && timeSlotData.assignCourse(course, room, granularity)) {
                                Schedule schedule = createScheduleWithoutTimeSlot(course, room, day + 1);
                                generatedSchedules.add(schedule);
                                saveTimeSlotForSchedule(timeSlotData, schedule, day + 1);
                                scheduledCourseIds.add(course.getId());
                                scheduled = true;
                                break;
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
                unscheduledCourses.add(course);
            } else {
                unscheduledCourses.remove(course);
            }
        }

        ScheduleResponseDTO response = new ScheduleResponseDTO();
        response.setSchedule(generatedSchedules);
        response.setUnscheduled(unscheduledCourses);
        return response;
    }

    private List<List<TimeSlotData>> initializeTimeSlots(List<Integer> weekdays, int granularity, int pauseDuration) {
        List<List<TimeSlotData>> weekTimeSlots = new ArrayList<>();
        for (int day = 0; day < weekdays.size(); day++) {
            int daySlots = weekdays.get(day);
            List<TimeSlotData> dayTimeSlots = new ArrayList<>();
            int totalMinutes = daySlots * granularity + (daySlots - 1) * pauseDuration;
            LocalDateTime startTime = LocalDateTime.of(2024, 1, day + 1, 8, 0); // Example start time
            for (int i = 0; i < totalMinutes; i += (granularity + pauseDuration)) {
                LocalDateTime endTime = startTime.plusMinutes(granularity);
                dayTimeSlots.add(new TimeSlotData(startTime, endTime));
                startTime = endTime.plusMinutes(pauseDuration);
            }
            weekTimeSlots.add(dayTimeSlots);
        }
        return weekTimeSlots;
    }

    private Schedule createScheduleWithoutTimeSlot(CourseDTO course, RoomDTO room, int dayOfWeek) {
        // Create the Schedule entity without setting the TimeSlot
        Schedule schedule = new Schedule();
        schedule.setTitle("Course " + course.getId());
        schedule.setDescription("Level: " + course.getLevel());
        schedule.setRoomId(room.getId());
        schedule.setClassId(course.getClassId());
        schedule.setCourseId(course.getId());

        // Save the Schedule entity
        return scheduleRepository.save(schedule);
    }

    private void saveTimeSlotForSchedule(TimeSlotData timeSlotData, Schedule schedule, int dayOfWeek) {
        // Check if a TimeSlot already exists
        Optional<TimeSlot> existingTimeSlot = timeSlotRepository.findByStartTimeAndEndTimeAndDayOfWeek(
                timeSlotData.getStartTime(), timeSlotData.getEndTime(), dayOfWeek);

        TimeSlot timeSlot;
        if (existingTimeSlot.isPresent()) {
            timeSlot = existingTimeSlot.get();
        } else {
            // Create and save a new TimeSlot entity
            timeSlot = new TimeSlot();
            timeSlot.setStartTime(timeSlotData.getStartTime());
            timeSlot.setEndTime(timeSlotData.getEndTime());
            timeSlot.setDayOfWeek(dayOfWeek);
            timeSlotRepository.save(timeSlot);
        }

        // Set the TimeSlot in Schedule and save it
        schedule.setTimeSlot(timeSlot);
        scheduleRepository.save(schedule);
    }

    private class TimeSlotData {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private boolean available = true;
        private Map<String, CourseDTO> assignedCourses = new HashMap<>(); // Keyed by classId

        public TimeSlotData(LocalDateTime startTime, LocalDateTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public boolean isAvailable() {
            return available;
        }

        public boolean canAccommodate(int granularity) {
            return available && (endTime.compareTo(startTime.plusMinutes(granularity))) != -1;
        }

        public boolean assignCourse(CourseDTO course, RoomDTO room, int granularity) {
            if (isAvailable()
                    && canAccommodate(granularity)
                    && room.getCapacity() >= course.getExpectedStudents()
                    && !assignedCourses.containsKey(course.getClassId())) {
                this.available = false;
                assignedCourses.put(course.getClassId(), course);
                return true;
            }
            return false;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }
    }

    public void saveSettings(int granularity, int pauseDuration) {
        Settings settings = new Settings();
        settings.setGranularity(granularity);
        settings.setPauseDuration(pauseDuration);
        settingsRepository.save(settings);
    }

    public Settings getSettings() {
        return settingsRepository.findAll().stream().findFirst().orElse(null);
    }

}
