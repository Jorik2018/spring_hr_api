package gob.regionancash.hr.repository;

import gob.regionancash.hr.model.Attendance;
import gob.regionancash.hr.service.AttendanceFacade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository 
    extends JpaRepository<Attendance,Integer>,AttendanceFacade{

}