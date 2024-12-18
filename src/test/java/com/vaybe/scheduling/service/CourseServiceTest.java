package com.vaybe.scheduling.service;

import com.vaybe.scheduling.dto.CourseDTO;
import com.vaybe.scheduling.model.Course;
import com.vaybe.scheduling.model.SchoolClass;
import com.vaybe.scheduling.repository.CourseRepository;
import com.vaybe.scheduling.repository.SchoolClassRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private SchoolClassRepository schoolClassRepository;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCourses() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setLevel("Beginner");
        courseDTO.setSchoolClassId("CLASS_001");

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("CLASS_001");

        Course course = new Course();
        course.setLevel("Beginner");
        course.setSchoolClass(schoolClass);

        when(schoolClassRepository.findById("CLASS_001")).thenReturn(Optional.of(schoolClass));
        when(courseRepository.saveAll(any())).thenReturn(Arrays.asList(course));

        List<Course> createdCourses = courseService
                .createCourses(courseService.createCoursesFromRequest(Arrays.asList(courseDTO)), true);

        assertEquals(1, createdCourses.size());
        assertEquals("Beginner", createdCourses.get(0).getLevel());
        verify(courseRepository, times(1)).deleteAll();
        verify(courseRepository, times(2)).saveAll(any());
    }

    @Test
    public void testGetAllCourses() {
        Course course1 = new Course();
        Course course2 = new Course();

        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<Course> result = courseService.getAllCourses();

        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteCourseById() {
        doNothing().when(courseRepository).deleteById(1L);

        courseService.deleteCourseById(1L);

        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdateCourse() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setLevel("Advanced");
        courseDTO.setSchoolClassId("CLASS_002");

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("CLASS_002");

        Course course = new Course();
        course.setLevel("Advanced");
        course.setSchoolClass(schoolClass);

        when(courseRepository.existsById(1L)).thenReturn(true);
        when(schoolClassRepository.findById("CLASS_002")).thenReturn(Optional.of(schoolClass));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course result = courseService.updateCourse(1L, courseDTO);

        assertNotNull(result);
        assertEquals("Advanced", result.getLevel());
    }

    @Test
    public void testGetCourseById() {
        Course course = new Course();
        course.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course result = courseService.getCourseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }
}
