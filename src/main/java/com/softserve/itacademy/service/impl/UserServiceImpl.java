package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exceptions.DuplicateEmailException;
import com.softserve.itacademy.exceptions.NullFieldException;
import com.softserve.itacademy.exceptions.NullUserException;
import com.softserve.itacademy.exceptions.UserNotFoundException;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        // разобраться с айди
        User userToSave = new User();
        if (user != null) {
            if (user.getFirstName() != null) {userToSave.setFirstName(user.getFirstName());}
                else {throw new NullFieldException("firstName shouldn't be null.");}
            if (user.getLastName() != null) {userToSave.setLastName(user.getLastName());}
                else {throw new NullFieldException("lastName shouldn't be null.");}
            if(user.getEmail() != null) {
                if (getByEmail(user.getEmail()) == null) {
                    userToSave.setEmail(user.getEmail());
                } else {
                    throw new DuplicateEmailException(
                            "User with email " + user.getEmail() + "exists already.");
                }
            } else {throw new NullFieldException("email shouldn't be null.");}
            if (user.getPassword() != null) {userToSave.setPassword(user.getPassword());}
                else {throw new NullFieldException("password shouldn't be null.");}
            if (user.getRole() != null) {userToSave.setRole(user.getRole());}
                else {throw new NullFieldException("role shouldn't be null.");}
            if (user.getMyTodos() == null) {
                userToSave.setMyTodos(new ArrayList<>());
            } else {userToSave.setMyTodos(user.getMyTodos());}
            if (user.getOtherTodos() == null) {
                userToSave.setOtherTodos(new ArrayList<>());
            } else {userToSave.setOtherTodos(user.getMyTodos());}
            userRepository.save(userToSave);
        } else {throw new NullUserException("user shouldn't be null.");}
        return userToSave;
    }

    @Override
    public User readById(long id) {
        return userRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException("There is no user with this id."));
    }

    @Override
    public User update(User user) {
        // разобраться с обновлением коллекций
        User userToUpdate = userRepository.findById(user.getId()).orElse(null);
        if (userToUpdate != null) {
            if (user.getFirstName() != null) {userToUpdate.setFirstName(user.getFirstName());}
            else {throw new NullFieldException("firstName shouldn't be null.");}
            if (user.getLastName() != null) {userToUpdate.setLastName(user.getLastName());}
            else {throw new NullFieldException("lastName shouldn't be null.");}
            if(user.getEmail() != null) {
                if (getByEmail(user.getEmail()) == null) {
                    userToUpdate.setEmail(user.getEmail());
                } else {
                    throw new DuplicateEmailException(
                            "User with email " + user.getEmail() + "exists already.");
                }
            } else {throw new NullFieldException("email shouldn't be null.");}
            if (user.getPassword() != null) {userToUpdate.setPassword(user.getPassword());}
            else {throw new NullFieldException("password shouldn't be null.");}
            if (user.getRole() != null) {userToUpdate.setRole(user.getRole());}
            else {throw new NullFieldException("role shouldn't be null.");}
            if (user.getMyTodos() == null) {
                userToUpdate.setMyTodos(new ArrayList<>());
            } else {userToUpdate.setMyTodos(user.getMyTodos());}
            if (user.getOtherTodos() == null) {
                userToUpdate.setOtherTodos(new ArrayList<>());
            } else {userToUpdate.setOtherTodos(user.getMyTodos());}
            userRepository.save(userToUpdate);
        } else {throw new NullUserException("user shouldn't be null.");}
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

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
