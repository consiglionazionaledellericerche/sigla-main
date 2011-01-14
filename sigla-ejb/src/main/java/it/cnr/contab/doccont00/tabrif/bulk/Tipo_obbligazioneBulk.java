package it.cnr.contab.doccont00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_obbligazioneBulk extends Tipo_obbligazioneBase {

/**
 * Esegue la validazione formale dei campi di input
 */

public void validate() throws ValidationException 
{
	if ( getCd_tipo_obbligazione() == null )
		throw new ValidationException( "Tipo Obbligazione e' un campo obbligatorio" );
	if ( getDs_tipo_obbligazione() == null )
		throw new ValidationException( "Descrizione e' un campo obbligatorio" );		
}
}
