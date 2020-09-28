package l3m.bdd;

import org.w3c.dom.Document;

public abstract class DomDAO<T> implements DAO<T> {

    protected String nomDocument;
    protected Document doc;

    public abstract boolean create(Object T);

    @Override
    public abstract T read(int id);

    @Override
    public abstract boolean update(Object T);

    @Override
    public abstract boolean delete(Object T);

}
