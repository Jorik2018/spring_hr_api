package gob.regionancash.hr.repository;

import java.util.List;
import java.util.Map;
import org.isobit.util.AbstractFacadeLocal;
import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hr.model.Employee;
import gob.regionancash.hr.service.EmployeeFacade;

public interface EmployeeRepository 
    extends JpaRepository<Employee,Integer>,EmployeeFacade{

}