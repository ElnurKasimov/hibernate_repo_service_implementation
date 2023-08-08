package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exceptions.DuplicateTaskException;
import com.softserve.itacademy.exceptions.TaskNotFoundException;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.repository.TaskRepository;
import com.softserve.itacademy.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

@Validated
@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    private Supplier<TaskNotFoundException> notFoundExceptionSupplier
            = () -> new TaskNotFoundException("There is no task with this id");

    @Valid
    @Override
    public Task create(Task task) {
        requireNonNull(task.getState());
        requireNonNull(task.getToDo());
        requireNonNull(task.getName());

        if (!taskRepository.existsById(task.getId())) {
            return taskRepository.save(task);
        } else {
            throw new DuplicateTaskException("Task with this id already exists");
        }
    }

    @Override
    public Task readById(long id) {
        return taskRepository.findById(id).orElseThrow(notFoundExceptionSupplier);
    }

    @Valid
    @Override
    public Task update(Task task) {
        taskRepository.findById(task.getId()).orElseThrow(notFoundExceptionSupplier);
        return taskRepository.save(task);
    }

    @Override
    public void delete(long id) {
        taskRepository.findById(id).orElseThrow(notFoundExceptionSupplier);
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getByTodoId(long todoId) {
        return taskRepository.findByToDoId(todoId);
    }
}
