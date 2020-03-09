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

/*
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.bulk;

import java.math.BigDecimal;

import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Voce_f_saldi_cdr_lineaBulk extends Voce_f_saldi_cdr_lineaBase 
{
	private it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk voce = new it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk();

	public static String IMPUTAZIONE_DIRETTA = "DIR";
	public static String DA_PREDISPOSIONE = "PDG";

	public static java.lang.Boolean SOLA_LETTURA = new Boolean(false); 	
		
	public static String TIPO_APPARTENENZA_CNR = "C";
	public static String TIPO_APPARTENENZA_CDS = "D";

	public static String TIPO_GESTIONE_SPESA = "S";
	public static String TIPO_GESTIONE_ENTRATA = "E";

	public static String TIPO_RESIDUO_PROPRIO = "P";
	public static String TIPO_RESIDUO_IMPROPRIO = "I";

	public static String TIPO_RESIDUO = "R";
	public static String TIPO_COMPETENZA = "C";

	private Voce_f_saldi_cdr_linea_resBulk residui;

	private java.math.BigDecimal im_1210;
	
	public Voce_f_saldi_cdr_lineaBulk() {
		super();
	}
	public Voce_f_saldi_cdr_lineaBulk(java.lang.Integer esercizio, java.lang.Integer esercizio_res, java.lang.String cd_centro_responsabilita, java.lang.String cd_linea_attivita, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_voce) {
		super(esercizio, esercizio_res, cd_centro_responsabilita, cd_linea_attivita, ti_appartenenza, ti_gestione, cd_voce);
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
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'residui'
 *
 * @return Il valore della proprietà 'residui'
 */
public Voce_f_saldi_cdr_linea_resBulk getResidui() {
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
	
	setTi_appartenenza(TIPO_APPARTENENZA_CDS);
	setTi_gestione(TIPO_GESTIONE_ENTRATA);

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
	
	setTi_appartenenza(TIPO_APPARTENENZA_CNR);
	setTi_gestione(TIPO_GESTIONE_ENTRATA);

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
	
	setTi_appartenenza(TIPO_APPARTENENZA_CDS);
	setTi_gestione(TIPO_GESTIONE_SPESA);

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
	
	setTi_appartenenza(TIPO_APPARTENENZA_CNR);
	setTi_gestione(TIPO_GESTIONE_SPESA);

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
	
	setTi_appartenenza(TIPO_APPARTENENZA_CDS);
	setTi_gestione(TIPO_GESTIONE_ENTRATA);
	
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
	
	setTi_appartenenza(TIPO_APPARTENENZA_CNR);
	setTi_gestione(TIPO_GESTIONE_ENTRATA);
	
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
	
	setTi_appartenenza(TIPO_APPARTENENZA_CDS);
	setTi_gestione(TIPO_GESTIONE_SPESA);
	
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
	
	setTi_appartenenza(TIPO_APPARTENENZA_CNR);
	setTi_gestione(TIPO_GESTIONE_SPESA);
	
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
	
	setTi_appartenenza(TIPO_APPARTENENZA_CDS);
	setTi_gestione(TIPO_GESTIONE_ENTRATA);

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
	
	setTi_appartenenza(TIPO_APPARTENENZA_CNR);
	setTi_gestione(TIPO_GESTIONE_ENTRATA);

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
	
	setTi_appartenenza(TIPO_APPARTENENZA_CDS);
	setTi_gestione(TIPO_GESTIONE_SPESA);

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
	
	setTi_appartenenza(TIPO_APPARTENENZA_CNR);
	setTi_gestione(TIPO_GESTIONE_SPESA);

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
	setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
    
	java.math.BigDecimal importoNullo = new java.math.BigDecimal(0);
	setVariazioni_piu(importoNullo);
	setVariazioni_meno(importoNullo);
	setIm_obbl_acc_comp(importoNullo);
	setIm_mandati_reversali_imp(importoNullo);
	setIm_mandati_reversali_pro(importoNullo);
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
public void setResidui(Voce_f_saldi_cdr_linea_resBulk newResidui) {
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
public BigDecimal getAssestato(){
	return getIm_stanz_iniziale_a1().add(getVariazioni_piu()).subtract(getVariazioni_meno());
}
public BigDecimal getAssestatoResiduoImproprio(){
    return getIm_stanz_res_improprio().add(getVar_piu_stanz_res_imp()).subtract(
           getVar_meno_stanz_res_imp()).subtract(getVar_piu_obbl_res_pro()).add(getVar_meno_obbl_res_pro());
}
public BigDecimal getDispAdImpResiduoImproprio(){
	return getAssestatoResiduoImproprio().subtract(getTotImpResiduoImproprio());
}
public BigDecimal getDispAdImpCompetenza(){
	return getAssestato().subtract(getTotImpCompetenza());
}
public BigDecimal getTotImpResiduoImproprio(){
	return getIm_obbl_res_imp().add(getVar_piu_obbl_res_imp()).subtract(getVar_meno_obbl_res_imp());
}
public BigDecimal getTotImpResiduoProprio(){
	return getIm_obbl_res_pro().add(getVar_piu_obbl_res_pro()).subtract(getVar_meno_obbl_res_pro());
}
public BigDecimal getTotImpCompetenza(){
	return getIm_obbl_acc_comp();
}
public BigDecimal getTotMandati(){
	return getIm_mandati_reversali_pro().add(getIm_mandati_reversali_imp());
}
public void inizializzaSommeAZero(){
	setIm_stanz_iniziale_a1(Utility.ZERO);
	setIm_stanz_iniziale_a2(Utility.ZERO);
	setIm_stanz_iniziale_a3(Utility.ZERO);
	setVariazioni_piu(Utility.ZERO);
	setVariazioni_meno(Utility.ZERO);
	setIm_stanz_iniziale_cassa(Utility.ZERO);
	setVariazioni_piu_cassa(Utility.ZERO);
	setVariazioni_meno_cassa(Utility.ZERO);
	setIm_obbl_acc_comp(Utility.ZERO);
	setIm_stanz_res_improprio(Utility.ZERO);
	setVar_piu_stanz_res_imp(Utility.ZERO);
	setVar_meno_stanz_res_imp(Utility.ZERO);
	setIm_obbl_res_imp(Utility.ZERO);
	setVar_piu_obbl_res_imp(Utility.ZERO);
	setVar_meno_obbl_res_imp(Utility.ZERO);
	setIm_obbl_res_pro(Utility.ZERO);
	setVar_piu_obbl_res_pro(Utility.ZERO);
	setVar_meno_obbl_res_pro(Utility.ZERO);
	setIm_mandati_reversali_imp(Utility.ZERO);
	setIm_mandati_reversali_pro(Utility.ZERO);
	setIm_pagamenti_incassi(Utility.ZERO);
	
}
public void validate() throws ValidationException 
{
	
	super.validate();
	
	// controllo su campo CD_VOCE 
	if ( getVoce().getCd_voce() == null )
	{
		throw new ValidationException( "Capitolo/Articolo non specificato!" );
	}
		
	java.math.BigDecimal importoNullo = new java.math.BigDecimal(0);
	
	if ( getIm_stanz_iniziale_a1() == null )
		setIm_stanz_iniziale_a1(importoNullo);
	if ( getIm_stanz_iniziale_a2() == null )
		setIm_stanz_iniziale_a2(importoNullo);	
	if ( getIm_stanz_iniziale_a3() == null )
		setIm_stanz_iniziale_a3(importoNullo);				
	if ( getVariazioni_piu() == null )
		setVariazioni_piu(importoNullo);
	if ( getVariazioni_meno() == null )
		setVariazioni_meno(importoNullo);
	if ( getIm_stanz_iniziale_cassa() == null )
		setIm_stanz_iniziale_cassa(importoNullo);
	if ( getVariazioni_piu_cassa() == null )
		setVariazioni_piu_cassa(importoNullo);
	if ( getVariazioni_meno_cassa() == null )
		setVariazioni_meno_cassa(importoNullo);
	if ( getIm_obbl_acc_comp() == null )
		setIm_obbl_acc_comp(importoNullo);
	if ( getIm_stanz_res_improprio() == null )
		setIm_stanz_res_improprio(importoNullo);
	if ( getVar_piu_stanz_res_imp() == null )
		setVar_piu_stanz_res_imp(importoNullo);
	if ( getVar_meno_stanz_res_imp() == null )
		setVar_meno_stanz_res_imp(importoNullo);
	if ( getIm_obbl_res_imp() == null )
		setIm_obbl_res_imp(importoNullo);
	if ( getVar_piu_obbl_res_imp() == null )
		setVar_piu_obbl_res_imp(importoNullo);
	if ( getVar_meno_obbl_res_imp() == null )
		setVar_meno_obbl_res_imp(importoNullo);
	if ( getIm_obbl_res_pro() == null )
		setIm_obbl_res_pro(importoNullo);
	if ( getVar_piu_obbl_res_pro() == null )
		setVar_piu_obbl_res_pro(importoNullo);
	if ( getVar_meno_obbl_res_pro() == null )
		setVar_meno_obbl_res_pro(importoNullo);
	if ( getIm_mandati_reversali_imp() == null )
		setIm_mandati_reversali_imp(importoNullo);
	if ( getIm_mandati_reversali_pro() == null )
		setIm_mandati_reversali_pro(importoNullo);		
	if ( getIm_pagamenti_incassi() == null )
		setIm_pagamenti_incassi(importoNullo);
}
}
