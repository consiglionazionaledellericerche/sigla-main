package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Gestione dei dati relativi alla tabella Tipo_rapporto
 */

public class Tipo_rapportoBulk extends Tipo_rapportoBase {

	public final static java.util.Dictionary DIPENDENTE_ALTRO;
	public final static String DIPENDENTE = "D";
	public final static String ALTRO = "A";
	static {
		DIPENDENTE_ALTRO = new it.cnr.jada.util.OrderedHashtable();
		DIPENDENTE_ALTRO.put(DIPENDENTE,"Dipendenti");
		DIPENDENTE_ALTRO.put(ALTRO,"Altri soggetti");
	}

	public final static java.util.Dictionary TIPI_RAPPORTO;
	public final static String PROFESSIONISTA             = "P";
	public final static String STAGIONALE                 = "S";
	public final static String COLLABORATORE_COORD_E_CONT = "C";
	public final static String ASSIMILATO_A_LAVORO_DIP    = "A";
	public final static String BORSISTI                   = "B";
	static {
		TIPI_RAPPORTO = new it.cnr.jada.util.OrderedHashtable();
		TIPI_RAPPORTO.put(PROFESSIONISTA,"Professionista");
		TIPI_RAPPORTO.put(STAGIONALE,"Stagionale");
		TIPI_RAPPORTO.put(COLLABORATORE_COORD_E_CONT,"Collaboratore coordinato e continuativo");
		TIPI_RAPPORTO.put(ASSIMILATO_A_LAVORO_DIP,"Assimilato a lavoro dipendente");
		TIPI_RAPPORTO.put(BORSISTI,"Borsista");
	}
/**
 * 
 */
public Tipo_rapportoBulk() {}
public Tipo_rapportoBulk(java.lang.String cd_tipo_rapporto) {
	super(cd_tipo_rapporto);
}
	public java.util.Dictionary getTi_dipendente_altroKeys() {
		return DIPENDENTE_ALTRO;
	}

/**
 * Insert the method's description here.
 * Creation date: (13/03/2002 12.43.15)
 * @return it.cnr.jada.bulk.BulkList
 */
public java.util.Dictionary getTipiRapportoKeys() {
	return TIPI_RAPPORTO;
}
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForInsert(bp, context);
	this.setTi_dipendente_altro(DIPENDENTE);
	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (13/03/2002 13.20.03)
 * @return boolean
 */
public boolean isDipendente() {
	return 
		getTi_dipendente_altro() != null &&
		getTi_dipendente_altro().equals(DIPENDENTE);
}
/**
 * Insert the method's description here.
 * Creation date: (13/03/2002 13.20.03)
 * @return boolean
 */
public boolean isFindTipoRapportoRO() {
	
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (13/03/2002 13.20.03)
 * @return boolean
 */
public boolean isTipoRapportoRO() {
	
	return ((getTi_dipendente_altro()==null) || (getTi_dipendente_altro().equals(DIPENDENTE)));
}
}
