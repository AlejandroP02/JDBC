import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * La clase ACBMenu representa el menú principal del programa ACB.
 * Proporciona opciones para interactuar con la base de datos, como
 * mostrar, borrar, crear y poblar tablas, realizar consultas SELECT,
 * actualizar datos y borrar datos.
 */
public class ACBMenu {
	/**
	 * Sirve para poder conectar las demas clases con la base de datos.
	 */
	private Connection connection;
	/**
	 * Sirve para mantener el programa activo.
	 */
	private boolean continua =  true;
	/**
	 * Crea una instancia de DBController donde se encuentran todos los
	 * metodos no relacionados con una tabla especifica.
	 */
	private DBController c;
	/**
	 * Crea una instancia de SerieControler donde se encuentran los
	 * metodos relacionados con serie, seriegenero y serieestudio.
	 */
	private SerieController serie;
	/**
	 * Crea una instancia de EstudioControler donde se encuentran los
	 * metodos relacionados con estudio.
	 */
	private EstudioController estudio;
	/**
	 * Crea una instancia de GeneroControler donde se encuentran los
	 * metodos relacionados con genero.
	 */
	private GeneroController genero;

	/**
	 * Constructor de la clase ACBMenu, donde se le da conexión a las
	 * demas clases.
	 * @param connection La conexión a la base de datos.
	 */
	public ACBMenu(Connection connection) {
		this.connection = connection;
		c = new DBController(connection);
		serie = new SerieController(connection);
		estudio = new EstudioController(connection);
		genero = new GeneroController(connection);
	}

	/**
	 * Método que inicia el menú principal del programa ACB.
	 * @throws SQLException Si ocurre un error de SQL al
	 * interactuar con la base de datos.
	 * @throws IOException Si ocurre un error de entrada/salida
	 * durante la ejecución del programa.
	 */
	public void mainMenu() throws SQLException, IOException {;

		while (continua){

			System.out.println(" \nMENU PRINCIPAL \n");

			System.out.println("1. Mostrar tablas");
			System.out.println("2. Borrar tabla");
			System.out.println("3. Borrar tablas");
			System.out.println("4. Crear tablas");
			System.out.println("5. Poblar tablas");
			System.out.println("6. Select con texto concreto");
			System.out.println("7. Select con condicion");
			System.out.println("8. Select elemento especifico");
			System.out.println("9. Update");
			System.out.println("10. Delete");
			System.out.println("11. Sortir");
			System.out.println("Esculli opció:");

			opcion(c.nextInt());

		}
	}

	/**
	 * Método que maneja las opciones del menú.
	 * @param o La opción seleccionada por el usuario.
	 * @throws SQLException Si ocurre un error de SQL al
	 * interactuar con la base de datos.
	 * @throws IOException Si ocurre un error de entrada/salida
	 * durante la ejecución del programa.
	 */
	private void opcion(int o) throws SQLException, IOException {
		c.nextLine();
		if(o==1){
			c.mostrarTablas();
			System.out.println();
			System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
			c.nextLine();
		}else if(o==2){
			c.mostrarTablas();
			System.out.println("Escribe el nombre de la tabla que quieres eliminar o "+ConsoleColors.GREEN+"cancel"+ConsoleColors.RESET+" para cancelar");
			c.eliminarTabla(c.next());
			System.out.println();
			System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
			c.nextLine();
		}else if(o==3){
			c.eliminarTablas();
			System.out.println("Tablas eliminadas");
			System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
			c.nextLine();
		}else if(o==4){
			serie.createTable();
			genero.createTable();
			estudio.createTable();
			serie.createSubTable1();
			serie.createSubTable2();
			System.out.println("Tablas creadas");
			System.out.println("Pulsa enter para continuar");
			c.nextLine();
		}else if(o==5){
			serie.autoSerie();
			genero.autoGenero();
			estudio.autoEstudio();
			serie.autoSerieGenero();
			serie.autoSerieEstudio();
			System.out.println("Tablas plobadas");
			System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
			c.nextLine();
		}else if(o==6){
			c.selectTextoConcreto();
			System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
			c.nextLine();
		}else if(o==7){
			c.selectCondicion();
			System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
			c.nextLine();
		}else if(o==8){
			c.selectElemento();
			System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
			c.nextLine();
			c.nextLine();
		}else if(o==9){
			c.update();
			System.out.println("Datos actualizados");
			System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
			c.nextLine();
		}else if(o==10){
			c.delete();
			System.out.println("Datos borrados");
			System.out.println("Pulsa "+ConsoleColors.GREEN+"enter"+ConsoleColors.RESET+" para continuar");
			c.nextLine();
		}else if(o==11){
			continua = false;
		}else{
			System.out.println("Opcion no valida");
		}
	}

}
