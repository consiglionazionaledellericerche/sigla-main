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

package it.cnr.contab.compensi00.comp;

import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contHome;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneHome;
import it.cnr.contab.compensi00.ejb.CompensoComponentSession;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoHome;
import it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.GregorianCalendar;

import it.cnr.contab.docamm00.docs.bulk.*;
import java.io.Serializable;

import java.rmi.RemoteException;

import it.cnr.contab.compensi00.bp.CRUDCompensoBP;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Parametri_cnrComponentSession;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.docamm00.ejb.*;
import it.cnr.contab.incarichi00.bulk.Ass_incarico_uoBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_varBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashMap;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;

/**
 * Insert the type's description here.
 * Creation date: (21/02/2002 16.13.52)
 * @author: Roberto Fantino
 */
public class MinicarrieraComponent extends it.cnr.jada.comp.CRUDComponent implements IMinicarrieraMgr,Cloneable,Serializable {
/**
 * CompensoComponent constructor comment.
 */
public MinicarrieraComponent() {
	super();
}
private void addStaticClause(SQLBuilder sql, MinicarrieraBulk carriera){

	// Nella ricerca del terzo devo inserire come clausole statiche:
	//	1°: Tipo Dipendente/Altro
	//	2°: Data Registrazione minicarriera compresa fra Data Inizio e Data Fine validità

	sql.addSQLClause("AND","TI_DIPENDENTE_ALTRO",sql.EQUALS,carriera.getTi_anagrafico());
	sql.addSQLClause("AND","DT_INI_VALIDITA",sql.LESS_EQUALS,carriera.getDt_inizio_minicarriera());
	sql.addSQLClause("AND","DT_FIN_VALIDITA",sql.GREATER_EQUALS,carriera.getDt_fine_minicarriera());

}
private CompensoBulk aggiornaCompensoTemporaneo(
	it.cnr.jada.UserContext userContext,
	CompensoBulk compensoTemporaneo) 
 	throws ComponentException{

	try {
		CompensoComponentSession session = (CompensoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
															"CNRCOMPENSI00_EJB_CompensoComponentSession", 
															CompensoComponentSession.class);
		
		// inserisco il compenso assegnando un pg definitivo 
		return session.inserisciCompenso(userContext, compensoTemporaneo);
		
	} catch (Throwable e) {
		throw handleException(compensoTemporaneo, e);
	}
}
private void aggiornaMontantiPerCompensiTemporanei(
	it.cnr.jada.UserContext userContext,
	it.cnr.jada.bulk.PrimaryKeyHashMap compensiTemporanei) 
 	throws ComponentException{

	if (compensiTemporanei != null)
		try {
			PrimaryKeyHashMap processed = new PrimaryKeyHashMap();
			
			CompensoComponentSession session = (CompensoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
																"CNRCOMPENSI00_EJB_CompensoComponentSession", 
																CompensoComponentSession.class);
			for (java.util.Iterator i = compensiTemporanei.keySet().iterator(); i.hasNext();) {
				CompensoBulk compensoTemporaneo = (CompensoBulk)i.next();
				deleteBulk(userContext, compensoTemporaneo);

				// inserisco il compenso assegnando un pg definitivo
				CompensoBulk compensoDef = (CompensoBulk)compensiTemporanei.get(compensoTemporaneo);
				session.aggiornaMontanti(userContext, compensoDef);
				aggiornaObbligazioneTemporanea(userContext, compensoDef, processed);
			}
		} catch (Throwable e) {
			throw handleException(e);
		}
}
/**
  * Conferma dell'obbligazione temporanea associata al compenso
  * Viene recuperato un progressivo definitivo per l'obbligazione
  * e viene salvata su db
  *
**/
private void aggiornaObbligazioneTemporanea(
	UserContext userContext,
	CompensoBulk compenso,
	PrimaryKeyHashMap processed) throws ComponentException {

	if (compenso != null && compenso.getObbligazioneScadenzario().getObbligazione().isTemporaneo()) {
		ObbligazioneBulk obbligazioneTemporanea = compenso.getObbligazioneScadenzario().getObbligazione();
		if (processed.get(obbligazioneTemporanea) == null) {
			try {
				Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache(userContext).getHome(it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.class);
				Long pg = null;
				pg = numHome.getNextPg(userContext,
								obbligazioneTemporanea.getEsercizio(), 
								obbligazioneTemporanea.getCd_cds(), 
								obbligazioneTemporanea.getCd_tipo_documento_cont(), 
								obbligazioneTemporanea.getUser());

				ObbligazioneHome home = (ObbligazioneHome)getHome(userContext, obbligazioneTemporanea);
				ObbligazioneBulk obbligazioneTemporaneaOriginale = (ObbligazioneBulk)obbligazioneTemporanea.clone();
				home.confirmObbligazioneTemporanea(userContext,obbligazioneTemporanea, pg, false);
				
				obbligazioneTemporanea.setPg_obbligazione(pg);
				updateBulk(userContext, compenso);
				try {
					deleteBulk(userContext, obbligazioneTemporaneaOriginale);
				} catch (ReferentialIntegrityException ex) {
					//Ignorato di proposito: esistono altre scadenze associate
					//ancora da processare --> demando all'ultimo processo il
					//compito di cancellare
				}
				compenso.addToRelationsDocContForSaldi(obbligazioneTemporanea, obbligazioneTemporaneaOriginale.getPg_obbligazione());
				processed.put(obbligazioneTemporaneaOriginale, compenso);
			} catch (it.cnr.jada.persistency.PersistencyException e) {
				throw handleException(obbligazioneTemporanea, e);
			} catch (it.cnr.jada.persistency.IntrospectionException e) {
				throw handleException(obbligazioneTemporanea, e);
			}
		} else {
			Long pgAssegnato = ((CompensoBulk)processed.get(obbligazioneTemporanea)).getObbligazioneScadenzario().getPg_obbligazione();
			ObbligazioneBulk obbligazioneTemporaneaOriginale = (ObbligazioneBulk)obbligazioneTemporanea.clone();
			obbligazioneTemporanea.setPg_obbligazione(pgAssegnato);
			try { 
				updateBulk(userContext, compenso);
				deleteBulk(userContext, obbligazioneTemporaneaOriginale);
			} catch (ReferentialIntegrityException ex) {
				//Ignorato di proposito: esistono altre scadenze associate
				//ancora da processare --> demando all'ultimo processo il
				//compito di cancellare
			} catch (it.cnr.jada.persistency.PersistencyException e) {
				throw handleException(compenso, e);
			}
			//compenso.addToRelationsDocContForSaldi(obbligazioneTemporanea, obbligazioneTemporaneaOriginale.getPg_obbligazione());
		}
	}
}
private PrimaryKeyHashMap aggiornaRate(
	it.cnr.jada.UserContext userContext,
	MinicarrieraBulk carriera,
	boolean doInsert) 
 	throws ComponentException {

	try {
		PrimaryKeyHashMap compensiTemporanei = getCompensiTemporanei(
																	userContext,
																	carriera);
		for (java.util.Iterator i = carriera.getMinicarriera_rate().iterator(); i.hasNext();) {
			Minicarriera_rataBulk rata = (Minicarriera_rataBulk)i.next();
			CompensoBulk compensoAssociato = rata.getCompenso();
			if (compensoAssociato != null && compensoAssociato.isTemporaneo()) {
				CompensoBulk compensoDef = null;
				if (compensiTemporanei.containsKey(compensoAssociato) &&
					(compensoDef = (CompensoBulk)compensiTemporanei.get(compensoAssociato)) == null) {
					compensoDef = aggiornaCompensoTemporaneo(
														userContext,
														compensoAssociato);
					compensiTemporanei.put(compensoAssociato, compensoDef);
				}
				rata.setCompenso(compensoDef);
			}
			rata.setUser(userContext.getUser());
			if (doInsert || rata.isToBeCreated()) {
				insertBulk(userContext, rata);
			} else 
				updateBulk(userContext, rata);
		}
		return compensiTemporanei;
	} catch (Throwable e) {
		throw handleException(e);
	}
}
private void aggiornaSaldi(
	it.cnr.jada.UserContext uc,
	MinicarrieraBulk carriera,
	IDocumentoContabileBulk docCont,
	OptionRequestParameter status) 
 	throws ComponentException{

	try {
		if (docCont != null && carriera != null && carriera.getDefferredSaldi() != null) {
			IDocumentoContabileBulk key = carriera.getDefferredSaldoFor(docCont);
			if (key != null) {
				java.util.Map values = (java.util.Map)carriera.getDefferredSaldi().get(key);
				//caso di creazione o di nessuna modifica sui doc cont
				if (values == null) return;
				//QUI chiamare component del documento contabile interessato
				String jndiName = null;
				Class clazz = null;
				jndiName = "CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession";
				clazz = ObbligazioneAbstractComponentSession.class;
				DocumentoContabileComponentSession session = 
						(ObbligazioneAbstractComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
						jndiName,clazz);
				if (session != null) {
					session.aggiornaSaldiInDifferita(uc, key, values, status);
					carriera.getDefferredSaldi().remove(key);
				}
			}
		}
	} catch (javax.ejb.EJBException e) {
		throw handleException(carriera, e);
	} catch (java.rmi.RemoteException e) {
		throw handleException(carriera, e);
	}
}
private void aggiornaSaldiPer(
	it.cnr.jada.UserContext uc,
	MinicarrieraBulk carriera,
	OptionRequestParameter status)
 	throws ComponentException{

	if (carriera != null && carriera.getMinicarriera_rate() != null) {

		//Costruisco il vettore per prendere una sola volta il compenso (che puo' essere associato
		//a più rate) in maniera da aggiornare una volta sola i saldi dell'obbligazione
		java.util.Vector compensi = new java.util.Vector();
		for (java.util.Iterator i = carriera.getMinicarriera_rate().iterator(); i.hasNext();) {
			Minicarriera_rataBulk rata = (Minicarriera_rataBulk)i.next();
			if (rata.isAssociataACompenso() && 
				!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(compensi, rata.getCompenso()))
				compensi.add(rata.getCompenso());
		}
		for (java.util.Iterator i = compensi.iterator(); i.hasNext();) {
			CompensoBulk compenso = (CompensoBulk)i.next();
			synchronizeCarriera(uc, carriera, compenso);
			aggiornaSaldi(
						uc,
						carriera,
						compenso.getObbligazioneScadenzario().getObbligazione(),
						status);
		}
	}
}
private Long assegnaProgressivo(
	UserContext userContext, 
	MinicarrieraBulk carriera) 
	throws ComponentException {

	try {
		// Assegno un nuovo progressivo alla minicarriera
		ProgressiviAmmComponentSession progressiviSession = (ProgressiviAmmComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_ProgressiviAmmComponentSession", ProgressiviAmmComponentSession.class);
		Numerazione_doc_ammBulk numerazione = new Numerazione_doc_ammBulk(carriera);
		return progressiviSession.getNextPG(userContext, numerazione);

	}catch(javax.ejb.EJBException ex){
		throw handleException(carriera, ex);
	}catch(RemoteException ex){
		throw handleException(carriera, ex);
	}

}
private Long assegnaProgressivoTemporaneo(
	UserContext userContext,
	MinicarrieraBulk carriera)
	throws ComponentException {

	try {
		// Assegno un nuovo progressivo temporaneo alla minicarriera
		NumerazioneTempDocAmmComponentSession session = (NumerazioneTempDocAmmComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_NumerazioneTempDocAmmComponentSession", NumerazioneTempDocAmmComponentSession.class);
		Numerazione_doc_ammBulk numerazione = new Numerazione_doc_ammBulk(carriera);
		numerazione.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		return session.getNextTempPG(userContext, numerazione);
	}catch(javax.ejb.EJBException ex){
		throw handleException(carriera, ex);
	}catch(RemoteException ex){
		throw handleException(carriera, ex);
	}

}
//^^@@
/** 
  *  Associa le rate selezionate al compenso.
  *    PreCondition:
  *      Viene richiesta l'associazione delle rate selezionate al compenso.
  *    PostCondition:
  *      Il "compenso" viene associato ad ogni elemento di "rateAssociate" della "minicarriera".
  *  Stato associazione della rata associata
  *    PreCondition:
  *      La rata (elemento di "rateAssociate") viene associata al compenso.
  *    PostCondition:
  *      Lo stato associazione viene impostato a 'T' (Totalmente associata).
  *  Stato di associazione a compenso della testata
  *    PreCondition:
  *      Le "rateAssociate" sono associate.
  *    PostCondition:
  *      Nel caso in cui tutte le rate della minicarriera sono associate lo stato associazione viene impostato a 'T'
  *		 (Totalmente associata); altrimenti lo stato viene impostato a 'P' (Parzialmente associata)
  *  Il processo viene eseguito correttamente
  *    PreCondition:
  *      Le "rateAssociate" vengono correttamente associate.
  *    PostCondition:
  *      Viene restituita la minicarriera debitamente modificata
  *  Si verifica un errore
  *    PreCondition:
  *      Le "rateAssociate" non vengono correttamente associate.
  *    PostCondition:
  *      Viene restituita l'errore relativo
 */
//^^@@

public MinicarrieraBulk associaCompenso(
	UserContext userContext,
	MinicarrieraBulk minicarriera,
	java.util.List rateAssociate,
	CompensoBulk compenso) 
	throws ComponentException {

	for (java.util.Iterator i = rateAssociate.iterator(); i.hasNext();) {
		Minicarriera_rataBulk rata = (Minicarriera_rataBulk)i.next();
		rata.setCompenso(compenso);
		rata.setStato_ass_compenso(rata.STATO_TOTALE_ASS_COMPENSO);
		rata.setToBeUpdated();			
	}

	if (minicarriera.hasRateAssociateACompenso()) {
		if (minicarriera.getMinicarriera_rate().size() == minicarriera.getRateAssociateACompenso().size())
			minicarriera.setStato_ass_compenso(minicarriera.STATO_TOTALE_ASS_COMPENSO);
		else 
			minicarriera.setStato_ass_compenso(minicarriera.STATO_PARZIALE_ASS_COMPENSO);
		minicarriera.setToBeUpdated();
	}
		
	try {
		makeBulkPersistent(userContext, minicarriera);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(minicarriera, e);
	}
						
	return (MinicarrieraBulk)reloadMinicarriera(userContext, minicarriera);
}
//^^@@
/** 
  * Tutti i controlli superati.
  *		PreCondition:
  *			Viene richiesto il calcolo dell'aliquota irpef media di una minicarriera
  *			a tassazione separata
  *		PostCondition:
  *			A fronte degli importi irpef dei due anni precedenti viene
  *			impostata in minicarriera il valore dell'aliquota calcolata.
  *			(Influiscono sul calcolo l'esercizio di scrivania e l'esercizio della
  *			minicarriera, la data di registrazione, il percipiente ed il suo tipo
  *			di anagrafico, trattamento e rapporto)
  * Errori riscontrati
  *		PreCondition:
  *			Durante il calcolo dell'aliquota irpef media si verifica un errore formale
  *		PostCondition:
  *			L'errore viene segnalato all'utente
 */
//^^@@

public MinicarrieraBulk calcolaAliquotaMedia(
	UserContext userContext, 
	MinicarrieraBulk carriera)
	throws it.cnr.jada.comp.ComponentException {
	
	java.math.BigDecimal aliquota = callAliquotaMediaIrpef(
													userContext, 
													carriera.getImponibile_irpef_eseprec1(),
													carriera.getImponibile_irpef_eseprec2(),
													it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),
													carriera.getEsercizio(),
													carriera.getDt_registrazione(),
													carriera.getTi_anagrafico(),
													carriera.getCd_terzo(),
													carriera.getCd_tipo_rapporto(),
													carriera.getCd_trattamento());
	carriera.setAliquota_irpef_media(aliquota);

	return carriera;
}
/**
 * Insert the method's description here.
 * Creation date: (22/02/2002 14.28.44)
 * @return java.util.List
 */
private java.math.BigDecimal callAliquotaMediaIrpef(
	UserContext userContext, 
	java.math.BigDecimal imp1,
	java.math.BigDecimal imp2,
	Integer esercizioScrivania,
	Integer esercizioMinicarriera,	
	java.sql.Timestamp dataRegistrazione,
	String tipoAnagrafico,
	Integer cdPercipiente,
	String cdTipoRapporto,
	String cdTipoTrattamento) 
	throws ComponentException {

	LoggableStatement cs = null;
	java.math.BigDecimal aliquota = new java.math.BigDecimal(0).setScale(6, java.math.BigDecimal.ROUND_HALF_UP);

	try {
		try	{
			cs =new LoggableStatement(getConnection(userContext), 
				"{ ? = call " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
				"CNRCTB600.getAliquotaMediaIrpef( ?, ?, ?, ?, ?, ?, ?, ?, ?) }",
				false,this.getClass());
			cs.registerOutParameter( 1, java.sql.Types.NUMERIC);
			cs.setBigDecimal(2, imp1);
			cs.setBigDecimal(3, imp2);
			cs.setInt(4, esercizioScrivania.intValue());
			cs.setInt(5, esercizioMinicarriera.intValue());
			cs.setTimestamp(6, dataRegistrazione);
			cs.setString(7, tipoAnagrafico);
			cs.setInt(8, cdPercipiente.intValue());
			cs.setString(9, cdTipoRapporto);
			cs.setString(10, cdTipoTrattamento);

			cs.executeQuery();
			
			aliquota = cs.getBigDecimal(1);
			
		} catch (Throwable e) {
			throw handleException(e);
		} finally {
			if (cs != null) cs.close();
		}
	} catch (java.sql.SQLException ex) {
		throw handleException(ex);
	}

	return aliquota;
}
/**
 * Insert the method's description here.
 * Creation date: (22/02/2002 14.28.44)
 * @return java.util.List
 */
private void callGeneraRate(UserContext userContext, MinicarrieraBulk carriera) throws ComponentException{

	LoggableStatement cs = null;
	try	{
		cs = new LoggableStatement(getConnection(userContext), 
					"{call " +
					it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
					"CNRCTB605.creaRateMinicarriera(?,?,?,?,?,?,?,?) }",
					false,this.getClass());
		cs.setString( 1, carriera.getCd_cds()                 );		
		cs.setString( 2, carriera.getCd_unita_organizzativa() );		
		cs.setInt( 3, carriera.getEsercizio().intValue() );
		cs.setLong( 4, carriera.getPg_minicarriera().longValue() );

		if (carriera.getPgMinicarrieraPerClone() != null) {
			cs.setString( 5, carriera.getCd_cds() );
			cs.setString( 6, carriera.getCd_unita_organizzativa() );		
			cs.setInt( 7, carriera.getEsercizio().intValue() );
			cs.setLong( 8, carriera.getPgMinicarrieraPerClone().longValue() );
		} else {
			cs.setNull( 5, java.sql.Types.VARCHAR );		
			cs.setNull( 6, java.sql.Types.VARCHAR );		
			cs.setNull( 7, java.sql.Types.NUMERIC );
			cs.setNull( 8, java.sql.Types.NUMERIC );
		}

		cs.executeQuery();

	} catch (Throwable e) {
		throw handleException(e);
	} finally {
		try {
			if (cs != null) cs.close();
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		}
 	}
}
/**
 * Insert the method's description here.
 * Creation date: (22/02/2002 14.28.44)
 * @return java.util.List
 */
private void callValidaRate(UserContext userContext, MinicarrieraBulk carriera) throws ComponentException{

	LoggableStatement cs = null;
	try	{
		cs = new LoggableStatement(getConnection(userContext), 
					"{ call " +
					it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
					"CNRCTB605.chkScadRateMinicarriera(?,?,?,?) }",
					false,this.getClass());
		cs.setString( 1, carriera.getCd_cds()                 );		
		cs.setString( 2, carriera.getCd_unita_organizzativa() );		
		cs.setInt( 3, carriera.getEsercizio().intValue() );
		cs.setLong( 4, carriera.getPg_minicarriera().longValue() );

		cs.executeQuery();

	} catch (Throwable e) {
		throw handleException(e);
	} finally {
		try {
			if (cs != null) cs.close();
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		}
 	}
}
//^^@@
/** 
  *  Cessazione
  *		PreCondition:
  *			Viene richiesta la cessazione della minicarriera
  *		PostCondition:
  *			Viene eseguita la richiesta e restituita la minicarriera aggiornata non più modificabile
 */
//^^@@

public MinicarrieraBulk cessa(
	UserContext userContext, 
	MinicarrieraBulk carriera)
	throws it.cnr.jada.comp.ComponentException {

	carriera.setStato(carriera.STATO_CESSATA);
	carriera.setToBeUpdated();

	return (MinicarrieraBulk)modificaConBulk(userContext, carriera);
}
/**
 * Insert the method's description here.
 * Creation date: (22/02/2002 14.28.44)
 * @return java.util.List
 */
private void completaMinicarriera(
	UserContext userContext, 
	MinicarrieraBulk carriera)
	throws ComponentException{

	try {

		carriera.setPercipiente(loadPercipiente(userContext,carriera));
		completaPercipiente(userContext, carriera);
		carriera.setTipiTrattamento(findTipiTrattamento(userContext, carriera));
		loadTipoTrattamento(userContext, carriera);
		
		//if (isGestitePrestazioni(userContext))
		//{	
			carriera.impostaVisualizzaPrestazione();
		    //carriera.setTipiPrestazioneCompenso(getHome(userContext,Tipo_prestazione_compensoBulk.class).findAll());
		    carriera.setTipiPrestazioneCompenso(findTipiPrestazioneCompenso(userContext,
					carriera));
		//}    
		//else
			//carriera.setVisualizzaPrestazione(false);
		
		if (isGestitiIncarichi(userContext))
			carriera.impostaVisualizzaIncarico();
		else
			carriera.setVisualizzaPrestazione(false);
		
		getHomeCache(userContext).fetchAll(userContext);
		carriera.setAliquotaCalcolata(
							carriera.getFl_tassazione_separata() != null &&
							carriera.getFl_tassazione_separata().booleanValue() &&
							new java.math.BigDecimal(0).compareTo(carriera.getAliquota_irpef_media()) != 0);
	} catch (it.cnr.jada.persistency.PersistencyException ex) {
		throw handleException(ex);
	}				
}
private MinicarrieraBulk completaPercipiente(UserContext userContext, MinicarrieraBulk carriera) 
	throws ComponentException {

	if (carriera != null) {

		V_terzo_per_compensoBulk vTerzo = carriera.getPercipiente();
		carriera.setCd_terzo(vTerzo.getCd_terzo());
		carriera.setNome(vTerzo.getNome());
		carriera.setCognome(vTerzo.getCognome());
		carriera.setRagione_sociale(vTerzo.getRagione_sociale());
		carriera.setCodice_fiscale(vTerzo.getCodice_fiscale());
		carriera.setPartita_iva(vTerzo.getPartita_iva());

		carriera.setModalita(findModalita(userContext, carriera));
		carriera.setTermini(findTermini(userContext, carriera));
		carriera.setTipiRapporto(findTipiRapporto(userContext, carriera));
	}
	return carriera;
	
}
//^^@@
/** 
  *  Normale.
  *    PreCondition:
  *      Vengono richiesti i dati relativi al percipiente della minicarriera
  *			- richiesta caricamento dati diretti (nome, cognome, ragione sociale, CF, PIva)
  *         - richiesta caricamento modalita di pagamento
  *         - richiesta caricamento termini di pagamento
  *         - richiesta caricamento tipi di rapporto
  *    PostCondition:
  *      vengono trasmessi i dati relativi alla minicarriera.
 */
//^^@@

public MinicarrieraBulk completaPercipiente(
	UserContext userContext, 
	MinicarrieraBulk carriera,
	V_terzo_per_compensoBulk vTerzo)
	throws ComponentException {

	carriera.setPercipiente(vTerzo);
	return completaPercipiente(userContext, carriera);
	
}
/**
 * Insert the method's description here.
 * Creation date: (22/02/2002 14.28.44)
 * @return java.util.List
 */
private MinicarrieraBulk copiaMinicarriera(UserContext userContext, MinicarrieraBulk carriera) throws ComponentException{

	LoggableStatement cs = null;
	Integer esCorrente = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext);

	try	{
		cs = new LoggableStatement(getConnection(userContext),
			"{ call " + 
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 
			"CNRCTB605.copiaMinicarriera(? ,? ,? ,? ,? ,? ,? ,? ) }",
			false,this.getClass());
		cs.setString( 1, carriera.getCd_cds()                 );		
		cs.setString( 2, carriera.getCd_unita_organizzativa() );		
		cs.setInt( 3, carriera.getEsercizio().intValue()      );
		cs.setLong( 4, carriera.getPg_minicarriera().longValue() );
		cs.setString( 5, carriera.getCd_cds()                 );		
		cs.setString( 6, carriera.getCd_unita_organizzativa() );		
		cs.setInt( 7, esCorrente.intValue() );
		cs.setLong( 8, carriera.getPgMinicarrieraPerClone().longValue() );

		cs.executeQuery();

	} catch (java.sql.SQLException e) {
		if (-20100 == e.getErrorCode())
			throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
		throw handleException(e);
	} catch (Throwable e) {
		throw handleException(e);
	} finally {
		try {
			if (cs != null) cs.close();
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		}
 	}
	return new MinicarrieraBulk(
						carriera.getCd_cds(),
						carriera.getCd_unita_organizzativa(),
						esCorrente,
						carriera.getPgMinicarrieraPerClone());
}
//^^@@
/** 
  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta la creazione di una minicarriera e la stessa non ha superato il metodo 'validaMinicarriera'.
  *	PostCondition:
  *		Non  viene consentita la registrazione della minicarriera.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la creazione di una minicarriera e la stessa ha superato il metodo 'validaMinicarriera'.
  *	PostCondition:
  *		Viene consentita la registrazione della minicarriera.
 */
//^^@@

public OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	return creaConBulk(userContext, bulk, null);
}
//^^@@
/** 
  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta la creazione di una minicarriera e la stessa non ha superato il metodo 'validaMinicarriera'.
  *	PostCondition:
  *		Non  viene consentita la registrazione della minicarriera.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la creazione di una minicarriera e la stessa ha superato il metodo 'validaMinicarriera'.
  *	PostCondition:
  *		Viene consentita la registrazione della minicarriera.
 */
//^^@@

public it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status) throws it.cnr.jada.comp.ComponentException {

	MinicarrieraBulk carriera = (MinicarrieraBulk)bulk;
	
	validaMinicarriera(userContext, carriera);

	Long pgTmp = carriera.getPg_minicarriera();
	Long pg = assegnaProgressivo(userContext,carriera);
	carriera.setPg_minicarriera(pg);

	it.cnr.jada.bulk.PrimaryKeyHashMap compensiTemporanei = inserisciMinicarriera(userContext, carriera);

	callValidaRate(userContext, carriera);
	
	eliminaMinicarrieraClone(userContext, carriera, pgTmp);

	// Gli unici casi in cui esistono i cloni con minicarriere temporanee è quando
	// sto salvando carriere generate da rinnovi e ripristini --> devo gestire
	// anche la cancellazione dei cloni dei temporanei!
	if (carriera.getPgMinicarrieraPerClone() != null)
		eliminaMinicarrieraClone(userContext, carriera, carriera.getPgMinicarrieraPerClone());
	
	carriera.setPg_minicarriera(pg);

	aggiornaMontantiPerCompensiTemporanei(userContext, compensiTemporanei);
	
	aggiornaSaldiPer(userContext, carriera, status);

    if (!verificaStatoEsercizio(userContext, new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(carriera.getCd_cds(), ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio())))
         throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare un documento per un esercizio non aperto!");			
            	
	return carriera;
}
//^^@@
/** 
  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta l'eliminazione di una minicarriera e la stessa non ha superato il metodo 'eliminaMinicarriera'.
  *	PostCondition:
  *		Non  viene consentita l'eliminazione della minicarriera.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta l'eliminazione di una minicarriera e la stessa ha superato il metodo 'eliminaMinicarriera'.
  *	PostCondition:
  *		Viene consentita l'eliminazione della minicarriera.
 */
//^^@@

public void eliminaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	//try{
		MinicarrieraBulk carriera = (MinicarrieraBulk)bulk;
		eliminaMinicarriera(userContext,carriera);

		Long pgTmp = carriera.getPgMinicarrieraPerClone();

		//if (!carriera.isCancellabile()){
			//carriera.setStato(MinicarrieraBulk.STATO_SOSPESA);
			//updateBulk(userContext, carriera);
		//} else {
		for (java.util.Iterator i = carriera.getMinicarriera_rate().iterator(); i.hasNext();)
			((Minicarriera_rataBulk)i.next()).setToBeDeleted();
		super.eliminaConBulk(userContext, carriera);
		//}

		if (pgTmp!=null)
			eliminaMinicarrieraClone(userContext, carriera, pgTmp);

	//}catch(it.cnr.jada.persistency.PersistencyException ex){
		//handleException(bulk, ex);
	//}
}
//^^@@
/** 
  *   Minicarriera autogenerata
  *	PreCondition:
  *		Viene richiesta l'eliminazione di una minicarriera generata come ripristino o rinnovo di altra minicarriera.
  *	PostCondition:
  *		Non  viene consentita l'eliminazione della minicarriera.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta l'eliminazione di una minicarriera con almeno una rata collegata a compenso
  *	PostCondition:
  *		Non  viene consentita l'eliminazione della minicarriera.
 */
//^^@@

private void eliminaMinicarriera(
	UserContext userContext,
	MinicarrieraBulk carriera)
	throws it.cnr.jada.comp.ComponentException {

	if (carriera.getMinicarriera_origine() != null)
		throw new it.cnr.jada.comp.ApplicationException("Questa minicarriera ha una minicarriera di origine. Impossibile proseguire con la cancellazione!");
	if (carriera.isAssociataACompenso())
		throw new it.cnr.jada.comp.ApplicationException("Questa minicarriera ha almeno una rata collegata a compenso. Impossibile proseguire con la cancellazione!");
	if (!carriera.isAttiva())
		throw new it.cnr.jada.comp.ApplicationException("Questa minicarriera non è in stato attivo. Impossibile proseguire con la cancellazione!");
}
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Viene richiesta la modifica del compenso
  *    PostCondition:
  *      Viene consentito la modifica del compenso e 
  * 	 della obbligazione associata
  *
  *  Controlli non superati
  *    PreCondition:
  *      Non sono stati superati i controlli di validazione 
  * 	 per la modifica del compenso
  *    PostCondition:
  *      Non viene permessa la modifica del compenso e della obbligazione
**/
public void eliminaMinicarrieraClone(UserContext userContext, MinicarrieraBulk carriera, Long tmp) throws ComponentException{

	try{

		carriera.setPg_minicarriera(tmp);
		//Aggiunto cascade. NON + necessario
		//for (java.util.Iterator i = carriera.getMinicarriera_rate().iterator(); i.hasNext();)
			//try {
				//deleteBulk(userContext, (Minicarriera_rataBulk)i.next());
			//} catch (it.cnr.jada.persistency.ObjectNotFoundException e) {
			//}
		deleteBulk(userContext, carriera);
	
	} catch(it.cnr.jada.persistency.ObjectNotFoundException ex){
	} catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
//^^@@
/** 
  *  Normale.
  *    PreCondition:
  *      Nessun errore segnalato.
  *    PostCondition:
  *      Viene restituita la lista delle banche del percipiente.
 */
//^^@@

public java.util.List findListaBanche(
	UserContext userContext, 
	MinicarrieraBulk carriera) 
	throws ComponentException {

	try {
		if(carriera.getPercipiente() == null ||
			carriera.getPercipiente().getCd_terzo() == null)
			return null;

		return getHome(userContext, BancaBulk.class).fetchAll(selectBancaByClause(userContext, carriera, null, null));
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
/**
  * Percipiente selezionato
  *    PreCondition:
  *		 Viene richiesta la lista delle Modalità di pagamento
  * 	 associate al percipiente
  *	   PostCondition:
  *		 Viene restituita la lista dei Modalità di pagamento
  * 	 associate al percipiente
  *
  * Percipiente NON selezionato
  *    PreCondition:
  *		 Non è stato selezionato il percipiente
  *	   PostCondition:
  *		 Non vengono caricate le modalità di pagamento
**/

public java.util.Collection findModalita(
	UserContext userContext,
	OggettoBulk bulk)
	throws ComponentException {

	try {

		MinicarrieraBulk carriera = (MinicarrieraBulk)bulk;
		if(carriera.getTerzo() == null ||
			carriera.getTerzo().getCd_terzo() == null)
			return null;


		TerzoHome terzoHome = (TerzoHome)getHome(userContext,TerzoBulk.class);
		return terzoHome.findRif_modalita_pagamento(carriera.getTerzo());

	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bulk, ex);
	}catch (it.cnr.jada.persistency.IntrospectionException ex){
		throw handleException(bulk, ex);
	}
}
//^^@@
/** 
  *	Normale.
  *		PreCondition:
  * 		Richiesta di caricamento rate di una minicarriera
  *   	PostCondition:
  *  		Restituisce la lista delle rate
 */
//^^@@

public java.util.List findRate(UserContext aUC,MinicarrieraBulk carriera)
	throws 
		ComponentException,
		it.cnr.jada.persistency.PersistencyException,
		it.cnr.jada.persistency.IntrospectionException {
	
	if (carriera == null) return null;
	
	it.cnr.jada.bulk.BulkHome home = getHome(aUC, Minicarriera_rataBulk.class);
	
	it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "pg_minicarriera", sql.EQUALS, carriera.getPg_minicarriera());
	sql.addClause("AND", "cd_cds", sql.EQUALS, carriera.getCd_cds());
	sql.addClause("AND", "esercizio", sql.EQUALS, carriera.getEsercizio());
	sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, carriera.getCd_unita_organizzativa());
	sql.addOrderBy("PG_RATA");
	return home.fetchAll(sql);
}
/**
  * Percipiente selezionato
  *    PreCondition:
  *		 Viene richiesta la lista dei Termini di pagamento
  * 	 associati al terzo
  *	   PostCondition:
  *		 Viene restituita la lista dei Termini di pagamento
  * 	 associati al terzo
  *
  * Percipiente NON selezionato
  *    PreCondition:
  *		 Non è stato selezionato il terzo
  *	   PostCondition:
  *		 Non vengono caricati i termini di pagamento
**/
public java.util.Collection findTermini(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	try{
		MinicarrieraBulk carriera = (MinicarrieraBulk)bulk;
		if(carriera.getTerzo() == null ||
			carriera.getTerzo().getCd_terzo() == null)
			return null;

		TerzoHome terzoHome = (TerzoHome)getHome(userContext,TerzoBulk.class);
		return terzoHome.findRif_termini_pagamento(carriera.getTerzo());

	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bulk, ex);
	}catch (it.cnr.jada.persistency.IntrospectionException ex){
		throw handleException(bulk, ex);
	}

}
/**
  * Percipiente selezionato
  *    PreCondition:
  *		 Viene richiesta la lista dei Tipi di rapporto
  * 	del terzo selezionato
  *	   PostCondition:
  *		 Viene restituita la lista dei Tipi di rapporto
  * 	 del Percipiente selezionato
  *
  * Percipiente NON selezionato
  *    PreCondition:
  *		 Non è stato selezionato il Percipiente
  *	   PostCondition:
  *		 Non vengono caricati i tipi di rapporto
**/
public java.util.Collection findTipiRapporto(
	UserContext userContext,
	MinicarrieraBulk carriera) throws ComponentException{

	try{
		if (carriera.getTerzo() == null ||
			carriera.getTerzo().getCd_terzo() == null)
			return null;

		it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoHome home = (it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoHome)getHome(userContext, it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk.class);
		return home.findTipiRapporto(carriera.getPercipiente(), carriera.getDt_inizio_minicarriera());
		
	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(carriera, ex);
	}
}
/**
  * Tipo di rapporto selezionato
  *    PreCondition:
  *		 Viene richiesta la lista dei Tipi di Trattamento legati al
  * 	 tipo di rapporto selezionato
  *	   PostCondition:
  *		 Viene restituita la lista dei Tipi di Trattamento legati al
  * 	 tipo di rapporto selezionato
  *
  * Tipo di rapporto NON selezionato
  *    PreCondition:
  *		 Non è stato selezionato il tipo di rapporto
  *	   PostCondition:
  *		 Non vengono caricati i tipi trattamento
**/
public java.util.Collection findTipiTrattamento(UserContext userContext, MinicarrieraBulk carriera) 
	throws ComponentException{

	try{
		if (carriera.getTipo_rapporto() == null)
			return null;
		Tipo_trattamentoHome trattHome = (Tipo_trattamentoHome)getHome(userContext,Tipo_trattamentoBulk.class);

		Filtro_trattamentoBulk filtro = new Filtro_trattamentoBulk();
		filtro.setCdTipoRapporto(carriera.getCd_tipo_rapporto());
		filtro.setTipoAnagrafico(carriera.getTi_anagrafico());
		filtro.setDataValidita(carriera.getDt_registrazione());
		//filtro.setFlDiaria(Boolean.FALSE);
		filtro.setFlSenzaCalcoli(Boolean.FALSE);
		filtro.setFlDefaultCongualio(Boolean.FALSE);
		filtro.setTiIstituzionaleCommerciale(carriera.getTi_istituz_commerc());
		if (carriera.getFl_tassazione_separata() != null && carriera.getFl_tassazione_separata().booleanValue())
			filtro.setFlTassazioneSeparata(carriera.getFl_tassazione_separata());
		if (filtro.getCdTipoRapporto() != null && filtro.getCdTipoRapporto().equals("DIP"))
		{
			try {
				TerzoHome tHome = (TerzoHome) getHomeCache(userContext).getHome( TerzoBulk.class );
				TerzoBulk tKey = new TerzoBulk(carriera.getCd_terzo());
				TerzoBulk t = (TerzoBulk)tHome.findByPrimaryKey(tKey);
		 
				RapportoHome rHome = (RapportoHome) getHomeCache(userContext).getHome( RapportoBulk.class );
				java.util.Collection collRapp = rHome.findByCdAnagCdTipoRapporto(t.getCd_anag(),filtro.getCdTipoRapporto());
				boolean exit=false;
				for (java.util.Iterator i = collRapp.iterator();i.hasNext()&&!exit;) {
					RapportoBulk r = (RapportoBulk)i.next();
					exit=true;
					if (r.getCd_ente_prev_sti()==null)
						//throw new it.cnr.jada.comp.ApplicationException("Non è stato possibile recuperare l''Ente Previdenziale del dipendente selezionato.");
						//non blocco perchè potrebbero esserci trattamenti che non prevedono contributi previdenziali
						//quindi passo il codice fittizio 'XX'
						filtro.setEntePrev("XX");
					else
						filtro.setEntePrev(r.getCd_ente_prev_sti());
					if (r.getCd_rapp_impiego_sti()==null)
						throw new it.cnr.jada.comp.ApplicationException("Per il dipendente in esame non è definito un Rapporto di Impiego!");
					else
					{
						Ass_rapp_impiegoHome assHome = (Ass_rapp_impiegoHome) getHome(userContext, Ass_rapp_impiegoBulk.class );
						Ass_rapp_impiegoBulk assKey = new Ass_rapp_impiegoBulk(r.getCd_rapp_impiego_sti());
						Ass_rapp_impiegoBulk ass = (Ass_rapp_impiegoBulk)assHome.findByPrimaryKey(assKey);
						filtro.setTipoRappImpiego(ass.getTipo_rapp_impiego());
					}	
				}
			} catch (IntrospectionException e) {
					throw handleException(e);
			}
			//solo per il rapporto DIP aggiungo il filtro "Anno prec" a seconda della data di inizio competenza
			GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
			data_da.setTime(carriera.getDt_inizio_minicarriera());
			if (data_da.get(GregorianCalendar.YEAR)==(carriera.getEsercizio()-1))
				filtro.setFlAnnoPrec(new Boolean(true));
			else
				filtro.setFlAnnoPrec(new Boolean(false));
		}
		if (carriera.getDt_inizio_minicarriera()!=null && carriera.getDt_fine_minicarriera()!=null)
		{
			GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar data_a = (GregorianCalendar) GregorianCalendar.getInstance();
			
			data_da.setTime(carriera.getDt_inizio_minicarriera());
			data_a.setTime(carriera.getDt_fine_minicarriera());
			
			if (data_da.get(GregorianCalendar.YEAR) == data_a.get(GregorianCalendar.YEAR))
			{
				
				TerzoHome tHome = (TerzoHome) getHomeCache(userContext).getHome( TerzoBulk.class );
				TerzoBulk tKey = new TerzoBulk(carriera.getCd_terzo());
				TerzoBulk t = (TerzoBulk)tHome.findByPrimaryKey(tKey);
			 
				AnagraficoHome aHome = (AnagraficoHome) getHomeCache(userContext).getHome( AnagraficoBulk.class );
				AnagraficoBulk aKey = new AnagraficoBulk(t.getCd_anag());
				AnagraficoBulk a = (AnagraficoBulk)aHome.findByPrimaryKey(aKey);
				
				if (a.getFl_cervellone()&& 
					!(new Integer(data_da.get(GregorianCalendar.YEAR)).compareTo(a.getAnno_inizio_res_fis().intValue()) < 0) &&
					!(new Integer(data_da.get(GregorianCalendar.YEAR)).compareTo(a.getAnno_fine_agevolazioni().intValue()) > 0))
				{
					filtro.setFlAgevolazioniCervelli(new Boolean(a.getFl_cervellone()));
				}
				else
					filtro.setFlAgevolazioniCervelli(new Boolean(false));
			}
			else
				filtro.setFlAgevolazioniCervelli(new Boolean(false));
		}
	
		return trattHome.findTipiTrattamento(filtro);
		
	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(carriera, ex);
	}
}
//^^@@
/** 
  *   Validazione minicarriera.
  *	PreCondition:
  *		Viene richiesta la generazione delle rate di una minicarriera e la stessa non ha superato il metodo 'validate'.
  *	PostCondition:
  *		Non  viene consentita la generazione delle rate della minicarriera.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la generazione delle rate di una minicarriera e la stessa ha superato il metodo 'validate'.
  *	PostCondition:
  *		Viene consentita la generazione delle rate della minicarriera.
 */
//^^@@

public MinicarrieraBulk generaRate(
	UserContext userContext, 
	MinicarrieraBulk carriera,
	boolean eseguiCopia)
	throws it.cnr.jada.comp.ComponentException {
	
	try{
		carriera.validate();
		Long pgTmp = null;
		//eseguiCopia && 
		if (carriera.getPgMinicarrieraPerClone() == null){
			pgTmp = assegnaProgressivoTemporaneo(userContext, carriera);
			carriera.setPgMinicarrieraPerClone(pgTmp);
			//copiaMinicarriera(userContext, carriera);
		}

		if (carriera.getPg_minicarriera() == null){
			pgTmp = assegnaProgressivoTemporaneo(userContext, carriera);
			carriera.setPg_minicarriera(pgTmp);
			insertBulk(userContext, carriera);
		}else{
			updateBulk(userContext, carriera);
		}

		callGeneraRate(userContext, carriera);
		return reloadMinicarriera(userContext, carriera);

	} catch(it.cnr.jada.persistency.PersistencyException ex) {
		throw handleException(carriera,ex);
	} catch(it.cnr.jada.bulk.ValidationException ex) {
		throw handleException(carriera,ex);
	}
}
private it.cnr.jada.bulk.PrimaryKeyHashMap getCompensiTemporanei(
	it.cnr.jada.UserContext userContext,
	MinicarrieraBulk carriera) 
 	throws ComponentException {

	it.cnr.jada.bulk.PrimaryKeyHashMap compensiTemporanei = new it.cnr.jada.bulk.PrimaryKeyHashMap();
	try {
		for (java.util.Iterator i = carriera.getMinicarriera_rate().iterator(); i.hasNext();) {
			Minicarriera_rataBulk rata = (Minicarriera_rataBulk)i.next();
			if (rata.getCompenso() != null && rata.getCompenso().isTemporaneo())
				compensiTemporanei.put(rata.getCompenso(), null);
		}
		return compensiTemporanei;
	} catch (Throwable e) {
		throw handleException(e);
	}
}
/**
  * Gestione della validazione del terzo selezionato
  *	Ritorna una ComponentException con il messaggio realtivo all'errore
  *
  *	errorCode		Significato
  *	=========		===========	
  *		0			Tutto bene
  *		1			Terzo assente
  *		2			Terzo non valido alla data registrazione
  *		3			Controllo se ho inserito le modalità di pagamento
  *		4			Tipo rapporto assente
  *		5			Tipo di rapporto non valido in data inizio validità
  *		6			Tipo trattamento assente
  *		7			Tipo trattamento non valido alla data registrazione
  *
  *
**/
private void handleExceptionsPercipiente(int error) throws ComponentException{

	switch (error) {
		case 1: {
			throw new it.cnr.jada.comp.ApplicationException("Inserire il percipiente");		
		}case 2: {
			throw new it.cnr.jada.comp.ApplicationException("Il percipiente selezionato non è valido in Data Registrazione");
		}case 3: {
			throw new it.cnr.jada.comp.ApplicationException("Selezionare la Modalità di pagamento");
		}case 4: {
			throw new it.cnr.jada.comp.ApplicationException("Selezionare il Tipo Rapporto");
		}case 5: {
			throw new it.cnr.jada.comp.ApplicationException("Il Tipo Rapporto selezionato non è valido alla Data Inizio Validità");
		}case 6: {
			throw new it.cnr.jada.comp.ApplicationException("Selezionare il Tipo Trattamento");
		}case 7: {
			throw new it.cnr.jada.comp.ApplicationException("Il Tipo Trattamento selezionato non è valido alla Data Registrazione");
		} 
	}
}
	
/**
 * Prepara un OggettoBulk per la presentazione all'utente per una possibile
 * operazione di modifica.
 *
 * Pre-post-conditions:
 *
 * Nome: Oggetto non esistente
 * Pre: L'OggettoBulk specificato non esiste.
 * Post: Viene generata una CRUDException con la descrizione dell'errore.
 *
 * Nome: Tutti i controlli superati
 * Pre: L'OggettoBulk specificato esiste.
 * Post: Viene riletto l'OggettoBulk, inizializzato con tutti gli oggetti collegati e preparato
 *			per l'operazione di presentazione e modifica nell'interfaccia visuale.
 *			L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è
 *			ottenuto concatenando il nome della component con la stringa ".edit"
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk da preparare
 * @return	l'OggettoBulk preparato
 */	
public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,OggettoBulk bulk) throws ComponentException {

	MinicarrieraBulk carriera = (MinicarrieraBulk)super.inizializzaBulkPerInserimento(userContext, bulk);
	
	if (!verificaStatoEsercizio(userContext, new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk( carriera.getCd_cds(), ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio())))
         throw new it.cnr.jada.comp.ApplicationException("Impossibile inserire una minicarriera per un esercizio non aperto!");
	/*
	if (isGestitePrestazioni(userContext))
		carriera.impostaVisualizzaPrestazione();
	else
		carriera.setVisualizzaPrestazione(false);
    */
	carriera.impostaVisualizzaPrestazione();
	
	if (isGestitiIncarichi(userContext))
		carriera.impostaVisualizzaIncarico();
	else
		carriera.setVisualizzaIncarico(false);

	return carriera;
}
/**
 * Prepara un OggettoBulk per la presentazione all'utente per una possibile
 * operazione di modifica.
 *
 * Pre-post-conditions:
 *
 * Nome: Oggetto non esistente
 * Pre: L'OggettoBulk specificato non esiste.
 * Post: Viene generata una CRUDException con la descrizione dell'errore.
 *
 * Nome: Tutti i controlli superati
 * Pre: L'OggettoBulk specificato esiste.
 * Post: Viene riletto l'OggettoBulk, inizializzato con tutti gli oggetti collegati e preparato
 *			per l'operazione di presentazione e modifica nell'interfaccia visuale.
 *			L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è
 *			ottenuto concatenando il nome della component con la stringa ".edit"
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk da preparare
 * @return	l'OggettoBulk preparato
 */	
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws ComponentException {

	MinicarrieraBulk carriera = (MinicarrieraBulk)super.inizializzaBulkPerModifica(userContext, bulk);
	if (carriera.getEsercizio().intValue() > it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue())
		throw new it.cnr.jada.comp.ApplicationException("Il documento deve appartenere o all'esercizio di scrivania o ad esercizi precedenti per essere aperto in modifica!");

	try {
		lockBulk(userContext, carriera);
	} catch (Throwable t) {
		throw handleException(t);
	}

	try {
		BulkList rate = new BulkList(findRate(userContext, carriera));
		carriera.setMinicarriera_rate(rate);
		getHomeCache(userContext).fetchAll(userContext);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(carriera, e);
	} catch (it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(carriera, e);
	}

	completaMinicarriera(userContext, carriera);
/*
	if (isGestitePrestazioni(userContext))
		carriera.impostaVisualizzaPrestazione();
	else
		carriera.setVisualizzaPrestazione(false);
	
	if (isGestitiIncarichi(userContext))
		carriera.impostaVisualizzaIncarico();
	else
		carriera.setVisualizzaIncarico(false);
*/	
	if (carriera.getPgMinicarrieraPerClone() == null) {
		Long pgTmp = assegnaProgressivoTemporaneo(userContext, carriera);
		carriera.setPgMinicarrieraPerClone(pgTmp);
	}
	
//	copiaMinicarriera(userContext, carriera);
	carriera.resetDefferredSaldi();
	
	return carriera;
}
private PrimaryKeyHashMap inserisciMinicarriera(
	UserContext userContext,
	MinicarrieraBulk carriera) throws ComponentException{

	try {
		
		insertBulk(userContext, carriera);
		return aggiornaRate(userContext, carriera, true);
		
	} catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
private boolean isTipoRapportoValido(UserContext userContext, MinicarrieraBulk minicarriera) throws ComponentException {

	try{

		Tipo_rapportoHome home = (Tipo_rapportoHome)getHome(userContext, Tipo_rapportoBulk.class);
		return home.isTipoRapportoValido(minicarriera.getPercipiente(), minicarriera.getCd_tipo_rapporto(), minicarriera.getDt_inizio_minicarriera());

	}catch(java.sql.SQLException ex){
		throw handleException(ex);
	}
}
private boolean isTipoTrattamentoValido(UserContext userContext, MinicarrieraBulk minicarriera) throws ComponentException{

	try{

		Tipo_trattamentoHome home = (Tipo_trattamentoHome)getHome(userContext, Tipo_trattamentoBulk.class);
		Filtro_trattamentoBulk filtro = new Filtro_trattamentoBulk();
		filtro.setCdTipoRapporto(minicarriera.getCd_tipo_rapporto());
		filtro.setCdTipoTrattamento(minicarriera.getCd_trattamento());
		filtro.setTipoAnagrafico(minicarriera.getTi_anagrafico());
		filtro.setDataValidita(minicarriera.getDt_registrazione());
		//filtro.setFlDiaria(Boolean.FALSE);
		filtro.setFlSenzaCalcoli(Boolean.FALSE);
		filtro.setFlDefaultCongualio(Boolean.FALSE);
		filtro.setTiIstituzionaleCommerciale(minicarriera.getTi_istituz_commerc());
		if (minicarriera.getFl_tassazione_separata() != null && 
			minicarriera.getFl_tassazione_separata().booleanValue())
			filtro.setFlTassazioneSeparata(minicarriera.getFl_tassazione_separata());
		if (filtro.getCdTipoRapporto() != null && filtro.getCdTipoRapporto().equals("DIP"))
		{
			try {
					TerzoHome tHome = (TerzoHome) getHomeCache(userContext).getHome( TerzoBulk.class );
					TerzoBulk tKey = new TerzoBulk(minicarriera.getCd_terzo());
					TerzoBulk t = (TerzoBulk)tHome.findByPrimaryKey(tKey);
			 
					RapportoHome rHome = (RapportoHome) getHomeCache(userContext).getHome( RapportoBulk.class );
					java.util.Collection collRapp = rHome.findByCdAnagCdTipoRapporto(t.getCd_anag(),filtro.getCdTipoRapporto());
					boolean exit=false;
					for (java.util.Iterator i = collRapp.iterator();i.hasNext()&&!exit;) {
						RapportoBulk r = (RapportoBulk)i.next();
						exit=true;
						if (r.getCd_ente_prev_sti()==null)
							//throw new it.cnr.jada.comp.ApplicationException("Non è stato possibile recuperare l''Ente Previdenziale del dipendente selezionato.");
							//non blocco perchè potrebbero esserci trattamenti che non prevedono contributi previdenziali
							//quindi passo il codice fittizio 'XX'
							filtro.setEntePrev("XX");
						else
							filtro.setEntePrev(r.getCd_ente_prev_sti());
						if (r.getCd_rapp_impiego_sti()==null)
							throw new it.cnr.jada.comp.ApplicationException("Per il dipendente in esame non è definito un Rapporto di Impiego!");
						else
						{
							Ass_rapp_impiegoHome assHome = (Ass_rapp_impiegoHome) getHome(userContext, Ass_rapp_impiegoBulk.class );
							Ass_rapp_impiegoBulk assKey = new Ass_rapp_impiegoBulk(r.getCd_rapp_impiego_sti());
							Ass_rapp_impiegoBulk ass = (Ass_rapp_impiegoBulk)assHome.findByPrimaryKey(assKey);
							filtro.setTipoRappImpiego(ass.getTipo_rapp_impiego());
						}	
					}
			} catch (IntrospectionException e) {
					throw handleException(e);
			}
			//solo per il rapporto DIP aggiungo il filtro "Anno prec" a seconda della data di inizio competenza
			GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
			data_da.setTime(minicarriera.getDt_inizio_minicarriera());
			if (data_da.get(GregorianCalendar.YEAR)==(minicarriera.getEsercizio()-1))
				filtro.setFlAnnoPrec(new Boolean(true));
			else
				filtro.setFlAnnoPrec(new Boolean(false));
		}
		if (minicarriera.getDt_inizio_minicarriera()!=null && minicarriera.getDt_fine_minicarriera()!=null)
		{
			GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar data_a = (GregorianCalendar) GregorianCalendar.getInstance();
			
			data_da.setTime(minicarriera.getDt_inizio_minicarriera());
			data_a.setTime(minicarriera.getDt_fine_minicarriera());
			
			if (data_da.get(GregorianCalendar.YEAR) == data_a.get(GregorianCalendar.YEAR))
			{
				
				TerzoHome tHome = (TerzoHome) getHomeCache(userContext).getHome( TerzoBulk.class );
				TerzoBulk tKey = new TerzoBulk(minicarriera.getCd_terzo());
				TerzoBulk t = (TerzoBulk)tHome.findByPrimaryKey(tKey);
			 
				AnagraficoHome aHome = (AnagraficoHome) getHomeCache(userContext).getHome( AnagraficoBulk.class );
				AnagraficoBulk aKey = new AnagraficoBulk(t.getCd_anag());
				AnagraficoBulk a = (AnagraficoBulk)aHome.findByPrimaryKey(aKey);
				
				if (a.getFl_cervellone()&& 
					!(new Integer(data_da.get(GregorianCalendar.YEAR)).compareTo(a.getAnno_inizio_res_fis().intValue()) < 0) &&
					!(new Integer(data_da.get(GregorianCalendar.YEAR)).compareTo(a.getAnno_fine_agevolazioni().intValue()) > 0))
				{
					filtro.setFlAgevolazioniCervelli(new Boolean(a.getFl_cervellone()));
				}
				else
					filtro.setFlAgevolazioniCervelli(new Boolean(false));
			}
			else
				filtro.setFlAgevolazioniCervelli(new Boolean(false));
		}

		return home.isTipoTrattamentoValido(filtro);

	}catch(java.sql.SQLException ex){
		throw handleException(ex);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
private V_terzo_per_compensoBulk loadPercipiente(UserContext userContext, MinicarrieraBulk carriera) throws ComponentException {

	try {

		V_terzo_per_compensoHome home = (V_terzo_per_compensoHome)getHome(userContext, V_terzo_per_compensoBulk.class, "DISTINCT_TERZO");
		return home.loadVTerzo(userContext,
						carriera.getTi_anagrafico(), 
						carriera.getCd_terzo(),
						carriera.getDt_registrazione(),
						carriera.getDt_inizio_minicarriera());
		
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (22/02/2002 14.28.44)
 * @return java.util.List
 */
private void loadTipoTrattamento(UserContext userContext, MinicarrieraBulk carriera) throws ComponentException {

	try {

		Tipo_trattamentoHome trattHome = (Tipo_trattamentoHome)getHome(userContext, Tipo_trattamentoBulk.class);
		if (carriera.getTipo_trattamento() == null) {
			String cod = carriera.getCd_trattamento();
			Tipo_trattamentoBulk tt = new Tipo_trattamentoBulk();
			tt.setCd_trattamento(cod);
			carriera.setTipo_trattamento(tt);
		}
		
		Filtro_trattamentoBulk filtro = new Filtro_trattamentoBulk();
		filtro.setCdTipoTrattamento(carriera.getCd_trattamento());
		filtro.setTipoAnagrafico(carriera.getTi_anagrafico());
		filtro.setDataValidita(carriera.getDt_registrazione());
		//filtro.setFlDiaria(Boolean.FALSE);
		filtro.setFlSenzaCalcoli(Boolean.FALSE);
		filtro.setFlDefaultCongualio(Boolean.FALSE);
		filtro.setTiIstituzionaleCommerciale(carriera.getTi_istituz_commerc());
		if (carriera.getFl_tassazione_separata() != null && carriera.getFl_tassazione_separata().booleanValue())
			filtro.setFlTassazioneSeparata(carriera.getFl_tassazione_separata());
		if (filtro.getCdTipoRapporto() != null && filtro.getCdTipoRapporto().equals("DIP"))
		{
			try {
					TerzoHome tHome = (TerzoHome) getHomeCache(userContext).getHome( TerzoBulk.class );
					TerzoBulk tKey = new TerzoBulk(carriera.getCd_terzo());
					TerzoBulk t = (TerzoBulk)tHome.findByPrimaryKey(tKey);
			 
					RapportoHome rHome = (RapportoHome) getHomeCache(userContext).getHome( RapportoBulk.class );
					java.util.Collection collRapp = rHome.findByCdAnagCdTipoRapporto(t.getCd_anag(),filtro.getCdTipoRapporto());
					boolean exit=false;
					for (java.util.Iterator i = collRapp.iterator();i.hasNext()&&!exit;) {
						RapportoBulk r = (RapportoBulk)i.next();
						exit=true;
						if (r.getCd_ente_prev_sti()==null)
							//throw new it.cnr.jada.comp.ApplicationException("Non è stato possibile recuperare l''Ente Previdenziale del dipendente selezionato.");
							//non blocco perchè potrebbero esserci trattamenti che non prevedono contributi previdenziali
							//quindi passo il codice fittizio 'XX'
							filtro.setEntePrev("XX");
						else
							filtro.setEntePrev(r.getCd_ente_prev_sti());
						if (r.getCd_rapp_impiego_sti()==null)
							throw new it.cnr.jada.comp.ApplicationException("Per il dipendente in esame non è definito un Rapporto di Impiego!");
						else
						{
							Ass_rapp_impiegoHome assHome = (Ass_rapp_impiegoHome) getHome(userContext, Ass_rapp_impiegoBulk.class );
							Ass_rapp_impiegoBulk assKey = new Ass_rapp_impiegoBulk(r.getCd_rapp_impiego_sti());
							Ass_rapp_impiegoBulk ass = (Ass_rapp_impiegoBulk)assHome.findByPrimaryKey(assKey);
							filtro.setTipoRappImpiego(ass.getTipo_rapp_impiego());
						}	
					}
			} catch (IntrospectionException e) {
						throw handleException(e);
			}
			//solo per il rapporto DIP aggiungo il filtro "Anno prec" a seconda della data di inizio competenza
			GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
			data_da.setTime(carriera.getDt_inizio_minicarriera());
			if (data_da.get(GregorianCalendar.YEAR)==(carriera.getEsercizio()-1))
				filtro.setFlAnnoPrec(new Boolean(true));
			else
				filtro.setFlAnnoPrec(new Boolean(false));
		}
		if (carriera.getDt_inizio_minicarriera()!=null && carriera.getDt_fine_minicarriera()!=null)
		{
			GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar data_a = (GregorianCalendar) GregorianCalendar.getInstance();
			
			data_da.setTime(carriera.getDt_inizio_minicarriera());
			data_a.setTime(carriera.getDt_fine_minicarriera());
			
			if (data_da.get(GregorianCalendar.YEAR) == data_a.get(GregorianCalendar.YEAR))
			{
				
				TerzoHome tHome = (TerzoHome) getHomeCache(userContext).getHome( TerzoBulk.class );
				TerzoBulk tKey = new TerzoBulk(carriera.getCd_terzo());
				TerzoBulk t = (TerzoBulk)tHome.findByPrimaryKey(tKey);
			 
				AnagraficoHome aHome = (AnagraficoHome) getHomeCache(userContext).getHome( AnagraficoBulk.class );
				AnagraficoBulk aKey = new AnagraficoBulk(t.getCd_anag());
				AnagraficoBulk a = (AnagraficoBulk)aHome.findByPrimaryKey(aKey);
				
				if (a.getFl_cervellone()&& 
					!(new Integer(data_da.get(GregorianCalendar.YEAR)).compareTo(a.getAnno_inizio_res_fis().intValue()) < 0) &&
					!(new Integer(data_da.get(GregorianCalendar.YEAR)).compareTo(a.getAnno_fine_agevolazioni().intValue()) > 0))
				{
					filtro.setFlAgevolazioniCervelli(new Boolean(a.getFl_cervellone()));
				}
				else
					filtro.setFlAgevolazioniCervelli(new Boolean(false));
			}
			else
				filtro.setFlAgevolazioniCervelli(new Boolean(false));
		}
		carriera.setTipo_trattamento(trattHome.findTipoTrattamentoValido(filtro));
		
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Viene richiesta la modifica del compenso
  *    PostCondition:
  *      Viene consentito la modifica del compenso e 
  * 	 della obbligazione associata
  *
  *  Controlli non superati
  *    PreCondition:
  *      Non sono stati superati i controlli di validazione 
  * 	 per la modifica del compenso
  *    PostCondition:
  *      Non viene permessa la modifica del compenso e della obbligazione
**/
public OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	return modificaConBulk(userContext, bulk, null);
}
//^^@@
/** 
  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta la modifica di una minicarriera e la stessa non ha superato il metodo 'validaMinicarriera'.
  *	PostCondition:
  *		Non  viene consentita la modifica della minicarriera.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la modifica di una minicarriera e la stessa ha superato il metodo 'validaMinicarriera'.
  *	PostCondition:
  *		Viene consentita la modifica della minicarriera.
 */
//^^@@

public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status) throws it.cnr.jada.comp.ComponentException {

	MinicarrieraBulk carriera = (MinicarrieraBulk)bulk;

	Long tmp = carriera.getPgMinicarrieraPerClone();
	Long current = carriera.getPg_minicarriera();

	validaMinicarriera(userContext, carriera);

	PrimaryKeyHashMap compensiTemporanei = aggiornaRate(userContext, carriera, false);

	carriera = (MinicarrieraBulk)super.modificaConBulk(userContext, carriera);
	
	callValidaRate(userContext, carriera);

	if (tmp!=null){
		eliminaMinicarrieraClone(userContext, carriera, tmp);
		carriera.setPg_minicarriera(current);
	}

	aggiornaMontantiPerCompensiTemporanei(userContext, compensiTemporanei);
	
	aggiornaSaldiPer(userContext, carriera, status);

	return carriera;
}
/**
 * Insert the method's description here.
 * Creation date: (22/02/2002 14.28.44)
 * @return java.util.List
 */
private MinicarrieraBulk reloadMinicarriera(UserContext userContext, MinicarrieraBulk carriera) throws ComponentException{

	try {
		Long pgTmp = carriera.getPgMinicarrieraPerClone();
		it.cnr.jada.bulk.PrimaryKeyHashMap saldi = carriera.getDefferredSaldi();
		
		MinicarrieraHome home = (MinicarrieraHome)getHome(userContext, carriera);
		MinicarrieraBulk carrieraCaricata = (MinicarrieraBulk)home.findByPrimaryKey(carriera);

		try {
			BulkList rate = new BulkList(findRate(userContext, carrieraCaricata));
			for (java.util.Iterator i = carriera.getMinicarriera_rate().iterator(); i.hasNext();) {
				Minicarriera_rataBulk vecchiaRata = (Minicarriera_rataBulk)i.next();
				if (vecchiaRata.isAssociataACompenso()) {
					Minicarriera_rataBulk rataCaricata = (Minicarriera_rataBulk)rate.get(rate.indexOfByPrimaryKey(vecchiaRata));
					rataCaricata.setCompenso(vecchiaRata.getCompenso());
				}
			}
			carrieraCaricata.setMinicarriera_rate(rate);
		} catch (it.cnr.jada.persistency.IntrospectionException e) {
			throw handleException(carriera, e);
		}

		getHomeCache(userContext).fetchAll(userContext);

		carrieraCaricata.setPgMinicarrieraPerClone(pgTmp);
		completaMinicarriera(userContext, carrieraCaricata);
		carrieraCaricata.setDefferredSaldi(saldi);
		
		return carrieraCaricata;

	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}	
}
//^^@@
/** 
  *  Minicarriera corrente
  *		PreCondition:
  *			Viene richiesto il rinnovo della minicarriera
  *		PostCondition:
  *			La minicarriera corrente viene aggiornata e diventa "non modificabile"
  *  Rinnovo
  *		PreCondition:
  *			La minicarriera di origine viene aggiornata correttamente
  *		PostCondition:
  *			Viene eseguita e restituita una copia della testata della minicarriera di origine
  *  Rate
  *		PreCondition:
  *			La minicarriera di origine viene aggiornata correttamente
  *		PostCondition:
  *			NON viene eseguita la copia delle rate, ma ne viene richiesta la generazione
 */
//^^@@

public MinicarrieraBulk rinnova(
	UserContext userContext, 
	MinicarrieraBulk carriera)
	throws it.cnr.jada.comp.ComponentException {


	carriera.setStato(carriera.STATO_RINNOVATA);
	carriera.setToBeUpdated();

	carriera = (MinicarrieraBulk)modificaConBulk(userContext, carriera);

	MinicarrieraBulk carrieraRinnovata = copiaMinicarriera(userContext, carriera);
					
	return reloadMinicarriera(userContext, carrieraRinnovata);
}
//^^@@
/** 
  *  Minicarriera corrente
  *		PreCondition:
  *			Viene richiesto il ripristino della minicarriera
  *		PostCondition:
  *			La minicarriera corrente viene aggiornata e diventa "non modificabile"
  *  Ripristino
  *		PreCondition:
  *			La minicarriera di origine viene aggiornata correttamente
  *		PostCondition:
  *			Viene eseguita e restituita una copia della testata della minicarriera di origine
  *  Rate
  *		PreCondition:
  *			La minicarriera di origine viene aggiornata correttamente
  *		PostCondition:
  *			NON viene eseguita la copia delle rate, ma ne viene richiesta la generazione
 */
//^^@@

public MinicarrieraBulk ripristina(
	UserContext userContext, 
	MinicarrieraBulk carriera)
	throws it.cnr.jada.comp.ComponentException {
	
	carriera.setStato(carriera.STATO_RIPRISTINATA);
	carriera.setToBeUpdated();

	carriera = (MinicarrieraBulk)modificaConBulk(userContext, carriera);

	MinicarrieraBulk carrieraRipristinata = copiaMinicarriera(userContext, carriera);
					
	return reloadMinicarriera(userContext, carrieraRipristinata);
}
/**
  * Filtro ricerca Compenso
  *   PreCondition:
  *     E' stata generata la richiesta di ricerca di un Compenso
  *   PostCondition:
  *	    Viene restituito l'SQLBuilder con l'elenco delle clausole 
  *		selezionate dall'utente e, in aggiunta:
  * 	  - CDS di scrivania
  * 	  - UO di scrivania
  * 	  - ESERCIZIO di scrivania
**/
public Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {

	MinicarrieraBulk carriera = (MinicarrieraBulk)bulk;
	MinicarrieraHome home = (MinicarrieraHome)getHome(userContext,carriera);
	SQLBuilder sql = home.createSQLBuilder();

	if (clauses == null) {
		if (bulk != null)
			clauses = bulk.buildFindClauses(null);
	} else
		clauses = it.cnr.jada.persistency.sql.CompoundFindClause.and(clauses,bulk.buildFindClauses(Boolean.FALSE));

	for (java.util.Iterator i = clauses.iterator(); i.hasNext();) {
		SimpleFindClause clause = (SimpleFindClause)i.next();
		if (clause.getPropertyName().equalsIgnoreCase("ti_anagraficoForSearch")) {
			clause.setPropertyName("ti_anagrafico");
			if (clause.getValue().equals("T")) clause.setValue(null);
		} else if (clause.getPropertyName().equalsIgnoreCase("percipiente.cd_terzo")) {
			clause.setPropertyName("cd_terzo");
		}
	}
	
	//sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, carriera.getCd_cds());
	//sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, carriera.getCd_unita_organizzativa());
	//sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, carriera.getEsercizio());
	if (carriera.getPercipiente() != null &&
		carriera.getPercipiente().getCd_terzo_precedente() != null) {
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("MINICARRIERA.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLClause("AND", "TERZO.CD_PRECEDENTE", sql.EQUALS, carriera.getPercipiente().getCd_terzo_precedente());
	}
	sql.addClause(clauses);

	return sql;
}
/**
  * Filtro ricerca per le banche
  *   PreCondition:
  *     E' stata generata la richiesta di ricerca delle banche
  *   PostCondition:
  *	    Viene restituito l'SQLBuilder per filtrare le banche
  * 	associate al TERZO selezionato e al TIPO PAGAMENTO selezionato
**/
public SQLBuilder selectBancaByClause(UserContext userContext, MinicarrieraBulk carriera, BancaBulk banca, CompoundFindClause clauses) throws ComponentException {

	BancaHome bancaHome = (BancaHome)getHome(userContext, BancaBulk.class);

	SQLBuilder sql = bancaHome.selectBancaFor(
							carriera.getModalita_pagamento(),
							carriera.getCd_terzo());
	sql.addClause(clauses);

	return sql;
}
/**
  * Filtro ricerca per il terzo
  *   PreCondition:
  *     E' stata generata la richiesta di ricerca di un terzo
  *   PostCondition:
  *	    Viene restituito l'SQLBuilder per filtrare i terzi validi
  * 	e con TIPO ANAGRAFICO compatibile con quello selezionato
**/
public SQLBuilder selectPercipienteByClause(
	UserContext userContext,
	MinicarrieraBulk carriera,
	V_terzo_per_compensoBulk vTerzo, 
	CompoundFindClause clauses) throws ComponentException {

	try {
		carriera.validaDateValidita();
	
		V_terzo_per_compensoHome home = (V_terzo_per_compensoHome)getHome(userContext,V_terzo_per_compensoBulk.class,"DISTINCT_TERZO");
		SQLBuilder sql = home.selectVTerzo(
								carriera.getTi_anagrafico(), 
								null,
								carriera.getDt_registrazione(),
								carriera.getDt_inizio_minicarriera(),
								clauses);
		sql.addSQLClause("AND", "V_TERZO_PER_COMPENSO.CD_TERZO_PRECEDENTE", sql.EQUALS, carriera.getPercipiente().getCd_terzo_precedente());
		sql.addSQLClause("AND", "V_TERZO_PER_COMPENSO.COGNOME", sql.CONTAINS, carriera.getCognome());
		sql.addSQLClause("AND", "V_TERZO_PER_COMPENSO.NOME", sql.CONTAINS, carriera.getNome());
		sql.addSQLClause("AND", "V_TERZO_PER_COMPENSO.RAGIONE_SOCIALE", sql.CONTAINS, carriera.getRagione_sociale());
		sql.addSQLClause("AND", "V_TERZO_PER_COMPENSO.CODICE_FISCALE", sql.CONTAINS, carriera.getCodice_fiscale());
		sql.addSQLClause("AND", "V_TERZO_PER_COMPENSO.PARTITA_IVA", sql.CONTAINS, carriera.getPartita_iva());
		return sql;

	} catch (it.cnr.jada.bulk.ValidationException e) {
		throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
	} catch(it.cnr.jada.persistency.PersistencyException ex) {
		throw handleException(ex);
	}
}
//^^@@
/** 
  *  Sospensione
  *		PreCondition:
  *			Viene richiesta la sospensione della minicarriera
  *		PostCondition:
  *			Viene eseguita la richiesta e restituita la minicarriera aggiornata non più modificabile
 */
//^^@@

public MinicarrieraBulk sospendi(
	UserContext userContext, 
	MinicarrieraBulk carriera)
	throws it.cnr.jada.comp.ComponentException {

	carriera.setStato(carriera.STATO_SOSPESA);
	carriera.setToBeUpdated();

	return (MinicarrieraBulk)modificaConBulk(userContext, carriera);
}
private void synchronizeCarriera(
	it.cnr.jada.UserContext uc,
	MinicarrieraBulk carriera,
	CompensoBulk compenso) {

	ObbligazioneBulk obbligazione = compenso.getObbligazioneScadenzario().getObbligazione();
	PrimaryKeyHashMap relazioni = compenso.getRelationsDocContForSaldi();
	if (relazioni != null) {
		Long temporaneo = (Long)relazioni.get(obbligazione);
		if (temporaneo != null && temporaneo.longValue() < 0 && relazioni != null && !relazioni.isEmpty()) {
			ObbligazioneBulk obblTemporanea = (ObbligazioneBulk)obbligazione.clone();
			obblTemporanea.setPg_obbligazione(temporaneo);
			if (carriera.getDefferredSaldi() != null) {
				java.util.Map values = (java.util.Map)carriera.getDefferredSaldi().get(obblTemporanea);
				carriera.removeFromDefferredSaldi(obblTemporanea);
				if (values == null)
					values = new java.util.HashMap();
				values.put("isObbTemp", Boolean.TRUE);
				carriera.addToDefferredSaldi(obbligazione, values);
			}
		}
	}
}
//^^@@
/** 
  *   Validazione documento.
  *		PreCondition:
  *			Viene richiesta la validazione di una minicarriera e la stessa viene superata.
  *		PostCondition:
  *			Viene consentita la continuazione del processo.
  *   La data di registrazione non è stata specificata
  *		PreCondition:
  *			Nella minicarriera non è stata specificata la data di registrazione.
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesta l'immissione del valore.
  *   La data di registrazione
  *		PreCondition:
  *			Nella minicarriera è stata specificata la data di registrazione ma è antecedente a 'oggi'
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesto l'immissione del valore corretto.
  *   La data di inizio e fine validità
  *		PreCondition:
  *			Nella minicarriera non è stata specificata la data di inizio o fine validità
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesta l'immissione del valore.
  *   Validità delle date di inizio e fine validità
  *		PreCondition:
  *			Nella minicarriera data di fine validità è precedente alla data di inizio validità
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesta l'immissione del valore corretto.
  *   Percipiente
  *		PreCondition:
  *			Nella minicarriera non è stato specificato il percipiente
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesto l'immissione del valore.
  *   Modalità pagamento
  *		PreCondition:
  *			Nella minicarriera non è stato specificato il valore modalità di pagamento del percipiente
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesto l'immissione del valore.
  *   Tipo rapporto
  *		PreCondition:
  *			Nella minicarriera non è stato specificato il valore tipo rapporto del percipiente
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesto l'immissione del valore.
  *   Tipo trattamento
  *		PreCondition:
  *			Nella minicarriera non è stato specificato il valore tipo trattamento del percipiente
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesto l'immissione del valore.
  *   Rate
  *		PreCondition:
  *			Nella minicarriera non sono state aggiunte o generate rate
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesto l'immissione di almeno una rata
  *   Importo totale minicarriera
  *		PreCondition:
  *			Nella minicarriera non è stato specificato l'importo totale del documento
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesto l'immissione del valore.
  *   Importo totale minicarriera non valido
  *		PreCondition:
  *			Nella minicarriera è stato specificato l'importo totale del documento, ma è minore o uguale a 0
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesto l'immissione del valore corretto.
  *   Importo totale rate non valido
  *		PreCondition:
  *			Nella minicarriera sono state inserite rate con importo complessivo diverso dall'importo totale di documento
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesta la correzione delle rate o dell'importo totale di testata.
  *   N. rate minicarriera
  *		PreCondition:
  *			Nella minicarriera non è stato specificato il numero delle rate del documento
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesto l'immissione del valore.
  *   N. rate minicarriera non valido
  *		PreCondition:
  *			Nella minicarriera è stato specificato il numero delle rate del documento, ma è minore o uguale a 0
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesto l'immissione del valore corretto.
  *   Numero totale rate non valido
  *		PreCondition:
  *			Nella minicarriera sono state inserite rate in eccesso o difetto rispetto al numero rate di testata
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesta la correzione delle rate o dell'numero totale di testata
  *   N. mesi di anticipo/posticipo
  *		PreCondition:
  *			Nella minicarriera è stato specificato il tipo anticipo/posticipo per le rate, ma non è stato specificato
  *			il numero di mesi
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesto l'immissione del valore.
  *   Validità n. mesi di anticipo/posticipo
  *		PreCondition:
  *			Nella minicarriera è stato specificato il tipo anticipo/posticipo per le rate, ma non è stato specificato
  *			il numero di mesi valido (minore o uguale a 0)
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesto l'immissione del valore corretto.
  *   Validità delle rate immesse
  *		PreCondition:
  *			La stored procedure per la validazione degli intervalli di tempo relativa alle rate fallisce
  *		PostCondition:
  *			Viene restituito messaggio relativo e richiesto la correzione.
 */
//^^@@

private void validaMinicarriera(
	UserContext userContext,
	MinicarrieraBulk carriera) throws ComponentException{

	// Controllo Testata Minicarriera e percipiente
	try {
		carriera.validaTestata();
		handleExceptionsPercipiente(validaPercipiente(userContext, carriera));
		carriera.validaCorpo();

		if (carriera.getDt_cessazione()== null)
		{
			//if(isGestitePrestazioni(userContext))
			//{
				carriera.impostaVisualizzaPrestazione();
				if(carriera.isVisualizzaPrestazione() && (carriera.getTipoPrestazioneCompenso() == null || carriera.getTipoPrestazioneCompenso().getCrudStatus()== OggettoBulk.UNDEFINED))
					throw new it.cnr.jada.comp.ApplicationException("Inserire il tipo di prestazione.");
			//}
			if(isGestitiIncarichi(userContext))
			{
				carriera.impostaVisualizzaIncarico();
				if(carriera.isVisualizzaIncarico() && (carriera.getIncarichi_repertorio() == null || carriera.getIncarichi_repertorio().getCrudStatus()== OggettoBulk.UNDEFINED))
					throw new it.cnr.jada.comp.ApplicationException("Inserire il Contratto.");
			}	
		}
		
		if (carriera.getMinicarriera_rate().size() <= 0)
			throw new it.cnr.jada.comp.ApplicationException("Per continuare è necessario generare le rate!");
		
		if (carriera.getIm_totale_minicarriera().compareTo(carriera.calcolaTotaleRate()) != 0)
			throw new it.cnr.jada.comp.ApplicationException("L'importo totale delle rate deve essere uguale all'importo specificato nella minicarriera.");
		
		if (carriera.getNumero_rate().intValue() != carriera.getMinicarriera_rate().size())
			throw new it.cnr.jada.comp.ApplicationException("Il numero totale delle rate deve essere uguale al numero rate specificate nella minicarriera.");

	} catch (it.cnr.jada.bulk.ValidationException e) {
		throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
	} catch (Throwable t) {
		throw handleException(carriera, t);
	}
}
/**
  * Viene richiesta la validazione del terzo selezionato
  *	Ritorna il codice di Errore relativo alla validzione
  *
  *	errorCode		Significato
  *	=========		===========	
  *		0			Tutto bene
  *		1			Terzo assente
  *		2			Terzo non valido alla data registrazione
  *		3			Controllo se ho inserito le modalità di pagamento
  *		4			Tipo rapporto assente
  *		5			Tipo di rapporto non valido in data inizio competenza coge
  *		6			Tipo trattamento assente
  *		7			Tipo trattamento non valido alla data registrazione
  *
  * Pre-post-conditions
  *
  * Nome: Terzo assente
  *	Pre: Non è stato selezionato un terzo
  *	Post: Ritorna il valore 1
  *
  * Nome: Terzo non valido alla data registrazione
  *	Pre: Il terzo selezionato non è valido alla data registrazione
  *	Post: Ritorna il valore 2
  *
  * Nome: Modalita di pagamento assente
  *	Pre: Non è stato selezionata una modalita di pagamento
  *	Post: Ritorna il valore 3
  *
  * Nome: Tipo rapporto assente
  *	Pre: Non è stato selezionato un tipo rapporto
  *	Post: Ritorna il valore 4
  *
  * Nome: Tipo rapporto non valido alla data inizio validità
  *	Pre: Il tipo rapporto selezionato non è valido in data inizio validità
  *	Post: Ritorna il valore 5
  *
  * Nome: Tipo trattamento assente
  *	Pre: Non è stato selezionato un tipo trattamento
  *	Post: Ritorna il valore 6
  *
  * Nome: Tipo trattamento non valido alla data registrazione
  *	Pre: Non è stato selezionato un tipo trattamento
  *	Post: Ritorna il valore 7
  *
  * Nome: Terzo valido
  *	Pre: Il terzo selezionato non ha errori
  *	Post: Ritorna il valore 0
  *
  * @param	userContext		lo UserContext che genera la richiesta
  * @param	minicarriera	il minicarriera di cui validare il terzo
  * @return	il codice di errore relativo
  *
 **/
public int validaPercipiente(
	UserContext userContext, 
	MinicarrieraBulk minicarriera) 
	throws ComponentException{

	TerzoBulk percipiente = minicarriera.getTerzo();

	int returnCode = V_terzo_per_compensoBulk.TUTTO_BENE;
	try {
		returnCode = minicarriera.validaPercipiente(false);
	} catch (it.cnr.jada.bulk.ValidationException e) {
		throw new it.cnr.jada.comp.ApplicationException(e.getMessage(), e);
	}
	if (returnCode != V_terzo_per_compensoBulk.TUTTO_BENE)
		return returnCode;

	// rapporto non valido in data inizio validità
	if (!isTipoRapportoValido(userContext, minicarriera))
		return V_terzo_per_compensoBulk.TIPO_RAPP_NON_VALIDO;

	// tipo trattamento non valido alla data registrazione
	if (!isTipoTrattamentoValido(userContext, minicarriera))
		return V_terzo_per_compensoBulk.TIPO_TRATT_NON_VALIDO;

	return(0);
}
public boolean verificaStatoEsercizio(UserContext userContext,it.cnr.contab.config00.esercizio.bulk.EsercizioBulk anEsercizio) throws ComponentException 
{
	try 
	{
		it.cnr.contab.config00.esercizio.bulk.EsercizioHome eHome = (it.cnr.contab.config00.esercizio.bulk.EsercizioHome)getHome(userContext, it.cnr.contab.config00.esercizio.bulk.EsercizioBulk.class);
		
		return !eHome.isEsercizioChiuso(userContext, anEsercizio.getEsercizio(), anEsercizio.getCd_cds());
	} 
	catch (it.cnr.jada.persistency.PersistencyException e) 
	{
		throw handleException(e);
	}
}
public boolean isTerzoCervellone(UserContext userContext, MinicarrieraBulk carriera) throws ComponentException
{
	if (carriera.getCd_terzo()!= null)
	{
		try {
			TerzoHome tHome = (TerzoHome) getHome(userContext,TerzoBulk.class);
			TerzoBulk tKey = new TerzoBulk(carriera.getCd_terzo());
			TerzoBulk t = (TerzoBulk)tHome.findByPrimaryKey(tKey);
			if (t == null)
			{
				return false;
			}
			else
			{
				AnagraficoHome aHome = (AnagraficoHome) getHome(userContext,AnagraficoBulk.class);
				AnagraficoBulk aKey = new AnagraficoBulk(t.getCd_anag());
				AnagraficoBulk a = (AnagraficoBulk)aHome.findByPrimaryKey(aKey);
			
				if (a.getFl_cervellone())
					return true;
				else
					return false;
			}	
			}catch(it.cnr.jada.persistency.PersistencyException ex){
				throw handleException(carriera, ex);
			}
	}
	else
		return false;
}
public boolean isGestitiIncarichi(UserContext userContext) throws ComponentException {
	try {
		Parametri_cnrBulk par = ((Parametri_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession",Parametri_cnrComponentSession.class)).getParametriCnr(userContext,CNRUserContext.getEsercizio(userContext));
		return 
		    par.getFl_incarico();
	} catch(Throwable e) {
		throw handleException(e);
	}
}
public SQLBuilder selectIncarichi_repertorioByClause(UserContext userContext, MinicarrieraBulk carriera, Incarichi_repertorioBulk incarico, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	if (carriera.getCd_terzo()==null)
		throw new it.cnr.jada.comp.ApplicationException("Inserire prima il codice del Terzo");
	
	SQLBuilder sql = getHome(userContext,Incarichi_repertorioBulk.class).createSQLBuilder();
	//getHomeCache(userContext).fetchAll(userContext);
	if (clauses != null) 
	  sql.addClause(clauses);
	
	sql.addClause("AND","stato",SQLBuilder.EQUALS, Incarichi_repertorioBulk.STATO_DEFINITIVO);
	sql.addClause("AND","ti_istituz_commerc",SQLBuilder.EQUALS, carriera.getTi_istituz_commerc());	
	sql.addClause("AND","cd_terzo",SQLBuilder.EQUALS, carriera.getCd_terzo());
	if(carriera.getCd_tipo_rapporto()!= null)
		sql.addSQLClause("AND","CD_TIPO_RAPPORTO",SQLBuilder.EQUALS, carriera.getCd_tipo_rapporto());
//	if(carriera.getCd_trattamento()!=null)
//		sql.addSQLClause("AND","CD_TRATTAMENTO",SQLBuilder.EQUALS, carriera.getCd_trattamento());
	
	sql.addSQLClause("AND","INCARICHI_REPERTORIO.DT_INIZIO_VALIDITA",SQLBuilder.LESS_EQUALS,carriera.getDt_inizio_minicarriera());

	SQLBuilder sqlUoExists = getHome(userContext,Ass_incarico_uoBulk.class).createSQLBuilder();
	sqlUoExists.addSQLJoin("ASS_INCARICO_UO.ESERCIZIO","INCARICHI_REPERTORIO.ESERCIZIO");
	sqlUoExists.addSQLJoin("ASS_INCARICO_UO.PG_REPERTORIO","INCARICHI_REPERTORIO.PG_REPERTORIO");
	sqlUoExists.addSQLClause("AND","ASS_INCARICO_UO.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS, carriera.getCd_unita_organizzativa());

	sql.openParenthesis(FindClause.AND);
		sql.openParenthesis(FindClause.OR);
			sql.addClause(FindClause.AND,"cd_cds",SQLBuilder.EQUALS, carriera.getCd_cds());
			sql.addClause(FindClause.AND,"cd_unita_organizzativa",SQLBuilder.EQUALS, carriera.getCd_unita_organizzativa());
		sql.closeParenthesis();
		sql.addSQLExistsClause(FindClause.OR,sqlUoExists);
	sql.closeParenthesis();

	sql.openParenthesis("AND");
		sql.openParenthesis("");
			sql.addSQLClause("AND","INCARICHI_REPERTORIO.DT_FINE_VALIDITA",SQLBuilder.GREATER_EQUALS,carriera.getDt_fine_minicarriera());
		sql.closeParenthesis();    
		sql.openParenthesis("OR");
			sql.addSQLClause("AND","INCARICHI_REPERTORIO.DT_PROROGA",SQLBuilder.ISNOTNULL,null);
			sql.addSQLClause("AND","INCARICHI_REPERTORIO.DT_PROROGA",SQLBuilder.GREATER_EQUALS,carriera.getDt_fine_minicarriera());
		sql.closeParenthesis();    
		sql.openParenthesis("OR");
			sql.addSQLClause("AND","INCARICHI_REPERTORIO.DT_PROROGA_PAGAM",SQLBuilder.ISNOTNULL,null);
			sql.addSQLClause("AND","INCARICHI_REPERTORIO.DT_PROROGA_PAGAM",SQLBuilder.GREATER_EQUALS,carriera.getDt_fine_minicarriera());
		sql.closeParenthesis();    
		
		SQLBuilder sqlExists = getHome(userContext,Incarichi_repertorio_varBulk.class).createSQLBuilder();
		sqlExists.addSQLJoin("INCARICHI_REPERTORIO_VAR.ESERCIZIO","INCARICHI_REPERTORIO.ESERCIZIO");
		sqlExists.addSQLJoin("INCARICHI_REPERTORIO_VAR.PG_REPERTORIO","INCARICHI_REPERTORIO.PG_REPERTORIO");
		sqlExists.openParenthesis(FindClause.AND);
		sqlExists.addSQLClause(FindClause.OR,"INCARICHI_REPERTORIO_VAR.TIPO_VARIAZIONE",SQLBuilder.EQUALS, Incarichi_repertorio_varBulk.TIPO_INTEGRAZIONE_INCARICO_TRANS);
		sqlExists.addSQLClause(FindClause.OR,"INCARICHI_REPERTORIO_VAR.TIPO_VARIAZIONE",SQLBuilder.EQUALS, Incarichi_repertorio_varBulk.TIPO_INTEGRAZIONE_INCARICO);
		sqlExists.closeParenthesis();
		sqlExists.addSQLClause("AND","INCARICHI_REPERTORIO_VAR.STATO",SQLBuilder.EQUALS, Incarichi_repertorio_varBulk.STATO_DEFINITIVO);
		sqlExists.addSQLClause("AND","INCARICHI_REPERTORIO_VAR.DT_FINE_VALIDITA",SQLBuilder.GREATER_EQUALS,carriera.getDt_fine_minicarriera());
		
		sql.addSQLExistsClause("OR",sqlExists);
	sql.closeParenthesis();

	/*
	sql.openParenthesis("AND");
		sql.openParenthesis("");
			sql.addSQLClause("AND","DT_PROROGA",sql.ISNULL,null);
		    sql.addSQLClause("AND","DT_FINE_VALIDITA",sql.GREATER_EQUALS,carriera.getDt_fine_minicarriera());
		sql.closeParenthesis();    
		sql.openParenthesis("OR");
			sql.addSQLClause("AND","DT_PROROGA",sql.ISNOTNULL,null);
		    sql.addSQLClause("AND","DT_PROROGA",sql.GREATER_EQUALS,carriera.getDt_fine_minicarriera());
		sql.closeParenthesis();
	sql.closeParenthesis();
    */
	return sql;
}
public MinicarrieraBulk completaIncarico(UserContext userContext, MinicarrieraBulk carriera,Incarichi_repertorioBulk incarico) throws ComponentException {

	if (carriera != null) {
		try {
			if(carriera.getTipo_trattamento()==null)
			{
				Tipo_trattamentoHome trattHome = (Tipo_trattamentoHome)getHome(userContext, Tipo_trattamentoBulk.class);
				Tipo_trattamentoBulk tratt;
				tratt = trattHome.findIntervallo(new Tipo_trattamentoBulk(incarico.getTipo_trattamento().getCd_trattamento(),trattHome.getServerDate()));
				carriera.impostaTipo_tratt(tratt);
			}
			/*int dim = getHome(userContext,carriera).getColumnMap().getMappingForProperty("ds_minicarriera").getColumnSize();
			String ds_minicarriera_new = carriera.getDs_minicarriera()+" - "+ incarico.getOggetto();
			if (ds_minicarriera_new.length()>dim-1)
				ds_minicarriera_new = ds_minicarriera_new.substring(0,dim-1);
			carriera.setDs_minicarriera(ds_minicarriera_new);
			*/
		}catch(it.cnr.jada.persistency.PersistencyException ex){
				throw handleException(ex);
		}
	}
	return carriera;
}
/*
public boolean isGestitePrestazioni(UserContext userContext) throws ComponentException {
	try {
		String attivaPrestazione = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession")).getVal01(userContext, CNRUserContext.getEsercizio(userContext), "*", "GESTIONE_COMPENSI", "ATTIVA_PRESTAZIONE");
		if (attivaPrestazione==null)
			throw new ApplicationException("Configurazione CNR: non sono stati impostati i valori per GESTIONE_COMPENSI - ATTIVA_PRESTAZIONE");
		if (attivaPrestazione.compareTo(new String("Y"))==0)
		    return true;
		else
			return false;
		
	} catch (Throwable e) {
		throw handleException(e);
	}
}
*/
public java.util.Collection findTipiPrestazioneCompenso(UserContext userContext,
		MinicarrieraBulk carriera) throws ComponentException {

	try {
		if (carriera.getTerzo() == null)
			return null;

		it.cnr.contab.compensi00.tabrif.bulk.Tipo_prestazione_compensoHome home = (it.cnr.contab.compensi00.tabrif.bulk.Tipo_prestazione_compensoHome) getHome(
				userContext,
				it.cnr.contab.compensi00.tabrif.bulk.Tipo_prestazione_compensoBulk.class);
		return home.findTipiPrestazioneCompensoDaMinicarriera(carriera.getCd_tipo_rapporto());

	} catch (it.cnr.jada.persistency.PersistencyException ex) {
		throw handleException(carriera, ex);
	}
}
}
