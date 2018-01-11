package it.cnr.contab.progettiric00.geco.bulk;

import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;

public interface Geco_moduloIBulk {
	public abstract java.lang.Long getId_mod();
	public abstract java.lang.Long getEsercizio();
	public abstract java.lang.String getFase();
	public abstract java.lang.String getCod_mod();
	public abstract java.lang.String getDescr_mod();
	public abstract java.lang.Long getId_comm();
	//public abstract java.lang.Long getCod_tip();
	public abstract java.lang.String getSede_princ_cdsuo();
	public abstract java.lang.String getCod_3rzo_gest();
	public abstract java.sql.Timestamp getData_inizio_attivita();
	public abstract java.lang.Integer getEsito_negoz();
	public abstract java.lang.Long getStato_att_scie();
	public abstract java.lang.Long getStato_att_contab();
	public abstract java.lang.Long getStato();
	public abstract void aggiornaProgettoSIP(Progetto_sipBulk progetto_sip);

}
