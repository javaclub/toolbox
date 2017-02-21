package com.github.javaclub.toolbox.core.ip;

/**
 * <p>
 * IPEntry [beginIp, endIp, country, area]
 * </p>
 * 
 */
public class IPEntry {

	public String beginIp;
	public String endIp;
	public String country;
	public String area;

	/**
	 * the default constructor.
	 */
	public IPEntry() {
		beginIp = endIp = country = area = "";
	}

}
