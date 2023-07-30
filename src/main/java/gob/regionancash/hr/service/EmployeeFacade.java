package gob.regionancash.hr.service;

import java.util.List;
import java.util.Map;

import org.isobit.directory.model.CivilStatus;
import org.isobit.util.AbstractFacadeLocal;

import gob.regionancash.hr.model.Employee;

public interface EmployeeFacade{

    public List<Employee> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(Employee employee);

    public Employee load(Object id);
    
    public List<CivilStatus> getCivilStatusList();
    
    public List getActiveEmployeList(Map m);

    public Object getStatusList();

}