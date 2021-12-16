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

package it.cnr.contab.anagraf00.bp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.ContattoBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TelefonoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.MessageToUser;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;

import javax.xml.registry.infomodel.User;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

/**
 * Gestisce le catene di elementi correlate con l'anagrafica in uso.
 */

public class CRUDTerzoBP extends SimpleCRUDBP {
	private final SimpleDetailCRUDController crudTelefoni = new SimpleDetailCRUDController(
			"Telefoni", TelefonoBulk.class, "telefoni", this);

	private final SimpleDetailCRUDController crudFax = new SimpleDetailCRUDController(
			"Fax", TelefonoBulk.class, "fax", this);

	private final SimpleDetailCRUDController crudEmail = new SimpleDetailCRUDController(
			"Email", TelefonoBulk.class, "email", this);

	private final SimpleDetailCRUDController crudPec = new SimpleDetailCRUDController(
			"Pec", TelefonoBulk.class, "pec", this);

	private final SimpleDetailCRUDController crudContatti = new SimpleDetailCRUDController(
			"Contatti", ContattoBulk.class, "contatti", this);

	private final SimpleDetailCRUDController crudTermini_pagamento = new SimpleDetailCRUDController(
			"Termini_pagamento", Rif_termini_pagamentoBulk.class,
			"rif_termini_pagamento", this);

	private final SimpleDetailCRUDController crudTermini_pagamento_disponibili = new SimpleDetailCRUDController(
			"Termini_pagamento_disponibili", Rif_termini_pagamentoBulk.class,
			"rif_termini_pagamento_disponibili", this);

	private final SimpleDetailCRUDController crudModalita_pagamento = new SimpleDetailCRUDController(
			"Modalita_pagamento", Modalita_pagamentoBulk.class,
			"modalita_pagamento", this);

	// private final SimpleDetailCRUDController
	// crudModalita_pagamento_disponibili = new
	// SimpleDetailCRUDController("Modalita_pagamento_disponibili",Rif_modalita_pagamentoBulk.class,"rif_modalita_pagamento_disponibili",crudTerzi);

	private final SimpleDetailCRUDController crudBanche = new SimpleDetailCRUDController(
			"Banche", BancaBulk.class, "banche", crudModalita_pagamento) {
		public java.util.List getDetails() {
			if (getTerzo() == null)
				return null;
			return getTerzo().getBanche(
					(Modalita_pagamentoBulk) crudModalita_pagamento.getModel());
		}

		public OggettoBulk getParentModel() {
			return getTerzo();
		}

		public void validateForDelete(ActionContext context, OggettoBulk detail)
				throws ValidationException {
			validaBancaPerCancellazione(context, (BancaBulk) detail);
		}

		public OggettoBulk removeDetail(OggettoBulk bulk, int index) {
			bulk = removeBanca(bulk, index);
			if (bulk != null)
				bulk.setToBeDeleted();
			return bulk;
		}

		public void add(ActionContext actioncontext, OggettoBulk oggettobulk)
				throws BusinessProcessException {
			if (oggettobulk instanceof BancaBulk) {
				BancaBulk bancaClone = null;

				try {
					bancaClone = (BancaBulk) Utility
							.createAbiCabComponentSession()
							.caricaStrutturaIban(
									actioncontext.getUserContext(),
									(BancaBulk) oggettobulk);
				} catch (Exception e) {
					throw new BusinessProcessException(e);
				}
				if (bancaClone != null)
					super.add(actioncontext, bancaClone);
				else
					super.add(actioncontext, oggettobulk);
			} else
				super.add(actioncontext, oggettobulk);
		}

		@Override
		protected void validate(ActionContext actioncontext,
				OggettoBulk oggettobulk) throws ValidationException {
			validaRiga(actioncontext, oggettobulk);
			super.validate(actioncontext, oggettobulk);
		}
	};

	private it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anagrafico;
	private Unita_organizzativaBulk unita_organizzativa;

	public CRUDTerzoBP(String function) throws BusinessProcessException {
		super(function);
	}

	public void validaRiga(ActionContext actioncontext, OggettoBulk oggettobulk)
			throws ValidationException {
		try {
			if (getCrudBanche() != null && getCrudBanche().getModel() != null) {
				getCrudBanche().getModel().validate(getModel());
				completeSearchTools(actioncontext, this);
			}
		} catch (BusinessProcessException e) {
			handleException(e);
		}

	}

	public CRUDTerzoBP(String function, AnagraficoBulk anagrafico)
			throws BusinessProcessException {
		this(function);
		this.anagrafico = anagrafico;
	}



	public CRUDTerzoBP(String function,
			Unita_organizzativaBulk unita_organizzativa)
			throws BusinessProcessException {
		this(function);
		this.unita_organizzativa = unita_organizzativa;
	}

	public void edit(it.cnr.jada.action.ActionContext context,
			OggettoBulk bulk, boolean doInitializeForEdit)
			throws it.cnr.jada.action.BusinessProcessException {
		super.edit(context, bulk, doInitializeForEdit);
		if (((TerzoBulk) getModel()).isTerzo_speciale())
			setMessage("Terzo non modificabile (terzo speciale)");
	}


	private boolean isUserLoggedOnEnte(UserContext usercontext) throws ComponentException, RemoteException {
		CNRUserContext.getCd_unita_organizzativa(usercontext);

		Unita_organizzativaBulk unita_organizzativaBulk = ( Unita_organizzativaBulk)Utility.createUnita_organizzativaComponentSession().findByPrimaryKey(usercontext,
				new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext)));
		if ( unita_organizzativaBulk.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
			return Boolean.TRUE;
		return false;
	}
	private boolean isEnbaleIpaPcc(UserContext usercontext, TerzoBulk terzoBulk, boolean isIpa) throws ComponentException, RemoteException {
		if (isUserLoggedOnEnte(usercontext))
			return Boolean.FALSE;

		if (!terzoBulk.isNotGestoreIstat())
			return Boolean.FALSE;
		if (isIpa){
			if (!terzoBulk.getAnagrafico().isStrutturaCNR())
				return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	public boolean isEnableChangeCodIpa(UserContext usercontext, TerzoBulk terzoBulk)
			throws ComponentException, RemoteException {

		return isEnbaleIpaPcc( usercontext,terzoBulk,true);
	}



	public boolean isEnableChangePCC(UserContext usercontext, TerzoBulk terzoBulk)
			throws ComponentException, RemoteException {

		return isEnbaleIpaPcc( usercontext,terzoBulk,false);
	}

	public boolean isGestoreIstat(UserContext usercontext, TerzoBulk terzoBulk)
			throws ComponentException, RemoteException {

		CNRUserContext.getCd_unita_organizzativa(usercontext);

		Unita_organizzativaBulk unita_organizzativaBulk = ( Unita_organizzativaBulk)Utility.createUnita_organizzativaComponentSession().findByPrimaryKey(usercontext,
				new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext)));
		if ( unita_organizzativaBulk.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
			return Boolean.FALSE;

		return !terzoBulk.isNotGestoreIstat();
	}

	/**
	 * 17:11:01)
	 * 
	 * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
	 */
	public it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk getAnagrafico() {
		return anagrafico;
	}

	@Override
	public void save(ActionContext actioncontext) throws ValidationException,
			BusinessProcessException {
		// TODO Auto-generated method stub
		super.save(actioncontext);
		TerzoBulk terzo = (TerzoBulk) getModel();
		
		if (terzo != null && terzo.getCodiceUnivocoUfficioIpa() == null && terzo.getAnagrafico() != null && terzo.getAnagrafico().getDataAvvioFattElettr() != null && terzo.getAnagrafico().isEntePubblico() && 
				!terzo.getAnagrafico().getDataAvvioFattElettr().after(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate())  && terzo.getAnagrafico().isItaliano() ){
			setMessage("Attenzione! L'anagrafica è censita nell'indice delle pubbliche amministrazioni, richiedere tramite helpdesk l'inserimento del codice IPA per questo terzo "+terzo.getCd_terzo()+". Salvataggio eseguito in modo corretto.");
		}
	}

	/**
	 * Restituisce il modello da riportare all'utilizzo di un crud tool;
	 * inquesto caso quello dei terzi.
	 * 
	 * @return OggettoBulk
	 */

	public OggettoBulk getBringBackModel() {

		TerzoBulk tb = (TerzoBulk) getModel();
		if (tb == null)
			throw new MessageToUser("E' necessario selezionare un terzo",
					ERROR_MESSAGE);

		return tb;
	}

	/**
	 * Restituisce il CRUDController relativo al tab delle banche.
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudBanche() {
		return crudBanche;
	}

	/**
	 * Restituisce il CRUDController relativo al tab dei Contatti.
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudContatti() {
		return crudContatti;
	}

	/**
	 * Restituisce il CRUDController relativo al tab delle e-mail.
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudEmail() {
		return crudEmail;
	}

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudPec() {
		return crudPec;
	}

	/**
	 * Restituisce il CRUDController relativo al tab dei fax.
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudFax() {
		return crudFax;
	}

	/**
	 * Restituisce il CRUDController relativo alle modalità di pagamento.
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudModalita_pagamento() {
		return crudModalita_pagamento;
	}

	/**
	 * Restituisce il CRUDController relativo al tab dei telefoni.
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudTelefoni() {
		return crudTelefoni;
	}

	/**
	 * Restituisce il CRUDController relativo ai termini di pagamento.
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudTermini_pagamento() {
		return crudTermini_pagamento;
	}

	/**
	 * Restituisce il CRUDController relativo ai termini di pagamento
	 * disponibili.
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudTermini_pagamento_disponibili() {
		return crudTermini_pagamento_disponibili;
	}

	public String getFreeSearchSet() {
		if (getAnagrafico() == null)
			return super.getFreeSearchSet();
		return getAnagrafico().isStrutturaCNR() ? "terzoStrutturaCNR"
				: "terzoPersonaFisica";
	}

	public TerzoBulk getTerzo() {
		return (TerzoBulk) getModel();
	}

	/**
	 * Insert the method's description here. Creation date: (08/08/2002
	 * 16:52:03)
	 * 
	 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public final it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
		return unita_organizzativa;
	}

	protected void initialize(it.cnr.jada.action.ActionContext context)
			throws it.cnr.jada.action.BusinessProcessException {
		try {
			if (unita_organizzativa == null) {
				if (getAnagrafico() == null || !isEditable())
					resetForSearch(context);
				else
					reset(context);
			} else {
				TerzoBulk terzo = ((it.cnr.contab.anagraf00.ejb.TerzoComponentSession) createComponentSession())
						.cercaTerzoPerUnitaOrganizzativa(
								context.getUserContext(), unita_organizzativa);
				if (terzo == null) {
					if (isEditable())
						reset(context,
								terzo = ((it.cnr.contab.anagraf00.ejb.TerzoComponentSession) createComponentSession())
										.inizializzaTerzoPerUnitaOrganizzativa(
												context.getUserContext(),
												unita_organizzativa));
					else
						throw new MessageToUser(
								"Non esiste nessun terzo per l'unità organizzativa "
										+ unita_organizzativa
												.getCd_unita_organizzativa());
				} else {
					edit(context, terzo);
					terzo = (TerzoBulk) getModel();
					anagrafico = terzo.getAnagrafico();
				}
			}
		} catch (it.cnr.jada.comp.ApplicationException e) {
			setMessage(e.getMessage());
			reset(context);
			getTerzo().setUnita_organizzativa(unita_organizzativa);
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public OggettoBulk initializeModelForFreeSearch(ActionContext context,
			OggettoBulk bulk) throws BusinessProcessException {
		TerzoBulk terzo = (TerzoBulk) super.initializeModelForFreeSearch(
				context, bulk);
		terzo.setAnagrafico(anagrafico);
		return terzo;
	}

	public OggettoBulk initializeModelForInsert(ActionContext context,
			OggettoBulk bulk) throws BusinessProcessException {
		TerzoBulk terzo = (TerzoBulk) super.initializeModelForInsert(context,
				bulk);
		try {
			terzo.setNotGestoreIstat(!UtenteBulk.isGestoreIstatSiope(context
					.getUserContext()));
		} catch (ComponentException e1) {
			handleException(e1);
		} catch (RemoteException e1) {
			handleException(e1);
		}
		terzo.setAnagrafico(anagrafico);
		return terzo;
	}

	@Override
	public OggettoBulk initializeModelForEdit(ActionContext actioncontext,
			OggettoBulk oggettobulk) throws BusinessProcessException {
		TerzoBulk terzo = (TerzoBulk) super.initializeModelForEdit(
				actioncontext, oggettobulk);
		try {
			terzo.setNotGestoreIstat(!UtenteBulk
					.isGestoreIstatSiope(actioncontext.getUserContext()));
		} catch (ComponentException e1) {
			handleException(e1);
		} catch (RemoteException e1) {
			handleException(e1);
		}

		BulkList bancheList = new BulkList();
		for (Iterator i = terzo.getBanche().iterator(); i.hasNext();) {
			BancaBulk banca = (BancaBulk) i.next();
			try {
				BancaBulk bancaClone = (BancaBulk) Utility
						.createAbiCabComponentSession().caricaStrutturaIban(
								actioncontext.getUserContext(), banca);
				bancheList.add(bancaClone);
			} catch (Exception e) {
				setMessage(e.getMessage());
				bancheList.add(banca);
			}
		}
		terzo.setBanche(bancheList);
		return terzo;
	}

	public OggettoBulk initializeModelForSearch(ActionContext context,
			OggettoBulk bulk) throws BusinessProcessException {
		TerzoBulk terzo = (TerzoBulk) super.initializeModelForSearch(context,
				bulk);
		terzo.setAnagrafico(anagrafico);
		return terzo;
	}

	/**
	 * Restituisce il CRUDController relativo alle modalità di pagamento.
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */

	public boolean isCrudModalita_pagamentoVisible() {

		// Modalita_pagamentoBulk modalita_pagamento =
		// (Modalita_pagamentoBulk)getCrudModalita_pagamento().getModel();

		// return (!isPerCessione()
		// && modalita_pagamento.getTerzo_delegato() != null
		// && modalita_pagamento.getTerzo_delegato().getCd_terzo() != null);

		if (getCrudModalita_pagamento().getModel() != null) {

			TerzoBulk terzo = (TerzoBulk) getModel();
			Modalita_pagamentoBulk modalita_pagamento = (Modalita_pagamentoBulk) getCrudModalita_pagamento()
					.getModel();

			if (!modalita_pagamento.isPerCessione()) {
				return true;
			} else if (modalita_pagamento.isPerCessione()
					&& modalita_pagamento.getTerzo_delegato() != null
					&& modalita_pagamento.getTerzo_delegato().getCd_terzo() != null) {
				return true;
			}
		}

		return false;
	}

	public boolean isDeleteButtonEnabled() {

		TerzoBulk terzo = (TerzoBulk) getModel();
		if (terzo != null
				&& (terzo.isDipendente() || terzo.isAnagraficoScaduto()))
			return false;

		return super.isDeleteButtonEnabled()
				&& ((TerzoBulk) getModel()).getDt_fine_rapporto() == null;
	}

	public boolean isInputReadonly() {
		return super.isInputReadonly()
				|| ((TerzoBulk) getModel()).isTerzo_speciale();
	}

	public boolean isNewButtonEnabled() {

		TerzoBulk terzo = (TerzoBulk) getModel();

		if (terzo != null
				&& (terzo.isDipendente() || terzo.isAnagraficoScaduto() || terzo
						.isTerzo_speciale())||(terzo.getAnagrafico()!=null &&terzo.getAnagrafico().isPersonaFisica()))
			return false;
		if (getStatus() == SEARCH)
			return false;
		return super.isNewButtonEnabled();
	}

	public boolean isNewButtonHidden() {

		return super.isNewButtonHidden() || getAnagrafico() == null;
	}

	public boolean isOrigineBancaPerStipendi() {

		if (getCrudBanche() != null) {
			BancaBulk banca = (BancaBulk) getCrudBanche().getModel();
			if (banca != null) {
				return banca.isOrigineStipendi();
			}
		}
		return false;
	}

	/**
	 * Restituisce il CRUDController relativo alle modalità di pagamento.
	 * 
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */

	public boolean isPerCessione() {

		if (getCrudModalita_pagamento().getModel() != null
				&& ((Modalita_pagamentoBulk) getCrudModalita_pagamento()
						.getModel()).isPerCessione()) {
			return true;
		}

		return false;
	}

	public OggettoBulk removeBanca(OggettoBulk bulk, int index) {
		return ((TerzoBulk) getModel()).removeFromBanche((BancaBulk) bulk,
				index);
	}

	public void resetTabs(ActionContext context) {
		setTab("tab", "tabAnagrafica");
		setTab("tabRecapiti", "tabTelefoni");
	}

	/**
	 * Insert the method's description here. Creation date: (05/08/2002
	 * 17:11:01)
	 * 
	 * @param newAnagrafico
	 *            it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
	 */
	public void setAnagrafico(
			it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk newAnagrafico) {
		anagrafico = newAnagrafico;
	}

	public void validaBancaPerCancellazione(ActionContext context,
			BancaBulk banca) throws ValidationException {
		if (banca.getFl_cancellato().booleanValue())
			throw new ValidationException(
					"La banca selezionata è già stata cancellata");
	}

	public void basicEdit(it.cnr.jada.action.ActionContext context,
			it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit)
			throws it.cnr.jada.action.BusinessProcessException {
		super.basicEdit(context, bulk, doInitializeForEdit);

		if (getStatus() != VIEW) {
			TerzoBulk terzo = (TerzoBulk) getModel();
			if (terzo != null && terzo.isAnagraficoScaduto()) {
				setStatus(VIEW);
			}
		}
	}

	public String getTiPagamento() {
		return Optional.ofNullable(getCrudModalita_pagamento())
				.flatMap(simpleDetailCRUDController -> Optional.ofNullable(simpleDetailCRUDController.getModel()))
				.filter(Modalita_pagamentoBulk.class::isInstance)
				.map(Modalita_pagamentoBulk.class::cast)
				.map(modalita_pagamentoBulk -> modalita_pagamentoBulk.getTiPagamentoColumnSet())
				.orElse(Rif_modalita_pagamentoBulk.ALTRO);
	}
}
