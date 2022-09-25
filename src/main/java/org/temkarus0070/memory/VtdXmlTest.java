package org.temkarus0070.memory;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import java.io.InputStream;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.temkarus0070.XPathCalculator;

public class VtdXmlTest implements MemoryTest{
    @Override
    public void doTest(InputStream file, Set<String> searchedValues, String tagToCalcCount) throws ParserConfigurationException {
        try(file) {
            int tagCount = 0;
            int valueCount = 0;

            VTDGen vg = new VTDGen();
            vg.setDoc(file.readAllBytes());
            vg.parse(true);
            VTDNav nav = vg.getNav();
            AutoPilot ap = new AutoPilot(nav);
            int i = -1;
            ap.selectXPath(XPathCalculator.getXpath(searchedValues, tagToCalcCount));
            while ((i = ap.evalXPath()) != -1) {
                String s = nav.toString(i);
                int prefixIndex = s.indexOf(":");
                if (s.substring(prefixIndex == -1 ? 0 : prefixIndex + 1).equals(tagToCalcCount)) {
                    tagCount++;
                }
                String value = nav.toString(i + 1);
                if (searchedValues.contains(value)) {
                    valueCount++;
                }

            }
            System.out.println(valueCount);
            System.out.println(tagCount);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public double getTestResult() throws IllegalArgumentException {
        return 0;
    }
}
