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

package it.cnr.contab.docamm00.bp;

import it.cnr.contab.doccont00.bp.IDocumentoContabileBP;
import it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession;
import it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession;
import it.cnr.contab.docamm00.comp.DocumentoAmministrativoComponentSession;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 2:18:04 PM)
 * @author: Roberto Peli
 */
public class RisultatoEliminazioneBP 
	extends it.cnr.jada.util.action.BulkBP
	implements IDefferedUpdateSaldiBP {

	private SimpleDetailCRUDController documentiAmministrativiController = null;
	private SimpleDetailCRUDController documentiContabiliController = null;

	private boolean bringBack = true;
	private boolean editOnly = false;
	private OggettoBulk bulkClone = null;
/**
 * RicercaObbligazioniBP constructor comment.
 */
public RisultatoEliminazioneBP() {
	super();
}
/**
 * CRUDFatturaPassivaAction constructor comment.
 */
public RisultatoEliminazioneBP(String options) {
	super(options);
	bringBack = options != null && options.indexOf('R') >= 0;
	if (bringBack)
		setEditOnly(options != null && options.indexOf('S') >= 0);

}
/**
 * Aggiorna i saldi dei documenti contabili cancellati
 */
 
public void aggiornaSaldi(
	ActionContext context,
	Risultato_eliminazioneVBulk re)
	throws	BusinessProcessException,
			java.rmi.RemoteException,
			it.cnr.jada.comp.ComponentException {

	if (re.getDefferredSaldi() != null && !re.getDefferredSaldi().isEmpty()) {
		for (java.util.Iterator i = re.getDefferredSaldi().keySet().iterator(); i.hasNext();) {
			IDocumentoContabileBulk key = (IDocumentoContabileBulk)i.next();
			if (key != null) {
				java.util.Map values = (java.util.Map)re.getDefferredSaldi().get(key);
				//QUI chiamare component del documento contabile interessato
				IDocumentoContabileBP docContBP = (IDocumentoContabileBP)context.createBusinessProcess(key.getManagerName(), new Object[] { "MRSWTh" });
				DocumentoContabileComponentSession session = docContBP.getVirtualSession(context, false);
				if (session != null)
					session.aggiornaSaldiInDifferita(
											context.getUserContext(), 
											key, 
											values,
											null);
			}
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (09/07/2001 14:55:11)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public it.cnr.jada.bulk.OggettoBulk cloneBulk(OggettoBulk bulk)
	throws java.io.IOException, ClassNotFoundException {

	if (bulk == null) {
		bulkClone = null;
		return null;
	}
	
	java.io.ByteArrayOutputStream cloneStream = new java.io.ByteArrayOutputStream();
	java.io.ObjectOutputStream objStream = null;
	try {
		objStream = new java.io.ObjectOutputStream(cloneStream);
		objStream.writeObject(bulk);
		objStream.flush();
		objStream.close();
	} catch (java.io.IOException e) {
		if (objStream != null) objStream.close();
		else cloneStream.close();
		throw e;
	}

	java.io.ByteArrayInputStream cloneIS = new java.io.ByteArrayInputStream(cloneStream.toByteArray());
	java.io.ObjectInputStream objOS = null;
	try {
		objOS = new java.io.ObjectInputStream(cloneIS);
		OggettoBulk obj = (OggettoBulk)objOS.readObject();
		objOS.close();
		bulkClone = bulk;
		return obj;
	} catch (ClassNotFoundException e) {
		objOS.close();
		throw e;
	} catch (java.io.IOException e) {
		if (objOS != null) objOS.close();
		else cloneIS.close();
		throw e;
	}
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() {
	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[2];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.bringBack");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.undoBringBack");
	return toolbar;
}
public void edit(it.cnr.jada.action.ActionContext context,OggettoBulk bulk) throws it.cnr.jada.action.BusinessProcessException {
	try {
		OggettoBulk bulkModel = cloneBulk(bulk);
		bulkModel.setUser(context.getUserInfo().getUserid());
		setModel(context,bulkModel);
		setDirty(false);
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
/**
 * Effettua una operazione di ricerca per un attributo di un modello.
 * @param actionContext contesto dell'azione in corso
 * @param clauses Albero di clausole da utilizzare per la ricerca
 * @param bulk prototipo del modello di cui si effettua la ricerca
 * @param context modello che fa da contesto alla ricerca (il modello del FormController padre del
 * 			controller che ha scatenato la ricerca)
 * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
 */
public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, java.lang.String property) throws it.cnr.jada.action.BusinessProcessException {
	return null;
}
/**
 * Restituisce il modello da usare su richiesta di un operazione di "riporta".
 * L'implementazione attuale restituisce il modello del BusinessProcess
 */
public it.cnr.jada.bulk.OggettoBulk getBringBackModel() {

	Risultato_eliminazioneVBulk re = (Risultato_eliminazioneVBulk)getModel();
	if (!re.getDocumentiAmministrativiScollegati().isEmpty())
		throw new it.cnr.jada.action.MessageToUser("Eseguire il controllo di quadratura per tutti i documenti amministrativi in elenco!",ERROR_MESSAGE);
	return getModel();
}
/**
 * Insert the method's description here.
 * Creation date: (11/30/2001 11:16:57 AM)
 * @return byte[]
 */
public it.cnr.jada.bulk.OggettoBulk getBulkClone() {
	return bulkClone;
}
/**
 * Insert the method's description here.
 * Creation date: (5/22/2002 10:49:41 AM)
 */
public it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi getDefferedUpdateSaldiBulk() {
	return (IDefferUpdateSaldi)getModel();
}
/**
 * Insert the method's description here.
 * Creation date: (5/22/2002 10:49:41 AM)
 */
public it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP getDefferedUpdateSaldiParentBP() {
	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 3:49:49 PM)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getDocumentiAmministrativiController() {
	return documentiAmministrativiController;
}
/**
 * Insert the method's description here.
 * Creation date: (2/7/2002 3:49:49 PM)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getDocumentiContabiliController() {
	return documentiContabiliController;
}
/**
 *	Abilito il bottone di cancellazione documento solo se non ho scadenze in fase di modifica/inserimento
 */

public void initializeControllers(
		ActionContext context, 
		IDocumentoAmministrativoBulk docAmm) {
	
	documentiAmministrativiController = new SimpleDetailCRUDController("Documenti Amministrativi",docAmm.getDocumentoAmministrativoClassForDelete(),"documentiAmministrativiScollegati",this);
	documentiContabiliController = new SimpleDetailCRUDController("Documenti Contabili",docAmm.getDocumentoContabileClassForDelete(),"documentiContabiliScollegati",this);
}
public boolean isBringBack() {
	return bringBack;
}
public boolean isBringbackButtonEnabled() {
	return true;
}
public boolean isBringbackButtonHidden() {
	return !isBringBack();
}
/**
 * Insert the method's description here.
 * Creation date: (11/30/2001 11:17:01 AM)
 * @return boolean
 */
public boolean isEditOnly() {
	return editOnly;
}
/**
 *	Abilito il bottone di cancellazione documento solo se non ho scadenze in fase di modifica/inserimento
 */

public boolean isUndoBringBackButtonEnabled() {
	
	return isEditOnly();
}
/**
 *	Abilito il bottone di cancellazione documento solo se non ho scadenze in fase di modifica/inserimento
 */

public boolean isUndoBringBackButtonHidden() {
	
	return !isEditOnly();
}
/**
 * Gestisce un comando di cancellazione.
 */
public Risultato_eliminazioneVBulk manageDelete(ActionContext context, IDocumentoAmministrativoBP bp)
	throws it.cnr.jada.comp.ComponentException, BusinessProcessException {

	Risultato_eliminazioneVBulk deleteManager = bp.getDeleteManager();
	IDocumentoAmministrativoBulk docAmm = bp.getDocumentoAmministrativoCorrente();
	java.util.Vector scadenze = new java.util.Vector();
	java.util.List children = docAmm.getChildren();
	if (children != null && !children.isEmpty()) {
		it.cnr.jada.ejb.CRUDComponentSession h = null;
		try {
			h = bp.createComponentSession();
		} catch (BusinessProcessException e) {
			throw handleException(e);
		}
		for (java.util.Iterator i = docAmm.getChildren().iterator(); i.hasNext();) {
			IDocumentoAmministrativoRigaBulk riga = (IDocumentoAmministrativoRigaBulk)i.next();
			if (!riga.isDirectlyLinkedToDC()) {
				IScadenzaDocumentoContabileBulk scadenzaCollegata = riga.getScadenzaDocumentoContabile();
				if (!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(
							scadenze,
							(it.cnr.jada.bulk.OggettoBulk)scadenzaCollegata)) {
					String docContBPName = scadenzaCollegata.getFather().getManagerName();
					try {
						it.cnr.contab.doccont00.bp.IDocumentoContabileBP docContBP = (it.cnr.contab.doccont00.bp.IDocumentoContabileBP)context.createBusinessProcess(docContBPName, new Object[] { "MRSWTh" });
						java.math.BigDecimal importo = new java.math.BigDecimal(0);
						java.util.List righeAssociate = null;

						if (docAmm.getObbligazioniHash() != null)
							righeAssociate = (java.util.List)docAmm.getObbligazioniHash().get(scadenzaCollegata);
						if (righeAssociate == null && docAmm.getAccertamentiHash() != null)
							righeAssociate = (java.util.List)docAmm.getAccertamentiHash().get(scadenzaCollegata);

						if (righeAssociate == null || righeAssociate.isEmpty()) {
							deleteManager.add(riga);
						} else {
							for (java.util.Iterator righe = righeAssociate.iterator(); righe.hasNext();) {
								IDocumentoAmministrativoRigaBulk rigaAssociata = (IDocumentoAmministrativoRigaBulk)righe.next();
								importo = importo.add(rigaAssociata.getIm_imponibile().add(rigaAssociata.getIm_iva()));
							}
							importo = docAmm.getImportoSignForDelete(importo.setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
							java.math.BigDecimal importoAggiornamento = scadenzaCollegata.getIm_scadenza().add(importo);
							if (importoAggiornamento.compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)) < 0)
								throw new it.cnr.jada.action.MessageToUser("Cancellare tutte le note di credito generate successivamente a questa nota di debito. Cancellazione annullata!");
							DocumentoContabileComponentSession docContSession = docContBP.getVirtualSession(context, true);
							try {
								scadenzaCollegata = docContSession.modificaScadenzaInAutomatico(
													context.getUserContext(), 
													scadenzaCollegata,
													importoAggiornamento,
													(importo.signum()>=0)?false:true);
							} catch (Throwable thr) {
								if (docContSession instanceof AccertamentoAbstractComponentSession)
									((AccertamentoAbstractComponentSession)docContSession).rollbackToSavePoint(context.getUserContext());
								else
									((ObbligazioneAbstractComponentSession)docContSession).rollbackToSavePoint(context.getUserContext());
								throw thr;
							}

							//E' NECESSARIO per gli aggiornamenti in automatico impostare i docCont SUL delete
							//manager ottenuto perchè NON è ancora il modello corrente del bp
							deleteManager.addToDefferredSaldi(
														scadenzaCollegata.getFather(), 
														scadenzaCollegata.getFather().getSaldiInfo());
							if (h instanceof DocumentoAmministrativoComponentSession) {
								scadenzaCollegata.setIm_associato_doc_amm(importoAggiornamento);
								scadenzaCollegata = ((DocumentoAmministrativoComponentSession)h).updateImportoAssociatoDocAmm(context.getUserContext(), scadenzaCollegata);
							}
						}
					} catch (ClassCastException exc) {
						//Questo errore viene lanciato nel caso in cui le ComponentSession dei doc cont vengano rigenerate
						//e lo sviluppatore si dimentichi di far implementare l'interfaccia DocumentoContabileComponentSession
						throw new it.cnr.jada.comp.ApplicationException("ATTENZIONE! Si è cercato di eseguire, durante l'operazione di cancellazione, un aggiornamento in automatico di una scadenza NON riconosciuta come Documento Contabile!");
					} catch (it.cnr.jada.action.MessageToUser t) {
						throw t;
					} catch (Throwable t) {
						deleteManager.add(riga);
					}
					scadenze.add(scadenzaCollegata);
				}
			} else
				deleteManager.add(riga);

			if (h instanceof it.cnr.contab.docamm00.comp.DocumentoAmministrativoComponentSession) {
				java.math.BigDecimal totRiga = riga.getIm_imponibile().add(riga.getIm_iva());
				IDocumentoAmministrativoRigaBulk originalDetail = riga.getOriginalDetail();
				if (originalDetail != null ) {
					java.math.BigDecimal impDisponibile = originalDetail.getIm_diponibile_nc();
					originalDetail.setIm_diponibile_nc(impDisponibile.add(riga.getFather().getImportoSignForDelete(totRiga)));
					try {
						((DocumentoAmministrativoComponentSession)h).update(
											context.getUserContext(), 
											originalDetail);
					} catch (java.rmi.RemoteException e) {
						throw handleException(e);
					}
				}
			}
		}
	}

	initializeControllers(context, docAmm);

	//Aggiorno i saldi di tutte le scadenze modificate in automatico nel caso in cui NON sia necessario
	//l'intervento dell'utente --> che il pannello della cancellazione non venga aperto.
	if (deleteManager.getDocumentiAmministrativiScollegati().isEmpty() &&
		deleteManager.getDocumentiContabiliScollegati().isEmpty())
		try {
			aggiornaSaldi(context, deleteManager);
		} catch (Throwable t) {
			throw handleException(t);
		}
		
	return deleteManager;
}
/**
 * Insert the method's description here.
 * Creation date: (11/30/2001 11:17:01 AM)
 * @param newBringBackWithoutCommit boolean
 */
public void setEditOnly(boolean newEditOnly) {
	editOnly = newEditOnly;
}
}
