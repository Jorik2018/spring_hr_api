package gob.regionancash.hr.escalafon.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hr.escalafon.model.Demerit;
import gob.regionancash.hr.escalafon.service.DemeritFacade;

public interface DemeritRepository 

    extends JpaRepository<Demerit,Integer>, DemeritFacade{

    public List<Demerit> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(Demerit demerito);

    Demerit find(Object id);

    public List getTypeList();
    
    public List getFaultTypeList();

}