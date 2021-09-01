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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import java.util.*;


import it.cnr.contab.compensi00.tabrif.bulk.Tipo_prestazione_compensoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;

import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class MinicarrieraBulk 
	extends MinicarrieraBase 
	implements IDefferUpdateSaldi {

	public static final Dictionary STATI;
	public static final Dictionary STATI_ASS_COMPENSO;
	public final static Dictionary TIPI_COMPENSO;
	public static Dictionary ANTICIPO_POSTICIPO;

	public static final String STATO_ATTIVA = "A";
	public static final String STATO_SOSPESA = "S";
	public static final String STATO_RIPRISTINATA = "R";
	public static final String STATO_RINNOVATA = "P";
	public static final String STATO_CESSATA = "C";

	public static final String STATO_NON_ASS_COMPENSO = "N";
	public static final String STATO_PARZIALE_ASS_COMPENSO = "P";
	public static final String STATO_TOTALE_ASS_COMPENSO = "T";

	public static final String TIPO_NESSUNO = "N";
	public static final String TIPO_ANTICIPO = "A";
	public static final String TIPO_POSTICIPO = "P";

	static {
		STATI = new it.cnr.jada.util.OrderedHashtable();
		STATI.put(STATO_ATTIVA, "Attiva");
		STATI.put(STATO_SOSPESA, "Sospesa");
		STATI.put(STATO_RIPRISTINATA, "Ripristinata");
		STATI.put(STATO_RINNOVATA, "Rinnovata");
		STATI.put(STATO_CESSATA, "Cessata");

		STATI_ASS_COMPENSO = new it.cnr.jada.util.OrderedHashtable();
		STATI_ASS_COMPENSO.put(STATO_NON_ASS_COMPENSO, "Non associata a compenso");
		STATI_ASS_COMPENSO.put(STATO_PARZIALE_ASS_COMPENSO, "Parzialmente associata a compenso");
		STATI_ASS_COMPENSO.put(STATO_TOTALE_ASS_COMPENSO, "Totalmente associata a compenso");

		TIPI_COMPENSO = new it.cnr.jada.util.OrderedHashtable();
		for (TipoIVA tipoIVA : TipoIVA.values()) {
			TIPI_COMPENSO.put(tipoIVA.value(), tipoIVA.label());
		}
	}
	
	private java.lang.Long pgMinicarrieraPerClone;

	private it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipo_rapporto = null;
	private it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk tipo_trattamento = null;
	private it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalita_pagamento = null;
	private it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk termini_pagamento = null;
	private it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = null;
	private V_terzo_per_compensoBulk percipiente = null;

	private BulkList minicarriera_rate = new BulkList();
	private Collection dettagliCancellati = null;
	private java.util.Collection modalita;
	private java.util.Collection termini;
	private java.util.Collection tipiRapporto;
	private java.util.Collection tipiTrattamento;
	private MinicarrieraBulk minicarriera_origine = null;
	private PrimaryKeyHashMap deferredSaldi = new PrimaryKeyHashMap();
	private boolean aliquotaCalcolata = false;
	
	private Incarichi_repertorioBulk  incarichi_repertorio;
	private boolean visualizzaIncarico = true;
	
	private Tipo_prestazione_compensoBulk tipoPrestazioneCompenso;
	private java.util.Collection tipiPrestazioneCompenso;
	
	private boolean visualizzaPrestazione = true;
	
public MinicarrieraBulk() {
	super();
}
public MinicarrieraBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_minicarriera) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_minicarriera);
}
/**
 * Aggiunge alla PrimaryKeyHashMap dei saldi il documento contabile (in chiave)
 * e i relativi valori di business (in valore). Se la mappa non esiste viene creata
 */
 
public void addToDefferredSaldi(
	it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docCont, 
	java.util.Map values) {

	if (docCont != null) {
		if (deferredSaldi == null)
			deferredSaldi = new PrimaryKeyHashMap();
		if (!deferredSaldi.containsKey(docCont))
			deferredSaldi.put(docCont, values);
		else {
			Map firstValues = (Map)deferredSaldi.get(docCont);
			deferredSaldi.remove(docCont);
			deferredSaldi.put(docCont, firstValues);
		}
	}
}
/**
 * Richiesto ma non usato
 */
 
public void addToDettagliCancellati(Minicarriera_rataBulk rata) {

}
/**
 * Crea, inizializza e aggiunge una rata su richiesta utente (bottone nuovo)
 */
 
public int addToMinicarriera_rate(Minicarriera_rataBulk rata) {

	rata.setMinicarriera(this);
	
	long max = 0;
	for (Iterator i = getMinicarriera_rate().iterator(); i.hasNext();) {
		long prog = ((Minicarriera_rataBulk)i.next()).getPg_rata().longValue();
		if (prog > max) max = prog;
	}
	rata.setPg_rata(new Long(max+1));

	rata.initialize();
	rata.setUser(getUser());

	int realSize = getMinicarriera_rate().size()-1;
	java.sql.Timestamp dataUltimaFine = ((Minicarriera_rataBulk)getMinicarriera_rate().get(realSize)).getDt_fine_rata();
	java.sql.Timestamp dataInizio = !getMinicarriera_rate().isEmpty() ? incrementaData(dataUltimaFine) : getDataOdierna();
	rata.setDt_inizio_rata(dataInizio);
	rata.setDt_fine_rata(dataInizio);
	rata.setDt_scadenza(dataInizio);
	
	getMinicarriera_rate().add(rata);
	removeFromDettagliCancellati(rata);
	
	if (STATO_TOTALE_ASS_COMPENSO.equalsIgnoreCase(getStato_ass_compenso()))
		setStato_ass_compenso(STATO_PARZIALE_ASS_COMPENSO);
		
	return getMinicarriera_rate().size()-1;
}
/**
 * Restituisce un boolean 'true' se il tipo anticipo/posticipo non è modificabile
 */
 
private boolean basicROImportiIrpef() {

	return	isSospesa() ||
			isCessata() ||
			isAssociataACompensoConTassazioneSeparata() ||
			(getFl_tassazione_separata() == null || !getFl_tassazione_separata().booleanValue());
}
/**
 * Calcola il totale di tutte le rate
 */

public java.math.BigDecimal calcolaTotaleRate() {

	return calcolaTotaleRate(getMinicarriera_rate());
}
/**
 * Calcola il totale delle rate selezionate
 */
 
public java.math.BigDecimal calcolaTotaleRate(List rateSelezionate) {

	java.math.BigDecimal tot = new java.math.BigDecimal(0);
	if (rateSelezionate != null)
		for (Iterator i = rateSelezionate.iterator(); i.hasNext();)
			tot = tot.add(((Minicarriera_rataBulk)i.next()).getIm_rata());
	return tot;
}
/**
 * Decrementa di un giorno la data 'data'
 */
 
public static java.sql.Timestamp decrementaData(java.sql.Timestamp data){

	java.util.GregorianCalendar gc = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
	gc.setTime(data);
	
	gc.set(java.util.Calendar.HOUR, 0);
	gc.set(java.util.Calendar.MINUTE, 0);
	gc.set(java.util.Calendar.SECOND, 0);
	gc.set(java.util.Calendar.MILLISECOND, 0);
	gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
	
	gc.add(java.util.Calendar.DATE, -1);
	return new java.sql.Timestamp(gc.getTime().getTime());
}
/**
 * Restituisce l'anno della data 'data'. Se data non è impostata restituisce l'anno corrente
 */
 
public static int getAnno(java.sql.Timestamp data) {

	try {
		java.util.Calendar gc = java.util.Calendar.getInstance();
		gc.setTime((data != null) ? data : it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp());
		gc.set(java.util.Calendar.HOUR, 0);
		gc.set(java.util.Calendar.MINUTE, 0);
		gc.set(java.util.Calendar.SECOND, 0);
		gc.set(java.util.Calendar.MILLISECOND, 0);
		gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
		return gc.get(Calendar.YEAR);
	} catch (javax.ejb.EJBException e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}	
}
/**
 * Insert the method's description here.
 * Creation date: (6/21/2002 1:40:55 PM)
 * @return it.cnr.contab.anagraf00.core.bulk.BancaBulk
 */
public it.cnr.contab.anagraf00.core.bulk.BancaBulk getBanca() {
	return banca;
}
/**
 * Insert the method's description here.
 * Creation date: (19/02/2002 14.22.21)
 * @return it.cnr.contab.anagraf00.core.bulk.BancaBulk
 */
public BulkCollection[] getBulkLists() {
	return new it.cnr.jada.bulk.BulkCollection[] { getMinicarriera_rate() };
}
public java.lang.String getCd_cds_minicarriera_ori() {
	it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk minicarriera_origine = this.getMinicarriera_origine();
	if (minicarriera_origine == null)
		return null;
	return minicarriera_origine.getCd_cds();
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
public java.lang.Integer getCd_terzo() {
	it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk percipiente = this.getPercipiente();
	if (percipiente == null)
		return super.getCd_terzo();
	return percipiente.getCd_terzo();
}
public java.lang.String getCd_tipo_rapporto() {
	it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipo_rapporto = this.getTipo_rapporto();
	if (tipo_rapporto == null)
		return null;
	return tipo_rapporto.getCd_tipo_rapporto();
}
public java.lang.String getCd_trattamento() {
	it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk tipo_trattamento = this.getTipo_trattamento();
	if (tipo_trattamento == null)
		return super.getCd_trattamento();
	return tipo_trattamento.getCd_trattamento();
}
public java.lang.String getCd_uo_minicarriera_ori() {
	it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk minicarriera_origine = this.getMinicarriera_origine();
	if (minicarriera_origine == null)
		return null;
	return minicarriera_origine.getCd_unita_organizzativa();
}
/**
 * Restituisce la data odierna
 */
 
public static java.sql.Timestamp getDataOdierna() {

	try {
		return getDataOdierna(it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp());
	} catch (javax.ejb.EJBException e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}
}
/**
 * Restituisce la data 'dataOdierna' com ore, minuti e secondi impostati a 0
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
 * Creation date: (5/15/2002 10:50:29 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
public it.cnr.jada.bulk.PrimaryKeyHashMap getDefferredSaldi() {
	return deferredSaldi;
}
/**
 * Restituisce se esiste il documento contabile con key = a docCont
 */
 
public IDocumentoContabileBulk getDefferredSaldoFor(IDocumentoContabileBulk docCont) {
	
	if (docCont != null && deferredSaldi != null)
		for (Iterator i = deferredSaldi.keySet().iterator(); i.hasNext();) {
			IDocumentoContabileBulk key = (IDocumentoContabileBulk)i.next();
			if (((OggettoBulk)docCont).equalsByPrimaryKey((OggettoBulk)key))
				return key;
		}
	return null;	
}
/**
 * Insert the method's description here.
 * Creation date: (27/05/2002 12.54.21)
 * @return java.util.Vector
 */
public java.util.Vector getDettagliCancellati() {
	return null;
}
public java.lang.Integer getEsercizio_minicarriera_ori() {
	it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk minicarriera_origine = this.getMinicarriera_origine();
	if (minicarriera_origine == null)
		return null;
	return minicarriera_origine.getEsercizio();
}
/**
 * Insert the method's description here.
 * Creation date: (6/24/2002 3:36:33 PM)
 * @return it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk
 */
public MinicarrieraBulk getMinicarriera_origine() {
	return minicarriera_origine;
}
/**
 * Insert the method's description here.
 * Creation date: (6/21/2002 3:59:28 PM)
 * @return it.cnr.jada.bulk.BulkList
 */
public it.cnr.jada.bulk.BulkList getMinicarriera_rate() {
	return minicarriera_rate;
}
/**
 * Insert the method's description here.
 * Creation date: (19/02/2002 14.24.54)
 * @return java.util.Collection
 */
public java.util.Collection getModalita() {
	return modalita;
}
/**
 * Insert the method's description here.
 * Creation date: (6/21/2002 1:39:04 PM)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk getModalita_pagamento() {
	return modalita_pagamento;
}
/**
 * Insert the method's description here.
 * Creation date: (6/21/2002 2:27:05 PM)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public V_terzo_per_compensoBulk getPercipiente() {
	return percipiente;
}
public java.lang.Long getPg_banca() {
	it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = this.getBanca();
	if (banca == null)
		return null;
	return banca.getPg_banca();
}
public java.lang.Long getPg_minicarriera_ori() {
	it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk minicarriera_origine = this.getMinicarriera_origine();
	if (minicarriera_origine == null)
		return null;
	return minicarriera_origine.getPg_minicarriera();
}
/**
 * Insert the method's description here.
 * Creation date: (6/21/2002 5:27:58 PM)
 * @return java.lang.Long
 */
public java.lang.Long getPgMinicarrieraPerClone() {
	return pgMinicarrieraPerClone;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 12.40.29)
 * @return java.lang.Long
 */
public java.lang.Long getPgMinicarrieraPos() {

	if (getPg_minicarriera() !=null && getPg_minicarriera().intValue() < 0)
		return null;

	return getPg_minicarriera();
}
/**
 * Restituisce un vettore di rate contenente tutte e sole le rate associate ad un
 * compenso
 */
 
public Vector getRateAssociateACompenso() {

	Vector associate = new Vector();
	if (getMinicarriera_rate() != null)
		for (Iterator i = getMinicarriera_rate().iterator(); i.hasNext();) {
			Minicarriera_rataBulk associata = (Minicarriera_rataBulk)i.next();
			if (associata.getCompenso() != null)
				associate.add(associata);
		}
	return associate;
}
/**
 * Restituisce un dictionary di stati validi per l'associazione a compenso
 */
 
public java.util.Dictionary getStato_ass_compensoKeys() {
	return STATI_ASS_COMPENSO;
}
/**
 * Insert the method's description here.
 * Creation date: (26/02/2002 11.19.49)
 * @return java.util.Collection
 */
public java.util.Dictionary getStatoKeys() {
	return STATI;
}
/**
 * Insert the method's description here.
 * Creation date: (19/02/2002 14.25.07)
 * @return java.util.Collection
 */
public java.util.Collection getTermini() {
	return termini;
}
/**
 * Insert the method's description here.
 * Creation date: (6/21/2002 1:39:57 PM)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk getTermini_pagamento() {
	return termini_pagamento;
}
/**
 * Insert the method's description here.
 * Creation date: (19/02/2002 14.25.23)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public TerzoBulk getTerzo() {

	if (getPercipiente() == null)
		return null;
	return getPercipiente().getTerzo();
}
public java.lang.String getTi_anagrafico() {
	it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk percipiente = this.getPercipiente();
	if (percipiente == null)
		return super.getTi_anagrafico();
	return percipiente.getTi_dipendente_altro();
}
public java.lang.String getTi_anagraficoForSearch() {
	
	if (getTi_anagrafico() == null)
		return "T";
	return getTi_anagrafico();
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 11.49.26)
 * @return java.util.Dictionary
 */
public java.util.Dictionary getTi_anagraficoKeys() {
	return Tipo_rapportoBulk.DIPENDENTE_ALTRO;
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 11.49.26)
 * @return java.util.Dictionary
 */
public java.util.Dictionary getTi_anagraficoKeysForSearch() {
	it.cnr.jada.util.OrderedHashtable	DIPENDENTE_ALTRO_FOR_SEARCH = new it.cnr.jada.util.OrderedHashtable();
	DIPENDENTE_ALTRO_FOR_SEARCH.put("T","Tutti");
	DIPENDENTE_ALTRO_FOR_SEARCH.put(Tipo_rapportoBulk.DIPENDENTE,"Dipendenti");
	DIPENDENTE_ALTRO_FOR_SEARCH.put(Tipo_rapportoBulk.ALTRO,"Altri soggetti");
	return DIPENDENTE_ALTRO_FOR_SEARCH;
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 11.49.26)
 * @return java.util.Dictionary
 */
public java.util.Dictionary getTi_anticipo_posticipoKeys() {

	if (ANTICIPO_POSTICIPO == null) {
		ANTICIPO_POSTICIPO = new it.cnr.jada.util.OrderedHashtable();
		ANTICIPO_POSTICIPO.put(TIPO_NESSUNO, "Nessuno");
		ANTICIPO_POSTICIPO.put(TIPO_ANTICIPO, "Anticipo");
		ANTICIPO_POSTICIPO.put(TIPO_POSTICIPO, "Posticipo");
	}
	return ANTICIPO_POSTICIPO;
}
/**
 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di compenso.
 *
 * @return java.util.Dictionary
 */

public Dictionary getTi_istituz_commercKeys() {
	return TIPI_COMPENSO;
}
/**
 * Insert the method's description here.
 * Creation date: (26/02/2002 11.19.49)
 * @return java.util.Collection
 */
public java.util.Collection getTipiRapporto() {
	return tipiRapporto;
}
/**
 * Insert the method's description here.
 * Creation date: (26/02/2002 11.58.06)
 * @return java.util.Collection
 */
public java.util.Collection getTipiTrattamento() {
	return tipiTrattamento;
}
/**
 * Insert the method's description here.
 * Creation date: (6/21/2002 1:35:19 PM)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk getTipo_rapporto() {
	return tipo_rapporto;
}
/**
 * Insert the method's description here.
 * Creation date: (6/21/2002 1:35:55 PM)
 * @return it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk
 */
public it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk getTipo_trattamento() {
	return tipo_trattamento;
}
/**
 * Restituisce un boolean 'true' nel caso in cui almeno una rata sia associata a
 * compenso
 */
 
public boolean hasRateAssociateACompenso() {

	return !getRateAssociateACompenso().isEmpty();
}
/**
 * Incrementa di un giorno la data 'data'
 */
 
public static java.sql.Timestamp incrementaData(java.sql.Timestamp data){

	java.util.GregorianCalendar gc = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
	gc.setTime(data);
	gc.set(java.util.Calendar.HOUR, 0);
	gc.set(java.util.Calendar.MINUTE, 0);
	gc.set(java.util.Calendar.SECOND, 0);
	gc.set(java.util.Calendar.MILLISECOND, 0);
	gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
	gc.add(java.util.Calendar.DATE, 1);
	
	return new java.sql.Timestamp(gc.getTime().getTime());
}
/**
 * inizializza il modello per la modifica
 */
 
public OggettoBulk initializeForEdit(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForEdit(bp,context);

	setTipo_trattamento(new Tipo_trattamentoBulk());
	
	return this;
}
/**
 * inizializza il modello per la ricerca libera
 */
 
public OggettoBulk initializeForFreeSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForFreeSearch(bp,context);

	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = null;
	unita_organizzativa =	it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
	setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	setCd_unita_organizzativa(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());

	setPercipiente(new V_terzo_per_compensoBulk());
//	setTi_anagrafico(Tipo_rapportoBulk.ALTRO);

	setMinicarriera_origine(new MinicarrieraBulk());
	
	return this;
}
/**
 * inizializza il modello per l'inserimento
 */
 
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForInsert(bp,context);

	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = null;
	unita_organizzativa =	it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
	setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	setCd_unita_organizzativa(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());

	reset(context);

	return this;
}
/**
 * inizializza il modello per la ricerca
 */
 
public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForSearch(bp,context);

	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = null;
	unita_organizzativa =	it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
	setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	setCd_unita_organizzativa(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());

	setPercipiente(new V_terzo_per_compensoBulk());
//	setTi_anagrafico(Tipo_rapportoBulk.ALTRO);

	setMinicarriera_origine(new MinicarrieraBulk());
	
	return this;
}
/**
 * Restituisce un boolean 'true' nel caso in cui NON posso ricercare banche
 */
 
public boolean isAbledToInsertBank() {

	return !(getTerzo()!= null &&
		getModalita_pagamento() != null &&
		!isROPercipiente());
}
/**
 * Insert the method's description here.
 * Creation date: (1/8/2003 12:25:41 PM)
 * @return boolean
 */
public boolean isAliquotaCalcolata() {
	return aliquotaCalcolata;
}
/**
 * Restituisce un boolean 'true' se la minicarriera è associata parzialmente o
 * totalmente a compenso
 */
 
public boolean isAssociataACompenso() {
	
	return getStato_ass_compenso() != null && 
			(STATO_TOTALE_ASS_COMPENSO.equalsIgnoreCase(getStato_ass_compenso()) ||
			STATO_PARZIALE_ASS_COMPENSO.equalsIgnoreCase(getStato_ass_compenso()));
}
/**
 * Restituisce un boolean 'true' se la minicarriera è associata parzialmente o
 * totalmente a compenso
 */
 
public boolean isAssociataACompensoConTassazioneSeparata() {

	if (!isAssociataACompenso()) return false;
	
	if (getMinicarriera_rate() != null)
		for (Iterator i = getMinicarriera_rate().iterator(); i.hasNext();) {
			Minicarriera_rataBulk unaRata = (Minicarriera_rataBulk)i.next();
			if (unaRata.isAssociataACompenso() &&
				isRataATassazioneSeparata(unaRata))
				return true;
		}
	return false;
	
}
/**
 * Restituisce un boolean 'true' se la minicarriera è in stato attivo
 */
 
public boolean isAttiva() {
	
	return getStato() == null || STATO_ATTIVA.equalsIgnoreCase(getStato());
}
/**
 * Restituisce un boolean 'true' se la minicarriera è in stato cessato
 */
 
public boolean isCessata() {
	
	return getStato() != null && STATO_CESSATA.equalsIgnoreCase(getStato());
}
/**
 * Restituisce un boolean 'true' se la minicarriera è in stato attivo o sospeso.
 * permette la modifica in questo caso del modello
 */
 
public boolean isEditable() {
	return isAttiva() || isSospesa();
}
/**
 * Restituisce un boolean 'true' se nemmeno una rata è associata a compenso
 */
 
public boolean isNonAssociataACompenso() {
	
	return !isAssociataACompenso() && !isParzialmenteAssociataACompenso();
}
/**
 * Restituisce un boolean 'true' se la minicarriera è associata parzialmente
 * a compenso
 */
 
public boolean isParzialmenteAssociataACompenso() {
	
	return getStato_ass_compenso() != null && STATO_PARZIALE_ASS_COMPENSO.equalsIgnoreCase(getStato_ass_compenso());
}
public boolean isRataATassazioneSeparata(Minicarriera_rataBulk rata) {

	if (rata == null || rata.getDt_fine_rata() == null)
		return false;
	return	getFl_tassazione_separata() != null &&
			getFl_tassazione_separata().booleanValue() &&
			getAnno(rata.getDt_fine_rata()) < getEsercizio().intValue();
}
/**
 * Restituisce un boolean 'true' se la minicarriera è in stato rinnovato
 */
 
public boolean isRinnovata() {
	
	return getStato() != null && STATO_RINNOVATA.equalsIgnoreCase(getStato());
}
/**
 * Restituisce un boolean 'true' se la minicarriera è in stato ripristinato
 */
 
public boolean isRipristinata() {
	
	return getStato() != null && STATO_RIPRISTINATA.equalsIgnoreCase(getStato());
}
/**
 * Restituisce un boolean 'true' se il tipo anticipo/posticipo non è modificabile
 */
 
public boolean isROBottoniImportiIrpef() {

	return	basicROImportiIrpef();
}
/**
 * Restituisce un boolean 'true' se la data cessazione non è modificabile
 */
 
public boolean isRODtCessazione() {

	return (getCrudStatus() != OggettoBulk.NORMAL && !isToBeUpdated()) ||
			(!isAttiva() && !isSospesa()) ||
			(getPgMinicarrieraPos() == null && getMinicarriera_origine() != null);
}
/**
 * Restituisce un boolean 'true' se la data inizio validità non è modificabile
 */
 
public boolean isRODtInizioFineValidita() {

	return //(getMinicarriera_rate() != null && !getMinicarriera_rate().isEmpty()) ||
			isROField() || hasRateAssociateACompenso();
}
/**
 * Restituisce un boolean 'true' se la data inizio validità non è modificabile
 */
 
public boolean isRODtRegistrazione() {

	return hasRateAssociateACompenso();
}
/**
 * Restituisce un boolean 'true' se la data rinnovo non è modificabile
 */
 
public boolean isRODtRinnovo() {

	return (getCrudStatus() != OggettoBulk.NORMAL && !isToBeUpdated()) || 
			!isAttiva() ||
			(getPgMinicarrieraPos() == null && getMinicarriera_origine() != null);
			
	// L'ultima condizione serve quando sono in creazione di una minicarriera con
	// minicarriera di origine
}
/**
 * Restituisce un boolean 'true' se la data ripristino non è modificabile
 */
 
public boolean isRODtRipristino() {

	return (getCrudStatus() != OggettoBulk.NORMAL && !isToBeUpdated()) || 
			!isSospesa()  ||
			(getPgMinicarrieraPos() == null && getMinicarriera_origine() != null);
}
/**
 * Restituisce un boolean 'true' se la data sospensione non è modificabile
 */
 
public boolean isRODtSospensione() {

	return	(getCrudStatus() != OggettoBulk.NORMAL && !isToBeUpdated()) || 
			isSospesa() ||
			(getPgMinicarrieraPos() == null && getMinicarriera_origine() != null);
}
/**
 * Restituisce un boolean 'true' se lo stato non è attivo
 */
 
public boolean isROField() {

	return !isAttiva();
}
/**
 * Restituisce un boolean 'true' se i campi anagrafici relativi al percipiente 
 * selezionato non sono modificabili
 */
 
public boolean isROFl_escludi_qvaria_deduzione() {

	return (getMinicarriera_rate() != null && 
			!getMinicarriera_rate().isEmpty());
}
/**
 * Restituisce un boolean 'true' se i campi anagrafici relativi al percipiente 
 * selezionato non sono modificabili
 */
 
public boolean isROFl_tassazione_separata() {

	return	isROPercipiente() ||
			(getMinicarriera_rate() != null && !getMinicarriera_rate().isEmpty());
}
/**
 * Restituisce un boolean 'true' se il tipo anticipo/posticipo non è modificabile
 */
 
public boolean isROImportiIrpef() {

	return	basicROImportiIrpef() ||
			isAliquotaCalcolata();
}
/**
 * Restituisce un boolean 'true' se l'importo totale non è modificabile
 */
 
public boolean isROImporto() {

	return (getStato_ass_compenso() != null && getStato_ass_compenso().equalsIgnoreCase(STATO_TOTALE_ASS_COMPENSO)) ||
			isROField();
}
/**
 * Restituisce un boolean 'true' se l'aspetto mesi per anticipo/posticipo
 * non è modificabile
 */
 
public boolean isROMesiAnticipoPosticipo() {

	return isROField() || TIPO_NESSUNO.equalsIgnoreCase(getTi_anticipo_posticipo());
}
/**
 * Restituisce un boolean 'true' se il numero delle rate non è modificabile
 */
 
public boolean isRONumeroRate() {

	return (getStato_ass_compenso() != null && getStato_ass_compenso().equalsIgnoreCase(STATO_TOTALE_ASS_COMPENSO)) ||
			isROField();
}
/**
 * Restituisce un boolean 'true' se il percipiente non è modificabile
 */
 
public boolean isROPercipiente() {

	return (getMinicarriera_origine() != null &&
			(getMinicarriera_origine().getCrudStatus() == OggettoBulk.NORMAL ||
			 getMinicarriera_origine().getCrudStatus() == OggettoBulk.TO_BE_UPDATED)) ||
			(getStato_ass_compenso() != null &&
				!STATO_NON_ASS_COMPENSO.equalsIgnoreCase(getStato_ass_compenso()) &&
				getCrudStatus() != OggettoBulk.UNDEFINED) ||
			!(getStato() == null || 
				getPercipiente() == null || 
				isAttiva());
}
/**
 * Restituisce un boolean 'true' se i campi anagrafici relativi al percipiente 
 * selezionato non sono modificabili
 */
 
public boolean isROPercipienteAnag() {

	return	getPercipiente() == null || 
			getPercipiente().getCrudStatus() == OggettoBulk.NORMAL || 
			isROPercipiente();
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROTi_istituz_commerc() {

	return isROPercipiente();
}
/**
 * Restituisce un boolean 'true' se il tipo anticipo/posticipo non è modificabile
 */
 
public boolean isROTiAnticipoPosticipo() {

	return isROField() || (getMinicarriera_rate() != null && !getMinicarriera_rate().isEmpty());
}
/**
 * Restituisce un boolean 'true' se la inicarriera è in stato sospeso
 */
 
public boolean isSospesa() {
	
	return getStato() != null && STATO_SOSPESA.equalsIgnoreCase(getStato());
}
/**
 * Restituisce un boolean 'true' se il modello è temporaneo
 */
 
public boolean isTemporaneo() {

	if (getPg_minicarriera() == null)
		return false;
	return getPg_minicarriera().longValue() < 0;
}
/**
 * Rimuove il documento contabile dalla mappa dei saldi in differita
 */
public void removeFromDefferredSaldi(it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docCont) {

	if (docCont != null && deferredSaldi != null &&
		deferredSaldi.containsKey(docCont))
			deferredSaldi.remove(docCont);
}
/**
 * Richeisto non usato
 */
 
public int removeFromDettagliCancellati(Minicarriera_rataBulk rata) {
	return 0;
}
/**
 * Rimuove la rata all'indice 'indiceDiLinea' dalla minicarriera 
 */
 
public Minicarriera_rataBulk removeFromMinicarriera_rate(int indiceDiLinea) {

	Minicarriera_rataBulk element = (Minicarriera_rataBulk)getMinicarriera_rate().get(indiceDiLinea);
	addToDettagliCancellati(element);
	getMinicarriera_rate().remove(indiceDiLinea);

	if (STATO_PARZIALE_ASS_COMPENSO.equalsIgnoreCase(getStato_ass_compenso()) &&
		getRateAssociateACompenso().size() == getMinicarriera_rate().size())
		setStato_ass_compenso(STATO_TOTALE_ASS_COMPENSO);

	return element;
}
/**
 * Valori di default per l'inizializzazione dell'oggetto
 */
 
private void reset(it.cnr.jada.action.ActionContext context) {

	setStato(STATO_ATTIVA);
	setStato_ass_compenso(STATO_NON_ASS_COMPENSO);
	
	setIm_totale_minicarriera(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	
	setNumero_rate(new Integer(1));

	try {
		java.sql.Timestamp date = getDataOdierna();
		int esercizioInScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue();
		if (getAnno(date) != esercizioInScrivania)
			date = getDataOdierna(new java.sql.Timestamp(new java.text.SimpleDateFormat("dd/MM/yyyy").parse("31/12/" + esercizioInScrivania).getTime()));
		setDt_registrazione(date);

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
		setDt_inizio_minicarriera(new java.sql.Timestamp(sdf.parse("01/01/"+getEsercizio().intValue()).getTime()));
		setDt_fine_minicarriera(new java.sql.Timestamp(sdf.parse("31/12/"+getEsercizio().intValue()).getTime()));
	} catch (java.text.ParseException e) {
		setDt_registrazione(getDataOdierna());
		setDt_inizio_minicarriera(getDataOdierna());
		setDt_fine_minicarriera(getDataOdierna());
	}
	
	setNumero_rate(new Integer(it.cnr.jada.util.DateUtils.monthsBetweenDates(new Date(getDt_inizio_minicarriera().getTime()),new Date(getDt_fine_minicarriera().getTime()))));	
	setPercipiente(new V_terzo_per_compensoBulk());
	setTi_anagrafico(Tipo_rapportoBulk.ALTRO);
	setFl_tassazione_separata(Boolean.FALSE);
	setFl_escludi_qvaria_deduzione(Boolean.FALSE);
	setTi_istituz_commerc(TipoIVA.ISTITUZIONALE.value());
	resetTassazioneSeparataData();
	
	setTi_anticipo_posticipo(TIPO_NESSUNO);
	setMesi_anticipo_posticipo(new Integer(0));
}
/**
 * Imposta la mappa dei saldi in differita al valore di default
 */
 
public void resetDefferredSaldi() {
	
	deferredSaldi = null;	
}
/**
 * Valori di default per l'inizializzazione dell'oggetto
 */
 
public void resetTassazioneSeparataData() {

	setImponibile_irpef_eseprec1(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setImponibile_irpef_eseprec2(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setAliquota_irpef_media(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setAliquotaCalcolata(false);
}
/**
 * Insert the method's description here.
 * Creation date: (1/8/2003 12:25:41 PM)
 * @param newAliquotaCalcolata boolean
 */
public void setAliquotaCalcolata(boolean newAliquotaCalcolata) {
	aliquotaCalcolata = newAliquotaCalcolata;
}
/**
 * Insert the method's description here.
 * Creation date: (6/21/2002 1:40:55 PM)
 * @param newBanca it.cnr.contab.anagraf00.core.bulk.BancaBulk
 */
public void setBanca(it.cnr.contab.anagraf00.core.bulk.BancaBulk newBanca) {
	banca = newBanca;
}
public void setCd_cds_minicarriera_ori(java.lang.String cd_cds_minicarriera_ori) {
	this.getMinicarriera_origine().setCd_cds(cd_cds_minicarriera_ori);
}
public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
	this.getModalita_pagamento().setCd_modalita_pag(cd_modalita_pag);
}
public void setCd_termini_pag(java.lang.String cd_termini_pag) {
	this.getTermini_pagamento().setCd_termini_pag(cd_termini_pag);
}
public void setCd_terzo(java.lang.Integer cd_terzo) {
	if (getPercipiente() != null)
		this.getPercipiente().setCd_terzo(cd_terzo);
	else super.setCd_terzo(cd_terzo);
}
public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
	this.getTipo_rapporto().setCd_tipo_rapporto(cd_tipo_rapporto);
}
public void setCd_trattamento(java.lang.String cd_trattamento) {
	if (getTipo_trattamento() != null)
		getTipo_trattamento().setCd_trattamento(cd_trattamento);
	else super.setCd_trattamento(cd_trattamento);
}
public void setCd_uo_minicarriera_ori(java.lang.String cd_uo_minicarriera_ori) {
	this.getMinicarriera_origine().setCd_unita_organizzativa(cd_uo_minicarriera_ori);
}
/**
 * Insert the method's description here.
 * Creation date: (27/05/2002 12.54.21)
 * @param newDettagliCancellati java.util.Vector
 */
public void setDefferredSaldi(PrimaryKeyHashMap newDefferedSaldi) {

	deferredSaldi = newDefferedSaldi;
}
/**
 * Insert the method's description here.
 * Creation date: (27/05/2002 12.54.21)
 * @param newDettagliCancellati java.util.Vector
 */
public void setDettagliCancellati(java.util.Vector newDettagliCancellati) {}
public void setEsercizio_minicarriera_ori(java.lang.Integer esercizio_minicarriera_ori) {
	this.getMinicarriera_origine().setEsercizio(esercizio_minicarriera_ori);
}
/**
 * Insert the method's description here.
 * Creation date: (6/24/2002 3:36:33 PM)
 * @param newMinicarriera_origine it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk
 */
public void setMinicarriera_origine(MinicarrieraBulk newMinicarriera_origine) {
	minicarriera_origine = newMinicarriera_origine;
}
/**
 * Insert the method's description here.
 * Creation date: (6/21/2002 3:59:28 PM)
 * @param newMinicarriera_rate it.cnr.jada.bulk.BulkList
 */
public void setMinicarriera_rate(it.cnr.jada.bulk.BulkList newMinicarriera_rate) {
	minicarriera_rate = newMinicarriera_rate;
}
/**
 * Insert the method's description here.
 * Creation date: (19/02/2002 14.24.54)
 * @param newModalita java.util.Collection
 */
public void setModalita(java.util.Collection newModalita) {
	modalita = newModalita;
}
/**
 * Insert the method's description here.
 * Creation date: (6/21/2002 1:39:04 PM)
 * @param newModalita_pagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
public void setModalita_pagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk newModalita_pagamento) {
	modalita_pagamento = newModalita_pagamento;
}
/**
 * Insert the method's description here.
 * Creation date: (6/21/2002 2:27:05 PM)
 * @param newTerzo it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setPercipiente(V_terzo_per_compensoBulk newTerzo) {
	percipiente = newTerzo;
}
public void setPg_banca(java.lang.Long pg_banca) {
	this.getBanca().setPg_banca(pg_banca);
}
public void setPg_minicarriera_ori(java.lang.Long pg_minicarriera_ori) {
	this.getMinicarriera_origine().setPg_minicarriera(pg_minicarriera_ori);
}
/**
 * Insert the method's description here.
 * Creation date: (6/21/2002 5:27:58 PM)
 * @param newPgMinicarrieraPerClone java.lang.Long
 */
public void setPgMinicarrieraPerClone(java.lang.Long newPgMinicarrieraPerClone) {
	pgMinicarrieraPerClone = newPgMinicarrieraPerClone;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 12.40.29)
 * @param newPgCompensoPos java.lang.Long
 */
public void setPgMinicarrieraPos(java.lang.Long newPgMinicarrieraPos) {

	if (newPgMinicarrieraPos == null || newPgMinicarrieraPos.longValue() < 0)
		setPg_minicarriera(null);
	else
		setPg_minicarriera(newPgMinicarrieraPos);
}
/**
 * Insert the method's description here.
 * Creation date: (19/02/2002 14.25.07)
 * @param newTermini java.util.Collection
 */
public void setTermini(java.util.Collection newTermini) {
	termini = newTermini;
}
/**
 * Insert the method's description here.
 * Creation date: (6/21/2002 1:39:57 PM)
 * @param newTermine_pagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk
 */
public void setTermini_pagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk newTermini_pagamento) {
	termini_pagamento = newTermini_pagamento;
}
public void setTi_anagrafico(java.lang.String ti_anagrafico) {
	if (getPercipiente() != null)
		this.getPercipiente().setTi_dipendente_altro(ti_anagrafico);
	else super.setTi_anagrafico(ti_anagrafico);
}
public void setTi_anagraficoForSearch(java.lang.String ti_anagrafico) {

	if (ti_anagrafico == null || ti_anagrafico.equalsIgnoreCase("T"))
		setTi_anagrafico(null);
	else setTi_anagrafico(ti_anagrafico);
}
/**
 * Insert the method's description here.
 * Creation date: (26/02/2002 11.19.49)
 * @param newTipiRapporto java.util.Collection
 */
public void setTipiRapporto(java.util.Collection newTipiRapporto) {
	tipiRapporto = newTipiRapporto;
}
/**
 * Insert the method's description here.
 * Creation date: (26/02/2002 11.58.06)
 * @param newTipiTrattamento java.util.Collection
 */
public void setTipiTrattamento(java.util.Collection newTipiTrattamento) {
	tipiTrattamento = newTipiTrattamento;
}
/**
 * Insert the method's description here.
 * Creation date: (6/21/2002 1:35:19 PM)
 * @param newTipo_rapporto it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
 */
public void setTipo_rapporto(it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk newTipo_rapporto) {
	tipo_rapporto = newTipo_rapporto;
}
/**
 * Insert the method's description here.
 * Creation date: (6/21/2002 1:35:55 PM)
 * @param newTipo_trattamento it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk
 */
public void setTipo_trattamento(it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk newTipo_trattamento) {
	tipo_trattamento = newTipo_trattamento;
}
/**
 * Valida la minicarriera
 */
 
public void validaCorpo() throws ValidationException{

	validaImponibiliIrpef();
	
	if (getIm_totale_minicarriera() == null ||
		getIm_totale_minicarriera().compareTo(new java.math.BigDecimal(0)) <= 0)
		throw new ValidationException("Specificare l'importo totale della minicarriera.");

	if (getNumero_rate() == null ||
		getNumero_rate().intValue() <= 0)
		throw new ValidationException("Specificare il numero di rate della minicarriera.");

	if (!TIPO_NESSUNO.equalsIgnoreCase(getTi_anticipo_posticipo())) {
		if (getMesi_anticipo_posticipo() == null ||
			getMesi_anticipo_posticipo().intValue() <= 0)
			throw new ValidationException("Specificare il numero di mesi di anticipo/posticipo.");
	}

}
/**
 * Valida le date e i periodi della minicarriera
 */
 
public void validaDate() throws ValidationException {

	if (getDt_registrazione() == null)
		throw new ValidationException("Inserire la data registrazione");

	if (getDt_registrazione().after(getDataOdierna()) &&
		!getDt_registrazione().equals(getDataOdierna()))
		throw new ValidationException("La data registrazione non può essere successiva ad oggi.");
	
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
	java.sql.Timestamp dataInizioEsercizio = null;
	try {
		dataInizioEsercizio = getDataOdierna(new java.sql.Timestamp(sdf.parse("01/01/"+getEsercizio().intValue()).getTime()));
	} catch (java.text.ParseException e) {
		throw new ValidationException("Impossibile stabilire la data di inizio esercizio");
	}
	if (getDt_registrazione().before(dataInizioEsercizio) &&
		!getDt_registrazione().equals(dataInizioEsercizio))
		throw new ValidationException("La data di registrazione non può essere precedente alla data di inizio dell'esercizio di scrivania.");

	validaDateValidita();
}
/**
 * Valida le date di inizio e fine validità e relativi periodi della minicarriera
 */
 
public void validaDateValidita() throws ValidationException {

	if (getDt_inizio_minicarriera() == null || getDt_fine_minicarriera() == null)
		throw new ValidationException("Inserire le date di validità della minicarriera.");
	
	if (getDt_fine_minicarriera().before(getDt_inizio_minicarriera()))
		throw new ValidationException("Date di validità minicarriera non corrette. Verificare i periodi.");

	if (getFl_tassazione_separata() == null)
		throw new ValidationException("Impostare il flag di tassazione separata!");
	
	if (getFl_tassazione_separata().booleanValue()) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
		java.util.Calendar gc = java.util.GregorianCalendar.getInstance();
		gc.setTime(new java.util.Date(getDt_inizio_minicarriera().getTime()));
		int anno = gc.get(gc.YEAR);
		if (anno >= getEsercizio().intValue())
			throw new ValidationException("Quando il flag di tassazione separata è impostato, la data di inizio validità della minicarriera\ndeve essere anteriore all'esercizio di creazione!");
	}
}
/**
 * Valida la minicarriera
 */
 
public void validaImponibiliIrpef() throws ValidationException{

	if (getImponibile_irpef_eseprec1() == null) {
		if (getFl_tassazione_separata() != null && getFl_tassazione_separata().booleanValue())
			throw new ValidationException("Specificare l'importo dell'imponibile irpef 1 della minicarriera.");
		else
			setImponibile_irpef_eseprec1(new java.math.BigDecimal(0).setScale(6, java.math.BigDecimal.ROUND_HALF_UP));
	}
	if (getImponibile_irpef_eseprec2() == null) {
		if (getFl_tassazione_separata() != null && getFl_tassazione_separata().booleanValue())
			throw new ValidationException("Specificare l'importo dell'imponibile irpef 2 della minicarriera.");
		else
			setImponibile_irpef_eseprec2(new java.math.BigDecimal(0).setScale(6, java.math.BigDecimal.ROUND_HALF_UP));
	}
}
/**
 * Valida il percipiente e suoi attributi
 */
 
public int validaPercipiente(boolean throwExc) throws ValidationException{

	if (getPercipiente() == null || getPercipiente().getCrudStatus() != OggettoBulk.NORMAL) {
		if (!throwExc) return V_terzo_per_compensoBulk.TERZO_ASSENTE;
		throw new ValidationException("Selezionare un percipiente.");
	}

	if (getTerzo().getDt_fine_rapporto()!=null)
		if (getTerzo().getDt_fine_rapporto().compareTo(getDt_registrazione())<0) {
			if (!throwExc)
				return V_terzo_per_compensoBulk.TERZO_NON_VALIDO;
			throw new ValidationException("Il percipiente selezionato non è valido");
		}

	if (getModalita_pagamento() == null) {
		if (!throwExc)
			return V_terzo_per_compensoBulk.TERZO_SENZA_MOD_PAG;
		throw new ValidationException ("Inserire le modalità di pagamento");
	}

	if (getTipo_rapporto() == null) {
		if (!throwExc)
			return V_terzo_per_compensoBulk.TIPO_RAPP_ASSENTE;
		throw new ValidationException ("Inserire il tipo rapporto");
	}

	if (getTipo_trattamento() == null) {
		if (!throwExc)
			return V_terzo_per_compensoBulk.TIPO_TRATT_ASSENTE;
		throw new ValidationException("Inserire il tipo trattamento");
	}

	if (getDt_registrazione().compareTo(getTipo_trattamento().getDt_ini_validita())<0 || getDt_registrazione().compareTo(getTipo_trattamento().getDt_fin_validita())>0) {
		if (!throwExc)
			return V_terzo_per_compensoBulk.TIPO_TRATT_NON_VALIDO;
		throw new ValidationException("Il tipo trattamento selezionato non è valido alla data registrazione specificata");
	}

	return V_terzo_per_compensoBulk.TUTTO_BENE;
}
/**
 * Valida la minicarriera
 */
 
public void validate() throws ValidationException{

	super.validate();
	
	validaTestata();

	validaPercipiente(true);
	
	validaCorpo();
}
public void validaTestata() throws ValidationException {

	// Validazione Date
	validaDate();
}
public Incarichi_repertorioBulk getIncarichi_repertorio() {
	return incarichi_repertorio;
}
public void setIncarichi_repertorio(
		Incarichi_repertorioBulk incarichi_repertorio) {
	this.incarichi_repertorio = incarichi_repertorio;
}
public java.lang.Integer getEsercizio_rep() {
	if (getIncarichi_repertorio() == null)
	   return null;
	return getIncarichi_repertorio().getEsercizio();
}
public void setEsercizio_rep(java.lang.Integer esercizio_rep ) {
	this.getIncarichi_repertorio().setEsercizio(esercizio_rep);
}
public java.lang.Long getPg_repertorio() {
	if (getIncarichi_repertorio() == null)
	   return null;
	return getIncarichi_repertorio().getPg_repertorio();
}
public void setPg_repertorio(java.lang.Long pg_repertorio ) {
	this.getIncarichi_repertorio().setPg_repertorio(pg_repertorio);
}
public boolean isVisualizzaIncarico() {
	return visualizzaIncarico;
}
public void setVisualizzaIncarico(boolean visualizzaIncarico) {
	this.visualizzaIncarico = visualizzaIncarico;
}
public void impostaTipo_rapporto(Tipo_rapportoBulk newTipoRapporto) {

	for(java.util.Iterator i = getTipiRapporto().iterator();i.hasNext();){
		Tipo_rapportoBulk tipo = (Tipo_rapportoBulk)i.next();
		if (tipo.equalsByPrimaryKey(newTipoRapporto))
			setTipo_rapporto(tipo);
	}
}
public void impostaTipo_tratt(Tipo_trattamentoBulk newTipoTrattamento) {

	for(java.util.Iterator i = getTipiTrattamento().iterator();i.hasNext();){
		Tipo_trattamentoBulk tipo = (Tipo_trattamentoBulk)i.next();
		if (tipo.equalsByPrimaryKey(newTipoTrattamento))
			setTipo_trattamento(tipo);
	}
}

public void impostaVisualizzaIncarico()
{
	if(!this.isVisualizzaPrestazione()
		|| 
		(this.isVisualizzaPrestazione() 
			&& this.getTipoPrestazioneCompenso()!=null
			&& this.getTipoPrestazioneCompenso().getFl_incarico() != null 
			&& !this.getTipoPrestazioneCompenso().getFl_incarico()
		)
	  )
    {
		if(getIncarichi_repertorio()!=null)
		{
			setIncarichi_repertorio(null);
		}
		setVisualizzaIncarico(false);
    }
	else
	{
		setVisualizzaIncarico(true);
	}
}

public Tipo_prestazione_compensoBulk getTipoPrestazioneCompenso() {
	return tipoPrestazioneCompenso;
}

public void setTipoPrestazioneCompenso(
		Tipo_prestazione_compensoBulk tipoPrestazioneCompenso) {
	this.tipoPrestazioneCompenso = tipoPrestazioneCompenso;
}
public java.lang.String getTi_prestazione() {
	Tipo_prestazione_compensoBulk tipoPrestazioneCompenso = this
			.getTipoPrestazioneCompenso();
	if (tipoPrestazioneCompenso == null)
		return null;
	return tipoPrestazioneCompenso.getCd_ti_prestazione();
}
public void setTi_prestazione(java.lang.String ti_prestazione) {
	this.getTipoPrestazioneCompenso().setCd_ti_prestazione(ti_prestazione);
}

public java.util.Collection getTipiPrestazioneCompenso() {
	return tipiPrestazioneCompenso;
}

public void setTipiPrestazioneCompenso(
		java.util.Collection tipiPrestazioneCompenso) {
	this.tipiPrestazioneCompenso = tipiPrestazioneCompenso;
}

public boolean isVisualizzaPrestazione() {
	return visualizzaPrestazione;
}
public void setVisualizzaPrestazione(boolean visualizzaPrestazione) {
	this.visualizzaPrestazione = visualizzaPrestazione;
}

public void impostaVisualizzaPrestazione()
{
	if(isPrestazioneCompensoEnabled())
	{
		setVisualizzaPrestazione(true);
	}
	else
    {
		if(getTipoPrestazioneCompenso()!=null)
			setTipoPrestazioneCompenso(null);
		setVisualizzaPrestazione(false);
    }
	
}

public boolean isPrestazioneCompensoEnabled() {
	if ( (this.getTipo_trattamento() != null
					&& this.getTipo_trattamento().getFl_tipo_prestazione_obbl() != null && !this
					.getTipo_trattamento().getFl_tipo_prestazione_obbl()))
		return false;
	return true;
}
}
