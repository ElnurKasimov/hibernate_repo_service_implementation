package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exceptions.*;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.ToDoRepository;
import com.softserve.itacademy.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ToDoServiceImpl implements ToDoService {
    private ToDoRepository toDoRepository;

    @Autowired
    public ToDoServiceImpl(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }


    @Override
    public ToDo create(ToDo todo) {
        // данная реализация подразумевает , что у параметра todo присутствует  id
        ToDo toDoToSave = new ToDo();
        if (todo != null) {
            if (readById(todo.getId()) == null) {toDoToSave.setId(todo.getId());}
                   else {throw new DuplicateIdException( "toDo with id " + todo.getId() + "exists already.");}
            if (todo.getTitle() != null) {toDoToSave.setTitle(todo.getTitle());}
                   else {throw new NullFieldException("title shouldn't be null.");}
            if (todo.getCreatedAt() != null) {toDoToSave.setCreatedAt(todo.getCreatedAt());}
                   else {throw new NullFieldException("createdAt shouldn't be null.");}
            if (todo.getOwner() != null) {toDoToSave.setOwner(todo.getOwner());}
                 else {throw new NullFieldException("owner shouldn't be null.");}
            if (todo.getTasks() == null) {toDoToSave.setTasks(new ArrayList<>());}
                  else {toDoToSave.setTasks(todo.getTasks());}
            if (todo.getCollaborators() == null) {toDoToSave.setCollaborators(new ArrayList<>());}
                 else {toDoToSave.setCollaborators(todo.getCollaborators());}
            toDoRepository.save(toDoToSave);
        } else {throw new NullToDoException("todo shouldn't be null.");}
        return toDoToSave;
    }

    @Override
    public ToDo readById(long id) {
        return toDoRepository.findById(id).orElseThrow(
                () -> new ToDoNotFoundException("There is no user with this id."));
    }

    @Override
    public ToDo update(ToDo todo) {

        return null;
    }

    @Override
    public void delete(long id) {
        ToDo toDoToDelete = readById(id);
        toDoRepository.deleteById(id);
    }

    @Override
    public List<ToDo> getAll() {return toDoRepository.findAll();
    }

    @Override
    public List<ToDo> getByUserId(long userId) {
        boolean isIdExist = getAll().stream()
                .map(ToDo::getId)
                .anyMatch(id->id == userId);
        if( ! isIdExist)  throw new UserNotFoundException(
                "There is no user with id " + userId);
        return toDoRepository.findByUserId(userId);
    }
}
