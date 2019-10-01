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

package it.cnr.contab.missioni00.tabrif.bulk;

import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.contab.anagraf00.tabter.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Missione_tipo_pastoBulk extends Missione_tipo_pastoBase 
{
	private NazioneBulk nazione = new NazioneBulk();
	private DivisaBulk divisa = new DivisaBulk();	

		
	private Rif_inquadramentoBulk rifInquadramento;
	private java.util.Collection rifInquadramenti;
	
	private RifAreePaesiEsteriBulk rifAreePaesiEsteri = new RifAreePaesiEsteriBulk();
	
public Missione_tipo_pastoBulk() {
	super();
}
public Missione_tipo_pastoBulk(java.lang.String cd_ti_pasto,java.sql.Timestamp dt_inizio_validita,java.lang.Long pg_nazione,java.lang.Long pg_rif_inquadramento,java.lang.String ti_area_geografica,java.lang.String cd_area_estera) {
	super(cd_ti_pasto,dt_inizio_validita,pg_nazione,pg_rif_inquadramento,ti_area_geografica,cd_area_estera);
	setRifInquadramento(new it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk(pg_rif_inquadramento));
	setNazione(new it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk(pg_nazione));
	setRifAreePaesiEsteri(new RifAreePaesiEsteriBulk(cd_area_estera));
}
public java.lang.String getCd_divisa() {
	it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk divisa = this.getDivisa();
	if (divisa == null)
		return null;
	return divisa.getCd_divisa();
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/2002 16.59.30)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFineValidita() {
	if ( (getDt_fine_validita()!=null) && (getDt_fine_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO)))
		return null;
	return getDt_fine_validita();
}
/**
 * Insert the method's description here.
 * Creation date: (15/11/2001 17.31.15)
 * @return it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public DivisaBulk getDivisa() {
	return divisa;
}
/**
 * Insert the method's description here.
 * Creation date: (14/11/2001 15.56.05)
 * @return it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk
 */
public it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk getNazione() {
	return nazione;
}
public java.lang.Long getPg_nazione() {
	it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk nazione = this.getNazione();
	if (nazione == null)
		return null;
	return nazione.getPg_nazione();
}
public java.lang.String getCd_area_estera() {
	it.cnr.contab.anagraf00.tabter.bulk.RifAreePaesiEsteriBulk rifAreePaesiEsteri = this.getRifAreePaesiEsteri();
	if (rifAreePaesiEsteri == null)
		return null;
	return rifAreePaesiEsteri.getCd_area_estera();
}
public java.lang.Long getPg_rif_inquadramento() {
	it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk rifInquadramento = this.getRifInquadramento();
	if (rifInquadramento == null)
		return null;
	return rifInquadramento.getPg_rif_inquadramento();
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/2002 16.54.27)
 * @return java.util.Collection
 */
public java.util.Collection getRifInquadramenti() {
	return rifInquadramenti;
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/2002 16.53.19)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk
 */
public Rif_inquadramentoBulk getRifInquadramento() {
	return rifInquadramento;
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 11.53.47)
 * @return java.util.Dictionary
 */
public java.util.Dictionary getTi_area_geograficaKeys() {
	return TipoAreaGeografica.TIPI_AREA_GEOGRAFICA;
}
/**
 * Insert the method's description here.
 * Creation date: (18/01/2002 14.52.26)
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForInsert(bp,context);

	setDt_fine_validita(EsercizioHome.DATA_INFINITO);
	resetImporti();

	return this;
}
public boolean isRODivisa() {

	return getDivisa() == null || getDivisa().getCrudStatus() == NORMAL;
}
public boolean isROFindNazione(){

	if (getTi_area_geografica()==null)
		return false;

	return 
		(getTi_area_geografica().compareTo(TipoAreaGeografica.ITALIA)==0) ||
		(getTi_area_geografica().compareTo(TipoAreaGeografica.INDIFFERENTE)==0);
}
public boolean isRONazione() {
	
	return getNazione()==null || getNazione().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (18/01/2002 14.25.56)
 */
private void resetImporti() {
	
	setLimite_max_pasto(new java.math.BigDecimal(0));
}
public void setCd_divisa(java.lang.String cd_divisa) {
	this.getDivisa().setCd_divisa(cd_divisa);
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 16.59.01)
 * @param newTime java.sql.Timestamp
 */
public void setDataFineValidita(java.sql.Timestamp newDataFineValidita) {

	this.setDt_fine_validita(newDataFineValidita);
}
/**
 * Insert the method's description here.
 * Creation date: (15/11/2001 17.31.15)
 * @param newDivisa it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public void setDivisa(DivisaBulk newDivisa) {
	divisa = newDivisa;
}
/**
 * Insert the method's description here.
 * Creation date: (14/11/2001 15.56.05)
 * @param newNazione it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk
 */
public void setNazione(it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk newNazione) {
	nazione = newNazione;
}
public void setPg_nazione(java.lang.Long pg_nazione) {
	this.getNazione().setPg_nazione(pg_nazione);
}
public void setCd_area_estera(java.lang.String cd_area_estera) {
	this.getRifAreePaesiEsteri().setCd_area_estera(cd_area_estera);
}
public void setPg_rif_inquadramento(java.lang.Long pg_rif_inquadramento) {
	this.getRifInquadramento().setPg_rif_inquadramento(pg_rif_inquadramento);
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/2002 16.54.27)
 * @param newRifInquadramenti java.util.Collection
 */
public void setRifInquadramenti(java.util.Collection newRifInquadramenti) {
	rifInquadramenti = newRifInquadramenti;
}
/**
 * Insert the method's description here.
 * Creation date: (14/02/2002 16.53.19)
 * @param newRifInquadramento it.cnr.contab.anagraf00.tabrif.bulk.Rif_inquadramentoBulk
 */
public void setRifInquadramento(Rif_inquadramentoBulk newRifInquadramento) {
	rifInquadramento = newRifInquadramento;
}
public void validate() throws ValidationException {
	// controllo su campo CODICE
	if ( getCd_ti_pasto() == null )
		throw new ValidationException( "Il campo CODICE non può essere vuoto!" );

	// controllo su campo INQUADRAMENTO
	if (getPg_rif_inquadramento() == null ) 
		throw new ValidationException( "Selezionare l'INQUADRAMENTO!" );

	// controllo su campo TI_AREA_GEOGRAFICA
	if (getTi_area_geografica() == null ) 
		throw new ValidationException( "Selezionare l'AREA GEOGRAFICA!" );

	// controllo su campo NAZIONE
	if (getPg_nazione() == null ) 
		throw new ValidationException("Il campo NAZIONE non può essere vuoto");

	// controllo su campo DIVISA
	if (getCd_divisa() == null)
		throw new ValidationException( "Il campo VALUTA non può essere vuoto!" );

	// controllo su campo DATA INIZIO VALIDITA
	if (getDt_inizio_validita() == null )
		throw new ValidationException( "Il campo DATA INIZIO VALIDITA non può essere vuoto!" );
		
	// controllo su campo LIMITE PASTO
	if ( getLimite_max_pasto() == null )
		throw new ValidationException( "Il campo LIMITE PASTO non può essere vuoto!" );
	if (getLimite_max_pasto().compareTo( new java.math.BigDecimal(0)) <= 0)
		throw new ValidationException( "Il campo LIMITE PASTO deve essere maggiore di 0!" );

	// controllo su campo DATA CANCELLAZIONE
	if(getDt_cancellazione()!=null && getDt_cancellazione().compareTo(getDt_inizio_validita())<0)
		throw new ValidationException("Il campo DATA CANCELLAZIONE deve essere superiore alla Data Inizio Validità");
	
	// controllo su campo Area Estera
	if ( getCd_area_estera() == null )
		throw new ValidationException( "Il campo Area Estera non può essere vuoto!" );
}		

	public it.cnr.contab.anagraf00.tabter.bulk.RifAreePaesiEsteriBulk getRifAreePaesiEsteri() {
		return rifAreePaesiEsteri;
	}
	public void setRifAreePaesiEsteri(it.cnr.contab.anagraf00.tabter.bulk.RifAreePaesiEsteriBulk newArea) {
		rifAreePaesiEsteri = newArea;
	}
	
	public boolean isROFindRifAreePaesiEsteri(){
		return false;
	}
}
