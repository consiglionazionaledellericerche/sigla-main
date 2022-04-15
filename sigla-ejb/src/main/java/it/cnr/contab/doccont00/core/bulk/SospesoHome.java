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

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SQLExceptionHandler;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SQLExceptionHandler;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SospesoHome extends BulkHome {
	/**
	 * <!-- @TODO: da completare --> Costruisce un SospesoHome
	 *
	 * @param conn
	 *            La java.sql.Connection su cui vengono effettuate le operazione
	 *            di persistenza
	 */
	public SospesoHome(java.sql.Connection conn) {
		super(SospesoBulk.class, conn);
	}

	/**
	 * <!-- @TODO: da completare --> Costruisce un SospesoHome
	 *
	 * @param conn
	 *            La java.sql.Connection su cui vengono effettuate le operazione
	 *            di persistenza
	 * @param persistentCache
	 *            La PersistentCache in cui vengono cachati gli oggetti
	 *            persistenti caricati da questo Home
	 */
	public SospesoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(SospesoBulk.class, conn, persistentCache);
	}

	/**
	 * <!-- @TODO: da completare -->
	 *
	 *
	 * @param sospeso
	 * @throws PersistencyException
	 * @throws BusyResourceException
	 * @throws OutdatedResourceException
	 */
	public void aggiornaImportoAssociatoMod1210(SospesoBulk sospeso)
			throws PersistencyException, BusyResourceException,
			OutdatedResourceException {

		if (sospeso == null)
			return;

		lock(sospeso);

		StringBuffer stm = new StringBuffer("UPDATE ");
		stm.append(it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema());
		stm.append(getColumnMap().getTableName());
		stm.append(" SET IM_ASS_MOD_1210 = ? WHERE (");
		stm
				.append("CD_CDS = ? AND ESERCIZIO = ? AND CD_SOSPESO = ? AND TI_ENTRATA_SPESA = ? AND TI_SOSPESO_RISCONTRO = ? )");

		try {
			LoggableStatement ps = new LoggableStatement(getConnection(), stm
					.toString(), true, this.getClass());
			try {
				ps.setBigDecimal(1, sospeso.getIm_ass_mod_1210());
				ps.setString(2, sospeso.getCd_cds());
				ps.setInt(3, sospeso.getEsercizio().intValue());
				ps.setString(4, sospeso.getCd_sospeso());
				ps.setString(5, sospeso.getTi_entrata_spesa());
				ps.setString(6, sospeso.getTi_sospeso_riscontro());

				ps.executeUpdate();
			} finally {
				try {
					ps.close();
				} catch (java.sql.SQLException e) {
				}
				;
			}
		} catch (java.sql.SQLException e) {
			throw SQLExceptionHandler.getInstance().handleSQLException(e,
					sospeso);
		}
	}

	/**
	 * Metodo per cercare i sospesi di entrata (associati alla reversale).
	 *
	 * @param reversale
	 *            <code>ReversaleBulk</code> la reversale
	 *
	 * @return <code>Collection</code> i sospesi di entrata associati alla
	 *         reversale
	 *
	 */
	public Collection findSospesiDiEntrata(ReversaleBulk reversale,boolean tesoreriaUnica)
			throws IntrospectionException, PersistencyException {
		SQLBuilder sql = selectSospesiDiEntrata(reversale, null,tesoreriaUnica);
		return fetchAll(sql);

	}

	/**
	 * Metodo per cercare i sospesi di spesa (associati al mandato).
	 *
	 * @param mandato
	 *            <code>MandatoBulk</code> il mandato
	 *
	 * @return <code>Collection</code> i sospesi di spesa associati al mandato
	 *
	 */
	public Collection findSospesiDiSpesa(MandatoBulk mandato,boolean tesoreriaUnica)
			throws IntrospectionException, PersistencyException {
		SQLBuilder sql = selectSospesiDiSpesa(mandato,null,tesoreriaUnica);
		return fetchAll(sql);
	}

	/**
	 * Metodo per cercare i sospesi figli di un sospeso
	 *
	 * @param sospeso
	 *            <code>SospesoBulk</code> il sospeso
	 *
	 * @return <code>Collection</code> i sospesi figli di un sospeso
	 *
	 */
	public Collection findSospesiFigliColl(it.cnr.jada.UserContext userContext,
										   SospesoBulk sospeso) throws IntrospectionException,
			PersistencyException {
		SQLBuilder sql = createSQLBuilder();
		sql
				.addSQLClause("AND", "esercizio", sql.EQUALS, sospeso
						.getEsercizio());
		sql.addSQLClause("AND", "cd_cds", sql.EQUALS, sospeso.getCd_cds());
		sql.addSQLClause("AND", "ti_entrata_spesa", sql.EQUALS, sospeso
				.getTi_entrata_spesa());
		sql.addSQLClause("AND", "ti_sospeso_riscontro", sql.EQUALS,
				sospeso.TI_SOSPESO);
		sql.addSQLClause("AND", "cd_sospeso_padre", sql.EQUALS, sospeso
				.getCd_sospeso());
		List result = fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return result;

	}

	/**
	 * Metodo per cercare i sospesi di entrata.
	 *
	 * @param sospeso
	 *            <code>SospesoBulk</code> il sospeso
	 *
	 * @return <code>Collection</code> i sospesi di entrata associati alla
	 *         reversale
	 *
	 */
	public Collection findSospeso_det_etrColl(SospesoBulk sospeso)
			throws IntrospectionException, PersistencyException {
		SQLBuilder sql = getHomeCache().getHome(Sospeso_det_etrBulk.class)
				.createSQLBuilder();
		sql.addClause("AND", "esercizio", sql.EQUALS, sospeso.getEsercizio());
		sql.addClause("AND", "cd_cds", sql.EQUALS, sospeso.getCd_cds());
		sql.addClause("AND", "ti_entrata_spesa", sql.EQUALS,
				sospeso.TIPO_ENTRATA);
		sql.addClause("AND", "ti_sospeso_riscontro", sql.EQUALS, sospeso
				.getTi_sospeso_riscontro());
		sql.addClause("AND", "cd_sospeso", sql.EQUALS, sospeso.getCd_sospeso());
		sql.addClause("AND", "stato", sql.EQUALS,
				Sospeso_det_etrBulk.STATO_DEFAULT);
		return getHomeCache().getHome(Sospeso_det_etrBulk.class).fetchAll(sql);

	}

	/**
	 * Metodo per cercare i sospesi di entrata.
	 *
	 * @param sospeso
	 *            <code>SospesoBulk</code> il sospeso
	 *
	 * @return <code>Collection</code> i sospesi di entrata associati alla
	 *         reversale
	 *
	 */
	public Collection findSospeso_det_etrCollEsteso(SospesoBulk sospeso)
			throws IntrospectionException, PersistencyException {
		SQLBuilder sql = getHomeCache().getHome(Sospeso_det_etrBulk.class)
				.createSQLBuilder();
		sql.addClause("AND", "esercizio", sql.EQUALS, sospeso.getEsercizio());
		sql.addClause("AND", "cd_cds", sql.EQUALS, sospeso.getCd_cds());
		sql.addClause("AND", "ti_entrata_spesa", sql.EQUALS,
				sospeso.TIPO_ENTRATA);
		sql.addClause("AND", "ti_sospeso_riscontro", sql.EQUALS, sospeso
				.getTi_sospeso_riscontro());
		sql.addClause("AND", "cd_sospeso", sql.EQUALS, sospeso.getCd_sospeso());
		return getHomeCache().getHome(Sospeso_det_etrBulk.class).fetchAll(sql);

	}

	/**
	 * Metodo per cercare i sospesi di spesa.
	 *
	 * @param sospeso
	 *            <code>SospesoBulk</code> il sospeso
	 *
	 * @return <code>Collection</code> i sospesi di spesa associati al mandato
	 *
	 */
	public Collection findSospeso_det_uscColl(SospesoBulk sospeso)
			throws IntrospectionException, PersistencyException {
		SQLBuilder sql = getHomeCache().getHome(Sospeso_det_uscBulk.class)
				.createSQLBuilder();
		sql.addClause("AND", "esercizio", sql.EQUALS, sospeso.getEsercizio());
		sql.addClause("AND", "cd_cds", sql.EQUALS, sospeso.getCd_cds());
		sql
				.addClause("AND", "ti_entrata_spesa", sql.EQUALS,
						sospeso.TIPO_SPESA);
		sql.addClause("AND", "ti_sospeso_riscontro", sql.EQUALS, sospeso
				.getTi_sospeso_riscontro());
		sql.addClause("AND", "cd_sospeso", sql.EQUALS, sospeso.getCd_sospeso());
		sql.addClause("AND", "stato", sql.EQUALS,
				Sospeso_det_uscBulk.STATO_DEFAULT);
		return getHomeCache().getHome(Sospeso_det_uscBulk.class).fetchAll(sql);

	}

	/**
	 * Metodo per cercare i sospesi di spesa.
	 *
	 * @param sospeso
	 *            <code>SospesoBulk</code> il sospeso
	 *
	 * @return <code>Collection</code> i sospesi di spesa associati al mandato
	 *
	 */
	public Collection findSospeso_det_uscCollEsteso(SospesoBulk sospeso)
			throws IntrospectionException, PersistencyException {
		SQLBuilder sql = getHomeCache().getHome(Sospeso_det_uscBulk.class)
				.createSQLBuilder();
		sql.addClause("AND", "esercizio", sql.EQUALS, sospeso.getEsercizio());
		sql.addClause("AND", "cd_cds", sql.EQUALS, sospeso.getCd_cds());
		sql
				.addClause("AND", "ti_entrata_spesa", sql.EQUALS,
						sospeso.TIPO_SPESA);
		sql.addClause("AND", "ti_sospeso_riscontro", sql.EQUALS, sospeso
				.getTi_sospeso_riscontro());
		sql.addClause("AND", "cd_sospeso", sql.EQUALS, sospeso.getCd_sospeso());
		return getHomeCache().getHome(Sospeso_det_uscBulk.class).fetchAll(sql);

	}

	/**
	 * Metodo per cercare i sospesi di entrata (associati alla reversale).
	 *
	 * @param reversale
	 *            <code>ReversaleBulk</code> la reversale
	 *
	 * @return <code>SQLBuilder</code> la query per recuperare i sospesi di
	 *         entrata associati alla reversale
	 *
	 */
	public SQLBuilder selectSospesiDiEntrata(ReversaleBulk reversale,
											 it.cnr.jada.persistency.sql.CompoundFindClause clausole,boolean tesoreriaUnica) throws PersistencyException{
		SQLBuilder sql = createSQLBuilder();
		Unita_organizzativa_enteBulk uoEnte=null;
		if (clausole != null)
			sql.addClause(clausole);
		sql.addClause("AND", "esercizio", sql.EQUALS, reversale.getEsercizio());
		if (!tesoreriaUnica)
			sql.addClause("AND", "cd_cds", sql.EQUALS, reversale.getCd_cds());
		else{
			uoEnte = (Unita_organizzativa_enteBulk)(getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0));
			if (uoEnte!=null)
				sql.addClause("AND", "cd_cds", sql.EQUALS, uoEnte.getCd_cds());
		}
		// sql.addClause( "AND", "cd_uo_origine", sql.EQUALS,
		// reversale.getCd_uo_origine() );
		sql.addClause("AND", "fl_stornato", sql.EQUALS, new Boolean(false));
		sql.addClause("AND", "ti_entrata_spesa", sql.EQUALS,
				SospesoBulk.TIPO_ENTRATA);
		sql.addClause("AND", "ti_sospeso_riscontro", sql.EQUALS,
				SospesoBulk.TI_SOSPESO);
		/*
		 * ( (stato_sospeso = 'A' and cd_cds_origine = cds_di_scrivania ) or
		 * (stato_sospeso = 'S' and (cd_cds = cds_di_scrivania or cd_cds is
		 * null)))
		 */

		sql.openParenthesis("AND");
		sql.openParenthesis("AND");
		sql.addClause("AND", "stato_sospeso", sql.EQUALS,
				SospesoBulk.STATO_SOSP_ASS_A_CDS);
		sql.addClause("AND", "cd_cds_origine", sql.EQUALS, reversale
				.getCd_cds_origine());
		sql.closeParenthesis();
		sql.openParenthesis("OR");
		sql.addClause("OR", "stato_sospeso", sql.EQUALS,
				SospesoBulk.STATO_SOSP_IN_SOSPESO);
		sql.openParenthesis("AND");
		sql.addClause("AND", "cd_cds_origine", sql.EQUALS, reversale
				.getCd_cds_origine());
		sql.addClause("OR", "cd_cds_origine", sql.ISNULL, null);
		sql.closeParenthesis();
		sql.closeParenthesis();
		sql.closeParenthesis();

		sql.addSQLClause("AND", "IM_SOSPESO - IM_ASSOCIATO", sql.GREATER,
				new java.math.BigDecimal(0));
		for (Iterator i = reversale.getSospeso_det_etrColl().iterator(); i
				.hasNext();)
			sql.addClause("AND", "cd_sospeso", sql.NOT_EQUALS,
					((Sospeso_det_etrBulk) i.next()).getSospeso()
							.getCd_sospeso());

		// carica i sospesi BI per conto corrente speciale
		if (reversale.getCd_unita_organizzativa().equals(
				reversale.getCd_uo_ente())) {
			if (reversale.isBanca_italia())
				sql.addClause("AND", "ti_cc_bi", sql.EQUALS,
						SospesoBulk.TIPO_BANCA_ITALIA);
				// carica i sospesi CC per conto corrente speciale (ente)
			else
				sql.addClause("AND", "ti_cc_bi", sql.EQUALS,
						SospesoBulk.TIPO_CC);
		}

		return sql;

	}

	/**
	 * Metodo per cercare i sospesi di spesa (associati al mandato).
	 *
	 * @param mandato
	 *            <code>MandatoBulk</code> il mandato
	 *
	 * @return <code>SQLBuilder</code> la query per recuperare i sospesi di
	 *         spesa associati al mandato
	 *
	 */
	public SQLBuilder selectSospesiDiSpesa(MandatoBulk mandato,it.cnr.jada.persistency.sql.CompoundFindClause clausole, boolean tesoreriaUnica) throws PersistencyException{
		SQLBuilder sql = createSQLBuilder();
		if (clausole != null)
			sql.addClause(clausole);
		Unita_organizzativa_enteBulk uoEnte=null;
		sql.addClause("AND", "esercizio", sql.EQUALS, mandato.getEsercizio());
		if (!tesoreriaUnica)
			sql.addClause("AND", "cd_cds", sql.EQUALS, mandato.getCd_cds());
		else{
			uoEnte = (Unita_organizzativa_enteBulk)(getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0));
			if (uoEnte!=null)
				sql.addClause("AND", "cd_cds", sql.EQUALS, uoEnte.getCd_cds());
		}
		//altrimenti dovrebbe essere sempre 999
		// sql.addClause( "AND", "cd_uo_origine", sql.EQUALS,
		// mandato.getCd_uo_origine() );
		sql.addClause("AND", "fl_stornato", sql.EQUALS, new Boolean(false));
		sql.addClause("AND", "ti_entrata_spesa", sql.EQUALS,
				SospesoBulk.TIPO_SPESA);
		sql.addClause("AND", "ti_sospeso_riscontro", sql.EQUALS,
				SospesoBulk.TI_SOSPESO);

		/* stato */
		// sql.addClause( "AND", "stato_sospeso", sql.EQUALS,
		// SospesoBulk.STATO_SOSP_ASS_A_CDS );

		sql.openParenthesis("AND");
		sql.openParenthesis("AND");
		sql.addClause("AND", "stato_sospeso", sql.EQUALS,
				SospesoBulk.STATO_SOSP_ASS_A_CDS);
		sql.addClause("AND", "cd_cds_origine", sql.EQUALS, mandato
				.getCd_cds_origine());
		sql.closeParenthesis();
		sql.openParenthesis("OR");
		sql.addClause("OR", "stato_sospeso", sql.EQUALS,
				SospesoBulk.STATO_SOSP_IN_SOSPESO);
		sql.openParenthesis("AND");
		sql.addClause("AND", "cd_cds_origine", sql.EQUALS, mandato
				.getCd_cds_origine());
		sql.addClause("OR", "cd_cds_origine", sql.ISNULL, null);
		sql.closeParenthesis();
		sql.closeParenthesis();
		sql.closeParenthesis();

		sql.openParenthesis("AND");
		sql.addSQLClause("AND", "IM_SOSPESO - IM_ASSOCIATO", sql.GREATER,
				new java.math.BigDecimal(0));
		for (Iterator i = mandato.getSospeso_det_uscColl().deleteIterator(); i
				.hasNext();)
			sql.addClause("OR", "cd_sospeso", sql.EQUALS,
					((Sospeso_det_uscBulk) i.next()).getSospeso()
							.getCd_sospeso());
		sql.closeParenthesis();

		for (Iterator i = mandato.getSospeso_det_uscColl().iterator(); i
				.hasNext();)
			sql.addClause("AND", "cd_sospeso", sql.NOT_EQUALS,
					((Sospeso_det_uscBulk) i.next()).getSospeso()
							.getCd_sospeso());

		sql.openParenthesis("AND");
		sql.openParenthesis("AND"); // tutti quelli non associati 1210
		sql.addSQLClause("AND", "IM_SOSPESO - IM_ASS_MOD_1210", sql.GREATER,
				new java.math.BigDecimal(0));
		sql.closeParenthesis();
		if (mandato instanceof MandatoIBulk
				&& ((MandatoIBulk) mandato).getSospesiDa1210List().size() > 0) {
			sql.openParenthesis("OR");
			// tutti quelli associati a 1210 e inclusi in questo mandato
			for (Iterator i = ((MandatoIBulk) mandato).getSospesiDa1210List()
					.iterator(); i.hasNext();)
				sql
						.addClause("OR", "cd_sospeso", sql.EQUALS, (String) i
								.next());

			sql.closeParenthesis();
		}
		sql.closeParenthesis();

		/*
		 * ???????? vale anche per il mandato ??????? // carica i sospesi BI per
		 * conto corrente speciale if(
		 * reversale.getCd_unita_organizzativa().equals(
		 * reversale.getCd_uo_ente()) ) { if( reversale.isBanca_italia() )
		 * sql.addClause( "AND", "ti_cc_bi", sql.EQUALS,
		 * SospesoBulk.TIPO_BANCA_ITALIA ); // carica i sospesi CC per conto
		 * corrente speciale (ente) else sql.addClause( "AND", "ti_cc_bi",
		 * sql.EQUALS, SospesoBulk.TIPO_CC ); }
		 */
		return sql;

	}
	@Override
	public Persistent completeBulkRowByRow(UserContext userContext, Persistent persistent) throws PersistencyException {
		try {
			SospesoBulk sospeso = (SospesoBulk)persistent;
			String currentStato=null;
			if (sospeso.getFl_stornato()) {
				currentStato = "ANN";
			} else {
				sospeso.setSospesiFigliColl(new BulkList(this.findSospesiFigliColl(userContext, sospeso)));
				for (Iterator iterator = sospeso.getSospesiFigliColl().iterator(); iterator.hasNext();) {
					SospesoBulk figlio = (SospesoBulk) iterator.next();
					if (SospesoBulk.STATO_SOSP_IN_SOSPESO.equals(figlio.getStato_sospeso())) {
						currentStato = figlio.getStato_sospeso();
						break;
					} else if (currentStato==null)
						currentStato = figlio.getStato_sospeso();
					else if (!currentStato.equals(figlio.getStato_sospeso())) {
						currentStato = SospesoBulk.STATO_SOSP_INIZIALE;
						break;
					}
//					if(figlio.getIm_associato().compareTo(BigDecimal.ZERO)==0 && (figlio.getIm_ass_mod_1210().compareTo(BigDecimal.ZERO)==0))
//							currentStato = "LIBERO";
				}
			}
			sospeso.setStatoText(currentStato!=null?currentStato:SospesoBulk.STATO_SOSP_INIZIALE);
		} catch (IntrospectionException e) {
		}
		return super.completeBulkRowByRow(userContext, persistent);
	}
}
