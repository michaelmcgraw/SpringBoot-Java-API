package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.TaskCreateRequest;
import com.kenzie.appserver.controller.model.TaskResponse;
import com.kenzie.appserver.controller.model.TaskUpdateRequest;
import com.kenzie.appserver.service.TaskService;
import com.kenzie.appserver.service.model.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PostMapping
    public ResponseEntity<TaskResponse> addTask(@RequestBody TaskCreateRequest taskCreateRequest) {
        System.out.println(taskCreateRequest.toString());
        Task task = new Task(
                taskCreateRequest.getUserId(),
                UUID.randomUUID().toString(),
                taskCreateRequest.getTaskName(),
                taskCreateRequest.getTaskDescription(),
                "In Progress",
                "",
                Timestamp.valueOf(LocalDateTime.now()).toString()
        );
        taskService.addNewTask(task);
        TaskResponse taskResponse = createTaskResponse(task);
        return ResponseEntity.created(URI.create("/tasks/" + taskResponse.getTaskId())).body(taskResponse);
    }

    @PutMapping
    public ResponseEntity<TaskResponse> updateTask(@RequestBody TaskUpdateRequest taskUpdateRequest) {
        Task existingTask = taskService.findById(taskUpdateRequest.getTaskId());
        if (existingTask == null) {
            return ResponseEntity.notFound().build();
        }
        Task task = new Task(
                existingTask.getUserId(),
                existingTask.getTaskId(),
                existingTask.getTaskName(),
                existingTask.getTaskDescription(),
                taskUpdateRequest.getStatus(),
                taskUpdateRequest.getFailureReason(),
                existingTask.getDate()
        );
        taskService.updateTask(task);
        TaskResponse taskResponse = createTaskResponse(task);
        return ResponseEntity.ok(taskResponse);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTaskForUser(@RequestParam String userId) {
        List<Task> tasks = taskService.findTasksByUserId(userId);
        if (tasks == null ||  tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<TaskResponse> response = new ArrayList<>();
        for (Task task : tasks) {
            response.add(this.createTaskResponse(task));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{taskID}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable("taskID") String taskID) {
        Task task = taskService.findById(taskID);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        TaskResponse taskResponse = createTaskResponse(task);
        return ResponseEntity.ok(taskResponse);
    }



    private TaskResponse createTaskResponse(Task task) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setUserId(task.getUserId());
        taskResponse.setTaskId(task.getTaskId());
        taskResponse.setTaskName(task.getTaskName());
        taskResponse.setTaskDescription(task.getTaskDescription());
        taskResponse.setStatus(task.getStatus());
        taskResponse.setFailureReason(task.getFailureReason());
        return taskResponse;
    }
}
