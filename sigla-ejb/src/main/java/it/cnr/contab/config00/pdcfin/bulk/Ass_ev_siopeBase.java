/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/05/2007
 */
package it.cnr.contab.config00.pdcfin.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_ev_siopeBase extends Ass_ev_siopeKey implements Keyed {
	public Ass_ev_siopeBase() {
		super();
	}
	public Ass_ev_siopeBase(java.lang.Integer esercizio, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce, java.lang.String cd_siope) {
		super(esercizio, ti_appartenenza, ti_gestione, cd_elemento_voce, cd_siope);
	}
}