/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/08/2021
 */
package it.cnr.contab.progettiric00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class Progetto_anagraficoBase extends Progetto_anagraficoKey implements Keyed {
//    CD_ANAG DECIMAL(38,0) NOT NULL
	private Integer cdAnag;
 
//    PG_PROGETTO DECIMAL(38,0) NOT NULL
	private Integer pgProgetto;
 
//    DATA_INIZIO TIMESTAMP(7)
	private java.sql.Timestamp dataInizio;
 
//    DATA_FINE TIMESTAMP(7)
	private java.sql.Timestamp dataFine;
 
//    IMPORTO DECIMAL(15,2)
	private java.math.BigDecimal importo;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: PROGETTO_ANAGRAFICO
	 **/
	public Progetto_anagraficoBase() {
		super();
	}
	public Progetto_anagraficoBase(Long idPrgAnagrafico) {
		super(idPrgAnagrafico);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Codice entità anagrafica di primo livello]
	 **/
	public Integer getCdAnag() {
		return cdAnag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Codice entità anagrafica di primo livello]
	 **/
	public void setCdAnag(Integer cdAnag)  {
		this.cdAnag=cdAnag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Progressivo del progetto di ricerca]
	 **/
	public Integer getPgProgetto() {
		return pgProgetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Progressivo del progetto di ricerca]
	 **/
	public void setPgProgetto(Integer pgProgetto)  {
		this.pgProgetto=pgProgetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Data inizio nel progetto]
	 **/
	public java.sql.Timestamp getDataInizio() {
		return dataInizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Data inizio nel progetto]
	 **/
	public void setDataInizio(java.sql.Timestamp dataInizio)  {
		this.dataInizio=dataInizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Data fine  nel progetto]
	 **/
	public java.sql.Timestamp getDataFine() {
		return dataFine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Data fine  nel progetto]
	 **/
	public void setDataFine(java.sql.Timestamp dataFine)  {
		this.dataFine=dataFine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Importo totale]
	 **/
	public java.math.BigDecimal getImporto() {
		return importo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Importo totale]
	 **/
	public void setImporto(java.math.BigDecimal importo)  {
		this.importo=importo;
	}
}