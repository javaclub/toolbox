/*
 * @(#)XmlWriter.java	2010-1-24
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.core.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Makes writing XML much much easier.
 * 
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: XmlWriter.java 72 2011-06-28 07:45:05Z gerald.chen.hz@gmail.com $
 */
public class XmlWriter {

	/**
	 * CDATA start tag: {@value}
	 */
	public static final String CDATA_START = "<![CDATA[";
	/**
	 * CDATA end tag: {@value}
	 */
	public static final String CDATA_END = "]]>";

	/**
	 * Default encoding value which is {@value}
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(XmlWriter.class);

	private Writer out; // underlying writer
	private String encoding; // the encoding to be written into the XML header/metatag
	private Stack<String> stack = new Stack<String>(); // of xml element names
	private StringBuffer attrs; // current attribute string
	private boolean empty; // is the current node empty
	private boolean closed = true; // is the current node closed...

	private boolean pretty = true; // is pretty printing enabled?
	/**
	 * was text the last thing output?
	 */
	private boolean wroteText = false;
	/**
	 * output this to indent one level when pretty printing
	 */
	private String indent = "  ";
	/**
	 * output this to end a line when pretty printing
	 */
	private String newline = "\n";

	/**
	 * Create an XmlWriter on top of an existing java.io.Writer.
	 */
	public XmlWriter(Writer writer) {
		this(writer, null);
	}

	/**
	 * Create an XmlWriter on top of an existing java.io.Writer.
	 */
	public XmlWriter(Writer writer, String encoding) {
		setWriter(writer, encoding);
	}

	/**
	 * Create an XmlWriter on top of an existing {@link java.io.OutputStream}.
	 * 
	 * @param outputStream
	 * @param encoding The encoding to be used for writing to the given output stream. Can be <code>null</code>. If it is
	 *            <code>null</code> the {@link #DEFAULT_ENCODING} is used.
	 * @throws UnsupportedEncodingException
	 * @since 2.4
	 */
	public XmlWriter(OutputStream outputStream, String encoding)
			throws UnsupportedEncodingException {
		if (encoding == null) {
			encoding = DEFAULT_ENCODING;
		}
		OutputStreamWriter writer = new OutputStreamWriter(outputStream, encoding);
		setWriter(writer, encoding);
	}

	/**
	 * Turn pretty printing on or off. Pretty printing is enabled by default, but it can be turned off to generate more compact XML.
	 * 
	 * @param enable true to enable, false to disable pretty printing.
	 */
	public void enablePrettyPrint(boolean enable) {
		if (logger.isDebugEnabled())
			logger.debug("enablePrettyPrint(enable={" + String.valueOf(enable)
					+ "}) - start");

		this.pretty = enable;
	}

	/**
	 * Specify the string to prepend to a line for each level of indent. It is 2 spaces ("  ") by default. Some may prefer a single tab
	 * ("\t") or a different number of spaces. Specifying an empty string will turn off indentation when pretty printing.
	 * 
	 * @param indent representing one level of indentation while pretty printing.
	 */
	public void setIndent(String indent) {
		logger.debug("setIndent(indent={" + indent + "}) - start");

		this.indent = indent;
	}

	/**
	 * Specify the string used to terminate each line when pretty printing. It is a single newline ("\n") by default. Users who need to
	 * read generated XML documents in Windows editors like Notepad may wish to set this to a carriage return/newline sequence ("\r\n").
	 * Specifying an empty string will turn off generation of line breaks when pretty printing.
	 * 
	 * @param newline representing the newline sequence when pretty printing.
	 */
	public void setNewline(String newline) {
		logger.debug("setNewline(newline={" + newline + "}) - start");

		this.newline = newline;
	}

	/**
	 * A helper method. It writes out an element which contains only text.
	 * 
	 * @param name String name of tag
	 * @param text String of text to go inside the tag
	 */
	public XmlWriter writeElementWithText(String name, String text) throws IOException {
		logger.debug("writeElementWithText(name={" + name + "}, text={" + text
				+ "}) - start");

		writeElement(name);
		writeText(text);
		return endElement();
	}

	/**
	 * A helper method. It writes out empty entities.
	 * 
	 * @param name String name of tag
	 */
	public XmlWriter writeEmptyElement(String name) throws IOException {
		logger.debug("writeEmptyElement(name={" + name + "}) - start");

		writeElement(name);
		return endElement();
	}

	/**
	 * Begin to write out an element. Unlike the helper tags, this tag will need to be ended with the endElement method.
	 * 
	 * @param name String name of tag
	 */
	public XmlWriter writeElement(String name) throws IOException {
		logger.debug("writeElement(name={" + name + "}) - start");

		return openElement(name);
	}

	/**
	 * Begin to output an element.
	 * 
	 * @param name name of element.
	 */
	private XmlWriter openElement(String name) throws IOException {
		logger.debug("openElement(name={" + name + "}) - start");

		boolean wasClosed = this.closed;
		closeOpeningTag();
		this.closed = false;
		if (this.pretty) {
			// ! wasClosed separates adjacent opening tags by a newline.
			// this.wroteText makes sure an element embedded within the text of
			// its parent element begins on a new line, indented to the proper
			// level. This solves only part of the problem of pretty printing
			// entities which contain both text and child entities.
			if (!wasClosed || this.wroteText) {
				this.out.write(newline);
			}
			for (int i = 0; i < this.stack.size(); i++) {
				this.out.write(indent); // Indent opening tag to proper level
			}
		}
		this.out.write("<");
		this.out.write(name);
		stack.add(name);
		this.empty = true;
		this.wroteText = false;
		return this;
	}

	// close off the opening tag
	private void closeOpeningTag() throws IOException {
		logger.debug("closeOpeningTag() - start");

		if (!this.closed) {
			writeAttributes();
			this.closed = true;
			this.out.write(">");
		}
	}

	// write out all current attributes
	private void writeAttributes() throws IOException {
		logger.debug("writeAttributes() - start");

		if (this.attrs != null) {
			this.out.write(this.attrs.toString());
			this.attrs.setLength(0);
			this.empty = false;
		}
	}

	/**
	 * Write an attribute out for the current element. Any XML characters in the value are escaped. Currently it does not actually throw
	 * the exception, but the API is set that way for future changes.
	 * 
	 * @param attr name of attribute.
	 * @param value value of attribute.
	 * @see #writeAttribute(String, String, boolean)
	 */
	public XmlWriter writeAttribute(String attr, String value) throws IOException {
		logger.debug("writeAttribute(attr={" + attr + "}, value={" + value
				+ "}) - start");
		return this.writeAttribute(attr, value, false);
	}

	/**
	 * Write an attribute out for the current element. Any XML characters in the value are escaped. Currently it does not actually throw
	 * the exception, but the API is set that way for future changes.
	 * 
	 * @param attr name of attribute.
	 * @param value value of attribute.
	 * @param literally If the writer should be literally on the given value which means that meta characters will also be preserved by
	 *            escaping them. Mainly preserves newlines and tabs.
	 */
	public XmlWriter writeAttribute(String attr, String value, boolean literally)
			throws IOException {
		if (logger.isDebugEnabled())
			logger.debug("writeAttribute(attr={" + attr + "}, value={" + value
					+ "}, literally={" + String.valueOf(literally) + "}) - start");

		if (this.wroteText == true) {
			throw new IllegalStateException(
					"The text for the current element has already been written. Cannot add attributes afterwards.");
		}
		// maintain API
		/*if (false)
			throw new IOException();*/

		if (this.attrs == null) {
			this.attrs = new StringBuffer();
		}
		this.attrs.append(" ");
		this.attrs.append(attr);
		this.attrs.append("=\"");
		String val = escapeXml(value);
		if (literally) {
			val = escapeMetaCharacters(val);
		}
		this.attrs.append(val);
		this.attrs.append("\"");
		return this;
	}

	/**
	 * End the current element. This will throw an exception if it is called when there is not a currently open element.
	 */
	public XmlWriter endElement() throws IOException {
		logger.debug("endElement() - start");

		if (this.stack.empty()) {
			throw new IOException("Called endElement too many times. ");
		}
		String name = (String) this.stack.pop();
		if (name != null) {
			if (this.empty) {
				writeAttributes();
				this.out.write("/>");
			} else {
				if (this.pretty && !this.wroteText) {
					for (int i = 0; i < this.stack.size(); i++) {
						this.out.write(indent); // Indent closing tag to proper level
					}
				}
				this.out.write("</");
				this.out.write(name);
				this.out.write(">");
			}
			if (this.pretty)
				this.out.write(newline); // Add a newline after the closing tag
			this.empty = false;
			this.closed = true;
			this.wroteText = false;
		}
		return this;
	}

	/**
	 * Close this writer. It does not close the underlying writer, but does throw an exception if there are as yet unclosed tags.
	 */
	public void close() throws IOException {
		logger.debug("close() - start");

		this.out.flush();

		if (!this.stack.empty()) {
			throw new IOException("Tags are not all closed. " + "Possibly, "
					+ this.stack.pop() + " is unclosed. ");
		}
	}

	/**
	 * Output body text. Any XML characters are escaped.
	 * 
	 * @param text The text to be written
	 * @return This writer
	 * @throws IOException
	 * @see #writeText(String, boolean)
	 */
	public XmlWriter writeText(String text) throws IOException {
		logger.debug("writeText(text={" + text + "}) - start");
		return this.writeText(text, false);
	}

	/**
	 * Output body text. Any XML characters are escaped.
	 * 
	 * @param text The text to be written
	 * @param literally If the writer should be literally on the given value which means that meta characters will also be preserved by
	 *            escaping them. Mainly preserves newlines and tabs.
	 * @return This writer
	 * @throws IOException
	 */
	public XmlWriter writeText(String text, boolean literally) throws IOException {
		if (logger.isDebugEnabled())
			logger.debug("writeText(text={" + text + "}, literally={"
					+ String.valueOf(literally) + "}) - start");

		closeOpeningTag();
		this.empty = false;
		this.wroteText = true;

		String val = escapeXml(text);
		if (literally) {
			val = escapeMetaCharacters(val);
		}
		this.out.write(val);
		return this;
	}

	/**
	 * Write out a chunk of CDATA. This helper method surrounds the passed in data with the CDATA tag.
	 * 
	 * @param cdata of CDATA text.
	 */
	public XmlWriter writeCData(String cdata) throws IOException {
		logger.debug("writeCData(cdata={" + cdata + "}) - start");

		closeOpeningTag();

		boolean hasAlreadyEnclosingCdata = cdata.startsWith(CDATA_START)
				&& cdata.endsWith(CDATA_END);

		// There may already be CDATA sections inside the data.
		// But CDATA sections can't be nested - can't have ]]> inside a CDATA section.
		// (See http://www.w3.org/TR/REC-xml/#NT-CDStart in the W3C specs)
		// The solutions is to replace any occurrence of "]]>" by "]]]]><![CDATA[>",
		// so that the top CDATA section is split into many valid CDATA sections (you
		// can look at the "]]]]>" as if it was an escape sequence for "]]>").
		if (!hasAlreadyEnclosingCdata) {
			cdata = cdata.replaceAll(CDATA_END, "]]]]><![CDATA[>");
		}

		this.empty = false;
		this.wroteText = true;
		if (!hasAlreadyEnclosingCdata)
			this.out.write(CDATA_START);
		this.out.write(cdata);
		if (!hasAlreadyEnclosingCdata)
			this.out.write(CDATA_END);
		return this;
	}

	/**
	 * Write out a chunk of comment. This helper method surrounds the passed in data with the XML comment tag.
	 * 
	 * @param comment of text to comment.
	 */
	public XmlWriter writeComment(String comment) throws IOException {
		logger.debug("writeComment(comment={" + comment + "}) - start");

		writeChunk("<!-- " + comment + " -->");
		return this;
	}

	private void writeChunk(String data) throws IOException {
		logger.debug("writeChunk(data={" + data + "}) - start");

		closeOpeningTag();
		this.empty = false;
		if (this.pretty && !this.wroteText) {
			for (int i = 0; i < this.stack.size(); i++) {
				this.out.write(indent);
			}
		}

		this.out.write(data);

		if (this.pretty) {
			this.out.write(newline);
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	// Added for DbUnit

	/**
	 * Escapes some meta characters like \n, \r that should be preserved in the XML so that a reader will not filter out those symbols.
	 * 
	 * @param str The string to be escaped
	 * @return The escaped string
	 * @since 2.3.0
	 */
	private String escapeMetaCharacters(String str) {
		logger.debug("escapeXml(str={" + str + "}) - start");

		// 2. Do additional escapes. See http://www.w3.org/TR/2004/REC-xml-20040204/#AVNormalize
		str = replace(str, "\n", "&#xA;"); // linefeed (LF)
		str = replace(str, "\r", "&#xD;"); // carriage return (CR)
		return str;
	}

	private String escapeXml(String str) {
		logger.debug("escapeXml(str={" + str + "}) - start");

		str = replace(str, "&", "&amp;");
		str = replace(str, "<", "&lt;");
		str = replace(str, ">", "&gt;");
		str = replace(str, "\"", "&quot;");
		str = replace(str, "'", "&apos;");
		str = replace(str, "\t", "&#09;"); // tab
		return str;
	}

	private String replace(String value, String original, String replacement) {
		if (logger.isDebugEnabled())
			logger.debug("replace(value=" + value + ", original=" + original
					+ ", replacement=" + replacement + ") - start");

		StringBuffer buffer = null;

		int startIndex = 0;
		int lastEndIndex = 0;
		for (;;) {
			startIndex = value.indexOf(original, lastEndIndex);
			if (startIndex == -1) {
				if (buffer != null) {
					buffer.append(value.substring(lastEndIndex));
				}
				break;
			}

			if (buffer == null) {
				buffer = new StringBuffer((int) (original.length() * 1.5));
			}
			buffer.append(value.substring(lastEndIndex, startIndex));
			buffer.append(replacement);
			lastEndIndex = startIndex + original.length();
		}

		return buffer == null ? value : buffer.toString();
	}

	private void setEncoding(String encoding) {
		logger.debug("setEncoding(encoding={" + encoding + "}) - start");

		if (encoding == null && out instanceof OutputStreamWriter)
			encoding = ((OutputStreamWriter) out).getEncoding();

		if (encoding != null) {
			encoding = encoding.toUpperCase();

			// Use official encoding names where we know them,
			// avoiding the Java-only names. When using common
			// encodings where we can easily tell if characters
			// are out of range, we'll escape out-of-range
			// characters using character refs for safety.

			// I _think_ these are all the main synonyms for these!
			if ("UTF8".equals(encoding)) {
				encoding = "UTF-8";
			} else if ("US-ASCII".equals(encoding) || "ASCII".equals(encoding)) {
				// dangerMask = (short)0xff80;
				encoding = "US-ASCII";
			} else if ("ISO-8859-1".equals(encoding) || "8859_1".equals(encoding)
					|| "ISO8859_1".equals(encoding)) {
				// dangerMask = (short)0xff00;
				encoding = "ISO-8859-1";
			} else if ("UNICODE".equals(encoding) || "UNICODE-BIG".equals(encoding)
					|| "UNICODE-LITTLE".equals(encoding)) {
				encoding = "UTF-16";

				// TODO: UTF-16BE, UTF-16LE ... no BOM; what
				// release of JDK supports those Unicode names?
			}

			// if (dangerMask != 0)
			// stringBuf = new StringBuffer();
		}

		this.encoding = encoding;
	}

	/**
	 * Resets the handler to write a new text document.
	 * 
	 * @param writer XML text is written to this writer.
	 * @param encoding if non-null, and an XML declaration is written, this is the name that will be used for the character encoding.
	 * 
	 * @exception IllegalStateException if the current document hasn't yet ended (i.e. the output stream {@link #out} is not null)
	 */
	public final void setWriter(Writer writer, String encoding) {
		logger.debug("setWriter(writer={" + writer + "}, encoding={" + encoding
				+ "}) - start");

		if (this.out != null)
			throw new IllegalStateException("can't change stream in mid course");
		this.out = writer;
		if (this.out != null)
			setEncoding(encoding);
		// if (!(this.out instanceof BufferedWriter))
		// this.out = new BufferedWriter(this.out);
	}

	public XmlWriter writeDeclaration() throws IOException {
		logger.debug("writeDeclaration() - start");

		if (this.encoding != null) {
			this.out.write("<?xml version='1.0'");
			this.out.write(" encoding='" + this.encoding + "'");
			this.out.write("?>");
			this.out.write(this.newline);
		}

		return this;
	}

	public XmlWriter writeDoctype(String systemId, String publicId) throws IOException {
		logger.debug("writeDoctype(systemId={" + systemId + "}, publicId={" + publicId
				+ "}) - start");

		if (systemId != null || publicId != null) {
			this.out.write("<!DOCTYPE dataset");

			if (systemId != null) {
				this.out.write(" SYSTEM \"");
				this.out.write(systemId);
				this.out.write("\"");
			}

			if (publicId != null) {
				this.out.write(" PUBLIC \"");
				this.out.write(publicId);
				this.out.write("\"");
			}

			this.out.write(">");
			this.out.write(this.newline);
		}

		return this;
	}
	
	public static void test1() throws IOException {
		logger.debug("test1() - start");

		Writer writer = new java.io.StringWriter();
		XmlWriter xmlwriter = new XmlWriter(writer);
		xmlwriter.writeElement("person").writeAttribute("name", "fred").writeAttribute(
				"age", "12").writeElement("phone").writeText("4254343").endElement()
				.writeElement("friends").writeElement("bob").endElement().writeElement(
						"jim").endElement().endElement().endElement();
		xmlwriter.close();
		System.err.println(writer.toString());
	}

	public static void test2() throws IOException {
		logger.debug("test2() - start");

		Writer writer = new java.io.StringWriter();
		XmlWriter xmlwriter = new XmlWriter(writer);
		xmlwriter.writeComment("Example of XmlWriter running");
		xmlwriter.writeElement("person");
		xmlwriter.writeAttribute("name", "fred");
		xmlwriter.writeAttribute("age", "12");
		xmlwriter.writeElement("phone");
		xmlwriter.writeText("4254343");
		xmlwriter.endElement();
		xmlwriter.writeComment("Examples of empty tags");
		// xmlwriter.setDefaultNamespace("test");
		xmlwriter.writeElement("friends");
		xmlwriter.writeEmptyElement("bob");
		xmlwriter.writeEmptyElement("jim");
		xmlwriter.endElement();
		xmlwriter.writeElementWithText("foo", "This is an example.");
		xmlwriter.endElement();
		xmlwriter.close();
		System.err.println(writer.toString());
	}
	
	// Two example methods. They should output the same XML:
	// <person name="fred" age="12"><phone>425343</phone><bob/></person>
	public static void main(String[] args) throws IOException {

		test1();
		test2();
	}
}
