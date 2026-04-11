package gob.regionancash.remuneracion.service;

import gob.regionancash.hr.model.EmployeeType;
import java.util.List;
import java.util.Map;
import java.util.Date;

public interface IngresoDescuentoService {

    Object getConstanciaHD(int v, Date a, Date b, List payrolls);
  
}
