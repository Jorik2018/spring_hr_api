package gob.regionancash.hr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
    exclude = {
        DataSourceAutoConfiguration.class
    },
	scanBasePackages = {
		"org.isobit",
		"gob.regionancash.hr",
		"gob.regionancash.config",
		"gob.regionancash.remuneracion",
		"org.eclipse.microprofile.jwt",
        "edu.uns.remuneracion"
	}
)
public class HrApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

}
