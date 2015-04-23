package it.cnr.contab.utenze00.bp;

/**
 * Classe che definisce il contesto REST per i servizi JSON
 */

public class RESTUserContext extends CNRUserContext implements it.cnr.jada.UserContext {
	private static final long serialVersionUID = 1L;
	
	public RESTUserContext() {
		this(null, null, null, null);
	}
	public RESTUserContext(Integer esercizio,
			String cd_unita_organizzativa, String cd_cds, String cd_cdr) {
		super("RESTUSER", null, esercizio, cd_unita_organizzativa, cd_cds, cd_cdr);
	}		
}