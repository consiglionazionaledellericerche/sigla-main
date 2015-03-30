package it.cnr.contab.config00.tabnum.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Numerazione_baseBulk extends Numerazione_baseBase {
	public Numerazione_baseBulk() {
		super();
	}
	public Numerazione_baseBulk(java.lang.String colonna,java.lang.Integer esercizio,java.lang.String tabella) {
		super(colonna,esercizio,tabella);
	}
}
