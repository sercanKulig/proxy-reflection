package com.dynamic.proxy;

import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {
    @Override
    public void soutName(String name) {
        System.out.println(name);
    }
}
