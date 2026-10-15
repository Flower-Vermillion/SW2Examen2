package co.edu.poli.SW2Examen2.aplicacion.puerto.servicio;

import co.edu.poli.SW2Examen2.aplicacion.puerto.entrada.ConsultarUnoSeguroCasosDeUso;
import co.edu.poli.SW2Examen2.aplicacion.puerto.salida.SeguroRepository;
import co.edu.poli.SW2Examen2.dominio.modelo.Seguro;

/**
 * Servicio de aplicación — implementa el caso de uso de leer un seguro por numero.
 */
public class ConsultarUnoSeguroServicio implements ConsultarUnoSeguroCasosDeUso {

    /** Puerto de salida usado para buscar el seguro. */
    private final SeguroRepository seguroRepository;

    /**
     * Crea el servicio con el repositorio en el que buscará el seguro.
     *
     * @param seguroRepository puerto de salida de seguros
     */
    public ConsultarUnoSeguroServicio(SeguroRepository seguroRepository) {
        this.seguroRepository = seguroRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Valida que el número sea mayor que 0 antes de consultar.
     * </p>
     *
     * @throws Exception si el número no es válido o ocurre un error durante la búsqueda
     */
    @Override
    public Seguro consultarUno(int numero) throws Exception {
        if (numero <= 0) {
            throw new Exception("El numero del seguro es obligatorio.");
        }
        return seguroRepository.consultarPorNumero(numero);
    }
}