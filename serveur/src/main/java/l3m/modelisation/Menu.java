package l3m.modelisation;

import java.util.List;

public class Menu {

    private int idMenu;
    private List<Integer> idPlats;
    private List<String> genres;

    /**
     * @return the idMenu
     */
    public int getIdMenu() {
        return idMenu;
    }

    /**
     * @param idMenu the idMenu to set
     */
    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    /**
     * @return the idPlats
     */
    public List<Integer> getIdPlats() {
        return idPlats;
    }

    /**
     * @param idPlats the idPlats to set
     */
    public void setIdPlats(List<Integer> idPlats) {
        this.idPlats = idPlats;
    }

    /**
     * @return the genres
     */
    public List<String> getGenres() {
        return genres;
    }

    /**
     * @param genres the genres to set
     */
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Menu{" + "idMenu=" + idMenu + ", idPlats=" + idPlats + ", genres=" + genres + '}';
    }

}
