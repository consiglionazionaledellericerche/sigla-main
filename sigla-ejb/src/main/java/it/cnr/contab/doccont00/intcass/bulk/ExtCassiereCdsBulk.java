/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 30/05/2013
 */
package it.cnr.contab.doccont00.intcass.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class ExtCassiereCdsBulk extends ExtCassiereCdsBase {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: EXT_CASSIERE_CDS
	 **/
		
	public ExtCassiereCdsBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: EXT_CASSIERE_CDS
	 **/
	public ExtCassiereCdsBulk(java.lang.Integer esercizio, java.lang.String codiceProto) {
		super(esercizio, codiceProto);
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
	
}