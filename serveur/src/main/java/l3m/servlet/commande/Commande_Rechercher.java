package l3m.servlet.commande;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import l3m.bdd.ClientDAO;
import l3m.bdd.CommandeDAO;
import l3m.bdd.DomPlatDAO;
import l3m.modelisation.Client;
import l3m.modelisation.Commande;
import l3m.modelisation.Plat;

/**
 * @author Frederic PROCHNOW Crée le @30 avr. 2020
 *
 *******************************************
 *         2- API POUR RECHERCHER UNE COMMANDE
 *******************************************
 * 
 *         /api/commande/rechercher
 * 
 *         parametre = { idClient:A, dateCommande:B, (PEUT ETRE FACULTATIF)
 *         ingredient:C (PEUT ETRE FACULTATIF) }
 * 
 *         -> Si B et C, les deux filtres ne sont pas renseigner, l'ensemble des
 *         commandes concernant le client A est renvoyé dans ce cas.
 * 
 *         RECOIT DU XML :
 * 
 *         <?xml version="1.0"?>
 *         <xsd:schema elementFormDefault="qualified" targetNamespace=
 *         "http://integrateur/menu" xmlns ="http://integrateur/menu" xmlns:xsd=
 *         "http://www.w3.org/2001/XMLSchema">
 * 
 *         <xsd:element name="commandes" type="Commandes"/>
 * 
 *         <xsd:complexType name="Commandes"> <xsd:sequence>
 *         <xsd:element name="commande" minOccurs="0" maxOccurs="unbounded" type
 *         ="Commande"/> </xsd:sequence> </xsd:complexType>
 * 
 *         <xsd:complexType name="Commande"> <xsd:sequence>
 *         <xsd:element name="id" type="xsd:string"/>
 *         <xsd:element name="idClient" type="xsd:string"/>
 *         <xsd:element name="idPlats" type="xsd:string" minOccurs="0" maxOccurs
 *         ="unbounded"/>
 *         <xsd:element name="idFilms" type="xsd:string" minOccurs="1" maxOccurs
 *         ="1"/> <xsd:element name="date" type="xsd:string"/>
 *         <xsd:element name="adresseLivraison" type="xsd:string"/>
 *         <xsd:element name="prix" type="xsd:double"/> </xsd:sequence>
 *         </xsd:complexType> </xsd:schema>
 *
 *
 * 
 * 
 */
public class Commande_Rechercher extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// DAO et focntion commune
		Fonction_commune commun = new Fonction_commune();
		ClientDAO daoClient = new ClientDAO();
		CommandeDAO daoCommande = new CommandeDAO(daoClient.getConnect());
		DomPlatDAO daoPlat = new DomPlatDAO();
		daoPlat.setNomDocument("src/main/java/l3m/bdd/xml/carte.xml");

		// parametre
		request.setCharacterEncoding("UTF-8");
		String idClient = request.getParameter("idClient");
		String dateCommande = request.getParameter("dateCommande");
		String ingredient = request.getParameter("ingredient");

		// filtre
		Boolean idClientNul = idClient == null || idClient.equals("null");
		Boolean filtreDate = dateCommande != null && !dateCommande.equals("null");
		Boolean filtreIngredient = ingredient != null && !ingredient.equals("null");

		if (idClientNul) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			Client client = daoClient.read(idClient);
			Boolean existe = client != null && client.getNom() != null;
			if (!existe) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				response.setContentType("text/xml");
				response.addHeader("Access-Control-Allow-Origin", "*");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
						+ "<p:commandes xmlns:p=\"http://integrateur/menu\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://integrateur/menu Commade.xsd \">");

				List<Integer> listeNumeroCommande = daoClient.listeCommandeClient(idClient);
				List<Commande> listeCommande = new ArrayList<>();

				for (int i : listeNumeroCommande) {
					Commande c = daoCommande.read(i);

					if ((!filtreDate && !filtreIngredient)
							|| (filtreDate && dateCommande.equals(commun.formatageFormatDate(c.getDateCommande())))
							|| (filtreIngredient && !filtreDate)) {
						listeCommande.add(c);
					}
				}

				for (Commande c : listeCommande) {
					if (!filtreIngredient) {
						commun.ajoutInfoCommande(response, c);
					} else {
						Boolean ingredientTrouve = false;
						for (Entry<Integer, Integer> map : c.getIdPlats().entrySet()) {
							Plat p = daoPlat.read(map.getKey());
							if (p != null && p.getIngredients() != null) {
								for (String s : p.getIngredients()) {
									if (s.equals(ingredient)) {
										ingredientTrouve = true;
									}
								}
							}
						}
						if (ingredientTrouve) {
							commun.ajoutInfoCommande(response, c);
						}
					}
				}
			}
			response.getWriter().print("</p:commandes>");
		}
		// fermeture connection bdd
		daoCommande.fermerConnectionBDD();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
