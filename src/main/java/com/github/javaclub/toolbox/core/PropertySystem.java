/*
 * @(#)PropertySystem.java	2011-6-16
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.github.javaclub.toolbox.util.IOUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

/**
 * System properties manager.
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: PropertySystem.java 117 2011-07-13 09:52:00Z gerald.chen.hz@gmail.com $
 */
public class PropertySystem {
	
	private static final String SYSTEM_PROPERTY_MONITOR_THREAD = "system-property-monitor-thread";

	/** logger */
	protected static final Log LOG = LogFactory.getLog(PropertySystem.class);

	/** The default file is in the root of classpath: config.properties */
	private static String propertyFile = "config.properties";
	
	/** whether is monitored  */
	private static boolean monitoring = false;
	
	/** properties collection */
	private static Map<String, String> properties = null;
	
	private static Date startupTime;
	
	/**
	 * Starts up system properties.
	 *
	 */
	public synchronized boolean startup() {
		try {
			getProperties();
			return true;
		} catch (IOException e) {
			LOG.error("Failed to startup PropertySystem.", e);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Shuts down system properties.
	 */
	public synchronized void shutdown() {
		if (properties != null) {
			properties.clear();
			properties = null;
		}
	}
	
	/**
	 * Reload the system properties.
	 *
	 */
	public synchronized void reload() {
		try {
			loading();
		} catch (IOException e) {
			LOG.error("Failed to reload PropertySystem.", e);
			e.printStackTrace();
		}
		if(!monitoring) {
			monitoring();
			monitoring = true;
		}
	}
	
	public static String getProperty(String key) {
		try {
			Map<String, String> map = getProperties();
			return map.get(key);
		} catch (IOException e) {
			LOG.error("Failed to getProperty from PropertySystem.", e);
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getProperty(String key, String defaultValue) {
		try {
			Map<String, String> map = getProperties();
			return map.get(key) == null ? defaultValue : map.get(key);
		} catch (IOException e) {
			return defaultValue;
		}
	}
	
	public synchronized static String setProperty(String key, String value) throws IOException {
		Resource resource = new ClassPathResource(getPropertyFile());
		FileOutputStream out = null;
		try {
			File file = resource.getFile();
			Properties p = new Properties();
			p.load(new FileInputStream(file));
			p.setProperty(key, value);
			// save to file
			out = new FileOutputStream(file);
			p.store(out, "Modified on " + new Date());
			
			return getProperties().put(key, value);
		} finally {
			IOUtil.close(out);
		}
	}
	
	public static byte byteValue(String key) {
		return Byte.parseByte(getProperty(key));
	}
	
	public static short shortValue(String key) {
		return Short.parseShort(getProperty(key));
	}
	
	public static int intValue(String key) {
		return Integer.parseInt(getProperty(key));
	}
	
	public static long longValue(String key) {
		return Long.parseLong(getProperty(key));
	}
	
	public static float floatValue(String key) {
		return Float.parseFloat(getProperty(key));
	}
	
	public static double doubleValue(String key) {
		return Double.parseDouble(getProperty(key));
	}
	
	public static boolean boolValue(String key) {
		return "true".equals(getProperty(key));
	}
	
	/**
	 * Get system properties.
	 *
	 * @return system properties
	 * @throws IOException 
	 */
	public static final Map<String, String> getProperties() throws IOException {
		if (properties == null) {
			synchronized (PropertySystem.class) {
				if (properties == null) {
					loading();
					startupTime = new Date();
					if(!monitoring) {
						monitoring();
						monitoring = true;
					}
				}
			}
		}
		return properties;
	}
	
	public static Date getStartupTime() {
		return startupTime;
	}

	/**
	 * Monitor the property files
	 *
	 */
	protected static void monitoring() {
		if(monitoring) {
			LOG.warn("the property file has been monitored.");
			return;
		}
		final String path = getAbsolutePath();
		if(!StringUtils.hasLength(path)) return;
		new Thread(new AbstractFileGuard(path) {
			protected void doOnChange() {
				try {
					logger.info("Reloading system property from " + path);
					loading();
					logger.info("Reloaded system property successfully.");
				} catch (Exception e) {
					logger.error("Reloaded system property failed.", e);
				}
			}
		}, SYSTEM_PROPERTY_MONITOR_THREAD).start();
		LOG.info("File monitor has started.");
	}
	
	private static String getAbsolutePath() {
		String path = null;
		Resource resource = new ClassPathResource(getPropertyFile());
		try {
			path = resource.getFile().getAbsolutePath();
		} catch (IOException e) {
			LOG.error("failed to get the property file.", e);
		}
		return path;
	}

	/**
	 * Reload the properties.
	 * @throws IOException 
	 *
	 */
	private final static void loading() throws IOException {
		if(LOG.isDebugEnabled()) {
			LOG.debug("Loading system properties ...");
		}
		if(properties != null) {
			properties.clear();
			properties = null;
			if(LOG.isDebugEnabled()) {
				LOG.debug("Cleared system properties ...");
			}
		}
		System.gc();
		System.gc();
		Properties p = new Properties();
		Resource resource = new ClassPathResource(getPropertyFile());
		InputStream stream = null;
		try {
			stream = resource.getInputStream();
			p.load(stream);
			properties = new HashMap(p);
		} finally {
			IOUtil.close(stream);
			p.clear();
		}
		System.gc();
		System.gc();
		if(LOG.isDebugEnabled()) {
			LOG.debug("Loaded system properties successfully.");
		}
	}

	public static String getPropertyFile() {
		return propertyFile;
	}

	/**
	 * set the propertyFile
	 *
	 * @param propertyFile the property file
	 */
	public void setPropertyFile(String propertyFile) {
		PropertySystem.propertyFile = propertyFile;
	}
	
}
