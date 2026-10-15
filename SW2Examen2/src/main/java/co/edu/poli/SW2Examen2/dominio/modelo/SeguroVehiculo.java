package co.edu.poli.SW2Examen2.dominio.modelo;

/**
 * Seguro de vehículo: añade la marca del vehículo asegurado a los datos
 * comunes de {@link Seguro}.
 */
public class SeguroVehiculo extends Seguro {

    /** Marca del vehículo asegurado. */
    private String marca;

    /**
     * Crea un seguro de vehículo.
     *
     * @param numero          número identificador del seguro
     * @param fechaExpedicion fecha de expedición ({@code yyyy-MM-dd})
     * @param valor           valor asegurado
     * @param estado          {@code true} si el seguro está activo
     * @param asegurado       persona asegurada
     * @param marca           marca del vehículo asegurado
     */
    public SeguroVehiculo(int numero, String fechaExpedicion, double valor, boolean estado,
                          Asegurado asegurado, String marca) {
        super(numero, fechaExpedicion, valor, estado, asegurado);
        this.marca = marca;
    }

    /** @return el texto {@code "Vehículo"} */
    @Override
    public String getTipo() { return "Vehículo"; }

    /** @return marca del vehículo asegurado */
    public String getMarca() { return marca; }

    /** @param marca nueva marca del vehículo */
    public void setMarca(String marca) { this.marca = marca; }
}