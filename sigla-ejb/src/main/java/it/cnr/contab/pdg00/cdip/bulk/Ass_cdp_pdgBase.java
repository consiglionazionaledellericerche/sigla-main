package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_cdp_pdgBase extends Ass_cdp_pdgKey implements Keyed {

public Ass_cdp_pdgBase() {
	super();
}
public Ass_cdp_pdgBase(java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.String id_matricola,java.lang.Long pg_spesa,java.lang.String ti_appartenenza,java.lang.String ti_gestione,java.lang.String ti_prev_cons) {
	super(cd_centro_responsabilita,cd_elemento_voce,cd_linea_attivita,esercizio,id_matricola,pg_spesa,ti_appartenenza,ti_gestione,ti_prev_cons);
}
}
