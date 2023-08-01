package com.kenzie.appserver.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {

    private final String userId;
    private final String taskId;
    private final String taskName;
    private final String taskDescription;

    private final String status;
    private final String failureReason;
    private final String date;

    public Task(String userId, String taskId, String taskName, String taskDescription, String status, String failureReason, String date){
        this.userId = userId;
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
        this.failureReason = failureReason;
        this.date = date;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public String getTaskDescription() {
        return this.taskDescription;
    }

    public String getStatus() {
        return this.status;
    }

    public String getFailureReason() {
        return this.failureReason;
    }

    public String getDate() {
        return date;
    }
}
