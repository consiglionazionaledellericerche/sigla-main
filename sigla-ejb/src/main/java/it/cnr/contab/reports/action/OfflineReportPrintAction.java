package it.cnr.contab.reports.action;

import java.util.Enumeration;
import java.util.Iterator;

import it.cnr.contab.gestiva00.core.bulk.Liquid_iva_interfBulk;
import it.cnr.contab.reports.bp.*;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.PrintFieldProperty;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.FormBP;
import it.cnr.jada.util.action.OptionBP;

public class OfflineReportPrintAction extends it.cnr.jada.util.action.FormAction {
/**
 * OfflineReportPrintAction constructor comment.
 */
public OfflineReportPrintAction() {
	super();
}
public Forward doSendEMail(ActionContext context) {
	OfflineReportPrintBP bp = (OfflineReportPrintBP)context.getBusinessProcess();
	try {
		bp.fillModel(context);
	} catch (FillException e) {
		return handleException(context,e);
	}

	return context.findDefaultForward();
}
public Forward doPrint(ActionContext context) {
	OfflineReportPrintBP bp = (OfflineReportPrintBP)context.getBusinessProcess();
	try {
		bp.fillModel(context);
		bp.controllaCampiEMail();
		bp.getModel().setReport(bp.getReportName());
		if (bp.getPrintProps() != null && !bp.getPrintProps().isEmpty()){
			for (java.util.Enumeration e =  bp.getPrintProps().keys();e.hasMoreElements();) {
				String name = (String)e.nextElement();
				Print_spooler_paramBulk param = new Print_spooler_paramBulk();
				param.setNomeParam(name);
				param.setValoreParam(bp.getPrintProps().getProperty(name));
				bp.addToPrintSpoolerParam(param);			
			}
		}
		// Ora viene gestito dalla componente tramite la tabella PRINT_PRIORITY
		//bp.getModel().setPriorita_server(new Integer(bp.getServerPriority()));

		// (02/07/2002 15:41:13) CNRADM
		// Ora viene gestito dalla componente tramite la tabella PRINT_PRIORITY
		//try {
			//com.inet.report.Engine engine = new com.inet.report.Engine("pdf");
			//engine.setReportFile("file:"+((HttpActionContext)context).getServlet().getServletContext().getRealPath("../reports"+bp.getReportName()));
			//bp.getModel().setDs_stampa(engine.getReportTitle());
		//} catch(Throwable e) {
			//return handleException(context,e);
		//}
		
		if (bp.createComponentSession().controllaStampeInCoda(context.getUserContext(), bp.getModel())){
			String message = "Attenzione esiste già la stessa stampa in coda o in esecuzione. Vuoi continuare?";
			return openConfirm(context,message,OptionBP.CONFIRM_YES_NO,"doConfermaStampaInCoda");
		}
		
				bp.createComponentSession().addJob(
						context.getUserContext(),
						bp.getModel(),
						bp.getPrintSpoolerParam());
		
		boolean requiresCommit = bp.getUserTransaction() != null;
		Forward forward = context.closeBusinessProcess();
		
		if (requiresCommit)
			context.getBusinessProcess().commitUserTransaction();
		PrintSpoolerBP printSpoolerBP = (PrintSpoolerBP)context.createBusinessProcess("PrintSpoolerBP");
		printSpoolerBP.setMessage(FormBP.INFO_MESSAGE, "Stampa accodata con successo");
		return context.addBusinessProcess(printSpoolerBP);

	} catch(Throwable e) {
		return handleException(context,e);
	} 
}


public Forward doConfermaStampaInCoda(ActionContext context,int option) throws java.rmi.RemoteException {
	try
	{
		if (option == OptionBP.NO_BUTTON) {
			PrintSpoolerBP printSpoolerBP = (PrintSpoolerBP)context.createBusinessProcess("PrintSpoolerBP");
			printSpoolerBP.setMessage("La stampa non è stata accodata");
			return context.addBusinessProcess(printSpoolerBP);
		}
		else {
			OfflineReportPrintBP bp = (OfflineReportPrintBP)context.getBusinessProcess();
			bp.createComponentSession().addJob(context.getUserContext(),bp.getModel(),bp.getPrintSpoolerParam());
			PrintSpoolerBP printSpoolerBP = (PrintSpoolerBP)context.createBusinessProcess("PrintSpoolerBP");
			boolean requiresCommit = bp.getUserTransaction() != null;
			Forward forward = context.closeBusinessProcess();
			if (requiresCommit)
				context.getBusinessProcess().commitUserTransaction();		
			printSpoolerBP.setMessage(FormBP.INFO_MESSAGE,"Stampa accodata con successo");
			return context.addBusinessProcess(printSpoolerBP);
		}
						
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

protected Forward handleException(ActionContext context, Throwable ex) {
	try {
		throw ex;
	} catch(it.cnr.jada.bulk.ValidationException e) {
		setErrorMessage(context,e.getMessage());
		return context.findDefaultForward();
	} catch(it.cnr.jada.bulk.FillException e) {
		setErrorMessage(context,e.getMessage());
		return context.findDefaultForward();
	} catch(it.cnr.jada.comp.CRUDConstraintException e) {
		((OfflineReportPrintBP)context.getBusinessProcess()).setErrorMessage(e.getUserMessage());
		return context.findDefaultForward();
	} catch(it.cnr.jada.comp.CRUDException e) {
		((OfflineReportPrintBP)context.getBusinessProcess()).setErrorMessage(e.getMessage());
		return context.findDefaultForward();
	} catch(Throwable e) {
		return super.handleException(context,e);
	}
}


}