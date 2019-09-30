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

package it.cnr.contab.cori00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * OggettoBulk costruito per visualizzare i dati provenienti dalla vista <code>VSX_LIQUIDAZIONE_CORI</code> 
 *
*/

public class Vsx_liquidazione_coriBulk extends Vsx_liquidazione_coriBase {

public Vsx_liquidazione_coriBulk() {
	super();
}
/**
  * Imposta le proprietà del Vsx_liquidazione_cori.
  *	E' stata generata la richiesta di effettuare una liquidazione: l'utente ha selezionato
  *	i gruppi CORI da liquidare, i quali vengono utilizzati per popolare le tabelle dalle
  *	quali la procedura di liquidazione andrà a prelevare i dati.
  *	Il metodo setta le varie proprietà del Vsx_liquidazione_cori, sulla base del gruppo
  *	di riferimento. Il bulk così creato verrà poi inserito in tabella.
  *	
  * @param gruppo il <code>Liquid_gruppo_coriBulk</code> gruppo da liquidare.
**/  
public void completeFrom(it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_coriBulk gruppo) {

	if (gruppo == null)	return;

	setCd_cds(gruppo.getCd_cds());
	setEsercizio(gruppo.getEsercizio());
	setPg_liquidazione(gruppo.getPg_liquidazione());
	setCd_gruppo_cr(gruppo.getCd_gruppo_cr());

	setCd_cds_origine(gruppo.getCd_cds_origine());
	setCd_unita_organizzativa(gruppo.getCd_unita_organizzativa());
	setCd_uo_origine(gruppo.getCd_uo_origine());
	setPg_liquidazione_origine(gruppo.getPg_liquidazione_origine());

	setCd_regione(gruppo.getCd_regione());
	setPg_comune(gruppo.getPg_comune());

	setProc_name("CNRCTB570.vsx_liquida_cori");
	setUser(gruppo.getUser());
}
}
