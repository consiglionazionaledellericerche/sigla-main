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

public class Saldo_cogeBulk extends Saldo_cogeBase {
	
	protected it.cnr.contab.config00.pdcep.bulk.ContoBulk conto = new it.cnr.contab.config00.pdcep.bulk.ContoBulk();
	protected it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo = new it.cnr.contab.anagraf00.core.bulk.TerzoBulk();
	protected it.cnr.contab.config00.sto.bulk.CdsBulk cds = new it.cnr.contab.config00.sto.bulk.CdsBulk();
	protected it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk();		
	public final static Dictionary TIPO = TipoIVA.TipoIVAKeys;

public Saldo_cogeBulk() {
	super();
}
public Saldo_cogeBulk(java.lang.String cd_cds,java.lang.Integer cd_terzo,java.lang.String cd_voce_ep,java.lang.Integer esercizio,java.lang.String ti_istituz_commerc) {
	super(cd_cds,cd_terzo,cd_voce_ep,esercizio,ti_istituz_commerc);
	setTerzo(new it.cnr.contab.anagraf00.core.bulk.TerzoBulk(cd_terzo));
	setCds(new it.cnr.contab.config00.sto.bulk.CdsBulk(cd_cds));
	setConto(new it.cnr.contab.config00.pdcep.bulk.ContoBulk(cd_voce_ep,esercizio));
}
public java.lang.String getCd_cds() {
	it.cnr.contab.config00.sto.bulk.CdsBulk cds = this.getCds();
	if (cds == null)
		return null;
	return cds.getCd_unita_organizzativa();
}
public java.lang.Integer getCd_terzo() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo = this.getTerzo();
	if (terzo == null)
		return null;
	return terzo.getCd_terzo();
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
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo() {
	return terzo;
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
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.getTerzo().setCd_terzo(cd_terzo);
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
 * @param newTerzo it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setTerzo(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newTerzo) {
	terzo = newTerzo;
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
