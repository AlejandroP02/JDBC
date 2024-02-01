import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

/**
 * La clase EstudioController proporciona métodos para
 * interactuar con la tabla estudio en la base de datos.
 * Incluye funciones para poblar la tabla desde un archivo
 * CSV y crear la estructura de la tabla en la base de datos.
 */
public class EstudioController {
    /**
     * Conexión con la base de datos.
     */
    private Connection connection;

    /**
     * Constructor de la clase EstudioController.
     * @param connection La conexión a la base de datos.
     */
    public EstudioController(Connection connection) {
        this.connection = connection;
    }

    /**
     * Puebla la tabla estudio usando el csv pertinente.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     * @throws IOException Si ocurre un error de entrada/salida
     * durante la ejecución del programa.
     */
    public void autoEstudio() throws IOException, SQLException {
        CSVReader reader = new CSVReader(new FileReader("resources/estudios.csv"), ',');
        String[] record = null;

        reader.readNext();
        while ((record = reader.readNext()) != null){
            int id = Integer.parseInt(record[0]);
            String nombre = record[1];
            String link = record[2];
            LocalDate fecha;
            if (record[3].equals("null"))fecha = null;
            else fecha = LocalDate.parse(record[3]);
            int num_series = Integer.parseInt(record[4]);

            String sql = "INSERT INTO estudio(id, nombre, link, fecha, num_series) VALUES (?,?,?,?,?)";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            pst.setString(2, nombre);
            pst.setString(3, link);
            if (fecha == null) pst.setDate(4, null);
            else pst.setDate(4, Date.valueOf(fecha));
            pst.setInt(5, num_series);
            pst.executeUpdate();
        }
        reader.close();
    }

    /**
     * Crea la tabla estudio.
     * @throws SQLException Si ocurre un error de SQL al
     * interactuar con la base de datos.
     */
    public void createTable() throws SQLException {

        Statement sts = connection.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS estudio ("+
                "id INTEGER PRIMARY KEY NOT NULL,"+
                "nombre VARCHAR(50) NOT NULL,"+
                "link VARCHAR(100) NOT NULL,"+
                "fecha DATE,"+
                "num_series INTEGER NOT NULL)";

        sts.executeUpdate(sql);
    }
}
