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

package it.cnr.contab.utenze00.bulk;

import java.sql.PreparedStatement;
import java.util.List;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.bulk.Parametri_enteHome;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.consultazioni.bulk.ConsultazioniRestHome;
import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperBulk;
import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperMagBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class UtenteHome extends BulkHome implements ConsultazioniRestHome {


	final static String TIPO_COMUNE = "U";
	final static String TIPO_AMMINISTRATORE = "A";
	final static String TIPO_SUPERUTENTE = "S";
	protected UtenteHome(Class clazz,java.sql.Connection connection) {
		super(clazz,connection);
	}

	protected UtenteHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
		super(clazz,connection,persistentCache);
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Costruisce un UtenteHome
	 *
	 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
	 */
	public UtenteHome(java.sql.Connection conn) {
		super(UtenteBulk.class,conn);
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Costruisce un UtenteHome
	 *
	 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
	 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
	 */
	public UtenteHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(UtenteBulk.class,conn,persistentCache);
	}
	public void initializePrimaryKeyForInsert(UserContext usercontext,OggettoBulk oggettobulk) throws PersistencyException, ComponentException {
		// verifichiamo che tipo di autenticazione è ora attiva sui parametri ente
		Parametri_enteHome enteHome = (Parametri_enteHome)getHomeCache().getHome(Parametri_enteBulk.class);
		SQLBuilder sqlEnte = enteHome.createSQLBuilder();
		sqlEnte.addClause("AND","attivo",sqlEnte.EQUALS,new Boolean(true));
		Parametri_enteBulk ente = (Parametri_enteBulk) getHomeCache().getHome(Parametri_enteBulk.class).fetchAll(sqlEnte).get(0);

		boolean fl = ente.getFl_autenticazione_ldap();

		((UtenteBulk)oggettobulk).setFl_autenticazione_ldap(fl);
		super.initializePrimaryKeyForInsert(usercontext, oggettobulk);
	}
	/**
	 * Recupera la lista di accessi (AccessoBulk) di tipo pubblico ed eventualmente (se il gestore ne e' autorizzato)
	 * di tipoo riservato CNR, ordinandoli per codice
	 * @param utente parametro non usato
	 * @return List lista di AccessoBulk
	 */

	public List findAccessi_disponibili(UtenteTemplateBulk utente, CompoundFindClause compoundfindclause) throws IntrospectionException, PersistencyException
	{
		PersistentHome accessoHome = getHomeCache().getHome( AccessoBulk.class);
		SQLBuilder sql = accessoHome.createSQLBuilder();
		if (compoundfindclause != null)
			sql.addClause(compoundfindclause);
		if ( !utente.getGestore().getCd_cds_configuratore().equals("*"))
			sql.addSQLClause("AND","TI_ACCESSO",sql.NOT_EQUALS,AccessoBulk.TIPO_RISERVATO_CNR);
		sql.addSQLClause("AND","TI_ACCESSO",sql.NOT_EQUALS,AccessoBulk.TIPO_SUPERUTENTE);
		sql.addSQLClause("AND","TI_ACCESSO",sql.NOT_EQUALS,AccessoBulk.TIPO_AMMIN_UTENZE);
		sql.addOrderBy( "CD_ACCESSO");
		return accessoHome.fetchAll(sql);
	}
	/**
	 * Recupera la lista di ruoli (RuoloBulk) ordinandoli per codice
	 * @param utente parametro non usato
	 * @return List lista di RuoloBulk
	 */

	public List findRuoli_disponibili(UtenteTemplateBulk utente) throws IntrospectionException, PersistencyException
	{
		PersistentHome ruoloHome = getHomeCache().getHome( RuoloBulk.class);
		SQLBuilder sql = ruoloHome.createSQLBuilder();
		if (!"*".equals( utente.getGestore().getCd_cds_configuratore() ))
		{
			sql.addClause( "AND", "cd_cds", sql.EQUALS, utente.getGestore().getCd_cds_configuratore());
			sql.addClause( "OR", "cd_cds", sql.ISNULL, null);
		}
		sql.addOrderBy( "CD_RUOLO");
		return ruoloHome.fetchAll(sql);
	}
	/**
	 * Estrae il codice di tutte le unità organizzative su cui l'utente ha accessi propri
	 *
	 * @param utente Utente in processo
	 * @return collezione di codice di UO su cui l'utente ha accessi propri
	 */

	public java.util.Collection findUO_accessi_propri(UtenteTemplateBulk utente) throws IntrospectionException, PersistencyException
	{
		try {
			SQLBuilder sql = new SQLBuilder();
			sql.setHeader("SELECT DISTINCT CD_UNITA_ORGANIZZATIVA");
			sql.addTableToHeader("UTENTE_UNITA_ACCESSO");
			sql.addSQLClause("AND","CD_UTENTE",sql.EQUALS,utente.getCd_utente());
			LoggableStatement stm = sql.prepareStatement(getConnection());
			try {
				java.sql.ResultSet rs = stm.executeQuery();
				java.util.ArrayList aAL =  new java.util.ArrayList();
				while(rs.next())
					aAL.add(rs.getString(1));
				return aAL;
			} finally {
				try{stm.close();}catch( java.sql.SQLException e ){};
			}
		} catch(java.sql.SQLException e) {
			throw new PersistencyException(e);
		}
	}
	/**
	 * Estrae il codice di tutte le unità organizzative su cui l'utente ha ruoli propri
	 *
	 * @param utente Utente in processo
	 * @return collezione di codice di UO su cui l'utente ha accessi propri
	 */

	public java.util.Collection findUO_ruoli_propri(UtenteTemplateBulk utente) throws IntrospectionException, PersistencyException
	{
		try {
			SQLBuilder sql = new SQLBuilder();
			sql.setHeader("SELECT DISTINCT CD_UNITA_ORGANIZZATIVA");
			sql.addTableToHeader("UTENTE_UNITA_RUOLO");
			sql.addSQLClause("AND","CD_UTENTE",sql.EQUALS,utente.getCd_utente());
			LoggableStatement stm = sql.prepareStatement(getConnection());
			try {
				java.sql.ResultSet rs = stm.executeQuery();
				java.util.ArrayList aAL =  new java.util.ArrayList();
				while(rs.next())
					aAL.add(rs.getString(1));
				return aAL;
			} finally {
				try{stm.close();}catch( java.sql.SQLException e ){};
			}
		} catch(java.sql.SQLException e) {
			throw new PersistencyException(e);
		}
	}
	/**
	 * Recupera la lista di associazioni utente-unita-accesso (Utente_unita_accessoBulk) definite per un certo utente e
	 * per una unita' organizzativa
	 * @param utente UtenteTemplateBulk o UtenteComuneBulk per cui cercare le associazioni
	 * @return List lista di Utente_unita_accessoBulk
	 */

	public java.util.Collection findUtente_unita_accessi(UtenteTemplateBulk utente) throws ApplicationException, IntrospectionException, PersistencyException
	{
		PersistentHome uuaHome = getHomeCache().getHome( Utente_unita_accessoBulk.class);

		if(utente.getUnita_org_per_accesso() == null)
			throw new ApplicationException("Nessuna unità organizzativa selezionata!");

		SQLBuilder sql = uuaHome.createSQLBuilder();
		sql.addSQLClause("AND","CD_UTENTE",sql.EQUALS,utente.getCd_utente());
		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,utente.getUnita_org_per_accesso().getCd_unita_organizzativa());
		return uuaHome.fetchAll(sql);
	}
	/**
	 * Recupera la lista di associazioni utente-unita-ruolo (Utente_unita_ruoloBulk) definite per un certo utente e
	 * per una unita' organizzativa
	 * @param utente UtenteTemplateBulk o UtenteComuneBulk per cui cercare le associazioni
	 * @return List lista di Utente_unita_ruoloBulk
	 */

	public java.util.Collection findUtente_unita_ruoli(UtenteTemplateBulk utente) throws IntrospectionException, PersistencyException, ApplicationException
	{
		if(utente.getUnita_org_per_ruolo() == null)
			throw new ApplicationException("Nessuna unità organizzativa selezionata!");

		PersistentHome uurHome = getHomeCache().getHome( Utente_unita_ruoloBulk.class);
		SQLBuilder sql = uurHome.createSQLBuilder();
		sql.addSQLClause("AND","CD_UTENTE",sql.EQUALS,utente.getCd_utente());
		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,utente.getUnita_org_per_ruolo().getCd_unita_organizzativa());
		return uurHome.fetchAll(sql);
	}
	public SQLBuilder SqlTofindUtenteByCDR(String cdr) throws IntrospectionException, PersistencyException
	{
		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND","CD_CDR",sql.EQUALS,cdr);
		return sql;
	}
	/**
	 * Recupera la lista di tutti gli utenti per CDR
	 *
	 * @param CDR Centro di responsabilità per il quale cercare
	 * @return List lista di Utente
	 */

	public java.util.Collection findUtenteByCDR(String cdr) throws IntrospectionException, PersistencyException
	{
		return fetchAll(SqlTofindUtenteByCDR(cdr));
	}
	/**
	 * Recupera la lista di tutti gli utenti per CDR, inclusi quelli
	 * del CDR di primo livello associato
	 *
	 * @param CDR Centro di responsabilità per il quale cercare
	 * @return List lista di Utente
	 */

	public java.util.Collection findUtenteByCDRIncludeFirstLevel(String cdr) throws IntrospectionException, PersistencyException
	{
		SQLBuilder sql = SqlTofindUtenteByCDR(cdr);

		CdrHome cdrHome = (CdrHome)getHomeCache().getHome( CdrBulk.class);
		SQLBuilder sqlPrimoLivello = cdrHome.createSQLBuilderEsteso();
		sqlPrimoLivello.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdr);
		sqlPrimoLivello.addSQLJoin("CDR.CD_CDR_AFFERENZA",sql.EQUALS,"UTENTE.CD_CDR");
		sql.addSQLExistsClause("OR",sqlPrimoLivello);
		return fetchAll(sql);
	}

	public java.util.Collection findUtente_indirizzi_email(UtenteBulk utente) throws IntrospectionException, PersistencyException, ApplicationException {
		PersistentHome uurHome = getHomeCache().getHome( Utente_indirizzi_mailBulk.class);
		SQLBuilder sql = uurHome.createSQLBuilder();
		sql.addSQLClause("AND","CD_UTENTE",sql.EQUALS,utente.getCd_utente());
		return uurHome.fetchAll(sql);
	}

	public java.util.Collection findUtente_abil_ordine(UtenteBulk utente) throws IntrospectionException, PersistencyException, ApplicationException {
		PersistentHome uurHome = getHomeCache().getHome( AbilUtenteUopOperBulk.class);
		SQLBuilder sql = uurHome.createSQLBuilder();
		sql.addSQLClause("AND","CD_UTENTE",sql.EQUALS,utente.getCd_utente());

		List<AbilUtenteUopOperBulk> lista = uurHome.fetchAll(sql);
		if ( lista!=null && lista.size()>0) {
			for (AbilUtenteUopOperBulk abilUtenteUopOperBulk : lista) {
				abilUtenteUopOperBulk.setUtente_abil_magazzini(findAbilMagazzini(abilUtenteUopOperBulk));
			}
		}
		return lista;
	}

	private BulkList<AbilUtenteUopOperMagBulk> findAbilMagazzini(AbilUtenteUopOperBulk abilUtenteUopOperBulk) throws PersistencyException{
		PersistentHome abilOrdMagazzHome = getHomeCache().getHome( AbilUtenteUopOperMagBulk.class);
		SQLBuilder sql = abilOrdMagazzHome.createSQLBuilder();
		sql.addSQLClause("AND","CD_UTENTE",sql.EQUALS,abilUtenteUopOperBulk.getCdUtente());
		sql.addSQLClause("AND","CD_UNITA_OPERATIVA",sql.EQUALS,abilUtenteUopOperBulk.getCdUnitaOperativa());
		sql.addSQLClause("AND","CD_TIPO_OPERAZIONE",sql.EQUALS,abilUtenteUopOperBulk.getCdTipoOperazione());


		BulkList bulkList = new BulkList(abilOrdMagazzHome.fetchAll(sql));
		return bulkList;
	}

	public List<UtenteBulk> findUtenteByUID(UserContext userContext, String cd_utente_uid) throws PersistencyException {
		final SQLBuilder sqlBuilder = createSQLBuilder();
		sqlBuilder.addClause(FindClause.AND, "cd_utente_uid", SQLBuilder.EQUALS, cd_utente_uid);
		sqlBuilder.addOrderBy("dt_ultimo_accesso desc");
		return fetchAll(sqlBuilder);
	}

	@Override
	public SQLBuilder restSelect(UserContext userContext, SQLBuilder sql, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		if(compoundfindclause == null){
			if(oggettobulk != null)
				compoundfindclause = oggettobulk.buildFindClauses(null);
		}else{
			compoundfindclause = CompoundFindClause.and(compoundfindclause, oggettobulk.buildFindClauses(Boolean.FALSE));
		}
		sql =  getHomeCache().getHome(UtenteBulk.class, "V_UTENTE_LDAP").selectByClause(userContext, compoundfindclause);

		return sql;
	}

}
