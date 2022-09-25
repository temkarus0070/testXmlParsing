package org.temkarus0070.memory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

//JDOM 2/0/6/1 имеет 4 уязвимости из зависимостей
public class JdomMemTest implements MemoryTest{
    long result=-1;
    @Override
    public void doTest(InputStream file, Set<String> searchedValues, String tagToCalcCount) throws ParserConfigurationException {
try(file){
    long begin=System.nanoTime();
    SAXBuilder saxBuilder = new SAXBuilder();
    Document document = saxBuilder.build(file);
checkValuesAndTags(searchedValues,document.getRootElement(),tagToCalcCount);
long end=System.nanoTime();
result=end-begin;
}
catch (IOException | JDOMException e){
    e.printStackTrace();
}
    }

    private void checkValuesAndTags(Set<String> values, org.jdom2.Element root, String tag){
        LinkedList<Element>stack=new LinkedList<>();
        int valuesCount=0;
        int tagsCount=0;
        stack.add(root);
        while (!stack.isEmpty()) {
            Element element = stack.removeLast();
            if (values.contains(element.getValue())){
                valuesCount++;
            }
            if (element.getName().equals(tag)){
                tagsCount++;
            }
            List<Element> children = element.getChildren();
        stack.addAll(children);
        }
        System.out.println(valuesCount);
        System.out.println(tagsCount);
    }

    @Override
    public long getTestResult() throws IllegalArgumentException {
        if (result==-1)
            throw  new IllegalArgumentException();
        return result;
    }
}
