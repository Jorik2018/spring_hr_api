package gob.regionancash.hr.service;

import java.util.List;
import java.util.Map;
import gob.regionancash.hr.escalafon.model.TiempoServicioDet;
import gob.regionancash.hr.model.Employee;


public interface EscPersonalFacade {

    void load(Employee e);

    Object getContent(Map m);

    List<Object[]> loadArray(int first, int pageSize, String sortField, Map<String, Object> filters);

    public List<Employee> load(int first, int pageSize, String sortField, Map<String, Object> filters);
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
//    public static void main(String[] args) throws IOException {
//        ClassPathUtil.load("D:\\proyecto\\boot.json");
//        EmployeeFacade c = new EmployeeFacade();
//        EntityManager em = JPA.getInstance().getEntityManager();
//        BeanUtils.inject(c, em);
//        XMap m = new XMap("escPersonalList", em.find(Employee.class, 151));
//        c.loadTiempoServicio(0, 0, null, m);
//    }
    List<TiempoServicioDet> loadTiempoServicio(int first, int pageSize, String sortField, Map<String, Object> filters);
    
}
