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

import it.cnr.contab.anagraf00.core.bulk.V_anagrafico_terzoBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaBulk;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.Collection;

public class ReversaleAutomaticaWizardHome extends ReversaleIHome {
	public ReversaleAutomaticaWizardHome(Class clazz, java.sql.Connection conn) {
		super(clazz,conn);

	}

	public ReversaleAutomaticaWizardHome(Class clazz, java.sql.Connection conn, PersistentCache persistentCache) {
		super(clazz,conn, persistentCache);

	}

	public ReversaleAutomaticaWizardHome(java.sql.Connection conn) {
		super(ReversaleAutomaticaWizardBulk.class,conn);
	}

	public ReversaleAutomaticaWizardHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(ReversaleAutomaticaWizardBulk.class,conn, persistentCache);
	}

	public Collection findTerzi(ReversaleAutomaticaWizardBulk reversale ) throws IntrospectionException, PersistencyException
	{
		return getHomeCache().getHome(V_anagrafico_terzoBulk.class ).fetchAll( selectTerzo( reversale ));
	}

	public Collection findDocAttivi(IDocumentoAmministrativoSpesaBulk documento ) throws IntrospectionException, PersistencyException
	{
		return getHomeCache().getHome(V_doc_attivo_accertamento_wizardBulk.class ).fetchAll( selectDocAttivi( documento ));
	}

	public Collection findAccertamenti( ReversaleAutomaticaWizardBulk reversale ) throws IntrospectionException, PersistencyException
	{
		return getHomeCache().getHome(Accertamento_scadenzarioBulk.class ).fetchAll( selectAccertamento( reversale ));
	}

	public SQLBuilder selectTerzo(ReversaleIBulk reversale ) throws IntrospectionException, PersistencyException
	{
		PersistentHome home = getHomeCache().getHome( V_anagrafico_terzoBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(FindClause.AND, "cd_terzo", SQLBuilder.EQUALS, reversale.getFind_doc_attivi().getCd_terzo());
		sql.addClause( FindClause.AND, "cd_precedente", SQLBuilder.EQUALS, reversale.getFind_doc_attivi().getCd_precedente());
		sql.addClause( FindClause.AND, "cognome", SQLBuilder.EQUALS, reversale.getFind_doc_attivi().getCognome());
		sql.addClause( FindClause.AND, "ragione_sociale", SQLBuilder.EQUALS, reversale.getFind_doc_attivi().getRagione_sociale());
		sql.addClause( FindClause.AND, "nome", SQLBuilder.EQUALS, reversale.getFind_doc_attivi().getNome());
		sql.addClause( FindClause.AND, "partita_iva", SQLBuilder.EQUALS, reversale.getFind_doc_attivi().getPartita_iva());
		sql.addClause( FindClause.AND, "codice_fiscale", SQLBuilder.EQUALS, reversale.getFind_doc_attivi().getCodice_fiscale());

		return sql;
	}

	public SQLBuilder selectDocAttivi(IDocumentoAmministrativoBulk documento ) throws IntrospectionException, PersistencyException
	{
		PersistentHome home = getHomeCache().getHome( V_doc_attivo_accertamento_wizardBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause( FindClause.AND, "cd_cds", SQLBuilder.EQUALS, documento.getCd_cds());
		sql.addClause( FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, documento.getCd_uo());
		sql.addClause( FindClause.AND, "esercizio", SQLBuilder.EQUALS, documento.getEsercizio());
		sql.addClause( FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.EQUALS, documento.getCd_tipo_doc_amm());
		sql.addClause( FindClause.AND, "pg_documento_amm", SQLBuilder.EQUALS, documento.getPg_doc_amm());
		return sql;
	}

	public SQLBuilder selectAccertamento( ReversaleAutomaticaWizardBulk reversale ) throws IntrospectionException, PersistencyException
	{
		PersistentHome home = getHomeCache().getHome( Accertamento_scadenzarioBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause( FindClause.AND, "esercizio", SQLBuilder.EQUALS, reversale.getEsercizio());
		sql.addClause( FindClause.AND, "cd_cds", sql.EQUALS, reversale.getCd_cds());
		sql.addClause( FindClause.AND, "cd_terzo", sql.EQUALS, reversale.getReversale_terzo().getCd_terzo());
		if (reversale.getTi_accertamenti().equals(ReversaleAutomaticaWizardBulk.ACCERTAMENTI_TIPO_COMPETENZA)) {
			sql.openParenthesis(FindClause.AND);
			sql.addClause( FindClause.OR, "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_ACR);
			sql.closeParenthesis();
		} else if (reversale.getTi_accertamenti().equals(ReversaleAutomaticaWizardBulk.ACCERTAMENTI_TIPO_RESIDUO)) {
			sql.openParenthesis(FindClause.AND);
			sql.addClause( FindClause.OR, "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_ACR_RES);
			sql.closeParenthesis();
		}
		sql.addSQLClause( FindClause.AND, "IM_SCADENZA-IM_ASSOCIATO_DOC_AMM", sql.GREATER, new java.math.BigDecimal(0));

		return sql;
	}

}
