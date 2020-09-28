package l3m.bdd;

public interface DAO<T> {

	public boolean create(Object T);

	public Object read(int id);

	public boolean update(Object T);

	public boolean delete(Object T);

}
