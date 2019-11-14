package com.dynamic.proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {


    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setEmployeeId(1);
    }

    @Test
    void getEmployeeId() {
        assertEquals(1, employee.getEmployeeId());
    }

    @Test
    void setEmployeeId() {
        assertEquals(1, employee.getEmployeeId());
    }
}