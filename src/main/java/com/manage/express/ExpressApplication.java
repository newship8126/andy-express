package com.manage.express;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ExpressApplication {

  public static void main(String[] args) {
    SpringApplication.run(ExpressApplication.class, args);
  }
}
