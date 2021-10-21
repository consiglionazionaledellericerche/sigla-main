/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import it.cnr.jada.persistency.Keyed;
public class OrdineAcqBase extends OrdineAcqKey implements Keyed {
//    DATA_ORDINE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dataOrdine;
 
//    CD_DIVISA VARCHAR(10) NOT NULL
	private java.lang.String cdDivisa;
 
//    CAMBIO DECIMAL(15,4) NOT NULL
	private java.math.BigDecimal cambio;
 
//    CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cdTerzo;
 
//    RAGIONE_SOCIALE VARCHAR(100)
	private java.lang.String ragioneSociale;
 
//    NOME VARCHAR(50)
	private java.lang.String nome;
 
//    COGNOME VARCHAR(50)
	private java.lang.String cognome;
 
//    CODICE_FISCALE VARCHAR(20)
	private java.lang.String codiceFiscale;
 
//    PARTITA_IVA VARCHAR(20)
	private java.lang.String partitaIva;
 
//    CD_TERMINI_PAG VARCHAR(10)
	private java.lang.String cdTerminiPag;
 
//    PG_BANCA DECIMAL(10,0) NOT NULL
	private java.lang.Long pgBanca;
 
//    CD_MODALITA_PAG VARCHAR(10) NOT NULL
	private java.lang.String cdModalitaPag;
 
//    NOTA VARCHAR(2000)
	private java.lang.String nota;
 
//    CD_NOTA_PRECODIFICATA VARCHAR(3)
	private java.lang.String cdNotaPrecodificata;
 
//  CD_CDS_NOTA_PREC VARCHAR(30)
	private java.lang.String cdCdsNotaPrec;

//    ESERCIZIO_CONTRATTO DECIMAL(4,0)
	private java.lang.Integer esercizioContratto;
 
//    STATO_CONTRATTO CHAR(1)
	private java.lang.String statoContratto;
 
//    PG_CONTRATTO DECIMAL(10,0)
	private java.lang.Long pgContratto;
 
//    STATO VARCHAR(3) NOT NULL
	private java.lang.String stato;
 
//    DATA_ORDINE_DEF TIMESTAMP(7)
	private java.sql.Timestamp dataOrdineDef;
 
//    RESPONSABILE_PROC DECIMAL(8,0)
	private java.lang.Integer responsabileProc;
 
//    FIRMATARIO DECIMAL(8,0)
	private java.lang.Integer firmatario;
 
//    DIRETTORE DECIMAL(8,0)
	private java.lang.Integer direttore;
 
//    CDR DECIMAL(8,0)
	private java.lang.Integer cdr;
 
//    REFERENTE_ESTERNO VARCHAR(100)
	private java.lang.String referenteEsterno;
 
//    CD_TIPO_ORDINE VARCHAR(5)
	private java.lang.String cdTipoOrdine;
 
//    CD_PROC_AMM VARCHAR(5)
	private java.lang.String cdProcAmm;
 
//    CD_CIG VARCHAR(10)
	private java.lang.String cdCig;
 
//    CD_CUP VARCHAR(15)
	private java.lang.String cdCup;
 
//  CD_CUP VARCHAR(15)
	private java.lang.String cdUopOrdine;

//  SCONTO1 DECIMAL(5,2)
	private java.math.BigDecimal percProrata;

// TI_ISTITUZ_COMMERC CHAR(1) NOT NULL
	private java.lang.String tiAttivita;

	private java.math.BigDecimal imImponibile;

	public Boolean getFl_mepa() {
		return fl_mepa;
	}

	public void setFl_mepa(Boolean fl_mepa) {
		this.fl_mepa = fl_mepa;
	}

	private java.math.BigDecimal imIva;
	private java.math.BigDecimal imIvaD;
	private java.math.BigDecimal imTotaleOrdine;
	private java.lang.Boolean fl_mepa;

//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ORDINE_ACQ
	 **/
	public OrdineAcqBase() {
		super();
	}
	public OrdineAcqBase(java.lang.String cdCds, java.lang.String cdUnitaOperativa, java.lang.Integer esercizio, java.lang.String cdNumeratore, java.lang.Integer numero) {
		super(cdCds, cdUnitaOperativa, esercizio, cdNumeratore, numero);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataOrdine]
	 **/
	public java.sql.Timestamp getDataOrdine() {
		return dataOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataOrdine]
	 **/
	public void setDataOrdine(java.sql.Timestamp dataOrdine)  {
		this.dataOrdine=dataOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdDivisa]
	 **/
	public java.lang.String getCdDivisa() {
		return cdDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdDivisa]
	 **/
	public void setCdDivisa(java.lang.String cdDivisa)  {
		this.cdDivisa=cdDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cambio]
	 **/
	public java.math.BigDecimal getCambio() {
		return cambio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cambio]
	 **/
	public void setCambio(java.math.BigDecimal cambio)  {
		this.cambio=cambio;
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
	 * Restituisce il valore di: [ragioneSociale]
	 **/
	public java.lang.String getRagioneSociale() {
		return ragioneSociale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ragioneSociale]
	 **/
	public void setRagioneSociale(java.lang.String ragioneSociale)  {
		this.ragioneSociale=ragioneSociale;
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
	 * Restituisce il valore di: [partitaIva]
	 **/
	public java.lang.String getPartitaIva() {
		return partitaIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [partitaIva]
	 **/
	public void setPartitaIva(java.lang.String partitaIva)  {
		this.partitaIva=partitaIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerminiPag]
	 **/
	public java.lang.String getCdTerminiPag() {
		return cdTerminiPag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerminiPag]
	 **/
	public void setCdTerminiPag(java.lang.String cdTerminiPag)  {
		this.cdTerminiPag=cdTerminiPag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgBanca]
	 **/
	public java.lang.Long getPgBanca() {
		return pgBanca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgBanca]
	 **/
	public void setPgBanca(java.lang.Long pgBanca)  {
		this.pgBanca=pgBanca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdModalitaPag]
	 **/
	public java.lang.String getCdModalitaPag() {
		return cdModalitaPag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdModalitaPag]
	 **/
	public void setCdModalitaPag(java.lang.String cdModalitaPag)  {
		this.cdModalitaPag=cdModalitaPag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nota]
	 **/
	public java.lang.String getNota() {
		return nota;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nota]
	 **/
	public void setNota(java.lang.String nota)  {
		this.nota=nota;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNotaPrecodificata]
	 **/
	public java.lang.String getCdNotaPrecodificata() {
		return cdNotaPrecodificata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNotaPrecodificata]
	 **/
	public void setCdNotaPrecodificata(java.lang.String cdNotaPrecodificata)  {
		this.cdNotaPrecodificata=cdNotaPrecodificata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioContratto]
	 **/
	public java.lang.Integer getEsercizioContratto() {
		return esercizioContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioContratto]
	 **/
	public void setEsercizioContratto(java.lang.Integer esercizioContratto)  {
		this.esercizioContratto=esercizioContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [statoContratto]
	 **/
	public java.lang.String getStatoContratto() {
		return statoContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [statoContratto]
	 **/
	public void setStatoContratto(java.lang.String statoContratto)  {
		this.statoContratto=statoContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgContratto]
	 **/
	public java.lang.Long getPgContratto() {
		return pgContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgContratto]
	 **/
	public void setPgContratto(java.lang.Long pgContratto)  {
		this.pgContratto=pgContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stato]
	 **/
	public java.lang.String getStato() {
		return stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stato]
	 **/
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataOrdineDef]
	 **/
	public java.sql.Timestamp getDataOrdineDef() {
		return dataOrdineDef;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataOrdineDef]
	 **/
	public void setDataOrdineDef(java.sql.Timestamp dataOrdineDef)  {
		this.dataOrdineDef=dataOrdineDef;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [responsabileProc]
	 **/
	public java.lang.Integer getResponsabileProc() {
		return responsabileProc;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [responsabileProc]
	 **/
	public void setResponsabileProc(java.lang.Integer responsabileProc)  {
		this.responsabileProc=responsabileProc;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [firmatario]
	 **/
	public java.lang.Integer getFirmatario() {
		return firmatario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [firmatario]
	 **/
	public void setFirmatario(java.lang.Integer firmatario)  {
		this.firmatario=firmatario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [direttore]
	 **/
	public java.lang.Integer getDirettore() {
		return direttore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [direttore]
	 **/
	public void setDirettore(java.lang.Integer direttore)  {
		this.direttore=direttore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdr]
	 **/
	public java.lang.Integer getCdr() {
		return cdr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdr]
	 **/
	public void setCdr(java.lang.Integer cdr)  {
		this.cdr=cdr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [referenteEsterno]
	 **/
	public java.lang.String getReferenteEsterno() {
		return referenteEsterno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [referenteEsterno]
	 **/
	public void setReferenteEsterno(java.lang.String referenteEsterno)  {
		this.referenteEsterno=referenteEsterno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoOrdine]
	 **/
	public java.lang.String getCdTipoOrdine() {
		return cdTipoOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoOrdine]
	 **/
	public void setCdTipoOrdine(java.lang.String cdTipoOrdine)  {
		this.cdTipoOrdine=cdTipoOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdProcAmm]
	 **/
	public java.lang.String getCdProcAmm() {
		return cdProcAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdProcAmm]
	 **/
	public void setCdProcAmm(java.lang.String cdProcAmm)  {
		this.cdProcAmm=cdProcAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCig]
	 **/
	public java.lang.String getCdCig() {
		return cdCig;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCig]
	 **/
	public void setCdCig(java.lang.String cdCig)  {
		this.cdCig=cdCig;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCup]
	 **/
	public java.lang.String getCdCup() {
		return cdCup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCup]
	 **/
	public void setCdCup(java.lang.String cdCup)  {
		this.cdCup=cdCup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtCancellazione]
	 **/
	public java.sql.Timestamp getDtCancellazione() {
		return dtCancellazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtCancellazione]
	 **/
	public void setDtCancellazione(java.sql.Timestamp dtCancellazione)  {
		this.dtCancellazione=dtCancellazione;
	}
	public java.lang.String getCdCdsNotaPrec() {
		return cdCdsNotaPrec;
	}
	public void setCdCdsNotaPrec(java.lang.String cdCdsNotaPrec) {
		this.cdCdsNotaPrec = cdCdsNotaPrec;
	}
	public java.math.BigDecimal getPercProrata() {
		return percProrata;
	}
	public void setPercProrata(java.math.BigDecimal percProrata) {
		this.percProrata = percProrata;
	}
	public java.lang.String getTiAttivita() {
		return tiAttivita;
	}
	public void setTiAttivita(java.lang.String tiAttivita) {
		this.tiAttivita = tiAttivita;
	}
	public java.math.BigDecimal getImImponibile() {
		return imImponibile;
	}
	public void setImImponibile(java.math.BigDecimal imImponibile) {
		this.imImponibile = imImponibile;
	}
	public java.math.BigDecimal getImIva() {
		return imIva;
	}
	public void setImIva(java.math.BigDecimal imIva) {
		this.imIva = imIva;
	}
	public java.math.BigDecimal getImIvaD() {
		return imIvaD;
	}
	public void setImIvaD(java.math.BigDecimal imIvaD) {
		this.imIvaD = imIvaD;
	}
	public java.math.BigDecimal getImTotaleOrdine() {
		return imTotaleOrdine;
	}
	public void setImTotaleOrdine(java.math.BigDecimal imTotaleOrdine) {
		this.imTotaleOrdine = imTotaleOrdine;
	}
	public java.lang.String getCdUopOrdine() {
		return cdUopOrdine;
	}
	public void setCdUopOrdine(java.lang.String cdUopOrdine) {
		this.cdUopOrdine = cdUopOrdine;
	}
}