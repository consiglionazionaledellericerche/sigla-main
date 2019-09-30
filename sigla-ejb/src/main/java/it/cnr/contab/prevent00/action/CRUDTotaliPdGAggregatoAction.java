/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.prevent00.action;

import it.cnr.contab.pdg00.bulk.*;
import it.cnr.contab.prevent00.bulk.*;
import it.cnr.contab.reports.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

public class CRUDTotaliPdGAggregatoAction extends BulkAction {
public CRUDTotaliPdGAggregatoAction() {
	super();
}
/**
 * Stampa aggregata per CDR I/Area per capitolo
 */
public Forward doStampaAggCapitolo(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/pdg/pdgcdrprimo_voce_funzione_natura.rpt");
		Pdg_aggregatoBulk aggregato = (Pdg_aggregatoBulk)bp.getModel();
        String cd_cdr = aggregato.getCd_centro_responsabilita();
		String cd_proprio = cd_cdr.substring(cd_cdr.lastIndexOf(".")+1);
		if(Integer.parseInt(cd_proprio) != 0) {
		 setMessage(context,0, "Stampa non permessa sul CDR selezionato");
	     return context.findDefaultForward();
	    } else {
		 printbp.setReportParameter(0,"C");
		 printbp.setReportParameter(1,aggregato.getEsercizio().toString());
		 printbp.setReportParameter(2,aggregato.getCd_centro_responsabilita());
    	 return context.addBusinessProcess(printbp);
		}
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}
/**
 * Stampa VARIAZIONI aggregato modificato dal centro per CDR I/Area
 */
public Forward doStampaAggEtrDelta(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/pdg/pdg_aggregato_etr_delta.rpt");
		Pdg_aggregatoBulk aggregato = (Pdg_aggregatoBulk)bp.getModel();
        String cd_cdr = aggregato.getCd_centro_responsabilita();
		String cd_proprio = cd_cdr.substring(cd_cdr.lastIndexOf(".")+1);
		if(Integer.parseInt(cd_proprio) != 0) {
		 setMessage(context,0, "Stampa non permessa sul CDR selezionato");
	     return context.findDefaultForward();
	    } else {
		 printbp.setReportParameter(0,aggregato.getEsercizio().toString());
		 printbp.setReportParameter(1,aggregato.getCd_centro_responsabilita());
		 return context.addBusinessProcess(printbp);
	    }
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}
/**
 * Stampa VARIAZIONI aggregato modificato dal centro per CDR I/Area
 */
public Forward doStampaAggEtrVar(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/pdg/pdg_aggregato_etr_var.rpt");
		Pdg_aggregatoBulk aggregato = (Pdg_aggregatoBulk)bp.getModel();
        String cd_cdr = aggregato.getCd_centro_responsabilita();
		String cd_proprio = cd_cdr.substring(cd_cdr.lastIndexOf(".")+1);
		if(Integer.parseInt(cd_proprio) != 0) {
		 setMessage(context,0, "Stampa non permessa sul CDR selezionato");
	     return context.findDefaultForward();
	    } else {
		 printbp.setReportParameter(0,aggregato.getEsercizio().toString());
		 printbp.setReportParameter(1,aggregato.getCd_centro_responsabilita());
		 return context.addBusinessProcess(printbp);
	    }
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}
/**
 * Stampa aggregata per per CDR I/Area per funzione
 */
public Forward doStampaAggFunzione(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/pdg/pdgcdrprimo_voce_funzione_natura.rpt");
		//Pdg_preventivoBulk preventivo = (Pdg_preventivoBulk)bp.getModel();
		Pdg_aggregatoBulk aggregato = (Pdg_aggregatoBulk)bp.getModel();
		String cd_cdr = aggregato.getCd_centro_responsabilita();
		String cd_proprio = cd_cdr.substring(cd_cdr.lastIndexOf(".")+1);
		if(Integer.parseInt(cd_proprio) != 0) {
		 setMessage(context,0, "Stampa non permessa sul CDR selezionato");
	     return context.findDefaultForward();
	    } else {
		 printbp.setReportParameter(0,"F");
		 printbp.setReportParameter(1,aggregato.getEsercizio().toString());
		 printbp.setReportParameter(2,aggregato.getCd_centro_responsabilita());
		 return context.addBusinessProcess(printbp);
	    }      
	    
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}
/**
 * Stampa aggregata per CDR I/Area per natura
 */
public Forward doStampaAggNatura(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/pdg/pdgcdrprimo_voce_funzione_natura.rpt");
		Pdg_aggregatoBulk aggregato = (Pdg_aggregatoBulk)bp.getModel();
        String cd_cdr = aggregato.getCd_centro_responsabilita();
		String cd_proprio = cd_cdr.substring(cd_cdr.lastIndexOf(".")+1);
		if(Integer.parseInt(cd_proprio) != 0) {
		 setMessage(context,0, "Stampa non permessa sul CDR selezionato");
	     return context.findDefaultForward();
	    } else {
		 printbp.setReportParameter(0,"N");
		 printbp.setReportParameter(1,aggregato.getEsercizio().toString());
		 printbp.setReportParameter(2,aggregato.getCd_centro_responsabilita());
		 return context.addBusinessProcess(printbp);
	    }
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}
/**
 * Stampa VARIAZIONI aggregato modificato dal centro per CDR I/Area
 */
public Forward doStampaAggSpeDelta(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/pdg/pdg_aggregato_spe_delta.rpt");
		Pdg_aggregatoBulk aggregato = (Pdg_aggregatoBulk)bp.getModel();
        String cd_cdr = aggregato.getCd_centro_responsabilita();
		String cd_proprio = cd_cdr.substring(cd_cdr.lastIndexOf(".")+1);
		if(Integer.parseInt(cd_proprio) != 0) {
		 setMessage(context,0, "Stampa non permessa sul CDR selezionato");
	     return context.findDefaultForward();
	    } else {
		 printbp.setReportParameter(0,aggregato.getEsercizio().toString());
		 printbp.setReportParameter(1,aggregato.getCd_centro_responsabilita());
		 return context.addBusinessProcess(printbp);
	    }
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}
/**
 * Stampa VARIAZIONI aggregato modificato dal centro per CDR I/Area
 */
public Forward doStampaAggSpeVar(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/pdg/pdg_aggregato_spe_var.rpt");
		Pdg_aggregatoBulk aggregato = (Pdg_aggregatoBulk)bp.getModel();
        String cd_cdr = aggregato.getCd_centro_responsabilita();
		String cd_proprio = cd_cdr.substring(cd_cdr.lastIndexOf(".")+1);
		if(Integer.parseInt(cd_proprio) != 0) {
		 setMessage(context,0, "Stampa non permessa sul CDR selezionato");
	     return context.findDefaultForward();
	    } else {
		 printbp.setReportParameter(0,aggregato.getEsercizio().toString());
		 printbp.setReportParameter(1,aggregato.getCd_centro_responsabilita());
		 return context.addBusinessProcess(printbp);
	    }
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}
/**
 * Stampa aggregata per CDR I/Area per capitolo
 */
public Forward OLDdoStampaAggCapitolo(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/pdg/pdgcdrprimo_voce_funzione_natura.rpt");
		Pdg_preventivoBulk preventivo = (Pdg_preventivoBulk)bp.getModel();
        if((new Integer(preventivo.getCentro_responsabilita().getCd_proprio_cdr())).intValue() != 0) {
		 setMessage(context,0, "Stampa non permessa sul CDR selezionato");
	     return context.findDefaultForward();
	    } else {
		 printbp.setReportParameter(0,"C");
		 printbp.setReportParameter(1,preventivo.getEsercizio().toString());
		 printbp.setReportParameter(2,preventivo.getCentro_responsabilita().getCd_centro_responsabilita());
    	 return context.addBusinessProcess(printbp);
		}
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}
/**
 * Stampa aggregata per per CDR I/Area per funzione
 */
public Forward OLDdoStampaAggFunzione(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/pdg/pdgcdrprimo_voce_funzione_natura.rpt");
		Pdg_preventivoBulk preventivo = (Pdg_preventivoBulk)bp.getModel();
        if((new Integer(preventivo.getCentro_responsabilita().getCd_proprio_cdr())).intValue() != 0) {
		 setMessage(context,0, "Stampa non permessa sul CDR selezionato");
	     return context.findDefaultForward();
	    } else {
		 printbp.setReportParameter(0,"F");
		 printbp.setReportParameter(1,preventivo.getEsercizio().toString());
		 printbp.setReportParameter(2,preventivo.getCentro_responsabilita().getCd_centro_responsabilita());
		 return context.addBusinessProcess(printbp);
	    }
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}
/**
 * Stampa aggregata per CDR I/Area per natura
 */
public Forward OLDdoStampaAggNatura(ActionContext context) {
	try {
		BulkBP bp = (BulkBP)context.getBusinessProcess();
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess(bp.getPrintbp());
		printbp.setReportName("/preventivo/pdg/pdgcdrprimo_voce_funzione_natura.rpt");
		Pdg_preventivoBulk preventivo = (Pdg_preventivoBulk)bp.getModel();
        if((new Integer(preventivo.getCentro_responsabilita().getCd_proprio_cdr())).intValue() != 0) {
		 setMessage(context,0, "Stampa non permessa sul CDR selezionato");
	     return context.findDefaultForward();
	    } else {
		 printbp.setReportParameter(0,"N");
		 printbp.setReportParameter(1,preventivo.getEsercizio().toString());
		 printbp.setReportParameter(2,preventivo.getCentro_responsabilita().getCd_centro_responsabilita());
		 return context.addBusinessProcess(printbp);
	    }
	} catch(BusinessProcessException e) {
		throw new ActionPerformingError(e);
	}
}
}
