/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package l3m.bdd;

import java.util.ArrayList;
import java.util.List;
import l3m.modelisation.Plat;
import l3m.modelisation.TypeDePlat;

/**
 *
 * @author labbo
 */
public class TestClassDomPlat {

    public static void main(String[] args) {

        DomPlatDAO test = new DomPlatDAO();
        //Exemple d'une carte 
        test.nomDocument = "./src/main/java/l3m/bdd/xml/carte.xml";

        //lecture dun plat de la carte ci-dessus carte.xml
        System.out.println("PLAT RECUPERER");
        System.out.println(test.read(1).toString());
        System.out.println("");

          //decommenter pour tester
          
////      //Ajout d'un plat dans une carte
          //creation du plat à ajouter
//        Plat p = new Plat();
//        List<String> ingredients = new ArrayList<>();
//        p.setIdPlat(6);
//        p.setNomPlat("Mon plat");
//        p.setPhoto("Vers limage image");
//        p.setPrix(12);
//        p.setType(TypeDePlat.PLAT);
//        ingredients.add("Ingredient");
//        ingredients.add("Ingredient");
//        ingredients.add("Ing 3");
//        ingredients.add("Ing 4");
//        p.setIngredients(ingredients);

//        //test.create(p);
        
        //La mise à jour d'un plat
       // test.update(p);
        
        System.out.println("");
//     //Test pour supprimer un plat cette methode recois un plat existant dans le plat en parametre
//      test.delete(test.read(7));
    }

}
