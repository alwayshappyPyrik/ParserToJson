package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ParserToJsonTest {

    @Test
    public void parseCSVTest() {
        //given
        String fileName = "data.csv";
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        //when
        List<Employee> actual = ParserToJson.parseCSV(columnMapping, fileName);
        //then
        Assertions.assertNotNull(actual);
    }

    @Test
    public void getEmployeeEqualsTest() throws ParserConfigurationException, IOException, SAXException {
        //given
        Employee employeeExpected = new Employee(1, "John", "Smith", "USA", 25);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("data.xml"));
        Node node = doc.getElementsByTagName("employee").item(0);
        //when
        Employee employeeActual = ParserToJson.getEmployee(node);
        //then
        Assertions.assertEquals(employeeExpected, employeeActual);
    }

    @Test
    public void listToJsonTest() {
        //given
        List<Employee> employeesTest = new ArrayList<>();
        employeesTest.add(new Employee(1, "John", "Smith", "USA", 25));
        employeesTest.add(new Employee(2, "Inav", "Petrov", "Russian", 23));
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder
                .setPrettyPrinting()
                .create();

        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String listToJsonExpected = gson.toJson(employeesTest, listType);
        //when
        String listToJsonActual = ParserToJson.listToJson(employeesTest);
        //then
        Assertions.assertEquals(listToJsonExpected, listToJsonActual);
    }

    @Test
    public void parseXmlHamcrestTest() {
        String fileName = "data.xml";
        List<Employee> employees = ParserToJson.parseXML(fileName);
        assertThat(employees, is(not(empty())));
    }

    @Test
    public void readJsonHamcrestTest() {
        String fileName = "data.json";
        String jsonToList = ParserToJson.readJson(fileName);
        assertThat(jsonToList, containsString("Smith"));
    }
}



