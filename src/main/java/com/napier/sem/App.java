package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        //Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        //Method to get countries by population
        a.getCountriesByPopulation();

        // Method to get top N populated countries in the world
        a.getTopNCountriesInWorldByPop(5);

        //Method to get top N populated countries in a continent
        a.getTopNCountriesInContinentByPop(5, "Asia");


        //Method to get top N populated countries in a region
        a.getTopNCountriesInRegionByPop(10, "Caribbean");

        //Method to display countries by population in continent
        a.getCountriesInContByPop("Asia");

        //Method to display countries by population in region
        a.getCountriesInRegionByPop("Caribbean");

        //Method to display cities in the world by population
        a.getCitiesByPop();

        //Method to display cities in the world by population in a continent
        a.getCitiesByPopinAContinent("Europe");

        //Method to display cities in the world by population in a region
        a.getCitiesbyPopinARegion("Caribbean");

        //Method to display cities in the world by population in a country
        a.getCitiesbyPopinACountry("Barbados");

        //Method to display cities in the world by population in a district
        a.getCitiesbyPopinADistrict("Castries");

        //Method to display top N cities in the world by population
        a.getTopNCitiesbyPopinTheWorld(6);

        //Method to display top N cities in a continent by population
        a.getTopNCitiesbyPopinContinent(6, "Asia");

        //Method to display top N cities in a region by population
        a.getTopNCitiesbyPopinRegion(4, "Caribbean");

        //Method to display top N cities in a country by population
        a.getTopNCitiesbyPopinCountry(5, "Haiti");

        //Method to display top N cities in a district by population
        a.getTopNCitiesbyPopinDistrict(3, "Castries");

        //Method to display capital cities in the world by population
        a.getCapitalCities();

        //Method to display capital cities in a continent by population
        a.getCapitalCitiesinContinent("Europe");

        //Method to display capital cities in a region by population
        a.getCapitalCitiesinRegion("Caribbean");

        //Method to display top N capital cities by population
        a.getTopNCapitalCities(5);

        //Method to display top N capital cities in a continent by population
        a.getTopNCapitalCitiesinaContinent(5, "North America");

        //Method to display top N capital cities in a region by population
        a.getTopNCapitalCitiesinaRegion(3, "Caribbean");

        //Method to display the population of people , living in cities , and not living in cities in each continent
        a.getPopulationofPeopleinContinent();

        //Method to display the population of people , living in cities , and not living in cities in each region
        a.getPopulationofPeopleinRegion();

        //Method to display the population of people , living in cities , and not living in cities in each country
        a.getPopulationofPeopleinCountry();

        //Method to display total world population
        a.getTotalPopulation();

        //Method to display total population of a continent
         a.getTotalPopulationofContinent("Asia");

        //Method to display total population of a region
        a.getTotalPopulationofRegion("Caribbean");

        //Method to display total population of a country
        a.getTotalPopulationofCountry("Barbados");

        //Method to display total population of a district
        a.getTotalPopulationofDistrict("Castries");

        //Method to display total population of a city
        a.getTotalPopulationofCity("Georgetown");

        //Method to display language speakers in the world
        a.getLanguageSpeakers();


        // Disconnect from database
        a.disconnect();


    }

    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */

    public void connect() {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        // Connection to the database
        int retries = 100;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false", "root", "w0rldDBp@ss");
                System.out.println("Successfully connected");
                // Wait a bit
                Thread.sleep(10000);
                // Exit for loop
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnection from MySQL database.
     */
    public void disconnect() {
        if (con != null) {
            try {
                // Close connection
                System.out.println("Closing World Database connection...");
                con.close();
                System.out.println("Connection closed.");

            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }


    //Method to display countries sorted by population
    public ArrayList<Country> getCountriesByPopulation() {
        ArrayList<Country> countryList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT  country.continent, country.capital, city.ID, city.name AS capital_city, country.code, country.name, country.region, country.population "
                            + "FROM country "
                            + "JOIN city ON country.capital = city.ID "
                            + "ORDER BY population DESC";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                Country country = new Country();
                country.country_code = rset.getString("country.code");
                country.country_name = rset.getString("country.name");
                country.continent = rset.getString("country.continent");
                country.region = rset.getString("country.region");
                country.population = rset.getInt("country.population");
                country.country_capital = rset.getString("capital_city");
                countryList.add(country);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }



        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Countries Sorted by Population:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s %-30s %-15s %-15s", "Code", "Name", "Continent", "Region", "Population", "Capital"));
        // Print each country's details
        for (Country country : countryList) {
            String country_string =
                    String.format("%-15s %-30s %-15s %-30s %-15s %-15s",
                            country.country_code, country.country_name, country.continent, country.region, country.population, country.country_capital);
            System.out.println(country_string);
        }
        return countryList;
    }


    //Method to display the top N populated countries in the world
    public ArrayList<Country> getTopNCountriesInWorldByPop(Integer number) {
        ArrayList<Country> countryList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT  country.continent, country.capital, city.ID, city.name AS capital_city, country.code, country.name, country.region, country.population "
                            + "FROM country "
                            + "JOIN city ON country.capital = city.ID "
                            + "ORDER BY population DESC"
                            + " LIMIT " + number + " ";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                Country country = new Country();
                country.country_code = rset.getString("country.code");
                country.country_name = rset.getString("country.name");
                country.continent = rset.getString("country.continent");
                country.region = rset.getString("country.region");
                country.population = rset.getInt("country.population");
                country.country_capital = rset.getString("capital_city");
                countryList.add(country);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Top N Countries In The World Sorted By Population:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s %-30s %-15s %-15s", "Code", "Name", "Continent", "Region", "Population", "Capital"));
        // Print each country's details
        for (Country country : countryList) {
            String country_string =
                    String.format("%-15s %-30s %-15s %-30s %-15s %-15s",
                            country.country_code, country.country_name, country.continent, country.region, country.population, country.country_capital);
            System.out.println(country_string);
        }
        return countryList;
    }

    /** Method to display the top N populated countries in a region where N is provided by the user
     *
     */
    public ArrayList<Country> getTopNCountriesInRegionByPop(int number, String Region) {
        ArrayList<Country> countryList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();


            // Create string for SQL statement
            String strSelect =
                    "SELECT country.continent, country.capital, city.ID, city.name AS capital_city, country.code, country.name, country.region, country.population "
                            + "FROM country "
                            + "JOIN city ON country.capital = city.ID "
                            + "WHERE country.region = '" + Region + "'"
                            + "ORDER BY population DESC "
                            + "LIMIT " + number;


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                Country country = new Country();
                country.country_code = rset.getString("country.code");
                country.country_name = rset.getString("country.name");
                country.continent = rset.getString("country.continent");
                country.region = rset.getString("country.region");
                country.population = rset.getInt("country.population");
                country.country_capital = rset.getString("capital_city");
                countryList.add(country);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Top N Countries In The Region Sorted By Population:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s %-30s %-15s %-15s", "Code", "Name", "Continent", "Region", "Population", "Capital"));
        // Print each country's details
        for (Country country : countryList) {
            String country_string =
                    String.format("%-15s %-30s %-15s %-30s %-15s %-15s",
                            country.country_code, country.country_name, country.continent, country.region, country.population, country.country_capital);
            System.out.println(country_string);
        }
        return countryList;
    }


    //Method to display the top N populated countries in continent
    public ArrayList<Country> getTopNCountriesInContinentByPop(int number, String Continent) {
        ArrayList<Country> countryList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();


            // Create string for SQL statement
            String strSelect =
                    "SELECT country.continent, country.capital, city.ID, city.name AS capital_city, country.code, country.name, country.region, country.population "
                            + "FROM country "
                            + "JOIN city ON country.capital = city.ID "
                            + "WHERE country.continent = '" + Continent + "'"
                            + "ORDER BY population DESC "
                            + "LIMIT " + number;


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                Country country = new Country();
                country.country_code = rset.getString("country.code");
                country.country_name = rset.getString("country.name");
                country.continent = rset.getString("country.continent");
                country.region = rset.getString("country.region");
                country.population = rset.getInt("country.population");
                country.country_capital = rset.getString("capital_city");
                countryList.add(country);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Top N Countries In The Continent Sorted By Population:");
        // Print a blank line after the heading
        System.out.print("\n");

        System.out.println(String.format("%-15s %-30s %-15s %-30s %-15s %-15s", "Code", "Name", "Continent", "Region", "Population", "Capital"));
        // Print each country's details
        for (Country country : countryList) {
            String country_string =
                    String.format("%-15s %-30s %-15s %-30s %-15s %-15s",
                            country.country_code, country.country_name, country.continent, country.region, country.population, country.country_capital);
            System.out.println(country_string);
        }
        return countryList;
    }

    //Method to display countries in continent by population
    public ArrayList<Country> getCountriesInContByPop(String Continent) {
        ArrayList<Country> countryList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT  country.continent, country.capital, city.ID, city.name AS capital_city, country.code, country.name, country.region, country.population "
                            + "FROM country "
                            + "JOIN city ON country.capital = city.ID "
                            + "WHERE country.continent = '" + Continent + "'"
                            + "ORDER BY population DESC";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                Country country = new Country();
                country.country_code = rset.getString("country.code");
                country.country_name = rset.getString("country.name");
                country.continent = rset.getString("country.continent");
                country.region = rset.getString("country.region");
                country.population = rset.getInt("country.population");
                country.country_capital = rset.getString("capital_city");
                countryList.add(country);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Countries In The Continent Sorted By Population:");
        // Print a blank line after the heading
        System.out.print("\n");


        // Print header
        System.out.println(String.format("%-15s %-30s %-15s %-30s %-15s %-15s", "Code", "Name", "Continent", "Region", "Population", "Capital"));
        // Print each country's details
        for (Country country : countryList) {
            String country_string =
                    String.format("%-15s %-30s %-15s %-30s %-15s %-15s",
                            country.country_code, country.country_name, country.continent, country.region, country.population, country.country_capital);
            System.out.println(country_string);
        }
        return countryList;
    }


    //Method to display all the countries in a region organized by largest population to smallest
    public ArrayList<Country> getCountriesInRegionByPop(String Region) {
        ArrayList<Country> countryList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT  country.continent, country.capital, city.ID, city.name AS capital_city, country.code, country.name, country.region, country.population "
                            + "FROM country "
                            + "JOIN city ON country.capital = city.ID "
                            + "WHERE country.region = '" + Region + "'"
                            + "ORDER BY population DESC";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                Country country = new Country();
                country.country_code = rset.getString("country.code");
                country.country_name = rset.getString("country.name");
                country.continent = rset.getString("country.continent");
                country.region = rset.getString("country.region");
                country.population = rset.getInt("country.population");
                country.country_capital = rset.getString("capital_city");
                countryList.add(country);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Countries In The Region Sorted By Population:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s %-30s %-15s %-15s", "Code", "Name", "Continent", "Region", "Population", "Capital"));
        // Print each country's details
        for (Country country : countryList) {
            String country_string =
                    String.format("%-15s %-30s %-15s %-30s %-15s %-15s",
                            country.country_code, country.country_name, country.continent, country.region, country.population, country.country_capital);
            System.out.println(country_string);
        }
        return countryList;
    }

    //Method to display cities in world by population
    public ArrayList<City> getCitiesByPop() {
        ArrayList<City> cityList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();

            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.District, city.Population "
                            + "FROM world.city , world.country "
                            + "where country.Code = city.CountryCode "
                            + "ORDER BY Population desc;";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.name");
                city.country_name = rset.getString("country.name");
                city.district = rset.getString("city.district");
                city.population = rset.getInt("city.population");
                cityList.add(city);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Cities In The World Sorted By Population:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s %-30s", "Name", "Country", "District", "Population"));
        // Print each city's details
        for (City city : cityList) {
            String country_string =
                    String.format("%-15s %-30s %-15s %-30s",
                            city.name, city.country_name, city.district, city.population);
            System.out.println(country_string);
        }
        return cityList;


    }

    //Method to display cities in world by population in a continent
    public ArrayList<City> getCitiesByPopinAContinent(String continent) {
        ArrayList<City> cityList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();

            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.District, city.Population "
                            + "FROM world.city "
                            + "JOIN world.country on city.CountryCode = country.Code "
                            + "WHERE country.Continent like '" + continent + "'"
                            + "ORDER BY Population desc";


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.name");
                city.country_name = rset.getString("country.name");
                city.district = rset.getString("city.district");
                city.population = rset.getInt("city.population");
                cityList.add(city);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Cities Sorted By Population In A Continent:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s %-30s", "Name", "Country", "District", "Population"));
        // Print each country's details
        for (City city : cityList) {
            String country_string =
                    String.format("%-15s %-30s %-15s %-30s",
                            city.name, city.country_name, city.district, city.population);
            System.out.println(country_string);
        }
        return cityList;


    }

    //Method to display cities in world by population in a region
    public ArrayList<City> getCitiesbyPopinARegion(String Region) {
        ArrayList<City> cityList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();

            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.District, city.Population "
                            + "FROM world.city "
                            + "JOIN world.country on city.CountryCode = country.Code "
                            + "WHERE country.region = '" + Region + "'"
                            + "ORDER BY Population desc";


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.name");
                city.country_name = rset.getString("country.name");
                city.district = rset.getString("city.district");
                city.population = rset.getInt("city.population");
                cityList.add(city);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Cities Sorted by Population In A Region:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s %-30s", "Name", "Country", "District", "Population"));
        // Print each city's details
        for (City city : cityList) {
            String country_string =
                    String.format("%-15s %-30s %-15s %-30s",
                            city.name, city.country_name, city.district, city.population);
            System.out.println(country_string);
        }
        return cityList;


    }

    //Method to display cities in world by population in a country
    public ArrayList<City> getCitiesbyPopinACountry(String Country) {
        ArrayList<City> cityList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();

            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.District, city.Population "
                            + "FROM world.city "
                            + "JOIN world.country on city.CountryCode = country.Code "
                            + "WHERE country.name = '" + Country + "'"
                            + "ORDER BY Population desc";


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.name");
                city.country_name = rset.getString("country.name");
                city.district = rset.getString("city.district");
                city.population = rset.getInt("city.population");
                cityList.add(city);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Cities Sorted by Population In A Country:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s %-30s", "Name", "Country", "District", "Population"));
        // Print each country's details
        for (City city : cityList) {
            String country_string =
                    String.format("%-15s %-30s %-15s %-30s",
                            city.name, city.country_name, city.district, city.population);
            System.out.println(country_string);
        }
        return cityList;

    }

    //Method to display cities by population in a district
    public ArrayList<City> getCitiesbyPopinADistrict(String District) {
        ArrayList<City> cityList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();

            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.District, city.Population "
                            + "FROM world.city "
                            + "JOIN world.country on city.CountryCode = country.Code "
                            + "WHERE city.district = '" + District + "'"
                            + "ORDER BY Population desc";


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.name");
                city.country_name = rset.getString("country.name");
                city.district = rset.getString("city.district");
                city.population = rset.getInt("city.population");
                cityList.add(city);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Cities Sorted by Population In A District:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s %-30s", "Name", "Country", "District", "Population"));
        // Print each city's details
        for (City city : cityList) {
            String country_string =
                    String.format("%-15s %-30s %-15s %-30s",
                            city.name, city.country_name, city.district, city.population);
            System.out.println(country_string);
        }
        return cityList;

    }

    //Method to display top N cities by population in the world
    public ArrayList<City> getTopNCitiesbyPopinTheWorld(int number) {
        ArrayList<City> cityList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.District, city.Population "
                            + "FROM world.city "
                            + "JOIN world.country ON city.CountryCode = country.Code "
                            + "ORDER BY Population DESC "
                            + "LIMIT " + number;


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.name");
                city.country_name = rset.getString("country.name");
                city.district = rset.getString("city.district");
                city.population = rset.getInt("city.population");
                cityList.add(city);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Cities Sorted by Population In The World:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s %-30s", "Name", "Country", "District", "Population"));
        // Print each city's details
        for (City city : cityList) {
            String country_string =
                    String.format("%-15s %-30s %-15s %-30s",
                            city.name, city.country_name, city.district, city.population);
            System.out.println(country_string);
        }
        return cityList;

    }

    //Method to display top N cities in a continent by population
    public ArrayList<City> getTopNCitiesbyPopinContinent(int number, String Continent) {
        ArrayList<City> cityList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.District, city.Population "
                            + "FROM world.city "
                            + "JOIN world.country ON city.CountryCode = country.Code "
                            + "WHERE country.continent = '" + Continent + "'"
                            + "ORDER BY Population DESC "
                            + "LIMIT " + number;


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.name");
                city.country_name = rset.getString("country.name");
                city.district = rset.getString("city.district");
                city.population = rset.getInt("city.population");
                cityList.add(city);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Top N Cities Sorted by Population in A Continent:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s %-30s", "Name", "Country", "District", "Population"));
        // Print each city's details
        for (City city : cityList) {
            String country_string =
                    String.format("%-15s %-30s %-15s %-30s",
                            city.name, city.country_name, city.district, city.population);
            System.out.println(country_string);
        }
        return cityList;

    }

    //Method to display top N cities in a region by population
    public ArrayList<City> getTopNCitiesbyPopinRegion(int number, String Region) {
        ArrayList<City> cityList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.District, city.Population "
                            + "FROM world.city "
                            + "JOIN world.country ON city.CountryCode = country.Code "
                            + "WHERE country.region = '" + Region + "'"
                            + "ORDER BY Population DESC "
                            + "LIMIT " + number;


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.name");
                city.country_name = rset.getString("country.name");
                city.district = rset.getString("city.district");
                city.population = rset.getInt("city.population");
                cityList.add(city);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Top N Cities Sorted by Population In A Region:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s %-30s", "Name", "Country", "District", "Population"));
        // Print each city's details
        for (City city : cityList) {
            String country_string =
                    String.format("%-15s %-30s %-15s %-30s",
                            city.name, city.country_name, city.district, city.population);
            System.out.println(country_string);
        }
        return cityList;

    }

    //Method to display top N cities in a country by population
    public ArrayList<City> getTopNCitiesbyPopinCountry(int number, String Country) {
        ArrayList<City> cityList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.District, city.Population "
                            + "FROM world.city "
                            + "JOIN world.country ON city.CountryCode = country.Code "
                            + "WHERE country.name = '" + Country + "'"
                            + "ORDER BY Population DESC "
                            + "LIMIT " + number;


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.name");
                city.country_name = rset.getString("country.name");
                city.district = rset.getString("city.district");
                city.population = rset.getInt("city.population");
                cityList.add(city);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Top N Cities Sorted by Population In A Country:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s %-30s", "Name", "Country", "District", "Population"));
        // Print each city's details
        for (City city : cityList) {
            String country_string =
                    String.format("%-15s %-30s %-15s %-30s",
                            city.name, city.country_name, city.district, city.population);
            System.out.println(country_string);
        }
        return cityList;

    }

    //Method to display top N cities in a district by population
    public ArrayList<City> getTopNCitiesbyPopinDistrict(int number, String District) {
        ArrayList<City> cityList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.District, city.Population "
                            + "FROM world.city "
                            + "JOIN world.country ON city.CountryCode = country.Code "
                            + "WHERE city.district = '" + District + "'"
                            + "ORDER BY Population DESC "
                            + "LIMIT " + number;


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                City city = new City();
                city.name = rset.getString("city.name");
                city.country_name = rset.getString("country.name");
                city.district = rset.getString("city.district");
                city.population = rset.getInt("city.population");
                cityList.add(city);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Top N Cities Sorted by Population in A District:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s %-30s", "Name", "Country", "District", "Population"));
        // Print each city's details
        for (City city : cityList) {
            String country_string =
                    String.format("%-15s %-30s %-15s %-30s",
                            city.name, city.country_name, city.district, city.population);
            System.out.println(country_string);
        }
        return cityList;

    }

    //Method to display capital cities in the world by population
    public ArrayList<CapitalCity> getCapitalCities() {
        ArrayList<CapitalCity> CapitalCityList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.District, city.Population "
                            + "FROM world.city "
                            + "JOIN world.country ON city.ID = country.Capital "
                            + "ORDER BY Population DESC ";


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                CapitalCity capitalcity = new CapitalCity();
                capitalcity.name = rset.getString("city.name");
                capitalcity.country_name = rset.getString("country.name");
                capitalcity.population = rset.getInt("city.population");
                CapitalCityList.add(capitalcity);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get capital city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Capital Cities In The World Sorted By Population:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s", "Name", "Country", "Population"));
        // Print each city's details
        for (CapitalCity capitalcity : CapitalCityList) {
            String country_string =
                    String.format("%-15s %-30s %-15s",
                            capitalcity.name, capitalcity.country_name, capitalcity.population);
            System.out.println(country_string);
        }
        return CapitalCityList;

    }


    //Method to display capital cities in a continent by population
    public ArrayList<CapitalCity> getCapitalCitiesinContinent(String Continent) {
        ArrayList<CapitalCity> CapitalCityList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.District, city.Population "
                            + "FROM world.city "
                            + "JOIN world.country ON city.ID = country.Capital "
                            + "WHERE country.continent = '" + Continent + "'"
                            + "ORDER BY Population DESC ";


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                CapitalCity capitalcity = new CapitalCity();
                capitalcity.name = rset.getString("city.name");
                capitalcity.country_name = rset.getString("country.name");
                capitalcity.population = rset.getInt("city.population");
                CapitalCityList.add(capitalcity);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get capital city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Capital Cities In A Continent Sorted By Population:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s", "Name", "Country", "Population"));
        // Print each city's details
        for (CapitalCity capitalcity : CapitalCityList) {
            String country_string =
                    String.format("%-15s %-30s %-15s",
                            capitalcity.name, capitalcity.country_name, capitalcity.population);
            System.out.println(country_string);
        }
        return CapitalCityList;

    }

    //Method to display capital cities in a region by population
    public ArrayList<CapitalCity> getCapitalCitiesinRegion(String Region) {
        ArrayList<CapitalCity> CapitalCityList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.District, city.Population "
                            + "FROM world.city "
                            + "JOIN world.country ON city.ID = country.Capital "
                            + "WHERE country.region = '" + Region + "'"
                            + "ORDER BY Population DESC ";


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                CapitalCity capitalcity = new CapitalCity();
                capitalcity.name = rset.getString("city.name");
                capitalcity.country_name = rset.getString("country.name");
                capitalcity.population = rset.getInt("city.population");
                CapitalCityList.add(capitalcity);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get capital city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Capital Cities In A Region Sorted By Population:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s", "Name", "Country", "Population"));
        // Print each city's details
        for (CapitalCity capitalcity : CapitalCityList) {
            String country_string =
                    String.format("%-15s %-30s %-15s",
                            capitalcity.name, capitalcity.country_name, capitalcity.population);
            System.out.println(country_string);
        }
        return CapitalCityList;

    }

    //Method to display top N capital cities by population
    public ArrayList<CapitalCity> getTopNCapitalCities(int number) {
        ArrayList<CapitalCity> CapitalCityList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.District, city.Population "
                            + "FROM world.city "
                            + "JOIN world.country ON city.ID = country.Capital "
                            + "ORDER BY Population DESC "
                            + "LIMIT " + number;


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                CapitalCity capitalcity = new CapitalCity();
                capitalcity.name = rset.getString("city.name");
                capitalcity.country_name = rset.getString("country.name");
                capitalcity.population = rset.getInt("city.population");
                CapitalCityList.add(capitalcity);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get capital city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Top N Capital Cities Sorted By Population:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s", "Name", "Country", "Population"));
        // Print each city's details
        for (CapitalCity capitalcity : CapitalCityList) {
            String country_string =
                    String.format("%-15s %-30s %-15s",
                            capitalcity.name, capitalcity.country_name, capitalcity.population);
            System.out.println(country_string);
        }
        return CapitalCityList;

    }

    //Method to display top N capital cities in a continent by population
    public ArrayList<CapitalCity> getTopNCapitalCitiesinaContinent(int number, String Continent) {
        ArrayList<CapitalCity> CapitalCityList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.District, city.Population "
                            + "FROM world.city "
                            + "JOIN world.country ON city.ID = country.Capital "
                            + "WHERE country.continent = '" + Continent + "'"
                            + "ORDER BY Population DESC "
                            + "LIMIT " + number;


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                CapitalCity capitalcity = new CapitalCity();
                capitalcity.name = rset.getString("city.name");
                capitalcity.country_name = rset.getString("country.name");
                capitalcity.population = rset.getInt("city.population");
                CapitalCityList.add(capitalcity);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get capital city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Top N Capital Cities In A Continent Sorted By Population:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s", "Name", "Country", "Population"));
        // Print each city's details
        for (CapitalCity capitalcity : CapitalCityList) {
            String country_string =
                    String.format("%-15s %-30s %-15s",
                            capitalcity.name, capitalcity.country_name, capitalcity.population);
            System.out.println(country_string);
        }
        return CapitalCityList;

    }

    //Method to display top N capital cities in a region by population
    public ArrayList<CapitalCity> getTopNCapitalCitiesinaRegion(int number, String Region) {
        ArrayList<CapitalCity> CapitalCityList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.Name, country.Name, city.District, city.Population "
                            + "FROM world.city "
                            + "JOIN world.country ON city.ID = country.Capital "
                            + "WHERE country.region = '" + Region + "'"
                            + "ORDER BY Population DESC "
                            + "LIMIT " + number;


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                CapitalCity capitalcity = new CapitalCity();
                capitalcity.name = rset.getString("city.name");
                capitalcity.country_name = rset.getString("country.name");
                capitalcity.population = rset.getInt("city.population");
                CapitalCityList.add(capitalcity);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get capital city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Top N Capital Cities In A Region Sorted By Population:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-15s %-30s %-15s", "Name", "Country", "Population"));
        // Print each city's details
        for (CapitalCity capitalcity : CapitalCityList) {
            String country_string =
                    String.format("%-15s %-30s %-15s",
                            capitalcity.name, capitalcity.country_name, capitalcity.population);
            System.out.println(country_string);
        }
        return CapitalCityList;

    }

    //Method to display the population of people , people living in cities and people not living in cities in each continent
    public ArrayList<Population> getPopulationofPeopleinContinent() {
        ArrayList<Population> PopulationList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Continent, " +
                            "SUM(city.Population) AS inCity, " +
                            "SUM(DISTINCT(country.Population)) - SUM(city.Population) AS outCity, " +
                            "SUM(DISTINCT(country.Population)) AS totalPopulation, " +
                            "SUM(city.Population) / SUM(DISTINCT(country.Population)) * 100 AS inCityPercentage, " +
                            "(SUM(DISTINCT(country.Population)) - SUM(city.Population)) / SUM(DISTINCT(country.Population)) * 100 AS outCityPercentage " +
                            "FROM country " +
                            "LEFT JOIN city ON country.Code = city.CountryCode " +
                            "GROUP BY country.Continent " +
                            "ORDER BY country.Continent";


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                Population population = new Population();
                population.name = rset.getString("Continent");
                population.total_population = rset.getLong("totalPopulation");
                population.total_populationincities = rset.getDouble("inCityPercentage");
                population.total_populationnotincities = rset.getDouble("outCityPercentage");
                PopulationList.add(population);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get capital city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Population of People Per Continent:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-45s %-30s %-15s %-6s", "Name", "Total Population", "In Cities", "Not in Cities"));
        // Print each city's details
        for (Population population : PopulationList) {
            String country_string =
                    String.format("%-45s %-30s %-15s %-6s",
                            population.name, population.total_population, population.total_populationincities + "%", population.total_populationnotincities + "%");
            System.out.println(country_string);
        }
        return PopulationList;

    }


    //Method to display the population of people , living in cities and people not living in cities in each region
    public ArrayList<Population> getPopulationofPeopleinRegion() {
        ArrayList<Population> PopulationList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.Region, " +
                            "SUM(city.Population) AS inCity, " +
                            "SUM(DISTINCT(country.Population)) - SUM(city.Population) AS outCity, " +
                            "SUM(DISTINCT(country.Population)) AS totalPopulation, " +
                            "SUM(city.Population) / SUM(DISTINCT(country.Population)) * 100 AS inCityPercentage, " +
                            "(SUM(DISTINCT(country.Population)) - SUM(city.Population)) / SUM(DISTINCT(country.Population)) * 100 AS outCityPercentage " +
                            "FROM country " +
                            "LEFT JOIN city ON country.Code = city.CountryCode " +
                            "GROUP BY country.region " +
                            "ORDER BY country.region";


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                Population population = new Population();
                population.name = rset.getString("Region");
                population.total_population = rset.getLong("totalPopulation");
                population.total_populationincities = rset.getDouble("inCityPercentage");
                population.total_populationnotincities = rset.getDouble("outCityPercentage");
                PopulationList.add(population);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get capital city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Population Of People Per Region:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-45s %-30s %-15s %-6s", "Name", "Total Population", "In Cities", "Not In Cities"));
        // Print each city's details
        for (Population population : PopulationList) {
            String country_string =
                    String.format("%-45s %-30s %-15s %-6s",
                            population.name, population.total_population, population.total_populationincities + "%", population.total_populationnotincities + "%");
            System.out.println(country_string);
        }
        return PopulationList;

    }

    //Method to display the population of people , living in cities and people not living in cities in each country
    public ArrayList<Population> getPopulationofPeopleinCountry() {
        ArrayList<Population> PopulationList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT country.name, " +
                            "SUM(city.Population) AS inCity, " +
                            "SUM(DISTINCT(country.Population)) - SUM(city.Population) AS outCity, " +
                            "SUM(DISTINCT(country.Population)) AS totalPopulation, " +
                            "SUM(city.Population) / SUM(DISTINCT(country.Population)) * 100 AS inCityPercentage, " +
                            "(SUM(DISTINCT(country.Population)) - SUM(city.Population)) / SUM(DISTINCT(country.Population)) * 100 AS outCityPercentage " +
                            "FROM country " +
                            "LEFT JOIN city ON country.Code = city.CountryCode " +
                            "GROUP BY country.name " +
                            "ORDER BY country.name";


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                Population population = new Population();
                population.name = rset.getString("name");
                population.total_population = rset.getLong("totalPopulation");
                population.total_populationincities = rset.getDouble("inCityPercentage");
                population.total_populationnotincities = rset.getDouble("outCityPercentage");
                PopulationList.add(population);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get capital city details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Population Of People Per Country:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-45s %-30s %-15s %-6s", "Name", "Total Population", "In Cities", "Not In Cities"));
        // Print each city's details
        for (Population population : PopulationList) {
            String country_string =
                    String.format("%-45s %-30s %-15s %-6s",
                            population.name, population.total_population, population.total_populationincities + "%", population.total_populationnotincities + "%");
            System.out.println(country_string);
        }
        return PopulationList;

    }

    //Method to display the total population of the world
    public long getTotalPopulation() {
        long totalPopulation = 0;
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT SUM(country.population) AS total_population "
                            + "FROM country "
                            + "JOIN city ON country.capital = city.ID";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Print a blank line before showing results
            System.out.print("\n");

            // Process the result set
            if (rset.next()) {
                totalPopulation = rset.getLong("total_population");
                System.out.println("Total population of all countries: " + totalPopulation);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get total population");
        }
             return totalPopulation;
    }

    //Method to display the total population of a continent
    public long getTotalPopulationofContinent(String Continent) {
        long totalPopulation = 0;
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT SUM(population) AS total_population FROM country WHERE continent = '" + Continent + "'";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set
            if (rset.next()) {
                totalPopulation = rset.getLong("total_population");
                System.out.println("Total population of all countries in " + Continent + ":" + totalPopulation);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get total population");
        }
        return totalPopulation;
    }


    //Method to display the total population of a region
    public long getTotalPopulationofRegion(String Region) {
        long totalPopulation = 0;
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT SUM(population) AS total_population FROM country WHERE region = '" + Region + "'";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set
            if (rset.next()) {
                totalPopulation = rset.getLong("total_population");
                System.out.println("Total population of all countries in " + Region + ":" + totalPopulation);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get total population");
        }
        return totalPopulation;
    }


    //Method to display the total population of a country
    public long getTotalPopulationofCountry(String Country) {
        long totalPopulation = 0;
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT SUM(population) AS total_population FROM country WHERE country.name = '" + Country + "'";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set
            if (rset.next()) {
                totalPopulation = rset.getLong("total_population");
                System.out.println("Total population of  " + Country + " :" + totalPopulation);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get total population");
        }
        return totalPopulation;
    }

    //Method to display the total population of a district
    public long getTotalPopulationofDistrict(String District) {
        long totalPopulation = 0;
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT SUM(population) AS total_population FROM city WHERE city.district = '" + District + "'";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set
            if (rset.next()) {
                totalPopulation = rset.getLong("total_population");
                System.out.println("Total population of  " + District + " :" + totalPopulation);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get total population");
        }
        return totalPopulation;
    }


    //Method to display the total population of a city
    public long getTotalPopulationofCity(String City) {
        long totalPopulation = 0;
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT SUM(population) AS total_population FROM city WHERE city.name = '" + City + "'";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set
            if (rset.next()) {
                totalPopulation = rset.getLong("total_population");
                System.out.println("Total population of  " + City + " :" + totalPopulation);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get total population");
        }
        return totalPopulation;
    }


    //Method to display the total he number of people who speak the Chinese , English , Hindi , Spanish and Arabic the following languages from greatest number to smallest, including the percentage of the world population:
    public ArrayList<Language> getLanguageSpeakers() {

        ArrayList<Language> LanguageList = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT q.Language, q.TotalSpeakers, " +
                            "(q.TotalSpeakers / (SELECT SUM(c.Population) FROM country c)) * 100 AS WorldPercent " +
                            "FROM (SELECT cl.Language, SUM(c.Population) AS TotalSpeakers " +
                            "FROM countrylanguage cl INNER JOIN country c ON cl.CountryCode = c.Code " +
                            "WHERE cl.Language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic') " +
                            "GROUP BY cl.Language) AS q " +
                            "ORDER BY WorldPercent DESC;";


            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Process the result set and add to the list
            while (rset.next()) {
                Language language = new Language();
                language.language_name = rset.getString("Language");
                language.total_speakers = rset.getLong("TotalSpeakers");
                language.world_percentage = rset.getLong("WorldPercent");
                LanguageList.add(language);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get language details");
            return null;
        }

        // Print a blank line before the heading
        System.out.print("\n");
        // Print heading
        System.out.println("Total Number Of Speakers Per Language:");
        // Print a blank line after the heading
        System.out.print("\n");

        // Print header
        System.out.println(String.format("%-45s %-30s %-15s", "Language", "Total Speakers", "World Percentage of Speakers"));
        // Print each city's details
        for (Language language : LanguageList) {
            String country_string =
                    String.format("%-45s %-30s %-15s",
                            language.language_name, language.total_speakers, language.world_percentage + "%");
            System.out.println(country_string);
        }
        return LanguageList;

    }


}