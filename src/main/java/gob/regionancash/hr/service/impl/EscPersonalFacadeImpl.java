package gob.regionancash.hr.service.impl;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import javax.sql.DataSource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import org.isobit.app.X;
import org.isobit.directory.model.Company;
import org.isobit.directory.model.Dependency;
import org.isobit.util.AbstractFacade;
import org.isobit.util.XDate;
import org.isobit.util.XMap;
import org.isobit.util.XUtil;

import gob.regionancash.hr.escalafon.model.Demerit;
import gob.regionancash.hr.escalafon.model.EscCapacitacion;
import gob.regionancash.hr.escalafon.model.EscCarreraLaboral;
import gob.regionancash.hr.escalafon.model.EscContrato;
import gob.regionancash.hr.escalafon.model.EscContratoDoc;
import gob.regionancash.hr.escalafon.model.EscContratoOtro;
import gob.regionancash.hr.escalafon.model.EscEncargatura;
import gob.regionancash.hr.escalafon.model.EscNombramiento;
import gob.regionancash.hr.escalafon.model.EscNombramientoDoc;
import gob.regionancash.hr.escalafon.model.EscPromocion;
import gob.regionancash.hr.escalafon.model.EscPromocionDoc;
import gob.regionancash.hr.escalafon.model.EscReposicion;
import gob.regionancash.hr.escalafon.model.EscRotacion;
import gob.regionancash.hr.escalafon.model.Study2;
import gob.regionancash.hr.escalafon.model.TiempoServicioDet;
import gob.regionancash.hr.model.Employee;
import gob.regionancash.hr.model.EscTipoPersonal;
import gob.regionancash.hr.model.LicensePeople;
import gob.regionancash.hr.service.EscPersonalFacade;

public class EscPersonalFacadeImpl implements EscPersonalFacade {

    @PersistenceContext
    private EntityManager em;

    /*private DependencyFacade.DependencyModule dependencyModule = new DependencyModule() {

        @Override
        public Dependency load(Dependency dependency) {
            Map ext = (Map) dependency.getExt();
            ext.put("escCarreraLaboral",
                    em.createQuery("SELECT c FROM EscCarreraLaboral c JOIN c.employee p JOIN p.people pn WHERE c.jefe=TRUE AND c.estado=TRUE AND c.dependency=:dependency ORDER BY pn.nombreCompleto")
                            .setParameter("dependency", dependency).getResultList());
            return dependency;
        }
    };*/

    public List getList(Dependency dependency, String cls) {
        if (EscCarreraLaboral.class.getSimpleName().equals(cls)) {
            return AbstractFacade
            .getColumn(em.createQuery("SELECT c,p,pn,cap,n FROM EscCarreraLaboral c JOIN c.escPersonal p JOIN p.drtPersonaNatural pn LEFT JOIN c.escCapUns cap LEFT JOIN cap.escNivelRemunerativoUns n WHERE c.estado=TRUE AND c.dependency=:dependency ORDER BY pn.nombreCompleto")
                    .setParameter("dependency", dependency).getResultList());
        } else {
            return null;
        }
    }

    enum Perm {
        ACCESS_PERSONAL,
        ACCESS_PLANILLA,
        ACCESS_CONTROL_PERSONAL,
        ACCESS_REMUNERACIONES
    };

    @Override
    public List<Employee> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        Object qs = filters.get("personaNatural");
        if (qs == null) {
            qs = filters.get("q");
        }
        Object employee = XUtil.isEmpty(filters.get("employee"), null);
        Object numeroPndid = XUtil.isEmpty(filters.get("numeroPndid"), null);
        Object mail = XUtil.isEmpty(filters.get("mail"), null);
        List<Query> ql = new ArrayList();
        String sql;
//        em.createQuery("SELECT e  FROM Employee e JOIN e.id=:employee WHERE 1=1 ");
        ql.add(em.createQuery("SELECT e " + (sql = "FROM Employee e JOIN e.people p WHERE 1=1 "
                + (qs != null ? " AND (UPPER(p.fullName) LIKE :q OR p.numeroPndid like :q OR e.idEsc=:idEsc)" : "")
                + (employee != null ? " AND e.id=:employee" : "")
                + (numeroPndid != null ? " AND p.code like :numeroPndid" : "")
                + (mail != null ? " AND LOWER(p.emailPrin) like :emailPrin" : "")) + " ORDER BY p.fullName"));
        if (pageSize > 0) {
            ql.get(0).setFirstResult(first).setMaxResults(pageSize);
            ql.add(em.createQuery("SELECT COUNT(e) " + sql));
        }
//        em.createQuery("SELECT p FROM DrtPersonaNatural p WHERE p.emailPrin");
        for (Query q : ql) {
            if (qs != null) {
                q.setParameter("q", "%" + qs.toString().toUpperCase() + "%");
                q.setParameter("idEsc", XUtil.intValue(qs));
            }
            if (employee != null) {
                q.setParameter("employee", employee);
            }
            if (numeroPndid != null) {
                q.setParameter("numeroPndid", "%" + numeroPndid.toString().toUpperCase() + "%");
            }
            if (mail != null) {
                q.setParameter("emailPrin", "%" + mail.toString().toLowerCase() + "%");
            }
        }
        if (pageSize > 0) {
            filters.put("size", ql.get(1).getSingleResult());
        }
        return ql.get(0).getResultList();
    }

    private static boolean esc_instituciones;

    public void load(Employee e) {
        Map m = new HashMap();
        List ids = new ArrayList();
        Map m2 = new HashMap();
        List<Study2> estudioList = em.createQuery("SELECT e FROM EscEstudio e WHERE e.escPersonal=:personal")
                .setParameter("personal", e)
                .getResultList();
        for (Study2 ee : estudioList) {
            ids.add(ee.getIdInstitucion());
        }
        if (ids.size() > 0) {
            if (esc_instituciones) {
                try {
                    for (Object[] r : (List<Object[]>) em.createNativeQuery("SELECT id_inst,nombre_inst FROM esc_instituciones WHERE id_inst IN(:ids)")
                            .setParameter("ids", ids).getResultList()) {
                        Company pn = new Company();
                        pn.setBusinessName("" + r[1]);
                        m2.put(r[0], pn);
                    }
                } catch (Exception ex) {
                    esc_instituciones = false;
                }
            } else {
                for (Company r : (List<Company>) em.createQuery("SELECT p FROM Company p WHERE -p.idDir IN(:ids)")
                        .setParameter("ids", ids).getResultList()) {
                    m2.put(-r.getId(), r);
                }
            }
            for (Study2 ee : estudioList) {
                ee.setInstitucion((Company) m2.get(ee.getIdInstitucion()));
            }
        }
        m.put("estudioList", estudioList);
        ids.clear();
        m2.clear();
        List<EscCapacitacion> capacitacionList = em.createQuery("SELECT e FROM EscCapacitacion e WHERE e.personal=:personal")
                .setParameter("personal", e)
                .getResultList();
        for (EscCapacitacion ee : capacitacionList) {
            ids.add(ee.getCompanyId());
        }
        if (ids.size() > 0) {
            if (esc_instituciones) {
                try {
                    for (Object[] r : (List<Object[]>) em.createNativeQuery("SELECT id_inst,nombre_inst FROM esc_instituciones WHERE id_inst IN(:ids)")
                            .setParameter("ids", ids).getResultList()) {
                        Company pn = new Company();
                        pn.setBusinessName("" + r[1]);
                        m2.put(r[0], pn);
                    }
                } catch (Exception ex) {
                    esc_instituciones = false;
                }
            } else {
                for (Company r : (List<Company>) em.createQuery("SELECT p FROM Company p WHERE -p.idDir IN(:ids)")
                        .setParameter("ids", ids).getResultList()) {
                    m2.put(-r.getId(), r);
                }
            }
            for (EscCapacitacion ee : capacitacionList) {
                ee.setCompany((Company) m2.get(ee.getCompanyId()));
            }
        }
        m.put("estudioList", estudioList);
        e.setExt(m);
    }

    public Object getContent(Map m) {
        Object[] tipoPersonal = (Object[]) m.get("tipoPersonal");
        Date fechaIni = (Date) m.get("fechaIni");
        //XDate.getDate(2013, 2, 25);
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIni);
        String header[] = new String[31];
        Integer DAY_TYPE[] = new Integer[31];
        DateFormatSymbols s = new DateFormatSymbols();
        String dn[] = s.getShortWeekdays();
        int ini = c.get(Calendar.YEAR) * 1000 + c.get(Calendar.DAY_OF_YEAR);

        for (int i = 0; i < header.length; i++) {
            int wd = c.get(Calendar.DAY_OF_WEEK);
            DAY_TYPE[i] = (wd == 1 || wd == 7) ? 1 : 0;
            header[i] = dn[wd].toUpperCase()
                    + "\n" + c.get(Calendar.DAY_OF_MONTH);
            c.add(Calendar.DAY_OF_YEAR, 1);
        }
        c.add(Calendar.DAY_OF_YEAR, -1);
        SimpleDateFormat sdf0 = new SimpleDateFormat("dd/MM/yy");
        String TIPO_PERSONAL = "";
        if (XUtil.isEmpty(tipoPersonal)) {
            tipoPersonal = null;
        }
        Query q = em.createQuery("SELECT p.idEsc,pn.fullName,c.perColectPK.fecmar,MIN(c.perColectPK.hormar),MAX(c.perColectPK.hormar) FROM PerColect c,Employee p JOIN p.drtPersonaNatural pn WHERE "
                + " 1=1 "
                + (tipoPersonal != null ? " AND p.escTipoPersonal IN :tipoPersonal" : "")
                + " AND p.idEsc=c.perColectPK.idEsc "
                + "AND c.perColectPK.fecmar>=:fechaIni AND DATE(c.perColectPK.fecmar)<=DATE(:fechaFin) "
                + "GROUP BY p.idEsc,pn.fullName,c.perColectPK.fecmar ORDER BY pn.fullName,c.perColectPK.fecmar")
                .setParameter("fechaIni", fechaIni)
                .setParameter("fechaFin", c.getTime());
        X.log(sdf0.format(fechaIni) + " -- " + sdf0.format(c.getTime()));
        if (tipoPersonal != null) {
            q.setParameter("tipoPersonal", Arrays.asList((Object[]) tipoPersonal));
            for (Object t : tipoPersonal) {
                TIPO_PERSONAL += ", " + ((EscTipoPersonal) t).getName();
            }
            TIPO_PERSONAL = TIPO_PERSONAL.substring(2);
        } else {
            TIPO_PERSONAL = "TODOS";
        }

        List<Object[]> l0 = q
                .getResultList();
        Object o = false;
        ArrayList l = new ArrayList();
        int i = 0;
        String dia[] = null;
//        RowAsistencia ra;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date fechaFin = c.getTime();
        c.setTime(fechaIni);
        Map tmp = new HashMap();
//        for (Object[] r : l0) {
//            if (!r[0].equals(o)) {
//                ra = new RowAsistencia();
//                o = r[0];
//                Employee p = (Employee) tmp.get(r[0]);
//                if (p == null) {
//                    DrtPersonaNatural pn = new DrtPersonaNatural();
//                    pn.setNombreCompleto(X.toText(r[1]));
//                    p = new Employee();
//                    p.setId(XUtil.intValue(r[0]));
//                    p.setPeople(pn);
//                    tmp.put(r[0], p);
//                }
//                ra.setPersonal(p);
//                ra.setDia(dia = new String[31]);
//                l.add(ra);
//            }
//            c.setTime((Date) r[2]);
//            //(int) TimeUnit.DAYS.convert(((Date) r[1]).getTime() - tIni, TimeUnit.MILLISECONDS);
//            dia[c.get(Calendar.YEAR) * 1000 + c.get(Calendar.DAY_OF_YEAR)
//                    - ini]
//                    =//sdf0.format(r[1]) + "\n"+
//                    sdf.format(r[3]) + "\n" + sdf.format(r[4]);
//
//        }

        sdf = new SimpleDateFormat("MMM 'de' yyyy");
        return null/*JR.open("/edu/uns/rh/asistencia/jr/dw_asistencia_ingreso_salida_mes.jasper", new XMap(
                DataSource.class, l,
                JR.EXTENSION, m.get(JR.EXTENSION),
                "PERIODO", sdf0.format(fechaIni) + " A " + sdf0.format(fechaFin),
                "PERIODO_HEADER", sdf.format(fechaIni).toUpperCase() + " - " + sdf.format(fechaFin).toUpperCase(),
                "TIPO_PERSONAL", TIPO_PERSONAL,
                "DAY_TYPE", DAY_TYPE,
                "HEADER", header))*/;
    }

    /*@PostMappingConstruct
    public void init() {
        add(new Module());
        add(dependencyModule);
    }*/

    public List<Object[]> loadArray(int first, int pageSize, String sortField, Map<String, Object> filters) {
        String sql = "SELECT p,e FROM Employee p,EscEstudio e WHERE e.idEsc=p.idEsc";
        Query q = em.createQuery("SELECT p " + sql).setMaxResults(pageSize).setFirstResult(first);
        if (filters != null) {
            filters.put("size", em.createQuery("SELECT count(p) " + sql).getSingleResult());
        }
        return q.getResultList();
    }

    private class Module {

        /*
        @Override
        public void addMenu(List<Object[]> menuList) {
            menuList.add(new Object[]{Perm.ACCESS_PERSONAL, new MenuFacade.Tree("Recursos Humanos", "", "/admin/rh", "/resources/images/rh.png"), new Object[][]{
                {null, "Operación", null, null, null, null, new Object[][]{
                    {"/admin/rh/escalafon/List.xhtml", "Personal", null, null, null, null, null},
                    //                    {"/admin/rh/personal/", "Personal", null, null, null, null, null},
                    {"-"},
                    {"/faces/rh/marcacion/List.xhtml", "Marcaciones de Asistencia", null, null, null, null, null},
                    {"/faces/rh/papeleta/List.xhtml", "Papeletas de Salida", null, null, null, null, null},
                    {"-"},
                    {"/faces/rh/planilla/List.xhtml", "Planillas de Trabajadores y Pensionistas", null, null, null, null, null},
                    {"/faces/rh/htareo/List.xhtml", "Hoja de tareo", null, null, null, null, null},
                    {"-"},
                    {"/faces/rh/planilla/List.xhtml", "Consolidado Resumen de Planillas", null, null, null, null, null}
                }},
                {null, "Reportes", null, null, null, null, new Object[][]{
                    {"/faces/rh/report/marcacionIngresoSalida.xhtml", "Marcación de Ingreso y Salida de Personal", null, null, null, null, null},
                    {"/faces/rh/report/hd.xhtml", "Constancia de Haberes y Descuentos", null, null, null, null, null},
                    {"/faces/rh/report/marcacionDiaria.xhtml", "Reporte Diario de Marcaciones", null, null, null, null, null},
                    {"/admin/rh/report/marcacionMensualTrabajador.xhtml", "Marcacion Mensual por Trabajador", null, null, null, null, null},
                    {"-"},
                    {"/faces/rh/report/tardanzaAdministrativo.xhtml", "Resumen Mensual de Tardanzas [Administrativo]", null, null, null, null, null},
                    {"/faces/rh/report/tardanza-vigilante", "Resumen Mensual de Tardanzas [Vigilancia]", null, null, null, null, null},
                    {"-"},
                    {"/faces/rh/report/papeleta.xhtml", "Reporte Mensual de Papeletas de Salida", null, null, null, null, null},
                    {"-"},
                    {"/faces/rh/report/resumenPlus.xhtml", "Resumen mensual Plus", null, null, null, null, null},
                    {"/admin/rh/report/resumen-afp", "Resumen Mensual de Descuentos de AFP", null, null, null, null, null},
                    {"/admin/rh/report/resumen-gastos", "Resumen Mensual de Gasto en Planillas", null, null, null, null, null},
                    {"-"},
                    {"/faces/rh/report/personalActivo.xhtml", "Relación de Personal Activo", null, null, null, null, null},
                    {"/admin/rh/report/estadistica-remuneracion", "Estadística Mensual de Remuneraciones", null, null, null, null, null},
                    {"-"},
                    {"/admin/rh/report/onomastico", "Relación Mensual de Onomásticos", null, null, null, null, null}
                }},
                {null, "Utilitarios", null, null, null, null, new Object[][]{
                    {"/faces/rh/pdt/Interface.xhtml", "Interface PDT", null, null, null, null, null}
                }},
                {null, "Tablas", null, null, null, null, new Object[][]{
                    {"/faces/personal/nivelRemunerativo/List.xhtml", "Niveles Remunerativos", null, null, null, null, null},
                    {"/faces/personal/concepto/List.xhtml", "Conceptos", null, null, null, null, null},
                    {"/faces/personal/personalDscto/List.xhtml", "Cargos Funcionales", null, null, null, null, null},
                    {"-"},
                    {"/faces/personal/horario/List.xhtml", "Horarios de Marcaciones", null, null, null, null, null},
                    {"/faces/personal/turnoVigilante/List.xhtml", "Turnos de Vigilantes", null, null, null, null, null},
                    {"-"},
                    {"/faces/personal/horario/List.xhtml", "Horarios de Marcaciones", null, null, null, null, null},
                    {"/faces/personal/personalDscto/List.xhtml", "Afectaciones por Trabajador", null, null, null, null, null},
                    {"/faces/personal/afectacion/List.xhtml", "Afectaciones por Régimen", null, null, null, null, null},
                    {"/faces/personal/afectacion/List.xhtml", "Tabla Salarial - Construcción Civil", null, null, null, null, null}
                }},
                {null, "Herramientas", null, null, null, null, new Object[][]{
                    {"/faces/personal/pdt/Interface.xhtml", "Interfaz PDT 601 - Planillas de Obras", null, null, null, null, null},
                    {"/faces/personal/report/onomastico.xhtml", "Interfaz Altas y Bajas T-Registro", null, null, null, null, null},
                    {"/faces/personal/report/onomastico.xhtml", "Verificación de Identificación de Personal de Obras", null, null, null, null, null},
                    {"/faces/personal/interfaceT/List.xhtml", "Impresión de cheques SIAF", null, null, null, null, null}
                }}
            }
            });
        }
         */
    }
    /*
    
     Map m=(Map) ((Object[])metadata.get(6))[1];
     m.put("D","esc_tipo_personal.tipo");
     super.setMetadata(metadata);
     }

     public PersonalLaboralQ(){
     setQuery("SELECT "
     + "esc_personal.id_esc,"
     + "esc_personal.id_dir,"
     + "esc_nivel_estudios.abrev grado,"
     + "drt_personanatural.nombre_completo,"
     + "esc_personal.id_tipo,esc_personal.condicion,"

     + "esc_tipo_personal.tipo tipo_trabajador,"
     + "esc_personal.fecha_ing,"
     + "esc_personal.id_estado,"
     + "esc_estado_trabajador.estado "
     + "FROM esc_personal "
     + "INNER JOIN drt_personanatural on drt_personanatural.id_dir=esc_personal.id_dir "
     + "LEFT OUTER JOIN esc_estudios on esc_estudios.estado = true and esc_estudios.id_esc=esc_personal.id_esc "
     + "LEFT OUTER JOIN esc_nivel_estudios on esc_nivel_estudios.id_nivel=esc_estudios.id_nivel  "
     + "LEFT OUTER JOIN esc_estado_trabajador on esc_estado_trabajador.id_estado=esc_personal.id_estado "
     + "LEFT OUTER JOIN esc_tipo_personal on esc_tipo_personal.id_tipo=esc_personal.id_tipo "
     );
     */
    private List data = new ArrayList();

    public TableModel tm = new AbstractTableModel() {

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int r, int c) {
            return ((Object[]) data.get(r))[c];
        }

        @Override
        public void setValueAt(Object v, int r, int c) {
            ((Object[]) data.get(r))[c] = v;
        }
    };

//    public static void main(String[] args) throws IOException {
//        ClassPathUtil.load("D:\\proyecto\\boot.json");
//        EmployeeFacade c = new EmployeeFacade();
//        EntityManager em = JPA.getInstance().em;
//        BeanUtils.inject(c, em);
//        XMap m = new XMap("escPersonalList", em.find(Employee.class, 151));
//        c.loadTiempoServicio(0, 0, null, m);
//    }
    public List<TiempoServicioDet> loadTiempoServicio(int first, int pageSize, String sortField, Map<String, Object> filters) {
        List<TiempoServicioDet> tsdList = new ArrayList();
        Object employeeList = filters != null ? filters.get("employee") : null;
        for (EscContrato c : em.createQuery("SELECT c FROM EscContrato c WHERE c.employee IN :employee", EscContrato.class)
                .setParameter("employee", employeeList)
                .getResultList()) {
            TiempoServicioDet tsd = new TiempoServicioDet();
            tsd.setTipo("CONTRATADO");
            tsd.setEmployee(c.getEmployee());
            tsd.setDescripcion(c.getEscCap().getCargo());
            if (c.getEscCap().getNivelRem() != null) {
                tsd.setAbrev(c.getEscCap().getNivelRem().getNivel());
            }
            tsd.setDependency(c.getEscCap().getDependency());
            tsd.setDocument(c.getIdDtra());
            tsd.setFechaIni(c.getFechaIni());
            tsd.setFechaFin(c.getFechaFin() != null ? c.getFechaFin() : new Date());
            tsd.init();
            tsdList.add(tsd);
        }
        for (EscNombramiento c : em.createQuery("SELECT c FROM EscNombramiento c WHERE c.employee IN :idEsc", EscNombramiento.class)
                .setParameter("idEsc", employeeList)
                .getResultList()) {
            TiempoServicioDet tsd = new TiempoServicioDet();
            tsd.setTipo("NOMBRADO");
            tsd.setEmployee(c.getEmployee());
            if (c.getEscCap() != null) {
                tsd.setDescripcion(c.getEscCap().getCargo());
                if (c.getEscCap().getNivelRem() != null) {
                    tsd.setAbrev(c.getEscCap().getNivelRem().getNivel());
                }
                //tsd.setDependency(c.getEscCap().getDependency());
            }
            tsd.setDocument(c.getIdDtra());
            tsd.setFechaIni(c.getFechaNomb());
            tsd.setFechaFin(c.getFechaFin() != null ? c.getFechaFin() : new Date());
            tsd.init();
            tsdList.add(tsd);
        }
        for (EscPromocion c : em.createQuery("SELECT c FROM EscPromocion c WHERE c.employee IN :employee", EscPromocion.class)
                .setParameter("employee", employeeList)
                .getResultList()) {
            TiempoServicioDet tsd = new TiempoServicioDet();
            tsd.setTipo("PROMOVIDO");
            tsd.setEmployee(c.getEmployee());
            tsd.setDescripcion(c.getEscCap().getCargo());
            if (c.getEscCap().getNivelRem() != null) {
                tsd.setAbrev(c.getEscCap().getNivelRem().getNivel());
            }
            tsd.setDependency(c.getEscCap().getDependency());
            tsd.setDocument(c.getIdDtra());
            tsd.setFechaIni(c.getFechaProm());
            tsd.setFechaFin(c.getFechaFin() != null ? c.getFechaFin() : new Date());
//            cal.setTime(c.getFechaProm());
//            cal.add(Calendar.DAY_OF_MONTH,-1);
            tsd.init();
            tsdList.add(tsd);
        }
        for (EscRotacion c : em.createQuery("SELECT c FROM EscRotacion c WHERE c.employee IN :idEsc", EscRotacion.class)
                .setParameter("idEsc", employeeList)
                .getResultList()) {
            TiempoServicioDet tsd = new TiempoServicioDet();
            tsd.setTipo("ROTACION");
            tsd.setEmployee(c.getEmployee());
            tsd.setDescripcion(c.getEscCap().getCargo());
            if (c.getEscCap().getNivelRem() != null) {
                tsd.setAbrev(c.getEscCap().getNivelRem().getNivel());
            }
            tsd.setDependency(c.getEscCap().getDependency());
            tsd.setDocument(c.getIdDtra());
            tsd.setFechaIni(c.getFechaIni());
            tsd.setFechaFin(c.getFechaFin() != null ? c.getFechaFin() : new Date());
            tsd.init();
            tsdList.add(tsd);
        }
        for (Demerit c : em.createQuery("SELECT c FROM EscDemerito c WHERE c.employee IN :employee AND c.activo=TRUE", Demerit.class)
                .setParameter("employee", employeeList)
                .getResultList()) {
            TiempoServicioDet tsd = new TiempoServicioDet();
            tsd.setTipo("DEMERITO");
            tsd.setEmployee(c.getEmployee());
            tsd.setDocument(c.getDocument());
            tsd.setFechaIni(c.getFechaIni() != null ? c.getFechaIni() : c.getFechaDem());
            tsd.setFechaFin(c.getFechaFin());
            tsd.setMovimiento((short) -1);
            tsd.init();
            tsdList.add(tsd);
        }
        for (EscReposicion c : em.createQuery("SELECT c FROM EscReposicion c WHERE c.employee IN :idEsc", EscReposicion.class)
                .setParameter("idEsc", employeeList)
                .getResultList()) {
            TiempoServicioDet tsd = new TiempoServicioDet();
            tsd.setTipo("REPOSICION");
            tsd.setEmployee(c.getEmployee());
            tsd.setDocument(c.getIdDtra());
            tsd.setFechaIni(c.getFechaRep());
            tsd.setFechaFin(c.getFechaIni() != null ? c.getFechaIni() : new Date());
            tsd.init();
            tsdList.add(tsd);
        }
        for (EscContratoDoc c : em.createQuery("SELECT c FROM EscContratoDoc c WHERE c.employee IN :idEsc", EscContratoDoc.class)
                .setParameter("idEsc", employeeList)
                .getResultList()) {
            TiempoServicioDet tsd = new TiempoServicioDet();
            tsd.setTipo("CONTRATADO");
            tsd.setEmployee(c.getEmployee());
            tsd.setDescripcion(c.getEscCapDocente().getCategoria().getCategoria());
            tsd.setAbrev(c.getEscCapDocente().getCategoria().getAbrevDed());
            tsd.setDependency(c.getEscCapDocente().getDependency());
            tsd.setDocument(c.getIdDtra());
            tsd.setFechaIni(c.getFechaIni());
            tsd.setFechaFin(c.getFechaFin() != null ? c.getFechaFin() : new Date());
            tsd.setPeriodo(c.getPeriodo());
            tsd.init();
            tsdList.add(tsd);
        }
        for (EscNombramientoDoc c : em.createQuery("SELECT c FROM EscNombramientoDoc c WHERE c.employee IN :employee", EscNombramientoDoc.class)
                .setParameter("employee", employeeList)
                .getResultList()) {
            TiempoServicioDet tsd = new TiempoServicioDet();
            tsd.setTipo("NOMBRADO");
            tsd.setEmployee(c.getEmployee());
            tsd.setDescripcion(c.getEscCapDocente().getCategoria().getCategoria());
            tsd.setAbrev(c.getEscCapDocente().getCategoria().getAbrevDed());
            tsd.setDependency(c.getEscCapDocente().getDependency());
            tsd.setDocument(c.getIdDtra());
            tsd.setFechaIni(c.getFechaNomb());
            tsd.setFechaFin(c.getFechaFin() != null ? c.getFechaFin() : new Date());
            tsd.init();
            tsdList.add(tsd);
        }
        for (EscContratoOtro c : em.createQuery("SELECT c FROM EscContratoOtro c WHERE c.employee IN :employee", EscContratoOtro.class)
                .setParameter("employee", employeeList)
                .getResultList()) {
            TiempoServicioDet tsd = new TiempoServicioDet();
            tsd.setTipo("CONTRATO");
            tsd.setEmployee(c.getEmployee());
            tsd.setDescripcion(c.getEscServicio().getServicio());
            tsd.setDependency(c.getDependency());
            tsd.setDocument(c.getIdDtra());
            tsd.setFechaIni(c.getFecInicio());
            tsd.setFechaFin(c.getFecFin() != null ? c.getFecFin() : new Date());
            tsd.init();
            tsdList.add(tsd);
        }
        for (LicensePeople c : em.createQuery("SELECT p FROM LicensePeople p WHERE p.employee IN :employee", LicensePeople.class)
                .setParameter("employee", employeeList)
                .getResultList()) {
            TiempoServicioDet tsd = new TiempoServicioDet();
            tsd.setEmployee(c.getEmployee());
            tsd.setTipo("LICENCIA");
            //tsd.setDescripcion(c.ge.getEspecificacion());
            //tsd.setAbrev(c.getPerEspecPl().getPermLice().getAbrev());
            tsd.setDocument(c.getDocumento());
            tsd.setFechaIni(c.getDesde());
            tsd.setFechaFin(c.getHasta());
            if ("S/G".equals(tsd.getAbrev())) {
                tsd.setMovimiento((short) -1);
            } else {
                tsd.setMovimiento((short) 0);
            }
            tsd.init();
            tsdList.add(tsd);
        }
        for (EscEncargatura c : em.createQuery("SELECT e FROM EscEncargatura e WHERE e.employee IN :employee", EscEncargatura.class)
                .setParameter("employee", employeeList)
                .getResultList()) {
            TiempoServicioDet tsd = new TiempoServicioDet();
            tsd.setEmployee(c.getEmployee());
            tsd.setTipo("ENCARGATURA");
            tsd.setDescripcion(c.getEscCap().getCargo());
            tsd.setDependency(c.getEscCap().getDependency());
            tsd.setDocument(c.getIdDtra());
            tsd.setFechaIni(c.getFechaIni());
            tsd.setFechaFin(c.getFechaFin());
            tsd.init();
            tsdList.add(tsd);
        }
        for (EscPromocionDoc c : em.createQuery("SELECT p FROM EscPromocionDoc p WHERE p.employee IN :employee", EscPromocionDoc.class)
                .setParameter("employee", employeeList)
                .getResultList()) {
            TiempoServicioDet tsd = new TiempoServicioDet();
            tsd.setEmployee(c.getEmployee());
            tsd.setTipo("PROMOVIDO");
            tsd.setDescripcion(c.getEscCapDocente().getCategoria().getCategoria());
            tsd.setAbrev(c.getEscCapDocente().getCategoria().getAbrevDed());
            tsd.setDependency(c.getEscCapDocente().getDependency());
            tsd.setDocument(c.getIdDtra());
            tsd.setFechaIni(c.getFechaProm());
            tsd.setFechaFin(c.getFechaFin());
            tsd.init();
            tsdList.add(tsd);
        }
        Comparator<TiempoServicioDet> comparator = new Comparator<TiempoServicioDet>() {
            @Override
            public int compare(TiempoServicioDet c1, TiempoServicioDet c2) {
                int r = c1.getEmployee().getId().compareTo(c2.getEmployee().getId());
//                System.out.println(c2.getType()+"-"+c2.getDocumento()+" "+c2.getFechaIni()+" "+c2.getFechaFin());
                return r == 0 ? c1.getFechaIni().compareTo(c2.getFechaIni()) : r;
            }
        };

        Collections.sort(tsdList, comparator);
        Employee p = null;
        for (TiempoServicioDet t : tsdList) {
            if (p == null || !t.getEmployee().equals(p)) {
                if (p != null) {
                    System.out.println("Calculando de " + p);
                    p.setExt(XDate.getTime(tm, 1, 2, 0, tm.getRowCount(), (Date) tm.getValueAt(0, 1), new Date()));
                }
                data.clear();
                p = t.getEmployee();
            }
            data.add(new Object[]{t.getMovimiento(), t.getFechaIni(), t.getFechaFin()});
        }
        if (p != null) {
            System.out.println("Calculando de " + p);
            p.setExt(XDate.getTime(tm, 1, 2, 0, tm.getRowCount(), (Date) tm.getValueAt(0, 1), new Date()));
        }
        return tsdList;
    }
}
