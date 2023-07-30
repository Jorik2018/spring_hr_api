package gob.regionancash.hd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hd.model.OrgContrato;
import gob.regionancash.hd.service.OrgContratoFacade;

public interface OrgContratoRepository 
    extends JpaRepository<OrgContrato,Integer>,OrgContratoFacade{

}