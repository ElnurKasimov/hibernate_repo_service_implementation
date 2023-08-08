package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exceptions.StateNotFoundException;
import com.softserve.itacademy.model.State;
import com.softserve.itacademy.repository.StateRepository;
import com.softserve.itacademy.service.StateService;
import com.softserve.itacademy.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Supplier;

@Validated
@Service
public class StateServiceImpl implements StateService {

    private StateRepository stateRepository;
    private TaskService taskService;
    private State defaultState;
    private Supplier<StateNotFoundException> notFoundExceptionSupplier
            = () -> new StateNotFoundException("State wasn't found");

    {
        defaultState = new State();
        defaultState.setName("default");
    }

    public StateServiceImpl(StateRepository stateRepository, TaskService taskService) {
        this.stateRepository = stateRepository;
        this.taskService = taskService;
    }

    @Valid
    @Override
    public State create(State state) {
        return stateRepository.save(state);
    }

    @Override
    public State readById(long id) {

        return stateRepository.findById(id).orElseThrow(notFoundExceptionSupplier);
    }

    @Valid
    @Override
    public State update(State state) {
        stateRepository.findById(state.getId()).orElseThrow(notFoundExceptionSupplier);
        return stateRepository.save(state);
    }

    @Override
    public void delete(long id) {
        stateRepository.findById(id).orElseThrow(notFoundExceptionSupplier);
        taskService.getAll().stream()
                .filter(task -> task.getState().getId() == id)
                .forEach(task -> task.setState(defaultState));
        stateRepository.deleteById(id);
    }

    @Override
    public List<State> getAll() {
        return stateRepository.findAll();
    }

    @Override
    public State getByName(String name) {
        return stateRepository.findByName(name).orElseThrow(notFoundExceptionSupplier);
    }

    @Override
    public List<State> getSortAsc() {
        return stateRepository.findAllByOrderByNameAsc();
    }
}
