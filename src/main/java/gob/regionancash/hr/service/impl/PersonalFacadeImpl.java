package gob.regionancash.hr.service.impl;

import gob.regionancash.hr.model.Personal;
import gob.regionancash.hr.service.PersonalFacade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.isobit.util.XUtil;

public class PersonalFacadeImpl implements PersonalFacade {

    @PersistenceContext
    private EntityManager em;

    public Personal prepareCreate(){
        //        p.setNivelEducativo(nivelEducativoFacade.find("07"));
//        p.setOcupacion(ocupacionFacade.find("999001"));
//        p.setNacionalidad(paisFacade.find("9589"));
//        p.setRegPension(regimenPensionarioFacade.find("99"));
//        try {
//            String upload_dir = (String) systemFacade.get("upload_dir", File.separator);
//            if (!upload_dir.endsWith(File.separator)) {
//                upload_dir += File.separator;
//            }
////            foto = new DefaultStreamedContent(new FileInputStream(new File(getAnonymousPath(upload_dir))));
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
        return new Personal();
    }
    
    @Override
    public List<Personal> load(int first, int pageSize, String sortField, Map<String, Object> filters) {
        int optper = XUtil.intValue(filters.get("optper"));
        String qw = (String) filters.get("qPersonal");
        System.out.println("personalFacade.load filters=" + filters);
        Object idCargo = XUtil.isEmpty(filters.get("idCargo"), null);
        Object modLaboral = XUtil.isEmpty(filters.get("modLaboral"), null);
        Object situacion = filters.get("situacion");
        String nombComp = (String) filters.get("nombreCompleto"), dni = (String) filters.get("dni");
        Object mesEje = filters.get("mesEje");
        Object funcionario = filters.get("funcionario");
        String entidad = (String) filters.get("entidad");;
        Object tipoTrabajador = filters.get("tipoTrabajador");
        String JOIN = "", WHERE = "";
        switch (optper) {
            case 200:
                WHERE = " AND ( p.situacion='11' OR ((p.fechaIngreso>=:fechaIni AND p.fechaIngreso<=:fechaFin) OR "
                        + "(p.fechaCese>=:fechaIni AND p.fechaCese<=:fechaFin))) ";
                break;
            case 5:
                //tramite documentario
                WHERE = " AND ((p.situacion='11' AND p.tipoTrabajador.id IN ('20','21','67')) OR (p.tipoTrabajador.id IN ('27','99') AND NOT(p.local IS NULL) AND p.local.code<>'013' )) ";
                break;
            case 2:
                JOIN = " JOIN p.modLaboral m JOIN p.oficina o ";
                WHERE = " AND (p.situacion='11' AND p.entidad='00' AND m.id<>'8' AND (m.id<>'4' OR (m.id='4' AND p.local.code='001'))) ";
                break;
            case 10:
                WHERE = " AND p.situacion='11' AND (NOT p.modLaboral.id='4' OR (p.modLaboral.id='4' AND p.local.code='001')) ";
                sortField="MONTH(p.fechaNac),DAY(p.fechaNac)";
                break;
            default:
                break;
        }
        String sql;
        List<Query> lq = new ArrayList();
        lq.add(em.createQuery("SELECT p " + (sql = "FROM Personal p " + JOIN + " WHERE 1=1 "
                + WHERE
                + (idCargo != null ? " AND p.cargo.idCargo=:idCargo " : "")
                + (funcionario != null ? " AND NOT (p.funcionario IS NULL) " : "")
                + (nombComp != null ? " AND p.nombComp LIKE :nombComp " : "")
                + (mesEje != null ? " AND MONTH(p.fechaNac)=:mes " : "")
                + (dni != null ? " AND p.dni LIKE :dni " : "")
                + (qw != null ? " AND (p.nombComp LIKE :q OR p.dni like :q) " : "")
                + (tipoTrabajador != null ? " AND p.tipoTrabajador=:tipoTrabajador " : "")
                + (entidad != null ? " AND p.entidad=:entidad" : "")
                + (modLaboral != null ? " AND p.modLaboral=:modLaboral " : "")
                + (situacion != null ? " AND p.situacion=:situacion " : ""))
                + (sortField==null?(optper == 2 ? " ORDER BY o.oficinaPK.codarea,o.oficinaPK.codoficina,p.nombComp " : " ORDER BY p.nombComp"):(" ORDER BY "+sortField))
        ));
        if (pageSize > 0) {
            lq.get(0).setFirstResult(first).setMaxResults(pageSize);
            lq.add(em.createQuery("SELECT COUNT(p) " + sql));
        }
        for (Query q : lq) {
            if (entidad != null) {
                q.setParameter("entidad", entidad);
            }
            if (optper == 200) {
                Date fechaIni = (Date) filters.get("fechaIni");
                Date fechaFin = (Date) filters.get("fechaFin");
                q.setParameter("fechaIni", fechaIni);
                q.setParameter("fechaFin", fechaFin);
            }
            if (idCargo != null) {
                q.setParameter("idCargo", idCargo);
            }
            if (tipoTrabajador != null) {
                q.setParameter("tipoTrabajador", tipoTrabajador);
            }
            if (modLaboral != null) {
                q.setParameter("modLaboral", modLaboral);
            }
            if (situacion != null) {
                q.setParameter("situacion", situacion);
            }
            if (nombComp != null) {
                q.setParameter("nombComp", "%" + nombComp.trim().toUpperCase() + "%");
            }
            if (dni != null) {
                q.setParameter("dni", "%" + dni.trim().toUpperCase() + "%");
            }
            if (qw != null) {
                q.setParameter("q", "%" + qw.toUpperCase() + "%");
            }
            if (mesEje != null) {
                q.setParameter("mes", XUtil.intValue(mesEje));
            }
        }
        if (pageSize > 0) {
            filters.put("size", lq.get(1).getSingleResult());
        }
        return lq.get(0).getResultList();
    }

    @Override
    public void remove(List<Personal> personal) {
        for (Personal p : personal) {
            //si alguno tiene un valor de historial no puede eliminarse y
            em.remove(p);
        }
    }

}
