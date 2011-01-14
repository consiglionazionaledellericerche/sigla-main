package it.cnr.contab.docamm00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_stm_paramin_ft_attivaBulk extends V_stm_paramin_ft_attivaBase {

public V_stm_paramin_ft_attivaBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (5/16/2002 4:37:43 PM)
 * @param spesa it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk
 */
public void completeFrom(it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk docAmm) {

	if (docAmm == null) return;
	
	setCd_cds(docAmm.getCd_cds());
	setCd_uo(docAmm.getCd_unita_organizzativa());
	setEsercizio(new java.math.BigDecimal(docAmm.getEsercizio().doubleValue()));
	setPg_fattura_attiva(new java.math.BigDecimal(docAmm.getPg_fattura_attiva().doubleValue()));
}
}
