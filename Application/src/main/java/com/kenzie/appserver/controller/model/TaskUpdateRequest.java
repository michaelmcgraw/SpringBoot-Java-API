package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;

public class TaskUpdateRequest {

    @NotEmpty
    @JsonProperty("taskId")
    private String taskId;

    @JsonProperty("taskName")
    private String taskName;

    @JsonProperty("taskDescription")
    private String taskDescription;

    @NotEmpty
    @JsonProperty("status")
    private String status;

    @JsonProperty("failureReason")
    private String failureReason;



    public String getTaskId() {
        return taskId;
    }

    public void setTaskID(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
