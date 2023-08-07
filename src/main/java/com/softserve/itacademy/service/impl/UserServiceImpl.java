package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exceptions.DuplicateEmailException;
import com.softserve.itacademy.exceptions.UserNotFoundException;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User readById(long id) {
        return userRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException("There is no user with this id."));
    }

    @Override
    public User update(User user) {
        User userToUpdate = userRepository.findById(user.getId()).orElse(null);
        if (userToUpdate != null) {
            if(user.getFirstName() != null) userToUpdate.setFirstName(user.getFirstName());
            if(user.getLastName() != null) userToUpdate.setLastName(user.getLastName());
            if(user.getFirstName() != null) {
                if (findByEmail(user.getEmail()) != null ) {
                    userToUpdate.setEmail(user.getEmail());
                } else {
                    throw new DuplicateEmailException(
                            "User with email " + user.getEmail() + "exists already.");
                }
            }
            if(user.getPassword() != null) userToUpdate.setPassword(user.getPassword());
            if(user.getRole() != null) userToUpdate.setRole(user.getRole());
            userToUpdate.setOtherTodos(user.getOtherTodos());
            userRepository.save(userToUpdate);
        }
        return userToUpdate;
    }

    @Override
    public void delete(long id) {
        User userToUpdate = userRepository.findById(id).orElseThrow(
          () -> new UserNotFoundException("There is no user with this id."));
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
