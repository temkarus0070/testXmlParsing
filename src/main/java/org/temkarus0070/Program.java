package org.temkarus0070;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.temkarus0070.memory.*;

public class Program {
    public static void main(String[] args) {
        List<String> files=List.of("10","10h","23","25","50","60","75","602");
    List<MemoryTest> memoryTests=List.of(new JdomMemTestWithXPATH(),
            new SaxMemTest(),new VtdXmlTest(),new WoodStoxSaxMemTest(),new XomMemTest(),new XpathMemTest());
    //List<MemoryTest> memoryTests=List.of(new Dom4jMemTest(),new DomMemTest(),new JdomMemTest(),new JdomMemTestWithXPATH(), new SaxMemTest(),new VtdXmlTest(),new WoodStoxSaxMemTest(),new XomMemTest(),new XpathMemTest());
        Set<String> values=Set.of("Выпуск товаров разрешен","ЛАСТОЧКИНА АЛИНА ДМИТРИЕВНА");
        String tag="catESAD_ru:DecisionCode";
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        for (String file : files) {
            InputStream inputStream = contextClassLoader.getResourceAsStream(String.format("%s.xml", file));
            byte[] allBytes = new byte[0];
            try {
                allBytes = inputStream.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (MemoryTest memoryTest : memoryTests) {
                try {
                    ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(allBytes);
                    memoryTest.doTest(byteArrayInputStream,values,tag);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
