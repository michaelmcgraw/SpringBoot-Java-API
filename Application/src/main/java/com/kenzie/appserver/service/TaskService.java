package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.TaskRepository;
import com.kenzie.appserver.repositories.model.TaskRecord;
import com.kenzie.appserver.service.model.Task;
import org.springframework.stereotype.Service;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TaskService {

    private TaskRepository taskRepository;
    private List<String> failureMessages;
    String filePath = "FailureMessages.csv";
    InputStream inputStream = TaskService.class.getClassLoader().getResourceAsStream(filePath);

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.failureMessages= readFromCSV(inputStream);
    }

    public List<Task> findTasksByUserId(String userId) {
        /*
        List<Task> tasks = new ArrayList<>();
        taskRepository.findTasksByUserId(userId)
                .stream()
                .filter(task -> task.getUserId().equals(userId))
                .forEach(task -> tasks.add(new Task(task.getUserId(), task.getTaskId(), task.getTaskName(), task.getTaskDescription(), task.isCompleted(), task.isFailed(), task.getFailureReason(), task.getDate())));
        return tasks;

         */
        return taskRepository.findTasksByUserId(userId);
    }

    public Task findById(String taskId){
        Task taskToRetrieve = taskRepository
                .findById(taskId)
                .map(task -> new Task(task.getUserId(),task.getTaskId(), task.getTaskName(), task.getTaskDescription(),task.getStatus(), task.getFailureReason(), task.getDate()))
                .orElse(null);
        return taskToRetrieve;
    }

    public Task addNewTask(Task task) {
        TaskRecord taskRecord = new TaskRecord();
        taskRecord.setUserId(task.getUserId());
        taskRecord.setTaskId(task.getTaskId());
        taskRecord.setTaskName(task.getTaskName());
        taskRecord.setTaskDescription(task.getTaskDescription());
        taskRecord.setStatus(task.getStatus());
        taskRecord.setFailureReason(task.getFailureReason());
        taskRecord.setDate(task.getDate());
        taskRepository.save(taskRecord);
        return task;
    }

    //implement
    public void updateTask(Task task) {
        // Retrieve the existing task from the repository
        TaskRecord existingTaskRecord = taskRepository.findById(task.getTaskId()).orElse(null);

        if (existingTaskRecord != null) {
            // Update the task properties with the new values
            existingTaskRecord.setTaskId(task.getTaskId());
            existingTaskRecord.setTaskName(task.getTaskName());
            existingTaskRecord.setTaskDescription(task.getTaskDescription());
            existingTaskRecord.setStatus(task.getStatus());
            existingTaskRecord.setFailureReason(task.getFailureReason());

            // Save the updated task record to the repository
            taskRepository.save(existingTaskRecord);
        }
    }

    public static List<String> readFromCSV(InputStream inputStream) {
        List<String> messages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                messages.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
    }

    //Call this method from failure endpoint to get random failure message
    public static String getRandomFailureMessage(List<String> sentences) {
        if (sentences.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(sentences.size());
        return sentences.get(randomIndex);
    }

    public void deleteTask(String taskId){
        taskRepository.deleteById(taskId);
    }
}
