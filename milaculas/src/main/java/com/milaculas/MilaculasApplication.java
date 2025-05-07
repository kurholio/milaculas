package com.milaculas;

import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MilaculasApplication {

	
	  static {
	        // Prevent Tomcat from trying to re-register URL handler
	        TomcatURLStreamHandlerFactory.disable();
	    }
	public static void main(String[] args) {
		SpringApplication.run(MilaculasApplication.class, args);
	}

}
