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
 * 6- API pour recevoir les infos client
 * ******************************************
 *
 *         /api/client/infos
 *
 * param = { login: A }
 *
 * RECOIT DU XML :
 *
 * <?xml version="1.0"?>
 * <xsd:schema xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 *         attributeFormDefault="unqualified" elementFormDefault="qualified"
 *         targetNamespace="http://integrateur/menu" xmlns:xsd=
 *         "http://www.w3.org/2001/XMLSchema">
 * <xsd:element name="client" type="Client"/>
 * <xsd:complexType name="Client"> <xsd:sequence>
 * <xsd:element name="id" type="xsd:string"/>
 * <xsd:element name="nom" type="xsd:string"/>
 * <xsd:element name="photo" type="xsd:string"/>
 * <xsd:element name="email" type="xsd:string"/>
 * <xsd:element name="tel" type="xsd:string"/>
 * <xsd:element name="adresse" type="xsd:string"/>
 *
 * </xsd:sequence> </xsd:complexType> </xsd:schema>
 *
 */
public class Client_Infos extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // DAO et focntion commune
        ClientDAO daoClient = new ClientDAO();

        // parametre
        request.setCharacterEncoding("UTF-8");
        String idClient = request.getParameter("login");

        // filtre
        Boolean idClientNul = idClient == null || idClient.equals("null");

        if (idClientNul) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            Client client = daoClient.read(idClient);
            if (client.getNom() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                response.setContentType("text/xml");
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter()
                        .print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
                                + "<p:client xmlns:p=\"http://integrateur/menu\"\r\n"
                                + "	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
                                + "	xsi:schemaLocation=\"http://integrateur/menu Client.xsd \">\r\n" + "	<p:id>"
                                + client.getIdClient() + "</p:id>\r\n" + "	<p:nom>" + client.getNom() + "</p:nom>\r\n"
                                + "	<p:photo>" + client.getPhoto() + "</p:photo>\r\n" + "	<p:email>"
                                + client.getEmail() + "</p:email>\r\n" + "	<p:tel>" + client.getTel() + "</p:tel>\r\n"
                                + "	<p:adresse>" + client.getAdresse() + "</p:adresse>\r\n" + "</p:client>");
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
