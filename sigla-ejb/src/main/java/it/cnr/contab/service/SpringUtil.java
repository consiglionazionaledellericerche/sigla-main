package it.cnr.contab.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware{
	private static ApplicationContext CONTEXT;

	public static <T extends Object> T getBean(String beanName, Class<T> clazz){
		return (T) CONTEXT.getBean(beanName, clazz);
	}

	public static <T extends Object> T getBean(Class<T> clazz){
		return (T) CONTEXT.getBean(clazz);
	}
	public static boolean containsBean(String name) {
		return CONTEXT.containsBean(name);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		CONTEXT = applicationContext;
	}
}