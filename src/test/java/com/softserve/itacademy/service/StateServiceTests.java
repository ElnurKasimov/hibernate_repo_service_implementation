package com.softserve.itacademy.service;

import com.softserve.itacademy.model.State;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class StateServiceTests {

    StateService stateService;
    TaskService taskService;

    @Autowired
    public StateServiceTests(StateService stateService, TaskService taskService){
        this.stateService = stateService;
        this.taskService = taskService;
    }

    @Test
    public void getStateByNameTest(){
        State expected = new State();
        expected.setName("New");
        expected.setId(5L);
        State actual = stateService.getByName("New");
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void createStateTest(){
        State state = new State();
        state.setName("NotNew");
        state = stateService.create(state);
        assertNotEquals(0, state.getId());
    }

    @Test
    public void getStateById(){
        State expected = new State();
        expected.setName("New");
        expected.setId(5L);
        State actual = stateService.readById(5L);
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void updateStateTest(){
        String newName = "New 2";
        State state = stateService.readById(5L);
        state.setName(newName);
        State actual = stateService.update(state);
        State expected = new State();
        expected.setName(newName);
        expected.setId(5L);
        assertEquals(expected, actual);
    }
    
    @Test
    @Transactional
    public void getAllStateTest(){
        taskService.delete(5L);
        taskService.delete(6L);
        taskService.delete(7L);
        stateService.delete(5l);
        stateService.delete(6L);
        stateService.delete(7l);
        stateService.delete(8L);
        int expectedSize = 0;
        List<State> states = stateService.getAll();
        assertEquals(expectedSize, states.size());
    }

    @Test
    public void exceptionWhenDeleteStateWithTaskTest(){
        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, ()->{
            stateService.delete(5L);
        });
    }

    @Test
    @Transactional
    public void deleteStateWithTaskTest(){
        taskService.delete(6L);
        stateService.delete(5L);
        List<State> states = stateService.getAll();
        assertEquals(3, states.size());
    }

    @Test
    public void allStateSortedByNameTest(){
        State s1 = new State();
        s1.setName("Doing");
        s1.setId(6L);
        State s2 = new State();
        s2.setName("Done");
        s2.setId(8L);
        State s3 = new State();
        s3.setName("New");
        s3.setId(5L);
        State s4 = new State();
        s4.setName("Verify");
        s4.setId((7L));
        List<State> expected = Arrays.asList(s1,s2,s3,s4);
        List<State> actual = stateService.getSortAsc();
        assertEquals(expected, actual);
    }

}
