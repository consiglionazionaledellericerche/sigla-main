
package it.cnr.contab.prevent01.consultazioni.bp;

import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Iterator;

import javax.ejb.EJBException;

import it.cnr.contab.config00.ejb.Classificazione_vociComponentSession;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.contab.prevent01.consultazioni.bulk.V_cons_pdgg_dipfoBulk;
import it.cnr.contab.prevent01.consultazioni.bulk.V_cons_pdgg_dipfo_speBulk;
import it.cnr.contab.prevent01.consultazioni.ejb.ConsPDGGDipfoComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;

public class ConsPDGGDipFoBP extends ConsultazioniBP {
	public static final String LIVELLO_ETRDIP= "ETRDIP";
	public static final String LIVELLO_SPEDIP= "SPEDIP";
	public static final String LIVELLO_CDR= "CDR";
	public static final String LIVELLO_PRO= "PRO";
	public static final String LIVELLO_DIP= "DIP";
	public static final String LIVELLO_COM= "COM";
	public static final String LIVELLO_TIP= "TIP";
	public static final String LIVELLO_MOD= "MOD";
	public static final String LIVELLO_LIV1= "LIV1";
	public static final String LIVELLO_LIV2= "LIV2";
	public static final String LIVELLO_LIV3= "LIV3";
	public static final String LIVELLO_LIN= "LIN";
	public static final String LIVELLO_VOC= "VOC";
	public static final String LIVELLO_DET= "DET";
	
	private String livelloConsultazione;
	private String pathConsultazione;
	private String ds_livello1;
	private String ds_livello2;
	private String ds_livello3;
	public ConsPDGGDipfoComponentSession createPdggDipfoComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		
		   return (ConsPDGGDipfoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPREVENT01_EJB_ConsPDGGDipfoComponentSession", ConsPDGGDipfoComponentSession.class);
	}

	   public Classificazione_vociComponentSession createClassificazioneVociComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		   return (Classificazione_vociComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Classificazione_vociComponentSession",Classificazione_vociComponentSession.class);
	   }

	   protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		   Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());
			
		   CompoundFindClause clauses = new CompoundFindClause();
		   clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
		   setBaseclause(clauses);
		
		   if (getPathConsultazione()==null) {
				if (this instanceof ConsPDGGDipFoEtrBP){
					setPathConsultazione(this.LIVELLO_ETRDIP);					
					setLivelloConsultazione(this.LIVELLO_ETRDIP);
				} 
				else
				{
					setPathConsultazione(this.LIVELLO_SPEDIP);					
					setLivelloConsultazione(this.LIVELLO_SPEDIP);
				} 
			
				super.init(config,context);
				initVariabili(context, null,getPathConsultazione());   
		   }	 		
	   }
	   public void initVariabili(it.cnr.jada.action.ActionContext context, String pathProvenienza, String livello_destinazione) throws it.cnr.jada.action.BusinessProcessException {
		   try {
			
				 if (pathProvenienza == null && (livello_destinazione.equals(this.LIVELLO_ETRDIP)||livello_destinazione.equals(this.LIVELLO_SPEDIP))){
					if (this instanceof ConsPDGGDipFoEtrBP){
						setPathConsultazione(this.LIVELLO_ETRDIP);
						setLivelloConsultazione(this.LIVELLO_ETRDIP);
					  }else
					  {
						setPathConsultazione(this.LIVELLO_SPEDIP);
						setLivelloConsultazione(this.LIVELLO_SPEDIP);
					  }					  
			   }
			   else
			   {
				   setPathConsultazione(pathProvenienza.concat(livello_destinazione));
				   setLivelloConsultazione(livello_destinazione);
			   }
		
			   setSearchResultColumnSet(getPathConsultazione());
			   setFreeSearchSet(getPathConsultazione());
			   setTitle();
			   setDs_livello1(getDs_livello1(context.getUserContext()));
			   setDs_livello2(getDs_livello2(context.getUserContext()));
			   setDs_livello3(getDs_livello3(context.getUserContext()));
			
		   if (livello_destinazione.equals(this.LIVELLO_DET))
				   setMultiSelection(false);
		   }catch(Throwable e) {
			   throw new BusinessProcessException(e);
		   }
	   }
	
	   public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
		   if (getLivelloConsultazione().equals(this.LIVELLO_ETRDIP)||getLivelloConsultazione().equals(this.LIVELLO_SPEDIP)) {
			   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.pro");
			   button.setSeparator(true);
			   listButton.addElement(button);
			   Button button2 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.fo");
			   button2.setSeparator(true);
			   listButton.addElement(button2);
		   }
			if (getLivelloConsultazione().equals(this.LIVELLO_TIP)) {
				Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.cdr");
				button.setSeparator(true);
				listButton.addElement(button);
			}
			if (getLivelloConsultazione().equals(this.LIVELLO_PRO)) {
					Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.com");
					button.setSeparator(true);
					listButton.addElement(button);
			}
		   	if (getLivelloConsultazione().equals(this.LIVELLO_COM)) {
			   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.cdr");
			   button.setSeparator(true);
			   listButton.addElement(button);
		   	}
			if (getLivelloConsultazione().equals(this.LIVELLO_CDR)) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.modulo");
					   button.setSeparator(true);
					   listButton.addElement(button);
			}
		   	if (getLivelloConsultazione().equals(this.LIVELLO_MOD)) {
			   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.livello1");
			   button.setLabel(getDs_livello1());
			   button.setSeparator(true);
			   listButton.addElement(button);
		   	}
		   	if (getLivelloConsultazione().equals(this.LIVELLO_LIV1)) {
				Button buttonLiv2 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.livello2");
				buttonLiv2.setLabel(getDs_livello2());
				buttonLiv2.setSeparator(true);
				listButton.addElement(buttonLiv2);		   
			}
			if (getLivelloConsultazione().equals(this.LIVELLO_LIV2)) {
				   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.livello3");
				   button.setLabel(getDs_livello3());
				   button.setSeparator(true);
				   listButton.addElement(button);
			   }

		   if (getLivelloConsultazione().equals(this.LIVELLO_LIV3)&& this instanceof ConsPDGGDipFoEtrBP) {
			   
				Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.voce");
				button.setSeparator(true);
				listButton.addElement(button);
		   }
		   else {
		       if (getLivelloConsultazione().equals(this.LIVELLO_LIV3)){
			       Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.linea");
				   button.setSeparator(true);
				   listButton.addElement(button);
			   }
			}
			if (getLivelloConsultazione().equals(this.LIVELLO_LIN)){
				Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.voce");
				button.setSeparator(true);
				listButton.addElement(button);
			}

			if (getLivelloConsultazione().equals(this.LIVELLO_VOC)) {
				Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli");
				button.setSeparator(true);
				listButton.addElement(button);
			}

		   return listButton;
	   }	

	   public String getLivelloConsultazione() {
		   return livelloConsultazione;
	   }
	   public void setLivelloConsultazione(String string) {
		   livelloConsultazione = string;
	   }
	   public String getPathConsultazione() {
		   return pathConsultazione;
	   }
	   public void setPathConsultazione(String string) {
		   pathConsultazione = string;
	   }
	   public String getPathDestinazione(String destinazione) {
		   return getPathConsultazione().concat(destinazione);
	   }
		public boolean isPresenteCDR() {
		   return getPathConsultazione().indexOf(LIVELLO_CDR)>=0;
	   }
	   public boolean isPresenteDIP() {
			return getPathConsultazione().indexOf(LIVELLO_DIP)>=0;
		}
		public boolean isPresenteTIP() {
		   return getPathConsultazione().indexOf(LIVELLO_TIP)>=0;
	   }
		public boolean isPresenteLIV1() {
			   return getPathConsultazione().indexOf(LIVELLO_LIV1)>=0;
		   }
		 public boolean isPresenteLIV2() {
		   return getPathConsultazione().indexOf(LIVELLO_LIV2)>=0;
	   }
		public boolean isPresenteLIV3() {
			  return getPathConsultazione().indexOf(LIVELLO_LIV3)>=0;
		}
		public boolean isPresenteLIN() {
			 return getPathConsultazione().indexOf(LIVELLO_LIN)>=0;
	   }
		public boolean isPresenteVOC() {
			 return getPathConsultazione().indexOf(LIVELLO_VOC)>=0;
	   }
	   public boolean isPresenteCOM() {
		   return getPathConsultazione().indexOf(LIVELLO_COM)>=0;
	   }
	   public boolean isPresentePRO() {
		  return getPathConsultazione().indexOf(LIVELLO_PRO)>=0;
	   }
	   public boolean isPresenteMOD() {
		   return getPathConsultazione().indexOf(LIVELLO_MOD)>=0;
	   }
	   public boolean isPresenteDET() {
		   return getPathConsultazione().indexOf(LIVELLO_DET)>=0;
	   }
	   /**
		* Setta il titolo della mappa di consultazione (BulkInfo.setShortDescription e BulkInfo.setLongDescription)
		* sulla base del path della consultazione
		*/
	   public void setTitle() {
		   String title=null;
		   if (this instanceof ConsPDGGDipFoEtrBP)
			   title = "Riepilogo PDGG per Istituto-F.O. Entrate";
		   else
			   title = "Riepilogo PDGG per Istituto-F.O. Spese";
		
			
			if (isPresenteDIP()) title = title.concat(" - Dipartimento");
			if (isPresentePRO()) title = title.concat("\\Progetto");
			if (isPresenteCOM()) title = title.concat("\\Commessa");
			if (isPresenteCDR()) title = title.concat("\\CdR");
			if (isPresenteTIP()) title = title.concat("\\Funz.Obiettivo");
			if (isPresenteMOD()) title = title.concat("\\Modulo");
			if (isPresenteLIV1()) title = title.concat("\\Titolo");
			if (isPresenteLIV2()) title = title.concat("\\Categoria");
			if (isPresenteLIV3()) title = title.concat("\\").concat(getDs_livello3());
			if (isPresenteLIN()) title = title.concat("\\GAE");
			if (isPresenteVOC()) title = title.concat("\\Voce");
			if (isPresenteDET()) title = title.concat("\\Dettagli");
		   getBulkInfo().setShortDescription(title);
		   if (this instanceof ConsPDGGDipFoEtrBP)
			   getBulkInfo().setLongDescription("Riepilogo PDGG Entrate");
		   else
			   getBulkInfo().setLongDescription("Riepilogo PDGG Spese");
	   }
	   public String getDs_livello1() {
		   return ds_livello1;
	   }
	   public String getDs_livello2() {
		   return ds_livello2;
	   }
	   public void setDs_livello1(String string) {
		   ds_livello1 = string;
	   }
	   public void setDs_livello2(String string) {
		   ds_livello2 = string;
	   }
	 
	   /**
		* Ritorna la descrizione del primo livello della classificazione ufficiale
		*
		* @param userContext il context di riferimento 
		* @return String la descrizione del livello richiesto 
		*/
	   public String getDs_livello1(UserContext userContext) throws BusinessProcessException {
		   try {
			   if (getDs_livello1()==null) {
				   if ( this instanceof ConsPDGGDipFoSpeBP)
					   setDs_livello1(createClassificazioneVociComponentSession().getDsLivelloClassificazione(userContext, 
																											  CNRUserContext.getEsercizio(userContext),
																											  Elemento_voceHome.GESTIONE_SPESE,
																											  new Integer(Classificazione_vociHome.LIVELLO_PRIMO)));
				   else
					   setDs_livello1(createClassificazioneVociComponentSession().getDsLivelloClassificazione(userContext, 
																											  CNRUserContext.getEsercizio(userContext),
																											  Elemento_voceHome.GESTIONE_ENTRATE,
																											  new Integer(Classificazione_vociHome.LIVELLO_PRIMO)));
			   }
			   return getDs_livello1();
		   }catch(Throwable e) {
			   throw new BusinessProcessException(e);
		   }
	   }
	   /**
		* Ritorna la descrizione del secondo livello della classificazione ufficiale
		*
		* @param userContext il context di riferimento 
		* @return String la descrizione del livello richiesto 
		*/
	   public String getDs_livello2(UserContext userContext) throws it.cnr.jada.action.BusinessProcessException {
		   try {
			   if (getDs_livello2()==null) {
				   if (this instanceof ConsPDGGDipFoSpeBP)
					   setDs_livello2(createClassificazioneVociComponentSession().getDsLivelloClassificazione(userContext, 
																											  CNRUserContext.getEsercizio(userContext),
																											  Elemento_voceHome.GESTIONE_SPESE,
																											  new Integer(Classificazione_vociHome.LIVELLO_SECONDO)));
				   else
					   setDs_livello2(createClassificazioneVociComponentSession().getDsLivelloClassificazione(userContext, 
																											  CNRUserContext.getEsercizio(userContext),
																											  Elemento_voceHome.GESTIONE_ENTRATE,
																											  new Integer(Classificazione_vociHome.LIVELLO_SECONDO)));
			   }
			   return getDs_livello2();
		   }catch(Throwable e) {
			   throw new BusinessProcessException(e);
		   }
	   }
		public String getDs_livello3(UserContext userContext) throws it.cnr.jada.action.BusinessProcessException {
			  try {
				  if (getDs_livello3()==null) {
					  if (this instanceof ConsPDGGDipFoSpeBP)
						  setDs_livello3(createClassificazioneVociComponentSession().getDsLivelloClassificazione(userContext, 
																												 CNRUserContext.getEsercizio(userContext),
																												 Elemento_voceHome.GESTIONE_SPESE,
																												 new Integer(Classificazione_vociHome.LIVELLO_TERZO)));
					  else
						  setDs_livello3(createClassificazioneVociComponentSession().getDsLivelloClassificazione(userContext, 
																												 CNRUserContext.getEsercizio(userContext),
																												 Elemento_voceHome.GESTIONE_ENTRATE,
																												 new Integer(Classificazione_vociHome.LIVELLO_TERZO)));
				  }
				  return getDs_livello3();
			  }catch(Throwable e) {
				  throw new BusinessProcessException(e);
			  }
		  }

	   /**
		* Ritorna la CompoundFindClause ottenuta in base alla selezione effettuata
		*
		* @param field il campo da aggiornare 
		* @param label il nuovo valore da sostituire al vecchio
		*/
	   public CompoundFindClause getSelezione(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		   try	{
			   CompoundFindClause clauses = null;
			   for (Iterator i = getSelectedElements(context).iterator();i.hasNext();) 
			   {
				   V_cons_pdgg_dipfoBulk wpb = (V_cons_pdgg_dipfoBulk)i.next();
				   CompoundFindClause parzclause = new CompoundFindClause();
	
				   if (isPresenteCDR()) 
					   parzclause.addClause("AND","cd_centro_responsabilita",SQLBuilder.EQUALS,wpb.getCd_centro_responsabilita());
					if (isPresenteTIP()) 
						parzclause.addClause("AND","cd_tipo_modulo",SQLBuilder.EQUALS,wpb.getCd_tipo_modulo());
					if (isPresenteDIP()) 
						parzclause.addClause("AND","cd_dipartimento",SQLBuilder.EQUALS,wpb.getCd_dipartimento());	
					if (isPresentePRO()) 
						parzclause.addClause("AND","cd_progetto",SQLBuilder.EQUALS,wpb.getCd_progetto());
					if (isPresenteCOM()) 
						parzclause.addClause("AND","cd_commessa",SQLBuilder.EQUALS,wpb.getCd_commessa());
				   if (isPresenteLIV1()) 
					   parzclause.addClause("AND","cd_livello1",SQLBuilder.EQUALS,wpb.getCd_livello1());
				   if (isPresenteLIV2()) 
					   parzclause.addClause("AND","cd_livello2",SQLBuilder.EQUALS,wpb.getCd_livello2());
					if (isPresenteLIV3()) 
						parzclause.addClause("AND","cd_livello3",SQLBuilder.EQUALS,wpb.getCd_livello3());		   	   
				   if (isPresenteMOD()) 
					   parzclause.addClause("AND","cd_modulo",SQLBuilder.EQUALS,wpb.getCd_modulo());
				   if (isPresenteVOC()) 
					   parzclause.addClause("AND","cd_elemento_voce",SQLBuilder.EQUALS,wpb.getCd_elemento_voce());
				   if (isPresenteLIN()) 
					   parzclause.addClause("AND","cd_linea_attivita",SQLBuilder.EQUALS,wpb.getCd_linea_attivita());
				   if (isPresenteDET()) 
					   parzclause.addClause("AND","cd_classificazione",SQLBuilder.EQUALS,wpb.getCd_classificazione());
	
				   clauses = clauses.or(clauses, parzclause);
			   }
			   return clauses;
		   }catch(Throwable e) {
			   throw new BusinessProcessException(e);
		   }
	   } 
	/**
	 * @return
	 */
	public String getDs_livello3() {
		return ds_livello3;
	}

	/**
	 * @param string
	 */
	public void setDs_livello3(String string) {
		ds_livello3 = string;
	}
	public String getLabelCd_livello1(){
			return ds_livello1;
	}
	public String getLabelCd_livello2(){
			return ds_livello2;
	}
	public String getLabelCd_livello3(){
		return ds_livello3;
	}
	public String getColumnLabelCd_livello1(){
				return ds_livello1;
	}
	public String getColumnLabelCd_livello2(){
				return ds_livello2;
	}	
	public String getColumnLabelCd_livello3(){
			return ds_livello3;
	}
	
   }
