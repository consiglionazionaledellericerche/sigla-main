/*
 * Created by BulkGenerator 1.5 [30/07/2008]
 * Date 31/07/2008
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;
import it.cnr.jada.bulk.annotation.InputType;
import it.cnr.jada.persistency.Keyed;
public class SessionTraceBase extends SessionTraceKey implements Keyed {
//    SERVER_URL VARCHAR(100) NOT NULL
	@FieldPropertyAnnotation(name="serverUrl",
			inputType=InputType.TEXTAREA,
			cols=60,
			rows=5, 
			maxLength=100,
			nullable=false,
			label="Server Url")
	private java.lang.String server_url;
 
//    CD_UTENTE VARCHAR(20)
	@FieldPropertyAnnotation(name="cdUtente",
			inputType=InputType.TEXT,
			inputSize=20,
			maxLength=20,
			label="Utente")
	private java.lang.String cd_utente;
 
//  CD_CDS VARCHAR(30)
	@FieldPropertyAnnotation(name="cdCds",
			inputType=InputType.TEXT,
			inputSize=3,
			maxLength=3,
			label="CdS")
	private java.lang.String cd_cds;
	
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Table name: SESSION_TRACE
	 **/
	public SessionTraceBase() {
		super();
	}
	public SessionTraceBase(java.lang.String id_sessione) {
		super(id_sessione);
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Restituisce il valore di: [server_url]
	 **/
	public java.lang.String getServer_url() {
		return server_url;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Setta il valore di: [server_url]
	 **/
	public void setServer_url(java.lang.String server_url)  {
		this.server_url=server_url;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Restituisce il valore di: [cd_utente]
	 **/
	public java.lang.String getCd_utente() {
		return cd_utente;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Setta il valore di: [cd_utente]
	 **/
	public void setCd_utente(java.lang.String cd_utente)  {
		this.cd_utente=cd_utente;
	}

	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds) {
		this.cd_cds = cd_cds;
	}
}