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

package it.cnr.contab.utenze00.bulk;

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;

import java.util.*;

/**
 * Definisce un Template di Utente del sistema Gestione Contabilita' CNR (tipo utente = COMUNE e flag
 * utente template = TRUE)  
 *	
 */
public class UtenteTemplateBulk extends UtenteBulk 
{

	private Unita_organizzativaBulk	unita_org_per_accesso = new Unita_organizzativaBulk();
	private Unita_organizzativaBulk	unita_org_per_ruolo   = new Unita_organizzativaBulk();

	private it.cnr.jada.bulk.BulkList	utente_unita_accessi = new it.cnr.jada.bulk.BulkList();
	private it.cnr.jada.bulk.BulkList	utente_unita_ruoli   = new it.cnr.jada.bulk.BulkList();
	private it.cnr.jada.bulk.BulkList	utente_firma_dettaglio   = new it.cnr.jada.bulk.BulkList();

	private List utente_unita_accessi_disponibili = new Vector();;	
	private List utente_unita_ruoli_disponibili   = new Vector();;

	private List accessi             = new it.cnr.jada.util.Collect(utente_unita_accessi,"accesso");
	private List ruoli               = new it.cnr.jada.util.Collect(utente_unita_ruoli,"ruolo");
	private List accessi_disponibili = new it.cnr.jada.util.Collect(utente_unita_accessi_disponibili,"accesso");
	private List ruoli_disponibili   = new it.cnr.jada.util.Collect(utente_unita_ruoli_disponibili,"ruolo");		

	public UtenteTemplateBulk() {
		super();
		inizializza();	
	}
	public UtenteTemplateBulk(java.lang.String cd_utente) {
		super(cd_utente);
		inizializza();	
	}


	/**
	 * Aggiunge una nuova associazione utente-unita-accesso (Utente_unita_accessoBulk) alla lista definita per l'utente
	 * inizializzandone alcuni campi e rimuove tale associazione dalla lista di quelle ancora disponibili per l'utente
	 * @param index indice della collezione di utente-unita-accesso disponibili da cui rimuovere l'associazione
	 * @return Utente_unita_accessoBulk l'associazione aggiunta
	 */

	public Utente_unita_accessoBulk addToUtente_unita_accessi(int index) {
		Utente_unita_accessoBulk uua = (Utente_unita_accessoBulk)this.utente_unita_accessi_disponibili.remove(index);
		uua.setCd_unita_organizzativa( unita_org_per_accesso.getCd_unita_organizzativa() );
		uua.setCd_utente( getCd_utente() );
		//		uua.setCd_accesso( uua.getAccesso().getCd_accesso());
		this.utente_unita_accessi.add(uua);
		return uua;
	}

	/**
	 * Aggiunge una nuova associazione utente-unita-accesso (Utente_unita_accessoBulk) alla lista 
	 * definita per l'utente 
	 * @param uua Utente_unita_accessoBulk da aggiungere
	 * @return int numero di associazioni utente-unita-accesso
	 */

	public int addToUtente_unita_accessi(Utente_unita_accessoBulk uua) {
		this.utente_unita_accessi.add(uua);
		return this.utente_unita_accessi.size()-1;
	}
	/**
	 * Aggiunge una nuova associazione utente-unita-ruolo (Utente_unita_ruoloBulk) alla lista definita per l'utente
	 * inizializzandone alcuni campi e rimuove tale associazione dalla lista di quelle ancora disponibili per l'utente
	 * @param index indice della collezione di utente-unita-ruolo disponibili da cui rimuovere l'associazione
	 * @return Utente_unita_ruoloBulk l'associazione aggiunta
	 */

	public Utente_unita_ruoloBulk addToUtente_unita_ruoli(int index) {
		Utente_unita_ruoloBulk uur = (Utente_unita_ruoloBulk)this.utente_unita_ruoli_disponibili.remove(index);
		uur.setCd_unita_organizzativa( unita_org_per_ruolo.getCd_unita_organizzativa() );
		uur.setCd_utente( getCd_utente() );		
		this.utente_unita_ruoli.add(uur);
		return uur;
	}

	/**
	 * Aggiunge una nuova associazione utente-unita-ruolo (Utente_unita_ruoloBulk) alla lista 
	 * definita per l'utente 
	 * @param uur Utente_unita_ruoloBulk da aggiungere
	 * @return int numero di associazioni utente-unita-ruolo
	 */

	public int addToUtente_unita_ruoli(Utente_unita_ruoloBulk uur) {
		this.utente_unita_ruoli.add(uur);
		return this.utente_unita_ruoli.size()-1;
	}
	/**
	 * @return java.util.List
	 */
	public java.util.List getAccessi() {
		return accessi;
	}
	/**
	 * @return java.util.List
	 */
	public java.util.List getAccessi_disponibili() {
		return accessi_disponibili;
	}
	/**
	 * Restituisce la collezione di Utente_unita_accessoBulk e di Utente_unita_ruoloBulk associate all'utente per 
	 * renderle persistenti contestualmente alla gestione della persistenza dell'utene stesso
	 */

	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] { 
				utente_unita_accessi,
				utente_unita_ruoli,
				getUtente_indirizzi_mail(),
				utente_firma_dettaglio};
	}
	/**
	 * @return java.util.List
	 */
	public java.util.List getRuoli() {
		return ruoli;
	}
	/**
	 * @return java.util.List
	 */
	public java.util.List getRuoli_disponibili() {
		return ruoli_disponibili;
	}
	/**
	 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_org_per_accesso() {
		return unita_org_per_accesso;
	}
	/**
	 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_org_per_ruolo() {
		return unita_org_per_ruolo;
	}
	/**
	 * @return it.cnr.jada.bulk.BulkList
	 */
	public it.cnr.jada.bulk.BulkList getUtente_unita_accessi() {
		return utente_unita_accessi;
	}
	/**
	 * @return java.util.List
	 */
	public java.util.List getUtente_unita_accessi_disponibili() {
		return utente_unita_accessi_disponibili;
	}
	/**
	 * @return it.cnr.jada.bulk.BulkList
	 */
	public it.cnr.jada.bulk.BulkList getUtente_unita_ruoli() {
		return utente_unita_ruoli;
	}
	/**
	 * @return java.util.List
	 */
	public java.util.List getUtente_unita_ruoli_disponibili() {
		return utente_unita_ruoli_disponibili;
	}
	/**
	 * Inizializza gli attributi specifici dell'Utente Template
	 */

	private void inizializza() 
	{
		setTi_utente(UtenteHome.TIPO_COMUNE);
		setFl_utente_templ( new Boolean( true ) );
	}
	/**
	 * Determina quando abilitare o meno nell'interfaccia utente il campo unita_org_per_accesso
	 * @return boolean true quando il campo deve essere disabilitato
	 */

	public boolean isROFind_unita_org_per_accesso() {
		return  ( getCrudStatus() == UNDEFINED ); // l'utente non è ancora stato ricercato
	}
	/**
	 * Determina quando abilitare o meno nell'interfaccia utente il campo unita_org_per_ruolo
	 * @return boolean true quando il campo deve essere disabilitato
	 */

	public boolean isROFind_unita_org_per_ruolo() {
		return  ( getCrudStatus() == UNDEFINED ); // l'utente non è ancora stato ricercato
	}
	/**
	 * Determina quando abilitare o meno nell'interfaccia utente il campo unita_org_per_accesso
	 * @return boolean true quando il campo deve essere disabilitato
	 */

	public boolean isROUnita_org_per_accesso() {

		return isROFind_unita_org_per_accesso() || unita_org_per_accesso == null || unita_org_per_accesso.getCrudStatus() == NORMAL;
	}
	/**
	 * Determina quando abilitare o meno nell'interfaccia utente il campo unita_org_per_ruolo
	 * @return boolean true quando il campo deve essere disabilitato
	 */

	public boolean isROUnita_org_per_ruolo() {
		return isROFind_unita_org_per_ruolo() || unita_org_per_ruolo == null || unita_org_per_ruolo.getCrudStatus() == NORMAL;
	}
	/**
	 * Rimuove una associazione utente-unita-accesso (Utente_unita_accessoBulk) alla lista definita per l'utente
	 * e aggiunge tale associazione alla lista di quelle ancora disponibili per l'utente
	 * @param index indice della collezione di utente-unita-accesso assegnati all'utente da cui rimuovere l'associazione
	 * @return Utente_unita_accessoBulk l'associazione rimossa
	 */

	public Utente_unita_accessoBulk removeFromUtente_unita_accessi(int index) {
		Utente_unita_accessoBulk uua = (Utente_unita_accessoBulk)this.utente_unita_accessi.remove(index);
		this.utente_unita_accessi_disponibili.add(uua);
		return uua;
	}
	/**
	 * Rimuove una associazione utente-unita-ruolo (Utente_unita_ruoloBulk) alla lista definita per l'utente
	 * e aggiunge tale associazione alla lista di quelle ancora disponibili per l'utente
	 * @param index indice della collezione di utente-unita-ruolo assegnati all'utente da cui rimuovere l'associazione
	 * @return Utente_unita_ruoloBulk l'associazione rimossa
	 */

	public Utente_unita_ruoloBulk removeFromUtente_unita_ruoli(int index) {
		Utente_unita_ruoloBulk uur = (Utente_unita_ruoloBulk)this.utente_unita_ruoli.remove(index);
		this.utente_unita_ruoli_disponibili.add(uur);
		return uur;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * 
	 *
	 */
	public void resetAccessi()
	{
		utente_unita_accessi.clear();
		utente_unita_accessi_disponibili.clear();

	}
	/**
	 * Reset dei ruoli assegnati e disponibili
	 */
	public void resetRuoli()
	{
		utente_unita_ruoli.clear();
		utente_unita_ruoli_disponibili.clear();


	}
	/**
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'accessi_disponibili'
	 *
	 * @param newAccessi_disponibili	Il valore da assegnare a 'accessi_disponibili'
	 */
	public void setAccessi_disponibili(java.util.List newAccessi_disponibili) 
	{
		for (java.util.Iterator i = newAccessi_disponibili.iterator();i.hasNext();) 
		{
			AccessoBulk accesso = (AccessoBulk)i.next();
			if (this.accessi.contains(accesso)) continue;
			Utente_unita_accessoBulk uua = new Utente_unita_accessoBulk();
			uua.setAccesso( accesso );
			utente_unita_accessi_disponibili.add(uua);
		}
	}
	/**
	 * @param newRuoli_disponibili java.util.List
	 */
	public void setRuoli_disponibili(java.util.List newRuoli_disponibili) 
	{
		for (java.util.Iterator i = newRuoli_disponibili.iterator();i.hasNext();) 
		{
			RuoloBulk ruolo = (RuoloBulk)i.next();
			if (this.ruoli.contains(ruolo)) continue;
			Utente_unita_ruoloBulk uur = new Utente_unita_ruoloBulk();
			uur.setRuolo( ruolo );
			utente_unita_ruoli_disponibili.add(uur);
		}
	}
	public void setUnita_org_per_accesso(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_org_per_accesso) {
		unita_org_per_accesso = newUnita_org_per_accesso;
	}
	public void setUnita_org_per_ruolo(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_org_per_ruolo) {
		unita_org_per_ruolo = newUnita_org_per_ruolo;
	}
	/**
	 * Aggiunge una nuova associazione utente-firma-dettaglio (UtenteFirmaDettaglioBulk) alla lista definita per l'utente
	 */	
	public int addToUtente_firma_dettaglio(UtenteFirmaDettaglioBulk dett) {
		dett.setUtente(this);
		getUtente_firma_dettaglio().add(dett);
		return getUtente_firma_dettaglio().size()-1;
	}
	public it.cnr.jada.bulk.BulkList getUtente_firma_dettaglio() {
		return utente_firma_dettaglio;
	}
	public void setUtente_firma_dettaglio(
			it.cnr.jada.bulk.BulkList utente_firma_dettaglio) {
		this.utente_firma_dettaglio = utente_firma_dettaglio;
	}
	public UtenteFirmaDettaglioBulk removeFromUtente_firma_dettaglio(int index) {
		UtenteFirmaDettaglioBulk dett = (UtenteFirmaDettaglioBulk)getUtente_firma_dettaglio().remove(index);
		return dett;
	}
}