package gob.regionancash.hd.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.isobit.app.service.UserService;
import org.isobit.util.XUtil;
import org.springframework.beans.factory.annotation.Autowired;

import gob.regionancash.hd.model.PerDedicacion;
import gob.regionancash.hd.service.PerDedicacionFacade;

public class PerDedicacionFacadeImpl implements PerDedicacionFacade{

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserService userFacade;

    @Override
    public List<PerDedicacion> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        Object filter = XUtil.isEmpty(filters.get("filter"), null);
        List<Query> ql = new ArrayList();
        String sql;
        ql.add(em.createQuery("SELECT o " + (sql="FROM PerDedicacion o WHERE 1=1 "
                //+ (filter != null ? " AND UPPER(w.name) like :filter" : "")
        )+"  ORDER BY 1 ASC"));
        if (pageSize > 0) {
            ql.get(0).setFirstResult(first).setMaxResults(pageSize);
            ql.add(em.createQuery("SELECT COUNT(o) " + sql));
        }
        for (Query q : ql) {
            if (filter != null) {
                q.setParameter("filter", "%" + filter.toString().toUpperCase().replace(" ", "%") + "%");
            }
        }
        if (pageSize > 0) {
            filters.put("size", ql.get(1).getSingleResult());
        }
        return ql.get(0).getResultList();
    }

    @Override
    public void edit(PerDedicacion entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

}
