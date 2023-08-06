package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.repository.ToDoRepository;
import com.softserve.itacademy.service.ToDoService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ToDoServiceImpl implements ToDoService {


    @Override
    public ToDo create(ToDo todo) {
        return null;
    }

    @Override
    public ToDo readById(long id) {
        return null;
    }

    @Override
    public ToDo update(ToDo todo) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public List<ToDo> getAll() {
        return null;
    }

    @Override
    public List<ToDo> getByUserId(long userId) {
        return null;
    }
}
