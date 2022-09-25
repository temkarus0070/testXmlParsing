package org.temkarus0070;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class DataFromFile {
    public String dataName="";
    public Map<String,String> map=new HashMap<>();

    public Map<Integer,String> fieldsName=new HashMap<>();

    public DataFromFile(String dataName, Map<Integer, String> fieldsName) {
        this.dataName = dataName;
        this.fieldsName = fieldsName;
    }

    public void addData(int index,String value){
map.put(fieldsName.get(index),value);
    }

    public double getField(String name){
        return Double.parseDouble(map.get(name));
    }
    public String getStrField(String name){
        return map.get(name);
    }

    public LocalTime getTime(){
        String date = map.get("time");
        if (date.matches(".*[A-Zaz].*")) {
            int indexOfPm = date.indexOf("PM");
            date=date.substring(0,indexOfPm-1);
            int hour = date.indexOf(":");
            int minute=hour+1;
            int seconds=date.indexOf(":",minute);
            int dotPlace=date.indexOf(".");
            return LocalTime.of(getHour(Integer.parseInt(date.substring(0,hour))),Integer.parseInt(date.substring(minute,seconds)),
                    Integer.parseInt(date.substring(seconds+1,dotPlace)),
                    Integer.parseInt(date.substring(dotPlace+1))*1000000);
        }
        else {
return LocalTime.parse(date);
        }
    }

    private int getHour(int hour){
        return 12+hour;
    }

    public long getLongField(String name) {
        return Long.parseLong(map.get(name));
    }
}
