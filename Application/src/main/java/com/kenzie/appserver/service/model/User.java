package com.kenzie.appserver.service.model;

import java.util.List;

public class User {
    private final String id;
    private final List<Task> taskList;
    private final String completionRate;

    public User(String id, List<Task> taskList, String completionRate){
        this.id=id;
        this.taskList=taskList;
        this.completionRate=completionRate;
    }

    public String getId() {
        return id;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public String getCompletionRate() {
        return completionRate;
    }
}
