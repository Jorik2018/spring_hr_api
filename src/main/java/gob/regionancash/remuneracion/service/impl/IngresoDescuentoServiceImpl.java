package gob.regionancash.remuneracion.service.impl;

import gob.regionancash.hr.escalafon.model.TiempoServicioDet;
import gob.regionancash.hr.model.Employee;
import gob.regionancash.hr.model.EmployeeType;
import gob.regionancash.hr.service.EscPersonalFacade;
import gob.regionancash.remuneracion.service.IngresoDescuentoService;
import gob.regionancash.remuneracion.model.PayrollType;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.math.BigDecimal;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import org.isobit.app.X;
import org.isobit.app.service.SystemFacade;
import org.isobit.util.XDate;
import org.isobit.util.XUtil;
import org.isobit.util.XMap;
import org.isobit.directory.model.People;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngresoDescuentoServiceImpl
      implements IngresoDescuentoService {

    private EntityManager em;

    private EscPersonalFacade employeeFacade;

    private SystemFacade systemFacade;

    private List<Object[]> data = new ArrayList();

    private TableModel tm = new AbstractTableModel() {

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

    public Object getConstanciaHD(int employeeId, Date fechaIni, Date fechaFin, List planillas) {
        //  2. Registros detalle de Planillas Oceper
        //  3. Registros sumas de valores conceptos de Planillas de Oceper
        //  4. Registros maximos valores de conceptos de Planillas de Oceper
        //  5. Registros sumas de valores conceptos de Planillas de Ocid
        HashMap m = new HashMap();
        if (fechaFin == null) {
            fechaFin = X.getServerDate();
        }
        Employee employee = (Employee) employeeFacade.load(0, 0, null, new XMap("employee", employeeId)).get(0);
        m.put("employee", Arrays.asList(employee));
        List<TiempoServicioDet> tiempoServicioDetList = employeeFacade.loadTiempoServicio(0, 0, null, m);
        m.put("tiempoServicioDetList", tiempoServicioDetList);
        m.put("employee", employee);
        m.put("tableModel", tm);
        data.clear();
        for (TiempoServicioDet t : tiempoServicioDetList) {
            t.setDescripcion(t.getDescripcion().replace("TECNICO", "TEC.").replace("ESPECIALISTA", "ESP.").replace("DIRECTOR", "DIR."));
            data.add(new Object[]{t.getMovimiento(), t.getFechaIni(), t.getFechaFin()});
        }
//        for (Object[] r : (List<Object[]>) em.createNativeQuery("SELECT id_cargo,id_nivel,horas,fecha_ini,fecha_fin FROM per_contrato_x WHERE id_esc=:id_esc ORDER BY fecha_ini")
//                .setParameter("id_esc", employee.getId())
//                .getResultList()) {
//            data.add(new Object[]{1, (Date) r[3], (Date) r[4]});
//        }
        Comparator<Object[]> comparator = new Comparator<Object[]>() {
            @Override
            public int compare(Object[] c1, Object[] c2) {
                return c1 != null ? ((Date) c1[1]).compareTo(((Date) c2[1])) : 0;
            }
        };

        Collections.sort(data, comparator);

        X.log("fechaIni0=" + fechaIni);
        if (fechaIni == null && !data.isEmpty()) {
            fechaIni = (Date) ((Object[]) data.get(0))[1];
        }
        X.log("fechaIni=" + fechaIni);
        X.log("fechaFin=" + fechaFin);
        X.log("planillas=" + planillas);
        List<Object[]> lista = em.createQuery("SELECT "
                + " YEAR(p.fecha) AS anio,"
                + " MONTH(p.fecha) AS mes,"
                + " DAY(p.fecha) AS dia,MAX(c.nivel),MAX(c.cargo)"
                + " FROM PerDetallePla0 c,PerPlanilla0 p "
                + " WHERE p.idPlanilla=c.perDetallePla0PK.idPlanilla AND c.perDetallePla0PK.idEsc=:idEsc "
                + (!XUtil.isEmpty(planillas) ? " AND p.idTipopla IN (" + XUtil.implode(planillas) + ") " : "")
                + " AND p.fecha>=:fechaIni AND p.fecha<=:fechaFin "
                + " GROUP BY 1,2,3 ORDER BY 1,2,3")
                .setParameter("idEsc", employeeId)
                .setParameter("fechaIni", fechaIni)
                .setParameter("fechaFin", fechaFin)
                .getResultList();

        lista.addAll(
                em.createQuery("SELECT "
                        + " pp.anio,"
                        + " pp.mes,"
                        + " 1 AS dia,MAX(c.nivel),MAX(c.cargo)"
                        + " FROM PerDetallePla c JOIN c.perPlanilla p JOIN p.perPeriodoPla pp "
                        + " WHERE p.idPlanilla=c.perDetallePlaPK.idPlanilla AND c.perDetallePlaPK.idEsc=:idEsc "
                        + (!XUtil.isEmpty(planillas) ? " AND p.idTipopla IN (" + XUtil.implode(planillas) + ") " : "")
                        //                + " AND p.fecha>=:fechaIni AND p.fecha<=:fechaFin"
                        + " AND pp.anio>2006 "
                        + " GROUP BY 1,2 ORDER BY 1,2,3")
                        .setParameter("idEsc", employeeId)
                        //                .setParameter("fechaIni", fechaIni)
                        //                .setParameter("fechaFin", fechaFin)
                        .getResultList()
        );
        m.put("perDetallePla", lista);
        //  2. Registros detalle de Planillas Ocper
        //fecha     nivelRemunerativo       dias        horasExtras     dominicales
        lista = em.createQuery("SELECT "
                + "p.fecha,"
                + "d.nivel,"
                + "MAX(d.diaslaborados),"
                + "MAX(d.horasextra),"
                + "MAX(d.dominicales) "
                + "FROM PerDetallePla0 d,PerPlanilla0 p "
                + "WHERE p.idPlanilla=d.perDetallePla0PK.idPlanilla "
                + (!XUtil.isEmpty(planillas) ? "AND d.perDetallePla0PK.idPlanilla IN (" + XUtil.implode(planillas) + ") " : "")
                + "AND d.perDetallePla0PK.idEsc=:idEsc "
                + "AND p.fecha>=:fechaIni "
                + "AND p.fecha<=:fechaFin "
                + "GROUP BY p.fecha,d.nivel "
                + "ORDER BY p.fecha ")
                .setParameter("idEsc", employeeId)
                .setParameter("fechaIni", fechaIni)
                .setParameter("fechaFin", fechaFin)
                .getResultList();
        //Registros sumas de valores conceptos de Planillas de Ocper
        lista = em.createQuery("SELECT "
                + " YEAR(p.fecha) AS anio,"
                + " MONTH(p.fecha) AS mes,"
                + " DAY(p.fecha) AS dia,"
                + " c.perPlanillaconcepto0PK.idConcepto,"
                + " c.concepto,"
                + " CASE WHEN c.idTipomov=1 THEN MAX(c.monto) ELSE SUM(c.monto) END,"
                + " c.tipoconcepto,"
                + " 0 "//tipoMovimiento
                + " FROM PerPlanillaconcepto0 c,PerPlanilla0 p "
                + " WHERE p.idPlanilla=c.perPlanillaconcepto0PK.idPlanilla AND c.perPlanillaconcepto0PK.idEsc=:idEsc "
                + (!XUtil.isEmpty(planillas) ? " AND p.idTipopla IN (" + XUtil.implode(planillas) + ") " : "")
                + " AND p.fecha>=:fechaIni AND p.fecha<=:fechaFin "
                + " GROUP BY 1,2,3,c.perPlanillaconcepto0PK.idConcepto,c.concepto,c.tipoconcepto,c.idTipomov "
                + " ORDER BY 1,2,3 ")
                .setParameter("idEsc", employeeId)
                .setParameter("fechaIni", fechaIni)
                .setParameter("fechaFin", fechaFin)
                .getResultList();
        for (Object[] rr : (List<Object[]>) lista) {
            //ingreso descuento y neto
            if ((Integer) rr[3] == 49 || (Integer) rr[3] == 21 || (Integer) rr[3] == 22) {
                rr[7] = rr[3];
                //de tipo ingreso o descuento
            } else if ((Integer) rr[6] == 69 || (Integer) rr[6] == 70) {
                rr[7] = rr[6];
            }
        }
//        // 4. Registros maximos valores de conceptos de Planillas de Ocper
//        // usado para valores como numero de hijos
//        // q no son acumulables en casos repetidos
//        // estos seran de tipo 16
//        lista = new ArrayList();
//        em.createQuery("SELECT"
//                + " year(per_planilla.fecha) anio,"
//                + " month(per_planilla.fecha ) mes,"
//                + " day(per_planilla.fecha) dia,"
//                + " per_planillaconcepto.id_concepto,"
//                + " max(per_planillaconcepto.concepto),"
//                + " max(per_planillaconcepto.monto),"
//                + " tipoconcepto,"
//                + " 0 as idTipoMovimiento"
//                + " FROM per_planillaconcepto"
//                + " INNER JOIN per_planilla ON per_planilla.id_planilla=per_planillaconcepto.id_planilla"
//                + " WHERE per_planillaconcepto.tipoconcepto=16 AND per_planillaconcepto.id_esc=:idEsc "
//                + " AND per_planilla.id_tipo_planilla IN (" + (planillas.toString()).substring(1, planillas.toString().length() - 1) + ") "
//                + " AND per_planilla.fecha>=fechaIni AND per_planilla.fecha<=:fechaFin "
//                + " GROUP BY anio,mes,dia,per_planillaconcepto.id_concepto"
//        ).setParameter("idEsc", idEsc)
//                .setParameter("fechaIni", fechaIni)
//                .setParameter("fechaFin", fechaFin)
//                .getResultList();
//
//        //  5. Registros sumas de valores conceptos de Planillas de Ocid
        lista.addAll(em.createQuery("SELECT "
                + " pp.anio,"
                + " pp.mes,"
                + " 1 AS dia,"
                + " c.perPlanillaconceptoPK.idConcepto,"
                + " max(c.concepto),"
                + " sum(c.monto),"
                + " c.tipoconcepto,"
                + " c.idTipomov"
                + " FROM PerPlanillaconcepto c "
                + " JOIN c.perPlanilla p"
                + " JOIN p.perPeriodoPla pp"
                + " WHERE c.perPlanillaconceptoPK.idEsc=:idEsc AND pp.anio>2006 "
                + " AND (pp.anio*12+pp.mes>=:year_1*12+:month_1+1)"
                + " AND (pp.anio*12+pp.mes<=:year_2*12+:month_2+1)"
                + (!XUtil.isEmpty(planillas) ? " AND p.idTipopla IN (" + XUtil.implode(planillas) + ") " : "")
                + " GROUP BY pp.anio,pp.mes,c.perPlanillaconceptoPK.idConcepto,c.idTipomov,c.tipoconcepto"
                + " ORDER BY pp.anio,pp.mes,c.idTipomov,c.perPlanillaconceptoPK.idConcepto")
                .setParameter("idEsc", employeeId)
                .setParameter("year_1", XDate.getYear(fechaIni))
                .setParameter("month_1", XDate.getMonth(fechaIni))
                .setParameter("year_2", XDate.getYear(fechaFin))
                .setParameter("month_2", XDate.getMonth(fechaFin))
                .getResultList()
        );
        Map tmp = new HashMap();
        for (Object[] r : lista) {
            r[0] = XUtil.intValue(r[0]);
            List l = (List) tmp.get(r[0]);
            if (l == null) {
                tmp.put(r[0], l = new ArrayList());
            }
            l.add(r);
        }
        lista.clear();
        for (Map.Entry e : (Set<Map.Entry>) tmp.entrySet()) {
            lista.add(new Object[]{e.getKey(), e.getValue()});
        }
        lista.sort((Object[] o1, Object[] o2) -> ((Integer) o1[0]) - ((Integer) o2[0]));
        ArrayList pageList = new ArrayList();
        int i = 0;
        for (; i < lista.size() - 1; i++) {
            if (((Integer) lista.get(i + 1)[0] - (Integer) lista.get(i)[0]) == 1) {
                ((List) lista.get(i)[1]).addAll((List) lista.get(i + 1)[1]);
                pageList.add(lista.get(i)[1]);
                i++;
            } else {
                pageList.add(lista.get(i)[1]);
            }
        }
        if (i == lista.size() - 1) {
            pageList.add(lista.get(lista.size() - 1)[1]);
        }
        m.put("pageList", pageList);
        return m;
    }

    public List getPeriodosLaboralesTreeModel(int idEsc) {
        int n;
        List idPlanillas = new ArrayList();
        Map<Object, Object[]> m = new HashMap();
        for (Object[] row : (List<Object[]>) (em.createQuery("SELECT p.idTipopla,MIN(100*pp.anio+pp.mes),MAX(100*pp.anio+pp.mes) FROM PerDetallePla d JOIN d.perPlanilla p JOIN p.perPeriodoPla pp WHERE d.perDetallePlaPK.idEsc=:idEsc GROUP BY p.idTipopla")
                .setParameter("idEsc", idEsc)
                .getResultList())) {
            n = XUtil.intValue(row[1]);
            row[1] = XDate.getDate(n / 100, n % 100, 1);
            n = XUtil.intValue(row[2]);
            row[2] = XDate.getDate(n / 100, n % 100, 1);
            m.put(row[0], row);
            idPlanillas.add(row[0]);
        }
        for (Object[] row : (List<Object[]>) (em.createQuery("SELECT tp.idTipopla,MIN(p.fecha),MAX(p.fecha) FROM PerDetallePla0 d ,PerPlanilla0 p JOIN p.tipoPlanilla tp WHERE p.idPlanilla=d.perDetallePla0PK.idPlanilla AND d.perDetallePla0PK.idEsc=:idEsc AND tp.chd='T' GROUP BY tp.idTipopla")
                .setParameter("idEsc", idEsc)
                .getResultList())) {
            idPlanillas.add(row[0]);
            Object[] rr = m.get(row[0]);
            if (rr == null) {
                rr = row;
            } else {
                if (((Date) row[1]).before((Date) rr[1])) {
                    rr[1] = row[1];
                }
                if (((Date) row[2]).after((Date) rr[2])) {
                    rr[2] = row[2];
                }
            }
            m.put(row[0], rr);
        }
        idPlanillas.add(0);
        List result = new ArrayList();
        for (Object r : em.createQuery("SELECT DISTINCT tp FROM PerTipoPlanilla0 tp WHERE  tp.idTipopla IN :tipoPlanilla ORDER BY tp.idTipopla,tp.idModalidad,tp.idActividad")
                .setParameter("tipoPlanilla", idPlanillas).getResultList()) {
            Object[] rr = m.get(((PayrollType) r).getIdTipopla());
            result.add(new Object[]{r, rr[1], rr[2]});
        }
        return result;
    }


    public Object getContent(Object option) {
        Map m = null;
        if (option instanceof Map) {
            m = (Map) option;
            int opt = XUtil.intValue(m.get("option"));
            if (opt == 1) {
                return null;/*JR.open("/gob/regionancash/rh/jr/TiempoServicioDet.jasper", new XMap(
                        DataSource.class,
                        employeeFacade.loadTiempoServicio(0, 0, null,
                                new XMap("employee", Arrays.asList(employeeFacade.find(m.get("employee"))))
                        )
                ));*/
            } else if (!(m.get("employee") instanceof Employee)) {
                //m = (Map) getContanciaHD(XUtil.intValue(m.get("employee")), null, null, null);
            }
        } else {
           // m = (Map) getContanciaHD(XUtil.intValue(option), null, null, null);
        }
        try {
            com.itextpdf.text.Font FN6 = FontFactory.getFont("/net/sf/jasperreports/fonts/arimo/Arimo-Regular.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 6, com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);



            BaseFont BF6 = FN6.getCalculatedBaseFont(true);
            com.itextpdf.text.Font FN8 = FontFactory.getFont("/net/sf/jasperreports/fonts/arimo/Arimo-Regular.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 8, com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
            com.itextpdf.text.Font FB8 = FontFactory.getFont("/net/sf/jasperreports/fonts/arimo/Arimo-Bold.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 8, com.itextpdf.text.Font.BOLD, BaseColor.BLACK);
            com.itextpdf.text.Font FB13 = FontFactory.getFont("/net/sf/jasperreports/fonts/arimo/Arimo-Bold.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 13, com.itextpdf.text.Font.BOLD, BaseColor.BLACK);
            com.itextpdf.text.Font FB17 = FontFactory.getFont("/net/sf/jasperreports/fonts/arimo/Arimo-Bold.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17, com.itextpdf.text.Font.BOLD, BaseColor.BLACK);
            String institucion = "UNIVERSIDAD NACIONAL DEL SANTA";
            String dependencia = "Dirección de Recursos Humanos";
            Employee employee = (Employee) m.get("employee");
            List<List<Object[]>> pageList = (List) m.get("pageList");
            //[AÑO,MES,DAY,ID_CONCEPTO,CONCEPTO,CANTIDAD]

            List<Object[]> perDetallePla = (List) m.get("perDetallePla");
            List<TiempoServicioDet> tiempoServicioDetList = (List) m.get("tiempoServicioDetList");
            Integer tiempoTotal[] = (Integer[]) tiempoServicioDetList.get(0).getEmployee().getExt();

            TableModel tm = (TableModel) m.get("tableModel");
//            Integer[] ndt = (Integer[]) XDate.getTime(tm, 1, 2, 0, tm.getRowCount(),
//                    (Date) tm.getValueAt(0, 1),
//                    new Date()
//            );

            String groups[][][] = {
                {
                    {"_130_115_149_", null},
                    {"_166_109_", null},
                    {"_20_25_37_58_64_87_101_132_", "OTROS"},
                    {"_10_41_68_76_128_137_147_", "Aguinaldos\nFiestas\nEscolaridad"},
                    {"_24_59_", "Reintegros"}
                },
                {
                    {"_19_73_78_85_90_97_112_117_118_119_126_127_128_129_130_133_", "OTROS"},
                    {"_24_25_58_", "Aguinaldos\nFiestas\nEscolaridad"},
                    {"_62_", "Reintegros"}
                }
            };
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd' de 'MMMM' de 'yyyy");
            HashMap conceptoMap = null;
            int r;
            DecimalFormatSymbols dsf = new DecimalFormatSymbols();
            dsf.setDecimalSeparator(',');
            java.text.DecimalFormat df = new java.text.DecimalFormat("0.00", dsf);
            String nmes[] = {"ENERO", "FEBRERO", "MARZO", "ABRIL",
                "MAYO", "JUNIO", "JULIO", "AGOSTO",
                "SETIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
            Document document = new Document(PageSize.A4.rotate(), 30, 30, 30, 30);
            float pw = document.getPageSize().getWidth() - 60;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            PdfPTable table;
            PdfPCell cell;
            Paragraph pa;
            Paragraph blank = new Paragraph("", FN6);
            blank.setSpacingBefore(0.0f);
            blank.setSpacingAfter(0.0f);
            systemFacade.prepareReport(m);

            m.put("SIGN-01", "LIC. WILDER IBARRA MARQUEZ");
            m.put("SIGN-02", "MG. CARLOS GUERRA CORDERO");
            m.put("AREA-01", "Jefe(e) de la Oficina de Remuneraciones");
            m.put("AREA-02", "Director de Recursos Humanos");
            Image image1 = null;
            try {
                image1 = Image.getInstance("" + m.get("COMPANY_LOGO"));
                image1.scaleAbsolute(0.10f * pw, 0.10f * pw);
                image1.setAlignment(Element.ALIGN_CENTER);
            } catch (Exception e) {
            }
            Image image2 = null;
            try {
                image2 = Image.getInstance("" + m.get("COUNTRY_LOGO"));
                image2.scaleAbsolute(0.10f * pw, 0.10f * pw);
                image2.setAlignment(Element.ALIGN_CENTER);
            } catch (Exception e) {
            }
            int firstYear;
            int pageCounter = 0;
            boolean bbc = false;
            People people = employee.getPeople();
            X.log("m0444=" + m);
            for (List<Object[]> conceptoRow : pageList) {
                pageCounter++;
                firstYear = XUtil.intValue(conceptoRow.get(0)[0]);
                String[][] group = groups[firstYear < 2007 ? 0 : 1];
                document.newPage();
                table = new PdfPTable(3);
                table.setWidths(new float[]{18f, 64f, 18f});
                table.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("ID: 00000000", FN6));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                cell.setBorderWidth(0.0f);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setBorderWidth(0.0f);
                cell.setPaddingTop(0.0f);
                cell.setRowspan(2);
                Paragraph ph = new Paragraph(institucion.toUpperCase(), FB17);
                ph.setLeading(15, 0);
                ph.setExtraParagraphSpace(0.0f);
                ph.setFirstLineIndent(0.0f);
                ph.setSpacingBefore(0.0f);
                ph.setAlignment(Element.ALIGN_CENTER);
                ph.setSpacingAfter(0.0f);
                cell.addElement(ph);
                ph = (new Paragraph(dependencia.toUpperCase(), FB13));
                ph.setAlignment(Element.ALIGN_CENTER);
                ph.setSpacingAfter(0.0f);
                cell.addElement(ph);
                ph = (new Paragraph("CONSTANCIA DE HABERES Y DESCUENTOS", FB13));
                ph.setAlignment(Element.ALIGN_CENTER);
                ph.setSpacingAfter(0.0f);
                ph.setSpacingBefore(0.0f);
                cell.addElement(ph);
                ph = (new Paragraph("El Jefe de la " + dependencia + " de la Universidad Nacional del Santa,", FN8));
                ph.setAlignment(Element.ALIGN_CENTER);
                ph.setSpacingAfter(0.0f);
                cell.addElement(ph);
                ph = (new Paragraph("HACE CONSTAR", FN8));
                ph.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(ph);

                Object cargoC[] = new Object[26];
                Object abrevC[] = new Object[26];
                float maxWidth = 0;
                float cc2 = 0;
                String doc = "";
                String sTop = "";
                HashMap<String, Map> headerSummary = new HashMap();
                //que solo salgan 2 años
                Calendar ca = Calendar.getInstance();
                char condicion = 'C';
                for (int anioI = 0; anioI < 2; anioI++) {
                    for (int mesJ = 0; mesJ < 12; mesJ++) {
                        TiempoServicioDet tiempoServicioDetCur = null;
                        for (TiempoServicioDet tiempoServicioDet : tiempoServicioDetList) {
                            ca.set(Calendar.YEAR, firstYear + anioI);
                            ca.set(Calendar.DAY_OF_MONTH, 1);
                            ca.set(Calendar.MONTH, mesJ);
                            if (!tiempoServicioDet.getFechaIni().after(ca.getTime()) && tiempoServicioDet.getMovimiento() == 1) {
                                tiempoServicioDetCur = tiempoServicioDet;
                            }
                        }

                        if (tiempoServicioDetCur != null) {
                            //Si el nuevo documento es diferente se agrega en el arbol de el resumen de pagina
                            if (!doc.equals(tiempoServicioDetCur.getDocument())) {
                                if (tiempoServicioDetCur.getTipo().contains("NOMBR")) {
                                    condicion = 'N';
                                }
                                String employeeType = tiempoServicioDetCur.getEmployee().getType().getName();
                                Map<Object, Map> typeGroup = headerSummary.get(employeeType);
                                if (typeGroup == null) {
                                    headerSummary.put(employeeType, typeGroup = new HashMap());
                                }
                                Map<Object, List> condicionGroup = typeGroup.get(condicion);
                                if (condicionGroup == null) {
                                    typeGroup.put(condicion, condicionGroup = new HashMap());
                                }
                                List lc = condicionGroup.get(tiempoServicioDetCur.getDescripcion() + " " + tiempoServicioDetCur.getAbrev());
                                if (lc == null) {
                                    condicionGroup.put(tiempoServicioDetCur.getDescripcion() + " " + tiempoServicioDetCur.getAbrev(), lc = new ArrayList());
                                }
                                lc.add(tiempoServicioDetCur);
                            }
                            doc = tiempoServicioDetCur.getDocument();
                            //Debe ponerse en blanco si no existe planilla en el mes
                            cargoC[2 + anioI * 12 + mesJ] = tiempoServicioDetCur.getDescripcion();
                            abrevC[2 + anioI * 12 + mesJ] = tiempoServicioDetCur.getAbrev();
                            cc2 = 30;
                            float auxw = (int) BF6.getWidthPoint(tiempoServicioDetCur.getDescripcion(), FN6.getCalculatedSize());
                            if (auxw > maxWidth) {
                                maxWidth = auxw;
                            }
                            auxw = BF6.getWidthPoint("" + tiempoServicioDetCur.getAbrev(), FN6.getCalculatedSize());
                            if (auxw > cc2) {
                                cc2 = auxw;
                            }
                        }
                    }
                }

                cargoC[0] = maxWidth;
                abrevC[0] = cc2;
                for (Object employeeTypeKey : headerSummary.keySet()) {
                    Map<Object, Map> employeeTypeGroup = headerSummary.get(employeeTypeKey);
                    for (Object condicionKey : employeeTypeGroup.keySet()) {
                        Map<Object, List> condicionGroup = employeeTypeGroup.get(condicionKey);
                        sTop += " " + employeeTypeKey + " " + (condicionKey.equals('C') ? "CONTRATADO" : "NOMBRADO") + " en la ";
                        int kk = 0;
                        for (Object k : condicionGroup.keySet()) {
                            sTop += "CATEGORIA de " + k + " el ";
                            List<TiempoServicioDet> l = condicionGroup.get(k);
                            int kt = l.size();
                            for (kk = 0; kk < kt; kk++) {
                                TiempoServicioDet d = l.get(kk);
                                if (kk > 0) {
                                    sTop += kk == kt - 1 ? " y " : ", ";
                                }
                                sTop += sdf.format(d.getFechaIni()) + "(" + d.getDocument() + ")";
                            }
                        }
                    }
                }
                ph = (new Paragraph("Que " + (people.getSex() == 'F' ? "la Sra." : "el Sr.") + " " + people.getNames() + " " + people.getFirstSurname() + " " + people.getLastSurname() + "," + sTop
                        + " en la UNS,\n "
                        + "ha percidido remuneraciones a las que se han aplicado los descuentos de Ley, "
                        + "conforme se detalla", FN8));

                ph.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(ph);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("Página " + pageCounter + " de " + pageList.size(), FN6));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                cell.setBorderWidth(0.0f);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("IZQ", FN6));
                if (image1 != null) {
                    cell.addElement(image1);
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setBorderWidth(0.0f);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("DER", FN6));
                if (image2 != null) {
                    cell.addElement(image2);
                }
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setBorderWidth(0.0f);
                table.addCell(cell);
                if (!document.isOpen()) {
                    document.open();
                }
                document.add(table);
                Object[] totalIngresoColumn = new Object[26];
                Object[] totalEgresoColumn = new Object[26];
                Object[] netoColumn = new Object[26];

                totalIngresoColumn[0] = new Object[]{0, 0, 0, 0, "Total Ingresos", 0, 0, 4};
                totalEgresoColumn[0] = new Object[]{0, 0, 0, 0, "Total Descuentos", 0, 0, 4};
                netoColumn[0] = new Object[]{0, 0, 0, 0, "Neto a Pagar", 0, 0, 4};

                int ncolumnas = 0;
                Map columnExtraMap = new HashMap();

                conceptoMap = new HashMap();
                //[AÑO,MES,DAY,ID_CONCEPTO,CONCEPTO,CANTIDAD]
                /*
                La lista de concepto se agrupara en columnas de 26 espacioss
                en el espacio cero se guardara el primer registro concepto de 
                un tipo concepto
                
                 */
                for (Object[] row : conceptoRow) {
                    Object[] f = (Object[]) conceptoMap.get(row[3]);
                    if (f == null) {
                        conceptoMap.put(row[3], f = new Object[24 + 2]);
                        f[0] = row;
                    }
                    f[1 + 12 * (XUtil.intValue(row[0]) - firstYear)
                            + XUtil.intValue(row[1])] = row[5];
                }
                Object[] mesPagado = new Object[26];
                /*
                Se revisa los meses donde s hubo pago y se 
                guarda eel resultado en un array para usarlo mas adelante
                 */
                for (r = 2; r <= 25; r++) {
                    for (Object[] column : (Collection<Object[]>) conceptoMap.values()) {
                        if (column[r] != null && !column[r].equals(BigDecimal.ZERO)) {
                            mesPagado[r] = true;
                            break;
                        }
                    }
                }
                List<Object[]> columnList = new ArrayList();
                List<Object[]> egresoColumnList = new ArrayList();
                List lextra = new ArrayList();
                for (Object[] column : (Collection<Object[]>) conceptoMap.values()) {
                    Object[] headerColumn = (Object[]) column[0];
                    boolean empty = true;
                    /*
                    se descartan las columnas vaciass excepto las q es siempre deben ser visibles o 
                    si existe muy pocas columnas
                     */
                    for (r = 2; r <= 25; r++) {
                        if (column[r] != null && !column[r].equals(BigDecimal.ZERO)) {
                            empty = false;
                            break;
                        }
                    }
                    if (empty) {
                        continue;
                    }
                    Object[] integratedColumn = null;
                    for (String[] gs : group) {
                        if (gs[0].contains("_" + headerColumn[3] + "_")) {

                            integratedColumn = (Object[]) columnExtraMap.get(gs[0]);
                            lextra.add(headerColumn[3]);
                            if (integratedColumn == null) {
                                if (gs[1] != null) {
                                    headerColumn[4] = gs[1];
                                }
                                columnExtraMap.put(gs[0], column);
                            } else {
                                for (r = 2; r < 26; r++) {
                                    BigDecimal db2 = (BigDecimal) column[r];
                                    BigDecimal db = (BigDecimal) integratedColumn[r];
                                    if (db == null) {
                                        db = db2;
                                    } else if (db2 != null) {
                                        db = db.add(db2);
                                    }
                                    integratedColumn[r] = db;
                                }
                            }
                            break;
                        }

                    }
                }
                for (Object o : lextra) {
                    conceptoMap.remove(o);
                }
                conceptoMap.putAll(columnExtraMap);
                for (Object[] column : (Collection<Object[]>) conceptoMap.values()) {
                    Object[] headerColumn = (Object[]) column[0];
                    boolean empty = true;
                    //se descartan las columnas excepto las q es siempre deben ser visibles o si existe muy pocas columnas
                    for (r = 2; r <= 25; r++) {
                        if (column[r] != null && !column[r].equals(BigDecimal.ZERO)) {
                            empty = false;
                            break;
                        }
                    }
                    if (empty) {
                        continue;
                    }
                    ncolumnas++;
                    switch (((Integer) headerColumn[7])) {
                        case 3:
                            continue;
                        case 69:
                            columnList.add(column);
                            break;
                        case 70:
                            egresoColumnList.add(column);
                            break;
                        case 49:
                            bbc = true;
                            for (r = 2; r <= 25; r++) {
                                totalIngresoColumn[r] = column[r];
                            }
                            break;
                        case 21:
                            bbc = true;
                            for (r = 2; r <= 25; r++) {
                                totalEgresoColumn[r] = column[r];
                            }
                            break;
                        case 22:
                            bbc = true;
                            for (r = 2; r <= 25; r++) {
                                netoColumn[r] = column[r];
                            }
                            break;
                        case 1:
                            //case 0://las quincenales no tiene tipo de concepto
                            for (r = 2; r <= 25; r++) {
                                BigDecimal db2 = (BigDecimal) column[r];
                                BigDecimal db = (BigDecimal) totalIngresoColumn[r];
                                if (db == null) {
                                    db = db2;
                                } else if (db2 != null) {
                                    db = db.add(db2);
                                }
                                totalIngresoColumn[r] = db;
                            }
                            columnList.add(column);
                            break;
                        case 2:
                            for (r = 2; r <= 25; r++) {
                                BigDecimal db2 = (BigDecimal) column[r];
                                BigDecimal db = (BigDecimal) totalEgresoColumn[r];
                                if (db == null) {
                                    db = db2;
                                } else if (db2 != null) {
                                    db = db.add(db2);
                                }
                                totalEgresoColumn[r] = db;
                            }
                            egresoColumnList.add(column);
                    }

                }
                if (!bbc) {
                    for (r = 2; r <= 25; r++) {
                        BigDecimal db1 = (BigDecimal) totalIngresoColumn[r];
                        BigDecimal db2 = (BigDecimal) totalEgresoColumn[r];
                        if (db1 != null) {
                            if (db2 != null) {
                                db1 = db1.subtract(db2);
                            }
                        } else if (db2 != null) {
                            db1 = BigDecimal.ZERO.subtract(db2);
                        }
                        netoColumn[r] = db1;
                    }
                }

                int ingresoColumnSize = columnList.size();
                columnList.add(totalIngresoColumn);

                if (columnList.size() + egresoColumnList.size() < 12) {
                    Object dummy[] = new Object[26];
                    dummy[0] = new Object[]{0, 0, 0, 0, "", 0, 0, 0};
                    int nnn = 12 - (columnList.size() + egresoColumnList.size());
                    while (nnn-- > 0) {
                        egresoColumnList.add(dummy);
                    }
                }

                columnList.addAll(egresoColumnList);
                columnList.add(totalEgresoColumn);
                columnList.add(netoColumn);

                ncolumnas = columnList.size();
                X.log("firstYear=" + firstYear);

                table = new PdfPTable(ncolumnas + 3);
                float[] nv = new float[ncolumnas + 3];
                nv[0] = 480;
                double fac = (13.0 - 15.0) / (109.0 - 6.9960003);
                nv[1] = (float) ((float) cargoC[0] * (((-109.0 + (float) cargoC[0]) * (fac)) + (float) 13.0));
                nv[2] = (float) ((float) abrevC[0] * (((-109.0 + (float) abrevC[0]) * (fac)) + (float) 13.0));

                float w = 10000;
                w = (w - nv[0] - nv[1] - nv[2]) / ncolumnas;
                for (int ii = 0; ii < ncolumnas; ii++) {
                    nv[3 + ii] = w;
                }
                table.setWidths(nv);
                table.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph("AÑO\nMES", FN6));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setRowspan(2);
                table.addCell(cell);
                int tt = employee.getType().getId();
                cell = new PdfPCell(new Paragraph(tt == 2 ? "CATEGORIA" : "CARGO", FN6));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setBorderWidthLeft(0);
                cell.setRowspan(2);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(tt == 2 ? "DEDICACION" : "NIVEL", FN6));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setBorderWidthLeft(0);
                cell.setRowspan(2);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("INGRESOS", FN6));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setColspan(ingresoColumnSize);
                cell.setBorderWidthLeft(0);
                cell.setBorderWidthBottom(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("TOTAL INGRESOS", FN6));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setRowspan(2);
                cell.setBorderWidthLeft(0);
                cell.setBorderWidthBottom(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("DESCUENTOS", FN6));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setColspan(egresoColumnList.size());
                cell.setBorderWidthLeft(0);
                cell.setBorderWidthBottom(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("TOTAL DESCUENTOS", FN6));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setRowspan(2);
                cell.setBorderWidthLeft(0);
                cell.setBorderWidthBottom(0);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("NETO A PAGAR", FN6));
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cell.setRowspan(2);
                cell.setBorderWidthLeft(0);
                cell.setBorderWidthBottom(0);
                table.addCell(cell);
                //Cabeceras
                for (Object[] col2 : columnList) {
                    Object[] header = (Object[]) col2[0];
                    if (((Integer) header[7]) != 4) {//no son los totales
                        cell = new PdfPCell(new Paragraph(header[3] + "."
                                + "" + header[4] + "-" + header[7], FN6));
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        cell.getColumn().setFilledWidth(1000);
                        cell.setBorderWidthLeft(0);
                        cell.setPadding(0.7f);
                        table.addCell(cell);
                    }
                }
                //por cada 2 años;
                for (int anioI = 0; anioI < 2; anioI++) {
                    cell = new PdfPCell(new Paragraph("" + (firstYear + anioI), FN6));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    cell.setPadding(0.0f);
                    cell.setPaddingBottom(1.2f);
                    table.addCell(cell);
                    cell = new PdfPCell(new Paragraph(""));
                    cell.setPadding(0.0f);
                    cell.setColspan(columnList.size() + 2);
                    table.addCell(cell);
                    int rowIndex = 0;

                    for (int mesJ = 0; mesJ < 12; mesJ++) {
                        //Columna meses
                        pa = new Paragraph("" + nmes[mesJ], FN6);
                        pa.setSpacingBefore(0.0f);
                        pa.setSpacingAfter(0.0f);
                        cell = new PdfPCell(pa);
                        cell.setNoWrap(true);
                        cell.setPadding(0.0f);
                        cell.setPaddingBottom(1.2f);
                        cell.setPaddingLeft(0.7f);
                        cell.setBorderWidthTop(0);

                        rowIndex = mesJ + 2 + anioI * 12;
                        if (rowIndex < 25) {
                            cell.setBorderWidthBottom(0);
                        }
                        table.addCell(cell);
                        pa = new Paragraph(
                                mesPagado[2 + anioI * 12 + mesJ] != null ? X.toText(cargoC[2 + anioI * 12 + mesJ]) : "", FN6);
                        pa.setSpacingBefore(0.0f);
                        pa.setSpacingAfter(0.0f);
                        cell = new PdfPCell(pa);
                        cell.setNoWrap(true);
                        cell.setPadding(0.0f);
                        cell.setPaddingBottom(1.2f);
                        cell.setBorderWidthLeft(0);
                        cell.setPaddingLeft(0.7f);
                        cell.setBorderWidthTop(0);
                        if (rowIndex < 25) {
                            cell.setBorderWidthBottom(0);
                        }
                        table.addCell(cell);
                        pa = new Paragraph(
                                mesPagado[2 + anioI * 12 + mesJ] != null ? X.toText(abrevC[2 + anioI * 12 + mesJ]) : "", FN6);
                        pa.setSpacingBefore(0.0f);
                        pa.setSpacingAfter(0.0f);
                        cell = new PdfPCell(pa);
                        cell.setNoWrap(true);
                        cell.setPadding(0.0f);
                        cell.setPaddingBottom(1.2f);
                        cell.setBorderWidthLeft(0);
                        cell.setBorderWidthTop(0);
                        cell.setPaddingLeft(0.0f);
                        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        if (rowIndex < 25) {
                            cell.setBorderWidthBottom(0);
                        }
                        table.addCell(cell);
                        //Columnas cantidades
                        for (Object[] col2 : (Collection<Object[]>) columnList) {
                            Object v = col2[rowIndex];
                            if (v != null) {
                                pa = new Paragraph("" + df.format(v), FN6);
                                pa.setSpacingBefore(0.0f);
                                pa.setSpacingAfter(0.0f);
                                cell = new PdfPCell(pa);
                            } else {
                                cell = new PdfPCell(blank);
                            }
                            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                            cell.setPadding(0.0f);
                            cell.setPaddingBottom(1.2f);
                            cell.setPaddingRight(0.8f);
                            cell.setBorderWidthLeft(0);
                            cell.setBorderWidthTop(0);
                            if (rowIndex < 25) {
                                cell.setBorderWidthBottom(0);
                            }
                            cell.setNoWrap(true);
                            table.addCell(cell);
                        }
                    }
                }
                document.add(table);
                table = new PdfPTable(2);
                table.setWidthPercentage(100);
                Calendar c = Calendar.getInstance();
                Date ini = XDate.getDate(firstYear, 0, 1);
                c.setTime(ini);
                c.add(Calendar.YEAR, 2);
                c.add(Calendar.DAY_OF_YEAR, -1);
                Date fin = c.getTime();
                System.out.println("---- ini=" + XDate.format(ini) + ";fin=" + XDate.format(fin));
                Integer nd[] = (Integer[]) getTime(tm, 1, 2, 0, tm.getRowCount(),
                        ini, fin
                );
                System.out.println(X.gson.toJson(nd));
                cell = new PdfPCell(new Paragraph("Tiempo de Servicio Registrado: " + (XUtil.intValue(nd[0]) > 0 ? nd[0] + " AÑO" + (XUtil.intValue(nd[0]) > 1 ? "S" : "") : "") + " " + (XUtil.intValue(nd[1]) > 0 ? nd[1] + " mes" + (XUtil.intValue(nd[1]) > 1 ? "es" : "") : "") + " " + (XUtil.intValue(nd[2]) > 0 ? nd[2] + " dia" + (XUtil.intValue(nd[2]) > 1 ? "s" : "") : "")
                        + " de " + (XUtil.intValue(tiempoTotal[0]) > 0 ? tiempoTotal[0] + " AÑO" + (XUtil.intValue(tiempoTotal[0]) > 1 ? "S" : "") : "") + " " + (XUtil.intValue(tiempoTotal[1]) > 0 ? tiempoTotal[1] + " mes" + (XUtil.intValue(tiempoTotal[1]) > 1 ? "es" : "") : "") + " " + (XUtil.intValue(tiempoTotal[2]) > 0 ? tiempoTotal[2] + " dia" + (XUtil.intValue(tiempoTotal[2]) > 1 ? "s" : "") : ""),
                        FN8));
                cell.setPadding(0.0f);
                cell.setPaddingBottom(1.2f);
                cell.setPaddingRight(0.6f);
                cell.setBorderWidth(0);
                cell.setColspan(2);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("Se expide la presente, a solicitud de la persona interesada para los fines que estime conveniente", FN8));
                cell.setPadding(0.0f);
                cell.setPaddingBottom(1.2f);
                cell.setPaddingRight(0.6f);
                cell.setBorderWidth(0);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Nuevo Chimbote, " + sdf2.format(X.getServerDate()) + "\n\n\n\n\n\n\n\n\n\n", FN8));
                cell.setPadding(0.0f);
                cell.setPaddingBottom(1.2f);
                cell.setPaddingRight(0.6f);
                cell.setBorderWidth(0);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                cell.setColspan(2);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph((String) m.get("SIGN-01"), FB8));
                cell.setPadding(0.0f);
                cell.setPaddingBottom(1.2f);
                cell.setPaddingRight(0.6f);
                cell.setBorderWidth(0);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph((String) m.get("SIGN-02"), FB8));
                cell.setPadding(0.0f);
                cell.setPaddingBottom(1.2f);
                cell.setPaddingRight(0.6f);
                cell.setBorderWidth(0);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph((String) m.get("AREA-01"), FN8));
                cell.setPadding(0.0f);
                cell.setPaddingBottom(1.2f);
                cell.setPaddingRight(0.6f);
                cell.setBorderWidth(0);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph((String) m.get("AREA-02"), FN8));
                cell.setPadding(0.0f);
                cell.setPaddingBottom(1.2f);
                cell.setPaddingRight(0.6f);
                cell.setBorderWidth(0);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell);
//                PdfContentByte cb = writer.getDirectContent();
//                            ColumnText.showTextAligned(
//                                    cb,
//                                    Element.ALIGN_LEFT,
//                                    new Phrase(String.format("Elaborado: eaap-jcgc", 12), FN8),
//                                    document.left(),
//                                    document.bottom() - 20,
//                                    0);
                if (!document.isOpen()) {
                    document.open();
                }
                document.add(table);
            }
            document.close();
            return null;///JR.open(out);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getTime(TableModel model, int coIni, int coFin, int rini, int rfin, Date xIni, Date xFin) {
        //Las fechas deben estar ordenadas
        Calendar cIni = new GregorianCalendar();
        Calendar cFin = new GregorianCalendar();
        int tAños = 0;
        int tMeses = 0;
        int tDias = 0;

        ArrayList<Date[]> fechas = new ArrayList();
//        System.out.println("limitado de " + getSQLDate(xIni) + " a " + getSQLDate(xFin));

        for (int i = rini; i < rfin; i++) {
            Date ini = (Date) model.getValueAt(i, coIni);
            Date fin = (Date) XUtil.isEmpty(model.getValueAt(i, coFin), xFin);

            //para asegurarse q la fecha fin no es nula o no supera la fecha actual
            if (xFin != null) {
                if (XDate.compareDate(ini, xFin) > 0) {
                    continue;
                } else if (fin == null || fin.after(xFin)) {
                    fin = xFin;
                }
            } else if (fin == null) {
                continue;
            }

            if (xIni != null) {
                if (XDate.compareDate(fin, xIni) < 0) {
                    continue;
                } else if (ini.before(xIni)) {
                    ini = xIni;
                }
            }

            int p = -1;
            int ii = 0;
            int jj = fechas.size();
            for (ii = 0; ii < jj; ii++) {
                if (!fechas.get(ii)[0].before(ini)) {
                    p = ii;
                    break;
                }
            }
            if (p == -1) {
                cIni.setTime(ini);
                cIni.add(Calendar.DAY_OF_MONTH, -1);
                if (fechas.size() > 0 && !fechas.get(ii - 1)[1].before(cIni.getTime())) {
                    if (fin.after(fechas.get(ii - 1)[1])) {
                        fechas.get(ii - 1)[1] = fin;
                    }
                } else {
                    fechas.add(new Date[]{ini, fin});
                }
            } else {
                fechas.add(p, new Date[]{ini, fin});
                cFin.setTime(fechas.get(p++)[1]);
                cFin.add(Calendar.DAY_OF_MONTH, 1);

                for (ii = p; ii < fechas.size(); ii++) {
                    if (!cFin.getTime().before(fechas.get(ii)[0])) {
                        if (fechas.get(ii)[1].after(fechas.get(p)[1])) {
                            fechas.get(p)[1] = fechas.get(ii)[1];
                        }
                        fechas.remove(ii--);
                    } else {
                        break;
                    }
                }
            }
        }
        for (Date[] d : fechas) {
            System.out.println(XDate.format(d[0]) + " -> " + XDate.format(d[1]));
        }
        for (Date[] d : fechas) {
            cIni.setTime(d[0]);
            cFin.setTime(d[1]);
            int nAños = cFin.get(Calendar.YEAR) - cIni.get(Calendar.YEAR);
            int nMeses = cFin.get(Calendar.MONTH) - cIni.get(Calendar.MONTH) - 1;
            if (nMeses < 0) {
                nAños--;
                nMeses = 12 + nMeses;
            }
            int mdi = cIni.getActualMaximum(Calendar.DAY_OF_MONTH);
            int mdf = cFin.getActualMaximum(Calendar.DAY_OF_MONTH);
            int ndi = mdi - cIni.get(Calendar.DAY_OF_MONTH) + 1;
            int ndf = cFin.get(Calendar.DAY_OF_MONTH);
            int nd = ndi + ndf + tDias;
//            System.out.println("nd=" + nd + ";mdi=" + mdi + ";ndi=" + ndi);
            if (nd >= mdi) {
                nMeses += (int) Math.floor(nd / mdi);
                tDias = nd % mdi;
//                tDias += ndf;
                nd = tDias;
//                System.out.println("nMeses=" + nMeses + ";nDias=" + nd + ";tDias=" + tDias + ";mdi=" + mdi);
                if (nd >= mdf) {
                    nMeses += (int) Math.floor(nd / mdf);
                    tDias = nd % mdf;
                    nd = tDias;
                }
            } else //                nd += ndf;
            if (mdf - ndf < mdi - ndi) {
                if (nd >= mdf) {
                    nMeses += (int) Math.floor(nd / mdf);
                    tDias = nd % mdf;
                } else {
                    tDias = nd % mdf;
                }
            } else if (nd >= mdi) {
                nMeses += (int) Math.floor(nd / mdi);
                tDias = nd % mdi;
            } else {
                tDias = nd % mdi;
            }
            nAños += (int) Math.floor(nMeses / 12.0);
            nMeses = nMeses % 12;
            tAños += nAños;
            tMeses += nMeses;
            tAños += (int) Math.floor(tMeses / 12.0);
            tMeses = tMeses % 12;
//            System.out.println(getSQLDate(d[0]) + " a " + d[1]);
//            System.out.println();
//            System.out.println("mdi=" + mdi + ";mdf=" + mdf + ";nAños=" + nAños + ";nMeses=" + nMeses + ";nDias=" + nd);
//            System.out.println("tAños=" + tAños + " tMeses=" + tMeses + " tDias=" + tDias);
        }
        return new Integer[]{tAños, tMeses, tDias};
    }

}