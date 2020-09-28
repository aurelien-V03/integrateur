package l3m.bdd;

/**
 *
 * @author labbo
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import l3m.modelisation.Plat;
import l3m.modelisation.TypeDePlat;

public class DomPlatDAO extends DomDAO<Plat> {

	public DomPlatDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setNomDocument(String nomDocument) {
		super.nomDocument = nomDocument;
	}
	
	// cette methode retourne un objet Plat dont l'id est passé en parametre
	@Override
	public Plat read(int id) {

		// plat a retourner
		Plat plat = new Plat();
		List<String> ingredientsDunplat = new ArrayList<>();
		DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
		if (this.nomDocument != null) {
			try {
				DocumentBuilder constructeur = fabrique.newDocumentBuilder();
				File xml = new File(this.nomDocument);
				this.doc = constructeur.parse(xml);
				NodeList lesplats = this.doc.getElementsByTagName("p:idPlat");
				NodeList fils;
				int i = 0;
				int j;
				int lid;
				Node tmp;
				while (i < lesplats.getLength()) {
					tmp = lesplats.item(i);
					lid = Integer.parseInt(lesplats.item(i).getTextContent());
					if (tmp.getParentNode().getNodeName().equals("p:Plats") && lid == id) {
						fils = tmp.getParentNode().getChildNodes();
						j = 0;
						while (j < fils.getLength()) {
							switch (fils.item(j).getNodeName()) {
							case "p:idPlat":
								plat.setIdPlat(Integer.parseInt(fils.item(j).getTextContent()));
								break;
							case "p:nomPlat":
								plat.setNomPlat(fils.item(j).getTextContent());
								break;
							case "p:type":
								if (fils.item(j).getTextContent().equals("Entree")) {
									plat.setType(TypeDePlat.ENTREE);
								}
								if (fils.item(j).getTextContent().equals("Plat")) {
									plat.setType(TypeDePlat.PLAT);
								}
								if (fils.item(j).getTextContent().equals("Dessert")) {
									plat.setType(TypeDePlat.DESSERT);
								}
								if (fils.item(j).getTextContent().equals("Boisson")) {
									plat.setType(TypeDePlat.BOISSON);
								}
								break;
							case "p:prix":
								plat.setPrix(Double.parseDouble(fils.item(j).getTextContent()));
								break;
							case "p:photo":
								plat.setPhoto(fils.item(j).getTextContent());
								break;
							case "p:ingredients":
								NodeList lesIngredients = fils.item(j).getChildNodes();
								int k = 0;
								while (k < lesIngredients.getLength()) {
									if (lesIngredients.item(k).getNodeName().equals("p:ingredient")) {
										ingredientsDunplat.add(lesIngredients.item(k).getTextContent());
									}
									k++;
								}
								plat.setIngredients(ingredientsDunplat);
								break;
							default:
								break;
							}
							j++;
						}

					}

					i++;
				}

			} catch (IOException | SAXException e) {
				System.out.println(e.getMessage());

			} catch (ParserConfigurationException ex) {
				Logger.getLogger(DomPlatDAO.class.getName()).log(Level.SEVERE, null, ex);
			}

		} else {
			System.out.println("Merci de donner le fichier xml " + "contenant la carte à lire");
			System.out.println("objet DomPlatDao.nomDocument=chemin du fichier");
		}

		return plat;
	}

	// cette methode supprime un plat de la carte de this.nomDocument
	@Override
	public boolean delete(Object Plat) {
		boolean res = false;
		Node parent;
		Node aSupprimer;
		int i = 0;
		Plat leplat = (Plat) Plat;
		DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();

		if (this.nomDocument != null) {
			try {
				DocumentBuilder constructeur = fabrique.newDocumentBuilder();
				File xml = new File(this.nomDocument);
				this.doc = constructeur.parse(xml);
				NodeList lesidPlats = this.doc.getElementsByTagName("p:idPlat");

				while (i < lesidPlats.getLength()) {
					// Supprimer ce plat en plat dans la liste des plats de la carte et
					if (lesidPlats.item(i).getParentNode().getNodeName().equals("p:Plats")) {
						if (Integer.parseInt(lesidPlats.item(i).getTextContent()) == leplat.getIdPlat()) {
							// recuperer le parent du noued à supprimer
							parent = lesidPlats.item(i).getParentNode().getParentNode();
							// recuperer le noeud à supprimer
							aSupprimer = lesidPlats.item(i).getParentNode();
							parent.removeChild(aSupprimer);
						}
					} else if (lesidPlats.item(i).getParentNode().getNodeName().equals("p:listePlat")) {
						if (Integer.parseInt(lesidPlats.item(i).getTextContent()) == leplat.getIdPlat()) {
							parent = lesidPlats.item(i).getParentNode();
							aSupprimer = lesidPlats.item(i);
							parent.removeChild(aSupprimer);
						}
					}
					i++;
				}
				this.EnregistrerLesModification();
				System.out.println("Un plat  suprrimer avec succes");
				res = true;
			} catch (SAXException e) {
			} catch (IOException | ParserConfigurationException | TransformerException ex) {
				Logger.getLogger(DomPlatDAO.class.getName()).log(Level.SEVERE, null, ex);
			}

		} else {
			System.out.println("Merci de donner le nom du document à supprimer");
			System.out.println("objet DomPlatDao.nomDocument=chemin du fichier xml");
		}

		return res;
	}

	// ajoute d'un plat dans this.nomDocument l'id des plats est calculé
	// automatiquement
	// pour minimiser des redondances
	@Override
	public boolean create(Object Plat) {

		Plat plat = (Plat) Plat;
		boolean resultat = false;
		int id;
		NodeList lesIdsExistants;

		DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();

		if (plat != null && this.nomDocument != null) {

			try {

				DocumentBuilder constructeur = fabrique.newDocumentBuilder();
				File xml = new File(this.nomDocument);
				this.doc = constructeur.parse(xml);
				// Chercher le dernier id du documment
				lesIdsExistants = this.doc.getElementsByTagName("p:idPlat");
				id = lesIdsExistants.getLength() / 2;
				id = id + 1;

				// element plat à ajouter
				Element platAjouter = this.doc.createElement("p:Plats");

				// element fils du plat : identifiant du plat
				Element fils = this.doc.createElement("p:idPlat");
				fils.setTextContent("" + id);
				platAjouter.appendChild(fils);

				// element fils du plat : nom du plat
				fils = this.doc.createElement("p:nomPlat");
				fils.setTextContent("" + plat.getNomPlat());
				platAjouter.appendChild(fils);

				// element fils du plat :type de plat
				fils = this.doc.createElement("p:type");
				fils.setTextContent(plat.getType().getValeur());
				platAjouter.appendChild(fils);

				// element fils du plat :prix
				fils = this.doc.createElement("p:prix");
				fils.setTextContent("" + plat.getPrix());
				platAjouter.appendChild(fils);

				// element fils du plat :photo
				fils = this.doc.createElement("p:photo");
				fils.setTextContent("" + plat.getPhoto());
				platAjouter.appendChild(fils);

				// element fils du plat :les ingredients
				Element ingredients = this.doc.createElement("p:ingredients");
				for (String ingredient : plat.getIngredients()) {
					fils = this.doc.createElement("p:ingredient");
					fils.setTextContent(ingredient);
					ingredients.appendChild(fils);
				}
				platAjouter.appendChild(ingredients);
				// ajouter le plat avant le menu pour que ça soit conforme avec le schema xsd
				// d'une carte
				Node apresMenu = this.doc.getElementsByTagName("p:Menu").item(0).getParentNode();
				this.doc.getElementsByTagName("p:Carte").item(0).insertBefore(platAjouter, apresMenu);

				// Ajouter ce plat dans le menu
				fils = this.doc.createElement("p:idPlat");
				fils.setTextContent("" + id);
				platAjouter.appendChild(fils);

				// Ajouter ce plat dans le menu
				this.doc.getElementsByTagName("p:listePlat").item(0).appendChild(fils);

				this.EnregistrerLesModification();
				System.out.println("Un plat ajouté avec succes");
				resultat = true;

			} catch (IOException | SAXException | TransformerException | ParserConfigurationException ex) {
				Logger.getLogger(DomPlatDAO.class.getName()).log(Level.SEVERE, null, ex);
			}

		} else {
			System.out.println("Assurer vous d'avoir mis un plat non null en paramettre");
			System.out.println("Et que vous avez fourni un chemin de fichier existant");
		}

		return resultat;
	}

	void EnregistrerLesModification() throws TransformerException {
		// ecrire dans un fichier xml le resultat
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(this.nomDocument));
		transformer.transform(source, result);
	}

	// Cette methode mets à jour le plat
	@Override
	public boolean update(Object Plat) {
		Plat leplat = (Plat) Plat;
		if (this.delete(leplat)) {
			if (this.create(leplat)) {
				System.out.println("plat mis à jour avec succees");
				return true;
			}
		}
		return false;
	}

}
