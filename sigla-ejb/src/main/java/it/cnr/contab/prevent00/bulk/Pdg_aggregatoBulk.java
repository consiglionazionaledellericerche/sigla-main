package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Adatta e implementa: {@link Pdg_aggregatoBase } 
 * 		perchè si ottengano e si settino gli oggetti complessi.
 * 
 * @author: Bisquadro Vincenzo
 */
 
public class Pdg_aggregatoBulk extends Pdg_aggregatoBase {
    private it.cnr.contab.config00.sto.bulk.CdrBulk cdr;
    private final static java.util.Dictionary statiKeys;

    private final static java.util.Dictionary statiKeys_A;
    private final static java.util.Dictionary statiKeys_B;
    private final static java.util.Dictionary statiKeys_M;
    private final static java.util.Dictionary statiKeys_E;

    public static String STATO_A       = "A";
    public static String STATO_B       = "B";
    public static String STATO_M       = "M";
    public static String STATO_E       = "E";

	static {
		statiKeys = new it.cnr.jada.util.OrderedHashtable();
		statiKeys.put(STATO_A,"A - Iniziale");
		statiKeys.put(STATO_B,"B - Finale");
		statiKeys.put(STATO_M,"M - Modificato per variazioni");
		statiKeys.put(STATO_E,"E - Esaminato per variazioni");

		statiKeys_A = new it.cnr.jada.util.OrderedHashtable();
		statiKeys_A.put(STATO_A,statiKeys.get(STATO_A));
		statiKeys_A.put(STATO_B,statiKeys.get(STATO_B));

		statiKeys_B = new it.cnr.jada.util.OrderedHashtable();
		statiKeys_B.put(STATO_B,statiKeys.get(STATO_B));
		statiKeys_B.put(STATO_M,statiKeys.get(STATO_M));

		statiKeys_M = new it.cnr.jada.util.OrderedHashtable();
		statiKeys_M.put(STATO_M,statiKeys.get(STATO_M));
		statiKeys_M.put(STATO_E,statiKeys.get(STATO_E));

		statiKeys_E = new it.cnr.jada.util.OrderedHashtable();
		statiKeys_E.put(STATO_E,statiKeys.get(STATO_E));
		statiKeys_E.put(STATO_M,statiKeys.get(STATO_M));
		statiKeys_E.put(STATO_B,statiKeys.get(STATO_B));
	}
/**
 * Costruttore standard di Pdg_aggregatoBulk
 */
public Pdg_aggregatoBulk() {
	super();
}
/**
 * Costruttore di Pdg_aggregato_etr_detBulk cui vengono passati in ingresso:
 *		cd_centro_responsabilita, esercizio
 *
 * @param cd_centro_responsabilita java.lang.String
 * @param esercizio java.lang.Integer
 */
public Pdg_aggregatoBulk(java.lang.String cd_centro_responsabilita,java.lang.Integer esercizio) {
	super();
	setCdr(new it.cnr.contab.config00.sto.bulk.CdrBulk(cd_centro_responsabilita));
	setEsercizio(esercizio);
}
/**
 * Restituisce il centro di responsabilità.
 * 
 * @return java.lang.String Cd_centro_responsabilita
 */
public java.lang.String getCd_centro_responsabilita() {
	it.cnr.contab.config00.sto.bulk.CdrBulk cdr = this.getCdr();
	if (cdr == null)
		return null;
	return cdr.getCd_centro_responsabilita();
}
/**
 * Restituisce il Bulk del cdr.
 * 
 * @return it.cnr.contab.config00.sto.bulk.CdrBulk cdr
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCdr() {
	return cdr;
}
/**
 * Restituisce il dizionario {@link java.util.Dictionary } degli stati.
 * 
 * @return java.util.Dictionary statiKeys
 */
public java.util.Dictionary getStatiKeys() {
	if (STATO_A.equals(getStato()))
		return statiKeys_A;
	else if (STATO_B.equals(getStato()))
		return statiKeys_B;
	else if (STATO_M.equals(getStato()))
		return statiKeys_M;
	else if (STATO_E.equals(getStato()))
		return statiKeys_E;
	else
		return statiKeys;
}
/**
 * Init del bp e del context.
 *
 * @param bp it.cnr.jada.util.action.CRUDBP
 *
 * @param context it.cnr.jada.action.ActionContext
 * 
 * @return OggettoBulk Pdg_aggregato_etr_detBulk
 */
public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	return super.initialize(bp, context);
}
/**
 * Imposta il centro di responsabilità.
 *
 * @param cd_centro_responsabilita java.lang.String 
 */
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.getCdr().setCd_centro_responsabilita(cd_centro_responsabilita);
}
/**
 * Imposta il centro di responsabilità.
 *
 * @param newCdr it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public void setCdr(it.cnr.contab.config00.sto.bulk.CdrBulk newCdr) {
	cdr = newCdr;
}
}
