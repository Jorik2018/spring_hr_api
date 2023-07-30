package gob.regionancash.hr.escalafon.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

import gob.regionancash.hr.escalafon.model.SanctionType;
import gob.regionancash.hr.escalafon.service.EscTipoSancionFacade;

public interface EscTipoSancionRepository
    extends JpaRepository<SanctionType,Integer>,EscTipoSancionFacade{

    public List<SanctionType> load(int first, int pageSize, String sortField, Map<String, Object> filters);

    void edit(SanctionType esctiposancion);

}