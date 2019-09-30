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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 22/11/2013
 */
package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class VSpesometroBulk extends VSpesometroBase {
	private static java.util.Dictionary meseKeys = new it.cnr.jada.util.OrderedHashtable();
	private static java.util.Dictionary beneservizioKeys= new it.cnr.jada.util.OrderedHashtable();
	private static java.util.Dictionary tipofatturaKeys= new it.cnr.jada.util.OrderedHashtable();
	private static java.util.Dictionary tipofiscalitaKeys= new it.cnr.jada.util.OrderedHashtable();
	
	public static final int GENNAIO = 1;
	public static final int FEBBRAIO = 2;
	public static final int MARZO = 3;
	public static final int APRILE = 4;
	public static final int MAGGIO = 5;
	public static final int GIUGNO = 6;
	public static final int LUGLIO = 7;
	public static final int AGOSTO = 8;
	public static final int SETTEMBRE = 9;
	public static final int OTTOBRE = 10;
	public static final int NOVEMBRE = 11;
	public static final int DICEMBRE = 12;
	static {
		meseKeys.put(new Integer(GENNAIO),"Gennaio");
		meseKeys.put(new Integer(FEBBRAIO),"Febbraio");
		meseKeys.put(new Integer(MARZO),"Marzo");
		meseKeys.put(new Integer(APRILE),"Aprile");
		meseKeys.put(new Integer(MAGGIO),"Maggio");
		meseKeys.put(new Integer(GIUGNO),"Giugno");
		meseKeys.put(new Integer(LUGLIO),"Luglio");
		meseKeys.put(new Integer(AGOSTO),"Agosto");
		meseKeys.put(new Integer(SETTEMBRE),"Settembre");
		meseKeys.put(new Integer(OTTOBRE),"Ottobre");
		meseKeys.put(new Integer(NOVEMBRE),"Novembre");
		meseKeys.put(new Integer(DICEMBRE),"Dicembre");
		
		tipofiscalitaKeys.put("FS","Fiscalità Speciale");
		tipofiscalitaKeys.put("FO","Fiscalità Ordinaria");
		
		tipofatturaKeys.put("ATTIVE","Attiva");
		tipofatturaKeys.put("PASSIVE","Passiva");
		
		beneservizioKeys.put(Fattura_passivaBulk.FATTURA_DI_BENI,"Beni");
		beneservizioKeys.put(Fattura_passivaBulk.FATTURA_DI_SERVIZI,"Servizi");
	}
	boolean flBlacklist=false;
	boolean comunicazionePoliv=false;
	
	public final java.util.Dictionary getBeneservizioKeys() {
		return beneservizioKeys;
	}

	public static void setBeneservizioKeys(java.util.Dictionary beneservizioKeys) {
		VSpesometroBulk.beneservizioKeys = beneservizioKeys;
	}

	public final java.util.Dictionary getTipofatturaKeys() {
		return tipofatturaKeys;
	}

	public static void setTipofatturaKeys(java.util.Dictionary tipofatturaKeys) {
		VSpesometroBulk.tipofatturaKeys = tipofatturaKeys;
	}

	/**
	 * @param dictionary
	 */
	public final java.util.Dictionary getMeseKeys() {
		return meseKeys;
	}
	public static java.util.Dictionary getTipofiscalitaKeys() {
		return tipofiscalitaKeys;
	}

	public static void setTipofiscalitaKeys(java.util.Dictionary tipofiscalitaKeys) {
		VSpesometroBulk.tipofiscalitaKeys = tipofiscalitaKeys;
	}
	/**
	 * @param dictionary
	 */
	public static void setMeseKeys(java.util.Dictionary dictionary) {
		meseKeys = dictionary;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_SPESOMETRO
	 **/
	public VSpesometroBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_SPESOMETRO
	 **/
	public VSpesometroBulk(java.lang.Long pg) {
		super(pg);
	}

	public boolean isFlBlacklist() {
		return flBlacklist;
	}

	public void setFlBlacklist(boolean fl_blacklist) {
		this.flBlacklist = fl_blacklist;
	}

	public boolean isComunicazionePoliv() {
		return comunicazionePoliv;
	}

	public void setComunicazionePoliv(boolean comunicazionePoliv) {
		this.comunicazionePoliv = comunicazionePoliv;
	}
	
	
}