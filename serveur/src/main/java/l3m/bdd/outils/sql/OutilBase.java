package l3m.bdd.outils.sql;

import java.sql.ResultSet;

/**
 * @author Frederic PROCHNOW Cr�e le @31 mars 2020
 *
 *         Gestion de l'execution des requette sql
 */
public class OutilBase {

	public boolean executionRequetteSansResultSet(String requette, OutilConnection connection) {
		if (connection.querySansResultSet(requette)) {
			OutilsAffichage.afficherMessageConfirmation("Execution de la requette en base avec succes!");
			OutilsAffichage.afficherMessageInformation("Requette :" + requette);
			return true;
		} else {
			OutilsAffichage.afficherMessageErreur("Echec de l'execution de la requette sur la table en base!");
			OutilsAffichage.afficherMessageInformation("Requette :" + requette);
			return false;
		}
	}

	public ResultSet executionRequetteAvecResultSet(String requette, OutilConnection connection) {
		ResultSet resultSet = connection.query(requette);
		if (resultSet != null) {
			OutilsAffichage.afficherMessageConfirmation("Execution de la requette en base avec succes!");
			OutilsAffichage.afficherMessageInformation("Requette :" + requette);
		} else {
			OutilsAffichage.afficherMessageErreur("Echec de l'execution de la requette sur la table en base!");
			OutilsAffichage.afficherMessageInformation("Requette :" + requette);
		}
		return resultSet;
	}

}