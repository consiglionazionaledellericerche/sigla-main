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

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Missione_diariaBulk extends Missione_diariaBase 
{
	private NazioneBulk nazione = new NazioneBulk();
	private DivisaBulk divisa = new DivisaBulk();	

	private java.util.Collection gruppiInquadramento;		
	private Gruppo_inquadramentoBulk gruppoInquadramento;
public Missione_diariaBulk() {
	super();
}
public Missione_diariaBulk(java.lang.String cd_gruppo_inquadramento,java.sql.Timestamp dt_inizio_validita,java.lang.Long pg_nazione) {
	super(cd_gruppo_inquadramento,dt_inizio_validita,pg_nazione);
	setGruppoInquadramento(new it.cnr.contab.missioni00.tabrif.bulk.Gruppo_inquadramentoBulk(cd_gruppo_inquadramento));
	setNazione(new it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk(pg_nazione));
}
public java.lang.String getCd_divisa() {
	it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk divisa = this.getDivisa();
	if (divisa == null)
		return null;
	return divisa.getCd_divisa();
}
public java.lang.String getCd_gruppo_inquadramento() {
	it.cnr.contab.missioni00.tabrif.bulk.Gruppo_inquadramentoBulk gruppoInquadramento = this.getGruppoInquadramento();
	if (gruppoInquadramento == null)
		return null;
	return gruppoInquadramento.getCd_gruppo_inquadramento();
}
/**
 * Insert the method's description here.
 * Creation date: (08/02/2002 17.34.52)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFineValidita() {
	
	if ( (getDt_fine_validita()!=null) && (getDt_fine_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO)))
		return null;
	return getDt_fine_validita();
}
/**
 * Insert the method's description here.
 * Creation date: (23/11/2001 14.26.46)
 * @return it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk getDivisa() {
	return divisa;
}
/**
 * Insert the method's description here.
 * Creation date: (23/11/2001 14.26.46)
 * @return java.util.Collection
 */
public java.util.Collection getGruppiInquadramento() {
	return gruppiInquadramento;
}
/**
 * Insert the method's description here.
 * Creation date: (08/02/2002 16.04.45)
 * @return it.cnr.contab.missioni00.tabrif.bulk.Gruppo_inquadramentoBulk
 */
public Gruppo_inquadramentoBulk getGruppoInquadramento() {
	return gruppoInquadramento;
}
/**
 * Insert the method's description here.
 * Creation date: (23/11/2001 14.26.46)
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
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	
	super.initializeForInsert(bp,context);
	resetImporti();
	setDt_fine_validita(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO);

	return this;	
}
public boolean isRODivisa() {
	return getDivisa()==null || getDivisa().getCrudStatus()==NORMAL;
}
public boolean isRONazione() {

	return getNazione()== null || getNazione().getCrudStatus() == NORMAL;
}
private void resetImporti() {

	setIm_diaria(new java.math.BigDecimal(0));
}
public void setCd_divisa(java.lang.String cd_divisa) {
	this.getDivisa().setCd_divisa(cd_divisa);
}
public void setCd_gruppo_inquadramento(java.lang.String cd_gruppo_inquadramento) {
	this.getGruppoInquadramento().setCd_gruppo_inquadramento(cd_gruppo_inquadramento);
}
/**
 * Insert the method's description here.
 * Creation date: (08/02/2002 17.37.42)
 * @param newDate java.sql.Timestamp
 */
public void setDataFineValidita(java.sql.Timestamp newDate) {
	
	this.setDt_fine_validita(newDate);
}
/**
 * Insert the method's description here.
 * Creation date: (23/11/2001 14.26.46)
 * @param newDivisa it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public void setDivisa(it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk newDivisa) {
	divisa = newDivisa;
}
/**
 * Insert the method's description here.
 * Creation date: (23/11/2001 14.26.46)
 * @param newProfili java.util.Collection
 */
public void setGruppiInquadramento(java.util.Collection newColl) {
	gruppiInquadramento = newColl;
}
/**
 * Insert the method's description here.
 * Creation date: (08/02/2002 16.04.45)
 * @param newGruppoInquadramento it.cnr.contab.missioni00.tabrif.bulk.Gruppo_inquadramentoBulk
 */
public void setGruppoInquadramento(Gruppo_inquadramentoBulk newGruppoInquadramento) {
	gruppoInquadramento = newGruppoInquadramento;
}
/**
 * Insert the method's description here.
 * Creation date: (23/11/2001 14.26.46)
 * @param newNazione it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk
 */
public void setNazione(it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk newNazione) {
	nazione = newNazione;
}
public void setPg_nazione(java.lang.Long pg_nazione) {
	this.getNazione().setPg_nazione(pg_nazione);
}
public void validate() throws ValidationException {

	// controllo su campo INQUADRAMENTO
	if (getCd_gruppo_inquadramento()==null)
		throw new ValidationException( "Il campo INQUADRAMENTO non può essere vuoto" );

	// controllo su campo NAZIONE
	if (getPg_nazione()==null)
		throw new ValidationException( "Il campo NAZIONE non può essere vuoto" );

	// controllo su campo DIVISA
	if (getCd_divisa()==null)
		throw new ValidationException( "Il campo DIVISA non può essere vuoto" );

	// controllo su campo INIZIO VALIDITA
	if (getDt_inizio_validita()==null)
		throw new ValidationException( "Il campo DATA INIZIO VALIDITA non può essere vuoto" );

	// controllo su campo IMPORTO DIARIA
	if (getIm_diaria()==null)
		throw new ValidationException( "Il campo IMPORTO DIARIA non può essere vuoto" );
	if ( getIm_diaria().compareTo(new java.math.BigDecimal(0))<=0 )
		throw new ValidationException( "Il campo IMPORTO DIARIA deve essere maggiore di 0 !" );
}
}
