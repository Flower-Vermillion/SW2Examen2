package co.edu.poli.SW2Examen2.dominio.modelo;

/**
 * Seguro de vida: añade el beneficiario y su parentesco con el asegurado a los
 * datos comunes de {@link Seguro}.
 */
public class SeguroVida extends Seguro {

    /** Nombre de la persona beneficiaria del seguro. */
    private String nombreBeneficiario;

    /** Parentesco del beneficiario con el asegurado (por ejemplo, madre, hijo). */
    private String parentesco;

    /**
     * Crea un seguro de vida.
     *
     * @param numero             número identificador del seguro
     * @param fechaExpedicion    fecha de expedición ({@code yyyy-MM-dd})
     * @param valor              valor asegurado
     * @param estado             {@code true} si el seguro está activo
     * @param asegurado          persona asegurada
     * @param nombreBeneficiario nombre del beneficiario
     * @param parentesco         parentesco del beneficiario con el asegurado
     */
    public SeguroVida(int numero, String fechaExpedicion, double valor, boolean estado,
                      Asegurado asegurado, String nombreBeneficiario, String parentesco) {
        super(numero, fechaExpedicion, valor, estado, asegurado);
        this.nombreBeneficiario = nombreBeneficiario;
        this.parentesco = parentesco;
    }

    /** @return el texto {@code "Vida"} */
    @Override
    public String getTipo() { return "Vida"; }

    /** @return nombre del beneficiario */
    public String getNombreBeneficiario() { return nombreBeneficiario; }

    /** @param nombreBeneficiario nuevo nombre del beneficiario */
    public void setNombreBeneficiario(String nombreBeneficiario) { this.nombreBeneficiario = nombreBeneficiario; }

    /** @return parentesco del beneficiario con el asegurado */
    public String getParentesco() { return parentesco; }

    /** @param parentesco nuevo parentesco del beneficiario */
    public void setParentesco(String parentesco) { this.parentesco = parentesco; }
}