package gob.regionancash.hr.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hr.model.License;
import gob.regionancash.hr.service.LicenseFacade;

public interface LicenseRepository
    extends JpaRepository<License,Integer>,LicenseFacade{

}