package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
    void getTopNCountriesByPopulationTest() {

        app.getTopNCountriesInWorldByPop(null);
    }

}