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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

public class MandatoAccreditamento_rigaHome extends Mandato_rigaHome {
public MandatoAccreditamento_rigaHome(java.sql.Connection conn) {
	super(MandatoAccreditamento_rigaBulk.class, conn);
}
public MandatoAccreditamento_rigaHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(MandatoAccreditamento_rigaBulk.class,conn, persistentCache);
}
/**
 * Metodo per cercare l'impegno di tipo competenza o residuo (IMP o IMP_RES).
 *
 * @param riga <code>Mandato_rigaBulk</code> la riga del mandato
 * @param home istanza di <code>V_impegnoHome</code>
 * @param impegno <code>V_impegnoBulk</code> gli impegni selezionati dall'utente
 * @param clause <code>CompoundFindClause</code> le clausole della selezione
 *
 * @return sql il risultato della selezione
 *
 */
public SQLBuilder selectImpegnoByClause( Mandato_rigaBulk riga, V_impegnoHome home, V_impegnoBulk impegno, CompoundFindClause clause ) throws IntrospectionException, PersistencyException
{
	SQLBuilder sql = ((MandatoAccreditamentoHome)getHomeCache().getHome( MandatoAccreditamentoBulk.class)).selectImpegno( riga.getMandato() );
	if ( riga.getMandato().getTi_competenza_residuo().equals( MandatoBulk.TIPO_COMPETENZA))
		sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP);
	else if ( riga.getMandato().getTi_competenza_residuo().equals( MandatoBulk.TIPO_RESIDUO))
		sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP_RES);	
	sql.addClause( clause );
	return sql;
}

}
