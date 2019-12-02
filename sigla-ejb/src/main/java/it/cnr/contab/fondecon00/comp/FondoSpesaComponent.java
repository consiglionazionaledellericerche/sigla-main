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

package it.cnr.contab.fondecon00.comp;

import java.rmi.RemoteException;
import java.sql.PreparedStatement;

import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;

import it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk;
import it.cnr.contab.fondecon00.core.bulk.Fondo_spesaHome;
import it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk;

import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaHome;
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_passivoBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_genericoHome;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IHome;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.compensi00.docs.bulk.Bonus_nucleo_famBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;

import it.cnr.contab.anagraf00.util.PartitaIVAControllo;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class FondoSpesaComponent extends it.cnr.jada.comp.CRUDComponent implements IFondoSpesaMgr {
	/**
	 * FondoEconomaleComponent costruttore standard.
	 */
	public FondoSpesaComponent() {
		super();
	}

/**
 * Verifica della quadratura delle spese:
 * - quadratura e aggiornamento disponibilità del fondo;
 * - quadratura coi massimali mensili;
 * - quadratura coi massimali di spesa.
 */
public void controlloQuadrature(
	UserContext context, 
	Fondo_spesaBulk spesa) 
	throws it.cnr.jada.comp.ComponentException {

	if (spesa == null) return;
	
	Fondo_economaleBulk fondo = spesa.getFondo_economale();

	try {
		fondo = (Fondo_economaleBulk)getHome(context, fondo).findByPrimaryKey(fondo);
		spesa.setFondo_economale(fondo);
	} catch (it.cnr.jada.persistency.ObjectNotFoundException e) {
		throw new it.cnr.jada.comp.ApplicationException(
						"Attenzione: il fondo economale \"" + 
						((fondo.getDs_fondo() == null) ? 
									fondo.getCd_codice_fondo().toString() : 
									fondo.getDs_fondo()) + 
						"\" è stato eliminato! Impossibile salvare.");
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(spesa, e);
	}

	//QUADRADURA E AGGIORNAMENTO CON LA DISPONIBILITà DEL FONDO
	if(spesa.getIm_ammontare_spesa() == null)
		throw new it.cnr.jada.comp.ApplicationException("Ammontare Spesa non può essere nullo");
	if(fondo.getIm_residuo_fondo().compareTo( spesa.getIm_ammontare_spesa() ) < 0)
		throw new it.cnr.jada.comp.ApplicationException("Disponibilità non sufficente a sostenere la spesa.");

	//QUADRATURA MASSIMALI DI SPESA
	if(spesa.getFl_documentata().booleanValue()) {
		if( fondo.getIm_max_gg_spesa_doc() != null
			&& fondo.getIm_max_gg_spesa_doc().compareTo( spesa.getIm_ammontare_spesa() ) < 0)
			throw new it.cnr.jada.comp.ApplicationException("Importo per spesa documentata non consentito (max: "+fondo.getIm_max_gg_spesa_doc()+").");
	} else {
		if( fondo.getIm_max_gg_spesa_non_doc() != null
			&& fondo.getIm_max_gg_spesa_non_doc().compareTo( spesa.getIm_ammontare_spesa() ) < 0)
			throw new it.cnr.jada.comp.ApplicationException("Importo per spesa non documentata non consentito (max: "+fondo.getIm_max_gg_spesa_non_doc()+").");
	}

	//QUADRATURA MASSIMALI MENSILI
	if( spesa.getDt_spesa() == null)
		throw new it.cnr.jada.comp.ApplicationException("Data Spesa non può essere nulla.");

	LoggableStatement ps = null;
	try {
		lockBulk(context, fondo);
		Fondo_spesaHome spesaHome = (Fondo_spesaHome)getHome(context, Fondo_spesaBulk.class);
		java.util.Calendar dt_spesa = java.util.GregorianCalendar.getInstance();
		dt_spesa.setTime( spesa.getDt_spesa() );
		java.math.BigDecimal im_max = new java.math.BigDecimal(0);

		ps = spesaHome.getSpeseMese(fondo, dt_spesa.get(dt_spesa.YEAR), dt_spesa.get(dt_spesa.MONTH)+1).prepareStatement(getConnection(context));
		java.sql.ResultSet rs = ps.executeQuery();
		try {
			while(rs.next()) {
				if( (spesa.getFl_documentata().booleanValue()?"Y":"N").equals( rs.getString("FL_DOCUMENTATA") )) {
					im_max = im_max.add( rs.getBigDecimal("IM_AMMONTARE_SPESA") );
				}
			}
		} finally {
			if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
		}
		try{ps.close();}catch( java.sql.SQLException e ){};

		if (!spesa.isCheckSfondamentoMassimaleEseguito()) {
			if(spesa.getFl_documentata().booleanValue()) {
				if( fondo.getIm_max_mm_spesa_doc() != null
					&& fondo.getIm_max_mm_spesa_doc().compareTo( im_max ) < 0)
					throw new ErroreSquadraturaFondo("L'importo delle spese mensili documentate supererà il limite mensile.\nSi vuole continuare comunque?", spesa);
			} else {
				if( fondo.getIm_max_mm_spesa_non_doc() != null
					&& fondo.getIm_max_mm_spesa_non_doc().compareTo( im_max ) < 0)
					throw new ErroreSquadraturaFondo("L'importo delle spese mensili non documentate supererà il limite mensile.\nSi vuole continuare comunque?", spesa);
			}
		}

	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	} catch(it.cnr.jada.bulk.OutdatedResourceException e) {
		throw handleException(e);
	} catch(it.cnr.jada.bulk.BusyResourceException e) {
		throw handleException(e);
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	} finally {
			if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
	}
}
public OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) 
	throws it.cnr.jada.comp.ComponentException {
		
	Fondo_spesaBulk spesa = (Fondo_spesaBulk)bulk;
	validaSpesa(userContext, spesa);

	try {

		Fondo_economaleBulk fondoRicaricato = (Fondo_economaleBulk)getHome(userContext, spesa.getFondo_economale()).findAndLock(spesa.getFondo_economale());
		if (fondoRicaricato.isChiuso())
			throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare. Il fondo economale è stato chiuso!");

		// Richiesta Iaccarino - 21/10/2003 ***********************
		// Questa op viene effettuata qui per evitare l'implementazione
		// di callback in fase di modifica dell'importo ammontare.
		if (!spesa.isSpesa_documentata())
			spesa.setIm_netto_spesa(spesa.getIm_ammontare_spesa());
		//*********************************************************
		
		updateFondoEconomale(userContext, spesa);
		updateDocumentoAmministrativoCollegato(userContext, spesa);
			
		spesa = (Fondo_spesaBulk)super.creaConBulk(userContext,spesa);
		if (!verificaStatoEsercizio(
								userContext, 
								new EsercizioBulk( 
											spesa.getCd_cds(), 
											((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio())))
			throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare una spesa per un esercizio non aperto!");

		//Verifica della tracciabilità dei pagamenti
		try {
			verificaTracciabilitaPagamenti(userContext, spesa);
		} catch (RemoteException e) {
			throw handleException(spesa, e);
		}
		
		return spesa;
	} catch (Throwable e) {
		throw handleException(spesa, e);
	}
}
//^^@@
/** 
  *  tutti i controlli superati.
  *    PreCondition:
  *      Nessun errore rilevato.
  *    PostCondition:
  *      Permette la cancellazione della spesa.
  *  validazione eliminazione spesa.
  *    PreCondition:
  *      Erorre rilevato
  *    PostCondition:
  *      Viene inviato un messaggio congruente
 */
//^^@@

public void eliminaConBulk(UserContext userContext,OggettoBulk bulk)
	throws it.cnr.jada.comp.ComponentException {

	Fondo_spesaBulk spesa = (Fondo_spesaBulk)bulk;

	try {
		validaEliminazioneSpesa(userContext, spesa);
		
		updateFondoEconomale(userContext, spesa);
		updateDocumentoAmministrativoCollegato(userContext, spesa);
		super.eliminaConBulk(userContext, spesa);

		if (!verificaStatoEsercizio(
								userContext, 
								new EsercizioBulk( 
											spesa.getCd_cds(), 
											((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio())))
			throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare una spesa per un esercizio non aperto!");
	} catch (it.cnr.jada.comp.ApplicationException e) {
		throw handleException(bulk, e);
	} catch (Throwable e) {
		throw handleException(spesa, e);
	}
}
//^^@@
/** 
  *  Oggetto non esistente
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore.
 */
//^^@@

public OggettoBulk inizializzaBulkPerModifica (UserContext userContext,OggettoBulk bulk) 
	throws ComponentException {

	if (bulk == null)
		throw new ComponentException("Attenzione: non esiste alcuna spesa con i criteri impostati!");

	Fondo_spesaBulk spesa = (Fondo_spesaBulk)bulk;

	if (spesa.getEsercizio() == null)
		throw new it.cnr.jada.comp.ApplicationException("L'esercizio della spesa non è valorizzato! Impossibile proseguire.");
			
	if (it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue() !=
		spesa.getEsercizio().intValue())
		throw new it.cnr.jada.comp.ApplicationException("La spesa non appartiene all'esercizio di scrivania!");

	spesa = (Fondo_spesaBulk)super.inizializzaBulkPerModifica(userContext, spesa);
	
	try {
		getHomeCache(userContext).fetchAll(userContext);
		
		if (spesa.getFl_documentata().booleanValue())
			loadDocumentoAmministrativo(userContext, spesa);

		getHomeCache(userContext).fetchAll(userContext);
		
		spesa.getFondo_economale().setOnlyForClose(
			((it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue() != 
				spesa.getFondo_economale().getEsercizio().intValue()) ||
			 (Fondo_spesaBulk.getDateCalendar(null).get(java.util.Calendar.YEAR) !=
				spesa.getFondo_economale().getEsercizio().intValue())));
		return spesa;
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(spesa, e);
	}
}
//^^@@
/** 
  *  Oggetto non esistente
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore.
 */
//^^@@

private void loadDocumentoAmministrativo (
		UserContext userContext,
		Fondo_spesaBulk spesa) throws it.cnr.jada.comp.ComponentException {

	if (spesa == null) return;

	IDocumentoAmministrativoSpesaBulk docAmm = null;
	if (spesa.getCd_tipo_documento_amm() == null)
		return;
	else if (Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA.equalsIgnoreCase(spesa.getCd_tipo_documento_amm()))
		spesa.setTipoDocumento(spesa.TIPO_DOC_FP);
	else if (Numerazione_doc_ammBulk.TIPO_DOC_GENERICO_S.equalsIgnoreCase(spesa.getCd_tipo_documento_amm()))
		spesa.setTipoDocumento(spesa.TIPO_DOC_GP);
	else if (Numerazione_doc_ammBulk.TIPO_MISSIONE.equalsIgnoreCase(spesa.getCd_tipo_documento_amm()))
		spesa.setTipoDocumento(spesa.TIPO_DOC_MIS);
	else if (Numerazione_doc_ammBulk.TIPO_COMPENSO.equalsIgnoreCase(spesa.getCd_tipo_documento_amm()))
		spesa.setTipoDocumento(spesa.TIPO_DOC_COMP);
	else if (Numerazione_doc_ammBulk.TIPO_ANTICIPO.equalsIgnoreCase(spesa.getCd_tipo_documento_amm()))
		spesa.setTipoDocumento(spesa.TIPO_DOC_ANT);

	Class clazz = spesa.getClasseDocAmm();
	if (clazz == null) return;

	try {
		docAmm = (IDocumentoAmministrativoSpesaBulk)clazz.newInstance();
		docAmm.setCd_cds(spesa.getCd_cds_doc_amm());
		docAmm.setCd_uo(spesa.getCd_uo_doc_amm());
		docAmm.setEsercizio(spesa.getEsercizio_doc_amm());
		docAmm.setPg_doc_amm(spesa.getPg_documento_amm());
		docAmm.setCd_tipo_doc_amm(spesa.getCd_tipo_documento_amm());

		it.cnr.jada.bulk.BulkHome home = getHome(userContext, (OggettoBulk)docAmm);
		docAmm = (IDocumentoAmministrativoSpesaBulk)home.findByPrimaryKey(docAmm);
	} catch (InstantiationException e) {
		throw handleException(spesa, e);
	} catch (IllegalAccessException e) {
		throw handleException(spesa, e);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(spesa, e);
	}

	spesa.setDocumento(docAmm);
}
public OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	
	// Metodo NON chiamato (Edit della spesa non permessa). Le parti commentate
	// devono essere considerate e eventualmente modificate in caso di 
	// richiesta specifica di modifica in fase di edit.
	
	try {
		Fondo_spesaBulk spesa = (Fondo_spesaBulk)bulk;

		//Fondo_economaleBulk fondoRicaricato = (Fondo_economaleBulk)getHome(userContext, spesa.getFondo_economale()).findAndLock(spesa.getFondo_economale());
		//if (fondoRicaricato.isChiuso())
			//throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare. Il fondo economale è stato chiuso!");
			
		//updateFondoEconomale(userContext, spesa);
		//updateDocumentoAmministrativoCollegato(userContext, spesa);
		spesa = (Fondo_spesaBulk)super.modificaConBulk(userContext,spesa);
		//if (!verificaStatoEsercizio(
								//userContext, 
								//new EsercizioBulk( 
											//spesa.getCd_cds(), 
											//((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio())))
			//throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare una spesa per un esercizio non aperto!");

		return spesa;
	} catch (Throwable e) {
		throw handleException(bulk, e);
	}
}
//^^@@
/** 
  *  Oggetto non esistente
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore.
 */
//^^@@

protected it.cnr.jada.persistency.sql.Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {

	if (clauses == null) {
		if (bulk != null)
			clauses = bulk.buildFindClauses(null);
	} else
		clauses = it.cnr.jada.persistency.sql.CompoundFindClause.and(clauses,bulk.buildFindClauses(Boolean.FALSE));

	for (java.util.Iterator i = clauses.iterator(); i.hasNext();) {
		it.cnr.jada.persistency.sql.SimpleFindClause clause = (it.cnr.jada.persistency.sql.SimpleFindClause)i.next();
		if (clause.getPropertyName().equalsIgnoreCase("codice_fiscale") ||
			clause.getPropertyName().equalsIgnoreCase("partita_iva")) {
			clause.setOperator(SQLBuilder.CONTAINS);
		} else if (clause.getPropertyName().equalsIgnoreCase("fornitoreSaltuario")) {
			clause.setPropertyName("fl_fornitore_saltuario");
			if (clause.getValue() != null && !clause.getValue().equals("0"))
				clause.setValue((((String)clause.getValue()).equalsIgnoreCase("1"))? Boolean.TRUE : Boolean.FALSE);
			else
				clause.setValue(null);
		}
	}

	SQLBuilder sql = getHome(userContext,bulk).selectByClause(clauses);
	
	sql.addOrderBy("PG_FONDO_SPESA");
	sql.addOrderBy("DT_SPESA");
	return sql;
}
/**
 * Pre:  Ricerca terzo fornitore
 * Post: Limitazione ai terzi ancora validi.
 */
public it.cnr.jada.persistency.sql.SQLBuilder selectFornitoreByClause(
	it.cnr.jada.UserContext userContext,
	it.cnr.jada.bulk.OggettoBulk bulk,
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk responsabile,
	it.cnr.jada.persistency.sql.CompoundFindClause clause)
	throws	it.cnr.jada.comp.ComponentException, 
			it.cnr.jada.persistency.PersistencyException {

	Fondo_spesaBulk spesa = (Fondo_spesaBulk)bulk;
	if (spesa.getDt_spesa() == null)
		throw new it.cnr.jada.comp.ApplicationException("Inserire la data spesa prima di selezionare un fornitore.");
	if (spesa.getDateCalendar(spesa.getDt_spesa()).get(java.util.Calendar.YEAR) != 
		spesa.getEsercizio().intValue())
		throw new it.cnr.jada.comp.ApplicationException("La data spesa deve appartenere all'esercizio del documento!");
		
	if (clause == null) clause = responsabile.buildFindClauses(null);

	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(userContext, responsabile,"V_TERZO_CF_PI").createSQLBuilder();
	sql.addClause(
		it.cnr.jada.persistency.sql.CompoundFindClause.or(
			new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.ISNULL, null),
			new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.GREATER, spesa.getDt_spesa())
		)
	);
	if (spesa.getFornitore() != null)
		sql.addClause("AND", "cd_precedente", sql.EQUALS, spesa.getFornitore().getCd_precedente());

	if (spesa.getPartita_iva() != null || spesa.getFornitore() != null) {
		sql.addTableToHeader("ANAGRAFICO");
		sql.addSQLJoin("V_TERZO_CF_PI.CD_ANAG", "ANAGRAFICO.CD_ANAG");
		sql.addSQLClause("AND", "ANAGRAFICO.PARTITA_IVA", sql.CONTAINS, spesa.getPartita_iva());
		sql.addSQLClause("AND", "ANAGRAFICO.CODICE_FISCALE", sql.CONTAINS, spesa.getCodice_fiscale());
	}
		
	if (clause != null) 
		sql.addClause(clause);

	return sql;
}

/**
 * Imposta il comune fiscale relativo alla spesa.
 *
 * Nome: Gestione comune fiscale;
 * Pre:  Ricerca del comune e acricamenti dei cap relativi;
 * Post: Viene assegnato il comune e lanciato l'aggornamento dell'elenco dei cap associati.
 *
 * @param spsea Fondo_spesaBulk su cui va impostato il comune fiscale.
 * @param comune il ComuneBulk del comune da impostare.
 *
 * @return Fondo_spesaBulk con comune impostato.
 */
 
public Fondo_spesaBulk setCitta(it.cnr.jada.UserContext userContext, Fondo_spesaBulk spesa, it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune) throws it.cnr.jada.comp.ComponentException {
	spesa.setCitta(comune);
	spesa.setCaps_fornitore(null);
	initializeKeysAndOptionsInto(userContext,spesa);
	if (comune != null)
		spesa.setCap_fornitore(comune.getCd_cap());
	else
		spesa.setCap_fornitore(null);
	return spesa;
}

//^^@@
/** 
  *  Oggetto non esistente
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore.
 */
//^^@@

private void updateDocumentoAmministrativoCollegato (
		UserContext userContext,
		Fondo_spesaBulk spesa)
	throws	it.cnr.jada.persistency.PersistencyException,
			ComponentException {

	if (spesa == null || 
		spesa.getFl_documentata() == null ||
		!spesa.getFl_documentata().booleanValue())
		return;

	try {
		it.cnr.jada.bulk.BulkHome home = getHome(userContext, (OggettoBulk)spesa.getDocumento());
		if (home instanceof IDocumentoAmministrativoSpesaHome)
			((IDocumentoAmministrativoSpesaHome)home).updateFondoEconomale(spesa);
	} catch (it.cnr.jada.bulk.OutdatedResourceException e) {
		throw handleException(spesa, e);
	} catch (it.cnr.jada.bulk.BusyResourceException e) {
		throw handleException(spesa, e);
	}
}
//^^@@
/** 
  *  Oggetto non esistente
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore.
 */
//^^@@

private void updateFondoEconomale (
		UserContext userContext,
		Fondo_spesaBulk spesa) 
	throws	it.cnr.jada.persistency.PersistencyException,
			ComponentException {

	Fondo_economaleBulk fondo = spesa.getFondo_economale();
	if (spesa.isToBeCreated() || spesa.isToBeUpdated()) {
		fondo.setIm_totale_spese(fondo.getIm_totale_spese().add(spesa.getIm_ammontare_spesa()));
		fondo.setIm_totale_netto_spese(fondo.getIm_totale_netto_spese().add(spesa.getImportoNettoSpesa()));
		fondo.setIm_residuo_fondo(fondo.getIm_residuo_fondo().subtract(spesa.getIm_ammontare_spesa()));
	} else if (spesa.isToBeDeleted()) {
		fondo.setIm_totale_spese(fondo.getIm_totale_spese().subtract(spesa.getIm_ammontare_spesa()));
		fondo.setIm_totale_netto_spese(fondo.getIm_totale_netto_spese().subtract(spesa.getImportoNettoSpesa()));
		fondo.setIm_residuo_fondo(fondo.getIm_residuo_fondo().add(spesa.getIm_ammontare_spesa()));
	}
			
	updateBulk(userContext, fondo);
}
/**
 * Verifiche e inizializazioni relative a una spesa (Fondo_spesaBulk)
 *
 * Se il fornitore è saltuario viene verificata la presenza dei relativi dati minimi:
 * - Denominazione
 * - Codice fiscale
 * - Città
 * Se il fornitore non è saltuario viene verificata la selezione di un terzo
 *
 * Se c'è partita iva ne controllo l'esattezza
 *
 * Se c'è codice fiscale ne controllo l'esattezza
 *
 * Si effettuano i controlli di quadratura
 */
private void validaEliminazioneSpesa(it.cnr.jada.UserContext userContext, Fondo_spesaBulk spesa)
	throws it.cnr.jada.comp.ComponentException,
		it.cnr.jada.bulk.OutdatedResourceException,
		it.cnr.jada.bulk.BusyResourceException,
		it.cnr.jada.persistency.PersistencyException {

	if (spesa == null)
		throw new it.cnr.jada.comp.ApplicationException("Nessuna spesa selezionata per la cancellazione");

	//if (!verificaStatoEsercizio(userContext, new EsercizioBulk( spesa.getCd_cds(), spesa.getEsercizio())))
		//throw new it.cnr.jada.comp.ApplicationException("Impossibile eliminare una spesa per un esercizio non aperto!");

	Fondo_economaleBulk fondoRicaricato = (Fondo_economaleBulk)getHome(userContext, spesa.getFondo_economale()).
													findAndLock(spesa.getFondo_economale());
	if (fondoRicaricato.isChiuso())
		throw new it.cnr.jada.comp.ApplicationException("Non è possibile eliminare spese appartenenti ad un fondo chiuso!");

	//if (spesa.getFondo_economale().isOnlyForClose())
		//throw new it.cnr.jada.comp.ApplicationException("Non è possibile eliminare spese appartenenti ad un fondo di un altro esercizio!");

	if (spesa.isSpesa_reintegrata())
		throw new it.cnr.jada.comp.ApplicationException("Non è possibile eliminare spes già reintegrate!");

	if (!spesa.isSpesa_documentata() && spesa.getObb_scad() != null)
		throw new it.cnr.jada.comp.ApplicationException("Disassociare questa spesa dal fondo economale prima di cancellarla!");

	if (spesa.isSpesa_documentata() && 
		spesa.TIPO_DOC_ANT.equalsIgnoreCase(spesa.getTipoDocumento())) {
		Boolean associatoAMissione = ((it.cnr.contab.missioni00.docs.bulk.AnticipoBulk)spesa.getDocumento()).getFl_associato_missione();		
		if (associatoAMissione != null && associatoAMissione.booleanValue())
			throw new it.cnr.jada.comp.ApplicationException("Impossibile eliminare la spesa perchè l'anticipo è associato ad una missione!");
	}
}

/**
 * Verifiche e inizializazioni relative a una spesa (Fondo_spesaBulk)
 *
 * Se il fornitore è saltuario viene verificata la presenza dei relativi dati minimi:
 * - Denominazione
 * - Codice fiscale
 * - Città
 * Se il fornitore non è saltuario viene verificata la selezione di un terzo
 *
 * Se c'è partita iva ne controllo l'esattezza
 *
 * Se c'è codice fiscale ne controllo l'esattezza
 *
 * Si effettuano i controlli di quadratura
 */
private void validaSpesa(it.cnr.jada.UserContext userContext, Fondo_spesaBulk spesa) throws it.cnr.jada.comp.ComponentException {

	//if (!verificaStatoEsercizio(userContext, new EsercizioBulk( spesa.getCd_cds(), spesa.getEsercizio())))
		//throw new it.cnr.jada.comp.ApplicationException("Impossibile inserire o salvare una spesa per un esercizio non aperto!");

	if (spesa.getFondo_economale().isChiuso())
		throw new it.cnr.jada.comp.ApplicationException("Non è possibile creare o apportare modifiche a spese appartenenti ad un fondo chiuso!");

	//if (spesa.getFondo_economale().isOnlyForClose())
		//throw new it.cnr.jada.comp.ApplicationException("Non è possibile creare o apportare modifiche a spese appartenenti ad un fondo di un altro esercizio!");

	if(spesa.getIm_ammontare_spesa() == null)
		throw new it.cnr.jada.comp.ApplicationException("Specificare l'importo della spesa!");
	if(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP).compareTo(spesa.getIm_ammontare_spesa()) >= 0)
		throw new it.cnr.jada.comp.ApplicationException("L'importo della spesa deve essere positivo e diverso da 0!");

	if(spesa.getDt_spesa() == null)
		throw new it.cnr.jada.comp.ApplicationException("Specificare la data della spesa!");
	java.util.Calendar today = spesa.getDateCalendar(null);
	java.util.Calendar dataSpesa = spesa.getDateCalendar(spesa.getDt_spesa());
	if (!dataSpesa.equals(today) && !dataSpesa.before(today))
		throw new it.cnr.jada.comp.ApplicationException("La data della spesa deve essere precedente o uguale alla data odierna!");
	if (dataSpesa.get(java.util.Calendar.YEAR) != it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue())
		throw new it.cnr.jada.comp.ApplicationException("La data della spesa deve appartenere all'esercizio di scrivania!");
	
	java.sql.Timestamp competenzaDa = spesa.getDt_da_competenza_coge();
	if(competenzaDa == null) {
		if (spesa.getFl_documentata() != null && spesa.getFl_documentata().booleanValue()) {
			competenzaDa = spesa.getCurrentDate();
			spesa.setDt_da_competenza_coge(competenzaDa);
		} else
			throw new it.cnr.jada.comp.ApplicationException("Specificare la data \"competenza da\" per la spesa!");
	}
	java.sql.Timestamp competenzaA = spesa.getDt_a_competenza_coge();
	if(competenzaA == null) {
		if (spesa.getFl_documentata() != null && spesa.getFl_documentata().booleanValue()) {
			competenzaA = spesa.getCurrentDate();
			spesa.setDt_a_competenza_coge(competenzaA);
		} else
			throw new it.cnr.jada.comp.ApplicationException("Specificare la data \"competenza a\" per la spesa!");
	}
	if (spesa.getFl_documentata() == null || !spesa.getFl_documentata().booleanValue()) {
		java.util.Calendar competenzaDaCalendar = spesa.getDateCalendar(competenzaDa);
		java.util.Calendar competenzaACalendar = spesa.getDateCalendar(competenzaA);
		if (!competenzaDaCalendar.equals(competenzaACalendar) && !competenzaDaCalendar.before(competenzaACalendar))
			throw new it.cnr.jada.comp.ApplicationException("La data \"competenza da\" deve essere precedente o uguale a \"competenza a\"!");
	}
		
	if(spesa.isFornitore_saltuario()) {
		spesa.setFornitore(null);
		if( spesa.getDenominazione_fornitore() == null ||
			spesa.getDenominazione_fornitore().trim().length() <= 0 ||
			spesa.getCodice_fiscale() == null ||
			spesa.getCodice_fiscale().trim().length() <= 0 ||
			spesa.getCitta() == null || 
			spesa.getCitta().getCrudStatus() != OggettoBulk.NORMAL) {
			throw new it.cnr.jada.comp.ApplicationException("Dati fornitore non sufficienti (min: Denominazione, Codice Fiscale, Città).");
		}
		try {
			if(spesa.getPartita_iva() != null && spesa.getPartita_iva().length() != 0) {
				PartitaIVAControllo.parsePartitaIVA(spesa.getPartita_iva());
			}
		} catch(it.cnr.contab.anagraf00.util.ExPartitaIVA ecf) {
			throw new it.cnr.jada.comp.ApplicationException("La partita I.V.A. è errata.");
		}

		if(spesa.getCodice_fiscale() != null) {
			if(spesa.getCodice_fiscale() != null && spesa.getCodice_fiscale().length() != 0) {
				try {
					new Long(spesa.getCodice_fiscale());
					try {
						it.cnr.contab.anagraf00.util.PartitaIVAControllo.parsePartitaIVA(spesa.getCodice_fiscale());
					} catch(it.cnr.contab.anagraf00.util.ExPartitaIVA ecf) {
						throw new it.cnr.jada.comp.ApplicationException("Codice fiscale inserito errato.");
					}
				} catch (NumberFormatException nfe){
					if(spesa.getCodice_fiscale().length() != 16 ||
						!it.cnr.contab.anagraf00.util.CodiceFiscaleControllo.checkCC(spesa.getCodice_fiscale().toUpperCase()))
						throw new it.cnr.jada.comp.ApplicationException("Codice fiscale inserito errato.");
				}
			}
		}
	} else if(spesa.getFornitore() == null || spesa.getFornitore().getCrudStatus() != OggettoBulk.NORMAL) {
		throw new it.cnr.jada.comp.ApplicationException("Specificare un fornitore per la spesa!");
	}

	//Il controllo di quadratura DEVE essere operato successivamente agli altri controlli
	controlloQuadrature(userContext, spesa);

}
//^^@@
/** 
  *  Verifica l'esistenza e apertura dell'esercizio
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      Viene consentita l'attività richiesta
  *  L'esercizio non è aperto
  *    PreCondition:
  *      L'esercizio su cui insiste il controllo non è aperto
  *    PostCondition:
  *      Viene notificato l'errore
 */
//^^@@

public boolean verificaStatoEsercizio(
	UserContext userContext,
	EsercizioBulk anEsercizio) 
	throws ComponentException {

	try {
		it.cnr.contab.config00.esercizio.bulk.EsercizioHome eHome = (it.cnr.contab.config00.esercizio.bulk.EsercizioHome)getHome(userContext, EsercizioBulk.class);
		return !eHome.isEsercizioChiuso(
									userContext,
									anEsercizio.getEsercizio(),
									anEsercizio.getCd_cds());
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}

}
public void verificaTracciabilitaPagamenti(UserContext context,
		Fondo_spesaBulk spesa) throws ComponentException, RemoteException {
	Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
	java.math.BigDecimal limite=sess.getIm01(context, new Integer(0), null,"LIMITE_UTILIZZO_CONTANTI", "LIMITE1");
	java.sql.Timestamp data_da=sess.getDt01(context, new Integer(0), null,"LIMITE_UTILIZZO_CONTANTI", "LIMITE1");
	java.sql.Timestamp data_a=sess.getDt02(context, new Integer(0), null,"LIMITE_UTILIZZO_CONTANTI", "LIMITE1");
	if ( sess == null  )
		throw new ApplicationException("Configurazione CNR: manca la definizione del LIMITE_UTILIZZO_CONTANTI");	
	
	if ( limite == null || data_da == null || data_a == null)
		throw new ApplicationException("Configurazione CNR: non sono stati impostati i valori per LIMITE_UTILIZZO_CONTANTI - LIMITE1");			

	if (spesa.getDt_spesa().compareTo(data_da) > -1 && 
		spesa.getDt_spesa().compareTo(data_a) < 1 &&
		spesa.getIm_netto_spesa().compareTo(limite)>0)
		throw new ApplicationException("Non è possibile procedere. Nel rispetto della tracciabilità dei pagamenti, non sono ammissibili spese con importo superiore a " + limite);
}

}
