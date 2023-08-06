package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//TODO
// implements methods for retrieving State by Name
// and all states sorted by name
@Repository
public interface StateRepository extends JpaRepository<State, Long> {

}
