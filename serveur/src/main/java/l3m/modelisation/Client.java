package l3m.modelisation;

public class Client {
	
	private String idClient;
    private String nom;
    private String photo;
    private String email;
    private String tel;
    private String adresse;
    private int pointFidelites;
    
	/**
	 * @return the idClient
	 */
	public String getIdClient() {
		return idClient;
	}
	/**
	 * @param idClient the idClient to set
	 */
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}
	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}
	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return the adresse
	 */
	public String getAdresse() {
		return adresse;
	}
	/**
	 * @param adresse the adresse to set
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	/**
	 * @return the pointFidelites
	 */
	public int getPointFidelites() {
		return pointFidelites;
	}
	/**
	 * @param pointFidelites the pointFidelites to set
	 */
	public void setPointFidelites(int pointFidelites) {
		this.pointFidelites = pointFidelites;
	}
	@Override
	public String toString() {
		return "Client [idClient=" + idClient + ", nom=" + nom + ", photo=" + photo + ", email=" + email + ", tel="
				+ tel + ", adresse=" + adresse + ", pointFidelites=" + pointFidelites + "]";
	}
    
	
}
