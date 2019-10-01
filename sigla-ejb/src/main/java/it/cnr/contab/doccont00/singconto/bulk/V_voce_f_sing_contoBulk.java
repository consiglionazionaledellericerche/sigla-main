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

package it.cnr.contab.doccont00.singconto.bulk;


import java.util.Dictionary;
import java.util.Hashtable;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_voce_f_sing_contoBulk extends V_voce_f_sing_contoBase {

	public final static Dictionary ti_gestioneKeys = it.cnr.contab.doccont00.core.bulk.SospesoBulk.ti_entrata_spesaKeys;
	public final static Dictionary ti_competenza_residuoKeys;
	public final static String TIPO_COMPETENZA 	= "C";
	public final static String TIPO_RESIDUO 		= "R";		

	static 
	{
		ti_competenza_residuoKeys = new Hashtable();
		ti_competenza_residuoKeys.put(TIPO_COMPETENZA,	"Competenza");
		ti_competenza_residuoKeys.put(TIPO_RESIDUO,	"Residuo");
	};

	protected String cd_cds_ente;
	protected String cd_cds_scrivania;
	
	protected Boolean fl_ente = Boolean.FALSE;
	protected String ti_competenza_residuo;
	protected Elemento_voceBulk elemento_voce = new Elemento_voceBulk();
	protected Voce_fBulk voce_f = new Voce_fBulk();
	protected Unita_organizzativaBulk unita_organizzativa = new Unita_organizzativaBulk();
	protected CdsBulk cds = new CdsBulk();
	protected NaturaBulk natura = new NaturaBulk();
	protected FunzioneBulk funzione = new FunzioneBulk();
	protected Unita_organizzativaBulk area_ricerca = new Unita_organizzativaBulk();
	private it.cnr.jada.persistency.sql.CompoundFindClause sqlClauses = null;
public V_voce_f_sing_contoBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (16/01/2004 10.49.24)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getArea_ricerca() {
	return area_ricerca;
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2004 16.04.26)
 * @return java.lang.String
 */
public java.lang.String getCd_cds_ente() {
	return cd_cds_ente;
}
public java.lang.String getCd_cds_proprio() {
	CdsBulk cds = this.getCds();
	if (cds == null)
		return null;
	return cds.getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2004 16.04.26)
 * @return java.lang.String
 */
public java.lang.String getCd_cds_scrivania() {
	return cd_cds_scrivania;
}
public java.lang.String getCd_elemento_voce() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getCd_elemento_voce();
}
public java.lang.String getCd_funzione() {
	FunzioneBulk funzione = this.getFunzione();
	if (funzione == null)
		return null;
	return funzione.getCd_funzione();
}
public java.lang.String getCd_natura() {
	NaturaBulk natura = this.getNatura();
	if (natura == null)
		return null;
	return natura.getCd_natura();
}
public java.lang.String getCd_proprio_voce() {
	Unita_organizzativaBulk area_ricerca = this.getArea_ricerca();
	if (area_ricerca == null)
		return null;
	return area_ricerca.getCd_unita_organizzativa();
}
public java.lang.String getCd_unita_organizzativa() {
	Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
	if (unita_organizzativa == null)
		return null;
	return unita_organizzativa.getCd_unita_organizzativa();
}
public java.lang.String getCd_voce() {
	it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk voce_f = this.getVoce_f();
	if (this.voce_f == null)
		return null;
	return voce_f.getCd_voce();
}
/**
 * Insert the method's description here.
 * Creation date: (16/01/2004 10.35.53)
 * @return it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public it.cnr.contab.config00.sto.bulk.CdsBulk getCds() {
	return cds;
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2004 16.02.26)
 * @return it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
 */
public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getElemento_voce() {
	return elemento_voce;
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2004 16.02.51)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_ente() {
	return fl_ente;
}
/**
 * Insert the method's description here.
 * Creation date: (16/01/2004 10.49.24)
 * @return it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk
 */
public it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk getFunzione() {
	return funzione;
}
/**
 * Insert the method's description here.
 * Creation date: (16/01/2004 10.49.24)
 * @return it.cnr.contab.config00.pdcfin.bulk.NaturaBulk
 */
public it.cnr.contab.config00.pdcfin.bulk.NaturaBulk getNatura() {
	return natura;
}
/**
 * Insert the method's description here.
 * Creation date: (19/01/2004 12.44.46)
 * @return it.cnr.jada.persistency.sql.CompoundFindClause
 */
public it.cnr.jada.persistency.sql.CompoundFindClause getSqlClauses() {
	return sqlClauses;
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2004 16.27.50)
 * @return java.lang.String
 */
public java.lang.String getTi_competenza_residuo() {
	return ti_competenza_residuo;
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2004 16.27.50)
 * @return java.util.Dictionary
 */
public final static java.util.Dictionary getTi_competenza_residuoKeys() {
	return ti_competenza_residuoKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2004 16.27.50)
 * @return java.util.Dictionary
 */
public final java.util.Dictionary getTi_gestioneKeys() {
	return ti_gestioneKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (16/01/2004 15.36.59)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
	return unita_organizzativa;
}
/**
 * Insert the method's description here.
 * Creation date: (16/01/2004 10.34.11)
 * @return it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk
 */
public it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk getVoce_f() {
	return voce_f;
}
public boolean isEnteInScrivania() 
{
	if (getCd_cds_ente() == null || getCd_cds_scrivania() == null)
		return false;
	return getCd_cds_ente().equals( getCd_cds_scrivania());
}
public boolean isEntrata() {

	return it.cnr.contab.doccont00.core.bulk.SospesoBulk.TIPO_ENTRATA.equalsIgnoreCase(getTi_gestione());
}
public boolean isROArea_ricerca() {
	return area_ricerca == null || area_ricerca.getCrudStatus() == NORMAL;
}
public boolean isROCds() {
	return cds == null || cds.getCrudStatus() == NORMAL;
}
public boolean isROElemento_voce() {
	return elemento_voce == null || elemento_voce.getCrudStatus() == NORMAL;
}
public boolean isROFunzione() {
	return funzione == null || funzione.getCrudStatus() == NORMAL;
}
public boolean isRONatura() {
	return natura == null || natura.getCrudStatus() == NORMAL;
}
public boolean isROUnita_organizzativa() {
	return unita_organizzativa == null || unita_organizzativa.getCrudStatus() == NORMAL;
}
public boolean isROVoce_f() {
	return voce_f == null || voce_f.getCrudStatus() == NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (16/01/2004 10.49.24)
 * @param newArea_ricerca it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setArea_ricerca(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newArea_ricerca) {
	area_ricerca = newArea_ricerca;
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2004 16.04.26)
 * @param newCd_cds_ente java.lang.String
 */
public void setCd_cds_ente(java.lang.String newCd_cds_ente) {
	cd_cds_ente = newCd_cds_ente;
}
public void setCd_cds_proprio(java.lang.String cd_cds_proprio) {
	this.getCds().setCd_unita_organizzativa(cd_cds_proprio);
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2004 16.04.26)
 * @param newCd_cds_scrivania java.lang.String
 */
public void setCd_cds_scrivania(java.lang.String newCd_cds_scrivania) {
	cd_cds_scrivania = newCd_cds_scrivania;
}
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
}
public void setCd_funzione(java.lang.String cd_funzione) {
	this.getFunzione().setCd_funzione(cd_funzione);
}
public void setCd_natura(java.lang.String cd_natura) {
	this.getNatura().setCd_natura(cd_natura);
}
public void setCd_proprio_voce(java.lang.String cd_proprio_voce) {
	this.getArea_ricerca().setCd_unita_organizzativa(cd_proprio_voce);
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
}
public void setCd_voce(java.lang.String cd_voce) {
	this.getVoce_f().setCd_voce(cd_voce);
}
/**
 * Insert the method's description here.
 * Creation date: (16/01/2004 10.35.53)
 * @param newCds it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public void setCds(it.cnr.contab.config00.sto.bulk.CdsBulk newCds) {
	cds = newCds;
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2004 16.02.26)
 * @param newElemento_voce it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
 */
public void setElemento_voce(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk newElemento_voce) {
	elemento_voce = newElemento_voce;
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2004 16.02.51)
 * @param newFl_ente java.lang.Boolean
 */
public void setFl_ente(java.lang.Boolean newFl_ente) {
	fl_ente = newFl_ente;
}
/**
 * Insert the method's description here.
 * Creation date: (16/01/2004 10.49.24)
 * @param newFunzione it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk
 */
public void setFunzione(it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk newFunzione) {
	funzione = newFunzione;
}
/**
 * Insert the method's description here.
 * Creation date: (16/01/2004 10.49.24)
 * @param newNatura it.cnr.contab.config00.pdcfin.bulk.NaturaBulk
 */
public void setNatura(it.cnr.contab.config00.pdcfin.bulk.NaturaBulk newNatura) {
	natura = newNatura;
}
/**
 * Insert the method's description here.
 * Creation date: (19/01/2004 12.44.46)
 * @param newSqlClauses it.cnr.jada.persistency.sql.CompoundFindClause
 */
public void setSqlClauses(it.cnr.jada.persistency.sql.CompoundFindClause newSqlClauses) {
	sqlClauses = newSqlClauses;
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2004 16.27.50)
 * @param newTi_competenza_residuo java.lang.String
 */
public void setTi_competenza_residuo(java.lang.String newTi_competenza_residuo) {
	ti_competenza_residuo = newTi_competenza_residuo;
}
/**
 * Insert the method's description here.
 * Creation date: (16/01/2004 15.36.59)
 * @param newUnita_organizzativa it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
	unita_organizzativa = newUnita_organizzativa;
}
/**
 * Insert the method's description here.
 * Creation date: (16/01/2004 10.34.11)
 * @param newVoce_f it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk
 */
public void setVoce_f(it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk newVoce_f) {
	voce_f = newVoce_f;
}
}
