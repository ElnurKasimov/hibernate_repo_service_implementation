package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// TODO
// implements methods for retrieving Tasks by todo_id
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByToDoId(long todoId);
}
