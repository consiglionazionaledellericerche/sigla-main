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

package it.cnr.contab.ordmag.ordini.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.contab.ordmag.anag00.TipoOperazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.ejb.EvasioneOrdineComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * Gestisce le catene di elementi correlate con il documento in uso.
 */
public class CRUDEvasioneOrdineBP extends SimpleCRUDBP {

	private it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm = null;
	private boolean carryingThrough = false;
	
    private boolean criteriRicercaCollapse = false;
    private boolean dettConsegneCollapse = false;

    public boolean isCriteriRicercaCollapse() {
        return criteriRicercaCollapse;
    }

    public void setCriteriRicercaCollapse(boolean criteriRicercaCollapse) {
        this.criteriRicercaCollapse = criteriRicercaCollapse;
    }
    
    public boolean isDettConsegneCollapse() {
		return dettConsegneCollapse;
	}
    
    public void setDettConsegneCollapse(boolean dettConsegneCollapse) {
		this.dettConsegneCollapse = dettConsegneCollapse;
	}
    
	public boolean isInputReadonly() 
	{
			return false;
	}

	private final SimpleDetailCRUDController consegne = new SimpleDetailCRUDController("ConsegneDaEvadere",OrdineAcqConsegnaBulk.class,"righeConsegnaDaEvadereColl",this){
		@Override
		public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
			OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk)oggettobulk;
			consegna.setTipoConsegna(consegna.getOrdineAcqRiga().getTipoConsegnaDefault());
			int index = super.addDetail(oggettobulk);
			return index;
		}
	};

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getConsegne() {
		return consegne;
	}
	
	public CRUDEvasioneOrdineBP() {
		this(EvasioneOrdineBulk.class);
	}
	protected void setTab() {
		setTab("tab","tabEvasioneConsegne");
//		setTab("tabOrdineAcqDettaglio","tabOrdineDettaglio");
	}

	public CRUDEvasioneOrdineBP(Class dettObbligazioniControllerClass) {
		super("Tr");
		setTab();
	}

	public CRUDEvasioneOrdineBP(String function)
			throws BusinessProcessException {
		super(function + "Tr");
		setTab();

	}

	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.save");

		return toolbar;
	}
	public it.cnr.contab.doccont00.core.bulk.OptionRequestParameter getUserConfirm() {
		return userConfirm;
	}
	public void setUserConfirm(it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm) {
		this.userConfirm = userConfirm;
	}
	public boolean isCarryingThrough() {
		return carryingThrough;
	}
	public void setCarryingThrough(boolean carryingThrough) {
		this.carryingThrough = carryingThrough;
	}

	@Override
	public boolean isDeleteButtonHidden() {
		return true;
	}

	@Override
	public boolean isFreeSearchButtonHidden() {
		return true;
	}

	@Override
	public boolean isSearchButtonHidden() {
		return true;
	}

	@Override
	public boolean isStartSearchButtonHidden() {
		return true;
	}
	public void cercaConsegne(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		EvasioneOrdineBulk bulk = (EvasioneOrdineBulk) getModel();	
		try 
		{
			bulk.setRigheConsegnaDaEvadereColl(new BulkList<>());
			EvasioneOrdineComponentSession comp = (EvasioneOrdineComponentSession)createComponentSession();
			bulk = comp.cercaOrdini(context.getUserContext(), bulk);

			setModel( context, bulk );
			resyncChildren( context );
		} catch(Exception e) 
		{
			throw handleException(e);
		}
	}

	public String getColumnSetForBollaScarico() {
		return "bollaScaricoforPrint";
	}

	public List<BollaScaricoMagBulk> evadiConsegne(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		EvasioneOrdineBulk bulk = (EvasioneOrdineBulk) getModel();	
		try 
		{
			EvasioneOrdineComponentSession comp = (EvasioneOrdineComponentSession)createComponentSession();
			List<BollaScaricoMagBulk> listaBolleScarico = comp.evadiOrdine(context.getUserContext(), bulk);

		    commitUserTransaction();
		    setModel(context, null);
		    setDirty(false);
		    return listaBolleScarico;
		} catch(Exception e) 
		{
			throw handleException(e);
		}
	}

	@Override
	public boolean isNewButtonHidden() {
		return true;
	}
	
	@Override
	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk)
			throws BusinessProcessException {
		try {
			oggettobulk = super.initializeModelForInsert(actioncontext, oggettobulk);

			Optional.ofNullable(oggettobulk)
				.filter(EvasioneOrdineBulk.class::isInstance)
                .map(EvasioneOrdineBulk.class::cast)
				.ifPresent(obj->{
					try {
						List<UnitaOperativaOrdBulk> listUop = 
								((EvasioneOrdineComponentSession)this.createComponentSession()).find(actioncontext.getUserContext(), UnitaOperativaOrdBulk.class, "findUnitaOperativeAbilitate", actioncontext.getUserContext(), TipoOperazioneOrdBulk.EVASIONE_ORDINE);
						UnitaOperativaOrdBulk unitaOperativa = Optional.ofNullable(listUop)
																.map(e->{
																	if (e.stream().count()>1) {
																		return null;
																	};
																	return e.stream().findFirst().orElse(null);
																}).orElse(null);
						
						this.initializeUnitaOperativaOrd(actioncontext, obj, unitaOperativa);
					} catch (ComponentException | RemoteException | BusinessProcessException e) {
						throw new DetailedRuntimeException(e);
					}
				});
		
			return oggettobulk;
		} catch (BusinessProcessException e) {
			throw new BusinessProcessException(e);
		}
	}

	public void initializeUnitaOperativaOrd(ActionContext actioncontext, EvasioneOrdineBulk evasioneOrdine, UnitaOperativaOrdBulk unitaOperativa) throws BusinessProcessException {
		try {
			if (unitaOperativa != null) {
				evasioneOrdine.setUnitaOperativaAbilitata(unitaOperativa);
				List<MagazzinoBulk> listMag = 
						((EvasioneOrdineComponentSession)this.createComponentSession()).find(actioncontext.getUserContext(), EvasioneOrdineBulk.class, "findMagazziniAbilitati", actioncontext.getUserContext(), evasioneOrdine);
				MagazzinoBulk magazzino = Optional.ofNullable(listMag)
														.map(e->{
															if (e.stream().count()>1) {
																return null;
															};
															return e.stream().findFirst().orElse(null);
														}).orElse(null);
				this.initializeMagazzino(actioncontext, evasioneOrdine, magazzino);
			}			
		} catch (ComponentException | RemoteException e) {
			throw new BusinessProcessException(e);
		}
	}

	public EvasioneOrdineBulk initializeMagazzino(ActionContext actioncontext, EvasioneOrdineBulk evasioneOrdine, MagazzinoBulk magazzino) throws BusinessProcessException {
		try {
			if (magazzino!=null) {
				evasioneOrdine.setMagazzinoAbilitato(magazzino);

				NumerazioneMagBulk numerazioneMag = new NumerazioneMagBulk(CNRUserContext.getCd_cds(actioncontext.getUserContext()), 
						magazzino.getCdMagazzino(), CNRUserContext.getEsercizio(actioncontext.getUserContext()), TipoOperazioneOrdBulk.EVASIONE_ORDINE);
				numerazioneMag = (NumerazioneMagBulk)((EvasioneOrdineComponentSession)this.createComponentSession()).findByPrimaryKey(actioncontext.getUserContext(), numerazioneMag);

				evasioneOrdine.setNumerazioneMag(numerazioneMag);
			}
			return evasioneOrdine;
		} catch (ComponentException | RemoteException e) {
			throw new BusinessProcessException(e);
		}
	}

	public void inizializeUnitaMisuraEvasa(ActionContext actioncontext, OrdineAcqConsegnaBulk consegna, UnitaMisuraBulk unitaMisura) throws BusinessProcessException {
		Optional.ofNullable(consegna).ifPresent(cns->{
			Optional.ofNullable(unitaMisura).ifPresent(um->{
				cns.setUnitaMisuraEvasa(um);
				Optional.ofNullable(cns.getOrdineAcqRiga().getBeneServizio())
					.filter(bene->bene.getUnitaMisura().equalsByPrimaryKey(unitaMisura)).ifPresent(bs->{
					cns.setCoefConvEvasa(BigDecimal.ONE);
				});
			});
		});
	}
}
