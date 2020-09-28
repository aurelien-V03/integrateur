package l3m.servlet.commande;

import java.io.IOException;
import java.sql.Date;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import l3m.bdd.DomPlatDAO;
import l3m.modelisation.Commande;
import l3m.modelisation.Plat;

public class Fonction_commune {

	public void ajoutInfoCommande(HttpServletResponse response, Commande commande) throws IOException {
		DomPlatDAO dao = new DomPlatDAO();
		dao.setNomDocument("src/main/java/l3m/bdd/xml/carte.xml");

		double prix = 0.0;

		response.getWriter().print("<p:commande>\r\n" + "    <p:id>" + commande.getIdCommande() + "</p:id>\r\n"
				+ "    <p:idClient>" + commande.getIdClient() + "</p:idClient>\r\n");
		if (!commande.getIdPlats().isEmpty()) {
			for (Entry<Integer, Integer> map : commande.getIdPlats().entrySet()) {
				response.getWriter()
						.print("    <p:idPlats>" + map.getKey() + "-" + map.getValue() + "</p:idPlats>\r\n");
				Plat p = dao.read(map.getKey());
				if (p != null) {
					prix += p.getPrix();
				}
			}
		}
		response.getWriter()
				.print("    <p:idFilms>" + commande.getIdFilms() + "</p:idFilms>\r\n" + "<p:date>"
						+ formatageFormatDate(commande.getDateCommande()) + "</p:date>\r\n" + "    <p:adresseLivraison>"
						+ commande.getAdresseLivraison() + "</p:adresseLivraison>\r\n <p:prix>" + prix
						+ "</p:prix>  </p:commande>");
	}

	@SuppressWarnings("deprecation")
	public String formatageFormatDate(Date d) {
		String res = "";
		if (d != null) {
			int annee = d.getYear() - 100;
			int jour = d.getDate();
			int mois = d.getMonth() + 1;

			if (jour < 10) {
				res += "0";
			}
			res += jour + "-";
			if (mois < 10) {
				res += "0";
			}
			res += mois + "-";
			if (annee < 10) {
				res += "0";
			}
			res += annee;
		}
		return res;
	}

}
