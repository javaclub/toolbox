package com.github.javaclub.toolbox.core;

import java.io.Serializable;

/**
 * Root exception for all runtime exceptions.
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: JRuntimeException.java 56 2011-06-27 02:11:24Z gerald.chen.hz@gmail.com $
 */
public class JRuntimeException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 8093585396636106475L;

	/**
     * Creates a new JRuntimeException.
     */
    public JRuntimeException() {
        super();
    }

    /**
     * Constructs a new JRuntimeException.
     *
     * @param message the reason for the exception
     */
    public JRuntimeException(String message) {
        super(message);
    }

    /**
     * Constructs a new JRuntimeException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public JRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new JRuntimeException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public JRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
