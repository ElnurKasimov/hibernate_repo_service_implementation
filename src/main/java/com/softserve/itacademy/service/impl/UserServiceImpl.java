package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exceptions.*;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        // данная реализация подразумевает , что у параметра user присутствует  id
        User userToSave = new User();
        if (user != null) {
            if (readById(user.getId()) == null) {userToSave.setId(user.getId());}
            else {throw new DuplicateIdException( "User with id " + user.getId() + "exists already.");}
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
        // принимается за основу такая логика:
        // если передается юзер с полем null, то считается что это поле остается в том состоянии, в каком оно сохранено в базе
        // email  уникальное и не null
        User userToUpdate = userRepository.findById(user.getId()).orElse(null);
        if (userToUpdate != null) {
            if (user.getFirstName() != null) {userToUpdate.setFirstName(user.getFirstName());}
            if (user.getLastName() != null) {userToUpdate.setLastName(user.getLastName());}
            if(user.getEmail() != null) {
                if (getByEmail(user.getEmail()) == null) {
                    userToUpdate.setEmail(user.getEmail());
                } else {
                    throw new DuplicateEmailException(
                            "User with email " + user.getEmail() + "exists already.");
                }
            } else {throw new NullFieldException("email shouldn't be null.");}
            if (user.getPassword() != null) {userToUpdate.setPassword(user.getPassword());}
            if (user.getRole() != null) {userToUpdate.setRole(user.getRole());}
            if (user.getMyTodos() != null) {
                List<ToDo> listFromDB = userToUpdate.getMyTodos();
                List<ToDo> listToUpdate = user.getMyTodos();
                for (ToDo todo : listToUpdate) {
                    if(! listFromDB.contains(todo)) listFromDB.add(todo);
                }
                listFromDB.removeIf(todo -> !listToUpdate.contains(todo));
                userToUpdate.setMyTodos(listFromDB);
            }
            if (user.getOtherTodos() != null) {
                List<ToDo> listFromDB = userToUpdate.getOtherTodos();
                List<ToDo> listToUpdate = user.getOtherTodos();
                for (ToDo todo : listToUpdate) {
                    if(! listFromDB.contains(todo)) listFromDB.add(todo);
                }
                listFromDB.removeIf(todo -> !listToUpdate.contains(todo));
                userToUpdate.setOtherTodos(listFromDB);
            }
            userRepository.save(userToUpdate);
        } else {throw new NullUserException("user for update shouldn't be null.");}
        return userToUpdate;
    }

    @Override
    public void delete(long id) {
        User userToDelete = userRepository.findById(id).orElseThrow(
          () -> new UserNotFoundException("There is no user with this id."));
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getByEmail(String email) {
        String existingEmail = getAll().stream()
                .map(User::getEmail)
                .findAny().orElseThrow(() -> {throw new UserNotFoundException(
                        "There is no user with email " + email); } );
        return userRepository.findByEmail(existingEmail);
    }
}
