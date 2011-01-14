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
