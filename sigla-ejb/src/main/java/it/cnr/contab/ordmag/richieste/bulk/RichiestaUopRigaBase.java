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
 * Date 12/05/2017
 */
package it.cnr.contab.ordmag.richieste.bulk;
import it.cnr.jada.persistency.Keyed;
public class RichiestaUopRigaBase extends RichiestaUopRigaKey implements Keyed {
//    CD_BENE_SERVIZIO VARCHAR(15)
	private java.lang.String cdBeneServizio;
 
//    DS_BENE_SERVIZIO VARCHAR(300)
	private java.lang.String dsBeneServizio;
 
//    NOTA_RIGA VARCHAR(2000)
	private java.lang.String notaRiga;
 
//    STATO VARCHAR(3)
	private java.lang.String stato;
 
//    CD_CATEGORIA_GRUPPO VARCHAR(10)
	private java.lang.String cdCategoriaGruppo;
 
//    QUANTITA_RICHIESTA DECIMAL(17,5)
	private java.math.BigDecimal quantitaRichiesta;
 
//    CD_UNITA_MISURA VARCHAR(10)
	private java.lang.String cdUnitaMisura;
 
//    COEF_CONV DECIMAL(12,5)
	private java.math.BigDecimal coefConv;
 
//    CD_BENE_SERVIZIO_DEF VARCHAR(15)
	private java.lang.String cdBeneServizioDef;
 
//    QUANTITA_AUTORIZZATA DECIMAL(17,5)
	private java.math.BigDecimal quantitaAutorizzata;
 
//    NOTA_UOP_DEST VARCHAR(2000)
	private java.lang.String notaUopDest;
 
//    CD_CDS_OBBL VARCHAR(30)
	private java.lang.String cdCdsObbl;
 
//    ESERCIZIO_OBBL DECIMAL(4,0)
	private java.lang.Integer esercizioObbl;
 
//    ESERCIZIO_ORIG_OBBL DECIMAL(4,0)
	private java.lang.Integer esercizioOrigObbl;
 
//    PG_OBBLIGAZIONE DECIMAL(10,0)
	private java.lang.Long pgObbligazione;
 
//    CD_CENTRO_RESPONSABILITA VARCHAR(30)
	private java.lang.String cdCentroResponsabilita;
 
//    CD_LINEA_ATTIVITA VARCHAR(10)
	private java.lang.String cdLineaAttivita;
 
//    ESERCIZIO_VOCE DECIMAL(4,0)
	private java.lang.Integer esercizioVoce;
 
//    TI_APPARTENENZA CHAR(1)
	private java.lang.String tiAppartenenza;
 
//    TI_GESTIONE CHAR(1)
	private java.lang.String tiGestione;
 
//    CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cdElementoVoce;
 
//    PG_PROGETTO DECIMAL(10,0)
	private java.lang.Long pgProgetto;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: RICHIESTA_UOP_RIGA
	 **/
	public RichiestaUopRigaBase() {
		super();
	}
	public RichiestaUopRigaBase(java.lang.String cdCds, java.lang.String cdUnitaOperativa, java.lang.Integer esercizio, java.lang.String cdNumeratore, java.lang.Integer numero, java.lang.Integer riga) {
		super(cdCds, cdUnitaOperativa, esercizio, cdNumeratore, numero, riga);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdBeneServizio]
	 **/
	public java.lang.String getCdBeneServizio() {
		return cdBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdBeneServizio]
	 **/
	public void setCdBeneServizio(java.lang.String cdBeneServizio)  {
		this.cdBeneServizio=cdBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsBeneServizio]
	 **/
	public java.lang.String getDsBeneServizio() {
		return dsBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsBeneServizio]
	 **/
	public void setDsBeneServizio(java.lang.String dsBeneServizio)  {
		this.dsBeneServizio=dsBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [notaRiga]
	 **/
	public java.lang.String getNotaRiga() {
		return notaRiga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [notaRiga]
	 **/
	public void setNotaRiga(java.lang.String notaRiga)  {
		this.notaRiga=notaRiga;
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
	 * Restituisce il valore di: [cdCategoriaGruppo]
	 **/
	public java.lang.String getCdCategoriaGruppo() {
		return cdCategoriaGruppo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCategoriaGruppo]
	 **/
	public void setCdCategoriaGruppo(java.lang.String cdCategoriaGruppo)  {
		this.cdCategoriaGruppo=cdCategoriaGruppo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [quantitaRichiesta]
	 **/
	public java.math.BigDecimal getQuantitaRichiesta() {
		return quantitaRichiesta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [quantitaRichiesta]
	 **/
	public void setQuantitaRichiesta(java.math.BigDecimal quantitaRichiesta)  {
		this.quantitaRichiesta=quantitaRichiesta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaMisura]
	 **/
	public java.lang.String getCdUnitaMisura() {
		return cdUnitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaMisura]
	 **/
	public void setCdUnitaMisura(java.lang.String cdUnitaMisura)  {
		this.cdUnitaMisura=cdUnitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [coefConv]
	 **/
	public java.math.BigDecimal getCoefConv() {
		return coefConv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [coefConv]
	 **/
	public void setCoefConv(java.math.BigDecimal coefConv)  {
		this.coefConv=coefConv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdBeneServizioDef]
	 **/
	public java.lang.String getCdBeneServizioDef() {
		return cdBeneServizioDef;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdBeneServizioDef]
	 **/
	public void setCdBeneServizioDef(java.lang.String cdBeneServizioDef)  {
		this.cdBeneServizioDef=cdBeneServizioDef;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [quantitaAutorizzata]
	 **/
	public java.math.BigDecimal getQuantitaAutorizzata() {
		return quantitaAutorizzata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [quantitaAutorizzata]
	 **/
	public void setQuantitaAutorizzata(java.math.BigDecimal quantitaAutorizzata)  {
		this.quantitaAutorizzata=quantitaAutorizzata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [notaUopDest]
	 **/
	public java.lang.String getNotaUopDest() {
		return notaUopDest;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [notaUopDest]
	 **/
	public void setNotaUopDest(java.lang.String notaUopDest)  {
		this.notaUopDest=notaUopDest;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsObbl]
	 **/
	public java.lang.String getCdCdsObbl() {
		return cdCdsObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsObbl]
	 **/
	public void setCdCdsObbl(java.lang.String cdCdsObbl)  {
		this.cdCdsObbl=cdCdsObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioObbl]
	 **/
	public java.lang.Integer getEsercizioObbl() {
		return esercizioObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioObbl]
	 **/
	public void setEsercizioObbl(java.lang.Integer esercizioObbl)  {
		this.esercizioObbl=esercizioObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOrigObbl]
	 **/
	public java.lang.Integer getEsercizioOrigObbl() {
		return esercizioOrigObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOrigObbl]
	 **/
	public void setEsercizioOrigObbl(java.lang.Integer esercizioOrigObbl)  {
		this.esercizioOrigObbl=esercizioOrigObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazione]
	 **/
	public java.lang.Long getPgObbligazione() {
		return pgObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazione]
	 **/
	public void setPgObbligazione(java.lang.Long pgObbligazione)  {
		this.pgObbligazione=pgObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCentroResponsabilita]
	 **/
	public java.lang.String getCdCentroResponsabilita() {
		return cdCentroResponsabilita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCentroResponsabilita]
	 **/
	public void setCdCentroResponsabilita(java.lang.String cdCentroResponsabilita)  {
		this.cdCentroResponsabilita=cdCentroResponsabilita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdLineaAttivita]
	 **/
	public java.lang.String getCdLineaAttivita() {
		return cdLineaAttivita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdLineaAttivita]
	 **/
	public void setCdLineaAttivita(java.lang.String cdLineaAttivita)  {
		this.cdLineaAttivita=cdLineaAttivita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioVoce]
	 **/
	public java.lang.Integer getEsercizioVoce() {
		return esercizioVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioVoce]
	 **/
	public void setEsercizioVoce(java.lang.Integer esercizioVoce)  {
		this.esercizioVoce=esercizioVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiAppartenenza]
	 **/
	public java.lang.String getTiAppartenenza() {
		return tiAppartenenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiAppartenenza]
	 **/
	public void setTiAppartenenza(java.lang.String tiAppartenenza)  {
		this.tiAppartenenza=tiAppartenenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiGestione]
	 **/
	public java.lang.String getTiGestione() {
		return tiGestione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiGestione]
	 **/
	public void setTiGestione(java.lang.String tiGestione)  {
		this.tiGestione=tiGestione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdElementoVoce]
	 **/
	public java.lang.String getCdElementoVoce() {
		return cdElementoVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdElementoVoce]
	 **/
	public void setCdElementoVoce(java.lang.String cdElementoVoce)  {
		this.cdElementoVoce=cdElementoVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgProgetto]
	 **/
	public java.lang.Long getPgProgetto() {
		return pgProgetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgProgetto]
	 **/
	public void setPgProgetto(java.lang.Long pgProgetto)  {
		this.pgProgetto=pgProgetto;
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
}