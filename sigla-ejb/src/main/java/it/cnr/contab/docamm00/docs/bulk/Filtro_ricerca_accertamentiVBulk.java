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

import it.cnr.contab.config00.pdcfin.bulk.*;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 11:10:31 AM)
 * @author: Roberto Peli
 */
public class Filtro_ricerca_accertamentiVBulk 
	extends it.cnr.jada.bulk.OggettoBulk {

	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk cliente = null;
	private java.sql.Timestamp data_scadenziario = null;
	private java.math.BigDecimal im_importo = new java.math.BigDecimal(0);
	private java.lang.Integer esercizio_ori_accertamento = null;
	private java.lang.Long nr_accertamento = null;
	private java.lang.Boolean fl_data_scadenziario = Boolean.TRUE;
	private java.lang.Boolean fl_cliente = Boolean.TRUE;
	private java.lang.Boolean fl_importo = Boolean.TRUE;
	private java.lang.Boolean fl_nr_accertamento = Boolean.FALSE;
	private java.lang.String cd_unita_organizzativa = null;
	private java.lang.String cd_uo_origine = null;
	private boolean hasDocumentoCompetenzaCOGEInAnnoPrecedente = false;
	private boolean hasDocumentoCompetenzaCOGESoloInAnnoCorrente = false;
	private boolean competenzaCOGESuEnte = false;
	private boolean attivoCds = false;
	private boolean passivo_ente;
/**
 * Filtro_ricerca_obbligazioniVBulk constructor comment.
 */
public Filtro_ricerca_accertamentiVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean canSearchCliente() {
	
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
 * Creation date: (26/03/2002 9.44.09)
 * @return java.lang.String
 */
public java.lang.String getCd_uo_origine() {
	return cd_uo_origine;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:14:18 AM)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getCliente() {
	return cliente;
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
 * Creation date: (10/16/2001 11:18:42 AM)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_cliente() {
	return fl_cliente;
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
public java.lang.Boolean getFl_importo() {
	return fl_importo;
}
/**
 * Insert the method's description here.
 * Creation date: (12/18/2001 5:13:56 PM)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_nr_accertamento() {
	return fl_nr_accertamento;
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
public java.lang.Integer getEsercizio_ori_accertamento() {
	return esercizio_ori_accertamento;
}
/**
 * Insert the method's description here.
 * Creation date: (12/18/2001 5:13:56 PM)
 * @return java.lang.Long
 */
public java.lang.Long getNr_accertamento() {
	return nr_accertamento;
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
 * Creation date: (11/6/2002 5:13:35 PM)
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
public Filtro_ricerca_accertamentiVBulk initializeForSearch(
	it.cnr.contab.docamm00.bp.RicercaAccertamentiBP bp,
	it.cnr.jada.action.ActionContext context) {

	setData_scadenziario(getCurrentDate());
	setIm_importo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setFl_data_scadenziario(Boolean.TRUE);
	setFl_cliente(Boolean.TRUE);
	setFl_importo(Boolean.TRUE);
	setFl_nr_accertamento(Boolean.FALSE);
	
	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (11/12/2002 3:13:00 PM)
 * @return boolean
 */
public boolean isAttivoCds() {
	return attivoCds;
}
/**
 * Insert the method's description here.
 * Creation date: (11/7/2002 3:03:34 PM)
 * @return boolean
 */
public boolean isCompetenzaCOGESuEnte() {
	return competenzaCOGESuEnte;
}
/**
 * Insert the method's description here.
 * Creation date: (11/6/2002 5:13:35 PM)
 * @return boolean
 */
public boolean isHasDocumentoCompetenzaCOGESoloInAnnoCorrente() {
	return hasDocumentoCompetenzaCOGESoloInAnnoCorrente;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROcliente() {
	
	return canSearchCliente();
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROclientetool() {
	//mod. da alf
	return false;//!getFl_fornitore().booleanValue() ;
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
public boolean isROimporto() {
	
	return !getFl_importo().booleanValue();
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROnraccertamento() {
	
	return !getFl_nr_accertamento().booleanValue() ;
}
/**
 * Insert the method's description here.
 * Creation date: (11/12/2002 3:13:00 PM)
 * @param newAttivoEnte boolean
 */
public void setAttivoCds(boolean newAttivoCds) {
	attivoCds = newAttivoCds;
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
 * Creation date: (26/03/2002 9.44.09)
 * @param newCd_uo_origine java.lang.String
 */
public void setCd_uo_origine(java.lang.String newCd_uo_origine) {
	cd_uo_origine = newCd_uo_origine;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:14:18 AM)
 * @param newFornitore it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setCliente(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newCliente) {
	cliente = newCliente;
}
/**
 * Insert the method's description here.
 * Creation date: (11/7/2002 3:03:34 PM)
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
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_fornitore java.lang.Boolean
 */
public void setFl_cliente(java.lang.Boolean newFl_cliente) {
	fl_cliente = newFl_cliente;
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
 * @param newFl_importo java.lang.Boolean
 */
public void setFl_importo(java.lang.Boolean newFl_importo) {
	fl_importo = newFl_importo;
}
/**
 * Insert the method's description here.
 * Creation date: (12/18/2001 5:13:56 PM)
 * @param newFl_nr_accertamento java.lang.Boolean
 */
public void setFl_nr_accertamento(java.lang.Boolean newFl_nr_accertamento) {
	fl_nr_accertamento = newFl_nr_accertamento;
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
 * Creation date: (11/6/2002 5:13:35 PM)
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
public void setEsercizio_ori_accertamento(java.lang.Integer integer) {
	esercizio_ori_accertamento = integer;
}
/**
 * Insert the method's description here.
 * Creation date: (12/18/2001 5:13:56 PM)
 * @param newNr_accertamento java.lang.Long
 */
public void setNr_accertamento(java.lang.Long newNr_accertamento) {
	nr_accertamento = newNr_accertamento;
}
public boolean isPassivo_ente() {
	return passivo_ente;
}
public void setPassivo_ente(boolean newPassivo_ente) {
	passivo_ente = newPassivo_ente;
}
}
