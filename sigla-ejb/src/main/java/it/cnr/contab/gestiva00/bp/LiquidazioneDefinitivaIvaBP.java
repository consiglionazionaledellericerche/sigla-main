package it.cnr.contab.gestiva00.bp;
import java.math.BigDecimal;
import java.util.TreeMap;
import java.util.stream.Stream;

import it.cnr.contab.doccont00.core.bulk.Mandato_rigaIBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_definitiva_ivaVBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_ripart_finBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_variazioniBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class LiquidazioneDefinitivaIvaBP extends LiquidazioneIvaBP {
	private final SimpleDetailCRUDController dettaglio_prospetti = new SimpleDetailCRUDController("prospetti_stampati", Liquidazione_ivaBulk.class,"prospetti_stampati",this);
	private final SimpleDetailCRUDController ripartizione_finanziaria = new SimpleDetailCRUDController("Ripartizione finanziaria", Liquidazione_iva_ripart_finBulk.class,"ripartizione_finanziaria",this){
		public void validate(ActionContext context,OggettoBulk model) throws ValidationException {
			Liquidazione_iva_ripart_finBulk bulk = (Liquidazione_iva_ripart_finBulk)model;
			if (bulk.getEsercizio_variazione()==null)
				throw new ValidationException("Il campo Esercizio Variazione � obbligatorio.");
			if (bulk.getIm_variazione()==null || bulk.getIm_variazione().compareTo(BigDecimal.ZERO)<=0)
				throw new ValidationException("Il campo Importo deve essere valorizzato e di segno positivo.");
		}
	};
	private final SimpleDetailCRUDController variazioni_associate = new SimpleDetailCRUDController("Variazioni associate", Liquidazione_iva_variazioniBulk.class,"variazioni_associate",this);
	private final SimpleDetailCRUDController mandato_righe_associate = new SimpleDetailCRUDController("Mandato Righe associate", Mandato_rigaIBulk.class,"mandato_righe_associate",this);
	
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
 * Restituisce il valore della propriet� 'dettaglio_prospetti'
 *
 * @return Il valore della propriet� 'dettaglio_prospetti'
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getDettaglio_prospetti() {
	return dettaglio_prospetti;
}

protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);
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
	TreeMap<Integer, String[]> hash = new TreeMap<Integer, String[]>();
	int i=0;

	hash.put(i++, new String[]{"tabEsigDetr", "Esigibilit�/Detraibilit�","/gestiva00/tab_esigdetr.jsp" });
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
		list.forEach(e->e.caricaAnniList(context));
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
		this.setMessage("Salvataggio effettuato.");
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
	return (model!=null && model.isLiquidazione_commerciale() && model.getMese()!=null && 
			model.getNextMeseForLiquidazioneDefinitiva().equals(model.getMese()));
}

public boolean isTabRipartizioneFinanziariaVisible() {
	Liquidazione_definitiva_ivaVBulk model = (Liquidazione_definitiva_ivaVBulk)this.getModel();
	return !this.isUoEnte() && model!=null && model.isLiquidazione_commerciale() && model.getMese()!=null;
}

public boolean isTabVariazioneAssociateVisible() {
	Liquidazione_definitiva_ivaVBulk model = (Liquidazione_definitiva_ivaVBulk)this.getModel();
	return !this.isUoEnte() && model!=null && model.isLiquidazione_commerciale() && model.getMese()!=null && model.isRegistroStampato(model.getMese()); 
}

public boolean isTabMandatoRigheAssociateVisible() {
	Liquidazione_definitiva_ivaVBulk model = (Liquidazione_definitiva_ivaVBulk)this.getModel();
	return this.isUoEnte() && model!=null && model.isLiquidazione_commerciale() && model.getMese()!=null && model.isRegistroStampato(model.getMese()); 
}
public boolean isConsultaDettFattureButtonHidden(){
	Liquidazione_definitiva_ivaVBulk model = (Liquidazione_definitiva_ivaVBulk)this.getModel();
	return !(model!=null && model.isLiquidazione_commerciale() && model.getMese()!=null );
}
}
