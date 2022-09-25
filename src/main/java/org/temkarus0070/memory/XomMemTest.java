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
            Builder parser = new Builder(true);
            Document doc = null;
            try {
                doc = parser.build(file);
            } catch (nu.xom.ValidityException validityException) {
                if (!validityException.getMessage().contains("Document root element \"Envelope\", must match DOCTYPE root")) {
                    validityException.printStackTrace();
                }
                doc = validityException.getDocument();
            } catch (nu.xom.ParsingException parsingException) {
               parsingException.printStackTrace();
            }
            Nodes elementsWithSearchedValues = doc.getRootElement().query(XPathCalculator.getXpath(searchedValues,tagToCalcCount));
           printValuesAndTagCount(elementsWithSearchedValues,searchedValues,tagToCalcCount);
           long end=System.nanoTime();
           result=end-begin;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
