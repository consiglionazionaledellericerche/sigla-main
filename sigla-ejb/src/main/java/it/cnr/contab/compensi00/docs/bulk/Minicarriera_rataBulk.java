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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Minicarriera_rataBulk 
	extends Minicarriera_rataBase {

	public static final String STATO_NON_ASS_COMPENSO = "N";
	public static final String STATO_TOTALE_ASS_COMPENSO = "T";

	public static final java.util.Dictionary STATI_ASS_COMPENSO;
	
	static {
		STATI_ASS_COMPENSO = new it.cnr.jada.util.OrderedHashtable();
		STATI_ASS_COMPENSO.put(STATO_NON_ASS_COMPENSO, "Non associata a compenso");
		STATI_ASS_COMPENSO.put(STATO_TOTALE_ASS_COMPENSO, "Associata a compenso");
	}

	private MinicarrieraBulk minicarriera = null;
	
	private CompensoBulk compenso = null;
public Minicarriera_rataBulk() {
	super();
}
public Minicarriera_rataBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_minicarriera,java.lang.Long pg_rata) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_minicarriera,pg_rata);
	setMinicarriera(new it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk(cd_cds,cd_unita_organizzativa,esercizio,pg_minicarriera));
}
public java.lang.String getCd_cds() {
	it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk minicarriera = this.getMinicarriera();
	if (minicarriera == null)
		return null;
	return minicarriera.getCd_cds();
}
public java.lang.String getCd_cds_compenso() {
	it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso = this.getCompenso();
	if (compenso == null)
		return null;
	return compenso.getCd_cds();
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk minicarriera = this.getMinicarriera();
	if (minicarriera == null)
		return null;
	return minicarriera.getCd_unita_organizzativa();
}
public java.lang.String getCd_uo_compenso() {
	it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso = this.getCompenso();
	if (compenso == null)
		return null;
	return compenso.getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (6/25/2002 3:31:41 PM)
 * @return it.cnr.contab.compensi00.docs.bulk.CompensoBulk
 */
public CompensoBulk getCompenso() {
	return compenso;
}
public java.lang.Integer getEsercizio() {
	it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk minicarriera = this.getMinicarriera();
	if (minicarriera == null)
		return null;
	return minicarriera.getEsercizio();
}
public java.lang.Integer getEsercizio_compenso() {
	it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso = this.getCompenso();
	if (compenso == null)
		return null;
	return compenso.getEsercizio();
}
/**
 * Insert the method's description here.
 * Creation date: (6/25/2002 2:45:59 PM)
 * @return it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk
 */
public MinicarrieraBulk getMinicarriera() {
	return minicarriera;
}
public java.lang.Long getPg_compenso() {
	it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso = this.getCompenso();
	if (compenso == null)
		return null;
	return compenso.getPg_compenso();
}
public java.lang.Long getPg_minicarriera() {
	it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk minicarriera = this.getMinicarriera();
	if (minicarriera == null)
		return null;
	return minicarriera.getPg_minicarriera();
}
/**
 * Dictionary contenente i valori disponibili per lo stato di associazione 
 * a compenso
 */
 
public java.util.Dictionary getStato_ass_compensoKeys() {
	return STATI_ASS_COMPENSO;
}
/**
 * Inizializza in creazione o caricamento l'oggetto minicarriera
 */
public void initialize() {

	setDt_inizio_rata(MinicarrieraBulk.getDataOdierna());
	setDt_fine_rata(MinicarrieraBulk.getDataOdierna());
	setDt_scadenza(MinicarrieraBulk.getDataOdierna());

	setIm_rata(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	
	setStato_ass_compenso(STATO_NON_ASS_COMPENSO);
}
/**
 * Restituisce un boolean 'true' se la rata è associata a compenso
 */
 
public boolean isAssociataACompenso() {

	return STATO_TOTALE_ASS_COMPENSO.equalsIgnoreCase(getStato_ass_compenso());
}
public void setCd_cds(java.lang.String cd_cds) {
	this.getMinicarriera().setCd_cds(cd_cds);
}
public void setCd_cds_compenso(java.lang.String cd_cds_compenso) {
	this.getCompenso().setCd_cds(cd_cds_compenso);
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getMinicarriera().setCd_unita_organizzativa(cd_unita_organizzativa);
}
public void setCd_uo_compenso(java.lang.String cd_uo_compenso) {
	this.getCompenso().setCd_unita_organizzativa(cd_uo_compenso);
}
/**
 * Insert the method's description here.
 * Creation date: (6/25/2002 3:31:41 PM)
 * @param newCompenso it.cnr.contab.compensi00.docs.bulk.CompensoBulk
 */
public void setCompenso(CompensoBulk newCompenso) {
	compenso = newCompenso;
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.getMinicarriera().setEsercizio(esercizio);
}
public void setEsercizio_compenso(java.lang.Integer esercizio_compenso) {
	this.getCompenso().setEsercizio(esercizio_compenso);
}
/**
 * Insert the method's description here.
 * Creation date: (6/25/2002 2:45:59 PM)
 * @param newMinicarriera it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk
 */
public void setMinicarriera(MinicarrieraBulk newMinicarriera) {
	minicarriera = newMinicarriera;
}
public void setPg_compenso(java.lang.Long pg_compenso) {
	this.getCompenso().setPg_compenso(pg_compenso);
}
public void setPg_minicarriera(java.lang.Long pg_minicarriera) {
	this.getMinicarriera().setPg_minicarriera(pg_minicarriera);
}
}
