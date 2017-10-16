package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.OggettoBulk;

public class AccertamentoOrdBulk extends AccertamentoBulk {
public AccertamentoOrdBulk() 
{
	super();
}
public AccertamentoOrdBulk(String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_accertamento) 
{
	super(cd_cds, esercizio, esercizio_originale, pg_accertamento);
}
/**
 * Inizializza l'Oggetto Bulk.
 * @param bp Il Business Process in uso
 * @param context Il contesto dell'azione
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context)
{
	super.initialize(bp, context);
	
//	setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_ACR );
	setFl_pgiro( new Boolean( false ));

	return this;
}
public boolean isInitialized()
{
	//return getLinee_attivitaColl() != null && getLinee_attivitaColl().size() != 0;
	return getLineeAttivitaColl() != null && getLineeAttivitaColl().size() != 0;
}
}
