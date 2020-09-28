package l3m.modelisation;

import java.sql.Date;
import java.util.Map;

public class Commande {

	 private int idCommande;
	 private String idClient;
	 private Date dateCommande;
	 private String adresseLivraison;
	 private Map<Integer,Integer> idPlats; 
	 //pour chaque plats, on associe un numero de produit a une quantite
	 private int idFilms; // il y a 1 film
	/**
	 * @return the idCommande
	 */
	public int getIdCommande() {
		return idCommande;
	}
	/**
	 * @param idCommande the idCommande to set
	 */
	public void setIdCommande(int idCommande) {
		this.idCommande = idCommande;
	}
	/**
	 * @return the idClient
	 */
	public String getIdClient() {
		return idClient;
	}
	/**
	 * @param idClient the idClient to set
	 */
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}
	/**
	 * @return the dateCommande
	 */
	public Date getDateCommande() {
		return dateCommande;
	}
	/**
	 * @param dateCommande the dateCommande to set
	 */
	public void setDateCommande(Date dateCommande) {
		this.dateCommande = dateCommande;
	}
	/**
	 * @return the adresseLivraison
	 */
	public String getAdresseLivraison() {
		return adresseLivraison;
	}
	/**
	 * @param adresseLivraison the adresseLivraison to set
	 */
	public void setAdresseLivraison(String adresseLivraison) {
		this.adresseLivraison = adresseLivraison;
	}
	/**
	 * @return the idPlats
	 */
	public Map<Integer, Integer> getIdPlats() {
		return idPlats;
	}
	/**
	 * @param listePlat the idPlats to set
	 */
	public void setIdPlats(Map<Integer, Integer> listePlat) {
		this.idPlats = listePlat;
	}
	/**
	 * @return the idFilms
	 */
	public int getIdFilms() {
		return idFilms;
	}
	/**
	 * @param idFilms the idFilms to set
	 */
	public void setIdFilms(int idFilms) {
		this.idFilms = idFilms;
	}
	 
	
}