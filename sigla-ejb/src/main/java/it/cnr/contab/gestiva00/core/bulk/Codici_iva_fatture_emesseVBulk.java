package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.bulk.BulkList;

/**
 * Insert the type's description here.
 * Creation date: (02/12/2003 12.57.12)
 * @author: Roberto Peli
 */
public class Codici_iva_fatture_emesseVBulk extends Liquidazione_iva_annualeVBulk {
public java.util.Vector getReportParameters() {

	java.util.Vector params = new java.util.Vector();
	if (getId_report() != null) {
		params.add(getId_report().toString());
		params.add("N");
		params.add("V");
	}
	return params;
}

public BulkList getPrintSpoolerParam() {
	BulkList printSpoolerParam = new BulkList();
	Print_spooler_paramBulk param;
	param = new Print_spooler_paramBulk();
	param.setNomeParam("id");
	param.setValoreParam(getId_report().toString());
	param.setParamType("java.lang.Long");
	printSpoolerParam.add(param);
	
	param = new Print_spooler_paramBulk();
	param.setNomeParam("gestioneav");
	param.setValoreParam("V");
	param.setParamType("java.lang.String");
	printSpoolerParam.add(param);
	
	param = new Print_spooler_paramBulk();
	param.setNomeParam("flesclusione");
	param.setValoreParam("N");
	param.setParamType("java.lang.String");
	printSpoolerParam.add(param);
	return printSpoolerParam;
}
}
