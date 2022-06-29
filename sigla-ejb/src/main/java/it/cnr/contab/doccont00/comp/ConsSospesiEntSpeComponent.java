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

package it.cnr.contab.doccont00.comp;



import java.rmi.RemoteException;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.doccont00.bp.ConsSospesiBP;
import it.cnr.contab.doccont00.intcass.bulk.V_cons_sospesiBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;


public class ConsSospesiEntSpeComponent extends CRUDComponent {
	
	private static String TIPO_ETR = "E"; 
	private static String TIPO_SPE = "S";
	private static String LIVELLO_1 = "1"; 
	private static String LIVELLO_2 = "2"; 
	private static String LIVELLO_3 = "3"; 
	
			public RemoteIterator findConsSospesiEntrata(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws ComponentException, RemoteException, IntrospectionException {
				BulkHome home = getHome(userContext, V_cons_sospesiBulk.class, pathDestinazione);
				SQLBuilder sql = home.createSQLBuilder();
				sql.addSQLClause("AND","TI_ENTRATA_SPESA",  SQLBuilder.EQUALS,TIPO_ETR);
				if(!Utility.createParametriCnrComponentSession().getParametriCnr(userContext,CNRUserContext.getEsercizio(userContext)).getFl_tesoreria_unica().booleanValue()){
					if (livelloDestinazione.equals(ConsSospesiBP.LIV_SOSREV))
						sql.addSQLClause("AND", "LIVELLO", SQLBuilder.EQUALS, LIVELLO_1);
				}else
					if(isUtenteEnte(userContext))
						if (livelloDestinazione.equals(ConsSospesiBP.LIV_SOSREV))
							sql.addSQLClause("AND", "LIVELLO", SQLBuilder.EQUALS, LIVELLO_1);
					
				if (livelloDestinazione.equals(ConsSospesiBP.LIV_SOSREVRDETT))
						sql.addSQLClause("AND", "LIVELLO", SQLBuilder.EQUALS, LIVELLO_2);
						
						addBaseColumns(userContext, sql, pathDestinazione, livelloDestinazione);
				if(livelloDestinazione.equals(ConsSospesiBP.LIV_SOSREVRDETTREV)) {
					sql.addSQLClause("AND", "LIVELLO", SQLBuilder.EQUALS, LIVELLO_3);
					addColumnsColl(userContext, sql, pathDestinazione, livelloDestinazione);
				}
				
				return iterator(userContext, completaSQL(sql, baseClause, findClause),V_cons_sospesiBulk.class,null);
			}
			
			public RemoteIterator findConsSospesiSpesa(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws ComponentException, RemoteException, IntrospectionException {
				BulkHome home = getHome(userContext, V_cons_sospesiBulk.class, pathDestinazione);
				SQLBuilder sql = home.createSQLBuilder();
				sql.addSQLClause("AND","TI_ENTRATA_SPESA",  SQLBuilder.EQUALS,TIPO_SPE);
				if(!Utility.createParametriCnrComponentSession().getParametriCnr(userContext,CNRUserContext.getEsercizio(userContext)).getFl_tesoreria_unica().booleanValue()){
					if (livelloDestinazione.equals(ConsSospesiBP.LIV_SOSMAN))
						sql.addSQLClause("AND", "LIVELLO", SQLBuilder.EQUALS, LIVELLO_1);
				}else 
					if(isUtenteEnte(userContext))
						if (livelloDestinazione.equals(ConsSospesiBP.LIV_SOSMAN))
							sql.addSQLClause("AND", "LIVELLO", SQLBuilder.EQUALS, LIVELLO_1);

				if (livelloDestinazione.equals(ConsSospesiBP.LIV_SOSMANMDETT))
						sql.addSQLClause("AND", "LIVELLO", SQLBuilder.EQUALS, LIVELLO_2);
						
						addBaseColumns(userContext, sql, pathDestinazione, livelloDestinazione);
				if(livelloDestinazione.equals(ConsSospesiBP.LIV_SOSMANMDETTMAN)) {
					sql.addSQLClause("AND", "LIVELLO", SQLBuilder.EQUALS, LIVELLO_3);
					addColumnsColl(userContext, sql, pathDestinazione, livelloDestinazione);
				}
				return iterator(userContext, completaSQL(sql,baseClause,findClause),V_cons_sospesiBulk.class,null);
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
			 * @return
			 * @throws IntrospectionException
			 */
			
		private void addBaseColumns(UserContext userContext, SQLBuilder sql,  String pathDestinazione, String livelloDestinazione) throws it.cnr.jada.comp.ComponentException {		
			sql.resetColumns();
			sql.addColumn("ESERCIZIO");
			sql.addColumn("CD_CDS");
			sql.addColumn("CD_SOSPESO");
			sql.addColumn("CD_CDS_ORIGINE");
			sql.addColumn("DT_REGISTRAZIONE");
			sql.addColumn("DS_ANAGRAFICO");
			sql.addColumn("CAUSALE");
			sql.addColumn("DES_TI_CC_BI");
			sql.addColumn("STATO_VALIDITA");
			sql.addColumn("DS_STATO_SOSPESO");
			sql.addColumn("IM_SOSPESO");
			sql.addColumn("IM_ASSOCIATO");
			sql.addColumn("IM_DA_ASSOCIARE");
			sql.addColumn("CD_SOSPESO_PADRE");
			sql.addColumn("TI_ENTRATA_SPESA");
			sql.addColumn("LIVELLO");
			sql.addColumn("CD_AVVISO_PAGOPA");
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
			private SQLBuilder completaSQL(SQLBuilder sql, CompoundFindClause baseClause, CompoundFindClause findClause){
				sql.addClause(baseClause);
				if (findClause==null)
					return sql;
				else {
					sql.addClause(findClause);
					return sql;
				}
			}
			
			
			private void addColumnsColl(UserContext userContext, SQLBuilder sql,  String pathDestinazione, String livelloDestinazione) throws it.cnr.jada.comp.ComponentException {
				sql.resetColumns();
				sql.addColumn("ESERCIZIO");
				sql.addColumn("CD_CDS");
				sql.addColumn("CD_SOSPESO");
				sql.addColumn("STATO_VALIDITA");
				sql.addColumn("DS_STATO_SOSPESO");
				sql.addColumn("PG_MAN_REV");
				sql.addColumn("DT_REGISTRAZIONE");
				sql.addColumn("CAUSALE");
				sql.addColumn("IM_ASSOCIATO");
				sql.addColumn("CD_SOSPESO_PADRE");
				sql.addColumn("TI_ENTRATA_SPESA");
				sql.addColumn("LIVELLO");
				
		}		

}