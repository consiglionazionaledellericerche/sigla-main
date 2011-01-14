package it.cnr.contab.doccont00.action;

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.FormField;
/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 15.31.13)
 * @author: Roberto Fantino
 */
public class StampaRegistroAnnotazioneSpesePGiroAction extends it.cnr.contab.reports.action.ParametricPrintAction {
/**
 * StampaRegistroAnnotazioneEntratePGiroAction constructor comment.
 */
public StampaRegistroAnnotazioneSpesePGiroAction() {
	super();
}
public Forward doBlankSearchFindCdsOrigineForPrint(ActionContext context, Stampa_registro_annotazione_spese_pgiroBulk stampa) {

	 try {
        fillModel(context);
        FormField field = getFormField(context, "main.findCdsOrigineForPrint");        
		
        stampa.setUoForPrint(new Unita_organizzativaBulk());
        blankSearch(context, field, createEmptyModelForSearchTool(context, field));

        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
public Forward doBlankSearchFindUoForPrint(ActionContext context, Stampa_registro_annotazione_spese_pgiroBulk stampa) {

	if (stampa!=null)
		stampa.setUoForPrint(new Unita_organizzativaBulk());
	return context.findDefaultForward();
}
public Forward doBringBackSearchFindCdsOrigineForPrint(ActionContext context, Stampa_registro_annotazione_spese_pgiroBulk stampa, CdsBulk cdsOrigine) {

	try {
	    it.cnr.contab.config00.sto.bulk.CdsBulk old_cds = stampa.getCdsOrigineForPrint();
        fillModel(context);
    
       if (cdsOrigine != null && old_cds!= null) {
	       stampa.setUoForPrint(new Unita_organizzativaBulk());
	       stampa.setCdsOrigineForPrint(cdsOrigine);
       }
       
       return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
public Forward doBringBackSearchFindUoForPrint(ActionContext context, Stampa_registro_annotazione_spese_pgiroBulk stampa, Unita_organizzativaBulk uo) {

	if (uo!=null){
		stampa.setUoForPrint(uo);
	}
	return context.findDefaultForward();
}
}
