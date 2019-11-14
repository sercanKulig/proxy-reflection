package com.dynamic.proxy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonAndEmployeeReflectionUnitTest extends TestHelper {

    private static final String LAST_NAME_FIELD = "lastName";
    private static final String FIRST_NAME_FIELD = "firstName";
    private static final String EMPLOYEE_ID_FIELD = "employeeId";

    @Test
    void givenPersonClass_whenGetDeclaredFields_thenTwoFields() {
        Field[] allFields = Person.class.getDeclaredFields();

        assertEquals(2, allFields.length);

        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(LAST_NAME_FIELD)
                        && field.getType().equals(String.class))
        );
        assertTrue(Arrays.stream(allFields).anyMatch(field ->
                field.getName().equals(FIRST_NAME_FIELD)
                        && field.getType().equals(String.class))
        );
    }

    @Test
    void givenEmployeeClass_whenGetDeclaredFieldsOnBothClasses_thenThreeFields() {
        List<Field> personFields = Arrays.stream(Employee.class.getSuperclass().getDeclaredFields())
                .filter(f -> Modifier.isPublic(f.getModifiers()) || Modifier.isProtected(f.getModifiers())
                        || Modifier.isPrivate(f.getModifiers()))
                .collect(toList());
        Field[] employeeFields = Employee.class.getDeclaredFields();
        Field[] allFields = new Field[employeeFields.length + personFields.size()];
        Arrays.setAll(allFields, i ->
                (i < personFields.size() ? personFields.get(i) : employeeFields[i - personFields.size()]));

        assertEquals(3, allFields.length);

        Field lastNameField = allFields[0];
        assertEquals(LAST_NAME_FIELD, lastNameField.getName());
        assertEquals(String.class, lastNameField.getType());
        assertTrue(Modifier.isPrivate(lastNameField.getModifiers()));

        Field firstNameField = allFields[1];
        assertEquals(FIRST_NAME_FIELD, firstNameField.getName());
        assertEquals(String.class, firstNameField.getType());

        Field employeeIdField = allFields[2];
        assertEquals(EMPLOYEE_ID_FIELD, employeeIdField.getName());
        assertEquals(int.class, employeeIdField.getType());
    }

    @Test
    void givenMonthEmployeeClass_whenGetAllFields_thenThreeFields() {
        List<Field> allFields = getAllFields(Employee.class);

        assertEquals(3, allFields.size());

        assertTrue(allFields.stream().anyMatch(field ->
                field.getName().equals(LAST_NAME_FIELD)
                        && field.getType().equals(String.class))
        );
        assertTrue(allFields.stream().anyMatch(field ->
                field.getName().equals(EMPLOYEE_ID_FIELD)
                        && field.getType().equals(int.class))
        );
        assertTrue(allFields.stream().anyMatch(field ->
                field.getName().equals(FIRST_NAME_FIELD)
                        && field.getType().equals(String.class))
        );
    }

    @Test
    void whenGetConstructorParams_thenOk() throws NoSuchMethodException {
        List<Parameter> parameters = Arrays.stream(Person.class.getConstructor(String.class, String.class).getParameters()).filter(Parameter::isNamePresent).collect(toList());
        assertTrue(parameters.stream().anyMatch(m -> m.getName().contains("lastName") || m.getName().contains("firstName")));
    }
}
