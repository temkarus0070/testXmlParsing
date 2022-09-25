package org.temkarus0070;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import org.temkarus0070.memory.*;

public class Program {
    public static void main(String[] args) {
        List<String> files = List.of("10", "10h", "23", "25", "50", "75", "602");
        //   List<String> files=List.of("10");
        //NOT ALL //List<MemoryTest> memoryTests=List.of(new JdomMemTestWithXPATH(), new SaxMemTest(),new VtdXmlTest(),new WoodStoxSaxMemTest(),new XomMemTest(),new XpathMemTest());
        List<MemoryTest> memoryTests = List.of(new Dom4jMemTest(), new DomMemTest(), new JdomMemTest(), new JdomMemTestWithXPATH(), new SaxMemTest(), new VtdXmlTest(), new WoodStoxSaxMemTest(), new XomMemTest(), new XpathMemTest());
        Set<String> values = Set.of("Выпуск товаров разрешен", "ЛАСТОЧКИНА АЛИНА ДМИТРИЕВНА");
        //  String tag="catESAD_ru:DecisionCode"; //ПРОБЛЕМЫ С НЕЙМСПЕЙСОМ ПРИ XPATH
        String tag = "DecisionCode";
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        for (String file : files) {
            try (InputStream inputStream = contextClassLoader.getResourceAsStream(String.format("%s.xml", file));) {
                byte[] allBytes = new byte[0];
                try {
                    allBytes = inputStream.readAllBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (MemoryTest memoryTest : memoryTests) {
                    try {
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(allBytes);
                        System.out.println(memoryTest.getClass().getSimpleName());
                        memoryTest.doTest(byteArrayInputStream, values, tag);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
