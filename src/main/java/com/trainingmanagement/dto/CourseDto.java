package com.trainingmanagement.dto;

import java.time.LocalDateTime;

public class CourseDto {
    private Long id;
    private String title;
    private String description;
    private String coverImage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UserDto instructor;
    private int studentCount;
    private int moduleCount;
    private String category;
    private String type;
    private String level;
    private Integer durationHours;
    private String address;
    private String room;
    private Integer capacity;
    private String onlineLink;
    private String connectionInstructions;
    private Double cost;
    private Boolean fullPayment;
    private Boolean installmentPayment;
    private Boolean fundingAvailable;

    // Getters and Setters
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

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public UserDto getInstructor() {
        return instructor;
    }

    public void setInstructor(UserDto instructor) {
        this.instructor = instructor;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public int getModuleCount() {
        return moduleCount;
    }

    public void setModuleCount(int moduleCount) {
        this.moduleCount = moduleCount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Integer durationHours) {
        this.durationHours = durationHours;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getOnlineLink() {
        return onlineLink;
    }

    public void setOnlineLink(String onlineLink) {
        this.onlineLink = onlineLink;
    }

    public String getConnectionInstructions() {
        return connectionInstructions;
    }

    public void setConnectionInstructions(String connectionInstructions) {
        this.connectionInstructions = connectionInstructions;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Boolean getFullPayment() {
        return fullPayment;
    }

    public void setFullPayment(Boolean fullPayment) {
        this.fullPayment = fullPayment;
    }

    public Boolean getInstallmentPayment() {
        return installmentPayment;
    }

    public void setInstallmentPayment(Boolean installmentPayment) {
        this.installmentPayment = installmentPayment;
    }

    public Boolean getFundingAvailable() {
        return fundingAvailable;
    }

    public void setFundingAvailable(Boolean fundingAvailable) {
        this.fundingAvailable = fundingAvailable;
    }
}