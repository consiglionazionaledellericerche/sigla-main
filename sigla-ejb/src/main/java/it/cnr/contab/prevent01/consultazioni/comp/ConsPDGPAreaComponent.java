
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

package it.cnr.contab.prevent01.consultazioni.comp;

import java.rmi.RemoteException;
import java.util.Enumeration;

import javax.ejb.EJBException;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.prevent01.consultazioni.bp.ConsPDGPAreaBP;
import it.cnr.contab.prevent01.consultazioni.bp.ConsPDGPIstBP;
import it.cnr.contab.prevent01.consultazioni.bulk.V_cons_pdgp_areaBulk;
import it.cnr.contab.prevent01.consultazioni.bulk.V_cons_pdgp_istBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.ColumnMap;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SimpleFindClause;
import it.cnr.jada.util.RemoteIterator;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsPDGPAreaComponent extends CRUDComponent {
	
		public RemoteIterator findConsultazioneEtr(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws it.cnr.jada.comp.ComponentException {
			return findConsultazioneDettaglio(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause, null);
		}

		public RemoteIterator findConsultazioneSpe(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws it.cnr.jada.comp.ComponentException {
			return findConsultazioneDettaglio(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause, null);
		}

		private RemoteIterator findConsultazioneDettaglio(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause, String tipoCons) throws it.cnr.jada.comp.ComponentException {
			try{
			   String cd_unita_organizzativa = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
			   Unita_organizzativaBulk 	uoScrivania = (Unita_organizzativaBulk) getHome(userContext,Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cd_unita_organizzativa));
			   Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache(userContext).getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
			   
			if (pathDestinazione.indexOf("ETRCDR")>=0 || pathDestinazione.indexOf("SPECDR")>=0){ 
				BulkHome home = getHome(userContext, V_cons_pdgp_areaBulk.class, pathDestinazione);
				SQLBuilder sql = home.createSQLBuilder();
				SQLBuilder sqlEsterna = home.createSQLBuilder();
				String tabAlias = sql.getColumnMap().getTableName();
				   if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){			
					   sql.addSQLClause("AND","SUBSTR(".concat(getAlias(tabAlias)).concat("CD_CENTRO_RESPONSABILITA,1,3)"),SQLBuilder.EQUALS,CNRUserContext.getCd_cds(userContext));
					   sqlEsterna.addSQLClause("AND","SUBSTR(".concat(getAlias(tabAlias)).concat("CD_CENTRO_RESPONSABILITA,1,3)"),SQLBuilder.EQUALS,CNRUserContext.getCd_cds(userContext));
				   }
				   if(!uoScrivania.isUoCds()){
					   sql.addSQLClause("AND","SUBSTR(".concat(getAlias(tabAlias)).concat("CD_CENTRO_RESPONSABILITA,1,7)"),SQLBuilder.EQUALS,uoScrivania.getCd_unita_organizzativa());  
					   sqlEsterna.addSQLClause("AND","SUBSTR(".concat(getAlias(tabAlias)).concat("CD_CENTRO_RESPONSABILITA,1,7)"),SQLBuilder.EQUALS,uoScrivania.getCd_unita_organizzativa());
				   }
				addBaseColumns(userContext,sql, sqlEsterna, tabAlias, pathDestinazione, livelloDestinazione);
				return iterator(userContext,completaSQL(sql,sqlEsterna,tabAlias,baseClause,findClause),V_cons_pdgp_areaBulk.class,null);
			}
			else
			{
				BulkHome home = getHome(userContext, V_cons_pdgp_istBulk.class, pathDestinazione);
				SQLBuilder sql = home.createSQLBuilder();
				SQLBuilder sqlEsterna = home.createSQLBuilder();
				String tabAlias = sql.getColumnMap().getTableName();
				   if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){			
					   sql.addSQLClause("AND","SUBSTR(".concat(getAlias(tabAlias)).concat("CD_CENTRO_RESPONSABILITA,1,3)"),SQLBuilder.EQUALS,CNRUserContext.getCd_cds(userContext));
					   sqlEsterna.addSQLClause("AND","SUBSTR(".concat(getAlias(tabAlias)).concat("CD_CENTRO_RESPONSABILITA,1,3)"),SQLBuilder.EQUALS,CNRUserContext.getCd_cds(userContext));
				   }
				   if(!uoScrivania.isUoCds()){
					   sql.addSQLClause("AND","SUBSTR(".concat(getAlias(tabAlias)).concat("CD_CENTRO_RESPONSABILITA,1,7)"),SQLBuilder.EQUALS,uoScrivania.getCd_unita_organizzativa());  
					   sqlEsterna.addSQLClause("AND","SUBSTR(".concat(getAlias(tabAlias)).concat("CD_CENTRO_RESPONSABILITA,1,7)"),SQLBuilder.EQUALS,uoScrivania.getCd_unita_organizzativa());
				   }
				addBaseColumns(userContext,sql, sqlEsterna, tabAlias, pathDestinazione, livelloDestinazione);
				return iterator(userContext,completaSQL(sql,sqlEsterna,tabAlias,baseClause,findClause),V_cons_pdgp_istBulk.class,null);
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
	}
		/**
		 * Costruisce l'SQLBuilder individuando i campi da ricercare sulla base del path della  consultazione 
		 * <pathDestinazione> indicata. 
		 *
		 * @param sql la select principale contenente le Sum e i GroupBy
		 * @param sqlEsterna la select esterna necessaria per interrogare la select principale come view
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param pathDestinazione il path completo della mappa di consultazione che ha effettuato la richiesta 
		 * @param livelloDestinazione il livello della mappa di consultazione che ha effettuato la richiesta 
		 */
	private void addBaseColumns(UserContext userContext,SQLBuilder sql, SQLBuilder sqlEsterna, String tabAlias, String pathDestinazione, String livelloDestinazione) throws it.cnr.jada.comp.ComponentException {
		sql.resetColumns();
		sqlEsterna.resetColumns();
		if (pathDestinazione.indexOf("ETRCDR")>=0 || pathDestinazione.indexOf("SPECDR")>=0){ 
			
			if (pathDestinazione.indexOf(ConsPDGPAreaBP.LIVELLO_CDR)>=0) {
				addColumnCDR(sql,tabAlias,(livelloDestinazione.equals(ConsPDGPAreaBP.LIVELLO_ETRCDR)||livelloDestinazione.equals(ConsPDGPAreaBP.LIVELLO_SPECDR)),true,pathDestinazione);
				addColumnCDR(sqlEsterna,tabAlias,(livelloDestinazione.equals(ConsPDGPAreaBP.LIVELLO_ETRCDR)||livelloDestinazione.equals(ConsPDGPAreaBP.LIVELLO_SPECDR)),false,pathDestinazione);
			}
			if (pathDestinazione.indexOf(ConsPDGPAreaBP.LIVELLO_AREA)>=0){
				addColumnAREA(sql,tabAlias,livelloDestinazione.equals(ConsPDGPAreaBP.LIVELLO_AREA),true,pathDestinazione);
				addColumnAREA(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGPAreaBP.LIVELLO_AREA),false,pathDestinazione);
			}
			if (pathDestinazione.indexOf(ConsPDGPAreaBP.LIVELLO_MOD)>=0){
				addColumnMOD(sql,tabAlias,livelloDestinazione.equals(ConsPDGPAreaBP.LIVELLO_MOD),true);
				addColumnMOD(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGPAreaBP.LIVELLO_MOD),false);
			}
			if (pathDestinazione.indexOf(ConsPDGPAreaBP.LIVELLO_LIV1)>=0){
				addColumnLIV1(sql,tabAlias,livelloDestinazione.equals(ConsPDGPAreaBP.LIVELLO_LIV1),true);
				addColumnLIV1(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGPAreaBP.LIVELLO_LIV1),false);
			}
			if (pathDestinazione.indexOf(ConsPDGPAreaBP.LIVELLO_LIV2)>=0){
				addColumnLIV2(sql,tabAlias,livelloDestinazione.equals(ConsPDGPAreaBP.LIVELLO_LIV2),true);
				addColumnLIV2(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGPAreaBP.LIVELLO_LIV2),false);
			}
			if (pathDestinazione.indexOf(ConsPDGPAreaBP.LIVELLO_LIV3)>=0){
				addColumnLIV3(sql,tabAlias,livelloDestinazione.equals(ConsPDGPAreaBP.LIVELLO_LIV3),true);
				addColumnLIV3(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGPAreaBP.LIVELLO_LIV3),false);
			}
			if (pathDestinazione.indexOf(ConsPDGPAreaBP.LIVELLO_DET)>=0){
				addColumnDET(sql,tabAlias,livelloDestinazione.equals(ConsPDGPAreaBP.LIVELLO_DET),true);
				addColumnDET(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGPAreaBP.LIVELLO_DET),false);
			}
		}
		else
		{
			if (pathDestinazione.indexOf(ConsPDGPIstBP.LIVELLO_AREA)>=0) {
				addColumnAREA(sql,tabAlias,(livelloDestinazione.equals(ConsPDGPIstBP.LIVELLO_ETRAREA)||livelloDestinazione.equals(ConsPDGPIstBP.LIVELLO_SPEAREA)),true,pathDestinazione);
				addColumnAREA(sqlEsterna,tabAlias,(livelloDestinazione.equals(ConsPDGPIstBP.LIVELLO_ETRAREA)||livelloDestinazione.equals(ConsPDGPIstBP.LIVELLO_SPEAREA)),false,pathDestinazione);
				}												
			if (pathDestinazione.indexOf(ConsPDGPIstBP.LIVELLO_CDR)>=0){
				addColumnCDR(sql,tabAlias,livelloDestinazione.equals(ConsPDGPIstBP.LIVELLO_CDR),true,pathDestinazione);
				addColumnCDR(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGPIstBP.LIVELLO_CDR),false,pathDestinazione);
			}
			if (pathDestinazione.indexOf(ConsPDGPIstBP.LIVELLO_MOD)>=0){
				addColumnMOD(sql,tabAlias,livelloDestinazione.equals(ConsPDGPIstBP.LIVELLO_MOD),true);
				addColumnMOD(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGPIstBP.LIVELLO_MOD),false);
			}
			if (pathDestinazione.indexOf(ConsPDGPIstBP.LIVELLO_LIV1)>=0){
				addColumnLIV1(sql,tabAlias,livelloDestinazione.equals(ConsPDGPIstBP.LIVELLO_LIV1),true);
				addColumnLIV1(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGPIstBP.LIVELLO_LIV1),false);
			}
			if (pathDestinazione.indexOf(ConsPDGPIstBP.LIVELLO_LIV2)>=0){
				addColumnLIV2(sql,tabAlias,livelloDestinazione.equals(ConsPDGPIstBP.LIVELLO_LIV2),true);
				addColumnLIV2(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGPIstBP.LIVELLO_LIV2),false);
			}
			if (pathDestinazione.indexOf(ConsPDGPIstBP.LIVELLO_LIV3)>=0){
				addColumnLIV3(sql,tabAlias,livelloDestinazione.equals(ConsPDGPIstBP.LIVELLO_LIV3),true);
				addColumnLIV3(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGPIstBP.LIVELLO_LIV3),false);
			}
			if (pathDestinazione.indexOf(ConsPDGPIstBP.LIVELLO_DET)>=0){
				addColumnDET(sql,tabAlias,livelloDestinazione.equals(ConsPDGPIstBP.LIVELLO_DET),true);
				addColumnDET(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGPIstBP.LIVELLO_DET),false);
			}
		}	
			if(!isUtenteEnte(userContext)){ 
			CdrBulk cdrUtente = cdrFromUserContext(userContext);
				
				sql.addTableToHeader("V_CDR_VALIDO_LIV1");
				sql.addSQLJoin(tabAlias.concat(".ESERCIZIO"),"V_CDR_VALIDO_LIV1.ESERCIZIO");
				sql.addSQLJoin(tabAlias.concat(".CD_CENTRO_RESPONSABILITA"),"V_CDR_VALIDO_LIV1.CD_CENTRO_RESPONSABILITA");
				sql.addSQLClause("AND", "V_CDR_VALIDO_LIV1.CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdrUtente.getCd_centro_responsabilita());
				sql.addSQLClause("AND", "V_CDR_VALIDO_LIV1.ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
				
			}
	}			

		/**
		 * Individua e completa l'SQLBuilder da utilizzare:
		 * 1) è stata effettuata una ricerca mirata (<findClause> != null)
		 * 	  la select finale è costruita come interrogazione di una view costruita sulla select principale <baseClause>
		 * 2) non è stata fatta una ricerca mirata
		 * 	  la select finale è uguale alla select principale
		 *
		 * @param sql la select principale contenente le Sum e i GroupBy
		 * @param sqlEsterna la select esterna necessaria per interrogare la select principale come view
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param baseClause la condizione principale non proveniente dall'utente
		 * @param findClause la condizione secondaria proveniente dall'utente tramite la mappa di ricerca guidata
		 * @return SQLBuilder la query da effettuare
		 */
		private SQLBuilder completaSQL(SQLBuilder sql, SQLBuilder sqlEsterna, String tabAlias, CompoundFindClause baseClause, CompoundFindClause findClause){ 
			sql.addClause(baseClause);		
			if (findClause == null) 
			{
				addColumnIMPORTI(sql,tabAlias,true);
				return sql;
			}
			else
			{
				addColumnIMPORTI(sql,tabAlias,true);		
				addColumnIMPORTI(sqlEsterna,tabAlias,false);		
				sqlEsterna.setFromClause(new StringBuffer("(".concat(sql.toString().concat(") ".concat(tabAlias)))));
				sqlEsterna.addClause(findClause);
				return sqlEsterna;
			}
		}

		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne importo.
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param isSommatoria se TRUE aggiunge la SUM delle colonne
		 */
		private void addColumnIMPORTI(SQLBuilder sql, String tabAlias, boolean isSommatoria) { 
			tabAlias = getAlias(tabAlias);
		if (tabAlias.indexOf("V_CONS_PDG_ETR_X_AREA_DET")>=0){
			if (isSommatoria) 
			{
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_ENTRATA),0)")), "IM_ENTRATA");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_ENTRATA_A2),0)")), "IM_ENTRATA_A2");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_ENTRATA_A3),0)")), "IM_ENTRATA_A3");
			}
			else
			{
				sql.addColumn("NVL(IM_ENTRATA,0)", "IM_ENTRATA");
				sql.addColumn("NVL(IM_ENTRATA_A2,0)", "IM_ENTRATA_A2");
				sql.addColumn("NVL(IM_ENTRATA_A3,0)", "IM_ENTRATA_A3");
			}
		}else
		{	
				if (isSommatoria) 
				{
					sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_DEC_FONTE_INT),0)")), "IM_DEC_FONTE_INT");
					sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_DEC_FONTE_EST),0)")), "IM_DEC_FONTE_EST");
					sql.addColumn("NVL(SUM(nvl(".concat(tabAlias.concat("IM_DEC_FONTE_INT,0)),0)+NVL(SUM(nvl(".concat(tabAlias.concat("IM_DEC_FONTE_EST,0)),0)")))), "IM_TOT_DECENTRATO");
					sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_SPESE_A2),0)")), "IM_SPESE_A2");
					sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_SPESE_A3),0)")), "IM_SPESE_A3");
				}
				else
				{
					sql.addColumn("NVL(IM_DEC_FONTE_INT,0)", "IM_DEC_FONTE_INT");
					sql.addColumn("NVL(IM_DEC_FONTE_EST,0)", "IM_DEC_FONTE_EST");
					sql.addColumn("NVL(IM_TOT_DECENTRATO,0)", "IM_TOT_DECENTRATO");
					sql.addColumn("NVL(IM_SPESE_A2,0)", "IM_SPESE_A2");
					sql.addColumn("NVL(IM_SPESE_A3,0)", "IM_SPESE_A3");
				}
		}
	}

		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne del Cdr.
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		private void addColumnCDR(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL,String pathDestinazione ){ 
			tabAlias = getAlias(tabAlias);
			if (pathDestinazione.indexOf("ETRCDR")>=0 || pathDestinazione.indexOf("SPECDR")>=0){
				addColumn(sql,tabAlias.concat("ESERCIZIO"),true);
				addColumn(sql,tabAlias.concat("CD_CENTRO_RESPONSABILITA"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("esercizio"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_centro_responsabilita"),isBaseSQL);

				addColumn(sql,tabAlias.concat("DS_CDR"),addDescrizione);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_cdr"),isBaseSQL&&addDescrizione);
			}
			else
			{
				addColumn(sql,tabAlias.concat("CD_CENTRO_RESPONSABILITA"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_centro_responsabilita"),isBaseSQL);
				addColumn(sql,tabAlias.concat("DS_CDR"),addDescrizione);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_cdr"),isBaseSQL&&addDescrizione);					
			}
		}
		
		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne del Area.
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		private void addColumnAREA(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL,String pathDestinazione ){ 
			tabAlias = getAlias(tabAlias);
			if (pathDestinazione.indexOf("ETRCDR")>=0 || pathDestinazione.indexOf("SPECDR")>=0){
				addColumn(sql,tabAlias.concat("CD_CDS_AREA"),true);
				addColumn(sql,tabAlias.concat("DS_UNITA_ORGANIZZATIVA"),addDescrizione);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_cds_area"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_unita_organizzativa"),isBaseSQL&&addDescrizione);
			}
			else
			{
				addColumn(sql,tabAlias.concat("ESERCIZIO"),true);
				addColumn(sql,tabAlias.concat("CD_CDS_AREA"),true);
				addColumn(sql,tabAlias.concat("DS_UNITA_ORGANIZZATIVA"),addDescrizione);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_cds_area"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_unita_organizzativa"),isBaseSQL&&addDescrizione);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("esercizio"),isBaseSQL);	
			}
			
		}
		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne Modulo
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		private void addColumnMOD(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			addColumn(sql,tabAlias.concat("CD_MODULO"),true);
			addColumn(sql,tabAlias.concat("DS_MODULO"),addDescrizione);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_modulo"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_modulo"),isBaseSQL&&addDescrizione);
		}
		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne del primo livello della classificazione.
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		private void addColumnLIV1(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			addColumn(sql,tabAlias.concat("CD_LIVELLO1"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_livello1"),isBaseSQL);
			if (isBaseSQL && addDescrizione) {
				sql.addTableToHeader("CLASSIFICAZIONE_VOCI");
				addColumn(sql,"CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE",true);
				addSQLGroupBy(sql,"classificazione_voci.ds_classificazione",true);

				sql.addSQLJoin(tabAlias.concat("ESERCIZIO"),"CLASSIFICAZIONE_VOCI.ESERCIZIO");
				if (sql.getColumnMap().getTableName().equals("V_CONS_PDG_ETR_X_AREA_DET"))
					sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_ENTRATE);
				else
					sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_SPESE);
			
				sql.addSQLJoin(tabAlias.concat("CD_LIVELLO1"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO1");
				sql.addSQLClause("AND", "CLASSIFICAZIONE_VOCI.CD_LIVELLO2",sql.ISNULL,null);
			}
			else
				addColumn(sql,"DS_CLASSIFICAZIONE",addDescrizione);
		}
		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne del secondo livello della classificazione.
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		private void addColumnLIV2(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			addColumn(sql,tabAlias.concat("CD_LIVELLO2"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_livello2"),isBaseSQL);
			if (isBaseSQL && addDescrizione) {
				sql.addTableToHeader("CLASSIFICAZIONE_VOCI");
				addColumn(sql,"CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE",true);
				sql.addSQLGroupBy("classificazione_voci.ds_classificazione");
				sql.addSQLJoin(tabAlias.concat("ESERCIZIO"),"CLASSIFICAZIONE_VOCI.ESERCIZIO");
				if (sql.getColumnMap().getTableName().equals("V_CONS_PDG_ETR_X_AREA_DET"))
					sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_ENTRATE);
				else
					sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_SPESE);
			
				sql.addSQLJoin(tabAlias.concat("CD_LIVELLO1"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO1");
				sql.addSQLJoin(tabAlias.concat("CD_LIVELLO2"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO2");
				sql.addSQLClause("AND", "CLASSIFICAZIONE_VOCI.CD_LIVELLO3",sql.ISNULL,null);
			}
			else
				addColumn(sql,"DS_CLASSIFICAZIONE",addDescrizione);
		}
		private void addColumnLIV3(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
				tabAlias = getAlias(tabAlias);
				addColumn(sql,tabAlias.concat("CD_LIVELLO3"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_livello3"),isBaseSQL);
				if (isBaseSQL && addDescrizione) {
					sql.addTableToHeader("CLASSIFICAZIONE_VOCI");
					addColumn(sql,"CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE",true);
					sql.addSQLGroupBy("classificazione_voci.ds_classificazione");
					sql.addSQLJoin(tabAlias.concat("ESERCIZIO"),"CLASSIFICAZIONE_VOCI.ESERCIZIO");
					if (sql.getColumnMap().getTableName().equals("V_CONS_PDG_ETR_X_AREA_DET"))
						sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_ENTRATE);
					else
						sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_SPESE);
			
					sql.addSQLJoin(tabAlias.concat("CD_LIVELLO1"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO1");
					sql.addSQLJoin(tabAlias.concat("CD_LIVELLO2"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO2");
					sql.addSQLJoin(tabAlias.concat("CD_LIVELLO3"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO3");
					sql.addSQLClause("AND", "CLASSIFICAZIONE_VOCI.CD_LIVELLO4",sql.ISNULL,null);
				}
				else
					addColumn(sql,"DS_CLASSIFICAZIONE",addDescrizione);
			}
		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne del dettaglio ultimo della Consultazione.
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		private void addColumnDET(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			if (tabAlias.indexOf("V_CONS_PDG_ETR_X_AREA_DET")>=0){
				addColumn(sql,tabAlias.concat("CD_TERZO_FINANZIATORE"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_terzo_finanziatore"),isBaseSQL);
				addColumn(sql,tabAlias.concat("CD_PROGETTO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_progetto"),isBaseSQL);
				addColumn(sql,tabAlias.concat("CD_MODULO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_modulo"),isBaseSQL);
				addColumn(sql,tabAlias.concat("DS_MODULO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_modulo"),isBaseSQL);
				addColumn(sql,tabAlias.concat("DS_DETTAGLIO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_dettaglio"),isBaseSQL);
				addColumn(sql,tabAlias.concat("TOT_FINANZIAMENTO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("tot_finanziamento"),isBaseSQL);
				addColumn(sql,tabAlias.concat("CD_COMMESSA"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_commessa"),isBaseSQL);
				addColumn(sql,tabAlias.concat("CD_CLASSIFICAZIONE"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_classificazione"),isBaseSQL);
				addColumn(sql,tabAlias.concat("DS_CLASSIFICAZIONE"),addDescrizione);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_classificazione"),isBaseSQL&&addDescrizione);
			}	else
			{
				addColumn(sql,tabAlias.concat("CD_PROGETTO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_progetto"),isBaseSQL);
				addColumn(sql,tabAlias.concat("CD_MODULO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_modulo"),isBaseSQL);
				addColumn(sql,tabAlias.concat("DS_MODULO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_modulo"),isBaseSQL);
				addColumn(sql,tabAlias.concat("CD_COMMESSA"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_commessa"),isBaseSQL);
				addColumn(sql,tabAlias.concat("CD_CLASSIFICAZIONE"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_classificazione"),isBaseSQL);
				addColumn(sql,tabAlias.concat("DS_CLASSIFICAZIONE"),addDescrizione);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_classificazione"),isBaseSQL&&addDescrizione);	
			}
		}
		/**
		 * Ritorna l'alias della tabella da aggiungere alle colonne di una select.
		 * Viene verificato il parametro <alias> indicato
		 * 1) se null ritorna null;
		 * 2) se pieno concatena "."
		 *
		 * @param alias l'alias da aggiornare per aggiungerlo alle colonne di una select.
		 * @return l'alias da utilizzare nella select.
		 */
		private String getAlias(String alias){ 
			if (alias == null) return new String("");
			if (alias.equals(new String(""))) return new String("");
			return new String(alias.concat("."));
		}
		/**
		 * Aggiunge nel GroupBy dell'SQLBuilder <sql> indicato il valore <valore>
		 *
		 * @param sql l'SQLBuilder da aggiornare.
		 * @param valore il valore da aggiungere al GroupBy.
		 * @param addGroupBy se TRUE aggiunge il valore. Se FALSE non effettua alcuna istruzione.
		 */
		private void addSQLGroupBy(SQLBuilder sql, String valore, boolean addGroupBy){
			if (addGroupBy)
				sql.addSQLGroupBy(valore);
		}
		/**
		 * Aggiunge nella lista delle colonne da interrogare dell'SQLBuilder <sql> indicato il valore <valore>
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param valore il valore da aggiungere alla lista delle colonne da interrogare.
		 * @param addColumn se TRUE aggiunge il valore. Se FALSE non effettua alcuna istruzione.
		 */
		private void addColumn(SQLBuilder sql, String valore, boolean addColumn){
			if (addColumn)
				sql.addColumn(valore);
		}
	
	public CdrBulk cdrFromUserContext(UserContext userContext) throws ComponentException {

		try {
			it.cnr.contab.utenze00.bulk.UtenteBulk user = new it.cnr.contab.utenze00.bulk.UtenteBulk( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser() );
			user = (it.cnr.contab.utenze00.bulk.UtenteBulk)getHome(userContext, user).findByPrimaryKey(user);

			CdrBulk cdr = new CdrBulk( it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext) );

			return (CdrBulk)getHome(userContext, cdr).findByPrimaryKey(cdr);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw new ComponentException(e);
		}
	}
	private boolean isCdrEnte(UserContext userContext,CdrBulk cdr) throws ComponentException {
			try {
				getHome(userContext,cdr.getUnita_padre()).findByPrimaryKey(cdr.getUnita_padre());
				return cdr.isCdrAC();
			} catch(Throwable e) {
				throw handleException(e);
			}
		}
	private boolean isUtenteEnte(UserContext userContext) throws ComponentException {
			return isCdrEnte(userContext,cdrFromUserContext(userContext));
	}	
}
