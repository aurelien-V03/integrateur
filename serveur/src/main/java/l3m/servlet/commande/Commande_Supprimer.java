package l3m.servlet.commande;

import java.io.IOException;

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
 *         4-API POUR SUPPRIMER UNE COMMANDE CLIENT
 *******************************************
 * 
 *         /api/commande/supprimer
 * 
 *         parametre = { idCommande : A }
 * 
 *         RECOIT DU TEXTE: 'true' La suppression a bien ete faite 'false'
 *         Erreur
 */
public class Commande_Supprimer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// DAO et focntion commune
		CommandeDAO dao = new CommandeDAO();

		// parametre
		request.setCharacterEncoding("UTF-8");
		String idCom = request.getParameter("idCommande");

		// test parametre
		int idCommande = 0;
		Boolean continuer = true;
		try {
			idCommande = Integer.parseInt(idCom);
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			continuer = false;
		}

		if (continuer) {
			Commande c = new Commande();
			c.setIdCommande(idCommande);
			if (dao.delete(c)) {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print("true");
			} else {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print("false");
			}
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		// fermeture connection bdd
		dao.fermerConnectionBDD();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
