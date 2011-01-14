package it.cnr.contab.anagraf00.action;

import it.cnr.contab.anagraf00.core.bulk.V_prev_dipBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.Stampa_previdenziale_dipendentiVBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
/**
 * Insert the type's description here.
 * Creation date: (01/07/2003 10.46.31)
 * @author: Gennaro Borriello
 */
public class Stampa_previdenziale_dipendentiAction extends it.cnr.contab.reports.action.ParametricPrintAction {
/**
 * Stampa_previdenziale_dipendentiAction constructor comment.
 */
public Stampa_previdenziale_dipendentiAction() {
	super();
}
/**
 * Stampa_previdenziale_dipendentiAction constructor comment.
 */
public Forward doBlankSearchFindMatricolaForPrint(ActionContext context, Stampa_previdenziale_dipendentiVBulk stampa){

	try{
		fillModel(context);

		
	
		stampa.setMatricolaForPrint(new V_prev_dipBulk());
		
		stampa.setTerzoForPrintEnabled(true);
		
		return context.findDefaultForward();

	}catch(Throwable ex){
		return handleException(context, ex);
	}
}
/**
 * Stampa_previdenziale_dipendentiAction constructor comment.
 */
public Forward doBlankSearchFindTerzoForPrint(ActionContext context, Stampa_previdenziale_dipendentiVBulk stampa){


	try{
		fillModel(context);

			
		stampa.setTerzoForPrint(new TerzoBulk());
		
		stampa.setMatricolaForPrintEnabled(true);
		
		return context.findDefaultForward();

	}catch(Throwable ex){
		return handleException(context, ex);
	}
}
/**
 * Stampa_previdenziale_dipendentiAction constructor comment.
 */
public Forward doBringBackSearchFindMatricolaForPrint(ActionContext context, Stampa_previdenziale_dipendentiVBulk stampa, V_prev_dipBulk matricola){


	try{
		fillModel(context);

		if (matricola != null){			
			stampa.setMatricolaForPrint(matricola);			
			stampa.setTerzoForPrintEnabled(false);
		}
		
		return context.findDefaultForward();

	}catch(Throwable ex){
		return handleException(context, ex);
	}
}
/**
 * Stampa_previdenziale_dipendentiAction constructor comment.
 */
public Forward doBringBackSearchFindTerzoForPrint(ActionContext context, Stampa_previdenziale_dipendentiVBulk stampa, TerzoBulk terzo){

	try{
		fillModel(context);

		if (terzo != null){
			stampa.setTerzoForPrint(terzo);			
			stampa.setMatricolaForPrintEnabled(false);
		}
		
		return context.findDefaultForward();

	}catch(Throwable ex){
		return handleException(context, ex);
	}
}
}
