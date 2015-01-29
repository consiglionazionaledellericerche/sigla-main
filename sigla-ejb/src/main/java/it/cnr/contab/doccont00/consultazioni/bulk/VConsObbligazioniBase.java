/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 27/01/2015
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
import it.cnr.jada.persistency.Keyed;
public class VConsObbligazioniBase extends VConsObbligazioniKey implements Keyed {
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cdUnitaOrganizzativa;
 
//    CD_CDS_ORIGINE VARCHAR(30) NOT NULL
	private java.lang.String cdCdsOrigine;
 
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
 
//    IM_SCADENZA_COMP DECIMAL(22,0)
	private java.lang.Long imScadenzaComp;
 
//    IM_SCADENZA_RES DECIMAL(22,0)
	private java.lang.Long imScadenzaRes;
 
//    IM_ASSOCIATO_DOC_AMM_COMP DECIMAL(22,0)
	private java.lang.Long imAssociatoDocAmmComp;
 
//    IM_ASSOCIATO_DOC_AMM_RES DECIMAL(22,0)
	private java.lang.Long imAssociatoDocAmmRes;
 
//    IM_PAGATO_COMP DECIMAL(22,0)
	private java.lang.Long imPagatoComp;
 
//    IM_PAGATO_RES DECIMAL(22,0)
	private java.lang.Long imPagatoRes;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CONS_OBBLIGAZIONI
	 **/
	public VConsObbligazioniBase() {
		super();
	}
	public VConsObbligazioniBase(java.lang.String cdCds, java.lang.Integer esercizio, java.lang.Integer esercizioOriginale, java.lang.Long pgObbligazione) {
		super(cdCds, esercizio, esercizioOriginale, pgObbligazione);
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
	public java.lang.Long getImScadenzaComp() {
		return imScadenzaComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imScadenzaComp]
	 **/
	public void setImScadenzaComp(java.lang.Long imScadenzaComp)  {
		this.imScadenzaComp=imScadenzaComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imScadenzaRes]
	 **/
	public java.lang.Long getImScadenzaRes() {
		return imScadenzaRes;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imScadenzaRes]
	 **/
	public void setImScadenzaRes(java.lang.Long imScadenzaRes)  {
		this.imScadenzaRes=imScadenzaRes;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imAssociatoDocAmmComp]
	 **/
	public java.lang.Long getImAssociatoDocAmmComp() {
		return imAssociatoDocAmmComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imAssociatoDocAmmComp]
	 **/
	public void setImAssociatoDocAmmComp(java.lang.Long imAssociatoDocAmmComp)  {
		this.imAssociatoDocAmmComp=imAssociatoDocAmmComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imAssociatoDocAmmRes]
	 **/
	public java.lang.Long getImAssociatoDocAmmRes() {
		return imAssociatoDocAmmRes;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imAssociatoDocAmmRes]
	 **/
	public void setImAssociatoDocAmmRes(java.lang.Long imAssociatoDocAmmRes)  {
		this.imAssociatoDocAmmRes=imAssociatoDocAmmRes;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imPagatoComp]
	 **/
	public java.lang.Long getImPagatoComp() {
		return imPagatoComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imPagatoComp]
	 **/
	public void setImPagatoComp(java.lang.Long imPagatoComp)  {
		this.imPagatoComp=imPagatoComp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imPagatoRes]
	 **/
	public java.lang.Long getImPagatoRes() {
		return imPagatoRes;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imPagatoRes]
	 **/
	public void setImPagatoRes(java.lang.Long imPagatoRes)  {
		this.imPagatoRes=imPagatoRes;
	}
	public java.lang.Boolean getFlPgiro() {
		return flPgiro;
	}
	public void setFlPgiro(java.lang.Boolean flPgiro) {
		this.flPgiro = flPgiro;
	}
}