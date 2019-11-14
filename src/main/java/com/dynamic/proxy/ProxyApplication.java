package com.dynamic.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

@SpringBootApplication
public class ProxyApplication {

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Reflection.runner();
        PersonServiceImpl personService = new PersonServiceImpl();
        DynamicProxy dynamicProxy = new DynamicProxy(personService);
        PersonService ps = (PersonService) Proxy.newProxyInstance(PersonService.class.getClassLoader(),
                new Class[] {PersonService.class},
                dynamicProxy);
        ps.soutName("Hey");
        SpringApplication.run(ProxyApplication.class, args);
    }

}
