/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/01/2008
 */
package it.cnr.contab.doccont00.intcass.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Ext_cassiere00_scartiBulk extends Ext_cassiere00_scartiBase {
	public Ext_cassiere00_scartiBulk() {
		super();
	}
	public Ext_cassiere00_scartiBulk(java.lang.Integer esercizio, java.lang.String nome_file, java.lang.Long pg_esecuzione, java.lang.Long pg_rec) {
		super(esercizio, nome_file, pg_esecuzione, pg_rec);
	}
}