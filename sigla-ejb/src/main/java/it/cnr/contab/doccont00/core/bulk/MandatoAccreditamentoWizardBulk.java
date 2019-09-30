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

package it.cnr.contab.doccont00.core.bulk;

import java.util.*;
import java.math.*;

import it.cnr.jada.bulk.*;

public class MandatoAccreditamentoWizardBulk extends MandatoAccreditamentoBulk {
	private java.util.Collection impegniColl = java.util.Collections.EMPTY_LIST;
	private java.util.Collection mandatiColl = new java.util.LinkedList();

public MandatoAccreditamentoWizardBulk() {
	super();
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @throws ValidationException	
 */
public void assegnaImportiInBaseAPriorita() throws ValidationException 
{
	ordinaImpegniPerPriorita();
	validatePriorita();
	resetImportoImpegniSelezionatiColl();
	V_impegnoBulk impegno;
	BigDecimal totTrasferito = new BigDecimal(0);
	for (Iterator i = getImpegniSelezionatiColl().iterator(); i.hasNext(); )
	{
		impegno = (V_impegnoBulk) i.next();
		if ( impegno.getIm_disponibile().compareTo( getIm_mandato().subtract( totTrasferito)) <= 0 )
			impegno.setIm_da_trasferire( impegno.getIm_disponibile());
		else
		{
			impegno.setIm_da_trasferire( getIm_mandato().subtract(totTrasferito));
			break;
		}	
		totTrasferito = totTrasferito.add( impegno.getIm_da_trasferire());
	}
	//rimuovo dalla lista degli impegni selezionati quelli che hanno im_da_trasferire = 0 
	for (Iterator i = getImpegniSelezionatiColl().iterator(); i.hasNext(); )
	{
		impegno = (V_impegnoBulk) i.next();
		if ( impegno.getIm_da_trasferire() == null || impegno.getIm_da_trasferire().compareTo( new BigDecimal(0)) == 0 )
			i.remove();
	}		
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'im_totale_impegni_selezionati'
 *
 * @return Il valore della proprietà 'im_totale_impegni_selezionati'
 */
public BigDecimal getIm_totale_impegni_selezionati() {
	java.math.BigDecimal totImpegni = new BigDecimal(0);
	V_impegnoBulk impegno;
	for ( java.util.Iterator i = impegniSelezionatiColl.iterator(); i.hasNext(); )
	{
		impegno = (V_impegnoBulk) i.next();
		if ( impegno.getIm_da_trasferire() != null )
			totImpegni = totImpegni.add( impegno.getIm_da_trasferire() );
	}		
	return totImpegni;	

}
/**
 * @return java.util.Collection
 */
public java.util.Collection getImpegniColl() {
	return impegniColl;
}
/**
 * @return java.util.Collection
 */
public java.util.Collection getMandatiColl() {
	return mandatiColl;
}
/**
 * Inizializza l'Oggetto Bulk per l'inserimento.
 * @param bp Il Business Process in uso
 * @param context Il contesto dell'azione
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	super.initializeForInsert( bp, context );	
	this.codice_cds = ((it.cnr.contab.doccont00.bp.MandatoAccreditamentoWizardBP) bp).getCodice_cds();
	setIm_mandato(((it.cnr.contab.doccont00.bp.MandatoAccreditamentoWizardBP) bp).getIm_mandato());
	setTi_mandato( TIPO_ACCREDITAMENTO);
	return this;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @throws ValidationException	
 */
public void ordinaImpegniPerPriorita() throws ValidationException 
{
	V_impegnoBulk impegno;
	if ( getImpegniSelezionatiColl().size() > 0 )
	{
		impegno = (V_impegnoBulk) getImpegniSelezionatiColl().iterator().next();
		setImpegniSelezionatiColl( impegno.ordinaPerPriorita((List)getImpegniSelezionatiColl()));
	}	
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 */
public void resetImportoImpegniSelezionatiColl() {
	for (Iterator i = getImpegniSelezionatiColl().iterator(); i.hasNext(); )
		((V_impegnoBulk)i.next()).setIm_da_trasferire( new BigDecimal(0));
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 */
public void selezionaImpegni()
{
	impegniSelezionatiColl = new LinkedList();
	V_impegnoBulk impegno;
	for (Iterator i = impegniColl.iterator(); i.hasNext(); )
	{
		impegno = (V_impegnoBulk) i.next();
		if ( getFl_imputazione_manuale() && (impegno.getIm_da_trasferire() != null && impegno.getIm_da_trasferire().compareTo(new BigDecimal(0)) != 0 ))
			impegniSelezionatiColl.add( impegno );
		else if ( !getFl_imputazione_manuale() && impegno.getPriorita() != null )
			impegniSelezionatiColl.add( impegno );
	}		
}
/**
 * @param newImpegniColl java.util.Collection
 */
public void setImpegniColl(java.util.Collection newImpegniColl) {
	impegniColl = newImpegniColl;
}
/**
 * @param newMandatiColl java.util.Collection
 */
public void setMandatiColl(java.util.Collection newMandatiColl) {
	mandatiColl = newMandatiColl;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws ValidationException {
	super.validate();
	for ( java.util.Iterator i = impegniSelezionatiColl.iterator(); i.hasNext(); )
		((V_impegnoBulk)i.next()).validate();
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
 
/* gli impegni sono già stati ordinati*/
public void validatePriorita() throws ValidationException 
{
	V_impegnoBulk impegno;
	int nrImpegni = getImpegniSelezionatiColl().size();
	int prioritaPrecedente = 0;
	for (java.util.Iterator i = getImpegniSelezionatiColl().iterator(); i.hasNext(); )
	{
		impegno = (V_impegnoBulk) i.next();
		if ( impegno.getPriorita().intValue() == prioritaPrecedente )
			throw new ValidationException("La priorita' " + prioritaPrecedente + " e' stata specificata piu' volte" );
		prioritaPrecedente = impegno.getPriorita().intValue();	
	}		
	
}
}
