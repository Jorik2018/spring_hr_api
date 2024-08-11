package gob.regionancash.hr.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.isobit.directory.model.People;
import org.isobit.util.OptionMap;

@Entity
@Table(name = "personal")
public class Personal implements Serializable {

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    private String dni;
    @Transient
    private People people;
    @Size(max = 2)
    @Column(name = "entidad")
    private String entidad;
    @Size(max = 11)
    @Column(name = "ruc")
    private String ruc;
    @Size(max = 12)
    @Column(name = "cuspp")
    private String cuspp;
    @Column(name = "sctr_salud")
    private Character sctrSalud;
    @Column(name = "sctr_pension")
    private Character sctrPension;
    @Column(name = "tipo_pension")
    private Character tipoPension;
    private static final long serialVersionUID = 1L;
    @Size(max = 20)
    @Column(name = "telefono")
    private String telefono;
    @Size(max = 50)
    @Column(name = "correo_e")
    private String correoE;
    @Column(name = "essalud_vida")
    private Character essaludVida;
    @Size(max = 150)
    @Column(name = "domicilio")
    private String domicilio;
    @Column(name = "estado_civil")
    private Character estadoCivil;
    @Size(max = 120)
    @Column(name = "nombre_pareja")
    private String nombrePareja;
    @Column(name = "num_hijos")
    private Integer numHijos;
    @Column(name = "retencion")
    private Character retencion;
    @Column(name = "discapacidad")
    private Character discapacidad;
    @Column(name = "fecha_inscrip")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInscrip;
    @Column(name = "hor_nocturno")
    private Character horNocturno;
    @Column(name = "sindicalizado")
    private Character sindicalizado;
    @Column(name = "desc_judicial")
    private Character descJudicial;
    @Column(name = "periodicidad")
    private Character periodicidad;
    @Size(max = 2)
    @Column(name = "situacion")
    private String situacion;
    @Column(name = "sit_especial")
    private Character sitEspecial;
    @Column(name = "tipo_pago")
    private Character tipoPago;
    @Size(max = 11)
    @Column(name = "num_cuenta")
    private String numCuenta;
    @Size(max = 2)
    @Column(name = "nivel_rem")
    private String nivelRem;
    @Size(max = 3)
    @Column(name = "cargo_func")
    private String cargoFunc;
    @Size(max = 100)
    @Column(name = "desc_cargo")
    private String descCargo;
    @Column(name = "tipo_hor")
    private Character tipoHor;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_rem")
    private BigDecimal montoRem;
    @Column(name = "monto_incafp")
    private BigDecimal montoIncafp;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "fecha_cese")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCese;
    @Column(name = "fecha_reg")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReg;
    @Column(name = "fecha_mod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMod;
    @Column(name = "estado")
    private Character estado;
    @Column(name = "marcacion")
    private Character marcacion;
    @Size(max = 4)
    @Column(name = "sec_fun")
    private String secFun;
    @Size(max = 2)
    @Column(name = "fuente_f")
    private String fuenteF;
    @Size(max = 5)
    @Column(name = "funcionario")
    private String funcionario;
    @Size(max = 200)
    @Column(name = "cod_reso")
    private String codReso;
    @Size(max = 10)
    @Column(name = "cod_plza")
    private String codPlza;
    @Size(max = 20)
    @Column(name = "cod_cts")
    private String codCts;
//    @JoinColumn(name = "codlocal", referencedColumnName = "codlocal")
//    @ManyToOne
//    private ELocal local;
    @JoinColumn(name = "mod_laboral", referencedColumnName = "mod_laboral")
    @ManyToOne
    private ModLaboral modLaboral;
//    @JoinColumns({
//        @JoinColumn(name = "tipo_trab", referencedColumnName = "tipo_trab", insertable = false, updatable = false)
//        ,
//        @JoinColumn(name = "nivel_rem", referencedColumnName = "nivel_rem", insertable = false, updatable = false)})
//    @ManyToOne(optional = true)
//    private NivelRem nivelRemunerativo;
    @JoinColumn(name = "nivel_educativo", referencedColumnName = "nivel_educativo")
    @ManyToOne
    private NivelEducativo nivelEducativo;
    @JoinColumn(name = "ocupacion", referencedColumnName = "id")
    @ManyToOne
    private Position position;
    @JoinColumn(name = "tipo_contrato", referencedColumnName = "tipo_contrato")
    @ManyToOne
    private TipoContrato tipoContrato;
    @JoinColumn(name = "tipo_trab", referencedColumnName = "tipo_trab")
    @ManyToOne
    private TipoTrabajador tipoTrabajador;

    @Transient
    public Object ext;

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }

//    public Sector getSector() {
//        return sector;
//    }
//
//    public void setSector(Sector sector) {
//        this.sector = sector;
//    }
    private static final Map TIPO_HORARIO_MAP = new OptionMap();

    static {
        TIPO_HORARIO_MAP.put('1', "ADMINISTRATIVO");
        TIPO_HORARIO_MAP.put('2', "VIGILANTE DIA");
        TIPO_HORARIO_MAP.put('3', "VIGILANTE NOCHE");
        TIPO_HORARIO_MAP.put('4', "LIMPIEZA");
    }

    public Map getTIPO_HORARIO_MAP() {
        return TIPO_HORARIO_MAP;
    }

    public Personal() {
    }

    public Personal(String dni) {
        this.dni = dni;
    }

    public String getDni() {
        return dni;
    }

//    public void setLocal(ELocal local) {
//        this.local = local;
//    }
//
//    public ELocal getLocal() {
//        return local;
//    }
//
//    public void setCodlocal(ELocal local) {
//        this.local = local;
//    }
    public void setDni(String dni) {
        this.dni = dni;
    }

//    public NivelRem getNivelRemunerativo() {
//        return nivelRemunerativo;
//    }
//
//    public void setNivelRemunerativo(NivelRem nivelRemunerativo) {
//        this.nivelRemunerativo = nivelRemunerativo;
//        setNivelRem(nivelRemunerativo != null ? nivelRemunerativo.getNivelRemPK().getNivelRem() : null);
//
//    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoE() {
        return correoE;
    }

    public void setCorreoE(String correoE) {
        this.correoE = correoE;
    }

    public Character getEssaludVida() {
        return essaludVida;
    }

    public void setEssaludVida(Character essaludVida) {
        this.essaludVida = essaludVida;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public Character getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(Character estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getNombrePareja() {
        return nombrePareja;
    }

    public void setNombrePareja(String nombrePareja) {
        this.nombrePareja = nombrePareja;
    }

    public Integer getNumHijos() {
        return numHijos;
    }

    public void setNumHijos(Integer numHijos) {
        this.numHijos = numHijos;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public Character getRetencion() {
        return retencion;
    }

    public void setRetencion(Character retencion) {
        this.retencion = retencion;
    }

    public Character getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(Character discapacidad) {
        this.discapacidad = discapacidad;
    }

    public Date getFechaInscrip() {
        return fechaInscrip;
    }

    public void setFechaInscrip(Date fechaInscrip) {
        this.fechaInscrip = fechaInscrip;
    }

    public String getCuspp() {
        return cuspp;
    }

    public void setCuspp(String cuspp) {
        this.cuspp = cuspp;
    }

    public Character getHorNocturno() {
        return horNocturno;
    }

    public void setHorNocturno(Character horNocturno) {
        this.horNocturno = horNocturno;
    }

    public Character getSindicalizado() {
        return sindicalizado;
    }

    public void setSindicalizado(Character sindicalizado) {
        this.sindicalizado = sindicalizado;
    }

    public Character getDescJudicial() {
        return descJudicial;
    }

    public void setDescJudicial(Character descJudicial) {
        this.descJudicial = descJudicial;
    }

    public Character getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(Character periodicidad) {
        this.periodicidad = periodicidad;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public Character getSitEspecial() {
        return sitEspecial;
    }

    public void setSitEspecial(Character sitEspecial) {
        this.sitEspecial = sitEspecial;
    }

    public Character getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(Character tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }

    public String getNivelRem() {
        return nivelRem;
    }

    public void setNivelRem(String nivelRem) {
        this.nivelRem = nivelRem;
    }

    public String getCargoFunc() {
        return cargoFunc;
    }

    public void setCargoFunc(String cargoFunc) {
        this.cargoFunc = cargoFunc;
    }

    public String getDescCargo() {
        return descCargo;
    }

    public void setDescCargo(String descCargo) {
        this.descCargo = descCargo;
    }

    public Character getTipoHor() {
        return tipoHor;
    }

    public void setTipoHor(Character tipoHor) {
        this.tipoHor = tipoHor;
    }

    public BigDecimal getMontoRem() {
        return montoRem;
    }

    public void setMontoRem(BigDecimal montoRem) {
        this.montoRem = montoRem;
    }

    public BigDecimal getMontoIncafp() {
        return montoIncafp;
    }

    public void setMontoIncafp(BigDecimal montoIncafp) {
        this.montoIncafp = montoIncafp;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaCese() {
        return fechaCese;
    }

    public void setFechaCese(Date fechaCese) {
        this.fechaCese = fechaCese;
    }

    public Date getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(Date fechaReg) {
        this.fechaReg = fechaReg;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod(Date fechaMod) {
        this.fechaMod = fechaMod;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public Character getMarcacion() {
        return marcacion;
    }

    public void setMarcacion(Character marcacion) {
        this.marcacion = marcacion;
    }

    public String getSecFun() {
        return secFun;
    }

    public void setSecFun(String secFun) {
        this.secFun = secFun;
    }

    public String getFuenteF() {
        return fuenteF;
    }

    public void setFuenteF(String fuenteF) {
        this.fuenteF = fuenteF;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getCodReso() {
        return codReso;
    }

    public void setCodReso(String codReso) {
        this.codReso = codReso;
    }

    public String getCodPlza() {
        return codPlza;
    }

    public void setCodPlza(String codPlza) {
        this.codPlza = codPlza;
    }

    public String getCodCts() {
        return codCts;
    }

    public void setCodCts(String codCts) {
        this.codCts = codCts;
    }

    public ModLaboral getModLaboral() {
        return modLaboral;
    }

    public void setModLaboral(ModLaboral modLaboral) {
        this.modLaboral = modLaboral;
    }

    public NivelEducativo getNivelEducativo() {
        return nivelEducativo;
    }

    public void setNivelEducativo(NivelEducativo nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

//    public Oficina getOficina() {
//        return oficina;
//    }
//
//    public void setOficina(Oficina oficina) {
//        this.oficina = oficina;
//    }
//    public Pais getNacionalidad() {
//        return nacionalidad;
//    }
//
//    public void setNacionalidad(Pais nacionalidad) {
//        this.nacionalidad = nacionalidad;
//    }
    public TipoContrato getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(TipoContrato tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public TipoTrabajador getTipoTrabajador() {
        return tipoTrabajador;
    }

    public void setTipoTrabajador(TipoTrabajador tipoTrab) {
        this.tipoTrabajador = tipoTrab;
    }

//    public Zona getZona() {
//        return zona;
//    }
//
//    public void setZona(Zona zona) {
//        this.zona = zona;
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dni != null ? dni.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Personal)) {
            return false;
        }
        Personal other = (Personal) object;
        if ((this.dni == null && other.dni != null) || (this.dni != null && !this.dni.equals(other.dni))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return dni != null ? (dni + ": " + "").trim() : null;
    }

    public Character getSctrSalud() {
        return sctrSalud;
    }

    public void setSctrSalud(Character sctrSalud) {
        this.sctrSalud = sctrSalud;
    }

    public Character getSctrPension() {
        return sctrPension;
    }

    public void setSctrPension(Character sctrPension) {
        this.sctrPension = sctrPension;
    }

    public void setTipoPension(Character tipoPension) {
        this.tipoPension = tipoPension;
    }

    public Character getTipoPension() {
        return this.tipoPension;
    }
}
