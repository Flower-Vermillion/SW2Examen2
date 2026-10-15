# Gestión de Seguros — SW2Examen2

Aplicación de escritorio (JavaFX) para administrar **seguros de vida** y **seguros de vehículo**, con persistencia en **MySQL**. El proyecto está construido con **arquitectura hexagonal** (puertos y adaptadores): la lógica de negocio no depende de la interfaz gráfica ni de la base de datos.

## Funcionalidades

- **Crear** un seguro (de vida o de vehículo).
- **Consultar** todos los seguros en una tabla y **consultar uno por número**.
- **Actualizar** los datos de un seguro existente.
- **Eliminar** un seguro (con confirmación).
- **Limpiar** el formulario.
- El formulario muestra solo los campos propios del tipo elegido:
  - Vida: beneficiario y parentesco.
  - Vehículo: marca.

### Reglas de validación (capa de aplicación)

- El número del seguro debe ser mayor que 0.
- La fecha de expedición es obligatoria y debe tener formato `yyyy-MM-dd`.
- El valor debe ser mayor que 0.
- Al crear: no puede existir otro seguro con el mismo número.
- Al actualizar y eliminar: el seguro debe existir.

## Tecnologías

| Componente | Detalle |
|---|---|
| Lenguaje | Java 17 (`maven.compiler.release` = 17) |
| Interfaz | JavaFX 21.0.2 (`javafx-controls` y `javafx-fxml`) + FXML |
| Base de datos | MySQL 8 + `mysql-connector-j` 8.4.0 (JDBC) |
| Configuración | [dotenv-java](https://github.com/cdimascio/dotenv-java) 3.0.0 (`io.github.cdimascio:dotenv-java`) |
| Build | Maven (con `javafx-maven-plugin` 0.0.8) |

## Arquitectura

📐 **Ver el [Diagrama Hexagonal](https://github.com/Flower-Vermillion/SW2Examen2/wiki/Diagrama-Hexagonal)** en la wiki del repositorio.

- **Dominio**: entidades del negocio. `Seguro` es una clase abstracta con los datos comunes; `SeguroVida` y `SeguroVehiculo` la extienden con sus campos propios. Cada seguro pertenece a un `Asegurado`.
- **Aplicación**: define los *puertos de entrada* (un caso de uso por operación) y el *puerto de salida* `SeguroRepository`. Los servicios implementan los casos de uso y contienen las validaciones.
- **Infraestructura**: los *adaptadores*. `ControlFormSeguro` (entrada) solo conoce las interfaces de los casos de uso. `MySqlSeguroRepository` (salida) implementa el repositorio con JDBC.
- **Vista / `App`**: punto de entrada que conecta todas las piezas (inyección de dependencias manual por constructor).

### Diagrama de clases

📐 **Ver el [Diagrama de clases](https://github.com/Flower-Vermillion/SW2Examen2/wiki/Diagrama-de-clases)** en la wiki del repositorio.

`Asegurado` se relaciona con muchos `Seguro` (1 a *). `Seguro` es abstracta y se especializa en `SeguroVida` y `SeguroVehiculo`. `ConexionBD` implementa el patrón Singleton para compartir una única conexión JDBC.

## Estructura del proyecto

```
src/main/java/co/edu/poli/SW2Examen2/
├── aplicacion/puerto/
│   ├── entrada/        # Puertos de entrada (casos de uso)
│   │   ├── CrearSeguroCasosDeUso
│   │   ├── ConsultarUnoSeguroCasosDeUso
│   │   ├── ConsultarTodosSeguroCasosDeUso
│   │   ├── ActualizarSeguroCasosDeUso
│   │   └── EliminarSeguroCasosDeUso
│   ├── salida/
│   │   └── SeguroRepository
│   └── servicio/       # Implementación de los casos de uso
│       ├── CrearSeguroServicio
│       ├── ConsultarUnoSeguroServicio
│       ├── ConsultarTodosSeguroServicio
│       ├── ActualizarSeguroServicio
│       └── EliminarSeguroServicio
├── dominio/modelo/
│   ├── Asegurado
│   ├── Seguro (abstracta)
│   ├── SeguroVida
│   └── SeguroVehiculo
├── infraestructura/
│   ├── persistencia/
│   │   ├── ConexionBD              # Singleton de conexión JDBC
│   │   └── MySqlSeguroRepository   # Adaptador de salida
│   └── ui/
│       └── ControlFormSeguro       # Adaptador de entrada (controlador JavaFX)
└── vista/
    └── App                         # Punto de entrada

src/main/resources/co/edu/poli/SW2Examen2/vista/
└── FormSeguro.fxml

.env.example   # Plantilla de configuración de la conexión
pom.xml
```

## Base de datos

Se usa una tabla por subtipo: `seguro` guarda los datos comunes y `seguroVida` / `seguroVehiculo` guardan los datos específicos, unidas por la columna `numero`.

```sql
CREATE DATABASE examen2;
USE examen2;

CREATE TABLE asegurado (
    identificacion VARCHAR(20)  NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    correoElectronico VARCHAR(100) NOT NULL,
    PRIMARY KEY (identificacion)
);

CREATE TABLE seguro (
    numero INT NOT NULL PRIMARY KEY,
    fechaExpedicion VARCHAR(10) NOT NULL,
    valor DOUBLE NOT NULL,
    estado BOOLEAN NOT NULL,
    identificacionAsegurado VARCHAR(20) NOT NULL,
    FOREIGN KEY (identificacionAsegurado) REFERENCES asegurado (identificacion)
);

CREATE TABLE seguroVida (
    numero INT NOT NULL PRIMARY KEY,
    nombre_beneficiario VARCHAR(100) NOT NULL,
    parentesco VARCHAR(50) NOT NULL,
    FOREIGN KEY (numero) REFERENCES seguro (numero)
);

CREATE TABLE seguroVehiculo (
    numero INT NOT NULL PRIMARY KEY,
    marca VARCHAR(50) NOT NULL,
    FOREIGN KEY (numero) REFERENCES seguro (numero)
);

INSERT INTO asegurado (identificacion, nombre, correoElectronico) VALUES
    ('1001', 'Laura Gómez',    'laura.gomez@gmail.com'),
    ('1002', 'Carlos Ramírez', 'carlos.ramirez@gmail.com'),
    ('1003', 'Marta Londoño',  'marta.londono@gmail.com'),
    ('1004', 'Andrés Pérez',   'andres.perez@gmail.com');
```

Los asegurados que aparecen en el combo del formulario están definidos en `App.java` y **deben coincidir** (identificación) con los de la tabla `asegurado`.

## Configuración de la conexión

La conexión se lee desde un archivo `.env` en la raíz del proyecto. Copia `.env.example` a `.env` y ajusta los valores:

```env
DB_URL=jdbc:mysql://localhost:3306/examen2?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
DB_USER=root
DB_PASSWORD=tu_contraseña
```

Si el archivo no existe, `ConexionBD` usa valores por defecto (`localhost:3306`, usuario `root`, contraseña vacía). **Ojo:** la base de datos por defecto en el código se llama `seguros`, pero el script crea `examen2`, así que debes definir `DB_URL` apuntando a `examen2` (o cambiar el valor por defecto en `ConexionBD`).

> No subas el `.env` al repositorio: agrégalo a `.gitignore`.

## Requisitos

- JDK 17 o superior
- Maven 3.9+
- MySQL 8 en ejecución
- Conexión a internet la primera vez (para descargar dependencias)

## Cómo ejecutarlo

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Flower-Vermillion/SW2Examen2.git
   cd SW2Examen2/SW2Examen2
   ```
   (El proyecto está dentro de la carpeta `SW2Examen2` del repositorio.)
2. Ejecuta el script SQL de la sección *Base de datos* en tu servidor MySQL.
3. Crea el archivo `.env` con tus credenciales.
4. Ejecuta la aplicación:
   - Desde el IDE: ejecuta la clase `co.edu.poli.SW2Examen2.vista.App`.
   - Desde la terminal:
     ```bash
     mvn clean javafx:run
     ```

## Uso

1. Elige el **tipo de seguro**; el formulario muestra los campos que le corresponden.
2. Completa número, fecha (`yyyy-MM-dd`), valor, estado y asegurado.
3. Pulsa **Crear**. El seguro aparece en la tabla.
4. Selecciona una fila para cargarla en el formulario; desde ahí puedes **Actualizar** o **Eliminar**.
5. **Consultar por número** abre un diálogo para buscar un seguro específico.
6. **Limpiar** deja el formulario listo para un nuevo registro.

## Autor

[Flower-Vermillion](https://github.com/Flower-Vermillion) — Examen 2 Software 2
