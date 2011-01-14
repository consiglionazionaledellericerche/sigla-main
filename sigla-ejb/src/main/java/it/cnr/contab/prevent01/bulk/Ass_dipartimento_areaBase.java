/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 20/01/2007
 */
package it.cnr.contab.prevent01.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_dipartimento_areaBase extends Ass_dipartimento_areaKey implements Keyed {
	public Ass_dipartimento_areaBase() {
		super();
	}
	public Ass_dipartimento_areaBase(java.lang.Integer esercizio, java.lang.String cd_dipartimento, java.lang.String cd_cds_area) {
		super(esercizio, cd_dipartimento, cd_cds_area);
	}
}