/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
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
 * Date 02/07/2014
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;
public class VIncarichiAssRicBorseStBase extends VIncarichiAssRicBorseStKey implements Keyed {
//    CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cdCds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cdUnitaOrganizzativa;
 
//    STATO VARCHAR(2) NOT NULL
	private java.lang.String stato;
 
//    ESERCIZIO_PROCEDURA DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizioProcedura;
 
//    PG_PROCEDURA DECIMAL(10,0) NOT NULL
	private java.lang.Long pgProcedura;
 
//    CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cdTerzo;
 
//    COGNOME VARCHAR(50)
	private java.lang.String cognome;
 
//    NOME VARCHAR(50)
	private java.lang.String nome;
 
//    CODICE_FISCALE VARCHAR(20)
	private java.lang.String codiceFiscale;
 
//    DT_REGISTRAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtRegistrazione;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
//    DT_STIPULA TIMESTAMP(7)
	private java.sql.Timestamp dtStipula;
 
//    DT_INIZIO_VALIDITA TIMESTAMP(7)
	private java.sql.Timestamp dtInizioValidita;
 
//    DT_FINE_VALIDITA TIMESTAMP(7)
	private java.sql.Timestamp dtFineValidita;
 
//    DT_PROROGA TIMESTAMP(7)
	private java.sql.Timestamp dtProroga;
 
//    DT_PROROGA_PAGAM TIMESTAMP(7)
	private java.sql.Timestamp dtProrogaPagam;
 
//    TI_ISTITUZ_COMMERC CHAR(1)
	private java.lang.String tiIstituzCommerc;
 
//    CD_TIPO_RAPPORTO VARCHAR(10)
	private java.lang.String cdTipoRapporto;
 
//    CD_TRATTAMENTO VARCHAR(10)
	private java.lang.String cdTrattamento;
 
//    FL_PUBBLICA_CONTRATTO CHAR(1) NOT NULL
	private java.lang.Boolean flPubblicaContratto;
 
//    IMPORTO_LORDO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importoLordo;
 
//    IMPORTO_COMPLESSIVO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importoComplessivo;
 
//    FL_INVIATO_CORTE_CONTI CHAR(1) NOT NULL
	private java.lang.Boolean flInviatoCorteConti;
 
//    DT_INVIO_CORTE_CONTI TIMESTAMP(7)
	private java.sql.Timestamp dtInvioCorteConti;
 
//    ESITO_CORTE_CONTI VARCHAR(3)
	private java.lang.String esitoCorteConti;
 
//    CD_PROVV VARCHAR(20)
	private java.lang.String cdProvv;
 
//    NR_PROVV DECIMAL(10,0)
	private java.lang.Long nrProvv;
 
//    DT_PROVV TIMESTAMP(7)
	private java.sql.Timestamp dtProvv;
 
//    DT_EFFICACIA TIMESTAMP(7)
	private java.sql.Timestamp dtEfficacia;
 
//    OGGETTO VARCHAR(2000) NOT NULL
	private java.lang.String oggetto;
 
//    TIPO_NATURA VARCHAR(3) NOT NULL
	private java.lang.String tipoNatura;
 
//    TIPO_ASSOCIAZIONE VARCHAR(3) NOT NULL
	private java.lang.String tipoAssociazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_INCARICHI_ELENCO_CONS
	 **/
	public VIncarichiAssRicBorseStBase() {
		super();
	}
	public VIncarichiAssRicBorseStBase(java.lang.Integer esercizio, java.lang.Long pgRepertorio) {
		super(esercizio, pgRepertorio);
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
	 * Restituisce il valore di: [esercizioProcedura]
	 **/
	public java.lang.Integer getEsercizioProcedura() {
		return esercizioProcedura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioProcedura]
	 **/
	public void setEsercizioProcedura(java.lang.Integer esercizioProcedura)  {
		this.esercizioProcedura=esercizioProcedura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgProcedura]
	 **/
	public java.lang.Long getPgProcedura() {
		return pgProcedura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgProcedura]
	 **/
	public void setPgProcedura(java.lang.Long pgProcedura)  {
		this.pgProcedura=pgProcedura;
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
	 * Restituisce il valore di: [dtRegistrazione]
	 **/
	public java.sql.Timestamp getDtRegistrazione() {
		return dtRegistrazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtRegistrazione]
	 **/
	public void setDtRegistrazione(java.sql.Timestamp dtRegistrazione)  {
		this.dtRegistrazione=dtRegistrazione;
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
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtStipula]
	 **/
	public java.sql.Timestamp getDtStipula() {
		return dtStipula;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtStipula]
	 **/
	public void setDtStipula(java.sql.Timestamp dtStipula)  {
		this.dtStipula=dtStipula;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtInizioValidita]
	 **/
	public java.sql.Timestamp getDtInizioValidita() {
		return dtInizioValidita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtInizioValidita]
	 **/
	public void setDtInizioValidita(java.sql.Timestamp dtInizioValidita)  {
		this.dtInizioValidita=dtInizioValidita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtFineValidita]
	 **/
	public java.sql.Timestamp getDtFineValidita() {
		return dtFineValidita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtFineValidita]
	 **/
	public void setDtFineValidita(java.sql.Timestamp dtFineValidita)  {
		this.dtFineValidita=dtFineValidita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtProroga]
	 **/
	public java.sql.Timestamp getDtProroga() {
		return dtProroga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtProroga]
	 **/
	public void setDtProroga(java.sql.Timestamp dtProroga)  {
		this.dtProroga=dtProroga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtProrogaPagam]
	 **/
	public java.sql.Timestamp getDtProrogaPagam() {
		return dtProrogaPagam;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtProrogaPagam]
	 **/
	public void setDtProrogaPagam(java.sql.Timestamp dtProrogaPagam)  {
		this.dtProrogaPagam=dtProrogaPagam;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiIstituzCommerc]
	 **/
	public java.lang.String getTiIstituzCommerc() {
		return tiIstituzCommerc;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiIstituzCommerc]
	 **/
	public void setTiIstituzCommerc(java.lang.String tiIstituzCommerc)  {
		this.tiIstituzCommerc=tiIstituzCommerc;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoRapporto]
	 **/
	public java.lang.String getCdTipoRapporto() {
		return cdTipoRapporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoRapporto]
	 **/
	public void setCdTipoRapporto(java.lang.String cdTipoRapporto)  {
		this.cdTipoRapporto=cdTipoRapporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTrattamento]
	 **/
	public java.lang.String getCdTrattamento() {
		return cdTrattamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTrattamento]
	 **/
	public void setCdTrattamento(java.lang.String cdTrattamento)  {
		this.cdTrattamento=cdTrattamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flPubblicaContratto]
	 **/
	public java.lang.Boolean getFlPubblicaContratto() {
		return flPubblicaContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flPubblicaContratto]
	 **/
	public void setFlPubblicaContratto(java.lang.Boolean flPubblicaContratto)  {
		this.flPubblicaContratto=flPubblicaContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoLordo]
	 **/
	public java.math.BigDecimal getImportoLordo() {
		return importoLordo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoLordo]
	 **/
	public void setImportoLordo(java.math.BigDecimal importoLordo)  {
		this.importoLordo=importoLordo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoComplessivo]
	 **/
	public java.math.BigDecimal getImportoComplessivo() {
		return importoComplessivo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoComplessivo]
	 **/
	public void setImportoComplessivo(java.math.BigDecimal importoComplessivo)  {
		this.importoComplessivo=importoComplessivo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flInviatoCorteConti]
	 **/
	public java.lang.Boolean getFlInviatoCorteConti() {
		return flInviatoCorteConti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flInviatoCorteConti]
	 **/
	public void setFlInviatoCorteConti(java.lang.Boolean flInviatoCorteConti)  {
		this.flInviatoCorteConti=flInviatoCorteConti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtInvioCorteConti]
	 **/
	public java.sql.Timestamp getDtInvioCorteConti() {
		return dtInvioCorteConti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtInvioCorteConti]
	 **/
	public void setDtInvioCorteConti(java.sql.Timestamp dtInvioCorteConti)  {
		this.dtInvioCorteConti=dtInvioCorteConti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esitoCorteConti]
	 **/
	public java.lang.String getEsitoCorteConti() {
		return esitoCorteConti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esitoCorteConti]
	 **/
	public void setEsitoCorteConti(java.lang.String esitoCorteConti)  {
		this.esitoCorteConti=esitoCorteConti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdProvv]
	 **/
	public java.lang.String getCdProvv() {
		return cdProvv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdProvv]
	 **/
	public void setCdProvv(java.lang.String cdProvv)  {
		this.cdProvv=cdProvv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nrProvv]
	 **/
	public java.lang.Long getNrProvv() {
		return nrProvv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nrProvv]
	 **/
	public void setNrProvv(java.lang.Long nrProvv)  {
		this.nrProvv=nrProvv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtProvv]
	 **/
	public java.sql.Timestamp getDtProvv() {
		return dtProvv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtProvv]
	 **/
	public void setDtProvv(java.sql.Timestamp dtProvv)  {
		this.dtProvv=dtProvv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtEfficacia]
	 **/
	public java.sql.Timestamp getDtEfficacia() {
		return dtEfficacia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtEfficacia]
	 **/
	public void setDtEfficacia(java.sql.Timestamp dtEfficacia)  {
		this.dtEfficacia=dtEfficacia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [oggetto]
	 **/
	public java.lang.String getOggetto() {
		return oggetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [oggetto]
	 **/
	public void setOggetto(java.lang.String oggetto)  {
		this.oggetto=oggetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoNatura]
	 **/
	public java.lang.String getTipoNatura() {
		return tipoNatura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoNatura]
	 **/
	public void setTipoNatura(java.lang.String tipoNatura)  {
		this.tipoNatura=tipoNatura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoAssociazione]
	 **/
	public java.lang.String getTipoAssociazione() {
		return tipoAssociazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoAssociazione]
	 **/
	public void setTipoAssociazione(java.lang.String tipoAssociazione)  {
		this.tipoAssociazione=tipoAssociazione;
	}
}