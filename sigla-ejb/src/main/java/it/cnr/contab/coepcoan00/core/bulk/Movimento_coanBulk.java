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

package it.cnr.contab.coepcoan00.core.bulk;

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.pdcep.bulk.*;
import java.util.*;
import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Movimento_coanBulk extends Movimento_coanBase {
	protected ContoBulk conto = new ContoBulk();
	protected WorkpackageBulk latt = new WorkpackageBulk();
	protected Scrittura_analiticaBulk scrittura = new Scrittura_analiticaBulk();
	public java.lang.Long pgScritturaAnnullata;	
	
	protected TerzoBulk terzo = new TerzoBulk();

	public final static String SEZIONE_DARE  = "D";
	public final static String SEZIONE_AVERE = "A";

	public final static Dictionary sezioneKeys;
	static 
	{
		sezioneKeys = new Hashtable();
		sezioneKeys.put(SEZIONE_DARE,	"Dare");
		sezioneKeys.put(SEZIONE_AVERE,	"Avere");
	};
	public final static Dictionary naturaContoKeys = Voce_epBulk.natura_voce_Keys;	


	public final static String STATO_DEFINITIVO = "D";

	// Attiva
	public java.lang.String attiva;	
	public final static java.util.Dictionary STATO_ATTIVA;	
	public final static String ATTIVA_YES = "Y";
	public final static String ATTIVA_NO = "N";	
	static
	{
		STATO_ATTIVA = new it.cnr.jada.util.OrderedHashtable();
		STATO_ATTIVA.put(ATTIVA_YES,"Y");
		STATO_ATTIVA.put(ATTIVA_NO,"N");
	}		
public Movimento_coanBulk() {
	super();
}
public Movimento_coanBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.String cd_voce_ep,java.lang.Integer esercizio,java.lang.Long pg_movimento,java.lang.Long pg_scrittura,java.lang.String sezione) {
	super(cd_cds,cd_unita_organizzativa,cd_voce_ep,esercizio,pg_movimento,pg_scrittura,sezione);
	setScrittura(new it.cnr.contab.coepcoan00.core.bulk.Scrittura_analiticaBulk(cd_cds,cd_unita_organizzativa,esercizio,pg_scrittura));
}
/**
 * Insert the method's description here.
 * Creation date: (28/08/2003 17.36.13)
 * @return java.lang.String
 */
public java.lang.String getAttiva() {
	it.cnr.contab.coepcoan00.core.bulk.Scrittura_analiticaBulk scrittura = this.getScrittura();
	if (scrittura == null)
		return null;
	return scrittura.getAttiva();
}
public java.lang.String getCd_cds() {
	it.cnr.contab.coepcoan00.core.bulk.Scrittura_analiticaBulk scrittura = this.getScrittura();
	if (scrittura == null)
		return null;
	it.cnr.contab.config00.sto.bulk.CdsBulk cds = scrittura.getCds();
	if (cds == null)
		return null;
	return cds.getCd_unita_organizzativa();
}
public java.lang.String getCd_centro_responsabilita() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt = this.getLatt();
	if (latt == null)
		return null;
	it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita = latt.getCentro_responsabilita();
	if (centro_responsabilita == null)
		return null;
	return centro_responsabilita.getCd_centro_responsabilita();
}
public java.lang.String getCd_funzione() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt = this.getLatt();
	if (latt == null)
		return null;
	return latt.getCd_funzione();
}
public java.lang.String getCd_linea_attivita() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt = this.getLatt();
	if (latt == null)
		return null;
	return latt.getCd_linea_attivita();
}
public java.lang.String getCd_natura() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt = this.getLatt();
	if (latt == null)
		return null;
	return latt.getCd_natura();
}
public java.lang.Integer getCd_terzo() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo = getTerzo();
	if (terzo == null)
		return null;
	return terzo.getCd_terzo();
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.coepcoan00.core.bulk.Scrittura_analiticaBulk scrittura = this.getScrittura();
	if (scrittura == null)
		return null;
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = scrittura.getUo();
	if (uo == null)
		return null;
	return uo.getCd_unita_organizzativa();
}
public java.lang.String getCd_voce_ep() {
	it.cnr.contab.config00.pdcep.bulk.ContoBulk conto = this.getConto();
	if (conto == null)
		return null;
	return conto.getCd_voce_ep();
}
public CdsBulk getCds() {
	it.cnr.contab.coepcoan00.core.bulk.Scrittura_analiticaBulk scrittura = this.getScrittura();
	if (scrittura == null)
		return null;
	return scrittura.getCds();
}
/**
 * @return it.cnr.contab.config00.pdcep.bulk.ContoBulk
 */
public it.cnr.contab.config00.pdcep.bulk.ContoBulk getConto() {
	return conto;
}
public String getDs_terzo() {
	if ( getTerzo() != null )
		return getTerzo().getDenominazione_sede();
	return "";
}
public java.lang.Integer getEsercizio() {
	it.cnr.contab.coepcoan00.core.bulk.Scrittura_analiticaBulk scrittura = this.getScrittura();
	if (scrittura == null)
		return null;
	return scrittura.getEsercizio();
}
/**
 * @return it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
 */
public it.cnr.contab.config00.latt.bulk.WorkpackageBulk getLatt() {
	return latt;
}
public java.lang.Long getPg_scrittura() {
	it.cnr.contab.coepcoan00.core.bulk.Scrittura_analiticaBulk scrittura = this.getScrittura();
	if (scrittura == null)
		return null;
	return scrittura.getPg_scrittura();
}
/**
 * Insert the method's description here.
 * Creation date: (28/08/2003 17.36.13)
 * @return java.lang.Long
 */
public java.lang.Long getPgScritturaAnnullata() {
	it.cnr.contab.coepcoan00.core.bulk.Scrittura_analiticaBulk scrittura = this.getScrittura();
	if (scrittura == null)
		return null;
	return scrittura.getPg_scrittura_annullata();
}
/**
 * @return it.cnr.contab.coepcoan00.core.bulk.Scrittura_analiticaBulk
 */
public Scrittura_analiticaBulk getScrittura() {
	return scrittura;
}
/**
 * Il metodo restituisce il dictionary per la gestione degli stati ATTIVA
 * e NON ATTIVA
 */
public java.util.Dictionary getStato_attivaKeys() 
{
	return STATO_ATTIVA;
}
/**
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo() {
	return terzo;
}
/**
 * Metodo per inizializzare un Oggetto Bulk in fase di inserimento.
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setStato( STATO_DEFINITIVO);
	setDs_movimento(" ");
	return this;
}
/**
 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>
 * in stato <code>SEARCH</code>.
 * Questo metodo viene invocato automaticamente da un 
 * <code>it.cnr.jada.util.action.CRUDBP</code> quando viene inizializzato
 * per la ricerca di un OggettoBulk.
 */
public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
{
	setEsercizio( ((it.cnr.contab.utenze00.bp.CNRUserContext)context.getUserContext()).getEsercizio() );
	setCd_cds( ((it.cnr.contab.utenze00.bp.CNRUserContext)context.getUserContext()).getCd_cds());
	getScrittura().setUo( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa( context ));
	getScrittura().setTerzo( new it.cnr.contab.anagraf00.core.bulk.TerzoBulk());
	return this;
}
public boolean isROConto() {
	return 	getConto() != null &&
	       getConto().getCrudStatus() != UNDEFINED;	

}
public boolean isROLatt() {
	return 	getLatt() != null &&
	       getLatt().getCrudStatus() != UNDEFINED;	

}
/**
 * Insert the method's description here.
 * Creation date: (28/08/2003 17.36.13)
 * @param newAttiva java.lang.String
 */
public void setAttiva(java.lang.String newAttiva) {
	this.getScrittura().setAttiva(newAttiva);
}
public void setCd_cds(java.lang.String cd_cds) {
	this.getScrittura().getCds().setCd_unita_organizzativa(cd_cds);
}
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.getLatt().getCentro_responsabilita().setCd_centro_responsabilita(cd_centro_responsabilita);
}
// metodo modificato perchè oggetto FunzioneBulk non inizializzato
public void setCd_funzione(java.lang.String cd_funzione) {
	this.getLatt().setFunzione( new it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk(cd_funzione));
//	this.getLatt().setCd_funzione(cd_funzione);
}
public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
	this.getLatt().setCd_linea_attivita(cd_linea_attivita);
}
// metodo modificato perchè oggetto NaturaBulk non inizializzato
public void setCd_natura(java.lang.String cd_natura) {
	this.getLatt().setNatura( new it.cnr.contab.config00.pdcfin.bulk.NaturaBulk(cd_natura));
}
public void setCd_terzo(java.lang.Integer cd_terzo) {
	 this.getTerzo().setCd_terzo(cd_terzo);
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getScrittura().getUo().setCd_unita_organizzativa(cd_unita_organizzativa);
}
public void setCd_voce_ep(java.lang.String cd_voce_ep) {
	this.getConto().setCd_voce_ep(cd_voce_ep);
}
public void setCds(CdsBulk cds) {
	this.getScrittura().setCds(cds);
}
/**
 * @param newConto it.cnr.contab.config00.pdcep.bulk.ContoBulk
 */
public void setConto(it.cnr.contab.config00.pdcep.bulk.ContoBulk newConto) {
	conto = newConto;
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.getScrittura().setEsercizio(esercizio);
}
/**
 * @param newLatt it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
 */
public void setLatt(it.cnr.contab.config00.latt.bulk.WorkpackageBulk newLatt) {
	latt = newLatt;
}
public void setPg_scrittura(java.lang.Long pg_scrittura) {
	this.getScrittura().setPg_scrittura(pg_scrittura);
}
/**
 * Insert the method's description here.
 * Creation date: (28/08/2003 17.36.13)
 * @param newPgScritturaAnnullata java.lang.Long
 */
public void setPgScritturaAnnullata(java.lang.Long newPgScritturaAnnullata) {
	this.getScrittura().setPg_scrittura_annullata(newPgScritturaAnnullata);
}
/**
 * @param newScrittura it.cnr.contab.coepcoan00.core.bulk.Scrittura_analiticaBulk
 */
public void setScrittura(Scrittura_analiticaBulk newScrittura) {
	scrittura = newScrittura;
}
/**
 * @param newTerzo it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setTerzo(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newTerzo) {
	terzo = newTerzo;
}
/**
 * Effettua una validazione formale del contenuto dello stato dell'oggetto
 * bulk. Viene invocato da <code>CRUDBP</code> in
 * seguito ad una richiesta di salvataggio.
 * @exception it.cnr.jada.bulk.ValidationException Se la validazione fallisce. 
 *		Contiene il messaggio da visualizzare all'utente per la notifica
 *		dell'errore di validazione.
 * @see it.cnr.jada.util.action.CRUDBP
 */
public void validate() throws ValidationException 
{
	// Controllo sul campo CONTO
	if ( getCd_voce_ep() == null )
		throw new ValidationException( "E' necessario selezionare il Conto per ogni movimento inserito");
		
	// Controllo sul campo IMPORTO
	if ( getIm_movimento() == null )
		throw new ValidationException( "E' necessario inserire l'Importo per ogni movimento inserito");

	if ( getIm_movimento().compareTo( new java.math.BigDecimal(0)) <= 0 )
		throw new ValidationException( "L'Importo movimento deve essere maggiore di zero");	
}
}
