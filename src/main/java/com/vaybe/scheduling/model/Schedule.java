package com.vaybe.scheduling.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String classId;
    private String courseId;

    @ManyToOne
    private Room room;

    @ManyToOne
    private TimeSlot timeSlot;

    @ManyToOne
    private SchoolClass schoolClass; // Add this field to link with SchoolClass

    public String whoAmI() {
        return "-------------"
                + "\nid: " + id
                + "\ntitle: " + title
                + "\ndescription: " + description
                + "\nclassId: " + classId
                + "\ncourseId: " + courseId + "\n";
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public String getPresentation() {
        return "title: " + title
                + "\ndescription: " + description
                + "\nid: " + id
                + "\nroomId: " + room.getName()
                + "\nclassId: " + classId;
    }
}
