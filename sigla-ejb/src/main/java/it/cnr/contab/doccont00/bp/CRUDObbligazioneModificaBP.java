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

import javax.ejb.RemoveException;

import it.cnr.contab.compensi00.bp.CRUDCompensoBP;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_mod_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class CRUDObbligazioneModificaBP extends it.cnr.jada.util.action.SimpleCRUDBP { 
	public static final String TIPO_ACCESSO_VISUALIZZAZIONE = "V";
	public static final String TIPO_ACCESSO_MODIFICA = "M";
	private final SimpleDetailCRUDController dettagliModifica = new SimpleDetailCRUDController("DettagliModifica",Obbligazione_mod_voceBulk.class,"obbligazione_mod_voceColl",this);
	private ObbligazioneBulk obbligazione;
	private String tipoAccesso;

	/**
	 * Metodo con cui si ottiene il valore della variabile <code>dettagliModifica</code>
	 * di tipo <code>SimpleDetailCRUDController</code>.
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */
	public final it.cnr.jada.util.action.SimpleDetailCRUDController getDettagliModifica() {
		return dettagliModifica;
	}

	public CRUDObbligazioneModificaBP() {
		super();
	}
	public CRUDObbligazioneModificaBP(String function) {
		super(function);
	}

	public CRUDObbligazioneModificaBP(String function, ObbligazioneBulk obbligazione, String tipoAccesso) {
		super(function);
		setObbligazione(obbligazione);
		setTipoAccesso(tipoAccesso);
	}

	public ObbligazioneBulk getObbligazione() {
		return obbligazione;
	}

	public void setObbligazione(ObbligazioneBulk obbligazione) {
		this.obbligazione = obbligazione;
	}

	public void initialize(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		try {
			Obbligazione_modificaBulk obbMod = new Obbligazione_modificaBulk();
			obbMod.setObbligazione(getObbligazione());
			obbMod.setEsercizio(getObbligazione().getEsercizio());
			obbMod.setCds(getObbligazione().getCds());
			obbMod.setCd_cds(getObbligazione().getCd_cds());
			
			if (getTipoAccesso().equals(TIPO_ACCESSO_MODIFICA)) {
				obbMod = (Obbligazione_modificaBulk) initializeModelForInsert(context,obbMod);
				setModel(context, obbMod);
			}
			else
			{
				obbMod.setPg_obbligazione(getObbligazione().getPg_obbligazione());
				setModel(context, obbMod);
				cerca(context);
			}
		} catch(Exception e) {
			throw handleException(e);
		}
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
}
