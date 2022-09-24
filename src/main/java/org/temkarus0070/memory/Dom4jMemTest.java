package org.temkarus0070.memory;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.temkarus0070.XPathCalculator;

public class Dom4jMemTest implements MemoryTest{
    @Override
    public void doTest(InputStream file, Set<String> searchedValues, String tagToCalcCount) throws ParserConfigurationException {
        try(file) {
            SAXReader saxReader=new SAXReader();
            org.dom4j.Document document = saxReader.read(file);
            List<Node> nodesWithValuesAndTag = document.selectNodes(XPathCalculator.getXpath(searchedValues,tagToCalcCount));
            printCountWithValues(nodesWithValuesAndTag,searchedValues);
            printCountWithTag(nodesWithValuesAndTag,tagToCalcCount);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

private void printCountWithValues(List<Node> list,Set<String> values){
        int count=0;
    for (Node node : list) {
        if (values.contains(node.getStringValue())){
            count++;
        }
    }
    System.out.println(count);
}
private void printCountWithTag(List<Node> list,String tag){
    int count=0;
    for (Node node : list) {
        if (node.getName().equals(tag)){
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
