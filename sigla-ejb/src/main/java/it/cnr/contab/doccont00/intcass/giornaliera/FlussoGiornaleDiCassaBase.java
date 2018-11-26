/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/10/2018
 */
package it.cnr.contab.doccont00.intcass.giornaliera;
import it.cnr.jada.persistency.Keyed;
public class FlussoGiornaleDiCassaBase extends FlussoGiornaleDiCassaKey implements Keyed {
//    CODICE_ABI_BT DECIMAL(10,0)
	private java.lang.Long codiceAbiBt;
 
//    DATA_ORA_CREAZIONE_FLUSSO TIMESTAMP(7)
	private java.sql.Timestamp dataOraCreazioneFlusso;
 
//    DATA_INIZIO_PERIODO_RIF TIMESTAMP(7)
	private java.sql.Timestamp dataInizioPeriodoRif;
 
//    DATA_FINE_PERIODO_RIF TIMESTAMP(7)
	private java.sql.Timestamp dataFinePeriodoRif;
 
//    CODICE_ENTE VARCHAR(30)
	private java.lang.String codiceEnte;
 
//    DESCRIZIONE_ENTE VARCHAR(100)
	private java.lang.String descrizioneEnte;
 
//    CODICE_ENTE_BT VARCHAR(8)
	private java.lang.String codiceEnteBt;
 
//    RIFERIMENTO_ENTE VARCHAR(20)
	private java.lang.String riferimentoEnte;
 
//    SALDO_COMPLESSIVO_PREC DECIMAL(15,2)
	private java.math.BigDecimal saldoComplessivoPrec;
 
//    TOTALE_COMPLESSIVO_ENTRATE DECIMAL(15,2)
	private java.math.BigDecimal totaleComplessivoEntrate;
 
//    TOTALE_COMPLESSIVO_USCITE DECIMAL(15,2)
	private java.math.BigDecimal totaleComplessivoUscite;
 
//    SALDO_COMPLESSIVO_FINALE DECIMAL(15,2)
	private java.math.BigDecimal saldoComplessivoFinale;
 
//    FONDO_DI_CASSA DECIMAL(15,2)
	private java.math.BigDecimal fondoDiCassa;
 
//    TOTALE_REVERSALI_RISCOSSE DECIMAL(15,2)
	private java.math.BigDecimal totaleReversaliRiscosse;
 
//    TOTALE_SOSPESI_ENTRATA DECIMAL(15,2)
	private java.math.BigDecimal totaleSospesiEntrata;
 
//    TOTALE_ENTRATE DECIMAL(15,2)
	private java.math.BigDecimal totaleEntrate;
 
//    DEFICIT_DI_CASSA DECIMAL(15,2)
	private java.math.BigDecimal deficitDiCassa;
 
//    TOTALE_MANDATI_PAGATI DECIMAL(15,2)
	private java.math.BigDecimal totaleMandatiPagati;
 
//    TOTALE_SOSPESI_USCITA DECIMAL(15,2)
	private java.math.BigDecimal totaleSospesiUscita;
 
//    TOTALE_USCITE DECIMAL(15,2)
	private java.math.BigDecimal totaleUscite;
 
//    SALDO_ESERCIZIO DECIMAL(15,2)
	private java.math.BigDecimal saldoEsercizio;
 
//    SALDO_CONTI_CORRENTI DECIMAL(15,2)
	private java.math.BigDecimal saldoContiCorrenti;
 
//    SALDO_CONTI_BI DECIMAL(15,2)
	private java.math.BigDecimal saldoContiBi;
 
//    TOTALE_CONTI DECIMAL(15,2)
	private java.math.BigDecimal totaleConti;
 
//    VINCOLI_CONTI_CORRENTI DECIMAL(15,2)
	private java.math.BigDecimal vincoliContiCorrenti;
 
//    VINCOLI_CONTI_BI DECIMAL(15,2)
	private java.math.BigDecimal vincoliContiBi;
 
//    TOTALE_VINCOLI DECIMAL(15,2)
	private java.math.BigDecimal totaleVincoli;
 
//    ANTICIPAZIONE_ACCORDATA DECIMAL(15,2)
	private java.math.BigDecimal anticipazioneAccordata;
 
//    ANTICIPAZIONE_UTILIZZATA DECIMAL(15,2)
	private java.math.BigDecimal anticipazioneUtilizzata;
 
//    DISPONIBILITA DECIMAL(15,2)
	private java.math.BigDecimal disponibilita;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: FLUSSO_GIORNALE_DI_CASSA
	 **/
	public FlussoGiornaleDiCassaBase() {
		super();
	}
	public FlussoGiornaleDiCassaBase(java.lang.Integer esercizio, java.lang.String identificativoFlusso) {
		super(esercizio, identificativoFlusso);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceAbiBt]
	 **/
	public java.lang.Long getCodiceAbiBt() {
		return codiceAbiBt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceAbiBt]
	 **/
	public void setCodiceAbiBt(java.lang.Long codiceAbiBt)  {
		this.codiceAbiBt=codiceAbiBt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataOraCreazioneFlusso]
	 **/
	public java.sql.Timestamp getDataOraCreazioneFlusso() {
		return dataOraCreazioneFlusso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataOraCreazioneFlusso]
	 **/
	public void setDataOraCreazioneFlusso(java.sql.Timestamp dataOraCreazioneFlusso)  {
		this.dataOraCreazioneFlusso=dataOraCreazioneFlusso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataInizioPeriodoRif]
	 **/
	public java.sql.Timestamp getDataInizioPeriodoRif() {
		return dataInizioPeriodoRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataInizioPeriodoRif]
	 **/
	public void setDataInizioPeriodoRif(java.sql.Timestamp dataInizioPeriodoRif)  {
		this.dataInizioPeriodoRif=dataInizioPeriodoRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataFinePeriodoRif]
	 **/
	public java.sql.Timestamp getDataFinePeriodoRif() {
		return dataFinePeriodoRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataFinePeriodoRif]
	 **/
	public void setDataFinePeriodoRif(java.sql.Timestamp dataFinePeriodoRif)  {
		this.dataFinePeriodoRif=dataFinePeriodoRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceEnte]
	 **/
	public java.lang.String getCodiceEnte() {
		return codiceEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceEnte]
	 **/
	public void setCodiceEnte(java.lang.String codiceEnte)  {
		this.codiceEnte=codiceEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [descrizioneEnte]
	 **/
	public java.lang.String getDescrizioneEnte() {
		return descrizioneEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [descrizioneEnte]
	 **/
	public void setDescrizioneEnte(java.lang.String descrizioneEnte)  {
		this.descrizioneEnte=descrizioneEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceEnteBt]
	 **/
	public java.lang.String getCodiceEnteBt() {
		return codiceEnteBt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceEnteBt]
	 **/
	public void setCodiceEnteBt(java.lang.String codiceEnteBt)  {
		this.codiceEnteBt=codiceEnteBt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [riferimentoEnte]
	 **/
	public java.lang.String getRiferimentoEnte() {
		return riferimentoEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [riferimentoEnte]
	 **/
	public void setRiferimentoEnte(java.lang.String riferimentoEnte)  {
		this.riferimentoEnte=riferimentoEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [saldoComplessivoPrec]
	 **/
	public java.math.BigDecimal getSaldoComplessivoPrec() {
		return saldoComplessivoPrec;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [saldoComplessivoPrec]
	 **/
	public void setSaldoComplessivoPrec(java.math.BigDecimal saldoComplessivoPrec)  {
		this.saldoComplessivoPrec=saldoComplessivoPrec;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [totaleComplessivoEntrate]
	 **/
	public java.math.BigDecimal getTotaleComplessivoEntrate() {
		return totaleComplessivoEntrate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [totaleComplessivoEntrate]
	 **/
	public void setTotaleComplessivoEntrate(java.math.BigDecimal totaleComplessivoEntrate)  {
		this.totaleComplessivoEntrate=totaleComplessivoEntrate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [totaleComplessivoUscite]
	 **/
	public java.math.BigDecimal getTotaleComplessivoUscite() {
		return totaleComplessivoUscite;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [totaleComplessivoUscite]
	 **/
	public void setTotaleComplessivoUscite(java.math.BigDecimal totaleComplessivoUscite)  {
		this.totaleComplessivoUscite=totaleComplessivoUscite;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [saldoComplessivoFinale]
	 **/
	public java.math.BigDecimal getSaldoComplessivoFinale() {
		return saldoComplessivoFinale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [saldoComplessivoFinale]
	 **/
	public void setSaldoComplessivoFinale(java.math.BigDecimal saldoComplessivoFinale)  {
		this.saldoComplessivoFinale=saldoComplessivoFinale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [fondoDiCassa]
	 **/
	public java.math.BigDecimal getFondoDiCassa() {
		return fondoDiCassa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [fondoDiCassa]
	 **/
	public void setFondoDiCassa(java.math.BigDecimal fondoDiCassa)  {
		this.fondoDiCassa=fondoDiCassa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [totaleReversaliRiscosse]
	 **/
	public java.math.BigDecimal getTotaleReversaliRiscosse() {
		return totaleReversaliRiscosse;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [totaleReversaliRiscosse]
	 **/
	public void setTotaleReversaliRiscosse(java.math.BigDecimal totaleReversaliRiscosse)  {
		this.totaleReversaliRiscosse=totaleReversaliRiscosse;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [totaleSospesiEntrata]
	 **/
	public java.math.BigDecimal getTotaleSospesiEntrata() {
		return totaleSospesiEntrata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [totaleSospesiEntrata]
	 **/
	public void setTotaleSospesiEntrata(java.math.BigDecimal totaleSospesiEntrata)  {
		this.totaleSospesiEntrata=totaleSospesiEntrata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [totaleEntrate]
	 **/
	public java.math.BigDecimal getTotaleEntrate() {
		return totaleEntrate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [totaleEntrate]
	 **/
	public void setTotaleEntrate(java.math.BigDecimal totaleEntrate)  {
		this.totaleEntrate=totaleEntrate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [deficitDiCassa]
	 **/
	public java.math.BigDecimal getDeficitDiCassa() {
		return deficitDiCassa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [deficitDiCassa]
	 **/
	public void setDeficitDiCassa(java.math.BigDecimal deficitDiCassa)  {
		this.deficitDiCassa=deficitDiCassa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [totaleMandatiPagati]
	 **/
	public java.math.BigDecimal getTotaleMandatiPagati() {
		return totaleMandatiPagati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [totaleMandatiPagati]
	 **/
	public void setTotaleMandatiPagati(java.math.BigDecimal totaleMandatiPagati)  {
		this.totaleMandatiPagati=totaleMandatiPagati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [totaleSospesiUscita]
	 **/
	public java.math.BigDecimal getTotaleSospesiUscita() {
		return totaleSospesiUscita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [totaleSospesiUscita]
	 **/
	public void setTotaleSospesiUscita(java.math.BigDecimal totaleSospesiUscita)  {
		this.totaleSospesiUscita=totaleSospesiUscita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [totaleUscite]
	 **/
	public java.math.BigDecimal getTotaleUscite() {
		return totaleUscite;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [totaleUscite]
	 **/
	public void setTotaleUscite(java.math.BigDecimal totaleUscite)  {
		this.totaleUscite=totaleUscite;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [saldoEsercizio]
	 **/
	public java.math.BigDecimal getSaldoEsercizio() {
		return saldoEsercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [saldoEsercizio]
	 **/
	public void setSaldoEsercizio(java.math.BigDecimal saldoEsercizio)  {
		this.saldoEsercizio=saldoEsercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [saldoContiCorrenti]
	 **/
	public java.math.BigDecimal getSaldoContiCorrenti() {
		return saldoContiCorrenti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [saldoContiCorrenti]
	 **/
	public void setSaldoContiCorrenti(java.math.BigDecimal saldoContiCorrenti)  {
		this.saldoContiCorrenti=saldoContiCorrenti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [saldoContiBi]
	 **/
	public java.math.BigDecimal getSaldoContiBi() {
		return saldoContiBi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [saldoContiBi]
	 **/
	public void setSaldoContiBi(java.math.BigDecimal saldoContiBi)  {
		this.saldoContiBi=saldoContiBi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [totaleConti]
	 **/
	public java.math.BigDecimal getTotaleConti() {
		return totaleConti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [totaleConti]
	 **/
	public void setTotaleConti(java.math.BigDecimal totaleConti)  {
		this.totaleConti=totaleConti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [vincoliContiCorrenti]
	 **/
	public java.math.BigDecimal getVincoliContiCorrenti() {
		return vincoliContiCorrenti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [vincoliContiCorrenti]
	 **/
	public void setVincoliContiCorrenti(java.math.BigDecimal vincoliContiCorrenti)  {
		this.vincoliContiCorrenti=vincoliContiCorrenti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [vincoliContiBi]
	 **/
	public java.math.BigDecimal getVincoliContiBi() {
		return vincoliContiBi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [vincoliContiBi]
	 **/
	public void setVincoliContiBi(java.math.BigDecimal vincoliContiBi)  {
		this.vincoliContiBi=vincoliContiBi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [totaleVincoli]
	 **/
	public java.math.BigDecimal getTotaleVincoli() {
		return totaleVincoli;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [totaleVincoli]
	 **/
	public void setTotaleVincoli(java.math.BigDecimal totaleVincoli)  {
		this.totaleVincoli=totaleVincoli;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [anticipazioneAccordata]
	 **/
	public java.math.BigDecimal getAnticipazioneAccordata() {
		return anticipazioneAccordata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [anticipazioneAccordata]
	 **/
	public void setAnticipazioneAccordata(java.math.BigDecimal anticipazioneAccordata)  {
		this.anticipazioneAccordata=anticipazioneAccordata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [anticipazioneUtilizzata]
	 **/
	public java.math.BigDecimal getAnticipazioneUtilizzata() {
		return anticipazioneUtilizzata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [anticipazioneUtilizzata]
	 **/
	public void setAnticipazioneUtilizzata(java.math.BigDecimal anticipazioneUtilizzata)  {
		this.anticipazioneUtilizzata=anticipazioneUtilizzata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [disponibilita]
	 **/
	public java.math.BigDecimal getDisponibilita() {
		return disponibilita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [disponibilita]
	 **/
	public void setDisponibilita(java.math.BigDecimal disponibilita)  {
		this.disponibilita=disponibilita;
	}
}