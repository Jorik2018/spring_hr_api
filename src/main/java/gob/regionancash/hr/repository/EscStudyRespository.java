package gob.regionancash.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hr.model.Study;
import gob.regionancash.hr.service.EscStudyFacade;

public interface EscStudyRespository extends JpaRepository<Study,Integer>,
    EscStudyFacade{

}