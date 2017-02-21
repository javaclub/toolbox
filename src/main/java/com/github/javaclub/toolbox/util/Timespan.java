/*
 * @(#)Timespan.java	
 *
 * Copyright (c) 2014. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.util;

import java.io.Serializable;
import java.util.Date;

import org.springframework.util.Assert;

/**
 * Timespan
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">hongyuan.czq@alibaba-inc.com</a>
 */
public class Timespan implements Serializable {

	/** desc */
	private static final long serialVersionUID = -2139655325679062477L;
	
	/** 时间跨度，单位是毫秒 */
	private Long spanMillis;
	
	/** 天 */
	private Long day;
	
	/** 小时 */
	private Long hour;
	
	/** 分 */
	private Long minute;
	
	/** 秒 */
	private Long second;
	
	/** 毫秒 */
	private Long millis;

	public Timespan() {
		
	}

	public Timespan(Long spanMillis) {
		this.spanMillis = spanMillis;
		initialize(spanMillis);
	}
	
	/**
	 * 计算出格式为#天#小时#分#秒的显示格式
	 *
	 * @param includingMillis
	 * @return
	 */
	public String format(boolean includingMillis) {
		if(getSpanMillis() <= 0) {
			return "";// 返回空即是0天0小时0分0秒
		}
		StringBuilder sbf = new StringBuilder();
		if(day != 0) {
			sbf.append(day + "天");
		}
		if(hour != 0) {
			sbf.append(hour + "小时");
		}
		if(minute != 0) {
			sbf.append(minute + "分");
		}
		if(second != 0) {
			sbf.append(second + "秒");
		}
		if(includingMillis && millis != 0) {
			sbf.append(millis + "毫秒");
		}
		return sbf.toString();
	}
	
	/**
	 * 计算两个时间之间的跨度
	 *
	 * @param start 时间点1
	 * @param end   时间点2
	 * @return      时间跨度对象
	 */
	public static Timespan calculate(Date start, Date end) {
		Assert.notNull(start, "Object is null --> [start]");
		Assert.notNull(end, "Object is null --> [end]");
		return new Timespan(Math.abs(end.getTime() - start.getTime()));
	}
	
	private void initialize(final long span) {
		long msUsed = span;
		if(msUsed > 0) {
			if (msUsed < 1000) {
				millis = msUsed;
				return;
			}
			// 长于1秒的过程，毫秒不计
			msUsed /= 1000;// 此刻开始msUsed已经为秒了
			if (msUsed < 60) {
				second = msUsed;
				millis = span % 1000;
				return;
			}
			if (msUsed < 3600) {
				minute = msUsed / 60;
				second = msUsed % 60;
				millis = span % 1000;
				return;
			}
			// 3600 * 24 = 86400
			if (msUsed < 86400) {
				hour = msUsed / 3600;
				minute = (msUsed - hour * 3600) / 60;
				second = (msUsed - hour * 3600) % 60;
				millis = span % 1000;
				return;
			}

			day = msUsed / 86400;
			hour = (msUsed - day * 86400) / 3600;
			minute = (msUsed - day * 86400 - hour * 3600) / 60;
			second = (msUsed - day * 86400 - hour * 3600) % 60;
			millis = span % 1000;
		}
	}

	public Long getSpanMillis() {
		return spanMillis;
	}

	public void setSpanMillis(Long spanMillis) {
		this.spanMillis = spanMillis;
	}
	
	public Long getDay() {
		return day;
	}

	public void setDay(Long day) {
		this.day = day;
	}

	public Long getHour() {
		return hour;
	}

	public void setHour(Long hour) {
		this.hour = hour;
	}

	public Long getMinute() {
		return minute;
	}

	public void setMinute(Long minute) {
		this.minute = minute;
	}

	public Long getSecond() {
		return second;
	}

	public void setSecond(Long second) {
		this.second = second;
	}

	public Long getMillis() {
		return millis;
	}

	public void setMillis(Long millis) {
		this.millis = millis;
	}

	public static void main(String[] args) {
		Long val = Long.valueOf(200);
		System.out.println(val.intValue());
		
		Timespan span = Timespan.calculate(DateUtil.randomDate("2010-08-25", "2010-10-01"), DateUtil.randomDate("2011-08-25", "2011-10-01"));
		System.out.println(span.format(true));
		
	}
}
