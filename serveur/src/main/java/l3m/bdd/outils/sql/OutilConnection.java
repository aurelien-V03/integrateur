package l3m.bdd.outils.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Frederic PROCHNOW Cr�e le @31 mars 2020
 *
 *         Gestion des connection SQL
 */
public class OutilConnection {
	private static final OutilConnection INSTANCE = new OutilConnection();
	
	private Connection connection = null;
	private Statement statement = null;

	private Boolean etatConnect = false;

	public OutilConnection() {
		connect();
	}
	
	public OutilConnection getInstance() {
		return INSTANCE;
	}

	public void connect() {
		if (!etatConnect) {
			try {
				DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
				connection = DriverManager.getConnection(InfoBdd.URL_DATABASE.getValeur(),
						InfoBdd.USER_LOGIN.getValeur(), InfoBdd.USER_MDP.getValeur());
				statement = connection.createStatement();
				connection.setAutoCommit(false);
				etatConnect = true;
				OutilsAffichage.afficherMessageConfirmation("Connexion a la database avec succes !");
			} catch (SQLException sqlException) {
				sqlException.printStackTrace();
				OutilsAffichage.afficherMessageErreur("Echec de connection a la base !");
			}
		} else {
			OutilsAffichage.afficherMessageInformation("Connection d�j� fait !");
		}
	}

	public void close() {
		try {
			statement.close();
			connection.close();
			OutilsAffichage.afficherMessageConfirmation("CLOSE effectue !");
		} catch (SQLException e) {
			e.printStackTrace();
			OutilsAffichage.afficherMessageErreur("Echec dans la fermeture de la connection a la base !");
		}
	}

	public void commit() {
		try {
			connection.commit();
			OutilsAffichage.afficherMessageConfirmation("COMMIT effectue !");
		} catch (SQLException e) {
			e.printStackTrace();
			OutilsAffichage.afficherMessageErreur("Echec dans le commit !");
			close();
		}
	}

	public void rollback() {
		try {
			connection.rollback();
			OutilsAffichage.afficherMessageConfirmation("ROLLBACK effectue !");
		} catch (SQLException e) {
			e.printStackTrace();
			OutilsAffichage.afficherMessageErreur("Echec dans le roolback !");
			close();
		}
	}

	public ResultSet query(String requette) {
		ResultSet resultat = null;
		try {
			resultat = statement.executeQuery(requette);
		} catch (SQLException e) {
			e.printStackTrace();
			OutilsAffichage.afficherMessageErreur("Echec de l'execution de la requette : " + requette);
		}
		return resultat;
	}

	public boolean querySansResultSet(String requette) {
		try {
			statement.execute(requette);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			OutilsAffichage.afficherMessageErreur("Echec de l'execution de la requette : " + requette);
			return false;
		}
	}
}
