package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;

public class Tipo_unita_organizzativaBulk extends Tipo_unita_organizzativaBase {



public Tipo_unita_organizzativaBulk() {
	super();
}
public Tipo_unita_organizzativaBulk(java.lang.String cd_tipo_unita) {
	super(cd_tipo_unita);
}
/**
 * Esegue la validazione formale dei campi di input
 */

public void validate() throws ValidationException {
	if ( getCd_tipo_unita() == null || getCd_tipo_unita().equals(""))
		throw new ValidationException( "Il campo CODICE è obbligatorio." );
	if ( getDs_tipo_unita() == null || getDs_tipo_unita().equals(""))
		throw new ValidationException( "Il campo DESCRIZIONE è obbligatorio." );		
}
}
