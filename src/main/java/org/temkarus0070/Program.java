package org.temkarus0070;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import org.temkarus0070.memory.*;

public class Program {
    public static void main(String[] args) {
        //   List<String> files = List.of("10", "10h", "23", "25", "50", "75", "602");
        List<String> files = List.of("10", "23", "25", "50", "75", "602");
        //   List<String> files=List.of("10");
        //NOT ALL //List<MemoryTest> memoryTests=List.of(new JdomMemTestWithXPATH(), new SaxMemTest(),new VtdXmlTest(),new WoodStoxSaxMemTest(),new XomMemTest(),new XpathMemTest());
       // List<MemoryTest> memoryTests = List.of(new Dom4jMemTest(), new DomMemTest(), new JdomMemTest(), new JdomMemTestWithXPATH(), new SaxMemTest(), new VtdXmlTest(), new WoodStoxSaxMemTest(), new XomMemTest(), new XpathMemTest());
        List<MemoryTest> memoryTests = List.of(new StaxMemTest());
        Set<String> values = Set.of("Выпуск товаров разрешен", "ЛАСТОЧКИНА АЛИНА ДМИТРИЕВНА");
        //  String tag="catESAD_ru:DecisionCode"; //ПРОБЛЕМЫ С НЕЙМСПЕЙСОМ ПРИ XPATH
        String tag = "DecisionCode";
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        doTests(files.subList(0, 2), values, tag, memoryTests, contextClassLoader, 1, null, null);
        System.out.println("end prepare");
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("BEGIN TESTS!!!");
        try (FileWriter fileWriter = new FileWriter("tests.txt"); FileWriter timeWritter = new FileWriter("time.txt")) {
            doTests(files, values, tag, memoryTests, contextClassLoader, 1, fileWriter, timeWritter);
            fileWriter.flush();
            timeWritter.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    private static void doTests(List<String> files, Set<String> values, String tag, List<MemoryTest> memoryTests, ClassLoader contextClassLoader, int repeatCount, FileWriter fileWriter, FileWriter timeWritter) {
        for (String file : files) {
            String fileName = String.format("%s.xml", file);
            System.out.println(fileName);
            try (InputStream inputStream = contextClassLoader.getResourceAsStream(fileName);) {
                byte[] allBytes = new byte[0];
                try {
                    allBytes = inputStream.readAllBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (MemoryTest memoryTest : memoryTests) {
                    try {
                        long result = 0;
                        for (int i = 0; i < repeatCount; i++) {
                            System.out.println(memoryTest.getClass().getSimpleName()+" "+i);
                            if (timeWritter != null) {
                                timeWritter.write(String.format("BEGIN %s %d fileSize=%d %s\n", memoryTest.getClass().getSimpleName(), i,
                                        allBytes.length, LocalTime.now().toString()));
                            }
                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(allBytes);
                            memoryTest.doTest(byteArrayInputStream, values, tag);
                            result += memoryTest.getTestResult();
                            if (fileWriter != null)
                                Thread.sleep(1);
                            if (timeWritter != null) {
                                timeWritter.write(String.format("END %s %d fileSize=%d %s\n", memoryTest.getClass().getSimpleName(), i,
                                        allBytes.length, LocalTime.now().toString()));
                            }
                        }
                        if (fileWriter != null)
                            fileWriter.write(String.format("%s fileSize=%d avgTime=%f \n",
                                    memoryTest.getClass().getSimpleName(), allBytes.length / 1024, (result / repeatCount) * 0.001));

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
