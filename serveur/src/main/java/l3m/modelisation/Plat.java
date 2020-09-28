package l3m.modelisation;

import java.util.List;

public class Plat {

    private int idPlat;
    private String nomPlat;
    private TypeDePlat type;
    private double prix;
    private String photo;
    private List<String> ingredients;

    public int getIdPlat() {
        return idPlat;
    }

    public String getNomPlat() {
        return nomPlat;
    }

    public void setNomPlat(String nomPlat) {
        this.nomPlat = nomPlat;
    }

    /**
     * @return the idPlat
     */
    public void setIdPlat(int idPlat) {
        this.idPlat = idPlat;
    }

    /**
     * @param idPlat the idPlat to set
     */
    /**
     * @return the type
     */
    public TypeDePlat getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(TypeDePlat type) {
        this.type = type;
    }

    /**
     * @return the prix
     */
    public double getPrix() {
        return prix;
    }

    /**
     * @param prix the prix to set
     */
    public void setPrix(double prix) {
        this.prix = prix;
    }

    /**
     * @return the photo
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * @param photo the photo to set
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * @return the ingredients
     */
    public List<String> getIngredients() {
        return ingredients;
    }

    /**
     * @param ingredients the ingredients to set
     */
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Plat{" + "idPlat=" + idPlat + ", nomPlat=" + nomPlat + ", type=" + type + ", prix=" + prix + ", photo=" + photo + ", ingredients=" + ingredients + '}';
    }

}
