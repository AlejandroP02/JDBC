import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

/**
 * La clase ACBMain representa la clase principal del programa ACB.
 * Esta clase contiene el método main, que inicia la ejecución del
 * programa.
 */
public class ACBMain {
	/**
	 * Método principal que inicia la ejecución del programa ACB.
	 * @param args Los argumentos de la línea de comandos
	 *               (no se utilizan en este programa).
	 * @throws SQLException Si ocurre un error de SQL al interactuar
	 * con la base de datos.
	 * @throws IOException Si ocurre un error de entrada/salida
	 * durante la ejecución del programa.
	 */
	public static void main(String[] args) throws SQLException, IOException {
		ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
		Connection c = connectionFactory.connect();
		ACBMenu menu = new ACBMenu(c);
		menu.mainMenu();
	}

}
