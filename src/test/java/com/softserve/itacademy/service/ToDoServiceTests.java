package com.softserve.itacademy.service;

import com.softserve.itacademy.model.ToDo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class ToDoServiceTests {

    ToDoService toDoService;

    @Autowired
    public ToDoServiceTests(ToDoService toDoService){
        this.toDoService = toDoService;
    }

    public void getAllToDosByUserIdTest(){
        int expectedSize = 4;
        List<ToDo> toDos = toDoService.getByUserId(5L);
        assertEquals(expectedSize, toDos.size());
    }

}
