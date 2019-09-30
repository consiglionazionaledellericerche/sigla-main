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



import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_siope_mandatiBulk;
import it.cnr.contab.doccont00.ejb.ConsRiepilogoSiopeComponentSession;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;


public class ConsRiepilogoSiopeMandatiBP extends BulkBP {
	
	public Parametri_livelliBulk parametriLivelli;
	private String descrizioneClassificazione;
	private String livelloConsultazione;
	private String pathConsultazione;
	private CdsBulk cds_scrivania;
	
	public static final String LIV_BASE= "BASE";
	public static final String LIV_BASEDETT= "DETT";
	
		
		public ConsRiepilogoSiopeComponentSession createConsInviatoSiopeComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
			return (ConsRiepilogoSiopeComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ConsRiepilogoSiopeComponentSession", ConsRiepilogoSiopeComponentSession.class);
		}
		
		public ConsRiepilogoSiopeComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
			return (ConsRiepilogoSiopeComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ConsRiepilogoSiopeComponentSession", ConsRiepilogoSiopeComponentSession.class);
		}
		
		public it.cnr.jada.util.jsp.Button[] createToolbar() {
			it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
			int i = 0;
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.ricerca");
			return toolbar;
		}

		protected void init(Config config, ActionContext context) throws BusinessProcessException {
			
				V_cons_siope_mandatiBulk bulk = new V_cons_siope_mandatiBulk();
				CompoundFindClause clauses = new CompoundFindClause();
				Integer esercizio = CNRUserContext.getEsercizio(context.getUserContext());
			    String cds = CNRUserContext.getCd_cds(context.getUserContext());
			    bulk.setROFindCds(false);
			    
				if(!isUoEnte(context))	 {					
					clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
					clauses.addClause("AND", "cds",SQLBuilder.EQUALS, cds);
					bulk.setCds(new CdsBulk(cds));
					try {
						completeSearchTool(context,bulk,bulk.getBulkInfo().getFieldProperty("find_cds"));
					} catch (ValidationException e) {
					
						e.printStackTrace();
					}
					bulk.setROFindCds(true);
				}
			    
			    if (isUoEnte(context))
					clauses.addClause("AND", "esercizio", SQLBuilder.EQUALS, esercizio);
			    				
				setModel(context,bulk);
				bulk.setEsercizio(esercizio);		
				
			super.init(config, context);
		}
		
		   
		   
		public boolean isUoEnte(ActionContext context){	
			Unita_organizzativaBulk uo = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
			if (uo.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
				return true;	
			return false; 
		}	
	 
		public void setTitle() {
			
			   String title=null;
			   		   title = "Consultazione Riepilogo Codici Siope su Mandati";
			
				getBulkInfo().setShortDescription(title);
			}	

		public boolean isRicercaButtonEnabled()
		{
			return true;
		}
		

		public RemoteIterator find(ActionContext actionContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk,OggettoBulk context,String property) throws it.cnr.jada.action.BusinessProcessException {
			try {
				return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createComponentSession().cerca(actionContext.getUserContext(),clauses,bulk,context,property));
			} catch(Exception e) {
				throw new it.cnr.jada.action.BusinessProcessException(e);
			}
		}

		
	/*	public void valorizzaDate(ActionContext context, V_cons_siope_mandatiBulk bulk) throws ValidationException{
			if ((bulk.getDt_emissione_da()== null||bulk.getDt_emissione_a()==null)&&
				(bulk.getDt_pagamento_da()== null||bulk.getDt_pagamento_a()== null)&&
				(bulk.getDt_trasmissione_da()== null||bulk.getDt_trasmissione_a()== null))
				throw new ValidationException("Valorizzare o le Date di Emissione o le Date di Riscontro o le Date di Trasmissione");
		}*/

		public CdsBulk getCds_scrivania() {
			return cds_scrivania;
		}

		public void setCds_scrivania(CdsBulk cds_scrivania) {
			this.cds_scrivania = cds_scrivania;
		}

		
		
	}
