package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.bulk.BulkList;

/**
 * Insert the type's description here.
 * Creation date: (02/12/2003 12.57.12)
 * @author: Roberto Peli
 */
public class Liquidazione_annualeVBulk extends Liquidazione_iva_annualeVBulk {
	private java.math.BigDecimal importoTotale = null;
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 13.00.09)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getImportoTotale() {
	return importoTotale;
}
public java.lang.String getReportName() {

	return "/gestiva/gestiva/liquid_iva_annuale.jasper";
}
public java.util.Vector getReportParameters() {

	java.util.Vector params = new java.util.Vector();
	if (getId_report() != null) {
		params.add(getId_report().toString());
	}
	return params;
}
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 13.00.09)
 * @param newImportoTotale java.math.BigDecimal
 */
public void setImportoTotale(java.math.BigDecimal newImportoTotale) {
	importoTotale = newImportoTotale;
}
public BulkList getPrintSpoolerParam() {
	BulkList printSpoolerParam = new BulkList();
	Print_spooler_paramBulk param;
	param = new Print_spooler_paramBulk();
	param.setNomeParam("id");
	param.setValoreParam(getId_report().toString());
	param.setParamType("java.lang.Long");
	printSpoolerParam.add(param);
	return printSpoolerParam;
}
}
