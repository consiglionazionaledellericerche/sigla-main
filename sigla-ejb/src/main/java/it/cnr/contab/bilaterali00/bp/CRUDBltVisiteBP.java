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

package it.cnr.contab.bilaterali00.bp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.V_persona_fisicaBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_accordiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_autorizzatiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_progettiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_visiteBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.config00.contratto.bulk.Procedure_amministrativeBulk;
import it.cnr.contab.config00.contratto.bulk.Tipo_atto_amministrativoBulk;
import it.cnr.contab.config00.ejb.EsercizioComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.doccont00.bp.CRUDObbligazioneBP;
import it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.incarichi00.bp.CRUDIncarichiProceduraBP;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_annoBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_attivitaBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_incaricoBulk;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.AbstractPrintBP;
import it.cnr.jada.util.jsp.Button;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TreeMap;

public class CRUDBltVisiteBP extends it.cnr.jada.util.action.SimpleCRUDBP implements IDefferedUpdateSaldiBP{
	/**
	 * Primo costruttore della classe <code>CRUDBltVisiteBP</code>.
	 */
	public CRUDBltVisiteBP() {
		super("Tr");
	}

	private boolean amministratore;
	
	private void setAmministratore(boolean amministratore) {
		this.amministratore = amministratore;
	}
	public boolean isAmministratore() {
		return amministratore;
	}
	/**
	 * Secondo costruttore della classe <code>CRUDBltVisiteBP</code>.
	 * @param function
	 */
	public CRUDBltVisiteBP(String function) {
		super(function+"Tr");
	}
	public CRUDBltVisiteBP(String function, OggettoBulk bulk) {
		super(function);
		if (bulk instanceof Blt_visiteBulk)
			setAmministratore(Boolean.TRUE);
	}
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		setTab("tab","tabTestata");
		super.init(config, actioncontext);
	}
	public String[][] getTabs() {
		TreeMap<Integer, String[]> hash = new TreeMap<Integer, String[]>();
		int i=0;

		hash.put(i++, new String[]{"tabTestata", "Candidatura", "/bilaterali00/tab_candidatura.jsp" });
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
		if (visita!=null && visita.getCrudStatus()!=OggettoBulk.UNDEFINED && !visita.isToBeCreated() && !isSearching()) {
			if (visita.isVisitaDipendente() || (visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero())) {
				if (visita.getCurrentFase()>=Blt_visiteBulk.FASE_QUINTA)
					hash.put(i++, new String[]{"tabObbligazione", "Impegno", "/bilaterali00/tab_obbligazione.jsp" });
				if (visita.isVisitaDipendente()) {
					if (visita.getCurrentFase()>=Blt_visiteBulk.FASE_OTTAVA && !visita.isVisitaAnnullata())
						hash.put(i++, new String[]{"tabRimborsoSpese", "Rimborso Spese", "/bilaterali00/tab_rimborso_spese_ita.jsp" });
				} else {
					if (visita.getCurrentFase()>=Blt_visiteBulk.FASE_SESTA && !visita.isAccordoPagataAdEnteStraniero() && !visita.isVisitaAnnullata() && (visita.isVisitaPagataAdEnteStraniero() && visita.getCurrentFase()>=Blt_visiteBulk.FASE_OTTAVA))
						hash.put(i++, new String[]{"tabRimborsoSpese", "Rimborso Spese", "/bilaterali00/tab_rimborso_spese.jsp" });
					if (visita.getCurrentFase()>=Blt_visiteBulk.FASE_SESTA && !visita.isVisitaAnnullata() && (visita.isAccordoPagataAdEnteStraniero() && visita.getCurrentFase()>=Blt_visiteBulk.FASE_SESTA))
						hash.put(i++, new String[]{"tabRimborsoSpese", "Rimborso Spese", "/bilaterali00/tab_rimborso_spese.jsp" });
				}
			} else {
				if (visita.getCurrentFase()>=Blt_visiteBulk.FASE_QUARTA)
					hash.put(i++, new String[]{"tabContratto", "Contratto", "/bilaterali00/tab_contratto.jsp" });
				if (visita.getCurrentFase()>=Blt_visiteBulk.FASE_OTTAVA)
					hash.put(i++, new String[]{"tabObbligazione", "Incarico / Impegno", "/bilaterali00/tab_obbligazione.jsp" });
				if (visita.isVisitaStraniero()) {
					if (visita.getCurrentFase()>=Blt_visiteBulk.FASE_DODICESIMA && !visita.isVisitaAnnullata())
						hash.put(i++, new String[]{"tabRimborsoSpese", "Rimborso Spese", "/bilaterali00/tab_rimborso_spese.jsp" });
				} else if (visita.isVisitaUniversitario()) {
					if (visita.getCurrentFase()>=Blt_visiteBulk.FASE_DODICESIMA && !visita.isVisitaAnnullata())
						hash.put(i++, new String[]{"tabRimborsoSpese", "Rimborso Spese", "/bilaterali00/tab_rimborso_spese_ita.jsp" });
				}
			}
			if (visita.isVisitaAnnullata())
				hash.put(i++, new String[]{"tabAnnullamento", "Dati Annullamento", "/bilaterali00/tab_annullamento.jsp" });
		}
		String[][] tabs = new String[i][3];
		for (int j = 0; j < i; j++) {
			tabs[j]=new String[]{hash.get(j)[0],hash.get(j)[1],hash.get(j)[2]};
		}
		return tabs;		
	}
	
	protected void resetTabs(ActionContext context) {
		setTab("tab","tabTestata");
		setTab("tabBltVisiteRimborsoSpese","tabBltVisiteRimborsoSpeseRiepilogo");
		if (getModel()!=null && getModel() instanceof Blt_visiteBulk){
			Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
			if (visita.isVisitaDipendente()) {
				if (visita.isQuintaFase() || visita.isSestaFase() || visita.isSettimaFase())
					setTab("tab","tabObbligazione");
				else if (visita.isOttavaFase() || visita.isNonaFase()) {
					setTab("tab","tabRimborsoSpese");
				}
			} else if (visita.isVisitaUniversitario()) {
				if (visita.isQuartaFase() || visita.isQuintaFase() || visita.isSestaFase() || visita.isSettimaFase())
					setTab("tab","tabContratto");
				else if (visita.isOttavaFase() || visita.isNonaFase() || visita.isDecimaFase() || visita.isUndicesimaFase())
					setTab("tab","tabObbligazione");
				else if (visita.isDodicesimaFase() || visita.isTredicesimaFase()) {
					setTab("tab","tabRimborsoSpese");
				}
			} else if (visita.isVisitaStraniero()) {
				if(visita.isVisitaPagataAdEnteStraniero() && !visita.isAccordoPagataAdEnteStraniero()){
					if (visita.isQuintaFase() || visita.isSestaFase() || visita.isSettimaFase())
						setTab("tab","tabObbligazione");
					else if (visita.isOttavaFase() || visita.isNonaFase() || visita.isDecimaFase()) {
						setTab("tab","tabRimborsoSpese");
						setTab("tabBltVisiteRimborsoSpese","tabBltVisiteRimborsoSpeseSaldo");
					}
				} else if(visita.isAccordoPagataAdEnteStraniero()){
						if (visita.isQuintaFase())
							setTab("tab","tabObbligazione");
						else if (visita.isSestaFase() || visita.isSettimaFase() || visita.isOttavaFase() || visita.isNonaFase() || visita.isDecimaFase()) {
							setTab("tab","tabRimborsoSpese");
							setTab("tabBltVisiteRimborsoSpese","tabBltVisiteRimborsoSpeseSaldo");
						}
				} else {
					if (visita.isQuartaFase() || visita.isQuintaFase() || visita.isSestaFase() || visita.isSettimaFase())
						setTab("tab","tabContratto");
					else if (visita.isOttavaFase() || visita.isNonaFase() || visita.isDecimaFase() || visita.isUndicesimaFase())
						setTab("tab","tabObbligazione");
					else if (visita.isDodicesimaFase() || visita.isTredicesimaFase() ) {
						setTab("tab","tabRimborsoSpese");
						setTab("tabBltVisiteRimborsoSpese","tabBltVisiteRimborsoSpeseAnticipo");
					} else if (visita.isQuattordicesimaFase() || visita.isQuindicesimaFase() || visita.isSedicesimaFase() || 
							   visita.isDiciassettesimaFase() || visita.isDiciottesimaFase() || visita.isDiciannovesimaFase()) {
						setTab("tab","tabRimborsoSpese");
						setTab("tabBltVisiteRimborsoSpese","tabBltVisiteRimborsoSpeseSaldo");
					}
				}
			}
		}
}
	
	@Override
	public  Button[] createToolbar() {
		Button[] toolbar = super.createToolbar();
		Button[] newToolbar = new Button[ toolbar.length + 20 ];
		for ( int i = 0; i< toolbar.length; i++ )
			newToolbar[ i ] = toolbar[ i ];
		newToolbar[ toolbar.length ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.annullaVisita");
		newToolbar[ toolbar.length ].setSeparator(true);
		newToolbar[ toolbar.length+1 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.printDocumentiCandidatura");
		newToolbar[ toolbar.length+1 ].setSeparator(true);
		newToolbar[ toolbar.length+2 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.printTrasmisCandidatura");
		newToolbar[ toolbar.length+2 ].setSeparator(true);
		newToolbar[ toolbar.length+3 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.printDispFinanziarie");
		newToolbar[ toolbar.length+4 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.printAccettDispFinanziarie");
		newToolbar[ toolbar.length+5 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.printModuloRimborsoSpese");
		newToolbar[ toolbar.length+6 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.printModelloAccettazioneVisita");
		newToolbar[ toolbar.length+7 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.printModelloAttestatoSoggiorno");
		newToolbar[ toolbar.length+8 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.printAttribuzioneIncarico");
		newToolbar[ toolbar.length+9 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.printModelloContratto");
		newToolbar[ toolbar.length+9 ].setSeparator(true);
		newToolbar[ toolbar.length+10 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.printNotaAddebito");
		newToolbar[ toolbar.length+11 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.creaIncarico");
		newToolbar[ toolbar.length+11 ].setSeparator(true);
		newToolbar[ toolbar.length+12 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.apriIncarico");
		newToolbar[ toolbar.length+12 ].setSeparator(true);
		newToolbar[ toolbar.length+13 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.printAutorizPartenza");
		newToolbar[ toolbar.length+13 ].setSeparator(true);
		newToolbar[ toolbar.length+14 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.creaObbligazione");
		newToolbar[ toolbar.length+14 ].setSeparator(true);
		newToolbar[ toolbar.length+15 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.printProvvObbligazione");
		newToolbar[ toolbar.length+15 ].setSeparator(true);
		newToolbar[ toolbar.length+16 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.printProvvPagamentoAnticipo");
		newToolbar[ toolbar.length+16 ].setSeparator(true);
		newToolbar[ toolbar.length+17 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.printProvvPagamento");
		newToolbar[ toolbar.length+17 ].setSeparator(true);
		newToolbar[ toolbar.length+18 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.printAnnullaProvvObbligazione");
		newToolbar[ toolbar.length+18 ].setSeparator(true);
		newToolbar[ toolbar.length+19 ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.returnPreStato");
		newToolbar[ toolbar.length+19 ].setSeparator(true);
		return newToolbar;
	}

	/* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    public void initializeStampaDispFinanziariePrintBP(AbstractPrintBP bp) 
	{
    	OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;
		printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_disp_finanziarie_cnr.jasper");
		addToPrintSpoolerParams(printbp);

		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam("flag_to_update");
		param.setValoreParam("FL_STAMPATO_DOC_CANDIDATURA");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
	}

     /* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    public void initializeStampaAccettDispFinanziariePrintBP(AbstractPrintBP bp) 
	{
    	OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;
		printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_disp_finanziarie_cnr.jasper");
		addToPrintSpoolerParams(printbp);
	}

    /* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    public void initializeStampaModuloRimborsoSpesePrintBP(AbstractPrintBP bp) 
	{
    	OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;
		printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_modulo_rimborso_cnr_e_ass_e_univ.jasper");
		addToPrintSpoolerParams(printbp);

		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam("flag_to_update");
		param.setValoreParam("FL_STAMPATO_DOC_CANDIDATURA");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
	}

    /* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    public void initializeStampaDocumentiCandidaturaPrintBP(AbstractPrintBP bp) 
	{
   	
   	    OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;

      	Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
		if (visita.isVisitaItaliano())
			printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_documenti_candidatura_cnr.jasper");
		else
			printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_documenti_candidatura_str.jasper");
		
		addToPrintSpoolerParams(printbp);

		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam("flag_to_update");
		param.setValoreParam("FL_STAMPATO_DOC_CANDIDATURA");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
	}

    /* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    public void initializeStampaTrasmissioneCandidaturaPrintBP(AbstractPrintBP bp) 
	{
    	
    	OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;

    	Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
		if (visita.isVisitaDipendente() || visita.isVisitaUniversitario())
			printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_trasmissione_candidatura_cnr.jasper");
		else
			printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_trasmissione_candidatura_str.jasper");
		
		addToPrintSpoolerParams(printbp);

		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam("flag_to_update");
		param.setValoreParam("FL_STAMPATO_DOC_CANDIDATURA");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
	}

    /* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    public void initializeStampaAttribuzioneIncaricoPrintBP(AbstractPrintBP bp) 
	{
    	OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;
		printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_attribuzione_incarico_str.jasper");
		addToPrintSpoolerParams(printbp);

		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam("flag_to_update");
		param.setValoreParam("FL_STAMPATO_DOC_CANDIDATURA");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
	}

    /* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    public void initializeStampaModelloAccettazioneVisitaPrintBP(AbstractPrintBP bp) 
	{
    	OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;
		printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_modello_accettazione_visita.jasper");
		addToPrintSpoolerParams(printbp);

		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam("flag_to_update");
		param.setValoreParam("FL_STAMPATO_DOC_CANDIDATURA");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
	}

    /* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    public void initializeStampaModelloAttestatoSoggiornoPrintBP(AbstractPrintBP bp) 
	{
    	OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;
		printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_modello_attestato_soggiorno.jasper");
		addToPrintSpoolerParams(printbp);

		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam("flag_to_update");
		param.setValoreParam("FL_STAMPATO_DOC_CANDIDATURA");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
	}
    
    /* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    public void initializeStampaModelloContrattoPrintBP(AbstractPrintBP bp) 
	{
    	OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;

      	Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
		if (visita.isVisitaUniversitario())
			printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_modello_contratto_univ.jasper");
		else
			printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_modello_contratto_str.jasper");
		addToPrintSpoolerParams(printbp);

		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam("flag_to_update");
		param.setValoreParam("FL_STAMPATO_MODELLO_CONTRATTO");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
	}

    /* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    public void initializeStampaNotaAddebitoPrintBP(AbstractPrintBP bp) 
	{
    	OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;
		printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_nota_addebito_str.jasper");
		addToPrintSpoolerParams(printbp);

		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam("fl_tipo_stampa");

		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
      	if (visita.isTerzaFase() || visita.isQuartaFase()) {
      		 if (visita.isNotaAddebitoAnticipoRequired()) {
      			if (visita.isNotaAddebitoSaldoConAnticipoRequired())
      				param.setValoreParam("T");
          		else 
          			param.setValoreParam("A");
      		 } else if (visita.getFlStampatoNotaAddebito() || visita.isNotaAddebitoSaldoRequired())
      			param.setValoreParam("S");
      	} else {
  			param.setValoreParam("S");
      	}

		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);

      	param = new Print_spooler_paramBulk();
		param.setNomeParam("flag_to_update");
		param.setValoreParam("FL_STAMPATO_MODELLO_CONTRATTO");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
	}

    /* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    public void initializeStampaAutorizzazionePartenzaPrintBP(AbstractPrintBP bp) 
	{
    	OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;

      	Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
      	if (visita.isVisitaDipendente() || visita.isVisitaUniversitario())
        	printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_autorizzazione_partenza_cnr.jasper");
		else
	    	printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_autorizzazione_partenza_str.jasper");

		addToPrintSpoolerParams(printbp);

		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam("flag_to_update");
		param.setValoreParam("FL_STAMPATO_AUTORIZ_PARTENZA");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
	}
    
    /* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    public void initializeStampaProvvObbligazionePrintBP(AbstractPrintBP bp) 
    {
	    OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;

      	Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
      	if ((visita.isVisitaStraniero() && visita.getFlPagamentoFineVisita()) || (visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero()))
        	printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_impegno_str_fin_visita.jasper");
		else
		    printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_provvedimento_impegno_cnr.jasper");

		addToPrintSpoolerParams(printbp);

		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam("flag_to_update");
		param.setValoreParam("FL_STAMPATO_PROVV_IMPEGNO");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
    }

    /* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    public void initializeStampaPagamentoAnticipoPrintBP(AbstractPrintBP bp) 
    {
	    OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;
		printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_pagamento_saldo_str.jasper");
		addToPrintSpoolerParams(printbp);

		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam("anticipo");
		param.setValoreParam("Y");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);

		param = new Print_spooler_paramBulk();
		param.setNomeParam("flag_to_update");
		param.setValoreParam("FL_STAMPATO_PROVV_PAGAM_ANT");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
	}

    /* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    public void initializeStampaPagamentoSaldoPrintBP(AbstractPrintBP bp) 
    {
	    OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;

	    Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
      	if (visita.isVisitaDipendente() || visita.isVisitaUniversitario()) {
    	    printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_pagamento_saldo_cnr.jasper");
          	addToPrintSpoolerParams(printbp);
      	} else if (visita.getFlPagamentoFineVisita()){
		    printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_pagamento_saldo_str_fin_visita.jasper");
          	addToPrintSpoolerParams(printbp);
         
      	} else if (visita.isVisitaPagataAdEnteStraniero()){
		    printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_provvedimento_pagamento_ente_str.jasper");
          	addToPrintSpoolerParams(printbp);
          	
          	
      	} else {	
		    printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_pagamento_saldo_str.jasper");
	      	addToPrintSpoolerParams(printbp);

			Print_spooler_paramBulk param = new Print_spooler_paramBulk();
			param.setNomeParam("anticipo");
			param.setValoreParam("N");
			param.setParamType("java.lang.String");
			printbp.addToPrintSpoolerParam(param);
		}

		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam("flag_to_update");
		param.setValoreParam("FL_STAMPATO_PROVV_PAGAMENTO");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
	}
 
    /* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    public void initializeStampaAnnullaProvvObbligazionePrintBP(AbstractPrintBP bp) 
    {
	    OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;
		printbp.setReportName("/cnrbilaterali/bilaterali/blt_visite_annulla_provvedimento_impegno_cnr.jasper");
		addToPrintSpoolerParams(printbp);

		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam("flag_to_update");
		param.setValoreParam("FL_STAMPATO_ANN_PROVV_IMPEGNO");
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
    }

    /* inizializza il BP delle stampe impostando il nome del report da stampare e i suoi parametri */
    private void addToPrintSpoolerParams(OfflineReportPrintBP printbp) {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
		Print_spooler_paramBulk param;

		param = new Print_spooler_paramBulk();
		param.setNomeParam("cd_accordo");
		param.setValoreParam(visita.getCdAccordo());
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
		
		param = new Print_spooler_paramBulk();
		param.setNomeParam("cd_progetto");
		param.setValoreParam(visita.getCdProgetto());
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);

		param = new Print_spooler_paramBulk();
		param.setNomeParam("cd_terzo");
		param.setValoreParam(visita.getCdTerzo().toString());
		param.setParamType("java.lang.Integer");
		printbp.addToPrintSpoolerParam(param);

		param = new Print_spooler_paramBulk();
		param.setNomeParam("pg_autorizzazione");
		param.setValoreParam(visita.getPgAutorizzazione().toString());
		param.setParamType("java.lang.Long");
		printbp.addToPrintSpoolerParam(param);

		param = new Print_spooler_paramBulk();
		param.setNomeParam("pg_visita");
		param.setValoreParam(visita.getPgVisita().toString());
		param.setParamType("java.lang.Long");
		printbp.addToPrintSpoolerParam(param);

		param = new Print_spooler_paramBulk();
		param.setNomeParam("pg_ver_rec");
		param.setValoreParam(visita.getPg_ver_rec().toString());
		param.setParamType("java.lang.Long");
		printbp.addToPrintSpoolerParam(param);
	}

    public boolean isStampaDocumentiCandidaturaButtonHidden() {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
    	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || 
    		visita.isVisitaAnnullata() || visita.isVisitaItaliano() || visita.isVisitaStraniero())
    		return true;
    	return false;
    }
    public boolean isStampaTrasmissioneCandidaturaButtonHidden() {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
    	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || 
       		(!visita.isPrimaFase() && !visita.isSecondaFase()) || !isStampaAutorizzazionePartenzaButtonHidden())
    		return true;
    	return false;
    }
    public boolean isStampaDisposizioniFinanziarieButtonHidden() {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
    	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || 
        	(!visita.isVisitaDipendente() && !visita.isVisitaUniversitario()) ||
       		(!visita.isPrimaFase() && !visita.isSecondaFase()) || !isStampaAutorizzazionePartenzaButtonHidden())
    		return true;
    	return false;
    }
    public boolean isStampaAccettazioneDisposizioniFinanziarieButtonHidden() {
		return true;
//		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
//    	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || !visita.isVisitaDipendente() ||
//    		(!visita.isPrimaFase() && !visita.isSecondaFase()) || !isStampaAutorizzazionePartenzaButtonHidden())
//    		return true;
//    	return false;
    }
    public boolean isStampaModuloRimborsoSpeseButtonHidden() {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
    	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || 
    		visita.isVisitaStraniero() || (visita.isVisitaDipendente() && visita.getCurrentFase()>=Blt_visiteBulk.FASE_QUINTA) ||
    		(visita.isVisitaUniversitario() && visita.getCurrentFase()>=Blt_visiteBulk.FASE_SETTIMA))
    		return true;
    	return false;
    }
    public boolean isStampaModelloAccettazioneVisitaButtonHidden() {
		return true;
//		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
//    	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || 
//    		visita.isVisitaDipendente() || visita.isVisitaUniversitario() ||
//    		visita.isVisitaAnnullata() || (!visita.isPrimaFase() && !visita.isSecondaFase()))
//    		return true;
//    	return false;
    }
    public boolean isStampaModelloAttestatoSoggiornoButtonHidden() {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
    	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || 
    		visita.isVisitaDipendente() || visita.isVisitaUniversitario() ||
    		visita.isVisitaAnnullata() || (!visita.isPrimaFase() && !visita.isSecondaFase()))
    		return true;
    	return false;
    }
    public boolean isStampaAttribuzioneIncaricoButtonHidden() {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
    	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || visita.isVisitaDipendente() ||
    		(visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero()) ||
    		(visita.isVisitaStraniero() && visita.isAccordoPagataAdEnteStraniero()) ||
    		visita.isVisitaAnnullata() || (!visita.isPrimaFase() && !visita.isSecondaFase()))
    		return true;
    	return false;
    }
    public boolean isStampaModelloContrattoButtonHidden() {
 		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
     	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || visita.isVisitaDipendente() ||
       		(visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero()) ||
       		(visita.isVisitaStraniero() && visita.isAccordoPagataAdEnteStraniero()) ||
     		visita.isVisitaAnnullata() || (!visita.isTerzaFase() && !visita.isQuartaFase()))
     		return true;
     	return false;
    }
    public boolean isStampaNotaAddebitoButtonHidden() {
 		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
     	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || visita.isVisitaDipendente() ||
     		visita.isVisitaUniversitario() || visita.isVisitaAnnullata() ||
     		(visita.isVisitaStraniero() && visita.isAccordoPagataAdEnteStraniero()) || !(visita.isTerzaFase()|| visita.isQuartaFase() || visita.isQuindicesimaFase() || visita.isSedicesimaFase()) || 
     		((visita.isVisitaStraniero() && visita.getFlPagamentoFineVisita() && !visita.isQuindicesimaFase() && !visita.isSedicesimaFase()) ||
     		((visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero() && !visita.isQuindicesimaFase()))))
     		return true;
     	return false;
    }
    public boolean isCreaIncaricoButtonHidden() {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
     	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || visita.isVisitaDipendente() ||
     	   (visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero()) ||
       		visita.isVisitaAnnullata() || !visita.isSettimaFase())
       		return true;
    	return false;
    }
    public boolean isApriIncaricoButtonHidden() {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
     	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || visita.isVisitaDipendente() ||
     		(visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero()) || visita.isVisitaAnnullata() || 
        	(!visita.isOttavaFase() && (!visita.isNonaFase()||
        			(visita.getObbligazioneScadenzario()!=null && visita.getObbligazioneScadenzario().getObbligazione()!=null &&
        			 visita.getObbligazioneScadenzario().getObbligazione().isTemporaneo()))))
       		return true;
    	return false;
    }
    public boolean isStampaAutorizzazionePartenzaButtonHidden() {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
     	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || visita.isVisitaAnnullata() || 
     		(visita.isVisitaDipendente() && !visita.isTerzaFase() && !visita.isQuartaFase()) ||
     		(visita.isVisitaUniversitario() && !visita.isQuintaFase() && !visita.isSestaFase()) ||
     		((visita.isVisitaStraniero() && !visita.isVisitaPagataAdEnteStraniero()) && !visita.isQuintaFase() && !visita.isSestaFase()) || 
     		((visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero()) && !visita.isTerzaFase() && !visita.isQuartaFase()))
     		return true;
    	return false;
    }
    public boolean isCreaObbligazioneButtonHidden() {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
     	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || visita.isVisitaAnnullata() || 
     		((visita.isVisitaUniversitario() || visita.isVisitaDipendente() || (visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero())) && visita.getTipo_obbligazione()==null) ||
        	((visita.isVisitaUniversitario() || (visita.isVisitaStraniero() && !visita.isVisitaPagataAdEnteStraniero())) && 
        		(!visita.isNonaFase()||(visita.getObbligazioneScadenzario()!=null && visita.getObbligazioneScadenzario().getObbligazione()!=null &&
       			 visita.getObbligazioneScadenzario().getObbligazione().isTemporaneo()))) ||
 		    ((visita.isVisitaDipendente() || (visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero())) && 
        		(!visita.isQuintaFase()||(visita.getObbligazioneScadenzario()!=null && visita.getObbligazioneScadenzario().getPg_obbligazione()!=null &&
       			  visita.getObbligazioneScadenzario().getObbligazione().isTemporaneo()))))
       		return true;
    	return false;
    }
    public boolean isStampaProvvObbligazioneButtonHidden() {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
    	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || visita.isVisitaAnnullata() || visita.isAccordoPagataAdEnteStraniero() ||
    	    (visita.isVisitaStraniero() &&  !visita.isVisitaPagataAdEnteStraniero() && !visita.getFlPagamentoFineVisita() && visita.getCurrentFase()>=Blt_visiteBulk.FASE_PRIMA) ||
    		(visita.isVisitaDipendente() && !visita.isSestaFase() && !visita.isSettimaFase()) ||
    	    (visita.isVisitaUniversitario() && !visita.isDecimaFase() && !visita.isUndicesimaFase()) ||
    	    (visita.isVisitaStraniero()  && visita.getFlPagamentoFineVisita() && !visita.isDecimaFase() && !visita.isUndicesimaFase()) ||
    	    ((visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero() && !visita.isSestaFase()) && 
    	    (visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero() && !visita.isSettimaFase())) 
    	    
    	    )
    	    
    		return true;
    	return false;
    }
    public boolean isStampaAnnullaProvvObbligazioneButtonHidden() {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
    	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || !visita.isVisitaAnnullata() ||
        	visita.getObbligazioneScadenzario()==null || visita.getObbligazioneScadenzario().getObbligazione()==null ||
    		visita.getObbligazioneScadenzario().getObbligazione().isTemporaneo() ||
    		visita.getNumProtProvvImpegno()==null || visita.getDtProtProvvImpegno()==null ||
    		visita.isInFaseAnnullamento())
    		return true;
    	return false;
    }
    public boolean isStampaPagamentoAnticipoButtonHidden() {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
     	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || visita.isVisitaAnnullata() || visita.getPgBancaAnt()==null ||
            visita.isVisitaDipendente() || (!visita.isUndicesimaFase() && !visita.isDodicesimaFase() ))
       		return true;
    	return false;
    }
    public boolean isStampaPagamentoSaldoButtonHidden() {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
     	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || visita.isVisitaAnnullata() || 
     		visita.isVentesimaFase() ||
     		visita.getImRimbSpese()==null || visita.getImRimbSpese().compareTo(BigDecimal.ZERO)!=1 || visita.getPgBanca()==null ||
            (visita.isVisitaUniversitario() && visita.getCurrentFase()<Blt_visiteBulk.FASE_DODICESIMA) ||
             (visita.isVisitaStraniero() && !visita.isVisitaPagataAdEnteStraniero() && (visita.getImRimbSpese().compareTo(BigDecimal.ZERO)==0 || 
             		visita.getCurrentFase()<Blt_visiteBulk.FASE_QUINDICESIMA)) ||
             (visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero() && 
            		visita.getCurrentFase()<Blt_visiteBulk.FASE_SESTA) ||
             (visita.isVisitaDipendente() && (visita.getCurrentFase()<Blt_visiteBulk.FASE_OTTAVA ||
    				(visita.getNumProtRimbSpese()==null || visita.getDtProtRimbSpese()==null || visita.getImRimbSpese()==null))))
    		return true;
    	return false;
    }
    public boolean isAnnullaVisitaButtonHidden() {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
    	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || visita.isVisitaAnnullata() || visita.isPrimaFase() ||
    		visita.getFlStampatoProvvPagamento())
    		return true;
    	return false;
    }
    public void validaLancioStampaAutorizzazionePartenza() throws ValidationException {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
    	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED)
	    	throw new ValidationException( "Stampa non possibile! L'oggetto da stampare non è correttamente definito!");
    	if (!visita.getFlStampatoDocCandidatura())
	    	throw new ValidationException( "Stampa non possibile! Occorre ancora eseguire le stampe iniziali della candidatura!");
    	if (visita.getNumProtTrasmissCandidatura()==null || visita.getDtProtTrasmissCandidatura()==null)
	    	throw new ValidationException( "Stampa non possibile! Indicare il protocollo della lettera di trasmissione candidatura!");
    	if (visita.getNumProtAccettEnteStr()==null || visita.getDtProtAccettEnteStr()==null)
	    	throw new ValidationException( "Stampa non possibile! Indicare il protocollo della lettera di accettazione di trasmissione candidatura!");
    	if (visita.isVisitaDipendente()){
	    	if (visita.getNumProtDispFin()==null || visita.getDtProtDispFin()==null)
		    	throw new ValidationException( "Stampa non possibile! Indicare il protocollo della lettera delle disposizioni finanziarie!");
//	    	if (visita.getNumProtAccettDispFin()==null || visita.getDtProtAccettDispFin()==null)
//		    	throw new ValidationException( "Stampa non possibile! Indicare il protocollo della lettera di accettazione delle disposizioni finanziarie!");
    	} 
    }
    public void validaLancioStampaPagamentoAnticipo() throws ValidationException {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
    	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED)
	    	throw new ValidationException( "Stampa non possibile! L'oggetto da stampare non è correttamente definito!");
    	if (visita.getImRimbSpeseAnt()==null)
	    	throw new ValidationException( "Stampa non possibile! Indicare l'importo dell'anticipo di pagamento!");
    	if (visita.getBancaAnticipo()==null || visita.getBancaAnticipo().getPg_banca()==null || visita.getModalitaPagamentoAnticipo()==null || visita.getCdModalitaPagAnt()==null)
	    	throw new ValidationException( "Stampa non possibile! Indicare la modalità di pagamento dell'anticipo!");
    }
    public void validaLancioStampaPagamentoSaldo() throws ValidationException {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
    	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED)
	    	throw new ValidationException( "Stampa non possibile! L'oggetto da stampare non è correttamente definito!");
    	if (visita.getImRimbSpese()==null)
	    	throw new ValidationException( "Stampa non possibile! Indicare l'importo del saldo di pagamento o di rimborso!");
    	if (visita.getDtPagamSaldo()==null)
	    	throw new ValidationException( "Stampa non possibile! Indicare la data di pagamento del saldo di pagamento o di rimborso!");
    	if (visita.getImRimbSpese().compareTo(BigDecimal.ZERO)==1) {
	    	if (visita.getBanca()==null || visita.getBanca().getPg_banca()==null || visita.getModalitaPagamento()==null || visita.getCdModalitaPag()==null)
		    	throw new ValidationException( "Stampa non possibile! Indicare la modalità di pagamento del saldo!");
	    	if (visita.isVisitaStraniero() && !visita.isVisitaPagataAdEnteStraniero() &&
	    		!visita.isNotaAddebitoSaldoConAnticipoRequired() &&
				(visita.getNumProtAttestatoSogg()==null || visita.getDtProtAttestatoSogg()==null ||
				 visita.getDtIniVisitaEffettiva()==null || visita.getDtFinVisitaEffettiva()==null))
	    		throw new ValidationException( "Per effettuare il pagamento del saldo è necessario inserire i dati relativi all'attestato di soggiorno!");
    	}
    }
    public void validaLancioCreazioneIncarico() throws ValidationException {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
    	if (visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED)
	    	throw new ValidationException( "Creazione incarico non possibile! La visita di riferimento non è correttamente definita!");
    	if (visita.getNumProtContratto()==null || visita.getDataProtContratto()==null)
	    	throw new ValidationException( "Creazione incarico non possibile! Indicare il protocollo del contratto!");
    	if (visita.isVisitaStraniero()) {
    		if (visita.isNotaAddebitoAnticipoRequired()) {
        		if (visita.getNumProtNotaAddebitoAnt()==null || visita.getDtProtNotaAddebitoAnt()==null)
        			throw new ValidationException( "Creazione incarico non possibile! Indicare il protocollo della nota di addebito di anticipo!");
    		}
    	//	if (visita.isNotaAddebitoSaldoConAnticipoRequired()) {
	    //		if (visita.getNumProtNotaAddebito()==null || visita.getDtProtNotaAddebito()==null)
		//	    	throw new ValidationException( "Creazione incarico non possibile! Indicare il protocollo della nota di addebito!");
        //	}
    	}
    }

    public IDefferUpdateSaldi getDefferedUpdateSaldiBulk() {
    	return (IDefferUpdateSaldi)getModel();
    }

    public IDefferedUpdateSaldiBP getDefferedUpdateSaldiParentBP() {
    	return this;
    }
    @Override
	public boolean isDeleteButtonHidden() {
		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
    	return isAmministratore() || !visita.isPrimaFase();
	}
	public void annullaVisita(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
			if (!visita.getFlStampatoProvvPagamento()) {
				visita.setInFaseAnnullamento(Boolean.TRUE);
				visita.setFlVisitaAnnullata(Boolean.TRUE);
				visita.setNumProtRimbSpese(null);
				visita.setDtProtRimbSpese(null);
			    visita.setImRimbSpese(null);
			    visita.setBanca(null);
			    visita.setModalitaPagamento(null);
			    visita.initializeFase();
				visita.setToBeUpdated();
				this.setDirty(Boolean.TRUE);
			}
		}
		catch(Exception e) 
		{
			throw handleException(e);
		}
	}
	
	public void validaCreazioneObbligazione(it.cnr.jada.action.ActionContext context) throws ValidationException {
		try {
			EsercizioComponentSession esercizioComponentSession = ((it.cnr.contab.config00.ejb.EsercizioComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_EsercizioComponentSession",	EsercizioComponentSession.class));
			EsercizioBulk lastEsercizio = esercizioComponentSession.getLastEsercizioOpen(context.getUserContext());
			if (lastEsercizio!=null && !lastEsercizio.getEsercizio().equals(CNRUserContext.getEsercizio(context.getUserContext())))
				throw new ValidationException("La creazione dell'impegno è possibile effettuarla solo accedendo all'esercizio "+lastEsercizio.getEsercizio()+" che risulta essere l'ultimo aperto!");
		} catch (ComponentException e) {
			throw new ValidationException("Creazione impegno non possibile! Si è verificato un errore nella ricerca dell'ultimo esercizio aperto.");
		} catch (RemoteException e) {
			throw new ValidationException("Creazione impegno non possibile! Si è verificato un errore nella ricerca dell'ultimo esercizio aperto.");
		}
	}

    public CRUDIncarichiProceduraBP createBPCreazioneIncarico(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
  		try {
  			Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
  			Blt_progettiBulk progetto = visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti();
  			Blt_accordiBulk accordo = progetto.getBltAccordo();

  			CRUDIncarichiProceduraBP incaricoProceduraBP=(CRUDIncarichiProceduraBP)context.getUserInfo().createBusinessProcess(context,"CRUDIncarichiProceduraBP",new Object[] {"MRSW", visita});

  			incaricoProceduraBP.reset(context);

			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)incaricoProceduraBP.getModel();
			procedura.setCd_firmatario(61999);
			//metodo per riempire immediatamente il firmatario
			V_persona_fisicaBulk firmatario = new V_persona_fisicaBulk();
			firmatario.setCd_terzo(61999);
			try	{
				RemoteIterator ri = incaricoProceduraBP.find(context,null,firmatario,procedura,"firmatario");	
				ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
				if (ri != null && ri.countElements() == 1)
					procedura.setFirmatario((V_persona_fisicaBulk)ri.nextElement());
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			} catch (Exception e) {
			}
			
			//metodo per riempire immediatamente la Procedura Amministrativa
			try	{
				RemoteIterator ri = incaricoProceduraBP.find(context,null,new Tipo_atto_amministrativoBulk("DEC"),procedura,"atto");	
				ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
				if (ri != null && ri.countElements() == 1)
					procedura.setAtto((Tipo_atto_amministrativoBulk)ri.nextElement());
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			} catch (Exception e) {
			}

			procedura.setDs_atto("Protocollo nr."+visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getNum_prot_dec_contr()+" del "
					+new java.text.SimpleDateFormat("dd/MM/yyyy").format(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getData_prot_dec_contr().getTime()));
			procedura.setOggetto("Accordo di cooperazione scientifica tra CNR e "+accordo.getNome_ente_str()+" ("+accordo.getAcronimo_ente_str()+"), "+
					accordo.getNazioneStr().getDs_nazione()+" - "+(accordo.getAnno_fin()-accordo.getAnno_ini()==1?"Biennio":(accordo.getAnno_fin()-accordo.getAnno_ini()==2?"Triennio":"Anni"))+ " " +
					accordo.getAnno_ini().toString()+"-"+accordo.getAnno_fin().toString()+
					". Progetto comune di ricerca "+progetto.getResponsabileIta().getDenominazione_sede().toUpperCase()+" / "+progetto.getResponsabileStr().getDenominazione_sede().toUpperCase()+
					". Soggiorno di ricerca Dott. "+visita.getBltAutorizzatiDett().getBltAutorizzati().getTerzo().getDenominazione_sede());

			//metodo per riempire immediatamente il responsabile del procedimento
			V_persona_fisicaBulk responsabile = new V_persona_fisicaBulk();
			responsabile.setCd_terzo(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getCd_respons_ita());
			try	{
				RemoteIterator ri = incaricoProceduraBP.find(context,null,responsabile,procedura,"terzo_resp");	
				ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
				if (ri != null && ri.countElements() == 1)
					procedura.setTerzo_resp((V_persona_fisicaBulk)ri.nextElement());
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			} catch (Exception e) {
			}

			//metodo per riempire immediatamente la Procedura Amministrativa
			try	{
				RemoteIterator ri = incaricoProceduraBP.find(context,null,new Procedure_amministrativeBulk("INC3"),procedura,"procedura_amministrativa");	
				ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
				if (ri != null && ri.countElements() == 1)
					procedura = incaricoProceduraBP.initializeProcedura_amministrativa(context, procedura, (Procedure_amministrativeBulk)ri.nextElement());
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			} catch (Exception e) {
			}

			//metodo per riempire immediatamente il tipo di attivita
			try	{
				RemoteIterator ri = incaricoProceduraBP.find(context,null,new Tipo_attivitaBulk("2"),procedura,"tipo_attivita");	
				ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
				if (ri != null && ri.countElements() == 1)
					procedura.setTipo_attivita((Tipo_attivitaBulk)ri.nextElement());
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			} catch (Exception e) {
			}

			//metodo per riempire immediatamente il tipo di incarico
			try	{
				RemoteIterator ri = incaricoProceduraBP.find(context,null,new Tipo_incaricoBulk("1"),procedura,"tipo_incarico");	
				ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
				if (ri != null && ri.countElements() == 1)
					procedura = incaricoProceduraBP.initializeFind_tipo_incarico(context, procedura, (Tipo_incaricoBulk)ri.nextElement());
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			} catch (Exception e) {
			}

			if (visita.isVisitaItaliano())
				procedura.setImporto_lordo(BigDecimal.ZERO);
			else
				procedura.setImporto_lordo(visita.getImRimbPrevisto());
			
			incaricoProceduraBP.changeImportoLordo(context, procedura, Utility.nvl(procedura.getImporto_lordo()));
			procedura.setToBeCreated();

			if (procedura.getImporto_complessivo().compareTo(BigDecimal.ZERO)!=0) {
				incaricoProceduraBP.getRipartizionePerAnno().add(context);
				Incarichi_procedura_annoBulk procAnno = (Incarichi_procedura_annoBulk)incaricoProceduraBP.getRipartizionePerAnno().getModel();
				procAnno.setImporto_iniziale(procedura.getImporto_complessivo());
				procAnno.setToBeCreated();
			}

			Incarichi_repertorioBulk incarico = new Incarichi_repertorioBulk();
			incarico.setDt_stipula(visita.getDataProtContratto());
			incarico.setDt_inizio_validita(visita.getDtIniVisita());
			incarico.setDt_fine_validita(visita.getDtFinVisita());

			incarico.setCd_provv("CNR-AMMCNT");
			incarico.setNr_provv(visita.getNumProtAttribIncarico().intValue());
			incarico.setDt_provv(visita.getDtProtAttribIncarico());
			incarico.setIncarichi_procedura(procedura);
			
			//metodo per riempire immediatamente il responsabile del procedimento
			V_terzo_per_compensoBulk terzo = new V_terzo_per_compensoBulk();
			terzo.setCd_terzo(visita.getCdTerzo());
			try	{
				RemoteIterator ri = incaricoProceduraBP.find(context,null,terzo,incarico,"v_terzo");	
				ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
				if (ri != null && ri.countElements() == 1)
					incarico = incaricoProceduraBP.initializeTerzo(context, incarico, (V_terzo_per_compensoBulk)ri.nextElement());
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			} catch (BusinessProcessException e) {
				throw handleException(e);
			} catch (Exception e) {
			}
			incarico.setToBeCreated();
			incaricoProceduraBP.getIncarichiColl().add(context, incarico);
  				
			incaricoProceduraBP.setDirty(Boolean.TRUE);
			return incaricoProceduraBP;
   		} catch(Throwable e) {
   			throw handleException(e);
   		}
   	}
    
    public CRUDObbligazioneBP createBPCreazioneObbligazione(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
  		try {
  			Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
  			Blt_progettiBulk progetto = visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti();
  			Blt_accordiBulk accordo = progetto.getBltAccordo();

  			Incarichi_repertorio_annoBulk repannogood = null;
  			if ((visita.isVisitaStraniero() && !visita.isVisitaPagataAdEnteStraniero()) && ((visita.isVisitaStraniero() && !visita.isAccordoPagataAdEnteStraniero()))) {
				Incarichi_repertorioBulk incaricoClone = (Incarichi_repertorioBulk)Utility.createIncarichiRepertorioComponentSession().inizializzaBulkPerModifica(context.getUserContext(), visita.getIncaricoRepertorio());
				for (Iterator iterator = incaricoClone.getIncarichi_repertorio_annoColl().iterator(); iterator.hasNext();) {
					Incarichi_repertorio_annoBulk repanno = (Incarichi_repertorio_annoBulk) iterator.next();
					if (repanno.getImporto_complessivo().compareTo(BigDecimal.ZERO)==1) {
						if (repannogood==null)
							repannogood = repanno;
						else
							throw new it.cnr.jada.comp.ApplicationException("Attenzione! L'incarico risulta essere imputato a più esercizi finanziari! Creazione Obbligazione non consentita.");
					}						
				}
				if (repannogood==null)
					throw new it.cnr.jada.comp.ApplicationException("Attenzione! L'incarico non risulta essere imputato ad alcun esercizio finanziario!");
			}

  			CRUDObbligazioneBP obbligazioneBP=null;

			if ((visita.isVisitaDipendente() || visita.isVisitaUniversitario()) || (visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero()))  {
				if (visita.getTipo_obbligazione()==null)
					throw new it.cnr.jada.comp.ApplicationException("Indicare la tipologia di impegno da creare.");
				else if (visita.getTipo_obbligazione()==null || visita.getTipo_obbligazione().equals(ObbligazioneBulk.TIPO_COMPETENZA))
	  				obbligazioneBP = (it.cnr.contab.doccont00.bp.CRUDObbligazioneBP)context.getUserInfo().createBusinessProcess(context,"CRUDObbligazioneBP",new Object[] { "MRSWTh" });
	  			else if (visita.getTipo_obbligazione().equals(ObbligazioneBulk.TIPO_RESIDUO_IMPROPRIO)) 
					obbligazioneBP = (it.cnr.contab.doccont00.bp.CRUDObbligazioneBP)context.getUserInfo().createBusinessProcess(context,"CRUDObbligazioneResImpropriaBP",new Object[] { "MRSWTh" });			
				else if (visita.getTipo_obbligazione().equals(ObbligazioneBulk.TIPO_RESIDUO_PROPRIO))
					throw new it.cnr.jada.comp.ApplicationException("Impossibile creare un nuovo impegno residuo proprio. Scegliere una diversa tipologia di impegno da creare.");
			} else {
				if (repannogood.getEsercizio_limite().compareTo(CNRUserContext.getEsercizio(context.getUserContext()))==0)
	  				obbligazioneBP = (it.cnr.contab.doccont00.bp.CRUDObbligazioneBP)context.getUserInfo().createBusinessProcess(context,"CRUDObbligazioneBP",new Object[] { "MRSWTh" });
				else if (repannogood.getEsercizio_limite().compareTo(CNRUserContext.getEsercizio(context.getUserContext()))==-1)
					obbligazioneBP = (it.cnr.contab.doccont00.bp.CRUDObbligazioneBP)context.getUserInfo().createBusinessProcess(context,"CRUDObbligazioneResImpropriaBP",new Object[] { "MRSWTh" });			
				else 
					throw new it.cnr.jada.comp.ApplicationException("Attenzione! L'incarico risulta essere imputato ad un esercizio finanziario "+repannogood.getEsercizio_limite()+" successivo a quello corrente.");
			}
			
			obbligazioneBP.reset(context);

			ObbligazioneBulk obbligazione = (ObbligazioneBulk)obbligazioneBP.getModel();
			if (visita.isVisitaStraniero() && !visita.isVisitaPagataAdEnteStraniero()) {
				if (obbligazioneBP.getName().equals("CRUDObbligazioneResImpropriaBP") && 
					repannogood.getEsercizio_limite().compareTo(CNRUserContext.getEsercizio(context.getUserContext()))==-1)
					obbligazione.setEsercizio_originale(repannogood.getEsercizio_limite());
			}

			obbligazione.setStato_obbligazione(ObbligazioneBulk.STATO_OBB_PROVVISORIO);

			if (visita.isVisitaStraniero() && visita.isAccordoPagataAdEnteStraniero()) {
				if (visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getCd_terzo_ente()==null)
					throw new it.cnr.jada.comp.ApplicationException("Attenzione! E' previsto il pagamento a favore dell'ente straniero ma sull'accordo non è indicato il codice terzo relativo.");
				obbligazione.setCreditore(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getTerzoEnte());
			} else {
				obbligazione.setCreditore(visita.getBltAutorizzatiDett().getBltAutorizzati().getTerzo());
			}
			
			if (visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero()) {
				if (visita.getTerzoPagamento()==null)
					throw new it.cnr.jada.comp.ApplicationException("Attenzione! E' previsto il pagamento a favore dell'ente straniero ma sulla visita non è indicato il codice terzo relativo.");
				obbligazione.setCreditore(visita.getTerzoPagamento());
			} else {
				obbligazione.setCreditore(visita.getBltAutorizzatiDett().getBltAutorizzati().getTerzo());
			}
			
			
  			obbligazione.setDs_obbligazione("Accordo di cooperazione scientifica tra CNR e "+accordo.getNome_ente_str()+" ("+accordo.getAcronimo_ente_str()+"), "+
					accordo.getNazioneStr().getDs_nazione()+" - "+(accordo.getAnno_fin()-accordo.getAnno_ini()==1?"Biennio":(accordo.getAnno_fin()-accordo.getAnno_ini()==2?"Triennio":"Anni"))+ " " +
					accordo.getAnno_ini().toString()+"-"+accordo.getAnno_fin().toString()+
					". Progetto comune di ricerca "+progetto.getResponsabileIta().getDenominazione_sede().toUpperCase()+" / "+progetto.getResponsabileStr().getDenominazione_sede().toUpperCase()+
					". Soggiorno di ricerca Dott. "+visita.getBltAutorizzatiDett().getBltAutorizzati().getTerzo().getDenominazione_sede());
  				
			java.sql.Timestamp dataReg = null;
			try {
				dataReg = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
			} catch (javax.ejb.EJBException e) {
				throw handleException(e);
			}
			java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
			calendar.setTime(dataReg);
			int esercizioInScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue();
			if (calendar.get(java.util.Calendar.YEAR) != esercizioInScrivania)
				dataReg = new java.sql.Timestamp(new java.text.SimpleDateFormat("dd/MM/yyyy").parse("31/12/" + esercizioInScrivania).getTime());
			obbligazione.setDt_registrazione(dataReg);

			obbligazione.setFl_calcolo_automatico(Boolean.TRUE);
			obbligazione.setIm_obbligazione(visita.getImRimborsoLordoPrevisto().add(visita.getImContributiPrevisto()));

			if (visita.isVisitaStraniero() && (visita.getFlPagamentoConBonifico() || visita.isVisitaPagataAdEnteStraniero()))
				obbligazione.setIm_obbligazione(obbligazione.getIm_obbligazione());
				
			if (visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getElemento_voce() != null)
  				obbligazione.getElemento_voce().setCd_elemento_voce(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getElemento_voce().getCd_elemento_voce());
			obbligazione.setRiportato("N");
  						
			//metodo per riempire immediatamente il campo incarico
			if (visita.isVisitaStraniero() && visita.getIncaricoRepertorio()!=null && visita.getIncaricoRepertorio().getEsercizio()!=null && visita.getIncaricoRepertorio().getPg_repertorio()!=null){
				try	{
					RemoteIterator ri = obbligazioneBP.find(context,null,new Incarichi_repertorioBulk(visita.getIncaricoRepertorio().getEsercizio(),visita.getIncaricoRepertorio().getPg_repertorio()), obbligazione,"find_incarico_repertorio");	
					ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
					if (ri != null && ri.countElements() == 1)
						obbligazione.setIncarico_repertorio((Incarichi_repertorioBulk)ri.nextElement());
					it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
				} catch (Exception e) {
				}
			}

			Obbligazione_scadenzarioBulk scadenza = new Obbligazione_scadenzarioBulk(obbligazione);
			obbligazione.addToObbligazione_scadenzarioColl(scadenza);

			java.util.Calendar calendarScad = java.util.GregorianCalendar.getInstance();
			calendarScad.setTime(dataReg);
			calendarScad.add(Calendar.YEAR, 1);
			scadenza.setDt_scadenza(new java.sql.Timestamp(calendarScad.getTime().getTime()));
			scadenza.setIm_scadenza(obbligazione.getIm_obbligazione());
			scadenza.setDs_scadenza(obbligazione.getDs_obbligazione());
			scadenza.setUser(obbligazione.getUser());
			scadenza.setToBeCreated();
  				
			return obbligazioneBP;
   		} catch(Throwable e) {
   			throw handleException(e);
   		}
   	}
 

	public void validaObbligazione(it.cnr.jada.action.ActionContext context, Obbligazione_scadenzarioBulk obblig) throws ValidationException {
		try {
			Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
			TerzoBulk creditore = obblig.getObbligazione().getCreditore();

			TerzoBulk creditoreVisita=null;
			if (visita.isVisitaStraniero() && visita.isVisitaPagataAdEnteStraniero())
				creditoreVisita = visita.getTerzoPagamento();
			else
				creditoreVisita = visita.getBltAutorizzatiDett().getBltAutorizzati().getTerzo();
			
			if (!creditoreVisita.equalsByPrimaryKey(creditore) &&
				!AnagraficoBulk.DIVERSI.equalsIgnoreCase(creditore.getAnagrafico().getTi_entita())) {
				throw new ValidationException("La scadenza selezionata deve appartenere ad un impegno che ha come creditore il terzo della visita!");
			}
			
			if (visita.isVisitaUniversitario() || (visita.isVisitaStraniero() && !visita.isVisitaPagataAdEnteStraniero())) {
	  			Incarichi_repertorio_annoBulk repannogood = null;
				Incarichi_repertorioBulk incaricoClone = (Incarichi_repertorioBulk)Utility.createIncarichiRepertorioComponentSession().inizializzaBulkPerModifica(context.getUserContext(), visita.getIncaricoRepertorio());
				if (visita.isVisitaStraniero()) {
					for (Iterator iterator = incaricoClone.getIncarichi_repertorio_annoColl().iterator(); iterator.hasNext();) {
						Incarichi_repertorio_annoBulk repanno = (Incarichi_repertorio_annoBulk) iterator.next();
						if (repanno.getImporto_complessivo().compareTo(BigDecimal.ZERO)==1) {
							if (repannogood==null)
								repannogood = repanno;
							else
								throw new ValidationException("Creazione impegno non possibile! L'incarico risulta essere imputato a più esercizi finanziari!");
						}						
					}
					if (repannogood==null)
						throw new ValidationException("Creazione impegno non possibile! L'incarico non risulta essere imputato ad alcun esercizio finanziario!");
					if (repannogood.getEsercizio_limite().compareTo(obblig.getObbligazione().getEsercizio_originale())!=0)
						throw new ValidationException("Creazione impegno non possibile! L'impegno deve appartenere all'esercizio finanziario "+repannogood.getEsercizio_limite()+"!");
				}

				boolean isFontiInterne=false, isFontiEsterne=false;
				for (Iterator iterator = obblig.getObbligazione_scad_voceColl().iterator(); iterator.hasNext();) {
					Obbligazione_scad_voceBulk scadVoce = (Obbligazione_scad_voceBulk) iterator.next();
					if (scadVoce.getLinea_attivita()!=null && scadVoce.getLinea_attivita().getNatura()!=null && scadVoce.getLinea_attivita().getNatura().getTipo()!=null) {
						if (scadVoce.getLinea_attivita().getNatura().getTipo().equals(NaturaBulk.TIPO_NATURA_FONTI_INTERNE))
							isFontiInterne=true;
						if (scadVoce.getLinea_attivita().getNatura().getTipo().equals(NaturaBulk.TIPO_NATURA_FONTI_ESTERNE))
							isFontiEsterne=true;
					}
				}
				if (isFontiInterne && isFontiEsterne)
					throw new ValidationException("Creazione impegno non possibile! L'impegno deve appartenere solo ad una tipologia di fonti (interne o esterne)!");
				if ((incaricoClone.getIncarichi_procedura().getTipo_natura().equals(NaturaBulk.TIPO_NATURA_FONTI_INTERNE) && isFontiEsterne) ||
					(incaricoClone.getIncarichi_procedura().getTipo_natura().equals(NaturaBulk.TIPO_NATURA_FONTI_ESTERNE) && isFontiInterne))
					throw new ValidationException("Creazione impegno non possibile! L'impegno deve appartenere alla stessa tipologia di fonti dell'incarico (fonti "+(isFontiInterne?"esterne":"interne")+")!");
			}
	
			if (visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getElemento_voce() != null) {
				if (!(obblig.getObbligazione().getElemento_voce().getCd_elemento_voce().equals(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getElemento_voce().getCd_elemento_voce())))
					throw new ValidationException("Creazione impegno non possibile! L'impegno deve essere imputato alla voce di bilancio "+visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getElemento_voce().getCd_elemento_voce()+".");
			}
		} catch (ComponentException e) {
			throw new ValidationException("Creazione impegno non possibile! Si è verificato un errore nella ricerca dell'ultimo esercizio aperto.");
		} catch (RemoteException e) {
			throw new ValidationException("Creazione impegno non possibile! Si è verificato un errore nella ricerca dell'ultimo esercizio aperto.");
		}
	}
	@Override
	public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk = super.initializeModelForEdit(actioncontext, oggettobulk);
		if (oggettobulk instanceof Blt_visiteBulk)
			((Blt_visiteBulk)oggettobulk).initializeFase();
		return oggettobulk;
	}
	
	@Override
	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk = super.initializeModelForInsert(actioncontext, oggettobulk);
		if (oggettobulk instanceof Blt_visiteBulk)
			((Blt_visiteBulk)oggettobulk).setFase(Blt_visiteBulk.FASE_PRIMA);
		return oggettobulk;
	}
	
	public void caricaBltAutorizzati(ActionContext actioncontext){
		try	{
			if (getModel()!=null && getModel() instanceof Blt_visiteBulk) {
				Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
				if (visita.getBltAutorizzatiDett()!=null && visita.getBltAutorizzatiDett().getCdAccordo()!=null && visita.getBltAutorizzatiDett().getCdProgetto()!=null &&
					visita.getBltAutorizzatiDett().getCdTerzo()!=null) {
					RemoteIterator ri = find(actioncontext,null,visita.getBltAutorizzatiDett().getBltAutorizzati(),visita,"bltAutorizzatiDett.bltAutorizzati");	
					ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actioncontext, ri);
					if (ri != null && ri.countElements() == 1)
						visita.getBltAutorizzatiDett().setBltAutorizzati((Blt_autorizzatiBulk)ri.nextElement());
					it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(actioncontext,ri);
				}
			}
		} catch (Exception e) {
		}
	}

    public String getFormTitle()
    {
        StringBuffer stringbuffer = new StringBuffer("<script>document.write(document.title)</script>");
        stringbuffer.append(" - ");
        switch(getStatus())
        {
        case 1: // '\001'
            stringbuffer.append("Inserimento");
            break;

        case 2: // '\002'
            if (getModel()!=null) {
            	if (((Blt_visiteBulk)getModel()).isVisitaDipendente())
            		stringbuffer.append("Dipendente/Assimilato - ");
            	else if (((Blt_visiteBulk)getModel()).isVisitaUniversitario())
            		stringbuffer.append("Universitario - ");
            	else if (((Blt_visiteBulk)getModel()).isVisitaStraniero())
            		stringbuffer.append("Straniero - ");
            }
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

    public boolean isFasePrecedenteButtonHidden() {
 		Blt_visiteBulk visita = (Blt_visiteBulk)getModel();
     	if (!isAmministratore() || visita==null || visita.isToBeCreated() || visita.getCrudStatus()==OggettoBulk.UNDEFINED || 
     		visita.isPrimaFase())
     		return true;
     	return false;
    }
    
    public Blt_visiteBulk returnToFasePrecedente(ActionContext actioncontext, Blt_visiteBulk visita) {
    	return visita.returnToFasePrecedente();
    }
}