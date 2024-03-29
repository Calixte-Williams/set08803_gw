package com.napier.sem;

import java.sql.*;

public class App
{
    public static void main(String[] args)
    {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        //Get Countries
        Country country = a.getCountriesByPopulation();
        a.getCountriesByPopulation();

        //Display World Country Report sorted by Population
        // a.displayCountries(country);

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

        public void connect()
        {
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

        //Method to display countries sorted by population
       public Country getCountriesByPopulation(){
           try
           {
               // Create an SQL statement
               Statement stmt = con.createStatement();
               // Create string for SQL statement
               String strSelect =
                       "SELECT country.Code, country.Name, country.Continent, country.Population "
                               + "FROM world.country "
                               + "ORDER BY Population desc";
               // Execute SQL statement
               ResultSet rset = stmt.executeQuery(strSelect);
               // Check one is returned
               if (rset.next())
               {
                   Country country = new Country();
                   country.country_code = rset.getString("country.code");
                   country.country_name = rset.getString("country.name");
                   country.continent = rset.getString("country.continent");
                   country.population = rset.getInt("country.population");
                   System.out.println(country.country_code + ", " + country.country_name + ", " + country.continent + ", " + country.population);
                   return country;
               }
               else
                   return null;
           }
           catch (Exception e)
           {
               System.out.println(e.getMessage());
               System.out.println("Failed to get Country information");
               return null;
           }

           /* public void displayCountries(Country country){
               if (country != null) {
                   System.out.println(country.country_code + ", " + country.country_name + ", " + country.continent + ", " + country.population);
               }
               } */

       }


    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                System.out.println("Closing World Database connection...");
                con.close();
                System.out.println("Connection closed.");

            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }
}