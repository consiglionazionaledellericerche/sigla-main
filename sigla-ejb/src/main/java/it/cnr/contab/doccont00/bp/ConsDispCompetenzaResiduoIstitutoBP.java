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

package it.cnr.contab.doccont00.bp;



import java.math.BigDecimal;
import java.util.Iterator;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_disp_comp_resBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_disp_comp_res_entBulk;
import it.cnr.contab.doccont00.ejb.ConsDispCompetenzaResiduoIstitutoComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;


/**
 * @author fgiardina
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class ConsDispCompetenzaResiduoIstitutoBP extends ConsultazioniBP {
	public static final String LIV_BASECDS= "BASECDS";
	public static final String LIV_BASECDSPROG= "PROG";
	public static final String LIV_BASECDSPROGCOMM= "COMM";
	public static final String LIV_BASECDSPROGCOMMMOD= "MOD";
	public static final String LIV_BASECDSPROGCOMMMODCDR= "CDR";
	public static final String LIV_BASECDSPROGCOMMMODCDRGAE= "GAE";
	public static final String LIV_BASECDSPROGCOMMMODCDRGAEDET= "DET";
	public static final String LIV_BASECDSCDR= "CDR";
	public static final String LIV_BASECDSCDRGAE= "GAE";
	public static final String LIV_BASECDSCDRGAEDET= "DET";
	
	private String livelloConsultazione;
	private String pathConsultazione;
	

	public static final String LIV_BASECDSVOCE= "VOCE";
	public static final String LIV_BASECDSVOCEPROG= "PROG";
	public static final String LIV_BASECDSVOCEPROGCOMM= "COMM";
	public static final String LIV_BASECDSVOCEPROGCOMMMOD= "MOD";
	public static final String LIV_BASECDSVOCEPROGCOMMMODCDR= "CDR";
	public static final String LIV_BASECDSVOCEPROGCOMMMODCDRGAE= "GAE";
	public static final String LIV_BASECDSVOCEPROGCOMMMODCDRGAEDET= "DET";
	public static final String LIV_BASECDSVOCECDR= "CDR";
	public static final String LIV_BASECDSVOCECDRGAE= "GAE";
	public static final String LIV_BASECDSVOCECDRGAEDET= "DET";
	
	public static final String LIV_DIP= "DIP";
	public static final String LIV_DIPCDS= "CDS";
	public static final String LIV_DIPCDSPROG= "PROG";
	public static final String LIV_DIPCDSPROGCOMM= "COMM";
	public static final String LIV_DIPCDSPROGCOMMMOD= "MOD";
	public static final String LIV_DIPCDSPROGCOMMMODCDR= "CDR";
	public static final String LIV_DIPCDSPROGCOMMMODCDRGAE= "GAE";
	public static final String LIV_DIPCDSPROGCOMMMODCDRGAEDET= "DET";
	public static final String LIV_DIPCDSCDR= "CDR";
	public static final String LIV_DIPCDSCDRGAE= "GAE";
	public static final String LIV_DIPCDSCDRGAEDET= "DET";
	
	public static final String LIV_VOCE= "VOCEVOCE";
	public static final String LIV_VOCENAT= "VOCENAT";
	public static final String LIV_VOCENATMOD= "VOCEMOD";
	public static final String LIV_VOCENATMODCDR= "VOCECDR";
	public static final String LIV_VOCENATMODCDRGAE= "VOCEGAE";
	
	public static final String LIV_ENTCDS= "ENTCDS";
	public static final String LIV_ENTCDSPROG= "PROG";
	public static final String LIV_ENTCDSPROGCOMM= "COMM";
	public static final String LIV_ENTCDSPROGCOMMMOD= "MOD";
	public static final String LIV_ENTCDSPROGCOMMMODCDR= "CDR";
	public static final String LIV_ENTCDSPROGCOMMMODCDRGAE= "GAE";
	public static final String LIV_ENTCDSPROGCOMMMODCDRGAEDET= "DET";
	public static final String LIV_ENTCDSCDR= "CDR";
	public static final String LIV_ENTCDSCDRGAE= "GAE";
	public static final String LIV_ENTCDSCDRGAEDET= "DET";
	
	public static final String LIV_ENTCDSVOCE= "VOCE";
	public static final String LIV_ENTCDSVOCEPROG= "PROG";
	public static final String LIV_ENTCDSVOCEPROGCOMM= "COMM";
	public static final String LIV_ENTCDSVOCEPROGCOMMMOD= "MOD";
	public static final String LIV_ENTCDSVOCEPROGCOMMMODCDR= "CDR";
	public static final String LIV_ENTCDSVOCEPROGCOMMMODCDRGAE= "GAE";
	public static final String LIV_ENTCDSVOCEPROGCOMMMODCDRGAEDET= "DET";
	public static final String LIV_ENTCDSVOCECDR= "CDR";
	public static final String LIV_ENTCDSVOCECDRGAE= "GAE";
	public static final String LIV_ENTCDSVOCECDRGAEDET= "DET";
	
	public ConsDispCompetenzaResiduoIstitutoComponentSession createConsDispCompetenzaResiduoIstitutoComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
			return (ConsDispCompetenzaResiduoIstitutoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ConsDispCompetenzaResiduoIstitutoComponentSession", ConsDispCompetenzaResiduoIstitutoComponentSession.class);
	}

	protected void init(it.cnr.jada.action.Config config,ActionContext context) throws BusinessProcessException {
		if (context.getUserContext() != null){
			Integer esercizio = CNRUserContext.getEsercizio(context.getUserContext());
			String cds = CNRUserContext.getCd_cds(context.getUserContext());
			CompoundFindClause clauses = new CompoundFindClause();
			String uo_scrivania = CNRUserContext.getCd_unita_organizzativa(context.getUserContext());

			Unita_organizzativaBulk uo = new Unita_organizzativaBulk(uo_scrivania);
			if (this instanceof ConsDispCompResIstCdrGaeBP||this instanceof ConsDispCompResIstVoceBP
					||this instanceof ConsDispCompResEntIstCdrGaeBP||this instanceof ConsDispCompResEntIstVoceBP	   )
			{

				if(!isUoEnte(context) && !uo.isUoCds())	 {
					clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
					clauses.addClause("AND", "uo",SQLBuilder.EQUALS, uo_scrivania);
				}
				if (!isUoEnte(context) && uo.isUoCds()) {
					clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
				}
				if (isUoEnte(context))
					clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);

				setBaseclause(clauses);

				if (getPathConsultazione()==null && (this instanceof ConsDispCompResIstCdrGaeBP||this instanceof ConsDispCompResIstVoceBP)) {
					setPathConsultazione(this.LIV_BASECDS);
					setLivelloConsultazione(this.LIV_BASECDS);
				}

				if (getPathConsultazione()==null && (this instanceof ConsDispCompResEntIstCdrGaeBP||this instanceof ConsDispCompResEntIstVoceBP)) {
					setPathConsultazione(this.LIV_ENTCDS);
					setLivelloConsultazione(this.LIV_ENTCDS);
				}

			}
			if (this instanceof ConsDispCompResDipIstBP){

				clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);

				setBaseclause(clauses);
				if (getPathConsultazione()==null) {
					setPathConsultazione(this.LIV_DIP);
					setLivelloConsultazione(this.LIV_DIP);
				}
			}

			if (this instanceof ConsDispCompResVoceNatBP){

				clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);

				setBaseclause(clauses);
				if (getPathConsultazione()==null) {
					setPathConsultazione(this.LIV_VOCE);
					setLivelloConsultazione(this.LIV_VOCE);
				}
			}
		}

			super.init(config,context);
			initVariabili(context,null,getPathConsultazione()); 
		}

	   public void initVariabili(ActionContext context, String pathProvenienza, String livello_destinazione) throws BusinessProcessException {
			if (context.getUserContext() != null){
				try {
					if (this instanceof ConsDispCompResIstVoceBP){
						if ((pathProvenienza==null) && (livello_destinazione.equals(this.LIV_BASECDSVOCE))) {
							setPathConsultazione(this.LIV_BASECDSVOCE);
							setLivelloConsultazione(this.LIV_BASECDSVOCE);
						}
						if (pathConsultazione.equals(this.LIV_BASECDSVOCE) && (livello_destinazione.equals(this.LIV_BASECDSVOCEPROG))){
							setPathConsultazione(this.LIV_BASECDSVOCEPROG);
							setLivelloConsultazione(this.LIV_BASECDSVOCEPROG);
						}
						if (pathConsultazione.equals(this.LIV_BASECDSVOCE) && (livello_destinazione.equals(this.LIV_BASECDSVOCECDR))){
							setPathConsultazione(this.LIV_BASECDSVOCECDR);
							setLivelloConsultazione(this.LIV_BASECDSVOCECDR);
						}
						if (pathProvenienza!=null){
							setPathConsultazione(pathProvenienza.concat(livello_destinazione));
							setLivelloConsultazione(livello_destinazione);
						}
					}
					if (this instanceof ConsDispCompResIstCdrGaeBP || this instanceof ConsDispCompResEntIstCdrGaeBP){

						if ((pathProvenienza == null) && (livello_destinazione.equals(this.LIV_BASECDSPROG))){
							setPathConsultazione(this.LIV_BASECDSPROG);
							setLivelloConsultazione(this.LIV_BASECDSPROG);
						}
						if ((pathProvenienza == null) &&  (livello_destinazione.equals(this.LIV_BASECDSCDR))){
							setPathConsultazione(this.LIV_BASECDSCDR);
							setLivelloConsultazione(this.LIV_BASECDSCDR);
						}
						if (pathProvenienza!=null){
							setPathConsultazione(pathProvenienza.concat(livello_destinazione));
							setLivelloConsultazione(livello_destinazione);
						}
					}

					if (this instanceof ConsDispCompResDipIstBP){

						if ((pathProvenienza == null) && (livello_destinazione.equals(this.LIV_DIPCDS))){
							setPathConsultazione(this.LIV_DIPCDS);
							setLivelloConsultazione(this.LIV_DIPCDS);
						}
						if ((pathProvenienza == null) &&  (livello_destinazione.equals(this.LIV_DIPCDSCDR))){
							setPathConsultazione(this.LIV_DIPCDSCDR);
							setLivelloConsultazione(this.LIV_DIPCDSCDR);
						}
						if (pathProvenienza!=null){
							setPathConsultazione(pathProvenienza.concat(livello_destinazione));
							setLivelloConsultazione(livello_destinazione);
						}
					}

					if (this instanceof ConsDispCompResVoceNatBP){

						if ((pathProvenienza == null) && (livello_destinazione.equals(this.LIV_VOCENAT))){
							setPathConsultazione(this.LIV_VOCENAT);
							setLivelloConsultazione(this.LIV_VOCENAT);
						}
						if (pathProvenienza!=null){
							setPathConsultazione(pathProvenienza.concat(livello_destinazione));
							setLivelloConsultazione(livello_destinazione);
						}
					}

					if (this instanceof  ConsDispCompResEntIstVoceBP){
						if ((pathProvenienza==null) && (livello_destinazione.equals(this.LIV_ENTCDSVOCE))) {
							setPathConsultazione(this.LIV_ENTCDSVOCE);
							setLivelloConsultazione(this.LIV_ENTCDSVOCE);
						}
						if (pathConsultazione.equals(this.LIV_ENTCDSVOCE) && (livello_destinazione.equals(this.LIV_ENTCDSVOCEPROG))){
							setPathConsultazione(this.LIV_ENTCDSVOCEPROG);
							setLivelloConsultazione(this.LIV_ENTCDSVOCEPROG);
						}
						if (pathConsultazione.equals(this.LIV_ENTCDSVOCE) && (livello_destinazione.equals(this.LIV_ENTCDSVOCECDR))){
							setPathConsultazione(this.LIV_ENTCDSVOCECDR);
							setLivelloConsultazione(this.LIV_ENTCDSVOCECDR);
						}
						if (pathProvenienza!=null){
							setPathConsultazione(pathProvenienza.concat(livello_destinazione));
							setLivelloConsultazione(livello_destinazione);
						}

						if (this instanceof ConsDispCompResIstCdrGaeBP || this instanceof ConsDispCompResEntIstCdrGaeBP){

							if ((pathProvenienza == null) && (livello_destinazione.equals(this.LIV_ENTCDSPROG))){
								setPathConsultazione(this.LIV_ENTCDSPROG);
								setLivelloConsultazione(this.LIV_ENTCDSPROG);
							}
							if ((pathProvenienza == null) &&  (livello_destinazione.equals(this.LIV_ENTCDSCDR))){
								setPathConsultazione(this.LIV_ENTCDSCDR);
								setLivelloConsultazione(this.LIV_ENTCDSCDR);
							}
							if (pathProvenienza!=null){
								setPathConsultazione(pathProvenienza.concat(livello_destinazione));
								setLivelloConsultazione(livello_destinazione);
							}
						}
					}
					setSearchResultColumnSet(getPathConsultazione());
					setFreeSearchSet(getPathConsultazione());
					setTitle();

					if (this instanceof ConsDispCompResIstCdrGaeBP)	 {
						if (livello_destinazione.equals(this.LIV_BASECDSPROGCOMMMODCDRGAEDET)||livello_destinazione.equals(this.LIV_BASECDSCDRGAEDET))
							setMultiSelection(false);
					}
					if (this instanceof ConsDispCompResIstVoceBP)	{
						if (livello_destinazione.equals(this.LIV_BASECDSVOCEPROGCOMMMODCDRGAE)||livello_destinazione.equals(this.LIV_BASECDSVOCECDRGAE))
							setMultiSelection(false);
					}
					if (this instanceof ConsDispCompResDipIstBP)	 {
						if (livello_destinazione.equals(this.LIV_DIPCDSPROGCOMMMODCDRGAEDET)||livello_destinazione.equals(this.LIV_DIPCDSCDRGAEDET))
							setMultiSelection(false);
					}
					if (this instanceof ConsDispCompResVoceNatBP)	 {
						if (livello_destinazione.equals(this.LIV_VOCENATMODCDRGAE))
							setMultiSelection(false);
					}
					if (this instanceof ConsDispCompResEntIstCdrGaeBP)	 {
						if (livello_destinazione.equals(this.LIV_ENTCDSPROGCOMMMODCDRGAEDET)||livello_destinazione.equals(this.LIV_ENTCDSCDRGAEDET))
							setMultiSelection(false);
					}
					if (this instanceof  ConsDispCompResEntIstVoceBP)	{
						if (livello_destinazione.equals(this.LIV_ENTCDSVOCEPROGCOMMMODCDRGAE)||livello_destinazione.equals(this.LIV_ENTCDSVOCECDRGAE))
							setMultiSelection(false);
					}
				}

				catch(Throwable e) {
					throw new BusinessProcessException(e);
				}
			}
	   }
	   
	public boolean isUoEnte(ActionContext context){	
			Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
			if (uo.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
				return true;	
			return false; 
	}	

	public String getLivelloConsultazione() {
		return livelloConsultazione;
	}
	
	public void setLivelloConsultazione(String livelloConsultazione) {
		this.livelloConsultazione = livelloConsultazione;
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
	public void setTitle() {
		if (this instanceof ConsDispCompResIstCdrGaeBP|| this instanceof ConsDispCompResIstVoceBP){
		   String title=null;
		   		   title = "Competenza e Residuo Spesa per Istituto";
			
			if (isPresentePROG()||isPresenteVOCEPROG()) title = title.concat(" - Progetto");
			if (isPresenteCOMM()||isPresenteVOCECOMM()) title = title.concat(" - Commessa");
			if (isPresenteMOD()||isPresenteVOCEMOD()) title = title.concat(" - Modulo");
			if (isPresenteCDR()||isPresenteVOCECDR()||isPresenteCDR2()||isPresenteVOCECDR2()) title = title.concat(" - CdR");
			if (isPresenteGAE()||isPresenteVOCEGAE()||isPresenteGAE2()||isPresenteVOCEGAE2()) title = title.concat(" - GAE");
			if (isPresenteDET()||isPresenteDET2()||isPresenteVOCE()) title = title.concat(" - Voce");
		
			getBulkInfo().setShortDescription(title);
		}	
			if (this instanceof ConsDispCompResDipIstBP){
				String title=null;
		   		   title = "Competenza e Residuo per Dipartimento";
		   		if (isPresenteDIPCDS()) title = title.concat(" - Istituto");
		   		if (isPresenteDIPPROG()) title = title.concat(" - Progetto");
				if (isPresenteDIPCOMM()) title = title.concat(" - Commessa");
				if (isPresenteDIPMOD()) title = title.concat(" - Modulo");
				if (isPresenteDIPCDR()||isPresenteDIPCDR2()) title = title.concat(" - CdR");
				if (isPresenteDIPGAE()||isPresenteDIPGAE2()) title = title.concat(" - GAE");
				if (isPresenteDIPDET()||isPresenteDIPDET2()) title = title.concat(" - Voce");
				
				getBulkInfo().setShortDescription(title);
			}
			
			if (this instanceof ConsDispCompResVoceNatBP){
				String title=null;
		   		   title = "Competenza e Residuo per Voce";
		   		if (isPresenteVOCEVOCENAT()) title = title.concat(" - Natura");
		   		if (isPresenteVOCEVOCEMOD()) title = title.concat(" - Modulo");
				if (isPresenteVOCEVOCEMODCDR()) title = title.concat(" - CDR");
				if (isPresenteVOCEVOCEMODCDRGAE()) title = title.concat(" - GAE");
				
				getBulkInfo().setShortDescription(title);
			}
			
			if (this instanceof ConsDispCompResEntIstCdrGaeBP|| this instanceof ConsDispCompResEntIstVoceBP){
				String title=null;
				   title = "Competenza e Residuo Entrate per Istituto";
					
				if (isPresentePROG()||isPresenteVOCEPROG()) title = title.concat(" - Progetto");
				if (isPresenteCOMM()||isPresenteVOCECOMM()) title = title.concat(" - Commessa");
				if (isPresenteMOD()||isPresenteVOCEMOD()) title = title.concat(" - Modulo");
				if (isPresenteCDR()||isPresenteVOCECDR()||isPresenteCDR2()||isPresenteVOCECDR2()) title = title.concat(" - CdR");
				if (isPresenteGAE()||isPresenteVOCEGAE()||isPresenteGAE2()||isPresenteVOCEGAE2()) title = title.concat(" - GAE");
				if (isPresenteDET()||isPresenteDET2()||isPresenteVOCE()) title = title.concat(" - Voce");
				
				getBulkInfo().setShortDescription(title);
			}
		}
	
	
		public boolean isPresenteVOCE() {
			return getPathConsultazione().indexOf(LIV_BASECDSVOCE)>=0;
			}
		
		public boolean isPresenteVOCEPROG() {
		   return getPathConsultazione().indexOf(LIV_BASECDSVOCEPROG)>=0;
		}
	   
		public boolean isPresenteVOCECOMM() {
		   return getPathConsultazione().indexOf(LIV_BASECDSVOCEPROGCOMM)>=0;
		}
		
		public boolean isPresenteVOCEMOD() {
		   return getPathConsultazione().indexOf(LIV_BASECDSVOCEPROGCOMMMOD)>=0;
		}
		
		public boolean isPresenteVOCECDR() {
			  return getPathConsultazione().indexOf(LIV_BASECDSVOCEPROGCOMMMODCDR)>=0;
		}
	   
		public boolean isPresenteVOCEGAE() {
		   return getPathConsultazione().indexOf(LIV_BASECDSVOCEPROGCOMMMODCDRGAE)>=0;
		}
	   
		public boolean isPresenteVOCECDR2() {
			  return getPathConsultazione().indexOf(LIV_BASECDSVOCECDR)>=0;
		}
	   
		public boolean isPresenteVOCEGAE2() {
		   return getPathConsultazione().indexOf(LIV_BASECDSVOCECDRGAE)>=0;
		}
		
		public boolean isPresentePROG() {
			   return getPathConsultazione().indexOf(LIV_BASECDSPROG)>=0;
			}
	   
		public boolean isPresenteCOMM() {
		   return getPathConsultazione().indexOf(LIV_BASECDSPROGCOMM)>=0;
		}
		
		public boolean isPresenteMOD() {
		   return getPathConsultazione().indexOf(LIV_BASECDSPROGCOMMMOD)>=0;
		}
		
		public boolean isPresenteCDR() {
			  return getPathConsultazione().indexOf(LIV_BASECDSPROGCOMMMODCDR)>=0;
		}
	   
		public boolean isPresenteGAE() {
		   return getPathConsultazione().indexOf(LIV_BASECDSPROGCOMMMODCDRGAE)>=0;
		}
	   
		public boolean isPresenteDET() {
		   return getPathConsultazione().indexOf(LIV_BASECDSPROGCOMMMODCDRGAEDET)>=0;
		}
		
		public boolean isPresenteCDR2() {
			  return getPathConsultazione().indexOf(LIV_BASECDSCDR)>=0;
		}
		
		public boolean isPresenteGAE2() {
			   return getPathConsultazione().indexOf(LIV_BASECDSCDRGAE)>=0;
			}
		   
		public boolean isPresenteDET2() {
			   return getPathConsultazione().indexOf(LIV_BASECDSCDRGAEDET)>=0;
		}
		
//		Consultazione per Dipartimento
		public boolean isPresenteDIPCDS() {
			   return getPathConsultazione().indexOf(LIV_DIPCDS)>=0;
		}
		
		public boolean isPresenteDIPPROG() {
			   return getPathConsultazione().indexOf(LIV_DIPCDSPROG)>=0;
		} 
		
		public boolean isPresenteDIPCOMM() {
		   return getPathConsultazione().indexOf(LIV_DIPCDSPROGCOMM)>=0;
		}
		
		public boolean isPresenteDIPMOD() {
		   return getPathConsultazione().indexOf(LIV_DIPCDSPROGCOMMMOD)>=0;
		}
		
		public boolean isPresenteDIPCDR() {
			  return getPathConsultazione().indexOf(LIV_DIPCDSPROGCOMMMODCDR)>=0;
		}
	   
		public boolean isPresenteDIPGAE() {
		   return getPathConsultazione().indexOf(LIV_DIPCDSPROGCOMMMODCDRGAE)>=0;
		}
	   
		public boolean isPresenteDIPDET() {
		   return getPathConsultazione().indexOf(LIV_DIPCDSPROGCOMMMODCDRGAEDET)>=0;
		}
		
		public boolean isPresenteDIPCDR2() {
			  return getPathConsultazione().indexOf(LIV_DIPCDSCDR)>=0;
		}
		
		public boolean isPresenteDIPGAE2() {
			   return getPathConsultazione().indexOf(LIV_DIPCDSCDRGAE)>=0;
			}
		   
		public boolean isPresenteDIPDET2() {
			   return getPathConsultazione().indexOf(LIV_DIPCDSCDRGAEDET)>=0;
		}
		
		
//		Consultazione per Voce - Natura - Modulo - CDR - GAE
		
		public boolean isPresenteVOCEVOCE() {
			   return getPathConsultazione().indexOf(LIV_VOCE)>=0;
		}
		
		public boolean isPresenteVOCEVOCENAT() {
			   return getPathConsultazione().indexOf(LIV_VOCENAT)>=0;
		} 
		
		public boolean isPresenteVOCEVOCEMOD() {
		   return getPathConsultazione().indexOf(LIV_VOCENATMOD)>=0;
		}
		
		public boolean isPresenteVOCEVOCEMODCDR() {
		   return getPathConsultazione().indexOf(LIV_VOCENATMODCDR)>=0;
		}
		
		public boolean isPresenteVOCEVOCEMODCDRGAE() {
			  return getPathConsultazione().indexOf(LIV_VOCENATMODCDRGAE)>=0;
		}
		
//  Consultazioni per le ENTRATE		
		public boolean isPresenteENTVOCE() {
			return getPathConsultazione().indexOf(LIV_ENTCDSVOCE)>=0;
			}
		
		public boolean isPresenteENTVOCEPROG() {
		   return getPathConsultazione().indexOf(LIV_ENTCDSVOCEPROG)>=0;
		}
	   
		public boolean isPresenteENTVOCECOMM() {
		   return getPathConsultazione().indexOf(LIV_ENTCDSVOCEPROGCOMM)>=0;
		}
		
		public boolean isPresenteENTVOCEMOD() {
		   return getPathConsultazione().indexOf(LIV_ENTCDSVOCEPROGCOMMMOD)>=0;
		}
		
		public boolean isPresenteENTVOCECDR() {
			  return getPathConsultazione().indexOf(LIV_ENTCDSVOCEPROGCOMMMODCDR)>=0;
		}
	   
		public boolean isPresenteENTVOCEGAE() {
		   return getPathConsultazione().indexOf(LIV_ENTCDSVOCEPROGCOMMMODCDRGAE)>=0;
		}
	   
		public boolean isPresenteENTVOCECDR2() {
			  return getPathConsultazione().indexOf(LIV_ENTCDSVOCECDR)>=0;
		}
	   
		public boolean isPresenteENTVOCEGAE2() {
		   return getPathConsultazione().indexOf(LIV_ENTCDSVOCECDRGAE)>=0;
		}
		
		public boolean isPresenteENTPROG() {
			   return getPathConsultazione().indexOf(LIV_ENTCDSPROG)>=0;
			}
	   
		public boolean isPresenteENTCOMM() {
		   return getPathConsultazione().indexOf(LIV_ENTCDSPROGCOMM)>=0;
		}
		
		public boolean isPresenteENTMOD() {
		   return getPathConsultazione().indexOf(LIV_ENTCDSPROGCOMMMOD)>=0;
		}
		
		
		public boolean isPresenteENTCDR() {
			  return getPathConsultazione().indexOf(LIV_BASECDSPROGCOMMMODCDR)>=0;
		}
	   
		public boolean isPresenteENTGAE() {
		   return getPathConsultazione().indexOf(LIV_BASECDSPROGCOMMMODCDRGAE)>=0;
		}
	   
		public boolean isPresenteENTDET() {
		   return getPathConsultazione().indexOf(LIV_BASECDSPROGCOMMMODCDRGAEDET)>=0;
		}
		
		public boolean isPresenteENTCDR2() {
			  return getPathConsultazione().indexOf(LIV_BASECDSCDR)>=0;
		}
		
		public boolean isPresenteENTGAE2() {
			   return getPathConsultazione().indexOf(LIV_BASECDSCDRGAE)>=0;
			}
		   
		public boolean isPresenteENTDET2() {
			   return getPathConsultazione().indexOf(LIV_BASECDSCDRGAEDET)>=0;
		}
		
		
		
		public CompoundFindClause getSelezione(ActionContext context) throws BusinessProcessException {
			   try	{
				   CompoundFindClause clauses = null;
				   for (Iterator i = getSelectedElements(context).iterator();i.hasNext();) 
				   {
					   if (this instanceof ConsDispCompResEntIstCdrGaeBP || this instanceof ConsDispCompResEntIstVoceBP ) {
						   V_cons_disp_comp_res_entBulk wpb = (V_cons_disp_comp_res_entBulk)i.next();
					   	CompoundFindClause parzclause = new CompoundFindClause();
					   		parzclause.addClause("AND","cds",SQLBuilder.EQUALS,wpb.getCds());
					   		parzclause.addClause("AND","uo",SQLBuilder.EQUALS,wpb.getUo());
					   		parzclause.addClause("AND","esercizio",SQLBuilder.EQUALS,wpb.getEsercizio());
					   		parzclause.addClause("AND","esercizio_res",SQLBuilder.EQUALS,wpb.getEsercizio_res());
					 	
					   		if (isPresenteENTPROG()||isPresenteENTVOCEPROG()) 
								   parzclause.addClause("AND","progetto",SQLBuilder.EQUALS,wpb.getProgetto());
							   if (isPresenteENTCOMM()||isPresenteENTVOCECOMM()) 
								   parzclause.addClause("AND","commessa",SQLBuilder.EQUALS,wpb.getCommessa());
							   if (isPresenteENTMOD()||isPresenteENTVOCEMOD()) 
								   parzclause.addClause("AND","modulo",SQLBuilder.EQUALS,wpb.getModulo());
							   if (isPresenteENTCDR()||isPresenteENTCDR2()||isPresenteENTVOCECDR()||isPresenteENTVOCECDR2()) 
								   parzclause.addClause("AND","cdr",SQLBuilder.EQUALS,wpb.getCdr());
							   if (isPresenteENTGAE()||isPresenteENTGAE2()||isPresenteENTVOCEGAE()||isPresenteENTVOCEGAE2()) 
								   parzclause.addClause("AND","lda",SQLBuilder.EQUALS,wpb.getLda());
							   if (isPresenteENTDET()||isPresenteENTDET2()||isPresenteENTVOCE()) {
								   parzclause.addClause("AND","cd_voce",SQLBuilder.EQUALS,wpb.getCd_voce());
							   	   parzclause.addClause("AND","cd_elemento_voce",SQLBuilder.EQUALS,wpb.getCd_elemento_voce());
							   }
							   clauses = clauses.or(clauses, parzclause);
					   }
						   else {
							   V_cons_disp_comp_resBulk wpb = (V_cons_disp_comp_resBulk)i.next();
					   CompoundFindClause parzclause = new CompoundFindClause();
					 if (this instanceof ConsDispCompResIstCdrGaeBP|| this instanceof ConsDispCompResIstVoceBP){
					   		parzclause.addClause("AND","cds",SQLBuilder.EQUALS,wpb.getCds());
					   		parzclause.addClause("AND","uo",SQLBuilder.EQUALS,wpb.getUo());
					   		parzclause.addClause("AND","esercizio",SQLBuilder.EQUALS,wpb.getEsercizio());
					   		parzclause.addClause("AND","esercizio_res",SQLBuilder.EQUALS,wpb.getEsercizio_res());
					 	}
					 
					/* if (this instanceof ConsDispCompResDipIstBP) {
						 		parzclause.addClause("AND", "dipartimento", SQLBuilder.EQUALS, wpb.getDipartimento());
						   		parzclause.addClause("AND","esercizio",SQLBuilder.EQUALS,wpb.getEsercizio());
						   		parzclause.addClause("AND","esercizio_res",SQLBuilder.EQUALS,wpb.getEsercizio_res());
					   	}
					   			
					   if (isPresenteDIPCDS()) {
						   parzclause.addClause("AND","cds",SQLBuilder.EQUALS,wpb.getCds());
						   parzclause.addClause("AND","uo",SQLBuilder.EQUALS,wpb.getUo());
					   }
					   if (isPresentePROG()||isPresenteVOCEPROG()||isPresenteDIPPROG()) {
					   		parzclause.addClause("AND","esercizio",SQLBuilder.EQUALS,wpb.getEsercizio());
					   		parzclause.addClause("AND","esercizio_res",SQLBuilder.EQUALS,wpb.getEsercizio_res());
					 	}*/
					 
					 if (this instanceof ConsDispCompResDipIstBP) {
						 		parzclause.addClause("AND", "dipartimento", SQLBuilder.EQUALS, wpb.getDipartimento());
						   		parzclause.addClause("AND","esercizio",SQLBuilder.EQUALS,wpb.getEsercizio());
						   		parzclause.addClause("AND","esercizio_res",SQLBuilder.EQUALS,wpb.getEsercizio_res());
					   	}
					   			
					   if (isPresenteDIPCDS()) {
						   parzclause.addClause("AND","cds",SQLBuilder.EQUALS,wpb.getCds());
						   parzclause.addClause("AND","uo",SQLBuilder.EQUALS,wpb.getUo());
					   }
					   if (isPresentePROG()||isPresenteVOCEPROG()||isPresenteDIPPROG()) 
						   parzclause.addClause("AND","progetto",SQLBuilder.EQUALS,wpb.getProgetto());
					   if (isPresenteCOMM()||isPresenteVOCECOMM()||isPresenteDIPCOMM()) 
						   parzclause.addClause("AND","commessa",SQLBuilder.EQUALS,wpb.getCommessa());
					   if (isPresenteMOD()||isPresenteVOCEMOD()||isPresenteDIPMOD()) 
						   parzclause.addClause("AND","modulo",SQLBuilder.EQUALS,wpb.getModulo());
					   if (isPresenteCDR()||isPresenteCDR2()||isPresenteVOCECDR()||isPresenteVOCECDR2()||isPresenteDIPCDR2()) 
						   parzclause.addClause("AND","cdr",SQLBuilder.EQUALS,wpb.getCdr());
					   if (isPresenteGAE()||isPresenteGAE2()||isPresenteVOCEGAE()||isPresenteVOCEGAE2()||isPresenteDIPGAE2()) 
						   parzclause.addClause("AND","lda",SQLBuilder.EQUALS,wpb.getLda());
					   if (isPresenteDET()||isPresenteDET2()||isPresenteVOCE()||isPresenteDIPDET2()) {
						   parzclause.addClause("AND","cd_voce",SQLBuilder.EQUALS,wpb.getCd_voce());
					   	   parzclause.addClause("AND","cd_elemento_voce",SQLBuilder.EQUALS,wpb.getCd_elemento_voce());
					   }
					   
					   if (this instanceof ConsDispCompResVoceNatBP) {
					   		parzclause.addClause("AND","esercizio",SQLBuilder.EQUALS,wpb.getEsercizio());
					   		parzclause.addClause("AND","esercizio_res",SQLBuilder.EQUALS,wpb.getEsercizio_res());
					   }	
					   
					   if (isPresenteVOCEVOCENAT()) {
						   parzclause.addClause("AND","cd_natura",SQLBuilder.EQUALS,wpb.getCd_natura());
					   }
					   if (isPresenteVOCEVOCEMOD()) {
						   parzclause.addClause("AND","modulo",SQLBuilder.EQUALS,wpb.getModulo());
//						   parzclause.addClause("AND","commessa",SQLBuilder.EQUALS,wpb.getCommessa());
//						   parzclause.addClause("AND","progetto",SQLBuilder.EQUALS,wpb.getProgetto());
					   }
					   if (isPresenteVOCEVOCEMODCDR()) {
						   parzclause.addClause("AND","cdr",SQLBuilder.EQUALS,wpb.getCdr());
					   }
					   if (isPresenteVOCEVOCEMODCDRGAE()) {
						   parzclause.addClause("AND","lda",SQLBuilder.EQUALS,wpb.getLda());
					   }
					   
					   clauses = clauses.or(clauses, parzclause);
				   }
				   }
				   return clauses;
			   }catch(Throwable e) {
				   throw new BusinessProcessException(e);
			   }
		}
		
		public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
		   	if(this instanceof ConsDispCompResIstCdrGaeBP || this instanceof ConsDispCompResEntIstCdrGaeBP){
		   		if (getLivelloConsultazione().equals(this.LIV_BASECDS) || getLivelloConsultazione().equals(this.LIV_ENTCDS)) {
					Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_prog");
					button.setSeparator(true);
					
					listButton.addElement(button);
					Button button2 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_cdscdr");
				   	button2.setSeparator(true);
				   	listButton.addElement(button2);
				}

		   		if (getLivelloConsultazione().equals(this.LIV_BASECDSPROG) || getLivelloConsultazione().equals(this.LIV_ENTCDSPROG)) {
						   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_comm");
						   button.setSeparator(true);
						   listButton.addElement(button);
				}
			   	
				if (getLivelloConsultazione().equals(this.LIV_BASECDSPROGCOMM)|| getLivelloConsultazione().equals(this.LIV_ENTCDSPROGCOMM)) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_mod");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
				
				if (getLivelloConsultazione().equals(this.LIV_ENTCDSPROGCOMMMOD)|| getLivelloConsultazione().equals(this.LIV_ENTCDSPROGCOMMMOD)) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_cdr");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
				
				if ((getLivelloConsultazione().equals(this.LIV_BASECDSPROGCOMMMODCDR)&& getPathConsultazione().equals("BASECDSPROGCOMMMODCDR"))
						||(getLivelloConsultazione().equals(this.LIV_ENTCDSPROGCOMMMODCDR)&& getPathConsultazione().equals("ENTCDSPROGCOMMMODCDR"))) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_gae");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
				
				if ((getLivelloConsultazione().equals(this.LIV_BASECDSPROGCOMMMODCDRGAE) && getPathConsultazione().equals("BASECDSPROGCOMMMODCDRGAE"))
						||(getLivelloConsultazione().equals(this.LIV_ENTCDSPROGCOMMMODCDRGAE) && getPathConsultazione().equals("ENTCDSPROGCOMMMODCDRGAE"))){
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_voce");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
				
				if ((getLivelloConsultazione().equals(this.LIV_BASECDSCDR)&& getPathConsultazione().equals("BASECDSCDR"))
						||(getLivelloConsultazione().equals(this.LIV_ENTCDSCDR)&& getPathConsultazione().equals("ENTCDSCDR"))){
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_cdscdrgae");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
				
				if ((getLivelloConsultazione().equals(this.LIV_BASECDSCDRGAE) && getPathConsultazione().equals("BASECDSCDRGAE"))
						||(getLivelloConsultazione().equals(this.LIV_ENTCDSCDRGAE) && getPathConsultazione().equals("ENTCDSCDRGAE"))){
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_cdscdrgaevoce");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
		   	}
		   	if (this instanceof ConsDispCompResIstVoceBP || this instanceof ConsDispCompResEntIstVoceBP){
		   		
		   		if (getLivelloConsultazione().equals(this.LIV_BASECDS)||getLivelloConsultazione().equals(this.LIV_ENTCDS)) {
					Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_vocevoce");
					button.setSeparator(true); 
					listButton.addElement(button);
			   	}
			   
			   if ((getLivelloConsultazione().equals(this.LIV_BASECDSVOCE) && getPathConsultazione().equals("BASECDSVOCE")) 
					||(getLivelloConsultazione().equals(this.LIV_ENTCDSVOCE) && getPathConsultazione().equals("ENTCDSVOCE"))){
					Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_voceprog");
					button.setSeparator(true);
					listButton.addElement(button);
						   
					Button button2 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_vocecdr2");
					button2.setSeparator(true);
					listButton.addElement(button2);
				}
			   	
			   if (getLivelloConsultazione().equals(this.LIV_BASECDSVOCEPROG)||getLivelloConsultazione().equals(this.LIV_ENTCDSVOCEPROG)) {
				   		Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_vocecomm");
				   		button.setSeparator(true);
				   		listButton.addElement(button);
			   }
			   
			   if (getLivelloConsultazione().equals(this.LIV_BASECDSVOCEPROGCOMM)||getLivelloConsultazione().equals(this.LIV_ENTCDSVOCEPROGCOMM)) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_vocemod");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
				
				if (getLivelloConsultazione().equals(this.LIV_BASECDSVOCEPROGCOMMMOD)||getLivelloConsultazione().equals(this.LIV_ENTCDSVOCEPROGCOMMMOD)) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_vocecdr");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
				
				if ((getLivelloConsultazione().equals(this.LIV_BASECDSVOCEPROGCOMMMODCDR) && getPathConsultazione().equals("BASECDSVOCEPROGCOMMMODCDR"))
						||(getLivelloConsultazione().equals(this.LIV_ENTCDSVOCEPROGCOMMMODCDR) && getPathConsultazione().equals("ENTCDSVOCEPROGCOMMMODCDR"))){
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_vocegae");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
		   	
				if ((getLivelloConsultazione().equals(this.LIV_BASECDSVOCECDR) && getPathConsultazione().equals("BASECDSVOCECDR")) 
						||(getLivelloConsultazione().equals(this.LIV_ENTCDSVOCECDR) && getPathConsultazione().equals("ENTCDSVOCECDR"))){
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_vocegae2");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
		   	}
		   	
			if(this instanceof ConsDispCompResDipIstBP){
				
				if (getLivelloConsultazione().equals(this.LIV_DIP)) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_dipcds");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
		   		if (getLivelloConsultazione().equals(this.LIV_DIPCDS)) {
					Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_dipprog");
					button.setSeparator(true);
					listButton.addElement(button);
					
					Button button2 = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_dipcdscdr");
				   	button2.setSeparator(true);
				   	listButton.addElement(button2);
				}

		   		if (getLivelloConsultazione().equals(this.LIV_DIPCDSPROG)) {
						   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_dipcomm");
						   button.setSeparator(true);
						   listButton.addElement(button);
				}
			   	
				if (getLivelloConsultazione().equals(this.LIV_DIPCDSPROGCOMM)) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_dipmod");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
				
				if (getLivelloConsultazione().equals(this.LIV_DIPCDSPROGCOMMMOD)) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_dipcdr");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
				
				if (getLivelloConsultazione().equals(this.LIV_DIPCDSPROGCOMMMODCDR)&& getPathConsultazione().equals("DIPCDSPROGCOMMMODCDR") ) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_dipgae");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
				
				if (getLivelloConsultazione().equals(this.LIV_DIPCDSPROGCOMMMODCDRGAE) && getPathConsultazione().equals("DIPCDSPROGCOMMMODCDRGAE")) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_dipvoce");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
				
				if (getLivelloConsultazione().equals(this.LIV_DIPCDSCDR)&& getPathConsultazione().equals("DIPCDSCDR")) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_dipcdscdrgae");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
				
				if (getLivelloConsultazione().equals(this.LIV_DIPCDSCDRGAE) && getPathConsultazione().equals("DIPCDSCDRGAE")) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_dipcdscdrgaevoce");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
		   	}
			
			if(this instanceof ConsDispCompResVoceNatBP){
				
				if (getLivelloConsultazione().equals(this.LIV_VOCE)) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_vocevocenat");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
		   		
		   		if (getLivelloConsultazione().equals(this.LIV_VOCENAT)) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_vocevocemod");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
			   	
				if (getLivelloConsultazione().equals(this.LIV_VOCENATMOD)) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_vocevocecdr");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
				
				if (getLivelloConsultazione().equals(this.LIV_VOCENATMODCDR)) {
					   Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.dettagli_vocevocegae");
					   button.setSeparator(true);
					   listButton.addElement(button);
				}
			 }
				return listButton;
		   }
}