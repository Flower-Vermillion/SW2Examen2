package co.edu.poli.SW2Examen2.dominio.modelo;

/**
 * Entidad de dominio abstracta que agrupa los datos comunes de todo seguro.
 * <p>
 * Cada tipo concreto ({@link SeguroVida}, {@link SeguroVehiculo}) la extiende
 * con sus datos propios y define su nombre legible mediante {@link #getTipo()}.
 * </p>
 */
public abstract class Seguro {

    /** Número del seguro; actúa como identificador único. */
    private int numero;

    /** Fecha de expedición en formato {@code yyyy-MM-dd}. */
    private String fechaExpedicion;

    /** Valor asegurado. */
    private double valor;

    /** Estado del seguro: {@code true} si está activo. */
    private boolean estado;

    /** Persona asegurada a la que pertenece el seguro. */
    private Asegurado asegurado;

    /**
     * Inicializa los datos comunes del seguro. Es {@code protected} porque solo
     * deben invocarlo las subclases.
     *
     * @param numero          número identificador del seguro
     * @param fechaExpedicion fecha de expedición ({@code yyyy-MM-dd})
     * @param valor           valor asegurado
     * @param estado          {@code true} si el seguro está activo
     * @param asegurado       persona asegurada
     */
    protected Seguro(int numero, String fechaExpedicion, double valor, boolean estado, Asegurado asegurado) {
        this.numero = numero;
        this.fechaExpedicion = fechaExpedicion;
        this.valor = valor;
        this.estado = estado;
        this.asegurado = asegurado;
    }

    /**
     * Nombre legible del tipo de seguro (Vida, Vehículo).
     *
     * @return el nombre del tipo de seguro
     */
    public abstract String getTipo();

    /** @return número identificador del seguro */
    public int getNumero() { return numero; }

    /** @param numero nuevo número identificador */
    public void setNumero(int numero) { this.numero = numero; }

    /** @return fecha de expedición ({@code yyyy-MM-dd}) */
    public String getFechaExpedicion() { return fechaExpedicion; }

    /** @param fechaExpedicion nueva fecha de expedición ({@code yyyy-MM-dd}) */
    public void setFechaExpedicion(String fechaExpedicion) { this.fechaExpedicion = fechaExpedicion; }

    /** @return valor asegurado */
    public double getValor() { return valor; }

    /** @param valor nuevo valor asegurado */
    public void setValor(double valor) { this.valor = valor; }

    /** @return {@code true} si el seguro está activo */
    public boolean isEstado() { return estado; }

    /** @param estado {@code true} para marcar el seguro como activo */
    public void setEstado(boolean estado) { this.estado = estado; }

    /** @return persona asegurada */
    public Asegurado getAsegurado() { return asegurado; }

    /** @param asegurado nueva persona asegurada */
    public void setAsegurado(Asegurado asegurado) { this.asegurado = asegurado; }
}