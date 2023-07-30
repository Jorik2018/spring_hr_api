package gob.regionancash.hr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
	"gob.regionancash.hr.escalafon.model", 
	"gob.regionancash.hr.model",
	"org.isobit.directory.model"})
@EnableJpaRepositories(basePackages = {
	"gob.regionancash.hr",
	"org.isobit.directory"})
@ComponentScan(basePackages = {"org.isobit","gob.regionancash.hr"})
public class HrApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

}
