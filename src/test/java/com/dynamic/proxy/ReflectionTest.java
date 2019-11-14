package com.dynamic.proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionTest {

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person("a","b");
    }

    @Test
    void runner() {
        dump();
        reflectionTryout();
    }

    @Test
    void reflectionTryout() {
        String expect =
                "Person{" +
                        "lastName='" + person.getLastName() + '\'' +
                        ", firstName='" + person.getFirstName() + '\'' +
                        '}';
        assertEquals(expect, person.toString());
    }



    @Test
    void dump() {
        String expect =
        "\n"+
        "\t{\n"+
        "\tlastName="+person.getLastName()+"\n"+
        "\tfirstName="+person.getFirstName()+"\n"+
        "\t}\n"+
        "";
        assertEquals(expect, Reflection.dump(person, 0));
    }
}