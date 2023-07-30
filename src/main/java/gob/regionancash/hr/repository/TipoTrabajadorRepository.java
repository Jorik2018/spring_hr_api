package gob.regionancash.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hr.model.TipoTrabajador;
import gob.regionancash.hr.service.TipoTrabajadorFacade;

public interface TipoTrabajadorRepository 
    extends JpaRepository<TipoTrabajador,Short>,TipoTrabajadorFacade{
    
}
