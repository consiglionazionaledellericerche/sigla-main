/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 18/02/2010
 */
package it.cnr.contab.docamm00.intrastat.bulk;
import java.util.Dictionary;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Codici_cpaBulk extends Codici_cpaBase {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CODICI_CPA
	 **/
	public Codici_cpaBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CODICI_CPA
	 **/
	public Codici_cpaBulk(java.lang.Long id_cpa) {
		super(id_cpa);
	}
	
	public final static String SERVIZIO = "S";
	public final static String BENE = "B";

	public final static Dictionary BENI_SERVIZI;

	static {
		
		BENI_SERVIZI = new it.cnr.jada.util.OrderedHashtable();
		BENI_SERVIZI.put(BENE,"Bene");
		BENI_SERVIZI.put(SERVIZIO,"Servizio");
	}
	public boolean isROcpa() {
		return getCrudStatus() == OggettoBulk.NORMAL;
	}
	@Override
	public OggettoBulk initializeForInsert(CRUDBP crudbp,
			ActionContext actioncontext) {
		setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(actioncontext.getUserContext()));
		return super.initializeForInsert(crudbp, actioncontext);
	}
	@Override
		public OggettoBulk initializeForSearch(CRUDBP crudbp,
				ActionContext actioncontext) {
			setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(actioncontext.getUserContext()));
			return super.initializeForSearch(crudbp, actioncontext);
		}
	public Dictionary getTi_bene_servizioKeys() {
		
		return BENI_SERVIZI;
	}
}