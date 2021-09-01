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

package it.cnr.contab.coepcoan00.core.bulk;

import java.util.Dictionary;

import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Saldo_coanBulk extends Saldo_coanBase {
	protected it.cnr.contab.config00.pdcep.bulk.ContoBulk conto = new it.cnr.contab.config00.pdcep.bulk.ContoBulk();
	protected it.cnr.contab.config00.sto.bulk.CdsBulk cds = new it.cnr.contab.config00.sto.bulk.CdsBulk();
	protected it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk();
	protected it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt = new it.cnr.contab.config00.latt.bulk.WorkpackageBulk();
	public final static Dictionary TIPO = TipoIVA.TipoIVAKeys;
	
public Saldo_coanBulk() {
	super();
}
public Saldo_coanBulk(java.lang.String cd_cds,java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita,java.lang.String cd_unita_organizzativa,java.lang.String cd_voce_ep,java.lang.Integer esercizio,java.lang.String ti_istituz_commerc) {
	super(cd_cds,cd_centro_responsabilita,cd_linea_attivita,cd_unita_organizzativa,cd_voce_ep,esercizio,ti_istituz_commerc);
	setCds(new it.cnr.contab.config00.sto.bulk.CdsBulk(cd_cds));
	setLatt(new it.cnr.contab.config00.latt.bulk.WorkpackageBulk(cd_centro_responsabilita,cd_linea_attivita));
	setConto(new it.cnr.contab.config00.pdcep.bulk.ContoBulk(cd_voce_ep,esercizio));
}
public java.lang.String getCd_cds() {
	it.cnr.contab.config00.sto.bulk.CdsBulk cds = this.getCds();
	if (cds == null)
		return null;
	return cds.getCd_unita_organizzativa();
}
public java.lang.String getCd_centro_responsabilita() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt = this.getLatt();
	if (latt == null)
		return null;
	it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita = latt.getCentro_responsabilita();
	if (centro_responsabilita == null)
		return null;
	return centro_responsabilita.getCd_centro_responsabilita();
}
public java.lang.String getCd_linea_attivita() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt = this.getLatt();
	if (latt == null)
		return null;
	return latt.getCd_linea_attivita();
}
public java.lang.String getCd_voce_ep() {
	it.cnr.contab.config00.pdcep.bulk.ContoBulk conto = this.getConto();
	if (conto == null)
		return null;
	return conto.getCd_voce_ep();
}
/**
 * @return it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public it.cnr.contab.config00.sto.bulk.CdsBulk getCds() {
	return cds;
}
/**
 * @return it.cnr.contab.config00.pdcep.bulk.ContoBulk
 */
public it.cnr.contab.config00.pdcep.bulk.ContoBulk getConto() {
	return conto;
}
/**
 * @return it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
 */
public it.cnr.contab.config00.latt.bulk.WorkpackageBulk getLatt() {
	return latt;
}
/**
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUo() {
	return uo;
}
/**
 * Inizializza l'Oggetto Bulk per la ricerca.
 * @param bp Il Business Process in uso
 * @param context Il contesto dell'azione
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
{
	setEsercizio( ((it.cnr.contab.utenze00.bp.CNRUserContext)context.getUserContext()).getEsercizio() );
	setCd_cds( ((it.cnr.contab.utenze00.bp.CNRUserContext)context.getUserContext()).getCd_cds());
	setUo(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa( context));	
	return this;
}
public void setCd_cds(java.lang.String cd_cds) {
	this.getCds().setCd_unita_organizzativa(cd_cds);
}
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.getLatt().getCentro_responsabilita().setCd_centro_responsabilita(cd_centro_responsabilita);
}
public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
	this.getLatt().setCd_linea_attivita(cd_linea_attivita);
}
public void setCd_voce_ep(java.lang.String cd_voce_ep) {
	this.getConto().setCd_voce_ep(cd_voce_ep);
}
/**
 * @param newCds it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public void setCds(it.cnr.contab.config00.sto.bulk.CdsBulk newCds) {
	cds = newCds;
}
/**
 * @param newConto it.cnr.contab.config00.pdcep.bulk.ContoBulk
 */
public void setConto(it.cnr.contab.config00.pdcep.bulk.ContoBulk newConto) {
	conto = newConto;
}
/**
 * @param newLatt it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
 */
public void setLatt(it.cnr.contab.config00.latt.bulk.WorkpackageBulk newLatt) {
	latt = newLatt;
}
/**
 * @param newUo it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUo(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUo) {
	uo = newUo;
}
public Dictionary getTi_istituz_commercKeys() {
	return TIPO;
}
}
