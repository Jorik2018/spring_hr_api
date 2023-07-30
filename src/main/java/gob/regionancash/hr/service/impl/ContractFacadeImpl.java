package gob.regionancash.hr.service.impl;

import org.isobit.directory.model.Dependency;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import org.isobit.app.X;
import org.isobit.app.model.User;
import org.isobit.app.service.UserFacade;
import org.isobit.directory.model.Company;
import org.isobit.directory.model.DependencyType;
import org.isobit.directory.model.DocumentType;
import org.isobit.directory.model.People;
import org.isobit.directory.service.CompanyFacade;
import org.isobit.directory.service.PeopleFacade;
import org.isobit.util.AbstractFacade;
import org.isobit.util.XMap;
import org.isobit.util.XUtil;
import org.springframework.beans.factory.annotation.Autowired;

import gob.regionancash.hr.model.Contract;
import gob.regionancash.hr.model.Position;
import gob.regionancash.hr.model.RemunerativeLevel;
import gob.regionancash.hr.service.ContractFacade;

public class ContractFacadeImpl implements ContractFacade {

    public static void main(String[] args) throws MalformedURLException, IOException {
        System.out.println((int) 20301 / 100);
        System.out.println(((int) 20301 / 100) % 100);
        /*String urlParameters = "aubigeo=021809";
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        String request = "http://sige.inei.gob.pe/test/atlas/index.php/ubicacion/consultar_centropoblado";
        URL url = new URL(request);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);
        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.write(postData);
        }*/
    }

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private PeopleFacade peopleFacade;

    @Autowired
    private CompanyFacade companyFacade;

    @Override
    public List<Contract> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        Object dependency = XUtil.isEmpty(filters.get("dependency"), null);
        System.out.println("dependency=" + dependency);
        Object query = XUtil.isEmpty(filters.get("query"), null);
        Object code = XUtil.isEmpty(filters.get("code"), null);
        Object company = XUtil.isEmpty(filters.get("company"), null);
        Object people = XUtil.isEmpty(filters.get("people"), null);
        Object peopleId = XUtil.isEmpty(filters.get("people.id"), null);
        Boolean active = XUtil.booleanValue(filters.get("active"));
        Object position = XUtil.isEmpty(filters.get("position"), null);
        Object level = XUtil.isEmpty(filters.get("level"), null);
        X.DEBUG = true;
        X.log(filters);
        int option = XUtil.intValue(filters.get("option"));
        if (option == 2) {
            return loadUnidadEjecutoraList();
        }
        List<Query> ql = new ArrayList();
        String sql;
//        em.createQuery("SELECT o FROM Contract o WHERE o.");
        ql.add(em.createQuery("SELECT o,do,pe " + (sql = "FROM Contract o JOIN o.people pe LEFT JOIN pe.documentType do LEFT JOIN o.position p " + (dependency instanceof String ? "JOIN o.dependency d " : "") + "WHERE o.canceled=false "
                //+ (active ? " AND o.fechaIni<=:hoy AND (o.fechaFin>=:hoy OR o.fechaFin IS NULL)" : "")
                + (active ? " AND o.active=1" : "")
                + (level != null ? " AND p.level=:level" : "")
                + (code != null ? " AND UPPER(o.people.code) LIKE :code" : "")
                + (company != null ? " AND (o.company.businessName LIKE :company OR o.company.ruc LIKE :company)" : "")
                + (position != null ? " AND " + (position instanceof String ? "UPPER(p.name) LIKE :position" : (position instanceof Position ? "o.position" : "o.position.id") + "=:position") : "")
                + (dependency != null ? " AND " + (dependency instanceof String ? "UPPER(CONCAT(d.type.name,d.name)) LIKE :dependency" : (dependency instanceof Dependency ? "o.dependency" : "o.dependency.id") + "=:dependency") : "")
                + (people != null ? " AND " + (people instanceof People ? "o.people=" : (people instanceof Number ? "o.people.id=" : "UPPER(o.people.fullName) LIKE ")) + ":people" : "")
                + (peopleId != null ? " AND o.peopleId=:peopleId" : "")
                + (query != null ? " AND (UPPER(o.people.fullName) LIKE :query OR UPPER(o.people.code) LIKE :query)" : ""))
                + " ORDER BY o.active DESC,o.fechaIni DESC"));
        if (pageSize > 0) {
            ql.get(0).setFirstResult(first).setMaxResults(pageSize);
            ql.add(em.createQuery("SELECT COUNT(o) " + sql));
        }
        for (Query q : ql) {
            if (peopleId != null) {
                q.setParameter("peopleId", peopleId);
            }
            if (level != null) {
                q.setParameter("level", level);
            }
            if (code != null) {
                q.setParameter("code", "%" + code + "%");
            }
            if (active) {
                //q.setParameter("hoy", X.getServerDate());
            }
            if (query != null) {
                q.setParameter("query", "%" + query.toString().toUpperCase().replace(" ", "%") + "%");
            }
            if (people != null) {
                if (people instanceof Number) {
                    q.setParameter("people", people);
                } else {
                    q.setParameter("people", people instanceof People ? people : ("%" + people.toString().toUpperCase().replace(" ", "%") + "%"));
                }
            }
            if (company != null) {
                q.setParameter("company", company instanceof Company ? company : ("%" + company + "%"));
            }
            if (dependency != null) {
                if (dependency instanceof String) {
                    q.setParameter("dependency", "%" + ((String) dependency).toUpperCase().replace(" ", "%") + "%");
                } else {
                    q.setParameter("dependency", dependency instanceof Dependency ? dependency : XUtil.intValue(dependency));
                }
            }
            if (position != null) {
                if (position instanceof String) {
                    q.setParameter("position", "%" + ((String) position).toUpperCase() + "%");
                } else {
                    q.setParameter("position", position instanceof Position ? position : XUtil.intValue(position));
                }
            }
        }
        if (pageSize > 0) {
            filters.put("size", ql.get(1).getSingleResult());
        }
        List<Contract> l = AbstractFacade.getColumn(ql.get(0).getResultList());
        List provinceIdList = new ArrayList();
        List districtIdList = new ArrayList();
        for (Contract c : l) {
            if (c.getRemunerativeLevelId() != null) {
                RemunerativeLevel remunerativeLevel = em.find(RemunerativeLevel.class, c.getRemunerativeLevelId());
                if (remunerativeLevel != null) {
                    c.setRemunerativeLevelName(remunerativeLevel.getName());
                }
            }
            String ubigeo = c.getProvinceId();
            if (ubigeo != null) {
                if (ubigeo.length() > 4) {
                    districtIdList.add(c.getProvinceId());
                } else {
                    provinceIdList.add(c.getProvinceId());
                }
            }
        }
        Map<String, Object[]> m = new HashMap();
        if (!provinceIdList.isEmpty()) {
            for (Object[] d : (List<Object[]>) em.createQuery("SELECT p.code,p.name FROM Province p WHERE p.code IN :code")
                    .setParameter("code", provinceIdList).getResultList()) {
                m.put(d[0].toString(), d);
            }
        }
        if (!districtIdList.isEmpty()) {
            for (Object[] d : (List<Object[]>) em.createQuery("SELECT d.code,p.name,d.name FROM District d JOIN d.province p WHERE d.code IN :code")
                    .setParameter("code", districtIdList).getResultList()) {
                m.put(d[0].toString(), d);
            }
        }
        if (!m.isEmpty()) {
            for (Contract c : l) {
                HashMap h = new HashMap();
                Object[] d = m.get(c.getProvinceId());
                if (d != null) {
                    h.put("ubigeo", d.length > 2 ? d[1] + "/" + d[2] : d[1]);
                }
                c.setExt(h);
            }
        }
        return l;
    }

    @Override
    public List<Object> peopleList(int first, int pageSize, String sortField, Map<String, Object> filters) {
        Object dependency = XUtil.isEmpty(filters.get("dependency"), null);
        Object query = XUtil.isEmpty(filters.get("query"), null);
        Object code = XUtil.isEmpty(filters.get("code"), null);
        Object company = XUtil.isEmpty(filters.get("company"), null);
        Object people = XUtil.isEmpty(filters.get("people"), null);
        Boolean active = XUtil.booleanValue(filters.get("active"));
        Object position = XUtil.isEmpty(filters.get("position"), null);
        Object level = XUtil.isEmpty(filters.get("level"), null);
        X.DEBUG = true;
        X.log(filters);
        int option = XUtil.intValue(filters.get("option"));
        if (option == 2) {
            //return loadUnidadEjecutoraList();
        }
        List<Query> ql = new ArrayList();
        String sql;
//        em.createQuery("SELECT o FROM Contract o WHERE o.active=1");
        ql.add(em.createQuery("SELECT o,do " + (sql = "FROM Contract o JOIN o.people pe LEFT JOIN pe.documentType do LEFT JOIN o.position p " + (dependency instanceof String ? "JOIN o.dependency d " : "") + "WHERE o.canceled=0 "
                //+ (active ? " AND o.fechaIni<=:hoy AND (o.fechaFin>=:hoy OR o.fechaFin IS NULL)" : "")
                + (active ? " AND o.active=1" : "")
                + (level != null ? " AND p.level=:level" : "")
                + (code != null ? " AND UPPER(o.people.code) LIKE :code" : "")
                + (company != null ? " AND (o.company.razonSocial LIKE :company OR o.company.ruc LIKE :company)" : "")
                + (position != null ? " AND " + (position instanceof String ? "UPPER(p.name) LIKE :position" : (position instanceof Position ? "o.position" : "o.position.id") + "=:position") : "")
                + (dependency != null ? " AND " + (dependency instanceof String ? "UPPER(CONCAT(d.type.name,d.name)) LIKE :dependency" : (dependency instanceof Dependency ? "o.dependency" : "o.dependency.id") + "=:dependency") : "")
                + (people != null ? " AND " + (people instanceof People ? "o.people=" : "UPPER(o.people.fullName) LIKE ") + ":people" : "")
                + (query != null ? " AND (UPPER(o.people.fullName) LIKE :query OR UPPER(o.people.code) LIKE :query)" : ""))
                + " ORDER BY o.active DESC,o.fechaIni DESC"));
        if (pageSize > 0) {
            ql.get(0).setFirstResult(first).setMaxResults(pageSize);
            ql.add(em.createQuery("SELECT COUNT(o) " + sql));
        }
        for (Query q : ql) {
            if (level != null) {
                q.setParameter("level", level);
            }
            if (code != null) {
                q.setParameter("code", "%" + code + "%");
            }
            if (active) {
                //q.setParameter("hoy", X.getServerDate());
            }
            if (query != null) {
                q.setParameter("query", "%" + query.toString().toUpperCase().replace(" ", "%") + "%");
            }
            if (people != null) {
                q.setParameter("people", people instanceof People ? people : ("%" + people.toString().toUpperCase().replace(" ", "%") + "%"));
            }
            if (company != null) {
                q.setParameter("company", company instanceof Company ? company : ("%" + company + "%"));
            }
            if (dependency != null) {
                if (dependency instanceof String) {
                    q.setParameter("dependency", "%" + ((String) dependency).toUpperCase() + "%");
                } else {
                    q.setParameter("dependency", dependency instanceof Dependency ? dependency : XUtil.intValue(dependency));
                }
            }
            if (position != null) {
                if (position instanceof String) {
                    q.setParameter("position", "%" + ((String) position).toUpperCase() + "%");
                } else {
                    q.setParameter("position", position instanceof Position ? position : XUtil.intValue(position));
                }
            }
        }
        if (pageSize > 0) {
            filters.put("size", ql.get(1).getSingleResult());
        }
        List<Contract> l = AbstractFacade.getColumn(ql.get(0).getResultList());
        List provinceIdList = new ArrayList();
        List districtIdList = new ArrayList();
        for (Contract c : l) {
            String ubigeo = c.getProvinceId();
            if (ubigeo != null) {
                if (ubigeo.length() > 4) {
                    districtIdList.add(c.getProvinceId());
                } else {
                    provinceIdList.add(c.getProvinceId());
                }
            }
        }
        Map<String, Object[]> m = new HashMap();
        if (!provinceIdList.isEmpty()) {
            for (Object[] d : (List<Object[]>) em.createQuery("SELECT p.code,p.name FROM Province p WHERE p.code IN :code")
                    .setParameter("code", provinceIdList).getResultList()) {
                m.put(d[0].toString(), d);
            }
        }
        if (!districtIdList.isEmpty()) {
            for (Object[] d : (List<Object[]>) em.createQuery("SELECT d.code,p.name,d.name FROM District d JOIN d.province p WHERE d.code IN :code")
                    .setParameter("code", districtIdList).getResultList()) {
                m.put(d[0].toString(), d);
            }
        }
        if (!m.isEmpty()) {
            for (Contract c : l) {
                HashMap h = new HashMap();
                Object[] d = m.get(c.getProvinceId());
                if (d != null) {
                    h.put("ubigeo", d.length > 2 ? d[1] + "/" + d[2] : d[1]);
                }
                c.setExt(h);
            }
        }
        return null;
    }

    @Override
    public void edit(Contract entity) {
        System.out.println("CONTRACT EDIT");
        Map ext = (Map) entity.getExt();
        People people = entity.getPeople();
        if (people.getId() == null) {
            people.setDocumentType(em.find(DocumentType.class, people.getDocumentType().getId()));
            peopleFacade.edit(people);
        }
        entity.setPeopleId(people.getId());
        Company company = entity.getCompany();
        if (company.getId() == null) {
            companyFacade.edit(company);
        }
        entity.setCompanyId(company.getId());
        entity.setProvinceId(""
                + (XUtil.intValue(ext.get("idDpto")) * 10000
                + XUtil.intValue(ext.get("idProv")) * 100));
        if (XUtil.intValue(ext.get("jurisdiction")) > 0) {
            entity.setProvinceId(ext.get("jurisdiction").toString());
        }
        Object crud = ext.get("crud");
        /*ContractFacadeImpl.CrudModule crufFacade = null;
        if (crud != null) {
            crufFacade = getModule(ContractFacadeImpl.CrudModule.class, crud);
            crufFacade.beforeEdit(entity);
        }*/
        Map dependencyMap = (Map) ext.get("dependency");
        if (dependencyMap != null) {
            Dependency dependency = new Dependency();
            dependency.setName((String) dependencyMap.get("name"));
            Map dependencyTypeMap = (Map) dependencyMap.get("type");
            dependency.setType(em.find(DependencyType.class, XUtil.intValue(dependencyTypeMap.get("id"))));
            dependency.setStatus("1");
            dependency.setFechaReg(X.getServerDate());
            em.persist(dependency);
            entity.setDependencyId(dependency.getId());
        }
        Map positionMap = (Map) ext.get("position");
        if (positionMap != null) {
            Position position = new Position();
            position.setName((String) positionMap.get("name"));
            position.setLevel("");
            position.setNivel(0);
            position.setOrdenFirma(1);
            em.persist(position);
            entity.setPositionId(position.getId());
        }

        if (entity.getId() == null) {
            entity.setFechaReg(X.getServerDate());
            entity.setUserId(0);
            em.persist(entity);
        } else {
            em.merge(entity);
        }
        System.out.println("SAVED CONTRACT=" + entity.getId());
        if (XUtil.booleanValue(ext.get("createAccount"))) {
            User u = userFacade.getUserByDir(entity.getPeopleId());
            people = entity.getPeople();
            Object pass = ext.get("pass");
            Object confirm = ext.get("pass");
            if (u == null) {
                u = new User();
                u.setStatus((short) 1);
                u.setName(entity.getPeople().getCode());
                if (!XUtil.isEmpty(people.getMail())) {
                    u.setMail(people.getMail());
                } else {
                    u.setMail(u.getName() + "@mail.org");
                }
            }
            if (pass == null) {
                pass = u.getName();
                confirm = u.getName();
            }
            /*u.setExt(new XMap(
                    "clave", pass,
                    "confirm", confirm,
                    "people", people
            ));*/
            u.setStatus((short) (XUtil.booleanValue(ext.get("status")) ? 1 : 0));
            userFacade.edit(u);
        } else {
            User u = userFacade.getUserByDir(entity.getPeopleId());
            if (u != null && XUtil.booleanValue(ext.get("hasAccount"))) {
                short status = ext.get("status") != null ? ((short) (XUtil.booleanValue(ext.get("status")) ? 1 : 0)) : u.getStatus();
                boolean confirm = !XUtil.isEmpty(ext.get("confirm"));
                if (confirm || u.getStatus() != status) {
                    Map m = new HashMap();
                    if (confirm) {
                        m.put("clave", ext.get("pass"));
                        m.put("confirm", ext.get("confirm"));
                    }
                    //u.setExt(m);
                    u.setStatus(status);
                    userFacade.edit(u);
                }
            }
            //Si mando clave es porq ya existe pero se actualizara
        }
        /*if (crufFacade != null) {
            crufFacade.afterEdit(entity);
        }*/
    }

    private List<Contract> loadUnidadEjecutoraList() {
        List l = new ArrayList();
        for (Object[] r : (List<Object[]>) em.createQuery("SELECT o,e FROM DrtRelation r JOIN r.otherCompany o LEFT JOIN Contract e ON e.company=o")
                .getResultList()) {
            Company c = (Company) r[0];
            Contract co = (Contract) r[1];
            if (co != null) {
                em.detach(co);
            } else {
                co = new Contract();
            }
            co.setCompany(c);
            l.add(co);
        }
        return l;
    }

    @Override
    public Contract load(Object o) {
        Contract c = (Contract) ((Object[]) em.createQuery("SELECT c,p,d FROM Contract c JOIN c.people p JOIN p.document d WHERE c.id=:id")
                .setParameter("id", o instanceof Contract ? ((Contract) o).getId() : XUtil.intValue(o))
                .getSingleResult())[0];
        Map m = new HashMap();
        String ubigeo = c.getProvinceId();

        if (!XUtil.isEmpty(ubigeo)) {
            int p = XUtil.intValue(ubigeo);
            m.put("idDpto", (int) p / 10000);
            m.put("idProv", ((int) p / 100) % 100);
            m.put("jurisdiction", ubigeo);
        }
        List<Object[]> l = em.createQuery("SELECT COUNT(u),MAX(u.status) FROM User u WHERE u.idDir=:peopleId").setParameter("peopleId", c.getPeopleId()).getResultList();
        if (!l.isEmpty()) {
            m.put("hasAccount", XUtil.intValue(l.get(0)[0]) > 0);
            m.put("status", XUtil.intValue(l.get(0)[1]) > 0);
        }
        c.setExt(m);
        return c;
    }

    @Override
    public List<Contract> getConsejeroList(Map m) {
        List l = new ArrayList();
        List<Object[]> l2 = em.createQuery("SELECT c,pe,pr.name,co.id,pe.emailPrin FROM Contract c JOIN c.people pe,Province pr INNER JOIN Company co ON co.ruc=pr.governmentId WHERE c.position.id=72 AND c.provinceId=pr.code*100 ORDER BY pr.name").getResultList();
        for (Object[] r : l2) {
            Contract con = (Contract) r[0];
            if (r[4] != null) {
                //System.out.println(X.gson.toJson(((String) r[4]).split("\\r?\\n")));
                r[4] = ((String) r[4]).split("\\r?\\n")[0];
            }
            con.setExt(r);
            l.add(con);
        }
        return l;
    }

    @Override
    public List getDataSet(int i, XMap map) {
        return em.createNativeQuery("SELECT l.id,'P',l.create_date,pn.id_dir,pn.numero_pndid,pn.nombre_completo,concat(dt.nombre_dep_tipo,' ',d.nombre_dep) dependency,p.name position FROM marking l \n"
                + " LEFT OUTER JOIN drt_personanatural pn ON pn.id_dir=l.people_id  \n"
                + " LEFT OUTER JOIN org_dependencia d ON d.id_dep=l.dependency_id\n"
                + " LEFT OUTER JOIN org_dep_tipo dt ON dt.id_dep_tipo=d.id_dep_tipo\n"
                + " LEFT OUTER JOIN position p ON p.id=l.position_id\n"
                + " WHERE company_id=20530689019\n"
                + " UNION "
                + " SELECT l.id,'W',l.action_date,pn.id_dir,pn.numero_pndid,pn.nombre_completo,concat(dt.nombre_dep_tipo,' ',d.nombre_dep) dependency,p.name position FROM dru_login l \n"
                + " LEFT OUTER JOIN dru_users u ON u.uid=-l.people_id \n"
                + " LEFT OUTER JOIN drt_personanatural pn ON pn.id_dir=l.people_id  \n"
                + " LEFT OUTER JOIN org_dependencia d ON d.id_dep=l.dependency_id\n"
                + " LEFT OUTER JOIN org_dep_tipo dt ON dt.id_dep_tipo=d.id_dep_tipo\n"
                + " LEFT OUTER JOIN position p ON p.id=l.position_id\n"
                + " WHERE company_id=20530689019\n"
                + " ORDER by 5,3 DESC").getResultList();
    }

    @Override
    public void remove(Integer id) {
        Contract c = em.find(Contract.class,id);
        c.setCanceled(true);
        em.merge(c);
    }

}
