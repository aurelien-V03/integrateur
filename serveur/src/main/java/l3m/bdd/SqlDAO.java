package l3m.bdd;

import l3m.bdd.outils.sql.OutilConnection;

public abstract class SqlDAO<T> implements DAO<T> {

	private OutilConnection connect;
	
	public SqlDAO (){
		connect = new OutilConnection();
	}
	
	public SqlDAO(OutilConnection connect) {
		this.connect = connect;
	}
	
	public OutilConnection getConnect() {
		return connect;
	}
	
	public void fermerConnectionBDD() {
		this.connect.close();
	}

	
	@Override
	public abstract boolean create(java.lang.Object T);

	@Override
	public abstract java.lang.Object read(int id);

	@Override
	public abstract boolean update(java.lang.Object T);

	@Override
	public abstract boolean delete(java.lang.Object T);

}
