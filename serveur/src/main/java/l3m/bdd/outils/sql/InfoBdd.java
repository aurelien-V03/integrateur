package l3m.bdd.outils.sql;

/**
 * @author Frederic PROCHNOW Cr�e le @31 mars 2020
 *
 * Info de connection
 */
public enum InfoBdd {

    URL_DATABASE("jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:im2ag"), USER_LOGIN("prochnof"),
    USER_MDP("1234Fred39");

    private String valeur;

    private InfoBdd(String valeur) {
        this.valeur = valeur;
    }
    public String getValeur() {
        return valeur;
    }
    public void setValeur(String valeur) {
        this.valeur = valeur;
    }
}
