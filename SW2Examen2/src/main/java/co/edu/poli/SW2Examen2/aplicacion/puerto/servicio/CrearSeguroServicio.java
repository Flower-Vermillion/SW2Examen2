package co.edu.poli.SW2Examen2.aplicacion.puerto.servicio;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import co.edu.poli.SW2Examen2.aplicacion.puerto.entrada.CrearSeguroCasosDeUso;
import co.edu.poli.SW2Examen2.aplicacion.puerto.salida.SeguroRepository;
import co.edu.poli.SW2Examen2.dominio.modelo.Seguro;

/**
 * Servicio de aplicación — implementa el caso de uso de crear un seguro.
 * <p>
 * Valida los datos recibidos, comprueba que el número no esté registrado y
 * delega la persistencia en el puerto de salida {@link SeguroRepository}.
 * </p>
 */
public class CrearSeguroServicio implements CrearSeguroCasosDeUso {

    /** Puerto de salida usado para consultar y guardar seguros. */
    private final SeguroRepository seguroRepository;

    /**
     * Crea el servicio con el repositorio en el que guardará los seguros.
     *
     * @param seguroRepository puerto de salida de seguros
     */
    public CrearSeguroServicio(SeguroRepository seguroRepository) {
        this.seguroRepository = seguroRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Reglas: el seguro no puede ser nulo, el número debe ser mayor que 0, la
     * fecha es obligatoria y debe tener formato {@code yyyy-MM-dd}, el valor
     * debe ser mayor que 0 y no puede existir otro seguro con el mismo número.
     * </p>
     *
     * @throws Exception si alguna validación falla, el número ya existe o
     *                   ocurre un error durante el registro
     */
    @Override
    public void crear(Seguro seguro) throws Exception {
        if (seguro == null) {
            throw new Exception("El seguro no puede ser nulo.");
        }
        if (seguro.getNumero() <= 0) {
            throw new Exception("El numero del seguro es obligatorio.");
        }
        if (esVacio(seguro.getFechaExpedicion())) {
            throw new Exception("La fecha de expedición del seguro es obligatoria.");
        }
        try {
            LocalDate.parse(seguro.getFechaExpedicion().trim());
        } catch (DateTimeParseException e) {
            throw new Exception("La fecha de expedición debe tener el formato yyyy-MM-dd.");
        }
        if (seguro.getValor() <= 0) {
            throw new Exception("El valor del seguro es obligatorio.");
        }

        if (seguroRepository.consultarPorNumero(seguro.getNumero()) != null) {
            throw new Exception("Ya existe un seguro con el numero " + seguro.getNumero() + ".");
        }

        seguroRepository.guardar(seguro);
    }

    /**
     * Indica si un texto es nulo o está compuesto solo por espacios.
     *
     * @param texto texto a evaluar
     * @return {@code true} si el texto es nulo o vacío
     */
    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}