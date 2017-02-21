/*
 * @(#)JdkXmlApi.java	2011-8-26
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package com.github.javaclub.toolbox.core.xml;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.github.javaclub.toolbox.core.Strings;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A utility class for Xml procesing, just using jdk's api only.
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: JdkXmlApi.java 2011-8-26 10:30:10 Exp $
 */
public class JdkXmlApi {

	static final String LINE_SEPARATER = System.getProperty("line.separator",
			"\r\n");

	private Document doc;
	
	/**
     * Document validation types.
     */
    public static enum VALIDATION_TYPE {
        /**
         * No validation.
         */
        NONE,
        /**
         * DTD based validation.
         */
        DTD,
        /**
         * XSD based validation.
         */
        XSD,
    }
    private static String ELEMENT_NAME_FUNC = "/name()";

    private static XPathFactory xPathFactory = XPathFactory.newInstance();

	public JdkXmlApi() {
		super();
	}

	public JdkXmlApi(String xml) {
		this(new File(xml));
	}

	public JdkXmlApi(File xml) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setExpandEntityReferences(false);
		try {
			doc = factory.newDocumentBuilder().parse(xml);
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to parse XML => "
					+ LINE_SEPARATER + "[" + xml.getAbsolutePath() + "]", e);
		}

	}

	public JdkXmlApi(InputStream xml) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setExpandEntityReferences(false);
		try {
			doc = factory.newDocumentBuilder().parse(xml);
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to parse XML stream => "
					+ LINE_SEPARATER + xml, e);
		}
	}

	public Node rootNode() {
		NodeList list = doc.getChildNodes();
		if(null == list || list.getLength() < 1) {
			return null;
		}
		return list.item(0);
	}

	public Node selectNode(String xpath) {
		javax.xml.xpath.XPath xp = xPathFactory.newXPath();
		try {
			Node node = (Node) xp.evaluate(xpath, doc, XPathConstants.NODE);
			return node;
		} catch (ClassCastException e) {
			throw new IllegalArgumentException(
					"Found result is not a org.w3c.dom.Node implmentation, ensure that you have the right expression which evaluates to Node.");
		} catch (Exception e) {
			throw new XmlParserException(e);
		}
	}

	/**
	 * select Node By Attribute
	 *
	 * @param xpathExp
	 * @param attrName
	 * @param attrValue
	 * @return
	 */
	public Node selectNode(String xpathExp, String attrName, String attrValue) {
		javax.xml.xpath.XPath xpath = xPathFactory.newXPath();
		NodeList nodes = null;
		try {
			nodes = (NodeList) xpath.evaluate(xpathExp, doc,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new IllegalStateException("Failed to get NODESET.", e);
		}
		Node node = null;
		for (int i = 0; i < nodes.getLength(); i++) {
			node = nodes.item(i);
			if (Strings.equals(attrValue, getAttributeValue(node, attrName))) {
				return node;
			}
		}
		return null;
	}

	public String selectText(String xpath) {
		XPathFactory factory = XPathFactory.newInstance();
		javax.xml.xpath.XPath xp = factory.newXPath();
		try {
			Node node = (Node) xp.evaluate(xpath, doc, XPathConstants.NODE);
			return node.getTextContent();
		} catch (ClassCastException e) {
			throw new IllegalArgumentException(
					"Found result is not text, ensure that you have the right expression which evaluates to text.");
		} catch (Exception e) {
			throw new XmlParserException(e);
		}
	}

	// ----------------------------------------------------------------
	// attributes

	/**
	 * Returns a map of all node's attributes. All non-attribute nodes are
	 * ignored.
	 */
	public static Map<String, String> getAllAttributes(Node node) {
		HashMap<String, String> attrs = new HashMap<String, String>();
		NamedNodeMap nmm = node.getAttributes();
		for (int j = 0; j < nmm.getLength(); j++) {
			Node attribute = nmm.item(j);
			if (attribute.getNodeType() != Node.ATTRIBUTE_NODE) {
				continue;
			}
			attrs.put(attribute.getNodeName(), attribute.getNodeValue());
		}
		return attrs;
	}

	/**
	 * Returns attribute value of a node or <code>null</code> if attribute name
	 * not found. Specified attribute is searched on every call. Consider
	 * {@link #getAllAttributes(org.w3c.dom.Node)} for better performances.
	 */
	public static String getAttributeValue(Node node, String attrName) {
		NamedNodeMap nmm = node.getAttributes();
		for (int j = 0; j < nmm.getLength(); j++) {
			Node attribute = nmm.item(j);
			if (attribute.getNodeType() != Node.ATTRIBUTE_NODE) {
				continue;
			}
			String nodeName = attribute.getNodeName();
			if (nodeName.equals(attrName)) {
				return attribute.getNodeValue();
			}
		}
		return null;
	}

	/**
	 * Get element's attribute value or <code>null</code> if attribute not found
	 * or empty.
	 */
	public static String getAttributeValue(Element element, String name) {
		String value = element.getAttribute(name);
		if (value == null || value.length() == 0) {
			value = null;
		}
		return value;
	}

	// ---------------------------------------------------------------- nodelist

	/**
	 * Filters node list by keeping nodes of specified type.
	 */
	public static List filterNodeList(NodeList nodeList, short keepNodeType) {
		return filterNodeList(nodeList, keepNodeType, null);
	}

	/**
	 * Filters node list by keeping nodes of specified type and node name.
	 */
	public static List<Node> filterNodeList(NodeList nodeList,
			short keepNodeType, String nodeName) {
		List<Node> nodes = new ArrayList<Node>();
		for (int k = 0; k < nodeList.getLength(); k++) {
			Node node = nodeList.item(k);
			if (node.getNodeType() != keepNodeType) {
				continue;
			}
			if (nodeName != null
					&& (node.getNodeName().equals(nodeName) == false)) {
				continue;
			}
			nodes.add(node);
		}
		return nodes;
	}

	/**
	 * Filter node list for all Element nodes.
	 */
	public static List filterNodeListElements(NodeList nodeList) {
		return filterNodeListElements(nodeList, null);
	}

	/**
	 * Filter node list for Element nodes of specified name.
	 */
	public static List<Node> filterNodeListElements(NodeList nodeList, String nodeName) {
		List<Node> nodes = new ArrayList<Node>();
		for (int k = 0; k < nodeList.getLength(); k++) {
			Node node = nodeList.item(k);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			if (nodeName != null && (node.getNodeName().equals(nodeName) == false)) {
				continue;
			}
			nodes.add(node);
		}
		return nodes;
	}

	/**
	 * Returns a list of all child Elements,
	 */
	public static List getChildElements(Node node) {
		return getChildElements(node, null);
	}

	/**
	 * Returns a list of child Elements of specified name.
	 */
	public static List getChildElements(Node node, String nodeName) {
		NodeList childs = node.getChildNodes();
		return filterNodeListElements(childs, nodeName);
	}

	// ---------------------------------------------------------------- node

	/**
	 * Returns value of first available child text node or <code>null</code> if
	 * not found.
	 */
	public static String getFirstChildTextNodeValue(Node node) {
		NodeList children = node.getChildNodes();
		int len = children.getLength();
		for (int i = 0; i < len; i++) {
			Node n = children.item(i);
			if (n.getNodeType() == Node.TEXT_NODE) {
				return n.getNodeValue();
			}
		}
		return null;
	}

	/**
	 * Returns value of single child text node or <code>null</code>.
	 */
	public static String getChildTextNodeValue(Node node) {
		if (node.getChildNodes().getLength() != 1) {
			return null;
		}
		Node item0 = node.getChildNodes().item(0);
		if (item0.getNodeType() != Node.TEXT_NODE) {
			return null;
		}
		return item0.getNodeValue();
	}
	
	/**
     * Get the String data associated with the XPath selection supplied.
     *
     * @param node  The node to be searched.
     * @param xpath The XPath String to be used in the selection.
     * @return The string data located at the specified location in the
     *         document, or an empty string for an empty resultset query.
     */
    public static String getString(Node node, String xpath) {
        NodeList nodeList = getNodeList(node, xpath);

        if (nodeList == null || nodeList.getLength() == 0) {
            return "";
        }

        if (xpath.endsWith(ELEMENT_NAME_FUNC)) {
            if (nodeList.getLength() > 0) {
                return nodeList.item(0).getNodeName();
            } else {
                return "";
            }
        } else {
            return serialize(nodeList);
        }
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     * <p/>
     * The output is unformatted.
     *
     * @param nodeList The DOM subtree as a NodeList.
     * @return The subtree in serailised form.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static String serialize(NodeList nodeList) throws DOMException {
        return serialize(nodeList, false);
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     *
     * @param node The DOM node to be serialized.
     * @param format Format the output.
     * @return The subtree in serailised form.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static String serialize(final Node node, boolean format) throws DOMException {
        StringWriter writer = new StringWriter();
        serialize(node, format, writer);
        return writer.toString();
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     *
     * @param node The DOM node to be serialized.
     * @param format Format the output.
     * @param writer The target writer for serialization.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static void serialize(final Node node, boolean format, Writer writer) throws DOMException {
        if(node.getNodeType() == Node.DOCUMENT_NODE) {
            serialize(node.getChildNodes(), format, writer);
        } else {
            serialize(new NodeList() {
                public Node item(int index) {
                    return node;
                }

                public int getLength() {
                    return 1;
                }
            }, format, writer);
        }
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     *
     * @param nodeList The DOM subtree as a NodeList.
     * @param format Format the output.
     * @return The subtree in serailised form.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static String serialize(NodeList nodeList, boolean format) throws DOMException {
        StringWriter writer = new StringWriter();
        serialize(nodeList, format, writer);
        return writer.toString();
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     *
     * @param nodeList The DOM subtree as a NodeList.
     * @param format Format the output.
     * @param writer The target writer for serialization.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static void serialize(NodeList nodeList, boolean format, Writer writer) throws DOMException {

        if (nodeList == null) {
            throw new IllegalArgumentException(
                    "null 'subtree' NodeIterator arg in method call.");
        }

        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer;

            if(format) {
                try {
                    factory.setAttribute("indent-number", new Integer(4));
                } catch(Exception e) {
                    // Ignore... Xalan may throw on this!!
                    // We handle Xalan indentation below (yeuckkk) ...
                }
            }
            transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            if(format) {
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");
            }

            int listLength = nodeList.getLength();

            // Iterate through the Node List.
            for (int i = 0; i < listLength; i++) {
                Node node = nodeList.item(i);

                if (isTextNode(node)) {
                    writer.write(node.getNodeValue());
                } else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                    writer.write(((Attr) node).getValue());
                } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                    transformer.transform(new DOMSource(node), new StreamResult(writer));
                }
            }
        } catch (Exception e) {
            DOMException domExcep = new DOMException(
                    DOMException.INVALID_ACCESS_ERR,
                    "Unable to serailise DOM subtree.");
            domExcep.initCause(e);
            throw domExcep;
        }
    }

    /**
     * Is the supplied W3C DOM Node a text node.
     *
     * @param node The node to be tested.
     * @return True if the node is a text node, otherwise false.
     */
    public static boolean isTextNode(Node node) {
        short nodeType;

        if (node == null) {
            return false;
        }
        nodeType = node.getNodeType();

        return nodeType == Node.CDATA_SECTION_NODE
                || nodeType == Node.TEXT_NODE;
    }

    /**
     * Get the W3C NodeList instance associated with the XPath selection
     * supplied.
     *
     * @param node  The document node to be searched.
     * @param xpath The XPath String to be used in the selection.
     * @return The W3C NodeList instance at the specified location in the
     *         document, or null.
     */
    public static NodeList getNodeList(Node node, String xpath) {
        if (node == null) {
            throw new IllegalArgumentException(
                    "null 'document' arg in method call.");
        } else if (xpath == null) {
            throw new IllegalArgumentException(
                    "null 'xpath' arg in method call.");
        }
        try {
            XPath xpathEvaluater = xPathFactory.newXPath();

            if (xpath.endsWith(ELEMENT_NAME_FUNC)) {
                return (NodeList) xpathEvaluater.evaluate(xpath.substring(0,
                        xpath.length() - ELEMENT_NAME_FUNC.length()), node,
                        XPathConstants.NODESET);
            } else {
                return (NodeList) xpathEvaluater.evaluate(xpath, node,
                        XPathConstants.NODESET);
            }
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException("bad 'xpath' expression ["
                    + xpath + "].");
        }
    }

	public static void main(String[] args) {
		String xml = "D:/tmp/jdbc.cfg.xml";
		String txt = new JdkXmlApi(xml).selectText("//connections/connection[2]/property[3]/text()");
		/*String expr = "//constant";
		Node node = new JdkXmlApi(xml).selectNode(expr, "name", "show_sql");
		System.out.println(node.getAttributes().getNamedItem("value").getTextContent());
		Node root = new JdkXmlApi(xml).rootNode();*/
		System.out.println(txt);
	}
}
