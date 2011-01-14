package it.cnr.contab.gestiva00.core.bulk;
import java.text.SimpleDateFormat;

import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 11:10:31 AM)
 * @author: Ardire Alfonso
 */
public class Liquidazione_definitiva_ivaVBulk extends Liquidazione_ivaVBulk {
  
	private java.util.Collection prospetti_stampati;
	private boolean Liquidazione_commerciale = false;
	
/**
 * Filtro_ricerca_obbligazioniVBulk constructor comment.
 */
public Liquidazione_definitiva_ivaVBulk() {
	super();
}

/**
 * Insert the method's description here.
 * Creation date: (12/4/2002 3:35:36 PM)
 * @return BulkList
 */
public void completeFrom(Liquidazione_ivaBulk liqDef) {

	setTipoSezionaleFlag(liqDef.getTipo_liquidazione());
	setCd_cds(liqDef.getCd_cds());
	setCd_unita_organizzativa(liqDef.getCd_unita_organizzativa());
	setEsercizio(liqDef.getEsercizio());
	setData_da(liqDef.getDt_inizio());
	setData_a(liqDef.getDt_fine());

	java.util.Calendar cal = java.util.GregorianCalendar.getInstance();
	cal.setTime(new java.util.Date(getData_da().getTime()));
	Integer meseNum = new Integer(cal.get(java.util.Calendar.MONTH)+1);
	if (meseNum.intValue() > 10) meseNum = new Integer(-1);
	setMese((String)getInt_mesi().get(meseNum));
}

/**
 * Insert the method's description here.
 * Creation date: (17/07/2002 11.02.47)
 * @author: Alfonso Ardire
 * @return java.util.Collection
 */
public java.util.Collection getProspetti_stampati() {
	return prospetti_stampati;
}

public BulkList getPrintSpoolerParam() {
	BulkList printSpoolerParam = new BulkList();
	Print_spooler_paramBulk param;
	param = new Print_spooler_paramBulk();
	param.setNomeParam("idReport");
	param.setValoreParam("0");
	//param.setValore_param(getId_report().toString());
	param.setParamType("java.lang.Integer");
	printSpoolerParam.add(param);
	
	param = new Print_spooler_paramBulk();
	param.setNomeParam("aPagIniziale");
	param.setValoreParam(getPageNumber().toString());
	param.setParamType("java.lang.String");
	printSpoolerParam.add(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("cd_cds");
	param.setValoreParam(getCd_cds());
	param.setParamType("java.lang.String");
	printSpoolerParam.add(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("cd_unita_organizzativa");
	param.setValoreParam(getCd_unita_organizzativa());
	param.setParamType("java.lang.String");
	printSpoolerParam.add(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("esercizio");
	param.setValoreParam(getEsercizio().toString());
	param.setParamType("java.lang.Integer");
	printSpoolerParam.add(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("ti_liquidazione");
	param.setValoreParam(getTipo_documento_stampato());
	param.setParamType("java.lang.String");
	printSpoolerParam.add(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("dataInizio");
	param.setValoreParam(new SimpleDateFormat("yyyy/MM/dd").format(getData_da()));
	param.setParamType("java.util.Date");
	printSpoolerParam.add(param);

	param = new Print_spooler_paramBulk();
	param.setNomeParam("dataFine");
	param.setValoreParam(new SimpleDateFormat("yyyy/MM/dd").format(getData_a()));
	param.setParamType("java.util.Date");
	printSpoolerParam.add(param);
	
	return printSpoolerParam;
}
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:18:38 PM)
 */
public java.util.Vector getReportParameters() {

	java.util.Vector params = new java.util.Vector();
	if (getId_report() != null) {
//		params.add(getId_report().toString());
		params.add("0"); // Nella liquidazione definitiva il pg di liq. è 0
		params.add(getPageNumber().toString());
		params.add(getCd_cds());
		params.add(getCd_unita_organizzativa());
		params.add(getEsercizio().toString());
		params.add(getTipo_documento_stampato());
		params.add(getData_da());
		params.add(getData_a());
	}
	return params;

}

public java.lang.String getTipo_documento_stampato() {
	return getTipoSezionaleFlag();
}

/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_fornitore java.lang.Boolean
 */
public OggettoBulk initializeForSearch(
	it.cnr.jada.util.action.BulkBP bp,
	it.cnr.jada.action.ActionContext context) {

	Liquidazione_definitiva_ivaVBulk bulk = (Liquidazione_definitiva_ivaVBulk)super.initializeForSearch(bp, context);
	
	bulk.setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	bulk.setCd_unita_organizzativa(unita_organizzativa.getCd_unita_organizzativa());
	
	bulk.setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
	bulk.setTipo_stampa(TIPO_STAMPA_LIQUIDAZIONE);
	bulk.resetLiquidazioneIva();
	
	bulk.setTipo_report(DEFINITIVO);
	bulk.setTipoSezionaleFlag(SEZIONALI_COMMERCIALI);
	bulk.setLiquidazione_commerciale(true);
	bulk.setTipoImpegnoFlag(IMPEGNI_COMPETENZA);
	bulk.setPageNumber(new Integer(1));
	
	return bulk;
}

public boolean isPageNumberRequired() {
	return true;
}

/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:17:20 PM)
 */
public boolean isRistampabile() {
	return true;
}

/**
 * Insert the method's description here.
 * Creation date: (17/07/2002 11.02.47)
 * @author: Alfonso Ardire
 * @param newProspetti_stampati java.util.Collection
 */
public void setProspetti_stampati(java.util.Collection newProspetti_stampati) {
	prospetti_stampati = newProspetti_stampati;
}
public void validate() throws ValidationException {

	super.validate();

	if (getTipoImpegnoFlag() == null && getTipoSezionaleFlag().equals(SEZIONALI_COMMERCIALI))
		throw new ValidationException("Selezionare il tipo impegno");
}

public boolean isLiquidazione_commerciale() {
	return Liquidazione_commerciale;
}

public void setLiquidazione_commerciale(boolean liquidazione_commerciale) {
	Liquidazione_commerciale = liquidazione_commerciale;
}
}