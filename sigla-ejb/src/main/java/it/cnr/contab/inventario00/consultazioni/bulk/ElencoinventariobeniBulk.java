/*
* Creted by Generator 1.0
* Date 03/03/2005
*/
package it.cnr.contab.inventario00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class ElencoinventariobeniBulk extends ElencoinventariobeniBase {
	public ElencoinventariobeniBulk() {
		super();
	}
	public ElencoinventariobeniBulk(java.lang.String cd_unita_organizzativa, java.lang.String cd_categoria_gruppo, java.lang.Long nr_inventario, java.lang.Long progressivo, java.lang.String ds_bene, java.sql.Timestamp data_registrazione) {
		super(cd_unita_organizzativa, cd_categoria_gruppo, nr_inventario, progressivo, ds_bene, data_registrazione);
	}
}