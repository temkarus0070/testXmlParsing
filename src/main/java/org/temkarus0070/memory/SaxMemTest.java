package org.temkarus0070.memory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxMemTest implements MemoryTest{
    long result=-1;
    @Override
    public void doTest(InputStream file, Set<String> searchedValues, String tagToCalcCount) throws ParserConfigurationException {
        try(file){
            long begin=System.nanoTime();
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            UserHandler userHandler = new UserHandler(searchedValues, tagToCalcCount);
            saxParser.parse(file,userHandler);
            long end=System.nanoTime();
            result=end-begin;
        }
        catch (IOException | SAXException ioException){
            ioException.printStackTrace();
        }
    }

    @Override
    public long getTestResult() throws IllegalArgumentException {
        if (result==-1)
            throw  new IllegalArgumentException();
        return result;
    }
}

class UserHandler extends DefaultHandler{
    Set<String> searchedValues;
    String tagToCalcCount;

    int tagCount=0;
    int valuesCount=0;

    public UserHandler(Set<String> searchedValues, String tagToCalcCount) {
        this.searchedValues = searchedValues;
        this.tagToCalcCount = tagToCalcCount;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        int prefixIndex = qName.indexOf(":");
        if (qName.substring(prefixIndex == -1 ? 0 : prefixIndex + 1).equals(tagToCalcCount))
            tagCount++;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String s = String.copyValueOf(ch, start, length);
        if (searchedValues.contains(s)) {
            valuesCount++;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println(valuesCount);
        System.out.println(tagCount);
    }
}
