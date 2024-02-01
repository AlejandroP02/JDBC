import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * La clase DBController proporciona métodos para interactuar
 * con la base de datos PostgreSQL.
 * Incluye funciones para comprobar la existencia de tablas y
 * columnas, mostrar información sobre
 * tablas y columnas, eliminar tablas, realizar consultas SELECT
 * con diversas condiciones, y realizar
 * actualizaciones y eliminaciones de registros.
 */
public class DBController {
    /**
     * Conexión con la base de datos.
     */
    private Connection connection;

    /**
     * Scanner para poder captar lo que escribe el usuario.
     */
    Scanner sc = new Scanner(System.in);

    /**
     * Constructor de la clase DBController.
     * @param connection La conexión a la base de datos.
     */
    public DBController(Connection connection) {
        this.connection = connection;
    }

    /**
     * Comprueba si la tabla existe.
     * @param tabla Nombre de la tabla.
     * @return Si la tabla existe o no.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     */
    public boolean tablaExiste(String tabla) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String checkTableSQL = "select tablename from pg_catalog.pg_tables where schemaname='public' and tablename='" + tabla + "'";
            return statement.executeQuery(checkTableSQL).next();
        }
    }

    /**
     * Comprueba si la columna existe.
     * @param tabla Nombre de la tabla.
     * @param columna Nombre de la columna.
     * @return Si la columna existe o no.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     */
    public boolean columnaExiste(String tabla,String columna) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String checkTableSQL = "SELECT column_name FROM information_schema.columns where table_schema = 'public' AND table_name='" + tabla + "' AND column_name='"+columna+"'";
            return statement.executeQuery(checkTableSQL).next();
        }
    }

    /**
     * Muestra el nombre de las tablas de la base de datos.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     */
    public void mostrarTablas() throws SQLException {
        System.out.println();
        String sql = "SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname='public'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        // Iterate through the result set and print table names
        while (resultSet.next()) {
            String tableName = resultSet.getString(1);
            System.out.print(tableName+" ");
        }
        System.out.println();
    }

    /**
     * Muestra el nombre de las columnas que hay en la
     * tabla especificada.
     * @param tabla Nombre de la tabla.
     * @return Numero de columnas que hay en la tabla.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     */
    public int mostrarColumnas(String tabla) throws SQLException {
        System.out.println();
        String sql = "SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '"+tabla+"'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        int columnas=0;
        // Iterate through the result set and print table names
        while (resultSet.next()) {
            String tableName = resultSet.getString(1);
            System.out.print(tableName+" ");
            columnas++;
        }
        System.out.println();
        return columnas;
    }

    /**
     * Elimina una tabla especificada.
     * @param tabla Nombre de la tabla.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     */
    public void eliminarTabla(String tabla) throws SQLException {
        if(tablaExiste(tabla)){
            String sql = "DROP TABLE "+tabla+" CASCADE";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } else if (tabla.equals("cancel")) {
            return;
        } else {
            System.out.println("El nombre esta mal escrito o la tabla no existe.");
            System.out.println("Reescribe el nombre");
            mostrarTablas();
            System.out.println("Escribe "+ConsoleColors.GREEN + "cancel" + ConsoleColors.RESET + " para cancelar");
            eliminarTabla(next());
        }
    }

    /**
     * Elimina todas las tablas de la base de datos.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     */
    public void eliminarTablas() throws SQLException {
        String sql = "SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname='public'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        // Iterate through the result set and print table names
        while (resultSet.next()) {
            String tabla = resultSet.getString(1);

            if(tablaExiste(tabla)){
                String sqlD = "DROP TABLE "+tabla+" CASCADE";
                Statement statementD = connection.createStatement();
                statementD.executeUpdate(sqlD);
            }
        }
    }

    /**
     * Realiza un SELECT buscando un texto especifico.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     */
    public void selectTextoConcreto() throws SQLException {
        mostrarTablas();
        System.out.println("Escribe la tabla que quieres observar: ");
        String tabla = nextLine();
        int numColumnas = mostrarColumnas(tabla);
        System.out.println("Escribe la columna que quieres observar: ");
        String columna = nextLine();
        if(tablaExiste(tabla)){
            if(columnaExiste(tabla, columna)){
                String sql = "SELECT data_type FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '"+tabla+"' AND column_name = '"+columna+"'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                String type="";
                while (resultSet.next()) {
                    type = resultSet.getString("data_type");
                }
                if(type.equals("integer")){
                    System.out.println("Escribe un numero a buscar: ");
                    String texto = nextLine();
                    sql = "SELECT * FROM "+tabla+" WHERE "+columna+"="+texto;
                }else if(type.equals("date")){
                    System.out.println("Escribe un numero a buscar: ");
                    String texto = nextLine();
                    sql = "SELECT * FROM "+tabla+" WHERE (EXTRACT(DAY FROM "+columna+") = "+texto+" OR EXTRACT(MONTH FROM "+columna+") = "+texto+" OR EXTRACT(YEAR FROM "+columna+") = "+texto+")";
                }else if(type.equals("numeric")){
                    System.out.println("Escribe un decimal a buscar: ");
                    String texto = nextLine();
                    sql = "SELECT * FROM "+tabla+" WHERE "+columna+"="+texto;
                }else{
                    System.out.println("Escribe el texto a buscar: ");
                    String texto = nextLine();
                    sql = "SELECT * FROM "+tabla+" WHERE "+columna+" like '%"+texto+"%'";
                }

                resultSet = statement.executeQuery(sql);
                while (resultSet.next()){
                    for (int x = 1; x <= numColumnas; x++) {
                        if(resultSet.getString(x) != null){
                            if (resultSet.getString(x).length()>200){
                                System.out.println(resultSet.getString(x).substring(0, 50)+"...");
                            }else System.out.println(resultSet.getString(x));
                        }else System.out.println();
                    }
                    System.out.println("-".repeat(10));
                }

            }else {
                System.out.println("La columna no existe");
            }
        }else {
            System.out.println("La tabla no existe");
        }
    }

    /**
     * Realiza un SELECT con una condicion dada por el usuario.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     */
    public void selectCondicion() throws SQLException {
        mostrarTablas();
        System.out.println("Escribe la tabla que quieres observar: ");
        String tabla = nextLine();
        int numColumnas = mostrarColumnas(tabla);
        System.out.println("Escribe la condicion que necesites(usa condiciones sql): ");
        String condicion = nextLine();
        if(tablaExiste(tabla)){
            String sql = "SELECT * FROM "+tabla+" WHERE "+condicion;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                for (int x = 1; x <= numColumnas; x++) {
                    if(resultSet.getString(x) != null){
                        if (resultSet.getString(x).length()>200){
                            System.out.println(resultSet.getString(x).substring(0, 50)+"...");
                        }else System.out.println(resultSet.getString(x));
                    }else System.out.println();
                }
                System.out.println("-".repeat(10));
            }
        }else{
            System.out.println("La tabla no existe");
        }
    }

    /**
     * Realiza un SELECT en base a identificadores, por lo que se
     * usa para realizar un SELECT a un elemento especifico de la
     * tabla.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     */
    public void selectElemento() throws SQLException {
        mostrarTablas();
        System.out.println("Escribe la tabla que quieres observar: ");
        String tabla = nextLine();
        int numColumnas = mostrarColumnas(tabla);
        System.out.println("Escribe la id del elemento que quieres ver: ");
        int id1 = nextInt();
        int id2=0;
        if(tabla.equals("serieestudio") || tabla.equals("seriegenero")){
            System.out.println("La tabla que has introducido cuenta de dos identificadores escribe el segundo: ");
            id2 = nextInt();
        }

        if(tablaExiste(tabla)){
            String sql;
            if(tabla.equals("serieestudio") || tabla.equals("seriegenero")){
                sql = "SELECT * FROM "+tabla+" WHERE id_serie ="+id1+ " AND id_"+tabla.replaceAll("serie", "")+"="+id2;
            }else{
                sql = "SELECT * FROM "+tabla+" WHERE id ="+id1;
            }
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                for (int x = 1; x <= numColumnas; x++) {
                    if(resultSet.getString(x) != null){
                        if (resultSet.getString(x).length()>200){
                            System.out.println(resultSet.getString(x).substring(0, 50)+"...");
                        }else System.out.println(resultSet.getString(x));
                    }else System.out.println();
                }
                System.out.println("-".repeat(10));
            }
        }else{
            System.out.println("La tabla no existe");
        }
    }

    /**
     * Realiza un UPDATE en una columna especificada por el usuario,
     * con condiciones especificadas por el usuario.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     */
    public void update() throws SQLException {
        mostrarTablas();
        System.out.println("Escribe la tabla que quieres cambiar: ");
        String tabla = nextLine();
        mostrarColumnas(tabla);
        System.out.println("Escribe la columna que quieres cambiar: ");
        String columna = nextLine();
        String sql = "SELECT data_type FROM information_schema.columns WHERE table_schema = 'public' AND table_name = '"+tabla+"' AND column_name = '"+columna+"'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        String type="";
        while (resultSet.next()) {
            type = resultSet.getString("data_type");
        }
        System.out.println("Escribe la modificacion que quieres hacer("+type+" y que no sea una id): ");
        String update = nextLine();
        System.out.println("Escribe la condicion que para especificar un elemento(usa condiciones sql): ");
        String condicion = nextLine();
        if(tablaExiste(tabla)){
            if (columnaExiste(tabla, columna)){
                sql = "UPDATE "+tabla+" SET "+columna+" = '"+update+"' WHERE "+condicion;
                statement = connection.createStatement();
                statement.executeUpdate(sql);
            }else{
                System.out.println("La columna no existe");
            }
        }else{
            System.out.println("La tabla no existe");
        }
    }

    /**
     * Realiza un DELETE a elección del usuario.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     */
    public void delete() throws SQLException {
        mostrarTablas();
        System.out.println("Escribe la tabla en la que quieres eliminar: ");
        String tabla = nextLine();
        mostrarColumnas(tabla);
        System.out.println("Escribe la condicion que para no eliminar todo(usa condiciones sql): ");
        String condicion = nextLine();
        if(tablaExiste(tabla)){
            String sql = "DELETE FROM "+tabla+" WHERE "+condicion;
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }else{
            System.out.println("La tabla no existe");
        }
    }

    /**
     * Lee un numero escrito por el usuario.
     * @return Un int escrito por el usuario.
     */
    public int nextInt(){
        return sc.nextInt();
    }
    /**
     * Le una palabra escrita por el usuario.
     * @return Un Strign escrito por el usuario.
     */
    public String next(){
        return sc.next();
    }
    /**
     * Le una cadena de texto escrita por el usuario.
     * @return Un String escrito por el usuario.
     */
    public String nextLine(){
        return sc.nextLine();
    }
}
