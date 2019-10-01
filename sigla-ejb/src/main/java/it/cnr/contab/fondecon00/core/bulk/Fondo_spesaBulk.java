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

package it.cnr.contab.fondecon00.core.bulk;

import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.missioni00.docs.bulk.AnticipoBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import java.util.Dictionary;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fondo_spesaBulk extends Fondo_spesaBase {

	public final static String TIPO_DOC_FP = "0";
	public final static String TIPO_DOC_GP = "1";
	public final static String TIPO_DOC_MIS = "2";
	public final static String TIPO_DOC_COMP = "3";
	public final static String TIPO_DOC_ANT = "4";

	public final static String IGNORA = "0";
	public final static String SI = "1";
	public final static String NO = "2";

	private Fondo_economaleBulk fondo_economale;
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk fornitore;

	private it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk citta;
	private java.util.Collection caps_fornitore;
	private Obbligazione_scadenzarioBulk obb_scad;

	private IDocumentoAmministrativoSpesaBulk documento = null;

	private Class classeDocAmm = null;
	private it.cnr.jada.util.OrderedHashtable tipiDocumentoKeys = null;
	private boolean checkSfondamentoMassimaleEseguito = false;
	public Fondo_spesaBulk() {
		super();
	}

public Fondo_spesaBulk(java.lang.String cd_cds,java.lang.String cd_codice_fondo,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_fondo_spesa) {
	super(cd_cds,cd_codice_fondo,cd_unita_organizzativa,esercizio,pg_fondo_spesa);
	setFondo_economale(new it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk(cd_cds,cd_codice_fondo,cd_unita_organizzativa,esercizio));
}
public java.util.Collection getCaps_fornitore() {
	return caps_fornitore;
}
public java.lang.String getCd_cds() {
	it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk fondo_economale = this.getFondo_economale();
	if (fondo_economale == null)
		return null;
	return fondo_economale.getCd_cds();
}
/* 
 * Getter dell'attributo cd_cds_doc_amm
 */
public java.lang.String getCd_cds_doc_amm() {
	if (documento != null)
		return documento.getCd_cds();
	return super.getCd_cds_doc_amm();
}
public java.lang.String getCd_cds_obbligazione() {
	it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obb_scad = this.getObb_scad();
	if (obb_scad == null)
		return null;
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obb_scad.getObbligazione();
	if (obbligazione == null)
		return null;
	it.cnr.contab.config00.sto.bulk.CdsBulk cds = obbligazione.getCds();
	if (cds == null)
		return null;
	return cds.getCd_unita_organizzativa();
}
public java.lang.String getCd_codice_fondo() {
	it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk fondo_economale = this.getFondo_economale();
	if (fondo_economale == null)
		return null;
	return fondo_economale.getCd_codice_fondo();
}
public java.lang.Integer getCd_terzo() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk fornitore = this.getFornitore();
	if (fornitore == null)
		return null;
	return fornitore.getCd_terzo();
}
/* 
 * Getter dell'attributo cd_tipo_documento_amm
 */
public java.lang.String getCd_tipo_documento_amm() {
	if (documento != null)
		return documento.getCd_tipo_doc_amm();
	return super.getCd_tipo_documento_amm();
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk fondo_economale = this.getFondo_economale();
	if (fondo_economale == null)
		return null;
	String unita_organizzativa = fondo_economale.getCd_unita_organizzativa();
	if (unita_organizzativa == null)
		return null;
	return unita_organizzativa;
}
/* 
 * Getter dell'attributo cd_uo_doc_amm
 */
public java.lang.String getCd_uo_doc_amm() {
	if (documento != null)
		return documento.getCd_uo();
	return super.getCd_uo_doc_amm();
}
public it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk getCitta() {
	return citta;
}

/**
 * Insert the method's description here.
 * Creation date: (5/3/2002 5:29:14 PM)
 * @return java.lang.Class
 */
public java.lang.Class getClasseDocAmm() {
	return classeDocAmm;
}
public java.sql.Timestamp getCurrentDate() {

	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
	} catch (javax.ejb.EJBException e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}	
}

public static java.util.Calendar getDateCalendar(java.sql.Timestamp date) {

	if (date == null)
		try {
			date = it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp();
		} catch (javax.ejb.EJBException e) {
			throw new it.cnr.jada.DetailedRuntimeException(e);
		}
		
	java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
	calendar.setTime(new java.util.Date(date.getTime()));
	calendar.set(java.util.Calendar.HOUR, 0);
	calendar.set(java.util.Calendar.MINUTE, 0);
	calendar.set(java.util.Calendar.SECOND, 0);
	calendar.set(java.util.Calendar.MILLISECOND, 0);
	calendar.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);

	return calendar;
}
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:42:42 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaBulk
 */
public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaBulk getDocumento() {
	return documento;
}
public java.lang.Integer getEsercizio() {
	it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk fondo_economale = this.getFondo_economale();
	if (fondo_economale == null)
		return null;
	return fondo_economale.getEsercizio();
}
/* 
 * Getter dell'attributo esercizio_doc_amm
 */
public java.lang.Integer getEsercizio_doc_amm() {
	if (documento != null)
		return documento.getEsercizio();
	return super.getEsercizio_doc_amm();
}
public java.lang.Integer getEsercizio_obbligazione() {
	it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obb_scad = this.getObb_scad();
	if (obb_scad == null)
		return null;
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obb_scad.getObbligazione();
	if (obbligazione == null)
		return null;
	return obbligazione.getEsercizio();
}
	public Fondo_economaleBulk getFondo_economale() {
		return fondo_economale;
	}

	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getFornitore() {
		return fornitore;
	}

/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @return int
 */
public String getFornitoreSaltuario() {

	if (getFl_fornitore_saltuario() == null)
		return IGNORA;
	else if (Boolean.TRUE.equals(getFl_fornitore_saltuario()))
		return SI;
	else if (Boolean.FALSE.equals(getFl_fornitore_saltuario()))
		return NO;
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @return int
 */
public Dictionary getFornitoreSaltuarioKeys() {

	it.cnr.jada.util.OrderedHashtable oh = new it.cnr.jada.util.OrderedHashtable();
	oh.put(IGNORA, "Ignora");
	oh.put(SI, "Si");
	oh.put(NO, "No");
	return oh;
}
/* 
 * Getter dell'attributo ImportoNettoSpesa
 */
public java.math.BigDecimal getImportoNettoSpesa() {

	if (!isSpesa_documentata())
		return getIm_ammontare_spesa();
	return getIm_netto_spesa();
}
/**
 * Insert the method's description here.
 * Creation date: (13/03/2002 10.57.38)
 * @return it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk
 */
public it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk getObb_scad() {
	return obb_scad;
}
public java.lang.Long getPg_comune() {
	it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk citta = this.getCitta();
	if (citta == null)
		return null;
	return citta.getPg_comune();
}
/* 
 * Getter dell'attributo pg_documento_amm
 */
public java.lang.Long getPg_documento_amm() {
	
	if (documento != null)
		return documento.getPg_doc_amm();
	return super.getPg_documento_amm();
}
public java.lang.Integer getEsercizio_ori_obbligazione() {
	it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obb_scad = this.getObb_scad();
	if (obb_scad == null)
		return null;
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obb_scad.getObbligazione();
	if (obbligazione == null)
		return null;
	return obbligazione.getEsercizio_originale();
}
public java.lang.Long getPg_obbligazione() {
	it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obb_scad = this.getObb_scad();
	if (obb_scad == null)
		return null;
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obb_scad.getObbligazione();
	if (obbligazione == null)
		return null;
	return obbligazione.getPg_obbligazione();
}
public java.lang.Long getPg_obbligazione_scadenzario() {
	it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obb_scad = this.getObb_scad();
	if (obb_scad == null)
		return null;
	return obb_scad.getPg_obbligazione_scadenzario();
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @return int
 */
public String getSpesaAssociata() {

	if (getFl_obbligazione() == null)
		return IGNORA;
	else if (Boolean.TRUE.equals(getFl_obbligazione()))
		return SI;
	else if (Boolean.FALSE.equals(getFl_obbligazione()))
		return NO;
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @return int
 */
public Dictionary getSpesaAssociataKeys() {

	return getSpesaDocumentataKeys();
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @return int
 */
public String getSpesaDocumentata() {

	if (getFl_documentata() == null)
		return IGNORA;
	else if (Boolean.TRUE.equals(getFl_documentata()))
		return SI;
	else if (Boolean.FALSE.equals(getFl_documentata()))
		return NO;
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @return int
 */
public Dictionary getSpesaDocumentataKeys() {

	it.cnr.jada.util.OrderedHashtable oh = new it.cnr.jada.util.OrderedHashtable();
	oh.put(IGNORA, "Ignora");
	oh.put(SI, "Si");
	oh.put(NO, "No");
	return oh;
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @return int
 */
public String getSpesaReintegrata() {

	if (getFl_reintegrata() == null)
		return IGNORA;
	else if (Boolean.TRUE.equals(getFl_reintegrata()))
		return SI;
	else if (Boolean.FALSE.equals(getFl_reintegrata()))
		return NO;
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @return int
 */
public Dictionary getSpesaReintegrataKeys() {

	it.cnr.jada.util.OrderedHashtable oh = new it.cnr.jada.util.OrderedHashtable();
	oh.put(IGNORA, "Ignora");
	oh.put(SI, "Si");
	oh.put(NO, "No");
	return oh;
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @return int
 */
public Dictionary getTipiDocumentoKeys() {

	if (tipiDocumentoKeys == null) {
		tipiDocumentoKeys = new it.cnr.jada.util.OrderedHashtable();
		tipiDocumentoKeys.put(TIPO_DOC_FP, "Fatture passive");
		tipiDocumentoKeys.put(TIPO_DOC_GP, "Documenti generici passivi");
		tipiDocumentoKeys.put(TIPO_DOC_MIS, "Missioni");
		tipiDocumentoKeys.put(TIPO_DOC_COMP, "Compensi");
		tipiDocumentoKeys.put(TIPO_DOC_ANT, "Anticipi");
	}

	return tipiDocumentoKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @return int
 */
public String getTipoDocumento() {

	if (getClasseDocAmm() == null || getClasseDocAmm().isAssignableFrom(Fattura_passiva_IBulk.class)) {
		return TIPO_DOC_FP;
	} else 	if (getClasseDocAmm() == null || getClasseDocAmm().isAssignableFrom(Documento_genericoBulk.class)) {
		return TIPO_DOC_GP;
	} else 	if (getClasseDocAmm() == null || getClasseDocAmm().isAssignableFrom(MissioneBulk.class)) {
		return TIPO_DOC_MIS;
	} else 	if (getClasseDocAmm() == null || getClasseDocAmm().isAssignableFrom(CompensoBulk.class)) {
		return TIPO_DOC_COMP;
	} else 	if (getClasseDocAmm() == null || getClasseDocAmm().isAssignableFrom(AnticipoBulk.class)) {
		return TIPO_DOC_ANT;
	}
	return null;
}
public OggettoBulk initializeForFreeSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	initializeForSearch(bp, context);
	
	return this;
}

public OggettoBulk initializeForInsert(
	it.cnr.jada.util.action.CRUDBP bp,
	it.cnr.jada.action.ActionContext context) {

	initialize(bp, context);

	setIm_ammontare_spesa(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setImportoNettoSpesa(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setDs_spesa(null);
	
	try {
		java.sql.Timestamp date = getCurrentDate();
		int annoSolare = getDateCalendar(date).get(java.util.Calendar.YEAR);
		int esercizioInScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue();
		if (annoSolare != esercizioInScrivania)
			date = new java.sql.Timestamp(new java.text.SimpleDateFormat("dd/MM/yyyy").parse("31/12/" + esercizioInScrivania).getTime());
		setDt_spesa(date);
	} catch (java.text.ParseException e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}
	
	setTipoDocumento(TIPO_DOC_FP);
	setFl_documentata(Boolean.FALSE);
	setFl_fornitore_saltuario(Boolean.TRUE);
	setFl_reintegrata(Boolean.FALSE);
	setFl_obbligazione(Boolean.FALSE);
	setDt_da_competenza_coge(getDt_spesa());
	setDt_a_competenza_coge(getDt_spesa());
	setCitta(new ComuneBulk());
	
	return this;
}

public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	setTipoDocumento(TIPO_DOC_FP);
	setFl_documentata(null);
	setFl_reintegrata(null);
	setFl_fornitore_saltuario(null);
	setFl_obbligazione(null);

	setDt_spesa(null);
	setDt_a_competenza_coge(null);
	setDt_da_competenza_coge(null);
	setIm_ammontare_spesa(null);
	setImportoNettoSpesa(null);
	
	return this;
}

/**
 * Insert the method's description here.
 * Creation date: (6/3/2002 5:38:36 PM)
 * @return boolean
 */
public boolean isCheckSfondamentoMassimaleEseguito() {
	return checkSfondamentoMassimaleEseguito;
}
public boolean isFornitore_non_saltuario() {
	
	return !isFornitore_saltuario();
}

public boolean isFornitore_saltuario() {

	if(getFl_fornitore_saltuario() == null)
		return !isSpesa_documentata();
	return getFl_fornitore_saltuario().booleanValue();
			
}

public boolean isROCd_precedente() {

	return (getFornitore() == null || getFornitore().getCrudStatus() == OggettoBulk.NORMAL);
}

public boolean isROCercaDocumento() {

	return getCrudStatus() != OggettoBulk.UNDEFINED && !isSpesa_documentata();
}

public boolean isROFornitoreData() {

	return (isFornitore_non_saltuario()) ?
				(getFornitore() == null || getFornitore().getCrudStatus() == OggettoBulk.NORMAL) :
				false;
}

public boolean isROSearchToolDocumento() {

	return !isSpesa_documentata();
}

public boolean isROSearchToolFornitore() {

	return isSpesa_documentata() || isFornitore_saltuario();
}

public boolean isSpesa_documentata() {
	if (getFl_documentata() == null)
		return false;
	return getFl_documentata().booleanValue();
}

public boolean isSpesa_documentata_in_search() {

	return getFl_documentata() == null || getFl_documentata().booleanValue();
}

public boolean isSpesa_reintegrata() {
	if (getFl_reintegrata() == null)
		return false;
	return getFl_reintegrata().booleanValue();
}

	public void setCaps_fornitore(java.util.Collection newCaps_fornitore) {
		caps_fornitore = newCaps_fornitore;
	}

public void setCd_cds(java.lang.String cd_cds) {
	super.setCd_cds(cd_cds);
	this.getFondo_economale().setCd_cds(cd_cds);
}
/* 
 * Setter dell'attributo cd_cds_doc_amm
 */
public void setCd_cds_doc_amm(java.lang.String cd_cds_doc_amm) {
	
	if (documento != null)
		documento.setCd_cds(cd_cds_doc_amm);
	else super.setCd_cds_doc_amm(cd_cds_doc_amm);
}
public void setCd_cds_obbligazione(java.lang.String cd_cds_obbligazione) {
	this.getObb_scad().getObbligazione().getCds().setCd_unita_organizzativa(cd_cds_obbligazione);
}
public void setCd_codice_fondo(java.lang.String cd_codice_fondo) {
	super.setCd_codice_fondo(cd_codice_fondo);
	this.getFondo_economale().setCd_codice_fondo(cd_codice_fondo);
}
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.getFornitore().setCd_terzo(cd_terzo);
}
/* 
 * Setter dell'attributo cd_tipo_documento_amm
 */
public void setCd_tipo_documento_amm(java.lang.String cd_tipo_documento_amm) {

	if (documento != null)
		documento.setCd_tipo_doc_amm(cd_tipo_documento_amm);
	else super.setCd_tipo_documento_amm(cd_tipo_documento_amm);
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getFondo_economale().setCd_unita_organizzativa(cd_unita_organizzativa);
}
/* 
 * Setter dell'attributo cd_uo_doc_amm
 */
public void setCd_uo_doc_amm(java.lang.String cd_uo_doc_amm) {
	
	if (documento != null)
		documento.setCd_uo(cd_uo_doc_amm);
	else super.setCd_uo_doc_amm(cd_uo_doc_amm);
}
/**
 * Insert the method's description here.
 * Creation date: (6/3/2002 5:38:36 PM)
 * @param newCheckSfondamentoMassimaleEseguito boolean
 */
public void setCheckSfondamentoMassimaleEseguito(boolean newCheckSfondamentoMassimaleEseguito) {
	checkSfondamentoMassimaleEseguito = newCheckSfondamentoMassimaleEseguito;
}
	public void setCitta(it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk newCitta) {
		citta = newCitta;
	}

/**
 * Insert the method's description here.
 * Creation date: (5/3/2002 5:29:14 PM)
 * @param newClasseDocAmm java.lang.Class
 */
public void setClasseDocAmm(java.lang.Class newClasseDocAmm) {
	classeDocAmm = newClasseDocAmm;
}
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:42:42 PM)
 * @param newDocAmm it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaBulk
 */
public void setDocumento(it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaBulk newDocumento) {
	documento = newDocumento;
}
public void setEsercizio(java.lang.Integer esercizio) {
	super.setEsercizio(esercizio);
	this.getFondo_economale().setEsercizio(esercizio);
}
/* 
 * Setter dell'attributo esercizio_doc_amm
 */
public void setEsercizio_doc_amm(java.lang.Integer esercizio_doc_amm) {
	if (documento != null)
		documento.setEsercizio(esercizio_doc_amm);
	else super.setEsercizio_doc_amm(esercizio_doc_amm);
}
public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione) {
	this.getObb_scad().getObbligazione().setEsercizio(esercizio_obbligazione);
}
	public void setFondo_economale(Fondo_economaleBulk newFondo_economale) {
		fondo_economale = newFondo_economale;
	}

	public void setFornitore(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newFornitore) {
		fornitore = newFornitore;
	}

/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @param newSezionaliFlag int
 */
public void setFornitoreSaltuario(String newFornitoreSaltuario) {

	switch (Integer.valueOf(newFornitoreSaltuario).intValue()) {
		case 0	:	{	
						setFl_fornitore_saltuario(null);
						break;
					} 
		case 1	:	{	
						setFl_fornitore_saltuario(Boolean.TRUE);
						break;
					} 
		case 2	:	{	
						setFl_fornitore_saltuario(Boolean.FALSE);
						break;
					} 
		default: 	{
						setFl_fornitore_saltuario(null);
						break;
					}
	}
}
/* 
 * Getter dell'attributo ImportoNettoSpesa
 */
public void setImportoNettoSpesa(java.math.BigDecimal importoNettoSpesa) {
	setIm_netto_spesa(importoNettoSpesa);
}
/**
 * Insert the method's description here.
 * Creation date: (13/03/2002 10.57.38)
 * @param newObb_scad it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk
 */
public void setObb_scad(it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk newObb_scad) {
	obb_scad = newObb_scad;
}
public void setPg_comune(java.lang.Long pg_comune) {
	this.getCitta().setPg_comune(pg_comune);
}
/* 
 * Setter dell'attributo pg_documento_amm
 */
public void setPg_documento_amm(java.lang.Long pg_documento_amm) {
	if (documento != null)
		documento.setPg_doc_amm(pg_documento_amm);
	else super.setPg_documento_amm(pg_documento_amm);
}
public void setEsercizio_ori_obbligazione(java.lang.Integer esercizio_ori_obbligazione) {
	this.getObb_scad().getObbligazione().setEsercizio_originale(esercizio_ori_obbligazione);
}
public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
	this.getObb_scad().getObbligazione().setPg_obbligazione(pg_obbligazione);
}
public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario) {
	this.getObb_scad().setPg_obbligazione_scadenzario(pg_obbligazione_scadenzario);
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @param newSezionaliFlag int
 */
public void setSpesaAssociata(String newSpesaAssociata) {

	switch (Integer.valueOf(newSpesaAssociata).intValue()) {
		case 0	:	{	
						setFl_obbligazione(null);
						break;
					} 
		case 1	:	{	
						setFl_obbligazione(Boolean.TRUE);
						setSpesaDocumentata(NO);
						break;
					} 
		case 2	:	{	
						setFl_obbligazione(Boolean.FALSE);
						setSpesaDocumentata(NO);
						break;
					} 
		default: 	{
						setFl_obbligazione(null);
						setSpesaDocumentata(IGNORA);
						break;
					}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @param newSezionaliFlag int
 */
public void setSpesaDocumentata(String newSpesaDocumentata) {

	switch (Integer.valueOf(newSpesaDocumentata).intValue()) {
		case 0	:	{	
						setFl_documentata(null);
						break;
					} 
		case 1	:	{	
						setFl_documentata(Boolean.TRUE);
						break;
					} 
		case 2	:	{	
						setFl_documentata(Boolean.FALSE);
						break;
					} 
		default: 	{
						setFl_documentata(null);
						break;
					}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @param newSezionaliFlag int
 */
public void setSpesaReintegrata(String newSpesaReintegrata) {

	switch (Integer.valueOf(newSpesaReintegrata).intValue()) {
		case 0	:	{	
						setFl_reintegrata(null);
						break;
					} 
		case 1	:	{	
						setFl_reintegrata(Boolean.TRUE);
						break;
					} 
		case 2	:	{	
						setFl_reintegrata(Boolean.FALSE);
						break;
					} 
		default: 	{
						setFl_reintegrata(null);
						break;
					}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @param newSezionaliFlag int
 */
public void setTipoDocumento(String newTipoDoc) {

	if (newTipoDoc == null)
		setClasseDocAmm(Fattura_passiva_IBulk.class);
	else switch (Integer.valueOf(newTipoDoc).intValue()) {
		case 0	:	{	
						setClasseDocAmm(Fattura_passiva_IBulk.class);
						break;
					} 
		case 1	:	{
						setClasseDocAmm(Documento_genericoBulk.class);
						break;
					} 
		case 2	:	{
						setClasseDocAmm(MissioneBulk.class);
						break;
					} 
		case 3	:	{
						setClasseDocAmm(CompensoBulk.class);
						break;
					} 
		case 4	:	{
						setClasseDocAmm(AnticipoBulk.class);
						break;
					} 
		default: 	{
						setClasseDocAmm(Fattura_passiva_IBulk.class);
						break;
					}
	}
}
}
