package com.himanshu.countryblotter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * application-{spring-profile}.properties, it gets activated when you use -Dspring.profiles.active=<profile-name>
 * If logging needs to be done on file then, start this class with additional property: -Dlogs.dir=C:/himanshu_work/tmp/logs
 */
@SpringBootApplication
public class Main {
  public static void main(String[] args) {
    /*try(ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args)) {

    }*/
    ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);
  }
}
