package l3m.servlet.commande;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import l3m.bdd.ClientDAO;
import l3m.bdd.CommandeDAO;
import l3m.modelisation.Client;
import l3m.modelisation.Commande;

/**
 * @author Frederic PROCHNOW Crée le @28 avr. 2020
 *
 *******************************************
 *         1-API POUR VALIDER UNE COMMANDE CLIENT
 *         ******************************************
 *
 *         /api/commande/valider
 *
 *         parametre = { idMovie : A, idMenu : B-C;B-C livr:D }
 *
 *         A = id du film, B = id d'un menu, C = quantite du menu, D = adresse
 *         de livraison 2 CAS : si D = vide alors adresse Client Sinon c'est D
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
 */
public class Commande_Valider extends HttpServlet {

	private static final long serialVersionUID = 1L;


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

		// DAO et focntion commune
		ClientDAO daoClient = new ClientDAO();
		CommandeDAO dao = new CommandeDAO(daoClient.getConnect());
		Fonction_commune commun = new Fonction_commune();

		// parametre
		request.setCharacterEncoding("UTF-8");
		String idClient = request.getParameter("idClient");
		String idMovie = request.getParameter("idMovie");
		String idMenu = request.getParameter("idMenu");
		String livr = request.getParameter("livr");

		// test
		Integer idFilm = 0;
		Map<Integer, Integer> mapPlat = new HashMap<>();
		Boolean continuer = true;
		try {
			idFilm = Integer.parseInt(idMovie);

			String[] listePlat = idMenu.split(" ");
			for (String s : listePlat) {
				String[] plat = s.split("-");
				mapPlat.put(Integer.parseInt(plat[0]), Integer.parseInt(plat[1]));
			}
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			continuer = false;
		}

		if (!continuer && (idClient == null || idClient.equals("null")) && (idMovie == null || idMovie.equals("null"))
				&& (idMenu == null || idMenu.equals("null"))) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			Client client = daoClient.read(idClient);
			Boolean existe = client != null && client.getNom() != null;
			if (!existe) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				Commande c = new Commande();
				if (livr != null && !idClient.equals("livr")) {
					c.setAdresseLivraison(livr);
				} else {
					c.setAdresseLivraison("");
				}
				c.setIdClient(idClient);
				c.setIdFilms(idFilm);
				c.setIdPlats(mapPlat);
				int idCommande = dao.create(c);
				if (idCommande != -1) {
					// on est ok, on regarde les infos
					response.setContentType("text/xml");
					response.addHeader("Access-Control-Allow-Origin", "*");
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
							+ "<p:commandes xmlns:p=\"http://integrateur/menu\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://integrateur/menu Commade.xsd \">");
					commun.ajoutInfoCommande(response, c);
					response.getWriter().print("</p:commandes>");
				} else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
			// fermeture connection bdd
			dao.fermerConnectionBDD();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
