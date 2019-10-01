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
* Created by Generator 1.0
* Date 07/08/2006
*/
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class V_stipendi_cofi_dettBulk extends V_stipendi_cofi_dettBase {
	public V_stipendi_cofi_dettBulk() {
		super();
	}

	private java.lang.Integer pg_exec;
	
	private static java.util.Dictionary meseKeys = new it.cnr.jada.util.OrderedHashtable();
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
	public static final int TREDICESIMA = 12;
	public static final int DICEMBRE = 13;
	public static final int RECUPERO_ADD = 15;

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
		meseKeys.put(new Integer(TREDICESIMA),"Tredicesima");
		meseKeys.put(new Integer(DICEMBRE),"Dicembre");
		meseKeys.put(new Integer(RECUPERO_ADD),"Recupero Addizionali");
	}
	
	private static java.util.Dictionary entrata_spesakeys = new it.cnr.jada.util.OrderedHashtable();
	public static final String ENTRATA = "E";
	public static final String SPESA = "S";

	static {
		entrata_spesakeys.put(new String(ENTRATA),"Entrata");
		entrata_spesakeys.put(new String(SPESA),"Spesa");
	}

	private static java.util.Dictionary tipo_flussokeys = new it.cnr.jada.util.OrderedHashtable();

	public static final String PRINCIPALE = "S";
	public static final String ANNULLI = "A";
	public static final String RIMBORSI = "R";
	public static final String IRAP = "I";
	public static final String FIGURATIVI = "F";
	public static final String ADDIZIONALI = "C";

	static {
		tipo_flussokeys.put(new String(PRINCIPALE),"Principale");
		tipo_flussokeys.put(new String(ANNULLI),"Annulli");
		tipo_flussokeys.put(new String(RIMBORSI),"Rimborsi");
		tipo_flussokeys.put(new String(IRAP),"Regionalizzazione IRAP");
		tipo_flussokeys.put(new String(FIGURATIVI),"Contributi Figurativi");
		tipo_flussokeys.put(new String(ADDIZIONALI),"Addizionali Comunali");
	}

	private BulkList v_stipendi_cofi_dett = new BulkList();
	private BulkList batch_log_riga = new BulkList();
	
	public it.cnr.jada.bulk.BulkList getV_stipendi_cofi_dett() {
		return v_stipendi_cofi_dett;
	}
	public void setV_stipendi_cofi_dett(it.cnr.jada.bulk.BulkList newV_stipendi_cofi_dett) {
		v_stipendi_cofi_dett = newV_stipendi_cofi_dett;
	}
	/**
	 * @param dictionary
	 */
	public final java.util.Dictionary getMeseKeys() {
		return meseKeys;
	}
	/**
	 * @param dictionary
	 */
	public static void setMeseKeys(java.util.Dictionary dictionary) {
		meseKeys = dictionary;
	}
	public java.lang.Integer getPg_exec() {
		return pg_exec;
	}
	public void setPg_exec(java.lang.Integer pg_exec) {
		this.pg_exec = pg_exec;
	}
	public BulkList getBatch_log_riga() {
		return batch_log_riga;
	}
	public void setBatch_log_riga(BulkList batch_log_riga) {
		this.batch_log_riga = batch_log_riga;
	}
	public java.util.Dictionary getEntrata_spesaKeys() {
		return entrata_spesakeys;
	}
	public java.util.Dictionary getTipo_flussoKeys() {
		return tipo_flussokeys;
	}
	public OggettoBulk initialize(CRUDBP bp, ActionContext context) {
		super.initialize(bp, context);
		setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
		return this;
	}
}