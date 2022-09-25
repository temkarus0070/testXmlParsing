package org.temkarus0070.memory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.temkarus0070.XPathCalculator;

public class JdomMemTestWithXPATH implements MemoryTest{
    long result=-1;
    @Override
    public void doTest(InputStream file, Set<String> searchedValues, String tagToCalcCount) throws ParserConfigurationException {
        try(file){
          long begin=System.nanoTime();
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(file);

            XPathExpression<Element> xPathForValuesAndTagSearch= XPathFactory.instance().compile(XPathCalculator.getXpath(searchedValues,tagToCalcCount),
                    Filters.element(),null,List.of(Namespace.XML_NAMESPACE));
            List<Element> evaluateValues = xPathForValuesAndTagSearch.evaluate(document);
            printCountWithValuesAndTagCount(evaluateValues,searchedValues,tagToCalcCount);
            long end = System.nanoTime();
            result=end-begin;
        }
        catch (IOException | JDOMException e){
            e.printStackTrace();
        }
    }

    private void printCountWithValuesAndTagCount(List<Element> list, Set<String> values,String tag){
        int valuesCount=0;
        int tagCount=0;
        for (Element node : list) {
            if (values.contains(node.getValue())){
                valuesCount++;
            }
            if (node.getName().equals(tag)){
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
