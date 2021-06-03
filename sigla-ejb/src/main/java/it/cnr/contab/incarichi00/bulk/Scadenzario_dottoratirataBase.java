/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/06/2021
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Scadenzario_dottoratirataBase extends Scadenzario_dottoratirataKey implements Keyed {
//    ID_SCADENZARIO_DOTTORATI DECIMAL(38,0) NOT NULL
	private Long idScadenzarioDottorati;
 
//    CD_CDS VARCHAR(30) NOT NULL
	private String cdCds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private String cdUnitaOrganizzativa;
 
//    ESERCIZIO DECIMAL(38,0) NOT NULL
	private Long esercizio;
 
//    PG_RATA DECIMAL(38,0) NOT NULL
	private Long pgRata;
 
//    PG_MANDATO DECIMAL(38,0) NOT NULL
	private Long pgMandato;
 
//    DT_INIZIO_RATA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtInizioRata;
 
//    DT_FINE_RATA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtFineRata;
 
//    DT_SCADENZA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtScadenza;
 
//    IM_RATA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imRata;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SCADENZARIO_DOTTORATI_RATA
	 **/
	public Scadenzario_dottoratirataBase() {
		super();
	}
	public Scadenzario_dottoratirataBase(Long id) {
		super(id);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Identificativo scadenzario dottorati.]
	 **/
	public Long getIdScadenzarioDottorati() {
		return idScadenzarioDottorati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Identificativo scadenzario dottorati.]
	 **/
	public void setIdScadenzarioDottorati(Long idScadenzarioDottorati)  {
		this.idScadenzarioDottorati=idScadenzarioDottorati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public String getCdCds() {
		return cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(String cdCds)  {
		this.cdCds=cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOrganizzativa]
	 **/
	public String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOrganizzativa]
	 **/
	public void setCdUnitaOrganizzativa(String cdUnitaOrganizzativa)  {
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public Long getEsercizio() {
		return esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(Long esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgRata]
	 **/
	public Long getPgRata() {
		return pgRata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgRata]
	 **/
	public void setPgRata(Long pgRata)  {
		this.pgRata=pgRata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgMandato]
	 **/
	public Long getPgMandato() {
		return pgMandato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgMandato]
	 **/
	public void setPgMandato(Long pgMandato)  {
		this.pgMandato=pgMandato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtInizioRata]
	 **/
	public java.sql.Timestamp getDtInizioRata() {
		return dtInizioRata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtInizioRata]
	 **/
	public void setDtInizioRata(java.sql.Timestamp dtInizioRata)  {
		this.dtInizioRata=dtInizioRata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtFineRata]
	 **/
	public java.sql.Timestamp getDtFineRata() {
		return dtFineRata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtFineRata]
	 **/
	public void setDtFineRata(java.sql.Timestamp dtFineRata)  {
		this.dtFineRata=dtFineRata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtScadenza]
	 **/
	public java.sql.Timestamp getDtScadenza() {
		return dtScadenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtScadenza]
	 **/
	public void setDtScadenza(java.sql.Timestamp dtScadenza)  {
		this.dtScadenza=dtScadenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imRata]
	 **/
	public java.math.BigDecimal getImRata() {
		return imRata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imRata]
	 **/
	public void setImRata(java.math.BigDecimal imRata)  {
		this.imRata=imRata;
	}
}