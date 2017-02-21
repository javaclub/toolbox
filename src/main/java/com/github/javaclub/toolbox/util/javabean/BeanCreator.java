package com.github.javaclub.toolbox.util.javabean;

import com.github.javaclub.toolbox.core.JRuntimeException;


/**
 * 用于创建bean对象。
 *
 */
public abstract class BeanCreator {
	
	@SuppressWarnings({ "rawtypes" })
	public static Object newBeanInstance(Class objectClass){
		try {
			return objectClass.newInstance() ;
		} catch (InstantiationException e) {
			throw new JRuntimeException("cann't init bean instance:" + objectClass, e) ;
		} catch (IllegalAccessException e) {
			throw new JRuntimeException("cann't init bean instance:" + objectClass, e) ;
		}
	}
	
	public static Object newBeanInstance(String className){
		try {
			return Class.forName(className).newInstance() ;
		} catch (InstantiationException e) {
			throw new JRuntimeException("cann't init bean instance:" + className, e) ;
		} catch (IllegalAccessException e) {
			throw new JRuntimeException("cann't init bean instance:" + className, e) ;
		} catch (ClassNotFoundException e) {
			throw new JRuntimeException("cann't init bean instance:" + className, e) ;
		}
	}

}
