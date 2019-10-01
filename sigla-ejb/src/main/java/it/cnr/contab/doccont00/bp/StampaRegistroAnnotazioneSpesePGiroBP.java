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

package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.core.bulk.Stampa_registro_annotazione_spese_pgiroBulk;
import it.cnr.contab.config00.ejb.*;
import it.cnr.jada.action.*;
/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 10.53.46)
 * @author: Simonetta Costa
 */
public class StampaRegistroAnnotazioneSpesePGiroBP extends it.cnr.contab.reports.bp.ParametricPrintBP {
	
	private boolean ente = false;

	private final String CDS = "CDS";
	private final String CNR = "CNR";

	private boolean stampa_cds;
	private boolean stampa_cnr;
/**
 * StampaRegistroAnnotazioneEntratePGiroBP constructor comment.
 */
public StampaRegistroAnnotazioneSpesePGiroBP() {
	super();
}
/**
 * StampaRegistroAnnotazioneEntratePGiroBP constructor comment.
 * @param function java.lang.String
 */
public StampaRegistroAnnotazioneSpesePGiroBP(String function) {
	super(function);
}
public String getJSPTitle(){

	//if (isEnte())
		//return "Stampa Registro Annotazioni di Spesa su Pgiro - CNR";
	//else
		//return "Stampa Registro Annotazioni di Spesa su Pgiro - CDS";

	if (isStampa_cnr())
		return "Stampa Registro Annotazioni di Spesa su Pgiro - CNR";
	else // (isStampa_cds())
		return "Stampa Registro Annotazioni di Spesa su Pgiro - CDS";
}
/**
 * 
 *
 * @param context <code>ActionContext</code>
 */

protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

		
	String type = config.getInitParameter("CDS_CNR");
	
	if (type != null && type.equals(CDS)){		
		setStampa_cds(true);
	} else if (type != null && type.equals(CNR)){
		setStampa_cnr(true);
	}

	super.init(config,context);


	
}
public it.cnr.jada.bulk.OggettoBulk initializeBulkForPrint(ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk) throws BusinessProcessException {

	Stampa_registro_annotazione_spese_pgiroBulk stampa = (Stampa_registro_annotazione_spese_pgiroBulk)bulk;

	if (stampa != null && isStampa_cds()){
		stampa.setStampa_cds(true);
	} else if (stampa != null && isStampa_cnr()){
		stampa.setStampa_cnr(true);
	}

	return super.initializeBulkForPrint(context,stampa);
}
/**
 * Insert the method's description here.
 * Creation date: (03/04/2003 9.24.16)
 * @return boolean
 */
public boolean isStampa_cds() {
	return stampa_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (03/04/2003 9.24.16)
 * @return boolean
 */
public boolean isStampa_cnr() {
	return stampa_cnr;
}
/**
 * Insert the method's description here.
 * Creation date: (03/04/2003 9.24.16)
 * @param newStampa_cds boolean
 */
public void setStampa_cds(boolean newStampa_cds) {
	stampa_cds = newStampa_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (03/04/2003 9.24.16)
 * @param newStampa_cnr boolean
 */
public void setStampa_cnr(boolean newStampa_cnr) {
	stampa_cnr = newStampa_cnr;
}
}
