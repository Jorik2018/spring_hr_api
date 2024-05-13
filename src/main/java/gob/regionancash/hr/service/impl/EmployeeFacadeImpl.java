package gob.regionancash.hr.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import reactor.core.publisher.Mono;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import org.isobit.app.X;
import org.isobit.app.service.UserService;
import org.isobit.directory.model.CivilStatus;
import org.isobit.directory.model.Company;
import org.isobit.directory.model.Dependency;
import org.isobit.directory.model.People;
import org.isobit.directory.service.PeopleFacade;
import org.isobit.util.XFile;
import org.isobit.util.XMap;
import org.isobit.util.XUtil;
import org.isobit.util.AbstractFacade.RowAdapter;
import org.isobit.util.AbstractFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import gob.regionancash.hr.model.Contract;
import gob.regionancash.hr.model.Employee;
import gob.regionancash.hr.model.LaborRegime;
import gob.regionancash.hr.model.Relative;
import gob.regionancash.hr.repository.ContractRepository;
import gob.regionancash.hr.service.ContractFacade;
import gob.regionancash.hr.service.EmployeeFacade;

public class EmployeeFacadeImpl implements EmployeeFacade {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserService userFacade;

    @Override
    public List<Employee> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        Object query = XUtil.isEmpty(filters.get("query"), null);
        Object code = XUtil.isEmpty(filters.get("code"), null);
        List laborRegime = (List) filters.get("laborRegime");
        List pensionSystem = (List) filters.get("pensionSystem");
        List workModality = (List) filters.get("workModality");
        Object people = XUtil.isEmpty(filters.get("employee"), null);
        Object active = filters.get("active");
        //assistancecontrol only view active workers
        List<Query> ql = new ArrayList();
        String sql;
        ql.add(em.createQuery("SELECT o,p,d,de,lr.name,co "
                + (sql = "FROM Employee o JOIN o.people p JOIN p.documentType d "
                + "LEFT JOIN Contract co ON co.peopleId=o.peopleId AND co.active=1 AND co.canceled = FALSE "
                + "LEFT JOIN Dependency de ON de.id=co.dependencyId "
                + "LEFT JOIN LaborRegime lr ON lr.id=o.laborRegimeId "
                + "WHERE o.canceled=FALSE AND (NOT o.laborRegimeId=7 OR o.laborRegimeId IS NULL) "
                + (workModality != null ? " AND (o.workModality IN :workModality "+(workModality.contains("N")?" OR o.workModality IS NULL ":"")+") " : "")
                + (laborRegime != null ? " AND o.laborRegimeId IN :laborRegime " : "")
                + (pensionSystem != null ? " AND o.pensionSystem.id IN :pensionSystem " : "")
                + (query != null ? " AND UPPER(o.people.fullName) LIKE :filter OR UPPER(o.people.code) LIKE :filter " : "")
                + (people != null ? " AND UPPER(o.people.fullName) LIKE :people " : "")
                + (code != null ? " AND UPPER(o.people.code) LIKE :code " : "")
                    + (active != null ? " AND oo.id>0 " : "")    
                )
                
                + "  ORDER BY p.fullName ASC"));
        if (pageSize > 0) {
            ql.get(0).setFirstResult(first).setMaxResults(pageSize);
            ql.add(em.createQuery("SELECT COUNT(o) " + sql));
        }
        for (Query q : ql) {
            if (query != null) {
                q.setParameter("filter", "%" + query.toString().toUpperCase().replaceAll("\\s+", "%") + "%");
            }
            if (people != null) {
                q.setParameter("people", "%" + people.toString().toUpperCase().replaceAll("\\s+", "%") + "%");
            }
            if (code != null) {
                q.setParameter("code", "%" + code + "%");
            }
            if (laborRegime != null) {
                q.setParameter("laborRegime", laborRegime);
            }
            if (pensionSystem != null) {
                q.setParameter("pensionSystem", pensionSystem);
            }
            if (workModality != null) {
                q.setParameter("workModality", workModality);
            }
        }
        if (pageSize > 0) {
            filters.put("size", ql.get(1).getSingleResult());
        }
        return AbstractFacade.getColumn(ql.get(0).getResultList(), new RowAdapter() {
            @Override
            public Object adapting(Object[] row) {
                Employee e = (Employee) row[0];
                HashMap ext = new HashMap();
                if (row[3] != null) {
                    Dependency de = (Dependency) row[3];

                    String dependency = de.getName();
                    if (de.getType() != null) {
                        dependency = (de.getType().getName() + " " + dependency).trim();
                    }
                    ext.put("dependency", dependency);
                }
                if (row[5] != null) {
                    Contract de = (Contract) row[5];
                    if (de != null && de.getPosition() != null) {
                        ext.put("position", de.getPosition().getName());
                    }

                }
                /*if (e.getRemunerativeLevelId() != null) {
                    RemunerativeLevel remunerativeLevel = em.find(RemunerativeLevel.class, e.getRemunerativeLevelId());
                    if (remunerativeLevel != null) {
                        ext.put("remunerativeLevel", remunerativeLevel.getName());
                    }
                }*/
                e.setLaborRegimeName((String) row[4]);
                e.setExt(ext);
                return e;
            }
        });
    }

    private static String UPLOAD_DIR;

private Mono<String> getUploadDir() {
    if (UPLOAD_DIR == null) {
        return WebClient.create("http://localhost/api/system/upload-dir").get()
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(uploadDir -> UPLOAD_DIR = uploadDir); // Cache the result
    } else {
        return Mono.just(UPLOAD_DIR);
    }
}

    public void edit(Employee entity) {
        Map ext = (Map) entity.getExt();
        if (ext == null) {
            ext = Collections.EMPTY_MAP;
        }
        People people = entity.getPeople();
        if (people != null && (people.getId() == null || XUtil.booleanValue(ext.get("peopleEdit")))) {
            peopleFacade.edit(people);
        }
        if ("N".equals(entity.getWorkModality())) {
            entity.setWorkModality(null);
        }
        entity.setPeopleId(entity.getPeople().getId());
        String tempFile = (String) ext.get("src");
        if (!XUtil.isEmpty(tempFile)) {
            if (tempFile.startsWith("data")) {
                System.out.println("tempFile.split(\",\")[1]=" + tempFile.split(",")[1].trim());
                byte[] data = Base64.getDecoder().decode(tempFile.split(",")[1].trim());
                try (OutputStream stream = new FileOutputStream(new File(XFile.getFile(new File(getUploadDir() + "foto/")), entity.getPeople().getId() + ".jpg"))) {
                    stream.write(data);
                } catch (Exception ex) {
                    throw new RuntimeException("Error al guardar la foto del empleado desde RENIEC.", ex);
                }
            } else {
                try {
                    File f = new File(File.createTempFile("temp-file-name", ".tmp").getParentFile(), tempFile);
                    File f0 = new File(XFile.getFile(new File(getUploadDir() + "foto/")), entity.getPeople().getId() + ".jpg");
                    if (f0.exists()) {
                        f0.delete();
                    }
                    if (XFile.copy(f, f0) == null) {
                        throw new RuntimeException("La foto " + f + " no pudo ser guardada en " + f0);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException("Error al guardar la foto adjunta del empleado.", ex);
                }
            }
        }
        if (entity.getStatus() != null) {
            entity.setStatusId(entity.getStatus().getId());
        }
        if (entity.getTypeId() == null) {
            entity.setTypeId((short) 1);
        }
        if (entity.getStatusId() == null) {
            entity.setStatusId((short) 1);
        }
        entity.setUserId(XUtil.intValue(userFacade.getCurrentUser().getDirectoryId()));
        if (entity.getId() == null) {
            if (entity.getIncomeDate() == null) {
                entity.setIncomeDate(X.getServerDate());
            }
            em.persist(entity);
        } else {
            em.merge(entity);
        }
        /*if (entity.getRelativeList() != null) {
            for (Relative r : entity.getRelativeList()) {
                if (r.getIdPariente() != null) {
                    em.merge(r);
                } else if (!XUtil.isEmpty(r.getNombreParent()) || !XUtil.isEmpty(r.getMaternoParent())) {
                    r.setIdDirperuns(entity.getPeopleId());
                    em.persist(r);
                }
            }
        }*/

    }

    @Autowired
    private PeopleFacade peopleFacade;

    @Override
    public Employee load(Object id) {
        Object[] m = (Object[]) em.createQuery("select em,pe,do from Employee em JOIN em.people pe JOIN pe.documentType do Where em.id=:id")
                .setParameter("id", id instanceof Employee ? ((Employee) id).getId() : XUtil.intValue(id))
                .getSingleResult();
        Employee e = (Employee) m[0];
        People people = e.getPeople();
        //people.setExt(peopleFacade.loadExt(people));
        //e.setExt(people.getExt());
        List<Relative> lr = em.createQuery("SELECT r FROM Relative r WHERE r.idDirperuns=:peopleId ORDER BY r.idPprn ASC")
                .setParameter("peopleId", e.getPeopleId()).getResultList();
        List<Short> l = new ArrayList();
        l.add((short) 1);
        l.add((short) 2);
        l.add((short) 7);
        l.add((short) 9);
        if (e.getLaborRegimeId() != null) {
            LaborRegime laborRegime = em.find(LaborRegime.class, (int) e.getLaborRegimeId());
            if (laborRegime != null) {
                e.setLaborRegimeName(laborRegime.getName());
            }
        }
        /*for (Relative r : lr) {
            l.remove((Short) r.getIdPprn());
        }
        for (Short s : l) {
            Relative r = new Relative();
            r.setIdPprn(s);
            lr.add(r);
        }

        e.setRelativeList(lr);*/
        e.setCompany(em.find(Company.class, 24));
        return e;
    }

    @Override
    public List<CivilStatus> getCivilStatusList() {
        return em.createQuery("SELECT e FROM CivilStatus e ORDER BY e.nombre").getResultList();
    }

    public List getActiveEmployeList(Map m) {
        int option = XUtil.intValue(m.get("option"));
        List laborRegime = (List) m.get("laborRegime");
        List workModality = (List) m.get("workModality");
        List employee = (List) m.get("employee");
        List people = (List) m.get("people");
        Query q = em.createQuery("SELECT "
                + "de.id,"
                + "TRIM(CONCAT(de.type.name,' ',de.name)),"
                + "pn.fullName,"
                + "rl.abbreviation,"
                + "po.name,"
                + "em.nPlaza,"
                + "r2.name,"
                + "pn.code,"
                + "pn.birthdate,"
                + "em.incomeDate,"
                + "em.remuneracion,"
                + "em.incentivoLaboral,"
                + "em.observations,pn.id "
                + " FROM Employee em "
                + " LEFT join LaborRegime r2 ON r2.id=em.laborRegimeId "
                + " INNER JOIN People pn ON pn.id=em.peopleId "
                + "INNER JOIN Contract co ON co.canceled=0 AND co.peopleId=pn.id "
                + " LEFT join RemunerativeLevel rl ON rl.id=co.remunerativeLevelId "
                + "LEFT JOIN Position po ON co.positionId=po.id "
                + "LEFT JOIN Dependency de ON co.dependencyId=de.id "
                + "where em.canceled=0 AND co.active=1  "
                + (laborRegime != null ? " AND em.laborRegimeId IN :laborRegime " : " AND r2.id in (1,2,8,9,10) ")
                + (workModality != null ? " AND em.workModality IN :workModality " : "")
                + (people != null ? " AND pn.id IN :people " : "")
                + (employee != null ? " AND em.id IN :employee " : "")
                + "ORDER by 2,em.nPlaza,pn.fullName"
        );
        if (laborRegime != null) {
            q.setParameter("laborRegime", laborRegime);
        }
        if (workModality != null) {
            q.setParameter("workModality", workModality);
        }
        if (people != null) {
            q.setParameter("people", people);
        }
        if (employee != null) {
            q.setParameter("employee", employee);
        }
        List<Object[]> l = q.getResultList();
        if (option == 2) {
            l.forEach(e -> {
                Object id = e[13];
                HashMap ext = new HashMap();
                ext.put("contracts", contractFacade.load(0, 0, null, new XMap("people.id", id)));
                e[13] = ext;
            });
        }
        return l;
    }

    @Autowired
    private ContractRepository contractFacade;

    @Override
    public Object getStatusList() {
        return em.createQuery("SELECT o FROM EmployeeStatus o").getResultList();
    }

}
