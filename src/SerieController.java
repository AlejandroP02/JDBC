import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;

/**
 * La clase SerieController proporciona métodos para
 * interactuar con las tablas serie, serieestudio y
 * seriegenero en la base de datos. Incluye
 * funciones para poblar las tablas desde un
 * archivo CSV y crear la estructura de las tablas
 * en la base de datos.
 */
public class SerieController {
    /**
     * Conexión con la base de datos.
     */
    private Connection connection;

    /**
     * Constructor de la clase SerieController.
     * @param connection La conexión a la base de datos.
     */
    public SerieController(Connection connection) {
        this.connection = connection;
    }

    /**
     * Puebla la tabla serie usando el csv pertinente.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     * @throws IOException Si ocurre un error de entrada/salida
     * durante la ejecución del programa.
     */
    public void autoSerie() throws IOException, SQLException {
        CSVReader reader = new CSVReader(new FileReader("resources/series.csv"), ',');
        String[] record = null;

        reader.readNext();
        while ((record = reader.readNext()) != null) {
            int id = Integer.parseInt(record[0]);
            String titulo = record[1];
            String imagen = record[2];
            String tipo = record[3];
            int episodios = Integer.parseInt(record[4]);
            String estado = record[5];
            LocalDate fechaEstreno;
            if (record[6].equals("null"))fechaEstreno = null;
            else fechaEstreno = LocalDate.parse(record[6]);
            String licencia = record[7];
            String src = record[9];
            float duracion = Float.parseFloat(record[11]);
            String descripcion = record[12];

            String sql = "INSERT INTO serie(id, titulo, imagen, tipo, episodios, estado, fecha_estreno, licencia, src, duracion, descripcion) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            pst.setString(2, titulo);
            pst.setString(3, imagen);
            pst.setString(4, tipo);
            pst.setInt(5, episodios);
            pst.setString(6, estado);
            if (fechaEstreno == null) pst.setDate(7, null);
            else pst.setDate(7, Date.valueOf(fechaEstreno));
            pst.setString(8, licencia);
            pst.setString(9, src);
            pst.setDouble(10, duracion);
            pst.setString(11, descripcion);
            pst.executeUpdate();

        }
        reader.close();
    }

    /**
     * Puebla la tabla serieestudio usando el csv pertinente.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     * @throws IOException Si ocurre un error de entrada/salida
     * durante la ejecución del programa.
     */
    public void autoSerieEstudio() throws IOException, SQLException{
        CSVReader reader = new CSVReader(new FileReader("resources/series.csv"), ',');
        String[] record = null;

        reader.readNext();
        while ((record = reader.readNext()) != null) {
            int id_serie = Integer.parseInt(record[0]);
            String a = record[8].replaceFirst("\\[", "").replaceFirst("]","");
            a=a.replaceAll(" ", "");
            String[] ids= a.split(",");
            ArrayList<Integer> id_estudio = new ArrayList<>();
            for (int x=0;x<ids.length;x++){
                if(ids[x].equals(""));
                else id_estudio.add(Integer.parseInt(ids[x]));
            }
            if(!id_estudio.isEmpty()){
                for (int x = 0; x < id_estudio.size(); x++) {
                    String sql = "INSERT INTO serieestudio(id_serie, id_estudio) VALUES (?,?)";
                    PreparedStatement pst = connection.prepareStatement(sql);
                    pst.setInt(1, id_serie);
                    pst.setInt(2, id_estudio.get(x));
                    pst.executeUpdate();
                }
            }
        }
        reader.close();
    }

    /**
     * Puebla la tabla seriegenero usando el csv pertinente.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     * @throws IOException Si ocurre un error de entrada/salida
     * durante la ejecución del programa.
     */
    public void autoSerieGenero() throws IOException, SQLException{
        CSVReader reader = new CSVReader(new FileReader("resources/series.csv"), ',');
        String[] record = null;

        reader.readNext();
        while ((record = reader.readNext()) != null) {
            int id_serie = Integer.parseInt(record[0]);
            String a = record[10].replaceFirst("\\[", "").replaceFirst("]","");
            a=a.replaceAll(" ", "");
            String[] ids= a.split(",");
            ArrayList<Integer> id_genero = new ArrayList<>();
            for (int x=0;x<ids.length;x++){
                id_genero.add(Integer.parseInt(ids[x]));
            }

            for (int x = 0; x < id_genero.size(); x++) {
                String sql = "INSERT INTO seriegenero(id_serie, id_genero) VALUES (?,?)";
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setInt(1, id_serie);
                pst.setInt(2, id_genero.get(x));
                pst.executeUpdate();
            }

        }
        reader.close();
    }

    /**
     * Crea la tabla serie.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     */
    public void createTable() throws SQLException {

        Statement sts = connection.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS serie ("+
                "id INTEGER PRIMARY KEY NOT NULL,"+
                "titulo VARCHAR(150) NOT NULL,"+
                "imagen VARCHAR(100) NOT NULL,"+
                "tipo VARCHAR(50) NOT NULL,"+
                "episodios INTEGER NOT NULL,"+
                "estado VARCHAR(50) NOT NULL,"+
                "fecha_estreno DATE,"+
                "licencia VARCHAR(100) NOT NULL,"+
                "src VARCHAR(50) NOT NULL,"+
                "duracion DECIMAL(10, 2) NOT NULL,"+
                "descripcion VARCHAR(2000) NOT NULL)";

        sts.executeUpdate(sql);
    }

    /**
     * Crea la tabla intermedia serieestudio.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     */
    public void createSubTable1() throws SQLException {

        Statement sts = connection.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS serieEstudio ("+
                "id_serie INTEGER REFERENCES serie(id) ON DELETE CASCADE," +
                "id_estudio INTEGER REFERENCES estudio(id) ON DELETE CASCADE," +
                "PRIMARY KEY(id_serie, id_estudio))";

        sts.executeUpdate(sql);
    }

    /**
     * Crea la tabla intermedia seriegenero.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     */
    public void createSubTable2() throws SQLException {

        Statement sts = connection.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS serieGenero ("+
                "id_serie INTEGER REFERENCES serie(id) ON DELETE CASCADE," +
                "id_genero INTEGER REFERENCES genero(id) ON DELETE CASCADE," +
                "PRIMARY KEY(id_serie, id_genero))";

        sts.executeUpdate(sql);
    }

}
