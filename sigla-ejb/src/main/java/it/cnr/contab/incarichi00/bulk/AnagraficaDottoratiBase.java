/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 22/03/2021
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;

import java.sql.Timestamp;

public class AnagraficaDottoratiBase extends AnagraficaDottoratiKey implements Keyed {
//    REGIONE VARCHAR(250) NOT NULL
	private String regione;
 
//    UNIVERSITA_CAPOFILA VARCHAR(250) NOT NULL
	private String universitaCapofila;
 
//    CD_TERZO DECIMAL(38,0) NOT NULL
	private Integer cdTerzo;
 
//    TELEFONO_ATENEO VARCHAR(250)
	private String telefonoAteneo;
 
//    EMAIL_ATENEO VARCHAR(250)
	private String emailAteneo;
 
//    ALTRE_UNIVERSITA VARCHAR(250)
	private String altreUniversita;
 
//    DIPARTIMENTO_UNIVERSITA VARCHAR(250)
	private String dipartimentoUniversita;
 
//    CORSO_DOTTORATO VARCHAR(250)
	private String corsoDottorato;
 
//    TEMATICA VARCHAR(250)
	private String tematica;
 
//    NUMERO_CICLI_FINANZIATI DECIMAL(38,0)
	private Long numeroCicliFinanziati;
 
//    RICERCATORE VARCHAR(250)
	private String ricercatore;
 
//    ISTITUTO_CNR VARCHAR(250)
	private String istitutoCnr;
 
//    DIPARTIMENTO_CNR VARCHAR(250)
	private String dipartimentoCnr;
 
//    DATA_STIPULA_CONVENZIONE TIMESTAMP(7)
	private java.sql.Timestamp dataStipulaConvenzione;
 
//    NOTE VARCHAR(250)
	private String note;

	private String azienda;

	private String aziendaSettore;
	private String aziendaSede;
	private String nomeInfraEuropea;
	private String descrInfraEuropea;
	private Integer numeroBorseFinanziate;
	private Integer totaleBorseFinanziate;
	private Double importoTotaleBorsa;
	private String nomeDottorato;

	//Dopo aggiornamento con Patrizia aggiunto questi campi 01/10/2021
	private Integer pgBanca;
	private String uoCnr;
	private String stato;
	private java.sql.Timestamp dtSospensione;
	private java.sql.Timestamp dtRiattivazione;
	private java.sql.Timestamp dtInizioDottorato;
	private java.sql.Timestamp dtFineDottorato;
	private java.sql.Timestamp dtProrogaDottorato;
	private java.sql.Timestamp dtRegistrazione;
	private Integer nRateDottorato;
	private String cdCds;
	private String cdUnitaOrganizzativa;
	private Integer esercizio;
	private Double cofinanziamentoCnr;
	private String cofinanziamentoEffettuato;

	//Fine



//    ID_PHDTIPO_DOTTORATI DECIMAL(38,0) NOT NULL
	private Long idPhdtipoDottorati;
 
//    ID_CICLO_DOTTORATI DECIMAL(38,0) NOT NULL
	private Long idCicloDottorati;
 
//    ID_TIPOCORSO_DOTTORATI DECIMAL(38,0) NOT NULL
	private Long idTipocorsoDottorati;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ANAGRAFICA_DOTTORATI
	 **/
	public AnagraficaDottoratiBase() {
		super();
	}
	public AnagraficaDottoratiBase(Long id) {
		super(id);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [regione]
	 **/
	public String getRegione() {
		return regione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [regione]
	 **/
	public void setRegione(String regione)  {
		this.regione=regione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [universitaCapofila]
	 **/
	public String getUniversitaCapofila() {
		return universitaCapofila;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [universitaCapofila]
	 **/
	public void setUniversitaCapofila(String universitaCapofila)  {
		this.universitaCapofila=universitaCapofila;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerzo]
	 *
	 * @return*/
	public Integer getCdTerzo() {
		return cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzo]
	 **/
	public void setCdTerzo(Integer cdTerzo)  {
		this.cdTerzo=cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [telefonoAteneo]
	 **/
	public String getTelefonoAteneo() {
		return telefonoAteneo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [telefonoAteneo]
	 **/
	public void setTelefonoAteneo(String telefonoAteneo)  {
		this.telefonoAteneo=telefonoAteneo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [emailAteneo]
	 **/
	public String getEmailAteneo() {
		return emailAteneo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [emailAteneo]
	 **/
	public void setEmailAteneo(String emailAteneo)  {
		this.emailAteneo=emailAteneo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [altreUniversita]
	 **/
	public String getAltreUniversita() {
		return altreUniversita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [altreUniversita]
	 **/
	public void setAltreUniversita(String altreUniversita)  {
		this.altreUniversita=altreUniversita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dipartimentoUniversita]
	 **/
	public String getDipartimentoUniversita() {
		return dipartimentoUniversita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dipartimentoUniversita]
	 **/
	public void setDipartimentoUniversita(String dipartimentoUniversita)  {
		this.dipartimentoUniversita=dipartimentoUniversita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [corsoDottorato]
	 **/
	public String getCorsoDottorato() {
		return corsoDottorato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [corsoDottorato]
	 **/
	public void setCorsoDottorato(String corsoDottorato)  {
		this.corsoDottorato=corsoDottorato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tematica]
	 **/
	public String getTematica() {
		return tematica;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tematica]
	 **/
	public void setTematica(String tematica)  {
		this.tematica=tematica;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroCicliFinanziati]
	 **/
	public Long getNumeroCicliFinanziati() {
		return numeroCicliFinanziati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroCicliFinanziati]
	 **/
	public void setNumeroCicliFinanziati(Long numeroCicliFinanziati)  {
		this.numeroCicliFinanziati=numeroCicliFinanziati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ricercatore]
	 **/
	public String getRicercatore() {
		return ricercatore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ricercatore]
	 **/
	public void setRicercatore(String ricercatore)  {
		this.ricercatore=ricercatore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [istitutoCnr]
	 **/
	public String getIstitutoCnr() {
		return istitutoCnr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [istitutoCnr]
	 **/
	public void setIstitutoCnr(String istitutoCnr)  {
		this.istitutoCnr=istitutoCnr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dipartimentoCnr]
	 **/
	public String getDipartimentoCnr() {
		return dipartimentoCnr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dipartimentoCnr]
	 **/
	public void setDipartimentoCnr(String dipartimentoCnr)  {
		this.dipartimentoCnr=dipartimentoCnr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataStipulaConvenzione]
	 **/
	public java.sql.Timestamp getDataStipulaConvenzione() {
		return dataStipulaConvenzione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataStipulaConvenzione]
	 **/
	public void setDataStipulaConvenzione(java.sql.Timestamp dataStipulaConvenzione)  {
		this.dataStipulaConvenzione=dataStipulaConvenzione;
	}
	/**
	 * Creato Valerio
	 */
	public String getAzienda() {
		return azienda;
	}

	public void setAzienda(String azienda) {
		this.azienda = azienda;
	}

	public String getAziendaSettore() {
		return aziendaSettore;
	}

	public void setAziendaSettore(String aziendaSettore) {
		this.aziendaSettore = aziendaSettore;
	}

	public String getAziendaSede() {
		return aziendaSede;
	}

	public void setAziendaSede(String aziendaSede) {
		this.aziendaSede = aziendaSede;
	}

	public String getNomeInfraEuropea() {
		return nomeInfraEuropea;
	}

	public void setNomeInfraEuropea(String nomeInfraEuropea) {
		this.nomeInfraEuropea = nomeInfraEuropea;
	}

	public String getDescrInfraEuropea() {
		return descrInfraEuropea;
	}

	public void setDescrInfraEuropea(String descrInfraEuropea) {
		this.descrInfraEuropea = descrInfraEuropea;
	}

	public Integer getNumeroBorseFinanziate() {
		return numeroBorseFinanziate;
	}

	public void setNumeroBorseFinanziate(Integer numeroBorseFinanziate) {
		this.numeroBorseFinanziate = numeroBorseFinanziate;
	}

	public Integer getTotaleBorseFinanziate() {
		return totaleBorseFinanziate;
	}

	public void setTotaleBorseFinanziate(Integer totaleBorseFinanziate) {
		this.totaleBorseFinanziate = totaleBorseFinanziate;
	}

	public Double getImportoTotaleBorsa() {
		return importoTotaleBorsa;
	}

	public void setImportoTotaleBorsa(Double totaleBorsa) {
		this.importoTotaleBorsa = totaleBorsa;
	}

	public String getNomeDottorato() {
		return nomeDottorato;
	}

	public void setNomeDottorato(String nomeDottorato) {
		this.nomeDottorato = nomeDottorato;
	}

	/**
	 * Fine Creato Valerio
	 */

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [note]
	 **/
	public String getNote() {
		return note;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [note]
	 **/
	public void setNote(String note)  {
		this.note=note;
	}



	public Integer getPgBanca() {
		return pgBanca;
	}

	public void setPgBanca(Integer pgBanca) {
		this.pgBanca = pgBanca;
	}

	public String getUoCnr() {
		return uoCnr;
	}

	public void setUoCnr(String uoCnr) {
		this.uoCnr = uoCnr;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Timestamp getDtSospensione() {
		return dtSospensione;
	}

	public void setDtSospensione(Timestamp dtSospensione) {
		this.dtSospensione = dtSospensione;
	}

	public Timestamp getDtRiattivazione() {
		return dtRiattivazione;
	}

	public void setDtRiattivazione(Timestamp dtRiattivazione) {
		this.dtRiattivazione = dtRiattivazione;
	}

	public Timestamp getDtInizioDottorato() {
		return dtInizioDottorato;
	}

	public void setDtInizioDottorato(Timestamp dtInizioDottorato) {
		this.dtInizioDottorato = dtInizioDottorato;
	}

	public Timestamp getDtFineDottorato() {
		return dtFineDottorato;
	}

	public void setDtFineDottorato(Timestamp dtFineDottorato) {
		this.dtFineDottorato = dtFineDottorato;
	}

	public Timestamp getDtProrogaDottorato() {
		return dtProrogaDottorato;
	}

	public void setDtProrogaDottorato(Timestamp dtProrogaDottorato) {
		this.dtProrogaDottorato = dtProrogaDottorato;
	}

	public Timestamp getDtRegistrazione() {
		return dtRegistrazione;
	}

	public void setDtRegistrazione(Timestamp dtRegistrazione) {
		this.dtRegistrazione = dtRegistrazione;
	}

	public Integer getnRateDottorato() {
		return nRateDottorato;
	}

	public void setnRateDottorato(Integer nRateDottorato) {
		this.nRateDottorato = nRateDottorato;
	}

	public String getCdCds() {
		return cdCds;
	}

	public void setCdCds(String cdCds) {
		this.cdCds = cdCds;
	}

	public String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}

	public void setCdUnitaOrganizzativa(String cdUnitaOrganizzativa) {
		this.cdUnitaOrganizzativa = cdUnitaOrganizzativa;
	}

	public Integer getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}

	public Double getCofinanziamentoCnr() {
		return cofinanziamentoCnr;
	}

	public void setCofinanziamentoCnr(Double cofinanziamentoCnr) {
		this.cofinanziamentoCnr = cofinanziamentoCnr;
	}

	public String getCofinanziamentoEffettuato() {
		return cofinanziamentoEffettuato;
	}

	public void setCofinanziamentoEffettuato(String cofinanziamentoEffettuato) {
		this.cofinanziamentoEffettuato = cofinanziamentoEffettuato;
	}


	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Identificativo del tipo dottorati.]
	 **/
	public Long getIdPhdtipoDottorati() {
		return idPhdtipoDottorati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Identificativo del tipo dottorati.]
	 **/
	public void setIdPhdtipoDottorati(Long idPhdtipoDottorati)  {
		this.idPhdtipoDottorati=idPhdtipoDottorati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Identificativo del ciclo dottorati.]
	 **/
	public Long getIdCicloDottorati() {
		return idCicloDottorati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Identificativo del ciclo dottorati.]
	 **/
	public void setIdCicloDottorati(Long idCicloDottorati)  {
		this.idCicloDottorati=idCicloDottorati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Identificativo del tipocorso_dottorati.]
	 **/
	public Long getIdTipocorsoDottorati() {
		return idTipocorsoDottorati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Identificativo del tipocorso_dottorati.]
	 **/
	public void setIdTipocorsoDottorati(Long idTipocorsoDottorati)  {
		this.idTipocorsoDottorati=idTipocorsoDottorati;
	}


	/**
	 * Non c'è in table da vedere se lasciarlo
	 */
	private String statoAssCompenso;
	public String getStatoAssCompenso() {
		return statoAssCompenso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [statoAssCompenso]
	 **/
	public void setStatoAssCompenso(String statoAssCompenso)  {
		this.statoAssCompenso=statoAssCompenso;
	}
}