package com.api.utils;

import io.cucumber.datatable.DataTable;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class XMLUtil {


    protected static final String FILEPATH = "src/test/resources/req-res/";

    public static String buildRequestBody(String requestFileName, DataTable dataTable) {
        Document updatedDocument;

        String fullPath = FILEPATH + requestFileName.replaceAll("^\"|\"$", "") + ".xml";


        try {
            Document doc = loadXmlFile(fullPath);
            updatedDocument = updateXml(dataTable, doc);
           System.out.println(getXmlString(updatedDocument));
            saveXmlFile(doc, FILEPATH + "//"+ "order_updated.xml");
        return getXmlString(updatedDocument);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static Document loadXmlFile(String xmlFilePath) throws Exception {
        File file = new File(xmlFilePath);
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        return doc;
    }



    private static Document updateXml(DataTable table , Document doc) throws Exception {

        XPath xpath = XPathFactory.newInstance().newXPath();
        List<Map<String, String>> updates = table.asMaps(String.class, String.class);

        for (Map<String, String> row : updates) {
            String xpathExpression = row.get("XPath");
            String attributeName = row.get("Attribute");
            String newValue = row.get("NewValue");

            // Find the XML Node using XPath
            Node node = (Node) xpath.evaluate(xpathExpression, doc, XPathConstants.NODE);
            if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (element.hasAttribute(attributeName)) {
                    element.setAttribute(attributeName, newValue);
                } else {
                    System.out.println("Attribute not found: " + attributeName);
                }
            } else {
                System.out.println("XPath not found: " + xpathExpression);
            }
        }
        return doc;
    }

    public static void saveXmlFile(String outputFilePath, Document doc) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(outputFilePath));
        transformer.transform(source, result);

        System.out.println("XML saved successfully at: " + outputFilePath);
    }

    private static String getXmlString(Document document) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.toString();
    }

    public static void saveXmlFile(Document doc,String outputFilePath) throws Exception {
        String updatedXml = getXmlString(doc);
        // Save XML to file
        FileWriter fileWriter = new FileWriter(outputFilePath);
        fileWriter.write(updatedXml);
        fileWriter.close();

        System.out.println("XML saved successfully at: " + outputFilePath);
    }


}

