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
import java.util.Vector;
import java.util.Collection;

import it.cnr.contab.docamm00.intrastat.bulk.Fattura_passiva_intraBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.CRUDBP;

/**
 * Insert the type's description here.
 * Creation date: (10/24/2001 2:26:43 PM)
 * @author: Roberto Peli
 */
public class Nota_di_creditoBulk extends Fattura_passivaBulk {

	private AccertamentiTable accertamenti_scadenzarioHash = null;

	private BancaBulk banca_uo;
	private TerzoBulk ente;
	private Rif_modalita_pagamentoBulk modalita_pagamento_uo;
	private Rif_termini_pagamentoBulk termini_pagamento_uo;

	private Collection banche_uo;
	private Collection modalita_uo;
	private Collection termini_uo;

	private java.math.BigDecimal importoTotalePerAccertamenti = new java.math.BigDecimal(0);;
/**
 * Nota_di_creditoBulk constructor comment.
 */
public Nota_di_creditoBulk() {
	super();

	setTi_fattura(TIPO_NOTA_DI_CREDITO);
}
/**
 * Nota_di_creditoBulk constructor comment.
 */
public Nota_di_creditoBulk(
	Fattura_passiva_IBulk fattura_passiva,
	Integer esercizioScrivania) {
	this();

	copyFrom(fattura_passiva, esercizioScrivania);
}
/**
 * Nota_di_creditoBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param cd_unita_organizzativa java.lang.String
 * @param esercizio java.lang.Integer
 * @param pg_fattura_passiva java.lang.Long
 */
public Nota_di_creditoBulk(String cd_cds, String cd_unita_organizzativa, Integer esercizio, Long pg_fattura_passiva) {
	super(cd_cds, cd_unita_organizzativa, esercizio, pg_fattura_passiva);

	setTi_fattura(TIPO_NOTA_DI_CREDITO);
}
public void addToAccertamenti_scadenzarioHash(
	Accertamento_scadenzarioBulk accertamento_scadenzario,
	Nota_di_credito_rigaBulk rigaNdC) {

	if (accertamenti_scadenzarioHash == null)
		accertamenti_scadenzarioHash = new AccertamentiTable();
	Vector righeAssociate = (Vector)accertamenti_scadenzarioHash.get(accertamento_scadenzario);
	if (righeAssociate == null) {
		righeAssociate = new Vector();
		//accertamenti_scadenzarioHash.put(accertamento_scadenzario, righeAssociate);
		addToFattura_passiva_ass_totaliMap(accertamento_scadenzario, new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	}
	if (rigaNdC != null && !righeAssociate.contains(rigaNdC)) {
		righeAssociate.add(rigaNdC);
		//Sono costretto alla rimozione della scadenza per evitare disallineamenti sul pg_ver_rec.
		//e quindi errori del tipo RisorsaNonPiuValida in fase di salvataggio
		if (accertamenti_scadenzarioHash.containsKey(accertamento_scadenzario))
			accertamenti_scadenzarioHash.remove(accertamento_scadenzario);
		//accertamenti_scadenzarioHash.put(accertamento_scadenzario, righeAssociate);
	}
	accertamenti_scadenzarioHash.put(accertamento_scadenzario, righeAssociate);
		
	if (getDocumentiContabiliCancellati() != null && 
		it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), accertamento_scadenzario))
		removeFromDocumentiContabiliCancellati(accertamento_scadenzario);
}

public void copyFrom(
	Fattura_passiva_IBulk fattura_passiva,
	Integer esercizioScrivania) {

	if (fattura_passiva == null || esercizioScrivania == null) return;

	setStato_cofi(STATO_INIZIALE);
	setStato_coge(NON_REGISTRATO_IN_COGE);
	setStato_coan(NON_CONTABILIZZATO_IN_COAN);
	setStato_pagamento_fondo_eco(NO_FONDO_ECO);
	setTi_associato_manrev(NON_ASSOCIATO_A_MANDATO);
	setStato_liquidazione(NOLIQ);
	setCausale(NCRED);
	setCd_cds(fattura_passiva.getCd_cds());
	setEsercizio(esercizioScrivania);
	setCd_unita_organizzativa(fattura_passiva.getCd_unita_organizzativa());
	setCd_cds_origine(fattura_passiva.getCd_cds_origine());
	setCd_uo_origine(fattura_passiva.getCd_uo_origine());
	// Gennaro Borriello/Farinella (09/11/2004 16.12.05)
	//	Aggiunta gestione dell'es di scrivania per il controllo sullo stato riportato/Anno di competenza
	setEsercizioInScrivania(fattura_passiva.getEsercizioInScrivania());
	setDataInizioFatturaElettronica(fattura_passiva.getDataInizioFatturaElettronica());
	setDataInizioSplitPayment(fattura_passiva.getDataInizioSplitPayment());
	
	try {
		java.sql.Timestamp date = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
		int annoSolare = getDateCalendar(date).get(java.util.Calendar.YEAR);
		if (annoSolare != getEsercizio().intValue())
			date = new java.sql.Timestamp(new java.text.SimpleDateFormat("dd/MM/yyyy").parse("31/12/"+fattura_passiva.getEsercizio()).getTime());
		setDt_registrazione(date);
	} catch (Throwable t) {
		throw new it.cnr.jada.DetailedRuntimeException(t);
	}
	setDt_da_competenza_coge(fattura_passiva.getDt_da_competenza_coge());
	setDt_a_competenza_coge(fattura_passiva.getDt_a_competenza_coge());
	
	//setNumero_protocollo(fattura_passiva.getNumero_protocollo());
	//setProtocollo_iva(fattura_passiva.getProtocollo_iva());
	//setProtocollo_iva_generale(fattura_passiva.getProtocollo_iva_generale());
	
	setTi_istituz_commerc(fattura_passiva.getTi_istituz_commerc());
	setSezionali(fattura_passiva.getSezionali());
	setTipo_sezionale(fattura_passiva.getTipo_sezionale());

	setFl_congelata(Boolean.FALSE);
	setFl_intra_ue(fattura_passiva.getFl_intra_ue());
	setFl_extra_ue(fattura_passiva.getFl_extra_ue());
	setFl_san_marino_con_iva(fattura_passiva.getFl_san_marino_con_iva());
	setFl_san_marino_senza_iva(fattura_passiva.getFl_san_marino_senza_iva());
	setFl_bolla_doganale(fattura_passiva.getFl_bolla_doganale());
	setFl_spedizioniere(fattura_passiva.getFl_spedizioniere());
	setFl_autofattura(fattura_passiva.getFl_autofattura());
	setTi_bene_servizio(fattura_passiva.getTi_bene_servizio());
	setFl_merce_extra_ue(fattura_passiva.getFl_merce_extra_ue());
	setFl_merce_intra_ue(fattura_passiva.getFl_merce_intra_ue());
	setFl_fattura_compenso(fattura_passiva.getFl_fattura_compenso());
	setDs_fattura_passiva(fattura_passiva.getDs_fattura_passiva());
	setFl_split_payment(fattura_passiva.getFl_split_payment());
	//setDt_fattura_fornitore(fattura_passiva.getDt_fattura_fornitore());
	//setDt_scadenza(fattura_passiva.getDt_scadenza());

	setValute(fattura_passiva.getValute());
	setValuta(fattura_passiva.getValuta());
	setCambio(fattura_passiva.getCambio());
	setDefaultValuta(fattura_passiva.isDefaultValuta());
	
	setTermini(fattura_passiva.getTermini());
	setTermini_pagamento(fattura_passiva.getTermini_pagamento());
	setModalita(fattura_passiva.getModalita());
	setModalita_pagamento(fattura_passiva.getModalita_pagamento());
	
	setFornitore(fattura_passiva.getFornitore());
	setRagione_sociale(fattura_passiva.getRagione_sociale());
	setNome(fattura_passiva.getNome());
	setCognome(fattura_passiva.getCognome());
	setCodice_fiscale(fattura_passiva.getCodice_fiscale());
	setPartita_iva(fattura_passiva.getPartita_iva());
	
	setCessionario(fattura_passiva.getCessionario());
	setBanca(fattura_passiva.getBanca());

	setIm_totale_fattura(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setIm_totale_imponibile_divisa(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	
	setFl_liquidazione_differita(fattura_passiva.getFl_liquidazione_differita());
	setModalita_trasportoColl(fattura_passiva.getModalita_trasportoColl());
	setCondizione_consegnaColl(fattura_passiva.getCondizione_consegnaColl());
	setModalita_incassoColl(fattura_passiva.getModalita_incassoColl());
	setModalita_erogazioneColl(fattura_passiva.getModalita_erogazioneColl());
	setDt_termine_creazione_docamm(fattura_passiva.getDt_termine_creazione_docamm());
	setFlDaOrdini(fattura_passiva.getFlDaOrdini());
}
/**
 * Insert the method's description here.
 * Creation date: (11/16/2001 3:21:35 PM)
 * @return java.util.Hashtable
 */
public AccertamentiTable getAccertamenti_scadenzarioHash() {
	return accertamenti_scadenzarioHash;
}
/**
 * Insert the method's description here.
 * Creation date: (11/07/2001 11.07.07)
 * @return it.cnr.jada.bulk.BulkList
 */
public AccertamentiTable getAccertamentiHash() {

	return getAccertamenti_scadenzarioHash();
}
/**
 * Insert the method's description here.
 * Creation date: (3/4/2002 11:20:17 AM)
 * @return it.cnr.contab.anagraf00.core.bulk.BancaBulk
 */
public it.cnr.contab.anagraf00.core.bulk.BancaBulk getBanca_uo() {
	return banca_uo;
}
/**
 * Insert the method's description here.
 * Creation date: (3/4/2002 11:20:17 AM)
 * @return java.util.Collection
 */
public java.util.Collection getBanche_uo() {
	return banche_uo;
}
public java.lang.String getCd_modalita_pag_uo_cds() {
	it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalita_pagamento_uo = this.getModalita_pagamento_uo();
	if (modalita_pagamento_uo == null)
		return null;
	return modalita_pagamento_uo.getCd_modalita_pag();
}
public java.lang.String getCd_termini_pag_uo_cds() {
	it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk termini_pagamento_uo = this.getTermini_pagamento_uo();
	if (termini_pagamento_uo == null)
		return null;
	return termini_pagamento_uo.getCd_termini_pag();
}
public java.lang.Integer getCd_terzo_uo_cds() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk ente = this.getEnte();
	if (ente == null)
		return null;
	return ente.getCd_terzo();
}
/**
 * Insert the method's description here.
 * Creation date: (2/11/2002 3:14:13 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
 */
public java.lang.Class getChildClass() {
	return Nota_di_credito_rigaBulk.class;
}
/**
 * Insert the method's description here.
 * Creation date: (4/9/2002 12:04:38 PM)
 * @return java.lang.String
 */
public java.lang.String getDescrizioneEntita() {
	return "nota di credito";
}
/**
 * Insert the method's description here.
 * Creation date: (4/9/2002 12:04:38 PM)
 * @return java.lang.String
 */
public java.lang.String getDescrizioneEntitaPlurale() {
	return "note di credito";
}
/**
 * Insert the method's description here.
 * Creation date: (3/4/2002 11:48:09 AM)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getEnte() {
	return ente;
}
public java.math.BigDecimal getImportoSignForDelete(java.math.BigDecimal importo) {

	return importo;
}
/**
 * Insert the method's description here.
 * Creation date: (11/16/2001 4:58:04 PM)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getImportoTotalePerAccertamenti() {
	return importoTotalePerAccertamenti;
}
public String getManagerName() {
	return "CRUDNotaDiCreditoBP";
}
/**
 * Insert the method's description here.
 * Creation date: (3/22/2002 2:35:58 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
 */
public java.lang.String getManagerOptions() {
	return "VTh";
}
/**
 * Insert the method's description here.
 * Creation date: (3/4/2002 11:20:17 AM)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk getModalita_pagamento_uo() {
	return modalita_pagamento_uo;
}
/**
 * Insert the method's description here.
 * Creation date: (3/4/2002 11:20:17 AM)
 * @return java.util.Collection
 */
public java.util.Collection getModalita_uo() {
	return modalita_uo;
}
public java.lang.Long getPg_banca_uo_cds() {
	it.cnr.contab.anagraf00.core.bulk.BancaBulk banca_uo = this.getBanca_uo();
	if (banca_uo == null)
		return null;
	return banca_uo.getPg_banca();
}
/**
 * Insert the method's description here.
 * Creation date: (3/4/2002 11:20:17 AM)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk getTermini_pagamento_uo() {
	return termini_pagamento_uo;
}
/**
 * Insert the method's description here.
 * Creation date: (3/4/2002 11:20:17 AM)
 * @return java.util.Collection
 */
public java.util.Collection getTermini_uo() {
	return termini_uo;
}
public OggettoBulk initialize(CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initialize(bp, context);

	setTi_fattura(TIPO_NOTA_DI_CREDITO);
	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isAbledToInsertUOBank() {
	
	return !(getEnte() != null && 
		getEnte().getCrudStatus() == OggettoBulk.NORMAL &&
		getModalita_pagamento_uo() != null);
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROfornitore() {
	
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROFornitoreCrudTool() {
	
	return isROFornitoreSearchTool();
}
public boolean isROfornitoreForSearch() {
	
	return getFornitore() == null ||
			getFornitore().getCrudStatus() == OggettoBulk.NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROFornitoreSearchTool() {
	
	return (super.isROFornitoreSearchTool() ||
			(getAccertamenti_scadenzarioHash() != null &&
			 !getAccertamenti_scadenzarioHash().isEmpty()));
}
public boolean isROImportoTotalePerAccertamenti() {
	
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROModalitaPagamentoUO() {
	
	return getAccertamentiHash() == null || getAccertamentiHash().isEmpty();
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROTerminiPagamentoUO() {
	
	return getAccertamentiHash() == null || getAccertamentiHash().isEmpty();
}
public void removeFromAccertamenti_scadenzarioHash(
	Nota_di_credito_rigaBulk rigaNdC) {

	Vector righeAssociate = (Vector)accertamenti_scadenzarioHash.get(rigaNdC.getAccertamento_scadenzario());
	if (righeAssociate != null) {
		righeAssociate.remove(rigaNdC);
		if (righeAssociate.isEmpty()) {
			accertamenti_scadenzarioHash.remove(rigaNdC.getAccertamento_scadenzario());
			addToDocumentiContabiliCancellati(rigaNdC.getAccertamento_scadenzario());
			if (accertamenti_scadenzarioHash.isEmpty()) {
				resetModalitaEnte();
			}
		}
	} else
		addToDocumentiContabiliCancellati(rigaNdC.getAccertamento_scadenzario());
}

public Fattura_passiva_rigaBulk removeFromFattura_passiva_dettColl( int indiceDiLinea ) {

	Nota_di_credito_rigaBulk element = (Nota_di_credito_rigaBulk)getFattura_passiva_dettColl().get(indiceDiLinea);
	addToDettagliCancellati(element);
	if (element != null && !element.STATO_INIZIALE.equals(element.getStato_cofi())) {
		if (element.getObbligazione_scadenziario() != null)
			removeFromFattura_passiva_obbligazioniHash(element);
		if (element.getAccertamento_scadenzario() != null)
			removeFromAccertamenti_scadenzarioHash(element);
	}
	return (Fattura_passiva_rigaBulk)getFattura_passiva_dettColl().remove(indiceDiLinea);
}

private void resetModalitaEnte() {

	setEnte(null);
	setBanca_uo(null);
	setBanche_uo(null);
	setModalita_pagamento_uo(null);
	setTermini_pagamento_uo(null);
	setTermini_uo(null);
	setModalita_uo(null);
}

/**
 * Insert the method's description here.
 * Creation date: (11/16/2001 3:21:35 PM)
 * @param newAccertamenti_scadenzarioHash java.util.Hashtable
 */
public void setAccertamenti_scadenzarioHash(AccertamentiTable newAccertamenti_scadenzarioHash) {
	accertamenti_scadenzarioHash = newAccertamenti_scadenzarioHash;
}
/**
 * Insert the method's description here.
 * Creation date: (3/4/2002 11:20:17 AM)
 * @param newBanca_uo it.cnr.contab.anagraf00.core.bulk.BancaBulk
 */
public void setBanca_uo(it.cnr.contab.anagraf00.core.bulk.BancaBulk newBanca_uo) {
	banca_uo = newBanca_uo;
}
/**
 * Insert the method's description here.
 * Creation date: (3/4/2002 11:20:17 AM)
 * @param newBanche_uo java.util.Collection
 */
public void setBanche_uo(java.util.Collection newBanche_uo) {
	banche_uo = newBanche_uo;
}
public void setCd_modalita_pag_uo_cds(java.lang.String cd_modalita_pag_uo_cds) {
	this.getModalita_pagamento_uo().setCd_modalita_pag(cd_modalita_pag_uo_cds);
}
public void setCd_termini_pag_uo_cds(java.lang.String cd_termini_pag_uo_cds) {
	this.getTermini_pagamento_uo().setCd_termini_pag(cd_termini_pag_uo_cds);
}
public void setCd_terzo_uo_cds(java.lang.Integer cd_terzo_uo_cds) {
	this.getEnte().setCd_terzo(cd_terzo_uo_cds);
}
/**
 * Insert the method's description here.
 * Creation date: (3/4/2002 11:48:09 AM)
 * @param newEnte it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setEnte(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newEnte) {
	ente = newEnte;
}
/**
 * Insert the method's description here.
 * Creation date: (11/16/2001 4:58:04 PM)
 * @param newImportoTotalePerAccertamenti java.math.BigDecimal
 */
public void setImportoTotalePerAccertamenti(java.math.BigDecimal newImportoTotalePerAccertamenti) {
	importoTotalePerAccertamenti = newImportoTotalePerAccertamenti;
}
/**
 * Insert the method's description here.
 * Creation date: (3/4/2002 11:20:17 AM)
 * @param newModalita_pagamento_uo it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
public void setModalita_pagamento_uo(it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk newModalita_pagamento_uo) {
	modalita_pagamento_uo = newModalita_pagamento_uo;
}
/**
 * Insert the method's description here.
 * Creation date: (3/4/2002 11:20:17 AM)
 * @param newModalita_uo java.util.Collection
 */
public void setModalita_uo(java.util.Collection newModalita_uo) {
	modalita_uo = newModalita_uo;
}
public void setPg_banca_uo_cds(java.lang.Long pg_banca_uo_cds) {
	this.getBanca_uo().setPg_banca(pg_banca_uo_cds);
}
/**
 * Insert the method's description here.
 * Creation date: (3/4/2002 11:20:17 AM)
 * @param newTermini_pagamento_uo it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk
 */
public void setTermini_pagamento_uo(it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk newTermini_pagamento_uo) {
	termini_pagamento_uo = newTermini_pagamento_uo;
}
/**
 * Insert the method's description here.
 * Creation date: (3/4/2002 11:20:17 AM)
 * @param newTermini_uo java.util.Collection
 */
public void setTermini_uo(java.util.Collection newTermini_uo) {
	termini_uo = newTermini_uo;
}

public Dictionary getCausaleKeys(){
	OrderedHashtable d = (OrderedHashtable)super.getCausaleKeys();
	if (d == null) return null;

	OrderedHashtable clone = (OrderedHashtable)d.clone();
	clone.remove(ATTNC);
	clone.put(NCRED,"Nota Credito");
	return clone;
}
}
