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

package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Voce_f_saldi_cmpBulk extends Voce_f_saldi_cmpBase 
{
	private it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk voce = new it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk();

	public static String imputazione_diretta = "DIR";
	public static String da_predisposione = "PDG";

	public static java.lang.Boolean sola_lettura = new Boolean(false); 	
		
	public static String tipo_appartenenza_cnr = "C";
	public static String tipo_appartenenza_cds = "D";

	public final static String TIPO_COMPETENZA = "C";
	public final static String TIPO_RESIDUO = "R";		

	public static String tipo_gestione_spesa = "S";
	public static String tipo_gestione_entrata = "E";

	private Voce_f_saldi_cmp_resBulk residui;

	private java.math.BigDecimal im_1210;
public Voce_f_saldi_cmpBulk() {
	super();
}
public Voce_f_saldi_cmpBulk(java.lang.String cd_cds,java.lang.String cd_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_competenza_residuo,java.lang.String ti_gestione) {
	super(cd_cds,cd_voce,esercizio,ti_appartenenza,ti_competenza_residuo,ti_gestione);
	setVoce(new it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk(cd_voce,esercizio,ti_appartenenza,ti_gestione));
}
public java.lang.String getCd_voce() {
	it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk voce = this.getVoce();
	if (voce == null)
		return null;
	return voce.getCd_voce();
}
/**
 * Insert the method's description here.
 * Creation date: (28/01/2003 10.12.24)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getIm_1210() {
	return im_1210;
}
/**
 * restituisce la disponibilità per i mandati
 *
 * @return La disponibilità di cassa per le voci di competenza
 */
public java.math.BigDecimal getIm_disp_cassa_mandato()
{
	if ( getIm_stanz_iniziale_a1() == null || getVariazioni_piu() == null || getVariazioni_meno() == null || getIm_mandati_reversali() == null )
		return null;
	return getIm_stanz_iniziale_a1().add( getVariazioni_piu()).subtract( getVariazioni_meno()).subtract( getIm_mandati_reversali());


    
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'residui'
 *
 * @return Il valore della proprietà 'residui'
 */
public Voce_f_saldi_cmp_resBulk getResidui() {
	return residui;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'voce'
 *
 * @return Il valore della proprietà 'voce'
 */
public it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk getVoce() 
{
	return voce;
}
/**
 * Inizializza il saldo di competenza di entrata CDS per la ricerca
 *
 * @param bp business process
 * @param context	L'ActionContext della richiesta
 * @return Il bulk del saldo dettaglio inizializzato
 */
public OggettoBulk initializeForFreeSearchEntrataCds(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
{
	super.initializeForFreeSearch(bp, context);

	initializzaAttributiComuniPerSearch(bp, context);
	
	setTi_appartenenza(tipo_appartenenza_cds);
	setTi_gestione(tipo_gestione_entrata);

	return this;
}
/**
 * Inizializza il saldo di competenza di entrata CNR per la ricerca libera
 *
 * @param bp business process
 * @param context	L'ActionContext della richiesta
 * @return Il bulk del saldo dettaglio inizializzato
 */
public OggettoBulk initializeForFreeSearchEntrataCnr(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
{
	super.initializeForFreeSearch(bp, context);

	initializzaAttributiComuniPerSearch(bp, context);
	
	setTi_appartenenza(tipo_appartenenza_cnr);
	setTi_gestione(tipo_gestione_entrata);

	return this;
}
/**
 * Inizializza il saldo di competenza di spesa CDS per la ricerca libera
 *
 * @param bp business process
 * @param context	L'ActionContext della richiesta
 * @return Il bulk del saldo dettaglio inizializzato
 */
public OggettoBulk initializeForFreeSearchSpesaCds(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
{
	super.initializeForFreeSearch(bp, context);

	initializzaAttributiComuniPerSearch(bp, context);
	
	setTi_appartenenza(tipo_appartenenza_cds);
	setTi_gestione(tipo_gestione_spesa);

	return this;
}
/**
 * Inizializza il saldo di competenza di spesa CNR per la ricerca libera
 *
 * @param bp business process
 * @param context	L'ActionContext della richiesta
 * @return Il bulk del saldo dettaglio inizializzato
 */
public OggettoBulk initializeForFreeSearchSpesaCnr(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
{
	super.initializeForFreeSearch(bp, context);

	initializzaAttributiComuniPerSearch(bp, context);
	
	setTi_appartenenza(tipo_appartenenza_cnr);
	setTi_gestione(tipo_gestione_spesa);

	return this;
}
/**
 * Inizializza il saldo di competenza di entrata CDS per l'inserimento
 *
 * @param bp business process
 * @param context	L'ActionContext della richiesta
 * @return Il bulk del saldo dettaglio inizializzato
 */
public OggettoBulk initializeForInsertEntrataCds(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context)  
{
	super.initializeForInsert(bp, context);

	initializzaAttributiComuniPerInsert(bp, context);
	
	setTi_appartenenza(tipo_appartenenza_cds);
	setTi_gestione(tipo_gestione_entrata);
	
	return this;
}
/**
 * Inizializza il saldo di competenza di entrata CNR per l'inserimento
 *
 * @param bp business process
 * @param context	L'ActionContext della richiesta
 * @return Il bulk del saldo dettaglio inizializzato
 */
public OggettoBulk initializeForInsertEntrataCnr(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context)  
{
	super.initializeForInsert(bp, context);
	
	initializzaAttributiComuniPerInsert(bp, context);
	
	setTi_appartenenza(tipo_appartenenza_cnr);
	setTi_gestione(tipo_gestione_entrata);
	
	return this;
}
/**
 * Inizializza il saldo di competenza di spesa CDS per l'inserimento
 *
 * @param bp business process
 * @param context	L'ActionContext della richiesta
 * @return Il bulk del saldo dettaglio inizializzato
 */
public OggettoBulk initializeForInsertSpesaCds(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context)  
{
	super.initializeForInsert(bp, context);

	initializzaAttributiComuniPerInsert(bp, context);
	
	setTi_appartenenza(tipo_appartenenza_cds);
	setTi_gestione(tipo_gestione_spesa);
	
	return this;
}
/**
 * Inizializza il saldo di competenza di spesa CNR per l'inserimento
 *
 * @param bp business process
 * @param context	L'ActionContext della richiesta
 * @return Il bulk del saldo dettaglio inizializzato
 */
public OggettoBulk initializeForInsertSpesaCnr(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context)  
{
	super.initializeForInsert(bp, context);
	
	initializzaAttributiComuniPerInsert(bp, context);
	
	setTi_appartenenza(tipo_appartenenza_cnr);
	setTi_gestione(tipo_gestione_spesa);
	
	return this;
}
/**
 * Inizializza il saldo di competenza di entrata CDS per la ricerca
 *
 * @param bp business process
 * @param context	L'ActionContext della richiesta
 * @return Il bulk del saldo dettaglio inizializzato
 */
public OggettoBulk initializeForSearchEntrataCds(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
{
	super.initializeForSearch(bp, context);

	initializzaAttributiComuniPerSearch(bp, context);
	
	setTi_appartenenza(tipo_appartenenza_cds);
	setTi_gestione(tipo_gestione_entrata);

	return this;
}
/**
 * Inizializza il saldo di competenza di entrata CNR per la ricerca
 *
 * @param bp business process
 * @param context	L'ActionContext della richiesta
 * @return Il bulk del saldo dettaglio inizializzato
 */
public OggettoBulk initializeForSearchEntrataCnr(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
{
	super.initializeForSearch(bp, context);

	initializzaAttributiComuniPerSearch(bp, context);
	
	setTi_appartenenza(tipo_appartenenza_cnr);
	setTi_gestione(tipo_gestione_entrata);

	return this;
}
/**
 * Inizializza il saldo di competenza di spesa CDS per la ricerca
 *
 * @param bp business process
 * @param context	L'ActionContext della richiesta
 * @return Il bulk del saldo dettaglio inizializzato
 */
public OggettoBulk initializeForSearchSpesaCds(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
{
	super.initializeForSearch(bp, context);

	initializzaAttributiComuniPerSearch(bp, context);
	
	setTi_appartenenza(tipo_appartenenza_cds);
	setTi_gestione(tipo_gestione_spesa);

	return this;
}
/**
 * Inizializza il saldo di competenza di spesa CNR per la ricerca
 *
 * @param bp business process
 * @param context	L'ActionContext della richiesta
 * @return Il bulk del saldo dettaglio inizializzato
 */
public OggettoBulk initializeForSearchSpesaCnr(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
{
	super.initializeForSearch(bp, context);

	initializzaAttributiComuniPerSearch(bp, context);
	
	setTi_appartenenza(tipo_appartenenza_cnr);
	setTi_gestione(tipo_gestione_spesa);

	return this;
}
/**
 * Inizializza il saldo di competenza con attributi comuni a entrate e spese CDS/CNR per inserimento
 *
 * @param bp business process
 * @param context	L'ActionContext della richiesta
 * @return Il bulk del saldo dettaglio inizializzato
 */
public void initializzaAttributiComuniPerInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) 
{
    setCd_cds(((Voce_f_saldi_cmpBulk) bp.getModel()).getCd_cds());
    setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));

	setOrigine(imputazione_diretta);
	setFl_sola_lettura(sola_lettura);
	setTi_competenza_residuo(TIPO_COMPETENZA);
    
    java.math.BigDecimal importoNullo = new java.math.BigDecimal(0);
    setVariazioni_piu(importoNullo);
    setVariazioni_meno(importoNullo);
    setIm_obblig_imp_acr(importoNullo);
    setIm_mandati_reversali(importoNullo);
    setIm_pagamenti_incassi(importoNullo);

    return;
}
/**
 * Inizializza il saldo di competenza con attributi comuni a entrate e spese CDS/CNR per ricerca
 *
 * @param bp business process
 * @param context	L'ActionContext della richiesta
 * @return Il bulk del saldo dettaglio inizializzato
 */
public void initializzaAttributiComuniPerSearch(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) 
{
   	setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context) );			
	setCd_cds(((Voce_f_saldi_cmpBulk)bp.getModel()).getCd_cds());
	setTi_competenza_residuo(TIPO_COMPETENZA);

    return;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOParte'
 *
 * @return Il valore della proprietà 'rOParte'
 */
public boolean isROParte() 
{
	if((getVoce().getCd_voce() != null) && (getVoce().getCd_parte() != null))
	{
		return true;
	}
	return false;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOPerRicercaVoce'
 *
 * @return Il valore della proprietà 'rOPerRicercaVoce'
 */
public boolean isROPerRicercaVoce() 
{
	if(getVoce().getCd_parte() == null)
	{
		return false;
	}
		
	if((getVoce().getCd_parte().equals("2")) || (getVoce().getCd_voce() != null))
	{
		return true;
	}
	return false;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOVoce'
 *
 * @return Il valore della proprietà 'rOVoce'
 */
public boolean isROVoce() 
{
	return voce == null || voce.getCrudStatus() == NORMAL;
}
public void setCd_voce(java.lang.String cd_voce) {
	this.getVoce().setCd_voce(cd_voce);
}
/**
 * Insert the method's description here.
 * Creation date: (28/01/2003 10.12.24)
 * @param newIm_1210 java.math.BigDecimal
 */
public void setIm_1210(java.math.BigDecimal newIm_1210) {
	im_1210 = newIm_1210;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'residui'
 *
 * @param newResidui	Il valore da assegnare a 'residui'
 */
public void setResidui(Voce_f_saldi_cmp_resBulk newResidui) {
	residui = newResidui;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'voce'
 *
 * @param newVoce	Il valore da assegnare a 'voce'
 */
public void setVoce(it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk newVoce) {
	voce = newVoce;
}
public void validate() throws ValidationException 
{
	
	super.validate();

	if(getFl_sola_lettura().booleanValue())
	{
		throw new ValidationException( "Dettaglio non modificabile!" );
	}
		
	// controllo su campo ESERCIZIO 
	if ( getVoce().getCd_voce() == null )
	{
		throw new ValidationException( "Capitolo/Articolo non specificato!" );
	}
		
	java.math.BigDecimal importoNullo = new java.math.BigDecimal(0);

	if ( getVariazioni_piu() == null )
		setVariazioni_piu(importoNullo);
	if ( getVariazioni_meno() == null )
		setVariazioni_meno(importoNullo);
	if ( getIm_obblig_imp_acr() == null )
		setIm_obblig_imp_acr(importoNullo);
	if ( getIm_mandati_reversali() == null )
		setIm_mandati_reversali(importoNullo);
	if ( getIm_pagamenti_incassi() == null )
		setIm_pagamenti_incassi(importoNullo);
	if ( getIm_stanz_iniziale_a1() == null )
		setIm_stanz_iniziale_a1(importoNullo);
	if ( getIm_stanz_iniziale_a2() == null )
		setIm_stanz_iniziale_a2(importoNullo);	
	if ( getIm_stanz_iniziale_a3() == null )
		setIm_stanz_iniziale_a3(importoNullo);				
}
}
