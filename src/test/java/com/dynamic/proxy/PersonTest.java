package com.dynamic.proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setFirstName("firstname");
        person.setLastName("lastname");
    }

    @Test
    void getLastName() throws NoSuchMethodException {
        Method parameter = Person.class.getMethod("getLastName", null);
        assertThat(parameter.getName().contains("lastName"));
        assertEquals("lastname", person.getLastName());
    }

    @Test
    void setLastName() throws NoSuchMethodException {
        person.setLastName("lastname_set");
        List<Parameter> parameters = Arrays.asList(
                Person.class.getMethod("setLastName", String.class).getParameters());
        Optional<Parameter> parameter= parameters.stream()
                .filter(Parameter::isNamePresent)
                .findFirst();
        assertThat(parameter.get().getName()).isEqualTo("lastName");
        assertEquals("lastname_set", person.getLastName());
    }

    @Test
    void getFirstName() throws NoSuchMethodException {
        Method parameter = Person.class.getMethod("getFirstName", null);
        assertThat(parameter.getName().contains("firstName"));
        assertEquals("firstname", person.getFirstName());
    }

    @Test
    void setFirstName() throws NoSuchMethodException {
        person.setFirstName("firstname_set");
        List<Parameter> parameters = Arrays.asList(
                Person.class.getMethod("setFirstName", String.class).getParameters());
        Optional<Parameter> parameter= parameters.stream()
                .filter(Parameter::isNamePresent)
                .findFirst();
        assertThat(parameter.get().getName()).isEqualTo("firstName");
        assertEquals("firstname_set", person.getFirstName());
    }

    @Test
    void testToString() {
        String expect = "Person{" +
                "lastName='" + person.getLastName() + '\'' +
                ", firstName='" + person.getFirstName() + '\'' +
                '}';
        assertEquals(expect, person.toString());
    }

    @Test
    void tryoutResult() {
        String expect = "Person{" +
                "lastName='" + person.getLastName() + '\'' +
                ", firstName='" + person.getFirstName() + '\'' +
                '}';
        assertEquals(expect, person.tryoutResult(person.getLastName(),person.getFirstName()));
    }
}