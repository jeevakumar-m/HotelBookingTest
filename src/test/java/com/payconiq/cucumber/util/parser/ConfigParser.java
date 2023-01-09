package com.payconiq.cucumber.util.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigParser
        {
            private static Properties properties;

            static
           {
                String propertyFilePath="src/test/resources/config/application.properties";
               BufferedReader reader = null;
               try {
                   reader = new BufferedReader(new FileReader(propertyFilePath));
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               }
               ;
                properties=new Properties();
               try {
                   properties.load(reader);
                   reader.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }

           }

            public static String getValueAsString(String propertyKey)
            {
                String propertyValue=properties.getProperty(propertyKey);
                if(propertyValue==null)
                {
                    throw new RuntimeException("Property : " + propertyKey + " is not found in config file");
                }
                return propertyValue;
            }

        }