/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.docamm00.docs.bulk;
/**
 * Insert the type's description here.
 * Creation date: (9/5/2001 5:02:18 PM)
 * @author: Ardire Alfonso
 */
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Documento_generico_rigaHome extends BulkHome {
public Documento_generico_rigaHome(java.sql.Connection conn) {
	super(Documento_generico_rigaBulk.class,conn);
}

public Documento_generico_rigaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Documento_generico_rigaBulk.class,conn,persistentCache);
}

/**
 * Inizializza la chiave primaria di un OggettoBulk per un
 * inserimento. Da usare principalmente per riempire i progressivi
 * automatici.
 * @param bulk l'OggettoBulk da inizializzare  
 */
public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {

	if (bulk == null) return;
	try {
		Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)bulk;
		java.sql.Connection contact = getConnection();
		java.sql.ResultSet rs = contact.createStatement().executeQuery("SELECT MAX(PROGRESSIVO_RIGA) FROM "+
		it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
		"DOCUMENTO_GENERICO_RIGA WHERE "+
		"(ESERCIZIO = " + riga.getEsercizio().intValue() + ") AND " + 
		"(CD_CDS = '" + riga.getCd_cds() + "') AND " + 
		"(CD_UNITA_ORGANIZZATIVA = '" + riga.getCd_unita_organizzativa() + "') AND " +
		"(CD_TIPO_DOCUMENTO_AMM = '" + riga.getDocumento_generico().getCd_tipo_documento_amm() + "') AND " + 
		"(PG_DOCUMENTO_GENERICO = " + riga.getPg_documento_generico().longValue() + ")");
		Long x;
		if(rs.next())
			x = new Long(rs.getLong(1) + 1);
		else
			x = new Long(0);
		riga.setProgressivo_riga( x );
	} catch(java.sql.SQLException sqle) {
		throw new PersistencyException(sqle);
	}
}
}