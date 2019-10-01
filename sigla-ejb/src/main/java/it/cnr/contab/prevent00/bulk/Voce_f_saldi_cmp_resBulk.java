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

public class Voce_f_saldi_cmp_resBulk extends Voce_f_saldi_cmpBase 
{
	private it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk voce = new it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk();

	public final static String IMPUTAZIONE_DIRETTA = "DIR";
	public final static String DA_PREDISPOSIZIONE = "PDG";

	public final static java.lang.Boolean SOLA_LETTURA = new Boolean(false); 	
		
	public final static String TIPO_APPARTENENZA_CNR = "C";
	public final static String TIPO_APPARTENENZA_CDS = "D";		

	public final static String TIPO_COMPETENZA = "C";
	public final static String TIPO_RESIDUO = "R";		

	public final static String TIPO_GESTIONE_SPESA = "S";
	public final static String TIPO_GESTIONE_ENTRATA = "E";

	private java.math.BigDecimal im_1210;	
public Voce_f_saldi_cmp_resBulk() {
	super();
}
public Voce_f_saldi_cmp_resBulk(java.lang.String cd_cds,java.lang.String cd_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_competenza_residuo,java.lang.String ti_gestione) {
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
 * Creation date: (29/01/2003 13.49.14)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getIm_1210() {
	return im_1210;
}
/**
 * restituisce la disponibilità per i mandati
 *
 * @return La disponibilità di cassa per le voci a residuo
 */
public java.math.BigDecimal getIm_disp_cassa_mandato()
{
	if ( getIm_obblig_imp_acr() == null ||  getIm_mandati_reversali() == null )
		return null;
	return getIm_obblig_imp_acr().subtract( getIm_mandati_reversali());

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
 * Inizializza gli attributi del dettaglio di saldo parte residui per inserimento
 *
 * @param bulk	
 * @param context	
 */
public void initializzaAttributiPerInsert(Voce_f_saldi_cmpBulk bulk, it.cnr.jada.UserContext context) {
	setTi_appartenenza(bulk.getTi_appartenenza());
	setTi_gestione(bulk.getTi_gestione());

    setCd_cds(bulk.getCd_cds());
    setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context));

	setOrigine(IMPUTAZIONE_DIRETTA);
	setFl_sola_lettura(bulk.getFl_sola_lettura());
	setTi_competenza_residuo(TIPO_RESIDUO);

    java.math.BigDecimal importoNullo = new java.math.BigDecimal(0);
    setIm_stanz_iniziale_a1(importoNullo);
    setIm_stanz_iniziale_a2(importoNullo);
    setIm_stanz_iniziale_a3(importoNullo);
    setVariazioni_piu(importoNullo);
    setVariazioni_meno(importoNullo);
    setIm_obblig_imp_acr(importoNullo);
    setIm_mandati_reversali(importoNullo);
    setIm_pagamenti_incassi(importoNullo);

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
 * Creation date: (29/01/2003 13.49.14)
 * @param newIm_1210 java.math.BigDecimal
 */
public void setIm_1210(java.math.BigDecimal newIm_1210) {
	im_1210 = newIm_1210;
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
//
//	Viene chiamato al salvataggio per la validazione dei dati
//

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
