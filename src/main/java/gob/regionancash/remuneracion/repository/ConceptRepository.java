package gob.regionancash.remuneracion.repository;

import gob.regionancash.remuneracion.model.Concept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, Integer> {

}