package l3m.bdd.outils.sql;

public class OutilsAffichage {
	
//	/**
//	 * Affiche un message d'erreur en rouge
//	 * @param message
//	 */
//	public static void afficherMessageErreur(String message) {
//		System.out.println(CouleurConsole.RED_BACKGROUND+"ERROR:"+CouleurConsole.RESET
//				+CouleurConsole.RED+" "+message+CouleurConsole.RESET);
//	}
//	
//	/**
//	 * Affiche un message d'information en bleu
//	 * @param message
//	 */
//	public static void afficherMessageInformation(String message) {
//		System.out.println(CouleurConsole.BLUE_BACKGROUND+"INFOS:"+CouleurConsole.RESET
//				+CouleurConsole.BLUE_BACKGROUND+" "+message+CouleurConsole.RESET);
//	}
//	
//	/**
//	 * Affiche un message d'information en vert
//	 * @param message
//	 */
//	public static void afficherMessageConfirmation(String message) {
//		System.out.println(CouleurConsole.GREEN_BACKGROUND+"INFOS:"+CouleurConsole.RESET
//				+CouleurConsole.GREEN_BACKGROUND+" "+message+CouleurConsole.RESET);
//	}
	
	/**
	 * Affiche un message d'erreur en rouge
	 * @param message
	 */
	public static void afficherMessageErreur(String message) {
		System.out.println("ERROR: "+message);
	}
	
	/**
	 * Affiche un message d'information en bleu
	 * @param message
	 */
	public static void afficherMessageInformation(String message) {
		System.out.println("INFOS: "+message);
	}
	
	/**
	 * Affiche un message d'information en vert
	 * @param message
	 */
	public static void afficherMessageConfirmation(String message) {
		System.out.println("INFOS: "+message);
	}
}
