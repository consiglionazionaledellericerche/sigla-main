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

package it.cnr.contab.pdg00.comp;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.esercizio.bulk.*;
import it.cnr.contab.config00.ejb.*;
import it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneHome;
import it.cnr.contab.doccont00.core.bulk.Stampa_registro_accertamentiBulk;
import it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk;
import it.cnr.contab.pdg00.cdip.bulk.*;
import it.cnr.contab.utenze00.bp.*;
import it.cnr.jada.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;

public class CRUDCostoDelDipendenteComponent extends it.cnr.jada.comp.CRUDComponent implements ICRUDCostoDelDipendenteMgr {
/**
 * CostoDelDipendenteComponent constructor comment.
 */
public CRUDCostoDelDipendenteComponent() {
	super();
}
/*
 * Pre-post-conditions:
 *
 * Nome: Dipendente non modificabile
 * Pre: Viene richiesta l'inizializzazione per modifica dei costi del dipendente.
 *		Per la matricola specificata esiste già una ripartizione dei costi.
 * Post: Viene impostato a false il flag "modificabile" del V_dipendenteBulk restituito
 * Nome: Tutti i controlli superati
 * Pre:	Viene richiesta l'inizializzazione per modifica dei costi del dipendente.
 *		Nessuna delle altre pre-condizioni è verificata.
 * Post: Viene caricato un oggetto V_dipendenteBulk per la matricola specificata e l'elenco
 *		dei costi per voce del pdc (Costo_del_dipendenteBulk)
 */

public void initializeKeysAndOptionsInto(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException{
	if (oggettobulk instanceof Stipendi_cofi_coriBulk)
	{
		try {
			Stipendi_cofiHome stipendi_cofiHome = (Stipendi_cofiHome)getHome(usercontext, it.cnr.contab.pdg00.cdip.bulk.Stipendi_cofiBulk.class);
			java.util.Collection stipendi_cofi;
			stipendi_cofi = stipendi_cofiHome.findStipendiCofiAnno(usercontext);
			((Stipendi_cofi_coriBulk)oggettobulk).setTipoStipendi_cofi(stipendi_cofi);
			((Stipendi_cofi_coriBulk)oggettobulk).setEsercizio(CNRUserContext.getEsercizio(usercontext));
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
	}
	if (oggettobulk instanceof Stipendi_cofi_obb_scadBulk)
	{
		try {
			Stipendi_cofiHome stipendi_cofiHome = (Stipendi_cofiHome)getHome(usercontext, it.cnr.contab.pdg00.cdip.bulk.Stipendi_cofiBulk.class);
			java.util.Collection stipendi_cofi;
			stipendi_cofi = stipendi_cofiHome.findStipendiCofiAnno(usercontext);
			((Stipendi_cofi_obb_scadBulk)oggettobulk).setTipoStipendi_cofi(stipendi_cofi);
			((Stipendi_cofi_obb_scadBulk)oggettobulk).setEsercizio(CNRUserContext.getEsercizio(usercontext));
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
	}
	super.initializeKeysAndOptionsInto(usercontext,oggettobulk);
}
public Stipendi_cofiVirtualBulk caricaDettagliFiltrati(UserContext userContext,OggettoBulk bulk, CompoundFindClause clause) throws ComponentException{
	try {
		Stipendi_cofiVirtualBulk stipendi_cofiVirtual = (Stipendi_cofiVirtualBulk)bulk;
		BulkHome home = getHome(userContext,Stipendi_cofi_obbBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addClause(clause);
		stipendi_cofiVirtual.setStipendi_obbligazioni(new BulkList(home.fetchAll(sql)));
		return stipendi_cofiVirtual;
	} catch (PersistencyException e) {
		throw new ComponentException(e);
	}
}
		
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	if (bulk instanceof Stipendi_cofi_coriBulk || bulk instanceof Stipendi_cofi_obb_scadBulk )	  
	  return super.inizializzaBulkPerModifica(userContext,bulk);
	
	if (bulk instanceof Stipendi_cofiVirtualBulk)
	{
		Stipendi_cofiVirtualBulk stipendi_cofiVirtual = (Stipendi_cofiVirtualBulk)bulk;

		BulkHome home = getHome(userContext,Stipendi_cofi_obbBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addClause(FindClause.AND,"esercizio_obbligazione",SQLBuilder.EQUALS,stipendi_cofiVirtual.getObbligazioni().getEsercizio());
		sql.addClause(FindClause.AND,"cd_cds_obbligazione",SQLBuilder.EQUALS,stipendi_cofiVirtual.getObbligazioni().getCd_cds());
		sql.addClause(FindClause.AND,"esercizio_ori_obbligazione",SQLBuilder.EQUALS,stipendi_cofiVirtual.getObbligazioni().getEsercizio_originale());
		sql.addClause(FindClause.AND,"pg_obbligazione",SQLBuilder.EQUALS,stipendi_cofiVirtual.getObbligazioni().getPg_obbligazione());
		try {
			stipendi_cofiVirtual.setStipendi_obbligazioni(new BulkList(home.fetchAll(sql)));
			getHomeCache(userContext).fetchAll(userContext);
			return stipendi_cofiVirtual;
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
	}
	
	try {
		V_dipendenteBulk dipendente = (V_dipendenteBulk)bulk;

		BulkHome home = getHome(userContext,Costo_del_dipendenteBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(FindClause.AND,"id_matricola",SQLBuilder.EQUALS,dipendente.getId_matricola());
		sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addClause(FindClause.AND,"mese",SQLBuilder.EQUALS,dipendente.getMese());
		dipendente.setCosti_per_elemento_voce(new BulkList(home.fetchAll(sql)));
		getHomeCache(userContext).fetchAll(userContext);

		boolean modificabile = isDipendenteModificabile(userContext,dipendente.getId_matricola(),dipendente.getMese());

		if (modificabile)
			modificabile = Optional.ofNullable(((Configurazione_cnrHome)getHome(userContext, Configurazione_cnrBulk.class)).getCdrPersonale(CNRUserContext.getEsercizio(userContext)))
					.filter(cdrPersonale->cdrPersonale.equals(CNRUserContext.getCd_cdr(userContext))).isPresent();

		if (!modificabile)
			return asRO(dipendente,"Importi non modificabili per questa matricola.");

		return dipendente;
	} catch(Throwable e) {
		throw handleException(e);
	}
}
/**
 * Testa se la matricola specificata possiede una ripartizione dei costi nell'esercizio di
 * scrivania.
 */
private boolean isDipendenteModificabile(UserContext userContext,String matricola,Integer mese) throws it.cnr.jada.comp.ComponentException {
	try {
		SQLBuilder sql_exists = getHome(userContext,Ass_cdp_laBulk.class).createSQLBuilder();
		sql_exists.addClause("AND","id_matricola",sql_exists.EQUALS,matricola);
		sql_exists.addClause("AND","esercizio",sql_exists.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql_exists.addClause("AND","mese",sql_exists.EQUALS,mese);
		if (sql_exists.executeExistsQuery(getConnection(userContext)))
			return false;
		sql_exists = getHome(userContext,Ass_cdp_uoBulk.class).createSQLBuilder();
		sql_exists.addClause("AND","id_matricola",sql_exists.EQUALS,matricola);
		sql_exists.addClause("AND","esercizio",sql_exists.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql_exists.addClause("AND","mese",sql_exists.EQUALS,mese);
		if (sql_exists.executeExistsQuery(getConnection(userContext)))
			return false;
		return true;
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	}
}
protected boolean isEsercizioChiuso(UserContext userContext,Unita_organizzativaBulk uo) throws ComponentException {
	try {
		if (uo.getCd_unita_padre() == null)
			uo = (Unita_organizzativaBulk)getHome(userContext,uo).findByPrimaryKey(uo);
		EsercizioHome home = (EsercizioHome)getHome(userContext,EsercizioBulk.class);
		return home.isEsercizioChiuso(userContext,CNRUserContext.getEsercizio(userContext),uo.getCd_unita_padre());
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/**
 * Mette un lock su tutti i record di COSTI_DEL_DIPENDENTE per la matricola specificata
 */
private void lockMatricola(UserContext userContext,String id_matricola,Integer mese) throws ComponentException, BusyResourceException {
	try {
		SQLBuilder sql = getHome(userContext,Costo_del_dipendenteBulk.class).createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND","ID_MATRICOLA",sql.EQUALS,id_matricola);
		sql.addSQLClause("AND","MESE",sql.EQUALS,mese);
		sql.setForUpdate(true);
		LoggableStatement stm = sql.prepareStatement(getConnection(userContext));
		try {
			java.sql.ResultSet rs = stm.executeQuery();
			while (rs.next());
		} finally {
			try{stm.close();}catch( java.sql.SQLException e ){};
		}
	} catch(java.sql.SQLException e) {
		throw new BusyResourceException();
	}
}
/*
 * Pre-post-conditions:
 *
 * Nome: Dipendente non modificabile
 * Pre: Viene richiesta la modifica dei costi del dipendente ma per la matricola specificata
 *		esiste già una ripartizione dei costi.
 * Post: Viene generata una ApplicationException con il messaggio: "Dipendente non modificabile perchè è già stata fatta una ripartizione dei costi."
 * Nome: Unità organizzativa del dipendente modificata
 * Pre: Viene richiesta la modifica dei costi del dipendente ed è stata modificata
 *		l'unità organizzativa di appartenenza.
 * Post: Viene eliminata la matricola specificata dalla tabella COSTO_DEL_DIPENDENTE e
 *		vengono inseriti nuovi record nella stessa tabella con la nuova u.o
 * Nome: Tutti i controlli superati
 * Pre:	Viene richiesta la modifica dei costi del dipendente.
 *		Nessuna delle altre pre-condizioni è verificata.
 * Post: Vengono salvati i costi del dipendente.
 */
public OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	if (bulk instanceof Stipendi_cofi_coriBulk || bulk instanceof Stipendi_cofi_obb_scadBulk
		|| bulk instanceof Stipendi_cofiVirtualBulk)
		return super.modificaConBulk(userContext,bulk);
	try {
		V_dipendenteBulk dipendente = (V_dipendenteBulk)bulk;

		if (!isDipendenteModificabile(userContext,dipendente.getId_matricola(),dipendente.getMese()))
			throw new ApplicationException("Dipendente non modificabile perchè è già stata fatta una ripartizione dei costi.");
		if (dipendente.getUnita_organizzativa() == null ||
			dipendente.getUnita_organizzativa().getCrudStatus() != dipendente.NORMAL)
			throw new ApplicationException("E' necessario specificare una unità organizzativa.");

		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext,dipendente.getUnita_organizzativa()))
			throw new ApplicationException("Importi non modificabili ad esercizio chiuso.");

		lockMatricola(userContext,dipendente.getId_matricola(),dipendente.getMese());
		V_dipendenteBulk dipendente_orig = (V_dipendenteBulk)getHome(userContext,dipendente).findByPrimaryKey(dipendente);

		for (java.util.Iterator i = dipendente.getCosti_per_elemento_voce().iterator();i.hasNext();)
			checkSQLConstraints(userContext,(Costo_del_dipendenteBulk)i.next());

		if (dipendente_orig.getUnita_organizzativa() != null &&
			dipendente_orig.getCd_unita_organizzativa().equals(dipendente.getCd_unita_organizzativa())) {
			makeBulkListPersistent(userContext,dipendente.getCosti_per_elemento_voce());
		} else {
			if (isEsercizioChiuso(userContext,dipendente_orig.getUnita_organizzativa()))
				throw new ApplicationException("Non è possibile modificare l'unità organizzativa della matricola perchè l'esercizio è chiuso.");

			for (java.util.Iterator i = dipendente.getCosti_per_elemento_voce().iterator();i.hasNext();) {
				Costo_del_dipendenteBulk cdd = (Costo_del_dipendenteBulk)i.next();
				deleteBulk(userContext,cdd);
				cdd.setCd_unita_organizzativa(dipendente.getCd_unita_organizzativa());
				cdd.setUser(CNRUserContext.getUser(userContext));
				insertBulk(userContext,cdd);
			}
		}
		return bulk;
	} catch(Throwable e) {
		throw handleException(e);
	}
}
protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
	if (bulk instanceof Stipendi_cofi_coriBulk || bulk instanceof Stipendi_cofi_obb_scadBulk){
		if (clauses == null) {
			if (bulk != null)
				clauses = bulk.buildFindClauses(null);
		}		
		SQLBuilder sql = getHome(userContext,bulk).selectByClause(clauses);
		sql.addClause("AND","esercizio",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		return sql;		
	}
	if (bulk instanceof Stipendi_cofiVirtualBulk){
		if (clauses == null) {
			if (bulk != null)
				clauses = bulk.buildFindClauses(null);
		}		
		Stipendi_cofiVirtualBulk stipendi_cofiVirtual = (Stipendi_cofiVirtualBulk)bulk;
		SQLBuilder sql = getHome(userContext,Stipendi_cofi_obbBulk.class).selectByClause(clauses);
		sql.addClause("AND","esercizio",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		BulkHome home = getHome(userContext,Stipendi_cofi_obbBulk.class);
		stipendi_cofiVirtual.setStipendi_obbligazioni(new BulkList(home.fetchAll(sql)));
		getHomeCache(userContext).fetchAll(userContext);
		return sql;		
	}
	if (clauses == null) {
		if (bulk != null)
			clauses = bulk.buildFindClauses(null);
	} else
		clauses = it.cnr.jada.persistency.sql.CompoundFindClause.and(clauses,bulk.buildFindClauses(Boolean.FALSE));
	if (clauses == null || !clauses.iterator().hasNext())
		throw new ApplicationException("E' necessario specificare almeno una clausola di ricerca.");
	SQLBuilder sql = getHome(userContext,bulk).selectByClause(clauses);
	V_dipendenteBulk dipendente = (V_dipendenteBulk)bulk;
	sql.addClause("AND","esercizio",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
	if (dipendente.getMese() == null)
		sql.addClause("AND","mese",sql.NOT_EQUALS,new Integer(0));
	return sql;
}
public SQLBuilder selectStipendi_cofi_obbByClause(UserContext userContext, Stipendi_cofi_obb_scadBulk obb_scad, Stipendi_cofi_obbBulk obb, CompoundFindClause clauses) throws ComponentException {

	Stipendi_cofi_obbHome home = (Stipendi_cofi_obbHome)getHome(userContext, Stipendi_cofi_obbBulk.class);
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "esercizio", sql.EQUALS, obb_scad.getEsercizio());
	sql.addClause(clauses);
	return sql;
}

public SQLBuilder selectObbligazioniByClause(UserContext userContext, Stipendi_cofi_obbBulk stiObb, ObbligazioneBulk obb, CompoundFindClause clauses) throws ComponentException {

	ObbligazioneHome home = (ObbligazioneHome)getHome(userContext, ObbligazioneBulk.class);
	SQLBuilder sql = home.createSQLBuilder();
	sql.addTableToHeader("UNITA_ORGANIZZATIVA");
	sql.addSQLJoin("OBBLIGAZIONE.CD_CDS","UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
	sql.addSQLClause("AND", "esercizio", sql.EQUALS, stiObb.getEsercizio());
	sql.addSQLClause("AND", "UNITA_ORGANIZZATIVA.CD_TIPO_UNITA", sql.EQUALS, it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC );
	sql.addClause(clauses);
	return sql;
}
}
