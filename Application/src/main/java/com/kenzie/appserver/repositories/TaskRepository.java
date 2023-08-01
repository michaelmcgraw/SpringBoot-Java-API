package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.TaskRecord;
import com.kenzie.appserver.service.model.Task;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@EnableScan
public interface TaskRepository extends CrudRepository<TaskRecord, String> {

    default List<Task> findTasksByUserId(String id) {
        List<TaskRecord> taskRecords = findByUserId(id);
        System.out.println("this is what you are looking for" + Arrays.toString(taskRecords.toArray()));
        return taskRecords.stream()
                .map(taskRecord -> new Task(
                        taskRecord.getUserId(),
                        taskRecord.getTaskId(),
                        taskRecord.getTaskName(),
                        taskRecord.getTaskDescription(),
                        taskRecord.getStatus(),
                        taskRecord.getFailureReason(),
                        taskRecord.getDate()
                ))
                .collect(Collectors.toList());
    }

    List<TaskRecord> findByUserId(String id);

}
