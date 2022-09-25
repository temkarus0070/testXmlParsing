package org.temkarus0070.memory;

import java.io.InputStream;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.temkarus0070.XPathCalculator;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XpathMemTest implements MemoryTest{
    long result=-1;
    @Override
    public void doTest(InputStream file, Set<String> searchedValues, String tagToCalcCount) throws ParserConfigurationException {
try(file) {
    long begin=System.nanoTime();
    DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document parse = builder.parse(file);
    NodeList nodesWithValuesAndTag = (NodeList) XPathFactory.newInstance().newXPath()
            .compile(XPathCalculator.getXpath(searchedValues, tagToCalcCount))
            .evaluate(parse, XPathConstants.NODESET);
    printCountWithValuesAndTagCount(nodesWithValuesAndTag, searchedValues,tagToCalcCount);
    long end=System.nanoTime();
    result=end-begin;
}
catch (Exception ex){
    ex.printStackTrace();
}
    }

    private void printCountWithValuesAndTagCount(NodeList list, Set<String> values, String tag){
        int valuesCount=0;
        int tagCount=0;
        if (list!=null)
        for (int i = 0; i < list.getLength() ; i++) {
            org.w3c.dom.Node node = list.item(i);
            String textContent = node.getTextContent();
            if (values.contains(textContent)) {
                valuesCount++;
            }
            String nodeName = node.getNodeName();
            int prefixIndex = nodeName.indexOf(":");
            if (tag.equals(nodeName.substring(prefixIndex == -1 ? 0 : prefixIndex + 1))) {
                tagCount++;
            }
        }
        System.out.println(valuesCount);
        System.out.println(tagCount);
    }


    @Override
    public long getTestResult() throws IllegalArgumentException {
        if (result==-1)
            throw  new IllegalArgumentException();
        return result;
    }
}
