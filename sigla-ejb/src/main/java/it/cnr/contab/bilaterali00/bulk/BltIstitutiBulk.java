/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/07/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class BltIstitutiBulk extends BltIstitutiBase {
	/**
	 * [COMUNE Codifica dei comuni italiani e delle città  estere.E' definito un dialogo utente per popolare le città  estere; per i comuni italiani si prevede il recupero in sede di migrazione]
	 **/
	private ComuneBulk comune =  new ComuneBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_ISTITUTI
	 **/
	public BltIstitutiBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_ISTITUTI
	 **/
	public BltIstitutiBulk(java.lang.String cdCentroResponsabilita) {
		super(cdCentroResponsabilita);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Codifica dei comuni italiani e delle città  estere.E' definito un dialogo utente per popolare le città  estere; per i comuni italiani si prevede il recupero in sede di migrazione]
	 **/
	public ComuneBulk getComune() {
		return comune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Codifica dei comuni italiani e delle città  estere.E' definito un dialogo utente per popolare le città  estere; per i comuni italiani si prevede il recupero in sede di migrazione]
	 **/
	public void setComune(ComuneBulk comune)  {
		this.comune=comune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgComune]
	 **/
	public java.lang.Long getPgComune() {
		ComuneBulk comune = this.getComune();
		if (comune == null)
			return null;
		return getComune().getPg_comune();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgComune]
	 **/
	public void setPgComune(java.lang.Long pgComune)  {
		this.getComune().setPg_comune(pgComune);
	}
}