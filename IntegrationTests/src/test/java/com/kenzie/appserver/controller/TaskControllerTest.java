package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.TaskCreateRequest;
import com.kenzie.appserver.controller.model.TaskResponse;
import com.kenzie.appserver.controller.model.TaskUpdateRequest;
import com.kenzie.appserver.service.TaskService;
import com.kenzie.appserver.service.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@IntegrationTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addTask_ValidRequest_ShouldReturnCreated() {
        // Given
        TaskCreateRequest request = new TaskCreateRequest();
        request.setUserId("user123");
        request.setTaskName("Task 1");
        request.setTaskDescription("Description 1");

        Task createdTask = new Task("user123", UUID.randomUUID().toString(), "Task 1", "Description 1", "In Progress", "", "2023-07-11");

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        when(taskService.addNewTask(taskCaptor.capture())).thenReturn(createdTask);

        // When
        ResponseEntity<TaskResponse> response = taskController.addTask(request);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        String expectedLocation = "/tasks/" + taskCaptor.getValue().getTaskId();
        assertEquals(expectedLocation, response.getHeaders().getLocation().getPath());

        verify(taskService, times(1)).addNewTask(any(Task.class));
    }

    @Test
    void updateTask_ExistingTask_ShouldReturnOk() {
        // Given
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTaskID("task123");
        request.setStatus("Completed");
        request.setFailureReason("Failed");

        Task existingTask = new Task("user123", "task123", "Task 1", "Description 1", "In Progress", "", "2023-07-11");

        when(taskService.findById("task123")).thenReturn(existingTask);

        // When
        ResponseEntity<TaskResponse> response = taskController.updateTask(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        TaskResponse responseBody = response.getBody();
        assertEquals(existingTask.getUserId(), responseBody.getUserId());
        assertEquals(existingTask.getTaskId(), responseBody.getTaskId());
        assertEquals(existingTask.getTaskName(), responseBody.getTaskName());
        assertEquals(existingTask.getTaskDescription(), responseBody.getTaskDescription());
        assertEquals(request.getStatus(), responseBody.getStatus());
        assertEquals(request.getFailureReason(), responseBody.getFailureReason());

        verify(taskService, times(1)).findById("task123");
        verify(taskService, times(1)).updateTask(any(Task.class));
    }

    @Test
    void updateTask_NonExistingTask_ShouldReturnNotFound() {
        // Given
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTaskID("task123");
        request.setStatus("Completed");
        request.setFailureReason("Failed");

        when(taskService.findById("task123")).thenReturn(null);

        // When
        ResponseEntity<TaskResponse> response = taskController.updateTask(request);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(taskService, times(1)).findById("task123");
        verify(taskService, times(0)).updateTask(any(Task.class));
    }

    @Test
    void getAllTaskForUser_TasksExist_ShouldReturnTasks() {
        // Given
        String userId = "user123";

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("user123", "task1", "Task 1", "Description 1", "In Progress", "", "2023-07-11"));
        tasks.add(new Task("user123", "task2", "Task 2", "Description 2", "Completed", "", "2023-07-12"));

        when(taskService.findTasksByUserId(userId)).thenReturn(tasks);

        // When
        ResponseEntity<List<TaskResponse>> response = taskController.getAllTaskForUser(userId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<TaskResponse> responseBody = response.getBody();
        assertEquals(2, responseBody.size());

        verify(taskService, times(1)).findTasksByUserId(userId);
    }

    @Test
    void getAllTaskForUser_NoTasksExist_ShouldReturnNoContent() {
        // Given
        String userId = "user123";

        when(taskService.findTasksByUserId(userId)).thenReturn(null);

        // When
        ResponseEntity<List<TaskResponse>> response = taskController.getAllTaskForUser(userId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(taskService, times(1)).findTasksByUserId(userId);
    }

    @Test
    void getTask_ExistingTask_ShouldReturnTask() {
        // Given
        String taskId = "task123";

        Task existingTask = new Task("user123", "task123", "Task 1", "Description 1", "In Progress", "", "2023-07-11");

        // When
        when(taskService.findById(taskId)).thenReturn(existingTask);

        ResponseEntity<TaskResponse> response = taskController.getTask(taskId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        TaskResponse responseBody = response.getBody();
        assertEquals(existingTask.getUserId(), responseBody.getUserId());
        assertEquals(existingTask.getTaskId(), responseBody.getTaskId());
        assertEquals(existingTask.getTaskName(), responseBody.getTaskName());
        assertEquals(existingTask.getTaskDescription(), responseBody.getTaskDescription());
        assertEquals(existingTask.getStatus(), responseBody.getStatus());
        assertEquals(existingTask.getFailureReason(), responseBody.getFailureReason());

        verify(taskService, times(1)).findById(taskId);
    }

    @Test
    void getTask_NonExistingTask_ShouldReturnNotFound() {
        // Given
        String taskId = "task123";

        when(taskService.findById(taskId)).thenReturn(null);

        // When
        ResponseEntity<TaskResponse> response = taskController.getTask(taskId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(taskService, times(1)).findById(taskId);
    }
}
