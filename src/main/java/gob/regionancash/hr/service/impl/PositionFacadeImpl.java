package gob.regionancash.hr.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import gob.regionancash.hr.model.Position;
import gob.regionancash.hr.service.PositionFacade;

import org.isobit.app.service.UserFacade;
import org.isobit.util.XUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class PositionFacadeImpl implements PositionFacade {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserFacade userFacade;

    @Override
    public List<Position> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        Object filter = XUtil.isEmpty(filters.get("filter"), null);
        Object name = XUtil.isEmpty(filters.get("position:name"), null);
        List<Query> ql = new ArrayList();
        String sql;
        ql.add(em.createQuery("SELECT o " + (sql = "FROM Position o WHERE 1=1 "
                + (name != null ? " AND UPPER(o.name) like :name" : "")) + "  ORDER BY " + (sortField != null ? sortField : "1 ASC")));
        if (pageSize > 0) {
            ql.get(0).setFirstResult(first).setMaxResults(pageSize);
            ql.add(em.createQuery("SELECT COUNT(o) " + sql));
        }
        for (Query q : ql) {
            if (name != null) {
                q.setParameter("name", "%" + name.toString().toUpperCase().replace(" ", "%") + "%");
            }
        }
        if (pageSize > 0) {
            filters.put("size", ql.get(1).getSingleResult());
        }
        return ql.get(0).getResultList();
    }

    @Override
    public void edit(Position entity) {
        if (entity.getNivel() == null) {
            entity.setNivel(0);
        }
        if (entity.getAbrev() == null) {
            entity.setAbrev("");
        }
        if (entity.getOrdenFirma() == null) {
            entity.setOrdenFirma(0);
        }
        if (entity.getId() == null) {

            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

}
