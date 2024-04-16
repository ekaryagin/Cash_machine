package com.kumpus.atm;

import com.kumpus.atm.service.ATM;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AtmApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(AtmApplication.class, args);
		ATM atm = context.getBean(ATM.class);
		atm.run();
	}
}
