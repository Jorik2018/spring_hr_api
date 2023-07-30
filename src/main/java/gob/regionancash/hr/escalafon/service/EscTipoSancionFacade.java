package gob.regionancash.hr.escalafon.service;

import java.util.List;
import java.util.Map;
import gob.regionancash.hr.escalafon.model.SanctionType;

public interface EscTipoSancionFacade{

    public List<SanctionType> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(SanctionType esctiposancion);

}