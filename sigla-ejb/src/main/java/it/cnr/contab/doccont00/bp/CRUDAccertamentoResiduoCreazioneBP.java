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

package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.*;
/**
 * Business Process che gestisce le attività di CRUD per l'entita' Accertamento Residuo.
 */
public class CRUDAccertamentoResiduoCreazioneBP extends CRUDAccertamentoBP {

	/**
	 * CRUDAccertamentoResiduoBP constructor comment.
	 */
	public CRUDAccertamentoResiduoCreazioneBP() {
		super();
	}
	/**
	 * CRUDAccertamentoResiduoBP constructor comment.
	 * @param function java.lang.String
	 */
	public CRUDAccertamentoResiduoCreazioneBP(String function) {
		super(function);
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.doccont00.bp.CRUDVirtualAccertamentoBP#init(it.cnr.jada.action.Config, it.cnr.jada.action.ActionContext)
	 */
	protected void init(Config config, ActionContext context) throws BusinessProcessException {
		super.init(config, context);
	}
	public void initialize(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		super.initialize(context);
		try {
            if ((it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context)).getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0)
				throw new ApplicationException( "Funzione non consentita per Unità Organizzativa " + (it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context)));
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	/**
	 * Inizializza il modello per la modifica.
	 * @param context Il contesto dell'azione
	 * @param bulk L'oggetto bulk in uso
	 * @return Oggetto Bulk L'oggetto bulk inizializzato
	 */
	public OggettoBulk initializeModelForEdit(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
		try {
			OggettoBulk oggettobulk = super.initializeModelForEdit(context, bulk);
		
			((AccertamentoBulk)oggettobulk).caricaAnniResidui(context);

			return oggettobulk;
		} catch(Throwable e) {
			throw new it.cnr.jada.action.BusinessProcessException(e);
		}
	}
	public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException 
	{
		
		super.basicEdit(context, bulk, doInitializeForEdit);
	
		if (getStatus()!=VIEW)
		{
			AccertamentoBulk accertamento = (AccertamentoBulk)getModel();
			if ( accertamento == null )
				return;
	/*		if ( !accertamento.getCd_uo_origine().equals( it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa( context ).getCd_unita_organizzativa()))
			{
				setStatus(VIEW);
				setMessage("Accertamento creato dall'Unità Organizzativa " + accertamento.getCd_uo_origine() + ". Non consentita la modifica.");
			}
			if ( accertamento.getDt_cancellazione() != null )
			{
				setStatus(VIEW);
				setMessage("Accertamento cancellato. Non consentita la modifica.");
			}*/
	/*		if ( "Y".equals(accertamento.getRiportato()) )
			{
				setStatus(VIEW);
				setMessage("Accertamento riportato all'esercizio successivo. Non consentita la modifica.");
			}
			*/
			
		}
	}
	/**
	 * Completa, dove possibile, i searchtool presenti nella form, effettuando le ricerche necessarie per
	 * tutti i searchtool con valore null e attributo completeOnSave a true.
	 * Per ogni searchtool abilitato viene effettuato una ricerca; se il risultato della ricerca
	 * contiene un unico elemento viene assegnato alla property del searchtool, altrimenti viene generata una
	 * eccezione di validazione.
	 * @param context l'ActionContext da cui proviene la richiesta
	 * @param controller il FormController da controllare
	 */
	public void completeSearchTools(ActionContext context,FormController controller) throws it.cnr.jada.action.BusinessProcessException,ValidationException 
	{
		OggettoBulk model = controller.getModel();
		for (java.util.Enumeration e = controller.getBulkInfo().getFieldProperties();e.hasMoreElements();)
			completeSearchTool(context,model,(FieldProperty)e.nextElement());
	}
}