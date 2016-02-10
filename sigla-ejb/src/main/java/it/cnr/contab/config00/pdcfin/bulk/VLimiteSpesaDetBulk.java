/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/02/2011
 */
package it.cnr.contab.config00.pdcfin.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class VLimiteSpesaDetBulk extends VLimiteSpesaDetBase {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_LIMITE_SPESA_DET
	 **/
	public static final java.util.Dictionary fonteKeys = new it.cnr.jada.util.OrderedHashtable();

	final public static String FONTE_INTERNA = "FIN";
	final public static String FONTE_ESTERNA = "FES";
	
	static {
		fonteKeys.put(FONTE_INTERNA,"Interna");
		fonteKeys.put(FONTE_ESTERNA,"Esterna");
	}
	public VLimiteSpesaDetBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_LIMITE_SPESA_DET
	 **/
	public VLimiteSpesaDetBulk(java.lang.Integer esercizio, java.lang.String cdCds, java.lang.String tiAppartenenza, java.lang.String tiGestione, java.lang.String cdElementoVoce, java.lang.String fonte) {
		super(esercizio, cdCds, tiAppartenenza, tiGestione, cdElementoVoce, fonte);
	}
}