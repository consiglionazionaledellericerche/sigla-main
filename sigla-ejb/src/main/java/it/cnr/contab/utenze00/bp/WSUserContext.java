package it.cnr.contab.utenze00.bp;


import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Classe che definisce il contesto WebService
 */

public class WSUserContext extends CNRUserContext implements it.cnr.jada.UserContext {

	public WSUserContext(String user, String sessionId, Integer esercizio,
			String cd_unita_organizzativa, String cd_cds, String cd_cdr) {
		super(user, sessionId, esercizio, cd_unita_organizzativa, cd_cds, cd_cdr);
		// TODO Auto-generated constructor stub
	}
}
