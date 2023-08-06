package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// implement method for retrieving all Todos by userId
@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
}
