package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Gestione dei dati relativi alla tabella Rif_termini_pagamento
 */

public class Rif_termini_pagamentoBulk extends Rif_termini_pagamentoBase {

/**
 * 
 */
public Rif_termini_pagamentoBulk() {}
public Rif_termini_pagamentoBulk(java.lang.String cd_termini_pag) {
	super(cd_termini_pag);
}
public String getCd_ds_termini_pagamento() {

	String result = "";
	result += ((getCd_termini_pag() == null) ? "n.n." : getCd_termini_pag()) + " - ";
	result += ((getDs_termini_pag() == null) ? "n.n." : getDs_termini_pag());
	return result;
}
}
