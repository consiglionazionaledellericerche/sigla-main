package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vp_liquid_iva_annualeBulk extends Vp_liquid_iva_annualeBase {

public Vp_liquid_iva_annualeBulk() {
	super();
}
public Vp_liquid_iva_annualeBulk(
	Integer esercizio,
	Long id,
	Long sequenza,
	String tipo) {
		
	super(esercizio, id, sequenza, tipo);
}
}
