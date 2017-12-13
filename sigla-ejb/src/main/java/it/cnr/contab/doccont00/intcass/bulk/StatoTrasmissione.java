package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;

public interface StatoTrasmissione extends AllegatoParentBulk{
	public static String ALL = "ALL";
	public java.lang.String getStato_trasmissione();
	public void setStato_trasmissione(java.lang.String stato_trasmissione);
	public String getStorePath();
	public Integer getEsercizio();
	public String getCd_cds();
	public String getCd_unita_organizzativa();
	public Long getPg_documento_cont();
	public String getCd_tipo_documento_cont();
	public String getCMISName();
	public String getCMISFolderName();
}