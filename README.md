# JDBC

Este proyecto se basa en la interacción con bases de datos de **PostgreSQL**. En el proyecto, se requiere la posibilidad de realizar distintas acciones en las tablas de la base de datos tanto como eliminar, crearlas, consultar, actualizar e eliminar datos específicos.

## Requisitos

- librería de **PostgreSQL**
- librería de **OpenCSV**
- Rellenar los campos con tu información en el fichero **db.properties**:

```
host		  192.168.1.123
port		  5432
dbname		myanimelist
user		  usuario
password      password
schema    	public
```

## Uso

Para usar el programa es necesario haber creado la base de datos y dar los permisos necesarios para que el programa tenga acceso a ella:


```
  GRANT ALL PRIVILEGES ON DATABASE "myanimelist" TO usuario;
```

> Si creas las tablas con un usuario que no sea el que has especificado en el **db.properties** escribe los siguientes comandos..

```
  alter table serie owner to usuario;
  alter table estudio owner to usuario;
  alter table genero owner to usuario;
  alter table seireestudio owner to usuario;
  alter table seriegenero owner to usuario;
```

Cuando ya esté todo bien configurado, lo único que hará falta será ejecutar el programa y leer las instrucciones que aparecen dependiendo de la opción escogida.
