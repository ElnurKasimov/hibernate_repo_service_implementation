package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exceptions.DuplicateIdException;
import com.softserve.itacademy.exceptions.NullFieldException;
import com.softserve.itacademy.exceptions.NullUserException;
import com.softserve.itacademy.exceptions.UserNotFoundException;
import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.RoleRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@EnableTransactionManagement
class UserServiceImplIntegrationTest {
    private UserService userServiceImpl;
    private RoleService roleServiceImpl;

    @Autowired
    UserServiceImplIntegrationTest(UserService userServiceImpl, RoleService roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
    }

    @Test
    @DisplayName("Test that when parameter userServiceImpl.create() null - exception throw")
    public void constraintViolationOnNullParameterInUserCreate(){
        Assertions.assertThrows(NullUserException.class, () -> userServiceImpl.create(null));
    }

    @Test
    @DisplayName("Test that userServiceImpl.create() works properly")
    public void TestThatCreateUserWorksProperly() {
        Role newRole = new Role();
        newRole.setName("Test");
        roleServiceImpl.create(newRole);
        User validUser  = new User();
        validUser.setEmail("valid@cv.edu.ua");
        validUser.setFirstName("Valid-Name");
        validUser.setLastName("Valid-Name");
        validUser.setPassword("qwQW12!@");
        validUser.setRole(newRole);
        userServiceImpl.create(validUser);
        User savedUser = userServiceImpl.getByEmail(validUser.getEmail());
        System.out.println(savedUser.getId());

        assertNotNull(savedUser);
        assertThat(savedUser.getFirstName()).isEqualTo("Valid-Name");
        assertThat(savedUser.getEmail()).isEqualTo("valid@cv.edu.ua");
    }

    @Test
    @DisplayName("Test that when userServiceImpl.create() checks duplicateID")
    public void constraintViolationOnDuplicateIdInUserCreate() {
        User existed = new User();
        User created = new User();
        existed.setId(50);
        userServiceImpl.create(existed);

        created.setId(50);
        Assertions.assertThrows(DuplicateIdException.class, () -> userServiceImpl.create(created));
    }

    @ParameterizedTest(name = "#{index} - Test that arguments for user {0},{1}")
    @MethodSource("provideInvalidUser")
    @DisplayName("Test checking different invalid user field  in userServiceImpl.create(user)")
    void constraintViolationInvalidEmail(String[] strings, Role role) {
        User inValidUser  = new User();
        inValidUser.setEmail(strings[0]);
        inValidUser.setFirstName(strings[1]);
        inValidUser.setLastName(strings[2]);
        inValidUser.setPassword(strings[3]);
        inValidUser.setRole(role);
        Assertions.assertThrows(NullFieldException.class, () -> userServiceImpl.create(inValidUser));
    }

    private static Stream<Arguments> provideInvalidUser(){
        return Stream.of(
                Arguments.of(new String[]{null,"Valid-Name","Valid-Name","qwQW12!@"}, new Role()),
                Arguments.of(new String[]{"valid@cv.edu.ua",null,"Valid-Name","qwQW12!@"}, new Role()),
                Arguments.of(new String[]{"valid@cv.edu.ua","Valid-Name",null,"qwQW12!@"}, new Role()),
                Arguments.of(new String[]{"valid@cv.edu.ua","Valid-Name","Valid-Name",null}, new Role()),
                Arguments.of(new String[]{"valid@cv.edu.ua","Valid-Name","Valid-Name","qwQW12!@"}, null)
        );
    }



}