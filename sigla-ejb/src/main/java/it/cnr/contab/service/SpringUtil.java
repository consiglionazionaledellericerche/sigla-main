/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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