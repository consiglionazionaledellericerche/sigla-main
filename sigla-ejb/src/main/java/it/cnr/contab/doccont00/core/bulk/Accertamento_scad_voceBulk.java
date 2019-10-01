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

import java.math.BigDecimal;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Accertamento_scad_voceBulk extends Accertamento_scad_voceBase 
{
	protected Accertamento_scadenzarioBulk accertamento_scadenzario= new Accertamento_scadenzarioBulk();
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita = new it.cnr.contab.config00.latt.bulk.WorkpackageBulk();	
	BigDecimal prc;
public Accertamento_scad_voceBulk() {
	super();
}
public Accertamento_scad_voceBulk(java.lang.String cd_cds,java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.Integer esercizio_originale,java.lang.Long pg_accertamento,java.lang.Long pg_accertamento_scadenzario) {
	super(cd_cds,cd_centro_responsabilita,cd_linea_attivita,esercizio,esercizio_originale,pg_accertamento,pg_accertamento_scadenzario);
	setAccertamento_scadenzario(new it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk(cd_cds,esercizio,esercizio_originale,pg_accertamento,pg_accertamento_scadenzario));
	setLinea_attivita(new it.cnr.contab.config00.latt.bulk.WorkpackageBulk(cd_centro_responsabilita,cd_linea_attivita));
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk
 */
public Accertamento_scadenzarioBulk getAccertamento_scadenzario() {
	return accertamento_scadenzario;
}
public java.lang.String getCd_cds() 
{
	// Ritorna il Cd_cds (ente) dell'accertamento 
	
	Accertamento_scadenzarioBulk scadenza = this.getAccertamento_scadenzario();
	if (scadenza == null)
		return null;
		
	AccertamentoBulk accertamento = scadenza.getAccertamento();
	if (accertamento == null)
		return null;

	return accertamento.getCd_cds();
}
public java.lang.String getCd_fondo_ricerca() 
{
	// Ritorna il Fondo di Ricerca dell'accertamento
		
	Accertamento_scadenzarioBulk scadenza = this.getAccertamento_scadenzario();
	if (scadenza == null)
		return null;
		
	AccertamentoBulk accertamento = scadenza.getAccertamento();
	if (accertamento == null)
		return null;
	return accertamento.getCd_fondo_ricerca();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_natura'
 *
 * @return Il valore della proprietà 'cd_natura'
 */
public String getCd_natura() 
{
	// Ritorna il Codice della Natura della Linea di Attivita selezionata
	
	if ( getLinea_attivita() != null )
		return getLinea_attivita().getCd_natura();
	else
		return "";	
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_voce'
 *
 * @return Il valore della proprietà 'cd_voce'
 */
public java.lang.String getCd_voce() 
{
	// Ritorna il Codice del Capitolo (voce_f) dell'accertamento
		
	Accertamento_scadenzarioBulk scadenza = this.getAccertamento_scadenzario();
	if (scadenza == null)
		return null;
		
	AccertamentoBulk accertamento = scadenza.getAccertamento();
	if (accertamento == null)
		return null;
	return accertamento.getCd_voce();
}
public java.lang.Integer getEsercizio() 
{
	// Ritorna l'esercizio dell'accertamento
	
	Accertamento_scadenzarioBulk scadenza = this.getAccertamento_scadenzario();
	if(scadenza == null)
		return null;
		
	AccertamentoBulk accertamento = scadenza.getAccertamento();
	if (accertamento == null)
		return null;
		
	return accertamento.getEsercizio();
}
/**
 * @return it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
 */
public it.cnr.contab.config00.latt.bulk.WorkpackageBulk getLinea_attivita() {
	return linea_attivita;
}
public java.lang.Integer getEsercizio_originale() 
{
	// Ritorna l'Esercizio dell'accertamento
		 
	Accertamento_scadenzarioBulk scadenza = this.getAccertamento_scadenzario();
	if (scadenza == null)
		return null;
		
	AccertamentoBulk accertamento = scadenza.getAccertamento();
	if (accertamento == null)
		return null;
	return accertamento.getEsercizio_originale();
}
public java.lang.Long getPg_accertamento() 
{
	// Ritorna il Numero dell'accertamento
		 
	Accertamento_scadenzarioBulk scadenza = this.getAccertamento_scadenzario();
	if (scadenza == null)
		return null;
		
	AccertamentoBulk accertamento = scadenza.getAccertamento();
	if (accertamento == null)
		return null;
	return accertamento.getPg_accertamento();
}
public java.lang.Long getPg_accertamento_scadenzario() 
{
	// Ritorna il numero della scadenza
	
	Accertamento_scadenzarioBulk scadenza = this.getAccertamento_scadenzario();
	if (scadenza == null)
		return super.getPg_accertamento_scadenzario();
	return scadenza.getPg_accertamento_scadenzario();
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param collDettagli	
 * @return 
 */
public BulkList ordinaPerCdr( BulkList collDettagli ) 
{
	// riordino la lista deli dettagli delle scadenze per Cdr
	
	java.util.Collections.sort(collDettagli,new java.util.Comparator() 	{	

		public int compare(Object o1, Object o2) 
		{
			Accertamento_scad_voceBulk os1 = (Accertamento_scad_voceBulk) o1;
			Accertamento_scad_voceBulk os2 = (Accertamento_scad_voceBulk) o2;

			return os1.getCd_centro_responsabilita().compareTo( os2.getCd_centro_responsabilita());
		}
	});

	return collDettagli;
}
/**
 * @param newAccertamento_scadenzario it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk
 */
public void setAccertamento_scadenzario(Accertamento_scadenzarioBulk newAccertamento_scadenzario) {
	accertamento_scadenzario = newAccertamento_scadenzario;
}
public void setCd_cds(java.lang.String cd_cds)
{
	// Imposta il Cd_cds (ente) dell'accertamento	
	this.getAccertamento_scadenzario().getAccertamento().setCd_cds(cd_cds);
}
public void setCd_fondo_ricerca(java.lang.String cd_fondo_ricerca)
{
	// Imposta il Fondo di Ricerca dell'accertamento	
	this.getAccertamento_scadenzario().getAccertamento().setCd_fondo_ricerca(cd_fondo_ricerca);
}
public void setEsercizio(java.lang.Integer esercizio) 
{
	// Imposta l'esercizio dell'accertamento
	this.getAccertamento_scadenzario().getAccertamento().setEsercizio(esercizio);
}
/**
 * @param newLinea_attivita it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
 */
public void setLinea_attivita(it.cnr.contab.config00.latt.bulk.WorkpackageBulk newLinea_attivita) {
	linea_attivita = newLinea_attivita;
}
public void setEsercizio_originale(java.lang.Integer esercizio_originale) 
{
	// Imposta l'anno dell'accertamento	
		
	this.getAccertamento_scadenzario().getAccertamento().setEsercizio_originale(esercizio_originale);
}
public void setPg_accertamento(java.lang.Long pg_accertamento) 
{
	// Imposta il numero dell'accertamento	
		
	this.getAccertamento_scadenzario().getAccertamento().setPg_accertamento(pg_accertamento);
}
public void setPg_accertamento_scadenzario(java.lang.Long pg_accertamento_scadenzario) 
{
	// Imposta il numero della scadenza
	this.getAccertamento_scadenzario().setPg_accertamento_scadenzario(pg_accertamento_scadenzario);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 */
public void storna() 
{
	setIm_voce( new java.math.BigDecimal(0));
	setToBeUpdated();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'prc'
 *
 * @return Il valore della proprietà 'prc'
 */
public BigDecimal getPrc() 
{
	if ( !accertamento_scadenzario.getAccertamento().getFl_calcolo_automatico().booleanValue() )
	{
		if ( getIm_voce() != null && accertamento_scadenzario.getIm_scadenza() != null && accertamento_scadenzario.getIm_scadenza().doubleValue() != 0)
			return  getIm_voce().multiply( new BigDecimal(100)).divide( getAccertamento_scadenzario().getIm_scadenza(), 2, BigDecimal.ROUND_HALF_UP );
		else
			return null;	
	}		
	return prc;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'prc'
 *
 * @param newPrc	Il valore da assegnare a 'prc'
 */
public void setPrc(BigDecimal newPrc) {
	prc = newPrc;
}
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	  getLinea_attivita().getCentro_responsabilita().setCd_centro_responsabilita(cd_centro_responsabilita);
}
public java.lang.String getCd_centro_responsabilita() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita = this.getLinea_attivita();
	if (linea_attivita == null)
		return null;
	it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita = linea_attivita.getCentro_responsabilita();
	if (centro_responsabilita == null)
		return null;
	return centro_responsabilita.getCd_centro_responsabilita();
	/*
	if ( linea_attivita != null )
		return linea_attivita.getCentro_responsabilita().getCd_centro_responsabilita();
	else
		return cd_centro_responsabilita;
	*/
}
public java.lang.String getCd_linea_attivita() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita = this.getLinea_attivita();
	if (linea_attivita == null)
		return null;
	return linea_attivita.getCd_linea_attivita();
	/*
	if ( linea_attivita != null )
		return linea_attivita.getCd_linea_attivita();
	else
		return cd_linea_attivita;
	*/
}
public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
	getLinea_attivita().setCd_linea_attivita(cd_linea_attivita);
}
}
