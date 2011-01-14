package it.cnr.contab.doccont00.core.bulk;

import 	it.cnr.contab.config00.pdcfin.bulk.*;

public class ImpegnoResiduoBulk extends ObbligazioneBulk {
	Voce_fBulk voce = new Voce_fBulk();
/**
 * ImpegnoPGiroResiduoBulk constructor comment.
 */
public ImpegnoResiduoBulk() {
	super();
	initialize();	
}
/**
 * ImpegnoPGiroResiduoBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param esercizio java.lang.Integer
 * @param esercizio_originale java.lang.Integer
 * @param pg_obbligazione java.lang.Long
 */
public ImpegnoResiduoBulk(String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_obbligazione) {
	super(cd_cds, esercizio, esercizio_originale, pg_obbligazione);
	initialize();	
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2003 13.43.47)
 * @return it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk
 */
public it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk getVoce() {
	return voce;
}
// metodo per inizializzare l'oggetto bulk
private void initialize () {
	setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_IMP_RES );
	setFl_pgiro( new Boolean( false ));
}
public boolean isROVoce() {
	return voce == null || voce.getCrudStatus() == NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (20/06/2003 13.43.47)
 * @param newVoce it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk
 */
public void setVoce(it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk newVoce) {
	voce = newVoce;
}
}
