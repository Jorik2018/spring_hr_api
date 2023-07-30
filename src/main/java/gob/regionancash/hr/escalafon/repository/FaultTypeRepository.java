package gob.regionancash.hr.escalafon.repository;

import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hr.escalafon.model.FaultType;
import gob.regionancash.hr.escalafon.service.FaultTypeFacade;

public interface FaultTypeRepository 
    extends JpaRepository<FaultType,Integer>, FaultTypeFacade{

    public List<FaultType> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(FaultType esctipofalta);

}