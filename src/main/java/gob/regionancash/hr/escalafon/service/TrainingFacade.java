package gob.regionancash.hr.escalafon.service;

import java.util.List;
import java.util.Map;

import gob.regionancash.hr.escalafon.model.EscCapacitacion;

public interface TrainingFacade {

    public List<EscCapacitacion> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(EscCapacitacion escdemerito);

    public EscCapacitacion load(Object m);

}
