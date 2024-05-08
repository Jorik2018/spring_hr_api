package gob.regionancash.hr.escalafon.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import gob.regionancash.hr.escalafon.model.EscCapacitacion;
import gob.regionancash.hr.escalafon.service.TrainingFacade;
import gob.regionancash.hr.model.Employee;

import java.util.HashMap;

import org.isobit.app.service.UserService;
import org.isobit.directory.model.Company;
import org.isobit.directory.service.CompanyFacade;
import org.isobit.util.XUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class TrainingFacadeImpl implements TrainingFacade {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserService userFacade;

    @Override
    public List<EscCapacitacion> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        Object filter = XUtil.isEmpty(filters.get("filter"), null);
        Object employee = XUtil.isEmpty(filters.get("employee"), null);

        List<Query> ql = new ArrayList();
        String sql;
//        ql.add(em.createQuery("SELECT o FROM EscCapacitacion o WHERE 1=1 AND o.employee.id=:employee  ORDER BY 1 ASC"));
        ql.add(em.createQuery("SELECT o " + (sql = "FROM EscCapacitacion o WHERE 1=1"
                + (employee != null ? " AND o.employee.id=:employee" : "") //+ (filter != null ? " AND UPPER(w.name) like :filter" : "")
                ) + " ORDER BY 1 ASC"));
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

    @Autowired
    private CompanyFacade companyFacade;

    @Override
    public void edit(EscCapacitacion entity) {
        Map ext = (Map) entity.getExt();
        if (ext.containsKey("company")) {
            Company company = (Company) ext.get("company");
            companyFacade.edit(company);
            entity.setCompany(company);
        }
        if (entity.getId() == null) {
            System.out.println("entity.getInstitution()=" + entity.getInstitution());
            if (entity.getCompany() != null) {
                entity.setCompanyId(entity.getCompany().getId());
            }
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

    /*
        private static boolean esc_instituciones;
    
    @Override
    public List<EscCapacitacion> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        EntityManager em = em;
        List<Query> ql = new ArrayList();
        Object qs =X.toText(filters.get("q"));
        String sql;
//        em.createQuery("SELECT c FROM EscCapacitacion c JOIN c.personal p JOIN p.drtPersonaNatural pn WHERE pn.nombreCompleto");
        ql.add(em.createQuery("SELECT c " + (sql = "FROM EscCapacitacion c JOIN c.personal p JOIN p.drtPersonaNatural pn WHERE pn.nombreCompleto LIKE :q")));
        if (pageSize > 0) {
            ql.get(0).setFirstResult(first).setMaxResults(pageSize);
            ql.add(em.createQuery("SELECT COUNT(c) " + sql));
        }
//        em.createQuery("SELECT p FROM People p WHERE p.emailPrin");
        for (Query q : ql) {
            if (qs != null) {
                q.setParameter("q", "%" + qs.toString().toUpperCase() + "%");
//                q.setParameter("idEsc", XUtil.intValue(qs));
            }
//            if (numeroPndid != null) {
//                q.setParameter("numeroPndid", "%" + numeroPndid.toString().toUpperCase() + "%");
//            }
//            if (mail != null) {
//                q.setParameter("emailPrin", "%" + mail.toString().toLowerCase() + "%");
//            }
        }
        if (pageSize > 0) {
            filters.put("size", ql.get(1).getSingleResult());
        }
        List<EscCapacitacion> capacitacionList= ql.get(0).getResultList();
        List ids=new ArrayList();
        Map m2=new HashMap();
        for (EscCapacitacion ee : capacitacionList) {
            ids.add(ee.getIdInst());
        }
        if (ids.size() > 0) {
            if (esc_instituciones) {
                try {
                    for (Object[] r : (List<Object[]>) em.createNativeQuery("SELECT id_inst,nombre_inst FROM esc_instituciones WHERE id_inst IN(:ids)")
                            .setParameter("ids", ids).getResultList()) {
                        Company pn = new Company();
                        pn.setRazonSocial("" + r[1]);
                        
                        m2.put(r[0], pn);
                    }
                } catch (Exception ex) {
                    esc_instituciones = false;
                }
            } else {
                for (Company r : (List<Company>) em.createQuery("SELECT p FROM Company p WHERE -p.idDir IN(:ids)")
                        .setParameter("ids", ids).getResultList()) {
                    m2.put(-r.getIdDir(), r);
                }
            }
            for (EscCapacitacion ee : capacitacionList) {
                ee.setCompany((Company) m2.get(ee.getIdInst()));
            }
        }
        return capacitacionList;
        //To change body of generated methods, choose Tools | Templates.
    }
    
     */

    @Override
    public EscCapacitacion load(Object o) {
        EscCapacitacion e = em.find(EscCapacitacion.class,o instanceof EscCapacitacion ? ((EscCapacitacion) o).getId() : XUtil.intValue(o));
        e.setExt(new HashMap());
        return e;
    }

}
