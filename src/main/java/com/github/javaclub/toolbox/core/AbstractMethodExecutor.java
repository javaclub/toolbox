/*
 * @(#)AbstractMethodExecutor.java	2011-1-12
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 执行预期的方法，直到达到某个特定的状态
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: AbstractMethodExecutor.java 56 2011-06-27 02:11:24Z gerald.chen.hz@gmail.com $
 */
public abstract class AbstractMethodExecutor<P, R> {
	
	protected static final Log LOG = LogFactory.getLog(AbstractMethodExecutor.class);

	/** 输入参数 */
	private P param;
	
	/** 最终的执行结果反馈 */
	private R result;
	
	/** 达到特定状态前，最多执行次数 */
	private int maxTimes;
	
	/** 实际尝试执行的次数 */
	private int tryTimes;
	
	protected AbstractMethodExecutor() {
		
	}
	
	protected AbstractMethodExecutor(int maxTimes) {
		this.maxTimes = maxTimes;
	}

	protected AbstractMethodExecutor(P param, int maxTimes) {
		this.setParam(param);
		this.maxTimes = maxTimes;
		tryTimes = 0;
	}

	public void doTask() {
		int count = 0;
		while (!isExpected() && count < maxTimes) {
			this.execute();
			count++;
			if(LOG.isInfoEnabled()) {
				LOG.info("第 " + count + "次执行...");
			}
		}
		tryTimes = count;
		// 如果达到了预期状态
		if(isExpected()) {
			result = success();
		} else {
			result = failure();
		}
	}

	/**
	 * 预期准备要执行的方法
	 *
	 */
	public abstract void execute();
	
	/**
	 * 是否已经到达了预期的状态
	 *
	 * @return
	 */
	public abstract boolean isExpected();
	
	/**
	 * 达到预期状态后执行方法
	 *
	 * @return
	 */
	public abstract R success();
	
	/**
	 * 未达到预期状态后执行方法
	 *
	 * @return
	 */
	public abstract R failure();

	public void setParam(P param) {
		this.param = param;
	}

	public P getParam() {
		return param;
	}
	
	public R getResult() {
		return result;
	}

	public int getTryTimes() {
		return tryTimes;
	}

	public int getMaxTimes() {
		return maxTimes;
	}

	public void setMaxTimes(int maxTimes) {
		this.maxTimes = maxTimes;
	}

}
