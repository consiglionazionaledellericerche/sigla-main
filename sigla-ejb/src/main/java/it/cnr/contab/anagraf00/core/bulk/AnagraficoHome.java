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

package it.cnr.contab.anagraf00.core.bulk;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;


import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk;
import it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.missioni00.docs.bulk.AnticipoBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

public class AnagraficoHome extends BulkHome {
	/**
	 * Costruttore
	 *
	 * @param conn <code>java.sql.Connection</code>
	 */

	public AnagraficoHome(java.sql.Connection conn) {
		super(AnagraficoBulk.class,conn);
	}
	/**
	 * Costruttore
	 *
	 * @param conn <code>java.sql.Connection</code>
	 * @param persistentCache <code>PersistentCache</code>
	 */

	public AnagraficoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(AnagraficoBulk.class,conn,persistentCache);
	}
	/**
	 * Verifica la presenza o meno della partita IVA assegnata a un oggetto anagrafico.
	 *
	 * @param anagrafico L'anagrafica da verificare.
	 *
	 * @return boolean <code>true</code> se la Partita iva esiste.
	 */

	public boolean checkPIVA(AnagraficoBulk anagrafico) throws PersistencyException {
		try {
			if(anagrafico.getPartita_iva() != null) {
				String strIVA = "SELECT COUNT(*)" +
								" FROM " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "ANAGRAFICO" +
								" WHERE PARTITA_IVA = '" + anagrafico.getPartita_iva() + "'";
				if(anagrafico.isToBeUpdated())
					strIVA += " AND CD_ANAG != " + anagrafico.getCd_anag();

				java.sql.ResultSet rsPIVA = getConnection().createStatement().executeQuery(strIVA);
				rsPIVA.next();
				if(rsPIVA.getInt(1) > 0)
					return true;
				else
					return false;
			} else {
				return false;
			}
		} catch (java.sql.SQLException sqle) {
			throw new PersistencyException(sqle.getMessage());
		}
	}
	/**
	 * Recupera tutti i dati nella tabella BANCA relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>BancaBulk</code>
	 */

	public java.util.Collection findBanca(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome bancaHome = getHomeCache().getHome(BancaBulk.class,"V_BANCA_ANAG");
		SQLBuilder sql = bancaHome.createSQLBuilder();
		sql.addSQLClause("AND","CD_ANAG",sql.EQUALS,anagrafico.getCd_anag());
		return bancaHome.fetchAll(sql);
	}
	/**
	 * Recupera tutti i cap relativi al comune fiscale in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 * @param capHome .
	 * @param clause .
	 *
	 * @return java.util.Collection Collezione di oggetti <code>CapBulk</code>
	 *
	 * @exception IntrospectionException
	 * @exception PersistencyException
	 *
	 * @see <code>ComuneBulk</code>.<code>findCaps</code>
	 */

	public java.util.Collection findCaps_comune(AnagraficoBulk anagrafico,
												it.cnr.contab.anagraf00.tabter.bulk.CapHome capHome,
												it.cnr.contab.anagraf00.tabter.bulk.CapBulk clause)
													throws IntrospectionException, PersistencyException {

		return ((it.cnr.contab.anagraf00.tabter.bulk.ComuneHome)
					getHomeCache().getHome(
						it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk.class
					)
				).findCaps(anagrafico.getComune_fiscale());
	}
	/**
	 * Recupera tutti i dati nella tabella CARICO_FAMILIARE_ANAG relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Carico_familiare_anagBulk</code>
	 */

	public java.util.Collection findCarichi_familiari_anag(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome home = getHomeCache().getHome(Carico_familiare_anagBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND","cd_anag",sql.EQUALS,anagrafico.getCd_anag());
		return home.fetchAll(sql);
	}
	/**
	 * Recupera tutti i dati nella tabella CONTATTO relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>ContattoBulk</code>
	 */

	public java.util.Collection findContatti(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome contattoHome = getHomeCache().getHome(ContattoBulk.class,"V_CONTATTO_ANAG");
		SQLBuilder sql = contattoHome.createSQLBuilder();
		sql.addSQLClause("AND","CD_ANAG",sql.EQUALS,anagrafico.getCd_anag());
		return contattoHome.fetchAll(sql);
	}
	/**
	 * Recupera tutti i dati nella tabella DICHIARAZIONE_INTENTO relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Dichiarazione_intentoBulk</code>
	 */

	public java.util.Collection findDichiarazioni_intento(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome home = getHomeCache().getHome(Dichiarazione_intentoBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND","cd_anag",sql.EQUALS,anagrafico.getCd_anag());
		return home.fetchAll(sql);
	}
	/**
	 * Recupera tutti i dati nella tabella INQUADRAMENTO relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>InquadramentoBulk</code>
	 */

	public java.util.Collection findInquadramenti(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome home = getHomeCache().getHome(InquadramentoBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause("AND","CD_ANAG",sql.EQUALS,anagrafico.getCd_anag());
		sql.setOrderBy("dt_ini_validita",it.cnr.jada.util.OrderConstants.ORDER_ASC);
		return home.fetchAll(sql);
	}
	/**
	 * Recupera tutti i dati nella tabella PAGAMENTO_ESTERNO relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Pagamento_esternoBulk</code>
	 */

	public java.util.Collection findPagamenti_esterni(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome home = getHomeCache().getHome(Pagamento_esternoBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause("AND","CD_ANAG",sql.EQUALS,anagrafico.getCd_anag());
		sql.setOrderBy("dt_pagamento",it.cnr.jada.util.OrderConstants.ORDER_ASC);
		return home.fetchAll(sql);
	}	
	/**
	 * Recupera tutti i dati nella tabella MODALITA_PAGAMENTO relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Modalita_pagamentoBulk</code>
	 */

	public java.util.Collection findModalita_pagamento(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome modalita_pagamentoHome = getHomeCache().getHome(Modalita_pagamentoBulk.class,"V_MODALITA_PAGAMENTO_ANAG");
		SQLBuilder sql = modalita_pagamentoHome.createSQLBuilder();
		sql.addSQLClause("AND","CD_ANAG",sql.EQUALS,anagrafico.getCd_anag());
		return modalita_pagamentoHome.fetchAll(sql);
	}
	/**
	 * Recupera tutti i dati nella tabella RAPPORTO relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>RapportoBulk</code>
	 */

	public java.util.Collection findRapporti(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome rapportoHome = getHomeCache().getHome(RapportoBulk.class);
		SQLBuilder sql = rapportoHome.createSQLBuilder();
		sql.addClause("AND","cd_anag",sql.EQUALS,anagrafico.getCd_anag());
		sql.setOrderBy("dt_ini_validita",it.cnr.jada.util.OrderConstants.ORDER_ASC);
		sql.setOrderBy("cd_tipo_rapporto",it.cnr.jada.util.OrderConstants.ORDER_ASC);
		return rapportoHome.fetchAll(sql);
	}
	public AnagraficoBulk findGruppoIva(AnagraficoBulk anagrafico, Timestamp data) throws IntrospectionException, PersistencyException {
		AssGruppoIvaAnagBulk ass = findAssGruppoIva(anagrafico, data);
		if (ass != null){
			return ((AnagraficoBulk) findByPrimaryKey(ass.getAnagraficoGruppoIva()));
		}
		return null;
	}
	public AssGruppoIvaAnagBulk findAssGruppoIva(AnagraficoBulk anagrafico, Timestamp data) throws IntrospectionException, PersistencyException {
		if (data != null){
			Collection coll = findAssGruppoIva(anagrafico);
			for (Iterator d = coll.iterator(); d.hasNext(); ) {
				AssGruppoIvaAnagBulk assColl = (AssGruppoIvaAnagBulk) d.next();
				AnagraficoBulk anagraficoBulk = (AnagraficoBulk) findByPrimaryKey(assColl.getAnagraficoGruppoIva());
				if (anagraficoBulk.getDtIniValGruppoIva().compareTo(data) <= 0 && anagraficoBulk.getDt_canc().compareTo(data) >= 0) {
					return assColl;
				}
			}
		}
		return null;
	}
	public java.util.Collection findAssGruppoIva(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome assGruppoIvaHome = getHomeCache().getHome(AssGruppoIvaAnagBulk.class);
		SQLBuilder sql = findAssGruppoIva(anagrafico, assGruppoIvaHome);
		sql.setOrderBy("cd_anag",it.cnr.jada.util.OrderConstants.ORDER_ASC);
		return assGruppoIvaHome.fetchAll(sql);
	}

	private SQLBuilder findAssGruppoIva(AnagraficoBulk anagrafico, PersistentHome assGruppoIvaHome) {
		SQLBuilder sql = assGruppoIvaHome.createSQLBuilder();
		if (anagrafico.isGruppoIVA()){
			sql.addClause("AND","cd_anag_gr_iva",sql.EQUALS, anagrafico.getCd_anag());
		} else {
			sql.addClause("AND","cd_anag",sql.EQUALS, anagrafico.getCd_anag());
		}
		sql.addClause("AND","stato",sql.EQUALS,"INS");
		return sql;
	}

	public java.util.Collection findGruppiIvaAssociati(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome assGruppoIvaHome = getHomeCache().getHome(AssGruppoIvaAnagBulk.class);
		SQLBuilder sql = assGruppoIvaHome.createSQLBuilder();
		sql.addClause("AND","cd_anag",sql.EQUALS,anagrafico.getCd_anag());
		sql.addClause("AND","stato",sql.EQUALS,"INS");
		sql.setOrderBy("cd_anag",it.cnr.jada.util.OrderConstants.ORDER_ASC);
		return assGruppoIvaHome.fetchAll(sql);
	}
	public java.util.Collection findAssociazioniAlGruppoIva(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome assGruppoIvaHome = getHomeCache().getHome(AssGruppoIvaAnagBulk.class);
		SQLBuilder sql = assGruppoIvaHome.createSQLBuilder();
		sql.addClause("AND","cd_anag_gr_iva",sql.EQUALS,anagrafico.getCd_anag());
		sql.addClause("AND","stato",sql.EQUALS,"INS");
		sql.setOrderBy("cd_anag",it.cnr.jada.util.OrderConstants.ORDER_ASC);
		return assGruppoIvaHome.fetchAll(sql);
	}
/**
 * Controlla che esista un rapporto di tipo DIPENDENTE per l'anagrafica in uso.
 *
 * @param anagrafico L'anagrafica in uso.
 *
 * @return java.util.Collection Collezione di oggetti <code>RapportoBulk</code>
 */

public boolean findRapportoDipendenteFor(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {

	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	
	String subQuery = "CD_TIPO_RAPPORTO IN ( " + 
						 "SELECT CD_TIPO_RAPPORTO FROM "+ schema + "TIPO_RAPPORTO " +
						 "WHERE TI_DIPENDENTE_ALTRO = '" + Tipo_rapportoBulk.DIPENDENTE + "' )";

	// RECUPERA LA DATA DI SISTEMA PER I CONFRONTI				
	java.sql.Timestamp dataOdierna = getServerDate();
						 
	try{
		 
		PersistentHome rapportoHome = getHomeCache().getHome(RapportoBulk.class);
		SQLBuilder sql = rapportoHome.createSQLBuilder();
		sql.addClause("AND","cd_anag",sql.EQUALS,anagrafico.getCd_anag());
		sql.addSQLClause("AND","DT_INI_VALIDITA",sql.LESS_EQUALS,dataOdierna);
		sql.addSQLClause("AND","DT_FIN_VALIDITA",sql.GREATER_EQUALS,dataOdierna);
		sql.addSQLClause("AND", subQuery);
		
		return sql.executeExistsQuery(getConnection());
		
	} catch (java.sql.SQLException e){
		throw new IntrospectionException(e);
	}
}
	/**
	 * Recupera tutti i dati nella tabella TELEFONO relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 * @param telefonoHome <code>TelefonoHome</code>.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>TelefonoBulk</code>
	 */

	public java.util.Collection findTelefoni(AnagraficoBulk anagrafico, PersistentHome telefonoHome) throws IntrospectionException, PersistencyException {
		SQLBuilder sql = telefonoHome.createSQLBuilder();
		sql.addSQLClause("AND", "CD_ANAG", sql.EQUALS, anagrafico.getCd_anag());
		return telefonoHome.fetchAll(sql);
	}
	/**
	 * Recupera tutti i dati nella tabella TERMINI_PAGAMENTO relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Termini_pagamentoBulk</code>
	 */

	public java.util.Collection findTermini_pagamento(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome termini_pagamentoHome = getHomeCache().getHome(Termini_pagamentoBulk.class,"V_TERMINI_PAGAMENTO_ANAG");
		SQLBuilder sql = termini_pagamentoHome.createSQLBuilder();
		sql.addSQLClause("AND","CD_ANAG",sql.EQUALS,anagrafico.getCd_anag());
		return termini_pagamentoHome.fetchAll(sql);
	}
	/**
	 * Recupera tutti i dati nella tabella TERZO relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>TerzoBulk</code>
	 */

	public java.util.Collection findTerzi(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome terzoHome = getHomeCache().getHome(TerzoBulk.class);
		SQLBuilder sql = terzoHome.createSQLBuilder();
		sql.addClause("AND","cd_anag",sql.EQUALS,anagrafico.getCd_anag());
		return terzoHome.fetchAll(sql);
	}
	/**
	 * Imposta il cd_anag di un oggetto <code>AnagraficoBulk</code>.
	 *
	 * @param ngrfc <code>AnagraficoBulk</code>
	 *
	 * @exception PersistencyException
	 */

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk ngrfc) throws PersistencyException,ApplicationException {
		try {
			it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession numerazione =
				(it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession)
					it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_TABNUM_EJB_Numerazione_baseComponentSession",
																	it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession.class);
			((AnagraficoBulk)ngrfc).setCd_anag(
				new Integer(
					( numerazione.creaNuovoProgressivo(userContext,new Integer(0), "ANAGRAFICO", "CD_ANAG", ((AnagraficoBulk)ngrfc).getUser()) ).toString()
				)
			);
		}catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new ApplicationException(e);
		}			
		catch(Throwable e) {
			throw new PersistencyException(e);
		}
	}
	/**
	 * Restituisce tutte le anagrafiche meno quella in uso.
	 *
	 * @param anagrafico <code>AnagraficoBulk</code>
	 * @param home <code>AnagraficoHome</code>
	 * @param ente_app <code>AnagraficoBulk</code>
	 * @param clause <code>CompoundFindClause</code>
	 *
	 * @return <code>SQLBuilder</code>
	 *
	 * @exception java.lang.reflect.InvocationTargetException
	 * @exception IllegalAccessException
	 * @exception PersistencyException
	 * @exception IntrospectionException
	 */

	public SQLBuilder selectCd_ente_appByClause(AnagraficoBulk anagrafico,AnagraficoHome home,AnagraficoBulk ente_app,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException,PersistencyException,IntrospectionException {
		SQLBuilder sql = home.selectByClause(clause);
		sql.addClause("AND","cd_anag",sql.NOT_EQUALS,anagrafico.getCd_anag());
		return sql;
	}

	/**
	 * Recupera tutti i dati nella tabella OBBLIGAZIONE relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>ObbligazioneBulk</code>
	 */

	public java.util.Collection findObbligazioni(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome obbligazioneHome = getHomeCache().getHome(ObbligazioneBulk.class);
		SQLBuilder sql = obbligazioneHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("OBBLIGAZIONE.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		return obbligazioneHome.fetchAll(sql);
	}
	/**
	 * Verifica se ci sono OBBLIGAZIONI legate all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>ObbligazioneBulk</code>
	 * @throws SQLException 
	 */


	public boolean existsObbligazioni(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException, SQLException {
		PersistentHome obbligazioneHome = getHomeCache().getHome(ObbligazioneBulk.class);
		SQLBuilder sql = obbligazioneHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("OBBLIGAZIONE.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		return sql.executeExistsQuery(getConnection());
	}

	/**
	 * Recupera tutti i dati nella tabella ACCERTAMENTO relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>AccertamentoBulk</code>
	 */

	public java.util.Collection findAccertamenti(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome accertamentoHome = getHomeCache().getHome(AccertamentoBulk.class);
		SQLBuilder sql = accertamentoHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("ACCERTAMENTO.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		return accertamentoHome.fetchAll(sql);
	}
	/**
	 * Verifica se ci sono ACCERTAMENTI legati all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>AccertamentoBulk</code>
	 * @throws SQLException 
	 */

	public boolean existsAccertamenti(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException, SQLException {
		PersistentHome accertamentoHome = getHomeCache().getHome(AccertamentoBulk.class);
		SQLBuilder sql = accertamentoHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("ACCERTAMENTO.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		return sql.executeExistsQuery(getConnection());
	}

	/**
	 * Recupera tutti i dati nella tabella DOCUMENTO_GENERICO_RIGA relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Documento_generico_rigaBulk</code>
	 */

	public java.util.Collection findRigheDocumenti(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome documento_generico_rigaHome = getHomeCache().getHome(Documento_generico_rigaBulk.class);
		SQLBuilder sql = documento_generico_rigaHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("DOCUMENTO_GENERICO_RIGA.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		return documento_generico_rigaHome.fetchAll(sql);
	}

	/**
	 * Verifica se ci sono DOCUMENTO_GENERICO_RIGA relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Documento_generico_rigaBulk</code>
	 * @throws SQLException 
	 */

	public boolean existsRigheDocumenti(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException, SQLException {
		PersistentHome documento_generico_rigaHome = getHomeCache().getHome(Documento_generico_rigaBulk.class);
		SQLBuilder sql = documento_generico_rigaHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("DOCUMENTO_GENERICO_RIGA.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		return sql.executeExistsQuery(getConnection());
	}
	
	/**
	 * Boolean che ritorna TRUE se esistono record nella tabella FATTURA_ATTIVA relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Fattura_attivaBulk</code>
	 */

	public boolean existsFattureAttive(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		try{
			PersistentHome fattura_attivaHome = getHomeCache().getHome(Fattura_attivaBulk.class);
			SQLBuilder sql = fattura_attivaHome.createSQLBuilder();
			sql.addTableToHeader("TERZO");
			sql.addSQLJoin("FATTURA_ATTIVA.CD_TERZO", "TERZO.CD_TERZO");
			sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
			return sql.executeExistsQuery(getConnection());
		}
		catch(Throwable e) {
			throw new PersistencyException(e);
		}
	}

	/**
	 * Boolean che ritorna TRUE se esistono record nella tabella FATTURA_PASSIVA relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Fattura_passivaBulk</code>
	 */

	public boolean existsFatturePassive(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		try{
			PersistentHome fattura_passivaHome = getHomeCache().getHome(Fattura_passivaBulk.class);
			SQLBuilder sql = fattura_passivaHome.createSQLBuilder();
			sql.addTableToHeader("TERZO");
			sql.addSQLJoin("FATTURA_PASSIVA.CD_TERZO", "TERZO.CD_TERZO");
			sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
			return sql.executeExistsQuery(getConnection());
		}
		catch(Throwable e) {
			throw new PersistencyException(e);
		}
	}

	/**
	 * Recupera tutti i dati nella tabella MISSIONE relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>MissioneBulk</code>
	 */

	public java.util.Collection findMissioni(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome missioneHome = getHomeCache().getHome(MissioneBulk.class);
		SQLBuilder sql = missioneHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("MISSIONE.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		return missioneHome.fetchAll(sql);
	}
	/**
	 * Boolean che ritorna TRUE se esistono record nella tabella MISSIONE relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>MissioneBulk</code>
	 * @throws SQLException 
	 */

	public boolean existsMissioni(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException, SQLException {
		PersistentHome missioneHome = getHomeCache().getHome(MissioneBulk.class);
		SQLBuilder sql = missioneHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("MISSIONE.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		return sql.executeExistsQuery(getConnection());
	}

	/**
	 * Recupera tutti i dati nella tabella COMPENSO relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>CompensoBulk</code>
	 */

	public java.util.Collection findCompensi(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome compensoHome = getHomeCache().getHome(CompensoBulk.class);
		SQLBuilder sql = compensoHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("COMPENSO.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		return compensoHome.fetchAll(sql);
	}
	/**
	 * Boolean che ritorna TRUE se esistono record nella tabella COMPENSO relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>CompensoBulk</code>
	 * @throws SQLException 
	 */

	public boolean existsCompensi(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException, SQLException {
		PersistentHome compensoHome = getHomeCache().getHome(CompensoBulk.class);
		SQLBuilder sql = compensoHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("COMPENSO.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		return sql.executeExistsQuery(getConnection());
	}

	public java.util.Collection findCompensoValido(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome compensoHome = getHomeCache().getHome(CompensoBulk.class);
		SQLBuilder sql = compensoHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("COMPENSO.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		sql.addSQLClause("AND", "COMPENSO.STATO_COFI", SQLBuilder.NOT_EQUALS, CompensoBulk.STATO_ANNULLATO);
		sql.openParenthesis("AND");
		sql.addSQLClause("AND", "COMPENSO.FL_COMPENSO_CONGUAGLIO", SQLBuilder.EQUALS, "Y");
		sql.addSQLClause("OR", "COMPENSO.FL_COMPENSO_MINICARRIERA", SQLBuilder.EQUALS, "Y");
		sql.closeParenthesis();
		return compensoHome.fetchAll(sql);
	}
	/**
	 * Recupera tutti i dati nella tabella MINICARRIERA relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>MinicarrieraBulk</code>
	 */

	public java.util.Collection findMinicarriere(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome minicarrieraHome = getHomeCache().getHome(MinicarrieraBulk.class);
		SQLBuilder sql = minicarrieraHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("MINICARRIERA.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		return minicarrieraHome.fetchAll(sql);
	}
	/**
	 * Boolean che ritorna TRUE se esistono record nella tabella MINICARRIERA relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>MinicarrieraBulk</code>
	 * @throws SQLException 
	 */

	public boolean existsMinicarriere(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException, SQLException {
		PersistentHome minicarrieraHome = getHomeCache().getHome(MinicarrieraBulk.class);
		SQLBuilder sql = minicarrieraHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("MINICARRIERA.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		return sql.executeExistsQuery(getConnection());
	}

	/**
	 * Recupera tutti i dati nella tabella CONGUAGLIO relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>ConguaglioBulk</code>
	 */

	public java.util.Collection findConguagli(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome conguaglioHome = getHomeCache().getHome(ConguaglioBulk.class);
		SQLBuilder sql = conguaglioHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("CONGUAGLIO.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		return conguaglioHome.fetchAll(sql);
	}

	/**
	 * Boolean che ritorna TRUE se esistono record nella tabella  CONGUAGLIO relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>ConguaglioBulk</code>
	 * @throws SQLException 
	 */

	public boolean existsConguagli(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException, SQLException {
		PersistentHome conguaglioHome = getHomeCache().getHome(ConguaglioBulk.class);
		SQLBuilder sql = conguaglioHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("CONGUAGLIO.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		return sql.executeExistsQuery(getConnection());
	}

	/**
	 * Recupera tutti i dati nella tabella ANTICIPO relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>AnticipoBulk</code>
	 */

	public java.util.Collection findAnticipi(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome anticipoHome = getHomeCache().getHome(AnticipoBulk.class);
		SQLBuilder sql = anticipoHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("ANTICIPO.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		return anticipoHome.fetchAll(sql);
	}
	/**
	 * Recupera tutti i dati nella tabella ANTICIPO relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>AnticipoBulk</code>
	 * @throws SQLException 
	 */

	public boolean existsAnticipi(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException, SQLException {
		PersistentHome anticipoHome = getHomeCache().getHome(AnticipoBulk.class);
		SQLBuilder sql = anticipoHome.createSQLBuilder();
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("ANTICIPO.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_ANAG", SQLBuilder.EQUALS, anagrafico.getCd_anag());
		return sql.executeExistsQuery(getConnection());
	}

	/**
	 * Recupera tutti i dati nella tabella ANAGRAFICA_TERZO di tipo "Studio Associato" 
	 * relativi all'anagrafica in uso.
	 *
	 * @param anagrafico L'anagrafica in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Anagrafico_terzoBulk</code>
	 */

	public java.util.Collection findAssociatiStudio(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		return findAssociatiStudio(anagrafico.getCd_anag());
	}

	public java.util.Collection findAssociatiStudio(Integer cdAnag) throws IntrospectionException, PersistencyException {
		PersistentHome anagraficoTerzoHome = getHomeCache().getHome(Anagrafico_terzoBulk.class);
		SQLBuilder sql = anagraficoTerzoHome.createSQLBuilder();
		sql.addClause("AND", "cd_anag", SQLBuilder.EQUALS, cdAnag);
		sql.addClause("AND", "ti_legame", SQLBuilder.EQUALS, Anagrafico_terzoBulk.LEGAME_STUDIO_ASSOCIATO);
		return anagraficoTerzoHome.fetchAll(sql);
	}

	/**
	 * Recupera tutte le anagrafiche dato il codice fiscale 
	 *
	 * @param codice fiscale.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>AnagraficoBulk</code>
	 */	

	@SuppressWarnings("unchecked")
	public java.util.List<AnagraficoBulk> findByCodiceFiscaleOrPartitaIVA(String codiceFiscale, String partitaIVA) throws IntrospectionException, PersistencyException {
		SQLBuilder sql = createSQLBuilder();
		if (codiceFiscale != null)
			sql.addClause("AND", "codice_fiscale", SQLBuilder.EQUALS, codiceFiscale);
		if (partitaIVA != null)
			if (codiceFiscale == null){
				sql.addClause("OR", "partita_iva", SQLBuilder.EQUALS, partitaIVA);
			} else {
				sql.addClause("OR", "partita_iva", SQLBuilder.EQUALS, partitaIVA);
				sql.addClause("AND", "ti_entita_giuridica", SQLBuilder.NOT_EQUALS, AnagraficoBulk.GIURIDICA);
			}
		return fetchAll(sql);
	}

	public java.util.List<AnagraficoBulk> findByCodiceFiscale(String codiceFiscale, String partitaIVA) throws IntrospectionException, PersistencyException {
		SQLBuilder sql = createSQLBuilder();
		if (codiceFiscale != null)
			sql.addClause("AND", "codice_fiscale", SQLBuilder.EQUALS, codiceFiscale);
		return fetchAll(sql);
	}

	public java.util.Collection findDichiarazioni_intentoValide(AnagraficoBulk anagrafico) throws IntrospectionException, PersistencyException {
		PersistentHome home = getHomeCache().getHome(Dichiarazione_intentoBulk.class);
		java.sql.Timestamp dataOdierna = getServerDate();
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND","cd_anag",sql.EQUALS,anagrafico.getCd_anag());
		sql.addSQLClause("AND","DT_INI_VALIDITA",sql.LESS_EQUALS,dataOdierna);
		sql.addSQLClause("AND","DT_FIN_VALIDITA",sql.GREATER_EQUALS,dataOdierna);
		return home.fetchAll(sql);	
	}
	public java.util.List<AnagraficoBulk> findAnagraficoNonDipValidi() throws IntrospectionException, PersistencyException {
		SQLBuilder sql = createSQLBuilder();

		java.sql.Timestamp dataOdierna = getServerDate();

		String subQuery = " CD_anag IN ( " +
				"SELECT CD_anag FROM RAPPORTO " +
				"WHERE cd_tipo_rapporto not in ('DIP','EXDIP') AND DT_INI_VALIDITA <= to_date('10/03/2021','dd/mm/yyyy') " +
				" and DT_FIN_VALIDITA >= to_date('10/02/2021','dd/mm/yyyy')   AND duva > to_date('10/02/2021','dd/mm/yyyy')) ";
		sql.addSQLClause("AND", subQuery);
		sql.setOrderBy("cd_anag",it.cnr.jada.util.OrderConstants.ORDER_ASC);
		return fetchAll(sql);
	}
}
