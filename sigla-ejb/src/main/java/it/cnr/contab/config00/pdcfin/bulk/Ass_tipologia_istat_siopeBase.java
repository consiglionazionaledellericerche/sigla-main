/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/06/2007
 */
package it.cnr.contab.config00.pdcfin.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_tipologia_istat_siopeBase extends Ass_tipologia_istat_siopeKey implements Keyed {
	public Ass_tipologia_istat_siopeBase() {
		super();
	}
	public Ass_tipologia_istat_siopeBase(java.lang.Integer pg_tipologia, java.lang.Integer esercizio_siope, java.lang.String ti_gestione_siope, java.lang.String cd_siope) {
		super(pg_tipologia, esercizio_siope, ti_gestione_siope, cd_siope);
	}
}