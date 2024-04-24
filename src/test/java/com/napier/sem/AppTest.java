package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void getCountriesByPopulationTest() {
        // Act
        ArrayList<Country> actualCountries = app.getCountriesByPopulation();

        // Assert
        //Expects the total number of Countries from the World DB (232) and fails if incorrect.
        assertEquals(232, actualCountries.size());

    }


    @Test
    void getTopNCountriesInWorldByPopTestExpectThree() {
        // Act
        ArrayList<Country> actualCountries = app.getTopNCountriesInWorldByPop(3);

        // Assert
        //Expects the total number of Countries from the World DB (232) and fails if incorrect.
        assertEquals(3, actualCountries.size());
    }
}