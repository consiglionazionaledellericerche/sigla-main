package it.cnr.contab.pdg00.action;

import it.cnr.contab.doccont00.bp.CaricaFileCassiereBP;
import it.cnr.contab.pdg00.bp.FirmaDigitalePdgVariazioniBP;
import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.upload.UploadedFile;

public class FirmaDigitaleStampaPdgVariazioneAction extends it.cnr.jada.util.action.SelezionatoreListaAction {
	/**
	 * SpoolerStatusAction constructor comment.
	 */
	public FirmaDigitaleStampaPdgVariazioneAction() {
		super();
	}
	
	public Forward doCambiaVisibilita(ActionContext context) {
		FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
		ArchiviaStampaPdgVariazioneBulk bulk = (ArchiviaStampaPdgVariazioneBulk)bp.getModel();
		String tiSigned = bulk.getTiSigned();
		try {
			fillModel(context);
			bp.refresh(context);
			return context.findDefaultForward();
		} catch(Throwable e) {
			bulk.setTiSigned(tiSigned);
			return handleException(context,e);
		}
	}
	
	public Forward doRefresh(ActionContext context) {
		try {
			FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
			bp.refresh(context);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	
	public Forward doSelection(ActionContext context,String name) {
		try {
			FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
			bp.setFocus(context);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	
	public Forward doSign(ActionContext context) {
		try {
			FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
			bp.sign(context);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doInstalla(ActionContext context) {
		try {
			FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
			OptionBP option = openConfirm(context,"L'installazione del software di firma digitale va effettuata avviando il browser quando la chiavetta USB Actalis NON è inserita nel computer. Procedere con l'installazione?",
					OptionBP.CONFIRM,"doConfirmInstalla");
			//option.addAttribute("discrepanze", e.getLista());
			return option;
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doConfirmInstalla(ActionContext context, it.cnr.jada.util.action.OptionBP option) {
		try {
			FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
			if (option.getOption() == it.cnr.jada.util.action.OptionBP.OK_BUTTON) {
				bp.installa(context);
			}
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doPersist(ActionContext context) {
		try {
			FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
	        String pathFirmaTempDir = ((HttpActionContext)context).getServlet().getServletContext().getRealPath("/tmp/"+((HttpActionContext)context).getSession().getId());
			String signFileRicevuto = ((HttpActionContext)context).getParameter("sign_file_ricevuto");
        	String fileNameCompleto = pathFirmaTempDir+"/"+signFileRicevuto;
			bp.persist(context, fileNameCompleto);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doUpload(ActionContext context) {
		try {
			FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
			if (!bp.isUploadFile())
				bp.setUploadFile(true);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doInvia(ActionContext context) {
		FirmaDigitalePdgVariazioniBP bp = (FirmaDigitalePdgVariazioniBP)context.getBusinessProcess();
		try {
			it.cnr.jada.action.HttpActionContext httpContext = (it.cnr.jada.action.HttpActionContext)context;
			UploadedFile file=httpContext.getMultipartParameter("main.file_to_upload");
			if (file == null || file.getName().equals(""))
				throw new it.cnr.jada.comp.ApplicationException("Selezionare la variazione firmata da inviare");
			bp.caricaDatiPEC(context);
			bp.persistUploadedFile(context, file);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
}
