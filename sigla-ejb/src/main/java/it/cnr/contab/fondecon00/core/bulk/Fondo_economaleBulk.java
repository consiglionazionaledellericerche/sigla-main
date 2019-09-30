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


import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;

import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleIBulk;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fondo_economaleBulk extends Fondo_economaleBase {

	public final static String IGNORA = "0";
	public final static String SI = "1";
	public final static String NO = "2";

	protected it.cnr.contab.config00.sto.bulk.CdsBulk cds = new it.cnr.contab.config00.sto.bulk.CdsBulk();
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa;

	private TerzoBulk economo = null;

	private MandatoIBulk mandato = null;
	private ReversaleIBulk reversale = null;
	private SospesoBulk sospeso_di_chiusura = null;

	private BancaBulk banca;
	private Rif_modalita_pagamentoBulk modalita_pagamento;
	private java.util.Collection banche;
	private java.util.Collection modalita;

	private BulkList associazioni_mandati = new BulkList();
	private it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza_ricerca = null;
	private java.math.BigDecimal importo_totale_scadenze_non_doc = null;
	private java.lang.Boolean squared = null;
	private java.lang.Boolean fl_documentata_for_search = null;
	private java.lang.Boolean fl_reintegrata_for_search = null;
	private java.lang.Boolean fl_associata_for_search = null;
	private boolean onlyForClose = false;
	public Fondo_economaleBulk() {
		super();
	}

public Fondo_economaleBulk(java.lang.String cd_cds,java.lang.String cd_codice_fondo,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio) {
	super(cd_cds,cd_codice_fondo,cd_unita_organizzativa,esercizio);
	setUnita_organizzativa(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk(cd_unita_organizzativa));
}
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 3:20:25 PM)
 * @return it.cnr.jada.bulk.BulkList
 */
public int addToAssociazioni_mandati(Ass_fondo_eco_mandatoBulk ass) {

	getAssociazioni_mandati().add(ass);
	
	return getAssociazioni_mandati().size()-1;
}
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 3:20:25 PM)
 * @return it.cnr.jada.bulk.BulkList
 */
public it.cnr.jada.bulk.BulkList getAssociazioni_mandati() {
	return associazioni_mandati;
}
/**
 * Insert the method's description here.
 * Creation date: (5/17/2002 2:34:06 PM)
 * @return it.cnr.contab.anagraf00.core.bulk.BancaBulk
 */
public it.cnr.contab.anagraf00.core.bulk.BancaBulk getBanca() {
	return banca;
}
/**
	 * Restituisce la <code>Collection</code> contenente l'elenco di banche 
	 * relativi al terzo selezionato
	 *
	 * @return java.util.Collection
	 *
	 * @see setBanche
	 */

	public java.util.Collection getBanche() {
		return banche;
	}
public BulkCollection[] getBulkLists() {

	// Metti solo le liste di oggetti che devono essere resi persistenti
	
	 return new it.cnr.jada.bulk.BulkCollection[] { 
			associazioni_mandati
		};
}
public java.lang.String getCd_cds() {
	it.cnr.contab.config00.sto.bulk.CdsBulk cds = this.getCds();
	if (cds == null)
		return null;
	return cds.getCd_unita_organizzativa();
}
public java.lang.String getCd_modalita_pag() {
	it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalita_pagamento = this.getModalita_pagamento();
	if (modalita_pagamento == null)
		return null;
	return modalita_pagamento.getCd_modalita_pag();
}
public java.lang.String getCd_sospeso() {
	it.cnr.contab.doccont00.core.bulk.SospesoBulk sospeso_di_chiusura = this.getSospeso_di_chiusura();
	if (sospeso_di_chiusura == null)
		return null;
	return sospeso_di_chiusura.getCd_sospeso();
}
public java.lang.Integer getCd_terzo() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk economo = this.getEconomo();
	if (economo == null)
		return null;
	return economo.getCd_terzo();
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
	if (unita_organizzativa == null)
		return null;
	return unita_organizzativa.getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (5/17/2002 2:34:06 PM)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
public TerzoBulk getCreditore_scadenza() {

	if (getScadenza_ricerca() == null) 
		return null;
	return getScadenza_ricerca().getObbligazione().getCreditore();
}
/**
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public TerzoBulk getEconomo() {
	return economo;
}
/**
 * Insert the method's description here.
 * Creation date: (5/23/2002 1:13:07 PM)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_associatata_for_search() {
	return fl_associata_for_search;
}
/**
 * Insert the method's description here.
 * Creation date: (5/23/2002 1:13:07 PM)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_documentata_for_search() {
	return fl_documentata_for_search;
}
/**
 * Insert the method's description here.
 * Creation date: (5/23/2002 1:13:07 PM)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_reintegrata_for_search() {
	return fl_reintegrata_for_search;
}
/* 
 * Getter dell'attributo im_max_gg_spesa_non_doc
 */
public java.math.BigDecimal getImporto_totale_scadenze_non_doc() {
	return importo_totale_scadenze_non_doc;
}
/**
 * Insert the method's description here.
 * Creation date: (5/14/2002 5:05:12 PM)
 * @return it.cnr.contab.doccont00.core.bulk.MandatoBulk
 */
public MandatoIBulk getMandato() {
	return mandato;
}
/**
	 * Restituisce la <code>Collection</code> contenente l'elenco di modalita di pagamento
	 * relativi al terzo selezionato
	 *
	 * @return java.util.Collection
	 *
	 * @see setModalita
	 */

	public java.util.Collection getModalita() {
		return modalita;
	}
/**
 * Insert the method's description here.
 * Creation date: (5/17/2002 2:34:06 PM)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk getModalita_pagamento() {
	return modalita_pagamento;
}
public java.lang.Long getPg_banca() {
	it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = this.getBanca();
	if (banca == null)
		return null;
	return banca.getPg_banca();
}
public java.lang.Long getPg_mandato() {
	it.cnr.contab.doccont00.core.bulk.MandatoIBulk mandato = this.getMandato();
	if (mandato == null)
		return null;
	return mandato.getPg_mandato();
}
public java.lang.Long getPg_reversale() {
	it.cnr.contab.doccont00.core.bulk.ReversaleBulk reversale = this.getReversale();
	if (reversale == null)
		return null;
	return reversale.getPg_reversale();
}
/**
 * Insert the method's description here.
 * Creation date: (5/14/2002 5:05:12 PM)
 * @return it.cnr.contab.doccont00.core.bulk.ReversaleBulk
 */
public ReversaleIBulk getReversale() {
	return reversale;
}
/**
 * Insert the method's description here.
 * Creation date: (5/22/2002 3:17:20 PM)
 * @return it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk
 */
public it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk getScadenza_ricerca() {
	return scadenza_ricerca;
}
/**
 * Insert the method's description here.
 * Creation date: (12/19/2002 11:41:45 AM)
 * @return it.cnr.contab.doccont00.core.bulk.SospesoBulk
 */
public it.cnr.contab.doccont00.core.bulk.SospesoBulk getSospeso_di_chiusura() {
	return sospeso_di_chiusura;
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @return int
 */
public String getSpesaDocumentata() {

	if (getFl_documentata_for_search() == null)
		return IGNORA;
	else if (Boolean.TRUE.equals(getFl_documentata_for_search()))
		return SI;
	else if (Boolean.FALSE.equals(getFl_documentata_for_search()))
		return NO;
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @return int
 */
public java.util.Dictionary getSpesaDocumentataKeys() {

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

	if (getFl_reintegrata_for_search() == null)
		return IGNORA;
	else if (Boolean.TRUE.equals(getFl_reintegrata_for_search()))
		return SI;
	else if (Boolean.FALSE.equals(getFl_reintegrata_for_search()))
		return NO;
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @return int
 */
public java.util.Dictionary getSpesaReintegrataKeys() {

	it.cnr.jada.util.OrderedHashtable oh = new it.cnr.jada.util.OrderedHashtable();
	oh.put(IGNORA, "Ignora");
	oh.put(SI, "Si");
	oh.put(NO, "No");
	return oh;
}
/**
 * Insert the method's description here.
 * Creation date: (5/22/2002 6:15:10 PM)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getSquared() {
	return squared;
}
public java.lang.String getTi_es_sospeso() {
	it.cnr.contab.doccont00.core.bulk.SospesoBulk sospeso_di_chiusura = this.getSospeso_di_chiusura();
	if (sospeso_di_chiusura == null)
		return null;
	return sospeso_di_chiusura.getTi_entrata_spesa();
}
public java.lang.String getTi_sr_sospeso() {
	it.cnr.contab.doccont00.core.bulk.SospesoBulk sospeso_di_chiusura = this.getSospeso_di_chiusura();
	if (sospeso_di_chiusura == null)
		return null;
	return sospeso_di_chiusura.getTi_sospeso_riscontro();
}
	public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
		return unita_organizzativa;
	}

/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isAbledToInsertBank() {
	
	return !(getEconomo() != null && 
		getEconomo().getCrudStatus() == OggettoBulk.NORMAL &&
		getModalita_pagamento() != null) ||
		isChiuso() ||
		isOnlyForClose();
}
	public boolean isChiuso() {
		return getFl_aperto() == null? false: !getFl_aperto().booleanValue();
	}

/**
 * Insert the method's description here.
 * Creation date: (05/06/2003 9.58.40)
 * @return boolean
 */
public boolean isOnlyForClose() {
	return onlyForClose;
}
public boolean isReversaleNecessaria() {

	return getFl_rev_da_emettere() == null ?
					true :
					((getFl_rev_da_emettere().booleanValue()) ?
						getPg_reversale() == null :
						false);
}

/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROcreditore_scadenza() {
	
	return getScadenza_ricerca() == null ||
			getScadenza_ricerca().getCrudStatus() == OggettoBulk.NORMAL ||
			getCreditore_scadenza() == null ||
			getCreditore_scadenza().getCrudStatus() == OggettoBulk.NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROcreditore_scadenzaSearchTool() {
	
	return getScadenza_ricerca().getCrudStatus() == OggettoBulk.NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROeconomo() {
	
	return getEconomo() == null ||
			getEconomo().getCrudStatus() == OggettoBulk.NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROEconomoCrudTool() {
	
	return isROEconomoSearchTool();
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROEconomoSearchTool() {
	
	return getCrudStatus() == OggettoBulk.NORMAL || isChiuso() || isOnlyForClose();
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROmandato() {
	
	return isROmandatoForSearch() ||
			isChiuso() ||
			isOnlyForClose();
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROmandatoForSearch() {
	
	return getMandato() == null ||
			getMandato().getCrudStatus() == OggettoBulk.NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROMandatoSearchTool() {
	
	return isChiuso() || isOnlyForClose(); //getCrudStatus() == OggettoBulk.NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROreversale() {
	
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROscadenza_ricerca() {
	
	return getScadenza_ricerca() == null ||
			getScadenza_ricerca().getCrudStatus() == OggettoBulk.NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public boolean isROscadenza_ricerca_filtri() {
	
	return getScadenza_ricerca() == null;
}
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 3:20:25 PM)
 * @return it.cnr.jada.bulk.BulkList
 */
public Ass_fondo_eco_mandatoBulk removeFromAssociazioni_mandati(int index) {

	return (Ass_fondo_eco_mandatoBulk)getAssociazioni_mandati().remove(index);
}
/**
 * Insert the method's description here.
 * Creation date: (10/4/2001 2:42:26 PM)
 * @return boolean
 */
public void resetImporti() {
	
	setIm_ammontare_fondo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setIm_ammontare_iniziale(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setIm_residuo_fondo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setIm_totale_reintegri(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setIm_totale_spese(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setIm_totale_netto_spese(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setIm_limite_min_reintegro(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));

}
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 3:20:25 PM)
 * @param newAssociazioni_mandati it.cnr.jada.bulk.BulkList
 */
public void setAssociazioni_mandati(it.cnr.jada.bulk.BulkList newAssociazioni_mandati) {
	associazioni_mandati = newAssociazioni_mandati;
}
/**
 * Insert the method's description here.
 * Creation date: (5/17/2002 2:34:06 PM)
 * @param newBanca it.cnr.contab.anagraf00.core.bulk.BancaBulk
 */
public void setBanca(it.cnr.contab.anagraf00.core.bulk.BancaBulk newBanca) {
	banca = newBanca;
}
	/**
	 * Imposta la <code>Collection</code> contenente l'elenco di banche
	 * relativi al terzo selezionato
	 * @param newValute <code>java.util.Collection</code>
	 *
	 * @see getBanche
	 */

	public void setBanche(java.util.Collection newBanche) {
		banche = newBanche;
	}
	public void setCd_cds(java.lang.String cd_cds) {
		this.getCds().setCd_unita_organizzativa(cd_cds);
	}
public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
	this.getModalita_pagamento().setCd_modalita_pag(cd_modalita_pag);
}
public void setCd_sospeso(java.lang.String cd_sospeso) {
	this.getSospeso_di_chiusura().setCd_sospeso(cd_sospeso);
}
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.getEconomo().setCd_terzo(cd_terzo);
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
}
/**
 * Insert the method's description here.
 * Creation date: (5/17/2002 2:34:06 PM)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
public void setCreditore_scadenza(TerzoBulk newCreditore) {

	if (getScadenza_ricerca() == null) 
		return;
	getScadenza_ricerca().getObbligazione().setCreditore(newCreditore);
}
/**
 * @return void
 */
public void setEconomo(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newEconomo) {
	economo = newEconomo;	
}
/**
 * Insert the method's description here.
 * Creation date: (5/23/2002 1:13:07 PM)
 * @param newFl_documentata_for_search java.lang.Boolean
 */
public void setFl_associata_for_search(java.lang.Boolean newFl_associata_for_search) {
	fl_associata_for_search = newFl_associata_for_search;
}
/**
 * Insert the method's description here.
 * Creation date: (5/23/2002 1:13:07 PM)
 * @param newFl_documentata_for_search java.lang.Boolean
 */
public void setFl_documentata_for_search(java.lang.Boolean newFl_documentata_for_search) {
	fl_documentata_for_search = newFl_documentata_for_search;
}
/**
 * Insert the method's description here.
 * Creation date: (5/23/2002 1:13:07 PM)
 * @param newFl_reintegrata_for_search java.lang.Boolean
 */
public void setFl_reintegrata_for_search(java.lang.Boolean newFl_reintegrata_for_search) {
	fl_reintegrata_for_search = newFl_reintegrata_for_search;
}
/* 
 * Setter dell'attributo im_ammontare_iniziale
 */
public void setImporto_totale_scadenze_non_doc(java.math.BigDecimal newImporto_totale_scadenze_non_doc) {
	importo_totale_scadenze_non_doc = newImporto_totale_scadenze_non_doc;
}
/**
 * Insert the method's description here.
 * Creation date: (5/14/2002 5:05:12 PM)
 * @param newMandato it.cnr.contab.doccont00.core.bulk.MandatoBulk
 */
public void setMandato(MandatoIBulk newMandato) {
	mandato = newMandato;
}
	/**
	 * Imposta la <code>Collection</code> contenente l'elenco di modalita di pagamento
	 * relativi al terzo selezionato
	 * @param newValute <code>java.util.Collection</code>
	 *
	 * @see getModalita
	 */

	public void setModalita(java.util.Collection newModalita) {
		modalita = newModalita;
	}
/**
 * Insert the method's description here.
 * Creation date: (5/17/2002 2:34:06 PM)
 * @param newModalita_pagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
public void setModalita_pagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk newModalita_pagamento) {
	modalita_pagamento = newModalita_pagamento;
}
/**
 * Insert the method's description here.
 * Creation date: (05/06/2003 9.58.40)
 * @param newOnlyForClose boolean
 */
public void setOnlyForClose(boolean newOnlyForClose) {
	onlyForClose = newOnlyForClose;
}
public void setPg_banca(java.lang.Long pg_banca) {
	this.getBanca().setPg_banca(pg_banca);
}
public void setPg_mandato(java.lang.Long pg_mandato) {
	this.getMandato().setPg_mandato(pg_mandato);
}
public void setPg_reversale(java.lang.Long pg_reversale) {
	this.getReversale().setPg_reversale(pg_reversale);
}
/**
 * Insert the method's description here.
 * Creation date: (5/14/2002 5:05:12 PM)
 * @param newReversale it.cnr.contab.doccont00.core.bulk.ReversaleBulk
 */
public void setReversale(ReversaleIBulk newReversale) {
	reversale = newReversale;
}
/**
 * Insert the method's description here.
 * Creation date: (5/22/2002 3:17:20 PM)
 * @param newScadenza_ricerca it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk
 */
public void setScadenza_ricerca(it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk newScadenza_ricerca) {
	scadenza_ricerca = newScadenza_ricerca;
}
/**
 * Insert the method's description here.
 * Creation date: (12/19/2002 11:41:45 AM)
 * @param newSospeso_di_chiusura it.cnr.contab.doccont00.core.bulk.SospesoBulk
 */
public void setSospeso_di_chiusura(it.cnr.contab.doccont00.core.bulk.SospesoBulk newSospeso_di_chiusura) {
	sospeso_di_chiusura = newSospeso_di_chiusura;
}
/**
 * Insert the method's description here.
 * Creation date: (3/13/2002 10:36:39 AM)
 * @param newSezionaliFlag int
 */
public void setSpesaDocumentata(String newSpesaDocumentata) {

	switch (Integer.valueOf(newSpesaDocumentata).intValue()) {
		case 0	:	{	
						setFl_documentata_for_search(null);
						break;
					} 
		case 1	:	{	
						setFl_documentata_for_search(Boolean.TRUE);
						break;
					} 
		case 2	:	{	
						setFl_documentata_for_search(Boolean.FALSE);
						break;
					} 
		default: 	{
						setFl_documentata_for_search(null);
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
						setFl_reintegrata_for_search(null);
						break;
					} 
		case 1	:	{	
						setFl_reintegrata_for_search(Boolean.TRUE);
						break;
					} 
		case 2	:	{	
						setFl_reintegrata_for_search(Boolean.FALSE);
						break;
					} 
		default: 	{
						setFl_reintegrata_for_search(null);
						break;
					}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (5/22/2002 6:15:10 PM)
 * @param newSquared java.lang.Boolean
 */
public void setSquared(java.lang.Boolean newSquared) {
	squared = newSquared;
}
public void setTi_es_sospeso(java.lang.String ti_es_sospeso) {
	this.getSospeso_di_chiusura().setTi_entrata_spesa(ti_es_sospeso);
}
public void setTi_sr_sospeso(java.lang.String ti_sr_sospeso) {
	this.getSospeso_di_chiusura().setTi_sospeso_riscontro(ti_sr_sospeso);
}
	public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
		unita_organizzativa = newUnita_organizzativa;
	}

public void validate() throws ValidationException {

	super.validate();
	if (getEconomo() == null || getEconomo().getCrudStatus() != OggettoBulk.NORMAL)
		throw new ValidationException("Specificare l'economo per il fondo economale!");
	if (getModalita_pagamento() == null)
		throw new ValidationException("Specificare la modalità di pagamento per il fondo economale!");
	if (getBanca() == null || getBanca().getCrudStatus() != OggettoBulk.NORMAL)
		throw new ValidationException("Specificare il conto d'appoggio per il fondo economale!");
	if (getMandato() == null || getMandato().getCrudStatus() != OggettoBulk.NORMAL)
		throw new ValidationException("Specificare il mandato di apertura per il fondo economale!");

	java.math.BigDecimal zero = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	if (zero.compareTo(getIm_ammontare_iniziale()) >= 0)
		throw new ValidationException("Il mandato di apertura non ha capienza per la creazione del fondo!");
		
	if (getIm_max_mm_spesa_doc() == null || zero.compareTo(getIm_max_mm_spesa_doc()) == 0)
		throw new ValidationException("Specificare il massimo importo mensile per le spese documentate!");
	if (getIm_max_mm_spesa_non_doc() == null || zero.compareTo(getIm_max_mm_spesa_non_doc()) == 0)
		throw new ValidationException("Specificare il massimo importo mensile per le spese non documentate!");
	if (getIm_max_gg_spesa_doc() == null || zero.compareTo(getIm_max_gg_spesa_doc()) == 0)
		throw new ValidationException("Specificare il massimo importo giornaliero per le spese documentate!");
	if (getIm_max_gg_spesa_non_doc() == null || zero.compareTo(getIm_max_gg_spesa_non_doc()) == 0)
		throw new ValidationException("Specificare il massimo importo giornaliero per le spese non documentate!");
	if (getIm_max_mm_spesa_doc().compareTo(getIm_max_gg_spesa_doc()) < 0)
		throw new ValidationException("Il massimo importo giornaliero per le spese documentate non può superare il massimo importo mensile!");
	if (getIm_max_mm_spesa_non_doc().compareTo(getIm_max_gg_spesa_non_doc()) < 0)
		throw new ValidationException("Il massimo importo giornaliero per le spese non documentate non può superare il massimo importo mensile!");
}
	/**
	 * @return
	 */
	public it.cnr.contab.config00.sto.bulk.CdsBulk getCds()
	{
		return cds;
	}

	/**
	 * @param bulk
	 */
	public void setCds(it.cnr.contab.config00.sto.bulk.CdsBulk bulk)
	{
		cds = bulk;
	}
	public java.lang.String getCd_cds_sospeso() {
		it.cnr.contab.doccont00.core.bulk.SospesoBulk sospeso_di_chiusura = this.getSospeso_di_chiusura();
		if (sospeso_di_chiusura == null)
			return null;
		return sospeso_di_chiusura.getCd_cds();
	}
	public void setCd_cds_sospeso(java.lang.String cd_cds_sospeso) {
		this.getSospeso_di_chiusura().setCd_cds(cd_cds_sospeso);
	}
}
