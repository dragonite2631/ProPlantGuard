package com.proptit.ProPlantGuard.ulti;

import ch.qos.logback.core.spi.ContextAware;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ContextUlti implements ApplicationContextAware {
    public static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ContextUlti.applicationContext = applicationContext;
    }
    public static ApplicationContext getApplicationContext (){
        return applicationContext;
    }
}
