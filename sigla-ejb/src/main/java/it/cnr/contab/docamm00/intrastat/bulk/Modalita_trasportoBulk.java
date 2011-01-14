package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.action.CRUDBP;

public class Modalita_trasportoBulk extends Modalita_trasportoBase {

public Modalita_trasportoBulk() {
	super();
}
public Modalita_trasportoBulk(java.lang.String cd_modalita_trasporto,java.lang.Integer esercizio) {
	super(cd_modalita_trasporto,esercizio);
}
/**
 * Insert the method's description here.
 * Creation date: (5/17/2002 11:42:49 AM)
 * @return boolean
 */
public boolean isROmodalita_trasporto() {
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
