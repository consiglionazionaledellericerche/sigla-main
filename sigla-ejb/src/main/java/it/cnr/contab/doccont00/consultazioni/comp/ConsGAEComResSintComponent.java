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

package it.cnr.contab.doccont00.consultazioni.comp;


import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.VRendicontazioneBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_gae_comp_res_sintesiBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;


public class ConsGAEComResSintComponent extends CRUDComponent {
	
	
	public RemoteIterator findConsultazione(UserContext userContext,  CompoundFindClause baseClause, CompoundFindClause findClause) throws it.cnr.jada.comp.ComponentException {
	
			BulkHome home = getHome(userContext, V_cons_gae_comp_res_sintesiBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			String tabAlias = sql.getColumnMap().getTableName();
			addBaseColumns(userContext, sql, tabAlias);
			return iterator(userContext,completaSQL(sql,tabAlias,baseClause,findClause),V_cons_gae_comp_res_sintesiBulk.class,null);
	}

	/**
	 * Costruisce l'SQLBuilder individuando i campi da ricercare sulla base del path della  consultazione 
	 * <pathDestinazione> indicata. 
	 *
	 * @param sql la select principale contenente le Sum e i GroupBy
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param pathDestinazione il path completo della mappa di consultazione che ha effettuato la richiesta 
	 * @param livelloDestinazione il livello della mappa di consultazione che ha effettuato la richiesta 
	 */
	private void addBaseColumns(UserContext userContext,SQLBuilder sql, String tabAlias) throws it.cnr.jada.comp.ComponentException {
		sql.resetColumns();
		addColumnBASE(userContext,sql, tabAlias);
	}			
	/**
	 * Individua e completa l'SQLBuilder da utilizzare:
	 * 1) è stata effettuata una ricerca mirata (<findClause> != null)
	 * 	  la select finale è costruita come interrogazione di una view costruita sulla select principale <baseClause>
	 * 2) non è stata fatta una ricerca mirata
	 * 	  la select finale è uguale alla select principale
	 *
	 * @param sql la select principale contenente le Sum e i GroupBy
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param baseClause la condizione principale non proveniente dall'utente
	 * @param findClause la condizione secondaria proveniente dall'utente tramite la mappa di ricerca guidata
	 * @return SQLBuilder la query da effettuare
	 */
	private SQLBuilder completaSQL(SQLBuilder sql, String tabAlias, CompoundFindClause baseClause, CompoundFindClause findClause){ 
		sql.addClause(baseClause); 		
		sql.addClause(findClause);
		return sql;
	}

	
	private void addColumnBASE(UserContext context,SQLBuilder sql, String tabAlias) throws ComponentException{ 
		tabAlias = getAlias(tabAlias);
		addColumn(sql,tabAlias.concat("ESERCIZIO"),true);
		addColumn(sql,tabAlias.concat("CDS"),true);
		addColumn(sql,tabAlias.concat("CDR"),true);
		addColumn(sql,tabAlias.concat("DS_CDR"),true);
		addColumn(sql,tabAlias.concat("LDA"),true);
		addColumn(sql,tabAlias.concat("DS_LDA"),true);
		addColumn(sql,tabAlias.concat("CD_RESPONSABILE_TERZO"),true);
		addColumn(sql,tabAlias.concat("DENOMINAZIONE_SEDE"),true);
		
		addColumn(sql,tabAlias.concat("CD_ELEMENTO_VOCE"),true);
		addColumn(sql,tabAlias.concat("DS_ELEMENTO_VOCE"),true);
		addColumn(sql,tabAlias.concat("CD_CDS_OBB"),true);
		addColumn(sql,tabAlias.concat("ESERCIZIO_OBBLIGAZIONE"),true);
		addColumn(sql,tabAlias.concat("ESERCIZIO_ORIGINALE"),true);
		addColumn(sql,tabAlias.concat("PG_OBBLIGAZIONE"),true);
		addColumn(sql,tabAlias.concat("PG_OBBLIGAZIONE_SCADENZARIO"),true);
		addColumn(sql,tabAlias.concat("DS_SCADENZA"),true);
		sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_IMPEGNI),0)")), "IM_IMPEGNI");
		sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_MANDATI),0)")), "IM_MANDATI");
		sql.addColumn("MAX(".concat(tabAlias.concat("CD_CDS_MAN)")), "CD_CDS_MAN");
		sql.addColumn("MAX(".concat(tabAlias.concat("PG_MANDATO)")), "PG_MANDATO");
		sql.addColumn("MAX(".concat(tabAlias.concat("DT_EMISSIONE)")), "DT_EMISSIONE");
		sql.addColumn("MAX(".concat(tabAlias.concat("DT_TRASMISSIONE)")), "DT_TRASMISSIONE");
		sql.addColumn("MAX(".concat(tabAlias.concat("DT_PAGAMENTO)")), "DT_PAGAMENTO");
		sql.addColumn("MAX(".concat(tabAlias.concat("DS_MANDATO)")), "DS_MANDATO");
		
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("esercizio"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cds"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cdr"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_cdr"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("lda"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_lda"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_responsabile_terzo"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("denominazione_sede"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_elemento_voce"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_elemento_voce"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_cds_obb"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("esercizio_obbligazione"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("esercizio_originale"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_obbligazione"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_obbligazione_scadenzario"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_scadenza"),true);
		if(!isUtenteEnte(context)){ 
			CdrBulk cdrUtente = cdrFromUserContext(context);
				if ( !cdrUtente.isCdrILiv() ){
				
			/*	sql.addTableToHeader("V_CDR_VALIDO_LIV1");
				sql.addSQLJoin(tabAlias.concat(".ESERCIZIO"),"V_CDR_VALIDO_LIV1.ESERCIZIO");
				sql.addSQLJoin(tabAlias.concat(".CD_CENTRO_RESPONSABILITA"),"V_CDR_VALIDO_LIV1.CD_CENTRO_RESPONSABILITA");
				sql.addSQLClause("AND", "V_CDR_VALIDO_LIV1.CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdrUtente.getCd_centro_responsabilita());
				sql.addSQLClause("AND", "V_CDR_VALIDO_LIV1.ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));*/
				
				sql.addSQLClause("AND", "CDR",SQLBuilder.EQUALS,CNRUserContext.getCd_cdr(context));
				sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context));
			 }
				else {
					sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context));
			 		sql.addSQLClause("AND", "CDS", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(context));
					
				}
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

	public RemoteIterator findConsultazioneRend(UserContext userContext,  CompoundFindClause baseClause, CompoundFindClause findClause) throws it.cnr.jada.comp.ComponentException {
		BulkHome home = getHome(userContext, VRendicontazioneBulk.class);
		try{
			SQLBuilder sql = home.createSQLBuilder();
			sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		    Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk) getHome(userContext,Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));		
			if(!isUtenteEnte(userContext)){ 
					if(!uoScrivania.isUoCds())
						  sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,CNRUserContext.getCd_unita_organizzativa(userContext));
					sql.addSQLClause("AND", "CD_CDS", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
			}
			sql.addClause(baseClause); 		 
			sql.addClause(findClause);
			return iterator(userContext,sql,VRendicontazioneBulk.class,null);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw new ComponentException(e);
		}
	}

}

