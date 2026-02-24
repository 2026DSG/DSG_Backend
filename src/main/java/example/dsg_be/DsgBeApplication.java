package example.dsg_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DsgBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DsgBeApplication.class, args);
    }

}
