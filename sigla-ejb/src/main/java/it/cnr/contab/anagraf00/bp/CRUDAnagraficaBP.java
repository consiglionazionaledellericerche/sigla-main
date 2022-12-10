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

import java.io.*;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import it.cnr.contab.anagraf00.ejb.*;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoHome;
import it.cnr.contab.compensi00.ejb.CompensoComponentSession;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.comp.Configurazione_cnrComponent;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession;
import it.cnr.contab.utente00.ejb.RuoloComponentSession;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_entrate_gestBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.*;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;


/**
 * Gestisce le catene di elementi correlate con l'anagrafica in uso.
 */

/**
 * @author rpucciarelli
 *
 */

public class CRUDAnagraficaBP extends SimpleCRUDBP {
	
	private final SimpleDetailCRUDController crudDichiarazioni_intento = new SimpleDetailCRUDController("Dichiarazioni_intento",Dichiarazione_intentoBulk.class,"dichiarazioni_intento",this){
		protected void validate(ActionContext context,OggettoBulk bulk) throws ValidationException {
			super.validate(context,bulk);
			validaDichiarazione(context,(Dichiarazione_intentoBulk)bulk);
		}
		public OggettoBulk removeDetail(int i) {
			if (!getModel().isNew()){	
				List list = getDetails();
				Dichiarazione_intentoBulk dic=(Dichiarazione_intentoBulk)list.get(i);
				if (dic.getAnagrafico().isUtilizzata()){
						setMessage("Cancellazione non possibile!");
						return null;
				}
				else
					return super.removeDetail(i);
			}
			return super.removeDetail(i);
		}
	};
	private final SimpleDetailCRUDController crudCarichi_familiari_anag = new SimpleDetailCRUDController("Carichi_familiari_anag",Carico_familiare_anagBulk.class,"carichi_familiari_anag",this){
		protected void validate(ActionContext context,OggettoBulk bulk) throws ValidationException {
			super.validate(context,bulk);
			validaCarico(context,(Carico_familiare_anagBulk)bulk);
		}
		
		public OggettoBulk removeDetail(int i) {
			if (!getModel().isNew()){	
				List list = getDetails();
				Carico_familiare_anagBulk carico=(Carico_familiare_anagBulk)list.get(i);
			
				if(carico.getAnagrafico()!=null &&  carico.getAnagrafico().isUtilizzata_detrazioni()){
					setMessage("Cancellazione non possibile! Carico familiare utilizzato nel calcolo delle detrazioni.");
					return null;
				}
				else{
					if (carico.isConiuge()){
						for(int j = list.size() - 1; j >= 0; j--){
							Carico_familiare_anagBulk carico_familiare=(Carico_familiare_anagBulk)list.get(j);
							validaRemoveDetail(carico_familiare);
						}
					}
					return super.removeDetail(i);
				}
		}
			return super.removeDetail(i);	
		}
	
	};

	private final SimpleDetailCRUDController crudRapporti = new SimpleDetailCRUDController("Rapporti",RapportoBulk.class,"rapporti",this) {
		protected void validate(ActionContext context,OggettoBulk bulk) throws ValidationException {
			validaRapporto(context,(RapportoBulk)bulk);
		}
		public boolean isInputReadonly() {
			return super.isInputReadonly() || isRapportoReadonly((AnagraficoBulk)getParentModel(), (RapportoBulk)getModel());
		}
		public boolean isShrinkable() {
			return super.isShrinkable() && isRapportoShrinkable((RapportoBulk)getModel());
		}
		public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {
			validaRapportoPerCancellazione(context, (RapportoBulk)detail);
		}
	};

	private final SimpleDetailCRUDController crudAnagraficoGruppiIvaCollegati = new SimpleDetailCRUDController("AnagraficoGruppiIvaCollegati",AssGruppoIvaAnagBulk.class,"assGruppoIva",this) {
	};

	public SimpleDetailCRUDController getCrudAnagraficoGruppiIvaCollegati() {
		return crudAnagraficoGruppiIvaCollegati;
	}


	private final SimpleDetailCRUDController crudInquadramenti = new SimpleDetailCRUDController("Inquadramenti",InquadramentoBulk.class,"inquadramenti",crudRapporti) {
		protected void validate(ActionContext context,OggettoBulk bulk) throws ValidationException {
			validaInquadramento(context,(InquadramentoBulk)bulk);
		}
		public boolean isInputReadonly() {
			return super.isInputReadonly() || isInquadramentoReadonly((InquadramentoBulk)getModel())||
			((InquadramentoBulk)this.getModel())==null ||((InquadramentoBulk)this.getModel()).getRapporto()==null||
			!((InquadramentoBulk)this.getModel()).getRapporto().isAbilitato_inquadramento();
		}
		public boolean isShrinkable() {
			return super.isShrinkable() && isInquadramentoShrinkable((InquadramentoBulk)getModel());
		}
		public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {
			validaInquadramentoPerCancellazione(context, (InquadramentoBulk)detail);
		}
	};
	private final SimpleDetailCRUDController crudPagamenti_esterni = new SimpleDetailCRUDController("Pagamenti_esterni",Pagamento_esternoBulk.class,"pagamenti_esterni",this) {
		protected void validate(ActionContext context,OggettoBulk bulk) throws ValidationException {
			validaPagamento_esterno(context,(Pagamento_esternoBulk)bulk);			
		}
		public boolean isInputReadonly() {
			return super.isInputReadonly();
		}
		public boolean isShrinkable() {
			return super.isShrinkable();
		}
		/*public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {
			validaPagamento_esternoPerCancellazione(context, (Pagamento_esternoBulk)detail);
		}*/
	};
	
	private final SimpleDetailCRUDController crudAssociatiStudio = new SimpleDetailCRUDController("AssociatiStudio",Anagrafico_terzoBulk.class,"associatiStudio",this){
		public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
			if (oggettobulk instanceof Anagrafico_terzoBulk) {
				Anagrafico_terzoBulk associato = (Anagrafico_terzoBulk)oggettobulk;
				if (!associato.isToBeCreated() && !associato.isToBeDeleted() &&	associato.getCrudStatus()!=OggettoBulk.UNDEFINED) {
					associato.setDt_canc(it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp());
					associato.setToBeUpdated();
					return associato;
				}
			}
			return super.removeDetail(oggettobulk, i);
		}
		public boolean isInputReadonly() {
			return super.isInputReadonly();
		}
		public boolean isShrinkable() {
			return super.isShrinkable();
		}
	};

	public CRUDAnagraficaBP(String function) throws BusinessProcessException {
		super(function);
		crudInquadramenti.setReadonly(false);
	}
protected it.cnr.jada.util.jsp.Button[] createToolbar() {
	Button[] toolbar = new Button[12];
	//Button[] toolbar = new Button[10];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.search");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.startSearch");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.freeSearch");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.new");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.save");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.delete");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.bringBack");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.print");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.undoBringBack");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.terzi");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.elenco");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.download");

	return toolbar;
}
	/**
	 * Restituisce lanagrafico su cui si stà lavorando.
	 *
	 * @return AnagraficoBulk l'anagrafico.
	 */

	public AnagraficoBulk getAnagrafico() {
		return (AnagraficoBulk)getModel();
	}
public OggettoBulk getBringBackModel(ActionContext context) throws BusinessProcessException {
	try {
		return ((AnagraficoComponentSession)createComponentSession()).getDefaultTerzo(context.getUserContext(),getAnagrafico());
	} catch(Throwable e) {
		throw handleException(e);
	}
}
	/**
	 * Restituisce il CRUDController relativo al tab dei dettagli familiari.
	 *
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudCarichi_familiari_anag() {
		return crudCarichi_familiari_anag;
	}
	/**
	 * Restituisce il CRUDController relativo al tab dell'esportatore abituale.
	 *
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudDichiarazioni_intento() {
		return crudDichiarazioni_intento;
	}
	/**
	 * Restituisce il CRUDController relativo al tab degli inquadramenti.
	 *
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudInquadramenti() {
		return crudInquadramenti;
	}
	/**
	 * Restituisce il CRUDController relativo al tab dei rapporti.
	 *
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudRapporti() {
		return crudRapporti;
	}
public boolean isDeleteButtonEnabled() {

	AnagraficoBulk anagrafico = (AnagraficoBulk)getModel();
	if (anagrafico != null && anagrafico.isDipendente())
		return false;
		
	return 
		super.isDeleteButtonEnabled() &&
		((AnagraficoBulk)getModel()).getDt_fine_rapporto() == null;
}
public boolean isScaricaButtonEnabled() {
	if(getFile()!=null)
		return true;
	else
		return false;
}
public boolean isElencoButtonHidden() {
	if(isAbilitatoECF()&& isElencoButtonEnabled())
		return false;
	else
		return true;
}
protected boolean isInquadramentoReadonly(InquadramentoBulk inquadramento) {
	return false;
}
protected boolean isInquadramentoShrinkable(InquadramentoBulk inquadramento) {
	return true;
}
protected boolean isRapportoReadonly(AnagraficoBulk anagraficoBulk, RapportoBulk rapporto) {
	return rapporto != null && rapporto.getTipo_rapporto() != null && (rapporto.getTipo_rapporto().isDipendente() && !anagraficoBulk.isAbilitatoTrattamenti());
}
protected boolean isRapportoShrinkable(RapportoBulk rapporto) {
	return true;
}
public boolean isTerziButtonEnabled() {
	return isEditing() || isViewing();
}
	/**
	 * Imposta come attivi i tab di default.
	 *
	 * @param context <code>ActionContext</code>
	 */

	public void resetTabs(ActionContext context) {
		setTab("tab","tabAnagrafica");
		setTab("tabTerziRecapiti","tabTerziTelefoni");
		setTab("tabRapporti","tabDettagliRapporto");
	}
protected void validaPagamento_esterno(ActionContext context,Pagamento_esternoBulk pagamento_esterno) throws ValidationException {
	try {
		if (pagamento_esterno.getDt_pagamento() == null)
			throw new ValidationException("Data pagamento obbligatoria");

		if (pagamento_esterno.getIm_pagamento() == null)
			throw new ValidationException("Importo pagamento obbligatorio");
			
		if (pagamento_esterno.getDs_pagamento() == null)
			throw new ValidationException("Descrizione del pagamento obbligatoria");

		if (pagamento_esterno.getTipo_rapporto() == null)
			throw new ValidationException("Inserire il tipo di rapporto!");

	} catch(javax.ejb.EJBException e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}	
}
protected void validaInquadramento(ActionContext context,InquadramentoBulk inquadramento) throws ValidationException {
	try {
		if (inquadramento.getPg_rif_inquadramento() == null)
			throw new ValidationException("Tipo inquadramento obbligatorio");

		if (inquadramento.getDt_ini_validita() == null)
			throw new ValidationException("Data inizio validita obbligatoria");

		if (inquadramento.getDt_ini_validita().before(inquadramento.getRapporto().getDt_ini_validita()))
			throw new ValidationException("La data di inizio validita dell'inquadramento deve essere maggiore della data di inizio validità del rapporto.");

		if (inquadramento.getDt_ini_validita().after(inquadramento.getRapporto().getDt_fin_validita()))
			throw new ValidationException("La data di inizio validita dell'inquadramento deve essere minore o uguale della data di inizio validità del rapporto.");

		if (inquadramento.getDt_fin_validita() != null && inquadramento.getDt_fin_validita().after(inquadramento.getRapporto().getDt_fin_validita()))
			throw new ValidationException("La data di fine validita dell'inquadramento deve essere minore o uguale della data di inizio validità del rapporto.");

		boolean fine_validita_infinito = inquadramento.getDt_fin_validita() == null;

		if (!fine_validita_infinito	&& inquadramento.getDt_ini_validita().after(inquadramento.getDt_fin_validita()))
			throw new ValidationException("La data inizio validità deve essere minore della data di fine validità");
		java.util.Calendar cal = java.util.Calendar.getInstance();
		
		for (java.util.Iterator i = inquadramento.getRapporto().getInquadramenti().iterator();i.hasNext();) {
			InquadramentoBulk inquadramento2 = (InquadramentoBulk)i.next();
			if (inquadramento2 == inquadramento) continue;
			if (inquadramento.getDt_ini_validita().before(inquadramento2.getDt_ini_validita())) {
				if (fine_validita_infinito) {
					if (inquadramento.getDt_fin_validita() == null || inquadramento.getDt_fin_validita().after(inquadramento2.getDt_fin_validita())) {
						cal.setTime(inquadramento2.getDt_ini_validita());
						cal.add(cal.DAY_OF_YEAR,-1);
						inquadramento.setDt_fin_validita(new java.sql.Timestamp(cal.getTime().getTime()));
					}
				} else if (inquadramento.getDt_fin_validita().equals(inquadramento2.getDt_ini_validita()) || inquadramento.getDt_fin_validita().after(inquadramento2.getDt_fin_validita()))
					throw new ValidationException("Data fine validità non valida perchè interseca un altro periodo");
			} else if (!inquadramento.getDt_ini_validita().after(inquadramento2.getDt_fin_validita())) {
				if (inquadramento2.getDt_fin_validita().equals(it.cnr.contab.docamm00.tabrif.bulk.CambioHome.getFineinfinito())) {
					cal.setTime(inquadramento.getDt_ini_validita());
					cal.add(cal.DAY_OF_YEAR,-1);
					inquadramento2.setDt_fin_validita(new java.sql.Timestamp(cal.getTime().getTime()));
					inquadramento2.setToBeUpdated();
				} else
					throw new ValidationException("Data inizio validità non valida perchè interseca un altro periodo");
			}
		}
		if (fine_validita_infinito && inquadramento.getDt_fin_validita() == null)
			inquadramento.setDt_fin_validita(inquadramento.getRapporto().getDt_fin_validita());

		// Non è possibile impostare una data di fine validità inferiore alla data
		// di fine validita originale, a meno che sia superiore alla data odierna.
		if (inquadramento.getDt_fin_validita_originale() != null) {
			java.sql.Timestamp dt_fin_validita_minima = DateUtils.min(
				//it.cnr.jada.util.ejb.EJBCommonServices.getServerDate(),
				inquadramento.getMax_dt_fin_validita_missione(),
				inquadramento.getDt_fin_validita_originale());
			if (inquadramento.getDt_fin_validita().before(dt_fin_validita_minima)) {
				inquadramento.setDt_fin_validita(dt_fin_validita_minima);
				throw new ValidationException("La data di fine validità non può essere anteriore al "+FieldProperty.getFormat("date_short").format(dt_fin_validita_minima));
			}
		}
	} catch(javax.ejb.EJBException e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}
}
protected void validaInquadramentoPerCancellazione(ActionContext context,InquadramentoBulk inquadramento) throws ValidationException {
	try {
		if (inquadramento.isNotNew() &&
			inquadramento.getDt_ini_validita() != null &&
			//inquadramento.getDt_ini_validita().before(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()))
			inquadramento.getMax_dt_fin_validita_missione()!=null && inquadramento.getDt_ini_validita().before(inquadramento.getMax_dt_fin_validita_missione()))
			//throw new ValidationException("Gli inquadramenti con data di inizio validità antecedente alla data odierna non sono eliminabili");
			throw new ValidationException("Gli inquadramenti con data di inizio validità antecedente al "+FieldProperty.getFormat("date_short").format(inquadramento.getMax_dt_fin_validita_missione())+" non sono eliminabili");
	} catch(javax.ejb.EJBException e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}
}

protected void validaRapporto(ActionContext context,RapportoBulk rapporto) throws ValidationException {
	try {
		// Controllo tipo rapporto non nullo
		if (rapporto.getCd_tipo_rapporto() == null)
			throw new ValidationException("Tipo rapporto obbligatorio");

		// Controllo dt_ini_validita non nulla
		if (rapporto.getDt_ini_validita() == null)
			throw new ValidationException("Data inizio validita obbligatoria");

		boolean fine_validita_infinito = rapporto.getDt_fin_validita() == null;

		if (!fine_validita_infinito) {
			// Se dt_fin_validita è non nulla deve essere superiore a dt_ini_validita
			if (rapporto.getDt_ini_validita().after(rapporto.getDt_fin_validita()))
				throw new ValidationException("La data di inizio validità deve essere minore della data di fine validità");
			if(rapporto.isAbilitato_inquadramento()){
				// e maggiore delle dt_fin_validita di tutti gli inquadrament
				for (java.util.Iterator i = rapporto.getInquadramenti().iterator();i.hasNext();) {
					InquadramentoBulk inquadramento = (InquadramentoBulk)i.next();
					if (inquadramento.getDt_fin_validita() != null && inquadramento.getDt_fin_validita().after(rapporto.getDt_fin_validita()))
						throw new ValidationException("La data di fine validità del rapporto deve essere maggiore delle date di fine validità degli inquadramenti.");
				}
			}
			else if (rapporto.getInquadramenti()!=null && !rapporto.getInquadramenti().isEmpty()){
					throw new ValidationException("Per questo tipo rapporto non sono previsti inquadramenti.");				
			}
				
		}

		java.util.Calendar cal = java.util.Calendar.getInstance();
		
		for (java.util.Iterator i = rapporto.getAnagrafico().getRapporti().iterator();i.hasNext();) {
			RapportoBulk rapporto2 = (RapportoBulk)i.next();
			if (rapporto2 == rapporto) continue;

			// Il controllo di intersezione dei rapporti va fatto solo per lo stesso
			// tipo rapporto!
			if (!rapporto.getCd_tipo_rapporto().equals(rapporto2.getCd_tipo_rapporto()))
				continue;

		
			if (rapporto.getDt_ini_validita().before(rapporto2.getDt_ini_validita())) {
				if (fine_validita_infinito) {
					// Se la dt_fin_validita non è stata specificata calcolo la minima dt_ini_validita-1
					// dei rapporti con dt_ini_validita superiore
					if (rapporto.getDt_fin_validita() == null || rapporto.getDt_fin_validita().after(rapporto2.getDt_fin_validita())) {
						cal.setTime(rapporto2.getDt_ini_validita());
						cal.add(cal.DAY_OF_YEAR,-1);
						rapporto.setDt_fin_validita(new java.sql.Timestamp(cal.getTime().getTime()));
					}
				} else if (!rapporto.getDt_fin_validita().before(rapporto2.getDt_ini_validita()))
					throw new ValidationException("Data fine validità non valida perchè interseca un altro periodo");
			} else if (!rapporto.getDt_ini_validita().after(rapporto2.getDt_fin_validita())) {
				if (rapporto2.getDt_fin_validita().equals(it.cnr.contab.docamm00.tabrif.bulk.CambioHome.getFineinfinito())) {
					cal.setTime(rapporto.getDt_ini_validita());
					cal.add(cal.DAY_OF_YEAR,-1);
					rapporto2.setDt_fin_validita(new java.sql.Timestamp(cal.getTime().getTime()));
					rapporto2.setToBeUpdated();
				} else
					throw new ValidationException("Data inizio validità non valida perchè interseca un altro periodo");
			}
		}

		// Se dt_fin_validita è ancora nulla (non ho trovato un altro rapporto
		// dopo) la imposto a infito
		if (fine_validita_infinito && rapporto.getDt_fin_validita() == null)
			rapporto.setDt_fin_validita(it.cnr.contab.docamm00.tabrif.bulk.CambioHome.getFineinfinito());

		// Non è possibile impostare una data di fine validità inferiore alla data
		// di fine validita originale, a meno che sia superiore alla data odierna.
		if (rapporto.getDt_fin_validita_originale() != null) {
			java.sql.Timestamp dt_fin_validita_minima = DateUtils.min(
				it.cnr.jada.util.ejb.EJBCommonServices.getServerDate(),
				rapporto.getDt_fin_validita_originale());
			if (rapporto.getDt_fin_validita().before(dt_fin_validita_minima)) {
				rapporto.setDt_fin_validita(dt_fin_validita_minima);
				throw new ValidationException("La data di fine validità non può essere anteriore al "+FieldProperty.getFormat("date_short").format(dt_fin_validita_minima));
			}
		}
	} catch(javax.ejb.EJBException e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}
}
protected void validaRapportoPerCancellazione(ActionContext context,RapportoBulk rapporto) throws ValidationException {
	try {
		if (rapporto.getTipo_rapporto() != null &&
		 	rapporto.getTipo_rapporto().isDipendente())
			throw new ValidationException("I rapporti di tipo dipendente non sono eliminabili");
		if (rapporto.isNotNew() &&
			rapporto.getDt_ini_validita() != null &&
			rapporto.getDt_ini_validita().before(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()))
			throw new ValidationException("I rapporti con data di inizio validità antecedente alla data odierna non sono eliminabili");
	} catch(javax.ejb.EJBException e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}
}
	/**
	 * Returns the crudPagamenti_esterni.
	 * @return SimpleDetailCRUDController
	 */
	public SimpleDetailCRUDController getCrudPagamenti_esterni() {
		return crudPagamenti_esterni;
	}
	public boolean isItalianoEsteroModificabile(UserContext userContext, AnagraficoBulk anagrafico) throws BusinessProcessException {
		try{
			AnagraficoComponentSession sess = (AnagraficoComponentSession)createComponentSession();
			return sess.isItalianoEsteroModificabile(userContext, anagrafico);

		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	public void checkConiugeAlreadyExistFor(ActionContext context, AnagraficoBulk bulk, Carico_familiare_anagBulk carichi_fam) throws BusinessProcessException {
		try{
			AnagraficoComponentSession sess = (AnagraficoComponentSession)createComponentSession();
			 sess.checkConiugeAlreadyExistFor(context.getUserContext(),bulk,carichi_fam);

		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
		
	}
	protected void validaCarico(ActionContext context,Carico_familiare_anagBulk carico) throws ValidationException {
		if(carico.isToBeCreated()){
		try {
			AnagraficoComponentSession sess = (AnagraficoComponentSession)createComponentSession();

			sess.controllaUnicitaCaricoInAnnoImposta(context.getUserContext(),carico.getAnagrafico(),carico);
			
			if (carico.isConiuge()|| carico.isFiglio()){
				sess.checkConiugeAlreadyExistFor(context.getUserContext(),carico.getAnagrafico(),carico);
			}
//			if (carico.isFiglio() && 
//				!carico.getFl_primo_figlio_manca_con() && 
//				carico.getPrc_carico().compareTo(new java.math.BigDecimal(100))==0 &&
//				//!sess.esisteConiugeValido(context.getUserContext(),carico.getAnagrafico(),carico) &&
//				carico.getCodice_fiscale_altro_gen() == null)
//				throw new ValidationException("Attenzione: è necessario specificare il Codice fiscale dell'altro genitore");
			java.util.GregorianCalendar data_da = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
			java.util.GregorianCalendar data_a = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
			data_da.setTime(carico.getDt_ini_validita());
			data_a.setTime(carico.getDt_fin_validita());
//			if (data_da.get(java.util.GregorianCalendar.YEAR)!=data_a.get(java.util.GregorianCalendar.YEAR)){
//				throw new ValidationException("La data di inizio e fine validità devono appartenere allo stesso esercizio.");
//			}
			sess.checkCaricoAlreadyExistFor(context.getUserContext(),carico.getAnagrafico(),carico);
		} catch(javax.ejb.EJBException e) {
			throw new it.cnr.jada.DetailedRuntimeException(e);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw new it.cnr.jada.DetailedRuntimeException(ex);
		}catch (BusinessProcessException ex) {
			throw new it.cnr.jada.DetailedRuntimeException(ex);
		}catch (java.rmi.RemoteException ex) {
			throw new it.cnr.jada.DetailedRuntimeException(ex);
		}
		}
	}
	public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
		super.basicEdit(context, bulk, doInitializeForEdit);

		if (getStatus()!=VIEW){
			AnagraficoBulk anagrafico= (AnagraficoBulk)getModel();
			if (anagrafico!=null && anagrafico.getDt_fine_rapporto() != null) {
				setStatus(VIEW);
			}
		}
	}
	
	
	public boolean isGestoreIstat(UserContext context, AnagraficoBulk anagraficoBulk) throws ComponentException, RemoteException {
		return !anagraficoBulk.isNotGestoreIstat();
	}

	public boolean isGestoreOk(UserContext context) throws ComponentException, RemoteException {
		return UtenteBulk.isGestoreIstatSiope(context);
	}
	
	protected void validaRemoveDetail(Carico_familiare_anagBulk carico_familiare) {
		if (carico_familiare.isFiglio()&& 
			!carico_familiare.getFl_primo_figlio_manca_con() && 
			carico_familiare.getPrc_carico().compareTo(new java.math.BigDecimal(100))==0 &&
			carico_familiare.getCodice_fiscale_altro_gen() == null)
		{
			 new ApplicationException("Attenzione: non è possibile cancellare il Coniuge poichè esiste un figlio per il quale non è stato indicato il Codice fiscale dell'altro genitore");
		}	
	}
	public boolean isGestiteDeduzioniIrpef(UserContext userContext) throws BusinessProcessException {
		try{
			AnagraficoComponentSession sess = (AnagraficoComponentSession)createComponentSession();
			return sess.isGestiteDeduzioniIrpef(userContext);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	public boolean isGestiteDeduzioniFamily(UserContext userContext) throws BusinessProcessException {
		try{
			AnagraficoComponentSession sess = (AnagraficoComponentSession)createComponentSession();
			return sess.isGestiteDeduzioniFamily(userContext);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	public boolean isGestiteDetrazioniAltre(UserContext userContext) throws BusinessProcessException {
		try{
			AnagraficoComponentSession sess = (AnagraficoComponentSession)createComponentSession();
			return sess.isGestiteDetrazioniAltre(userContext);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	public boolean isGestiteDetrazioniFamily(UserContext userContext) throws BusinessProcessException {
		try{
			AnagraficoComponentSession sess = (AnagraficoComponentSession)createComponentSession();
			return sess.isGestiteDetrazioniFamily(userContext);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}	
	public boolean isGestitoCreditoIrpef(UserContext userContext) throws BusinessProcessException {
		try{
			AnagraficoComponentSession sess = (AnagraficoComponentSession)createComponentSession();
			return sess.isGestitoCreditoIrpef(userContext);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}	
	public boolean isElencoButtonEnabled() {
		return isElenco();
	}
	
	private boolean elenco = true;
	private boolean attivaEconomica = false;
	private String file;
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		try {
			int solaris = EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.YEAR);
			int esercizioScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(actioncontext.getUserContext()).intValue();
			setElenco(solaris == esercizioScrivania+1);
			setAttivaEconomica(Utility.createConfigurazioneCnrComponentSession().isAttivaEconomica(actioncontext.getUserContext()));
			super.init(config, actioncontext);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	public boolean isElenco() {
		return elenco;
	}
	public void setElenco(boolean elenco) {
		this.elenco = elenco;
	}

	public boolean isAttivaEconomica() {
		return attivaEconomica;
	}

	public void setAttivaEconomica(boolean attivaEconomica) {
		this.attivaEconomica = attivaEconomica;
	}

	public void Estrazione(ActionContext context) throws ComponentException, RemoteException, BusinessProcessException{
		  try{			  
			  Long prog_estrazione=((AnagraficoComponentSession)createComponentSession()).Max_prog_estrazione(context.getUserContext());
			  
			  File f = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/",
					  "ECF"+it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext())+"-"+
					  +EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.DAY_OF_MONTH)+
					  +new Integer(EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.MONTH)+1)+
					  +EcfBulk.getDateCalendar(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()).get(java.util.Calendar.YEAR)+"-"+
					  +prog_estrazione.longValue()+".ecf");
			  OutputStream os = (OutputStream)new FileOutputStream(f);
		      OutputStreamWriter osw = new OutputStreamWriter(os);
		      BufferedWriter bw = new BufferedWriter(osw);
		      AnagraficoBulk ente = ((AnagraficoComponentSession)createComponentSession()).getAnagraficoEnte(context.getUserContext());
		      ((AnagraficoComponentSession)createComponentSession()).Popola_ecf(context.getUserContext(),prog_estrazione);
		      java.util.List lista=((AnagraficoComponentSession)createComponentSession()).EstraiLista(context.getUserContext(),prog_estrazione);
		      if (!lista.isEmpty())
		     {  
		    	  //Testata - posizionale lunghezza 1800 caratteri
		    	  String Codice_Fiscale =ente.getCodice_fiscale();
		    	  String P_iva=ente.getPartita_iva();
		    	  if(Codice_Fiscale==null||P_iva==null)
		    		  throw new ApplicationException("Partita Iva o Codice Fiscale non valorizzati per l'ente!");		
		    	  Long CodFisNum=null;
		    	  try{
		    		  CodFisNum =new Long(Codice_Fiscale);
		    	  }catch (NumberFormatException e) {
		    		  CodFisNum=null;
				 }
		    	  //parte iniziale Fissa
		    	  bw.append(new String("0ECF0038"));
		    	  if(CodFisNum!=null)
		    		  	Codice_Fiscale=Formatta(Codice_Fiscale,"S",16," ");
		    	  
		    		bw.append(Codice_Fiscale);
		    		bw.append(Formatta(P_iva,"S",11," "));
		    		
		    	  if (ente.getTi_entita()==AnagraficoBulk.FISICA){
		    		  if (	ente.getCognome()==null||
		    				ente.getNome()==null||
		    				ente.getTi_sesso()==null||
		    				ente.getDt_nascita()==null||
		    				ente.getComune_nascita()==null||
		    				ente.getComune_nascita().getDs_comune()==null||
		    				ente.getComune_nascita().getCd_provincia()==null)
		    			  throw new ApplicationException("Attenzione dati anagrafici mancanti!");
		    	  }else
		    	  {
		    		if(ente.getRagione_sociale()==null||
		    			ente.getComune_fiscale()==null||
		    			ente.getComune_fiscale().getDs_comune()==null||
		    			ente.getComune_fiscale().getCd_provincia()==null)
		    			  throw new ApplicationException("Attenzione dati anagrafici mancanti!");
		    	  }
		    	  // Obbligatori se Persona Fisica Cognome 
		    	  bw.append(Formatta(ente.getCognome(),"S",26," "));
		    	  bw.append(Formatta(ente.getNome(),"S",25," "));
		    	  bw.append(Formatta(ente.getTi_sesso(),"S",1," "));
		    	  bw.append(ente.getDt_nascita()==null?Formatta(null,"S",8,"0"):Formatta((new SimpleDateFormat("ddmmyyyy")).format(ente.getDt_nascita()),"S",8,"0"));
		    	  bw.append(ente.getComune_nascita()==null?Formatta(null,"S",40," "):Formatta(ente.getComune_nascita().getDs_comune(),"S",40," "));
		    	  bw.append(ente.getComune_nascita()==null?Formatta(null,"S",2," "):Formatta(ente.getComune_nascita().getCd_provincia(),"S",2," "));
		    	  // Obbligatori se non è Persona Fisica  
		    	  bw.append(Formatta(ente.getRagione_sociale(),"S",70," "));
		    	  bw.append(ente.getComune_fiscale()==null?Formatta(null,"S",40," "):Formatta(ente.getComune_fiscale().getDs_comune(),"S",40," "));
		    	  bw.append(ente.getComune_fiscale()==null?Formatta(null,"S",2," "):Formatta(ente.getComune_fiscale().getCd_provincia(),"S",2," "));
		    	  //???
		    	  bw.append(Formatta(null,"S",16," ")); //Codice Fiscale obbligato se diverso da contribuente
		    	  bw.append(Formatta(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).toString(),"S",4," ")); // anno riferimento
		    	  bw.append(Formatta(null,"S",4,"0")); //Prog invio
		    	  bw.append(Formatta(null,"S",4,"0")); //Prog totale invio
		    	  // intermediario
		    	  bw.append(Formatta(null,"S",16," ")); //Codice Fiscale intermediario
		    	  bw.append(Formatta(null,"S",5,"0")); //n° iscrizione CAF
		    	  bw.append(Formatta(null,"S",1,"0")); //"1" se comunicazione predisposta dal contribuente "2" se perdisposta da chi effettua l'invio "0" altrimenti
		    	  bw.append(Formatta(null,"S",8,"0"));// Data dell'impegno
		    	  // Fine intermediario 
		    	  // riempimento record di testata
		    	  bw.append(Formatta(null,"S",1490," "));
		    	  bw.append("A");
		    	  //bw.newLine(); da problemi per il formato del file
		    	  bw.append("\r\n");
		          // Fine Testata
		          // Variabili di appoggio per il record di Riepilogo
		          Integer Num_cliente=new Integer(0);
		          Integer Num_forn=new Integer(0);
		          BigDecimal SumCL004001=new BigDecimal(0);
		          BigDecimal SumCL004002=new BigDecimal(0);
		          BigDecimal SumCL005001=new BigDecimal(0);
		          BigDecimal SumCL006001=new BigDecimal(0);
		          BigDecimal SumCL007001=new BigDecimal(0);
		          BigDecimal SumCL008001=new BigDecimal(0);
		          BigDecimal SumCL008002=new BigDecimal(0);
		          BigDecimal SumCL009001=new BigDecimal(0);
		          BigDecimal SumCL010001=new BigDecimal(0);
		          BigDecimal SumCL011001=new BigDecimal(0);
		          BigDecimal SumFR004001=new BigDecimal(0);
		          BigDecimal SumFR004002=new BigDecimal(0);
		          BigDecimal SumFR005001=new BigDecimal(0);
		          BigDecimal SumFR006001=new BigDecimal(0);
		          BigDecimal SumFR007001=new BigDecimal(0);
		          BigDecimal SumFR008001=new BigDecimal(0);
		          BigDecimal SumFR009001=new BigDecimal(0);
		          BigDecimal SumFR009002=new BigDecimal(0);
		          BigDecimal SumFR010001=new BigDecimal(0);
		          BigDecimal SumFR011001=new BigDecimal(0);
		          BigDecimal SumFR012001=new BigDecimal(0);
		          BigDecimal SumFR013001=new BigDecimal(0);
		          Integer num_col_cl=0;
		          Integer num_col_fr=0;
		          Integer limite_col=70;
		          // recupero dettagli dalla lista dei C/F
				    for (Iterator i = lista.iterator(); i.hasNext();) {
				    	 EcfBulk ecf =(EcfBulk)i.next();
				    	 // verifico che sia un cliente e che quindi abbia valorizzato i campi obbligatori per i clienti 
				    	 // dal 2008 if(ecf.getCl002001()!=null||ecf.getCl003001()!=null){
				    	 if((it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext())<2008 && ecf.getCl003001()!=null )||
				    	   it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext())>=2008 && ecf.getCl002001()!=null ){
				    		 // carattere iniziale riga cliente solo per il 1°
				    		 if (Num_cliente==0)
				    			 bw.append("1");  
				    		 // essendo un record non posizionale ogni volta prima di scrivere una coppia intestazione-valore 
				    		 // controllo che non abbia superato il limite delle 70 colonne ed eventualmente chiudo la riga 
				    		 if(num_col_cl.compareTo(limite_col)==0)
				    			 num_col_cl=chiudi_riga(bw);
				    		 	if(num_col_cl==0 && Num_cliente!=0)
				    		 		bw.append("1");
					    		 Num_cliente++;
					    		 bw.append("CL001001");
					    		 bw.append(Formatta(Num_cliente.toString(),"D",16," "));
					    		 num_col_cl++;
					    		 if(num_col_cl.compareTo(limite_col)==0)
					    			 num_col_cl=chiudi_riga(bw);
					    			
					    		 if(ecf.getCl002001()!=null){
					    			 // dopo aver verificato che ho un'altra coppia da inserire
					    			 // e che ho appena azzerato il numero delle colonne inserisco 
					    			 // "1" che indica che la nuova riga è di tipo cliente
					    			 if(num_col_cl==0)
					    				 bw.append("1");
					    			 bw.append("CL002001");
						    		 bw.append(Formatta(ecf.getCl002001(),"S",16," "));
						    		 num_col_cl++;
					    		 }
					    		 if(num_col_cl.compareTo(limite_col)==0)
					    			 num_col_cl=chiudi_riga(bw);
					    		
					    		 if(ecf.getCl003001()!=null){
					    			 if(num_col_cl==0)
					    				 bw.append("1");
					    			 bw.append("CL003001");
						    		 bw.append(Formatta(ecf.getCl003001(),"S",16," "));
						    		 num_col_cl++;
					    		 }
					    		 if(num_col_cl.compareTo(limite_col)==0)
					    			 num_col_cl=chiudi_riga(bw);
					    		
					    		 if(ecf.getCl004001()!=null && ecf.getCl004001().compareTo(new BigDecimal(0))!=0){
					    			 if(num_col_cl==0)
					    				 bw.append("1");
					    			 bw.append("CL004001");
						    		 bw.append(Formatta(ecf.getCl004001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
						    		 SumCL004001=SumCL004001.add(ecf.getCl004001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
						    		 num_col_cl++;
					    		 }
					    		 if(num_col_cl.compareTo(limite_col)==0)
					    			 num_col_cl=chiudi_riga(bw);
					    		
					    		 if(ecf.getCl004002()!=null && ecf.getCl004002().compareTo(new BigDecimal(0))!=0){
					    			 if(num_col_cl==0)
					    				 bw.append("1");
					    			 bw.append("CL004002");
						    		 bw.append(Formatta(ecf.getCl004002().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
						    		 SumCL004002=SumCL004002.add(ecf.getCl004002().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
						    		 num_col_cl++;
					    		 }
					    		 if(num_col_cl.compareTo(limite_col)==0)
					    			 num_col_cl=chiudi_riga(bw);
					    		
					    		 if(ecf.getCl005001()!=null && ecf.getCl005001().compareTo(new BigDecimal(0))!=0){
					    			 if(num_col_cl==0)
					    				 bw.append("1");
					    			 bw.append("CL005001");
						    		 bw.append(Formatta(ecf.getCl005001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
						    		 SumCL005001=SumCL005001.add(ecf.getCl005001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
						    		 num_col_cl++;
					    		 }
					    		 if(num_col_cl.compareTo(limite_col)==0)
					    			 num_col_cl=chiudi_riga(bw);
					    		
					    		 if(ecf.getCl006001()!=null && ecf.getCl006001().compareTo(new BigDecimal(0))!=0){
					    			 if(num_col_cl==0)
					    				 bw.append("1");
					    			 bw.append("CL006001");
						    		 bw.append(Formatta(ecf.getCl006001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
						    		 SumCL006001=SumCL006001.add(ecf.getCl006001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
						    		 num_col_cl++;
					    		 }
					    		 if(num_col_cl.compareTo(limite_col)==0)
					    			 num_col_cl=chiudi_riga(bw);
					    		
					    		 if(ecf.getCl007001()!=null && ecf.getCl007001().compareTo(new BigDecimal(0))!=0){
					    			 if(num_col_cl==0)
					    				 bw.append("1");
					    			 bw.append("CL007001");
						    		 bw.append(Formatta(ecf.getCl007001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
						    		 SumCL007001=SumCL007001.add(ecf.getCl007001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
						    		 num_col_cl++;
					    		 }
					    		 if(num_col_cl.compareTo(limite_col)==0)
					    			 num_col_cl=chiudi_riga(bw);
					    		
					    		 if(ecf.getCl008001()!=null && ecf.getCl008001().compareTo(new BigDecimal(0))!=0){
					    			 if(num_col_cl==0)
					    				 bw.append("1");
					    			 bw.append("CL008001");
						    		 bw.append(Formatta(ecf.getCl008001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
						    		 SumCL008001=SumCL008001.add(ecf.getCl008001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
						    		 num_col_cl++;
					    		 }
					    		 if(num_col_cl.compareTo(limite_col)==0)
					    			 num_col_cl=chiudi_riga(bw);
					    		
					    		 if(ecf.getCl008002()!=null && ecf.getCl008002().compareTo(new BigDecimal(0))!=0){
					    			 if(num_col_cl==0)
					    				 bw.append("1");
					    			 bw.append("CL008002");
						    		 bw.append(Formatta(ecf.getCl008002().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
						    		 SumCL008002=SumCL008002.add(ecf.getCl008002().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
						    		 num_col_cl++;
					    		 }
					    		 if(num_col_cl.compareTo(limite_col)==0)
					    			 num_col_cl=chiudi_riga(bw);
					    		
					    		 if(ecf.getCl009001()!=null && ecf.getCl009001().compareTo(new BigDecimal(0))!=0){
					    			 if(num_col_cl==0)
					    				 bw.append("1");
					    			 bw.append("CL009001");
						    		 bw.append(Formatta(ecf.getCl009001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
						    		 SumCL009001=SumCL009001.add(ecf.getCl009001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
						    		 num_col_cl++;
					    		 }
					    		 if(num_col_cl.compareTo(limite_col)==0)
					    			 num_col_cl=chiudi_riga(bw);
					    		
					    		 if(ecf.getCl010001()!=null && ecf.getCl010001().compareTo(new BigDecimal(0))!=0){
					    			 if(num_col_cl==0)
					    				 bw.append("1");
					    			 bw.append("CL010001");
						    		 bw.append(Formatta(ecf.getCl010001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
						    		 SumCL010001=SumCL010001.add(ecf.getCl010001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
						    		 num_col_cl++;
					    		 }
					    		 if(num_col_cl.compareTo(limite_col)==0)
					    			 num_col_cl=chiudi_riga(bw);
					    		
					    		 if(ecf.getCl011001()!=null && ecf.getCl011001().compareTo(new BigDecimal(0))!=0){
					    			 if(num_col_cl==0)
					    				 bw.append("1");
					    			 bw.append("CL011001");
						    		 bw.append(Formatta(ecf.getCl011001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
						    		 SumCL011001=SumCL011001.add(ecf.getCl011001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
						    		 num_col_cl++;
					    		 }
					    	 }
					    	 else // dato obbligatorio dal 2008if (ecf.getFr002001()!=null || ecf.getFr003001()!=null){
					    		 //if (ecf.getFr003001()!=null){
					    		 if((it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext())<2008 && ecf.getFr003001()!=null )||
									   it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext())>=2008 && ecf.getFr002001()!=null
								   	   && ecf.getFr003001()!=null ){	 
						    		 if (Num_forn==0 && Num_cliente!=0 && num_col_cl!=0){
						    				 bw.append(Formatta(null,"D",(limite_col-num_col_cl--)*24," "));
							    			 bw.append(Formatta(null,"D",116," "));
							    			 bw.append("A");
									    	  //bw.newLine(); da problemi per il formato del file
									    	  bw.append("\r\n");
							    	 }
					    		 
						    		 // carattere iniziale riga fornitore solo per il 1°
						    		 if (Num_forn==0||num_col_fr==0)
						    			 bw.append("2"); 
						    		 // essendo un record non posizionale ogni volta prima di scrivere una coppia intestazione-valore 
						    		 // controllo che non abbia superato il limite delle 70 colonne ed eventualmente chiudo la riga 
						    		 if(num_col_fr.compareTo(limite_col)==0)
						    			 num_col_fr=chiudi_riga(bw);
						    		 Num_forn++;
						    		 bw.append("FR001001");
						    		 bw.append(Formatta(Num_forn.toString(),"D",16," "));
						    		 num_col_fr++;
						    		 if(num_col_fr.compareTo(limite_col)==0)
						    			 num_col_fr=chiudi_riga(bw);
						    		 if(ecf.getFr002001()!=null){
						    			 // dopo aver verificato che ho un'altra coppia da inserire
						    			 // e che ho appena azzerato il numero delle colonne inserisco 
						    			 // "2" che indica che la nuova riga è di tipo fornitore
						    			 if(num_col_fr==0)
							    		 		bw.append("2");
						    			 bw.append("FR002001");
							    		 bw.append(Formatta(ecf.getFr002001(),"S",16," "));
							    		 num_col_fr++;
						    		 }
						    		 if(num_col_fr.compareTo(limite_col)==0)
						    			 num_col_fr=chiudi_riga(bw);
						    		 if(ecf.getFr003001()!=null){
						    			 if(num_col_fr==0)
							    		 		bw.append("2");
						    			 bw.append("FR003001");
							    		 bw.append(Formatta(ecf.getFr003001(),"S",16," "));
							    		 num_col_fr++;
						    		 } 
						    		 if(num_col_fr.compareTo(limite_col)==0)
						    			 num_col_fr=chiudi_riga(bw);
						    		 if(ecf.getFr004001()!=null && ecf.getFr004001().compareTo(new BigDecimal(0))!=0){
						    			 if(num_col_fr==0)
							    		 		bw.append("2");
						    			 bw.append("FR004001");
							    		 bw.append(Formatta(ecf.getFr004001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
							    		 SumFR004001=SumFR004001.add(ecf.getFr004001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
							    		 num_col_fr++;
						    		 }	 
						    		 if(num_col_fr.compareTo(limite_col)==0)
						    			 num_col_fr=chiudi_riga(bw);
						    		 if(ecf.getFr004002()!=null && ecf.getFr004002().compareTo(new BigDecimal(0))!=0){
						    			 if(num_col_fr==0)
							    		 		bw.append("2");
						    			 bw.append("FR004002");
							    		 bw.append(Formatta(ecf.getFr004002().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
							    		 SumFR004002=SumFR004002.add(ecf.getFr004002().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
							    		 num_col_fr++;
						    		 }
						    		 if(num_col_fr.compareTo(limite_col)==0)
						    			 num_col_fr=chiudi_riga(bw);
						    		 if(ecf.getFr005001()!=null && ecf.getFr005001().compareTo(new BigDecimal(0))!=0){
						    			 if(num_col_fr==0)
							    		 		bw.append("2");
						    			 bw.append("FR005001");
							    		 bw.append(Formatta(ecf.getFr005001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
							    		 SumFR005001=SumFR005001.add(ecf.getFr005001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
							    		 num_col_fr++;
						    		 }
						    		 if(num_col_fr.compareTo(limite_col)==0)
						    			 num_col_fr=chiudi_riga(bw);
						    		 if(ecf.getFr006001()!=null && ecf.getFr006001().compareTo(new BigDecimal(0))!=0){
						    			 if(num_col_fr==0)
							    		 		bw.append("2");
						    			 bw.append("FR006001");
							    		 bw.append(Formatta(ecf.getFr006001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
							    		 SumFR006001=SumFR006001.add(ecf.getFr006001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
							    		 num_col_fr++;
						    		 }
						    		 if(num_col_fr.compareTo(limite_col)==0)
						    			 num_col_fr=chiudi_riga(bw);
						    		 if(ecf.getFr007001()!=null && ecf.getFr007001().compareTo(new BigDecimal(0))!=0){
						    			 if(num_col_fr==0)
							    		 		bw.append("2");
						    			 bw.append("FR007001");
							    		 bw.append(Formatta(ecf.getFr007001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
							    		 SumFR007001=SumFR007001.add(ecf.getFr007001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
							    		 num_col_fr++;
						    		 }
						    		 if(num_col_fr.compareTo(limite_col)==0)
						    			 num_col_fr=chiudi_riga(bw);
						    		 if(ecf.getFr008001()!=null && ecf.getFr008001().compareTo(new BigDecimal(0))!=0){
						    			 if(num_col_fr==0)
							    		 		bw.append("2");
						    			 bw.append("FR008001");
							    		 bw.append(Formatta(ecf.getFr008001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
							    		 SumFR008001=SumFR008001.add(ecf.getFr008001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
							    		 num_col_fr++;
						    		 }
						    		 if(num_col_fr.compareTo(limite_col)==0)
						    			 num_col_fr=chiudi_riga(bw);
						    		 if(ecf.getFr009001()!=null && ecf.getFr009001().compareTo(new BigDecimal(0))!=0){
						    			 if(num_col_fr==0)
							    		 		bw.append("2");
						    			 bw.append("FR009001");
							    		 bw.append(Formatta(ecf.getFr009001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
							    		 SumFR009001=SumFR009001.add(ecf.getFr009001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
							    		 num_col_fr++;
						    		 }	 
						    		 if(num_col_fr.compareTo(limite_col)==0)
						    			 num_col_fr=chiudi_riga(bw);
						    		 if(ecf.getFr009002()!=null && ecf.getFr009002().compareTo(new BigDecimal(0))!=0){
						    			 if(num_col_fr==0)
							    		 		bw.append("2");
						    			 bw.append("FR009002");
							    		 bw.append(Formatta(ecf.getFr009002().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
							    		 SumFR009002=SumFR009002.add(ecf.getFr009002().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
							    		 num_col_fr++;
						    		 }
						    		 if(num_col_fr.compareTo(limite_col)==0)
						    			 num_col_fr=chiudi_riga(bw);
						    		 if(ecf.getFr010001()!=null && ecf.getFr010001().compareTo(new BigDecimal(0))!=0){
						    			 if(num_col_fr==0)
							    		 		bw.append("2");
						    			 bw.append("FR010001");
							    		 bw.append(Formatta(ecf.getFr010001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
							    		 SumFR010001=SumFR010001.add(ecf.getFr010001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
							    		 num_col_fr++;
						    		 }
						    		 if(num_col_fr.compareTo(limite_col)==0)
						    			 num_col_fr=chiudi_riga(bw);
						    		 if(ecf.getFr011001()!=null && ecf.getFr011001().compareTo(new BigDecimal(0))!=0){
						    			 if(num_col_fr==0)
							    		 		bw.append("2");
						    			 bw.append("FR011001");
							    		 bw.append(Formatta(ecf.getFr011001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
							    		 SumFR011001=SumFR011001.add(ecf.getFr011001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
							    		 num_col_fr++;
						    		 }
						    		 if(num_col_fr.compareTo(limite_col)==0)
						    			 num_col_fr=chiudi_riga(bw);
						    		 if(ecf.getFr012001()!=null && ecf.getFr012001().compareTo(new BigDecimal(0))!=0){
						    			 if(num_col_fr==0)
							    		 		bw.append("2");
						    			 bw.append("FR012001");
							    		 bw.append(Formatta(ecf.getFr012001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
							    		 SumFR012001=SumFR012001.add(ecf.getFr012001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
							    		 num_col_fr++;
						    		 }
						    		 if(num_col_fr.compareTo(limite_col)==0)
						    			 num_col_fr=chiudi_riga(bw);
						    		 if(ecf.getFr013001()!=null && ecf.getFr013001().compareTo(new BigDecimal(0))!=0){
						    			 if(num_col_fr==0)
							    		 		bw.append("2");
						    			 bw.append("FR013001");
							    		 bw.append(Formatta(ecf.getFr013001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",16," "));
							    		 SumFR013001=SumFR013001.add(ecf.getFr013001().setScale(0, java.math.BigDecimal.ROUND_HALF_UP));
							    		 num_col_fr++;
						    		 }
						    	 }
			        }
				     // Fine stringa sia Fornitore che Cliente 
				    if(Num_cliente!=0 && Num_forn==0)
				    	bw.append(Formatta(null,"D",(limite_col-num_col_cl--)*24," "));
				    else 
				    	bw.append(Formatta(null,"D",(limite_col-num_col_fr--)*24," "));
				    
			    	  bw.append(Formatta(null,"D",116," "));
		    		  bw.append("A");
			    	  //bw.newLine(); da problemi per il formato del file
			    	  bw.append("\r\n");
				    //riepilogo  parte iniziale Fissa
			    	  bw.append(new String("3"));
			    	  bw.append(Formatta(Num_cliente.toString(),"D",8,"0"));
			    	  bw.append(Formatta(Num_forn.toString(),"D",8,"0"));
			    	  /*
			    	  bw.append(Formatta(SumCL004001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumCL004002.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumCL005001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumCL006001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumCL007001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumCL008001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumCL008002.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumCL009001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumCL010001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumCL011001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumFR004001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumFR004002.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumFR005001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumFR006001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumFR007001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumFR008001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumFR009001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumFR009002.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumFR010001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumFR011001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumFR012001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  bw.append(Formatta(SumFR013001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20,"0"));
			    	  */
			    	  bw.append(Formatta(SumCL004001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumCL004002.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumCL005001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumCL006001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumCL007001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumCL008001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumCL008002.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumCL009001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumCL010001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumCL011001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumFR004001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumFR004002.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumFR005001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumFR006001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumFR007001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumFR008001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumFR009001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumFR009002.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumFR010001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumFR011001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumFR012001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  bw.append(Formatta(SumFR013001.setScale(0, java.math.BigDecimal.ROUND_HALF_UP).toString(),"D",20," "));
			    	  
			    	  // Fine 
			    	  bw.append(Formatta(null,"S",1340," "));
			    	  bw.append("A");
			    	  //bw.newLine(); da problemi per il formato del file
			    	  bw.append("\r\n");
			          // Fine riepilogo 
			          
				    //Coda parte Fissa
			    	  bw.append(new String("9ECF0038"));
			    	  if(CodFisNum!=null)
			    		  	Codice_Fiscale=Formatta(Codice_Fiscale,"S",16," ");
			    	  
			    	  bw.append(Codice_Fiscale);
 		    		  bw.append(Formatta(P_iva,"S",11," "));
			    	  bw.append(Formatta(ente.getCognome(),"S",26," "));
			    	  bw.append(Formatta(ente.getNome(),"S",25," "));
			    	  bw.append(Formatta(ente.getTi_sesso(),"S",1," "));
			    	  bw.append(ente.getDt_nascita()==null?Formatta(null,"S",8,"0"):Formatta((new SimpleDateFormat("ddmmyyyy")).format(ente.getDt_nascita()),"S",8,"0"));
			    	  bw.append(ente.getComune_nascita()==null?Formatta(null,"S",40," "):Formatta(ente.getComune_nascita().getDs_comune(),"S",40," "));
			    	  bw.append(ente.getComune_nascita()==null?Formatta(null,"S",2," "):Formatta(ente.getComune_nascita().getCd_provincia(),"S",2," "));
			    	  bw.append(Formatta(ente.getRagione_sociale(),"S",70," "));
			    	  bw.append(ente.getComune_fiscale()==null?Formatta(null,"S",40," "):Formatta(ente.getComune_fiscale().getDs_comune(),"S",40," "));
			    	  bw.append(ente.getComune_fiscale()==null?Formatta(null,"S",2," "):Formatta(ente.getComune_fiscale().getCd_provincia(),"S",2," "));
			    	  bw.append(Formatta(null,"S",16," ")); //Codice Fiscale obbligato se diverso da contribuente
			    	  bw.append(Formatta(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).toString(),"S",4," ")); // anno riferimento
			    	  bw.append(Formatta(null,"S",4,"0")); //Prog invio
			    	  bw.append(Formatta(null,"S",4,"0")); //Prog totale invio
			    	  // intermediario
			    	  bw.append(Formatta(null,"S",16," ")); //Codice Fiscale intermediario
			    	  bw.append(Formatta(null,"S",5,"0")); //n° iscrizione CAF
			    	  bw.append(Formatta(null,"S",1,"0")); //"1" se comunicazione predisposta dal contribuente "2" se perdisposta da chi effettua l'invio "0" altrimenti
			    	  bw.append(Formatta(null,"S",8,"0"));// Data dell'impegno
			    	  // Fine intermediario
			    	  bw.append(Formatta(null,"S",1490," "));
			    	  bw.append("A");
			    	  //bw.newLine(); da problemi per il formato del file
			    	  bw.append("\r\n");
			          // Fine Coda 		      	      
		      bw.flush();
		      bw.close();
		      osw.close();
		      os.close();	      
		      ///
		      setFile("/tmp/"+f.getName());	  
		     }else{
		    	  bw.flush();
			      bw.close();
			      osw.close();
			      os.close();	      
		    	 throw new ApplicationException("Non ci sono dati!");    	 
		     }
		      ///
		} catch (FileNotFoundException e) {
			  throw new ApplicationException("File non trovato!");
		}
		catch (IllegalArgumentException e) {
			throw new ApplicationException("Formato file non valido!");
		}
		catch (IOException e) {
			throw new ApplicationException("Errore nella scrittura del file!");		
		}		 
	} 
	
	/**
	 * @param s Stringa in Input
	 * @param allineamento del testo "D" Destra - "S" Sinistra 
	 * @param dimensione richiesta del campo
	 * @param riempimento carattere di riempimento per raggiungere la dimensione richiesta
	 * @return La stringa formattata e riempita con l'allinemento richiesto
	 */
	public String Formatta(String s, String allineamento,Integer dimensione,String riempimento){
		if (s==null)
			s=riempimento;
		if (s.length()< dimensione){
			if (allineamento.compareTo("D")==0){
				while (s.length()<dimensione)
				 s=riempimento+s;
			   return s.toUpperCase();
			}
			else
			{
				while (s.length()<dimensione)
					 s=s+riempimento;
				return s.toUpperCase();
			}
		}else if (s.length()> dimensione){
			s=s.substring(0,dimensione);
			return s.toUpperCase();
		}
		return s.toUpperCase();
	}
	/**
	 * @param bw BufferedWriter a cui aggiungere la parte di chiusura della riga di Clienti e/o Fornitori
	 * @return 0 il numero di colonne utilizzate per la nuova riga, il massimo è 70
	 * @throws ApplicationException
	 */
	public Integer chiudi_riga(BufferedWriter bw) throws ApplicationException{
		try{
		 bw.append(Formatta(null,"D",116," "));
		 bw.append("A");
   	     //bw.newLine(); da problemi per il formato del file
   	     bw.append("\r\n");
         return 0;
		} catch (IOException e) {
			throw new ApplicationException("Errore nella scrittura del file!");		
		}    
	}
	private boolean abilitatoECF;
	
	public boolean isAbilitatoECF() {
		return abilitatoECF;
	}
	public void setAbilitatoECF(boolean abilitatoECF) {
		this.abilitatoECF = abilitatoECF;
	}
	
    private boolean abilitatoSospensioneCori;
	
	public boolean isAbilitatoSospensioneCori() {
		return abilitatoSospensioneCori;
	}
	public void setAbilitatoSospensioneCori(boolean abilitatoSospensioneCori) {
		this.abilitatoSospensioneCori = abilitatoSospensioneCori;
	}	

    private boolean abilitatoAutorizzareDiaria;
	
	public boolean isAbilitatoAutorizzareDiaria() {
		return abilitatoAutorizzareDiaria;
	}
	public void setAbilitatoAutorizzareDiaria(boolean abilitatoAutorizzareDiaria) {
		this.abilitatoAutorizzareDiaria = abilitatoAutorizzareDiaria;
	}
	private boolean supervisore=false;
	
public boolean isSupervisore() {
		return supervisore;
	}
	public void setSupervisore(boolean supervisore) {
		this.supervisore = supervisore;
	}
protected void initialize(ActionContext context) throws BusinessProcessException {
	try {
		setAbilitatoECF(UtenteBulk.isAbilitatoECF(context.getUserContext()));
		setAbilitatoSospensioneCori(UtenteBulk.isAbilitatoSospensioneCori(context.getUserContext()));
		setAbilitatoAutorizzareDiaria(UtenteBulk.isAbilitatoAutorizzareDiaria(context.getUserContext()));
		setSupervisore(Utility.createUtenteComponentSession().isSupervisore(context.getUserContext()));
	} catch (ComponentException e1) {
		throw handleException(e1);
	} catch (RemoteException e1) {
		throw handleException(e1);
	}
	super.initialize(context);
}

public OggettoBulk initializeModelForInsert(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
	AnagraficoBulk anagrafico = (AnagraficoBulk)super.initializeModelForInsert(context,bulk);
	try {
		setSupervisore(Utility.createUtenteComponentSession().isSupervisore(context.getUserContext()));
		anagrafico.setNotGestoreIstat(!UtenteBulk.isGestoreIstatSiope(context.getUserContext()));
		anagrafico.setAbilitatoTrattamenti(!UtenteBulk.isAbilitatoAllTrattamenti(context.getUserContext()));
		if (isUoEnte(context))
			anagrafico.setUo_ente(true);
		else
			anagrafico.setUo_ente(false);
		
	} catch (ComponentException e1) {
		handleException(e1);
	} catch (IntrospectionException e1) {
		handleException(e1);
	} catch (RemoteException e1) {
		handleException(e1);
	}
	return anagrafico;
}
@Override
public OggettoBulk initializeModelForEdit(ActionContext actioncontext,OggettoBulk oggettobulk) throws BusinessProcessException {
	oggettobulk = super.initializeModelForEdit(actioncontext,oggettobulk);
	if (oggettobulk instanceof AnagraficoBulk) {
		AnagraficoBulk anagrafico = (AnagraficoBulk)oggettobulk;
		try {
			setSupervisore(Utility.createUtenteComponentSession().isSupervisore(actioncontext.getUserContext()));
			anagrafico.setNotGestoreIstat(!UtenteBulk.isGestoreIstatSiope(actioncontext.getUserContext()));
			anagrafico.setAbilitatoTrattamenti(UtenteBulk.isAbilitatoAllTrattamenti(actioncontext.getUserContext()));
			if (isUoEnte(actioncontext))
				anagrafico.setUo_ente(true);
			else
				anagrafico.setUo_ente(false);
		} catch (ComponentException e1) {
			handleException(e1);
		} catch (IntrospectionException e1) {
			handleException(e1);
		} catch (RemoteException e1) {
			handleException(e1);
		}		
	}
	return oggettobulk;
}

public void writeToolbar(PageContext pagecontext) throws IOException, ServletException {
	Button[] toolbar = getToolbar();
	if(getFile()!=null){
		HttpServletResponse httpservletresp = (HttpServletResponse)pagecontext.getResponse();
		HttpServletRequest httpservletrequest = (HttpServletRequest)pagecontext.getRequest();
	    StringBuffer stringbuffer = new StringBuffer();
	    stringbuffer.append(JSPUtils.getAppRoot(httpservletrequest));
	    toolbar[11].setHref("javascript:doPrint('"+stringbuffer+getFile()+ "')");
	}
	super.writeToolbar(pagecontext);
}
public String getFile() {
	return file;
}
public void setFile(String file) {
	this.file = file;
}
public Timestamp findMaxDataCompValida(UserContext context,AnagraficoBulk anagrafico) throws BusinessProcessException, PersistencyException, IntrospectionException 
{
	try {
		AnagraficoComponentSession sess = (AnagraficoComponentSession)createComponentSession();
		return sess.findMaxDataCompValida(context, anagrafico);
	} catch (RemoteException e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
		//throw handleException(e);
	} catch (ComponentException e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
		//throw handleException(e);		
	}
}
public String[][] getTabs() {
	TreeMap<Integer, String[]> hash = new TreeMap<>();
	int i=0;

	hash.put(i++, new String[]{"tabAnagrafica","Anagrafica","/anagraf00/tab_anagrafica.jsp"});
	if (!this.isSearching()) {
		hash.put(i++, new String[]{"tabRapporto", "Rapporto", "/anagraf00/tab_rapporto.jsp"});
		if (((AnagraficoBulk) getModel()).isPersonaFisica()) {
			hash.put(i++, new String[]{"tabDetrazioniFamiliari", "Carichi familiari", "/anagraf00/tab_detrazioni_familiari.jsp"});
			hash.put(i++, new String[]{"tabDettagli", "Dettagli", "/anagraf00/tab_dettagli.jsp"});
			hash.put(i++, new String[]{"tabPagamentiEsterni", "Pagamenti esterni", "/anagraf00/tab_pagamenti_esterni.jsp"});
		}
		if (NazioneBulk.ITALIA.equals(((AnagraficoBulk) getModel()).getTi_italiano_estero()) &&
				((AnagraficoBulk) getModel()).getPartita_iva() != null &&
				(((AnagraficoBulk) getModel()).isPersonaFisica() || (((AnagraficoBulk) getModel()).isPersonaGiuridica() && !((AnagraficoBulk) getModel()).isStudioAssociato()))) {
			hash.put(i++, new String[]{"tabEsportatore", "Esportatore abituale", "/anagraf00/tab_esportatore.jsp"});
		}
		if (((AnagraficoBulk) getModel()).isStudioAssociato())
			hash.put(i++, new String[]{"tabAssociatiStudio", "Lista Associati", "/anagraf00/tab_associati_studio.jsp"});

		hash.put(i++, new String[]{"tabAssGruppoIva", "Associazione Gruppo Iva", "/anagraf00/tab_gruppo_iva.jsp"});
		if (this.isAttivaEconomica())
			hash.put(i++, new String[]{"tabAssVoceEp", "Associazione Conti Economici", "/anagraf00/tab_voce_ep.jsp"});
	}
	String[][] tabs = new String[i][3];
	for (int j = 0; j < i; j++)
		tabs[j]=new String[]{hash.get(j)[0],hash.get(j)[1],hash.get(j)[2]};
	return tabs;
}
public SimpleDetailCRUDController getCrudAssociatiStudio() {
	return crudAssociatiStudio;
}
protected void validaDichiarazione(ActionContext context,Dichiarazione_intentoBulk dic) throws ValidationException {
	for (java.util.Iterator i = dic.getAnagrafico().getDichiarazioni_intento().iterator();i.hasNext();) {
		Dichiarazione_intentoBulk dic_int = (Dichiarazione_intentoBulk)i.next();
		if (!dic.equals(dic_int) &&
			!((dic.getDt_ini_validita().before(dic_int.getDt_ini_validita()) &&
					dic.getDt_ini_validita().before(dic_int.getDt_fin_validita()) &&
					dic.getDt_fin_validita().before(dic_int.getDt_ini_validita()) &&
					dic.getDt_fin_validita().before(dic_int.getDt_fin_validita())) ||
					(dic.getDt_ini_validita().after(dic_int.getDt_ini_validita()) &&
		    		dic.getDt_ini_validita().after(dic_int.getDt_fin_validita()) &&
		    		dic.getDt_fin_validita().after(dic_int.getDt_ini_validita()) &&
		    		dic.getDt_fin_validita().after(dic_int.getDt_fin_validita())))){
 			throw new ValidationException ("Attenzione: non è possibile indicare una dichiarazione in questo periodo, esiste già una dichiarazione valida nello stesso periodo!");
 			
}
		if (!dic.equals(dic_int) &&
				(dic.getDt_inizio_val_dich()!=null && dic.getDt_fine_val_dich()!=null &&
				 dic_int.getDt_inizio_val_dich()!=null && dic_int.getDt_fine_val_dich()!=null) &&
				!((dic.getDt_inizio_val_dich().before(dic_int.getDt_inizio_val_dich()) &&
						dic.getDt_inizio_val_dich().before(dic_int.getDt_fine_val_dich()) &&
						dic.getDt_fine_val_dich().before(dic_int.getDt_inizio_val_dich()) &&
						dic.getDt_fine_val_dich().before(dic_int.getDt_fine_val_dich())) ||
						(dic.getDt_inizio_val_dich().after(dic_int.getDt_inizio_val_dich()) &&
			    		dic.getDt_inizio_val_dich().after(dic_int.getDt_fine_val_dich()) &&
			    		dic.getDt_fine_val_dich().after(dic_int.getDt_inizio_val_dich()) &&
			    		dic.getDt_fine_val_dich().after(dic_int.getDt_fine_val_dich())))){
	 			throw new ValidationException ("Attenzione: non è possibile indicare una dichiarazione in questo periodo di riferimento, esiste già una dichiarazione valida nello stesso periodo!");
		}
	}
}
	public boolean isUoEnte(ActionContext context){
		Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
		if (uo.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
			return true;
		return false;
	}
	public void aggiornaDatiAce(ActionContext context, AnagraficoBulk anagraficoBulk) throws BusinessProcessException {
		try {
			((AnagraficoComponentSession)createComponentSession()).aggiornaDatiAce(context.getUserContext(), anagraficoBulk);
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
}
