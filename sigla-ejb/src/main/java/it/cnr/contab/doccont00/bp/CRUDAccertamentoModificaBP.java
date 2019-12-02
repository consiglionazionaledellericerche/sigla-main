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
 * Created on Jun 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.bp;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.RemoveException;

import it.cnr.contab.compensi00.bp.CRUDCompensoBP;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.ejb.CompensoComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoResiduoBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_mod_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk;
import it.cnr.contab.doccont00.ejb.AccertamentoModificaComponentSession;
import it.cnr.contab.doccont00.ejb.AccertamentoResiduoComponentSession;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.varstanz00.bp.CRUDVar_stanz_resBP;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.ejb.VariazioniStanziamentoResiduoComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class CRUDAccertamentoModificaBP extends it.cnr.jada.util.action.SimpleCRUDBP { 
	public static final String TIPO_ACCESSO_VISUALIZZAZIONE = "V";
	public static final String TIPO_ACCESSO_MODIFICA = "M";
	private final SimpleDetailCRUDController dettagliModifica = new SimpleDetailCRUDController("DettagliModifica",Accertamento_mod_voceBulk.class,"accertamento_mod_voceColl",this);
	private AccertamentoBulk accertamento;
	private String tipoAccesso;
	private Unita_organizzativaBulk uoSrivania;

	/**
	 * Metodo con cui si ottiene il valore della variabile <code>dettagliModifica</code>
	 * di tipo <code>SimpleDetailCRUDController</code>.
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */
	public final it.cnr.jada.util.action.SimpleDetailCRUDController getDettagliModifica() {
		return dettagliModifica;
	}

	public CRUDAccertamentoModificaBP() {
		super();
	}
	public CRUDAccertamentoModificaBP(String function) {
		super(function);
	}

	public CRUDAccertamentoModificaBP(String function, AccertamentoBulk accertamento, String tipoAccesso) {
		super(function);
		setAccertamento(accertamento);
		setTipoAccesso(tipoAccesso);
	}

	public AccertamentoBulk getAccertamento() {
		return accertamento;
	}

	public void setAccertamento(AccertamentoBulk accertamento) {
		this.accertamento = accertamento;
	}

	public void initialize(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		super.initialize(context);
		try {
			Accertamento_modificaBulk obbMod = new Accertamento_modificaBulk();
			obbMod.setAccertamento(getAccertamento());
			obbMod.setEsercizio(getAccertamento().getEsercizio());
			
			setUoSrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));

			if (getTipoAccesso().equals(TIPO_ACCESSO_MODIFICA)) {
				obbMod.setCds(getAccertamento().getCds());
				obbMod.setCd_cds(getAccertamento().getCd_cds());

				obbMod = (Accertamento_modificaBulk) initializeModelForInsert(context,obbMod);
				setModel(context, obbMod);
			}
			else
			{
				if (!isUoEnte()) {
					obbMod.setCds(getAccertamento().getCds());
					obbMod.setCd_cds(getAccertamento().getCd_cds());
				}
				obbMod.setPg_accertamento(getAccertamento().getPg_accertamento());
				setModel(context, obbMod);
				cerca(context);
			}
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	public void save(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException , it.cnr.jada.bulk.ValidationException
	{
		// salvo il valore della variazione per reimpostarlo dopo
		Var_stanz_resBulk var = ((Accertamento_modificaBulk)getModel()).getVariazione();
		super.save( context );
		((Accertamento_modificaBulk)getModel()).setVariazione(var);
	}		

	public String getCds(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa;		
		unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		return(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
	}

	public void cerca(ActionContext actioncontext) throws RemoteException, InstantiationException, RemoveException, BusinessProcessException
	{
		try
		{
			fillModel(actioncontext);
			OggettoBulk oggettobulk = getModel();
			RemoteIterator remoteiterator = find(actioncontext, null, oggettobulk);
			if(remoteiterator == null || remoteiterator.countElements() == 0)
			{
				EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
				return;
			}
			if(remoteiterator.countElements() == 1)
			{
				OggettoBulk oggettobulk1 = (OggettoBulk)remoteiterator.nextElement();
				EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
				if(oggettobulk1 != null) {
					edit(actioncontext, oggettobulk1);
				}
				return;
			}
			else {
				EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
				//reset(actioncontext);
				setStatus(SEARCH);
			}
		}
		catch(Throwable throwable)
		{
			throw handleException(throwable);
		}
	}

	public String getTipoAccesso() {
		return tipoAccesso;
	}

	public void setTipoAccesso(String tipoAccesso) {
		this.tipoAccesso = tipoAccesso;
	}
	
	public CRUDBP avviaVariazStanzRes(ActionContext context, Var_stanz_resBulk var) throws ComponentException, RemoteException, EJBException, BusinessProcessException {
		try {
		    Accertamento_modificaBulk acrMod = (Accertamento_modificaBulk) getModel();
			
		    boolean viewMode = isViewing();
			String status = viewMode ?"V":"M";
			String mode = it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().validaBPPerUtente(context.getUserContext(),((CNRUserInfo)context.getUserInfo()).getUtente(),((CNRUserInfo)context.getUserInfo()).getUtente().isUtenteComune() ? ((CNRUserInfo)context.getUserInfo()).getUnita_organizzativa().getCd_unita_organizzativa() : "*","CRUDAccertamentoModificaBP");
			if (mode == null) 
				throw new it.cnr.jada.action.MessageToUser("Accesso non consentito alla mappa di creazione delle modifiche\nagli accertamenti residui. Impossibile continuare.");
	
			CRUDVar_stanz_resBP newbp = null;
			if (var==null)
				newbp = (CRUDVar_stanz_resBP) context.getUserInfo().createBusinessProcess(context,"CRUDVar_stanz_resBP",new Object[] { status + "RSWTh",  acrMod });
			else {
				newbp = (CRUDVar_stanz_resBP) context.getUserInfo().createBusinessProcess(context,"CRUDVar_stanz_resBP",new Object[] { status + "RSWTh"});
				newbp.setModel(context, var);
				newbp.cerca(context);
			}
			return newbp;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public CRUDBP avviaVariazStanzRes(ActionContext context) throws ComponentException, RemoteException, EJBException, BusinessProcessException {
		return avviaVariazStanzRes(context, null);
	}
	
	public void cancellaVariazioneTemporanea(ActionContext context, Var_stanz_resBulk var) throws BusinessProcessException {
		try {
			((AccertamentoModificaComponentSession)createComponentSession()).cancellaVariazioneTemporanea(context.getUserContext(), var);
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

    public boolean isUoEnte(){
    	return (getUoSrivania().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0);
    }
	public Unita_organizzativaBulk getUoSrivania() {
		return uoSrivania;
	}
	public void setUoSrivania(Unita_organizzativaBulk bulk) {
		uoSrivania = bulk;
	}
}
