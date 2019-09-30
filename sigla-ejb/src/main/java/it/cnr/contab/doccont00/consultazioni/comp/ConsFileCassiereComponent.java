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

import it.cnr.contab.doccont00.consultazioni.bp.ConsFileCassiereBP;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_giornaliera_cassaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;


public class ConsFileCassiereComponent extends CRUDComponent {
	
	
	public RemoteIterator findConsultazione(UserContext userContext, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws it.cnr.jada.comp.ComponentException {
		if(livelloDestinazione.compareTo(ConsFileCassiereBP.FILENAME)==0){
			BulkHome home = getHome(userContext, V_cons_giornaliera_cassaBulk.class,"FILENAME");
			SQLBuilder sql = home.createSQLBuilder();
			sql.setDistinctClause(true);
			String tabAlias = sql.getColumnMap().getTableName();
			sql.addOrderBy(getAlias(tabAlias).concat("NOME_FILE DESC"));
			return iterator(userContext,completaSQL(sql,tabAlias,baseClause,findClause),V_cons_giornaliera_cassaBulk.class,"FILENAME");
		}
		else{
			BulkHome home = getHome(userContext, V_cons_giornaliera_cassaBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			String tabAlias = sql.getColumnMap().getTableName();
			addBaseColumns(userContext, sql, tabAlias, livelloDestinazione);
			return iterator(userContext,completaSQL(sql,tabAlias,baseClause,findClause),V_cons_giornaliera_cassaBulk.class,null);
		}
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
	private void addBaseColumns(UserContext userContext,SQLBuilder sql, String tabAlias, String livelloDestinazione) throws it.cnr.jada.comp.ComponentException {
		sql.resetColumns();
		addColumnBASE(sql, tabAlias, livelloDestinazione);
		if(livelloDestinazione.compareTo(ConsFileCassiereBP.BASE)==0){
			addColumnIMPORTI(sql, tabAlias);
		}
		else{
			addColumnBOTT(sql,tabAlias,livelloDestinazione);
		}
		addColumnRestanti(sql,tabAlias,livelloDestinazione);
	}			

	private void addColumnRestanti(SQLBuilder sql, String tabAlias,String livelloDestinazione) {
		addColumn(sql,"NULL PG_REC",true);
		addColumn(sql,"NULL TR",true);
		if(livelloDestinazione.compareTo(ConsFileCassiereBP.BASE)==0){
			addColumn(sql,"NULL PG_REVERSALE",true);
			addColumn(sql,"NULL PG_MANDATO",true);
			addColumn(sql,"NULL CD_SOSPESO_E",true);
			addColumn(sql,"NULL CD_SOSPESO_S",true);
			//addColumn(sql,"NULL DATA_MOVIMENTO",true);
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
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param baseClause la condizione principale non proveniente dall'utente
	 * @param findClause la condizione secondaria proveniente dall'utente tramite la mappa di ricerca guidata
	 * @return SQLBuilder la query da effettuare
	 */
	private SQLBuilder completaSQL(SQLBuilder sql, String tabAlias, CompoundFindClause baseClause, CompoundFindClause findClause){ 
		sql.addClause(baseClause);		
		//addColumnIMPORTI(sql,tabAlias);
		sql.addClause(findClause);
		return sql;
	}

	/**
	 * Aggiunge nell'SQLBuilder <sql> le colonne importo.
	 *
	 * @param sql l'SQLBuilder da aggiornare
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 */
	private void addColumnIMPORTI(SQLBuilder sql, String tabAlias) { 
		tabAlias = getAlias(tabAlias);
		sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_SOS_E_APERTI),0)")), "IM_SOS_E_APERTI");
		sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_SOS_E_STORNI),0)")), "IM_SOS_E_STORNI");
		sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_REV_SOSPESI),0)")), "IM_REV_SOSPESI");
		sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_REVERSALI),0)")), "IM_REVERSALI");
		sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_REV_STORNI),0)")), "IM_REV_STORNI");
		
		sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_SOS_S_APERTI),0)")), "IM_SOS_S_APERTI");
		sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_SOS_S_STORNI),0)")), "IM_SOS_S_STORNI");
		sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_MAN_SOSPESI),0)")), "IM_MAN_SOSPESI");
		sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_MANDATI),0)")), "IM_MANDATI");
		sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_MAN_STORNI),0)")), "IM_MAN_STORNI");
		sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_SOS_E_APERTI),0)+NVL(SUM(".concat(tabAlias.concat("IM_SOS_E_STORNI),0)-NVL(SUM(".concat(tabAlias.concat("IM_REV_SOSPESI),0)+NVL(SUM(".concat(tabAlias.concat("IM_REVERSALI),0)-NVL(SUM(".concat(tabAlias.concat("IM_REV_STORNI),0)-NVL(SUM(".concat(tabAlias.concat("IM_MANDATI),0)+NVL(SUM(".concat(tabAlias.concat("IM_MAN_STORNI),0)-NVL(SUM(".concat(tabAlias.concat("IM_SOS_S_APERTI),0)-NVL(SUM(".concat(tabAlias.concat("IM_SOS_S_STORNI),0)+NVL(SUM(".concat(tabAlias.concat("IM_MAN_SOSPESI),0)")))))))))))))))))))), "TOT_SBILANCIO");
	}

	/**
	 * Aggiunge nell'SQLBuilder <sql> le colonne del Cdr.
	 *
	 * @param sql l'SQLBuilder da aggiornare
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param pathDestinazione il path completo della mappa di consultazione che ha effettuato la richiesta 
	 */
	private void addColumnBOTT(SQLBuilder sql, String tabAlias, String pathDestinazione){ 
		tabAlias = getAlias(tabAlias);
		if (pathDestinazione.indexOf(ConsFileCassiereBP.BOTT1)>=0) {
			//addColumn(sql,tabAlias.concat("DATA_MOVIMENTO"),true);
			addColumn(sql,tabAlias.concat("CD_SOSPESO_E"),true);
			addColumn(sql,tabAlias.concat("IM_SOS_E_APERTI"),true);
			
			addColumn(sql,"NULL PG_REVERSALE",true);
			addColumn(sql,"NULL PG_MANDATO",true);
			
			addColumn(sql,"NULL CD_SOSPESO_S",true);
			addColumn(sql,"NULL IM_SOS_E_STORNI",true);
			addColumn(sql,"NULL IM_REV_SOSPESI",true);
			addColumn(sql,"NULL IM_REVERSALI",true);
			addColumn(sql,"NULL IM_REV_STORNI",true);
			addColumn(sql,"NULL IM_SOS_S_APERTI",true);
			addColumn(sql,"NULL IM_SOS_S_STORNI",true);
			addColumn(sql,"NULL IM_MAN_SOSPESI",true);
			addColumn(sql,"NULL IM_MANDATI",true);
			addColumn(sql,"NULL IM_MAN_STORNI",true);
			addColumn(sql,"NULL TOT_SBILANCIO",true);
			
			//addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_centro_responsabilita"),true);
			//addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_cdr"),true);					
		}
		else if (pathDestinazione.indexOf(ConsFileCassiereBP.BOTT2)>=0){
			//addColumn(sql,tabAlias.concat("DATA_MOVIMENTO"),true);
			addColumn(sql,tabAlias.concat("CD_SOSPESO_E"),true);
			addColumn(sql,tabAlias.concat("IM_SOS_E_STORNI"),true);
			
			addColumn(sql,"NULL PG_REVERSALE",true);
			addColumn(sql,"NULL PG_MANDATO",true);
			
			addColumn(sql,"NULL CD_SOSPESO_S",true);
			addColumn(sql,"NULL IM_SOS_E_APERTI",true);
			addColumn(sql,"NULL IM_REV_SOSPESI",true);
			addColumn(sql,"NULL IM_REVERSALI",true);
			addColumn(sql,"NULL IM_REV_STORNI",true);
			addColumn(sql,"NULL IM_SOS_S_APERTI",true);
			addColumn(sql,"NULL IM_SOS_S_STORNI",true);
			addColumn(sql,"NULL IM_MAN_SOSPESI",true);
			addColumn(sql,"NULL IM_MANDATI",true);
			addColumn(sql,"NULL IM_MAN_STORNI",true);
			addColumn(sql,"NULL TOT_SBILANCIO",true);
		}
		else if (pathDestinazione.indexOf(ConsFileCassiereBP.BOTT3)>=0){
			//addColumn(sql,tabAlias.concat("DATA_MOVIMENTO"),true);
			addColumn(sql,tabAlias.concat("PG_REVERSALE"),true);
			addColumn(sql,tabAlias.concat("CD_SOSPESO_E"),true);
			addColumn(sql,tabAlias.concat("IM_REV_SOSPESI"),true);
			
			addColumn(sql,"NULL PG_MANDATO",true);
			
			addColumn(sql,"NULL CD_SOSPESO_S",true);
			addColumn(sql,"NULL IM_SOS_E_APERTI",true);
			addColumn(sql,"NULL IM_SOS_E_STORNI",true);
			addColumn(sql,"NULL IM_REVERSALI",true);
			addColumn(sql,"NULL IM_REV_STORNI",true);
			addColumn(sql,"NULL IM_SOS_S_APERTI",true);
			addColumn(sql,"NULL IM_SOS_S_STORNI",true);
			addColumn(sql,"NULL IM_MAN_SOSPESI",true);
			addColumn(sql,"NULL IM_MANDATI",true);
			addColumn(sql,"NULL IM_MAN_STORNI",true);
			addColumn(sql,"NULL TOT_SBILANCIO",true);
		}		
		else if (pathDestinazione.indexOf(ConsFileCassiereBP.BOTT4)>=0){
			//addColumn(sql,tabAlias.concat("DATA_MOVIMENTO"),true);
			addColumn(sql,tabAlias.concat("PG_REVERSALE"),true);
			addColumn(sql,tabAlias.concat("IM_REVERSALI"),true);
			
			addColumn(sql,"NULL PG_MANDATO",true);
			
			addColumn(sql,"NULL CD_SOSPESO_S",true);
			addColumn(sql,"NULL CD_SOSPESO_E",true);
			addColumn(sql,"NULL IM_SOS_E_APERTI",true);
			addColumn(sql,"NULL IM_SOS_E_STORNI",true);
			addColumn(sql,"NULL IM_REV_SOSPESI",true);
			addColumn(sql,"NULL IM_REV_STORNI",true);
			addColumn(sql,"NULL IM_SOS_S_APERTI",true);
			addColumn(sql,"NULL IM_SOS_S_STORNI",true);
			addColumn(sql,"NULL IM_MAN_SOSPESI",true);
			addColumn(sql,"NULL IM_MAN_STORNI",true);
			addColumn(sql,"NULL IM_MANDATI",true);
			addColumn(sql,"NULL TOT_SBILANCIO",true);
		}		
		else if (pathDestinazione.indexOf(ConsFileCassiereBP.BOTT5)>=0){
			//addColumn(sql,tabAlias.concat("DATA_MOVIMENTO"),true);
			addColumn(sql,tabAlias.concat("PG_REVERSALE"),true);
			addColumn(sql,tabAlias.concat("IM_REV_STORNI"),true);
			
			addColumn(sql,"NULL PG_MANDATO",true);
			
			addColumn(sql,"NULL CD_SOSPESO_S",true);
			addColumn(sql,"NULL CD_SOSPESO_E",true);
			addColumn(sql,"NULL IM_SOS_E_APERTI",true);
			addColumn(sql,"NULL IM_SOS_E_STORNI",true);
			addColumn(sql,"NULL IM_REV_SOSPESI",true);
			addColumn(sql,"NULL IM_REVERSALI",true);
			addColumn(sql,"NULL IM_SOS_S_APERTI",true);
			addColumn(sql,"NULL IM_SOS_S_STORNI",true);
			addColumn(sql,"NULL IM_MAN_SOSPESI",true);
			addColumn(sql,"NULL IM_MANDATI",true);
			addColumn(sql,"NULL IM_MAN_STORNI",true);
			addColumn(sql,"NULL TOT_SBILANCIO",true);
		}
		else if (pathDestinazione.indexOf(ConsFileCassiereBP.BOTT6)>=0) {
			//addColumn(sql,tabAlias.concat("DATA_MOVIMENTO"),true);
			addColumn(sql,tabAlias.concat("CD_SOSPESO_S"),true);
			addColumn(sql,tabAlias.concat("IM_SOS_S_APERTI"),true);
			
			addColumn(sql,"NULL PG_REVERSALE",true);
			addColumn(sql,"NULL PG_MANDATO",true);
			
			addColumn(sql,"NULL CD_SOSPESO_E",true);
			addColumn(sql,"NULL IM_SOS_E_APERTI",true);
			addColumn(sql,"NULL IM_SOS_E_STORNI",true);
			addColumn(sql,"NULL IM_REV_SOSPESI",true);
			addColumn(sql,"NULL IM_REVERSALI",true);
			addColumn(sql,"NULL IM_REV_STORNI",true);
			addColumn(sql,"NULL IM_SOS_S_STORNI",true);
			addColumn(sql,"NULL IM_MAN_SOSPESI",true);
			addColumn(sql,"NULL IM_MANDATI",true);
			addColumn(sql,"NULL IM_MAN_STORNI",true);
			addColumn(sql,"NULL TOT_SBILANCIO",true);
			//addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_centro_responsabilita"),true);
			//addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_cdr"),true);					
		}
		else if (pathDestinazione.indexOf(ConsFileCassiereBP.BOTT7)>=0){
			//addColumn(sql,tabAlias.concat("DATA_MOVIMENTO"),true);
			addColumn(sql,tabAlias.concat("CD_SOSPESO_S"),true);
			addColumn(sql,tabAlias.concat("IM_SOS_S_STORNI"),true);
			
			addColumn(sql,"NULL PG_REVERSALE",true);
			addColumn(sql,"NULL PG_MANDATO",true);
			
			addColumn(sql,"NULL CD_SOSPESO_E",true);
			addColumn(sql,"NULL IM_SOS_E_APERTI",true);
			addColumn(sql,"NULL IM_SOS_E_STORNI",true);
			addColumn(sql,"NULL IM_REV_SOSPESI",true);
			addColumn(sql,"NULL IM_REVERSALI",true);
			addColumn(sql,"NULL IM_REV_STORNI",true);
			addColumn(sql,"NULL IM_SOS_S_APERTI",true);
			addColumn(sql,"NULL IM_MAN_SOSPESI",true);
			addColumn(sql,"NULL IM_MANDATI",true);
			addColumn(sql,"NULL IM_MAN_STORNI",true);
			addColumn(sql,"NULL TOT_SBILANCIO",true);
		}
		else if (pathDestinazione.indexOf(ConsFileCassiereBP.BOTT8)>=0){
			//addColumn(sql,tabAlias.concat("DATA_MOVIMENTO"),true);
			addColumn(sql,tabAlias.concat("PG_MANDATO"),true);
			addColumn(sql,tabAlias.concat("CD_SOSPESO_S"),true);
			addColumn(sql,tabAlias.concat("IM_MAN_SOSPESI"),true);
			
			addColumn(sql,"NULL PG_REVERSALE",true);
			
			addColumn(sql,"NULL CD_SOSPESO_E",true);
			addColumn(sql,"NULL IM_SOS_E_APERTI",true);
			addColumn(sql,"NULL IM_SOS_E_STORNI",true);
			addColumn(sql,"NULL IM_REV_SOSPESI",true);
			addColumn(sql,"NULL IM_REVERSALI",true);
			addColumn(sql,"NULL IM_REV_STORNI",true);
			addColumn(sql,"NULL IM_SOS_S_APERTI",true);
			addColumn(sql,"NULL IM_SOS_S_STORNI",true);
			addColumn(sql,"NULL IM_MANDATI",true);
			addColumn(sql,"NULL IM_MAN_STORNI",true);
			addColumn(sql,"NULL TOT_SBILANCIO",true);
		}		
		else if (pathDestinazione.indexOf(ConsFileCassiereBP.BOTT9)>=0){
			//addColumn(sql,tabAlias.concat("DATA_MOVIMENTO"),true);
			addColumn(sql,tabAlias.concat("PG_MANDATO"),true);
			addColumn(sql,tabAlias.concat("IM_MANDATI"),true);
			
			addColumn(sql,"NULL PG_REVERSALE",true);
			
			addColumn(sql,"NULL CD_SOSPESO_S",true);
			addColumn(sql,"NULL CD_SOSPESO_E",true);
			addColumn(sql,"NULL IM_SOS_E_APERTI",true);
			addColumn(sql,"NULL IM_SOS_E_STORNI",true);
			addColumn(sql,"NULL IM_REV_SOSPESI",true);
			addColumn(sql,"NULL IM_REVERSALI",true);
			addColumn(sql,"NULL IM_REV_STORNI",true);
			addColumn(sql,"NULL IM_SOS_S_APERTI",true);
			addColumn(sql,"NULL IM_SOS_S_STORNI",true);
			addColumn(sql,"NULL IM_MAN_SOSPESI",true);
			addColumn(sql,"NULL IM_MAN_STORNI",true);
			addColumn(sql,"NULL TOT_SBILANCIO",true);
		}		
		else if (pathDestinazione.indexOf(ConsFileCassiereBP.BOTT10)>=0){
			//addColumn(sql,tabAlias.concat("DATA_MOVIMENTO"),true);
			addColumn(sql,tabAlias.concat("PG_MANDATO"),true);
			addColumn(sql,tabAlias.concat("IM_MAN_STORNI"),true);
			
			addColumn(sql,"NULL PG_REVERSALE",true);
			
			addColumn(sql,"NULL CD_SOSPESO_S",true);
			addColumn(sql,"NULL CD_SOSPESO_E",true);
			addColumn(sql,"NULL IM_SOS_E_APERTI",true);
			addColumn(sql,"NULL IM_SOS_E_STORNI",true);
			addColumn(sql,"NULL IM_REV_SOSPESI",true);
			addColumn(sql,"NULL IM_REVERSALI",true);
			addColumn(sql,"NULL IM_REV_STORNI",true);
			addColumn(sql,"NULL IM_SOS_S_APERTI",true);
			addColumn(sql,"NULL IM_SOS_S_STORNI",true);
			addColumn(sql,"NULL IM_MAN_SOSPESI",true);
			addColumn(sql,"NULL IM_MANDATI",true);
			addColumn(sql,"NULL TOT_SBILANCIO",true);
		}		
	}
	private void addColumnBASE(SQLBuilder sql, String tabAlias, String pathDestinazione){ 
		tabAlias = getAlias(tabAlias);
		if (pathDestinazione.indexOf(ConsFileCassiereBP.BASE)>=0) {
			addColumn(sql,tabAlias.concat("ESERCIZIO"),true);
			addColumn(sql,tabAlias.concat("NOME_FILE"),true);
			addColumn(sql,tabAlias.concat("DATA_GIORNALIERA"),true);
			addColumn(sql,tabAlias.concat("DATA_MOVIMENTO"),true);
			addColumn(sql,tabAlias.concat("CD_CDS"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("esercizio"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("nome_file"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("data_giornaliera"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("data_movimento"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_cds"),true);
		}
		else {
			addColumn(sql,"NULL ESERCIZIO",true);
			addColumn(sql,"NULL NOME_FILE",true);
			addColumn(sql,"NULL DATA_GIORNALIERA",true);
			addColumn(sql,"NULL DATA_MOVIMENTO",true);
			addColumn(sql,"NULL CD_CDS",true);
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

}

