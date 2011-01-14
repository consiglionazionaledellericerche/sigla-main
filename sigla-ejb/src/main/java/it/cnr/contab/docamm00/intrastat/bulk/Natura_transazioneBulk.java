package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.action.CRUDBP;

public class Natura_transazioneBulk extends Natura_transazioneBase {

public Natura_transazioneBulk() {
	super();
}
public Natura_transazioneBulk(java.lang.Long id_natura_transazione) {
	super(id_natura_transazione);
}
/**
 * Insert the method's description here.
 * Creation date: (5/17/2002 11:42:49 AM)
 * @return boolean
 */
public boolean isROnatura_transazione() {
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
}
