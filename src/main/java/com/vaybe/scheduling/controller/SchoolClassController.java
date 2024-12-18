package com.vaybe.scheduling.controller;

import com.vaybe.scheduling.dto.SchoolClassDTO;
import com.vaybe.scheduling.dto.SchoolClassesRequestDTO;
import com.vaybe.scheduling.model.SchoolClass;
import com.vaybe.scheduling.service.SchoolClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schoolclasses")
public class SchoolClassController {

    @Autowired
    private SchoolClassService schoolClassService;

    @PostMapping("/add")
    public SchoolClass addClass(@RequestBody SchoolClassDTO classDTO) {
        return schoolClassService.addClass(classDTO);
    }

    @PostMapping("/create-multiple")
    public List<SchoolClass> createClasses(@RequestBody SchoolClassesRequestDTO classesRequest) {
        return schoolClassService.createClassesFromRequest(classesRequest);
    }

    @GetMapping("/get/{id}")
    public Optional<SchoolClass> getClassById(@PathVariable String id) {
        return schoolClassService.getClassById(id);
    }

    @GetMapping("/get-all")
    public List<SchoolClass> getAllClasses() {
        return schoolClassService.getAllClasses();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteClassById(@PathVariable String id) {
        schoolClassService.deleteClassById(id);
    }

    @PutMapping("/update/{id}")
    public SchoolClass updateClass(@PathVariable String id, @RequestBody SchoolClassDTO classDTO) {
        return schoolClassService.updateClass(id, classDTO);
    }
}
