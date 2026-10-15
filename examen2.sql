CREATE DATABASE examen2;

use examen2;

CREATE TABLE asegurado (
    identificacion VARCHAR(20)  NOT NULL ,
    nombre VARCHAR(100) NOT NULL,
    correoElectronico VARCHAR(100) NOT NULL,
    PRIMARY KEY (identificacion)
);

CREATE TABLE seguro (
    numero INT NOT NULL PRIMARY KEY,
    fechaExpedicion VARCHAR(10)  NOT NULL,   
    valor DOUBLE NOT NULL,
    estado BOOLEAN NOT NULL,
    identificacionAsegurado VARCHAR(20) NOT NULL,
    FOREIGN KEY (identificacionAsegurado) REFERENCES asegurado (identificacion)
);

CREATE TABLE seguroVida (
    numero INT NOT NULL PRIMARY KEY,
    nombre_beneficiario VARCHAR(100) NOT NULL,
    parentesco VARCHAR(50)  NOT NULL,
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