package l3m;

import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import l3m.servlet.Status;
import l3m.servlet.client.Client_Authentification;
import l3m.servlet.client.Client_Infos;
import l3m.servlet.client.Client_Modifier;
import l3m.servlet.commande.Commande_Modifier;
import l3m.servlet.commande.Commande_Rechercher;
import l3m.servlet.commande.Commande_Supprimer;
import l3m.servlet.commande.Commande_Valider;
import l3m.servlet.menu.Menus;

/**
 * Lancement du serveur
 */
public class PizzaServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Server server;

	void start() throws Exception {
		int maxThreads = 100;
		int minThreads = 10;
		int idleTimeout = 120;

		QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);

		server = new Server(threadPool);
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8090);
		server.setConnectors(new Connector[] { connector });

		ServletHandler servletHandler = new ServletHandler();
		server.setHandler(servletHandler);

		// api
		
		//infos etat serveur
		servletHandler.addServletWithMapping(Status.class, "/status");
		servletHandler.addServletWithMapping(Status.class, "/infos");
		
		// API pour les commandes
		servletHandler.addServletWithMapping(Commande_Valider.class, "/api/commande/valider");
		servletHandler.addServletWithMapping(Commande_Rechercher.class, "/api/commande/rechercher");
		servletHandler.addServletWithMapping(Commande_Modifier.class, "/api/commande/modifier");
		servletHandler.addServletWithMapping(Commande_Supprimer.class, "/api/commande/supprimer");
		
		
		// API pour le client
		servletHandler.addServletWithMapping(Client_Authentification.class, "/api/client/authentification");
		servletHandler.addServletWithMapping(Client_Infos.class, "/api/client/infos");
		servletHandler.addServletWithMapping(Client_Modifier.class, "/api/client/modifierInfos");
		
		// API pour le menu
		servletHandler.addServletWithMapping(Menus.class, "/api/menus");

		server.start();
	}

	void stop() throws Exception {
		System.out.println("Server stop");
		server.stop();
	}

	public static void main(String[] args) throws Exception {
		PizzaServer server = new PizzaServer();

		server.start();
	}

}
