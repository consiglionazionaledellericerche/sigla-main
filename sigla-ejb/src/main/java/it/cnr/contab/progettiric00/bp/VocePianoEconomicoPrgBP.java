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

package it.cnr.contab.progettiric00.bp;

import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;


public class VocePianoEconomicoPrgBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	/**
	 * VocePianoEconomicoPrgBP constructor comment.
	 */
	public VocePianoEconomicoPrgBP() {
		super();
	}
	/**
	 * TestataProgettiRicercaBP constructor comment.
	 * @param function java.lang.String
	 */
	public VocePianoEconomicoPrgBP(String function) {
		super(function);
	}

	@Override
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		try {
			Parametri_enteBulk parEnte = Utility.createParametriEnteComponentSession().getParametriEnte(actioncontext.getUserContext());
			if (!parEnte.getFl_prg_pianoeco())
				throw new ApplicationException("Gestione Piano Economico per Progetti non attiva. Accesso non consentito.");
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
		super.init(config, actioncontext);
	}
	
	@Override
	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk = super.initializeModelForInsert(actioncontext, oggettobulk);
		((Voce_piano_economico_prgBulk)oggettobulk).setFl_valido(Boolean.TRUE);
		((Voce_piano_economico_prgBulk)oggettobulk).setUnita_organizzativa(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(actioncontext.getUserContext())));
		return oggettobulk;
	}
}
