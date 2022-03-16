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

package it.cnr.contab.progettiric00.consultazioni.bp;

import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.preventvar00.consultazioni.bp.ConsAssCompPerDataBP;
import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_ass_comp_per_dataBulk;
import it.cnr.contab.preventvar00.ejb.ConsAssCompPerDataComponentSession;
import it.cnr.contab.progettiric00.consultazioni.ejb.ConsProgEcoVociGaeComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;

import java.util.Iterator;


public class ConsProgEcoVoceGaeDettBP extends ConsultazioniBP
{
	public it.cnr.jada.ejb.CRUDComponentSession createComponentSession() throws BusinessProcessException {
		return (ConsProgEcoVociGaeComponentSession) createComponentSession("CNRPROGETTIRIC00_EJB_ConsProgEcoVociGaeComponentSession", ConsProgEcoVociGaeComponentSession.class);
	}

	public void impostaColonne(Boolean isDettagliata){
		if (isDettagliata){
			setSearchResultColumnSet("DETTAGLIATA");
			setFreeSearchSet("DETTAGLIATA");
		} else {
			setSearchResultColumnSet("SINTETICA");
			setFreeSearchSet("SINTETICA");
		}
	}

}
