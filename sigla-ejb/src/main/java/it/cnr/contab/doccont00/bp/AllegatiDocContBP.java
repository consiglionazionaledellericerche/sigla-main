package it.cnr.contab.doccont00.bp;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import it.cnr.contab.doccont00.core.bulk.AllegatoDocContBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.spring.storage.StorageObject;
import it.cnr.contab.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.OrderedHashtable;

public class AllegatiDocContBP extends AllegatiCRUDBP<AllegatoDocContBulk, StatoTrasmissione> {
	private static final long serialVersionUID = 1L;
	private String allegatiFormName;
	private Map<String, String> rifModalitaPagamento = new HashMap<String, String>();

	public AllegatiDocContBP() {
		super();
	}

	public AllegatiDocContBP(String s) {
		super(s);
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
	public boolean isDeleteButtonHidden() {
		return true;
	}
	@Override
	protected boolean excludeChild(StorageObject storageObject) {
		if (storageObject.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()).equals("D:doccont:document"))
			return true;
		return super.excludeChild(storageObject);
	}

	public void setAllegatiFormName(String allegatiFormName) {
		this.allegatiFormName = allegatiFormName;
	}

	@Override
	public String getAllegatiFormName() {
		return allegatiFormName;
	}
	public void addToRifModalitaPagamento(String key, String value) {
		rifModalitaPagamento.put(key, value);
	}

	@Override
	protected boolean isChildGrowable(boolean isGrowable) {
		return true;
	}

	@Override
	protected void getChildDetail(OggettoBulk oggettobulk) {
		AllegatoDocContBulk allegatoDocContBulk = (AllegatoDocContBulk)oggettobulk;
		initializeRifModalitaPagamentoKeys(allegatoDocContBulk);
		if (allegatoDocContBulk.getRifModalitaPagamento()!= null && !allegatoDocContBulk.getRifModalitaPagamento().equalsIgnoreCase("GEN") &&
				(((StatoTrasmissione)getModel()).getStato_trasmissione().equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_INSERITO) ||
						((StatoTrasmissione)getModel()).getStato_trasmissione().equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_TRASMESSO) ||
						((StatoTrasmissione)getModel()).getStato_trasmissione().equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA)))
			setStatus(VIEW);
		else
			setStatus(EDIT);
		super.getChildDetail(allegatoDocContBulk);
	}
	private void initializeRifModalitaPagamentoKeys(AllegatoDocContBulk allegatoDocContBulk) {
		OrderedHashtable rifModalitaPagamentoKeys = allegatoDocContBulk.getRifModalitaPagamentoKeys();
		rifModalitaPagamentoKeys.put("GEN", "Generico");
		if (!allegatoDocContBulk.isToBeCreated() || !(((StatoTrasmissione)getModel()).getStato_trasmissione().equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_INSERITO) ||
				((StatoTrasmissione)getModel()).getStato_trasmissione().equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_TRASMESSO) ||
				((StatoTrasmissione)getModel()).getStato_trasmissione().equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA))) {
			for (String key : rifModalitaPagamento.keySet()) {
				rifModalitaPagamentoKeys.put(key, rifModalitaPagamento.get(key));
			}
		}
	}

	@Override
	protected void completeAllegato(AllegatoDocContBulk allegato) throws ApplicationException  {
		super.completeAllegato(allegato);
		Optional.ofNullable(storeService.getStorageObjectBykey(allegato.getStorageKey()))
				.map(storageObject -> storageObject.getPropertyValue("doccont:rif_modalita_pagamento"))
				.map(String.class::cast)
				.ifPresent(s -> allegato.setRifModalitaPagamento(s));
	}

	@Override
	public boolean isInputReadonly() {
		return super.isInputReadonly();
	}
	@Override
	public boolean isNewButtonHidden() {
		return true;
	}

	@Override
	public boolean isSaveButtonEnabled() {
		return super.isSaveButtonEnabled();
	}

	@Override
	public void update(ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			archiviaAllegati(actioncontext);
		} catch (ApplicationException e) {
			throw handleException(e);
		}
	}

	@Override
	protected String getStorePath(StatoTrasmissione allegatoParentBulk, boolean create) throws BusinessProcessException {
		return allegatoParentBulk.getStorePath();
	}

	@Override
	protected Class<AllegatoDocContBulk> getAllegatoClass() {
		return AllegatoDocContBulk.class;
	}
}