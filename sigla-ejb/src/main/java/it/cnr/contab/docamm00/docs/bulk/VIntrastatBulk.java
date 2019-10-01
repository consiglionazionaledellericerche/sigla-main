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
 * Date 22/04/2010
 */
package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class VIntrastatBulk extends VIntrastatBase {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_INTRASTAT
	 **/
	public VIntrastatBulk() {
		super();
	}
	public VIntrastatBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_fattura,java.lang.Long pg_riga_intra,java.lang.Long pgStorico) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura,pg_riga_intra,pgStorico);
	}
	String nrProtocolloAcq;
	String nrProtocolloVen;

	public String getNrProtocolloAcq() {
		return nrProtocolloAcq;
	}
	public void setNrProtocolloAcq(String nrProtocolloAcq) {
		this.nrProtocolloAcq = nrProtocolloAcq;
	}
	public String getNrProtocolloVen() {
		return nrProtocolloVen;
	}
	public void setNrProtocolloVen(String nrProtocolloVen) {
		this.nrProtocolloVen = nrProtocolloVen;
	}
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

	public OggettoBulk initialize(CRUDBP bp, ActionContext context) {
		super.initialize(bp, context);
		setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
		return this;
	}
}