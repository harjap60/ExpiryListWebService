package ca.cmpt213.a4.webappserver;

import ca.cmpt213.a4.webappserver.control.ConsumableManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebAppServerApplication {

	public static void main(String[] args) {
		ConsumableManager.getInstance();
		SpringApplication.run(WebAppServerApplication.class, args);

	}

}
