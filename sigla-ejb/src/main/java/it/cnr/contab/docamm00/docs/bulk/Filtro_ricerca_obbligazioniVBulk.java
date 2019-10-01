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

package it.cnr.contab.docamm00.docs.bulk;

import java.util.Dictionary;
import java.util.List;

import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 11:10:31 AM)
 * @author: Roberto Peli
 */
public class Filtro_ricerca_obbligazioniVBulk 
	extends it.cnr.jada.bulk.OggettoBulk {

	public final static Dictionary competenzaResiduoKeys = ObbligazioneBulk.competenzaResiduoKeys;

	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk fornitore = null;
	private java.sql.Timestamp data_scadenziario = null;
	private java.math.BigDecimal im_importo = new java.math.BigDecimal(0);
	private boolean passivo_ente;
	private Tipo_documento_ammBulk tipo_documento;
	private java.lang.String tipo_obbligazione;
	private java.lang.Integer esercizio_ori_obbligazione = null;
	private java.lang.Long nr_obbligazione = null;
	private java.lang.Boolean fl_data_scadenziario = Boolean.TRUE;
	private java.lang.Boolean fl_fornitore = Boolean.TRUE;
	private java.lang.Boolean fl_importo = Boolean.TRUE;
	private java.lang.Boolean fl_nr_obbligazione = Boolean.FALSE;
	private java.lang.String cd_unita_organizzativa = null;
	private java.lang.String cd_uo_origine = null;
	private Elemento_voceBulk elemento_voce = null;
	private ContrattoBulk contratto = null;
	private java.lang.String ds_obbligazione;
	private java.lang.String ds_scadenza;
	private boolean hasDocumentoCompetenzaCOGEInAnnoPrecedente = false;
	private boolean hasDocumentoCompetenzaCOGESoloInAnnoCorrente = false;
	private List<Elemento_voceBulk> listaVociSelezionabili = null;
	private boolean competenzaCOGESuEnte = false;
/**
 * Filtro_ricerca_obbligazioniVBulk constructor comment.
 */
public Filtro_ricerca_obbligazioniVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean canSearchFornitore() {
	
	//return getFornitore() == null ||
			//getFornitore().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL;
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (11/23/2001 12:41:29 PM)
 * @return java.lang.String
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/**
 * Insert the method's description here.
 * Creation date: (26/03/2002 10.01.59)
 * @return java.lang.String
 */
public java.lang.String getCd_uo_origine() {
	return cd_uo_origine;
}
public java.sql.Timestamp getCurrentDate() {

	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
	} catch (javax.ejb.EJBException e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}	
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:14:50 AM)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getData_scadenziario() {
	return data_scadenziario;
}
/**
 * Insert the method's description here.
 * Creation date: (05/08/2002 14.27.23)
 * @return java.lang.String
 */
public java.lang.String getDs_obbligazione() {

	if (ds_obbligazione!=null)
		return ds_obbligazione;

	return "Impegno per documento amministrativo passivo";
}
/**
 * Insert the method's description here.
 * Creation date: (05/08/2002 14.29.29)
 * @return java.lang.String
 */
public java.lang.String getDs_scadenza() {

	if (ds_scadenza!=null)
		return ds_scadenza;

	return "Scadenza per documento amministrativo passivo";
}
/**
 * Insert the method's description here.
 * Creation date: (12/5/2001 4:58:02 PM)
 * @return java.lang.String
 */
public Elemento_voceBulk getElemento_voce() {
	return elemento_voce;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:17:56 AM)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_data_scadenziario() {
	return fl_data_scadenziario;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_fornitore() {
	return fl_fornitore;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_importo() {
	return fl_importo;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_nr_obbligazione() {
	return fl_nr_obbligazione;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:14:18 AM)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getFornitore() {
	return fornitore;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:15:39 AM)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getIm_importo() {
	return im_importo;
}
/**
 * @return
 */
public java.lang.Integer getEsercizio_ori_obbligazione() {
	return esercizio_ori_obbligazione;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:16:25 AM)
 * @return java.lang.Long
 */
public java.lang.Long getNr_obbligazione() {
	return nr_obbligazione;
}
/**
 * Insert the method's description here.
 * Creation date: (17/06/2002 12.41.38)
 * @author: CNRADM
 * @return it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk
 */
public Tipo_documento_ammBulk getTipo_documento() {
	return tipo_documento;
}
/**
 * Insert the method's description here.
 * Creation date: (11/6/2002 10:54:43 AM)
 * @return boolean
 */
public boolean hasDocumentoCompetenzaCOGEInAnnoPrecedente() {
	return hasDocumentoCompetenzaCOGEInAnnoPrecedente;
}
/**
 * Insert the method's description here.
 * Creation date: (11/7/2002 12:13:11 PM)
 * @return boolean
 */
public boolean hasDocumentoCompetenzaCOGESoloInAnnoCorrente() {
	return hasDocumentoCompetenzaCOGESoloInAnnoCorrente;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_fornitore java.lang.Boolean
 */
public Filtro_ricerca_obbligazioniVBulk initializeForSearch(
	it.cnr.contab.docamm00.bp.RicercaObbligazioniBP bp,
	it.cnr.jada.action.ActionContext context) {

	setData_scadenziario(getCurrentDate());
	setIm_importo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setFl_data_scadenziario(Boolean.TRUE);
	setFl_fornitore(Boolean.TRUE);
	setFl_importo(Boolean.TRUE);
	setFl_nr_obbligazione(Boolean.FALSE);
	
	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (11/7/2002 3:03:50 PM)
 * @return boolean
 */
public boolean isCompetenzaCOGESuEnte() {
	return competenzaCOGESuEnte;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 13.31.19)
 * @return boolean
 */
public boolean isPassivo_ente() {
	return passivo_ente;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROdatascadenza() {
	
	return !getFl_data_scadenziario().booleanValue();
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROfornitore() {
	
	return canSearchFornitore();
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROfornitoretool() {
	
	return true;//!getFl_fornitore().booleanValue() ;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROimporto() {
	
	return !getFl_importo().booleanValue();
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROnrobbligazione() {
	
	return !getFl_nr_obbligazione().booleanValue() ;
}
/**
 * Insert the method's description here.
 * Creation date: (11/23/2001 12:41:29 PM)
 * @param newCd_unita_organizzativa java.lang.String
 */
public void setCd_unita_organizzativa(java.lang.String newCd_unita_organizzativa) {
	cd_unita_organizzativa = newCd_unita_organizzativa;
}
/**
 * Insert the method's description here.
 * Creation date: (26/03/2002 10.01.59)
 * @param newCd_uo_origine java.lang.String
 */
public void setCd_uo_origine(java.lang.String newCd_uo_origine) {
	cd_uo_origine = newCd_uo_origine;
}
/**
 * Insert the method's description here.
 * Creation date: (11/7/2002 3:03:50 PM)
 * @param newCompetenzaCOGESuEnte boolean
 */
public void setCompetenzaCOGESuEnte(boolean newCompetenzaCOGESuEnte) {
	competenzaCOGESuEnte = newCompetenzaCOGESuEnte;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:14:50 AM)
 * @param newDataScadenziario java.sql.Timestamp
 */
public void setData_scadenziario(java.sql.Timestamp newDataScadenziario) {
	data_scadenziario = newDataScadenziario;
}
/**
 * Insert the method's description here.
 * Creation date: (05/08/2002 14.27.23)
 * @param newDs_obbligazione java.lang.String
 */
public void setDs_obbligazione(java.lang.String newDs_obbligazione) {
	ds_obbligazione = newDs_obbligazione;
}
/**
 * Insert the method's description here.
 * Creation date: (05/08/2002 14.29.29)
 * @param newDs_scadenza java.lang.String
 */
public void setDs_scadenza(java.lang.String newDs_scadenza) {
	ds_scadenza = newDs_scadenza;
}
/**
 * Insert the method's description here.
 * Creation date: (12/5/2001 4:58:02 PM)
 * @param newTitoloCapitolo java.lang.String
 */
public void setElemento_voce(Elemento_voceBulk newElementoVoce) {
	elemento_voce = newElementoVoce;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:17:56 AM)
 * @param newFl_data_scadenziario java.lang.Boolean
 */
public void setFl_data_scadenziario(java.lang.Boolean newFl_data_scadenziario) {
	fl_data_scadenziario = newFl_data_scadenziario;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_fornitore java.lang.Boolean
 */
public void setFl_fornitore(java.lang.Boolean newFl_fornitore) {
	fl_fornitore = newFl_fornitore;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_importo java.lang.Boolean
 */
public void setFl_importo(java.lang.Boolean newFl_importo) {
	fl_importo = newFl_importo;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_nr_obbligazione java.lang.Boolean
 */
public void setFl_nr_obbligazione(java.lang.Boolean newFl_nr_obbligazione) {
	fl_nr_obbligazione = newFl_nr_obbligazione;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:14:18 AM)
 * @param newFornitore it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setFornitore(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newFornitore) {
	fornitore = newFornitore;
}
/**
 * Insert the method's description here.
 * Creation date: (11/6/2002 10:54:43 AM)
 * @param newHasDocumentoCompetenzaCOGEInAnnoPrecedente boolean
 */
public void setHasDocumentoCompetenzaCOGEInAnnoPrecedente(boolean newHasDocumentoCompetenzaCOGEInAnnoPrecedente) {
	hasDocumentoCompetenzaCOGEInAnnoPrecedente = newHasDocumentoCompetenzaCOGEInAnnoPrecedente;
}
/**
 * Insert the method's description here.
 * Creation date: (11/7/2002 12:13:11 PM)
 * @param newHasDocumentoCompetenzaCOGESoloInAnnoCorrente boolean
 */
public void setHasDocumentoCompetenzaCOGESoloInAnnoCorrente(boolean newHasDocumentoCompetenzaCOGESoloInAnnoCorrente) {
	hasDocumentoCompetenzaCOGESoloInAnnoCorrente = newHasDocumentoCompetenzaCOGESoloInAnnoCorrente;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:15:39 AM)
 * @param newIm_importo java.math.BigDecimal
 */
public void setIm_importo(java.math.BigDecimal newIm_importo) {
	im_importo = newIm_importo;
}
/**
 * @param integer
 */
public void setEsercizio_ori_obbligazione(java.lang.Integer integer) {
	esercizio_ori_obbligazione = integer;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:16:25 AM)
 * @param newNr_obbligazione java.lang.Long
 */
public void setNr_obbligazione(java.lang.Long newNr_obbligazione) {
	nr_obbligazione = newNr_obbligazione;
}
/**
 * Insert the method's description here.
 * Creation date: (20/03/2002 13.31.19)
 * @param newPassivo_ente boolean
 */
public void setPassivo_ente(boolean newPassivo_ente) {
	passivo_ente = newPassivo_ente;
}
/**
 * Insert the method's description here.
 * Creation date: (17/06/2002 12.41.38)
 * @author: CNRADM
 * @param newTipo_documento it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk
 */
public void setTipo_documento(Tipo_documento_ammBulk newTipo_documento) {
	tipo_documento = newTipo_documento;
}
/**
 * @return
 */
public java.lang.String getTipo_obbligazione() {
	return tipo_obbligazione;
}
/**
 * @param string
 */
public void setTipo_obbligazione(java.lang.String string) {
	tipo_obbligazione = string;
}
public List<Elemento_voceBulk> getListaVociSelezionabili() {
	return listaVociSelezionabili;
}
public void setListaVociSelezionabili(List<Elemento_voceBulk> listaVociSelezionabili) {
	this.listaVociSelezionabili = listaVociSelezionabili;
}
public ContrattoBulk getContratto() {
	return contratto;
}
public void setContratto(ContrattoBulk contratto) {
	this.contratto = contratto;
}

}
