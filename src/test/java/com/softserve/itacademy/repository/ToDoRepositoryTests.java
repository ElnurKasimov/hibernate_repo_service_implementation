package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class ToDoRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ToDoRepository toDoRepository;

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void createUserTest(){
        Role role = roleRepository.getOne(2L);
        User validUser  = new User();
        validUser.setEmail("valid2@cv.edu.ua");
        validUser.setFirstName("Valid-Name");
        validUser.setLastName("Valid-Name");
        validUser.setPassword("qwQW12!@");
        validUser.setRole(role);

        validUser = userRepository.save(validUser);

        ToDo toDo = new ToDo();
        toDo.setTitle("Other");
        toDo.setOwner(validUser);
        toDo = toDoRepository.save(toDo);

        LocalDate localDate = toDo.getCreatedAt().toLocalDate();
        LocalDate today = LocalDate.now();
        assertEquals(localDate, today);
    }

    @Test
    public void exceptionWhenCreateNonUniqueToDoTest(){
        User validUser = userRepository.getOne(4L);

        ToDo toDo = new ToDo();
        toDo.setTitle("ValidOther");
        toDo.setOwner(validUser);
        toDo = toDoRepository.save(toDo);

        LocalDate localDate = toDo.getCreatedAt().toLocalDate();
        LocalDate today = LocalDate.now();
        assertEquals(localDate, today);

        toDo = new ToDo();
        toDo.setTitle("ValidOther");
        toDo.setOwner(validUser);

        ToDo finalToDo = toDo;
        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, ()->{
            toDoRepository.save(finalToDo);
        });

    }
}
