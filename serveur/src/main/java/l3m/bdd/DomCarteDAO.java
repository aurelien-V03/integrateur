package l3m.bdd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import l3m.modelisation.Carte;
import l3m.modelisation.Menu;
import l3m.modelisation.Plat;

/**
 *
 * @author labbo
 */
public class DomCarteDAO extends DomDAO<Carte> {

    public DomCarteDAO() {
        super();
    }

    //Cette methode retourne un objet java de type Carte  en lisant un  fichier xml
    // dont le chemin est egal this.nomDocument
    @Override
    public Carte read(int id) {
        //carte à retourner
        Carte carteAretouner = new Carte();
		List<Plat> lesplats = new ArrayList<>();
        List<Menu> leMenu = new ArrayList<>();
        Menu unMenu = new Menu();
        List<Integer> lesId = new ArrayList<>();
        List<String> lesGenres = new ArrayList<>();

        if (this.nomDocument != null) {
            //j'utilise la methode read de DomplatDao pour recuperer chaque plat
            //contenu  dans ce this.nomDocument
            DomPlatDAO plat = new DomPlatDAO();
            plat.nomDocument = this.nomDocument;
            plat.doc = this.doc;

            //parseur 
            DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder constructeur = fabrique.newDocumentBuilder();
                File xml = new File(this.nomDocument);
                this.doc = constructeur.parse(xml);
                NodeList lesNodePlats = this.doc.getElementsByTagName("p:Plats");
                NodeList lemenu = this.doc.getElementsByTagName("p:Menu");

                int i = 0;
                while (i < lesNodePlats.getLength()) {
                    NodeList unPlat = lesNodePlats.item(i).getChildNodes();
                    int j = 0;
                    while (j < unPlat.getLength()) {
                        if (unPlat.item(j).getNodeName().equals("p:idPlat")) {
                            lesplats.add(plat.read(Integer.parseInt(unPlat.item(j).getTextContent())));
                            lesId.add(Integer.parseInt(unPlat.item(j).getTextContent()));
                        }
                        j++;
                    }
                    i++;
                }
                carteAretouner.setPlats(lesplats);
                unMenu.setIdPlats(lesId);

                i = 0;
                while (i < lemenu.getLength()) {
                    NodeList lesFils = lemenu.item(i).getChildNodes();
                    int k = 0;
                    while (k < lesFils.getLength()) {
                        if (lesFils.item(k).getNodeName().equals("p:idMenu")) {
                            unMenu.setIdMenu(Integer.parseInt(lesFils.item(k).getTextContent()));
                        }
                        if (lesFils.item(k).getNodeName().equals("p:listeGenre")) {
                            int j = 0;
                            NodeList genresMovies = lesFils.item(k).getChildNodes();
                            while (j < genresMovies.getLength()) {
                                lesGenres.add(genresMovies.item(j).getTextContent());
                                j++;
                            }
                            unMenu.setGenres(lesGenres);
                        }

                        k++;
                    }
                    leMenu.add(unMenu);
                    carteAretouner.setLesMenus(leMenu);
                    i++;
                }
//                carteAretouner.setLesGenres(lesgenres);
                System.out.println("Une carte a ete lue avec succes");
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                Logger.getLogger(DomCarteDAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            System.out.println("Merci de donner le fichier xml "
                    + "contenant la carte à lire");
            System.out.println("comme ça : objet DomPlatDao.nomDocument=chemin du fichier");
        }

        return carteAretouner;
    }

    //Cette methode permet de generer une carte xml contraint sur le  platsSchema.xsd
    //Si this.nomDocument==null le fichier creer est placé dans src/main/java/l3m/bdd/xml/...
    //sinon le fichier est placé dans src/main/java/l3m/bdd/xml/this.nomDocument...
    @Override
    public boolean create(Object Carte) {
        Carte carte = (Carte) Carte;
        boolean resultat = false;

        if (carte != null) {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                this.doc = builder.newDocument();
                //creation de la racine
                Element racine = this.doc.createElement("p:Carte");
                Attr attribut = this.doc.createAttribute("xmlns:p");
                //ajout des attributs à la racine
                attribut.setValue("http://integrateur/carte");
                racine.setAttributeNode(attribut);
                attribut = this.doc.createAttribute("xmlns:xsi");
                attribut.setNodeValue("http://www.w3.org/2001/XMLSchema-instance");
                racine.setAttributeNode(attribut);
                attribut = this.doc.createAttribute("xsi:schemaLocation");
                attribut.setNodeValue("http://integrateur/carte platsSchema.xsd");
                racine.setAttributeNode(attribut);
                //ajout de la listes des plats

                carte.getPlats().forEach((plat) -> {
                    final Element platAjaouter = this.doc.createElement("p:Plats");
                    Element fils = this.doc.createElement("p:idPlat");
                    fils.setTextContent("" + plat.getIdPlat());
                    platAjaouter.appendChild(fils);
                    fils = this.doc.createElement("p:nomPlat");
                    fils.setTextContent(plat.getNomPlat());
                    platAjaouter.appendChild(fils);
                    fils = this.doc.createElement("p:type");

                    if (plat.getType().getValeur().equals("Entree")) {
                        fils.setTextContent("Entree");
                    }
                    if (plat.getType().getValeur().equals("Dessert")) {
                        fils.setTextContent("Dessert");
                    }
                    if (plat.getType().getValeur().equals("Boisson")) {
                        fils.setTextContent("Boisson");
                    }
                    if (plat.getType().getValeur().equals("Plat")) {
                        fils.setTextContent("Plat");
                    }
                    platAjaouter.appendChild(fils);
                    fils = this.doc.createElement("p:prix");
                    fils.setTextContent("" + plat.getPrix());
                    platAjaouter.appendChild(fils);
                    fils = this.doc.createElement("p:photo");
                    fils.setTextContent("" + plat.getPhoto());
                    platAjaouter.appendChild(fils);

                    fils = this.doc.createElement("p:ingredients");
                    int i = 0;
                    Element fils2;
                    while (i < plat.getIngredients().size()) {
                        String ingredient = plat.getIngredients().get(i);
                        fils2 = this.doc.createElement("p:ingredient");
                        fils2.setTextContent(ingredient);
                        fils.appendChild(fils2);
                        i++;
                    }
                    platAjaouter.appendChild(fils);
                    racine.appendChild(platAjaouter);
                });

                int id = 0;
                final Element menus = this.doc.createElement("p:Menus");
                Element fils = this.doc.createElement("p:Menu");
                Element fils2;
                for (Menu menu : carte.getLesMenus()) {
                    fils2 = this.doc.createElement("p:idMenu");
                    id = menu.getIdMenu();
                    fils2.setTextContent("" + id);
                    fils.appendChild(fils2);
                }

                fils2 = this.doc.createElement("p:listePlat");
                Element fils3;
                for (Plat plat : carte.getPlats()) {
                    fils3 = this.doc.createElement("p:idPlat");
                    fils3.setTextContent("" + plat.getIdPlat());
                    fils2.appendChild(fils3);
                }
                fils.appendChild(fils2);
                fils2 = this.doc.createElement("p:listeGenre");
                int i = 0;
                for (Menu menu : carte.getLesMenus()) {
                    List<String> genresDeMovies = new ArrayList<>();
                    for (String genre : menu.getGenres()) {
                        genresDeMovies.add(genre);
                        Node filsGenre = this.doc.createElement("p:genre");
                        filsGenre.setTextContent(genre);
                        fils2.appendChild(filsGenre);
                    }

                }
                fils.appendChild(fils2);
                menus.appendChild(fils);
                racine.appendChild(menus);

                this.doc.appendChild(racine);

                creationdelacarte(id);
                System.out.println("Une carte creer avec succes");
                System.out.println("Vous le trouverez dans ./src/main/java/l3m/bdd/xml/");
                resultat = true;
            } catch (ParserConfigurationException | TransformerConfigurationException ex) {
                Logger.getLogger(DomCarteDAO.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TransformerException ex) {
                Logger.getLogger(DomCarteDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Merci de passer une carte not null en paramettre de creatDomCarteDao");
        }
        return resultat;
    }

    private void creationdelacarte(long id) throws TransformerConfigurationException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        if (this.nomDocument == null) {
            DOMSource source = new DOMSource(this.doc);
            StreamResult sortie = new StreamResult(new File("./src/main/java/l3m/bdd/xml/carte" + id + ".xml"));
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source, sortie);
        } else {
            DOMSource source = new DOMSource(this.doc);
            StreamResult sortie = new StreamResult(new File(this.nomDocument + id + ".xml"));
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source, sortie);
        }

    }

    //Cette methode n'a pas d'interet pour une carte 
    @Override
    public boolean update(Object T) {
        return false;
    }

    //Cette methode n'a pas d'interet pour notre application
    @Override
    public boolean delete(Object T) {
        return false;
    }

}
