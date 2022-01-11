package server;

import server.control.ConsumableManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebAppServerApplication {

	public static void main(String[] args) {
		ConsumableManager.getInstance();
		SpringApplication.run(WebAppServerApplication.class, args);

	}

}
