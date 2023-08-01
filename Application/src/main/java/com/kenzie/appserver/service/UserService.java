package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.UserRepository;
import com.kenzie.appserver.repositories.model.UserRecord;
import com.kenzie.appserver.service.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(String id){
        User userToRetrieve = userRepository
                .findById(id)
                .map(user -> new User(user.getId(),user.getTaskList(),user.getCompletionRate()))
                .orElse(null);
        if (userToRetrieve != null){
            return userToRetrieve;
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User does not exist");
        }
    }

    public User addNewUser(User user){
        UserRecord userRecord = new UserRecord();
        userRecord.setId(user.getId());
        userRecord.setTaskList(user.getTaskList());
        userRecord.setCompletionRate(user.getCompletionRate());
        userRepository.save(userRecord);
        return user;
    }
}
