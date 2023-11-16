package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ParserToJson {
    static List<Employee> forJsonFromCsv;
    static List<Employee> forJsonFromXml = new ArrayList<>();

    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String fileName2 = "data.xml";
        List<Employee> list2 = parseXML(fileName2);
        String fileName3 = "data.json";
        readJson(fileName3);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            forJsonFromCsv = csv.parse();
            String json = listToJson(forJsonFromCsv);
            String fileNameResult = "data.json";
            writeString(json, fileNameResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return forJsonFromCsv;
    }

    public static List<Employee> parseXML(String fileName2) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName2));

            NodeList nodeList = doc.getElementsByTagName("employee");
            for (int i = 0; i < nodeList.getLength(); i++) {
                forJsonFromXml.add(getEmployee(nodeList.item(i)));
            }
            String json = listToJson(forJsonFromXml);
            String fileNameResult = "data2.json";
            writeString(json, fileNameResult);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return forJsonFromXml;
    }

    public static Employee getEmployee(Node node) {
        Employee employee = new Employee();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            employee.setId(Integer.parseInt(getTagValue("id", element)));
            employee.setFirstName(getTagValue("firstName", element));
            employee.setLastName(getTagValue("lastName", element));
            employee.setCountry(getTagValue("country", element));
            employee.setAge(Integer.parseInt(getTagValue("age", element)));
        }
        return employee;
    }

    public static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    public static String listToJson(List<Employee> staff) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .setPrettyPrinting()
                .create();

        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(staff, listType);
        return json;
    }

    public static void writeString(String json, String nameResultFile) {
        try (FileWriter file = new FileWriter(nameResultFile)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readJson(String fileName) {
        String json = null;
        JSONParser parser = new JSONParser();
        try {
            JSONArray employee = (JSONArray) parser.parse(new BufferedReader(new FileReader(fileName)));
            json = employee.toJSONString();
            jsonToList(json);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static void jsonToList(String json) {
        JSONParser parser = new JSONParser();
        List<Employee> employees = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            JSONArray jsonArray = (JSONArray) parser.parse(json);
            for (Object o : jsonArray) {
                JSONObject employee = (JSONObject) o;
                employees.add(gson.fromJson(employee.toString(), Employee.class));
            }
            employees.stream().forEach(System.out::println);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}