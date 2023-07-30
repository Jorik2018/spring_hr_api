package gob.regionancash.hr.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import gob.regionancash.hr.model.Ballot;
import gob.regionancash.hr.service.PerPapeletaFacade;

import java.util.Date;
import org.isobit.app.X;
import org.isobit.app.service.UserFacade;
import org.isobit.util.AbstractFacade;
import org.isobit.util.XDate;
import org.isobit.util.XUtil;
import org.isobit.util.AbstractFacade.RowAdapter;
import org.springframework.beans.factory.annotation.Autowired;

public class PerPapeletaFacadeImpl implements PerPapeletaFacade {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserFacade userFacade;

    @Override
    public List<Ballot> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        Object people = XUtil.isEmpty(filters.get("people"), null);
        
        List<Query> ql = new ArrayList();
        String sql;
//        em.createQuery("SELECT o FROM Ballot o JOIN o.people p where o.people.nombreCompleto LIKE o.id desc");
        ql.add(em.createQuery("SELECT o,1.0*TIME_TO_SEC(TIMEDIFF(NOW(),TIMESTAMP(fecha_efectiva,h_sal)))/3600,1.0*time_to_sec(timediff(TIMESTAMP(fecha_efectiva,h_ing),TIMESTAMP(fecha_efectiva,h_sal)))/3600 " + (sql = " FROM Ballot o "
                + "WHERE 1=1 " 
                +(people!=null?" AND UPPER(o.people.fullName) LIKE :people":"")
                ) + " ORDER BY o.id DESC"));
        if (pageSize > 0) {
            ql.get(0).setFirstResult(first).setMaxResults(pageSize);
            ql.add(em.createQuery("SELECT COUNT(o) " + sql));
        }
        for (Query q : ql) {
            if (people != null) {
                q.setParameter("people", "%" + people.toString().toUpperCase().replace(" ", "%") + "%");
            }
        }
        if (pageSize > 0) {
            filters.put("size", ql.get(1).getSingleResult());
        }
        return AbstractFacade.getColumn(ql.get(0).getResultList(),new RowAdapter() {
            @Override
            public Object adapting(Object[] row) {
                ((Ballot)row[0]).setExt(row);
                return row[0];
            }
        });
    }

    @Override
    public void edit(Ballot entity) {
        Date hing = entity.getHIng();
        if (hing != null) {
            entity.setHIng(XDate.getDateTime(X.getServerDate(), hing));
        }
        hing = entity.getHSal();
        if (hing != null) {
            entity.setHSal(XDate.getDateTime(X.getServerDate(), hing));
        }
        if (entity.getId() == null) {
            entity.setFechaEmision(X.getServerDate());
            entity.setEstado("");
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

}
