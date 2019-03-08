package com.himanshu.countryblotter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {
  public static void main(String[] args) {
    /*try(ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args)) {

    }*/
    ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);
  }
}
