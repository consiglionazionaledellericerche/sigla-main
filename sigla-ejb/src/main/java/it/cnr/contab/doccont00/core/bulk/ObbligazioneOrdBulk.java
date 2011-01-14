package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

import java.util.*;

public class ObbligazioneOrdBulk extends ObbligazioneBulk {

public ObbligazioneOrdBulk() {
	super();
	initialize();
}
public ObbligazioneOrdBulk(String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_obbligazione) {
	super(cd_cds, esercizio, esercizio_originale, pg_obbligazione);
	initialize();	
}
public Map getSaldiInfo() 
{
	Map values = super.getSaldiInfo();
	if ( isCheckDisponibilitaCassaEseguito())
		values.put( "checkDisponibilitaCassaEseguito", new Boolean( true ));
	return values;
}
// Inizializza l'Oggetto Bulk
private void initialize () {
	setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_OBB );
	setFl_pgiro( new Boolean( false ));
}
public boolean isInitialized()
{
	return getLineeAttivitaSelezionateColl().size() != 0  || getNuoveLineeAttivitaColl().size() != 0 ;
}
}
