/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package l3m.bdd;

import java.util.ArrayList;
import java.util.List;
import l3m.modelisation.Carte;
import l3m.modelisation.Menu;
import l3m.modelisation.Plat;

/**
 *
 * @author labbo
 */
public class TestDomCarteDao {

    public static void main(String[] args) {

        //la carte DomDaoCarte
        DomCarteDAO uneCarteTest = new DomCarteDAO();

//        //je fais le test sur le fichier ci dessous
//        uneCarteTest.nomDocument = "./src/main/java/l3m/bdd/xml/carte.xml";
//        //TEST DE LECTURE D'UNE CARTE DU FICHIER CI DESSOUS
//        System.out.println(uneCarteTest.read(0));

        //POUR FAIRE LE TEST DE LA CREATION D'UNE CARTE 
        //MERCI DE DECOMMENTER LE CODE CI-DESSOUS à partir de ligne 35 ET 
        //ET COMMENTER CELUI CI-DESSUS de ligne 25 à 30 SAUF LA DECLARATION DE uneCarteTest
        //la carte java a mettre à creer en xml
        Carte uneCarteJava = new Carte();
        //recuperation dune liste de plat dans une carte pour faire un test
        DomPlatDAO domplat = new DomPlatDAO();
        domplat.nomDocument = "./src/main/java/l3m/bdd/xml/carte.xml";
        List<Plat> listPlat = new ArrayList<>();
        listPlat.add(domplat.read(1));
        listPlat.add(domplat.read(2));
        listPlat.add(domplat.read(3));
        listPlat.add(domplat.read(4));

        //ajout de cette liste de plat a la carte java
        uneCarteJava.setPlats(listPlat);

        //creer une liste de genres 
        List<Menu> listedeMenu = new ArrayList<>();

        Menu unMenu = new Menu();

        List<String> lesGenres = new ArrayList<>();
        lesGenres.add("Amour");
        lesGenres.add("Policier");
        lesGenres.add("Science fiction");
        unMenu.setGenres(lesGenres);
        List<Integer> lesid = new ArrayList<>();
        lesid.add(1);
        lesid.add(2);
        lesid.add(1);
        lesid.add(2);
        unMenu.setIdPlats(lesid);
        unMenu.setIdMenu(150);

        listedeMenu.add(unMenu);

        //ajout de cette liste de genres à la carte Java
        uneCarteJava.setLesMenus(listedeMenu);

        //faire le test pour la creation de la nouvelle carte en xml
        uneCarteTest.create(uneCarteJava);
    }

}
