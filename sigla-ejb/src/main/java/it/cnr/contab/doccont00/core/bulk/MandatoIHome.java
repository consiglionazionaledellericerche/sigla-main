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


import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;

import java.util.Collection;

public class MandatoIHome extends MandatoHome {
    public MandatoIHome(Class clazz, java.sql.Connection conn) {
        super(clazz, conn);
    }

    public MandatoIHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
        super(clazz, conn, persistentCache);

    }

    public MandatoIHome(java.sql.Connection conn) {
        super(MandatoIBulk.class, conn);
    }

    public MandatoIHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
        super(MandatoIBulk.class, conn, persistentCache);

    }

    /**
     * Metodo per cercare i documenti attivi associati all'accertamento selezioanto per la regolarizzazione
     *
     * @param userContext <code>CNRUserContext</code>
     * @param mandato <code>MandatoIBulk</code> il mandato
     * @return <code>Collection</code> i documenti attivi associati all'accertamento
     */
    public Collection findDocAttiviPerRegolarizzazione(it.cnr.contab.utenze00.bp.CNRUserContext userContext, MandatoIBulk mandato) throws IntrospectionException, PersistencyException, java.sql.SQLException {
        PersistentHome home = getHomeCache().getHome(V_doc_attivo_accertamentoBulk.class);
        EnteBulk ente = (EnteBulk) getHomeCache().getHome(EnteBulk.class).findAll().get(0);
        String stm = "SELECT * FROM " +
                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                "V_DOC_ATTIVO_ACCERTAMENTO A " +
                "WHERE " +
                "A.CD_CDS = ? " +
                "AND A.STATO_COFI <> ? " +
                "AND A.STATO_COFI <> ? " +
                "AND A.CD_CDS_ACCERTAMENTO = ? " +
                "AND A.ESERCIZIO_ACCERTAMENTO = ? " +
                "AND A.ESERCIZIO_ORI_ACCERTAMENTO = ? " +
                "AND A.PG_ACCERTAMENTO = ? " +
                "AND NOT EXISTS ( SELECT 1 FROM " +
                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                "V_DOC_ATTIVO_ACCERTAMENTO B " +
                "WHERE " +
                "A.CD_CDS_ACCERTAMENTO = B.CD_CDS_ACCERTAMENTO " +
                "AND A.ESERCIZIO_ACCERTAMENTO = B.ESERCIZIO_ACCERTAMENTO " +
                "AND A.ESERCIZIO_ORI_ACCERTAMENTO = B.ESERCIZIO_ORI_ACCERTAMENTO " +
                "AND A.PG_ACCERTAMENTO = B.PG_ACCERTAMENTO " +
                "AND A.PG_ACCERTAMENTO_SCADENZARIO = B.PG_ACCERTAMENTO_SCADENZARIO " +
                "AND ( B.TI_FATTURA = ? " +
                "OR B.TI_FATTURA = ? )) ";
        if (!mandato.getCd_cds().equals(ente.getCd_unita_organizzativa())) //mandato CDS
        {
            String stm2 = "AND A.CD_CDS_ORIGINE = ? " +
                    "AND A.CD_UO_ORIGINE = ? ";
            stm = stm.concat(stm2);
        }

        LoggableStatement ps = new LoggableStatement(getConnection(), stm, true, this.getClass());
        try {
            ps.setString(1, mandato.getCd_cds());
            ps.setString(2, Documento_generico_rigaBulk.STATO_ANNULLATO);
            ps.setString(3, Documento_generico_rigaBulk.STATO_PAGATO);
            ps.setString(4, mandato.getAccertamentoPerRegolarizzazione().getCd_cds());
            ps.setObject(5, mandato.getAccertamentoPerRegolarizzazione().getEsercizio());
            ps.setObject(6, mandato.getAccertamentoPerRegolarizzazione().getEsercizio_originale());
            ps.setObject(7, mandato.getAccertamentoPerRegolarizzazione().getPg_accertamento());
            ps.setString(8, Fattura_attiva_IBulk.TIPO_NOTA_DI_CREDITO);
            ps.setString(9, Fattura_attiva_IBulk.TIPO_NOTA_DI_DEBITO);
            if (!mandato.getCd_cds().equals(ente.getCd_unita_organizzativa())) //mandato CDS
            {
                ps.setString(10, mandato.getCd_cds_origine());
                ps.setString(11, mandato.getCd_uo_origine());
            }
            java.sql.ResultSet rs = ps.executeQuery();
            try {
                SQLBroker broker = home.createBroker(ps, rs);
                return home.fetchAll(broker);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
                ;
            }
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
            ;
        }

	
						
	
	/*
	PersistentHome home = getHomeCache().getHome( V_doc_attivo_accertamentoBulk.class );
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause( "AND", "cd_cds", sql.EQUALS, mandato.getCd_cds());
	sql.addClause(	"AND", "stato_cofi", sql.NOT_EQUALS, Documento_generico_attivoBulk.STATO_ANNULLATO );
	sql.addClause(	"AND", "stato_cofi", sql.NOT_EQUALS, Documento_generico_attivoBulk.STATO_PAGATO );
	sql.addClause(	"AND", "cd_cds_accertamento", sql.EQUALS, mandato.getAccertamentoPerRegolarizzazione().getCd_cds());
	sql.addClause(	"AND", "esercizio_accertamento", sql.EQUALS, mandato.getAccertamentoPerRegolarizzazione().getEsercizio());
	sql.addClause(	"AND", "esercizio_ori_accertamento", sql.EQUALS, mandato.getAccertamentoPerRegolarizzazione().getEsercizio_originale());
	sql.addClause(	"AND", "pg_accertamento", sql.EQUALS, mandato.getAccertamentoPerRegolarizzazione().getPg_accertamento());	
	EnteBulk ente = (EnteBulk) getHomeCache().getHome(EnteBulk.class).findAll().get(0);
	if ( !mandato.getCd_cds().equals( ente.getCd_unita_organizzativa() )) //mandato CDS
	{
		sql.addClause( "AND", "cd_cds_origine", sql.EQUALS, mandato.getCd_cds_origine()  );
		sql.addClause( "AND", "cd_uo_origine", sql.EQUALS, mandato.getCd_uo_origine()  );
	}
	//escludo documenti con note di debito/credito
	SQLBuilder sql2 = home.createSQLBuilder();
	sql2.setHeader( "select * from V_doc_attivo_accertamento B" );
	sql2.addSQLJoin( "V_doc_attivo_accertamento.cd_cds_accertamento", "B.cd_cds_accertamento" );
	sql2.addSQLJoin( "V_doc_attivo_accertamento.esercizio_accertamento", "B.esercizio_accertamento" );
	sql2.addSQLJoin( "V_doc_attivo_accertamento.esercizio_ori_accertamento", "B.esercizio_ori_accertamento" );
	sql2.addSQLJoin( "V_doc_attivo_accertamento.pg_accertamento", "B.pa_accertamento" );
	sql2.addSQLJoin( "V_doc_attivo_accertamento.pg_accertamento_scadenzario", "B.pg_accertamento_scadenzario" );
	sql2.openParenthesis( "AND");
	sql2.addSQLClause( "AND", "b.ti_fattura", sql.EQUALS, Fattura_attiva_IBulk.TIPO_NOTA_DI_CREDITO );
	sql2.addSQLClause( "OR", "b.ti_fattura", sql.EQUALS, Fattura_attiva_IBulk.TIPO_NOTA_DI_DEBITO );
	sql2.closeParenthesis();

	sql.addSQLNotExistsClause( "AND", sql2 );
	return home.fetchAll( sql); 
	*/


    }

    /**
     * Metodo per cercare i documenti passivi associati al mandato.
     *
     * @param mandato <code>MandatoIBulk</code> il mandato
     * @param context <code>CNRUserContext</code>
     * @return <code>Collection</code> i documenti passivi associati al mandato
     */
    public Collection findDocPassivi(MandatoIBulk mandato, it.cnr.contab.utenze00.bp.CNRUserContext context) throws IntrospectionException, PersistencyException {
        PersistentHome home = getHomeCache().getHome(V_doc_passivo_obbligazioneBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
	/*
	CompoundFindClause clauses = mandato.getFind_doc_passivi().buildFindClauses(null);
	sql.addClause( clauses );
	*/
	/* simona 9.12.2002
	sql.openParenthesis( "AND" );
	sql.openParenthesis( "AND" );		
	sql.addClause( "AND", "cd_terzo", sql.EQUALS, mandato.getFind_doc_passivi().getCd_terzo());
	sql.addClause( "AND", "cd_terzo_cessionario", sql.ISNULL, null);
	sql.closeParenthesis() ;
	sql.addClause( "OR", "cd_terzo_cessionario", sql.EQUALS, mandato.getFind_doc_passivi().getCd_terzo());
	sql.closeParenthesis() ;
	*/

        /* simona 16.06.2003: aggiunta la clausola sull'esercizio_obbligazione x gestire documenti riportati */
        sql.addSQLClause("AND", "esercizio_obbligazione", sql.EQUALS, (((it.cnr.contab.utenze00.bp.CNRUserContext) context).getEsercizio()));
        sql.addClause("AND", "cd_terzo", sql.EQUALS, mandato.getFind_doc_passivi().getCd_terzo());
        sql.addClause("AND", "nr_fattura_fornitore", sql.EQUALS, mandato.getFind_doc_passivi().getNr_fattura_fornitore());
        sql.addClause("AND", "pg_documento_amm", sql.EQUALS, mandato.getFind_doc_passivi().getPg_documento_amm());
        sql.addClause("AND", "cd_tipo_documento_amm", sql.EQUALS, mandato.getFind_doc_passivi().getCd_tipo_documento_amm());
        sql.addClause("AND", "esercizio_ori_obbligazione", sql.EQUALS, mandato.getFind_doc_passivi().getEsercizio_ori_obbligazione());
        sql.addClause("AND", "pg_obbligazione", sql.EQUALS, mandato.getFind_doc_passivi().getPg_obbligazione());
        sql.addClause("AND", "dt_scadenza", sql.EQUALS, mandato.getFind_doc_passivi().getDt_scadenza());
        sql.addClause("AND", "im_scadenza", sql.EQUALS, mandato.getFind_doc_passivi().getIm_scadenza());
        sql.addClause("AND", "ti_pagamento", sql.EQUALS, mandato.getFind_doc_passivi().getTi_pagamento());

//	sql.addClause( "AND", "cd_unita_organizzativa", sql.EQUALS, mandato.getCd_unita_organizzativa());
        sql.addClause("AND", "cd_cds_obbligazione", sql.EQUALS, mandato.getCd_cds());
        if (!mandato.TIPO_REGOLARIZZAZIONE.equals(mandato.getTi_mandato())) {
            sql.addClause("AND", "cd_cds_origine", sql.EQUALS, context.getCd_cds());
            sql.addClause("AND", "cd_uo_origine", sql.EQUALS, context.getCd_unita_organizzativa());
        }
        sql.addSQLClause("AND", "fl_selezione", sql.EQUALS, "Y");
        sql.addClause("AND", "stato_cofi", sql.EQUALS, Documento_genericoBulk.STATO_CONTABILIZZATO);

/*
	sql.openParenthesis( "AND");
	sql.openParenthesis( "AND");
	sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP);
	sql.addSQLClause( "AND", "IM_TOTALE_DOC_AMM - IM_ASSOCIATO_DOC_CONTABILE", sql.GREATER, new java.math.BigDecimal(0));
	sql.closeParenthesis();
	sql.openParenthesis( "OR");
	sql.addClause( "AND", "cd_tipo_documento_cont", sql.NOT_EQUALS, Numerazione_doc_contBulk.TIPO_IMP);	
	sql.addSQLClause( "AND", "IM_SCADENZA - IM_ASSOCIATO_DOC_CONTABILE", sql.GREATER, new java.math.BigDecimal(0));
	sql.closeParenthesis();
	sql.closeParenthesis();
	*/
        sql.addSQLClause("AND", "IM_SCADENZA", sql.GREATER, new java.math.BigDecimal(0));


        sql.addOrderBy("esercizio_ori_obbligazione, pg_obbligazione");
        return home.fetchAll(sql);

    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param docPassivo
     * @return
     * @throws PersistencyException
     */
    public Collection findDocPassiviCollegati(V_doc_passivo_obbligazioneBulk docPassivo) throws PersistencyException {
        PersistentHome home = getHomeCache().getHome(V_doc_passivo_obbligazioneBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "cd_cds_origine", sql.EQUALS, docPassivo.getCd_cds_origine());
        sql.addClause("AND", "cd_uo_origine", sql.EQUALS, docPassivo.getCd_uo_origine());
        sql.addClause("AND", "cd_cds_obbligazione", sql.EQUALS, docPassivo.getCd_cds());
        sql.addClause("AND", "esercizio_obbligazione", sql.EQUALS, docPassivo.getEsercizio_obbligazione());
        sql.addClause("AND", "esercizio_ori_obbligazione", sql.EQUALS, docPassivo.getEsercizio_ori_obbligazione());
        sql.addClause("AND", "pg_obbligazione", sql.EQUALS, docPassivo.getPg_obbligazione());
        sql.addClause("AND", "pg_obbligazione_scadenzario", sql.EQUALS, docPassivo.getPg_obbligazione_scadenzario());
        // rp 29/09/2014 nel caso di documenti generici passivi ente  'NON liquidabili" venivano recuperati come collegati essendo su impegni a consumo
        sql.addClause(FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.NOT_EQUALS, Documento_genericoBulk.GENERICO_S);
        sql.addClause(FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.NOT_EQUALS, Numerazione_doc_ammBulk.TIPO_ORDINE);
        sql.addClause(FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.NOT_EQUALS, TipoDocumentoEnum.GEN_STIPENDI_SPESA.getValue());
        sql.addSQLClause(FindClause.AND, "fl_selezione", SQLBuilder.EQUALS, "N");
        return home.fetchAll(sql);

    }

    /**
     * Metodo per cercare la riga del mandato.
     *
     * @param mandato <code>MandatoBulk</code> il mandato
     * @return result la riga del mandato
     */
    public Collection findMandato_riga(it.cnr.jada.UserContext userContext, MandatoBulk mandato) throws PersistencyException, IntrospectionException {
        PersistentHome home = getHomeCache().getHome(Mandato_rigaIBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "esercizio", sql.EQUALS, mandato.getEsercizio());
        sql.addClause("AND", "cd_cds", sql.EQUALS, mandato.getCd_cds());
        sql.addClause("AND", "pg_mandato", sql.EQUALS, mandato.getPg_mandato());
        Collection result = home.fetchAll(sql);
        getHomeCache().fetchAll(userContext);
        return result;

    }


    /**
     * Metodo per cercare la righe siope del mandato.
     *
     * @param mandato <code>MandatoBulk</code> il mandato
     * @return result la riga siope del mandato
     */
    public Collection<Mandato_siopeBulk> findMandato_siope(it.cnr.jada.UserContext userContext, MandatoBulk mandato) throws PersistencyException, IntrospectionException {
        PersistentHome home = getHomeCache().getHome(Mandato_siopeIBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "esercizio", sql.EQUALS, mandato.getEsercizio());
        sql.addClause("AND", "cd_cds", sql.EQUALS, mandato.getCd_cds());
        sql.addClause("AND", "pg_mandato", sql.EQUALS, mandato.getPg_mandato());
        Collection result = home.fetchAll(sql);
        getHomeCache().fetchAll(userContext);
        return result;
    }

    /**
     * Metodo per cercare il Mandato_terzoBulk.
     *
     * @param mandato <code>MandatoBulk</code> il mandato
     * @return istanza di <code>Mandato_terzoBulk</code>
     */
    public Mandato_terzoBulk findMandato_terzo(it.cnr.jada.UserContext userContext, MandatoBulk mandato) throws PersistencyException, IntrospectionException {
        PersistentHome home = getHomeCache().getHome(Mandato_terzoIBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "esercizio", sql.EQUALS, mandato.getEsercizio());
        sql.addClause("AND", "cd_cds", sql.EQUALS, mandato.getCd_cds());
        sql.addClause("AND", "pg_mandato", sql.EQUALS, mandato.getPg_mandato());
        Collection result = home.fetchAll(sql);
        getHomeCache().fetchAll(userContext);
        return (Mandato_terzoBulk) result.iterator().next();

    }

    /**
     * Metodo per cercare le scadenze dell'accertamento selezionato per la regolarizzazione
     *
     * @param userContext <code>CNRUserContext</code>
     * @param mandato <code>MandatoIBulk</code> il mandato
     * @return <code>Collection</code> le scadenze dell'accertamento
     */
    public Collection findScadenzeAccertamentoPerRegolarizzazione(it.cnr.contab.utenze00.bp.CNRUserContext userContext, MandatoIBulk mandato) throws IntrospectionException, PersistencyException, java.sql.SQLException {
        PersistentHome home = getHomeCache().getHome(Accertamento_scadenzarioBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addNotNullableSQLClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, mandato.getAccertamentoPerRegolarizzazione().getCd_cds());
        sql.addNotNullableSQLClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, mandato.getAccertamentoPerRegolarizzazione().getEsercizio());
        sql.addNotNullableSQLClause(FindClause.AND, "esercizio_originale", SQLBuilder.EQUALS, mandato.getAccertamentoPerRegolarizzazione().getEsercizio_originale());
        sql.addNotNullableSQLClause(FindClause.AND, "pg_accertamento", SQLBuilder.EQUALS, mandato.getAccertamentoPerRegolarizzazione().getPg_accertamento());

        sql.openParenthesis(FindClause.AND);
        sql.addSQLClause(FindClause.OR, "NVL(IM_SCADENZA,0)-NVL(IM_ASSOCIATO_DOC_CONTABILE,0)>0");
        sql.addSQLClause(FindClause.OR, "NVL(IM_SCADENZA,0)-NVL(IM_ASSOCIATO_DOC_AMM,0)>0");
        sql.closeParenthesis();

        return home.fetchAll(sql);
    }
}
