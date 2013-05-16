package it.cnr.contab.missioni00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_missioneBulk extends Tipo_missioneBase {

public Tipo_missioneBulk() {
	super();
}
public Tipo_missioneBulk(java.lang.String cd_tipo_missione) {
	super(cd_tipo_missione);
}
public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	setFl_valido(null);
	return super.initializeForSearch(bp,context);
}
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	setFl_valido(new Boolean(true));
	return super.initializeForInsert(bp,context);
}
}
