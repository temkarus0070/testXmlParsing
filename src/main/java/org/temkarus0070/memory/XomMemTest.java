package org.temkarus0070.memory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import nu.xom.*;
import org.temkarus0070.XPathCalculator;

public class XomMemTest implements MemoryTest{
    @Override
    public void doTest(InputStream file, Set<String> searchedValues, String tagToCalcCount) throws ParserConfigurationException {
        try(file){
            Builder parser = new Builder(true);
            Document doc = parser.build(file);
            Nodes elementsWithSearchedValues = doc.getRootElement().query(XPathCalculator.getXpath(searchedValues));
            if(elementsWithSearchedValues.size()>0){
                Element element = (Element) elementsWithSearchedValues.get(0);
                System.out.println(element);
            }
            System.out.println(elementsWithSearchedValues.size());
            Nodes elementsWithTag = doc.getRootElement().query(".//" + tagToCalcCount);
            System.out.println(elementsWithTag.size());
        }
        catch (IOException | ParsingException ioException){
            ioException.printStackTrace();
        }
    }

    @Override
    public double getTestResult() throws IllegalArgumentException {
        return 0;
    }
}
