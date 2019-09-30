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

package it.cnr.contab.ordmag.comp;

import java.io.Serializable;

import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneMagHome;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

public class NumeratoriOrdMagComponent extends it.cnr.jada.comp.CRUDComponent implements ICRUDMgr,Cloneable,Serializable {



    public  NumeratoriOrdMagComponent()
    {

        /*Default constructor*/


    }
//^^@@
/** 
  *  tutti i controlli superati.
  *    PreCondition:
  *      Il progressivo è stato generato senza errori.
  *    PostCondition:
  *      Viene consentita la registrazione del progressivo.
  *  esistenza della tipologia della numerazione
  *    PreCondition:
  *      Non esiste la numerazione per il tipo di documento amministrativo
  *    PostCondition:
  *      Viene inserita una nuova numerazione per il tipo documento amministrativo, CDS, UO e esercizio correnti
  */
//^^@@

public Integer getNextPG (UserContext userContext,NumerazioneOrdBulk progressivo) 
	throws ComponentException {
	try {
		String tipoDoc = progressivo.getCdNumeratore();
		Integer es = progressivo.getEsercizio();
		String uop = progressivo.getCdUnitaOperativa();
		NumerazioneOrdHome home = (NumerazioneOrdHome)getHome(userContext, progressivo);
		try {
			progressivo = (NumerazioneOrdBulk)home.findAndLock(progressivo);
		} catch (it.cnr.jada.persistency.ObjectNotFoundException e) {
			progressivo = null;
		}		
		Integer pgCorrente = null;
		if (progressivo == null) {
			progressivo = new NumerazioneOrdBulk();
			progressivo.setCdUnitaOperativa(uop);
			progressivo.setCdNumeratore(tipoDoc);
			progressivo.setEsercizio(es);
			progressivo.setCdTipoOperazione(progressivo.getCdTipoOperazione());
			progressivo.setUser(userContext.getUser());
			pgCorrente = new Integer(1);
			progressivo.setCorrente(pgCorrente);
			home.insert(progressivo, userContext);
			return pgCorrente;
		}
		pgCorrente = new Integer(progressivo.getCorrente()+1);
		progressivo.setCorrente(pgCorrente);
		progressivo.setUser(userContext.getUser());
		home.lock(progressivo);
		home.update(progressivo, userContext);
		return pgCorrente;
	} catch(Exception e) {
		throw handleException(progressivo, e);
	}
}
public Long getNextPG (UserContext userContext,NumerazioneMagBulk progressivo) 
		throws ComponentException {
		try {
			String tipoDoc = progressivo.getCdNumeratoreMag();
			Integer es = progressivo.getEsercizio();
			String cds = progressivo.getCdCds();
			String mag = progressivo.getCdMagazzino();
			NumerazioneMagHome home = (NumerazioneMagHome)getHome(userContext, progressivo);
			try {
				progressivo = (NumerazioneMagBulk)home.findAndLock(progressivo);
			} catch (it.cnr.jada.persistency.ObjectNotFoundException e) {
				progressivo = null;
			}		
			Long pgCorrente = null;
			if (progressivo == null) {
				progressivo = new NumerazioneMagBulk();
				progressivo.setCdNumeratoreMag(tipoDoc);
				progressivo.setEsercizio(es);
				progressivo.setCdCds(cds);
				progressivo.setCdMagazzino(mag);
				progressivo.setUser(userContext.getUser());
				pgCorrente = new Long(1);
				progressivo.setCorrente(pgCorrente);
				home.insert(progressivo, userContext);
				return pgCorrente;
			}
			pgCorrente = new Long(progressivo.getCorrente().longValue()+1);
			progressivo.setCorrente(pgCorrente);
			progressivo.setUser(userContext.getUser());
			home.lock(progressivo);
			home.update(progressivo, userContext);
			return pgCorrente;
		} catch(Exception e) {
			throw handleException(progressivo, e);
		}
	}
}
