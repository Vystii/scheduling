package com.vaybe.scheduling.dto;

import java.util.List;

public class SchoolClassesRequestDTO {
    private boolean shouldDeleteSchoolClass;
    private List<SchoolClassDTO> data;

    // Getters and setters
    public boolean isShouldDeleteSchoolClass() {
        return shouldDeleteSchoolClass;
    }

    public void setShouldDeleteSchoolClass(boolean shouldDeleteSchoolClass) {
        this.shouldDeleteSchoolClass = shouldDeleteSchoolClass;
    }

    public List<SchoolClassDTO> getData() {
        return data;
    }

    public void setData(List<SchoolClassDTO> data) {
        this.data = data;
    }
}
