package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Termini_pagamentoBase extends Termini_pagamentoKey implements Keyed {

public Termini_pagamentoBase() {
	super();
}
public Termini_pagamentoBase(java.lang.String cd_termini_pag,java.lang.Integer cd_terzo) {
	super(cd_termini_pag,cd_terzo);
}
}
