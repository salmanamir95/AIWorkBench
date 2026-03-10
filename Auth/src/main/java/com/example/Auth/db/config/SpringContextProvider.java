package com.example.Auth.db.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static <T> T getBean(final Class<T> beanType) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBeanProvider(beanType).getIfAvailable();
    }
}
