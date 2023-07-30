package gob.regionancash.hr.escalafon.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import gob.regionancash.hr.escalafon.model.Demerit;
import gob.regionancash.hr.escalafon.service.DemeritFacade;
import gob.regionancash.hr.model.Employee;

import org.isobit.app.X;
import org.isobit.app.service.UserFacade;
import org.isobit.directory.model.People;
import org.isobit.util.XUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DemeritFacadeImpl implements DemeritFacade {

    @Autowired
    private UserFacade userFacade;

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Demerit> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        Object filter = XUtil.isEmpty(filters.get("filter"), null);
        Object employee = XUtil.isEmpty(filters.get("employee"), null);
        List<Query> ql = new ArrayList();
        String sql;
//        em.createQuery("SELECT o FROM EscDemerito o WHERE 1=1 AND o.employee.id=:employee ORDER BY 1 ASC");
        ql.add(em.createQuery("SELECT o " + (sql = "FROM Demerit o WHERE 1=1 "
                + (employee != null ? " AND o.employee.id=:employee" : "") //+ (filter != null ? " AND UPPER(w.name) like :filter" : "")
                ) + "  ORDER BY 1 ASC"));
        if (pageSize > 0) {
            ql.get(0).setFirstResult(first).setMaxResults(pageSize);
            ql.add(em.createQuery("SELECT COUNT(o) " + sql));
        }
        for (Query q : ql) {
            if (employee != null) {
                q.setParameter("employee", employee instanceof Employee ? ((Employee) employee).getId() : XUtil.intValue(employee));
            }
            if (filter != null) {
                q.setParameter("filter", "%" + filter.toString().toUpperCase().replace(" ", "%") + "%");
            }
        }
        if (pageSize > 0) {
            filters.put("size", ql.get(1).getSingleResult());
        }
        List<Demerit> l = ql.get(0).getResultList();
        for (Demerit d : l) {
            People p = d.getEmployee().getPeople();
            if (p != null) {
                em.detach(p);
                p.setDocumentType(null);
            }
        }
        return l;
    }

    @Override
    public void edit(Demerit entity) {
        if (entity.getEmployee() != null) {
            entity.setEmployeeId(entity.getEmployee().getId());
        }
        if (entity.getId() == null) {
            if (entity.getFechaDem() == null) {
                entity.setFechaDem(X.getServerDate());
            }
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

    @Override
    public List getTypeList() {
        return em.createQuery("SELECT d FROM SanctionType d ORDER BY d.name").getResultList();
    }

    public List getFaultTypeList() {
        return em.createQuery("SELECT d FROM FaultType d ORDER BY d.description").getResultList();
    }

    @Override
    public Demerit find(Object id) {
        return (Demerit) ((Object[]) em.createQuery("SELECT de,em,pe,do FROM Demerit de LEFT JOIN de.employee em JOIN em.people pe JOIN pe.document do WHERE de.id=:id")
                .setParameter("id", id).getSingleResult())[0];
    }

}
