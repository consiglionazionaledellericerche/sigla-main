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
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.util.*;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 11:10:31 AM)
 * @author: Roberto Peli
 */
public class Filtro_ricerca_doc_ammVBulk extends it.cnr.jada.bulk.OggettoBulk {

	public static final java.util.Dictionary GROUPS;

	public static final String DOC_ATT_GRUOP = "Documenti attivi";
	public static final String DOC_PASS_GRUOP = "Documenti passivi";
	public static final String DOC_GEN_GRUOP = "Documenti generici";

	private OrderedHashtable optionsHash = null;
	
	static {
		
		GROUPS = new OrderedHashtable();
		GROUPS.put(DOC_ATT_GRUOP,DOC_ATT_GRUOP);
		GROUPS.put(DOC_PASS_GRUOP, DOC_PASS_GRUOP);
		GROUPS.put(DOC_GEN_GRUOP, DOC_GEN_GRUOP);
	}

	private String group = null;
	private String option = null;
	
	private IDocumentoAmministrativoBulk instance = null;
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk soggetto = null;
/**
 * Filtro_ricerca_obbligazioniVBulk constructor comment.
 */
public Filtro_ricerca_doc_ammVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (3/12/2002 10:22:18 AM)
 * @return java.util.Collection
 */
private OrderedHashtable buildOptionsHash() {

	optionsHash = new OrderedHashtable();

	java.util.Dictionary options = new OrderedHashtable();
	options.put(Documento_amministrativo_attivoBulk.class.getName(), "Tutti");
	options.put(Fattura_attiva_IBulk.class.getName(), "Fatture attive");
	options.put(Nota_di_credito_attivaBulk.class.getName(), "Note di credito");
	options.put(Nota_di_debito_attivaBulk.class.getName(), "Note di debito");
	optionsHash.put(DOC_ATT_GRUOP, options);
	
	options = new OrderedHashtable();
	options.put(Documento_amministrativo_passivoBulk.class.getName(), "Tutti");
	options.put(Fattura_passiva_IBulk.class.getName(), "Fatture passive");
	options.put(Nota_di_creditoBulk.class.getName(), "Note di credito");
	options.put(Nota_di_debitoBulk.class.getName(), "Note di debito");
	optionsHash.put(DOC_PASS_GRUOP, options);
	
	options = new OrderedHashtable();
	options.put(Documento_genericoBulk.class.getName(), "Tutti");
	options.put(Documento_generico_attivoBulk.class.getName(), "Generici attivi");
	options.put(Documento_generico_passivoBulk.class.getName(), "Generici passivi");
	optionsHash.put(DOC_GEN_GRUOP, options);

	return optionsHash;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_fornitore java.lang.Boolean
 */
public IDocumentoAmministrativoBulk createInstance(it.cnr.jada.util.action.BulkBP bp, ActionContext context) {

	IDocumentoAmministrativoBulk instance = null;
	try {
		Class clazz = Class.forName(getOption());
		instance = (IDocumentoAmministrativoBulk)clazz.newInstance();
		instance = (IDocumentoAmministrativoBulk)((it.cnr.jada.bulk.OggettoBulk)instance).initializeForSearch(null, context);
	} catch (Throwable t) {
			instance = null;
	}
	
	return instance;
}
/**
 * Insert the method's description here.
 * Creation date: (24/05/2002 13.01.57)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 * @param map java.util.Map
 */
public static java.sql.Timestamp getDataOdierna() {

	try {
		return getDataOdierna(it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp());
	} catch (javax.ejb.EJBException e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (24/05/2002 13.01.57)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 * @param map java.util.Map
 */
public static java.sql.Timestamp getDataOdierna(java.sql.Timestamp dataOdierna) {

	java.util.Calendar gc = java.util.Calendar.getInstance();
	gc.setTime(dataOdierna);
	gc.set(java.util.Calendar.HOUR, 0);
	gc.set(java.util.Calendar.MINUTE, 0);
	gc.set(java.util.Calendar.SECOND, 0);
	gc.set(java.util.Calendar.MILLISECOND, 0);
	gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
	return new java.sql.Timestamp(gc.getTime().getTime());
}
/**
 * Insert the method's description here.
 * Creation date: (3/12/2002 5:36:56 PM)
 * @return java.lang.String
 */
public java.lang.String getGroup() {
	return group;
}
/**
 * Insert the method's description here.
 * Creation date: (3/12/2002 10:20:26 AM)
 * @return java.util.Collection
 */
public java.util.Dictionary getGroups() {
	return GROUPS;
}
/**
 * Insert the method's description here.
 * Creation date: (3/12/2002 10:23:49 AM)
 * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
 */
public IDocumentoAmministrativoBulk getInstance() {
	return instance;
}
/**
 * Insert the method's description here.
 * Creation date: (3/11/2002 6:04:55 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
 */
public java.lang.String getManagerName() {

	if (getInstance() == null) return null;
	return getInstance().getManagerName();
}
/**
 * Insert the method's description here.
 * Creation date: (3/11/2002 6:04:55 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
 */
public java.lang.String getManagerOptions() {

	if (getInstance() == null) return null;
	return getInstance().getManagerOptions();
}
/**
 * Insert the method's description here.
 * Creation date: (3/12/2002 5:36:56 PM)
 * @return java.lang.String
 */
public java.lang.String getOption() {
	return option;
}
/**
 * Insert the method's description here.
 * Creation date: (3/12/2002 10:22:18 AM)
 * @return java.util.Collection
 */
public java.util.Dictionary getOptions() {

	if (getGroup() == null) return null;
	
	if (optionsHash == null) {
		optionsHash = buildOptionsHash();
	}
	return (java.util.Dictionary)optionsHash.get(getGroup());
}
/**
 * Insert the method's description here.
 * Creation date: (3/25/2002 3:04:27 PM)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getSoggetto() {
	return soggetto;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_fornitore java.lang.Boolean
 */
public Filtro_ricerca_doc_ammVBulk initializeForSearch(
	it.cnr.contab.docamm00.bp.ListaDocumentiAmministrativiBP bp,
	it.cnr.jada.action.ActionContext context) {

	optionsHash = buildOptionsHash();
	
	setGroup(DOC_ATT_GRUOP);
	setOption((String)((OrderedHashtable)optionsHash.get(DOC_ATT_GRUOP)).keys().nextElement());
	
	instance = createInstance(bp, context);
	
	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROsoggetto() {
	
	return getSoggetto() == null ||
			getSoggetto().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (3/12/2002 5:36:56 PM)
 * @param newGroup java.lang.String
 */
public void setGroup(java.lang.String newGroup) {
	group = newGroup;
}
/**
 * Insert the method's description here.
 * Creation date: (3/12/2002 6:00:18 PM)
 * @param newInstance it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
 */
public void setInstance(IDocumentoAmministrativoBulk newInstance) {
	instance = newInstance;
}
/**
 * Insert the method's description here.
 * Creation date: (3/12/2002 5:36:56 PM)
 * @param newOption java.lang.String
 */
public void setOption(java.lang.String newOption) {
	option = newOption;
}
/**
 * Insert the method's description here.
 * Creation date: (3/25/2002 3:04:27 PM)
 * @param newSoggetto it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setSoggetto(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newSoggetto) {
	soggetto = newSoggetto;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_fornitore java.lang.Boolean
 */
public void updateOptions(ActionContext context) {

	setOption((String)((OrderedHashtable)optionsHash.get(getGroup())).keys().nextElement());
}
}
