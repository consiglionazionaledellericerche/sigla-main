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

/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 27/11/2006
 */
package it.cnr.contab.progettiric00.geco.bulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class Geco_commessaBulk extends Geco_commessaBase implements Geco_commessaIBulk{
	public Geco_commessaBulk() {
		super();
	}
	public Geco_commessaBulk(java.lang.Long id_comm, java.lang.Long esercizio, java.lang.String fase) {
		super(id_comm, esercizio, fase);
	}
	public void aggiornaProgettoSIP(Progetto_sipBulk progetto_sip){
		if (!getId_prog().equals(new Long(progetto_sip.getPg_progetto_padre()))){
			progetto_sip.setProgettopadre(new Progetto_sipBulk(getEsercizio().intValue(),getId_prog().intValue(),getFase()));
			progetto_sip.setToBeUpdated();
		}
		if (!getCod_comm().equals(progetto_sip.getCd_progetto())){
			progetto_sip.setCd_progetto(getCod_comm());
			progetto_sip.setToBeUpdated();
		}
		if (!getDescr_comm().equals(progetto_sip.getDs_progetto())){
			progetto_sip.setDs_progetto(getDescr_comm());
			progetto_sip.setToBeUpdated();
		}
		if (getCds() != null && getSede_svol_uo() != null && !((getCds()+"."+getSede_svol_uo()).equals(progetto_sip.getCd_unita_organizzativa()))){
			progetto_sip.setUnita_organizzativa(new Unita_organizzativaBulk(getCds()+"."+getSede_svol_uo()));
			progetto_sip.setToBeUpdated();
		}
		if (getCod_3rzo_resp() != null && progetto_sip.getCd_responsabile_terzo() != null && !getCod_3rzo_resp().equals(progetto_sip.getCd_responsabile_terzo().toString())){
			progetto_sip.setResponsabile(new TerzoBulk(new Integer(getCod_3rzo_resp())));
			progetto_sip.setToBeUpdated();
		}
		if (getData_inizio_attivita() != null && !getData_inizio_attivita().equals(progetto_sip.getDt_inizio())){
			progetto_sip.setDt_inizio(getData_inizio_attivita());
			progetto_sip.setToBeUpdated();
		}
		if (getEsito_negoz() != null){
			if (getEsito_negoz().equals(new Integer(2)) && !progetto_sip.getStato().equals(ProgettoBulk.TIPO_STATO_PROPOSTA)){
				progetto_sip.setStato(ProgettoBulk.TIPO_STATO_PROPOSTA);
				progetto_sip.setToBeUpdated();
			}else if (!getEsito_negoz().equals(new Integer(2)) && !progetto_sip.getStato().equals(ProgettoBulk.TIPO_STATO_APPROVATO)){
				progetto_sip.setStato(ProgettoBulk.TIPO_STATO_APPROVATO);
				progetto_sip.setToBeUpdated();
			}
		}
	}
	public Long getId_prog_padre() {
		return getId_prog();
	}
}