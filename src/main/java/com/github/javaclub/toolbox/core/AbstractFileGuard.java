package com.github.javaclub.toolbox.core;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Check every now and then that a certain file has not changed.
 * <p>
 * 
 * If it has, then call the {@link #doOnChange} method.
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: AbstractFileGuard.java 56 2011-06-27 02:11:24Z gerald.chen.hz@gmail.com $
 */
public abstract class AbstractFileGuard extends Thread {

	/**
	 * The default delay between every file modification check, set to 60 seconds.
	 */
	public static final long DEFAULT_DELAY = 60000L;
	/**
	 * The name of the file to observe for changes.
	 */
	protected String filename;

	protected static final Log logger = LogFactory.getLog(AbstractFileGuard.class);

	/**
	 * The delay to observe between every check. By default set {@link #DEFAULT_DELAY}.
	 */
	protected long delay = DEFAULT_DELAY;

	File file;
	long lastModif = 0L;
	boolean warnedAlready = false;
	boolean interrupted = false;

	protected AbstractFileGuard(String filename) {
		this.filename = filename;
		file = new File(filename);
		setDaemon(true);
		checkAndConfigure();
	}

	/**
	 * Set the delay to observe between each check of the file changes.
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

	protected abstract void doOnChange();

	protected void checkAndConfigure() {
		boolean fileExists;
		try {
			fileExists = file.exists();
		} catch (SecurityException e) {
			logger.warn("Was not allowed to read check file existance, file:["
					+ filename + "].");
			interrupted = true; // there is no point in continuing
			return;
		}

		if (fileExists) {
			long l = file.lastModified(); // this can also throw a SecurityException
			if (l > lastModif) { // however, if we reached this point this
				lastModif = l; // is very unlikely.
				doOnChange();
				warnedAlready = false;
			}
		} else {
			if (!warnedAlready) {
				logger.debug("[" + filename + "] does not exist.");
				warnedAlready = true;
			}
		}
	}

	public void run() {
		while (!interrupted) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// no interruption expected
			}
			checkAndConfigure();
		}
	}
}
