package com.vaybe.scheduling.service;

import com.vaybe.scheduling.dto.SchoolClassDTO;
import com.vaybe.scheduling.dto.SchoolClassesRequestDTO;
import com.vaybe.scheduling.model.SchoolClass;
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

public class SchoolClassServiceTest {

    @Mock
    private SchoolClassRepository schoolClassRepository;

    @InjectMocks
    private SchoolClassService schoolClassService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateClassesFromRequest() {
        SchoolClassDTO schoolClassDTO = new SchoolClassDTO();
        schoolClassDTO.setName("Class 1");
        schoolClassDTO.setNumberOfStudents(25);
        schoolClassDTO.setLevel("L1");

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("Class 1");
        schoolClass.setNumberOfStudents(25);
        schoolClass.setLevel("L1");

        when(schoolClassRepository.save(any(SchoolClass.class))).thenReturn(schoolClass);

        SchoolClassesRequestDTO requestDTO = new SchoolClassesRequestDTO();
        requestDTO.setShouldDeleteSchoolClass(false);
        requestDTO.setData(Arrays.asList(schoolClassDTO));

        List<SchoolClass> createdClasses = schoolClassService.createClassesFromRequest(requestDTO);

        assertEquals(1, createdClasses.size());
        assertEquals("Class 1", createdClasses.get(0).getName());
        verify(schoolClassRepository, times(1)).save(any(SchoolClass.class));
    }

    @Test
    public void testAddClass() {
        SchoolClassDTO schoolClassDTO = new SchoolClassDTO();
        schoolClassDTO.setName("Class 1");
        schoolClassDTO.setNumberOfStudents(25);
        schoolClassDTO.setLevel("L1");

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("Class 1");
        schoolClass.setNumberOfStudents(25);
        schoolClass.setLevel("L1");

        when(schoolClassRepository.save(any(SchoolClass.class))).thenReturn(schoolClass);

        SchoolClass addedClass = schoolClassService.addClass(schoolClassDTO);

        assertNotNull(addedClass);
        assertEquals("Class 1", addedClass.getName());
        verify(schoolClassRepository, times(1)).save(any(SchoolClass.class));
    }

    @Test
    public void testGetClassById() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName("CLASS_001");
        schoolClass.setName("Class 1");

        when(schoolClassRepository.findById("CLASS_001")).thenReturn(Optional.of(schoolClass));

        Optional<SchoolClass> result = schoolClassService.getClassById("CLASS_001");

        assertTrue(result.isPresent());
        assertEquals("Class 1", result.get().getName());
    }

    @Test
    public void testGetAllClasses() {
        SchoolClass schoolClass1 = new SchoolClass();
        SchoolClass schoolClass2 = new SchoolClass();

        when(schoolClassRepository.findAll()).thenReturn(Arrays.asList(schoolClass1, schoolClass2));

        List<SchoolClass> result = schoolClassService.getAllClasses();

        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteClassById() {
        doNothing().when(schoolClassRepository).deleteById("CLASS_001");

        schoolClassService.deleteClassById("CLASS_001");

        verify(schoolClassRepository, times(1)).deleteById("CLASS_001");
    }

    @Test
    public void testUpdateClass() {
        SchoolClassDTO schoolClassDTO = new SchoolClassDTO();
        schoolClassDTO.setName("CLASS_001");
        schoolClassDTO.setNumberOfStudents(30);
        schoolClassDTO.setLevel("L3");

        SchoolClass existingClass = new SchoolClass();
        existingClass.setName("CLASS_001");

        when(schoolClassRepository.existsById("CLASS_001")).thenReturn(true);
        when(schoolClassRepository.save(any(SchoolClass.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SchoolClass result = schoolClassService.updateClass("CLASS_001", schoolClassDTO);

        assertNotNull(result);
        assertEquals("CLASS_001", result.getId());
        assertEquals("CLASS_001", result.getName());
        assertEquals(30, result.getNumberOfStudents());
        assertEquals("L3", result.getLevel());
    }
}
