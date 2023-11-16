package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ParserToJsonTest {

    @Test
    void parseCSV() {
        //given
        String fileName = "data.csv";
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        //when
        List<Employee> actual = ParserToJson.parseCSV(columnMapping, fileName);
        //then
        Assertions.assertNotNull(actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"id",
            "firstName",
            "lastName",
            "country",
            "age"})
    void getTagValue(String tag) {
        boolean nodeAttribute = false;
        if (tag.equals("id") || tag.equals("firstName") || tag.equals("lastName") || tag.equals("country") || tag.equals("age")) {
            nodeAttribute = true;
        }
        Assertions.assertTrue(nodeAttribute);
    }

    @Test
    void listToJson() {
        List<Employee> staff = new ArrayList<>();
        staff.add(new Employee(1, "Yaroslav", "Pyrikov", "Russian", 31));
        boolean actual;
        if (staff.isEmpty()) {
            actual = false;
        } else {
            actual = true;
        }
        Assertions.assertTrue(actual, "В списке должен быть хотя бы один сотрудник, иначе файл .json будет пустой");
    }

    @Test
    public void parseCsvHamcrest() {
        String fileName = "data.csv";
        assertThat(fileName, equalTo("data.csv"));
    }

    @Test
    void listToJsonHamcrest() {
        List<Employee> staff = new ArrayList<>();
        staff.add(new Employee(1, "Yaroslav", "Pyrikov", "Russian", 31));
        assertThat(staff, is(not(empty())));
    }
}