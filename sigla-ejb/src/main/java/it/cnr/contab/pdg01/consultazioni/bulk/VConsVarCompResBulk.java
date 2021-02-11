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
 * Date 31/03/2014
 */
package it.cnr.contab.pdg01.consultazioni.bulk;

import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;

public class VConsVarCompResBulk extends OggettoBulk implements Persistent {
	private static final long serialVersionUID = 1L;

//  TIPO_VAR VARCHAR(10)
	private java.lang.String tipoVar;

//  ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;

//  NUM_VAR DECIMAL(10,0)
	private java.lang.Long numVar;

//  RIFERIMENTI_DESC_VARIAZIONE VARCHAR(4000)
	private java.lang.String riferimentiDescVariazione;

//  DESC_VARIAZIONE VARCHAR(2000)
	private java.lang.String descVariazione;

//  CDR_PROPONENTE VARCHAR(30)
	private java.lang.String cdrProponente;

//  DESC_CDR_PROPONENTE VARCHAR(300)
	private java.lang.String descCdrProponente;

//  ES_RESIDUO DECIMAL(22,0)
	private java.lang.Long esResiduo;

//  CDR_ASSEGN VARCHAR(30)
	private java.lang.String cdrAssegn;

//  DESC_CDR_ASSEGN VARCHAR(300)
	private java.lang.String descCdrAssegn;

//  DS_TIPO_VARIAZIONE VARCHAR(100)
	private java.lang.String dsTipoVariazione;

//  STATO VARCHAR(3)
	private java.lang.String stato;

//  FONTE VARCHAR(3)
	private java.lang.String fonte;

//  IMPORTO DECIMAL(22,0)
	private java.math.BigDecimal importo;

//  IM_DEC_INT DECIMAL(22,0)
	private java.math.BigDecimal imDecInt;

//  IM_DEC_EST DECIMAL(22,0)
	private java.math.BigDecimal imDecEst;

//  IM_ACC_INT DECIMAL(22,0)
	private java.math.BigDecimal imAccInt;

//  IM_ACC_EST DECIMAL(22,0)
	private java.math.BigDecimal imAccEst;

	private java.math.BigDecimal imEntrata;
//  GAE VARCHAR(10)
	private java.lang.String gae;

//  VOCE_DEL_PIANO VARCHAR(20)
	private java.lang.String voceDelPiano;
	
//  dtApprovazione TIMESTAMP(7)
	private java.sql.Timestamp dtApprovazione;

	// TI_MOTIVAZIONE_VARIAZIONE VARCHAR2(10)
	private java.lang.String tiMotivazioneVariazione;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_CONS_VAR_COMP_RES
	 **/
	public VConsVarCompResBulk() {
		super();
	}
	@Override
	public OggettoBulk initializeForSearch(CRUDBP crudbp,
			ActionContext actioncontext) {
		return inizializzaEsercizio((VConsVarCompResBulk)super.initializeForSearch(crudbp, actioncontext), actioncontext);
	}
	
	@Override
	public OggettoBulk initializeForFreeSearch(CRUDBP crudbp,
			ActionContext actioncontext) {
		return inizializzaEsercizio((VConsVarCompResBulk)super.initializeForFreeSearch(crudbp, actioncontext), actioncontext);
	}
	
	private OggettoBulk inizializzaEsercizio (VConsVarCompResBulk bulk,
			ActionContext actioncontext) {
		bulk.setEsercizio(CNRUserContext.getEsercizio(actioncontext.getUserContext()));
		return bulk;		
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoVar]
	 **/
	public java.lang.String getTipoVar() {
		return tipoVar;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoVar]
	 **/
	public void setTipoVar(java.lang.String tipoVar)  {
		this.tipoVar=tipoVar;
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
	 * Restituisce il valore di: [numVar]
	 **/
	public java.lang.Long getNumVar() {
		return numVar;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numVar]
	 **/
	public void setNumVar(java.lang.Long numVar)  {
		this.numVar=numVar;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [riferimentiDescVariazione]
	 **/
	public java.lang.String getRiferimentiDescVariazione() {
		return riferimentiDescVariazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [riferimentiDescVariazione]
	 **/
	public void setRiferimentiDescVariazione(java.lang.String riferimentiDescVariazione)  {
		this.riferimentiDescVariazione=riferimentiDescVariazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [descVariazione]
	 **/
	public java.lang.String getDescVariazione() {
		return descVariazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [descVariazione]
	 **/
	public void setDescVariazione(java.lang.String descVariazione)  {
		this.descVariazione=descVariazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdrProponente]
	 **/
	public java.lang.String getCdrProponente() {
		return cdrProponente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdrProponente]
	 **/
	public void setCdrProponente(java.lang.String cdrProponente)  {
		this.cdrProponente=cdrProponente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [descCdrProponente]
	 **/
	public java.lang.String getDescCdrProponente() {
		return descCdrProponente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [descCdrProponente]
	 **/
	public void setDescCdrProponente(java.lang.String descCdrProponente)  {
		this.descCdrProponente=descCdrProponente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esResiduo]
	 **/
	public java.lang.Long getEsResiduo() {
		return esResiduo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esResiduo]
	 **/
	public void setEsResiduo(java.lang.Long esResiduo)  {
		this.esResiduo=esResiduo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdrAssegn]
	 **/
	public java.lang.String getCdrAssegn() {
		return cdrAssegn;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdrAssegn]
	 **/
	public void setCdrAssegn(java.lang.String cdrAssegn)  {
		this.cdrAssegn=cdrAssegn;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [descCdrAssegn]
	 **/
	public java.lang.String getDescCdrAssegn() {
		return descCdrAssegn;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [descCdrAssegn]
	 **/
	public void setDescCdrAssegn(java.lang.String descCdrAssegn)  {
		this.descCdrAssegn=descCdrAssegn;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsTipoVariazione]
	 **/
	public java.lang.String getDsTipoVariazione() {
		return dsTipoVariazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsTipoVariazione]
	 **/
	public void setDsTipoVariazione(java.lang.String dsTipoVariazione)  {
		this.dsTipoVariazione=dsTipoVariazione;
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
	 * Restituisce il valore di: [fonte]
	 **/
	public java.lang.String getFonte() {
		return fonte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [fonte]
	 **/
	public void setFonte(java.lang.String fonte)  {
		this.fonte=fonte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importo]
	 **/
	public java.math.BigDecimal getImporto() {
		return importo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importo]
	 **/
	public void setImporto(java.math.BigDecimal importo)  {
		this.importo=importo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imDecInt]
	 **/
	public java.math.BigDecimal getImDecInt() {
		return imDecInt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imDecInt]
	 **/
	public void setImDecInt(java.math.BigDecimal imDecInt)  {
		this.imDecInt=imDecInt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imDecEst]
	 **/
	public java.math.BigDecimal getImDecEst() {
		return imDecEst;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imDecEst]
	 **/
	public void setImDecEst(java.math.BigDecimal imDecEst)  {
		this.imDecEst=imDecEst;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imAccInt]
	 **/
	public java.math.BigDecimal getImAccInt() {
		return imAccInt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imAccInt]
	 **/
	public void setImAccInt(java.math.BigDecimal imAccInt)  {
		this.imAccInt=imAccInt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imAccEst]
	 **/
	public java.math.BigDecimal getImAccEst() {
		return imAccEst;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imAccEst]
	 **/
	public void setImAccEst(java.math.BigDecimal imAccEst)  {
		this.imAccEst=imAccEst;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [gae]
	 **/
	public java.lang.String getGae() {
		return gae;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [gae]
	 **/
	public void setGae(java.lang.String gae)  {
		this.gae=gae;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [voceDelPiano]
	 **/
	public java.lang.String getVoceDelPiano() {
		return voceDelPiano;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [voceDelPiano]
	 **/
	public void setVoceDelPiano(java.lang.String voceDelPiano)  {
		this.voceDelPiano=voceDelPiano;
	}
	public java.math.BigDecimal getImEntrata() {
		return imEntrata;
	}
	public void setImEntrata(java.math.BigDecimal imEntrata) {
		this.imEntrata = imEntrata;
	}
	public java.sql.Timestamp getDtApprovazione() {
		return dtApprovazione;
	}
	public void setDtApprovazione(java.sql.Timestamp dtApprovazione) {
		this.dtApprovazione = dtApprovazione;
	}

	public String getTiMotivazioneVariazione() {
		return tiMotivazioneVariazione;
	}

	public void setTiMotivazioneVariazione(String tiMotivazioneVariazione) {
		this.tiMotivazioneVariazione = tiMotivazioneVariazione;
	}

	public final java.util.Dictionary getTiMotivazioneVariazioneForSearchKeys() {
		return Pdg_variazioneBulk.tiMotivazioneVariazioneForSearchKeys;
	}
}