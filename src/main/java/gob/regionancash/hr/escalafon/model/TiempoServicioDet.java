package gob.regionancash.hr.escalafon.model;

import java.util.Date;
import org.isobit.directory.model.Dependency;
import org.isobit.util.XDate;

import gob.regionancash.hr.model.Employee;
import lombok.Data;

@Data
public class TiempoServicioDet {
    
    public static final int[] vv = new int[]{0, 0, 0};

    private short movimiento = 1;
    private String tipo;
    private Employee employee;
    private String descripcion;
    private String abrev;
    private Dependency dependency;
    private String document;
    private Date fechaIni;
    private Date fechaFin;
    private String periodo;

    public int getYears() {
        return v[0];
    }

    public int getMonths() {
        return v[1];
    }

    public int getDays() {
        return v[2];
    }

    public void init() {
        if (fechaIni == null) {
            v = vv;
        } else if (fechaFin != null) {
            v = XDate.getDateDifferenceInDDMMYYYY(fechaIni, fechaFin);
        }
    }

    private int[] v = vv;

}
//public class TiempoServicioDet {
//
//        private short movimiento = 1;
//        private String tipo;
//        private EscPersonal personal;
//        private String descripcion;
//        private String abrev;
//        private Dependency dependency;
//        private String documento;
//        private Date fechaIni;
//        private Date fechaFin;
//        private String periodo;
//
//        public short getMovimiento() {
//            return movimiento;
//        }
//
//        public void setMovimiento(short movimiento) {
//            this.movimiento = movimiento;
//        }
//
//        public String getTipo() {
//            return tipo;
//        }
//
//        public void setTipo(String tipo) {
//            this.tipo = tipo;
//        }
//
//        public EscPersonal getPersonal() {
//            return personal;
//        }
//
//        public void setPersonal(EscPersonal personal) {
//            this.personal = personal;
//        }
//
//        public String getDescripcion() {
//            return descripcion;
//        }
//
//        public void setDescripcion(String descripcion) {
//            this.descripcion = descripcion;
//        }
//
//        public String getAbrev() {
//            return abrev;
//        }
//
//        public void setAbrev(String abrev) {
//            this.abrev = abrev;
//        }
//
//        public Dependency getDependency() {
//            return dependency;
//        }
//
//        public void setDependency(Dependency dependency) {
//            this.dependency = dependency;
//        }
//
//        public Date getFechaIni() {
//            return fechaIni;
//        }
//
//        public void setFechaIni(Date fechaIni) {
//            this.fechaIni = fechaIni;
//        }
//
//        public Date getFechaFin() {
//            return fechaFin;
//        }
//
//        public void setFechaFin(Date fechaFin) {
//            this.fechaFin = fechaFin;
//        }
//
//        public String getPeriodo() {
//            return periodo;
//        }
//
//        public void setPeriodo(String periodo) {
//            this.periodo = periodo;
//        }
//
//        public String getDocumento() {
//            return documento;
//        }
//
//        public void setDocumento(String documento) {
//            this.documento = documento;
//        }
//
//        public int getAnios() {
//            return v[0];
//        }
//
//        public int getMeses() {
//            return v[1];
//        }
//
//        public int getDias() {
//            return v[2];
//        }
//
//        public void init() {
//            if (fechaIni == null) {
//                v = vv;
//            } else if (fechaFin != null) {
//                v = XDate.getDateDifferenceInDDMMYYYY(fechaIni, fechaFin);
//            }
//        }
//
//        private int[] v = vv;
//
//    }
//
//    public static final int[] vv = new int[]{0, 0, 0};
