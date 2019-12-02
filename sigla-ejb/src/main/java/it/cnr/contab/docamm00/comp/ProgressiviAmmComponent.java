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

package it.cnr.contab.docamm00.comp;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBException;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

public class ProgressiviAmmComponent extends it.cnr.jada.comp.CRUDComponent implements ICRUDMgr,INumerazioneDocAmmMgr,Cloneable,Serializable {



    public  ProgressiviAmmComponent()
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

public Long getNextPG (UserContext userContext,Numerazione_doc_ammBulk progressivo) 
	throws ComponentException {
	try {
		String cds = progressivo.getCd_cds();
		String cdUnitaOrg = progressivo.getCd_unita_organizzativa();
		String tipoDoc = progressivo.getCd_tipo_documento_amm();
		Integer es = progressivo.getEsercizio();
		Numerazione_doc_ammHome home = (Numerazione_doc_ammHome)getHome(userContext, progressivo);
		if( progressivo.getCd_tipo_documento_amm().compareTo(Numerazione_doc_ammBulk.TIPO_LETTERA_ESTERO)==0){			
				if (Utility.createParametriCnrComponentSession().getParametriCnr(userContext,progressivo.getEsercizio()).getFl_tesoreria_unica().booleanValue()){
					Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0);					
					progressivo =new Numerazione_doc_ammBulk(uoEnte.getCd_cds(), tipoDoc, uoEnte.getCd_unita_organizzativa(), es );
				}
				else{
					progressivo = new Numerazione_doc_ammBulk(cds, tipoDoc, cdUnitaOrg, es );
				}			
		}
		try {
			progressivo = (Numerazione_doc_ammBulk)home.findAndLock(progressivo);
		} catch (it.cnr.jada.persistency.ObjectNotFoundException e) {
			progressivo = null;
		}		
		Long pgCorrente = null;
		if (progressivo == null) {
			progressivo = new Numerazione_doc_ammBulk();
			progressivo.setCd_tipo_documento_amm(tipoDoc);
			progressivo.setEsercizio(es);
			if( progressivo.getCd_tipo_documento_amm().compareTo(Numerazione_doc_ammBulk.TIPO_LETTERA_ESTERO)==0){			
				if (Utility.createParametriCnrComponentSession().getParametriCnr(userContext,progressivo.getEsercizio()).getFl_tesoreria_unica().booleanValue()){
					Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0);
					progressivo.setCd_cds(uoEnte.getCd_cds());
					progressivo.setCd_unita_organizzativa(uoEnte.getCd_unita_organizzativa());
				}
				else{
					progressivo.setCd_cds(cds);			
					progressivo.setCd_unita_organizzativa(cdUnitaOrg);
				}
			}
			else{
				progressivo.setCd_cds(cds);			
				progressivo.setCd_unita_organizzativa(cdUnitaOrg);
			} 
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
