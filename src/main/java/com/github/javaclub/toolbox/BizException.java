/*
 * @(#)BizException.java	2017年4月19日
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox;

import com.github.javaclub.toolbox.ToolBox.MessageCode;

/**
 * BizException
 * <p>
 * 通用业务异常，不知道抛啥异常均可以抛出此异常，一般由程序截获并返回错误信息给调用端
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: BizException.java 2017年4月19日 10:16:31 Exp $
 */
public class BizException extends RuntimeException {

	private static final long serialVersionUID = 5465203973769259142L;
	
	/**
	 * 唯一标识码（快速定位问题用）
	 */
	private int code;

	public BizException() {
		super();
	}

	public BizException(int code, String msg) {
		super(msg);
		this.code = code;
	}
	
	public BizException(MessageCode mc) {
		super(mc.getMessage());
		this.code = mc.getCode();
	}

	public BizException(String msg) {
		super(msg);
	}

	public BizException(Throwable t) {
		super(t);
	}

	public BizException(String msg, Throwable t) {
		super(msg, t);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
