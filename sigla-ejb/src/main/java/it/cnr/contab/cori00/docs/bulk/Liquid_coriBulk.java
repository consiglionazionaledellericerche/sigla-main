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

package it.cnr.contab.cori00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
/**
  * OggettoBulk utilizzato per la getione di una Liquidazione CORI. 
**/  
public class Liquid_coriBulk extends Liquid_coriBase {

	// Stringa utilizzata per il richiamo della procedura <code>CNRCTB018.getNextNumDocCont</code>
	public final static String TIPO_DOC = "LIQCORI";
	
	private SimpleBulkList coriColl;
	private it.cnr.contab.config00.sto.bulk.CdsBulk cds;

	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa;
public Liquid_coriBulk() {
	super();
}
public Liquid_coriBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Integer pg_liquidazione) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_liquidazione);
	setCds(new it.cnr.contab.config00.sto.bulk.CdsBulk(cd_cds));
	setUnita_organizzativa(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk(cd_unita_organizzativa));
}
/**
  * Aggiunge un gruppo CORI alla collezione dei gruppi.
  *
  * @param gruppo_cori il <code>Liquid_gruppo_coriBulk</code> gruppo da aggiungere alla collezione.
  *
  * @return getCoriColl().size()-1 la <code>int</code> dimensione della collezione
**/  
public int addToCoriColl (Liquid_gruppo_coriBulk gruppo_cori){

	getCoriColl().add(gruppo_cori);
	gruppo_cori.setLiquidazione_cori(this);

	return getCoriColl().size()-1;
}
public java.lang.String getCd_cds() {
	it.cnr.contab.config00.sto.bulk.CdsBulk cds = this.getCds();
	if (cds == null)
		return null;
	return cds.getCd_unita_organizzativa();
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
	if (unita_organizzativa == null)
		return null;
	return unita_organizzativa.getCd_unita_organizzativa();
}

public it.cnr.contab.config00.sto.bulk.CdsBulk getCds() {
	return cds;
}
/**
 * Restituisce la Collezione dei gruppi CORI.
 * 
 * @return coriColl la <code>SimpleBulkList</code> lista dei gruppi CORI.
 */
public it.cnr.jada.bulk.SimpleBulkList getCoriColl() {
	return coriColl;
}

public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
	return unita_organizzativa;
}
/**
  * Rimuove un gruppo CORI dalla collezione dei gruppi.
  *
  * @param indiceDiLinea <code>int</code> l'indice del gruppo da eliminare
  *
  * @return gruppo_cori il <code>Liquid_gruppo_coriBulk</code> gruppo eliminato
**/  
public Liquid_gruppo_coriBulk removeFromCoriColl( int indiceDiLinea ) {

	Liquid_gruppo_coriBulk element = (Liquid_gruppo_coriBulk)getCoriColl().get(indiceDiLinea);

	return (Liquid_gruppo_coriBulk)getCoriColl().remove(indiceDiLinea);
}
public void setCd_cds(java.lang.String cd_cds) {
	this.getCds().setCd_unita_organizzativa(cd_cds);
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
}

public void setCds(it.cnr.contab.config00.sto.bulk.CdsBulk newCds) {
	cds = newCds;
}

public void setCoriColl(it.cnr.jada.bulk.SimpleBulkList newCoriColl) {
	coriColl = newCoriColl;
}

public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
	unita_organizzativa = newUnita_organizzativa;
}
}
