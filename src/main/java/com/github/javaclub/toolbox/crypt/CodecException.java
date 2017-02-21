package com.github.javaclub.toolbox.crypt;

import com.github.javaclub.toolbox.core.JRuntimeException;

/**
 * Root exception related to issues during encoding or decoding.
 *
 * @author Les Hazlewood
 * @since 0.9
 */
public class CodecException extends JRuntimeException {

	private static final long serialVersionUID = 3586764372982587670L;

	/**
     * Creates a new <code>CodecException</code>.
     */
    public CodecException() {
        super();
    }

    /**
     * Creates a new <code>CodecException</code>.
     *
     * @param message the reason for the exception.
     */
    public CodecException(String message) {
        super(message);
    }

    /**
     * Creates a new <code>CodecException</code>.
     *
     * @param cause the underlying cause of the exception.
     */
    public CodecException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new <code>CodecException</code>.
     *
     * @param message the reason for the exception.
     * @param cause   the underlying cause of the exception.
     */
    public CodecException(String message, Throwable cause) {
        super(message, cause);
    }
}
