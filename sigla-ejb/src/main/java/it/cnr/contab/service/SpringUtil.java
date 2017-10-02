package it.cnr.contab.service;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class SpringUtil {
	private static BeanFactory beanFactory;

	static {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
				new String[] {"it/cnr/contab/service/sigla-service-context.xml"});
		beanFactory =  (BeanFactory) appContext;
	}

	public static <T extends Object> T getBean(String beanName, Class<T> clazz){
		return (T) beanFactory.getBean(beanName, clazz);
	}

	public static <T extends Object> T getBean(Class<T> clazz){
		return (T) beanFactory.getBean(clazz);
	}
	public static boolean containsBean(String name) {
		return beanFactory.containsBean(name);
	}

}