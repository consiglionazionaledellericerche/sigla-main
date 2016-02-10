package it.cnr.contab.config00.geco.bulk;

import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;

public interface Geco_dipartimentiIBulk {
	public abstract java.lang.String getDescrizione();
	public abstract java.sql.Timestamp getData_istituzione();
	public abstract java.lang.Long getId_dip();
	public abstract java.lang.Long getEsercizio();
	public abstract java.lang.String getCod_dip();
	public abstract void aggiornaDipartimentoSIP(DipartimentoBulk dipartimento);
}
