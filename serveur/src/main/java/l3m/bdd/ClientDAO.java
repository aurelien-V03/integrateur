package l3m.bdd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import l3m.bdd.outils.sql.OutilBase;
import l3m.bdd.outils.sql.OutilConnection;
import l3m.modelisation.Client;

public class ClientDAO extends SqlDAO<Client> {

	OutilConnection connection = super.getConnect().getInstance();

	public ClientDAO() {
		super();
	}

	public ClientDAO(OutilConnection connect) {
		super(connect);
	}

	public boolean create(Object Client) {
		// OUTILS BDD
		OutilBase outil = new OutilBase();
		Client c = (Client) Client;

		// REQUETTE
		String requette = "insert into Cine_Client (idClient,nom,pointFidelites) values ('" + c.getIdClient() + "','"
				+ c.getNom() + "'," + c.getPointFidelites() + ")";
		Boolean res = false;

		// BASE DE DONNEES
		if (outil.executionRequetteSansResultSet(requette, connection)) {
			connection.commit();
			res = true;
		} else {
			connection.rollback();
			res = false;
		}
		return res;
	}

	public Client read(String id) {
		// OUTILS BDD
		OutilBase outil = new OutilBase();

		// REQUETTE
		String requette = "select * from Cine_Client where idClient ='" + id + "'";
		Client c = new Client();
		String nom;
		String photo;
		String email;
		String tel;
		String adresse;

		try {
			ResultSet result = outil.executionRequetteAvecResultSet(requette, connection);
			if (result.next()) {
				// RECUPERATION INFORMATIONS
				c.setIdClient(id);
				nom = result.getString(2);
				if(nom == null) {
					c.setNom("--");
				}else {
					c.setNom(nom);
				}
				photo= result.getString(3);
				if(photo == null) {
					c.setPhoto("--");
				}else {
					c.setPhoto(photo);
				}
				email = result.getString(4);
				if(email == null) {
					c.setEmail("--");
				}else {
					c.setEmail(email);
				}
				tel = result.getString(5);
				if(tel == null) {
					c.setTel("--");
				}else {
					c.setTel(tel);
				}
				adresse = result.getString(6);
				if(adresse == null) {
					c.setAdresse("--");
				}else {
					c.setAdresse(adresse);
				}
				c.setPointFidelites(result.getInt(7));
			}
		} catch (SQLException e) {
			System.out.println("ERROR: Erreur lors de la recuperation des infos du Client !");
			e.printStackTrace();
			return null;
		}
		return c;
	}

	public Client readNom(String nom) {
		// OUTILS BDD
		OutilBase outil = new OutilBase();

		// REQUETTE
		String requette = "select * from Cine_Client where nom='" + nom + "'";
		Client c = new Client();

		try {
			ResultSet result = outil.executionRequetteAvecResultSet(requette, connection);
			if (result.next()) {
				// RECUPERATION INFORMATIONS
				c.setIdClient(result.getString(1));
				c.setNom(result.getString(2));
				c.setPhoto(result.getString(3));
				c.setEmail(result.getString(4));
				c.setTel(result.getString(5));
				c.setAdresse(result.getString(6));
				c.setPointFidelites(result.getInt(7));
			}
		} catch (SQLException e) {
			System.out.println("ERROR: Erreur lors de la recuperation des infos du Client !");
			e.printStackTrace();
			return null;
		}
		return c;
	}

	public Object read(int id) {
		return null;
	}

	public boolean update(Object Client) {
		// OUTILS BDD
		OutilBase outil = new OutilBase();
		Client c = (Client) Client;

		// REQUETTE
		Boolean virgule = false;
		String requette = "update Cine_Client set ";
		
		if (!c.getNom().equals("-")) {
			virgule = true;
			requette += " nom = '" + c.getNom()+"'";
		}
		if (!c.getPhoto().equals("-")) {
			if(virgule) {
				virgule = false;
				requette += ",";
			}
			virgule = true;
			requette += " photo ='" + c.getPhoto()+"'";
		}
		if (!c.getEmail().equals("-")) {
			if(virgule) {
				virgule = false;
				requette += ",";
			}
			virgule = true;
			requette += " email ='" + c.getEmail()+"'";
		}
		if (!c.getTel().equals("-")) {
			if(virgule) {
				virgule = false;
				requette += ",";
			}
			virgule = true;
			requette += " tel ='" + c.getTel()+"'";
		}
		if (!c.getAdresse().equals("-")) {
			if(virgule) {
				virgule = false;
				requette += ",";
			}
			requette += " adresse ='" + c.getAdresse()+"'";
		}
		requette += " where idClient ='" + c.getIdClient()+"'";
		Boolean res = false;

		// BASE DE DONNEES
		if (outil.executionRequetteSansResultSet(requette, connection)) {
			connection.commit();
			res = true;
		} else {
			connection.rollback();
			res = false;
		}
		return res;
	}

	public boolean delete(Object Client) {
		// OUTILS BDD
		OutilBase outil = new OutilBase();
		Client c = (Client) Client;

		// REQUETTE
		String requette = "delete from Cine_Client where idClient =" + c.getIdClient();
		Boolean res = false;

		// BASE DE DONNEES
		if (outil.executionRequetteSansResultSet(requette, connection)) {
			connection.commit();
			res = true;
		} else {
			connection.rollback();
			res = false;
		}
		return res;
	}

	public List<Integer> listeCommandeClient(String idClient) {
		// OUTILS BDD
		OutilBase outil = new OutilBase();

		// REQUETTE
		String requette = "select * from Cine_COMMANDE where idClient ='" + idClient + "'";
		List<Integer> liste = new ArrayList<Integer>();

		try {
			ResultSet result = outil.executionRequetteAvecResultSet(requette, connection);
			while (result.next()) {
				// RECUPERATION INFORMATIONS
				liste.add(result.getInt("idCommande"));
			}
		} catch (SQLException e) {
			System.out.println("ERROR: Erreur lors de la recuperation des infos du Client !");
			e.printStackTrace();
			return null;
		}
		return liste;
	}
}
