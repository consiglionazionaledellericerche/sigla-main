/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/02/2010
 */
package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

public class Modalita_erogazioneBulk extends Modalita_erogazioneBase {
	public Modalita_erogazioneBulk() {
		super();
	}
	public Modalita_erogazioneBulk(java.lang.Integer esercizio, java.lang.String cd_modalita_erogazione) {
		super(esercizio, cd_modalita_erogazione);
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
	public boolean isROmodalita_erogazione() {
		return getCrudStatus() == OggettoBulk.NORMAL;
	}
}