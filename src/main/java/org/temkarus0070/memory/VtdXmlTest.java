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
            int tagCount=0;
            int valueCount=0;
            AutoPilot ap = new AutoPilot();
            ap.selectXPath(XPathCalculator.getXpath(searchedValues,tagToCalcCount));
            VTDGen vg = new VTDGen();
            vg.setDoc(file.readAllBytes());
            VTDNav nav = vg.getNav();
          int i=0;
            ap.bind(nav);
            while((i=ap.evalXPath())!=-1){
                if (nav.toString(nav.getCurrentIndex()).equals(tagToCalcCount)) {
                    tagCount++;
                }
                String value = nav.toString(nav.getText());
                if (searchedValues.contains(value)){
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
