package it.cnr.contab.pdg00.bp;
/**
 * Business Process per la gestione dei Costi scaricati verso altra UO
 */

public class CRUDCostiScaricatiSpeBP extends CRUDCostiScaricatiBP {
public CRUDCostiScaricatiSpeBP() {
	super();
}

public CRUDCostiScaricatiSpeBP(String function) {
	super(function);
}

public CRUDCostiScaricatiSpeBP(String function, it.cnr.contab.config00.sto.bulk.CdrBulk cdr) {
	super(function, cdr);
}

protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	super.init(config,context);
	setColumns(getBulkInfo().getColumnFieldPropertyDictionary("costiScaricati"));
}
}