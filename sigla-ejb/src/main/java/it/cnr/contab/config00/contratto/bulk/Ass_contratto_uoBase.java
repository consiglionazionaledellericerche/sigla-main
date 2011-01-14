/*
* Creted by Generator 1.0
* Date 13/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_contratto_uoBase extends Ass_contratto_uoKey implements Keyed {
	public Ass_contratto_uoBase() {
		super();
	}
	public Ass_contratto_uoBase(java.lang.Integer esercizio, java.lang.String stato_contratto, java.lang.Long pg_contratto, java.lang.String cd_unita_organizzativa) {
		super(esercizio, stato_contratto, pg_contratto, cd_unita_organizzativa);
	}
}