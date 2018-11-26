package it.cnr.contab.doccont00.action;

import it.cnr.contab.config00.ejb.EsercizioComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.doccont00.bp.CaricaFileGiornalieraBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.blobs.bulk.Bframe_blobBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.RicercaLiberaBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.upload.UploadedFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.StringTokenizer;

/**
 * Insert the type's description here.
 * Creation date: (10/04/2003 12.04.09)
 * @author: Gennaro Borriello
 */
public class CaricaFileGiornalieraAction extends it.cnr.jada.util.action.SelezionatoreListaAction {
/**
 * CaricaFileGiornalieraAction constructor comment.
 */
public CaricaFileGiornalieraAction() {
	super();
}
public Forward doBringBack(ActionContext context) {
	return context.findDefaultForward();
}
/**
  * Richiamato per la procedura di upload del nuovo File Cassiere.
  *	
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
public Forward doCaricaFile(ActionContext context) {

	it.cnr.jada.action.HttpActionContext httpContext = (it.cnr.jada.action.HttpActionContext)context;
	
	UploadedFile file =httpContext.getMultipartParameter("fileGiornaliera");
	try {
		if (file == null || file.getName().equals(""))
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare un File da caricare.");

		CaricaFileGiornalieraBP bp = (CaricaFileGiornalieraBP)httpContext.getBusinessProcess();
		bp.caricaFile(context,file.getFile());
		bp.setMessage("Operazione Completata.");
		return context.findDefaultForward();
	}catch(Throwable e) {
		return handleException(context,e);
	}
}
}