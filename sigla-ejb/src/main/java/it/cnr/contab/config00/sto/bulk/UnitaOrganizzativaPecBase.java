/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 14/06/2010
 */
package it.cnr.contab.config00.sto.bulk;
import it.cnr.jada.persistency.Keyed;
public class UnitaOrganizzativaPecBase extends UnitaOrganizzativaPecKey implements Keyed {
//    EMAIL_PEC VARCHAR(100) NOT NULL
	private java.lang.String sigla;
	private java.lang.String emailPec;
	private java.lang.String emailPecDirettore;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: UNITA_ORGANIZZATIVA_PEC
	 **/
	public UnitaOrganizzativaPecBase() {
		super();
	}
	public UnitaOrganizzativaPecBase(java.lang.String cdUnitaOrganizzativa) {
		super(cdUnitaOrganizzativa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [emailPec]
	 **/
	public java.lang.String getEmailPec() {
		return emailPec;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [emailPec]
	 **/
	public void setEmailPec(java.lang.String emailPec)  {
		this.emailPec=emailPec;
	}
	public void setEmailPecDirettore(java.lang.String emailPecDirettore) {
		this.emailPecDirettore = emailPecDirettore;
	}
	public java.lang.String getEmailPecDirettore() {
		return emailPecDirettore;
	}
	public void setSigla(java.lang.String sigla) {
		this.sigla = sigla;
	}
	public java.lang.String getSigla() {
		return sigla;
	}
}