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

/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 19/02/2009
 */
package it.cnr.contab.compensi00.docs.bulk;
import java.math.BigDecimal;

import java.sql.Timestamp;
import java.util.Dictionary;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.SimpleBulkList;

public class BonusBulk extends BonusBase {
	private it.cnr.jada.bulk.SimpleBulkList BonusNucleoFamColl= new SimpleBulkList();
	Tipo_rapportoBulk tipo_rapporto=new Tipo_rapportoBulk();
	Bonus_condizioniBulk bonus_condizioni=new Bonus_condizioniBulk();
	TerzoBulk terzo=new TerzoBulk();
	private V_terzo_per_compensoBulk vTerzo;
	boolean modificabile;
	BigDecimal limite;
	public final static Dictionary ti_sessoKeys;
	public final static String FEMMINA       = "F";
	public final static String MASCHIO       = "M";
	static {
		ti_sessoKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_sessoKeys.put(MASCHIO,"Maschio");
		ti_sessoKeys.put(FEMMINA,"Femmina");
	}
	
	public BonusBulk() {
		super();
	}
	public BonusBulk(java.lang.Integer esercizio, java.lang.Long pg_bonus) {
		super(esercizio, pg_bonus);
	}

@Override
public Integer getCd_terzo() {
	if (getTerzo()!=null)
		return getTerzo().getCd_terzo();
	return null;
}
@Override
public void setCd_terzo(Integer cd_terzo) {
	getTerzo().setCd_terzo(cd_terzo);
}

public Bonus_condizioniBulk getBonus_condizioni() {
	return bonus_condizioni;
}

public void setBonus_condizioni(Bonus_condizioniBulk bonus_condizioni) {
	this.bonus_condizioni = bonus_condizioni;
}

@Override
public String getCd_condizione() {
	if(getBonus_condizioni()!=null)
		return getBonus_condizioni().getCd_condizione();
	return null;
}
@Override
public void setCd_condizione(String cd_condizione) {
	getBonus_condizioni().setCd_condizione(cd_condizione);
}


public java.util.Dictionary getTi_sessoKeys() {
	return ti_sessoKeys; 
}

public TerzoBulk getTerzo() {
	return terzo;
}

public void setTerzo(TerzoBulk terzo) {
	this.terzo = terzo;
}
public Tipo_rapportoBulk getTipo_rapporto() {
	return tipo_rapporto;
}
public void setTipo_rapporto(Tipo_rapportoBulk tipo_rapporto) {
	this.tipo_rapporto = tipo_rapporto;
}
public it.cnr.jada.bulk.SimpleBulkList getBonusNucleoFamColl() {
	return BonusNucleoFamColl;
}
public void setBonusNucleoFamColl(
		it.cnr.jada.bulk.SimpleBulkList bonusNucleoFamColl) {
	BonusNucleoFamColl = bonusNucleoFamColl;
}
public BulkCollection[] getBulkLists() {
	 return new it.cnr.jada.bulk.BulkCollection[] { this.getBonusNucleoFamColl() };
}
public int addToBonusNucleoFamColl(Bonus_nucleo_famBulk dett){
	dett.setBonus(this);
	dett.setIm_reddito_componente(BigDecimal.ZERO);
	getBonusNucleoFamColl().add(dett);
	return getBonusNucleoFamColl().size()-1;
}
public  Bonus_nucleo_famBulk removeFromBonusNucleoFamColl( int indiceDiLinea ) {
	Bonus_nucleo_famBulk element= (Bonus_nucleo_famBulk )BonusNucleoFamColl.get(indiceDiLinea);
	return (Bonus_nucleo_famBulk )BonusNucleoFamColl.remove(indiceDiLinea);
}
public V_terzo_per_compensoBulk getVTerzo() {
	return vTerzo;
}
public void setVTerzo(V_terzo_per_compensoBulk terzo) {
	vTerzo = terzo;
}
public boolean isModificabile() {
	return modificabile;
}
public boolean isROBonus() {
	return !isModificabile();
}
public void setModificabile(boolean modificabile) {
	this.modificabile = modificabile;
}
public BigDecimal getLimite() {
	return limite;
}
public void setLimite(BigDecimal limite) {
	this.limite = limite;
}
@Override
public String getNome() {
	if ((getTerzo()!=null) && getTerzo().getAnagrafico()!=null)
		return getTerzo().getAnagrafico().getNome();
	return null;
}
public String getCognome() {
	if ((getTerzo()!=null) && getTerzo().getAnagrafico()!=null)
		return getTerzo().getAnagrafico().getCognome();
	return null;
}
@Override
public void setNome(String nome) {
	if ((getTerzo()!=null) && getTerzo().getAnagrafico()!=null)
		getTerzo().getAnagrafico().setNome(nome);
}
public void setCognome(String cognome) {
	if ((getTerzo()!=null) && getTerzo().getAnagrafico()!=null)
		getTerzo().getAnagrafico().setCognome(cognome);
}
@Override
public Timestamp getDt_nascita() {
	if ((getTerzo()!=null) && getTerzo().getAnagrafico()!=null)
		return getTerzo().getAnagrafico().getDt_nascita();
	return null;
}
@Override
public void setDt_nascita(Timestamp timestamp) {
	if ((getTerzo()!=null) && getTerzo().getAnagrafico()!=null)
	super.setDt_nascita(timestamp);
}
}