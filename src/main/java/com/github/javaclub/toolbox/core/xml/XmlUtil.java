/*
 * @(#)XmlUtil.java	2010-1-24
 *
 * Copyright (c) 2010 by gerald. All Rights Reserved.
 */

package com.github.javaclub.toolbox.core.xml;

import java.io.CharArrayReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.github.javaclub.toolbox.core.Numbers;

/**
 * A utility class for Xml procesing.
 * 
 * @author <a href="mailto:gerald.chen@qq.com">Gerald Chen</a>
 * @version $Id: XmlUtil.java 66 2011-06-27 03:21:59Z gerald.chen.hz@gmail.com $
 */
public class XmlUtil {

	protected static final Log logger = LogFactory.getLog(XmlUtil.class);

	/**
	 * constructor: forbid to call
	 */
	protected XmlUtil() {
		super();
	}

	/**
	 * export an IXMLSupporter implementator to XML element
	 * 
	 * @param _sName name of the XML element to create
	 * @return the XML element builded
	 * @throws XmlParserException if encounter errors
	 */
	public final static Element toXMLElement(Xmlizable _xmlSupporter, String _sName)
			throws XmlParserException {
		Element element = DocumentHelper.createElement(_sName);
		_xmlSupporter.toXml(element);
		return element;
	}

	// =============================================================================
	// XML file import

	/**
	 * load XML document from the specified file
	 * 
	 * @param p_sFileName name of the XML file
	 * @return Returns the XML document loaded
	 * @throws XmlParserException if encounter errors
	 */
	public final static Document fileToDoc(String p_sFileName) throws XmlParserException {
		return fileToDoc(new File(p_sFileName));
	}

	/**
	 * load XML document from the specified file
	 * 
	 * @param _file the XML file object
	 * @return Returns the XML document loaded
	 * @throws XmlParserException if encounter errors
	 */
	public final static Document fileToDoc(File _file) throws XmlParserException {
		if (!_file.exists()) {
			throw new XmlParserException("File is not found: " + _file.getAbsolutePath());
		}

		// else
		try {
			SAXReader reader = new SAXReader();
			return reader.read(_file);
		} catch (Exception ex) {
			throw new XmlParserException("Failed to load XML doucment from file: "
					+ _file.getAbsolutePath(), ex);
		}
	}

	/**
	 * load XML root element from the specified file
	 * 
	 * @param p_sFileName name of the XML file
	 * @return Returns the XML root element loaded
	 * @throws XmlParserException if encounter errors
	 */
	public final static Element fileToElement(String p_sFileName) throws XmlParserException {
		Document xmlDoc = fileToDoc(p_sFileName);
		return (xmlDoc == null ? null : xmlDoc.getRootElement());
	}

	/**
	 * load XML root element from the specified file
	 * 
	 * @param p_file the XML file object
	 * @return Returns the XML root element loaded
	 * @throws XmlParserException if encounter errors
	 */
	public final static Element fileToElement(File p_file) throws XmlParserException {
		Document xmlDoc = fileToDoc(p_file);
		return (xmlDoc == null ? null : xmlDoc.getRootElement());
	}

	// =============================================================================
	// XML file export

	/**
	 * export XML document to the specified file
	 * 
	 * @param _xmlDoc the XML Document to export
	 * @param p_sFileName name of the file
	 * @param p_sXMLEncoding XML encoding
	 * @param p_sFileCharsetName charset name of the file
	 * @return Returns true if successul, otherwise false.
	 * @throws XmlParserException if encounter errors
	 */
	public final static boolean toFile(Document _xmlDoc, String _sFileName,
			String _sXMLEncoding, String _sFileCharsetName) throws XmlParserException {
		if (_xmlDoc == null)
			return false;

		// else
		FileOutputStream fos = null;
		Writer out = null;
		XMLWriter writer = null;
		try {
			// open the file output stream to write
			fos = new FileOutputStream(_sFileName);
			if (_sFileCharsetName == null) {
				out = new OutputStreamWriter(fos);
			} else {
				out = new OutputStreamWriter(fos, _sFileCharsetName); // 指定编码方式
			}

			// write the XML Document to the file
			OutputFormat outformat = OutputFormat.createPrettyPrint();
			if (_sXMLEncoding != null) {
				outformat.setEncoding(_sXMLEncoding);
			}
			writer = new XMLWriter(out, outformat);
			writer.write(_xmlDoc);
			writer.flush();

			return true;
		} catch (Exception ex) {
			throw new XmlParserException("Failed to export the XML document to file:"
					+ _sFileName, ex);
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (Exception ex) {
				}
			if (out != null)
				try {
					out.close();
				} catch (Exception ex) {
				}
			if (fos != null)
				try {
					fos.close();
				} catch (Exception ex) {
				}
		}
	}

	/**
	 * export XML element to the specified file
	 * 
	 * @param _xmlElement the XML element to export
	 * @param _sFileName name of the file
	 * @param _sXMLEncoding XML encoding
	 * @param _sFileCharsetName charset name of the file
	 * @return Returns true if successul, otherwise false.
	 * @throws XmlParserException if encounter errors
	 */
	public final static boolean toFile(Element _xmlElement, String _sFileName,
			String _sXMLEncoding, String _sFileCharsetName) throws XmlParserException {
		if (_xmlElement == null)
			return false;

		// else
		Document doc = DocumentHelper.createDocument(_xmlElement);
		return toFile(doc, _sFileName, _sXMLEncoding, _sFileCharsetName);
	}

	// =============================================================================
	// XML import from String

	/**
	 * convert the string to XML document
	 * 
	 * @param _sXMLString String in XML format
	 * @return the XML document object
	 * @throws XmlParserException if encounter error
	 */
	public final static Document strToDocument(String _sXMLString) throws XmlParserException {
		if (_sXMLString == null)
			return null;

		// else
		CharArrayReader strReader = null;
		try {
			strReader = new CharArrayReader(_sXMLString.toCharArray());
			SAXReader reader = new SAXReader();
			return reader.read(strReader);
		} catch (Exception ex) {
			throw new XmlParserException("Failed to convert string to XML Document.", ex);
		} finally {
			if (strReader != null)
				try {
					strReader.close();
				} catch (Exception ex) {
				}
		}
	}// END: strToDocument

	/**
	 * convert the string to XML document
	 * 
	 * @param _sXMLString String in XML format
	 * @return the XML document object
	 * @throws XmlParserException if encounter error
	 */
	public final static Element strToElement(String _sXMLString) throws XmlParserException {
		if (_sXMLString == null)
			return null;

		// else
		Document xmlDoc = strToDocument(_sXMLString);
		return (xmlDoc == null) ? null : xmlDoc.getRootElement();
	}

	// =============================================================================
	// XML export to string

	/**
	 * export XML element to string
	 * 
	 * @param _xmlElement XML element to export
	 * @param _sEncoding char encoding; optional，default null.
	 * @return string in XML format
	 */
	public final static String toString(Element _xmlElement, String _sEncoding)
			throws XmlParserException {
		if (_xmlElement == null)
			return null;

		// else
		Writer out = null;
		XMLWriter writer = null;
		try {
			OutputFormat outformat = OutputFormat.createPrettyPrint();
			if (_sEncoding != null) {
				outformat.setEncoding(_sEncoding);
			}

			out = new StringWriter();
			writer = new XMLWriter(out, outformat);
			writer.write(_xmlElement);
			writer.flush();
			return out.toString();
		} catch (Exception ex) {
			throw new XmlParserException("Failed to export XML element to string", ex);
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (Exception ex) {
				}
			if (out != null)
				try {
					out.close();
				} catch (Exception ex) {
				}
		}
	}// END: toString()

	/**
	 * @see #toString(Element,String)
	 */
	public final static String toString(Element _xmlElement) throws XmlParserException {
		return toString(_xmlElement, null);
	}

	/**
	 * export XML element to string
	 * 
	 * @param _xmlDoc XML document to export
	 * @param _sEncoding char encoding; optional，default null.
	 * @return string in XML format
	 */
	public final static String toString(Document _xmlDoc, String _sEncoding)
			throws XmlParserException {
		if (_xmlDoc == null)
			return null;

		// else
		Writer out = null;
		XMLWriter writer = null;
		try {
			OutputFormat outformat = OutputFormat.createPrettyPrint();
			if (_sEncoding != null) {
				outformat.setEncoding(_sEncoding);
			}

			out = new StringWriter();
			writer = new XMLWriter(out, outformat);
			writer.write(_xmlDoc);
			writer.flush();
			return out.toString();
		} catch (Exception ex) {
			throw new XmlParserException("Failed to export XML document to string", ex);
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (Exception ex) {
				}
			if (out != null)
				try {
					out.close();
				} catch (Exception ex) {
				}
		}
	}// END: toString()

	// =============================================================================
	// XML element constructor

	/**
	 * create XML element
	 * 
	 * @param _sName name of the XML element
	 * @param _sContent content value of the XML element
	 * @param _bUseCDATA if use CDATA format
	 * @return Returns the XML element created
	 */
	public final static Element createElement(String _sName, String _sContent,
			boolean _bUseCDATA) {
		if (_sName == null)
			return null;

		// else
		Element element = DocumentHelper.createElement(_sName);
		if (_sContent != null) {
			if (_bUseCDATA) {
				element.add(DocumentHelper.createCDATA(_sContent));
			} else {
				element.addText(_sContent);
			}
		}// endif
		return element;
	}

	/**
	 * Creat XML element
	 * 
	 * @param _sName name of the XML element
	 * @param _lContentValue integer content value
	 * @return Returns the XML element created
	 */
	public final static Element createElement(String _sName, long _lContentValue) {
		return createElement(_sName, String.valueOf(_lContentValue), false);
	}

	/**
	 * create XML element
	 * 
	 * @param _sName name of the XML element
	 * @param _bContentValue boolean content value
	 * @return Returns the XML element created
	 */
	public final static Element createElement(String _sName, boolean _bContentValue) {
		return createElement(_sName, Boolean.toString(_bContentValue), false);
	}

	// =============================================================================
	// handling with attribute

	/**
	 * Gets the blank trimed value of the specified attribute
	 * 
	 * @param _xmlElement XML element
	 * @param _sAttrName attribute name
	 * @return the trimed attribute value
	 */
	public final static String getAttributeValueTrim(Element _xmlElement,
			String _sAttrName) {
		String sValue = _xmlElement.attributeValue(_sAttrName);
		if (sValue != null) {
			sValue = sValue.trim();
		}
		return sValue;
	}

	/**
	 * get the integer attribute value
	 * 
	 * @param _xmlElement XML element
	 * @param _sAttrName attribute name
	 * @return Returns the integer attribute value
	 */
	public final static int getAttributeValueAsInt(Element _xmlElement,
			String _sAttrName) {
		String sValue = getAttributeValueTrim(_xmlElement, _sAttrName);
		return Numbers.toInt(sValue, _sAttrName);
	}

	/**
	 * get the integer attribute value
	 * 
	 * @param _xmlElement XML element
	 * @param _sAttrName attribute name
	 * @param _nDefault default value
	 * @return Returns the default value if attribute is empty, otherwise the integer attribute value
	 */
	public final static int getAttributeValueAsInt(Element _xmlElement,
			String _sAttrName, int _nDefault) {
		String sValue = getAttributeValueTrim(_xmlElement, _sAttrName);
		return Numbers.toInt(sValue, _nDefault);
	}

	/**
	 * get the long integer attribute value
	 * 
	 * @param _xmlElement XML element
	 * @param _sAttrName attribute name
	 * @return Returns the long integer attribute value
	 */
	public final static long getAttributeValueAsLong(Element _xmlElement,
			String _sAttrName) {
		String sValue = getAttributeValueTrim(_xmlElement, _sAttrName);
		return Numbers.toLong(sValue, _sAttrName);
	}

	/**
	 * get the long integer attribute value
	 * 
	 * @param _xmlElement XML element
	 * @param _sAttrName attribute name
	 * @param _lDefault default value
	 * @return Returns the default value if attribute is empty, otherwise the long integer attribute value
	 */
	public final static long getAttributeValueAsLong(Element _xmlElement,
			String _sAttrName, long _lDefault) {
		String sValue = getAttributeValueTrim(_xmlElement, _sAttrName);
		return Numbers.toLong(sValue, _lDefault);
	}

	/**
	 * get the boolean attribute value
	 * 
	 * @param _xmlElement XML element
	 * @param _sAttrName attribute name
	 * @param _bDefault default value
	 * @return Returns the default value if attribute is empty, otherwise returns the boolean attribute value
	 * @throws XmlParserException if encounter errors when converting string to boolean
	 */
	public final static boolean getAttributeValueAsBool(Element _xmlElement,
			String _sAttrName, boolean _bDefault) {
		String sValue = getAttributeValueTrim(_xmlElement, _sAttrName);
		return Numbers.toBoolean(sValue, _bDefault);
	}

	/**
	 * get the boolean content value of child
	 * 
	 * @param _xmlElement XML element
	 * @param _sAttrName attribute name
	 * @return Returns the boolean attribute value
	 */
	public final static boolean getAttributeIntValueAsBool(Element _xmlElement,
			String _sAttrName) {
		String sValue = getAttributeValueTrim(_xmlElement, _sAttrName);
		return Numbers.intToBoolean(sValue, _sAttrName);
	}

	/**
	 * get the boolean attribute value in integer format (like 0/1)
	 * 
	 * @param _xmlElement XML element
	 * @param _sAttrName attribute name
	 * @param _bDefault default value
	 * @return Returns the default value if attribute is empty, otherwise the boolean attribute value
	 */
	public final static boolean getAttributeIntValueAsBool(Element _xmlElement,
			String _sAttrName, boolean _bDefault) {
		String sValue = getAttributeValueTrim(_xmlElement, _sAttrName);
		return Numbers.intToBoolean(sValue, _sAttrName, _bDefault);
	}

	// =============================================================================
	// handling with child element

	/**
	 * get the integer content value of child
	 * 
	 * @param _xmlElement XML element
	 * @param _sChildName child element name
	 * @return Returns the integer content value
	 */
	public final static int getChildTextAsInt(Element _xmlElement, String _sChildName) {
		String sValue = _xmlElement.elementTextTrim(_sChildName);
		return Numbers.toInt(sValue, _sChildName);
	}

	/**
	 * get the integer content value of child
	 * 
	 * @param _xmlElement XML element
	 * @param _sChildName child element name
	 * @param _nDefault default value
	 * @return Returns the default value if content is empty, otherwise the integer content value
	 */
	public final static int getChildTextAsInt(Element _xmlElement, String _sChildName,
			int _nDefault) {
		String sValue = _xmlElement.elementTextTrim(_sChildName);
		return Numbers.toInt(sValue, _nDefault);
	}

	/**
	 * get the long integer content value of child
	 * 
	 * @param _xmlElement XML element
	 * @param _sChildName child element name
	 * @return Returns the long integer content value
	 */
	public final static long getChildTextAsLong(Element _xmlElement, String _sChildName) {
		String sValue = _xmlElement.elementTextTrim(_sChildName);
		return Numbers.toLong(sValue, _sChildName);
	}

	/**
	 * get the long integer content value of child
	 * 
	 * @param _xmlElement XML element
	 * @param _sChildName child element name
	 * @param _lDefault default value
	 * @return Returns the default value if content is empty, otherwise the long integer content value
	 */
	public final static long getChildTextAsLong(Element _xmlElement,
			String _sChildName, long _lDefault) {
		String sValue = _xmlElement.elementTextTrim(_sChildName);
		return Numbers.toLong(sValue, _lDefault);
	}

	/**
	 * get the boolean content value of child
	 * 
	 * @param _xmlElement XML element
	 * @param _sChildName child element name
	 * @param _bDefault default value
	 * @return Returns the default value if content is empty, otherwise returns the boolean content value
	 * @throws XmlParserException if encounter errors when converting string to boolean
	 */
	public final static boolean getChildTextAsBool(Element _xmlElement,
			String _sChildName, boolean _bDefault) {
		String sValue = _xmlElement.elementTextTrim(_sChildName);
		return Numbers.toBoolean(sValue, _bDefault);
	}

	/**
	 * get the boolean content value of child
	 * 
	 * @param _xmlElement XML element
	 * @param _sChildName child element name
	 * @return Returns the boolean content value
	 */
	public final static boolean getChildIntTextAsBool(Element _xmlElement,
			String _sChildName) {
		String sValue = _xmlElement.elementTextTrim(_sChildName);
		return Numbers.intToBoolean(sValue, _sChildName);
	}

	/**
	 * get the boolean content value in integer format (like 0/1) of child
	 * 
	 * @param _xmlElement XML element
	 * @param _sChildName child element name
	 * @param _bDefault default value
	 * @return Returns the default value if content is empty, otherwise the boolean content value
	 */
	public final static boolean getChildIntTextAsBool(Element _xmlElement,
			String _sChildName, boolean _bDefault) {
		String sValue = _xmlElement.elementTextTrim(_sChildName);
		return Numbers.intToBoolean(sValue, _sChildName, _bDefault);
	}

	// =============================================================================
	// transform utilities

	/**
	 * transform XML document
	 * 
	 * @param _xmlDoc XML document to transform
	 * @param _xslFileName XSL document as transformer
	 * @return Returns the XML document transformed
	 * @throws XmlParserException if encounter errors
	 */
	public static Document transformXML(Document _xmlDoc, String _xslFileName,
			String _sEncoding) throws XmlParserException {
		try {
			// load the transformer
			TransformerFactory factory = TransformerFactory.newInstance();
			Source xslSource = new StreamSource(new File(_xslFileName));
			Transformer transformer = factory.newTransformer(xslSource);

			// set output format
			Properties oformat = new Properties();
			oformat.setProperty(OutputKeys.VERSION, "1.0");
			if (_sEncoding != null) {
				oformat.setProperty(OutputKeys.ENCODING, _sEncoding);
			}
			transformer.setOutputProperties(oformat);

			// now lets create the TrAX source and result objects and do the
			// transformation
			Source source = new DocumentSource(_xmlDoc);
			DocumentResult result = new DocumentResult();
			transformer.transform(source, result);

			// output the transformed document
			return result.getDocument();
		} catch (Exception ex) {
			throw new XmlParserException("Faield to transform XML document!", ex);
		}
	}

	/**
	 * Get the trimed text of the element's child element.
	 * 
	 * @param _element the dom4j element
	 * @param _sClildName the naem of the child element.
	 * @return null if _element doesn's exist
	 */
	public static String elementTextTrim(Element _element, String _sClildName) {
		String sEleText = _element.elementText(_sClildName);
		return sEleText == null ? null : sEleText.replaceAll("\\s*\n\\s*", "\n").trim();
	}

	/**
	 * Get the trimed text of the element
	 * 
	 * @param _element the dom4j element
	 * @return empty string if _element has no text.
	 * 
	 */
	public static String getTextTrim(Element _element) {
		return _element.getText().replaceAll("\\s*\n\\s*", "\n").trim();
	}

	/**
	 * Get the value of the attribute that must be required.
	 * 
	 * @param _element
	 * @param _sName
	 * @return the value of the attribute
	 * @throws XmlParserException if the attribute doesn't exist.
	 */
	public static String getRequiredAttributeValue(Element _element, String _sName)
			throws XmlParserException {
		String sValue = _element.attributeValue(_sName);
		if (sValue == null) {
			throw new XmlParserException("element[" + _element.getName() + "] attribute["
					+ _sName + "] required");
		}
		return sValue;
	}

	public static boolean getRequiredAttributeBooleanValue(Element _element,
			String _sName) throws XmlParserException {
		String sRequired = _element.attributeValue(_sName);
		if (sRequired == null) {
			throw new XmlParserException("element[" + _element.getName() + "] attribute["
					+ _sName + "] required");
		}
		sRequired = sRequired.trim().toLowerCase();
		if (sRequired.equals("true")) {
			return true;
		} else if (sRequired.equals("false")) {
			return false;
		} else {
			throw new XmlParserException("element[" + _element.getName() + "] attribute["
					+ _sName + "] must be 'ture' or 'false'");
		}
	}

	@SuppressWarnings("unchecked")
	public static Map getAttributes(Element _element) {
		Map attrs = new HashMap();
		List attrList = _element.attributes();
		for (Iterator iter = attrList.iterator(); iter.hasNext();) {
			Attribute attr = (Attribute) iter.next();
			attrs.put(attr.getName(), attr.getValue());
		}
		return attrs;
	}

	public static Element getElement(Element _element, String _sEleName,
			boolean _bRequired) throws XmlParserException {
		Element ele = _element.element(_sEleName);
		if (_bRequired && ele == null) {
			throw new XmlParserException("element [" + _sEleName + "] required.");
		}
		return ele;
	}

	/**
	 * Element有序排列，相同name的Element也有序排列<br>
	 * Attribute无序排列
	 * 
	 * @param _sXMLString1
	 * @param _sXMLString2
	 * @throws XmlParserException
	 */
	public final static boolean isEquals(String _sXMLString1, String _sXMLString2)
			throws XmlParserException {
		Element expected = XmlUtil.strToElement(_sXMLString1);
		Element actual = XmlUtil.strToElement(_sXMLString2);
		return isEquals(expected, actual);

	}

	/**
	 * Element有序排列，相同name的Element也有序排列<br>
	 * Attribute无序排列
	 * 
	 * @param _element1
	 * @param _element2
	 */
	@SuppressWarnings("unchecked")
	public final static boolean isEquals(Element _element1, Element _element2) {
		if (!_element1.getName().equals(_element2.getName())) {
			if (logger.isDebugEnabled()) {
				logger.debug("element name not equals:" + _element1.getName() + " - "
						+ _element2.getName());
			}
			return false;
		}

		List expectedAttributes = _element1.attributes();
		List actualAttributes = _element2.attributes();
		if (expectedAttributes.size() != actualAttributes.size()) {
			if (logger.isDebugEnabled()) {
				logger.debug("attribute size not equals:" + expectedAttributes.size()//
						+ " - " + actualAttributes.size());
			}
			return false;
		}

		Map expectedNames = new HashMap();
		Map actualNames = new HashMap();
		for (Iterator iter = expectedAttributes.iterator(); iter.hasNext();) {
			Attribute attr = (Attribute) iter.next();
			expectedNames.put(attr.getName(), attr.getValue());
		}
		for (Iterator iter = actualAttributes.iterator(); iter.hasNext();) {
			Attribute attr = (Attribute) iter.next();
			actualNames.put(attr.getName(), attr.getValue());
		}
		if (!expectedNames.equals(actualNames)) {
			if (logger.isDebugEnabled()) {
				logger.debug("attributs not equals:" + expectedNames + " - "
						+ actualNames);
			}
			return false;
		}

		List expectedElements = _element1.elements();
		List actualElements = _element2.elements();
		if (expectedElements.size() != actualElements.size()) {
			if (logger.isDebugEnabled()) {
				logger.debug("element size not equals:" + expectedElements.size()
						+ " - " + actualElements.size());
			}
			return false;
		}

		for (Iterator iter1 = expectedElements.iterator(), iter2 = actualElements
				.iterator(); iter1.hasNext();) {
			Element expecteElement = (Element) iter1.next();
			Element actualElement = (Element) iter2.next();
			if (!isEquals(expecteElement, actualElement))
				return false;
		}
		return true;
	}

	public static String toStringWithoutIllegalChar(Element _element) {
		return _element.asXML().replaceAll("&#\\d+;", "");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
