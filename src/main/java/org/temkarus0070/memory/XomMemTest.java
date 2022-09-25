package org.temkarus0070.memory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Nodes;
import org.temkarus0070.XPathCalculator;

public class XomMemTest implements MemoryTest{
    @Override
    public void doTest(InputStream file, Set<String> searchedValues, String tagToCalcCount) throws ParserConfigurationException {
        try(file) {
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
                if (parsingException.getMessage().contains("Illegal path character")) {
                    System.out.println();
                }
            }
            Nodes elementsWithSearchedValues = doc.getRootElement().query(XPathCalculator.getXpath(searchedValues));
            System.out.println(elementsWithSearchedValues.size());
            //XPathContext xPathContext = new XPathContext();
            // xPathContext.addNamespace("catESAD_ru", "urn:customs.ru:RUCommonAggregateTypes:5.14.3");
            Nodes elementsWithTag = doc.getRootElement().query(String.format(".//*[local-name()='%s']", tagToCalcCount));
            //   Nodes elementsWithTag = doc.getRootElement().query(String.format(".//%s" , tagToCalcCount),xPathContext);
            System.out.println(elementsWithTag.size());
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

    @Override
    public double getTestResult() throws IllegalArgumentException {
        return 0;
    }
}
