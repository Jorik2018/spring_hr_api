package gob.regionancash.hr.escalafon.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hr.escalafon.model.Demerit;

public interface DemeritFacade{

    public List<Demerit> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(Demerit escdemerito);

    Demerit find(Object id);

    public List getTypeList();
    
    public List getFaultTypeList();

}