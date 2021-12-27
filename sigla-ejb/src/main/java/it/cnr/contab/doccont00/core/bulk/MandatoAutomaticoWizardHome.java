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
import it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

import java.util.*;

/**
 * <!-- @TODO: da completare -->
 */

public class MandatoAutomaticoWizardHome extends MandatoIHome {
	public MandatoAutomaticoWizardHome(Class clazz, java.sql.Connection conn) {
		super(clazz,conn);	
	
	}

	public MandatoAutomaticoWizardHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(clazz,conn, persistentCache);	
	
	}

	public MandatoAutomaticoWizardHome(java.sql.Connection conn) {
		super(MandatoAutomaticoWizardBulk.class,conn);
	}

	public MandatoAutomaticoWizardHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(MandatoAutomaticoWizardBulk.class,conn, persistentCache);	
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
	public Collection findImpegni( MandatoAutomaticoWizardBulk mandato ) throws IntrospectionException, PersistencyException 
	{
		return getHomeCache().getHome(V_obbligazioneBulk.class ).fetchAll( selectImpegno( mandato ));
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
	public Collection findTerzi( MandatoAutomaticoWizardBulk mandato ) throws IntrospectionException, PersistencyException 
	{
		return getHomeCache().getHome(V_anagrafico_terzoBulk.class ).fetchAll( selectTerzo( mandato ));
	}

	public Collection<V_doc_passivo_obbligazione_wizardBulk> findDocPassivi( MandatoIBulk mandato, it.cnr.contab.utenze00.bp.CNRUserContext context ) throws IntrospectionException, PersistencyException
	{
		return getHomeCache().getHome(V_doc_passivo_obbligazione_wizardBulk.class).fetchAll( selectDocPassivi( mandato ));
	}

	public Collection<V_doc_passivo_obbligazione_wizardBulk> findDocPassivi(IDocumentoAmministrativoSpesaBulk documento ) throws IntrospectionException, PersistencyException
	{
		return getHomeCache().getHome(V_doc_passivo_obbligazione_wizardBulk.class).fetchAll( selectDocPassivi( documento ));
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
	public SQLBuilder selectImpegno( MandatoAutomaticoWizardBulk mandato ) throws IntrospectionException, PersistencyException 
	{
		PersistentHome home = getHomeCache().getHome( V_obbligazioneBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause( "AND", "esercizio", sql.EQUALS, mandato.getEsercizio());
		sql.addClause( "AND", "cd_cds", sql.EQUALS, mandato.getCd_cds());
		sql.addClause( "AND", "cd_terzo", sql.EQUALS, mandato.getMandato_terzo().getCd_terzo());
 		if (mandato.getTi_impegni().equals(MandatoAutomaticoWizardBulk.IMPEGNI_TIPO_COMPETENZA)) {
			sql.openParenthesis("AND");
			sql.addClause( "OR", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP);
			sql.addClause( "OR", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB);
			sql.closeParenthesis();
		} else if (mandato.getTi_impegni().equals(MandatoAutomaticoWizardBulk.IMPEGNI_TIPO_RESIDUO)) {
			sql.openParenthesis("AND");
			sql.addClause( "OR", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP_RES);
			sql.addClause( "OR", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB_RES);
			sql.addClause( "OR", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA);
			sql.closeParenthesis();
		}
		sql.addSQLClause( "AND", "IM_SCADENZA-IM_ASSOCIATO_DOC_AMM", sql.GREATER, new java.math.BigDecimal(0));

		return sql;
	}

	public SQLBuilder selectTerzo( MandatoIBulk mandato ) throws IntrospectionException, PersistencyException 
	{
		PersistentHome home = getHomeCache().getHome( V_anagrafico_terzoBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause( "AND", "cd_terzo", sql.EQUALS, mandato.getFind_doc_passivi().getCd_terzo());
		sql.addClause( "AND", "cd_precedente", sql.EQUALS, mandato.getFind_doc_passivi().getCd_precedente());
		sql.addClause( "AND", "cognome", sql.EQUALS, mandato.getFind_doc_passivi().getCognome());
		sql.addClause( "AND", "ragione_sociale", sql.EQUALS, mandato.getFind_doc_passivi().getRagione_sociale());
		sql.addClause( "AND", "nome", sql.EQUALS, mandato.getFind_doc_passivi().getNome());
		sql.addClause( "AND", "partita_iva", sql.EQUALS, mandato.getFind_doc_passivi().getPartita_iva());
		sql.addClause( "AND", "codice_fiscale", sql.EQUALS, mandato.getFind_doc_passivi().getCodice_fiscale());

		return sql;
	}
	/**
	 * Metodo per cercare i documenti passivi associati al mandato.
	 *
	 * @param mandato <code>MandatoIBulk</code> il mandato
	 *
	 * @return <code>Collection</code> i documenti passivi associati al mandato
	 *
	 */
	public SQLBuilder selectDocPassivi( MandatoIBulk mandato ) throws IntrospectionException, PersistencyException 
	{
		PersistentHome home = getHomeCache().getHome( V_doc_passivo_obbligazione_wizardBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause( "AND", "esercizio_obbligazione", sql.EQUALS, mandato.getEsercizio());			
		sql.addClause( "AND", "cd_terzo", sql.EQUALS, mandato.getFind_doc_passivi().getCd_terzo());	
		sql.addClause( "AND", "nr_fattura_fornitore", sql.EQUALS, mandato.getFind_doc_passivi().getNr_fattura_fornitore());
		sql.addClause( "AND", "pg_documento_amm", sql.EQUALS, mandato.getFind_doc_passivi().getPg_documento_amm());
		sql.addClause( "AND", "cd_tipo_documento_amm", sql.EQUALS, mandato.getFind_doc_passivi().getCd_tipo_documento_amm());
//		sql.addClause( "AND", "cd_tipo_documento_amm", sql.NOT_EQUALS, Numerazione_doc_ammBulk.TIPO_COMPENSO);
		sql.addClause( "AND", "esercizio_ori_obbligazione", sql.EQUALS, mandato.getFind_doc_passivi().getEsercizio_ori_obbligazione());			
		sql.addClause( "AND", "pg_obbligazione", sql.EQUALS, mandato.getFind_doc_passivi().getPg_obbligazione());
		sql.addClause( "AND", "dt_scadenza", sql.EQUALS, mandato.getFind_doc_passivi().getDt_scadenza());
		sql.addClause( "AND", "im_scadenza", sql.EQUALS, mandato.getFind_doc_passivi().getIm_scadenza());
		sql.addClause( "AND", "ti_pagamento", sql.EQUALS, mandato.getFind_doc_passivi().getTi_pagamento());			
		sql.addClause( "AND", "cd_cds_obbligazione", sql.EQUALS, mandato.getCd_cds());

 		if (((MandatoAutomaticoWizardBulk)mandato).getTi_impegni().equals(MandatoAutomaticoWizardBulk.IMPEGNI_TIPO_COMPETENZA)) {
			sql.openParenthesis("AND");
			sql.addClause( "OR", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP);
			sql.addClause( "OR", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB);
			sql.closeParenthesis();
		} else if (((MandatoAutomaticoWizardBulk)mandato).getTi_impegni().equals(MandatoAutomaticoWizardBulk.IMPEGNI_TIPO_RESIDUO)) {
			sql.openParenthesis("AND");
			sql.addClause( "OR", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP_RES);
			sql.addClause( "OR", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB_RES);
			sql.addClause( "OR", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA);
			sql.closeParenthesis();
		}

		if ( !mandato.TIPO_REGOLARIZZAZIONE.equals( mandato.getTi_mandato()) )
		{
			sql.addClause( "AND", "cd_cds_origine", sql.EQUALS, mandato.getCd_cds());
			sql.addClause( "AND", "cd_uo_origine", sql.EQUALS, mandato.getCd_unita_organizzativa());
		}	
		sql.addSQLClause( "AND", "fl_selezione", sql.EQUALS, "Y");
		sql.addClause( "AND", "stato_cofi", sql.EQUALS, Documento_genericoBulk.STATO_CONTABILIZZATO);	

		sql.addSQLClause( "AND", "IM_SCADENZA", sql.GREATER, new java.math.BigDecimal(0));

		SQLBuilder sqlTerzo = selectTerzo(mandato);
		sqlTerzo.addSQLJoin("V_ANAGRAFICO_TERZO.CD_TERZO", "V_DOC_PASSIVO_OBBLIGAZIONE.CD_TERZO");
		sql.addSQLExistsClause("AND", sqlTerzo);
		return sql;
	}

	public SQLBuilder selectDocPassivi( IDocumentoAmministrativoSpesaBulk documento ) throws IntrospectionException, PersistencyException
	{
		PersistentHome home = getHomeCache().getHome( V_doc_passivo_obbligazione_wizardBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause( FindClause.AND, "cd_cds", SQLBuilder.EQUALS, documento.getCd_cds());
		sql.addClause( FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, documento.getCd_uo());
		sql.addClause( FindClause.AND, "esercizio", SQLBuilder.EQUALS, documento.getEsercizio());
		sql.addClause( FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.EQUALS, documento.getCd_tipo_doc_amm());
		sql.addClause( FindClause.AND, "pg_documento_amm", SQLBuilder.EQUALS, documento.getPg_doc_amm());
		return sql;
	}
}
