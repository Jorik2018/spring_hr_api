package gob.regionancash.hd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hd.model.PerCategoriaDocente;
import gob.regionancash.hd.service.PerCategoriaDocenteFacade;

public interface PerCategoriaDocenteRepository
    extends JpaRepository<PerCategoriaDocente,Integer>,PerCategoriaDocenteFacade{

}