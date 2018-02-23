package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipologia_rischioBulk extends Tipologia_rischioBase {

public Tipologia_rischioBulk() {
	super();
}
public Tipologia_rischioBulk(java.lang.String cd_tipologia_rischio,java.sql.Timestamp dt_inizio_validita) {
	super(cd_tipologia_rischio,dt_inizio_validita);
}
/**
 * Insert the method's description here.
 * Creation date: (22/03/2002 11.26.50)
 * @param newData java.sql.Timestamp
 */
public java.sql.Timestamp getDataFineValidita() {

	if ( (getDt_fine_validita()!=null) && (getDt_fine_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO)))
		return null;
	return getDt_fine_validita();
}
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForInsert(bp, context);
	resetAliquote();

	return this;
}
private void resetAliquote(){

	setAliquota_ente(new java.math.BigDecimal(0));
	setAliquota_percipiente(new java.math.BigDecimal(0));
}
/**
 * Insert the method's description here.
 * Creation date: (22/03/2002 11.26.50)
 * @param newData java.sql.Timestamp
 */
public void setDataFineValidita(java.sql.Timestamp newData) {
	
	this.setDt_fine_validita(newData);
}
public void validate() throws ValidationException {

	if ( getCd_tipologia_rischio() == null )
		throw new ValidationException("Il campo CODICE TIPOLOGIA non può essere vuoto");
	if ( getDs_tipologia_rischio() == null )
		throw new ValidationException("Il campo DESCRIZIONE non può essere vuoto");
	if ( getDt_inizio_validita() == null )
		throw new ValidationException( "Il campo DATA INIZIO VALIDITA non può essere vuoto" );
	if ( getAliquota_ente() == null )
		throw new ValidationException("Il campo ALIQUOTA ENTE non può essere vuoto");
	if ( getAliquota_percipiente() == null )
		throw new ValidationException("Il campo ALIQUOTA PERCIPIENTE non può essere vuoto");

}
}
