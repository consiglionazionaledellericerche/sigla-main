package it.cnr.contab.docamm00.consultazioni.action;
import java.io.File;
import it.cnr.contab.docamm00.consultazioni.bp.ConsEstrazione_glaBP;
import it.cnr.contab.docamm00.consultazioni.bp.Estrazione_glaBP;
import it.cnr.contab.docamm00.consultazioni.bulk.V_estrai_glaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.BulkAction;
import it.cnr.jada.util.upload.UploadedFile;

public class Estrazione_glaAction extends BulkAction {


	public Forward doCerca(ActionContext context){
		it.cnr.contab.docamm00.consultazioni.bp.Estrazione_glaBP bp= (it.cnr.contab.docamm00.consultazioni.bp.Estrazione_glaBP) context.getBusinessProcess();
		try {
			bp.fillModel(context); 
			V_estrai_glaBulk bulk = (V_estrai_glaBulk) bp.getModel();
		if (bulk.getCd_anag()!=null){
			ConsEstrazione_glaBP consBP = (ConsEstrazione_glaBP)context.createBusinessProcess("ConsEstrazione_glaBP");
			context.addBusinessProcess(consBP);
			consBP.openIterator(context,bulk);
		    return context.findDefaultForward();
		}else
			throw new it.cnr.jada.comp.ApplicationException("Attenzione bisogna indicare un anagrafico!");
			
		} catch (Exception e) {
				return handleException(context,e); 
		} 
	}
	public Forward doCloseForm(ActionContext actioncontext)
			throws BusinessProcessException
		{
			try
			{
					return doConfirmCloseForm(actioncontext, 4);
			}
			catch(Throwable throwable)
			{
				return handleException(actioncontext, throwable);
			}
		}
	public Forward doCarica(ActionContext context) {
		
		try{
			fillModel(context);
			Estrazione_glaBP bp = (Estrazione_glaBP)context.getBusinessProcess();
			it.cnr.jada.action.HttpActionContext httpContext = (it.cnr.jada.action.HttpActionContext)context;
			
			UploadedFile file =httpContext.getMultipartParameter("file");
			
			if (file == null || file.getName().equals("")){
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare un File da elaborare.");
			}
			
			file.getFile().createNewFile();
			if(file.getFile().getAbsolutePath().endsWith(".xls"))
				bp.doCarica(context,file.getFile());
			else
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: estensione File da elaborare errata.");
			
			return context.findDefaultForward();
		}
		catch(Throwable ex){
			return handleException(context, ex);
		}
	}

}