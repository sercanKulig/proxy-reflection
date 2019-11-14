package com.dynamic.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DynamicProxy implements InvocationHandler {
    /**
     * Proxy pattern'i methodlar çağırıldığında methodlar aktarır, istenildiğinde metotların davranışlarını istenilene göre değiştirmene yarar
     * Tek bir klas ve method ile istenilen interface ve methodu istenilern sayıda ve istenilen yerde çalıştırmana olanak verir.
     *  Hibernate - lazy loading entities, Spring - AOP, Lambda - java8 proxyden yararlanır.
     *  Özünde reflectionla method ve classlara erişerek üzerinde aksiyon alınmasını sağlar.
     * */
    private static Logger LOGGER = LoggerFactory.getLogger(
            DynamicProxy.class);

    private final Map<String, Method> methods = new HashMap<>();

    private Object target;

    DynamicProxy(Object target) {
        this.target = target;
        for(Method method: target.getClass().getDeclaredMethods()) {
            this.methods.put(method.getName(), method);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        long start = System.nanoTime();
        Object result = methods.get(method.getName()).invoke(target, args);
        long elapsed = System.nanoTime() - start;

        LOGGER.info("Executing {} finished in {} ns", method.getName(),
                elapsed);

        return result;
    }
}