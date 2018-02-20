/*
 * Created on Mar 20, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.consultazioni.action;
import java.util.Iterator;

import it.cnr.contab.consultazioni.bp.*;
import it.cnr.contab.inventario00.consultazioni.bulk.ElencoinventariobeniBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsElencoinventariobeniAction extends ConsultazioniAction {
	
	public Forward doRegistroinventario(ActionContext context) {

		try {

			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			CompoundFindClause clauses = new CompoundFindClause();
			if (bp.getSelection() != null && bp.getSelection().size() != bp.getElementsCount()){
				bp.setSelection(context);
	
				if ( bp.getSelectedElements(context) == null )
					return (Forward)context.findDefaultForward();
				
				if (bp.getSelectedElements(context).isEmpty()) {
					bp.setMessage("Non è stata selezionata nessuna riga.");
					return context.findDefaultForward();
				}							
				for (Iterator i = bp.getSelectedElements(context).iterator();i.hasNext();) 
				{
					ElencoinventariobeniBulk wpb = (ElencoinventariobeniBulk) i.next();
					CompoundFindClause claOR = new CompoundFindClause();
					claOR.addClause("AND","cd_unita_organizzativa",SQLBuilder.EQUALS,wpb.getCd_unita_organizzativa());
					claOR.addClause("AND","cd_categoria_gruppo",SQLBuilder.EQUALS,wpb.getCd_categoria_gruppo());
					claOR.addClause("AND","nr_inventario",SQLBuilder.EQUALS,wpb.getNr_inventario());
					claOR.addClause("AND","progressivo",SQLBuilder.EQUALS,wpb.getProgressivo());
					claOR.addClause("AND","ds_bene",SQLBuilder.EQUALS,wpb.getDs_bene());
					clauses = clauses.or(clauses,claOR);
				}
			}			
			CompoundFindClause findclause = bp.getFindclause();
			if (findclause==null)
				findclause = new CompoundFindClause();
			if (clauses!=null)	
			    findclause.addChild(clauses);

			ConsultazioniBP ricercaLiberaBP = (ConsultazioniBP)context.createBusinessProcess("ConsRegistroInventarioBP");
			ricercaLiberaBP.addToBaseclause(findclause);
			ricercaLiberaBP.openIterator(context);
			
			context.addHookForward("close",this,"doDefault");
			return context.addBusinessProcess(ricercaLiberaBP);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	
}
