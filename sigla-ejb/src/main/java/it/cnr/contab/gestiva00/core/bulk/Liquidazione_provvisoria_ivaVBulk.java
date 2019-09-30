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
public class Liquidazione_provvisoria_ivaVBulk extends Liquidazione_ivaVBulk {
	
	
	
	
/**
 * Filtro_ricerca_obbligazioniVBulk constructor comment.
 */
public Liquidazione_provvisoria_ivaVBulk() {
	super();
}
public BulkList getPrintSpoolerParam() {
	BulkList printSpoolerParam = new BulkList();
	Print_spooler_paramBulk param;
	param = new Print_spooler_paramBulk();
	param.setNomeParam("idReport");
	param.setValoreParam(getId_report().toString());
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
 * Creation date: (12/6/2002 4:19:41 PM)
 */
public java.util.Vector getReportParameters() {

	java.util.Vector params = new java.util.Vector();
	if (getId_report() != null) {
		params.add(getId_report().toString());
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

	Liquidazione_provvisoria_ivaVBulk bulk = (Liquidazione_provvisoria_ivaVBulk)super.initializeForSearch(bp, context);
	
	bulk.setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	bulk.setCd_unita_organizzativa(unita_organizzativa.getCd_unita_organizzativa());
	
	bulk.setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
	bulk.setTipo_stampa(TIPO_STAMPA_LIQUIDAZIONE);
	bulk.resetLiquidazioneIva();
	
	bulk.setTipo_report(PROVVISORIO);
	bulk.setTipoSezionaleFlag(SEZIONALI_COMMERCIALI);
	bulk.setPageNumber(new Integer(1));

	return bulk;
}

public boolean isPageNumberRequired() {
	return false;
}
}