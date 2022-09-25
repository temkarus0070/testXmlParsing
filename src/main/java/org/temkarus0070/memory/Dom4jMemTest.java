package org.temkarus0070.memory;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.temkarus0070.XPathCalculator;

public class Dom4jMemTest implements MemoryTest{
   private  long result=-1;
    @Override
    public void doTest(InputStream file, Set<String> searchedValues, String tagToCalcCount) throws ParserConfigurationException {
        try(file) {
            long begin=System.nanoTime();
            SAXReader saxReader=new SAXReader();
            org.dom4j.Document document = saxReader.read(file);
            List<Node> nodesWithValuesAndTag = document.selectNodes(XPathCalculator.getXpath(searchedValues,tagToCalcCount));
            printCountWithValuesAndTagCount(nodesWithValuesAndTag,searchedValues,tagToCalcCount);
            long end=System.nanoTime();
        result=end-begin;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

private void printCountWithValuesAndTagCount(List<Node> list, Set<String> values,String tag){
        int valueCount=0;
    int tagCount=0;
    for (Node node : list) {
        if (values.contains(node.getStringValue())){
            valueCount++;
        }
        if (node.getName().equals(tag)){
            tagCount++;
        }
    }
    System.out.println(valueCount);
    System.out.println(tagCount);
}

    @Override
    public long getTestResult() throws IllegalArgumentException {
        if (result==-1)
            throw new IllegalArgumentException();
        return result;
    }
}
