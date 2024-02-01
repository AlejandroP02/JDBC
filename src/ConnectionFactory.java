import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * La clase ConnectionFactory proporciona métodos para establecer
 * y cerrar conexiones a la base de datos PostgreSQL.
 * Utiliza un patrón de diseño Singleton para garantizar una única
 * instancia de la fábrica de conexiones.
 */
public class ConnectionFactory {
	// init database constants
	private static final String DATABASE_DRIVER = "org.postgresql.Driver";

	private static final String MAX_POOL = "250";
	private static ConnectionFactory instance;

	/**
	 * Nombre de la base de datos.
	 */
	private String dbname;
	/**
	 * Host al que se conecta el programa.
	 */
	private String host;
	/**
	 * Puerto al que se conecta el programa.
	 */
	private String port;
	/**
	 * Usuario con el que se conecta el programa.
	 */
	private String user;
	/**
	 * Contraeña con la que se conecta el programa.
	 */
	private String password;
	/**
	 * Esquema en el que se encuentra la base de datos
	 */
	private String schema;

	/**
	 * Inicializa el objeto de conexión
	 */
	private Connection connection;

	/**
	 * Inicializa el objeto de propiedades
	 */
	private Properties properties;

	/**
	 * Constructor privado para seguir el patrón de diseño Singleton.
	 */
	private ConnectionFactory() {
		super();
		init();
	}

	/**
	 * Método estático que devuelve la única instancia de ConnectionFactory.
	 * @return La única instancia de ConnectionFactory.
	 */
	public static ConnectionFactory getInstance() {
		if (instance == null) {
			instance = new ConnectionFactory();
		}
		return instance;
	}

	/**
	 * Inicializa la clase cargando el archivo de propiedades
	 * de la base de datos y asignando valores a las variables
	 * de instancia.
	 * @throws RuntimeException Si no se puede encontrar el
	 * archivo de propiedades.
	 */
	public void init() {
		Properties prop = new Properties();
		InputStream propStream = this.getClass().getClassLoader().getResourceAsStream("db.properties");

		try {
			prop.load(propStream);
			this.host = prop.getProperty("host");
			this.port = prop.getProperty("port");
			this.user = prop.getProperty("user");
			this.password = prop.getProperty("password");
			this.dbname = prop.getProperty("dbname");
			this.schema = prop.getProperty("schema");
		} catch (IOException e) {
			String message = "ERROR: db.properties file could not be found";
			System.err.println(message);
			throw new RuntimeException(message, e);
		}
	}

	/**
	 * Devuelve las propiedades de la conexión a la base de datos.
	 * @return Las propiedades de la conexión.
	 */
	private Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
			properties.setProperty("user", this.user);
			properties.setProperty("password", this.password);
			properties.setProperty("MaxPooledStatements", MAX_POOL);
		}
		return properties;
	}

	/**
	 * Establece la conexión a la base de datos PostgreSQL.
	 * @return La conexión establecida.
	 */
	public Connection connect() {
		if (connection == null) {

			try {
				String url = null;

				Class.forName(DATABASE_DRIVER);
				
				// Preprara connexió a la base de dades
				StringBuffer sbUrl = new StringBuffer();
				sbUrl.append("jdbc:postgresql:");
				if (host != null && !host.equals("")) {
					sbUrl.append("//").append(host);
					if (port != null && !port.equals("")) {
						sbUrl.append(":").append(port);
					}
				}
				sbUrl.append("/").append(dbname);
				url = sbUrl.toString();
				
				System.out.println(url);
				System.out.println(getProperties());
						
				connection = DriverManager.getConnection(url, getProperties());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return connection;
	}

	/**
	 * Cierra la conexión a la base de datos.
	 */
	public void disconnect() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
//	public void init() {
//		Properties prop = new Properties();
//		InputStream propStream = this.getClass().getClassLoader().getResourceAsStream("db.properties");
//
//		try {
//			prop.load(propStream);
//			this.host = prop.getProperty("host");
//			this.port = prop.getProperty("port");
//			this.dbname = prop.getProperty("dbname");
//			this.schema = prop.getProperty("schema");
//		} catch (IOException e) {
//			String message = "ERROR: db.properties file could not be found";
//			System.err.println(message);
//			throw new RuntimeException(message, e);
//		}
//	}
	
}
