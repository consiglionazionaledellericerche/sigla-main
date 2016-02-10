/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 29/05/2009
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Utente_gestoreBulk extends Utente_gestoreBase {
	
	private UtenteBulk utente;
	private UtenteBulk gestore;
	
	public Utente_gestoreBulk() {
		super();
	}
	public Utente_gestoreBulk(java.lang.String cd_utente, java.lang.String cd_gestore) {
		super(cd_utente, cd_gestore);
	}
	
	
	public UtenteBulk getUtente() {
		return utente;
	}
	public String getCd_utente() {
		UtenteBulk utente = this.getUtente();
		if (utente == null)
			return null;
		return utente.getCd_utente();
	}
	public void setUtente(UtenteBulk newUtente) {
		utente = newUtente;
	}
	public void setCd_utente(String cd_utente) {
		this.getUtente().setCd_utente(cd_utente);
	}
	
	
	public UtenteBulk getGestore() {
		return gestore;
	}
	public String getCd_gestore() {
		UtenteBulk gestore = this.getGestore();
		if (gestore == null)
			return null;
		return gestore.getCd_utente();
	}
	public void setGestore(UtenteBulk newGestore) {
		gestore = newGestore;
	}
	public void setCd_gestore(String cd_gestore) {
		this.getGestore().setCd_gestore(cd_gestore);
	}
	
	public static java.sql.Timestamp getDataOdierna() throws it.cnr.jada.action.BusinessProcessException
	{
		try{return getDataOdierna(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());}
		catch (javax.ejb.EJBException e){throw new it.cnr.jada.action.BusinessProcessException(e);}
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (24/05/2002 13.01.57)
	 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
	 * @param map java.util.Map
	 */
	public static java.sql.Timestamp getDataOdierna(java.sql.Timestamp dataOdierna) {

		java.util.Calendar gc = java.util.Calendar.getInstance();
		gc.setTime(dataOdierna);
		gc.set(java.util.Calendar.HOUR, 0);
		gc.set(java.util.Calendar.MINUTE, 0);
		gc.set(java.util.Calendar.SECOND, 0);
		gc.set(java.util.Calendar.MILLISECOND, 0);
		gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
		return new java.sql.Timestamp(gc.getTime().getTime());
	}
	public java.util.Calendar getDateCalendar(java.sql.Timestamp date) {

		if (date == null)
			date = new java.sql.Timestamp(System.currentTimeMillis());
			
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(java.util.Calendar.HOUR, 0);
		calendar.set(java.util.Calendar.MINUTE, 0);
		calendar.set(java.util.Calendar.SECOND, 0);
		calendar.set(java.util.Calendar.MILLISECOND, 0);
		calendar.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
		return calendar;
	}
}