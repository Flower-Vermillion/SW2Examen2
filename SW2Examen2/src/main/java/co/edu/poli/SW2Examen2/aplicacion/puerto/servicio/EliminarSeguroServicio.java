package co.edu.poli.SW2Examen2.aplicacion.puerto.servicio;

import co.edu.poli.SW2Examen2.aplicacion.puerto.entrada.EliminarSeguroCasosDeUso;
import co.edu.poli.SW2Examen2.aplicacion.puerto.salida.SeguroRepository;

/**
 * Servicio de aplicación — implementa el caso de uso de eliminar un seguro.
 */
public class EliminarSeguroServicio implements EliminarSeguroCasosDeUso {

    /** Puerto de salida usado para consultar y eliminar seguros. */
    private final SeguroRepository seguroRepository;

    /**
     * Crea el servicio con el repositorio del que eliminará los seguros.
     *
     * @param seguroRepository puerto de salida de seguros
     */
    public EliminarSeguroServicio(SeguroRepository seguroRepository) {
        this.seguroRepository = seguroRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Valida que el número sea mayor que 0 y que el seguro exista antes de
     * eliminarlo.
     * </p>
     *
     * @throws Exception si el número no es válido, el seguro no existe o
     *                   ocurre un error al eliminar
     */
    @Override
    public void eliminar(int numero) throws Exception {
        if (numero <= 0) {
            throw new Exception("El numero del seguro es obligatorio.");
        }
        if (seguroRepository.consultarPorNumero(numero) == null) {
            throw new Exception("No existe un dron con el numero " + numero + ".");
        }

        seguroRepository.eliminar(numero);
    }
}