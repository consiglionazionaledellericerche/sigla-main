package it.cnr.contab.cori00.bp;

/**
 * Controller utilizzato per la visualizzazione dei gruppi CORI di una Liquidazione.
**/
public class Liquid_gruppoCoriCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
public Liquid_gruppoCoriCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
	super(name, modelClass, listPropertyName, parent);
}
public Liquid_gruppoCoriCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent, boolean multiSelection) {
	super(name, modelClass, listPropertyName, parent, multiSelection);
}
}
