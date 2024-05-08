package gob.regionancash.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import gob.regionancash.hr.model.Holiday;

public interface HolidayRepository

        extends JpaRepository<Holiday, Integer> {

}
