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
 * Date 27/09/2018
 */
package it.cnr.contab.progettiric00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class TipoFinanziamentoBase extends TipoFinanziamentoKey implements Keyed {
//    CODICE VARCHAR(25) NOT NULL
	private String codice;
 
//    DESCRIZIONE VARCHAR(1000) NOT NULL
	private String descrizione;
 
//    FL_PIANO_ECO_FIN CHAR(1) NOT NULL
	private Boolean flPianoEcoFin;
 
//    FL_ASS_CAT_VOCI_IND CHAR(1) NOT NULL
	private Boolean flAssCatVociInd;
 
//    FL_ASS_CAT_VOCI_DET CHAR(1) NOT NULL
	private Boolean flAssCatVociDet;
 
//    FL_ASS_CAT_VOCI_ALTRO CHAR(1) NOT NULL
	private Boolean flAssCatVociAltro;
 
//    FL_PREV_ENT_SPESA CHAR(1) NOT NULL
	private Boolean flPrevEntSpesa;
 
//    FL_RIP_COSTI_PERS CHAR(1) NOT NULL
	private Boolean flRipCostiPers;
 
//    FL_QUAD_PDGP_ECONOM CHAR(1) NOT NULL
	private Boolean flQuadPdgpEconom;
 
//    FL_CONTR_VAL_PROG CHAR(1) NOT NULL
	private Boolean flContrValProg;
 
//    FL_PIANO_REND CHAR(1) NOT NULL
	private Boolean flPianoRend;
 
//    FL_VAR_CONS CHAR(1) NOT NULL
	private Boolean flVarCons;
 
//    FL_INC_CONS CHAR(1) NOT NULL
	private Boolean flIncCons;

//  FL_ALL_PREV_FIN CHAR(1) NOT NULL
	private Boolean flAllPrevFin;

//  FL_QUADRA_CONTRATTO CHAR(1) NOT NULL
	private Boolean flQuadraContratto;
	
//  FL_ASSOCIA_CONTRATTO CHAR(1) NOT NULL
	private Boolean flAssociaContratto;

	//  FL_VALIDAZIONE_AUTOMATICA CHAR(1) NOT NULL
	private Boolean flValidazioneAutomatica;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_FINANZIAMENTO
	 **/
	public TipoFinanziamentoBase() {
		super();
	}
	public TipoFinanziamentoBase(Long id) {
		super(id);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codice]
	 **/
	public String getCodice() {
		return codice;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codice]
	 **/
	public void setCodice(String codice)  {
		this.codice=codice;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [descrizione]
	 **/
	public String getDescrizione() {
		return descrizione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [descrizione]
	 **/
	public void setDescrizione(String descrizione)  {
		this.descrizione=descrizione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flPianoEcoFin]
	 **/
	public Boolean getFlPianoEcoFin() {
		return flPianoEcoFin;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flPianoEcoFin]
	 **/
	public void setFlPianoEcoFin(Boolean flPianoEcoFin)  {
		this.flPianoEcoFin=flPianoEcoFin;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flAssCatVociInd]
	 **/
	public Boolean getFlAssCatVociInd() {
		return flAssCatVociInd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flAssCatVociInd]
	 **/
	public void setFlAssCatVociInd(Boolean flAssCatVociInd)  {
		this.flAssCatVociInd=flAssCatVociInd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flAssCatVociDet]
	 **/
	public Boolean getFlAssCatVociDet() {
		return flAssCatVociDet;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flAssCatVociDet]
	 **/
	public void setFlAssCatVociDet(Boolean flAssCatVociDet)  {
		this.flAssCatVociDet=flAssCatVociDet;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flAssCatVociAltro]
	 **/
	public Boolean getFlAssCatVociAltro() {
		return flAssCatVociAltro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flAssCatVociAltro]
	 **/
	public void setFlAssCatVociAltro(Boolean flAssCatVociAltro)  {
		this.flAssCatVociAltro=flAssCatVociAltro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flPrevEntSpesa]
	 **/
	public Boolean getFlPrevEntSpesa() {
		return flPrevEntSpesa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flPrevEntSpesa]
	 **/
	public void setFlPrevEntSpesa(Boolean flPrevEntSpesa)  {
		this.flPrevEntSpesa=flPrevEntSpesa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flRipCostiPers]
	 **/
	public Boolean getFlRipCostiPers() {
		return flRipCostiPers;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flRipCostiPers]
	 **/
	public void setFlRipCostiPers(Boolean flRipCostiPers)  {
		this.flRipCostiPers=flRipCostiPers;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flQuadPdgpEconom]
	 **/
	public Boolean getFlQuadPdgpEconom() {
		return flQuadPdgpEconom;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flQuadPdgpEconom]
	 **/
	public void setFlQuadPdgpEconom(Boolean flQuadPdgpEconom)  {
		this.flQuadPdgpEconom=flQuadPdgpEconom;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flContrValProg]
	 **/
	public Boolean getFlContrValProg() {
		return flContrValProg;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flContrValProg]
	 **/
	public void setFlContrValProg(Boolean flContrValProg)  {
		this.flContrValProg=flContrValProg;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flPianoRend]
	 **/
	public Boolean getFlPianoRend() {
		return flPianoRend;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flPianoRend]
	 **/
	public void setFlPianoRend(Boolean flPianoRend)  {
		this.flPianoRend=flPianoRend;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flVarCons]
	 **/
	public Boolean getFlVarCons() {
		return flVarCons;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flVarCons]
	 **/
	public void setFlVarCons(Boolean flVarCons)  {
		this.flVarCons=flVarCons;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [flIncCons]
	 **/
	public Boolean getFlIncCons() {
		return flIncCons;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [flIncCons]
	 **/
	public void setFlIncCons(Boolean flIncCons)  {
		this.flIncCons=flIncCons;
	}
	
	public Boolean getFlAllPrevFin() {
		return flAllPrevFin;
	}
	
	public void setFlAllPrevFin(Boolean flAllPrevFin) {
		this.flAllPrevFin = flAllPrevFin;
	}
	
	public Boolean getFlQuadraContratto() {
		return flQuadraContratto;
	}
	
	public void setFlQuadraContratto(Boolean flQuadraContratto) {
		this.flQuadraContratto = flQuadraContratto;
	}
	
	public Boolean getFlAssociaContratto() {
		return flAssociaContratto;
	}
	
	public void setFlAssociaContratto(Boolean flAssociaContratto) {
		this.flAssociaContratto = flAssociaContratto;
	}

	public Boolean getFlValidazioneAutomatica() {
		return flValidazioneAutomatica;
	}

	public void setFlValidazioneAutomatica(Boolean flValidazioneAutomatica) {
		this.flValidazioneAutomatica = flValidazioneAutomatica;
	}
}