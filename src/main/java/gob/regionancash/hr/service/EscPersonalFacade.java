package gob.regionancash.hr.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hr.model.Employee;

public interface EscPersonalFacade {

    public List<Employee> load(int first, int pageSize, String sortField, Map<String, Object> filters);
    
}
