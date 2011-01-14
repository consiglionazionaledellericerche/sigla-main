package it.cnr.contab.fondecon00.views.bulk;

import it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vsx_reintegro_fondoBulk extends Vsx_reintegro_fondoBase {

public Vsx_reintegro_fondoBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (5/16/2002 4:37:43 PM)
 * @param spesa it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk
 */
public void completeFrom(Fondo_spesaBulk spesa) {

	if (spesa == null) return;
	
	setCd_cds(spesa.getCd_cds());
	setCd_codice_fondo(spesa.getCd_codice_fondo());
	setCd_uo(spesa.getCd_unita_organizzativa());
	setEsercizio(spesa.getEsercizio());
	setPg_fondo_spesa(spesa.getPg_fondo_spesa());
	setProc_name("CNRCTB130.vsx_reintegraSpeseFondo");
	setUser(spesa.getUser());
}
}
