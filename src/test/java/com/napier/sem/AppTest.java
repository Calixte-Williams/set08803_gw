package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.TestInstance;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    static App app;

    @BeforeAll
    static void init() {
        app = new App();
        app.connect("localhost:33060", 10000);

    }

    @Test
    void testGetCountriesByPopulation() {
        ArrayList<Country> expectedCountries = new ArrayList<>();

        // Act
        ArrayList<Country> actualCountries = app.getCountriesByPopulation();

        // Assert
        //Expects the total number of Countries from the World DB (232) and fails if incorrect.
        assertEquals(232, actualCountries.size());

    }

    @Test
    void testGetTopNCountriesInWorldByPopThrowsException() {

        assertThrows(Exception.class, () -> {
            app.getTopNCountriesInWorldByPop(0);
        });
    }

}