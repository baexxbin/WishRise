package com.baexxbin.wishrise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WishriseApplication {

	public static void main(String[] args) {
		SpringApplication.run(WishriseApplication.class, args);
	}

}
