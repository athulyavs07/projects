package com.brandanswers.dashboard.orchestrator;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {

	private static ApplicationContext context;
	
	public static <T extends Object> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
	
	public static Object getBean(String className) {
        return context.getBean(className);
    }
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		 context = applicationContext;
	}

}
