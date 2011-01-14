package it.cnr.contab.docamm00.views.bulk;

import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vsx_rif_protocollo_ivaBulk extends Vsx_rif_protocollo_ivaBase {

public Vsx_rif_protocollo_ivaBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (5/16/2002 4:37:43 PM)
 * @param spesa it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk
 */
public void completeFrom(it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk docAmm) {


	//SARA' de generalizzare per tutti i documenti amministrativi
	if (docAmm == null) return;
	
	setCd_cds(docAmm.getCd_cds());
	setCd_unita_organizzativa(docAmm.getCd_unita_organizzativa());
	setEsercizio(docAmm.getEsercizio());
	setPg_fattura(docAmm.getPg_fattura_attiva());
	setTipo_documento_amm(it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA);
	setProc_name("CNRCTB150.vsx_protocollazione_doc");
}
}
