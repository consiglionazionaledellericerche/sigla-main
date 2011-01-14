package it.cnr.contab.progettiric00.geco.bulk;

import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;

public interface Geco_progettoIBulk {
	
	public abstract java.lang.Long getId_prog();
	public abstract java.lang.Long getEsercizio();
	public abstract java.lang.String getFase();
	public abstract java.lang.String getCod_prog();
	public abstract java.lang.String getDescr_prog();
	public abstract java.sql.Timestamp getData_istituzione_prog();
	public abstract java.lang.Long getId_dip();
	public abstract void aggiornaProgettoSIP(Progetto_sipBulk progetto_sip);
}
