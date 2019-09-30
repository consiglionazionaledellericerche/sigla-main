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
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.jada.bulk.*;

public class Obbligazione_scad_voceBulk extends Obbligazione_scad_voceBase implements Cloneable
{
	Obbligazione_scadenzarioBulk obbligazione_scadenzario = new Obbligazione_scadenzarioBulk();
	WorkpackageBulk linea_attivita = new WorkpackageBulk();
	BigDecimal prc;
	Voce_fBulk voce_f = new Voce_fBulk();
		
public Obbligazione_scad_voceBulk() {
	super();
}
public Obbligazione_scad_voceBulk(java.lang.String cd_cds,java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita,java.lang.String cd_voce,java.lang.Integer esercizio,java.lang.Integer esercizio_originale,java.lang.Long pg_obbligazione,java.lang.Long pg_obbligazione_scadenzario,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_cds,cd_centro_responsabilita,cd_linea_attivita,cd_voce,esercizio,esercizio_originale,pg_obbligazione,pg_obbligazione_scadenzario,ti_appartenenza,ti_gestione);
	setObbligazione_scadenzario(new it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk(cd_cds,esercizio,esercizio_originale,pg_obbligazione,pg_obbligazione_scadenzario));
	setLinea_attivita(new it.cnr.contab.config00.latt.bulk.WorkpackageBulk(cd_centro_responsabilita,cd_linea_attivita));
}
public Object clone() 
{
	Obbligazione_scad_voceBulk nuova = new Obbligazione_scad_voceBulk();
	
	nuova.setEsercizio( getEsercizio());
	nuova.setIm_voce( getIm_voce());
	
	
	
	return nuova;
}
public java.lang.String getCd_cds() {
	it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazione_scadenzario = this.getObbligazione_scadenzario();
	if (obbligazione_scadenzario == null)
		return null;
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obbligazione_scadenzario.getObbligazione();
	if (obbligazione == null)
		return null;
	it.cnr.contab.config00.sto.bulk.CdsBulk cds = obbligazione.getCds();
	if (cds == null)
		return null;
	return cds.getCd_unita_organizzativa();
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
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_funzione'
 *
 * @return Il valore della proprietà 'cd_funzione'
 */
public String getCd_funzione() {
	if ( linea_attivita != null )
		return linea_attivita.getCd_funzione();
	else
		return "";	
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
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_natura'
 *
 * @return Il valore della proprietà 'cd_natura'
 */
public String getCd_natura() {
	if ( linea_attivita != null )
		return linea_attivita.getCd_natura();
	else
		return "";	
}
public java.lang.Integer getEsercizio() {
	it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazione_scadenzario = this.getObbligazione_scadenzario();
	if (obbligazione_scadenzario == null)
		return null;
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obbligazione_scadenzario.getObbligazione();
	if (obbligazione == null)
		return null;
	return obbligazione.getEsercizio();
}
/**
 * @return it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
 */
public it.cnr.contab.config00.latt.bulk.WorkpackageBulk getLinea_attivita() {
	return linea_attivita;
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk
 */
public Obbligazione_scadenzarioBulk getObbligazione_scadenzario() {
	return obbligazione_scadenzario;
}
public java.lang.Integer getEsercizio_originale() {
	it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazione_scadenzario = this.getObbligazione_scadenzario();
	if (obbligazione_scadenzario == null)
		return null;
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obbligazione_scadenzario.getObbligazione();
	if (obbligazione == null)
		return null;
	return obbligazione.getEsercizio_originale();
}
public java.lang.Long getPg_obbligazione() {
	it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazione_scadenzario = this.getObbligazione_scadenzario();
	if (obbligazione_scadenzario == null)
		return null;
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = obbligazione_scadenzario.getObbligazione();
	if (obbligazione == null)
		return null;
	return obbligazione.getPg_obbligazione();
}
public java.lang.Long getPg_obbligazione_scadenzario() {
	it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazione_scadenzario = this.getObbligazione_scadenzario();
	if (obbligazione_scadenzario == null)
		return null;
	return obbligazione_scadenzario.getPg_obbligazione_scadenzario();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'prc'
 *
 * @return Il valore della proprietà 'prc'
 */
public BigDecimal getPrc() 
{
	if ( !obbligazione_scadenzario.getObbligazione().getFl_calcolo_automatico().booleanValue() )
	{
		if ( getIm_voce() != null && obbligazione_scadenzario.getIm_scadenza() != null && obbligazione_scadenzario.getIm_scadenza().doubleValue() != 0)
			return  getIm_voce().multiply( new BigDecimal(100)).divide( getObbligazione_scadenzario().getIm_scadenza(), 2, BigDecimal.ROUND_HALF_UP );
		else
			return null;	
	}		
	return prc;
}
public void setCd_cds(java.lang.String cd_cds) {
	this.getObbligazione_scadenzario().getObbligazione().getCds().setCd_unita_organizzativa(cd_cds);
}
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.getLinea_attivita().getCentro_responsabilita().setCd_centro_responsabilita(cd_centro_responsabilita);
}
public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
	this.getLinea_attivita().setCd_linea_attivita(cd_linea_attivita);
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.getObbligazione_scadenzario().getObbligazione().setEsercizio(esercizio);
}
/**
 * @param newLinea_attivita it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
 */
public void setLinea_attivita(it.cnr.contab.config00.latt.bulk.WorkpackageBulk newLinea_attivita) {
	linea_attivita = newLinea_attivita;
}
/**
 * @param newObbligazione_scadenzario it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk
 */
public void setObbligazione_scadenzario(Obbligazione_scadenzarioBulk newObbligazione_scadenzario) {
	obbligazione_scadenzario = newObbligazione_scadenzario;
}
public void setEsercizio_originale(java.lang.Integer esercizio_originale) {
	this.getObbligazione_scadenzario().getObbligazione().setEsercizio_originale(esercizio_originale);
}
public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
	this.getObbligazione_scadenzario().getObbligazione().setPg_obbligazione(pg_obbligazione);
}
public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario) {
	this.getObbligazione_scadenzario().setPg_obbligazione_scadenzario(pg_obbligazione_scadenzario);
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
/**
 * Metodo per stornare l'importo.
 */
public void storna() 
{
	setIm_voce( new java.math.BigDecimal(0));
	setToBeUpdated();
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws ValidationException 
{
	super.validate();
	if ( getIm_voce() != null && getIm_voce().compareTo( new BigDecimal(0)) < 0 )
		throw new ValidationException( "L'importo specificato per il dettaglio della scadenza deve essere maggiore o uguale a 0");
}	
	/**
	 * @return
	 */
	public Voce_fBulk getVoce_f() {
		return voce_f;
	}

	/**
	 * @param bulk
	 */
	public void setVoce_f(Voce_fBulk bulk) {
		voce_f = bulk;
	}

}
