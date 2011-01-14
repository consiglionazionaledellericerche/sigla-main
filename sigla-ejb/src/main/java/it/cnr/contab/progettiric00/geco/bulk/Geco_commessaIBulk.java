package it.cnr.contab.progettiric00.geco.bulk;

import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;

public interface Geco_commessaIBulk{
	public abstract java.lang.Long getId_comm();
	public abstract java.lang.Long getEsercizio();
	public abstract java.lang.String getFase();
	public abstract java.lang.String getCod_comm();
	public abstract java.lang.String getDescr_comm();
	public abstract java.lang.Long getId_prog();
	public abstract java.lang.String getCds();
	public abstract java.lang.String getSede_svol_uo();
	public abstract java.sql.Timestamp getData_inizio_attivita();
	public abstract java.lang.String getCod_3rzo_resp();
	public abstract java.lang.Integer getEsito_negoz();
	public abstract void aggiornaProgettoSIP(Progetto_sipBulk progetto_sip);
}
