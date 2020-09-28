package l3m.servlet.client;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import l3m.bdd.ClientDAO;
import l3m.modelisation.Client;

/**
 * @author Frederic PROCHNOW Crée le @2 mai 2020
 *
 ******************************************** 
 *         7- API pour modifer les infos client
 *******************************************
 * 
 *         /api/client/modifierInfos
 * 
 *         param = { idClient:A, nom:B, (PEUT ETRE FACULTATIF) photo:C (PEUT
 *         ETRE FACULTATIF) email:D (PEUT ETRE FACULTATIF) tel:E (PEUT ETRE
 *         FACULTATIF) adresse:F (PEUT ETRE FACULTATIF) }
 * 
 *         Pour les champs de B a  F, , seul les valeurs a changer sont
 *         presentes dans les parametres
 * 
 *         RECOIT DU TEXTE: 'true' Les modifications sont bien faite 'false'
 *         Erreur
 */
public class Client_Modifier extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// DAO et focntion commune
		ClientDAO daoClient = new ClientDAO();

		// parametre
		request.setCharacterEncoding("UTF-8");
		String idClient = request.getParameter("idClient");
		String nom = request.getParameter("nom");
		String photo = request.getParameter("photo");
		String email = request.getParameter("email");
		String tel = request.getParameter("tel");
		String adresse = request.getParameter("adresse");

		// filtre
		Boolean idClientNul = idClient == null || idClient.equals("null");
		Boolean nomNul = nom == null || nom.equals("null");
		Boolean photoNul = photo == null || photo.equals("null");
		Boolean emailNul = email == null || email.equals("null");
		Boolean telNul = tel == null || tel.equals("null");
		Boolean adresseNul = adresse == null || adresse.equals("null");
		
		if(idClientNul || (nomNul && photoNul && emailNul && telNul && adresseNul)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}else {
			Client c = daoClient.read(idClient);
			Boolean existe = c != null && c.getNom() != null;
			if (!existe) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				response.addHeader("Access-Control-Allow-Origin", "*");
				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_OK);
				
				if (!nomNul && nom != c.getNom()) {
					c.setNom(nom);
				}else {
					c.setNom("-");
				}
				if (!photoNul && photo != c.getPhoto()) {
					c.setPhoto(photo);
				}else {
					c.setPhoto("-");
				}
				if (!emailNul && email != c.getEmail()) {
					c.setEmail(email);
				}else {
					c.setEmail("-");
				}
				if (!telNul && tel != c.getTel()) {
					c.setTel(tel);
				}else {
					c.setTel("-");
				}
				if (!adresseNul && tel != c.getAdresse()) {
					c.setAdresse(adresse);
				}else {
					c.setAdresse("-");
				}
				
				if(daoClient.update(c)) {
					response.getWriter().print("true");
				}else {
					response.getWriter().print("false");
				}
			}
		}
		// fermeture connection bdd
		daoClient.fermerConnectionBDD();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}