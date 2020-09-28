package l3m.modelisation;

import java.util.List;

public class Carte {

    List<Plat> plats;
    List<Menu> menus;

    public List<Menu> getLesMenus() {
        return menus;
    }

    public void setLesMenus(List<Menu> menus) {
        this.menus = menus;
    }

    /**
     * @return the plats
     */
    public List<Plat> getPlats() {
        return plats;
    }

    /**
     * @param plats the plats to set
     */
    public void setPlats(List<Plat> plats) {
        this.plats = plats;
    }

    @Override
    public String toString() {
        return "Carte{" + "plats=" + plats + ", menus=" + menus + '}';
    }

}
