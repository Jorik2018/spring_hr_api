package gob.regionancash.hr.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hr.model.Position;
import gob.regionancash.hr.service.PositionFacade;

public interface PositionRepository 
    extends JpaRepository<Position,Integer>,PositionFacade{

}