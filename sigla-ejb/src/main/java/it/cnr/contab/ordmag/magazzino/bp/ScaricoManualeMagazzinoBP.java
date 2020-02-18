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
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagazzinoRigaBulk;
import it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoBulk;
import it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoRigaBulk;
import it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoRigaLottoBulk;
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

public class ScaricoManualeMagazzinoBP extends SimpleCRUDBP {
	private static final long serialVersionUID = 1L;

	private final SimpleDetailCRUDController beniServiziColl = new SimpleDetailCRUDController("Righe",ScaricoMagazzinoRigaBulk.class,"scaricoMagazzinoRigaColl",this){
		public void add(ActionContext actioncontext) throws BusinessProcessException {
			ScaricoMagazzinoBulk scarico = (ScaricoMagazzinoBulk)this.getParentModel();
			if (scarico.getUnitaOperativaAbilitata()==null||scarico.getUnitaOperativaAbilitata().getCdUnitaOperativa()==null)
				((SimpleCRUDBP)getParentController()).setMessage("Occorre selezionare l'Unità Operativa prima di associare i beni/servizi.");
			else if (scarico.getMagazzinoAbilitato()==null||scarico.getMagazzinoAbilitato().getCdMagazzino()==null)
				((SimpleCRUDBP)getParentController()).setMessage("Occorre selezionare il Magazzino prima di associare i beni/servizi.");
			else if (scarico.getTipoMovimentoMag()==null||scarico.getTipoMovimentoMag().getCdTipoMovimento()==null)
				((SimpleCRUDBP)getParentController()).setMessage("Occorre selezionare il Tipo Movimento prima di associare i beni/servizi.");
			else if (scarico.getDataCompetenza()==null)
				((SimpleCRUDBP)getParentController()).setMessage("Occorre selezionare la Data di Competenza prima di associare i beni/servizi.");
			else 
				super.add(actioncontext);
		};
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			ScaricoMagazzinoRigaBulk riga = (ScaricoMagazzinoRigaBulk)oggettobulk;
			if (riga.getBeneServizio()==null || riga.getBeneServizio().getCd_bene_servizio()==null)
				throw new ValidationException("Valorizzare il Bene/Servizio.");
			if (riga.getUnitaMisura()==null || riga.getUnitaMisura().getCdUnitaMisura()==null)
				throw new ValidationException("Valorizzare l'Unità di Misura.");
			if (Optional.ofNullable(riga.getCoefConv()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)<=0)
				throw new ValidationException("Il Coefficiente di Conversione deve avere un valore positivo.");
			if (Optional.ofNullable(riga.getQuantita()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)<=0 &&
				Optional.ofNullable(riga.getTotQtScaricoLotti()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)<=0)
				throw new ValidationException("Valorizzare la quantità da scaricare.");
			if (riga.getUnitaOperativaRicevente()==null || riga.getUnitaOperativaRicevente().getCdUnitaOperativa()==null)
				throw new ValidationException("Valorizzare l'Unità Operativa Ricevente.");
			super.validate(actioncontext,oggettobulk);
		}
	};

	private final SimpleDetailCRUDController lottiMagazzinoColl = new SimpleDetailCRUDController("Lotti",ScaricoMagazzinoRigaLottoBulk.class,"scaricoMagazzinoRigaLottoColl",beniServiziColl);

	public ScaricoManualeMagazzinoBP() {
		super();
	}
	
	public ScaricoManualeMagazzinoBP(String function) {
		super(function);
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
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.scaricaMagazzino");
		return toolbar;
	}

	public SimpleDetailCRUDController getBeniServiziColl() {
		return beniServiziColl;
	}

	public SimpleDetailCRUDController getLottiMagazzinoColl() {
		return lottiMagazzinoColl;
	}
	
	public ScaricoMagazzinoBulk scaricaMagazzino(ActionContext context) throws BusinessProcessException{
		try {
			ScaricoMagazzinoBulk model = ((MovimentiMagComponentSession)createComponentSession()).scaricaMagazzino(context.getUserContext(), (ScaricoMagazzinoBulk)getModel());
			Stream<ScaricoMagazzinoRigaBulk> streamRiga = model.getScaricoMagazzinoRigaColl().stream().map(ScaricoMagazzinoRigaBulk.class::cast);
			List<ScaricoMagazzinoRigaBulk> newRigheList = streamRiga.filter(e->e.getAnomalia()!=null).collect(Collectors.toList());
			model.setScaricoMagazzinoRigaColl(new BulkList<ScaricoMagazzinoRigaBulk>(newRigheList));
			this.setModel(context, model);
			return (ScaricoMagazzinoBulk)this.getModel();
		} catch (ComponentException | PersistencyException | RemoteException | BusinessProcessException e) {
			throw new BusinessProcessException(e);
		}
	}
	
	@Override
	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk)
			throws BusinessProcessException {
		try {
			oggettobulk = super.initializeModelForInsert(actioncontext, oggettobulk);
			return ((MovimentiMagComponentSession)createComponentSession()).initializeMovimentiMagazzino(actioncontext.getUserContext(), (ScaricoMagazzinoBulk)oggettobulk);
		} catch (ComponentException | PersistencyException | RemoteException | BusinessProcessException e) {
			throw new BusinessProcessException(e);
		}
	}
	
	public void inizializeBeneServizio(ActionContext actioncontext, ScaricoMagazzinoRigaBulk scaricoRiga, Bene_servizioBulk beneServizio) throws BusinessProcessException {
		try {
			scaricoRiga.setBeneServizio(beneServizio);
			scaricoRiga.setUnitaMisura(beneServizio.getUnitaMisura());
			scaricoRiga.setCoefConv(BigDecimal.ONE);
			scaricoRiga.setQuantita(BigDecimal.ZERO);
			List<LottoMagBulk> lottiList = ((MovimentiMagComponentSession)this.createComponentSession()).find(actioncontext.getUserContext(), LottoMagBulk.class, "findLottiMagazzinoByClause", scaricoRiga);
			scaricoRiga.setScaricoMagazzinoRigaLottoColl(
				new BulkList(
					lottiList.stream().map(lotto->{
						ScaricoMagazzinoRigaLottoBulk rigaLotto = new ScaricoMagazzinoRigaLottoBulk();
						rigaLotto.setScaricoMagazzinoRiga(scaricoRiga);
						rigaLotto.setLottoMagazzino(lotto);
						rigaLotto.setQtScarico(BigDecimal.ZERO);
						return rigaLotto;
					}).collect(Collectors.toList()))
			);
			this.inizializeUnitaMisura(actioncontext, scaricoRiga, scaricoRiga.getUnitaMisura());
		} catch (ComponentException | RemoteException | BusinessProcessException e) {
			throw new BusinessProcessException(e);
		}
	}

	public void inizializeUnitaMisura(ActionContext actioncontext, ScaricoMagazzinoRigaBulk scaricoRiga, UnitaMisuraBulk unitaMisura) throws BusinessProcessException {
		Optional.ofNullable(scaricoRiga).ifPresent(sr->{
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
				((ScaricoMagazzinoRigaBulk)this.getBeniServiziColl().getModel()).getBeneServizio()!=null &&
						((ScaricoMagazzinoRigaBulk)this.getBeniServiziColl().getModel()).getBeneServizio().getCd_bene_servizio()!=null)
			return new String[][] {
				{ "tabBeneServizio","Articolo","/ordmag/magazzini/tab_bene_servizio.jsp" },
				{ "tabLottiMagazzino","Lotti Magazzino","/ordmag/magazzini/tab_lotti_magazzino.jsp" } };
		return new String[][] {
				{ "tabBeneServizio","Articolo","/ordmag/magazzini/tab_bene_servizio.jsp" } };
	}
}