package gob.regionancash.hr.escalafon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hr.escalafon.service.StudyFacade;
import gob.regionancash.hr.model.Study;

public interface StudyRepository 
    extends JpaRepository<Study,Integer>, StudyFacade{

}