package org.temkarus0070.memory;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.dom4j.Node;
import org.temkarus0070.XPathCalculator;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XpathMemTest implements MemoryTest{
    @Override
    public void doTest(InputStream file, Set<String> searchedValues, String tagToCalcCount) throws ParserConfigurationException {
try(file) {
    DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document parse = builder.parse(file);
    NodeList nodesWithValuesAndTag = (NodeList) XPathFactory.newInstance().newXPath().compile(XPathCalculator.getXpath(searchedValues,tagToCalcCount)).evaluate(parse, XPathConstants.NODE);
    printCountWithValues(nodesWithValuesAndTag,searchedValues);
    printCountWithTag(nodesWithValuesAndTag,tagToCalcCount);
}
catch (Exception ex){
    ex.printStackTrace();
}
    }

    private void printCountWithValues(NodeList list, Set<String> values){
        int count=0;
        if (list!=null)
        for (int i = 0; i < list.getLength() ; i++) {
            org.w3c.dom.Node node=list.item(i);
            if (values.contains(node.getNodeValue())){
                count++;
            }
        }
        System.out.println(count);
    }
    private void printCountWithTag(NodeList list,String tag){
        int count=0;
        if (list!=null)
        for (int i = 0; i < list.getLength() ; i++) {
            org.w3c.dom.Node node=list.item(i);
            if (tag.equals(node.getNodeName())){
                count++;
            }
        }
        System.out.println(count);
    }

    @Override
    public double getTestResult() throws IllegalArgumentException {
        return 0;
    }
}
