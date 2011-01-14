package it.cnr.contab.inventario00.actions;

import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.inventario00.docs.bulk.Stampa_registro_inventarioVBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk;
import it.cnr.contab.reports.bp.ParametricPrintBP;

/**
 * Insert the type's description here.
 * Creation date: (22/07/2004 12.21.54)
 * @author: Gennaro Borriello
 */
public class Stampa_registro_inventarioAction extends it.cnr.contab.reports.action.ParametricPrintAction {
/**
 * Stampa_registro_inventarioAction constructor comment.
 */
public Stampa_registro_inventarioAction() {
	super();
}
/**
 * Gestisce la ricerca di una Categoria
 */

public it.cnr.jada.action.Forward doBlankSearchFind_categoria(it.cnr.jada.action.ActionContext context, it.cnr.contab.inventario00.docs.bulk.Stampa_registro_inventarioVBulk stampa) {

    try {
        fillModel(context);
        it.cnr.jada.util.action.FormField field = getFormField(context, "main.find_categoria");        
		
        stampa.setGruppoForPrint(new it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk());
        blankSearch(context, field, createEmptyModelForSearchTool(context, field));

        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
public it.cnr.jada.action.Forward doClickFlagUfficiale(it.cnr.jada.action.ActionContext context) {

    try {
        fillModel(context);
        ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
    	Stampa_registro_inventarioVBulk stampa = (Stampa_registro_inventarioVBulk)bp.getModel();
    	if(stampa.getFl_ufficiale().booleanValue()){
    		stampa.setCategoriaForPrint(new Categoria_gruppo_inventBulk());
    		stampa.setGruppoForPrint(new Categoria_gruppo_inventBulk());
    		stampa.setTipoMovimento(new Tipo_carico_scaricoBulk());
    		stampa.setNrInventarioFrom(new Long(0));
    		stampa.setNrInventarioTo(new Long("9999999999"));
    		stampa.setFl_solo_totali(true);
    	}
        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
public it.cnr.jada.action.Forward doPrint(it.cnr.jada.action.ActionContext context) {
	ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
	Stampa_registro_inventarioVBulk stampa = (Stampa_registro_inventarioVBulk)bp.getModel();
	if (!stampa.getFl_ufficiale())
		  bp.setReportName("/cnrdocamm/docamm/registro_inventario_princ.jasper");
		else 
		  bp.setReportName("/cnrdocamm/docamm/reg_inv_ce_sp.jasper");
	
	return super.doPrint(context);
}	
}
