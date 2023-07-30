package gob.regionancash.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hr.model.EmployeeStatus;
import gob.regionancash.hr.service.EscEstadoTrabajadorFacade;

public interface EscEstadoTrabajadorRepository 
    extends JpaRepository<EmployeeStatus,Short>,EscEstadoTrabajadorFacade{

}