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

import java.sql.*;
import java.util.*;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

/**
 * <!-- @TODO: da completare -->
 */

public class MandatoAccreditamentoHome extends MandatoHome {
	public MandatoAccreditamentoHome(Class clazz, java.sql.Connection conn) {
		super(clazz,conn);

	}
	public MandatoAccreditamentoHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(clazz,conn, persistentCache);

	}
	public MandatoAccreditamentoHome(java.sql.Connection conn) {
		super(MandatoAccreditamentoBulk.class,conn);
	}
	public MandatoAccreditamentoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(MandatoAccreditamentoBulk.class,conn, persistentCache);

	}
	/**
	 * <!-- @TODO: da completare -->
	 *
	 *
	 * @param mandato
	 * @return
	 * @throws IntrospectionException
	 * @throws PersistencyException
	 */
	public Collection findImpegni( MandatoBulk mandato ) throws IntrospectionException, PersistencyException
	{
		return getHomeCache().getHome(V_impegnoBulk.class ).fetchAll( selectImpegno( mandato ));

	}

	public Collection findMandato_riga( it.cnr.jada.UserContext userContext, MandatoBulk mandato ) throws PersistencyException {
		return this.findMandato_riga(userContext, mandato, true);
	}

	public Collection findMandato_riga( it.cnr.jada.UserContext userContext, MandatoBulk mandato, boolean fetchAll) throws PersistencyException {
		PersistentHome home = getHomeCache().getHome( MandatoAccreditamento_rigaBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause( "AND", "esercizio", sql.EQUALS, mandato.getEsercizio());
		sql.addClause( "AND", "cd_cds", sql.EQUALS, mandato.getCd_cds());
		sql.addClause( "AND", "pg_mandato", sql.EQUALS, mandato.getPg_mandato());
		Collection result = home.fetchAll( sql);
		if (fetchAll) getHomeCache().fetchAll(userContext);
		return result;
	}

	public Mandato_terzoBulk findMandato_terzo( it.cnr.jada.UserContext userContext,MandatoBulk mandato ) throws PersistencyException {
		return this.findMandato_terzo(userContext, mandato, true);
	}

	public Mandato_terzoBulk findMandato_terzo( it.cnr.jada.UserContext userContext, MandatoBulk mandato, boolean fetchAll) throws PersistencyException {
		PersistentHome home = getHomeCache().getHome( MandatoAccreditamento_terzoBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause( "AND", "esercizio", sql.EQUALS, mandato.getEsercizio());
		sql.addClause( "AND", "cd_cds", sql.EQUALS, mandato.getCd_cds());
		sql.addClause( "AND", "pg_mandato", sql.EQUALS, mandato.getPg_mandato());
		Collection result = home.fetchAll( sql);
		if (fetchAll) getHomeCache().fetchAll(userContext);
		return (Mandato_terzoBulk) result.iterator().next();
	}

	public Collection findSituazioneCassaCds(it.cnr.jada.UserContext userContext, RicercaMandatoAccreditamentoBulk ricerca) throws PersistencyException
	{
		Collection result = new LinkedList();
		try
		{
			// creo le clausole in OR per ii CDS selezionati
			String cdsClause = "( B.CD_CDS = ? " ;
			int nrCds = ricerca.getCentriDiSpesaSelezionatiColl().size();
			for ( int i = 2; i <= nrCds; i ++ )
				cdsClause = cdsClause + " OR B.CD_CDS = ? ";
			cdsClause = cdsClause + " ) AND ";

			LoggableStatement ps = new LoggableStatement(getConnection(),"SELECT A.CD_CDS, sum(A.IM_ASSOCIATO_DOC_AMM - A.IM_ASSOCIATO_DOC_CONTABILE) " +
										  "FROM " +
										   it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
										  "OBBLIGAZIONE_SCADENZARIO A, " +
										   it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
										  "OBBLIGAZIONE B " +
										  "WHERE " +
										   cdsClause +
										  "B.STATO_OBBLIGAZIONE = '" + ObbligazioneBulk.STATO_OBB_DEFINITIVO + "' AND " +
										  "B.CD_TIPO_DOCUMENTO_CONT in ('"+ Numerazione_doc_contBulk.TIPO_OBB + "','"+Numerazione_doc_contBulk.TIPO_OBB_RES+ "','"+Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA+"') AND " +
										  "A.DT_SCADENZA <= ?  AND " +
										  "(A.IM_ASSOCIATO_DOC_AMM - A.IM_ASSOCIATO_DOC_CONTABILE) > 0 AND " +
										  "A.CD_CDS = B.CD_CDS AND " +
										  "A.ESERCIZIO = B.ESERCIZIO AND " +
										  "A.ESERCIZIO_ORIGINALE = B.ESERCIZIO_ORIGINALE AND " +
										  "A.PG_OBBLIGAZIONE = B.PG_OBBLIGAZIONE " +
										  "AND   B.ESERCIZIO = " + it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue() +
										  "GROUP BY A.CD_CDS" ,true,this.getClass());
			try
			{
				int index = 1;
				V_disp_cassa_cdsBulk cds;
				for ( Iterator i = ricerca.getCentriDiSpesaSelezionatiColl().iterator(); i.hasNext(); )
				{
					cds = (V_disp_cassa_cdsBulk) i.next();
					ps.setString( index++, cds.getCd_cds());
					cds.setIm_obbligazioni( new java.math.BigDecimal(0));
					cds.setIm_da_trasferire(cds.getIm_disponibilita_cassa().negate());
				}

				ps.setTimestamp( index, ricerca.getDt_scadenza_obbligazioni());
				ResultSet rs = ps.executeQuery();
				try
				{
					String cd_cds;

					while (rs.next())
					{
						cd_cds = rs.getString( 1 );
						cds = (V_disp_cassa_cdsBulk) ricerca.getCentriDiSpesaMap().get( cd_cds );
						cds.setIm_obbligazioni( rs.getBigDecimal(2));
						cds.setIm_da_trasferire( cds.getIm_obbligazioni().subtract( cds.getIm_disponibilita_cassa()));
	//					result.add( cds );
					}
				}
				catch ( SQLException e )
				{
					throw new PersistencyException( e );
				}
				finally
				{
					try{rs.close();}catch( java.sql.SQLException e ){};
				}
			}
			catch( SQLException e )
			{
				throw new PersistencyException( e );
			}
			finally
			{
				try{ps.close();}catch( java.sql.SQLException e ){};
			}
		}
		catch ( SQLException e )
		{
				throw new PersistencyException( e );
		}

		return result;
	}
	/**
	 * <!-- @TODO: da completare -->
	 *
	 *
	 * @param mandato
	 * @return
	 * @throws IntrospectionException
	 * @throws PersistencyException
	 */
	public SQLBuilder selectImpegno( MandatoBulk mandato ) throws IntrospectionException, PersistencyException
	{
		PersistentHome home = getHomeCache().getHome( V_impegnoBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause( "AND", "esercizio", sql.EQUALS, mandato.getEsercizio());
		sql.addSQLClause( "AND", "IM_SCADENZA-IM_ASSOCIATO_DOC_CONTABILE", sql.GREATER, new java.math.BigDecimal(0));
		//join su  VOCE_F per recuperare il cd_cds e la parte 1
		sql.addTableToHeader( "VOCE_F");
		sql.addSQLJoin("VOCE_F.ESERCIZIO", "V_IMPEGNO.ESERCIZIO");
		sql.addSQLJoin("VOCE_F.TI_APPARTENENZA", "V_IMPEGNO.TI_APPARTENENZA");
		sql.addSQLJoin("VOCE_F.TI_GESTIONE", "V_IMPEGNO.TI_GESTIONE");
		sql.addSQLJoin("VOCE_F.CD_VOCE", "V_IMPEGNO.CD_VOCE");
		sql.addSQLClause("AND", "VOCE_F.CD_PROPRIO_VOCE", sql.EQUALS, ((MandatoAccreditamentoBulk)mandato).getCodice_cds());
		sql.addSQLClause("AND", "VOCE_F.CD_PARTE", sql.EQUALS, Elemento_voceHome.PARTE_1);


		if ( mandato instanceof MandatoAccreditamentoBulk && ((MandatoAccreditamentoBulk) mandato).getImpegniSelezionatiColl().size() > 0 )
		{
			sql.openParenthesis( "AND");
			V_impegnoBulk impegno;
			for (Iterator i = ((MandatoAccreditamentoBulk) mandato).getImpegniSelezionatiColl().iterator(); i.hasNext(); )
			{
				impegno = (V_impegnoBulk) i.next();
				sql.openParenthesis( "AND");
				sql.addSQLClause( "AND", "ESERCIZIO_ORIGINALE", sql.NOT_EQUALS, impegno.getEsercizio_originale());
				sql.addSQLClause( "OR", "PG_OBBLIGAZIONE", sql.NOT_EQUALS, impegno.getPg_obbligazione());
				sql.closeParenthesis();
			}
			sql.closeParenthesis();
		}
		return sql;

	}
	/**
	 * <!-- @TODO: da completare -->
	 *
	 *
	 * @param riga
	 * @param home
	 * @param impegno
	 * @param clause
	 * @return
	 * @throws IntrospectionException
	 * @throws PersistencyException
	 */
	public SQLBuilder selectImpegnoByClause( Mandato_rigaBulk riga, V_impegnoHome home, V_impegnoBulk impegno, CompoundFindClause clause ) throws IntrospectionException, PersistencyException
	{
		return selectImpegno( riga.getMandato() );
	}
}
