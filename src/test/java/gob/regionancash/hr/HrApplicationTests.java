package gob.regionancash.hr;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@EntityScan(basePackages = {
	"org.isobit.directory.model",
	"gob.regionancash.hr.escalafon.model", 
	"gob.regionancash.hr.model"})
	@ComponentScan(basePackages = "org.isobit")
class HrApplicationTests {

	@Test
	void contextLoads() {
	}

}
