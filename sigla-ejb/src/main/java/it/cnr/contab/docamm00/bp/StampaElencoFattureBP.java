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

package it.cnr.contab.docamm00.bp;

/**
 * Insert the type's description here.
 * Creation date: (26/08/2004 17.01.53)
 * @author: Gennaro Borriello
 */
public class StampaElencoFattureBP extends it.cnr.contab.reports.bp.ParametricPrintBP {

	private final String STAMPA_FATTURA_PASSIVA = "P";
	private final String STAMPA_FATTURA_ATTIVA = "A";

	private String tipo_stampa;
/**
 * StampaElencoFattureBP constructor comment.
 */
public StampaElencoFattureBP() {
	super();
}
/**
 * StampaElencoFattureBP constructor comment.
 * @param function java.lang.String
 */
public StampaElencoFattureBP(String function) {
	super(function);
}
public String getJSPTitle(){

	if (isStampaElencoFattureP())
		return "Stampa Elenco Fatture Passive per Fornitore";
	else 
		return "Stampa Elenco Fatture Attive per Cliente";
}
/**
 * Inizializza il BP controllando da quale nodo è stata fatta la richiesta ed impostando, quindi,
 *	i parametri relativi a titolo, tipo attivo/passivo etc. che serviranno in seguito.
 *
 * @param config <code>it.cnr.jada.action.Config</code>
 * @param context <code>it.cnr.jada.action.ActionContext</code>
 */

protected void init(it.cnr.jada.action.Config config, it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

		
	String type = config.getInitParameter("tipo_stampa");
	
	if (type != null){
		tipo_stampa = type;	
	}

	super.init(config,context);


	
}
public boolean isStampaElencoFattureA(){
	
	return STAMPA_FATTURA_ATTIVA.equals(tipo_stampa);
}
public boolean isStampaElencoFattureP(){
	
	return STAMPA_FATTURA_PASSIVA.equals(tipo_stampa);
}
}
