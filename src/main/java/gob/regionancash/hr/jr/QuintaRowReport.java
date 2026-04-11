package gob.regionancash.hr.jr;

import gob.regionancash.hr.model.Employee;
import gob.regionancash.remuneracion.model.PdtQuinta;
import lombok.*;

@Data
public class QuintaRowReport {
    PdtQuinta pdtQuinta;
    Employee employee;
}
