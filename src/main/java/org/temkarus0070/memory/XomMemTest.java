package org.temkarus0070.memory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import nu.xom.*;
import org.temkarus0070.XPathCalculator;

public class XomMemTest implements MemoryTest{
   private long result=-1;
    @Override
    public void doTest(InputStream file, Set<String> searchedValues, String tagToCalcCount) throws ParserConfigurationException {
        try(file) {
            long begin=System.nanoTime();
            Builder parser = new Builder(false);
            Document doc = null;
                doc = parser.build(file);
            Nodes elementsWithSearchedValues = doc.getRootElement().query(XPathCalculator.getXpath(searchedValues,tagToCalcCount));
           printValuesAndTagCount(elementsWithSearchedValues,searchedValues,tagToCalcCount);
           long end=System.nanoTime();
           result=end-begin;
        } catch (IOException | ParsingException ioException) {
            ioException.printStackTrace();
        }
    }

    public void printValuesAndTagCount(Nodes nodes,Set<String> values,String tag){
        int valuesCount=0;
        int tagCount=0;
        if (nodes!=null)
        for (int i = 0; i < nodes.size() ; i++) {
            Element node =(Element) nodes.get(i);
        if (values.contains(node.getValue())){
            valuesCount++;
        }
            String localName = node.getLocalName();
           if (localName.equals(tag))
               tagCount++;
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
