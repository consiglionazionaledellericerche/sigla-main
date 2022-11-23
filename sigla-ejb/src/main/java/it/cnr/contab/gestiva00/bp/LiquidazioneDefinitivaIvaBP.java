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

package it.cnr.contab.gestiva00.bp;

import it.cnr.contab.doccont00.core.bulk.Mandato_rigaIBulk;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

public class LiquidazioneDefinitivaIvaBP extends LiquidazioneIvaBP {
	private final SimpleDetailCRUDController dettaglio_prospetti = new SimpleDetailCRUDController("prospetti_stampati", Liquidazione_ivaBulk.class,"prospetti_stampati",this) {
		@Override
		public void setModelIndex(ActionContext actioncontext, int i) {
			super.setModelIndex(actioncontext, i);
			Liquidazione_ivaBulk model = Optional.ofNullable(this.getModel()).filter(Liquidazione_ivaBulk.class::isInstance).map(Liquidazione_ivaBulk.class::cast).orElse(null);
			if (Optional.ofNullable(model).isPresent()) {
				Liquidazione_definitiva_ivaVBulk parentModel = Optional.of(this.getParentModel()).filter(Liquidazione_definitiva_ivaVBulk.class::isInstance)
						.map(Liquidazione_definitiva_ivaVBulk.class::cast).orElse(null);
				if (Optional.ofNullable(parentModel).isPresent() && Optional.ofNullable(model.getDt_inizio()).isPresent()) {
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(model.getDt_inizio().getTime());
					parentModel.setMese((String)parentModel.getInt_mesi().get(cal.get(Calendar.MONTH)+1));
					try {
						((LiquidazioneDefinitivaIvaBP) this.getParentController()).doOnMeseChange(actioncontext);
					} catch (BusinessProcessException e) {
						throw new ApplicationRuntimeException(e);
					}
				}
			}
		}
	};
	private final SimpleDetailCRUDController ripartizione_finanziaria = new SimpleDetailCRUDController("Ripartizione finanziaria", Liquidazione_iva_ripart_finBulk.class,"ripartizione_finanziaria",this){
		public void validate(ActionContext context,OggettoBulk model) throws ValidationException {
			Liquidazione_iva_ripart_finBulk bulk = (Liquidazione_iva_ripart_finBulk)model;
			if (bulk.getEsercizio_variazione()==null)
				throw new ValidationException("Il campo Esercizio Variazione è obbligatorio.");
			if (bulk.getIm_variazione()==null || bulk.getIm_variazione().compareTo(BigDecimal.ZERO)<=0)
				throw new ValidationException("Il campo Importo deve essere valorizzato e di segno positivo.");
		}
	};
	private final SimpleDetailCRUDController variazioni_associate = new SimpleDetailCRUDController("Variazioni associate", Liquidazione_iva_variazioniBulk.class,"variazioni_associate",this);
	private final SimpleDetailCRUDController mandato_righe_associate = new SimpleDetailCRUDController("Mandato Righe associate", Mandato_rigaIBulk.class,"mandato_righe_associate",this);

	private boolean isStanziamentoAccentrato = Boolean.FALSE;

	public LiquidazioneDefinitivaIvaBP() {
		this("");
	}

	public LiquidazioneDefinitivaIvaBP(String function) {
		super(function+"Tr");
	}

	/**
	 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
	 */
	public Liquidazione_definitiva_ivaVBulk aggiornaProspetti(ActionContext context,Liquidazione_definitiva_ivaVBulk bulk) throws BusinessProcessException {
		try {
			bulk.setProspetti_stampati(createComponentSession().selectProspetti_stampatiByClause(context.getUserContext(),bulk,new Liquidazione_ivaBulk(),null));
			getDettaglio_prospetti().reset(context);
			return bulk;
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Invocato per creare un modello vuoto da usare su una nuova richiesta di ricerca.
	 */
	public Liquidazione_definitiva_ivaVBulk createEmptyModelForSearch(ActionContext context) throws BusinessProcessException {

		try {
			return createNewBulk(context);
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
	 */
	public Liquidazione_definitiva_ivaVBulk createNewBulk(ActionContext context) throws BusinessProcessException {
		try {
			Liquidazione_definitiva_ivaVBulk bulk = new Liquidazione_definitiva_ivaVBulk();
			bulk.setUser(context.getUserInfo().getUserid());
			bulk = (Liquidazione_definitiva_ivaVBulk)bulk.initializeForSearch(this,context);
			setLiquidato(false);
			//bulk.setTipi_sezionali(createComponentSession().selectTipi_sezionaliByClause(context.getUserContext(),bulk,new it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk(),null));
			bulk=aggiornaProspetti(context,bulk);

			return bulk;
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	protected it.cnr.jada.util.jsp.Button[] createToolbar() {

		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[5];
		int i = 0;

		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.startSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.ristampa");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.reset");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.saveRipartFin");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.consultazionedettaglio");

		return toolbar;
	}

	/**
	 * Restituisce il valore della proprietà 'dettaglio_prospetti'
	 *
	 * @return Il valore della proprietà 'dettaglio_prospetti'
	 */
	public final it.cnr.jada.util.action.SimpleDetailCRUDController getDettaglio_prospetti() {
		return dettaglio_prospetti;
	}

	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

		super.init(config,context);

		try {
			String tipoStanziamentoLiquidazioneIva = Utility.createConfigurazioneCnrComponentSession().getTipoStanziamentoLiquidazioneIva(context.getUserContext());
			setStanziamentoAccentrato(Optional.ofNullable(tipoStanziamentoLiquidazioneIva).map(el->el.equals("STANZIAMENTI_CENTRALIZZATI")).orElse(Boolean.FALSE));
		} catch (RemoteException | ComponentException e) {
		}

		setStatus(SEARCH);
		resetTabs();
		resetForSearch(context);
	}

	public boolean isPrintButtonEnabled() {

		return	getDettaglio_prospetti() != null &&
				getDettaglio_prospetti().getDetails() != null &&
				!getDettaglio_prospetti().getDetails().isEmpty();
	}

	public boolean isPrintButtonHidden() {

		return getPrintbp() == null;
	}

	/**
	 * Inzializza il ricevente nello stato di SEARCH.
	 */
	public void resetForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			setModel(context,createEmptyModelForSearch(context));
			setStatus(SEARCH);
			resetChildren(context);
		} catch(Throwable e) {
			throw new it.cnr.jada.action.BusinessProcessException(e);
		}
	}

	/**
	* Imposta come attivi i tab di default.
	*
	*/

	public void resetTabs() {
		setTab("tab", "tabEsigDetr");
	}
	public SimpleDetailCRUDController getRipartizione_finanziaria() {
		return ripartizione_finanziaria;
	}
	public SimpleDetailCRUDController getVariazioni_associate() {
		return variazioni_associate;
	}
	public SimpleDetailCRUDController getMandato_righe_associate() {
		return mandato_righe_associate;
	}

	public String[][] getTabs() {
		TreeMap<Integer, String[]> hash = new TreeMap<>();
		int i=0;

		hash.put(i++, new String[]{"tabEsigDetr", "Esigibilità/Detraibilità","/gestiva00/tab_esigdetr.jsp" });
		hash.put(i++, new String[]{ "tabImporti", "Importi aggiuntivi", "/gestiva00/tab_importi.jsp" });
		hash.put(i++, new String[]{ "tabAltro", "Altro", "/gestiva00/tab_altro.jsp" });

		if (isTabRipartizioneFinanziariaVisible())
			hash.put(i++, new String[]{ "tabRipartFin", "Ripart.Finanziaria", "/gestiva00/tab_ripart_finanziaria.jsp" });
		if (isTabVariazioneAssociateVisible())
			hash.put(i++, new String[]{ "tabVariazioniAss", "Variazioni Associate", "/gestiva00/tab_variazioni_associate.jsp" });
		if (isTabMandatoRigheAssociateVisible())
			hash.put(i++, new String[]{ "tabMandatoRigheAss", "Mandati Associati", "/gestiva00/tab_mandato_righe_associate.jsp" });

		String[][] tabs = new String[i][3];
		for (int j = 0; j < i; j++) {
			tabs[j]=new String[]{hash.get(j)[0],hash.get(j)[1],hash.get(j)[2]};
		}
		return tabs;
	}

	public void inizializzaMese(ActionContext context) throws BusinessProcessException {
		try {
			Liquidazione_definitiva_ivaVBulk model = (Liquidazione_definitiva_ivaVBulk)this.getModel();
			this.setModel(context, Utility.createLiquidIvaInterfComponentSession().inizializzaMese(context.getUserContext(), model));
			Stream<Liquidazione_iva_ripart_finBulk> list = ((Liquidazione_definitiva_ivaVBulk)this.getModel()).getRipartizione_finanziaria().stream().map(Liquidazione_iva_ripart_finBulk.class::cast);
			list.forEach(e->{
				e.caricaAnniList(context);
				//Nel caso di liquidazione dicembre dovendo imputare la variazione sui residui
				//viene eliminato l'anno corrente
				if (Liquidazione_ivaVBulk.DICEMBRE.equals(((Liquidazione_definitiva_ivaVBulk)this.getModel()).getMese()))
					//non lo elimino solo se per caso l'anno risulta imputato
					if (e.getEsercizio_variazione()==null || !e.getEsercizio_variazione().equals(((Liquidazione_definitiva_ivaVBulk)this.getModel()).getEsercizio()))
						e.getAnniList().remove(((Liquidazione_definitiva_ivaVBulk)this.getModel()).getEsercizio());
			});
			if ((!isTabRipartizioneFinanziariaVisible() && ("tabRipartFin".equals(getTab("tab")) || "tabVariazioniAss".equals(getTab("tab")))) ||
				 (!isTabMandatoRigheAssociateVisible() && "tabMandatoRigheAss".equals(getTab("tab"))))
				resetTabs();
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	public boolean isSaveRipartFinButtonHidden() {
		return !isTabRipartizioneFinanziariaVisible();
	}

	public void saveRipartizioneFinanziaria(ActionContext context) throws BusinessProcessException {
		try {
			Liquidazione_definitiva_ivaVBulk model = (Liquidazione_definitiva_ivaVBulk)this.getModel();
			Utility.createLiquidIvaInterfComponentSession().saveRipartizioneFinanziaria(context.getUserContext(), model);
			this.setMessage("Salvataggio effettuato.\nProcedere con il CALCOLA per portare a termine la liquidazione e la conseguente creazione della variazione.");
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	public boolean isTabRipartizioneFinanziariaEnable() {
		Liquidazione_definitiva_ivaVBulk model = (Liquidazione_definitiva_ivaVBulk)this.getModel();
		return (model!=null && model.isLiquidazione_commerciale() && model.getMese()!=null && !model.isRegistroStampato(model.getMese()));
	}

	public boolean isAggiornaIvaDaVersareEnable() {
		Liquidazione_definitiva_ivaVBulk model = (Liquidazione_definitiva_ivaVBulk)this.getModel();
		return model!=null && model.isLiquidazione_commerciale() && model.getMese()!=null &&
			   model.getMese().equals(model.getNextMeseForLiquidazioneDefinitiva());
	}

	public boolean isTabRipartizioneFinanziariaVisible() {
		Liquidazione_definitiva_ivaVBulk model = (Liquidazione_definitiva_ivaVBulk)this.getModel();
		return !this.isStanziamentoAccentrato() && !this.isUoEnte() && model!=null && model.isLiquidazione_commerciale() && model.getMese()!=null;
	}

	public boolean isTabVariazioneAssociateVisible() {
		Liquidazione_definitiva_ivaVBulk model = (Liquidazione_definitiva_ivaVBulk)this.getModel();
		return !this.isStanziamentoAccentrato() && !this.isUoEnte() && model!=null && model.isLiquidazione_commerciale() && model.getMese()!=null && model.isRegistroStampato(model.getMese());
	}

	public boolean isTabMandatoRigheAssociateVisible() {
		Liquidazione_definitiva_ivaVBulk model = (Liquidazione_definitiva_ivaVBulk)this.getModel();
		return this.isUoEnte() && model!=null && model.getMese()!=null && model.isRegistroStampato(model.getMese());
	}
	public boolean isConsultaDettFattureButtonHidden(){
		Liquidazione_definitiva_ivaVBulk model = (Liquidazione_definitiva_ivaVBulk)this.getModel();
		return !(model!=null && model.isLiquidazione_commerciale() && model.getMese()!=null );
	}

	public boolean isStanziamentoAccentrato() {
		return isStanziamentoAccentrato;
	}

	private void setStanziamentoAccentrato(boolean stanziamentoAccentrato) {
		isStanziamentoAccentrato = stanziamentoAccentrato;
	}
}
