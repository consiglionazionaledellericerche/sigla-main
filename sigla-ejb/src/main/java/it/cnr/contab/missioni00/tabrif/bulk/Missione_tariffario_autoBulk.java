package it.cnr.contab.missioni00.tabrif.bulk;

import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Missione_tariffario_autoBulk extends Missione_tariffario_autoBase {

public Missione_tariffario_autoBulk() {
	super();
}
public Missione_tariffario_autoBulk(java.lang.String cd_tariffa_auto,java.sql.Timestamp dt_inizio_validita) {
	super(cd_tariffa_auto,dt_inizio_validita);
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 12.29.30)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFineValidita() {

	if ( (getDt_fine_validita()!=null) && (getDt_fine_validita().equals(EsercizioHome.DATA_INFINITO)))
		return null;
	return getDt_fine_validita();
}
/**
 * Insert the method's description here.
 * Creation date: (18/01/2002 14.52.26)
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForInsert(bp,context);

	setDt_fine_validita(EsercizioHome.DATA_INFINITO);
	resetImporti();

	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (18/01/2002 14.25.56)
 */
private void resetImporti() {
	setIndennita_chilometrica(new java.math.BigDecimal(0));
}
/**
 * Insert the method's description here.
 * Creation date: (12/02/2002 12.30.01)
 * @param newTime java.sql.Timestamp
 */
public void setDataFineValidita(java.sql.Timestamp newDataFineValidita) {
	this.setDt_fine_validita(newDataFineValidita);
}
public void validate() throws ValidationException {

	// controllo su campo CODICE
	if (getCd_tariffa_auto() == null )
		throw new ValidationException( "Il campo CODICE non può essere vuoto!" );

	// controllo su campo DESCRIZIONE
	if (getDs_tariffa_auto() == null ) 
		throw new ValidationException( "Il campo DESCRIZIONE non può essere vuoto!" );

	// controllo su campo INDENNITA CHILOMETRICA
	if (getIndennita_chilometrica() == null )
		throw new ValidationException( "Il campo INDENNITA CHILOMETRICA non può essere vuoto!" );
	if (getIndennita_chilometrica().compareTo(new java.math.BigDecimal(0))<=0)
		throw new ValidationException( "Il campo INDENNITA CHILOMETRICA deve essere maggiore di 0!" );

	// controllo su campo DATA INIZIO VALIDITA
	if (getDt_inizio_validita() == null )
		throw new ValidationException( "Il campo DATA INIZIO VALIDITA non può essere vuoto!" );

	// controllo su campo DATA CANCELLAZIONE
	if(getDt_cancellazione()!=null && getDt_cancellazione().compareTo(getDt_inizio_validita())<0)
		throw new ValidationException("Il campo DATA CANCELLAZIONE deve essere superiore alla Data Inizio Validità");
	
}		
}
