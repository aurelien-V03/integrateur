package l3m.modelisation;

public enum TypeDePlat {

    ENTREE("Entree"),
    PLAT("Plat"),
    DESSERT("Dessert"),
    BOISSON("Boisson");

    private String valeur;

    TypeDePlat(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }

    @Override
    public String toString() {
        return "TypeDePlat{" + "valeur=" + valeur + '}';
    }
}
