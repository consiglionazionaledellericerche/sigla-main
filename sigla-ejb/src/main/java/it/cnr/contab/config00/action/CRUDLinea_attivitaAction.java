package it.cnr.contab.config00.action;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.ejb.*;
import it.cnr.contab.config00.comp.*;
import it.cnr.contab.config00.bp.*;
import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.contab.progettiric00.core.bulk.*;
import it.cnr.contab.progettiric00.bp.*;
import it.cnr.contab.config00.blob.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.upload.UploadedFile;

import java.io.*;
/**
 * <!-- @TODO: da completare -->
 */

public class CRUDLinea_attivitaAction extends it.cnr.jada.util.action.CRUDAction {
	//Dimensione massima ammessa per il File
	private static final long lunghezzaMax=0x1000000;
public CRUDLinea_attivitaAction() {
	super();
}

/**
 * Gestisce una richiesta di ricerca del searchtool "centro_responsabilita"
 *
 * @param context	L'ActionContext della richiesta
 * @param linea_attivita	L'OggettoBulk padre del searchtool
 * @param cdr	L'OggettoBulk selezionato dall'utente
 * @return Il Forward alla pagina di risposta
 */
public Forward doBlankSearchCentro_responsabilita(ActionContext context,WorkpackageBulk linea_attivita) {
	try {
		linea_attivita.setCentro_responsabilita(new CdrBulk());
		linea_attivita.setInsieme_la(null);
		CRUDWorkpackageBP bp = (CRUDWorkpackageBP)context.getBusinessProcess();
		bp.setModel(context,
			((Linea_attivitaComponentSession)bp.createComponentSession()).inizializzaNaturaPerInsieme(
				context.getUserContext(),
				linea_attivita));
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

/**
 * Gestisce una richiesta di azzeramento del searchtool "tipo_linea_attivita"
 *
 * @param context	L'ActionContext della richiesta
 * @param linea_attivita	L'OggettoBulk padre del searchtool
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doBlankSearchTipo_linea_attivita(it.cnr.jada.action.ActionContext context,it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita) {
	linea_attivita.setTipo_linea_attivita(new it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk());
	linea_attivita.setCentro_responsabilita(null);
	return context.findDefaultForward();
}

/**
 * Gestisce una richiesta di ricerca del searchtool "centro_responsabilita"
 *
 * @param context	L'ActionContext della richiesta
 * @param linea_attivita	L'OggettoBulk padre del searchtool
 * @param cdr	L'OggettoBulk selezionato dall'utente
 * @return Il Forward alla pagina di risposta
 */
public Forward doBringBackSearchCentro_responsabilita(ActionContext context,WorkpackageBulk linea_attivita,CdrBulk cdr) {
	try {
		CRUDWorkpackageBP bp = (CRUDWorkpackageBP)context.getBusinessProcess();
		if (cdr != null) 
			if (linea_attivita.getCentro_responsabilita() == null ||
				linea_attivita.getCentro_responsabilita().getCrudStatus() != it.cnr.jada.bulk.OggettoBulk.NORMAL ||
				!cdr.getCd_centro_responsabilita().equals(linea_attivita.getCd_centro_responsabilita())) {
				linea_attivita.setCentro_responsabilita(cdr);
				// (11/06/2002 15.14.46) CNRADM
				// Modificato per azzerare l'insieme al cambiamento del cdr.
				linea_attivita.setInsieme_la(null);
				bp.setModel(context,
					((Linea_attivitaComponentSession)bp.createComponentSession()).inizializzaNaturaPerInsieme(
						context.getUserContext(),
						linea_attivita));
			}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

/**
 * Gestisce una richiesta di ricerca del searchtool "insieme_la"
 *
 * @param context	L'ActionContext della richiesta
 * @param linea_attivita	L'OggettoBulk padre del searchtool
 * @param insieme_la	L'OggettoBulk selezionato dall'utente
 * @return Il Forward alla pagina di risposta
 */
public Forward doBringBackSearchInsieme_la(ActionContext context,WorkpackageBulk linea_attivita,Insieme_laBulk insieme_la) {
	try {
		CRUDWorkpackageBP bp = (CRUDWorkpackageBP)context.getBusinessProcess();
		linea_attivita.setInsieme_la(insieme_la);
		if (insieme_la != null)
			bp.setModel(context,
				((Linea_attivitaComponentSession)bp.createComponentSession()).inizializzaNaturaPerInsieme(
					context.getUserContext(),
					linea_attivita));
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doCambiaGestione(ActionContext context){
	try {
		fillModel(context);
		CRUDWorkpackageBP bp = (CRUDWorkpackageBP)context.getBusinessProcess();
			bp.setModel(context,
				((Linea_attivitaComponentSession)bp.createComponentSession()).inizializzaNature(
					context.getUserContext(),
					(WorkpackageBulk)bp.getModel()));
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce una richiesta di ricerca del searchtool "tipo_linea_attivita"
 *
 * @param context	L'ActionContext della richiesta
 * @param linea_attivita	L'OggettoBulk padre del searchtool
 * @param tipo_linea_attivita	L'OggettoBulk selezionato dall'utente
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doBringBackSearchTipo_linea_attivita(it.cnr.jada.action.ActionContext context,it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita,it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk tipo_linea_attivita) {
	linea_attivita.setTipo_linea_attivita(tipo_linea_attivita);
	linea_attivita.setCentro_responsabilita(null);

	// Passo e funzione natura ereditati da tipologia di linea di attività

	if(tipo_linea_attivita != null) {
	 linea_attivita.setFunzione(tipo_linea_attivita.getFunzione());
	 linea_attivita.setNatura(tipo_linea_attivita.getNatura());
	}
	return context.findDefaultForward();
}

public Forward doFreeSearchFind_nodo_padre(ActionContext context) {
	try{
		ProgettoBulk progetto = new ProgettoBulk();
		progetto.setProgettopadre(new ProgettoBulk());	
		return freeSearch(context, getFormField(context, "main.find_nodo_padre"), progetto);
	} catch(Throwable e){
		return handleException(context, e);
	}		
}
/**
  *  E' stata generata la richiesta di cercare un Progetto che sia padre della Progetto
  *	che si sta creando.
  *	Il metodo controlla se l'utente ha indicato nel campo codice del Progetto padre un 
  *	valore: in caso affermativo, esegue una ricerca mirata per trovare esattamente il codice 
  *	indicato; altrimenti, apre un <code>SelezionatoreListaAlberoBP</code> che permette all'utente 
  *	di cercare il nodo padre scorrendo i Progetti secondo i vari livelli.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
/*public it.cnr.jada.action.Forward doSearchFind_nodo_padre(ActionContext context) {

	try{
			
		CRUDWorkpackageBP bpLinea = (CRUDWorkpackageBP)context.getBusinessProcess();
		WorkpackageBulk lineaProgetto = (WorkpackageBulk)bpLinea.getModel();
						
		String cd = null;

		if (lineaProgetto != null)
			cd = lineaProgetto.getCd_progetto();
		if (cd != null){
			// L'utente ha indicato un codice da cercare: esegue una ricerca mirata.
			return search(context, getFormField(context, "main.find_nodo_padre"),null);
		}

		TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)context.createBusinessProcess("TestataProgettiRicercaBP");
		//bp.setEditable(true);
		//bp.setStatus(CRUDBP.INSERT);
		context.addBusinessProcess(bp);
		ProgettoBulk progetto = (ProgettoBulk)bp.getModel();
		
		it.cnr.jada.util.RemoteIterator roots = bp.getProgettiTreeWorkpackage(context).getChildren(context,null);
		// Non ci sono Commesse disponibili
		if (roots.countElements()==0){
			context.closeBusinessProcess();
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(roots);
			setErrorMessage(context,"Attenzione: non sono state trovate Commesse disponibili");
			return context.findDefaultForward();
		}else {
			context.closeBusinessProcess();
			// Apre un Selezionatore ad Albero per cercare i Progetti selezionando i vari livelli
			ProgettoAlberoLABP slaBP = (ProgettoAlberoLABP)context.createBusinessProcess("ProgettoAlberoLABP");
			slaBP.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(ProgettoBulk.class));
			slaBP.setRemoteBulkTree(context,bp.getProgettiTree(context),roots);
			HookForward hook = (HookForward)context.addHookForward("seleziona",this,"doBringBackSearchResult");
			hook.addParameter("field",getFormField(context,"main.find_nodo_padre"));
			context.addBusinessProcess(slaBP);
			return slaBP;
		}
	} catch(Throwable e){
		return handleException(context, e);
	}
}
*/
/**
  *  E' stata generata la richiesta di cercare un Progetto che sia padre del Progetto
  *	che si sta creando.
  *	Il metodo antepone alla descrizione specificata dall'utente, quella del Progetto selezionato
  *	come padre.
  *	In caso di modifica di una Progetto esistente sul DB, il sistema controlla che il Progetto
  *	selezionato dall'utente non sia la stesso che sta modificando.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
 
public it.cnr.jada.action.Forward doBringBackSearchFind_nodo_padre(ActionContext context, WorkpackageBulk linea, ProgettoBulk progetto) throws java.rmi.RemoteException {

	// valore di default nel caso non fose valorizzato
	String columnDescription="Codice Modulo di Attività";
	// nome del campo nel file xml
	final String propName="cd_progetto";
    FieldProperty property = BulkInfo.getBulkInfo(linea.getClass()).getFieldProperty(propName);
    if (property != null)
    	columnDescription = property.getLabel();


	if (progetto!=null) {
		if (progetto.getLivello()==null || !progetto.getLivello().equals(new Integer("3"))) {
			setErrorMessage(context,"Attenzione: il valore immesso in "+columnDescription+" non è valido!");
			return context.findDefaultForward();
		}
	}
	linea.setProgetto(progetto);
	return context.findDefaultForward();
}

public Forward doSalva(ActionContext context) {
	it.cnr.jada.action.HttpActionContext httpContext = (it.cnr.jada.action.HttpActionContext)context;
	CRUDWorkpackageBP bp = (CRUDWorkpackageBP)httpContext.getBusinessProcess();
	
	// Angelo 18/11/2004 Aggiunto per getsione PostIt
	// Ciclo sui PostIt legati al ProgettoBulk
	for(int i = 0; ((WorkpackageBulk)bp.getModel()).getDettagliPostIt().size() > i; i++) {

		// Se esiste un PostIt in inserimento 
		if (((PostItBulk) ((WorkpackageBulk)bp.getModel()).getDettagliPostIt().get(i)).getCrudStatus() == OggettoBulk.TO_BE_CREATED )
		{
		  //Recupero eventuale File del PostIt da inserire 	
			UploadedFile file =httpContext.getMultipartParameter("main.DettagliPostIt.blob");
		  	
		  // Inizio codice per l'inserimento modifica della colonna BDATA
		  try 
		  {
			  if (file == null || file.getName().equals("")){
				  throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare un File da caricare.");
			  }
			  if (file.length() > lunghezzaMax){
				  throw new it.cnr.jada.comp.ApplicationException("Attenzione: la dimensione del file è superiore alla massima consentita (10 Mb).");
			  }  
		
			  /* Nome (compreso di Path) del file selezionato*/
			  String fileName = file.getName();	
			
				
			  /* Valorizzazione del nome_file sul PostIt con il valore impostato decurtato del path */
			  if (((PostItBulk) ((WorkpackageBulk)bp.getModel()).getDettagliPostIt().get(i)).getNome_file()== null )
					((PostItBulk) ((WorkpackageBulk)bp.getModel()).getDettagliPostIt().get(i)).setNome_file(PostItBulk.parseFilename(fileName));
   
			  /*Salvataggio record PostIt*/
			  super.doSalva(context);
			  WorkpackageBulk wp = (WorkpackageBulk)bp.getModel();

			  /*Inserimento nella colonna BLOB*/
		  ((it.cnr.contab.config00.ejb.Linea_attivitaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Linea_attivitaComponentSession", it.cnr.contab.config00.ejb.Linea_attivitaComponentSession.class)).Inserimento_BLOB(context.getUserContext(), wp, file.getFile());
					   }		
		  catch(Throwable e) 
		  {
				  if (file != null) 
					file.getFile().delete();
				  return handleException(context,e);
		  }

		 // Fine if PostIt in inserimento
		}
		
	} // Fine loop postit
	

	
	
		try 
		{	
			super.doSalva(context);
		}
		catch(Throwable ex)
		{
			return handleException(context, ex);
		}	 	
	 return context.findDefaultForward();
         
}

}
