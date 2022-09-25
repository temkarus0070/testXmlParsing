package org.temkarus0070;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ParseProgram {
    public static int TRIALS_COUNT=3;
    public static void main(String[] args) {
        List<DataFromFile> list=read();

Map<Long,List<TestData>> map=new TreeMap<>();
        List<Long> groupBySize = groupBySize(list);
        for (Long aLong : groupBySize) {
            map.put(aLong,new ArrayList<>());
        }
        List<DataFromFile> testsInJava = list.stream().filter(e -> e.dataName.equals("test")).collect(Collectors.toList());
        for (DataFromFile dataFromFile : testsInJava) {
            long fileSize =  dataFromFile.getLongField("fileSize");
            List<TestData> testData = map.get(fileSize);
            double time = dataFromFile.getField("time");
            String className = dataFromFile.getStrField("className");
            testData.add(new TestData(time,className));
       //     map.put(fileSize,testData);
        }


        Map<Long,List<DataFromFile>>mapGroupBySize=new TreeMap<>();
        for (Long aLong : groupBySize) {
            mapGroupBySize.put(aLong,new ArrayList<>());
        }
       list.stream().filter(e -> e.dataName.equals("testTime")).collect(Collectors.toList()).forEach(e->{
           long fileSizeinKB = e.getLongField("fileSize")/1024;
           List<DataFromFile> list1 = mapGroupBySize.get(fileSizeinKB);
            list1.add(e);
        });


        List<DataFromFile> memoryTestData = list.stream().filter(e -> e.dataName.equals("heap")).collect(Collectors.toList());
        mapGroupBySize.forEach((key,val)->{
            List<DataFromFile> trials=new ArrayList<>();
            DataFromFile dataFromFile = val.get(0);
            for (int i = 0; i < val.size(); i++) {
                 dataFromFile = val.get(i);
                if (trials.size()==TRIALS_COUNT*2){
                    List<LocalTime> interval = getInterval(trials);
                    long memoryUsed = getMemoryUsed(memoryTestData, interval);
                    List<TestData> testsForSize = map.get((dataFromFile.getLongField("fileSize")/1024));
                    Optional<TestData> className = testsForSize.stream().filter(e -> e.className.equals(trials.get(0).getStrField("className")))
                            .findFirst();
                    className.ifPresent(e->{
                        e.maxMemoryUsed=memoryUsed;
                    });
                    trials.clear();
                }
                trials.add(dataFromFile);
            }
            if (trials.size()==TRIALS_COUNT*2){
                List<LocalTime> interval = getInterval(trials);
                long memoryUsed = getMemoryUsed(memoryTestData, interval);
                List<TestData> testsForSize = map.get((dataFromFile.getLongField("fileSize")/1024));
                if (trials.get(0).getStrField("className").equals("XpathMemTest")){
                    System.out.println("keke");
                }
                Optional<TestData> className = testsForSize.stream().filter(e -> e.className.equals(trials.get(0).getStrField("className")))
                        .findFirst();
                className.ifPresent(e->{
                    e.maxMemoryUsed=memoryUsed;
                });
                trials.clear();
            }
        });

        writeInExcel(map);
        System.out.println(map.size());
    }

    public static void writeInExcel(Map<Long,List<TestData>> testData){
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("test");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);

        Row header = sheet.createRow(0);
        Cell cell = header.createCell(0);
        cell.setCellValue("class");
        Cell cell1 = header.createCell(1);
        cell1.setCellValue("fileSize");
        Cell cell2 = header.createCell(2);
        cell2.setCellValue("time");
        Cell cell3 = header.createCell(3);
        cell3.setCellValue("max memory used");
        final int[] index = {1};
        testData.forEach((key,val)->{
            for (TestData dataFromFile : val) {
                Row row = sheet.createRow(index[0]++);
            row.createCell(0).setCellValue(dataFromFile.className);
            row.createCell(1).setCellValue(key);
            row.createCell(2).setCellValue(dataFromFile.timeToParse);
            row.createCell(3).setCellValue(dataFromFile.maxMemoryUsed);
            }
        });
        try {
            FileOutputStream outputStream = new FileOutputStream("excel.xlsx");
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<DataFromFile>read(){
        List<DataFromFile> list=new LinkedList<>();
        try(BufferedReader timeReader=new BufferedReader(new FileReader("time.txt"));
            BufferedReader testDataReader=new BufferedReader(new FileReader("tests.txt"));
            FileReader fileReader=new FileReader("xml jvm data.csv"); ){
            timeReader.lines().forEach(e->{
                DataFromFile e1 = new DataFromFile("testTime",Map.of(1,"period",2,"className",3,"repeat",4,"fileSize",5,"time"));
                String[] strings = e.split(" ");
                strings[3]=strings[3].substring(9);
                for (int i = 0; i < strings.length; i++) {
                    e1.addData(i+1,strings[i]);
                }
                list.add(e1);
            });

            testDataReader.lines().forEach(e->{
                DataFromFile e1 = new DataFromFile("test",Map.of(1,"className",2,"fileSize",3,"time"));
                String[] strings = e.split(" ");
                strings[1]=strings[1].substring(9);
                strings[2]=strings[2].substring(8);
                for (int i = 0; i < strings.length; i++) {
                    e1.addData(i+1,strings[i]);
                }
                list.add(e1);
            });
            Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(fileReader);
            boolean first=true;
            for (CSVRecord record : records) {
                if (first) {
                    first=!first;
                    continue;
                }
                DataFromFile e1 = new DataFromFile("heap",Map.of(1,"time",2,"size",3,"used"));
                String time = record.get(0);
                String size = record.get(1).replace(",","");
                String used = record.get(2).replace(",","");
                e1.addData(1,time);
                e1.addData(2,size);
                e1.addData(3,used);
                list.add(e1);
            }
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }
        return list;
    }

    public static long  getMemoryUsed(List<DataFromFile> listOfMemoryData,List<LocalTime> interval){
        return listOfMemoryData.stream()
                .filter(e -> e.getTime().compareTo(interval.get(0)) >= 0 && e.getTime().compareTo(interval.get(1)) <= 0)
                .mapToLong(e -> e.getLongField("used")).max().getAsLong();
    }

    public static List<LocalTime> getInterval(List<DataFromFile> trials){
        LocalTime localTimeBegin=trials.get(0).getTime();
        LocalTime localTimeEnd=trials.get(trials.size()-1).getTime();
        return List.of(localTimeBegin,localTimeEnd);
    }
    public static List<Long> groupBySize(List<DataFromFile>list){
        return list.stream().filter(e -> e.dataName.equals("test")).map(e -> e.getLongField("fileSize")).distinct().collect(Collectors.toList());
    }
}
