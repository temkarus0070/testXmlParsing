package org.temkarus0070.memory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class StaxMemTest implements MemoryTest{
    long result=-1;
    @Override
    public void doTest(InputStream file, Set<String> searchedValues, String tagToCalcCount) throws ParserConfigurationException {
try(file) {
    long tagCount=0;
    long valuesCount=0;
    long begin=System.nanoTime();
    XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
    XMLEventReader reader = xmlInputFactory.createXMLEventReader(file);
    while (reader.hasNext()) {
        XMLEvent nextEvent = reader.nextEvent();
        if (nextEvent.isStartElement()){
            StartElement startElement = nextEvent.asStartElement();
            String localPart = startElement.getName().getLocalPart();
            if (localPart.equals(tagToCalcCount))
                tagCount++;
        }
        else if (nextEvent.isCharacters()){
            Characters characters = nextEvent.asCharacters();
            if (searchedValues.contains(characters.getData())) {
                valuesCount++;
            }
        }
    }
    System.out.println(valuesCount);
    System.out.println(tagCount);
    long end = System.nanoTime();
    result=end-begin;
} catch (XMLStreamException | IOException e) {
    e.printStackTrace();
}
    }

    @Override
    public long getTestResult() throws IllegalArgumentException {
        if (result==-1)
            throw  new IllegalArgumentException();
        return result;
    }
}
