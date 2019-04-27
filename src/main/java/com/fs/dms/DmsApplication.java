package com.fs.dms;

import com.fs.dms.config.AppConfig;
import com.fs.dms.rest.RestDeviceController;
import com.fs.dms.services.GraphQLService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {AppConfig.class, RestDeviceController.class, GraphQLService.class})
public class DmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DmsApplication.class, args);
	}

}
