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
 *         5- AUTHENTIFICATION AUPRES DU SERVEUR METIER
 *******************************************
 * 
 *         /api/client/authentification
 * 
 *         param = { login: A, nom : B }
 * 
 *         RECOIT DU TEXTE: 'true' L'utilisateur est logue 'inscrit'
 *         L'utilisateur est logue et inscrit 'false' Erreur
 */
public class Client_Authentification extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
						response.addHeader("Access-Control-Allow-Origin", "*");

		// DAO et focntion commune
		ClientDAO dao = new ClientDAO();

		// parametre
		request.setCharacterEncoding("UTF-8");
		String uid = request.getParameter("login");
		String name = request.getParameter("nom");

		// filtre
		Boolean uidNul = uid == null || uid.equals("null");
		Boolean nameNul = name == null || name.equals("null");

		if (uidNul && nameNul) {
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_OK);

			Client c = dao.read(uid);
			if (c != null && c.getNom() != null) {
				response.getWriter().print("true");
			} else {
				c = new Client();
				c.setIdClient(uid);
				c.setNom(name);
				c.setPointFidelites(0);
				System.out.println("ok2");
				if (dao.create(c)) {
					response.getWriter().print("inscrit");
				} else {
					response.getWriter().print("false");
				}
			}
		}
		dao.fermerConnectionBDD();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
