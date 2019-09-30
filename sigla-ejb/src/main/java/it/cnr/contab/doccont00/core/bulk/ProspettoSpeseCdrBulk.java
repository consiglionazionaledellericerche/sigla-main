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

import it.cnr.contab.config00.sto.bulk.*;
public class ProspettoSpeseCdrBulk extends it.cnr.jada.bulk.OggettoBulk 
{
	private it.cnr.contab.config00.sto.bulk.CdrBulk cdr;
	private java.util.List speseCdrColl;
	private java.math.BigDecimal totSpeseDaPdg1;
	private java.math.BigDecimal totSpeseDaPdg2;
	private java.math.BigDecimal totSpeseDaPdg3;
	private java.math.BigDecimal totObbligazioni1;
	private java.math.BigDecimal totObbligazioni2;
	private java.math.BigDecimal totObbligazioni3;
	private String message1;
	private String message2;
	private String message3;		
public ProspettoSpeseCdrBulk(  ) {
	super();
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param cdr	
 */
public ProspettoSpeseCdrBulk( CdrBulk cdr ) {
	super();
	setCdr( cdr );
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cdr'
 *
 * @return Il valore della proprietà 'cdr'
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCdr() {
	return cdr;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'message1'
 *
 * @return Il valore della proprietà 'message1'
 */
public java.lang.String getMessage1() {
	return message1;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'message2'
 *
 * @return Il valore della proprietà 'message2'
 */
public java.lang.String getMessage2() {
	return message2;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'message3'
 *
 * @return Il valore della proprietà 'message3'
 */
public java.lang.String getMessage3() {
	return message3;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'speseCdrColl'
 *
 * @return Il valore della proprietà 'speseCdrColl'
 */
public java.util.List getSpeseCdrColl() {
	return speseCdrColl;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'totObbligazioni1'
 *
 * @return Il valore della proprietà 'totObbligazioni1'
 */
public java.math.BigDecimal getTotObbligazioni1() {
	return totObbligazioni1;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'totObbligazioni2'
 *
 * @return Il valore della proprietà 'totObbligazioni2'
 */
public java.math.BigDecimal getTotObbligazioni2() {
	return totObbligazioni2;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'totObbligazioni3'
 *
 * @return Il valore della proprietà 'totObbligazioni3'
 */
public java.math.BigDecimal getTotObbligazioni3() {
	return totObbligazioni3;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'totSpeseDaPdg1'
 *
 * @return Il valore della proprietà 'totSpeseDaPdg1'
 */
public java.math.BigDecimal getTotSpeseDaPdg1() {
	return totSpeseDaPdg1;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'totSpeseDaPdg2'
 *
 * @return Il valore della proprietà 'totSpeseDaPdg2'
 */
public java.math.BigDecimal getTotSpeseDaPdg2() {
	return totSpeseDaPdg2;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'totSpeseDaPdg3'
 *
 * @return Il valore della proprietà 'totSpeseDaPdg3'
 */
public java.math.BigDecimal getTotSpeseDaPdg3() {
	return totSpeseDaPdg3;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 */
public void refreshTotali() 
{
	setTotObbligazioni1( new java.math.BigDecimal(0));
	setTotObbligazioni2( new java.math.BigDecimal(0));
	setTotObbligazioni3( new java.math.BigDecimal(0));
	setTotSpeseDaPdg1( new java.math.BigDecimal(0));
	setTotSpeseDaPdg2( new java.math.BigDecimal(0));
	setTotSpeseDaPdg3( new java.math.BigDecimal(0));
	for ( java.util.Iterator i = getSpeseCdrColl().iterator(); i.hasNext(); )
	{
		V_obblig_pdg_saldo_laBulk saldoLatt = (V_obblig_pdg_saldo_laBulk)i.next();
		setTotObbligazioni1( getTotObbligazioni1().add( saldoLatt.getIm_obb_a1()));
		setTotObbligazioni2( getTotObbligazioni2().add( saldoLatt.getIm_obb_a2()));
		setTotObbligazioni3( getTotObbligazioni3().add( saldoLatt.getIm_obb_a3()));
		setTotSpeseDaPdg1( getTotSpeseDaPdg1().add( saldoLatt.getIm_spese_a1()));
		setTotSpeseDaPdg2( getTotSpeseDaPdg2().add( saldoLatt.getIm_spese_a2()));
		setTotSpeseDaPdg3( getTotSpeseDaPdg3().add( saldoLatt.getIm_spese_a3()));						
	}
	if ( getTotObbligazioni1().compareTo( getTotSpeseDaPdg1()) > 0 )
		setMessage1( "Attenzione! Possibilità di sfondamento per il 1° esercizio");
	if ( getTotObbligazioni2().compareTo( getTotSpeseDaPdg2()) > 0 )
		setMessage2( "Attenzione! Possibilità di sfondamento per il 2° esercizio");
	if ( getTotObbligazioni3().compareTo( getTotSpeseDaPdg3()) > 0 )
		setMessage3( "Attenzione! Possibilità di sfondamento per il 3° esercizio"); 
		
		
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cdr'
 *
 * @param newCdr	Il valore da assegnare a 'cdr'
 */
public void setCdr(it.cnr.contab.config00.sto.bulk.CdrBulk newCdr) {
	cdr = newCdr;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'message1'
 *
 * @param newMessage1	Il valore da assegnare a 'message1'
 */
public void setMessage1(java.lang.String newMessage1) {
	message1 = newMessage1;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'message2'
 *
 * @param newMessage2	Il valore da assegnare a 'message2'
 */
public void setMessage2(java.lang.String newMessage2) {
	message2 = newMessage2;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'message3'
 *
 * @param newMessage3	Il valore da assegnare a 'message3'
 */
public void setMessage3(java.lang.String newMessage3) {
	message3 = newMessage3;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'speseCdrColl'
 *
 * @param newSpeseCdrColl	Il valore da assegnare a 'speseCdrColl'
 */
public void setSpeseCdrColl(java.util.List newSpeseCdrColl) {
	speseCdrColl = newSpeseCdrColl;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'totObbligazioni1'
 *
 * @param newTotObbligazioni1	Il valore da assegnare a 'totObbligazioni1'
 */
public void setTotObbligazioni1(java.math.BigDecimal newTotObbligazioni1) {
	totObbligazioni1 = newTotObbligazioni1;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'totObbligazioni2'
 *
 * @param newTotObbligazioni2	Il valore da assegnare a 'totObbligazioni2'
 */
public void setTotObbligazioni2(java.math.BigDecimal newTotObbligazioni2) {
	totObbligazioni2 = newTotObbligazioni2;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'totObbligazioni3'
 *
 * @param newTotObbligazioni3	Il valore da assegnare a 'totObbligazioni3'
 */
public void setTotObbligazioni3(java.math.BigDecimal newTotObbligazioni3) {
	totObbligazioni3 = newTotObbligazioni3;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'totSpeseDaPdg1'
 *
 * @param newTotSpeseDaPdg1	Il valore da assegnare a 'totSpeseDaPdg1'
 */
public void setTotSpeseDaPdg1(java.math.BigDecimal newTotSpeseDaPdg1) {
	totSpeseDaPdg1 = newTotSpeseDaPdg1;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'totSpeseDaPdg2'
 *
 * @param newTotSpeseDaPdg2	Il valore da assegnare a 'totSpeseDaPdg2'
 */
public void setTotSpeseDaPdg2(java.math.BigDecimal newTotSpeseDaPdg2) {
	totSpeseDaPdg2 = newTotSpeseDaPdg2;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'totSpeseDaPdg3'
 *
 * @param newTotSpeseDaPdg3	Il valore da assegnare a 'totSpeseDaPdg3'
 */
public void setTotSpeseDaPdg3(java.math.BigDecimal newTotSpeseDaPdg3) {
	totSpeseDaPdg3 = newTotSpeseDaPdg3;
}
}
