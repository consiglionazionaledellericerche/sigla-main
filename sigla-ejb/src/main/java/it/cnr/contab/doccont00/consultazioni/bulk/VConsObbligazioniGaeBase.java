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
 * Date 27/01/2015
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.persistency.Keyed;
public class VConsObbligazioniGaeBase extends VConsObbligazioniGaeKey implements Keyed {
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cdUnitaOrganizzativa;
 
//    CD_CDS_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cdCdsOrigine;
 
//  DS_OBBLIGAZIONE VARCHAR(300) NOT NULL
	private java.lang.String dsObbligazione;

//    CD_UO_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cdUoOrigine;
 
//    CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cdElementoVoce;
 
//    TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String tiAppartenenza;
 
//    TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String tiGestione;
 
//    FL_PGIRO CHAR(1) NOT NULL
	private java.lang.Boolean flPgiro;
 
//    IM_SCADENZA_COMP DECIMAL(22,2)
	private java.math.BigDecimal imScadenzaComp;
 
//    IM_SCADENZA_RES DECIMAL(22,2)
	private java.math.BigDecimal imScadenzaRes;
 
//    IM_ASSOCIATO_DOC_AMM_COMP DECIMAL(22,2)
	private java.math.BigDecimal imAssociatoDocAmmComp;
 
//    IM_ASSOCIATO_DOC_AMM_RES DECIMAL(22,2)
	private java.math.BigDecimal imAssociatoDocAmmRes;
 
//    IM_PAGATO_COMP DECIMAL(22,2)
	private java.math.BigDecimal imPagatoComp;
 
//    IM_PAGATO_RES DECIMAL(22,2)
	private java.math.BigDecimal imPagatoRes;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CONS_OBBLIGAZIONI
	 **/
	public VConsObbligazioniGaeBase() {
		super();
	}
	public VConsObbligazioniGaeBase(java.lang.String cdCds, java.lang.String cdLineaAttivita, java.lang.Integer esercizio, java.lang.Integer esercizioOriginale, java.lang.Long pgObbligazione) {
		super(cdCds, cdLineaAttivita, esercizio, esercizioOriginale, pgObbligazione);
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
	 * Restituisce il valore di: [cdCdsOrigine]
	 **/
	public java.lang.String getCdCdsOrigine() {
		return cdCdsOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsOrigine]
	 **/
	public void setCdCdsOrigine(java.lang.String cdCdsOrigine)  {
		this.cdCdsOrigine=cdCdsOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUoOrigine]
	 **/
	public java.lang.String getCdUoOrigine() {
		return cdUoOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUoOrigine]
	 **/
	public void setCdUoOrigine(java.lang.String cdUoOrigine)  {
		this.cdUoOrigine=cdUoOrigine;
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
	 * Restituisce il valore di: [imScadenzaComp]
	 **/
	public java.math.BigDecimal getImScadenzaComp() {
		return imScadenzaComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imScadenzaComp]
	 **/
	public void setImScadenzaComp(java.math.BigDecimal imScadenzaComp)  {
		this.imScadenzaComp=imScadenzaComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imScadenzaRes]
	 **/
	public java.math.BigDecimal getImScadenzaRes() {
		return imScadenzaRes;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imScadenzaRes]
	 **/
	public void setImScadenzaRes(java.math.BigDecimal imScadenzaRes)  {
		this.imScadenzaRes=imScadenzaRes;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imAssociatoDocAmmComp]
	 **/
	public java.math.BigDecimal getImAssociatoDocAmmComp() {
		return imAssociatoDocAmmComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imAssociatoDocAmmComp]
	 **/
	public void setImAssociatoDocAmmComp(java.math.BigDecimal imAssociatoDocAmmComp)  {
		this.imAssociatoDocAmmComp=imAssociatoDocAmmComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imAssociatoDocAmmRes]
	 **/
	public java.math.BigDecimal getImAssociatoDocAmmRes() {
		return imAssociatoDocAmmRes;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imAssociatoDocAmmRes]
	 **/
	public void setImAssociatoDocAmmRes(java.math.BigDecimal imAssociatoDocAmmRes)  {
		this.imAssociatoDocAmmRes=imAssociatoDocAmmRes;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imPagatoComp]
	 **/
	public java.math.BigDecimal getImPagatoComp() {
		return imPagatoComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imPagatoComp]
	 **/
	public void setImPagatoComp(java.math.BigDecimal imPagatoComp)  {
		this.imPagatoComp=imPagatoComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imPagatoRes]
	 **/
	public java.math.BigDecimal getImPagatoRes() {
		return imPagatoRes;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imPagatoRes]
	 **/
	public void setImPagatoRes(java.math.BigDecimal imPagatoRes)  {
		this.imPagatoRes=imPagatoRes;
	}
	public java.lang.Boolean getFlPgiro() {
		return flPgiro;
	}
	public void setFlPgiro(java.lang.Boolean flPgiro) {
		this.flPgiro = flPgiro;
	}
	public java.lang.String getDsObbligazione() {
		return dsObbligazione;
	}
	public void setDsObbligazione(java.lang.String dsObbligazione) {
		this.dsObbligazione = dsObbligazione;
	}
}