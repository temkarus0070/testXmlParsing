package org.temkarus0070.memory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.dom4j.Node;
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
    @Override
    public void doTest(InputStream file, Set<String> searchedValues, String tagToCalcCount) throws ParserConfigurationException {
        try(file){
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(file);

            XPathExpression<Element> xPathForValuesAndTagSearch= XPathFactory.instance().compile(XPathCalculator.getXpath(searchedValues,tagToCalcCount),
                    Filters.element(),null,List.of(Namespace.XML_NAMESPACE));
            List<Element> evaluateValues = xPathForValuesAndTagSearch.evaluate(document);
            printCountWithValues(evaluateValues,searchedValues);
            printCountWithTag(evaluateValues,tagToCalcCount);
        }
        catch (IOException | JDOMException e){
            e.printStackTrace();
        }
    }

    private void printCountWithValues(List<Element> list, Set<String> values){
        int count=0;
        for (Element node : list) {
            if (values.contains(node.getValue())){
                count++;
            }
        }
        System.out.println(count);
    }
    private void printCountWithTag(List<Element> list,String tag){
        int count=0;
        for (Element node : list) {
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
