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
	
	/**
	 * Metodo di utilità per caricare il contesto di Spring 
	 */
	public static void init(){
	}
	
	public static <T extends Object> T getBean(String beanName, Class<T> clazz){
		return (T) beanFactory.getBean(beanName, clazz);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T getBean(String beanName, Class<T> clazz, Object...constructorArgs){
		return (T) beanFactory.getBean(beanName, constructorArgs);
	}
	
}
