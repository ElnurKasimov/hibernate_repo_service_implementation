package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exceptions.*;
import com.softserve.itacademy.model.Task;
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
        // принимается за основу такая логика:
        // если передается todo с полем null, то считается что это поле остается в том состоянии, в каком оно сохранено в базе
        // title уникальный по всей базе
        ToDo todoToUpdate = toDoRepository.findById(todo.getId()).orElse(null);
        if (todoToUpdate != null) {
            if (todo.getTitle() != null) {
                boolean isTitlePresent = getAll().stream()
                        .map(ToDo::getTitle)
                        .anyMatch(title -> title.equals(todo.getTitle()));
                if (isTitlePresent) {
                    throw new DuplicateTitleException(
                            "Title " + todo.getTitle() + " is already present");
                }
                todoToUpdate.setTitle(todo.getTitle());
            }
            if (todo.getCreatedAt() != null) todoToUpdate.setCreatedAt(todo.getCreatedAt());
            if (todo.getOwner() != null) todoToUpdate.setOwner(todo.getOwner());
            if (todo.getTasks() != null) {
                List<Task> listFromDB = todoToUpdate.getTasks();
                List<Task> listToUpdate = todo.getTasks();
                for (Task task : listToUpdate) {
                    if (!listFromDB.contains(task)) listFromDB.add(task);
                }
                listFromDB.removeIf(task -> !listToUpdate.contains(task));
                todoToUpdate.setTasks(listFromDB);
            }
            if (todo.getCollaborators() != null) {
                List<User> listFromDB = todoToUpdate.getCollaborators();
                List<User> listToUpdate = todo.getCollaborators();
                for (User user : listToUpdate) {
                    if (!listFromDB.contains(user)) listFromDB.add(user);
                }
                listFromDB.removeIf(user -> !listToUpdate.contains(user));
                todoToUpdate.setCollaborators(listFromDB);
            }
            toDoRepository.save(todoToUpdate);
        } else {
            throw new NullToDoException("todo for update shouldn't be null.");
        }
        return todoToUpdate;
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
        boolean isUserIdPresent = getAll().stream()
                .map(ToDo::getId)
                .anyMatch(id->id == userId);
        if( ! isUserIdPresent)  throw new UserNotFoundException(
                "There is no user with id " + userId);
        return toDoRepository.findByUserId(userId);
    }
}
