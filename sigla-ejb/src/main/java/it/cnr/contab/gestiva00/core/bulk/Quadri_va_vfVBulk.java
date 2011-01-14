package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.bulk.BulkList;

/**
 * Insert the type's description here.
 * Creation date: (02/12/2003 12.57.12)
 * @author: Roberto Peli
 */
public class Quadri_va_vfVBulk extends Quadri_liq_annualeVBulk {
public java.util.Vector getReportParameters() {

	java.util.Vector params = new java.util.Vector();
	if (getId_report() != null) {
		params.add(getId_report().toString());
		params.add("Y");
		params.add("A");
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
	param.setValoreParam("A");
	param.setParamType("java.lang.String");
	printSpoolerParam.add(param);
	
	param = new Print_spooler_paramBulk();
	param.setNomeParam("flesclusione");
	param.setValoreParam("Y");
	param.setParamType("java.lang.String");
	printSpoolerParam.add(param);
	return printSpoolerParam;
}
}
