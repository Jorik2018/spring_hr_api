package gob.regionancash.hr.service;

import java.util.List;
import java.util.Map;
import gob.regionancash.hr.model.EmployeeStatus;

public interface EscEstadoTrabajadorFacade{

    public List<EmployeeStatus> load(int first, int pageSize, String sortField, Map<String, Object> filters);

}