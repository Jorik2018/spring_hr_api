package gob.regionancash.remuneracion.service.impl;

import gob.regionancash.hr.model.PensionSystem;
import gob.regionancash.remuneracion.service.PensionSystemFacadeLocal;
import java.util.List;
import org.springframework.data.domain.*;
import java.util.Map;

public class PensionSystemServiceImpl implements PensionSystemFacadeLocal {
  
  public Page<PensionSystem> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
     return null;//super.load(first, pageSize, sortField, filters);
  }

}