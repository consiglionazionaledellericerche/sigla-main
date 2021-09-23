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

package it.cnr.contab.doccont00.bp;


import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.pdcfin.bulk.Ass_ev_evBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.docamm00.bp.CRUDDocumentoGenericoAttivoBP;
import it.cnr.contab.docamm00.bp.CRUDFatturaAttivaIBP;
import it.cnr.contab.docamm00.bp.CRUDNotaDiCreditoAttivaBP;
import it.cnr.contab.docamm00.bp.CRUDNotaDiCreditoBP;
import it.cnr.contab.docamm00.bp.CRUDNotaDiDebitoAttivaBP;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.AccertamentoComponentSession;
import it.cnr.contab.doccont00.ejb.AccertamentoResiduoComponentSession;
import it.cnr.contab.prevent00.bulk.Pdg_vincoloBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.MessageToUser;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;

/**
 * Business Process che gestisce le attività di CRUD per l'entita' Accertamento
 */

public class CRUDAccertamentoBP extends CRUDVirtualAccertamentoBP {
	private final CRUDScadenzeController scadenzario = new CRUDScadenzeController("Scadenzario",Accertamento_scadenzarioBulk.class,"accertamento_scadenzarioColl",this);
	private final SimpleDetailCRUDController scadenzarioDettaglio = new SimpleDetailCRUDController("ScadenzarioDettaglio",Accertamento_scad_voceBulk.class,"accertamento_scad_voceColl",scadenzario);

	private final SimpleDetailCRUDController pdgVincoli= new SimpleDetailCRUDController("Vincoli",Pdg_vincoloBulk.class,"pdgVincoliColl",this);
	private final SimpleDetailCRUDController pdgVincoliPerenti= new SimpleDetailCRUDController("VincoliPerenti",Accertamento_vincolo_perenteBulk.class,"accertamentoVincoliPerentiColl",this);

	private final SimpleDetailCRUDController centriDiResponsabilita = new SimpleDetailCRUDController("CentriDiResponsabilita",CdrBulk.class,"cdrColl",this);
	private final SimpleDetailCRUDController lineeDiAttivita = new SimpleDetailCRUDController("LineeDiAttivita",V_pdg_accertamento_etrBulk.class,"lineeAttivitaColl",this);
	private final SimpleDetailCRUDController nuoveLineeDiAttivita = new SimpleDetailCRUDController("NuoveLineeDiAttivita",Linea_attivitaBulk.class,"nuoveLineeAttivitaColl",this);

	private final SimpleDetailCRUDController crudAccertamento_pluriennale = new SimpleDetailCRUDController("AccertamentiPluriennali", Accertamento_pluriennaleBulk.class,"accertamentiPluriennali",this){
		public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {
			Accertamento_pluriennaleBulk riga = (Accertamento_pluriennaleBulk) getCrudAccertamento_pluriennale().getModel();
			super.validateForDelete(context,riga);

		}
	};



	// "editingScadenza" viene messo a True solo quando si modifica una scadenza (bottone "editing scadenza")
	private boolean editingScadenza = false;
	private boolean siope_attiva = false;
	private boolean enableVoceNext = false;



	public CRUDAccertamentoBP() {
	super();
	setTab("tab","tabAccertamento");			// Mette il fuoco sul primo TabAccertamento di Tab
	setTab("tabScadenzario","tabScadenza");
	setTab("tabVincoli","tabVincoliRisorseCopertura");
}
public CRUDAccertamentoBP(String function) 
{
    super(function);

    setTab("tab", "tabAccertamento");				// Mette il fuoco sul primo TabAccertamento di Tab
	setTab("tabScadenzario","tabScadenza");    
	setTab("tabVincoli","tabVincoliRisorseCopertura");
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @throws BusinessProcessException	
 * @throws ValidationException	
 */
public void addScadenza(it.cnr.jada.action.ActionContext context ) throws it.cnr.jada.action.BusinessProcessException, it.cnr.jada.bulk.ValidationException 
{
	editingScadenza = true;				// inizio modalita' inserimento scadenza
	getScadenzario().add(context);
}
public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException 
{
	
	super.basicEdit(context, bulk, doInitializeForEdit);

	if (getStatus()!=VIEW) {
		AccertamentoBulk accertamento = (AccertamentoBulk)getModel();
		if ( accertamento == null ) return;
		
		String cd_uo_scrivania = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa( context ).getCd_unita_organizzativa();
		if (!accertamento.getCd_uo_origine().equals(cd_uo_scrivania)) {
			if (!accertamento.isResiduo()) {
				setStatus(VIEW);
				setMessage("Accertamento creato dall'Unità Organizzativa " + accertamento.getCd_uo_origine() + ". Non consentita la modifica.");
			//QUESTO CONFRONTO E' POSSIBILE SOLO perchè i residui esistono per l'Ente e non per CDS (uo = 999.000)
			} else if (!cd_uo_scrivania.equals(accertamento.getCd_unita_organizzativa())) {
				setStatus(VIEW);
				setMessage("Accertamento creato dall'Unità Organizzativa " + accertamento.getCd_uo_origine() + ". Non consentita la modifica.");
			}
		}

		if ( accertamento.getDt_cancellazione() != null ) {
			setStatus(VIEW);
			setMessage("Accertamento cancellato. Non consentita la modifica.");
		}
	}
}
/**
 * Valida il contratto riportato.
 * @param context Il contesto dell'azione
 */ 
public void validaContratto(it.cnr.jada.action.ActionContext context,ContrattoBulk contratto) throws it.cnr.jada.action.BusinessProcessException, it.cnr.jada.action.MessageToUser
{
	try 
	{
		AccertamentoBulk accertamento = ((AccertamentoBulk)getModel());
		((AccertamentoComponentSession)createComponentSession()).validaContratto( context.getUserContext(), accertamento, contratto, null);		
	} 
	catch(Exception e) {throw handleException(e);}
}
/**
 * Carica le linee di attività associate al capitolo selezionato nell'accertamento.
 * @param context Il contesto dell'azione
 */ 
public void caricaLineeAttivita(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException, it.cnr.jada.action.MessageToUser
{
	try 
	{
		annullaImputazioneFinanziariaLatt( context );

		Vector capitoli =  new Vector();
		Vector cdr =  new Vector(getCentriDiResponsabilita().getSelectedModels( context ));	
//		if ( cdr.size() == 0 )
//			throw new MessageToUser("E' necessario selezionare almeno un CdR");				

		AccertamentoBulk accertamento = ((AccertamentoBulk)getModel());
		capitoli.add(accertamento.getCapitolo());
		accertamento.setCapitoliDiEntrataCdsSelezionatiColl( capitoli );
		accertamento.setCdrSelezionatiColl( cdr );

		Vector lineeAttivita = ((AccertamentoComponentSession)createComponentSession()).listaLineeAttivitaPerCapitolo( context.getUserContext(), accertamento );
		accertamento.setLineeAttivitaColl( lineeAttivita );
		accertamento.setInternalStatus( ObbligazioneBulk.INT_STATO_CDR_CONFERMATI );		
		resyncChildren( context );
	} 
	catch(Exception e) {throw handleException(e);}
}
/**
 * Conferma la scadenza dell'accertamento.
 * @param context Il contesto dell'azione
 */
public void confermaScadenza(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) getScadenzario().getModel();
		AccertamentoBulk accertamento = (AccertamentoBulk) getModel();
		AccertamentoComponentSession session = (AccertamentoComponentSession) createComponentSession();
		
		IDocumentoAmministrativoBP docAmmBP = null;
		// Se provengo da BP dei doc amm imposto il flag fromDocAmm a true
		if ( IDocumentoAmministrativoBP.class.isAssignableFrom( getParent().getClass()))
		{
			docAmmBP = (IDocumentoAmministrativoBP) getParent();  			
			accertamento.setFromDocAmm( true );
			accertamento.updateScadenzeFromDocAmm( docAmmBP.getDocumentoAmministrativoCorrente().getAccertamentiHash()); 			
		}	
		else
			accertamento.setFromDocAmm( false );
			
		scadenza.validaImporto(docAmmBP);
		scadenza.validate();
			
		// Eventuale aggiornamento della scadenza successiva e creazione/rigenerazione dettagli scadenza
		accertamento = session.verificaScadenzarioAccertamento( context.getUserContext(), scadenza );

		// Riassegno l'accertamento eventualmente modificato dal server		
		setModel( context, accertamento );		

		// Fine modalita' modifica/inserimento scadenza		
		editingScadenza = false;		
	} catch(ValidationException e) 
	{
		throw new MessageToUser(e.getMessage());
	}	
	catch(Exception e) 
	{
		throw handleException(e);
	}
}

/**
 * Metodo per modificare l'accertamento.
 * @param context Il contesto dell'azione
 * @param bulk L'oggetto bulk in uso
 * @param true/false TRUE = l'oggetto bulk in uso è stato inizializzato per la modifica
 *					 FALSE = l'oggetto bulk in uso non è stato inizializzato per la modifica
 */
public void edit(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk, boolean initializeForEdit) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		super.edit(context, bulk, initializeForEdit);
		AccertamentoBulk accertamento = (AccertamentoBulk) getModel();
		getCentriDiResponsabilita().setSelection( ((Vector)accertamento.getCdrSelezionatiColl()).elements());
		getLineeDiAttivita().setSelection( ((Vector)accertamento.getLineeAttivitaSelezionateColl()).elements());
	
		if(accertamento.getDt_cancellazione() != null)
	 		setMessage("Accertamento cancellato !");
	} 
	catch(Throwable e) 
	{
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
/**
 * Per modificare la scadenza dell'accertamento.
 * @param context Il contesto dell'azione
 */
public void editaScadenza(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	editingScadenza = true;			// Inizio modalita' modifica scadenza
	Accertamento_scadenzarioBulk scad = (Accertamento_scadenzarioBulk) getScadenzario().getModel();
	Accertamento_scadenzarioBulk scadIniziale = new Accertamento_scadenzarioBulk();
	scadIniziale.setIm_scadenza( scad.getIm_scadenza());
	scadIniziale.setDt_scadenza_incasso( scad.getDt_scadenza_incasso());
	scadIniziale.setDs_scadenza( scad.getDs_scadenza());
	scad.setScadenza_iniziale( scadIniziale);	
}
public void save(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException , it.cnr.jada.bulk.ValidationException
{
	super.save( context );

	AccertamentoBulk accertamento = (AccertamentoBulk) getModel();
	getCentriDiResponsabilita().setSelection( ((Vector)accertamento.getCdrSelezionatiColl()).elements());
	getLineeDiAttivita().setSelection( ((Vector)accertamento.getLineeAttivitaSelezionateColl()).elements());
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @throws BusinessProcessException	
 * @throws ComponentException	
 * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
 * @throws ApplicationException	
 */
public void eliminaLogicamenteAccertamento(it.cnr.jada.action.ActionContext context ) throws it.cnr.jada.action.BusinessProcessException, it.cnr.jada.comp.ComponentException, java.rmi.RemoteException, it.cnr.jada.comp.ApplicationException
{
	AccertamentoBulk accertamento = (AccertamentoBulk)getModel();
		
//	if(!accertamento.validaScadenzePerCancellazione())
//		throw new it.cnr.jada.comp.ApplicationException( "Impossibile stornare accertamenti con documenti amministrativi associati");		
	
	accertamento = ((AccertamentoComponentSession)createComponentSession()).annullaAccertamento( context.getUserContext(), accertamento);
	setModel( context, accertamento );
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	
 * @param actionContext	L'ActionContext della richiesta
 * @throws BusinessProcessException	
 */
public void gestisciDettagliScadenzePerCambioLA(it.cnr.jada.UserContext context, ActionContext actionContext) throws it.cnr.jada.action.BusinessProcessException
{
	try  
	{
		AccertamentoBulk accertamento = ((AccertamentoBulk)getModel());

		accertamento = (AccertamentoBulk) ((AccertamentoComponentSession)createComponentSession()).generaDettagliScadenzaAccertamento(context, accertamento, null);
		setModel( actionContext, accertamento );
	} 
	catch(Exception e) {throw handleException(e);}
}
public OggettoBulk getBringBackModel() {

    Accertamento_scadenzarioBulk scadenzaSelezionata= (Accertamento_scadenzarioBulk) scadenzario.getModel();
    if (scadenzaSelezionata == null)
        throw new MessageToUser("E' necessario selezionare uno scadenziario", ERROR_MESSAGE);

    if (getParent() != null && (getParent() instanceof IDocumentoAmministrativoBP)) {
        AccertamentoBulk accertamento= (AccertamentoBulk) getModel();
        if (Numerazione_doc_contBulk.TIPO_ACR_PLUR.equalsIgnoreCase(accertamento.getCd_tipo_documento_cont()))
            throw new MessageToUser("L'accertamento non può essere di tipo \"pluriennale\"!", ERROR_MESSAGE);
        if (Numerazione_doc_contBulk.TIPO_ACR_SIST.equalsIgnoreCase(accertamento.getCd_tipo_documento_cont()))
            throw new MessageToUser("L'accertamento non può essere di tipo \"sistema\"!", ERROR_MESSAGE);

        IDocumentoAmministrativoBP docAmmBP= (IDocumentoAmministrativoBP) getParent();
        Accertamento_scadenzarioBulk scadCorrente= docAmmBP.getAccertamento_scadenziario_corrente();
        if (scadCorrente != null) {
            if ((docAmmBP instanceof CRUDNotaDiCreditoAttivaBP || docAmmBP instanceof CRUDNotaDiDebitoAttivaBP || docAmmBP instanceof CRUDNotaDiCreditoBP)
                && !scadCorrente.equalsByPrimaryKey(scadenzaSelezionata))
                throw new MessageToUser("La scadenza che si sta tentando di riportare NON è corretta! Selezionare \"" + scadCorrente.getDs_scadenza() + "\"", ERROR_MESSAGE);
            if (docAmmBP instanceof CRUDFatturaAttivaIBP) {
                CRUDFatturaAttivaIBP fatturaAttivaBP= (CRUDFatturaAttivaIBP) docAmmBP;
                Fattura_attiva_IBulk fatturaAttiva= (Fattura_attiva_IBulk) fatturaAttivaBP.getModel();
                if (!scadCorrente.equalsByPrimaryKey(scadenzaSelezionata)) {
                    if (fatturaAttiva.hasStorni() || fatturaAttiva.hasAddebiti() || fatturaAttivaBP.isDeleting())
                        throw new MessageToUser(
                            "Non è possibile modificare la scadenza associata al documento, perché esso ha degli addebiti o degli storni associati! Selezionare la scadenza \"" + scadCorrente.getDs_scadenza() + "\".",
                            ERROR_MESSAGE);
                    if (scadenzaSelezionata.getIm_associato_doc_amm() != null
                        && scadenzaSelezionata.getIm_associato_doc_amm().compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)) != 0
                        && !fatturaAttivaBP.isDeleting()) {
							if (!new Fattura_attiva_IBulk(
									scadenzaSelezionata.getAccertamento().getCd_cds_origine(),
									scadenzaSelezionata.getAccertamento().getCd_uo_origine(),
									scadenzaSelezionata.getEsercizio_doc_attivo(),
									scadenzaSelezionata.getPg_doc_attivo()
								).equalsByPrimaryKey(fatturaAttiva))
		                        throw new MessageToUser("Non è possibile collegare la scadenza \"" + scadenzaSelezionata.getDs_scadenza() + "\" perchè è già associata ad altri documenti amministrativi.", ERROR_MESSAGE);
                        }
                }
            } else if (docAmmBP instanceof CRUDDocumentoGenericoAttivoBP) {
                if (!scadCorrente.equalsByPrimaryKey(scadenzaSelezionata)) {
	                CRUDDocumentoGenericoAttivoBP docGenAttivoBP= (CRUDDocumentoGenericoAttivoBP) docAmmBP;
	                Documento_genericoBulk docGenAttivo = (Documento_genericoBulk) docGenAttivoBP.getModel();
                    if (scadenzaSelezionata.getIm_associato_doc_amm() != null
                        && scadenzaSelezionata.getIm_associato_doc_amm().compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)) != 0) {
	                        String cd_cds_doc_gen = (docGenAttivo.isFlagEnte()) ? 
	                        										docGenAttivo.getCd_cds() : 
	                        										scadenzaSelezionata.getAccertamento().getCd_cds_origine();
	                        String cd_uo_docgen = (docGenAttivo.isFlagEnte()) ? 
	                        										docGenAttivo.getCd_unita_organizzativa() : 
	                        										scadenzaSelezionata.getAccertamento().getCd_uo_origine();
                        	if (!new Documento_genericoBulk(
	                        	cd_cds_doc_gen,
	                        	scadenzaSelezionata.getCd_tipo_documento_amm(),
	                        	cd_uo_docgen,
	                        	scadenzaSelezionata.getEsercizio_doc_attivo(),
	                        	scadenzaSelezionata.getPg_doc_attivo()
                        	).equalsByPrimaryKey(docGenAttivo))
	                        throw new MessageToUser("Non è possibile collegare la scadenza \"" + scadenzaSelezionata.getDs_scadenza() + "\" perchè è già associata ad altri documenti amministrativi.", ERROR_MESSAGE);
                        }
                }
            }
            if (getParent() instanceof CRUDFatturaAttivaIBP || getParent() instanceof CRUDDocumentoGenericoAttivoBP) {
                if (new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP).compareTo(scadenzaSelezionata.getIm_scadenza()) == 0)
                    throw new MessageToUser("Non è possibile collegare la scadenza \"" + scadenzaSelezionata.getDs_scadenza() + "\" con importo 0.", ERROR_MESSAGE);
            }
        }
    }
    return scadenzario.getModel();
}
/**
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getLineeDiAttivita() {
	return lineeDiAttivita;
}
/**
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getScadenzario() {
	return scadenzario;
}
/**
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getScadenzarioDettaglio() {
	return scadenzarioDettaglio;
}
/**
 * Inizializza il modello per la modifica.
 * @param context Il contesto dell'azione
 * @param bulk L'oggetto bulk in uso
 * @return Oggetto Bulk L'oggetto bulk inizializzato
 */
public OggettoBulk initializeModelForEdit(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
	try {
		it.cnr.jada.ejb.CRUDComponentSession compSession = (getUserTransaction() == null) ?
																			createComponentSession() :
																			getVirtualComponentSession(context, false);
		bulk = compSession.inizializzaBulkPerModifica(
								context.getUserContext(),
								bulk.initializeForEdit(this,context));
		
		if (bulk instanceof AccertamentoResiduoBulk)
			((AccertamentoResiduoBulk)bulk).setIm_quota_inesigibile(((AccertamentoResiduoBulk)bulk).getIm_quota_inesigibile_ripartita());

		return bulk;
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
//
//	Abilito il bottone di ANNULLA RIPORTA documento solo se non ho scadenze in fase di modifica/inserimento
//

public boolean isBringbackButtonEnabled()
{
	return super.isBringbackButtonEnabled() && !isEditingScadenza();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'confermaScadenzaButtonEnabled'
 *
 * @return Il valore della proprietà 'confermaScadenzaButtonEnabled'
 * @throws BusinessProcessException	
 */
public boolean isConfermaScadenzaButtonEnabled() throws it.cnr.jada.action.BusinessProcessException 
{
	return ( getScadenzario().getModel() != null && isEditingScadenza() && isEditable());
}
//
//	Abilito il bottone di cancellazione documento solo se non ho scadenze 
//  in fase di modifica/inserimento e se il documento non e' gia' stato
// 	cancellato
//

public boolean isDeleteButtonEnabled() 
{
	return super.isDeleteButtonEnabled() && !isEditingScadenza() && (getModel() != null && ((AccertamentoBulk)getModel()).getDt_cancellazione() == null);	
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'editingScadenza'
 *
 * @return Il valore della proprietà 'editingScadenza'
 */
public boolean isEditingScadenza() 
{
	return editingScadenza;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'editScadenzaButtonEnabled'
 *
 * @return Il valore della proprietà 'editScadenzaButtonEnabled'
 * @throws BusinessProcessException	
 */
public boolean isEditScadenzaButtonEnabled() throws it.cnr.jada.action.BusinessProcessException 
{
	return ( getScadenzario().getModel() != null && !isEditingScadenza() && isEditable() && ((AccertamentoBulk)getModel()).getDt_cancellazione() == null);

}
//
//	Abilito il bottone di salvataggio documento solo se non ho scadenze 
//	in fase di modifica/inserimento oppure se il documento non e' stato
//	cancellato
//

public boolean isSaveButtonEnabled() 
{
	return super.isSaveButtonEnabled() && !isEditingScadenza() && (getModel() != null && ((AccertamentoBulk)getModel()).getDt_cancellazione() == null);
}
//
//	Abilito il bottone di RIPORTA documento solo se non ho scadenze in fase di modifica/inserimento
//

public boolean isUndoBringBackButtonEnabled() 
{
	return super.isUndoBringBackButtonEnabled() && !isEditingScadenza();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'undoScadenzaButtonEnabled'
 *
 * @return Il valore della proprietà 'undoScadenzaButtonEnabled'
 * @throws BusinessProcessException	
 */
public boolean isUndoScadenzaButtonEnabled() throws it.cnr.jada.action.BusinessProcessException 
{
	return ( getScadenzario().getModel() != null && isEditingScadenza() && isEditable());
}
/**
 * Inzializza il ricevente nello stato di INSERT.
 */
 
public void reset(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	editingScadenza = false;
	super.reset( context );
}
/**
 * Inzializza il ricevente nello stato di SEARCH.
 */
public void resetForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	editingScadenza = false;
	super.resetForSearch( context );
}
/* Metodo per riportare il fuoco sul tab iniziale */
protected void resetTabs(ActionContext context) {
	setTab( "tab", "tabAccertamento");
}
/**
 * Metodo per selezionare la scadenza dell'accertamento.
 * @param scadenza La scadenza dell'accertamento
 * @param context Il contesto dell'azione
 */
public void selezionaScadenza(Accertamento_scadenzarioBulk scadenza, it.cnr.jada.action.ActionContext context) {
	
	getScadenzario().setModelIndex( context, it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(getScadenzario().getDetails(), scadenza) );
	setTab("tab", "tabScadenziario");
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @throws BusinessProcessException	
 */
public void undoScadenza(it.cnr.jada.action.ActionContext context ) throws it.cnr.jada.action.BusinessProcessException 
{
	editingScadenza = false;		// Fine modalita' modifica/inserimento scadenza

	// Nel caso l'utente decida di fare un Undo di un inserimento non confermato 
	// rimuovo il dettaglio
	AccertamentoBulk accertamento = (AccertamentoBulk) getModel();
	Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) getScadenzario().getModel();

	//ripristino i dati iniziali
	if ( scadenza.getScadenza_iniziale() != null )
	{
		scadenza.setIm_scadenza( scadenza.getScadenza_iniziale().getIm_scadenza());
		scadenza.setDt_scadenza_incasso(scadenza.getScadenza_iniziale().getDt_scadenza_incasso());
		scadenza.setDs_scadenza( scadenza.getScadenza_iniziale().getDs_scadenza());
	}	

	
	int index = getScadenzario().getModelIndex();

	if ( scadenza.getStatus() == scadenza.STATUS_NOT_CONFIRMED )
	{
		scadenza.setToBeDeleted();
		accertamento.getAccertamento_scadenzarioColl().remove( index );
		getScadenzario().setModelIndex( context, -1 );			// altrimenti rimane abilitato il bottone di modifica
		resyncChildren( context );
	}
	getFieldValidationMap().clearAll(getScadenzario().getInputPrefix());	
/****************************************************	
	// Nel caso l'utente decida di fare un Undo di un inserimento senza valorizzare i campi
	// rimuovo il dettaglio
	AccertamentoBulk accertamento = (AccertamentoBulk) getModel();
	List scadenzeColl = getScadenzario().getDetails();

	for ( Iterator i = scadenzeColl.iterator(); i.hasNext();)
	{
		Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) i.next();
		
		if(	(scadenza.getDt_scadenza_incasso() == null) &&
			(scadenza.getIm_scadenza().compareTo( new BigDecimal(0)) == 0) &&
			(scadenza.getDs_scadenza() == null))
		{
			scadenza.setToBeDeleted();
			i.remove();
			getScadenzario().setModelIndex(-1);			// altrimenti rimane abilitato il bottone di modifica
		}	
	}
******************************/	
}
/**
 * Verifica la validità della nuova linea di attivita'
 * @param context Il contesto dell'azione
 */
public void validaLineaAttivita( ActionContext context, it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt ) throws ValidationException 
{
	try
	{
		AccertamentoBulk accertamento = (AccertamentoBulk) getModel();

		//tipo spese/entrate		
	    if (!it.cnr.contab.config00.latt.bulk.WorkpackageBulk.TI_GESTIONE_ENTRATE.equals(latt.getTi_gestione())) 
		    throw new ValidationException( "Non e' possibile selezionare un GAE con tipo gestione SPESE");
		    
		//cdr
		if ( !latt.getCd_centro_responsabilita().startsWith( accertamento.getCd_uo_origine()))
		    throw new ValidationException( "Non e' possibile selezionare un GAE con CDR non appartenente all'unità organizzativa di scrivania");
		    
		//natura
		Vector nature = ((AccertamentoComponentSession)createComponentSession()).listaCodiciNaturaPerCapitolo(context.getUserContext(),(AccertamentoBulk) getModel());
		boolean found = false;
		Ass_ev_evBulk ass;
		for ( Iterator i = nature.iterator(); i.hasNext(); )
		{
			ass = (Ass_ev_evBulk) i.next();
			if ( ass.getCd_natura().equals( latt.getCd_natura()))
			{
				found = true;
				break;
			}		
		}
		if ( !found )
			throw new ValidationException( "Il GAE selezionato ha una natura non compatibile con il capitolo");
	} 
	catch(Throwable e) 
	{
			throw new ValidationException(e.getMessage());
	}
}
/**
 * Verifica la validità dell'accertamento.
 * @param context Il contesto dell'azione
 */
public void validate(ActionContext context) throws ValidationException {

	super.validate(context);
	if (isEditOnly() && !isSaveOnBringBack())
		try	{
			((AccertamentoComponentSession)createComponentSession()).verificaAccertamento(context.getUserContext(),(AccertamentoBulk) getModel());
		} catch(Throwable e) {
			throw new ValidationException(e.getMessage());
		}
}
/**
 * Verifica se il bottone di Visualizzazione delle Spese del Cdr è abilitato.
 * @return 				TRUE 	Il bottone di Visualizzazione delle Spese del Cdr è abilitato
 *						FALSE 	Il bottone di Visualizzazione delle Spese del Cdr non è abilitato
 */
public boolean isVisualizzaSpeseCdrButtonEnabled()
{
	//return getModel.getInternalStatus() >= INT_STATO_CDR_CONFERMATI ;
	return getCentriDiResponsabilita().getModel() != null;
}	
/**
 * Metodo con cui si ottiene il valore della variabile <code>centriDiResponsabilita</code>
 * di tipo <code>SimpleDetailCRUDController</code>.
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getCentriDiResponsabilita() {
	return centriDiResponsabilita;
}
/**
 * Metodo con cui si ottiene il valore della variabile <code>nuoveLineeDiAttivita</code>
 * di tipo <code>SimpleDetailCRUDController</code>.
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getNuoveLineeDiAttivita() {
	return nuoveLineeDiAttivita;
}
/**
 * Gestisce l'annullamento dell'imputazione finanziaria
 * @param context Il contesto dell'azione
 */

private void annullaImputazioneFinanziariaCdr(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try {
		annullaImputazioneFinanziariaLatt( context );
		AccertamentoBulk accertamento = (AccertamentoBulk) getModel();		
		accertamento.setCdrColl( Collections.EMPTY_LIST );
		accertamento.setCdrSelezionatiColl(Collections.EMPTY_LIST );
		getCentriDiResponsabilita().getSelection().clear();
		setModel( context, accertamento );		
		resyncChildren( context );
		accertamento.setInternalStatus( AccertamentoBulk.INT_STATO_CAPITOLI_CONFERMATI );		

	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Gestisce l'annullamento dell'imputazione finanziaria
 * @param context Il contesto dell'azione
 */

private void annullaImputazioneFinanziariaLatt(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try {
		AccertamentoBulk accertamento = (AccertamentoBulk) getModel();
		accertamento.setLineeAttivitaColl( Collections.EMPTY_LIST );
		accertamento.setLineeAttivitaSelezionateColl(Collections.EMPTY_LIST );
		accertamento.setNuoveLineeAttivitaColl( new BulkList() );
		getLineeDiAttivita().getSelection().clear();
		annullaImputazioneFinanziariaDettagli( context );
		setModel( context, accertamento );		
		resyncChildren( context );
		accertamento.setInternalStatus( AccertamentoBulk.INT_STATO_CDR_CONFERMATI );		
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Gestisce l'annullamento dell'imputazione finanziaria
 * @param context Il contesto dell'azione
 */

private void annullaImputazioneFinanziariaDettagli(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try {
		AccertamentoBulk accertamento = (AccertamentoBulk) getModel();
		Accertamento_scad_voceBulk osv;
		Accertamento_scadenzarioBulk os;		
		for ( Iterator i = accertamento.getAccertamento_scadenzarioColl().iterator(); i.hasNext(); )
		{
			os = (Accertamento_scadenzarioBulk) i.next();
			for ( int index = os.getAccertamento_scad_voceColl().size() - 1; index >= 0 ; index--)
			{
				osv = (Accertamento_scad_voceBulk) os.getAccertamento_scad_voceColl().get( index );
				osv.setToBeDeleted();
				os.getAccertamento_scad_voceColl().remove( index );
			}			
		}
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Gestisce l'annullamento dell'imputazione finanziaria
 * @param context Il contesto dell'azione
 */
/**
 * Gestisce il caricamento dei centri di responsabilità.
 * @param context Il contesto dell'azione
 */

public void caricaCentriDiResponsabilita(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		annullaImputazioneFinanziariaCdr( context );
		AccertamentoBulk accertamento = ((AccertamentoBulk)getModel());
		BulkList capitoli = new BulkList();
		capitoli.add(accertamento.getCapitolo());
		accertamento.setCapitoliDiEntrataCdsSelezionatiColl( capitoli );
		Vector cdr = ((AccertamentoComponentSession)createComponentSession()).listaCdrPerCapitoli( context.getUserContext(), accertamento );
		accertamento.setCdrColl( cdr );
		accertamento.setLineeAttivitaColl( Collections.EMPTY_LIST );				
//		setModel( accertamento );
		accertamento.setInternalStatus( AccertamentoBulk.INT_STATO_CAPITOLI_CONFERMATI );
		resyncChildren( context );
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Metodo utilizzato per la conferma dei dati selezionati o immessi, relativi
 * alle linee di attività.
 * @param context Il contesto dell'azione
 */

public void confermaLineeAttivita(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try {
		AccertamentoBulk accertamento = ((AccertamentoBulk)getModel());		
		Vector latt =  new Vector(getLineeDiAttivita().getSelectedModels( context ));
		accertamento.setLineeAttivitaSelezionateColl( latt );
		BulkList nuoveLatt =  new BulkList(getNuoveLineeDiAttivita().getDetails());
		for ( Iterator i = nuoveLatt.iterator(); i.hasNext(); )
			((Linea_attivitaBulk) i.next()).validate();
		accertamento.setNuoveLineeAttivitaColl( nuoveLatt );
//		accertamento = ((AccertamentoComponentSession)createComponentSession()).verificaImputazioneFinanziaria( context.getUserContext(), accertamento );		
		accertamento = ((AccertamentoComponentSession)createComponentSession()).generaDettagliScadenzaAccertamento( context.getUserContext(), accertamento, null );
//		getLineeDiAttivita().setSelection( ((Vector)accertamento.getLineeAttivitaSelezionateColl()).elements());		
		setModel( context, accertamento );
		accertamento.setInternalStatus( AccertamentoBulk.INT_STATO_LATT_CONFERMATE );
		resyncChildren( context );
	} catch(ValidationException e) {
		throw new MessageToUser(e.getMessage());
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Gestisce il caricamento dei capitoli di spesa.
 * @param context Il contesto dell'azione
 */
/*
public void caricaCapitoliDiSpesaCDS(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try {
		annullaImputazioneFinanziariaCapitoli( context );		
//MITODO		AccertamentoBulk accertamento = ((AccertamentoComponentSession)createComponentSession()).listaCapitoliPerCdsVoce( context.getUserContext(), (AccertamentoBulk) getModel());
//		setModel( context, accertamento );
	} catch(Exception e) {
		throw handleException(e);
	}
}
*/
/**
 * Gestisce l'annullamento dell'imputazione finanziaria
 * @param context Il contesto dell'azione
 */

public void annullaImputazioneFinanziariaCapitoli(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try {
		annullaImputazioneFinanziariaCdr( context );
		AccertamentoBulk accertamento = (AccertamentoBulk) getModel();
		accertamento.setCapitoliDiEntrataCdsColl(Collections.EMPTY_LIST );
		accertamento.setCapitoliDiEntrataCdsSelezionatiColl(Collections.EMPTY_LIST );		
		//getCapitoliDiSpesaCds().getSelection().clear();
		setModel( context, accertamento );		
		resyncChildren( context );
		accertamento.setInternalStatus( AccertamentoBulk.INT_STATO_TESTATA_CONFERMATA );		
		
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Gestisce il cambio del flag imputazione finanziaria automatica o manuale 
 * dell'obbligazione.
 * @param context Il contesto dell'azione
 */

public void cambiaFl_calcolo_automatico(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try {
		AccertamentoBulk accertamento = ((AccertamentoBulk)getModel());
		if ( accertamento.getFl_calcolo_automatico().booleanValue() )
		accertamento = ((AccertamentoComponentSession)createComponentSession()).generaDettagliScadenzaAccertamento( context.getUserContext(), accertamento, null );
		setModel( context, accertamento );
		resyncChildren( context );
	} catch(Exception e) {
		throw handleException(e);
	}
}
/** 
  * Viene richiesta alla component che gestisce l'obbligazione di verificare la validità
  *	 della nuova Linea di Attività
  */
public void validaNuovaLineaAttivita(ActionContext context, it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk nuovaLatt, it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt) throws ValidationException 
{
	try	
	{
		if (latt != null )
		{
			nuovaLatt.getAccertamento().validateNuovaLineaAttivita( nuovaLatt, latt );
			// nel component della obbligazione il metodo è vuoto quindi non lo riporto
			//((AccertamentoComponentSession)createComponentSession()).verificaNuovaLineaAttivita(context.getUserContext(), latt );
			nuovaLatt.setLinea_att( latt );			
		}	

	} catch(Throwable e) {
			throw new ValidationException(e.getMessage());
		}
}
public boolean modificaAccertamentoResProprie(it.cnr.jada.action.ActionContext context, StringBuffer errControllo) throws BusinessProcessException {
	AccertamentoBulk accertamento = (AccertamentoBulk) getModel();
	String errore;
	if (accertamento.isAccertamentoResiduo()) {
		
		try {
			errore = ((AccertamentoResiduoComponentSession)createComponentSession()).controllaDettagliScadenzaAccertamento( context.getUserContext(), accertamento, null );
		} catch(Exception e) {
			throw handleException(e);
		}
		errControllo.append(errore);
		return true;
	}
	return false;
}
public boolean isModAccResButtonHidden() {
	AccertamentoBulk accertamento = (AccertamentoBulk) getModel();
	if (accertamento.isAccertamentoResiduo())
		return false;
	return true;
}
public boolean isModAccResButtonEnabled() {
	AccertamentoBulk accertamento = (AccertamentoBulk) getModel();
	if (accertamento.isAccertamentoResiduo() && !(isBringBack()))
		return true;
   return false;	
}
public void annullaScadenza(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try {
		Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) getScadenzario().getModel();
		AccertamentoBulk accertamento = (AccertamentoBulk) getModel();
		AccertamentoComponentSession session = (AccertamentoComponentSession) createComponentSession();
	
		if (accertamento.isAccertamentoResiduo() && scadenza.getIm_scadenza().compareTo(Utility.ZERO)!=0) {
			editaScadenza(context);
			
			// annulliamo la scadenza
			scadenza.setIm_scadenza(Utility.ZERO);
			if (scadenza.getPg_doc_attivo()!=null)
				scadenza.setIm_associato_doc_amm(scadenza.getIm_associato_doc_amm().subtract(scadenza.getScadenza_iniziale().getIm_scadenza()));
			scadenza.setToBeUpdated();
			getScadenzario().getDetails().set(getScadenzario().getModelIndex(),scadenza);
			for (int i = 0; i<getScadenzarioDettaglio().getDetails().size();i++) {
				Accertamento_scad_voceBulk scadVoce = (Accertamento_scad_voceBulk) getScadenzarioDettaglio().getDetails().get(i);
				scadVoce.setIm_voce(Utility.ZERO);
				scadVoce.setToBeUpdated();
				getScadenzarioDettaglio().getDetails().set(i,scadVoce);
			}

			// poi verifico, come farebbe il tasto di conferma
			accertamento = session.verificaScadenzarioAccertamento(context.getUserContext(), scadenza, false);

			accertamento.setIm_accertamento(accertamento.getIm_accertamento().subtract(scadenza.getScadenza_iniziale().getIm_scadenza()));
			// Riassegno l'accertamento eventualmente modificato dal server		
			setModel( context, accertamento );

			confermaScadenza(context);
		}
	}
	catch(Exception e) 
	{
		throw handleException(e);
	}
}
public boolean isAnnullaScadenzaButtonEnabled() throws it.cnr.jada.action.BusinessProcessException 
{
	return false;
}
public boolean isROImporto() {
	return false;
}
protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
	super.initialize(actioncontext);
	try {
		Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())); 
		setSiope_attiva(parCnr.getFl_siope().booleanValue());

		if (parCnr.isEnableVoceNext()) {
			Parametri_cnrBulk parCnrNewAnno = Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())+1); 
			setEnableVoceNext(parCnrNewAnno!=null);
		}
	}
    catch(Throwable throwable)
    {
        throw new BusinessProcessException(throwable);
    }
}
private boolean isSiope_attiva() {
	return siope_attiva;
}
private void setSiope_attiva(boolean siope_attiva) {
	this.siope_attiva = siope_attiva;
}
public boolean isROCapitolo() {
	AccertamentoBulk accertamento = (AccertamentoBulk)getModel();
	if (isSiope_attiva())
		return accertamento.isROCapitolo()||accertamento.isAssociataADocCont();
	return accertamento.isROCapitolo();
}
public boolean isROFindCapitolo() {
	AccertamentoBulk accertamento = (AccertamentoBulk)getModel();
	if (isSiope_attiva())
		return accertamento.isAssociataADocCont();
	return accertamento.isROCapitolo();
}

public boolean isEnableVoceNext() {
	return enableVoceNext;
}

private void setEnableVoceNext(boolean enableVoceNext) {
	this.enableVoceNext = enableVoceNext;
}

public boolean isElementoVoceNewVisible() {
	return this.isEnableVoceNext() && getModel()!=null && 
			((AccertamentoBulk)getModel()).isEnableVoceNext();
}
public SimpleDetailCRUDController getPdgVincoli() {
	return pdgVincoli;
}
public SimpleDetailCRUDController getPdgVincoliPerenti() {
	return pdgVincoliPerenti;
}
public void copiaAccertamento(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try {
		AccertamentoBulk nuovo = (AccertamentoBulk)((AccertamentoBulk)getModel()).clona(this, context);
		nuovo= (AccertamentoBulk)createComponentSession().inizializzaBulkPerInserimento(context.getUserContext(), nuovo);
		nuovo.setUser( context.getUserInfo().getUserid());
		setModel( context, nuovo);
		((AccertamentoBulk)nuovo).setInternalStatus( AccertamentoBulk.INT_STATO_LATT_CONFERMATE );
		setStatus( INSERT );
		resyncChildren( context );
		setTab("tab","tabAccertamento");
		setTab("tabScadenzario","tabScadenzario");
	} catch(Exception e) {
		throw handleException(e);
	}
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() 
{
		
	Button[] toolbar = super.createToolbar();
	Button[] newToolbar = new Button[ toolbar.length + 1 ];
	for ( int i = 0; i< toolbar.length; i++ )
		newToolbar[ i ] = toolbar[ i ];
	newToolbar[ toolbar.length ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.copia");
	return newToolbar;
}
public boolean isCopiaAccertamentoButtonEnabled() {
	AccertamentoBulk accertamento = (AccertamentoBulk)getModel();
	return isEditable() && isEditing() && !isEditingScadenza() && !accertamento.isAccertamentoResiduo() ;
}

public boolean isCopiaAccertamentoButtonHidden() {
	
	return isEditOnly();
}

	public SimpleDetailCRUDController getCrudAccertamento_pluriennale() {
		return crudAccertamento_pluriennale;
	}


}