/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.ordmag.magazzino.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.magazzino.bulk.LottoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagazzinoBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagazzinoRigaBulk;
import it.cnr.contab.ordmag.magazzino.bulk.CaricoMagazzinoBulk;
import it.cnr.contab.ordmag.magazzino.bulk.CaricoMagazzinoRigaBulk;
import it.cnr.contab.ordmag.magazzino.ejb.MovimentiMagComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class CaricoManualeMagazzinoBP extends SimpleCRUDBP {
	private static final long serialVersionUID = 1L;

	private final SimpleDetailCRUDController beniServiziColl = new SimpleDetailCRUDController("Righe",CaricoMagazzinoRigaBulk.class,"caricoMagazzinoRigaColl",this){
		public void add(ActionContext actioncontext) throws BusinessProcessException {
			CaricoMagazzinoBulk carico = (CaricoMagazzinoBulk)this.getParentModel();
			if (carico.getUnitaOperativaAbilitata()==null||carico.getUnitaOperativaAbilitata().getCdUnitaOperativa()==null)
				((SimpleCRUDBP)getParentController()).setMessage("Occorre selezionare l'Unità Operativa prima di associare i beni/servizi.");
			else if (carico.getMagazzinoAbilitato()==null||carico.getMagazzinoAbilitato().getCdMagazzino()==null)
				((SimpleCRUDBP)getParentController()).setMessage("Occorre selezionare il Magazzino prima di associare i beni/servizi.");
			else if (carico.getTipoMovimentoMag()==null||carico.getTipoMovimentoMag().getCdTipoMovimento()==null)
				((SimpleCRUDBP)getParentController()).setMessage("Occorre selezionare il Tipo Movimento prima di associare i beni/servizi.");
			else if (carico.getDataCompetenza()==null)
				((SimpleCRUDBP)getParentController()).setMessage("Occorre selezionare la Data di Competenza prima di associare i beni/servizi.");
			else 
				super.add(actioncontext);
		};
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			CaricoMagazzinoRigaBulk riga = (CaricoMagazzinoRigaBulk)oggettobulk;
			if (riga.getBeneServizio()==null || riga.getBeneServizio().getCd_bene_servizio()==null)
				throw new ValidationException("Valorizzare il Bene/Servizio.");
			if (riga.getUnitaMisura()==null || riga.getUnitaMisura().getCdUnitaMisura()==null)
				throw new ValidationException("Valorizzare l'Unità di Misura.");
			if (Optional.ofNullable(riga.getCoefConv()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)<=0)
				throw new ValidationException("Il Coefficiente di Conversione deve avere un valore positivo.");
			if (Optional.ofNullable(riga.getQuantita()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)<=0 )
				throw new ValidationException("Valorizzare la quantità da caricare.");
			super.validate(actioncontext,oggettobulk);
		}
	};

	private final SimpleDetailCRUDController lottiMagazzinoColl = new SimpleDetailCRUDController("Lotti",LottoMagBulk.class,"lottoColl",beniServiziColl);

	public CaricoManualeMagazzinoBP() {
		super();
		lottiMagazzinoColl.setEnabled(false);
	}
	
	public CaricoManualeMagazzinoBP(String function) {
		super(function);
		lottiMagazzinoColl.setEnabled(false);
	}

	@Override
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		setTab("tabArticolo","tabBeneServizio");
		super.init(config, actioncontext);
	}
	@Override
	public boolean isDeleteButtonHidden() {
		return true;
	}
	
	@Override
	public boolean isSaveButtonHidden() {
		return true;
	}
	
	@Override
	public boolean isSearchButtonHidden() {
		return true;
	}
	
	@Override
	public boolean isFreeSearchButtonHidden() {
		return true;
	}

	@Override
	public boolean isNewButtonHidden() {
		return true;
	}
	
	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.caricaMagazzino");
		return toolbar;
	}

	public SimpleDetailCRUDController getBeniServiziColl() {
		return beniServiziColl;
	}

	public SimpleDetailCRUDController getLottiMagazzinoColl() {
		return lottiMagazzinoColl;
	}
	
	public CaricoMagazzinoBulk caricaMagazzino(ActionContext context) throws BusinessProcessException{
		try {
			CaricoMagazzinoBulk model = ((MovimentiMagComponentSession)createComponentSession()).caricaMagazzino(context.getUserContext(), (CaricoMagazzinoBulk)getModel());
			Stream<CaricoMagazzinoRigaBulk> streamRiga = model.getCaricoMagazzinoRigaColl().stream().map(CaricoMagazzinoRigaBulk.class::cast);
			List<CaricoMagazzinoRigaBulk> newRigheList = streamRiga.collect(Collectors.toList());
			model.setCaricoMagazzinoRigaColl(new BulkList<CaricoMagazzinoRigaBulk>(newRigheList));
			CaricoMagazzinoBulk caricoMagazzinoBulk = new CaricoMagazzinoBulk();
			caricoMagazzinoBulk = (CaricoMagazzinoBulk)initializeModelForInsert(context, caricoMagazzinoBulk);
			this.setModel(context, caricoMagazzinoBulk);
			return (CaricoMagazzinoBulk)this.getModel();
		} catch (ComponentException | PersistencyException | RemoteException | BusinessProcessException e) {
			throw new BusinessProcessException(e);
		}
	}
	
	@Override
	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk)
			throws BusinessProcessException {
		try {
			oggettobulk = super.initializeModelForInsert(actioncontext, oggettobulk);
			return ((MovimentiMagComponentSession)createComponentSession()).initializeMovimentiMagazzino(actioncontext.getUserContext(), (MovimentiMagazzinoBulk)oggettobulk);
		} catch (ComponentException | PersistencyException | RemoteException | BusinessProcessException e) {
			throw new BusinessProcessException(e);
		}
	}
	
	public void inizializeBeneServizio(ActionContext actioncontext, CaricoMagazzinoRigaBulk caricoRiga, Bene_servizioBulk beneServizio) throws BusinessProcessException {
		try {
			caricoRiga.setBeneServizio(beneServizio);
			caricoRiga.setUnitaMisura(beneServizio.getUnitaMisura());
			caricoRiga.setVoceIva(beneServizio.getVoce_iva());
			caricoRiga.setCoefConv(BigDecimal.ONE);
			caricoRiga.setQuantita(BigDecimal.ZERO);
			caricoRiga.setPrezzoUnitario(BigDecimal.ZERO);
			List<LottoMagBulk> lottiList = ((MovimentiMagComponentSession)this.createComponentSession()).find(actioncontext.getUserContext(), LottoMagBulk.class, "findLottiMagazzinoByClause", caricoRiga);
			caricoRiga.setLottoColl(lottiList);
			this.inizializeUnitaMisura(actioncontext, caricoRiga, caricoRiga.getUnitaMisura());
		} catch (ComponentException | RemoteException | BusinessProcessException e) {
			throw new BusinessProcessException(e);
		}
	}

	public void inizializeUnitaMisura(ActionContext actioncontext, CaricoMagazzinoRigaBulk caricoRiga, UnitaMisuraBulk unitaMisura) throws BusinessProcessException {
		Optional.ofNullable(caricoRiga).ifPresent(sr->{
			Optional.ofNullable(unitaMisura).ifPresent(um->{
				sr.setUnitaMisura(um);
				Optional.ofNullable(sr.getBeneServizio())
					.filter(e->e.getUnitaMisura().equalsByPrimaryKey(unitaMisura)).ifPresent(bs->{
					sr.setCoefConv(BigDecimal.ONE);
				});
			});
		});
	}

	public String[][] getTabsArticolo() {
		if (this.getBeniServiziColl().getModel()!=null && 
				((CaricoMagazzinoRigaBulk)this.getBeniServiziColl().getModel()).getBeneServizio()!=null &&
						((CaricoMagazzinoRigaBulk)this.getBeniServiziColl().getModel()).getBeneServizio().getCd_bene_servizio()!=null)
			return new String[][] {
				{ "tabBeneServizio","Articolo","/ordmag/magazzini/tab_bene_servizio_carico.jsp" },
				{ "tabLottiMagazzino","Lotti Magazzino","/ordmag/magazzini/tab_lotti_magazzino_carico.jsp" } };
		return new String[][] {
				{ "tabBeneServizio","Articolo","/ordmag/magazzini/tab_bene_servizio_carico.jsp" } };
	}
}