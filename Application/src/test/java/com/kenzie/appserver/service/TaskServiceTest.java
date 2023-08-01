package com.kenzie.appserver.service;
import com.kenzie.appserver.repositories.TaskRepository;
import com.kenzie.appserver.repositories.model.TaskRecord;
import com.kenzie.appserver.service.model.Task;
import jdk.internal.loader.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void findTasksByOwner_ShouldReturnListOfTasks() {
        // Given

        String ownerId = "userId1";
        List<Task> taskRecordList = new ArrayList<>();
        Task taskRecord1 = new Task("userId1", "1", "Task 1", "Task description 1", "Completed", "Failure reason 1", "Date1");
        Task taskRecord2 = new Task("userId1", "2", "Task 2", "Task description 2", "Failed", "Failure reason 2", "Date2");
        Task taskRecord3 = new Task("otherUser", "3", "Task 3", "Task description 3", "Failed", "Failure reason 3", "Date3");
        taskRecordList.add(taskRecord1);
        taskRecordList.add(taskRecord2);
        taskRecordList.add(taskRecord3);
        when(taskRepository.findTasksByUserId(ownerId)).thenReturn(taskRecordList);

        // When
        List<Task> tasks = taskService.findTasksByUserId(ownerId);

        // Then
        assertEquals(2, tasks.size());

        assertEquals("userId1", tasks.get(0).getUserId());
        assertEquals("1", tasks.get(0).getTaskId());
        assertEquals("Task 1", tasks.get(0).getTaskName());
        assertEquals("Task description 1", tasks.get(0).getTaskDescription());
        assertEquals("Completed", tasks.get(0).getStatus());
        assertEquals("Failure reason 1", tasks.get(0).getFailureReason());

        assertEquals("userId1", tasks.get(1).getUserId());
        assertEquals("2", tasks.get(1).getTaskId());
        assertEquals("Task 2", tasks.get(1).getTaskName());
        assertEquals("Task description 2", tasks.get(1).getTaskDescription());
        assertEquals("Failed", tasks.get(1).getStatus());
        assertEquals("Failure reason 2", tasks.get(1).getFailureReason());

        // Verify that the taskRepository.findByOwner method was called with the correct argument
        verify(taskRepository).findByUserId(ownerId);
    }

    @Test
    void findById() {
        // GIVEN
        String id = randomUUID().toString();

        TaskRecord record = new TaskRecord();
        record.setUserId(id);
        record.setTaskName("taskname");

        // WHEN
        when(taskRepository.findById(id)).thenReturn(Optional.of(record));
        Task task = taskService.findById(id);

        // THEN
        Assertions.assertNotNull(task, "The object is returned");
        Assertions.assertEquals(record.getUserId(), task.getUserId(), "The id matches");
        Assertions.assertEquals(record.getTaskName(), task.getTaskName(), "The name matches");
    }

    @Test
    void findById_invalid() {
        // GIVEN
        String id = randomUUID().toString();

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        // WHEN
        Task task = taskService.findById(id);

        // THEN
        Assertions.assertNull(task, "The example is null when not found");
    }

    @Test
    void updateTask_ShouldUpdateExistingTask() {
        // Given
        String taskId = "taskId1";
        Task existingTask = new Task("1", taskId, "Task 1", "Task description 1", "Completed", "Failure reason 1", "Date");

        Task updatedTask = new Task("1", taskId, "Updated Task", "Updated description", "Failed", "Updated failure reason", "Date");

        TaskRecord existingTaskRecord = new TaskRecord();
        existingTaskRecord.setUserId("1");
        existingTaskRecord.setTaskId(taskId);
        existingTaskRecord.setTaskName("Task 1");
        existingTaskRecord.setTaskDescription("Task description 1");
        existingTaskRecord.setStatus("task status 1");
        existingTaskRecord.setFailureReason("Failure reason 1");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTaskRecord));
        when(taskRepository.save(existingTaskRecord)).thenReturn(existingTaskRecord);

        // When
        taskService.updateTask(updatedTask);

        // Then
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingTaskRecord);

        assertEquals(updatedTask.getTaskName(), existingTaskRecord.getTaskName());
        assertEquals(updatedTask.getTaskDescription(), existingTaskRecord.getTaskDescription());
        assertEquals(updatedTask.getStatus(), existingTaskRecord.getStatus());
        assertEquals(updatedTask.getFailureReason(), existingTaskRecord.getFailureReason());
    }
    @Test
    void testReadFromCSV() {
        String filePath = "FailureMessages.csv";
        List<String> expectedData = new ArrayList<>();
        expectedData.add("Failure Message 1");
        expectedData.add("Failure Message 2");
        expectedData.add("Failure Message 3");

        // Load the resource file using the ClassLoader
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);

        // Call the readFromCSV method with the InputStream
        List<String> actualData = taskService.readFromCSV(inputStream);

        Assertions.assertEquals(expectedData.size(), actualData.size(), "The number of lines matches");

        for (int i = 0; i < expectedData.size(); i++) {
            Assertions.assertEquals(expectedData.get(i), actualData.get(i), "The line content matches");
        }
    }
    @Test
    void testGetRandomFailureMessage() {
        List<String> sentences = new ArrayList<>();
        sentences.add("Failure Message 1");
        sentences.add("Failure Message 2");
        sentences.add("Failure Message 3");

        String randomMessage = taskService.getRandomFailureMessage(sentences);

        Assertions.assertNotNull(randomMessage, "A random message is returned");
        Assertions.assertTrue(sentences.contains(randomMessage), "The random message is one of the sentences");
    }
}
