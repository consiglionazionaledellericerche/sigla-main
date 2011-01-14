package it.cnr.contab.fondiric00.bp;

import it.cnr.contab.fondiric00.core.bulk.*;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class TestataFondiRicercaBP extends it.cnr.jada.util.action.SimpleCRUDBP {

	private SimpleDetailCRUDController crudDettagli = new SimpleDetailCRUDController( "Dettagli", Fondo_assegnatarioBulk.class, "dettagli", this);

/**
 * TestataFondiRicercaBP constructor comment.
 */
public TestataFondiRicercaBP() {
	super();
}
/**
 * TestataFondiRicercaBP constructor comment.
 * @param function java.lang.String
 */
public TestataFondiRicercaBP(String function) {
	super(function);
}
	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudDettagli() {
		return crudDettagli;
	}

protected void resetTabs(it.cnr.jada.action.ActionContext context) {
	setTab("tab","tabTestata");
}
}
