/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/06/2021
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;

import java.sql.Timestamp;

public class AnagraficaDottoratiRateBase extends AnagraficaDottoratiRateKey implements Keyed {
//    ID_SCADENZARIO_DOTTORATI DECIMAL(38,0) NOT NULL
	private Long idAnagraficaDottorati;
 
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


	//    dacr TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dacr;

	//    utcr VARCHAR(20) NOT NULL
	private String utcr;

	//    duva TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp duva;

	//    utuv VARCHAR(20) NOT NULL
	private String utuv;

	//    cdTerzo DECIMAL(38,0) NOT NULL
	private Integer cdTerzo;

	//    pgBanca DECIMAL(38,0) NOT NULL
	private Integer pgBanca;

	//    CD_MODALITA_PAG VARCHAR(10) NOT NULL
	private String cdModalitaPag;

	//    stato VARCHAR(100) NOT NULL
	private String stato;


	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ANAGRAFICA_DOTTORATI_RATE
	 **/
	public AnagraficaDottoratiRateBase() {
		super();
	}
	public AnagraficaDottoratiRateBase(Long id) {
		super(id);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Identificativo scadenzario dottorati.]
	 **/
	public Long getIdAnagraficaDottorati() {
		return idAnagraficaDottorati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Identificativo scadenzario dottorati.]
	 **/
	public void setIdAnagraficaDottorati(Long idAnagraficaDottorati)  {
		this.idAnagraficaDottorati=idAnagraficaDottorati;
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

	@Override
	public Timestamp getDacr() {
		return dacr;
	}

	@Override
	public void setDacr(Timestamp dacr) {
		this.dacr = dacr;
	}

	@Override
	public String getUtcr() {
		return utcr;
	}

	@Override
	public void setUtcr(String utcr) {
		this.utcr = utcr;
	}

	@Override
	public Timestamp getDuva() {
		return duva;
	}

	@Override
	public void setDuva(Timestamp duva) {
		this.duva = duva;
	}

	@Override
	public String getUtuv() {
		return utuv;
	}

	@Override
	public void setUtuv(String utuv) {
		this.utuv = utuv;
	}

	public Integer getCdTerzo() {
		return cdTerzo;
	}

	public void setCdTerzo(Integer cdTerzo) {
		this.cdTerzo = cdTerzo;
	}

	public String getCdModalitaPag() {
		return cdModalitaPag;
	}

	public void setCdModalitaPag(String cdModalitaPag) {
		this.cdModalitaPag = cdModalitaPag;
	}

	public Integer getPgBanca() {
		return pgBanca;
	}

	public void setPgBanca(Integer pgBanca) {
		this.pgBanca = pgBanca;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}
}