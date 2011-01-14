package it.cnr.contab.doccont00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (18/06/2003 11.40.00)
 * @author: Simonetta Costa
 */
public class AccertamentoPGiroResiduoBulk extends AccertamentoPGiroBulk {
/**
 * AccertamentoPGiroResiduoBulk constructor comment.
 */
public AccertamentoPGiroResiduoBulk() {
	super();
	initialize();
}
/**
 * AccertamentoPGiroResiduoBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param esercizio java.lang.Integer
 * @param esercizio_originale java.lang.Integer
 * @param pg_accertamento java.lang.Long
 */
public AccertamentoPGiroResiduoBulk(String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_accertamento) {
	super(cd_cds, esercizio, esercizio_originale, pg_accertamento);
	initialize();	
}
// metodo per inizializzare l'oggetto bulk
private void initialize () {
	setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_ACR_RES );
	setFl_pgiro( new Boolean( true ));
}
}
