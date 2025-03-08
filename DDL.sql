DROP SCHEMA IF EXISTS CuidarteDB;
CREATE SCHEMA IF NOT EXISTS CuidarteDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish2_ci;

USE CuidarteDB;
SHOW WARNINGS ;

DROP TABLE IF EXISTS pacientes;
CREATE TABLE IF NOT EXISTS pacientes (
    DNI_Paciente VARCHAR(20) NOT NULL,
    Nombre VARCHAR(100) NOT NULL,
    Apellidos VARCHAR(100) NOT NULL,
    Numero_Telefono VARCHAR(20),
    Email VARCHAR(50) UNIQUE NOT NULL,
    Fecha_Nacimiento DATE NOT NULL,
    Edad SMALLINT,
    Contraseña VARCHAR(255) NOT NULL,
    Salt INT NOT NULL,
    Codigo_Verificacion INT,
    CONSTRAINT PK_paciente PRIMARY KEY (DNI_Paciente)
) ENGINE = InnoDB;
DESC pacientes;

DROP TABLE IF EXISTS medicos;
CREATE TABLE IF NOT EXISTS medicos (
    DNI_Medico VARCHAR(20) NOT NULL,
    Nombre VARCHAR(100) NOT NULL,
    Apellidos VARCHAR(100) NOT NULL,
    Numero_Telefono VARCHAR(20) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Especialidad VARCHAR(50) NOT NULL,
    Descripcion VARCHAR(1000) DEFAULT ('Médico con amplia experiencia en su campo.'),
    CONSTRAINT PK_medico PRIMARY KEY (DNI_Medico)
) ENGINE = InnoDB;
DESC medicos;

DROP TABLE IF EXISTS clinicas;
CREATE TABLE IF NOT EXISTS clinicas (
    ID_Clinica INT UNSIGNED NOT NULL AUTO_INCREMENT,
    Nombre VARCHAR(50) NOT NULL,
    Direccion VARCHAR(100) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    Telefono VARCHAR(20) NOT NULL,
    CONSTRAINT PK_clinica PRIMARY KEY (ID_Clinica)
) ENGINE = InnoDB;
DESC clinicas;

DROP TABLE IF EXISTS disponibilidad_medico;
CREATE TABLE IF NOT EXISTS disponibilidad_medico (
    ID_Disponibilidad INT UNSIGNED NOT NULL AUTO_INCREMENT,
    DNI_Medico VARCHAR(20) NOT NULL,
    ID_Clinica INT UNSIGNED NOT NULL,
    Fecha DATE NOT NULL,
    Hora_Inicio TIME NOT NULL,
    Hora_Fin TIME NOT NULL,
    Duracion_Cita SMALLINT UNSIGNED NOT NULL CHECK (Duracion_Cita > 0),
    CONSTRAINT PK_disponibilidad_medico PRIMARY KEY (ID_Disponibilidad),
    CONSTRAINT FK_disponibilidad_medico_medico FOREIGN KEY (DNI_Medico) REFERENCES medicos(DNI_Medico) ON DELETE CASCADE,
    CONSTRAINT FK_disponibilidad_medico_clinica FOREIGN KEY (ID_Clinica) REFERENCES clinicas(ID_Clinica) ON DELETE CASCADE
) ENGINE = InnoDB;
DESC disponibilidad_medico;

DROP TABLE IF EXISTS franjas_horarias;
CREATE TABLE IF NOT EXISTS franjas_horarias (
    ID_Franja INT UNSIGNED NOT NULL AUTO_INCREMENT,
    ID_Disponibilidad INT UNSIGNED NOT NULL,
    Hora TIME NOT NULL,
    Estado ENUM('Disponible', 'Reservada') NOT NULL DEFAULT 'Disponible',
    CONSTRAINT PK_franjas_horarias PRIMARY KEY (ID_Franja),
    CONSTRAINT FK_franja_disponibilidad FOREIGN KEY (ID_Disponibilidad) REFERENCES disponibilidad_medico(ID_Disponibilidad) ON DELETE CASCADE
) ENGINE = InnoDB;
DESC franjas_horarias;

DROP TABLE IF EXISTS citas_medicas;
CREATE TABLE IF NOT EXISTS citas_medicas (
    ID_Cita INT UNSIGNED NOT NULL AUTO_INCREMENT,
    DNI_Paciente VARCHAR(20) NOT NULL,
    DNI_Medico VARCHAR(20) NOT NULL,
    ID_Clinica INT UNSIGNED NOT NULL,
    Fecha_Hora DATETIME NOT NULL,
    Estado_Cita ENUM('Pendiente', 'Confirmada', 'Cancelada', 'Atendida') DEFAULT 'Pendiente',
    Motivo_Consulta TEXT NOT NULL,
    Observaciones_Medicas TEXT,
    CONSTRAINT PK_citas_medicas PRIMARY KEY (ID_Cita),
    CONSTRAINT FK_citas_medicas_paciente FOREIGN KEY (DNI_Paciente) REFERENCES pacientes(DNI_Paciente) ON DELETE CASCADE,
    CONSTRAINT FK_citas_medicas_medico FOREIGN KEY (DNI_Medico) REFERENCES medicos(DNI_Medico) ON DELETE CASCADE,
    CONSTRAINT FK_citas_medicas_clinica FOREIGN KEY (ID_Clinica) REFERENCES clinicas(ID_Clinica) ON DELETE CASCADE
) ENGINE = InnoDB;
DESC citas_medicas;

DROP TABLE IF EXISTS historias_clinicas;
CREATE TABLE IF NOT EXISTS historias_clinicas (
    ID_Historia_Clinica INT UNSIGNED NOT NULL AUTO_INCREMENT,
    DNI_Paciente VARCHAR(20) NOT NULL,
    DNI_Medico VARCHAR(20) NOT NULL,
    Fecha_Visita DATETIME NOT NULL,
    Temperatura DECIMAL(4,2) UNSIGNED,
    Sistolica SMALLINT UNSIGNED,
    Diastolica SMALLINT UNSIGNED,
    Frecuencia_Cardiaca SMALLINT UNSIGNED,
    Saturacion_O2 TINYINT(3) UNSIGNED CHECK (Saturacion_O2 BETWEEN 0 AND 100),
    Peso DECIMAL(6,2) UNSIGNED,
    Altura DECIMAL(4,2) UNSIGNED,
    Antecedentes TEXT NOT NULL,
    Alergias TEXT NOT NULL,
    Diagnosis TEXT NOT NULL,
    Motivo_Consulta TEXT NOT NULL,
    Exploracion_Fisica TEXT NOT NULL,
    Tratamiento TEXT NOT NULL,
    CONSTRAINT PK_historia_clinica PRIMARY KEY (ID_Historia_Clinica),
    CONSTRAINT FK_historia_clinica_paciente FOREIGN KEY (DNI_Paciente) REFERENCES pacientes(DNI_Paciente) ON DELETE CASCADE,
    CONSTRAINT FK_historia_clinica_medico FOREIGN KEY (DNI_Medico) REFERENCES medicos(DNI_Medico) ON DELETE CASCADE
) ENGINE = InnoDB;
DESC historias_clinicas;

DROP TABLE IF EXISTS pruebas_diagnosticas;
CREATE TABLE IF NOT EXISTS pruebas_diagnosticas (
    ID_Prueba INT UNSIGNED NOT NULL AUTO_INCREMENT,
    ID_Historia_Clinica INT UNSIGNED NOT NULL,
    Tipo_Prueba ENUM('Hemograma', 'Bioquímica', 'Inmunología', 'Microbiología', 'Radiografía') NOT NULL,
    Fecha_Realizacion DATETIME NOT NULL,
    Resultados TEXT NOT NULL,
    CONSTRAINT PK_pruebas_diagnosticas PRIMARY KEY (ID_Prueba),
    CONSTRAINT FK_pruebas_diagnosticas_historia_clinica FOREIGN KEY (ID_Historia_Clinica) REFERENCES historias_clinicas(ID_Historia_Clinica) ON DELETE CASCADE
) ENGINE = InnoDB;
DESC pruebas_diagnosticas;

DROP TABLE IF EXISTS hemogramas;
CREATE TABLE IF NOT EXISTS hemogramas (
    ID_Hemograma INT UNSIGNED NOT NULL AUTO_INCREMENT,
    ID_Prueba INT UNSIGNED NOT NULL,
    Hematies DECIMAL(5,2) UNSIGNED NOT NULL,
    Hemoglobina DECIMAL(5,2) UNSIGNED NOT NULL,
    Hematocrito DECIMAL(5,2) UNSIGNED NOT NULL,
    VCM DECIMAL(5,2) UNSIGNED NOT NULL,
    HCM DECIMAL(5,2) UNSIGNED NOT NULL,
    CHCM DECIMAL(5,2) UNSIGNED NOT NULL,
    ADE DECIMAL(5,2) UNSIGNED NOT NULL,
    Plaquetas INT UNSIGNED NOT NULL,
    VPM DECIMAL(5,2) UNSIGNED NOT NULL,
    ADP DECIMAL(5,2) UNSIGNED NOT NULL,
    Plaquetocrito DECIMAL(5,4) UNSIGNED NOT NULL,
    Leucocitos DECIMAL(5,2) UNSIGNED NOT NULL,
    Neutrofilos DECIMAL(5,2) UNSIGNED NOT NULL,
    Linfocitos DECIMAL(5,2) UNSIGNED NOT NULL,
    Monocitos DECIMAL(5,2) UNSIGNED NOT NULL,
    Eosinofilos DECIMAL(5,2) UNSIGNED NOT NULL,
    Basofilos DECIMAL(5,2) UNSIGNED NOT NULL,
    CONSTRAINT PK_hemograma PRIMARY KEY (ID_Hemograma),
    CONSTRAINT FK_hemograma_prueba FOREIGN KEY (ID_Prueba) REFERENCES pruebas_diagnosticas(ID_Prueba) ON DELETE CASCADE
) ENGINE = InnoDB;
DESC hemogramas;

DROP TABLE IF EXISTS bioquimicas;
CREATE TABLE IF NOT EXISTS bioquimicas (
    ID_Bioquimica INT UNSIGNED NOT NULL AUTO_INCREMENT,
    ID_Prueba INT UNSIGNED NOT NULL,
    Glucemia INT UNSIGNED NOT NULL,
    Urea INT UNSIGNED NOT NULL,
    Creatina DECIMAL(5,2) UNSIGNED NOT NULL,
    Acido_Urico DECIMAL(5,2) UNSIGNED NOT NULL,
    Colesterol_Total INT UNSIGNED NOT NULL,
    Colesterol_HDL INT UNSIGNED NOT NULL,
    Colesterol_LDL INT UNSIGNED NOT NULL,
    Trigliceridos INT UNSIGNED NOT NULL,
    GOT_AST INT UNSIGNED NOT NULL,
    GPT_ALT INT UNSIGNED NOT NULL,
    Gamma_GT INT UNSIGNED NOT NULL,
    Fosfatasa_Alcalina INT UNSIGNED NOT NULL,
    Bilirrubina_Total DECIMAL(5,2) UNSIGNED NOT NULL,
    CONSTRAINT PK_bioquimica PRIMARY KEY (ID_Bioquimica),
    CONSTRAINT FK_bioquimica_prueba FOREIGN KEY (ID_Prueba) REFERENCES pruebas_diagnosticas(ID_Prueba) ON DELETE CASCADE
) ENGINE = InnoDB;
DESC bioquimicas;

DROP TABLE IF EXISTS inmunologias;
CREATE TABLE IF NOT EXISTS inmunologias (
    ID_Inmunologia INT UNSIGNED NOT NULL AUTO_INCREMENT,
    ID_Prueba INT UNSIGNED NOT NULL,
    Linfocitos_T_CD4 INT UNSIGNED NOT NULL,
    Linfocitos_T_CD4_Recuento INT UNSIGNED NOT NULL,
    Linfocitos_T_CD8 INT UNSIGNED NOT NULL,
    Linfocitos_T_CD8_Recuento INT UNSIGNED NOT NULL,
    Indice_CD4CD8 DECIMAL(5,2) UNSIGNED NOT NULL,
    CONSTRAINT PK_inmunologia PRIMARY KEY (ID_Inmunologia),
    CONSTRAINT FK_inmunologia_prueba FOREIGN KEY (ID_Prueba) REFERENCES pruebas_diagnosticas(ID_Prueba) ON DELETE CASCADE
) ENGINE = InnoDB;
DESC inmunologias;

DROP TABLE IF EXISTS microbiologias;
CREATE TABLE IF NOT EXISTS microbiologias (
    ID_Microbiologia INT UNSIGNED NOT NULL AUTO_INCREMENT,
    ID_Prueba INT UNSIGNED NOT NULL,
    Resultado VARCHAR(2000) NOT NULL,
    CONSTRAINT PK_microbiologia PRIMARY KEY (ID_Microbiologia),
    CONSTRAINT FK_microbiologia_prueba FOREIGN KEY (ID_Prueba) REFERENCES pruebas_diagnosticas(ID_Prueba) ON DELETE CASCADE
) ENGINE = InnoDB;
DESC microbiologias;

DROP TABLE IF EXISTS radiografias;
CREATE TABLE IF NOT EXISTS radiografias (
    ID_Radiografia INT UNSIGNED NOT NULL AUTO_INCREMENT,
    ID_Prueba INT UNSIGNED NOT NULL,
    Resultado VARCHAR(2000) NOT NULL,
    URLImagen VARCHAR(200) NOT NULL,
    CONSTRAINT PK_radiografia PRIMARY KEY (ID_Radiografia),
    CONSTRAINT FK_radiografia_prueba FOREIGN KEY (ID_Prueba) REFERENCES pruebas_diagnosticas(ID_Prueba) ON DELETE CASCADE
) ENGINE = InnoDB;
DESC radiografias;

DROP TABLE IF EXISTS mensajes;
CREATE TABLE IF NOT EXISTS mensajes (
    ID_Mensaje INT UNSIGNED NOT NULL AUTO_INCREMENT,
    DNI_Paciente VARCHAR(20) NOT NULL,
    DNI_Medico VARCHAR(20) NOT NULL,
    Fecha_Mensaje DATETIME NOT NULL,
    Contenido TEXT NOT NULL,
    CONSTRAINT PK_mensajes PRIMARY KEY (ID_Mensaje),
    CONSTRAINT FK_mensajes_paciente FOREIGN KEY (DNI_Paciente) REFERENCES pacientes(DNI_Paciente) ON DELETE CASCADE,
    CONSTRAINT FK_mensajes_medico FOREIGN KEY (DNI_Medico) REFERENCES medicos(DNI_Medico) ON DELETE CASCADE
) ENGINE = InnoDB;
DESC mensajes;