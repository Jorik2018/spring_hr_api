
package gob.regionancash.hd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hd.model.OrgResolucion;
import gob.regionancash.hd.service.OrgResolucionFacade;

public interface OrgResolucionRepository
    extends JpaRepository<OrgResolucion,Integer>,OrgResolucionFacade{
    
}