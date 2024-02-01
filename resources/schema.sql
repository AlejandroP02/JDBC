BEGIN WORK;
SET TRANSACTION READ WRITE;

SET datestyle = YMD;

DROP TABLE serieEstudio;
DROP TABLE serieGenero;
DROP TABLE serie;
DROP TABLE genero;
DROP TABLE estudio;

CREATE TABLE serie
  (
    id INTEGER PRIMARY KEY NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    imagen VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    episodios INTEGER NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha_estreno DATE,
    licencia VARCHAR(100) NOT NULL,
    src VARCHAR(50) NOT NULL,
    duracion DECIMAL(10, 2) NOT NULL,
    descripcion VARCHAR(2000) NOT NULL
  );

CREATE TABLE estudio
  (
    id INTEGER PRIMARY KEY NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    link VARCHAR(100) NOT NULL,
    fecha DATE,
    num_series INTEGER NOT NULL
  );

CREATE TABLE genero
  (
    id INTEGER PRIMARY KEY NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    link VARCHAR(100) NOT NULL,
    descripcion VARCHAR(2000) NOT NULL,
    num_series INTEGER NOT NULL
  );

CREATE TABLE serieEstudio
  (
    id_serie INTEGER REFERENCES serie(id) ON DELETE CASCADE,
    id_estudio INTEGER REFERENCES estudio(id) ON DELETE CASCADE,
    PRIMARY KEY(id_serie, id_estudio)
  );

CREATE TABLE serieGenero
   (
     id_serie INTEGER REFERENCES serie(id) ON DELETE CASCADE,
     id_genero INTEGER REFERENCES genero(id) ON DELETE CASCADE,
     PRIMARY KEY(id_serie, id_genero)
   );

COMMIT;