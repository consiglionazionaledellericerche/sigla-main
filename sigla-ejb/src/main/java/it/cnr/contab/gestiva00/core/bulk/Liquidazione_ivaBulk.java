package it.cnr.contab.gestiva00.core.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Liquidazione_ivaBulk extends Liquidazione_ivaBase {

public Liquidazione_ivaBulk() {
	super();
}

public Liquidazione_ivaBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_fine,java.sql.Timestamp dt_inizio,java.lang.Integer esercizio,java.lang.Long report_id) {
	super(cd_cds,cd_unita_organizzativa,dt_fine,dt_inizio,esercizio,report_id);
}

public void validate() throws ValidationException {

	super.validate();
	
	if (getAnnotazioni() != null && getAnnotazioni().length() > 1000)
		throw new ValidationException("Il campo \"annotazioni\" è troppo lungo: il massimo consentito è di 1000 caratteri!");
}
}