package gob.regionancash.hd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hd.model.PerNivelRemunerativo;
import gob.regionancash.hd.service.PerNivelRemunerativoFacade;

public interface PerNivelRemunerativoRepository
    extends JpaRepository<PerNivelRemunerativo,Integer>,PerNivelRemunerativoFacade{

}