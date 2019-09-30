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

import java.util.Vector;
import java.util.Collection;

import it.cnr.contab.docamm00.intrastat.bulk.Fattura_attiva_intraBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

/**
 * Insert the type's description here.
 * Creation date: (10/24/2001 2:26:43 PM)
 * @author: Roberto Peli
 */
public class Nota_di_credito_attivaBulk extends Fattura_attivaBulk {

	private BancaBulk banca;
	private Rif_modalita_pagamentoBulk modalita_pagamento;
	private Rif_termini_pagamentoBulk termini_pagamento;

	private Collection banche;
	private Collection modalita;
	private Collection termini;

	private ObbligazioniTable obbligazioni_scadenzarioHash = null;
	private java.math.BigDecimal importoTotalePerObbligazioni = new java.math.BigDecimal(0);;
/**
 * Nota_di_creditoBulk constructor comment.
 */
public Nota_di_credito_attivaBulk() {
	super();

	setTi_fattura(TIPO_NOTA_DI_CREDITO);
}
/**
 * Nota_di_creditoBulk constructor comment.
 */
public Nota_di_credito_attivaBulk(
	Fattura_attiva_IBulk fattura_attiva,
	Integer esercizioScrivania) {
	this();

	copyFrom(fattura_attiva, esercizioScrivania);
}
public Nota_di_credito_attivaBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_fattura_attiva) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_attiva);

	setTi_fattura(TIPO_NOTA_DI_CREDITO);
}
public void addToObbligazioni_scadenzarioHash(
	Obbligazione_scadenzarioBulk obbligazione_scadenzario,
	Nota_di_credito_attiva_rigaBulk rigaNdC) {

	if (obbligazioni_scadenzarioHash == null)
		obbligazioni_scadenzarioHash = new ObbligazioniTable();
	Vector righeAssociate = (Vector)obbligazioni_scadenzarioHash.get(obbligazione_scadenzario);
	if (righeAssociate == null) {
		righeAssociate = new Vector();
		//obbligazioni_scadenzarioHash.put(obbligazione_scadenzario, righeAssociate);
		addToFattura_attiva_ass_totaliMap(obbligazione_scadenzario, new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	}
	if (rigaNdC != null && !righeAssociate.contains(rigaNdC)) {
		righeAssociate.add(rigaNdC);
		//Sono costretto alla rimozione della scadenza per evitare disallineamenti sul pg_ver_rec.
		//e quindi errori del tipo RisorsaNonPiuValida in fase di salvataggio
		if (obbligazioni_scadenzarioHash.containsKey(obbligazione_scadenzario))
			obbligazioni_scadenzarioHash.remove(obbligazione_scadenzario);
		//obbligazioni_scadenzarioHash.put(obbligazione_scadenzario, righeAssociate);
	}
	obbligazioni_scadenzarioHash.put(obbligazione_scadenzario, righeAssociate);
	
	if (getDocumentiContabiliCancellati() != null && 
		it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(getDocumentiContabiliCancellati(), obbligazione_scadenzario))
		removeFromDocumentiContabiliCancellati(obbligazione_scadenzario);
}

public void copyFrom(
	Fattura_attiva_IBulk fattura_attiva,
	Integer esercizioScrivania) {

	if (fattura_attiva == null || esercizioScrivania == null) return;

	setStato_cofi(STATO_INIZIALE);
	setStato_coge(NON_REGISTRATO_IN_COGE);
	setStato_coan(NON_CONTABILIZZATO_IN_COAN);
	setTi_associato_manrev(NON_ASSOCIATO_A_MANDATO);
	
	setCd_cds(fattura_attiva.getCd_cds());
	setEsercizio(esercizioScrivania);
	setCd_unita_organizzativa(fattura_attiva.getCd_unita_organizzativa());
	setCd_cds_origine(fattura_attiva.getCd_cds_origine());
	setCd_uo_origine(fattura_attiva.getCd_uo_origine());
	// Gennaro Borriello/Farinella (09/11/2004 16.12.05)
	//	Aggiunta gestione dell'es di scrivania per il controllo sullo stato riportato/Anno di competenza
	setEsercizioInScrivania(fattura_attiva.getEsercizioInScrivania());
	
	
	try {
		java.sql.Timestamp date = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
		int annoSolare = getDateCalendar(date).get(java.util.Calendar.YEAR);
		if (annoSolare != getEsercizio().intValue())
			date = new java.sql.Timestamp(new java.text.SimpleDateFormat("dd/MM/yyyy").parse("31/12/"+fattura_attiva.getEsercizio()).getTime());
		setDt_registrazione(date);
	} catch (Throwable t) {
		throw new it.cnr.jada.DetailedRuntimeException(t);
	}

	setDt_termine_creazione_docamm(fattura_attiva.getDt_termine_creazione_docamm());
	setDt_da_competenza_coge(fattura_attiva.getDt_da_competenza_coge());
	setDt_a_competenza_coge(fattura_attiva.getDt_a_competenza_coge());

	//setFl_cliente_occasionale(fattura_attiva.getFl_cliente_occasionale());
	setFl_liquidazione_differita(fattura_attiva.getFl_liquidazione_differita());
	setAttivoSplitPayment(fattura_attiva.isAttivoSplitPayment());
	//setProtocollo_iva(fattura_attiva.getProtocollo_iva());
	//setProtocollo_iva_generale(fattura_attiva.getProtocollo_iva_generale());
	
	setSezionali(fattura_attiva.getSezionali());
	setTipo_sezionale(fattura_attiva.getTipo_sezionale());
	
	setTi_causale_emissione(fattura_attiva.getTi_causale_emissione());
	setFl_intra_ue(fattura_attiva.getFl_intra_ue());
	setFl_extra_ue(fattura_attiva.getFl_extra_ue());
	setFl_san_marino(fattura_attiva.getFl_san_marino());
	setFl_congelata(Boolean.FALSE);

	setDs_fattura_attiva(fattura_attiva.getDs_fattura_attiva());
	setRiferimento_ordine(fattura_attiva.getRiferimento_ordine());
	//setDt_scadenza(fattura_attiva.getDt_scadenza());

	setValute(fattura_attiva.getValute());
	setValuta(fattura_attiva.getValuta());
	setCambio(fattura_attiva.getCambio());
	
	setTermini_uo(fattura_attiva.getTermini_uo());
	setTermini_pagamento_uo(fattura_attiva.getTermini_pagamento_uo());
	setModalita_uo(fattura_attiva.getModalita_uo());
	setModalita_pagamento_uo(fattura_attiva.getModalita_pagamento_uo());
	
	setCliente(fattura_attiva.getCliente());
	setTerzo_uo(fattura_attiva.getTerzo_uo());

	setRagione_sociale(fattura_attiva.getRagione_sociale());
	setNome(fattura_attiva.getNome());
	setCognome(fattura_attiva.getCognome());
	setCodice_fiscale(fattura_attiva.getCodice_fiscale());
	setPartita_iva(fattura_attiva.getPartita_iva());

	setBanca_uo(fattura_attiva.getBanca_uo());
	
	setIm_totale_fattura(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setFl_stampa(Boolean.FALSE);
	setModalita_trasportoColl(fattura_attiva.getModalita_trasportoColl());
	setCondizione_consegnaColl(fattura_attiva.getCondizione_consegnaColl());
	setModalita_incassoColl(fattura_attiva.getModalita_incassoColl());
	setModalita_erogazioneColl(fattura_attiva.getModalita_erogazioneColl());
	setTi_bene_servizio(fattura_attiva.getTi_bene_servizio());
	setFl_pagamento_anticipato(fattura_attiva.getFl_pagamento_anticipato());
}
/**
 * Insert the method's description here.
 * Creation date: (3/6/2002 11:37:23 AM)
 * @return BancaBulk
 */
public BancaBulk getBanca() {
	return banca;
}
/**
 * Insert the method's description here.
 * Creation date: (3/6/2002 11:37:23 AM)
 * @return Collection
 */
public Collection getBanche() {
	return banche;
}
public java.lang.String getCd_modalita_pag() {
	it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalita_pagamento = this.getModalita_pagamento();
	if (modalita_pagamento == null)
		return null;
	return modalita_pagamento.getCd_modalita_pag();
}
public java.lang.String getCd_termini_pag() {
	it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk termini_pagamento = this.getTermini_pagamento();
	if (termini_pagamento == null)
		return null;
	return termini_pagamento.getCd_termini_pag();
}
public java.lang.Class getChildClass() {
	return Nota_di_credito_attiva_rigaBulk.class;
}
public java.math.BigDecimal getImportoSignForDelete(java.math.BigDecimal importo) {

	return importo;
}
/**
 * Insert the method's description here.
 * Creation date: (11/16/2001 4:58:04 PM)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getImportoTotalePerObbligazioni() {
	return importoTotalePerObbligazioni;
}
public String getManagerName() {
	return "CRUDNotaDiCreditoAttivaBP";
}
/**
 * Insert the method's description here.
 * Creation date: (3/22/2002 5:00:25 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
 */
public java.lang.String getManagerOptions() {
	return "VTh";
}
/**
 * Insert the method's description here.
 * Creation date: (3/6/2002 11:37:23 AM)
 * @return Collection
 */
public Collection getModalita() {
	return modalita;
}
/**
 * Insert the method's description here.
 * Creation date: (3/6/2002 11:37:23 AM)
 * @return Rif_modalita_pagamentoBulk
 */
public Rif_modalita_pagamentoBulk getModalita_pagamento() {
	return modalita_pagamento;
}
/**
 * Insert the method's description here.
 * Creation date: (11/16/2001 3:21:35 PM)
 * @return java.util.Hashtable
 */
public ObbligazioniTable getObbligazioni_scadenzarioHash() {
	return obbligazioni_scadenzarioHash;
}
/**
 * Insert the method's description here.
 * Creation date: (11/07/2001 11.07.07)
 * @return it.cnr.jada.bulk.BulkList
 */
public ObbligazioniTable getObbligazioniHash() {

	return getObbligazioni_scadenzarioHash();
}
public java.lang.Long getPg_banca() {
	it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = this.getBanca();
	if (banca == null)
		return null;
	return banca.getPg_banca();
}
/**
 * Insert the method's description here.
 * Creation date: (3/6/2002 11:37:23 AM)
 * @return Collection
 */
public Collection getTermini() {
	return termini;
}
/**
 * Insert the method's description here.
 * Creation date: (3/6/2002 11:37:23 AM)
 * @return Rif_termini_pagamentoBulk
 */
public Rif_termini_pagamentoBulk getTermini_pagamento() {
	return termini_pagamento;
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
public boolean isAbledToInsertBank() {
	
	return !(getCliente() != null && 
		getCliente().getCrudStatus() == OggettoBulk.NORMAL &&
		getModalita_pagamento() != null);
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROcliente() {
	
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROClienteCrudTool() {
	
	return isROClienteSearchTool();
}
public boolean isROclienteForSearch() {
	
	return getCliente() == null ||
			getCliente().getCrudStatus() == OggettoBulk.NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROClienteSearchTool() {
	
	return (super.isROClienteSearchTool() ||
			(getObbligazioni_scadenzarioHash() != null &&
			 !getObbligazioni_scadenzarioHash().isEmpty()));
}
public boolean isROImportoTotalePerObbligazioni() {
	
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROModalitaPagamento() {
	
	return getObbligazioniHash() == null || getObbligazioniHash().isEmpty();
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROTerminiPagamento() {
	
	return getObbligazioniHash() == null || getObbligazioniHash().isEmpty();
}
public Fattura_attiva_rigaBulk removeFromFattura_attiva_dettColl( int indiceDiLinea ) {

	Nota_di_credito_attiva_rigaBulk element = (Nota_di_credito_attiva_rigaBulk)getFattura_attiva_dettColl().get(indiceDiLinea);
	addToDettagliCancellati(element);
	if (element != null && !element.STATO_INIZIALE.equals(element.getStato_cofi())) {
		if (element.getAccertamento_scadenzario() != null)
			removeFromFattura_attiva_accertamentiHash(element);
		if (element.getObbligazione_scadenzario() != null)
			removeFromObbligazioni_scadenzarioHash(element);
	}
	return (Fattura_attiva_rigaBulk)getFattura_attiva_dettColl().remove(indiceDiLinea);
}

public void removeFromObbligazioni_scadenzarioHash(
	Nota_di_credito_attiva_rigaBulk rigaNdC) {

	Vector righeAssociate = (Vector)obbligazioni_scadenzarioHash.get(rigaNdC.getObbligazione_scadenzario());
	if (righeAssociate != null) {
		righeAssociate.remove(rigaNdC);
		if (righeAssociate.isEmpty()) {
			obbligazioni_scadenzarioHash.remove(rigaNdC.getObbligazione_scadenzario());
			addToDocumentiContabiliCancellati(rigaNdC.getObbligazione_scadenzario());
			if (obbligazioni_scadenzarioHash.isEmpty()) {
				resetModalitaCliente();
			}
		}
	} else
		addToDocumentiContabiliCancellati(rigaNdC.getObbligazione_scadenzario());
}

private void resetModalitaCliente() {

	setBanca(null);
	setBanche(null);
	setModalita_pagamento(null);
	setTermini_pagamento(null);
	setTermini(null);
	setModalita(null);
}
/**
 * Insert the method's description here.
 * Creation date: (3/6/2002 11:37:23 AM)
 * @param newBanca BancaBulk
 */
public void setBanca(BancaBulk newBanca) {
	banca = newBanca;
}
/**
 * Insert the method's description here.
 * Creation date: (3/6/2002 11:37:23 AM)
 * @param newBanche Collection
 */
public void setBanche(Collection newBanche) {
	banche = newBanche;
}
public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
	this.getModalita_pagamento().setCd_modalita_pag(cd_modalita_pag);
}
public void setCd_termini_pag(java.lang.String cd_termini_pag) {
	this.getTermini_pagamento().setCd_termini_pag(cd_termini_pag);
}
/**
 * Insert the method's description here.
 * Creation date: (11/16/2001 4:58:04 PM)
 * @param newImportoTotalePerAccertamenti java.math.BigDecimal
 */
public void setImportoTotalePerObbligazioni(java.math.BigDecimal newImportoTotalePerObbligazioni) {
	importoTotalePerObbligazioni = newImportoTotalePerObbligazioni;
}
/**
 * Insert the method's description here.
 * Creation date: (3/6/2002 11:37:23 AM)
 * @param newModalita Collection
 */
public void setModalita(Collection newModalita) {
	modalita = newModalita;
}
/**
 * Insert the method's description here.
 * Creation date: (3/6/2002 11:37:23 AM)
 * @param newModalita_pagamento Rif_modalita_pagamentoBulk
 */
public void setModalita_pagamento(Rif_modalita_pagamentoBulk newModalita_pagamento) {
	modalita_pagamento = newModalita_pagamento;
}
/**
 * Insert the method's description here.
 * Creation date: (11/16/2001 3:21:35 PM)
 * @param newAccertamentiHash java.util.Hashtable
 */
public void setObbligazioni_scadenzarioHash(ObbligazioniTable newObbligazioni_scadenzarioHash) {
	obbligazioni_scadenzarioHash = newObbligazioni_scadenzarioHash;
}
public void setPg_banca(java.lang.Long pg_banca) {
	this.getBanca().setPg_banca(pg_banca);
}
/**
 * Insert the method's description here.
 * Creation date: (3/6/2002 11:37:23 AM)
 * @param newTermini Collection
 */
public void setTermini(Collection newTermini) {
	termini = newTermini;
}
/**
 * Insert the method's description here.
 * Creation date: (3/6/2002 11:37:23 AM)
 * @param newTermini_pagamento Rif_termini_pagamentoBulk
 */
public void setTermini_pagamento(Rif_termini_pagamentoBulk newTermini_pagamento) {
	termini_pagamento = newTermini_pagamento;
}
}
