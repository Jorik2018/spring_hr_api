package gob.regionancash.hr.service.impl;

import gob.regionancash.hr.model.Contract;
import gob.regionancash.hr.model.License;
import gob.regionancash.hr.service.LicenseFacade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import javax.sql.DataSource;

import org.isobit.app.service.UserService;
import org.isobit.directory.model.People;
import org.isobit.directory.service.PeopleFacade;
import org.isobit.util.AbstractFacade;
import org.isobit.util.BeanUtils;
import org.isobit.util.XDate;
import org.isobit.util.XMap;
import org.isobit.util.XUtil;
import org.isobit.util.AbstractFacade.RowAdapter;
import org.springframework.beans.factory.annotation.Autowired;

public class LicenseFacadeImpl implements LicenseFacade {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserService userFacade;

    @Override
    public List<License> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        Object filter = XUtil.isEmpty(filters.get("filter"), null);
        Object sorter = XUtil.isEmpty(filters.get("sorter"), null);
        Object type = XUtil.isEmpty(filters.get("type"), null);
        Object people = XUtil.isEmpty(filters.get("people"), null);
        Object peopleId = XUtil.isEmpty(filters.get("peopleId"), null);
        Object detail = XUtil.isEmpty(filters.get("detail"), null);
        Object document = XUtil.isEmpty(filters.get("document"), null);
        Object laborRegime = XUtil.isEmpty(filters.get("laborRegime"), null);
        List<Query> ql = new ArrayList();
        String sql;
        ql.add(em.createQuery("SELECT o,w " + (sql = "FROM License o "
                + "JOIN o.worker w "
                + (laborRegime != null ? "JOIN Employee em ON em.peopleId=o.worker AND em.laborRegimeId=:laborRegime " : "")
                + "WHERE 1=1 "
                + (peopleId != null ? " AND o.peopleId=:peopleId" : "")
                + (people != null ? (people instanceof People ? " AND w.code LIKE :people" : " AND (UPPER(w.fullName) LIKE :people OR w.code like :people)") : "")
                + (detail != null ? " AND UPPER(o.detalle) LIKE :detail" : "")
                + (type != null ? " AND o.type IN :type" : "")
                + (document != null ? " AND UPPER(o.nroSoli) LIKE :document" : ""))
                + " ORDER BY " + (sorter != null ? sorter : "o.fecSoli DESC")));
        if (pageSize > 0) {
            ql.get(0).setFirstResult(first).setMaxResults(pageSize);
            ql.add(em.createQuery("SELECT COUNT(o) " + sql));
        }
        for (Query q : ql) {
            if (filter != null) {
                q.setParameter("filter", "%" + filter.toString().toUpperCase().replace("\\s+", "%") + "%");
            }
            if (laborRegime != null) {
                if("3".equals(""+laborRegime)){
                    laborRegime=(short)9;
                }
                q.setParameter("laborRegime", laborRegime);
            }
            if (document != null) {
                q.setParameter("document", "%" + document.toString().toUpperCase().replace("\\s+", "%") + "%");
            }
            if (peopleId != null) {
                q.setParameter("peopleId", peopleId);
            }
            if (detail != null) {
                q.setParameter("detail", "%" + detail.toString().toUpperCase().replace("\\s+", "%") + "%");
            }
            if (type != null) {
                q.setParameter("type", type);
            }
            if (people != null) {
                q.setParameter("people", people instanceof People ? ("%" + ((People) people).getCode() + "%")
                        : ("%" + people.toString().toUpperCase().replaceAll("\\s+", "%") + "%"));
            }
        }
        if (pageSize > 0) {
            filters.put("size", ql.get(1).getSingleResult());
        }
        return AbstractFacade.getColumn(ql.get(0).getResultList(), new RowAdapter() {
            @Override
            public Object adapting(Object[] row) {
                License l = (License) row[0];
                String type = License.TYPE.get(l.getType());
                Object authorizationType = License.AUTHORIZACTION_TYPE.get(l.getAuthorizationType());
                authorizationType = authorizationType != null ? authorizationType : l.getAuthorizationType();
                l.setDescription(((type != null ? type : l.getType()) + " " + (authorizationType != null ? authorizationType : "")).trim());
                People p = (People) row[1];
                em.detach(p);
                p.setDocumentType(null);
                return l;
            }

        });
    }

    @Override
    public void edit(License entity) {
        if (entity.getHoraSoli() == null) {
            entity.setHoraSoli("");
        }
        if ("P".equals(entity.getType()) || "O".equals(entity.getType())) {
            if (entity.getNroSoli() == null) {
                entity.setNroSoli("");
            }
            if (entity.getFecIni() == null) {
                entity.setFecIni(entity.getFecSoli());
            }
            if (entity.getFecFin() == null) {
                entity.setFecFin(entity.getFecSoli());
            }
        }
        entity.setDays(entity.getFecFin() == null ? 0 : XDate.getDaysDiff(
                entity.getFecIni().getTime(),
                entity.getFecFin().getTime()) + 1);
        if (XUtil.isEmpty(entity.getEstaSoli())) {
            entity.setEstaSoli("A");
        }
        //Si es vacacion escoger 
        if (entity.getType().equals("V")) {
            if (XUtil.isEmpty(entity.getCodMovi())) {
                throw new RuntimeException("Cuando se registran licencias por vacaciones es requerido un periodo anual");
            }
            int i = XUtil.intValue(entity.getCodMovi());
            entity.setDetalle("VACACIONES " + (2000 + i)
            ///+" - "+entity.getDays()+" DÍAS"
            );
        } else {
            entity.setCodMovi(entity.getType());
        }

        if (entity.getId() == null) {
            //entity.setPlaza(entity.getWorker().getCode());
            entity.setId(1 + XUtil.intValue(em.createQuery("SELECT MAX(l.id) FROM License l").getSingleResult()));
            em.persist(entity);
        } else {
            em.merge(entity);
        }
        Map m = (Map) entity.getFile();
        //DruFile file = new DruFile();
        System.out.println("m=" + m);
    }

    @Override
    public List getPeriodList(Integer peopleId) {
        String Y = "" + XDate.getYear(new Date());
        String c = "" + XDate.getYear(new Date()) % 1000;
        boolean ok = false;
        List<Object[]> r = em.createQuery("SELECT DISTINCT o.codMovi,o.detalle,30 FROM License o WHERE o.type='V' ORDER BY detalle DESC").getResultList();
        Map m2 = new HashMap();
        List<Object[]> l2 = new ArrayList();
        for (Object[] mr : r) {
            //Se revisa cada periodo si contiene el año actual
            if (mr[1] != null && mr[1].toString().contains(Y)) {
                ok = true;
            }
            m2.put(mr[0], mr);
            if (XUtil.intValue(mr[0]) > 18) {
                l2.add(mr);
            }
        }
        //Se cargan en m2 todos los periodos disponibles pro solo deberian estar 
        //los de años donde el trabajador ha estado trabajando y considerar un tiempo proporcional
        //Sie el periodo no existe se agregara een la lista para que se utilice en 
        if (!ok) {
            l2.add(0, new Object[]{c, "VACACIONES " + Y, 30});
        }
        //En la parte anterior se asegura q exista el periosdo actual

        r = l2;
        Date fechaIng = null;
        try {
            fechaIng = (Date) em.createQuery("SELECT MIN(e.incomeDate) FROM Employee e WHERE e.peopleId=:people AND e.canceled=0")
                    .setParameter("people", peopleId).getSingleResult();
        } catch (NoResultException e) {
        }
        //Aqui se calcula la cantidad de dias usados por codMovi de un trabajador
        Calendar ca = Calendar.getInstance();
        if (fechaIng != null) {
            Date d = new Date();
            ca.setTime(fechaIng);
            ca.add(Calendar.YEAR, 1);
            int year = ca.get(Calendar.YEAR);
            if (!ca.getTime().after(d)) {
                if (year == 2020) {
//                    if (r[9] == null) {
//                        r[9] = 0;
//                    }
                }
                if (year == 2021) {
//                    if (r[10] == null) {
//                        r[10] = 0;
//                    }
                }
            }
            ca.add(Calendar.YEAR, 1);
            year = ca.get(Calendar.YEAR);
            if (!ca.getTime().after(d)) {
                if (year == 2021) {
//                    if (r[10] == null) {
//                        r[10] = 0;
//                    }
                }
            }
        }
        for (Object mm : m2.values()) {

            System.out.println("mm=" + XUtil.implode((Object[]) (mm), " | "));
        }

        for (Object[] r2 : (List<Object[]>) em.createQuery("SELECT o.codMovi,DATEDIFF(o.fecFin,o.fecIni)+1 FROM License o WHERE o.type='V' AND o.worker.id=:peopleId")
                .setParameter("peopleId", peopleId)
                .getResultList()) {
            //Se busca el perido para descontar los dias usados
            Object rr[] = (Object[]) m2.get(r2[0]);

            System.out.println("dias=" + r2[1] + " r2=" + r2[0] + " mm=" + XUtil.implode((Object[]) (rr), " | "));

            int n = XUtil.intValue(rr[2]) - XUtil.intValue(r2[1]);
            //Se descuenta los dias del registro y se vulve
            rr[2] = n < 0 ? 0 : n;
        }
        return r;
    }

    @Override
    public void prepareCreate(License l) {
        People p = l.getWorker();
        Map m = (Map) l.getExt();
        String Y = "" + XDate.getYear(new Date());
        String c = "" + XDate.getYear(new Date()) % 1000;

        boolean ok = false;
        List<Object[]> r = em.createQuery("SELECT DISTINCT o.codMovi,o.detalle,30 FROM License o ORDER BY detalle").getResultList();
        Map m2 = new HashMap();
        for (Object[] mr : r) {
            if (mr[1] != null && mr[1].toString().contains(Y)) {
                ok = true;
            }
            m2.put(mr[0], mr);
        }
        System.out.println("ok=" + ok);
        if (!ok) {
            r.add(new Object[]{c, "PERIODO " + Y, 30});
        }
        for (Object[] r2 : (List<Object[]>) em.createQuery("SELECT o.codMovi,(o.fecFin-o.fecIni)+1 FROM License o WHERE o.worker=:worker")
                .setParameter("worker", p)
                .getResultList()) {
            Object rr[] = (Object[]) m2.get(r2[0]);

            int n = XUtil.intValue(rr[2]) - XUtil.intValue(r2[1]);
            if (n < 0) {
                n = 0;
            }
            rr[2] = n;
        }
        m.put("periodList", r);
    }

    @Override
    public License load(Object m) {
        License l = (License) ((Object[]) em.createQuery("SELECT l,w FROM License l JOIN l.worker w WHERE l.id=:id")
                .setParameter("id", m instanceof License ? ((License) m).getId() : XUtil.intValue(m)
                ).getSingleResult())[0];
        l.setExt(new HashMap());
        People p = l.getWorker();
        if (p != null) {
            p = em.find(People.class, p.getId());
            em.detach(p);
            l = BeanUtils.clone(l);
            p.setDocumentType(null);
        }
        this.prepareCreate(l);
        return l;
    }

    @Autowired
    private PeopleFacade peopleFacade;

    @Override
    public Object getContent(Map m) {
        /*System.out.println("m=" + m);
        Map p = new HashMap();
        p.putAll(m);
        if (p.containsKey("FORMAT")) {
            p.put(JR.EXTENSION, p.get("FORMAT"));
        }
        int option = XUtil.intValue(p.get("option"));
        List data = null;
        List l = peopleFacade.load(0, 0, null, m);
        if (option == 1) {
            p.put("people", l.get(0));
            //Aqui se lista todos los registros de people
            data = this.load(0, 0, null, p);
        } else {
            //Aqui se lista todas las marcaciones de people
            if (!l.isEmpty()) {
                p.put("PEOPLE", l.get(0));
            }
            Object from = m.get("from");
            Object to = m.get("to");
            Query q = em.createQuery("SELECT r,DATE(r.dateTime) FROM RecordAttendance r WHERE r.peopleId=:peopleId"
                    + (from != null ? " AND DATE(r.dateTime)>=:from" : "")
                    + (to != null ? " AND DATE(r.dateTime)<=:to" : "") + " ORDER BY 2 DESC,r.dateTime");
            if (from != null) {
                q.setParameter("from", from);
            }
            if (to != null) {
                q.setParameter("to", to);
            }
            data = AbstractFacade.getColumn(q
                    .setParameter("peopleId", XUtil.intValue(m.get("peopleId"))).getResultList());
        }

        XDate.setDefaultFormat("dd/MM/yyyy");
        if (p.containsKey("FECHA_FIN")) {
            p.put("FECHA_FIN", XDate.format((Date) p.get("FECHA_FIN")));
        }
        if (p.containsKey("FECHA_INI")) {
            p.put("FECHA_INI", XDate.format((Date) p.get("FECHA_INI")));
        }

        //p.put(JR.IS_ONE_PAGE_PER_SHEET, false);
        System.out.println("data.size=" + data.size());
        p.put(DataSource.class, data);
        String jr;
        switch (option) {
            case 1:
                jr = "license";
                break;
            default:
                jr = "record_attendance_by_people";
        }
        System.out.println("/gob/regionancash/rh/jr/" + jr + ".jasper");
        Object o = JR.open("/gob/regionancash/rh/jr/" + jr + ".jasper", p);
        System.out.println("o2=" + o);
        return o;*/
        return null;
    }

    @Override
    public Object report() {
        List<Contract> cl = em.createQuery("SELECT c FROM Contract c WHERE c.active=1").getResultList();
        return null;
    }

    @Override
    public List getLicenseSummaryList(Map<String, Object> filters) {
        //https://thorben-janssen.com/database-functions/
        int laborRegime = XUtil.intValue(filters.get("laborRegime"));
        String labor = "";
        switch (laborRegime) {
            case 0:
                labor = "(1,2,3,6,8,9,10)";
                filters.put("laborRegime", "TODOS");
                break;
            case 1:
                labor = "(2,3,6,8,10)";
                filters.put("laborRegime", "NOMBRADOS, MEDIDA CAUTELAR Y SENTENCIA JUDICIAL");
                break;
            case 2:
                labor = "(1)";
                filters.put("laborRegime", "CAS");
            case 3:
                labor = "(9)";
                filters.put("laborRegime", "CAS DIRECTIVOS");
                break;
        }

        List<Object[]> l = em.createQuery("SELECT "
                + "pn.code,"
                + "pn.fullName,"
                + "pn.id,"
                + "em.incomeDate,"
                + "de.id,"
                + "CONCAT(dt.name,' ',de.name),"
                + "po.name,"
                + "GROUP_CONCAT(li.nroSoli,', '),"
                + "sum(CASE WHEN li.codMovi='20' THEN li.days ELSE null END), \n"
                + "sum(CASE WHEN li.codMovi='21' THEN li.days ELSE null END), \n"
                + "sum(CASE WHEN li.codMovi='22' THEN li.days ELSE null END), \n"
                + "sum(CASE WHEN li.codMovi='23' THEN li.days ELSE null END), \n"
                + "sum(CASE WHEN li.codMovi='24' THEN li.days ELSE null END), \n"
                + "floor(datediff(curdate(),em.incomeDate) / 365) \n"
                + "FROM Contract c "
                + "JOIN Employee em ON em.peopleId=c.peopleId "
                + "JOIN People pn ON pn.id=c.peopleId "
                + "LEFT JOIN c.position po "
                + "LEFT JOIN c.dependency de "
                + "LEFT JOIN de.type dt "
                + "LEFT JOIN License li ON li.worker.id=c.peopleId AND li.type='V' AND li.codMovi>='20' "
                + "WHERE c.active=True AND c.canceled=False AND NOT de.name IS NULL AND em.laborRegimeId "
                + "IN  " + labor
                + " AND em.canceled=False "
                + "group by pn.code,pn.fullName,pn.id,em.incomeDate,de.id,dt.name,de.name,po.name\n"
                + "ORDER BY de.id,pn.fullName").getResultList();
        Calendar c = Calendar.getInstance();
        for (Object[] r : l) {
            int y = XUtil.intValue(r[11]);
            if ((Date) r[3] != null) {
                Date d = new Date();

                c.setTime((Date) r[3]);
                int year = c.get(Calendar.YEAR);
                if (year < 2019) {
                    c.set(Calendar.YEAR, 2019);
                }
                //si el año es menor al actual 2019 se debe avanzar

                c.add(Calendar.YEAR, 1);
                year = c.get(Calendar.YEAR);
                //fecha de ingreso no es despues de hoy
                if (!c.getTime().after(d)) {
                    if (year == 2020) {
                        if (r[9] == null) {
                            r[9] = 0;
                        }
                    }
                    if (year == 2021) {
                        if (r[10] == null) {
                            r[10] = 0;
                        }
                    }
                }
                c.add(Calendar.YEAR, 1);
                year = c.get(Calendar.YEAR);
                if (!c.getTime().after(d)) {
                    if (year == 2021) {
                        if (r[10] == null) {
                            r[10] = 0;
                        }
                    }
                }
            }

            //if(y>2&&r[8]==null)r[8]=0;
        }
        return l;
    }
//            return em.createQuery("SELECT li.plaza,pn.fullName,li.peopleIdLong,\n" +
//"de.id,CONCAT(dt.name,' ',de.name),p.name,GROUP_CONCAT(li.nroSoli)," +
//"sum(CASE WHEN li.codMovi=18 THEN li.days ELSE null END),\n" +
//"sum(CASE WHEN li.codMovi=19 THEN li.days ELSE null END),\n" +
//"sum(CASE WHEN li.codMovi=20 THEN li.days ELSE null END) \n" +
//"FROM License li\n" +
//"LEFT OUTER JOIN People pn ON 1*pn.code=1*plaza\n" +
//"LEFT OUTER JOIN Contract c ON c.peopleId=li.worker.id\n" +
//"LEFT JOIN c.position p " +
//"LEFT JOIN c.dependency de " +
//"LEFT JOIN de.type dt \n" +
//"WHERE li.type='V' AND li.codMovi>=18 AND c.active=1\n" +
//"group by li.plaza,pn.fullName,li.peopleIdLong,\n" +
//"de.id,de.name,p.name\n" +
//"ORDER BY pn.fullName").getResultList();
//    }

    @Override
    public void remove(Optional<License> findById) {
        
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

}
