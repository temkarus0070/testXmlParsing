package org.temkarus0070.memory;

import java.io.InputStream;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;

public interface MemoryTest {
    public void doTest(InputStream file, Set<String> searchedValues, String tagToCalcCount) throws ParserConfigurationException;
    public double getTestResult() throws IllegalArgumentException;
}
