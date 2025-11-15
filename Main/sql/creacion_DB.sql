DROP DATABASE tfi_mascotas;
CREATE DATABASE if NOT EXISTS tfi_mascotas CHARACTER  SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tfi_mascotas;
-- Clase A
create table mascota (
id BIGINT PRIMARY KEY AUTO_INCREMENT,
eliminado BOOLEAN NOT NULL DEFAULT FALSE,
NOMBRE VARCHAR(60) NOT NULL,
ESPECIE VARCHAR (30) NOT NULL,
RAZA VARCHAR (60),
FECHA_NACIMIENTO DATE,
DUENIO VARCHAR(120) NOT NULL) ENGINE = InnoDB
;
-- Clase B
CREATE TABLE microchip (
id BIGINT PRIMARY KEY AUTO_INCREMENT,
ELIMINADO BOOLEAN NOT NULL DEFAULT FALSE,
CODIGO VARCHAR(25) NOT NULL,
FECHA_IMPLANTACION DATE,
VETERINARIA VARCHAR(120),
OBSERVACIONES VARCHAR(255),
mascota_id BIGINT UNIQUE, 
CONSTRAINT uk_microchip_codigo UNIQUE (CODIGO),
CONSTRAINT fk_microchip_mascota FOREIGN KEY (mascota_id) REFERENCES mascota(id) 
ON DELETE CASCADE ) 
ENGINE =InnoDB;

INSERT INTO mascota (NOMBRE, ESPECIE, RAZA, FECHA_NACIMIENTO, DUENIO) VALUES
('Fido', 'Perro', 'Labrador Retriever', '2020-05-15', 'Ana López'),
('Michi', 'Gato', 'Siamés', '2021-01-20', 'Juan Pérez'),
('Coco', 'Perro', 'Bulldog Francés', '2019-11-01', 'María García'),
('Luna', 'Gato', 'Maine Coon', '2022-03-10', 'Carlos Rodríguez'),
('Max', 'Perro', 'Pastor Alemán', '2018-08-25', 'Sofía Martínez'),
('Pipo', 'Ave', 'Canario', '2023-04-05', 'Javier Sánchez'),
('Rocky', 'Perro', 'Golden Retriever', '2020-12-03', 'Valentina Díaz'),
('Kira', 'Gato', 'Persa', '2021-06-18', 'Pedro Fernández'),
('Toby', 'Perro', 'Beagle', '2022-02-14', 'Elena Ruíz'),
('Nemo', 'Pez', 'Pez Payaso', '2023-01-01', 'Ricardo Gómez');

INSERT INTO microchip (CODIGO, FECHA_IMPLANTACION, VETERINARIA, OBSERVACIONES, mascota_id) VALUES
('CHIP-A1B2C3D4E5F6', '2021-01-10', 'Veterinaria Central', 'Registro inicial de vacunación.', 1),
('CHIP-G7H8I9J0K1L2', '2022-05-20', 'Clínica Patitas Felices', 'Implantado durante esterilización.', 2),
('CHIP-M3N4O5P6Q7R8', '2020-03-01', 'Hospital Animal Sur', 'Control de displasia de cadera.', 3),
('CHIP-S9T0U1V2W3X4', '2023-01-15', 'Veterinaria de Barrio', 'Chequeo general en nueva adopción.', 4),
('CHIP-Y5Z6A7B8C9D0', '2019-02-14', 'Veterinaria El Guardián', 'Mascota de trabajo, registrado en base de datos oficial.', 5),
('CHIP-E1F2G3H4I5J6', '2023-05-10', 'Consultorio Aviar', 'Chip de seguimiento por migración.', 6),
('CHIP-K7L8M9N0O1P2', '2021-03-25', 'Veterinaria Canina Feliz', 'Revisión anual sin novedad.', 7),
('CHIP-Q3R4S5T6U7V8', '2022-08-01', 'Clínica Felina Norte', 'Implantación tardía, previo registro.', 8),
('CHIP-W9X0Y1Z2A3B4', '2022-05-05', 'Veterinaria El Sol', 'Vacunación y desparasitación completas.', 9),
('CHIP-C5D6E7F8G9H0', '2023-02-01', 'Acuario y Servicios', 'Chip especial para registro de peces ornamentales.', 10);

