package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest {
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33060", 10000);

    }

    @Test
    void getCountriesByPopulationTest() {
        ArrayList<Country> expectedCountries = new ArrayList<>();

        // Act
        ArrayList<Country> actualCountries = app.getCountriesByPopulation();

        // Assert
        //Expects the total number of Countries from the World DB (232) and fails if incorrect.
        assertEquals(232, actualCountries.size());

    }


    @Test
    void getTopNCountriesInWorldByPopThrowsExceptionTest() {
        // Act
        ArrayList<Country> actualCountries = app.getTopNCountriesInWorldByPop(3);

        // Assert
        //Expects the total number of Countries from the World DB (232) and fails if incorrect.
        assertEquals(3, actualCountries.size());


        /*
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            //Code under test
            app.getTopNCountriesInWorldByPop(3);
        });

        Assertions.assertEquals("Fail! Invalid Number input for Query", thrown.getMessage());
    }

        /*
        assertThrows(Exception.class, () -> {
            app.getTopNCountriesInWorldByPop(3);
        });

         */
    }
}
