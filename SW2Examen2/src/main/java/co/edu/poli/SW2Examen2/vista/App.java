package co.edu.poli.SW2Examen2.vista;

import java.util.List;

import co.edu.poli.SW2Examen2.aplicacion.puerto.salida.SeguroRepository;
import co.edu.poli.SW2Examen2.aplicacion.puerto.servicio.ActualizarSeguroServicio;
import co.edu.poli.SW2Examen2.aplicacion.puerto.servicio.ConsultarTodosSeguroServicio;
import co.edu.poli.SW2Examen2.aplicacion.puerto.servicio.ConsultarUnoSeguroServicio;
import co.edu.poli.SW2Examen2.aplicacion.puerto.servicio.CrearSeguroServicio;
import co.edu.poli.SW2Examen2.aplicacion.puerto.servicio.EliminarSeguroServicio;
import co.edu.poli.SW2Examen2.dominio.modelo.Asegurado;
import co.edu.poli.SW2Examen2.infraestructura.persistencia.MySqlSeguroRepository;
import co.edu.poli.SW2Examen2.infraestructura.ui.ControlFormSeguro;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Punto de entrada: aquí se conectan el adaptador de salida (MySQL), los
 * servicios de aplicación y el adaptador de entrada (formulario FXML).
 */
public class App extends Application {

    /**
     * Construye el grafo de dependencias (repositorio, servicios y
     * controlador), carga {@code FormSeguro.fxml} y muestra la ventana
     * principal. Si algo falla, informa el error y cierra la aplicación.
     *
     * @param stage ventana principal proporcionada por JavaFX
     */
    @Override
    public void start(Stage stage) {
        try {
            SeguroRepository repositorio = new MySqlSeguroRepository();

            // Asegurados predeterminados: deben coincidir con los de la tabla "asegurado".
            List<Asegurado> asegurados = List.of(
                    new Asegurado("1001", "Laura Gómez", "laura.gomez@gmail.com"),
                    new Asegurado("1002", "Carlos Ramírez", "carlos.ramirez@gmail.com"),
                    new Asegurado("1003", "Marta Londoño", "marta.londono@gmail.com"),
                    new Asegurado("1004", "Andrés Pérez", "andres.perez@gmail.com"));

            ControlFormSeguro control = new ControlFormSeguro(
                    new CrearSeguroServicio(repositorio),
                    new ConsultarUnoSeguroServicio(repositorio),
                    new ConsultarTodosSeguroServicio(repositorio),
                    new ActualizarSeguroServicio(repositorio),
                    new EliminarSeguroServicio(repositorio),
                    asegurados);

            // El FXML debe estar en src/main/resources/co/edu/poli/SW2Examen2/vista/FormSeguro.fxml
            FXMLLoader loader = new FXMLLoader(App.class.getResource("FormSeguro.fxml"));
            loader.setController(control);
            Parent raiz = loader.load();

            stage.setTitle("Gestión de Seguros");
            stage.setScene(new Scene(raiz, 1500, 620));
            stage.show();
        } catch (Exception e) {
            Alert alerta = new Alert(AlertType.ERROR,
                    "No se pudo iniciar la aplicación: " + e.getMessage());
            alerta.setHeaderText(null);
            alerta.showAndWait();
            Platform.exit();
        }
    }

    /**
     * Método principal: lanza la aplicación JavaFX.
     *
     * @param args argumentos de línea de comandos (no se usan)
     */
    public static void main(String[] args) {
        launch(args);
    }
}