/*
 * @(#)ResultDO.java	2013-11-29
 *
 * Copyright (c) 2013. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * ResultDO 简单结果对象的封装
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: ResultDO.java 2013-11-29 10:43:04 Exp $
 */
public class ResultDO<T> implements Serializable {

	private static final long serialVersionUID = 5146487207349338705L;
	
	/**
	 * 是否执行成功 
	 */
	private boolean success = false;
	
	/**
	 * 如果返货错误或失败信息，请以"ERROR-XXX-XXX"的格式  
	 */
	private String resultCode;
	
	/**
	 * 错误提示信息 
	 */
	private String errorMsg;
	
	/**
	 * 单值返回
	 */
	private T result;
	
	/**
	 * 多值返回,key为枚举字符串
	 */
	private Map<String, Object> models = new HashMap<String, Object>(16);

	public ResultDO() {
		super();
	}

	public ResultDO(boolean success) {
		super();
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}
	
	public boolean isFailured() {
		return !success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T value) {
		this.result = value;
	}

	public Object getModel(String key) {
		return getModels().get(key);
	}

	public void setModel(String key, Object model) {
		getModels().put(key, model);
	}

    public Map<String, Object> getModels() {
        return models;
    }

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
