package it.cnr.contab.pdg00.bp;
/**
 * Business Process per la gestione della stampa dello stato dei PDG
 */
 
public class PdgStampaStatoBP extends it.cnr.contab.reports.bp.OfflineReportPrintBP {
public PdgStampaStatoBP() {
	super();
}

public PdgStampaStatoBP(String function) {
	super();
}

protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);

	setReportName("/preventivo/pdg/pdg_stato.rpt");
	
	setReportParameter(0, it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context).toString());
	setReportParameter(1, ((it.cnr.contab.utenze00.bulk.CNRUserInfo)context.getUserInfo()).getCdr().getCd_centro_responsabilita());
}
}