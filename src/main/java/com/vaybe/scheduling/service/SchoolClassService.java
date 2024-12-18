package com.vaybe.scheduling.service;

import com.vaybe.scheduling.dto.SchoolClassDTO;
import com.vaybe.scheduling.dto.SchoolClassesRequestDTO;
import com.vaybe.scheduling.model.SchoolClass;
import com.vaybe.scheduling.repository.SchoolClassRepository;
import com.vaybe.scheduling.repository.CourseRepository;
import com.vaybe.scheduling.repository.ScheduleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SchoolClassService {

    @Autowired
    private SchoolClassRepository schoolClassRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Transactional
    public List<SchoolClass> createClassesFromRequest(SchoolClassesRequestDTO schoolClassesRequestDTO) {
        if (schoolClassesRequestDTO == null) {
            return new ArrayList<>();
        }

        if (schoolClassesRequestDTO.isShouldDeleteSchoolClass()) {
            scheduleRepository.deleteAll();
            scheduleRepository.flush();
            courseRepository.deleteAll();
            courseRepository.flush();
            schoolClassRepository.deleteAll();
            schoolClassRepository.flush(); // Ensure synchronization with the database
        }

        List<SchoolClassDTO> schoolClassDTOs = schoolClassesRequestDTO.getData();
        if (schoolClassDTOs == null) {
            return new ArrayList<>();
        }

        List<SchoolClass> schoolClasses = new ArrayList<>();
        for (SchoolClassDTO schoolClassDTO : schoolClassDTOs) {
            SchoolClass schoolClass = new SchoolClass();
            schoolClass.setName(schoolClassDTO.getName());
            schoolClass.setNumberOfStudents(schoolClassDTO.getNumberOfStudents());
            schoolClass.setLevel(schoolClassDTO.getLevel());
            schoolClasses.add(schoolClassRepository.save(schoolClass)); // Save each class individually
        }

        return schoolClasses;
    }

    public SchoolClass addClass(SchoolClassDTO schoolClassDTO) {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(schoolClassDTO.getName());
        schoolClass.setNumberOfStudents(schoolClassDTO.getNumberOfStudents());
        schoolClass.setLevel(schoolClassDTO.getLevel());
        return schoolClassRepository.save(schoolClass);
    }

    public Optional<SchoolClass> getClassById(String id) {
        return schoolClassRepository.findById(id);
    }

    public List<SchoolClass> getAllClasses() {
        return schoolClassRepository.findAll();
    }

    public void deleteClassById(String id) {
        schoolClassRepository.deleteById(id);
    }

    public SchoolClass updateClass(String id, SchoolClassDTO schoolClassDTO) {
        if (!schoolClassRepository.existsById(id)) {
            throw new RuntimeException("School class not found");
        }
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(schoolClassDTO.getName());
        schoolClass.setNumberOfStudents(schoolClassDTO.getNumberOfStudents());
        schoolClass.setLevel(schoolClassDTO.getLevel());
        return schoolClassRepository.save(schoolClass);
    }
}
