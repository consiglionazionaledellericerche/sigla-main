package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
public class AccertamentoCdsBulk extends AccertamentoBulk {
public AccertamentoCdsBulk() {
	super();
	initialize();
}
public AccertamentoCdsBulk(String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_accertamento) {
	super(cd_cds, esercizio, esercizio_originale, pg_accertamento);
	initialize();	
}
private void initialize () {
	setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_ACR_SIST );
	setFl_pgiro( new Boolean( false ));
	
}
}
