package org.temkarus0070;

import java.util.Set;

public class XPathCalculator {
    public  static String getXpath(Set<String> values){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("//*[");
        for (String value : values) {
            stringBuilder.append(String.format(" text()=\"%s\" or",value));
        }
        stringBuilder.delete(stringBuilder.length()-3,stringBuilder.length());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public  static String getXpath(Set<String> values,String tagName){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("//*[");
        for (String value : values) {
            stringBuilder.append(String.format("text()=\"%s\" or ",value));
        }
        stringBuilder.append(String.format("local-name()=\"%s\"", tagName));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
