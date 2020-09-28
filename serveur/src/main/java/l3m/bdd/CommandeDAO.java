package l3m.bdd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import l3m.bdd.outils.sql.OutilBase;
import l3m.bdd.outils.sql.OutilConnection;
import l3m.modelisation.Commande;

public class CommandeDAO extends SqlDAO<Commande> {

	OutilConnection connection = super.getConnect().getInstance();

	public CommandeDAO() {
		super();
	}

	public CommandeDAO(OutilConnection connect) {
		super(connect);
	}

	public boolean create(Object Commande) {
		Commande c = (Commande) Commande;
		if (create(c) != -1) {
			return true;
		} else {
			return false;
		}
	}

	public int create(Commande c) {
		// OUTILS BDD
		OutilBase outil = new OutilBase();

		// REQUETTE
		int res = -1;
		int i = 0;
		Map<Integer, Integer> listePlat = c.getIdPlats();
		List<String> listeRequetteAExceuter = new ArrayList<String>();

		// RECUPERATION ID COMMANDE
		String requette = "select max(idCommande)+1 from Cine_COMMANDE";

		try {
			ResultSet result = outil.executionRequetteAvecResultSet(requette, connection);
			if (result.next()) {
				int j = result.getInt(1);
				c.setIdCommande(j);
				res = j;
			}
		} catch (SQLException e) {
			System.out.println("ERROR: Erreur lors de la recuperation de l id commande !");
			e.printStackTrace();
		}
		if (c.getAdresseLivraison() == null || c.getAdresseLivraison() == "") {
			c.setAdresseLivraison("-");
		}
		String requette2 = "insert into Cine_COMMANDE values (" + c.getIdCommande() + ",'" + c.getIdClient()
				+ "',SYSDATE,'" + c.getAdresseLivraison() + "')";
		if (!outil.executionRequetteSansResultSet(requette2, connection)) {
			connection.rollback();
			res = -1;
		} else {
			connection.commit();
		}

		listeRequetteAExceuter
				.add("insert into Cine_PANIER values (" + c.getIdCommande() + "," + c.getIdFilms() + ",'Film',1)");
		for (Entry<Integer, Integer> map : listePlat.entrySet()) {
			listeRequetteAExceuter.add("insert into Cine_PANIER values (" + c.getIdCommande() + "," + map.getKey()
					+ ",'Plat'," + map.getValue() + ")");
		}

		// BASE DE DONNEES
		while (i < listeRequetteAExceuter.size() && res != -1) {
			if (!outil.executionRequetteSansResultSet(listeRequetteAExceuter.get(i), connection)) {
				res = -1;
			}
			i++;
		}
		if (res != -1) {
			connection.commit();
		} else {
			connection.rollback();
		}
		return res;
	}

	public Commande read(int id) {
		// OUTILS BDD
		OutilBase outil = new OutilBase();

		// REQUETTE
		String requette = "select * from Cine_COMMANDE where idCommande =" + id;
		String requette2 = "select * from Cine_Panier where idCommande =" + id;
		Commande c = new Commande();

		try {
			ResultSet result = outil.executionRequetteAvecResultSet(requette, connection);
			if (result.next()) {
				// RECUPERATION INFORMATIONS
				c.setIdCommande(id);
				c.setIdClient(result.getString(2));
				c.setDateCommande(result.getDate(3));
				c.setAdresseLivraison(result.getString(4));

				Map<Integer, Integer> listePlat = new HashMap<Integer, Integer>();

				result = outil.executionRequetteAvecResultSet(requette2, connection);
				while (result.next()) {
					String typeProd = result.getString("typeProduit");
					if (typeProd.equals("Plat")) {
						listePlat.put(result.getInt("idProduit"), result.getInt("quantite"));
					} else {
						c.setIdFilms(result.getInt("idProduit"));
					}
				}
				c.setIdPlats(listePlat);
			}
		} catch (SQLException e) {
			System.out.println("ERROR: Erreur lors de la recuperation des infos du Client !");
			e.printStackTrace();
			return null;
		}
		return c;
	}

	public boolean update(Object Commande) {
		// OUTILS BDD
		OutilBase outil = new OutilBase();
		Commande c = (Commande) Commande;

		// REQUETTE
		Boolean res = true;
		
		if(!c.getAdresseLivraison().equals("-")) {
			String requette = "update Cine_COMMANDE set adresseLivraison ='" + c.getAdresseLivraison() + "' where idCommande ="
				+ c.getIdCommande();
			if (outil.executionRequetteSansResultSet(requette, connection)) {
				res = true;
			}else {
				res = false;
			}
		}
		if(res && c.getIdFilms() != -1) {
			String requette = "update Cine_Panier set idProduit="+c.getIdFilms()+ " where idCommande ="+c.getIdCommande()+" and typeProduit='Film'";
			if (outil.executionRequetteSansResultSet(requette, connection)) {
				res = true;
			}else {
				res = false;
			}
		}
		if(res && c.getIdPlats() != null) {
			String requette = "delete from Cine_Panier where idCommande =" + c.getIdCommande() +" and typeProduit='Plat'";
			if (outil.executionRequetteSansResultSet(requette, connection)) {
				for (Entry<Integer, Integer> map : c.getIdPlats().entrySet()) {
					String requette2 = "insert into Cine_PANIER values (" + c.getIdCommande() + "," + map.getKey()
							+ ",'Plat'," + map.getValue() + ")";
					if (!res || !outil.executionRequetteSansResultSet(requette2, connection)) {
						res = false;
					}
				}
			}else {
				res = false;
			}
		}
		if(res) {
			connection.commit();
		}else {
			connection.rollback();
		}
		return res;
	}

	public boolean delete(Object Commande) {
		// OUTILS BDD
		OutilBase outil = new OutilBase();
		Commande c = (Commande) Commande;

		// REQUETTE
		String requette = "delete from Cine_Panier where idCommande =" + c.getIdCommande();
		String requette2 = "delete from Cine_COMMANDE where idCommande =" + c.getIdCommande();
		Boolean res = false;

		// BASE DE DONNEES
		if (outil.executionRequetteSansResultSet(requette, connection)
				&& outil.executionRequetteSansResultSet(requette2, connection)) {
			connection.commit();
			res = true;
		} else {
			connection.rollback();
			res = false;
		}
		return res;
	}

}
