/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/03/2008
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_tipo_ruolo_privilegioBase extends Ass_tipo_ruolo_privilegioKey implements Keyed {
	public Ass_tipo_ruolo_privilegioBase() {
		super();
	}
	public Ass_tipo_ruolo_privilegioBase(java.lang.String tipo, java.lang.String cd_privilegio) {
		super(tipo, cd_privilegio);
	}
}