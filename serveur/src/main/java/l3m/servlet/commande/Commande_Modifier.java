package l3m.servlet.commande;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import l3m.bdd.CommandeDAO;
import l3m.modelisation.Commande;

/**
 * @author Frederic PROCHNOW Crée le @2 mai 2020
 *
 ********************************************
 *         3-API POUR MODIFIER UNE COMMANDE CLIENT
 *******************************************
 * 
 *         /api/commande/modifier
 * 
 *         parametre = { idCommande : E idMovie : A, (PEUT ETRE FACULTATIF)
 *         idMenu : B/C (PEUT ETRE FACULTATIF) livr:D (PEUT ETRE FACULTATIF) }
 * 
 *         A = id du film, B = id d'un menu, C = quantite du menu, D = adresse
 *         de livraison 2 CAS : si D = vide alors adresse Client Sinon c'est D
 * 
 *         E = id de la commande
 * 
 * 
 *         Pour les champs A,B,C et D, seul les valeurs a changer sont presentes
 *         dans les parametres
 * 
 *         RECOIT UNE REPONSE EN XML AVEC LES NOUVELLES INFOS (voir xsd de
 *         /api/rechercherCommande) RECOIT DU XML :
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
 */
public class Commande_Modifier extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// DAO et focntion commune
		CommandeDAO dao = new CommandeDAO();
		Fonction_commune commun = new Fonction_commune();

		// parametre
		request.setCharacterEncoding("UTF-8");
		String idCom = request.getParameter("idCommande");
		String idMov = request.getParameter("idMovie");
		String idMenu = request.getParameter("idMenu");
		String livr = request.getParameter("livr");

		// filtre
		Boolean idMovieNul = (idMov == null || idMov.equals("null"));
		Boolean idMenuNul = (idMenu == null || idMenu.equals("null"));
		Boolean livrNul = (livr == null || livr.equals("null"));

		// test parametre
		int idCommande = 0;
		int idMovie = -1;
		Map<Integer, Integer> mapPlat = new HashMap<>();
		Boolean continuer = true;
		try {
			idCommande = Integer.parseInt(idCom);
			if (!idMovieNul) {
				idMovie = Integer.parseInt(idMov);
			}

			if (!idMenuNul) {
				String[] listePlat = idMenu.split(" ");
				for (String s : listePlat) {
					String[] plat = s.split("-");
					mapPlat.put(Integer.parseInt(plat[0]), Integer.parseInt(plat[1]));
				}
			}
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			continuer = false;
		}

		if (!continuer || (idMovieNul && idMenuNul && livrNul)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			Commande commande = dao.read(idCommande);
			Boolean existe = commande != null && commande.getIdClient() != null;
			if (!existe) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				if (!idMovieNul && idMovie != commande.getIdFilms()) {
					commande.setIdFilms(idMovie);
				} else {
					commande.setIdFilms(-1);
				}
				if (!idMenuNul && !mapPlat.equals(commande.getIdPlats())) {
					commande.setIdPlats(mapPlat);
				} else {
					commande.setIdPlats(null);
				}
				if (!livrNul && !livr.equals(commande.getAdresseLivraison())) {
					commande.setAdresseLivraison(livr);
				} else {
					commande.setAdresseLivraison("-");
				}
				if (dao.update(commande)) {
					response.setContentType("text/xml");
					response.addHeader("Access-Control-Allow-Origin", "*");
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
							+ "<p:commandes xmlns:p=\"http://integrateur/menu\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://integrateur/menu Commade.xsd \">");
					commande = dao.read(idCommande);
					commun.ajoutInfoCommande(response, commande);
					dao.getConnect().close();
					response.getWriter().print("</p:commandes>");
				} else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
		}
		// fermeture connection bdd
		dao.fermerConnectionBDD();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}