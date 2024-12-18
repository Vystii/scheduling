package com.vaybe.scheduling.service;

import com.vaybe.scheduling.dto.CourseDTO;
import com.vaybe.scheduling.model.Course;
import com.vaybe.scheduling.model.SchoolClass;
import com.vaybe.scheduling.repository.CourseRepository;
import com.vaybe.scheduling.repository.SchoolClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SchoolClassRepository schoolClassRepository;

    public List<Course> createCourses(List<Course> courses, boolean deleteExistingCourses) {
        if (deleteExistingCourses) {
            courseRepository.deleteAll();
        }
        return courseRepository.saveAll(courses);
    }

    public List<Course> createCoursesFromRequest(List<CourseDTO> courseDTOs) {
        List<Course> courses = new ArrayList<>();
        for (CourseDTO courseDTO : courseDTOs) {
            Course course = new Course();
            course.setId(courseDTO.getId());
            course.setLevel(courseDTO.getLevel());

            SchoolClass schoolClass = schoolClassRepository.findById(courseDTO.getSchoolClassId()).orElse(null);
            course.setSchoolClass(schoolClass);

            courses.add(course);
        }
        courseRepository.saveAll(courses);
        return courses;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);
    }

    public Course updateCourse(Long id, CourseDTO courseDTO) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found");
        }
        Course course = new Course();
        course.setId(id);
        course.setLevel(courseDTO.getLevel());

        SchoolClass schoolClass = schoolClassRepository.findById(courseDTO.getSchoolClassId()).orElse(null);
        course.setSchoolClass(schoolClass);

        return courseRepository.save(course);
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }
}
