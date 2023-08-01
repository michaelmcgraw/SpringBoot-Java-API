package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.kenzie.appserver.service.model.Task;

import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "User")
public class UserRecord {
    private String id;
    private List<Task> taskList;
    private String completionRate;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @DynamoDBAttribute(attributeName = "taskList")
    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
    @DynamoDBAttribute(attributeName = "completionRate")
    public String getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(String completionRate) {
        this.completionRate = completionRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRecord that = (UserRecord) o;
        return Objects.equals(id, that.id) && Objects.equals(taskList, that.taskList) && Objects.equals(completionRate, that.completionRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskList, completionRate);
    }
}
