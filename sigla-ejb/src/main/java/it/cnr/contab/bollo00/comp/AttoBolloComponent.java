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

package it.cnr.contab.bollo00.comp;

import java.util.Optional;

import it.cnr.contab.bollo00.bp.ConsAttoBolloBP;
import it.cnr.contab.bollo00.bulk.Atto_bolloBulk;
import it.cnr.contab.bollo00.bulk.Atto_bolloHome;
import it.cnr.contab.bollo00.bulk.V_cons_atto_bolloBulk;
import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsHome;
import it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;

public class AttoBolloComponent extends CRUDComponent {
	private static final long serialVersionUID = 1L;

	@Override
	public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext, OggettoBulk oggettoBulk) throws ComponentException {
		try {
			if (oggettoBulk instanceof Atto_bolloBulk) {
				((Atto_bolloBulk)oggettoBulk).setEsercizio(CNRUserContext.getEsercizio(userContext));
				Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).
						findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));
				if (!uo.isUoEnte())
					((Atto_bolloBulk)oggettoBulk).setUnitaOrganizzativa(uo);
			}
			return super.inizializzaBulkPerInserimento(userContext, oggettoBulk);
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}
	
	@Override
	public OggettoBulk inizializzaBulkPerRicerca(UserContext userContext, OggettoBulk oggettoBulk) throws ComponentException {
		try {
			if (oggettoBulk instanceof Atto_bolloBulk) {
				((Atto_bolloBulk)oggettoBulk).setEsercizio(CNRUserContext.getEsercizio(userContext));
				Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).
						findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));
				if (!uo.isUoEnte())
					((Atto_bolloBulk)oggettoBulk).setUnitaOrganizzativa(uo);
			}
			return super.inizializzaBulkPerRicerca(userContext, oggettoBulk);
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}

	@Override
	public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext userContext, OggettoBulk oggettoBulk) throws ComponentException {
		try {
			if (oggettoBulk instanceof Atto_bolloBulk) {
				((Atto_bolloBulk)oggettoBulk).setEsercizio(CNRUserContext.getEsercizio(userContext));
				Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).
						findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));
				if (!uo.isUoEnte())
					((Atto_bolloBulk)oggettoBulk).setUnitaOrganizzativa(uo);
			}
			return super.inizializzaBulkPerRicercaLibera(userContext, oggettoBulk);
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}
	
	@Override
	protected Query select(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk) getHome(usercontext,Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext)));

		Atto_bolloHome home = Optional.ofNullable(getHome(usercontext, Atto_bolloBulk.class, "V_ATTO_BOLLO"))
                .filter(Atto_bolloHome.class::isInstance)
                .map(Atto_bolloHome.class::cast)
                .orElseThrow(() -> new ComponentException("Cannot find Atto_bolloHome"));

        SQLBuilder sqlBuilder = home.createSQLBuilder();
        if (!uoScrivania.isUoEnte())			
        	sqlBuilder.addClause(FindClause.AND, "unitaOrganizzativa.cd_unita_organizzativa", SQLBuilder.EQUALS, uoScrivania.getCd_unita_organizzativa());        
        sqlBuilder.setAutoJoins(true);
        sqlBuilder.generateJoin("tipoAttoBollo", "TIPO_ATTO_BOLLO");
        sqlBuilder.addClause(CompoundFindClause.and(
        				Optional.ofNullable(compoundfindclause).orElse(null),
						Optional.ofNullable(oggettobulk)
						.map(obj->obj.buildFindClauses(Optional.ofNullable(compoundfindclause).map(cfc1->Boolean.FALSE).orElse(null)))
						.orElse(null)));
		return sqlBuilder;
	}

	public RemoteIterator findConsultazioneDettaglio(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause, boolean totGenerale) throws it.cnr.jada.comp.ComponentException {
		try{
			Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk) getHome(userContext,Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));
			
			String innerPathDestinazione = totGenerale?ConsAttoBolloBP.LIVELLO_TIP:pathDestinazione;
			String innerLivelloDestinazione = totGenerale?ConsAttoBolloBP.LIVELLO_TIP:livelloDestinazione;

			BulkHome home = getHome(userContext, V_cons_atto_bolloBulk.class, innerPathDestinazione);
			SQLBuilder sql = home.createSQLBuilder();
			SQLBuilder sqlEsterna = home.createSQLBuilder();

			String tabAlias = sql.getColumnMap().getTableName();
			if (!uoScrivania.isUoEnte()){			
				sql.addSQLClause(FindClause.AND, "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uoScrivania.getCd_unita_organizzativa());
				sqlEsterna.addSQLClause(FindClause.AND, "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uoScrivania.getCd_unita_organizzativa());
			}
			if (innerPathDestinazione.indexOf(ConsAttoBolloBP.LIVELLO_DET)>=0) {
				sql.addClause(baseClause);
				return iterator(userContext,sql,V_cons_atto_bolloBulk.class,null);
			} else {
				sql.resetColumns();
				sqlEsterna.resetColumns();
				
				addColumnBase(sql,tabAlias,true);
				addColumnBase(sqlEsterna,tabAlias,false);

				if (totGenerale || innerPathDestinazione.indexOf(ConsAttoBolloBP.LIVELLO_TIP)>=0) {
					addColumnTIP(sql,tabAlias,innerLivelloDestinazione.equals(ConsAttoBolloBP.LIVELLO_TIP),true);
					addColumnTIP(sqlEsterna,tabAlias,innerLivelloDestinazione.equals(ConsAttoBolloBP.LIVELLO_TIP),false);
				}
				if (!totGenerale && innerPathDestinazione.indexOf(ConsAttoBolloBP.LIVELLO_UO)>=0){
					addColumnUO(sql,tabAlias,innerLivelloDestinazione.equals(ConsAttoBolloBP.LIVELLO_UO),true);
					addColumnUO(sqlEsterna,tabAlias,innerLivelloDestinazione.equals(ConsAttoBolloBP.LIVELLO_UO),false);
				}

				return iterator(userContext,completaSQL(sql,sqlEsterna,tabAlias,baseClause,findClause),V_cons_atto_bolloBulk.class,null);
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
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
		if (isSommatoria) {
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("NUM_DETTAGLI),0)")), "NUM_DETTAGLI");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_TOTALE_BOLLO),0)")), "IM_TOTALE_BOLLO");
		} else {
			sql.addColumn("NVL(NUM_DETTAGLI,0)", "NUM_DETTAGLI");
			sql.addColumn("NVL(IM_TOTALE_BOLLO,0)", "IM_TOTALE_BOLLO");
		}

	}

	private void addColumnBase(SQLBuilder sql, String tabAlias, boolean isBaseSQL){ 
		tabAlias = getAlias(tabAlias);
		addColumn(sql,tabAlias.concat("ESERCIZIO"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("esercizio"),isBaseSQL);
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
	private void addColumnTIP(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
		tabAlias = getAlias(tabAlias);
		addColumn(sql,tabAlias.concat("CD_TIPO_ATTO"),true);
		addColumn(sql,tabAlias.concat("DS_TIPO_ATTO"),addDescrizione);
		addColumn(sql,tabAlias.concat("IM_BOLLO"),true);
		addColumn(sql,tabAlias.concat("TI_DETTAGLI"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_tipo_atto"),isBaseSQL);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_tipo_atto"),isBaseSQL&&addDescrizione);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("im_bollo"),isBaseSQL);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ti_dettagli"),isBaseSQL);
	}

	/**
	 * Aggiunge nell'SQLBuilder <sql> le colonne del dipartimento
	 *
	 * @param sql l'SQLBuilder da aggiornare
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
	 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
	 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
	 */
	private void addColumnUO(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
		tabAlias = getAlias(tabAlias);
		addColumn(sql,tabAlias.concat("CD_UNITA_ORGANIZZATIVA"),true);
		addColumn(sql,tabAlias.concat("DS_UNITA_ORGANIZZATIVA"),addDescrizione);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_unita_organizzativa"),isBaseSQL);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_unita_organizzativa"),isBaseSQL&&addDescrizione);
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
	
	public SQLBuilder selectTipoAttoBolloByClause(UserContext userContext, Atto_bolloBulk attoBollo, Tipo_atto_bolloBulk tipoAttoBollo, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException { 
		SQLBuilder sql = getHome(userContext,Tipo_atto_bolloBulk.class).createSQLBuilder();
		if (clauses != null) 
		  sql.addClause(clauses);

		sql.openParenthesis(FindClause.AND);
		sql.addClause(FindClause.OR, "tipoCalcolo", SQLBuilder.EQUALS, Tipo_atto_bolloBulk.TIPO_FOGLIO );		
		sql.addClause(FindClause.OR, "tipoCalcolo", SQLBuilder.EQUALS, Tipo_atto_bolloBulk.TIPO_ESEMPLARE );		
		sql.closeParenthesis();
		
		return sql; 
	}
	
	public SQLBuilder selectContrattoByClause(UserContext userContext, Atto_bolloBulk attoBollo, ContrattoBulk contratto, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		Parametri_cdsHome paramHome = (Parametri_cdsHome)getHome(userContext, Parametri_cdsBulk.class);
		Parametri_cdsBulk paramCds;
		try {
			paramCds = (Parametri_cdsBulk) paramHome.findByPrimaryKey(new Parametri_cdsBulk(CNRUserContext.getCd_cds(userContext),
					CNRUserContext.getEsercizio(userContext)));
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		
		SQLBuilder sql = getHome(userContext,ContrattoBulk.class).createSQLBuilder();
		
		if (clauses != null) 
		  sql.addClause(clauses);

		if(paramCds != null && paramCds.getFl_contratto_cessato().booleanValue()){
			sql.openParenthesis(FindClause.AND);  
			  sql.addSQLClause(FindClause.AND,"STATO",SQLBuilder.EQUALS, ContrattoBulk.STATO_DEFINITIVO);
			  sql.addSQLClause(FindClause.OR,"STATO",SQLBuilder.EQUALS, ContrattoBulk.STATO_CESSSATO);
			sql.closeParenthesis();		
		}	
		else  
		  sql.addSQLClause("AND", "STATO", SQLBuilder.EQUALS, ContrattoBulk.STATO_DEFINITIVO);

		// Se uo 999.000 in scrivania: visualizza tutti i contratti
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
		  sql.openParenthesis(FindClause.AND);
			sql.addSQLClause(FindClause.AND,"CONTRATTO.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,CNRUserContext.getCd_unita_organizzativa(userContext));
			SQLBuilder sqlAssUo = getHome(userContext,Ass_contratto_uoBulk.class).createSQLBuilder();		   
			sqlAssUo.addSQLJoin("CONTRATTO.ESERCIZIO","ASS_CONTRATTO_UO.ESERCIZIO");
			sqlAssUo.addSQLJoin("CONTRATTO.PG_CONTRATTO","ASS_CONTRATTO_UO.PG_CONTRATTO");
			sqlAssUo.addSQLClause(FindClause.AND,"ASS_CONTRATTO_UO.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,CNRUserContext.getCd_unita_organizzativa(userContext));
			sql.addSQLExistsClause(FindClause.OR,sqlAssUo);
		  sql.closeParenthesis();  		 
		}
		return sql;
	}
	
	@Override
	protected void validaCreaModificaConBulk(UserContext userContext, OggettoBulk oggettoBulk) throws ComponentException {
		try {
			if (Optional.ofNullable(oggettoBulk)
					.filter(Atto_bolloBulk.class::isInstance)
					.map(Atto_bolloBulk.class::cast)
					.filter(el->Optional.ofNullable(el.getDt_provv()).isPresent()&&Optional.ofNullable(el.getCdTipoAttoBollo()).isPresent())
					.isPresent()) {
				Atto_bolloBulk atto = (Atto_bolloBulk)oggettoBulk;
				Tipo_atto_bolloBulk tipoAtto = Utility.createTipoAttoBolloComponentSession().getTipoAttoBollo(userContext, atto.getDt_provv(), atto.getCdTipoAttoBollo());
				Optional.ofNullable(tipoAtto)
					.orElseThrow(() -> new ApplicationException("Il Tipo Atto "+atto.getCdTipoAttoBollo()+" non risulta attivo per la data di protocollo indicata."));
			}
			super.validaCreaModificaConBulk(userContext, oggettoBulk);
		} catch (Exception e) {
			throw handleException(e);
		}
	}
}
