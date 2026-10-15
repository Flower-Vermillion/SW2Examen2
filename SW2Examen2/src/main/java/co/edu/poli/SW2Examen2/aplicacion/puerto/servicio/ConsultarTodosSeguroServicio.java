package co.edu.poli.SW2Examen2.aplicacion.puerto.servicio;

import java.util.List;

import co.edu.poli.SW2Examen2.aplicacion.puerto.entrada.ConsultarTodosSeguroCasosDeUso;
import co.edu.poli.SW2Examen2.aplicacion.puerto.salida.SeguroRepository;
import co.edu.poli.SW2Examen2.dominio.modelo.Seguro;



/**
 * Servicio de aplicación — implementa el caso de uso de listar todos los seguros.
 */
public class ConsultarTodosSeguroServicio implements ConsultarTodosSeguroCasosDeUso {

    /** Puerto de salida usado para consultar los seguros. */
    private final SeguroRepository seguroRepository;

    /**
     * Crea el servicio con el repositorio del que leerá los seguros.
     *
     * @param seguroRepository puerto de salida de seguros
     */
    public ConsultarTodosSeguroServicio(SeguroRepository seguroRepository) {
        this.seguroRepository = seguroRepository;
    }

    /**
     * {@inheritDoc}
     *
     * @throws Exception si ocurre un error durante la consulta
     */
    @Override
    public List<Seguro> consultarTodos() throws Exception {
        return seguroRepository.consultarTodos();
    }
}