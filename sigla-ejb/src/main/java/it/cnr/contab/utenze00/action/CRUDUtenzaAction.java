package it.cnr.contab.utenze00.action;

/**
 * Action che gestisce l'interfaccia di Gestione Utenza Comune e Gestione Template di Utente: in particolare gestisce 
 * l'aggiunta e la rimozione di un accesso e/o ruolo ad un Utente Comune o ad un template
 *	
 */

import java.rmi.RemoteException;

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.doccont00.bp.CRUDAccertamentoBP;
import it.cnr.contab.doccont00.bp.CRUDAccertamentoModificaBP;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.utenze00.bp.CRUDUtenzaBP;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;
public class CRUDUtenzaAction extends it.cnr.jada.util.action.CRUDAction {

	
	/**
	 * CRUDAnagraficaAction constructor comment.
	 */
	public CRUDUtenzaAction() {
		super();
	}
/**
 * Aggiunge un nuovo accesso alla lista di Accessi associati ad un Utente
 * @param context contesto dell'azione
 * @return it.cnr.jada.action.Forward forward successivo  
 */
public Forward doAggiungiAccesso(ActionContext context) 
{
	CRUDUtenzaBP bp = (CRUDUtenzaBP)context.getBusinessProcess();
	UtenteTemplateBulk utente = (UtenteTemplateBulk)bp.getModel();
	int[] indexes = bp.getCrudAccessi_disponibili().getSelectedRows(context);
	
	java.util.Arrays.sort( indexes );
	for (int i = indexes.length - 1 ;i >= 0 ;i--) 
	{	
		Utente_unita_accessoBulk uua = utente.addToUtente_unita_accessi(indexes[i]);
		uua.setToBeCreated();
		uua.setUser(context.getUserInfo().getUserid());
	}
	bp.getCrudAccessi_disponibili().getSelection().clearSelection();
	return context.findDefaultForward();
}
/**
 * Aggiunge un nuovo ruolo alla lista di Accessi associati ad un Utente
 * @param context contesto dell'azione
 * @return it.cnr.jada.action.Forward forward successivo  
 */
public Forward doAggiungiRuolo(ActionContext context) 
{
	CRUDUtenzaBP bp = (CRUDUtenzaBP)context.getBusinessProcess();
	UtenteTemplateBulk utente = (UtenteTemplateBulk)bp.getModel();
	int[] indexes = bp.getCrudRuoli_disponibili().getSelectedRows(context);
	java.util.Arrays.sort( indexes );
	for (int i = indexes.length - 1 ;i >= 0 ;i--) 	
	{
		Utente_unita_ruoloBulk uur = utente.addToUtente_unita_ruoli(indexes[i]);
		uur.setToBeCreated();
		uur.setUser(context.getUserInfo().getUserid());
	}
	bp.getCrudRuoli_disponibili().getSelection().clearSelection();
	return context.findDefaultForward();
}
/**
 * Come conseguenza della azzeramento nell'interfaccia utente dell'Unita Organizzativa, vengono azzerati anche
 * gli eventuali Accessi che erano stati visualizzati in relazione a tale Unita Organizzativa.
 * @param context contesto dell'azione
 * @return it.cnr.jada.action.Forward forward successivo  
 */

public Forward doBlankSearchFind_uo_per_accesso(ActionContext context, OggettoBulk bulk )  throws java.rmi.RemoteException 
{
	try
	{
		CRUDUtenzaBP bp = (CRUDUtenzaBP)context.getBusinessProcess();		
		bp.resetAccessi(context);	
		return context.findDefaultForward();
	} catch(Exception ex) 
	{
			return handleException(context,ex);
	}
	
}
/**
 * Come conseguenza della azzeramento nell'interfaccia utente dell'Unita Organizzativa, vengono azzerati anche
 * gli eventuali Ruoli che erano stati visualizzati in relazione a tale Unita Organizzativa.
 * @param context contesto dell'azione
 * @return it.cnr.jada.action.Forward forward successivo  
 */

public Forward doBlankSearchFind_uo_per_ruolo(ActionContext context, OggettoBulk bulk )  throws java.rmi.RemoteException 
{
	try
	{
		CRUDUtenzaBP bp = (CRUDUtenzaBP)context.getBusinessProcess();		
		bp.resetRuoli(context);	
		return context.findDefaultForward();
	} catch(Exception ex) 
	{
			return handleException(context,ex);
	}
	
}
/**
 * Imposta all'Utente l'Unita Organizzativa per la quale si intendono gestire gli accessi e cerca gli eventuali accessi 
 * gia' definiti per questo Utente e Unita Organizzativa
 * @param context contesto dell'azione
 * @param utente istanza di UtenteTemplateBulk o di UtenteComuneBulk
 * @param uo Unita_organizzativaBulk
 * @return it.cnr.jada.action.Forward forward successivo  
 */

public Forward doBringBackSearchFind_uo_per_accesso(ActionContext context,UtenteTemplateBulk utente, Unita_organizzativaBulk uo )  throws java.rmi.RemoteException 
{
	try
	{
		CRUDUtenzaBP bp = (CRUDUtenzaBP)context.getBusinessProcess();		
		if ( utente!= null )
			utente.setUnita_org_per_accesso( uo );
		bp.cercaAccessi(context);	
		return context.findDefaultForward();
	} catch(Exception ex) 
	{
			return handleException(context,ex);
	}
	
}
/**
 * Imposta all'Utente l'Unita Organizzativa per la quale si intendono gestire i ruoli e cerca gli eventuali ruoli
 * gia' definiti per questo Utente e UnitaOrganizzativa
 * @param context contesto dell'azione
 * @param utente istanza di UtenteTemplateBulk o di UtenteComuneBulk
 * @param uo Unita_organizzativaBulk
 * @return it.cnr.jada.action.Forward forward successivo  
 */

public Forward doBringBackSearchFind_uo_per_ruolo(ActionContext context,UtenteTemplateBulk utente, Unita_organizzativaBulk uo )  throws java.rmi.RemoteException 
{
	try
	{
		CRUDUtenzaBP bp = (CRUDUtenzaBP)context.getBusinessProcess();		
		if ( utente!= null )
			utente.setUnita_org_per_ruolo( uo );
		bp.cercaRuoli(context);	
		return context.findDefaultForward();
	} catch(Exception ex) 
	{
			return handleException(context,ex);
	}
	
}
/**
 * Gestisce un comando di cancellazione di utente per visualizzare un messagio user consistente.
 */

// Fixx errore CNR n. 46 - 15/02/2002
 
public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {
 super.doElimina(context);
 CRUDBP bp = getBusinessProcess(context);
 bp.setMessage("Cancellazione effettuata. E' stata modificata la data di fine validità dell'utente.");
 return context.findDefaultForward();
}
/**
 * Gestisce un comando di stampa degli Utenti
 */
public Forward doPrint(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();

		fillModel(context);
		if (bp.isDirty())
			return openContinuePrompt(context,"doConfirmPrint");
		it.cnr.contab.reports.bp.OfflineReportPrintBP printbp = (it.cnr.contab.reports.bp.OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/configurazione/utenze/utente.jasper");
		UtenteBulk aUtente = (UtenteBulk)bp.getModel();
		if(aUtente.getCd_utente() == null)
		 throw new MessageToUser("Nessun utente specificato");
		
		Print_spooler_paramBulk param;
		param = new Print_spooler_paramBulk();
		param.setNomeParam("utente");
		param.setValoreParam(aUtente.getCd_utente());
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
		
		return context.addBusinessProcess(printbp);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Aggiunge un nuovo accesso alla lista di Accessi associati ad un Utente
 * @param context contesto dell'azione
 * @return it.cnr.jada.action.Forward forward successivo  
 */
public Forward doResetPassword(ActionContext context) 
{
	try
	{
		CRUDBP bp = (CRUDBP)context.getBusinessProcess();
		if ( bp instanceof it.cnr.contab.utenze00.bp.CRUDUtenzaAmministratoreBP )
			((it.cnr.contab.utenze00.bp.CRUDUtenzaAmministratoreBP)bp).resetPassword( context );
		else if ( bp instanceof CRUDUtenzaBP )
			((CRUDUtenzaBP)bp).resetPassword( context );		
		bp.setMessage( "Password annullata correttamente" );
		return context.findDefaultForward();
	}
	catch (Exception e )
	{
		return handleException( context, e );
	}	
}
/**
 * Rimuove un accesso dalla lista di Accessi associati ad un Utente
 * @param context contesto dell'azione
 * @return it.cnr.jada.action.Forward forward successivo  
 */
public Forward doRimuoviAccesso(ActionContext context) {
	CRUDUtenzaBP bp = (CRUDUtenzaBP) context.getBusinessProcess();
	UtenteTemplateBulk utente = (UtenteTemplateBulk) bp.getModel();
	int indexes[] = bp.getCrudAccessi().getSelectedRows(context);
	java.util.Arrays.sort(indexes);
	for (int i = indexes.length - 1; i >= 0; i--) {
		Utente_unita_accessoBulk uua =
			utente.removeFromUtente_unita_accessi(indexes[i]);
		uua.setToBeDeleted();
	}
	bp.getCrudAccessi().reset(context);
	return context.findDefaultForward();
}
/**
 * Rimuove un ruolo dalla lista di Ruoli associati ad un Utente
 * @param context contesto dell'azione
 * @return it.cnr.jada.action.Forward forward successivo  
 */

public Forward doRimuoviRuolo(ActionContext context) {
	CRUDUtenzaBP bp = (CRUDUtenzaBP)context.getBusinessProcess();
	UtenteTemplateBulk utente = (UtenteTemplateBulk)bp.getModel();
	int indexes[] = bp.getCrudRuoli().getSelectedRows(context);
	java.util.Arrays.sort( indexes );
	for (int i = indexes.length - 1 ;i >= 0 ;i--) 	
	{
		Utente_unita_ruoloBulk uur = utente.removeFromUtente_unita_ruoli(indexes[i]);
		uur.setToBeDeleted();
	}
	bp.getCrudRuoli().reset(context);
	return context.findDefaultForward();
}
/**
 * Gestisce un cambiamento di pagina su un controllo tabbed {@link it.cnr.jada.util.jsp.JSPUtils.tabbed}
   	 * @param context <code>ActionContext</code> in uso.
	 * @param tabName Nome del tab in uso.
	 * @param pageName Nome della pagina in uso.
	 *
	 * @return <code>Forward</code>
 */
public Forward doTab(ActionContext context,String tabName,String pageName) 
{
	try
	{
		fillModel( context );
		CRUDUtenzaBP bp = (CRUDUtenzaBP)getBusinessProcess(context);
		if ( bp.isInserting() )
		{
			bp.setMessage( "E' necessario salvare i dati, prima di procedere alla definizione di accessi e ruoli." );
			return context.findDefaultForward();
		}	
		return super.doTab( context, tabName, pageName );
	}
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
public Forward doVerificaAbilitazioneUtenteLdap(ActionContext context) 
{
	try
	{
		fillModel( context );
		CRUDUtenzaBP bp = (CRUDUtenzaBP)getBusinessProcess(context);
		if ( bp.isInserting() )
		{
			bp.setMessage( "E' necessario salvare i dati, prima di cambiare l'abilitazione dell'utente" );
			return context.findDefaultForward();
		}
		boolean isAbilitato = bp.isUtenteAbilitatoLdap(context.getUserContext());
		String message = null;
		if (isAbilitato) {
			message = "L'utente è abilitato ad accedere in SIGLA, si vuole disabilitarne l'accesso?";
			openConfirm(context, message, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doDisabilitaUtenteLdap");
		}
		else {
			message = "L'utente non è abilitato ad accedere in SIGLA, si vuole abilitarne l'accesso?";
			openConfirm(context, message, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doAbilitaUtenteLdap");
		}
		return context.findDefaultForward();
	}
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
public Forward doAbilitaUtenteLdap(ActionContext context, int opt) throws RemoteException {
	try 
	{
		CRUDUtenzaBP bp = (CRUDUtenzaBP)getBusinessProcess(context);
		if (opt == OptionBP.YES_BUTTON) {
			bp.cambiaAbilitazioneUtente(context.getUserContext(),true);
		}
	} catch(Exception e) {
		return handleException(context,e);
	}
	return context.findDefaultForward();
} 
public Forward doDisabilitaUtenteLdap(ActionContext context, int opt) throws RemoteException {
	try 
	{
		CRUDUtenzaBP bp = (CRUDUtenzaBP)getBusinessProcess(context);
		if (opt == OptionBP.YES_BUTTON) {
			bp.cambiaAbilitazioneUtente(context.getUserContext(),false);
		}
	} catch(Exception e) {
		return handleException(context,e);
	}
	return context.findDefaultForward();
}
public Forward doResetInutilizzo(ActionContext context) 
{
	try
	{
		CRUDBP bp = (CRUDBP)context.getBusinessProcess();
		if ( bp instanceof it.cnr.contab.utenze00.bp.CRUDUtenzaBP )
			((it.cnr.contab.utenze00.bp.CRUDUtenzaBP)bp).resetInutilizzo( context );
		bp.setMessage( "Utenza riattivata!" );
		return context.findDefaultForward();
	}
	catch (Exception e )
	{
		return handleException( context, e );
	}	
}

}
