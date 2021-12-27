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

package it.cnr.contab.incarichi00.bp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import javax.servlet.ServletException;

import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.util.action.Selection;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.contratto.bulk.Procedure_amministrativeBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.comp.DateServices;
import it.cnr.contab.incarichi00.bulk.Ass_incarico_uoBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_annoBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_noteBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_rappBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_rapp_detBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_varBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_richiestaBulk;
import it.cnr.contab.incarichi00.bulk.Repertorio_limitiBulk;
import it.cnr.contab.incarichi00.ejb.IncarichiProceduraComponentSession;
import it.cnr.contab.incarichi00.service.ContrattiService;
import it.cnr.contab.incarichi00.tabrif.bulk.Incarichi_parametriBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_attivitaBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_incaricoBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

public class CRUDIncarichiProceduraBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private transient static final Logger logger = LoggerFactory.getLogger(CRUDIncarichiProceduraBP.class);
	private final SimpleDetailCRUDController ripartizionePerAnno = new SimpleDetailCRUDController("ProceduraAnno",Incarichi_procedura_annoBulk.class,"incarichi_procedura_annoColl",this){
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			Incarichi_procedura_annoBulk proceduraAnno = (Incarichi_procedura_annoBulk)oggettobulk;
			if (proceduraAnno.getEsercizio_limite()==null)
				throw new ValidationException("Valorizzare l'esercizio di imputazione finanziaria.");
			if (proceduraAnno.getIncarichi_procedura().getFaseProcesso().equals(Incarichi_proceduraBulk.FASE_INSERIMENTO_INCARICO))
				proceduraAnno.setImporto_complessivo(proceduraAnno.getImporto_iniziale());
			super.validate(actioncontext,oggettobulk);
		}
		public void validateForDelete(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			Incarichi_procedura_annoBulk proceduraAnno = (Incarichi_procedura_annoBulk)oggettobulk;
			if (proceduraAnno !=  null)
				if (proceduraAnno.getImporto_utilizzato() != null &&
						proceduraAnno.getImporto_utilizzato().compareTo(Utility.ZERO)!=0)
					throw new ValidationException("Eliminazione non possibile!\nL'importo relativo all'anno selezionato risulta già utilizzato.");
			if (proceduraAnno.getIncarichi_procedura() != null &&
					(proceduraAnno.getIncarichi_procedura().getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_INCARICO)==1))
				if (proceduraAnno.getImporto_iniziale()!=null && proceduraAnno.getImporto_iniziale().compareTo(Utility.ZERO)!=0)
					throw new ValidationException("Eliminazione non possibile!\nLa procedura di conferimento incarico è già stata pubblicata.\nE' possibile solo modificare l'importo complessivo.");
			super.validateForDelete(actioncontext, oggettobulk);
		}
	};

	private SimpleDetailCRUDController crudArchivioAllegati = new Incarichi_archivioCRUDController( "ProceduraArchivioAllegati", Incarichi_procedura_archivioBulk.class, "archivioAllegati", this){
		@Override
	    public void writeHTMLToolbar(
				javax.servlet.jsp.PageContext context,
				boolean reset,
				boolean find,
				boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {

			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)getParentModel();
			boolean isFromBootstrap = HttpActionContext.isFromBootstrap(context);
			super.openButtonGROUPToolbar(context);
			if (isGrowable() &&
					procedura != null &&
					!procedura.isProceduraAnnullata() && !procedura.isProceduraChiusa() && !procedura.isProceduraDefinitiva()) {
				if (procedura.getBando() == null && procedura.isProceduraDaPubblicare()) {
					it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
							context,
							isFromBootstrap?"fa fa-fw fa-paper-plane-o text-success":"img/cnr16.gif",
							"javascript:submitForm('doAddBandoToCRUD(" + getInputPrefix() + ")')",
							true,
							"Avviso da pubblicare",
							"btn btn-sm btn-secondary btn-outline-secondary",
							isFromBootstrap);
				}
				if (procedura.getDecisioneAContrattare() == null) {
					it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
							context,
							isFromBootstrap?"fa fa-fw fa-file-text-o text-info":"img/book_leaf.gif",
							"javascript:submitForm('doAddDecisioneAContrattareToCRUD(" + getInputPrefix() + ")')",
							true,
							"Decisione a Contrattare",
							"btn btn-sm btn-secondary btn-outline-secondary",
							isFromBootstrap);
				}
				if (procedura.getProgetto()==null) {
					try{
						Incarichi_parametriBulk parametri = ((CRUDIncarichiProceduraBP)getParentController()).getIncarichiParametri(HttpActionContext.getUserContext(context.getSession()),procedura);
						if (parametri!=null && ((parametri.getAllega_progetto()!=null && parametri.getAllega_progetto().equals("Y"))||
								(parametri.getIndica_url_progetto()!=null && parametri.getIndica_url_progetto().equals("Y")))) {
							it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
									context,
									isFromBootstrap?"fa fa-fw fa-lightbulb-o text-primary":"img/tipoftheday16.gif",
									"javascript:submitForm('doAddProgettoToCRUD(" + getInputPrefix() + ")')",
									true,
									"Progetto",
									"btn btn-sm btn-secondary btn-outline-secondary",
									isFromBootstrap);
						}
					} catch (Exception e) {}
				}
				it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
						context,
						isFromBootstrap?"fa fa-fw fa-file-o text-primary":"img/new16.gif",
						"javascript:submitForm('doAddAllegatoGenericoToCRUD(" + getInputPrefix() + ")')",
						true,
						"Allegato Generico",
						"btn btn-sm btn-secondary btn-outline-secondary",
						isFromBootstrap);
				super.writeHTMLToolbar(context, false, false, delete, false, false);
			} else if (procedura != null && isGrowable() &&
					(procedura.isProceduraDefinitiva() || procedura.isProceduraInviataCorteConti() ||
							procedura.isProceduraChiusa() || procedura.isProceduraAnnullata())) {
					it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
							context,
							isFromBootstrap?"fa fa-fw fa-file-o text-primary":"img/new16.gif",
							"javascript:submitForm('doAddAllegatoGenericoToCRUD(" + getInputPrefix() + ")')",
							true,
							"Allegato Generico",
							"btn btn-sm btn-secondary btn-outline-secondary",
							isFromBootstrap);
					super.writeHTMLToolbar(context, false, false, (getModel()!=null && ((Incarichi_archivioBulk)getModel()).isAllegatoGenerico() && getModel().isToBeCreated()), false, false);
			}
            super.closeButtonGROUPToolbar(context);
		}
	};

	private SimpleDetailCRUDController crudArchivioAllegatiMI = new Incarichi_archivioCRUDController( "ProceduraArchivioAllegatiMI", Incarichi_archivioBulk.class, "archivioAllegatiMI", this){
		public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
			SimpleDetailCRUDController controller;
			if (oggettobulk instanceof Incarichi_procedura_archivioBulk)
				controller = ((CRUDIncarichiProceduraBP)getParentController()).getCrudArchivioAllegati();
			else
				controller = ((CRUDIncarichiProceduraBP)getParentController()).getCrudIncarichiArchivioAllegati();

			controller.removeDetail(oggettobulk, controller.getDetails().indexOf(oggettobulk));
			return super.removeDetail(oggettobulk, i);
		}
		public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
			SimpleDetailCRUDController controller;
			if (oggettobulk instanceof Incarichi_procedura_archivioBulk)
				controller = ((CRUDIncarichiProceduraBP)getParentController()).getCrudArchivioAllegati();
			else
				controller = ((CRUDIncarichiProceduraBP)getParentController()).getCrudIncarichiArchivioAllegati();

			controller.addDetail(oggettobulk);
			return super.addDetail(oggettobulk);
		}
		public void addBando(ActionContext actioncontext) throws BusinessProcessException {
			Incarichi_procedura_archivioBulk model = new Incarichi_procedura_archivioBulk();
			model.initializeForInsert(null, actioncontext);
			model.setTipo_archivio(Incarichi_procedura_archivioBulk.TIPO_BANDO);
			add(actioncontext, model);
		}
		public void addDecisioneAContrattare(ActionContext actioncontext) throws BusinessProcessException {
			Incarichi_procedura_archivioBulk model = new Incarichi_procedura_archivioBulk();
			model.initializeForInsert(null, actioncontext);
			model.setTipo_archivio(Incarichi_procedura_archivioBulk.TIPO_DECISIONE_A_CONTRATTARE);
			add(actioncontext, model);
		}
		public void addDecretoDiNomina(ActionContext actioncontext) throws BusinessProcessException {
			Incarichi_repertorio_archivioBulk model = new Incarichi_repertorio_archivioBulk();
			model.initializeForInsert(null, actioncontext);
			model.setTipo_archivio(Incarichi_repertorio_archivioBulk.TIPO_DECRETO_DI_NOMINA);
			add(actioncontext, model);
		}
		public void addAttoEsitoControllo(ActionContext actioncontext) throws BusinessProcessException {
			Incarichi_repertorio_archivioBulk model = new Incarichi_repertorio_archivioBulk();
			model.initializeForInsert(null, actioncontext);
			model.setTipo_archivio(Incarichi_repertorio_archivioBulk.TIPO_ATTO_ESITO_CONTROLLO);
			add(actioncontext, model);
		}
		public void addContratto(ActionContext actioncontext) throws BusinessProcessException {
			Incarichi_repertorio_archivioBulk model = new Incarichi_repertorio_archivioBulk();
			model.initializeForInsert(null, actioncontext);
			model.setTipo_archivio(Incarichi_repertorio_archivioBulk.TIPO_CONTRATTO);
			add(actioncontext, model);
		}
		public void addCurriculumVincitore(ActionContext actioncontext) throws BusinessProcessException {
			Incarichi_repertorio_archivioBulk model = new Incarichi_repertorio_archivioBulk();
			model.initializeForInsert(null, actioncontext);
			model.setTipo_archivio(Incarichi_repertorio_archivioBulk.TIPO_CURRICULUM_VINCITORE);
			add(actioncontext, model);
		}
		public void addAggiornamentoCurriculumVincitore(ActionContext actioncontext) throws BusinessProcessException {
			Incarichi_repertorio_archivioBulk model = new Incarichi_repertorio_archivioBulk();
			model.initializeForInsert(null, actioncontext);
			model.setTipo_archivio(Incarichi_repertorio_archivioBulk.TIPO_AGGIORNAMENTO_CURRICULUM_VINCITORE);
			add(actioncontext, model);
		}
		public void addProgetto(ActionContext actioncontext) throws BusinessProcessException {
			Incarichi_procedura_archivioBulk model = new Incarichi_procedura_archivioBulk();
			model.initializeForInsert(null, actioncontext);
			model.setTipo_archivio(Incarichi_procedura_archivioBulk.TIPO_PROGETTO);

			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)getParentModel();
			Incarichi_parametriBulk parametri = null;
			try{
				parametri = ((CRUDIncarichiProceduraBP)getParentController()).getIncarichiParametri(actioncontext.getUserContext(),procedura);
			} catch (Exception e) {}

			if (!(parametri!=null && parametri.getAllega_progetto()!=null && parametri.getAllega_progetto().equals("Y")))
				model.setFileRequired(Boolean.FALSE);
			if (parametri!=null && parametri.getIndica_url_progetto()!=null && parametri.getIndica_url_progetto().equals("Y"))
				model.setUrlRequired(Boolean.TRUE);
			add(actioncontext, model);
		}
		public void addConflittoInteresse(ActionContext actioncontext) throws BusinessProcessException {
			Incarichi_repertorio_archivioBulk model = new Incarichi_repertorio_archivioBulk();
			model.initializeForInsert(null, actioncontext);
			model.setTipo_archivio(Incarichi_repertorio_archivioBulk.TIPO_CONFLITTO_INTERESSI);
			add(actioncontext, model);
		}
		public void addAttestazioneDirettore(ActionContext actioncontext) throws BusinessProcessException {
			Incarichi_repertorio_archivioBulk model = new Incarichi_repertorio_archivioBulk();
			model.initializeForInsert(null, actioncontext);
			model.setTipo_archivio(Incarichi_repertorio_archivioBulk.TIPO_ATTESTAZIONE_DIRETTORE);
			add(actioncontext, model);
		}
		public void addAllegatoGenerico(ActionContext actioncontext) throws BusinessProcessException {
			Incarichi_procedura_archivioBulk model = new Incarichi_procedura_archivioBulk();
			model.initializeForInsert(null, actioncontext);
			model.setTipo_archivio(Incarichi_procedura_archivioBulk.TIPO_GENERICO);
			add(actioncontext, model);
		}
		@Override
		public void writeHTMLToolbar(
				javax.servlet.jsp.PageContext context,
				boolean reset,
				boolean find,
				boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {

			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)getParentModel();
			Incarichi_parametriBulk parametri = null;
			try{
				parametri = ((CRUDIncarichiProceduraBP)getParentController()).getIncarichiParametri(HttpActionContext.getUserContext(context.getSession()),procedura);
			} catch (Exception e) {}

			boolean isFromBootstrap = HttpActionContext.isFromBootstrap(context);
			boolean innerDelete = delete;
			
            super.openButtonGROUPToolbar(context);
			if (procedura != null && !procedura.isProceduraAnnullata() && !procedura.isProceduraChiusa() &&
					!procedura.isProceduraDefinitiva() && isGrowable()) {
				if (procedura.getBando() == null && procedura.isProceduraDaPubblicare()) {
					it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
							context,
							isFromBootstrap?"fa fa-fw fa-paper-plane-o text-success":"img/cnr16.gif",
							"javascript:submitForm('doAddBandoToCRUD(" + getInputPrefix() + ")')",
							true,
							"Avviso da pubblicare",
							"btn btn-sm btn-secondary btn-outline-secondary",
							isFromBootstrap);
				}
				if (procedura.getDecisioneAContrattare() == null) {
					it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
							context,
							isFromBootstrap?"fa fa-fw fa-file-text-o text-info":"img/book_leaf.gif",
							"javascript:submitForm('doAddDecisioneAContrattareToCRUD(" + getInputPrefix() + ")')",
							true,
							"Decisione a Contrattare",
							"btn btn-sm btn-secondary btn-outline-secondary",
							isFromBootstrap);
				}
				if (procedura.getProgetto()==null) {
					if (parametri!=null && ((parametri.getAllega_progetto()!=null && parametri.getAllega_progetto().equals("Y"))||
							(parametri.getIndica_url_progetto()!=null && parametri.getIndica_url_progetto().equals("Y")))) {
						it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
								context,
								isFromBootstrap?"fa fa-fw fa-lightbulb-o text-primary":"img/tipoftheday16.gif",
								"javascript:submitForm('doAddProgettoToCRUD(" + getInputPrefix() + ")')",
								true,
								"Progetto",
								"btn btn-sm btn-secondary btn-outline-secondary",
								isFromBootstrap);
					}
				}
				if (procedura.getNr_contratti().compareTo(1)==0 &&
						procedura.getIncarichi_repertorioValidiColl() != null &&
						procedura.getIncarichi_repertorioValidiColl().size() == 1 ) {

					Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)procedura.getIncarichi_repertorioValidiColl().get(0);

					if (incarico.getContratto()==null) {
						it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
								context,
								isFromBootstrap?"fa fa-fw fa-handshake-o text-primary":"img/history16.gif",
								"javascript:submitForm('doAddContrattoToCRUD(" + getInputPrefix() + ")')",
								true,
								"Contratto",
								"btn btn-sm btn-secondary btn-outline-secondary",
								isFromBootstrap);
					}
					if (parametri!=null) {
						if (parametri.getAllega_curriculum_vitae()!=null && parametri.getAllega_curriculum_vitae().equals("Y")) {
							if (incarico.getCurriculumVincitore()==null) {
								it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
										context,
										isFromBootstrap?"fa fa-fw fa-address-card-o text-primary":"img/paste16.gif",
										"javascript:submitForm('doAddCurriculumVincitoreToCRUD(" + getInputPrefix() + ")')",
										true,
										"Curriculum Vincitore",
										"btn btn-sm btn-secondary btn-outline-secondary",
										isFromBootstrap);
							} else {
								it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
										context,
										isFromBootstrap?"fa fa-fw fa-address-card text-primary":"img/paste16.gif",
										"javascript:submitForm('doAddAggiornamentoCurriculumVincitoreToCRUD(" + getInputPrefix() + ")')",
										true,
										"Aggiornamento Curriculum Vincitore",
										"btn btn-sm btn-secondary btn-outline-secondary",
										isFromBootstrap);
							}
						}
						if (parametri.getAllega_conflitto_interesse()!=null && parametri.getAllega_conflitto_interesse().equals("Y")) {
							if (incarico.getConflittoInteressi()==null) {
								it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
										context,
										isFromBootstrap?"fa fa-fw fa-user-times-o text-primary":"img/edit16.gif",
										"javascript:submitForm('doAddConflittoInteresseToCRUD(" + getInputPrefix() + ")')",
										true,
										"Attestazione Insussistenza Conflitto Interessi",
										"btn btn-sm btn-secondary btn-outline-secondary",
										isFromBootstrap);
							}
						}
						if (parametri.getAllega_attestazione_direttore()!=null && parametri.getAllega_attestazione_direttore().equals("Y")) {
							if (incarico.getAttestazioneDirettore()==null) {
								it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
										context,
										isFromBootstrap?"fa fa-fw fa-certificate text-primary":"img/properties16.gif",
										"javascript:submitForm('doAddAttestazioneDirettoreToCRUD(" + getInputPrefix() + ")')",
										true,
										"Attestazione Direttore",
										"btn btn-sm btn-secondary btn-outline-secondary",
										isFromBootstrap);
							}
						}
					}
					if (incarico.getDecretoDiNomina()==null) {
						if (parametri!=null && parametri.getAllega_decreto_nomina()!=null && parametri.getAllega_decreto_nomina().equals("Y")) {
							it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
									context,
									isFromBootstrap?"fa fa-fw fa-book text-primary":"img/book_closed.gif",
									"javascript:submitForm('doAddDecretoDiNominaToCRUD(" + getInputPrefix() + ")')",
									true,
									"Decreto di Nomina",
									"btn btn-sm btn-secondary btn-outline-secondary",
									isFromBootstrap);
						}
					}
					if (incarico.getFl_inviato_corte_conti() && incarico.getEsito_corte_conti()!=null &&
							incarico.getAttoEsitoControllo()==null) {
						it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
								context,
								isFromBootstrap?"fa fa-fw fa-certificate text-primary":"img/bookmarks16.gif",
								"javascript:submitForm('doAddAttoEsitoControlloToCRUD(" + getInputPrefix() + ")')",
								true,
								"Documentazione ricevuta dalla Corte dei Conti o Provvedimento Amm. Direttore",
								"btn btn-sm btn-secondary btn-outline-secondary",
								isFromBootstrap);
					}
				}
				it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
						context,
						isFromBootstrap?"fa fa-fw fa-file-o text-primary":"img/new16.gif",
						"javascript:submitForm('doAddAllegatoGenericoToCRUD(" + getInputPrefix() + ")')",
						true,
						"Allegato Generico",
						"btn btn-sm btn-secondary btn-outline-secondary",
						isFromBootstrap);

				innerDelete = delete;
			} else if (procedura != null && isGrowable() &&
					(procedura.isProceduraDefinitiva() || procedura.isProceduraInviataCorteConti() ||
							procedura.isProceduraChiusa() || procedura.isProceduraAnnullata())) {

				if (((CRUDIncarichiProceduraBP)getParentController()).isSuperUtente() ||
						((CRUDIncarichiProceduraBP)getParentController()).isUtenteAbilitatoModificaAllegatoContratto()) {
					if (procedura.getNr_contratti().compareTo(1)==0 &&
							procedura.getIncarichi_repertorioValidiColl() != null &&
							procedura.getIncarichi_repertorioValidiColl().size() == 1 ) {

						Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)procedura.getIncarichi_repertorioValidiColl().get(0);

						if (incarico.getContratto()==null) {
							it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
									context,
									isFromBootstrap?"fa fa-fw fa-handshake-o text-primary":"img/history16.gif",
									"javascript:submitForm('doAddContrattoToCRUD(" + getInputPrefix() + ")')",
									true,
									"Contratto",
									"btn btn-sm btn-secondary btn-outline-secondary",
									isFromBootstrap);
						}
						innerDelete = Optional.ofNullable(getModel())
											.filter(Incarichi_archivioBulk.class::isInstance)
											.map(Incarichi_archivioBulk.class::cast)
											.filter(el->el.isContratto())
											.isPresent();
					}
				}
				if (procedura.getNr_contratti().compareTo(1)==0 &&
						procedura.getIncarichi_repertorioValidiColl() != null &&
						procedura.getIncarichi_repertorioValidiColl().size() == 1 ) {
					Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)procedura.getIncarichi_repertorioValidiColl().get(0);
					if (parametri!=null) {
						if (parametri.getAllega_curriculum_vitae()!=null && parametri.getAllega_curriculum_vitae().equals("Y")) {
							if (incarico.getCurriculumVincitore() == null) {
								it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
										context,
										isFromBootstrap ? "fa fa-fw fa-address-card-o text-primary" : "img/paste16.gif",
										"javascript:submitForm('doAddCurriculumVincitoreToCRUD(" + getInputPrefix() + ")')",
										true,
										"Curriculum Vincitore",
										"btn btn-sm btn-secondary btn-outline-secondary",
										isFromBootstrap);
							} else {
								it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
										context,
										isFromBootstrap ? "fa fa-fw fa-address-card text-primary" : "img/paste16.gif",
										"javascript:submitForm('doAddAggiornamentoCurriculumVincitoreToCRUD(" + getInputPrefix() + ")')",
										true,
										"Aggiornamento Curriculum Vincitore",
										"btn btn-sm btn-secondary btn-outline-secondary",
										isFromBootstrap);

								innerDelete = innerDelete || Optional.ofNullable(getModel())
										.filter(Incarichi_archivioBulk.class::isInstance)
										.map(Incarichi_archivioBulk.class::cast)
										.filter(el -> el.isToBeCreated())
										.filter(el -> el.isAggiornamentoCurriculumVincitore())
										.isPresent();

								if (((CRUDIncarichiProceduraBP) getParentController()).isSuperUtente() ||
										((CRUDIncarichiProceduraBP) getParentController()).isUtenteAbilitatoModificaAllegatoCurriculum()) {
									innerDelete = innerDelete || Optional.ofNullable(getModel())
											.filter(Incarichi_archivioBulk.class::isInstance)
											.map(Incarichi_archivioBulk.class::cast)
											.filter(el -> el.isCurriculumVincitore() || el.isAggiornamentoCurriculumVincitore())
											.isPresent();
								}
							}
						}
						if (parametri.getAllega_conflitto_interesse()!=null && parametri.getAllega_conflitto_interesse().equals("Y")) {
							if (incarico.getConflittoInteressi() == null) {
								it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
										context,
										isFromBootstrap ? "fa fa-fw fa-user-times-o text-primary" : "img/edit16.gif",
										"javascript:submitForm('doAddConflittoInteresseToCRUD(" + getInputPrefix() + ")')",
										true,
										"Attestazione Insussistenza Conflitto Interessi",
										"btn btn-sm btn-secondary btn-outline-secondary",
										isFromBootstrap);
							}
						}
						if (parametri.getAllega_attestazione_direttore()!=null && parametri.getAllega_attestazione_direttore().equals("Y")) {
							if (incarico.getAttestazioneDirettore()==null) {
								it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
										context,
										isFromBootstrap?"fa fa-fw fa-certificate text-primary":"img/properties16.gif",
										"javascript:submitForm('doAddAttestazioneDirettoreToCRUD(" + getInputPrefix() + ")')",
										true,
										"Attestazione Direttore",
										"btn btn-sm btn-secondary btn-outline-secondary",
										isFromBootstrap);
							}
						}
					}
					if (incarico.getDecretoDiNomina()==null) {
						if (parametri!=null && parametri.getAllega_decreto_nomina()!=null && parametri.getAllega_decreto_nomina().equals("Y")) {
							it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
									context,
									isFromBootstrap?"fa fa-fw fa-book text-primary":"img/book_closed.gif",
									"javascript:submitForm('doAddDecretoDiNominaToCRUD(" + getInputPrefix() + ")')",
									true,
									"Decreto di Nomina",
									"btn btn-sm btn-secondary btn-outline-secondary",
									isFromBootstrap);
						}
					}
				}
				it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
							context,
							isFromBootstrap? "fa fa-plus text-primary":"img/new16.gif",
							"javascript:submitForm('doAddAllegatoGenericoToCRUD(" + getInputPrefix() + ")')",
							true,
							"Allegato Generico",
							"btn btn-sm btn-secondary btn-outline-secondary",
							isFromBootstrap);

				innerDelete = innerDelete || Optional.ofNullable(getModel())
									.filter(Incarichi_archivioBulk.class::isInstance)
									.map(Incarichi_archivioBulk.class::cast)
									.filter(el->el.isToBeCreated())
									.isPresent();
			}
			super.writeHTMLToolbar(context, false, false, innerDelete, false, false);
            super.closeButtonGROUPToolbar(context);
		}
	};

	private SimpleDetailCRUDController compensiAllegati = new SimpleDetailCRUDController( "CompensiAllegati", CompensoBulk.class, "compensiColl", ripartizionePerAnno);

	//Controller per gestione incarichi allegati
	private SimpleDetailCRUDController incarichiColl = new SimpleDetailCRUDController( "IncarichiColl", Incarichi_repertorioBulk.class, "incarichi_repertorioColl", this){
		public boolean isGrowable() {
			return super.isGrowable() && !((Incarichi_proceduraBulk)getParentModel()).isProceduraScaduta() &&
					((Incarichi_proceduraBulk)getParentModel()).getNr_contratti().compareTo(getDetails().size())==1;
		}
		public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
			if (oggettobulk instanceof Incarichi_repertorioBulk) {
				Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)oggettobulk;
				if ( incarico.getStato().equals(Incarichi_repertorioBulk.STATO_PROVVISORIO) )
				{
					incarico.setToBeDeleted();

					for ( Iterator dett = incarico.getIncarichi_repertorio_annoColl().iterator(); dett.hasNext(); )
						((Incarichi_repertorio_annoBulk) dett.next()).setToBeDeleted();

					for ( Iterator dett = incarico.getArchivioAllegati().iterator(); dett.hasNext(); )
						((Incarichi_repertorio_archivioBulk) dett.next()).setToBeDeleted();

					return super.removeDetail(oggettobulk, i);
				}
				else
				{
					incarico.setStato(Incarichi_repertorioBulk.STATO_PROVVISORIO);
				}
			}
			return oggettobulk;
		}
		@Override
		public void writeHTMLToolbar(
				javax.servlet.jsp.PageContext context,
				boolean reset,
				boolean find,
				boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {

			CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)getParentController();
			Incarichi_proceduraBulk  procedura = (Incarichi_proceduraBulk)bp.getModel();
			Incarichi_repertorioBulk incarico  = (Incarichi_repertorioBulk)getModel();

			super.writeHTMLToolbar(context, reset, false, delete&&incarico!=null&&incarico.isIncaricoProvvisorio(), false);

			boolean isFromBootstrap = HttpActionContext.isFromBootstrap(context);

			if (getModel()!=null) {
				if (isEditing() && !incarico.isROIncarico()) {
					String command = "javascript:submitForm('doSalvaDefinitivoContratto')";
					it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
							context,
							isFromBootstrap? "fa fa-floppy-o text-primary":"img/saveall16.gif",
							!(isInputReadonly() ||
									getDetails().isEmpty() ||
									((CRUDIncarichiProceduraBP)getParentController()).isSearching() ||
									getModel()==null ||
									incarico.isIncaricoAnnullato()||
									incarico.isIncaricoChiuso()||
									incarico.isIncaricoDefinitivo())? command : null,
							true,
							"Salva Definitivo",
							"btn btn-sm btn-secondary btn-outline-secondary",
							isFromBootstrap);
				}

				if (((Incarichi_repertorioBulk)getModel()).isIncaricoDefinitivo() &&
						((CRUDIncarichiProceduraBP)getParentController()).isUoEnte()) {

					String command = "javascript:submitForm('doAnnullaDefinitivoContratto')";
					it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
							context,
							isFromBootstrap? "fa fa-undo text-primary":"img/import16.gif",
							!(isInputReadonly() ||
									getDetails().isEmpty() ||
									((CRUDIncarichiProceduraBP)getParentController()).isSearching() ||
									getModel()==null ||
									!((Incarichi_repertorioBulk)getModel()).isIncaricoDefinitivo())? command : null,
							true,
							"Annulla Definitivo",
							"btn btn-sm btn-secondary btn-outline-secondary",
							isFromBootstrap);

					if (bp.isConcludiMonoIncaricoButtonHidden() && isEditing()) {
						String command2 = "javascript:submitForm('doConcludiIncarico')";
						it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
								context,
								"img/close.gif",
								!(isInputReadonly() ||
										getDetails().isEmpty() ||
										((CRUDIncarichiProceduraBP)getParentController()).isSearching() ||
										getModel()==null )? command2 : null,
								true,
								"Concludi Incarico",
								"btn btn-sm btn-secondary btn-outline-secondary",
								isFromBootstrap);
					}
				}
			}
            super.closeButtonGROUPToolbar(context);
		}
	};

	private final SimpleDetailCRUDController ripartizioneIncarichiPerAnno = new SimpleDetailCRUDController("IncarichiAnno",Incarichi_repertorio_annoBulk.class,"incarichi_repertorio_annoColl",incarichiColl){
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			Incarichi_repertorio_annoBulk incaricoAnno = (Incarichi_repertorio_annoBulk)oggettobulk;
			if (incaricoAnno.getEsercizio_limite()==null)
				throw new ValidationException("Valorizzare l'esercizio di imputazione finanziaria.");
			if (incaricoAnno.getIncarichi_repertorio().getIncarichi_procedura().getFaseProcesso().equals(Incarichi_proceduraBulk.FASE_INSERIMENTO_INCARICO))
				incaricoAnno.setImporto_complessivo(incaricoAnno.getImporto_iniziale());
			super.validate(actioncontext,oggettobulk);
		}
		public void validateForDelete(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			Incarichi_repertorio_annoBulk incaricoAnno = (Incarichi_repertorio_annoBulk)oggettobulk;
			if (incaricoAnno !=  null)
				if (incaricoAnno.getImporto_utilizzato() != null &&
						incaricoAnno.getImporto_utilizzato().compareTo(Utility.ZERO)!=0)
					throw new ValidationException("Eliminazione non possibile!\nL'importo relativo all'anno selezionato risulta già utilizzato.");
			if (incaricoAnno.getIncarichi_repertorio().getIncarichi_procedura() != null &&
					(incaricoAnno.getIncarichi_repertorio().getIncarichi_procedura().getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_INCARICO)==1))
				if (incaricoAnno.getImporto_iniziale()!=null && incaricoAnno.getImporto_iniziale().compareTo(Utility.ZERO)!=0)
					throw new ValidationException("Eliminazione non possibile!\nLa procedura di conferimento incarico è già stata pubblicata.\nE' possibile solo modificare l'importo complessivo.");
			super.validateForDelete(actioncontext, oggettobulk);
		}
	};

	private SimpleDetailCRUDController crudIncarichiArchivioAllegati = new Incarichi_archivioCRUDController( "IncarichiArchivioAllegati", Incarichi_repertorio_archivioBulk.class, "archivioAllegati", incarichiColl){
		@Override
	    public void writeHTMLToolbar(
				javax.servlet.jsp.PageContext context,
				boolean reset,
				boolean find,
				boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {

			Incarichi_repertorioBulk incarico  = (Incarichi_repertorioBulk)getParentModel();
			Incarichi_parametriBulk parametri = null;
			try{
				parametri = ((CRUDIncarichiProceduraBP)getParentController().getParentController()).getIncarichiParametri(HttpActionContext.getUserContext(context.getSession()),incarico.getIncarichi_procedura());
			} catch (Exception e) {}

			boolean isFromBootstrap = HttpActionContext.isFromBootstrap(context);
			boolean innerDelete = delete;
			
            super.openButtonGROUPToolbar(context);
			if (isGrowable() &&
					incarico != null &&
					!incarico.isIncaricoAnnullato() && !incarico.isIncaricoChiuso() && !incarico.isIncaricoDefinitivo()) {
				if (incarico.getContratto()==null) {
					it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
							context,
							isFromBootstrap?"fa fa-fw fa-handshake-o text-primary":"img/history16.gif",
							"javascript:submitForm('doAddContrattoToCRUD(" + getInputPrefix() + ")')",
							true,
							"Contratto",
							isFromBootstrap);
				}
				if (parametri!=null) {
					if (parametri.getAllega_curriculum_vitae() != null && parametri.getAllega_curriculum_vitae().equals("Y")) {
						if (incarico.getCurriculumVincitore() == null) {
							it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
									context,
									isFromBootstrap ? "fa fa-fw fa-address-card-o text-primary" : "img/paste16.gif",
									"javascript:submitForm('doAddCurriculumVincitoreToCRUD(" + getInputPrefix() + ")')",
									true,
									"Curriculum Vincitore",
									isFromBootstrap);
						} else {
							it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
									context,
									isFromBootstrap ? "fa fa-fw fa-address-card text-primary" : "img/paste16.gif",
									"javascript:submitForm('doAddAggiornamentoCurriculumVincitoreToCRUD(" + getInputPrefix() + ")')",
									true,
									"Aggiornamento Curriculum Vincitore",
									isFromBootstrap);
						}
					}
					if (parametri.getAllega_conflitto_interesse()!=null && parametri.getAllega_conflitto_interesse().equals("Y")) {
						if (incarico.getConflittoInteressi() == null) {
							it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
									context,
									isFromBootstrap ? "fa fa-fw fa-user-times-o text-primary" : "img/edit16.gif",
									"javascript:submitForm('doAddConflittoInteresseToCRUD(" + getInputPrefix() + ")')",
									true,
									"Attestazione Insussistenza Conflitto Interessi",
									"btn btn-sm btn-secondary btn-outline-secondary",
									isFromBootstrap);
						}
					}
					if (parametri.getAllega_attestazione_direttore()!=null && parametri.getAllega_attestazione_direttore().equals("Y")) {
						if (incarico.getAttestazioneDirettore()==null) {
							it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
									context,
									isFromBootstrap?"fa fa-fw fa-certificate text-primary":"img/properties16.gif",
									"javascript:submitForm('doAddAttestazioneDirettoreToCRUD(" + getInputPrefix() + ")')",
									true,
									"Attestazione Direttore",
									"btn btn-sm btn-secondary btn-outline-secondary",
									isFromBootstrap);
						}
					}
				}
				if (incarico.getDecretoDiNomina()==null) {
					if (parametri!=null && parametri.getAllega_decreto_nomina()!=null && parametri.getAllega_decreto_nomina().equals("Y")) {
						it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
								context,
								isFromBootstrap?"fa fa-fw fa-book text-primary":"img/book_closed.gif",
								"javascript:submitForm('doAddDecretoDiNominaToCRUD(" + getInputPrefix() + ")')",
								true,
								"Decreto di Nomina",
								"btn btn-sm btn-secondary btn-outline-secondary",
								isFromBootstrap);
					}
				}
				if (incarico.getFl_inviato_corte_conti() && incarico.getEsito_corte_conti()!=null &&
						incarico.getAttoEsitoControllo()==null) {
					it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
							context,
							isFromBootstrap?"fa fa-fw fa-certificate text-primary":"img/bookmarks16.gif",
							"javascript:submitForm('doAddAttoEsitoControlloToCRUD(" + getInputPrefix() + ")')",
							true,
							"Documentazione ricevuta dalla Corte dei Conti o Provvedimento Amm. Direttore",
							"btn btn-sm btn-secondary btn-outline-secondary",
							isFromBootstrap);
				}
				it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
						context,
						isFromBootstrap? "fa fa-plus text-primary":"img/new16.gif",
						"javascript:submitForm('doAddAllegatoGenericoToCRUD(" + getInputPrefix() + ")')",
						true,
						"Allegato Generico",
						"btn btn-sm btn-secondary btn-outline-secondary",
						isFromBootstrap);
				innerDelete = delete;
			} else if (incarico != null && isGrowable() &&
					(incarico.isIncaricoDefinitivo() || incarico.isIncaricoChiuso() || incarico.isIncaricoAnnullato())) {
				if (((CRUDIncarichiProceduraBP)getParentController().getParentController()).isSuperUtente() ||
						((CRUDIncarichiProceduraBP)getParentController().getParentController()).isUtenteAbilitatoModificaAllegatoContratto()) {
					if (incarico.getContratto()==null) {
						it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
								context,
								isFromBootstrap?"fa fa-fw fa-handshake-o text-primary":"img/history16.gif",
								"javascript:submitForm('doAddContrattoToCRUD(" + getInputPrefix() + ")')",
								true,
								"Contratto",
								isFromBootstrap);
					}
					innerDelete = Optional.ofNullable(getModel())
										.filter(Incarichi_archivioBulk.class::isInstance)
										.map(Incarichi_archivioBulk.class::cast)
										.filter(el->el.isContratto())
										.isPresent();
				}
				if (parametri!=null) {
					if (parametri.getAllega_curriculum_vitae() != null && parametri.getAllega_curriculum_vitae().equals("Y")) {
						if (incarico.getCurriculumVincitore() == null) {
							it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
									context,
									isFromBootstrap ? "fa fa-fw fa-address-card-o text-primary" : "img/paste16.gif",
									"javascript:submitForm('doAddCurriculumVincitoreToCRUD(" + getInputPrefix() + ")')",
									true,
									"Curriculum Vincitore",
									"btn btn-sm btn-secondary btn-outline-secondary",
									isFromBootstrap);
						} else {
							it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
									context,
									isFromBootstrap ? "fa fa-fw fa-address-card text-primary" : "img/paste16.gif",
									"javascript:submitForm('doAddAggiornamentoCurriculumVincitoreToCRUD(" + getInputPrefix() + ")')",
									true,
									"Aggiornamento Curriculum Vincitore",
									"btn btn-sm btn-secondary btn-outline-secondary",
									isFromBootstrap);

							innerDelete = innerDelete || Optional.ofNullable(getModel())
									.filter(Incarichi_archivioBulk.class::isInstance)
									.map(Incarichi_archivioBulk.class::cast)
									.filter(el -> el.isToBeCreated())
									.filter(el -> el.isAggiornamentoCurriculumVincitore())
									.isPresent();

							if (((CRUDIncarichiProceduraBP) getParentController().getParentController()).isSuperUtente() ||
									((CRUDIncarichiProceduraBP) getParentController().getParentController()).isUtenteAbilitatoModificaAllegatoCurriculum()) {
								innerDelete = innerDelete || Optional.ofNullable(getModel())
										.filter(Incarichi_archivioBulk.class::isInstance)
										.map(Incarichi_archivioBulk.class::cast)
										.filter(el -> el.isCurriculumVincitore() || el.isAggiornamentoCurriculumVincitore())
										.isPresent();
							}
						}
					}
					if (parametri.getAllega_conflitto_interesse() != null && parametri.getAllega_conflitto_interesse().equals("Y")) {
						if (incarico.getConflittoInteressi() == null) {
							it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
									context,
									isFromBootstrap ? "fa fa-fw fa-user-times-o text-primary" : "img/edit16.gif",
									"javascript:submitForm('doAddConflittoInteresseToCRUD(" + getInputPrefix() + ")')",
									true,
									"Attestazione Insussistenza Conflitto Interessi",
									"btn btn-sm btn-secondary btn-outline-secondary",
									isFromBootstrap);
						}
					}
					if (parametri.getAllega_attestazione_direttore()!=null && parametri.getAllega_attestazione_direttore().equals("Y")) {
						if (incarico.getAttestazioneDirettore()==null) {
							it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
									context,
									isFromBootstrap?"fa fa-fw fa-certificate text-primary":"img/properties16.gif",
									"javascript:submitForm('doAddAttestazioneDirettoreToCRUD(" + getInputPrefix() + ")')",
									true,
									"Attestazione Direttore",
									"btn btn-sm btn-secondary btn-outline-secondary",
									isFromBootstrap);
						}
					}
				}
				it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
							context,
							isFromBootstrap? "fa fa-plus text-primary":"img/new16.gif",
							"javascript:submitForm('doAddAllegatoGenericoToCRUD(" + getInputPrefix() + ")')",
							true,
							"Allegato Generico",
							"btn btn-sm btn-secondary btn-outline-secondary",
							isFromBootstrap);

				innerDelete = innerDelete || Optional.ofNullable(getModel())
							.filter(Incarichi_archivioBulk.class::isInstance)
							.map(Incarichi_archivioBulk.class::cast)
							.filter(el->el.isToBeCreated())
							.filter(el->el.isAllegatoGenerico())
							.isPresent();
			}
			super.writeHTMLToolbar(context, false, false, innerDelete, false, false);
            super.closeButtonGROUPToolbar(context);
		}
	};

	private SimpleDetailCRUDController compensiAllegatiIncarico = new SimpleDetailCRUDController( "CompensiAllegatiIncarico", CompensoBulk.class, "compensiColl", ripartizioneIncarichiPerAnno);

	private SimpleDetailCRUDController crudIncarichiVariazioni = new Incarichi_archivioCRUDController( "IncarichiVariazioni", Incarichi_repertorio_varBulk.class, "incarichi_repertorio_varColl", incarichiColl) {
		@Override
		public void validateForDelete(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			Incarichi_repertorio_varBulk variazione = (Incarichi_repertorio_varBulk)oggettobulk;
			if (variazione.isDefinitivo())
				throw new ValidationException("Eliminazione non possibile!\nLa variazione e' gia' stata resa definitiva.");
			if (variazione.isAnnullato())
				throw new ValidationException("Eliminazione non possibile!\nLa variazione e' gia' stata annullata.");
			super.validateForDelete(actioncontext, oggettobulk);
		}
		@Override
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			super.validate(actioncontext, oggettobulk);
			Incarichi_repertorio_varBulk variazione = (Incarichi_repertorio_varBulk)oggettobulk;
			if (variazione.getTipo_variazione()==null)
				throw new ValidationException("Attenzione: indicare il tipo di variazione.");
			if (variazione.isVariazioneIntegrazioneIncarico() || variazione.isVariazioneIntegrazioneContributi() || variazione.isVariazioneIntegrazioneMaternita()) {
				for (Iterator<Incarichi_repertorio_varBulk> i = variazione.getIncarichi_repertorio().getIncarichi_repertorio_varColl().iterator(); i.hasNext();) {
					Incarichi_repertorio_varBulk varia = i.next();
					if (!varia.equalsByPrimaryKey(variazione))
						if (varia.getStato().equals(Incarichi_archivioBulk.STATO_VALIDO) &&
								varia.getTipo_variazione().equals(variazione.getTipo_variazione()))
							throw new ValidationException("Attenzione: non è possibile inserire più di una variazione di tipo \""+Incarichi_repertorio_varBulk.tipo_variazioneForEnteKeys.get(varia.getTipo_variazione())+"\"");
				}
			}
			if (variazione.isVariazioneIntegrazioneIncarico()){
				if (variazione.isVariazioneIntegrazioneIncaricoTransitorio()){
					if (variazione.isDefinitivo()) {
						if (variazione.getDt_variazione()==null)
							throw new ValidationException("Attenzione: è obbligatorio indicare la data di stipula della variazione.");
						if (variazione.getDt_fine_validita()==null)
							throw new ValidationException("Attenzione: è obbligatorio indicare la nuova data di fine incarico.");
					}
					if (variazione.getImporto_lordo()==null||
							variazione.getImporto_lordo().compareTo(BigDecimal.ZERO)==0)
						throw new ValidationException("Attenzione: è obbligatorio indicare l'importo dell'integrazione del lordo percipiente.");
				} else {
					if (variazione.getDt_fine_validita()==null &&
							(variazione.getImporto_lordo()==null||variazione.getImporto_lordo().compareTo(BigDecimal.ZERO)==0))
						throw new ValidationException("Attenzione: indicare almeno un dato variato (\"data fine\" o \"integrazione lordo percipiente\") dell'incarico.");
				}
			}
			else if (variazione.getImporto_complessivo()==null || variazione.getImporto_complessivo().compareTo(BigDecimal.ZERO)==0)
				throw new ValidationException("Attenzione: è obbligatorio indicare l'importo dell'integrazione della \"Spesa complessiva presunta calcolata\".");
			else if (!variazione.isAnnullato() && variazione.isVariazioneIntegrazioneContributi()){
				java.math.BigDecimal prcIncrementoVar = Utility.nvl(variazione.getIncarichi_repertorio().getIncarichi_procedura().getTipo_incarico().getPrc_incremento_var());
				BigDecimal importoMaxVar = variazione.getIncarichi_repertorio().getIncarichi_procedura().getImporto_complessivo().multiply(prcIncrementoVar.divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_EVEN));
				if (variazione.getImporto_complessivo().compareTo(importoMaxVar)==1)
					throw new ValidationException("Attenzione: la variazione massima consentita per \"Adeguamento Incremento Aliquote\" è " + new it.cnr.contab.util.EuroFormat().format(importoMaxVar)+".");
			}
		};
		/*
		 * Metodo che attiva il tasto di cancellazione nel CRUDController
		 */
		@Override
		public boolean isShrinkable() {
			return super.isShrinkable() && ((getModel()!=null && getModel().isToBeCreated())||((CRUDIncarichiProceduraBP)getParentController().getParentController()).isUoEnte());
		}
		@Override
		public void writeHTMLToolbar(
				javax.servlet.jsp.PageContext context,
				boolean reset,
				boolean find,
				boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException {

			Incarichi_repertorio_varBulk incaricoVar = (Incarichi_repertorio_varBulk)getModel();

			super.writeHTMLToolbar(context, reset, find, delete, false);

			if (incaricoVar!=null) {
				if (isEditing() &&
						incaricoVar.isProvvisorio() &&
						incaricoVar.isVariazioneIntegrazioneIncarico() &&
						incaricoVar.getIncarichi_repertorio().isIncaricoDefinitivo()) {
					String command = "javascript:submitForm('doSalvaDefinitivoVariazioneContratto')";
					boolean isFromBootstrap = HttpActionContext.isFromBootstrap(context);					
					it.cnr.jada.util.jsp.JSPUtils.toolbarButton(
							context,
							"img/saveall16.gif",
							command,
							true,
							"Salva Definitivo Variazione Contratto",
							isFromBootstrap);
				}
			}
			super.closeButtonGROUPToolbar(context);
		}
	};

	private SimpleDetailCRUDController crudAssUO = new SimpleDetailCRUDController( "Associazione UO", Ass_incarico_uoBulk.class, "associazioneUO", incarichiColl);
	private SimpleDetailCRUDController crudAssUODisponibili = new SimpleDetailCRUDController( "Associazione UO Disponibili", Unita_organizzativaBulk.class, "associazioneUODisponibili", incarichiColl);
	private SimpleDetailCRUDController crudNote = new SimpleDetailCRUDController( "Annotazioni", Incarichi_procedura_noteBulk.class, "incarichi_procedura_noteColl", this) {
		public void validateForDelete(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			if (oggettobulk!=null && !oggettobulk.isToBeCreated())
				throw new ValidationException("Eliminazione di un record confermato non possibile!.");
			super.validateForDelete(actioncontext, oggettobulk);
		}
	};

	private SimpleDetailCRUDController incarichiRappColl = new Incarichi_archivioCRUDController( "IncarichiRapp", Incarichi_repertorio_rappBulk.class, "incarichi_repertorio_rappColl", incarichiColl) {
		@Override
		public void validateForDelete(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			Incarichi_repertorio_rappBulk rapporto = (Incarichi_repertorio_rappBulk)oggettobulk;
			if (rapporto.isAnnullato())
				throw new ValidationException("Eliminazione non possibile!\nLa dichiarazione e' gia' stata annullata.");
			super.validateForDelete(actioncontext, oggettobulk);
		}
		public boolean isGrowable() {
			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)((SimpleDetailCRUDController)getParentController()).getModel();
			return super.isShrinkable() && incarico.getDt_stipula()!=null;
		};
		/*
		 * Metodo che attiva il tasto di cancellazione nel CRUDController
		 */
		@Override
		public boolean isShrinkable() {
			return super.isShrinkable() && ((getModel()!=null && getModel().isToBeCreated())||((CRUDIncarichiProceduraBP)getParentController().getParentController()).isUoEnte());
		};
	};

	private SimpleDetailCRUDController incarichiRappDetColl = new SimpleDetailCRUDController( "IncarichiRappDet", Incarichi_repertorio_rapp_detBulk.class, "incarichi_repertorio_rapp_detColl", incarichiColl){
		public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
			if (oggettobulk instanceof Incarichi_repertorio_rapp_detBulk) {
				if (!oggettobulk.isToBeCreated() && !oggettobulk.isToBeDeleted() &&
						oggettobulk.getCrudStatus()!=OggettoBulk.UNDEFINED) {
					((Incarichi_repertorio_rapp_detBulk)oggettobulk).setStato(Incarichi_archivioBulk.STATO_ANNULLATO);
					((Incarichi_repertorio_rapp_detBulk)oggettobulk).setDt_annullamento(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
					oggettobulk.setToBeUpdated();
					return oggettobulk;
				}
			}
			return super.removeDetail(oggettobulk, i);
		}
	};

	private Incarichi_richiestaBulk incaricoRichiestaOrigine;
	private Incarichi_repertorioBulk incaricoRepertorioOrigine;
	private boolean utenteAbilitatoPubblicazioneSito;
	private boolean utenteAbilitatoFunzioniIncarichi;
	private Unita_organizzativaBulk uoSrivania;
	private boolean verificaFormatoBando = Boolean.FALSE;
	private boolean utenteAbilitatoInvioMail = Boolean.FALSE;
	private boolean utenteAbilitatoSbloccoProcedura = Boolean.FALSE;
	private boolean superUtente = Boolean.FALSE;
	private boolean creaERiportaNuovoIncarico=false;
	private Date dtLimiteVariazione = null;
	private boolean utenteAbilitatoModificaAllegatoContratto = Boolean.FALSE;
	private boolean utenteAbilitatoModificaAllegatoCurriculum = Boolean.FALSE;

	/**
	 * Primo costruttore della classe <code>CRUDIncarichiRepertorioBP</code>.
	 */
	public CRUDIncarichiProceduraBP() {
		super();
	}

	/**
	 * Secondo costruttore della classe <code>CRUDIncarichiRepertorioBP</code>.
	 * @param function String function
	 */
	public CRUDIncarichiProceduraBP(String function) {
		super(function);
	}
	public CRUDIncarichiProceduraBP(String function, OggettoBulk bulk) {
		super(function);
		if (bulk instanceof Incarichi_richiestaBulk)
			setIncaricoRichiestaOrigine((Incarichi_richiestaBulk)bulk);
		else if (bulk instanceof Incarichi_repertorioBulk)
			setIncaricoRepertorioOrigine((Incarichi_repertorioBulk)bulk);
	}
	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk=super.initializeModelForInsert(actioncontext, oggettobulk);

		Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)oggettobulk;

		if (getIncaricoRichiestaOrigine()!=null){
			procedura.setOggetto(getIncaricoRichiestaOrigine().getAttivita());
			procedura.setIncarichi_richiesta(getIncaricoRichiestaOrigine());
			procedura.setCds(getIncaricoRichiestaOrigine().getCds());
			procedura.setUnita_organizzativa(getIncaricoRichiestaOrigine().getUnita_organizzativa());
			procedura.setIndirizzo_unita_organizzativa(getIncaricoRichiestaOrigine().getIndirizzo_unita_organizzativa());
			procedura.setProcedura_amministrativa(new Procedure_amministrativeBulk());
			procedura.getProcedura_amministrativa().setFl_ricerca_incarico(Boolean.TRUE);
			procedura.setNr_contratti(getIncaricoRichiestaOrigine().getNrRisorseNonTrovate()-
					getIncaricoRichiestaOrigine().getNrContrattiAttivati());

			RemoteIterator ri = find(actioncontext,null,procedura.getProcedura_amministrativa(),procedura,"procedura_amministrativa");

			//metodo per riempire immediatamente la Procedura Amministrativa se ne esiste 1 sola
			try	{
				ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actioncontext, ri);
				if (ri != null && ri.countElements() == 1)
					procedura.setProcedura_amministrativa((Procedure_amministrativeBulk)ri.nextElement());
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(actioncontext, ri);
			} catch (Exception e) {
			}

			//ripulisco il dato per evitare che, se crea un nuovo incarico, riproponga il
			//collegamento
			setIncaricoRichiestaOrigine(null);
		}

		procedura.setUtenteCollegatoUoEnte(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(actioncontext).getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0);
		procedura.setUtenteCollegatoSuperUtente(isSuperUtente());
		procedura.setDt_registrazione(DateServices.getDt_valida(actioncontext.getUserContext()));

		if (this.isBorseStudioBP() || this.isAssegniRicercaBP()) {
			//metodo per riempire immediatamente la Procedura Amministrativa
			try	{
				RemoteIterator ri = this.find(actioncontext,null,new Procedure_amministrativeBulk("INC3"),procedura,"procedura_amministrativa");
				ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actioncontext, ri);
				if (ri != null && ri.countElements() == 1)
					procedura = this.initializeProcedura_amministrativa(actioncontext, procedura, (Procedure_amministrativeBulk)ri.nextElement());
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(actioncontext, ri);
			}catch(java.rmi.RemoteException ex){
				throw handleException(ex);
			}

			//metodo per riempire immediatamente il tipo di attivita
			try	{
				Tipo_attivitaBulk tipoAttivita = new Tipo_attivitaBulk();
				tipoAttivita.setTipo_associazione(this.isAssegniRicercaBP()?Tipo_attivitaBulk.ASS_ASSEGNI_RICERCA:Tipo_attivitaBulk.ASS_BORSE_STUDIO);
				RemoteIterator ri = this.find(actioncontext,null,tipoAttivita,procedura,"tipo_attivita");
				ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actioncontext, ri);
				if (ri != null && ri.countElements() == 1)
					procedura.setTipo_attivita((Tipo_attivitaBulk)ri.nextElement());
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(actioncontext, ri);
			}catch(java.rmi.RemoteException ex){
				throw handleException(ex);
			}

			//metodo per riempire immediatamente il tipo di incarico
			try	{
				Tipo_incaricoBulk tipoIncarico = new Tipo_incaricoBulk();
				tipoIncarico.setTipo_associazione(this.isAssegniRicercaBP()?Tipo_incaricoBulk.ASS_ASSEGNI_RICERCA:Tipo_incaricoBulk.ASS_BORSE_STUDIO);
				RemoteIterator ri = this.find(actioncontext,null,tipoIncarico,procedura,"tipo_incarico");
				ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actioncontext, ri);
				if (ri != null && ri.countElements() == 1)
					procedura = this.initializeFind_tipo_incarico(actioncontext, procedura, (Tipo_incaricoBulk)ri.nextElement());
				else {
					it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(actioncontext, ri);
					throw new it.cnr.jada.comp.ApplicationException("Errore di configurazione del tipo incarico relativamente alla tipologia "+
							(this.isAssegniRicercaBP()?"Assegni di Ricerca":"Borse di Studio")+". Contattare il Customer Support Team.");
				}
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(actioncontext, ri);
			}catch(it.cnr.jada.comp.ComponentException ex){
				throw handleException(ex);
			}catch(java.rmi.RemoteException ex){
				throw handleException(ex);
			}
		}
		//metodo richiamato per precaricare i parametri sull'oggetto
		this.getIncarichiParametri(actioncontext.getUserContext(),procedura);
		return procedura;
	}
	public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		try {
			oggettobulk=super.initializeModelForEdit(actioncontext, oggettobulk);
			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)oggettobulk;

			if (!procedura.getFl_migrata_to_cmis())
				throw handleException(new ApplicationException("Procedura non utilizzabile. E' in corso una operazione di migrazione dei dati. La procedura tornera' disponibile al termine della stessa."));

//			completaUnitaOrganizzativa(actioncontext, procedura, procedura.getUnita_organizzativa());
			for ( Iterator i = procedura.getIncarichi_procedura_annoColl().iterator(); i.hasNext(); ) {
				Incarichi_procedura_annoBulk procAnno = (Incarichi_procedura_annoBulk) i.next();
				procAnno.caricaAnniList(actioncontext);
			}
			for ( Iterator i = procedura.getIncarichi_repertorioColl().iterator(); i.hasNext(); ) {
				Incarichi_repertorioBulk repertorio = (Incarichi_repertorioBulk) i.next();
				for ( Iterator y = repertorio.getIncarichi_repertorio_annoColl().iterator(); y.hasNext(); ) {
					Incarichi_repertorio_annoBulk repAnno = (Incarichi_repertorio_annoBulk) y.next();
					repAnno.caricaAnniList(actioncontext);
				}
				if (repertorio.isIncaricoDefinitivo() && (isUoEnte() || isUtenteAbilitatoPubblicazioneSito()))
					repertorio.setDataProrogaEnableOnView(Boolean.TRUE);
			}
			for ( Iterator i = procedura.getArchivioAllegatiMI().iterator(); i.hasNext(); ) {
				Incarichi_archivioBulk archivio = (Incarichi_archivioBulk) i.next();
				if (archivio.isProgetto()) {
					Incarichi_parametriBulk parametri = this.getIncarichiParametri(actioncontext.getUserContext(),procedura);
					if (!(parametri!=null && parametri.getAllega_progetto()!=null && parametri.getAllega_progetto().equals("Y")))
						archivio.setFileRequired(Boolean.FALSE);
					if (parametri!=null && parametri.getIndica_url_progetto()!=null && parametri.getIndica_url_progetto().equals("Y"))
						archivio.setUrlRequired(Boolean.TRUE);
				}
			}

			procedura.setUtenteCollegatoUoEnte(isUoEnte());
			procedura.setUtenteCollegatoSuperUtente(isSuperUtente());

			return procedura;
		} catch(javax.ejb.EJBException ejbe){
			throw handleException(ejbe);
		}
	}
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		setTab("tab","tabIncarichi_procedura_contratto");
		super.init(config, actioncontext);
	}
	public void findTipiRapporto(ActionContext context) throws BusinessProcessException{
		Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)getModel();
		if (procedura != null) {
			for ( Iterator i = procedura.getIncarichi_repertorioColl().iterator(); i.hasNext(); )
				findTipiRapporto(context, (Incarichi_repertorioBulk)i.next());
		}
	}
	private void findTipiRapporto(ActionContext context, Incarichi_repertorioBulk incarico) throws BusinessProcessException{
		try{
			if (incarico.getTerzo()!= null) {
				java.util.Collection coll = Utility.createIncarichiRepertorioComponentSession().findTipiRapporto(context.getUserContext(), incarico);
				incarico.setTipiRapporto(coll);

				if(coll == null || coll.isEmpty()){
					incarico.setTipo_rapporto(null);
					throw new it.cnr.jada.comp.ApplicationException("Non esistono Tipi Rapporto validi associati ad uno dei contraenti selezionati (\""+incarico.getV_terzo().getCognome()+" "+incarico.getV_terzo().getNome()+"\")");
				}
				else if (incarico.getTipo_rapporto()!=null) {
					boolean trovato=false;
					for ( Iterator i = coll.iterator(); i.hasNext(); ) {
						if (((OggettoBulk)i.next()).equalsByPrimaryKey(incarico.getTipo_rapporto())){
							trovato=true;
							break;
						}
					}
					if (!trovato)
						incarico.setTipo_rapporto(null);
				}
			}else{
				incarico.setTipo_rapporto(null);
			}

		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	public final SimpleDetailCRUDController getRipartizionePerAnno() {
		return ripartizionePerAnno;
	}

	private Incarichi_richiestaBulk getIncaricoRichiestaOrigine() {
		return incaricoRichiestaOrigine;
	}

	private void setIncaricoRichiestaOrigine(
			Incarichi_richiestaBulk incaricoRichiestaOrigine) {
		this.incaricoRichiestaOrigine = incaricoRichiestaOrigine;
	}

	private Incarichi_repertorioBulk getIncaricoRepertorioOrigine() {
		return incaricoRepertorioOrigine;
	}

	private void setIncaricoRepertorioOrigine(
			Incarichi_repertorioBulk incaricoRepertorioOrigine) {
		this.incaricoRepertorioOrigine = incaricoRepertorioOrigine;
	}

	public boolean isROIncaricoRichiesta() {
		return getIncaricoRichiestaOrigine()!=null;
	}

	public boolean isROContraente() {
		Incarichi_repertorioBulk model = (Incarichi_repertorioBulk)getModel();
		return (!isSearching() &&
				((model.getDt_inizio_validita()==null || model.getDt_fine_validita()==null) ||
						(model.getTerzo() == null || model.getTerzo().getCrudStatus() == OggettoBulk.NORMAL)));
	}

	public boolean isUtenteAbilitatoPubblicazioneSito() {
		return utenteAbilitatoPubblicazioneSito;
	}

	private void setUtenteAbilitatoPubblicazioneSito(boolean utenteAbilitatoPubblicazioneSito) {
		this.utenteAbilitatoPubblicazioneSito = utenteAbilitatoPubblicazioneSito;
	}

	private Unita_organizzativaBulk getUoSrivania() {
		return uoSrivania;
	}

	private void setUoSrivania(Unita_organizzativaBulk uoSrivania) {
		this.uoSrivania = uoSrivania;
	}

	public boolean isUoEnte(){
		return (getUoSrivania().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0);
	}

	private boolean isUtenteAbilitatoInvioMail(){
		return utenteAbilitatoInvioMail;
	}

	private void setUtenteAbilitatoInvioMail(boolean utenteAbilitatoInvioMail){
		this.utenteAbilitatoInvioMail = utenteAbilitatoInvioMail;
	}

	private boolean isUtenteAbilitatoSbloccoProcedura(){
		return utenteAbilitatoSbloccoProcedura;
	}

	private void setUtenteAbilitatoSbloccoProcedura(boolean utenteAbilitatoSbloccoProcedura){
		this.utenteAbilitatoSbloccoProcedura = utenteAbilitatoSbloccoProcedura;
	}

	public boolean isSuperUtente(){
		return superUtente;
	}

	private void setSuperUtente(boolean superUtente){
		this.superUtente = superUtente;
	}

	public boolean isUtenteAbilitatoModificaAllegatoContratto(){
		return utenteAbilitatoModificaAllegatoContratto;
	}

	private void setUtenteAbilitatoModificaAllegatoContratto(boolean utenteAbilitatoModificaAllegatoContratto){
		this.utenteAbilitatoModificaAllegatoContratto = utenteAbilitatoModificaAllegatoContratto;
	}

	public boolean isUtenteAbilitatoModificaAllegatoCurriculum() {
		return utenteAbilitatoModificaAllegatoCurriculum;
	}
	
	private void setUtenteAbilitatoModificaAllegatoCurriculum(boolean utenteAbilitatoModificaAllegatoCurriculum) {
		this.utenteAbilitatoModificaAllegatoCurriculum = utenteAbilitatoModificaAllegatoCurriculum;
	}
	
	protected void initialize(ActionContext context) throws BusinessProcessException {
		super.initialize(context);
		try {
			setUoSrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));
			setUtenteAbilitatoPubblicazioneSito(UtenteBulk.isAbilitatoPubblicazioneSito(context.getUserContext()));
			setUtenteAbilitatoFunzioniIncarichi(UtenteBulk.isAbilitatoFunzioniIncarichi(context.getUserContext()));
			setSuperUtente(UtenteBulk.isSuperUtenteFunzioniIncarichi(context.getUserContext()));
			setDtLimiteVariazione(((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession")).getDt01(context.getUserContext(), new Integer(0), "*", "PROCEDURA_CONFERIMENTO_INCARICHI", "DATA_LIMITE_VARIAZIONE"));
			setUtenteAbilitatoInvioMail(context.getUserContext().getUser().equals("raffaele.pagano")||
					context.getUserContext().getUser().equals("roberto.tatarelli")||
					context.getUserContext().getUser().equals("matilde.durso")||
					context.getUserContext().getUser().equals("garzenini.paola")||
					context.getUserContext().getUser().equals("stanislao.fusco")||
					context.getUserContext().getUser().equals("flavia.giardina")||
					context.getUserContext().getUser().equals("rosangela.pucciarelli")||
					context.getUserContext().getUser().equals("mario.incarnato"));

			String value = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession")).getVal01(context.getUserContext(), new Integer(0), "*", Configurazione_cnrBulk.PK_INCARICHI_MODIFICA_ALLEGATI, Configurazione_cnrBulk.SK_INCARICHI_MOD_CONTRATTO);
			setUtenteAbilitatoModificaAllegatoContratto(value!=null && "Y".equals(value));

			String value2 = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession")).getVal01(context.getUserContext(), new Integer(0), "*", Configurazione_cnrBulk.PK_INCARICHI_MODIFICA_ALLEGATI, Configurazione_cnrBulk.SK_INCARICHI_MOD_CURRICULUM);
			setUtenteAbilitatoModificaAllegatoCurriculum(value2!=null && "Y".equals(value2));
			
			if (getModel()!=null && getModel() instanceof Incarichi_proceduraBulk)
				((Incarichi_proceduraBulk)getModel()).setUtenteCollegatoSuperUtente(isSuperUtente());
		} catch (ComponentException e1) {
			throw handleException(e1);
		} catch (RemoteException e1) {
			throw handleException(e1);
		}
	}

	public void completaUnitaOrganizzativa(it.cnr.jada.action.ActionContext context, Incarichi_proceduraBulk procedura, Unita_organizzativaBulk uo) throws BusinessProcessException {
		try {
			procedura.setUnita_organizzativa(uo);
			setModel(context, ((IncarichiProceduraComponentSession)createComponentSession()).caricaSedeUnitaOrganizzativa(context.getUserContext(),procedura));
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	public void changeImportoLordo(it.cnr.jada.action.ActionContext context, Incarichi_proceduraBulk incarico, BigDecimal importoLordo) {
		incarico.setImporto_lordo(importoLordo);
		//valorizzo il numero contratti attivato se null o 0
		if (incarico.getNr_contratti()==null ||incarico.getNr_contratti()==0)
			incarico.setNr_contratti(1);

		//BigDecimal importoLordoTotale = importoLordo.multiply(new BigDecimal(incarico.getNr_contratti()));
		if (incarico.getTipo_incarico()==null || incarico.getTipo_incarico().getPrc_incremento()==null)
			incarico.setImporto_complessivo(importoLordo);
		else
			incarico.setImporto_complessivo(importoLordo.add(importoLordo.multiply(incarico.getTipo_incarico().getPrc_incremento()).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_EVEN)));
	}
	public void changeImportoLordo(it.cnr.jada.action.ActionContext context, Incarichi_proceduraBulk incarico, Incarichi_repertorio_varBulk variazione, BigDecimal importoLordo) {
		variazione.setImporto_lordo(importoLordo);

		//BigDecimal importoLordoTotale = importoLordo.multiply(new BigDecimal(incarico.getNr_contratti()));
		if (incarico.getTipo_incarico()==null || incarico.getTipo_incarico().getPrc_incremento()==null)
			variazione.setImporto_complessivo(importoLordo);
		else
			variazione.setImporto_complessivo(importoLordo.add(importoLordo.multiply(incarico.getTipo_incarico().getPrc_incremento()).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_EVEN)));
	}
	public SimpleDetailCRUDController getCrudArchivioAllegati() {
		return crudArchivioAllegati;
	}
	public void setCrudArchivioAllegati(SimpleDetailCRUDController controller) {
		crudArchivioAllegati = controller;
	}
	public SimpleDetailCRUDController getCrudArchivioAllegatiMI() {
		return crudArchivioAllegatiMI;
	}
	public void setCrudArchivioAllegatiMI(SimpleDetailCRUDController controller) {
		crudArchivioAllegatiMI = controller;
	}
	/*
	 * Necessario per la creazione di una form con enctype di tipo "multipart/form-data"
	 * Sovrascrive quello presente nelle superclassi
	 *
	*/
	public void openForm(javax.servlet.jsp.PageContext context,String action,String target) throws java.io.IOException,javax.servlet.ServletException {
		if (getTab("tab").equals("tabIncarichi_procedura_allegati") ||
				getTab("tab").equals("tabIncarichi_procedura_variazioni") ||
				(getTab("tab").equals("tabIncarichi_procedura_rapporti") &&
						getTab("tabProceduraRapporti").equals("tabProceduraRapportiAnno")) ||
				(getTab("tab").equals("tabIncarichi_procedura_contratto") &&
						(getTab("tabProceduraContratto").equals("tabProceduraContrattoAllegati") ||
								getTab("tabProceduraContratto").equals("tabProceduraContrattoVariazioni") ||
								(getTab("tabProceduraContratto").equals("tabProceduraContrattoRapporti") &&
										getTab("tabProceduraRapporti").equals("tabProceduraRapportiAnno")))))
			openForm(context,action,target,"multipart/form-data");
		else
			super.openForm(context, action, target);
	}

	public void delete(ActionContext actioncontext) throws BusinessProcessException {
		int crudStatus = getModel().getCrudStatus();
		try
		{
			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) getModel();
			//In fase di inserimento si permette di eliminare
			if ( procedura.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_INCARICO)!=1 )
				super.delete(actioncontext);
				// si tratta di uno storno
			else if ( procedura.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_CONTRATTO)!=1)
				stornaProceduraIncarico(actioncontext);
				// si tratta di uno storno
			else if ( procedura.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_DEFINITIVA)==0) {
				if (!isUoEnte()&&!isUtenteAbilitatoFunzioniIncarichi())
					throw new it.cnr.jada.comp.ApplicationException("Eliminazione consentita solo ad utenti con l'abilitazione alle funzioni di direttore di istituto.");
				if (isIncaricoUtilizzato(actioncontext))
					chiudiProceduraIncarico(actioncontext);
				else
					stornaProceduraIncarico(actioncontext);
			}
			else // stato = STORNATA
				throw new it.cnr.jada.comp.ApplicationException("Lo stato della procedura di conferimento incarico non ne consente la cancellazione/storno");

		} catch(Exception e) {
			getModel().setCrudStatus(crudStatus);
			throw handleException(e);
		}
	}

	public void saveChildren(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
		allineaDatiProceduraIncarichi(actioncontext.getUserContext(),(Incarichi_proceduraBulk)getModel());
		super.saveChildren(actioncontext);
	}

	public void pubblicaSulSito(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			if (((Incarichi_proceduraBulk) getModel()).getBando()==null) {
				if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
					//Istanzio la classe per riempire tipo_archivioKeys
					new Incarichi_procedura_archivioBulk();
				}
				throw new it.cnr.jada.comp.ApplicationException("Allegare alla procedura di conferimento incarico un \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_BANDO).toString()+"\".");
			}
			setModel(context,((IncarichiProceduraComponentSession)createComponentSession()).pubblicaSulSito(context.getUserContext(), getModel()));
		}
		catch(Exception e)
		{
			throw handleException(e);
		}
	}
	public void annullaPubblicazioneSulSito(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException
	{
		try {
			setModel(context,((IncarichiProceduraComponentSession)createComponentSession()).annullaPubblicazioneSulSito(context.getUserContext(), getModel()));
		}
		catch(Exception e)
		{
			throw handleException(e);
		}
	}
	protected Button[] createToolbar() {
		Button[] toolbar = super.createToolbar();
		Button[] newToolbar = new Button[ toolbar.length + 7];
		int i;
		for ( i = 0; i < toolbar.length; i++ )
			newToolbar[i] = toolbar[i];
		newToolbar[ i ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.pubblica");
		newToolbar[ i ].setSeparator(true);
		newToolbar[ i+1 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.annullaPubblica");
		newToolbar[ i+1 ].setSeparator(true);
		newToolbar[ i+2 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.salvaDefinitivo");
		newToolbar[ i+2 ].setSeparator(true);
		newToolbar[ i+3 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.concludiProcedura");
		newToolbar[ i+3 ].setSeparator(true);
		newToolbar[ i+4 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.concludiMonoIncarico");
		newToolbar[ i+4 ].setSeparator(true);
		newToolbar[ i+5 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.annullaDefinitivo");
		newToolbar[ i+5 ].setSeparator(true);
		newToolbar[ i+6 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.sendEmailCds");
		newToolbar[ i+6 ].setSeparator(true);

		return newToolbar;
	}
	public boolean isPubblicaButtonHidden()	{
		return !(isUoEnte()||isUtenteAbilitatoPubblicazioneSito()) ||
				!((Incarichi_proceduraBulk)getModel()).isProceduraDaPubblicare() ||
				!(isInserting() ||
						((Incarichi_proceduraBulk)getModel()).getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_INCARICO)!=1 );
	}
	public boolean isAnnullaPubblicaButtonHidden() {
		return !(isUoEnte()||isUtenteAbilitatoPubblicazioneSito()) ||
				!((Incarichi_proceduraBulk)getModel()).isProceduraDaPubblicare() ||
				isInserting()||
				((Incarichi_proceduraBulk)getModel()).getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_PUBBLICAZIONE)!=0;
	}
	public String[][] getTabs() {
		TreeMap<Integer, String[]> hash = new TreeMap<Integer, String[]>();
		int i=0;

		hash.put(i++, new String[]{"tabTestata", "Procedura", "/incarichi00/tab_incarichi_procedura.jsp" });
		if (isSearching()) {
			hash.put(i++, new String[]{"tabSearchTerzo", "Terzo", "/incarichi00/tab_incarichi_procedura_search_terzo.jsp"});
			hash.put(i++, new String[]{"tabSearchIncarico", "Incarico", "/incarichi00/tab_incarichi_procedura_search_incarico.jsp"});
		} else {
			hash.put(i++, new String[]{"tabIncarichi_procedura_anno", "Importo per anno", "/incarichi00/tab_incarichi_procedura_anno.jsp" });

			if (isContrattoEnable())
				hash.put(i++, new String[]{"tabIncarichi_procedura_contratto","Incarichi","/incarichi00/tab_incarichi_procedura_contratto.jsp" });

			hash.put(i++, new String[]{"tabIncarichi_procedura_allegati","Allegati","/incarichi00/tab_incarichi_procedura_allegati.jsp" });

			if (getModel()!=null && !((Incarichi_proceduraBulk)getModel()).isProceduraMultiIncarico() &&
					!((Incarichi_proceduraBulk)getModel()).getIncarichi_repertorioColl().isEmpty()){
				Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)((Incarichi_proceduraBulk)getModel()).getIncarichi_repertorioColl().get(0);
				if (((Incarichi_proceduraBulk)getModel()).isDichiarazioneContraenteRequired())
					hash.put(i++, new String[]{"tabIncarichi_procedura_rapporti","Dichiarazione Contraente","/incarichi00/tab_incarichi_procedura_rapporti.jsp" });
				if (incarico.isIncaricoDefinitivo() || !incarico.getIncarichi_repertorio_varColl().isEmpty())
					hash.put(i++, new String[]{"tabIncarichi_procedura_variazioni","Variazioni","/incarichi00/tab_incarichi_procedura_variazioni.jsp" });
				if (incarico.getIncarichi_procedura().isUtenteCollegatoSuperUtente())
					hash.put(i++, new String[]{"tabIncarichi_procedura_ass_uo","UO","/incarichi00/tab_incarichi_procedura_ass_uo.jsp" });
			}

			if (this.isIncarichiProceduraBP())
				hash.put(i++, new String[]{"tabIncarichi_procedura_dati_perla","Dati PERLA","/incarichi00/tab_incarichi_procedura_dati_perla.jsp" });
			else
				hash.put(i++, new String[]{"tabIncarichi_procedura_dati_perla","Altri Dati","/incarichi00/tab_incarichi_procedura_dati_perla.jsp" });

			if (isSuperUtente())
				hash.put(i++, new String[]{"tabIncarichi_procedura_note","Annotazioni","/incarichi00/tab_incarichi_procedura_note.jsp" });
		}
		String[][] tabs = new String[i][3];
		for (int j = 0; j < i; j++) {
			tabs[j]=new String[]{hash.get(j)[0],hash.get(j)[1],hash.get(j)[2]};
		}
		return tabs;
	}

	protected void basicEdit(ActionContext actioncontext, OggettoBulk oggettobulk, boolean flag) throws BusinessProcessException {
		super.basicEdit(actioncontext, oggettobulk, flag);

		if (((Incarichi_proceduraBulk)getModel()).isProceduraScaduta()){
			if (!isUoEnte()) {
				setStatus(CRUDBP.VIEW);
				setMessage("Procedura di conferimento incarico scaduta. Non e' consentita la modifica.");
			}
			else
				setMessage("Procedura di conferimento incarico scaduta. E' consentita sola la modifica della \"data di scadenza\".");
		}
		if (((Incarichi_proceduraBulk)getModel()).isPubblicazioneInCorso()){
			setStatus(CRUDBP.VIEW);
			setMessage("Procedura di conferimento incarico in fase di pubblicazione. E' consentito solo l'annullamento della pubblicazione.");
		}
		else if (((Incarichi_proceduraBulk)getModel()).isProceduraAnnullata()){
			setStatus(CRUDBP.VIEW);
			if (((Incarichi_proceduraBulk)getModel()).getDt_cancellazione() != null && ((Incarichi_proceduraBulk)getModel()).getDt_fine_pubblicazione() != null &&
					((Incarichi_proceduraBulk)getModel()).getDt_cancellazione().before(((Incarichi_proceduraBulk)getModel()).getDt_fine_pubblicazione()))
				setMessage("La procedura di conferimento incarico e' stata annullata. Non e' consentita la modifica.");
			else
				setMessage("La procedura di conferimento incarico e' stata eliminata. Non e' consentita la modifica.");
		}
		else if (((Incarichi_proceduraBulk)getModel()).isProceduraDefinitiva()){
//			setStatus(CRUDBP.VIEW);
			if (((Incarichi_proceduraBulk)getModel()).getNr_contratti().compareTo(1)==0)
				setMessage("Procedura di conferimento incarico definitiva. E' consentita solo la modifica della ripartizione degli importi tra i diversi esercizi.");
			else
				setMessage("Procedura di conferimento incarico definitiva. E' consentita solo, per ogni contratto associato, la modifica della ripartizione degli importi tra i diversi esercizi.");
		}
		else if (((Incarichi_proceduraBulk)getModel()).isProceduraChiusa()){
			setStatus(CRUDBP.VIEW);
			if (((Incarichi_proceduraBulk)getModel()).getIncarichi_repertorioColl().size()==1 &&
					((Incarichi_repertorioBulk)((Incarichi_proceduraBulk)getModel()).getIncarichi_repertorioColl().get(0)).getEsito_corte_conti()!=null &&
					((Incarichi_repertorioBulk)((Incarichi_proceduraBulk)getModel()).getIncarichi_repertorioColl().get(0)).getEsito_corte_conti().equals(Incarichi_repertorioBulk.ESITO_ILLEGITTIMO))
				setMessage("Procedura di conferimento annullata per esito negativo del controllo della Corte dei Conti. Non e' consentita la modifica.");
			else
				setMessage("Procedura di conferimento incarico definitiva e chiusa. Non e' consentita la modifica.");
		}
		else {
			int nrChiusi = 0;
			for (Iterator<Incarichi_repertorioBulk> i = ((Incarichi_proceduraBulk)getModel()).getIncarichi_repertorioColl().iterator(); i.hasNext();) {
				Incarichi_repertorioBulk incarico = i.next();
				if (incarico.isIncaricoDefinitivo()){
					if (((Incarichi_proceduraBulk)getModel()).getNr_contratti().compareTo(1)==0)
						setMessage("Procedura di conferimento incarico definitiva. E' consentita solo la modifica della ripartizione degli importi tra i diversi esercizi.");
					else
						setMessage("Procedura di conferimento incarico con almeno un contratto associato definitivo. E' consentita solo, per ogni contratto associato, la modifica della ripartizione degli importi tra i diversi esercizi.");
					break;
				}
				else if (incarico.isIncaricoChiuso())
					nrChiusi++;
			}
			if (((Incarichi_proceduraBulk)getModel()).getNr_contratti().compareTo(nrChiusi)==0) {
				setStatus(CRUDBP.VIEW);
				setMessage("Procedura di conferimento incarico con tutti i contratti associati definitivi e chiusi. Non e' consentita la modifica.");
			}
		}

		/*serve per impostare la mappa con il contratto creato già evidenziato*/
		if (((Incarichi_proceduraBulk)getModel()).getNr_contratti().compareTo(1)==0 &&
				!((Incarichi_proceduraBulk)getModel()).getIncarichi_repertorioColl().isEmpty()){
			getIncarichiColl().setModelIndex(actioncontext, 0);
			getIncarichiColl().getSelection().setFocus(0);
		}
		if (getIncaricoRepertorioOrigine()!=null) {
			setIncaricoRepertorioOrigine(null);
			setTab("tab","tabIncarichi_procedura_contratto");
		}
	}
	public void stornaProceduraIncarico(ActionContext actioncontext) throws BusinessProcessException {
		int crudStatus = getModel().getCrudStatus();
		try
		{
			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) getModel();
			if ( procedura.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_INCARICO)==1 )
				setModel(actioncontext, ((IncarichiProceduraComponentSession)createComponentSession()).stornaProceduraIncaricoPubblicata(actioncontext.getUserContext(),getModel()));
			else // stato = STORNATA
				throw new it.cnr.jada.comp.ApplicationException( "Lo stato dell'incarico non ne consente lo storno");
		} catch(Exception e) {
			getModel().setCrudStatus(crudStatus);
			throw handleException(e);
		}
	}

	public boolean isContrattoEnable() {
		Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) getModel();
		return procedura != null &&
				procedura.getProcedura_amministrativa()!=null &&
				procedura.getProcedura_amministrativa().getCd_proc_amm()!=null &&
				procedura.getProcedura_amministrativa().getCrudStatus()==OggettoBulk.NORMAL &&
				(!procedura.isProceduraDaPubblicare()
						||
						procedura.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_PUBBLICAZIONE)==1);
	}

	public boolean isSalvaDefinitivoButtonHidden() {
		Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) getModel();
		Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk) getIncarichiColl().getModel();

		return !isEditing()||!isContrattoEnable()||incarico==null||
				procedura.getNr_contratti().compareTo(1)==1 ||
				procedura.isProceduraScaduta() || procedura.isProceduraDefinitiva();
	}

	public boolean isSalvaDefinitivoButtonEnabled() {
		return !isSalvaDefinitivoButtonHidden() &&
				!((Incarichi_proceduraBulk) getModel()).isProceduraScaduta() &&
				!((Incarichi_proceduraBulk) getModel()).isProceduraDefinitiva();
	}
	public void salvaDefinitivo(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			Incarichi_proceduraBulk  procedura = (Incarichi_proceduraBulk)getModel();
			if (procedura.getIncarichi_repertorioValidiColl().size()==0)
				throw new it.cnr.jada.comp.ApplicationException("Completare le informazioni relative al terzo al quale è stato conferito l'incarico.");

			if (procedura.getNr_contratti().compareTo(procedura.getIncarichi_repertorioValidiColl().size())==-1)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione! Risultano collegati alla procedura un numero di contratti definitivi " + procedura.getIncarichi_repertorioValidiColl().size() + " superiore a quello consentito.");

			if (procedura.getProcedura_amministrativa()==null || !(procedura.getProcedura_amministrativa().getCd_proc_amm().equals("INC4"))) {
				if (procedura.getDecisioneAContrattare()==null) {
					Incarichi_parametriBulk parametri = ((IncarichiProceduraComponentSession)createComponentSession()).getIncarichiParametri(context.getUserContext(), procedura);
					if (procedura.getDecisioneAContrattare()==null) {
						if (parametri==null || parametri.getAllega_decisione_ctr()==null || parametri.getAllega_decisione_ctr().equals("Y")) {
							if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
								//Istanzio la classe per riempire tipo_archivioKeys
								new Incarichi_procedura_archivioBulk();
							}
							throw new it.cnr.jada.comp.ApplicationException("Allegare alla procedura di conferimento incarico un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_DECISIONE_A_CONTRATTARE).toString()+"\".");
						}
					}
				}
			}

			if (procedura.getIncarichi_repertorioValidiColl().size()==1)
				Utility.createIncarichiRepertorioComponentSession().salvaDefinitivo(context.getUserContext(), (OggettoBulk)procedura.getIncarichi_repertorioValidiColl().get(0));

			setModel(context, (Incarichi_proceduraBulk)createComponentSession().inizializzaBulkPerModifica(context.getUserContext(), getModel()));
//			((IncarichiProceduraComponentSession)createComponentSession()).archiviaAllegati(context.getUserContext(), (Incarichi_proceduraBulk)getModel());
		}
		catch(Exception e)
		{
			throw handleException(e);
		}
	}
	public void salvaDefinitivoContratto(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			Incarichi_proceduraBulk  procedura = (Incarichi_proceduraBulk)getModel();
			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)getIncarichiColl().getModel();

			if (procedura.getProcedura_amministrativa()==null || !(procedura.getProcedura_amministrativa().getCd_proc_amm().equals("INC4"))) {
				if (procedura.getDecisioneAContrattare()==null) {
					Incarichi_parametriBulk parametri = ((IncarichiProceduraComponentSession)createComponentSession()).getIncarichiParametri(context.getUserContext(), procedura);
					if (procedura.getDecisioneAContrattare()==null) {
						if (parametri==null || parametri.getAllega_decisione_ctr()==null || parametri.getAllega_decisione_ctr().equals("Y")) {
							if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
								//Istanzio la classe per riempire tipo_archivioKeys
								new Incarichi_procedura_archivioBulk();
							}
							throw new it.cnr.jada.comp.ApplicationException("Allegare alla procedura di conferimento incarico un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_DECISIONE_A_CONTRATTARE).toString()+"\".");
						}
					}
				}
			}

			Utility.createIncarichiRepertorioComponentSession().salvaDefinitivo(context.getUserContext(), (OggettoBulk)incarico);

			procedura = (Incarichi_proceduraBulk)createComponentSession().inizializzaBulkPerModifica(context.getUserContext(), getModel());

			int contaDef=0, contaInv=0;
			for (Iterator i=procedura.getIncarichi_repertorioColl().iterator();i.hasNext();) {
				Incarichi_repertorioBulk incaricoNew = (Incarichi_repertorioBulk)i.next();
				if (incaricoNew.isIncaricoDefinitivo()||incaricoNew.isIncaricoChiuso())
					contaDef = contaDef+1;
				if (incaricoNew.isIncaricoInviatoCorteConti())
					contaInv = contaInv+1;
			}

			if (procedura.getNr_contratti().compareTo(contaDef+contaInv)==-1)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione! Risultano collegati alla procedura un numero di contratti definitivi " + contaDef + " superiore a quello consentito.");

			setModel(context, procedura);
		}
		catch(Exception e)
		{
			throw handleException(e);
		}
	}
	protected void resetTabs(ActionContext context) {
		setTab("tab","tabTestata");
		setTab("tabIncarichiProceduraAnno","tabIncarichiProceduraAnnoImporti");
		setTab("tabProceduraContratto","tabProceduraContrattoTestata");
		setTab("tabCompensiIncaricoAnno","tabCompensiIncaricoAnnoImporti");
		setTab("tabProceduraRapporti","tabProceduraRapportiAnno");
	}
	public boolean isIncaricoUtilizzato(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			/*Ricarico l'oggetto nel caso in cui qualcuno abbia, da un'altra sessione, caricato compensi sull'incarico*/
			Incarichi_proceduraBulk incDB = (Incarichi_proceduraBulk)createComponentSession().inizializzaBulkPerModifica(context.getUserContext(), getModel());
			return incDB.getImporto_utilizzato().compareTo(Utility.ZERO)==1;
		}
		catch(Exception e)
		{
			throw handleException(e);
		}
	}
	public void chiudiProceduraIncarico(ActionContext actioncontext) throws BusinessProcessException {
		int crudStatus = getModel().getCrudStatus();
		try
		{
			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) getModel();
			if ( procedura.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_INCARICO)==1 )
				setModel(actioncontext, ((IncarichiProceduraComponentSession)createComponentSession()).chiudiProceduraIncaricoPubblicata(actioncontext.getUserContext(),getModel()));
			else // stato = STORNATA
				throw new it.cnr.jada.comp.ApplicationException( "Lo stato della procedura di conferimento incarico non ne consente lo storno");
		} catch(Exception e) {
			getModel().setCrudStatus(crudStatus);
			throw handleException(e);
		}
	}
	public boolean isAnnullaDefinitivoButtonHidden() {
		return Optional.ofNullable(getModel())
				.filter(Incarichi_proceduraBulk.class::isInstance)
				.map(Incarichi_proceduraBulk.class::cast)
				.map(incarichi_proceduraBulk -> {
					return !incarichi_proceduraBulk.isProceduraDefinitiva() ||
                        !isUoEnte() ||
                        Optional.ofNullable(incarichi_proceduraBulk.getNr_contratti())
                                .map(numeroContratti -> numeroContratti.compareTo(1) == 1)
                        .orElse(false);
				})
				.orElse(true);
	}
	public boolean isAnnullaDefinitivoButtonEnabled() {
		return !isAnnullaDefinitivoButtonHidden() &&
				((Incarichi_proceduraBulk) getModel()).isProceduraDefinitiva();
	}
	public void annullaDefinitivo(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			Incarichi_proceduraBulk  procedura = (Incarichi_proceduraBulk)getModel();

			if (procedura.getIncarichi_repertorioValidiColl().size()==1)
				Utility.createIncarichiRepertorioComponentSession().annullaDefinitivo(context.getUserContext(), (OggettoBulk)procedura.getIncarichi_repertorioValidiColl().get(0));

			setModel(context, (Incarichi_proceduraBulk)createComponentSession().inizializzaBulkPerModifica(context.getUserContext(), getModel()));
//			((IncarichiProceduraComponentSession)createComponentSession()).archiviaAllegati(context.getUserContext(), (Incarichi_proceduraBulk)getModel());			
		}
		catch(Exception e)
		{
			throw handleException(e);
		}
	}
	public void annullaDefinitivoContratto(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)getIncarichiColl().getModel();

			//quindi il contratto selezionato
			if (incarico != null)
				Utility.createIncarichiRepertorioComponentSession().annullaDefinitivo(context.getUserContext(), (OggettoBulk)incarico);

			setModel(context, createComponentSession().inizializzaBulkPerModifica(context.getUserContext(), getModel()));
		}
		catch(Exception e)
		{
			throw handleException(e);
		}
	}
	public boolean isDeleteButtonEnabled() {
		Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) getModel();
		return super.isDeleteButtonEnabled() &&
				Optional.ofNullable(procedura)
					.map(incarichi_proceduraBulk -> Optional.ofNullable(incarichi_proceduraBulk.getFaseProcesso()).orElse(-1))
					.map(faseProcesso -> faseProcesso.compareTo(Incarichi_proceduraBulk.FASE_PUBBLICAZIONE)==-1)
					.orElse(false);
	}
	public boolean isDeleteButtonHidden() {
		return super.isDeleteButtonHidden() || isCreaERiportaNuovoIncarico();
	}
	public boolean isNewButtonHidden() {
		return super.isNewButtonHidden() || isCreaERiportaNuovoIncarico();
	}
	public boolean isSearchButtonHidden() {
		return super.isSearchButtonHidden() || isCreaERiportaNuovoIncarico();
	}
	public boolean isFreeSearchButtonHidden() {
		return super.isFreeSearchButtonHidden() || isCreaERiportaNuovoIncarico();
	}
	public boolean isChiudiButtonEnabled() {
		Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) getModel();
		return super.isDeleteButtonEnabled()&&
				!procedura.isProceduraScaduta()&&
				(!procedura.isProceduraDefinitiva()||(isUoEnte()||isUtenteAbilitatoFunzioniIncarichi()));
	}
	public boolean isChiudiButtonHidden() {
		return !isEditing()||!isContrattoEnable()||
				isDeleteButtonEnabled();
	}
	public boolean isVerificaFormatoBando() {
		return verificaFormatoBando;
	}

	public void setVerificaFormatoBando(boolean verificaFormatoBando) {
		this.verificaFormatoBando = verificaFormatoBando;
	}
	public OggettoBulk initializeModelForSearch(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		if (this.isBorseStudioBP() || this.isAssegniRicercaBP()) {
			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)oggettobulk;

			//metodo per riempire immediatamente la Procedura Amministrativa
			try	{
				RemoteIterator ri = this.find(actioncontext,null,new Procedure_amministrativeBulk("INC3"),procedura,"procedura_amministrativa");
				ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actioncontext, ri);
				if (ri != null && ri.countElements() == 1)
					procedura = this.initializeProcedura_amministrativa(actioncontext, procedura, (Procedure_amministrativeBulk)ri.nextElement());
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(actioncontext, ri);
			}catch(java.rmi.RemoteException ex){
				throw handleException(ex);
			}
		}
		return super.initializeModelForSearch(actioncontext, oggettobulk);
	}
	public SimpleDetailCRUDController getCompensiAllegati() {
		return compensiAllegati;
	}
	public boolean isTabCompensiProceduraAnnoEnabled() {
		return (getModel()!=null &&
				getRipartizionePerAnno()!=null &&
				getRipartizionePerAnno().getModel() != null &&
				!((Incarichi_procedura_annoBulk)getRipartizionePerAnno().getModel()).getCompensiColl().isEmpty());
	}
	public boolean isTabCompensiIncaricoAnnoEnabled() {
		return (getModel()!=null &&
				getRipartizioneIncarichiPerAnno()!=null &&
				getRipartizioneIncarichiPerAnno().getModel() != null &&
				!((Incarichi_repertorio_annoBulk)getRipartizioneIncarichiPerAnno().getModel()).getCompensiColl().isEmpty());
	}
	public boolean isUtenteAbilitatoFunzioniIncarichi() {
		return utenteAbilitatoFunzioniIncarichi;
	}
	private void setUtenteAbilitatoFunzioniIncarichi(boolean utenteAbilitatoFunzioniIncarichi) {
		this.utenteAbilitatoFunzioniIncarichi = utenteAbilitatoFunzioniIncarichi;
	}
	public SimpleDetailCRUDController getIncarichiColl() {
		return incarichiColl;
	}
	public void create(ActionContext actioncontext) throws BusinessProcessException {
		super.create(actioncontext);
		try {
			if (isUoEnte()){
				Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)getModel();
				StringBuffer messaggioAnno = new StringBuffer();
				for (Iterator<Incarichi_procedura_annoBulk> i = ((Incarichi_proceduraBulk)getModel()).getIncarichi_procedura_annoColl().iterator();i.hasNext();){
					Incarichi_procedura_annoBulk procAnno = i.next();
					Repertorio_limitiBulk repertorio = Utility.createRepertorioLimitiComponentSession().getRepertorioLimiti(actioncontext.getUserContext(),
							procAnno.getEsercizio_limite(),
							procedura.getCd_tipo_incarico(),
							procedura.getCd_tipo_attivita(),
							procedura.getTipo_natura());
					if (repertorio.getImporto_residuo().compareTo(Utility.ZERO)==-1)
						messaggioAnno.append("\n --> Esercizio: "+procAnno.getEsercizio_limite()+" - Limite superato di: " + new it.cnr.contab.util.EuroFormat().format(repertorio.getImporto_residuo().abs()));
				}
				if (messaggioAnno.length() != 0) {
					setMessage("Salvataggio eseguito in modo corretto.\n\n"+
							"\nI limiti dei seguenti esercizi risultano essere superati: "+messaggioAnno);
				}
			}
		}
		catch(Exception e)
		{
			throw handleException(e);
		}
	}
	public void update(ActionContext actioncontext) throws BusinessProcessException {
		super.update(actioncontext);
		try {
			if (isUoEnte()){
				Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)getModel();
				StringBuffer messaggioAnno = new StringBuffer();
				for (Iterator<Incarichi_procedura_annoBulk> i = ((Incarichi_proceduraBulk)getModel()).getIncarichi_procedura_annoColl().iterator();i.hasNext();){
					Incarichi_procedura_annoBulk procAnno = i.next();
					Repertorio_limitiBulk repertorio = Utility.createRepertorioLimitiComponentSession().getRepertorioLimiti(actioncontext.getUserContext(),
							procAnno.getEsercizio_limite(),
							procedura.getCd_tipo_incarico(),
							procedura.getCd_tipo_attivita(),
							procedura.getTipo_natura());
					if (repertorio.getImporto_residuo().compareTo(Utility.ZERO)==-1)
						messaggioAnno.append("\n --> Esercizio: "+procAnno.getEsercizio_limite()+" - Limite superato di: " + new it.cnr.contab.util.EuroFormat().format(repertorio.getImporto_residuo().abs()));
				}
				if (messaggioAnno.length() != 0) {
					setMessage("Salvataggio eseguito in modo corretto.\n\n"+
							"I limiti dei seguenti esercizi risultano essere superati: \n"+messaggioAnno);
				}
			}
		}
		catch(Exception e)
		{
			throw handleException(e);
		}
	}
	public SimpleDetailCRUDController getCompensiAllegatiIncarico() {
		return compensiAllegatiIncarico;
	}
	public final SimpleDetailCRUDController getRipartizioneIncarichiPerAnno() {
		return ripartizioneIncarichiPerAnno;
	}
	public SimpleDetailCRUDController getCrudIncarichiArchivioAllegati() {
		return crudIncarichiArchivioAllegati;
	}
	public void setCrudIncarichiArchivioAllegati(SimpleDetailCRUDController controller) {
		crudIncarichiArchivioAllegati = controller;
	}
	private void allineaDatiProceduraIncarichi(UserContext userContext, Incarichi_proceduraBulk procedura) throws it.cnr.jada.action.BusinessProcessException {
		if ( procedura.getIncarichi_repertorioColl().isEmpty() )
			return;

		if (procedura.getNr_contratti().compareTo(1)==0){
			if (procedura.getIncarichi_repertorioColl().size()>1)
				throw new BusinessProcessException( "Attenzione! Risultano collegati più contratti alla procedura che ne prevede uno solo.");

			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)procedura.getIncarichi_repertorioColl().get(0);

			for (Iterator<Incarichi_repertorio_annoBulk> i=incarico.getIncarichi_repertorio_annoColl().iterator();i.hasNext();){
				Incarichi_repertorio_annoBulk incarico_anno = i.next();

				if (procedura.getNr_contratti_iniziale()==null || procedura.getNr_contratti_iniziale().compareTo(1)!=1){
					incarico_anno.setImporto_iniziale(Utility.ZERO);
				}
				incarico_anno.setImporto_complessivo(Utility.ZERO);
				incarico_anno.setToBeUpdated();
			}

			boolean aggiornato = false;
			for (Iterator<Incarichi_procedura_annoBulk> i=procedura.getIncarichi_procedura_annoColl().iterator();i.hasNext();){
				Incarichi_procedura_annoBulk procedura_anno = i.next();
				aggiornato = false;
				for (Iterator<Incarichi_repertorio_annoBulk> y=incarico.getIncarichi_repertorio_annoColl().iterator();y.hasNext();){
					Incarichi_repertorio_annoBulk incarico_anno = y.next();
					if (incarico_anno.getEsercizio_limite()!=null && incarico_anno.getEsercizio_limite().equals(procedura_anno.getEsercizio_limite())){
						if (procedura.getNr_contratti_iniziale()==null || procedura.getNr_contratti_iniziale().compareTo(1)!=1){
							incarico_anno.setImporto_iniziale(procedura_anno.getImporto_iniziale());
						}
						incarico_anno.setImporto_complessivo(procedura_anno.getImporto_complessivo());
						incarico_anno.setToBeUpdated();
						aggiornato = true;
						break;
					}
				}
				if (!aggiornato){
					Incarichi_repertorio_annoBulk incarico_anno = new Incarichi_repertorio_annoBulk();
					incarico.addToIncarichi_repertorio_annoColl(incarico_anno);
					incarico_anno.setEsercizio_limite(procedura_anno.getEsercizio_limite());
					if (procedura.getNr_contratti_iniziale()==null || procedura.getNr_contratti_iniziale().compareTo(1)!=1){
						incarico_anno.setImporto_iniziale(procedura_anno.getImporto_iniziale());
					}
					incarico_anno.setImporto_complessivo(procedura_anno.getImporto_complessivo());
					incarico_anno.setToBeCreated();
				}
			}
			List<Incarichi_repertorio_annoBulk> incaricoAnnoListToBeDeleted = new ArrayList<Incarichi_repertorio_annoBulk>();
			for (Iterator i=incarico.getIncarichi_repertorio_annoColl().iterator();i.hasNext();){
				Incarichi_repertorio_annoBulk incarico_anno = (Incarichi_repertorio_annoBulk)i.next();
				if (
						Optional.ofNullable(incarico_anno)
							.flatMap(incarichi_repertorio_annoBulk -> Optional.ofNullable(incarichi_repertorio_annoBulk.getImporto_iniziale()))
							.orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) == 0 &&
						Optional.ofNullable(incarico_anno)
							.flatMap(incarichi_repertorio_annoBulk -> Optional.ofNullable(incarichi_repertorio_annoBulk.getImporto_complessivo()))
							.orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) == 0
				) {
					boolean trovatoAnno=false;
					for (Iterator<Incarichi_procedura_annoBulk> y=procedura.getIncarichi_procedura_annoColl().iterator();y.hasNext();) {
						Incarichi_procedura_annoBulk proceduraAnno = y.next();
						if (incarico_anno.getEsercizio_limite()!=null &&
								proceduraAnno.getEsercizio_limite().compareTo(incarico_anno.getEsercizio_limite())==0)
							trovatoAnno=true;
					}
					if (!trovatoAnno) {
						incarico_anno.setToBeDeleted();
						incaricoAnnoListToBeDeleted.add(incarico_anno);
					}
				}
			}
			for (Incarichi_repertorio_annoBulk incaricoAnnoToBeDelete : incaricoAnnoListToBeDeleted)
				incarico.removeFromIncarichi_repertorio_annoColl(incarico.getIncarichi_repertorio_annoColl().indexOf(incaricoAnnoToBeDelete));

			if (incarico.getIncarichi_repertorio_annoColl().isEmpty()) {
				Incarichi_repertorio_annoBulk incarico_anno = new Incarichi_repertorio_annoBulk();
				incarico.addToIncarichi_repertorio_annoColl(incarico_anno);
				incarico_anno.setEsercizio_limite(procedura.getEsercizio());
				incarico_anno.setImporto_iniziale(Utility.ZERO);
				incarico_anno.setImporto_complessivo(Utility.ZERO);
				incarico_anno.setToBeCreated();
			}
			//AGGIORNO ANCHE GLI IMPORTI SULLA TESTATA DELL'INCARICO
			incarico.setImporto_lordo(procedura.getImporto_lordo());
			incarico.setImporto_complessivo(procedura.getImporto_complessivo());
			incarico.setToBeUpdated();
		}
		else if (procedura.getNr_contratti().compareTo(1)==1)
		{
			BulkList procAnnoNewList = new BulkList();
			boolean  trovato = false;
			boolean  allContrattiInseriti = procedura.getNr_contratti().compareTo(procedura.getIncarichi_repertorioValidiColl().size())==0;
			int nrContrattidaInserire = procedura.getNr_contratti() - procedura.getIncarichi_repertorioValidiColl().size();

			/*
			 * Carico una tabella di appoggio in cui, leggendo gli importi per anno degli incarichi, li sommo e
			 * ricostruisco la struttura di ripartizione degli importi per anno a livello di procedura
			 */
			for (Iterator<Incarichi_repertorioBulk> i=procedura.getIncarichi_repertorioColl().iterator();i.hasNext();){
				Incarichi_repertorioBulk incarico = i.next();
				for (Iterator<Incarichi_repertorio_annoBulk> y=incarico.getIncarichi_repertorio_annoColl().iterator();y.hasNext();){
					Incarichi_repertorio_annoBulk incarico_anno = y.next();
					trovato = false;
					for (Iterator<Incarichi_procedura_annoBulk> z=procAnnoNewList.iterator();z.hasNext();){
						Incarichi_procedura_annoBulk proceduraAnnoAgg = z.next();
						if (proceduraAnnoAgg.getEsercizio_limite().equals(incarico_anno.getEsercizio_limite())){
							proceduraAnnoAgg.setImporto_complessivo(proceduraAnnoAgg.getImporto_complessivo().add(incarico_anno.getImporto_complessivo()));
							trovato = true;
							break;
						}
					}
					if (!trovato){
						Incarichi_procedura_annoBulk proceduraAnnoAgg = new Incarichi_procedura_annoBulk();
						proceduraAnnoAgg.setEsercizio_limite(incarico_anno.getEsercizio_limite());
						proceduraAnnoAgg.setImporto_complessivo(incarico_anno.getImporto_complessivo());
						procAnnoNewList.add(proceduraAnnoAgg);
					}
				}
			}

			/*
			 * Nel caso in cui non tutti i contratti sono stati inseriti carico le quote per anno dei contratti ancora
			 * da inserire calcolando l'importo per anno del singolo contratto (importo iniziale/nr contratti) e moltiplicandolo per
			 * i nr contratti ancora da attribuire.
			 */
			for (Iterator<Incarichi_procedura_annoBulk> i=procedura.getIncarichi_procedura_annoColl().iterator();i.hasNext();){
				Incarichi_procedura_annoBulk proceduraAnno = i.next();
				if ( allContrattiInseriti ) {
					proceduraAnno.setImporto_complessivo(Utility.ZERO);
					if (procedura.getDt_pubblicazione()==null)
						proceduraAnno.setImporto_iniziale(Utility.ZERO);
					proceduraAnno.setToBeUpdated();
				}
				else if (proceduraAnno.getImporto_iniziale().compareTo(BigDecimal.ZERO)==1){
					trovato = false;
					for (Iterator<Incarichi_procedura_annoBulk> y=procAnnoNewList.iterator();y.hasNext();){
						Incarichi_procedura_annoBulk proceduraAnnoAgg = y.next();
						if (proceduraAnnoAgg.getEsercizio_limite().equals(proceduraAnno.getEsercizio_limite())){
							proceduraAnnoAgg.setImporto_complessivo(proceduraAnnoAgg.getImporto_complessivo().add(proceduraAnno.getImporto_iniziale().multiply(new BigDecimal(nrContrattidaInserire)).divide(new BigDecimal(procedura.getNr_contratti()),2,BigDecimal.ROUND_HALF_EVEN)));
							trovato=true;
							break;
						}
					}
					if (!trovato){
						Incarichi_procedura_annoBulk proceduraAnnoAgg = new Incarichi_procedura_annoBulk();
						proceduraAnnoAgg.setEsercizio_limite(proceduraAnno.getEsercizio_limite());
						proceduraAnnoAgg.setImporto_complessivo(proceduraAnno.getImporto_iniziale().multiply(new BigDecimal(nrContrattidaInserire)).divide(new BigDecimal(procedura.getNr_contratti()),2,BigDecimal.ROUND_HALF_EVEN));
						procAnnoNewList.add(proceduraAnnoAgg);
					}
				}
			}

			/*
			 * Scarico la tabella ricostruita in quella effettiva.
			 */
			for (Iterator<Incarichi_procedura_annoBulk> i=procAnnoNewList.iterator();i.hasNext();){
				Incarichi_procedura_annoBulk proceduraAnnoAgg = i.next();
				trovato = false;
				for (Iterator<Incarichi_procedura_annoBulk> y=procedura.getIncarichi_procedura_annoColl().iterator();y.hasNext();){
					Incarichi_procedura_annoBulk proceduraAnno = y.next();

					if (proceduraAnno.getEsercizio_limite().equals(proceduraAnnoAgg.getEsercizio_limite())){
						if (proceduraAnno.getImporto_complessivo().compareTo(proceduraAnnoAgg.getImporto_complessivo())!=0) {
							proceduraAnno.setImporto_complessivo(proceduraAnnoAgg.getImporto_complessivo());
							proceduraAnno.setToBeUpdated();
						}
						trovato = true;
						break;
					}
				}

				if (!trovato){
					Incarichi_procedura_annoBulk proceduraAnno = new Incarichi_procedura_annoBulk();
					procedura.addToIncarichi_procedura_annoColl(proceduraAnno);
					proceduraAnno.setEsercizio_limite(proceduraAnnoAgg.getEsercizio_limite());
					proceduraAnno.setImporto_iniziale(Utility.ZERO);
					proceduraAnno.setImporto_complessivo(proceduraAnnoAgg.getImporto_complessivo());
					proceduraAnno.caricaAnniList(userContext);
					proceduraAnno.setToBeCreated();
				}
			}
			/*
			 * Azzero gli importi per i record eliminati.
			 */
			BulkList deleteList = new BulkList();
			for (Iterator<Incarichi_procedura_annoBulk> i=procedura.getIncarichi_procedura_annoColl().iterator();i.hasNext();){
				Incarichi_procedura_annoBulk proceduraAnno = i.next();

				trovato = false;
				for (Iterator<Incarichi_procedura_annoBulk> y=procAnnoNewList.iterator();y.hasNext();){
					if ((y.next()).getEsercizio_limite().equals(proceduraAnno.getEsercizio_limite())){
						trovato=true;
						break;
					}
				}
				if (!trovato) {
					proceduraAnno.setImporto_complessivo(Utility.ZERO);
					proceduraAnno.setToBeUpdated();
					if (proceduraAnno.getImporto_iniziale().compareTo(Utility.ZERO)==0) {
						proceduraAnno.setToBeDeleted();
						deleteList.add(proceduraAnno);
					}
				}
			}
			for (Iterator i=deleteList.iterator();i.hasNext();){
				procedura.removeFromIncarichi_procedura_annoColl(procedura.getIncarichi_procedura_annoColl().indexOf(i.next()));
			}
		}
	}
	public void concludiProceduraIncarico(ActionContext actioncontext) throws BusinessProcessException {
		int crudStatus = getModel().getCrudStatus();
		try
		{
			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) getModel();
			if ( procedura.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_INCARICO)==1 )
				setModel(actioncontext, ((IncarichiProceduraComponentSession)createComponentSession()).concludiProceduraIncaricoPubblicata(actioncontext.getUserContext(),getModel()));
			else // stato = STORNATA
				throw new it.cnr.jada.comp.ApplicationException( "Lo stato dell'incarico non ne consente la conclusione");
		} catch(Exception e) {
			getModel().setCrudStatus(crudStatus);
			throw handleException(e);
		}
	}
	public boolean isConcludiProceduraButtonHidden() {
		Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) getModel();
		return !(isUoEnte()||isUtenteAbilitatoFunzioniIncarichi()) ||
				isDeleteButtonEnabled() ||
				procedura.isProceduraAnnullata() ||
				procedura.isProceduraMultiIncarico() ||
				procedura.getNr_contratti()==null ||
				procedura.getNr_contratti().compareTo(procedura.getIncarichi_repertorioValidiColl().size())==0 ||
				procedura.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_PUBBLICAZIONE)!=1 ||
				procedura.isProceduraScaduta();
	}

	public void concludiMonoIncarico(ActionContext actioncontext) throws BusinessProcessException {
		int crudStatus = getModel().getCrudStatus();
		try
		{
			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) getModel();

			if (procedura.getNr_contratti().compareTo(1)!=0)
				throw new it.cnr.jada.comp.ApplicationException( "Funzionalità consentita solo per procedure di conferimento mono-incarico.");
			else if (procedura.getIncarichi_repertorioValidiColl().size()!=1)
				throw new it.cnr.jada.comp.ApplicationException( "Funzionalità consentita solo per procedure di conferimento a cui risulta associato un solo incarico.");

			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)procedura.getIncarichi_repertorioValidiColl().get(0);
			if (!incarico.isIncaricoDefinitivo() && !incarico.isIncaricoInviatoCorteConti() &&
					!(incarico.isIncaricoProvvisorio() && incarico.getFl_inviato_corte_conti() && incarico.getEsito_corte_conti()!=null))
				throw new it.cnr.jada.comp.ApplicationException( "Funzionalità consentita solo per procedura di conferimento a cui risulta associato un solo incarico con stato \"Definitivo\" o \"Inviato alla Corte dei Conti\".");

			Utility.createIncarichiRepertorioComponentSession().chiudiIncaricoPubblicato(actioncontext.getUserContext(), incarico);
			procedura = (Incarichi_proceduraBulk)((IncarichiProceduraComponentSession)createComponentSession()).chiudiProceduraIncaricoPubblicata(actioncontext.getUserContext(), procedura);

			setModel(actioncontext, procedura);
		} catch(Exception e) {
			getModel().setCrudStatus(crudStatus);
			throw handleException(e);
		}
	}

	public boolean isConcludiMonoIncaricoButtonHidden() {
		Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) getModel();
		return !(isUoEnte()/*||isUtenteAbilitatoFunzioniIncarichi()*/) ||
				isDeleteButtonEnabled() ||
				procedura.isProceduraAnnullata() ||
				procedura.isProceduraChiusa() ||
				procedura.isProceduraInviataCorteConti() ||
				procedura.getIncarichi_repertorioColl().isEmpty() ||
				procedura.getNr_contratti() == null ||
				procedura.getNr_contratti().compareTo(1)==1 ||
				(procedura.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_INCARICO)==1 &&
						procedura.isProceduraScaduta());
	}

	public void concludiIncarico(ActionContext actioncontext) throws BusinessProcessException {
		int crudStatus = getModel().getCrudStatus();
		try {
			Incarichi_proceduraBulk  procedura = (Incarichi_proceduraBulk)getModel();
			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)getIncarichiColl().getModel();

			if (procedura.getNr_contratti().compareTo(1)!=1)
				throw new it.cnr.jada.comp.ApplicationException( "Funzionalità consentita solo per procedure di conferimento multi-incarico.");
			else if (procedura.getIncarichi_repertorioValidiColl().size()<=1)
				throw new it.cnr.jada.comp.ApplicationException( "Funzionalità consentita solo per procedure di conferimento a cui risultano associati più incarichi.");

			procedura = (Incarichi_proceduraBulk)((IncarichiProceduraComponentSession)createComponentSession()).concludiIncaricoPubblicato(actioncontext.getUserContext(), procedura, incarico);
			procedura = (Incarichi_proceduraBulk)createComponentSession().inizializzaBulkPerModifica(actioncontext.getUserContext(), procedura);

			setModel(actioncontext, procedura);
		} catch(Exception e) {
			getModel().setCrudStatus(crudStatus);
			throw handleException(e);
		}
	}
	public SimpleDetailCRUDController getCrudIncarichiVariazioni() {
		return crudIncarichiVariazioni;
	}
	public void setCrudIncarichiVariazioni(SimpleDetailCRUDController controller) {
		crudIncarichiVariazioni = controller;
	}
	private Date getDtLimiteVariazione() {
		return dtLimiteVariazione;
	}
	private void setDtLimiteVariazione(Date data) {
		dtLimiteVariazione = data;
	}
	public String getFieldTipoVariazione() {
		if (this.getCrudIncarichiVariazioni().getModel()!=null) {
			Incarichi_repertorio_varBulk incaricoVar = (Incarichi_repertorio_varBulk)this.getCrudIncarichiVariazioni().getModel();
			if (isUoEnte() || incaricoVar.isNotNew())
				return "tipo_variazioneForEnte";
			if (incaricoVar.getIncarichi_repertorio()!=null &&
					incaricoVar.getIncarichi_repertorio().getTipo_rapporto()!=null &&
					incaricoVar.getIncarichi_repertorio().getTipo_rapporto().getFl_inquadramento().booleanValue() &&
					getDtLimiteVariazione()!=null &&
					incaricoVar.getIncarichi_repertorio().getDt_stipula()!=null &&
					getDtLimiteVariazione().after(incaricoVar.getIncarichi_repertorio().getDt_stipula()) &&
					incaricoVar.getIncarichi_repertorio().getDt_fine_validita()!=null &&
					!getDtLimiteVariazione().after(incaricoVar.getIncarichi_repertorio().getDt_fine_validita()))
				return "tipo_variazione"+(isAssegniRicercaBP()?"ForAssegniRicerca":"");
		}
		return "tipo_variazioneMinima"+(isAssegniRicercaBP()?"ForAssegniRicerca":"");
	}
	public void validaDataIntegrazioneIncarico(ActionContext actioncontext, Incarichi_repertorio_varBulk incaricoVar) throws ValidationException {
		if (incaricoVar!=null && incaricoVar.isVariazioneIntegrazioneIncarico()) {
			if (incaricoVar.getIncarichi_repertorio()!=null && incaricoVar.getDt_variazione()!=null){
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
				if (incaricoVar.getIncarichi_repertorio().getDt_stipula()!=null && incaricoVar.getIncarichi_repertorio().getDt_stipula().after(incaricoVar.getDt_variazione()))
					throw new ValidationException( "La data di stipula della variazione deve essere successiva o uguale alla data di stipula del contratto originale (" + sdf.format(incaricoVar.getIncarichi_repertorio().getDt_stipula())+").");
				if (getDtLimiteVariazione()!=null && getDtLimiteVariazione().after(incaricoVar.getDt_variazione()))
					throw new ValidationException( "La data di stipula della variazione deve essere successiva o uguale al " + sdf.format(getDtLimiteVariazione())+".");
				if (incaricoVar.getIncarichi_repertorio().getDt_fine_validita()!=null) {
					if (incaricoVar.getIncarichi_repertorio().getDt_fine_validita().before(incaricoVar.getDt_variazione()))
						throw new ValidationException( "La data di stipula della variazione deve essere inferiore o uguale alla data di fine validita' dell'incarico (" + sdf.format(incaricoVar.getIncarichi_repertorio().getDt_fine_validita())+").");
				}
			}
		}
	}
	public void salvaDefinitivoVariazioneContratto(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		Incarichi_repertorio_varBulk incaricoVar = (Incarichi_repertorio_varBulk)getCrudIncarichiVariazioni().getModel();
		try {
			if (incaricoVar.isProvvisorio()) {
				if (incaricoVar.isVariazioneIntegrazioneIncarico()) {
					if (incaricoVar.getDt_variazione()==null)
						throw new it.cnr.jada.comp.ApplicationException("Non \350 possibile effettuare l'operazione perchè non risulta inserita la data di stipula della variazione del contratto.");
					else if (!incaricoVar.getIncarichi_repertorio().getIncarichi_procedura().isUtenteCollegatoSuperUtente()){
						Incarichi_parametriBulk parametri = ((IncarichiProceduraComponentSession)createComponentSession()).getIncarichiParametri(context.getUserContext(), incaricoVar.getIncarichi_repertorio().getIncarichi_procedura());

						if ((parametri==null || parametri.getLimite_dt_stipula()==null) && DateUtils.daysBetweenDates(incaricoVar.getDt_variazione(), EJBCommonServices.getServerDate())>5)
							throw new it.cnr.jada.comp.ApplicationException("Non \350 possibile effettuare l'operazione perchè dalla data di stipula della variazione del contratto risultano trascorsi piu' di 5 giorni.");
						else if (parametri!=null && parametri.getLimite_dt_stipula()!=null && parametri.getLimite_dt_stipula().equals("Y")) {
							Integer limite = new Integer(0);
							if (parametri.getGiorni_limite_dt_stipula()!=null)
								limite = parametri.getGiorni_limite_dt_stipula();
							if (DateUtils.daysBetweenDates(incaricoVar.getDt_variazione(), EJBCommonServices.getServerDate())>limite.intValue())
								throw new it.cnr.jada.comp.ApplicationException("Non \350 possibile effettuare l'operazione perchè dalla data di stipula della variazione del contratto risultano trascorsi piu' di "+limite.toString()+" giorni.");
						}
					}
				}
				incaricoVar.setStato(Incarichi_archivioBulk.STATO_DEFINITIVO);
				incaricoVar.setToBeUpdated();
			}
			save(context);
		}
		catch(Exception e)
		{
			incaricoVar.setStato(Incarichi_archivioBulk.STATO_PROVVISORIO);
			throw handleException(e);
		}
	}
	public boolean isSendEmailCdsButtonHidden()	{
		return true;
//		return !(isUoEnte()) && !isUtenteAbilitatoInvioMail();
	}
	public boolean isSbloccaProceduraButtonHidden()	{
		return true;
//		return !(isUoEnte()) && !isUtenteAbilitatoSbloccoProcedura();
	}
	public boolean isMergeCMISButtonHidden()	{
		return !isUoEnte() || !isSuperUtente() ||
				!isEditing() ||getModel()==null;
	}
	public Incarichi_parametriBulk getIncarichiParametri(UserContext userContext, Incarichi_proceduraBulk procedura) throws it.cnr.jada.action.BusinessProcessException {
		if (!procedura.isEqualsFieldParameter()) {
			try{
				procedura.initIncarichiParametri(((IncarichiProceduraComponentSession)createComponentSession()).getIncarichiParametri(userContext, procedura));
			} catch(Exception e) {
				throw handleException(e);
			}
		}
		return procedura.getIncarichiParametri();
	}
	public Incarichi_parametriBulk getIncarichiParametri() throws it.cnr.jada.action.BusinessProcessException {
		return ((Incarichi_proceduraBulk)this.getModel()).getIncarichiParametri();
	}

	public SimpleDetailCRUDController getCrudAssUO() {
		return crudAssUO;
	}
	public void setCrudAssUO(SimpleDetailCRUDController controller) {
		crudAssUO = controller;
	}

	public SimpleDetailCRUDController getCrudAssUODisponibili() {
		return crudAssUODisponibili;
	}
	public void setCrudAssUODisponibili(SimpleDetailCRUDController controller) {
		crudAssUODisponibili = controller;
	}
	public void controllaCancellazioneAssociazioneUo(ActionContext context,Ass_incarico_uoBulk ass_incarico_uo) throws it.cnr.jada.action.BusinessProcessException{
		try {
			Utility.createIncarichiRepertorioComponentSession().controllaCancellazioneAssociazioneUo(context.getUserContext(), ass_incarico_uo);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw handleException(ex);
		}catch(java.rmi.RemoteException ex){
			throw handleException(ex);
		}
	}
	public String[][] getTabsMultiIncarico() {
		TreeMap<Integer, String[]> hash = new TreeMap<Integer, String[]>();
		int i=0;

		hash.put(i++, new String[]{"tabProceduraContrattoTestata", "Terzo", "/incarichi00/tab_incarichi_procedura_contratto_testata.jsp" });

		Incarichi_repertorioBulk incarico=null;
		if (getIncarichiColl()!=null)
			incarico = (Incarichi_repertorioBulk)getIncarichiColl().getModel();

		if (incarico==null || incarico.isUoAbilitata(getUoSrivania()) || incarico.getIncarichi_procedura().isUtenteCollegatoUoEnte() ||
				incarico.getIncarichi_procedura().isUtenteCollegatoSuperUtente()) {
			hash.put(i++, new String[]{"tabProceduraContrattoAnno", "Importo per anno", "/incarichi00/tab_incarichi_procedura_contratto_anno.jsp" });
			hash.put(i++, new String[]{"tabProceduraContrattoAllegati", "Allegati", "/incarichi00/tab_incarichi_procedura_contratto_allegati.jsp" });

			if (incarico!=null) {
				if (incarico.getIncarichi_procedura().isDichiarazioneContraenteRequired())
					hash.put(i++, new String[]{"tabProceduraContrattoRapporti","Dichiarazione Contraente","/incarichi00/tab_incarichi_procedura_rapporti.jsp" });
				if (incarico.isIncaricoDefinitivo()||!incarico.getIncarichi_repertorio_varColl().isEmpty())
					hash.put(i++, new String[]{"tabProceduraContrattoVariazioni","Variazioni","/incarichi00/tab_incarichi_procedura_variazioni.jsp" });

				if (incarico.getIncarichi_procedura().isUtenteCollegatoSuperUtente())
					hash.put(i++, new String[]{"tabProceduraContrattoAssUo","UO","/incarichi00/tab_incarichi_procedura_ass_uo.jsp" });
			}
		}

		String[][] tabs = new String[i][3];
		for (int j = 0; j < i; j++) {
			tabs[j]=new String[]{hash.get(j)[0],hash.get(j)[1],hash.get(j)[2]};
		}
		return tabs;
	}

	public String[][] getTabsDichiarazioneContraente() {
		TreeMap<Integer, String[]> hash = new TreeMap<Integer, String[]>();
		int i=0;

		hash.put(i++, new String[]{"tabProceduraRapportiAnno", "Info", "/incarichi00/tab_incarichi_procedura_rapporti_anno.jsp" });
		hash.put(i++, new String[]{"tabProceduraRapportiDett", "Dettaglio Rapporti", "/incarichi00/tab_incarichi_procedura_rapporti_dett.jsp" });

		String[][] tabs = new String[i][3];
		for (int j = 0; j < i; j++) {
			tabs[j]=new String[]{hash.get(j)[0],hash.get(j)[1],hash.get(j)[2]};
		}
		return tabs;
	}

	public SimpleDetailCRUDController getCrudNote() {
		return crudNote;
	}

	public void setCrudNote(SimpleDetailCRUDController crudNote) {
		this.crudNote = crudNote;
	}
	public SimpleDetailCRUDController getIncarichiRappColl() {
		return incarichiRappColl;
	}
	public void setIncarichiRappColl(SimpleDetailCRUDController incarichiRappColl) {
		this.incarichiRappColl = incarichiRappColl;
	}
	public SimpleDetailCRUDController getIncarichiRappDetColl() {
		return incarichiRappDetColl;
	}
	public void setIncarichiRappDetColl(SimpleDetailCRUDController incarichiRappDetColl) {
		this.incarichiRappDetColl = incarichiRappDetColl;
	}
	public void validateSearchProcedura_amministrativa(ActionContext context, Incarichi_proceduraBulk procedura, Procedure_amministrativeBulk procamm) throws ValidationException {
		if (procedura != null && procedura.getNr_contratti()!=null &&procedura.getNr_contratti().compareTo(new Integer(1))==1) {
			if (procamm != null &&
					procamm.getIncarico_ric_giorni_pubbl() != null &&
					procamm.getIncarico_ric_giorni_pubbl().compareTo(new Integer(0))==0)
				throw new ValidationException( "Procedura Ammininistrativa non selezionabile per un processo che prevede l'attivazione di più contratti.");
		}

		if (procamm != null && procamm.isMeramenteOccasionaleRequired()) {
			procedura.setFl_meramente_occasionale(Boolean.TRUE);
			if (procedura.getTipo_incarico()!=null &&
					procedura.getTipo_incarico().getTipoRapporto()!=null &&
					procedura.getTipo_incarico().getTipoRapporto().getFl_inquadramento()!=null &&
					procedura.getTipo_incarico().getTipoRapporto().getFl_inquadramento().booleanValue())
				throw new ValidationException( "Procedura Ammininistrativa selezionabile solo per l'attivazione di contratti in favore di \"Collaboratori Occasionali\".");
		}
	}
	public Incarichi_proceduraBulk initializeProcedura_amministrativa(ActionContext context, Incarichi_proceduraBulk procedura, Procedure_amministrativeBulk procamm) throws BusinessProcessException {
		try {
			validateSearchProcedura_amministrativa(context, procedura, procamm);

			procedura.setProcedura_amministrativa(procamm);
			if (procamm != null && !procamm.isMeramenteOccasionaleEnabled())
				procedura.setFl_meramente_occasionale(Boolean.FALSE);
			return procedura;
		}
		catch ( Exception e )
		{
			throw handleException( e )	;
		}
	}
	public Incarichi_proceduraBulk initializeFind_tipo_incarico(ActionContext context, Incarichi_proceduraBulk procedura, Tipo_incaricoBulk tipo_incarico) throws BusinessProcessException {
		procedura.setTipo_incarico(tipo_incarico);
		changeImportoLordo(context, procedura, Utility.nvl(procedura.getImporto_lordo()));
		findTipiRapporto(context);
		if (procedura.getFl_meramente_occasionale()!=null && procedura.getFl_meramente_occasionale().booleanValue()) {
			if (!procedura.isProceduraForOccasionali())
				procedura.setFl_meramente_occasionale(Boolean.FALSE);
		}
		return procedura;
	}
	public void validateTerzo(ActionContext context, Incarichi_repertorioBulk incarico, V_terzo_per_compensoBulk terzo)  throws ValidationException {
		if(terzo != null && terzo.getAnagrafico()!=null && terzo.getAnagrafico().getTitolo_studio()==null)
			throw new ValidationException( "Terzo non selezionabile in quanto non risulta valorizzato il campo \"Titolo di studio\" in anagrafica.");
	}
	public Incarichi_repertorioBulk initializeTerzo(ActionContext context, Incarichi_repertorioBulk incarico, V_terzo_per_compensoBulk terzo)  throws BusinessProcessException {
		try {
			validateTerzo(context, incarico, terzo);
			if (incarico!=null){
				V_terzo_per_compensoBulk v_terzo = new V_terzo_per_compensoBulk();
				v_terzo.setTerzo(new TerzoBulk());

				incarico.setV_terzo(v_terzo);
				incarico.setCd_terzo(null);

				incarico.setTipiRapporto(null);
				incarico.setTipo_rapporto(null);

				Incarichi_proceduraBulk procedura = incarico.getIncarichi_procedura();
				Incarichi_repertorioBulk incaricoClone = Utility.createIncarichiRepertorioComponentSession().completaTerzo(context.getUserContext(), incarico, terzo);
				incaricoClone.setIncarichi_procedura(procedura);
				return incaricoClone;
			}
			return incarico;
		}
		catch ( Exception e )
		{
			throw handleException( e )	;
		}
	}
	public void setCreaERiportaNuovoIncarico(boolean creaERiportaNuovoIncarico) {
		this.creaERiportaNuovoIncarico = creaERiportaNuovoIncarico;
	}
	public boolean isCreaERiportaNuovoIncarico() {
		return creaERiportaNuovoIncarico;
	}
	public boolean isIncarichiProceduraBP(){
		return !isBorseStudioBP() && !isAssegniRicercaBP();
		//this.getMapping()!=null && this.getMapping().getName()!=null &&
		//(this.getMapping().getName().equals("CFGINCARICHIINCREPM")||this.getMapping().getName().equals("CRUDIncarichiProceduraBP"));
	}
	public boolean isBorseStudioBP(){
		return this.getMapping()!=null && this.getMapping().getName()!=null && this.getMapping().getName().equals("CRUDBorseStudioProceduraBP");
	}
	public boolean isAssegniRicercaBP(){
		return this.getMapping()!=null && this.getMapping().getName()!=null && this.getMapping().getName().equals("CRUDAssegniRicercaProceduraBP");
	}
	public String getFormTitle()
	{
		StringBuffer stringbuffer = null;
		if (isBorseStudioBP())
			stringbuffer = new StringBuffer("Procedura Conferimento Borse di Studio");
		else if (isAssegniRicercaBP())
			stringbuffer = new StringBuffer("Procedura Conferimento Assegni di Ricerca");
		else
			stringbuffer = new StringBuffer("Procedura Conferimento Incarichi");

		stringbuffer.append(" - ");
		switch(getStatus())
		{
			case 1: // '\001'
				stringbuffer.append("Inserimento");
				break;

			case 2: // '\002'
				stringbuffer.append("Modifica");
				break;

			case 0: // '\0'
				stringbuffer.append("Ricerca");
				break;

			case 5: // '\005'
				stringbuffer.append("Visualizza");
				break;
		}
		return stringbuffer.toString();
	}

	public void scaricaFile(ActionContext actioncontext, String cmsNodeRef) throws IOException, ServletException, ApplicationException {
		ContrattiService contrattiService = SpringUtil.getBean(ContrattiService.class);
		StorageObject storageObject = contrattiService.getStorageObjectBykey(cmsNodeRef);
		InputStream is = contrattiService.getResource(storageObject);
		((HttpActionContext)actioncontext).getResponse().setContentLength(storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue());
		((HttpActionContext)actioncontext).getResponse().setContentType(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
		OutputStream os = ((HttpActionContext)actioncontext).getResponse().getOutputStream();
		((HttpActionContext)actioncontext).getResponse().setDateHeader("Expires", 0);
		IOUtils.copyLarge(is, os);
	}

	public String getNomeAllegatoIncarico() {
		return Optional.ofNullable(getCrudIncarichiArchivioAllegati())
				.map(simpleDetailCRUDController -> simpleDetailCRUDController.getModel())
				.filter(Incarichi_archivioBulk.class::isInstance)
				.map(Incarichi_archivioBulk.class::cast)
				.map(Incarichi_archivioBulk::getNomeAllegato)
				.orElse(null);
	}


	public String getNomeAllegato() {
		return Optional.ofNullable(getCrudIncarichiArchivioAllegati())
				.map(simpleDetailCRUDController -> simpleDetailCRUDController.getModel())
				.filter(Incarichi_archivioBulk.class::isInstance)
				.map(Incarichi_archivioBulk.class::cast)
				.map(Incarichi_archivioBulk::getNomeAllegato)
				.orElse(null);
	}

	private void scaricaAllegato(ActionContext actioncontext, Incarichi_archivioBulk incarichi_archivioBulk) throws IOException {
		ContrattiService storeService = SpringUtil.getBean(ContrattiService.class);
		StorageObject storageObject = storeService.getStorageObjectBykey(incarichi_archivioBulk.getCms_node_ref());
		InputStream is = storeService.getResource(incarichi_archivioBulk.getCms_node_ref());
		((HttpActionContext) actioncontext).getResponse().setContentLength(
				(storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value())).intValue()
		);
		((HttpActionContext) actioncontext).getResponse().setContentType(
				(String) storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value())
		);
		OutputStream os = ((HttpActionContext) actioncontext).getResponse().getOutputStream();
		((HttpActionContext) actioncontext).getResponse().setDateHeader("Expires", 0);
		byte[] buffer = new byte[((HttpActionContext) actioncontext).getResponse().getBufferSize()];
		int buflength;
		while ((buflength = is.read(buffer)) > 0) {
			os.write(buffer, 0, buflength);
		}
		is.close();
		os.flush();
	}

	public void scaricaAllegatoIncarico(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
		final Incarichi_archivioBulk allegato = Optional.ofNullable(getCrudIncarichiArchivioAllegati())
				.map(simpleDetailCRUDController -> simpleDetailCRUDController.getModel())
				.filter(Incarichi_archivioBulk.class::isInstance)
				.map(Incarichi_archivioBulk.class::cast)
				.orElseThrow(() -> new ApplicationRuntimeException("Allegato non trovato!"));
		scaricaAllegato(actioncontext, allegato);
	}

	public void scaricaAllegatoRapporto(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
		final Incarichi_archivioBulk allegato = Optional.ofNullable(getIncarichiRappColl())
				.map(simpleDetailCRUDController -> simpleDetailCRUDController.getModel())
				.filter(Incarichi_repertorio_rappBulk.class::isInstance)
				.map(Incarichi_repertorio_rappBulk.class::cast)
				.orElseThrow(() -> new ApplicationRuntimeException("Allegato non trovato!"));
		scaricaAllegato(actioncontext, allegato);
	}

	public void scaricaAllegatoVariazione(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
		final Incarichi_archivioBulk allegato = Optional.ofNullable(getCrudIncarichiVariazioni())
				.map(simpleDetailCRUDController -> simpleDetailCRUDController.getModel())
				.filter(Incarichi_repertorio_varBulk.class::isInstance)
				.map(Incarichi_repertorio_varBulk.class::cast)
				.orElseThrow(() -> new ApplicationRuntimeException("Allegato non trovato!"));
		scaricaAllegato(actioncontext, allegato);
	}

    public void scaricaAllegato(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
		boolean multi_incarico = false;
		Incarichi_proceduraBulk procedura = ((Incarichi_proceduraBulk)getModel());
		if (procedura!=null && procedura.getNr_contratti()!=null && procedura.getNr_contratti().compareTo(new Integer(1))==1)
			multi_incarico=true;
		SimpleDetailCRUDController controller = multi_incarico?getCrudArchivioAllegati():getCrudArchivioAllegatiMI();
		Incarichi_archivioBulk allegato;
		// Recupero il valore (posizione) del record selezionato
		int  sel = controller.getSelection().getFocus();
		/*
		** Quando navigo la prima volta nella tab e non ci sono 
		** record selezionati, il valore restituito è -1. 
		** In questo caso imposto il valore a 0.
		*/
		if (sel == -1)
		   allegato = null;
		else {
			allegato = (Incarichi_archivioBulk)controller.getModel();
			scaricaAllegato(actioncontext, allegato);
		}
    }
	public void mergeWithCMIS(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException
	{
		try {
			IncarichiProceduraComponentSession proceduraComponent = (IncarichiProceduraComponentSession)createComponentSession();
			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)getModel();
			if (procedura!=null && procedura.getEsercizio()!=null && procedura.getPg_procedura()!=null) {
				List l = proceduraComponent.getIncarichiForMergeWithCMIS(context.getUserContext(), procedura.getEsercizio(), procedura.getPg_procedura());

				int i=0;
				for (Object object : l) {
					i++;
					Incarichi_proceduraBulk currProcedura = (Incarichi_proceduraBulk)object;
					try{
						List<String> listError = proceduraComponent.mergeAllegatiWithCMIS(context.getUserContext(), (Incarichi_proceduraBulk)object);
						if (listError.isEmpty()) {
							this.setMessage("Operazione Effettuata. Non è stata riscontrato alcun disallineamento dei dati nella gestione documentale.");
							logger.debug("MergeWithCMIS OK - Esercizio: "+procedura.getEsercizio()+" - Rec "+i+" di "+l.size()+" - Procedura: "+procedura.getEsercizio()+"/"+procedura.getPg_procedura());
						} else {
							for (String error : listError)
								logger.debug(error);
							this.setMessage("Operazione Effettuata. Sono stati riscontrati disallineamenti dei dati nella gestione documentale. Controllare il file incarichi.log.");
						}
					} catch (Exception e) {
						logger.error("MergeWithCMIS ERRORE: Procedura: "+procedura.getEsercizio()+"/"+procedura.getPg_procedura(),e);
					}
				}
			}
		}
		catch(Exception e)
		{
			throw handleException(e);
		}
	}
}