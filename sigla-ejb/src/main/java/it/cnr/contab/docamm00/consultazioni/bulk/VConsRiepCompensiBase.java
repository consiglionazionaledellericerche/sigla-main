/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 09/09/2016
 */
package it.cnr.contab.docamm00.consultazioni.bulk;
import it.cnr.jada.persistency.Keyed;
public class VConsRiepCompensiBase extends VConsRiepCompensiKey implements Keyed {
//    CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cdCds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cdUnitaOrganizzativa;
 
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    PG_COMPENSO DECIMAL(10,0) NOT NULL
	private java.lang.Long pgCompenso;
 
//    CODICE_FISCALE VARCHAR(20)
	private java.lang.String codiceFiscale;
 
//    CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cdTerzo;
 
//    COGNOME VARCHAR(50)
	private java.lang.String cognome;
 
//    NOME VARCHAR(50)
	private java.lang.String nome;
 
//    DT_DA_COMPETENZA_COGE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtDaCompetenzaCoge;
 
//    DT_A_COMPETENZA_COGE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtACompetenzaCoge;
 
//    DT_TRASMISSIONE TIMESTAMP(7)
	private java.sql.Timestamp dtTrasmissione;
 
//    DT_PAGAMENTO TIMESTAMP(7)
	private java.sql.Timestamp dtPagamento;
 
//    IM_LORDO DECIMAL(22,0)
	private java.lang.Long imLordo;
 
//    IM_FISCALE DECIMAL(22,0)
	private java.lang.Long imFiscale;
 
//    IRAP_ENTE DECIMAL(22,0)
	private java.lang.Long irapEnte;
 
//    INPS_ENTE DECIMAL(22,0)
	private java.lang.Long inpsEnte;
 
//    INAIL_ENTE DECIMAL(22,0)
	private java.lang.Long inailEnte;
 
//    IRPEF DECIMAL(22,0)
	private java.lang.Long irpef;
 
//    BONUSDL66 DECIMAL(22,0)
	private java.lang.Long bonusdl66;
 
//    INPS_PERCIPIENTE DECIMAL(22,0)
	private java.lang.Long inpsPercipiente;
 
//    INAIL_PERCIPIENTE DECIMAL(22,0)
	private java.lang.Long inailPercipiente;
 
//    ADD_REG DECIMAL(22,0)
	private java.lang.Long addReg;
 
//    ADD_COM DECIMAL(22,0)
	private java.lang.Long addCom;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_CONS_RIEP_COMPENSI
	 **/
	public VConsRiepCompensiBase() {
		super();
	}
	public VConsRiepCompensiBase() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		return cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.cdCds=cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOrganizzativa]
	 **/
	public java.lang.String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOrganizzativa]
	 **/
	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa)  {
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgCompenso]
	 **/
	public java.lang.Long getPgCompenso() {
		return pgCompenso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgCompenso]
	 **/
	public void setPgCompenso(java.lang.Long pgCompenso)  {
		this.pgCompenso=pgCompenso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceFiscale]
	 **/
	public java.lang.String getCodiceFiscale() {
		return codiceFiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceFiscale]
	 **/
	public void setCodiceFiscale(java.lang.String codiceFiscale)  {
		this.codiceFiscale=codiceFiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerzo]
	 **/
	public java.lang.Integer getCdTerzo() {
		return cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzo]
	 **/
	public void setCdTerzo(java.lang.Integer cdTerzo)  {
		this.cdTerzo=cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cognome]
	 **/
	public java.lang.String getCognome() {
		return cognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cognome]
	 **/
	public void setCognome(java.lang.String cognome)  {
		this.cognome=cognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nome]
	 **/
	public java.lang.String getNome() {
		return nome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nome]
	 **/
	public void setNome(java.lang.String nome)  {
		this.nome=nome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtDaCompetenzaCoge]
	 **/
	public java.sql.Timestamp getDtDaCompetenzaCoge() {
		return dtDaCompetenzaCoge;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtDaCompetenzaCoge]
	 **/
	public void setDtDaCompetenzaCoge(java.sql.Timestamp dtDaCompetenzaCoge)  {
		this.dtDaCompetenzaCoge=dtDaCompetenzaCoge;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtACompetenzaCoge]
	 **/
	public java.sql.Timestamp getDtACompetenzaCoge() {
		return dtACompetenzaCoge;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtACompetenzaCoge]
	 **/
	public void setDtACompetenzaCoge(java.sql.Timestamp dtACompetenzaCoge)  {
		this.dtACompetenzaCoge=dtACompetenzaCoge;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtTrasmissione]
	 **/
	public java.sql.Timestamp getDtTrasmissione() {
		return dtTrasmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtTrasmissione]
	 **/
	public void setDtTrasmissione(java.sql.Timestamp dtTrasmissione)  {
		this.dtTrasmissione=dtTrasmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtPagamento]
	 **/
	public java.sql.Timestamp getDtPagamento() {
		return dtPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtPagamento]
	 **/
	public void setDtPagamento(java.sql.Timestamp dtPagamento)  {
		this.dtPagamento=dtPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imLordo]
	 **/
	public java.lang.Long getImLordo() {
		return imLordo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imLordo]
	 **/
	public void setImLordo(java.lang.Long imLordo)  {
		this.imLordo=imLordo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imFiscale]
	 **/
	public java.lang.Long getImFiscale() {
		return imFiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imFiscale]
	 **/
	public void setImFiscale(java.lang.Long imFiscale)  {
		this.imFiscale=imFiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [irapEnte]
	 **/
	public java.lang.Long getIrapEnte() {
		return irapEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [irapEnte]
	 **/
	public void setIrapEnte(java.lang.Long irapEnte)  {
		this.irapEnte=irapEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [inpsEnte]
	 **/
	public java.lang.Long getInpsEnte() {
		return inpsEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [inpsEnte]
	 **/
	public void setInpsEnte(java.lang.Long inpsEnte)  {
		this.inpsEnte=inpsEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [inailEnte]
	 **/
	public java.lang.Long getInailEnte() {
		return inailEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [inailEnte]
	 **/
	public void setInailEnte(java.lang.Long inailEnte)  {
		this.inailEnte=inailEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [irpef]
	 **/
	public java.lang.Long getIrpef() {
		return irpef;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [irpef]
	 **/
	public void setIrpef(java.lang.Long irpef)  {
		this.irpef=irpef;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [bonusdl66]
	 **/
	public java.lang.Long getBonusdl66() {
		return bonusdl66;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [bonusdl66]
	 **/
	public void setBonusdl66(java.lang.Long bonusdl66)  {
		this.bonusdl66=bonusdl66;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [inpsPercipiente]
	 **/
	public java.lang.Long getInpsPercipiente() {
		return inpsPercipiente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [inpsPercipiente]
	 **/
	public void setInpsPercipiente(java.lang.Long inpsPercipiente)  {
		this.inpsPercipiente=inpsPercipiente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [inailPercipiente]
	 **/
	public java.lang.Long getInailPercipiente() {
		return inailPercipiente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [inailPercipiente]
	 **/
	public void setInailPercipiente(java.lang.Long inailPercipiente)  {
		this.inailPercipiente=inailPercipiente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [addReg]
	 **/
	public java.lang.Long getAddReg() {
		return addReg;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [addReg]
	 **/
	public void setAddReg(java.lang.Long addReg)  {
		this.addReg=addReg;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [addCom]
	 **/
	public java.lang.Long getAddCom() {
		return addCom;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [addCom]
	 **/
	public void setAddCom(java.lang.Long addCom)  {
		this.addCom=addCom;
	}
}