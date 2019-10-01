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

package it.cnr.contab.pdg00.action;

import java.rmi.RemoteException;

import javax.ejb.RemoveException;

import it.cnr.contab.pdg00.bp.ArchiviaStampaPdgVariazioneBP;
import it.cnr.contab.pdg00.bp.FirmaDigitalePdgVariazioniBP;
import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.FormBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class ArchiviaStampaPdgVariazioneAction extends CRUDAction {
    public Forward doCerca(ActionContext actioncontext) throws RemoteException, InstantiationException, RemoveException{
	    try{
	        fillModel(actioncontext);
	        CRUDBP crudbp = getBusinessProcess(actioncontext);
	        OggettoBulk oggettobulk = crudbp.getModel();
	        if(Utility.createCdrComponentSession().isEnte(actioncontext.getUserContext()))
	        	((ArchiviaStampaPdgVariazioneBulk)oggettobulk).setTiSigned(ArchiviaStampaPdgVariazioneBulk.VIEW_SIGNED);
	        
	        RemoteIterator remoteiterator = crudbp.find(actioncontext, null, oggettobulk);
	        if(!(Utility.createCdrComponentSession().isEnte(actioncontext.getUserContext()))&&
	        		(remoteiterator == null || remoteiterator.countElements() == 0)){
	            EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
	            crudbp.setMessage("La ricerca non ha fornito alcun risultato.");
	            return actioncontext.findDefaultForward();
	        }
	        if(remoteiterator.countElements() == 1){
	            OggettoBulk oggettobulk1 = (OggettoBulk)remoteiterator.nextElement();
	            EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
	            crudbp.setMessage(FormBP.INFO_MESSAGE,"La ricerca ha fornito un solo risultato.");
	            return doRiportaSelezione(actioncontext, oggettobulk1);
	        }else{
	            crudbp.setModel(actioncontext, oggettobulk);
	            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.createBusinessProcess("SelezionatorePdgVariazioniBP");
	            selezionatorelistabp.setIterator(actioncontext, remoteiterator);
	            selezionatorelistabp.setBulkInfo(BulkInfo.getBulkInfo(ArchiviaStampaPdgVariazioneBulk.class));
	            selezionatorelistabp.setColumns(getBusinessProcess(actioncontext).getSearchResultColumns());
	    		ArchiviaStampaPdgVariazioneBulk bulk = new ArchiviaStampaPdgVariazioneBulk();
	    		if(Utility.createCdrComponentSession().isEnte(actioncontext.getUserContext()))
	    			bulk.setTiSigned(ArchiviaStampaPdgVariazioneBulk.VIEW_SIGNED);
	    		else
	    			bulk.setTiSigned(ArchiviaStampaPdgVariazioneBulk.VIEW_ALL);
	            selezionatorelistabp.setModel(actioncontext, bulk);
	            actioncontext.addHookForward("seleziona", this, "doRiportaSelezione");
	            return actioncontext.addBusinessProcess(selezionatorelistabp);
	        }
	    }catch(Throwable throwable){
	        return handleException(actioncontext, throwable);
	    }
    }	
}
