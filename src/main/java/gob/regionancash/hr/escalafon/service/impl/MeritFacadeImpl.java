package gob.regionancash.hr.escalafon.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import gob.regionancash.hr.escalafon.model.Merit;
import gob.regionancash.hr.escalafon.service.MeritFacade;
import gob.regionancash.hr.model.Employee;

import org.isobit.app.service.UserFacade;
import org.isobit.util.XUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class MeritFacadeImpl implements MeritFacade {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserFacade userFacade;

    @Override
    public List<Merit> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        Object filter = XUtil.isEmpty(filters.get("filter"), null);
        Object employee = XUtil.isEmpty(filters.get("employee"), null);
        List<Query> ql = new ArrayList();
        String sql;
//        em.createQuery("SELECT o FROM EscMerit o WHERE 1=1 AND o.employeeId=:employee ORDER BY 1 ASC");

        ql.add(em.createQuery("SELECT o " + (sql = "FROM Merit o WHERE 1=1 "
                + (employee != null ? " AND o.employeeId=:employee" : "")) + " ORDER BY 1 ASC"));
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
        return ql.get(0).getResultList();
    }

    @Override
    public void edit(Merit entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

    @Override
    public Merit find(Object id) {
        Merit merit = em.find(Merit.class,id);
        Employee employee=(Employee) ((Object[])em.createQuery("SELECT e,p,d FROM Employee e JOIN e.people p JOIN p.document d WHERE e.id=:id")
                .setParameter("id",merit.getEmployeeId()).getSingleResult())[0];
        merit.setEmployee(employee);
        return merit;
    }

}
