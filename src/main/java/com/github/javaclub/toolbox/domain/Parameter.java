package com.github.javaclub.toolbox.domain;

import java.util.HashMap;

/**
 * Parameter 封装的一个http请求参数对象
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Parameter.java 2013-11-29 10:32:02 Exp $
 */
public class Parameter extends HashMap<String, Object> {

	private static final long serialVersionUID = 3835161422303499668L;

	public Parameter() {

	}

	public Parameter(String key, Object value) {
		this.put(key, value);
	}

	public Parameter addParameter(String key, Object value) {
		this.put(key, value);
		return this;
	}

	public Parameter addParameterIfNotNull(String key, Object value) {
		if (null != value) {
			this.put(key, value);
		}
		return this;
	}

	public Parameter rm(String key) {
		this.remove(key);
		return this;
	}
	
	public Parameter rmAll() {
		this.clear();
		return this;
	}

	public Parameter add(String key, Object value) {
		return this.addParameter(key, value);
	}
	
}
