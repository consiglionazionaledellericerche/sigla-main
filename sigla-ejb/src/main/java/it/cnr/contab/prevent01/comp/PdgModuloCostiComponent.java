/*
 * Created on Oct 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.comp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Optional;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.latt.bulk.CofogBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.LimiteSpesaBulk;
import it.cnr.contab.config00.pdcfin.bulk.LimiteSpesaHome;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociHome;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.pdg01.consultazioni.bulk.V_cons_pdgp_pdgg_speBulk;
import it.cnr.contab.prevent01.bp.CRUDDettagliModuloCostiBP;
import it.cnr.contab.prevent01.bulk.Ass_pdg_missione_tipo_uoBulk;
import it.cnr.contab.prevent01.bulk.Ass_pdg_missione_tipo_uoHome;
import it.cnr.contab.prevent01.bulk.Pdg_contrattazione_speseBulk;
import it.cnr.contab.prevent01.bulk.Pdg_contrattazione_speseHome;
import it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk;
import it.cnr.contab.prevent01.bulk.Pdg_esercizioHome;
import it.cnr.contab.prevent01.bulk.Pdg_missioneBulk;
import it.cnr.contab.prevent01.bulk.Pdg_missioneHome;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_costiBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_costiHome;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_other_fieldBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PdgModuloCostiComponent extends CRUDComponent {

	public PdgModuloCostiComponent() {
		super();
	}
	public boolean esisteBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {
		try {
			Pdg_modulo_costiHome home = (Pdg_modulo_costiHome)getHome(userContext, Pdg_modulo_costiBulk.class);
			if (home.findByPrimaryKey(bulk) != null)
			  return true;
			return false;			  
		}catch (it.cnr.jada.comp.ComponentException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} 	
	}
	public OggettoBulk inizializzaBulkPerInserimento( UserContext usercontext,OggettoBulk oggettobulk)throws ComponentException {
		Pdg_modulo_costiBulk testata = (Pdg_modulo_costiBulk)super.inizializzaBulkPerInserimento(usercontext, oggettobulk);
		try {
			Integer anno_precedente = new Integer(CNRUserContext.getEsercizio(usercontext).intValue() -1);
			Pdg_modulo_costiHome testataHome = (Pdg_modulo_costiHome)getHome(usercontext, Pdg_modulo_costiBulk.class);
			testata.setTot_massa_spendibile_anno_prec(
			     calcolaImporto(usercontext, testataHome.calcolaTotMassaSpendibileAnnoPrecedente(usercontext,testata,anno_precedente)).add(
			       testata.getTot_risorse_provenienti_es_prec()));
			       
			testata.setTot_massa_spendibile_anno_in_corso(calcolaImporto(usercontext, testataHome.calcolaTotMassaSpendibileAnnoInCorsoCosti(usercontext,testata)).add(
														  calcolaImporto(usercontext, testataHome.calcolaTotMassaSpendibileAnnoInCorsoSpese(usercontext,testata))));
			testata.setValore_presunto_anno_in_corso(calcolaImporto(usercontext, testataHome.calcolaValorePresuntoAttivitaAnnoInCorsoCosti(usercontext,testata)).add(
														  calcolaImporto(usercontext, testataHome.calcolaValorePresuntoAttivitaAnnoInCorsoSpese(usercontext,testata))));
			testata.setTot_entr_fonti_est_anno_in_corso(calcolaImporto(usercontext, testataHome.calcolaTotEntratePrevisteAnnoInCorso(usercontext,testata)));
			testata.setTot_spese_coperte_fonti_esterne_anno_in_corso(calcolaImporto(usercontext, testataHome.calcolaTotaleSpeseCoperteFontiEsterneAnnoInCorso(usercontext,testata)));
			
			testata.setSpese_decentrate_fonti_interne_istituto(calcolaImporto(usercontext, 
			    testataHome.calcolaSpeseDecentrateFontiInterneIstituto(usercontext,testata)));		
			testata.setSpese_decentrate_fonti_interne_aree(calcolaImporto(usercontext, 
				testataHome.calcolaSpeseDecentrateFontiInterneArea(usercontext,testata)));
						
			testata.setSpese_accentrate_fonti_interne_istituto(calcolaImporto(usercontext, 
				testataHome.calcolaSpeseAccentrateFontiInterneIstituto(usercontext,testata)));		
			testata.setSpese_accentrate_fonti_interne_aree(calcolaImporto(usercontext, 
				testataHome.calcolaSpeseAccentrateFontiInterneArea(usercontext,testata)));
						
			testata.setTot_massa_spendibile_anno_in_corso(Utility.ZERO);
		}catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}		
		return testata;
	}

	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext,OggettoBulk oggettobulk)throws ComponentException {
		try {
			Integer anno_precedente = new Integer(CNRUserContext.getEsercizio(usercontext).intValue() -1);
			Pdg_modulo_costiBulk testata = (Pdg_modulo_costiBulk)super.inizializzaBulkPerModifica(usercontext,oggettobulk);
			Pdg_modulo_costiHome testataHome = (Pdg_modulo_costiHome)getHome(usercontext, Pdg_modulo_costiBulk.class);
			testata.setDettagliSpese(new it.cnr.jada.bulk.BulkList(testataHome.findPdgModuloSpeseDettagli(usercontext, testata)));
			testata.setDettagliContrSpese(new it.cnr.jada.bulk.BulkList(testataHome.findPdgModuloContrSpeseDettagli(usercontext, testata)));

			testata.getPdg_modulo().getProgetto().setOtherField(
					(Progetto_other_fieldBulk)getHome(usercontext, Progetto_other_fieldBulk.class)
					.findByPrimaryKey(new Progetto_other_fieldBulk(testata.getPdg_modulo().getProgetto().getPg_progetto())));

			getHomeCache(usercontext).fetchAll(usercontext);
			for(Iterator dettagli = testata.getDettagliSpese().iterator(); dettagli.hasNext();){
				Pdg_modulo_speseBulk pdg_modulo_spese = (Pdg_modulo_speseBulk)dettagli.next();
				try {
					pdg_modulo_spese.setSpeseScaricateDalPersonale(CRUDDettagliModuloCostiBP.getCostiDipendenteComponentSession().isSpeseFromScaricoDipendente(usercontext,pdg_modulo_spese));
				}catch (RemoteException e) {
					throw new ComponentException(e);
				}
			}
			testata.setTot_massa_spendibile_anno_prec(
			   calcolaImporto(usercontext, testataHome.calcolaTotMassaSpendibileAnnoPrecedente(usercontext,testata,anno_precedente)).add(
			     testata.getTot_risorse_provenienti_es_prec()));
			testata.setTot_massa_spendibile_anno_in_corso(calcolaImporto(usercontext, testataHome.calcolaTotMassaSpendibileAnnoInCorsoCosti(usercontext,testata)).add(
			                                              calcolaImporto(usercontext, testataHome.calcolaTotMassaSpendibileAnnoInCorsoSpese(usercontext,testata))));
			testata.setValore_presunto_anno_in_corso(calcolaImporto(usercontext, testataHome.calcolaValorePresuntoAttivitaAnnoInCorsoCosti(usercontext,testata)).add(
														  calcolaImporto(usercontext, testataHome.calcolaValorePresuntoAttivitaAnnoInCorsoSpese(usercontext,testata))));
			testata.setTot_entr_fonti_est_anno_in_corso(calcolaImporto(usercontext, testataHome.calcolaTotEntratePrevisteAnnoInCorso(usercontext,testata)));
			testata.setTot_spese_coperte_fonti_esterne_anno_in_corso(calcolaImporto(usercontext, testataHome.calcolaTotaleSpeseCoperteFontiEsterneAnnoInCorso(usercontext,testata)));

			testata.setSpese_decentrate_fonti_interne_istituto(calcolaImporto(usercontext, 
				testataHome.calcolaSpeseDecentrateFontiInterneIstituto(usercontext,testata)));		
			testata.setSpese_decentrate_fonti_interne_aree(calcolaImporto(usercontext, 
				testataHome.calcolaSpeseDecentrateFontiInterneArea(usercontext,testata)));
						
			testata.setSpese_accentrate_fonti_interne_istituto(calcolaImporto(usercontext, 
				testataHome.calcolaSpeseAccentrateFontiInterneIstituto(usercontext,testata)));		
			testata.setSpese_accentrate_fonti_interne_aree(calcolaImporto(usercontext, 
				testataHome.calcolaSpeseAccentrateFontiInterneArea(usercontext,testata)));
			                                             
			calcolaPrevisioneAssestata(usercontext,testata,anno_precedente);

			Pdg_contrattazione_speseHome pdgContrHome = (Pdg_contrattazione_speseHome)getHome(usercontext, Pdg_contrattazione_speseBulk.class);
			for(Iterator dettagli = testata.getDettagliContrSpese().iterator(); dettagli.hasNext();){
				Pdg_contrattazione_speseBulk pdg_contrattazione_spese = (Pdg_contrattazione_speseBulk)dettagli.next();
				pdg_contrattazione_spese.setTotalePropostoModificatoFE(calcolaImporto(usercontext, 
					pdgContrHome.calcolaTotalePropostoModificatoFE(usercontext,pdg_contrattazione_spese,pdg_contrattazione_spese.getArea())));
				pdg_contrattazione_spese.setTotalePropostoModificatoFI(calcolaImporto(usercontext, 
					pdgContrHome.calcolaTotalePropostoModificatoFI(usercontext,pdg_contrattazione_spese,pdg_contrattazione_spese.getArea())));
			}
			
			return testata;
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}		
	}
	public SQLBuilder selectAreaByClause (UserContext userContext,Pdg_modulo_speseBulk dett,CdsBulk ass_uo,CompoundFindClause clause) throws ComponentException, PersistencyException
	{
			SQLBuilder sql = ((CdsHome)getHome(userContext, CdsBulk.class)).createSQLBuilder();
			if (clause != null) 
			  sql.addClause(clause);			
			sql.addTableToHeader("ASS_UO_AREA");
		    sql.addTableToHeader("UNITA_ORGANIZZATIVA UO");
		    sql.addTableToHeader("UNITA_ORGANIZZATIVA CDS");
			sql.addSQLClause("AND", "ASS_UO_AREA.ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
		    sql.addSQLClause("AND", "UO.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, dett.getPdg_modulo_costi().getPdg_modulo().getCdr().getCd_unita_organizzativa());
		    sql.addSQLJoin("CDS.CD_UNITA_PADRE","UO.CD_UNITA_PADRE");
		    sql.addSQLJoin("CDS.CD_UNITA_ORGANIZZATIVA","ASS_UO_AREA.CD_UNITA_ORGANIZZATIVA");
		    sql.addSQLJoin("ASS_UO_AREA.CD_AREA_RICERCA","UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
		    
		    SQLBuilder sql2 = ((CdsHome)getHome(userContext, CdsBulk.class)).createSQLBuilder();
			sql2.addTableToHeader("UNITA_ORGANIZZATIVA UO");
			sql2.addSQLClause("AND", "UO.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, dett.getPdg_modulo_costi().getPdg_modulo().getCdr().getCd_unita_organizzativa());
			sql2.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA","UO.CD_UNITA_PADRE");
			return sql.union(sql2,true);
	}
	
	public SQLBuilder selectClassificazioneByClause (UserContext userContext,Pdg_modulo_speseBulk dett,V_classificazione_vociBulk classificazione,CompoundFindClause clause) throws ComponentException, PersistencyException
	{
			SQLBuilder sql = ((V_classificazione_vociHome)getHome(userContext, V_classificazione_vociBulk.class)).createSQLBuilder();
			sql.addTableToHeader("PARAMETRI_CNR");
			sql.addTableToHeader("CDR");
			sql.addTableToHeader("UNITA_ORGANIZZATIVA");
			sql.addSQLClause("AND", "PARAMETRI_CNR.ESERCIZIO", sql.EQUALS, dett.getEsercizio());
			sql.addSQLJoin("PARAMETRI_CNR.LIVELLO_PDG_DECIS_SPE","V_CLASSIFICAZIONE_VOCI.NR_LIVELLO");
			sql.addSQLClause("AND", "CDR.CD_CENTRO_RESPONSABILITA", sql.EQUALS, dett.getCd_centro_responsabilita());
			sql.addSQLJoin("CDR.CD_UNITA_ORGANIZZATIVA","UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
		    sql.openParenthesis("AND");
		      sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI.FL_ACCENTRATO", sql.EQUALS, "Y");
		      sql.addSQLClause("OR", "V_CLASSIFICAZIONE_VOCI.FL_DECENTRATO", sql.EQUALS, "Y");
		    sql.closeParenthesis();
			sql.openParenthesis("AND");
				sql.addSQLClause("AND", "UNITA_ORGANIZZATIVA.CD_TIPO_UNITA", sql.EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_SAC);
				//sql.addClause("AND", "V_CLASSIFICAZIONE_VOCI.FL_CLASS_SAC", sql.EQUALS, Boolean.TRUE);
				sql.openParenthesis("OR");		        
				  sql.addSQLClause("AND", "UNITA_ORGANIZZATIVA.CD_TIPO_UNITA", sql.NOT_EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_SAC);
				  sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI.FL_CLASS_SAC", sql.EQUALS, "N");
				sql.closeParenthesis();  		      
			sql.closeParenthesis();
		    sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI.ESERCIZIO", sql.EQUALS, dett.getEsercizio());
		    sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI.TI_GESTIONE", sql.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
		    sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI.FL_SOLO_GESTIONE", sql.EQUALS,"N");
			if (clause != null) 
			  sql.addClause(clause);
			return sql;
	}
	private BigDecimal calcolaImporto(UserContext userContext, SQLBuilder sql) throws ComponentException{
		BigDecimal totale = Utility.ZERO;
		try {
			java.sql.ResultSet rs = null;
			LoggableStatement ps = null;
			try {
				ps = sql.prepareStatement(getConnection(userContext));
				try {
					rs = ps.executeQuery();
					if (rs.next() && rs.getBigDecimal(1)!= null)
					  totale = totale.add(rs.getBigDecimal(1));
				} catch (java.sql.SQLException e) {
					throw handleSQLException(e);
				} finally {
					if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
				}
			} finally {
				if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
			}
		} catch (java.sql.SQLException ex) {
			throw handleException(ex);
		}		
		return totale;
	}
	public OggettoBulk calcolaPrevisioneAssestataRowByRow(UserContext usercontext,Pdg_modulo_costiBulk testata, Pdg_modulo_speseBulk pdg_modulo_spese,Integer anno_precedente)throws ComponentException {
		try {
			Pdg_modulo_speseHome speseHome = (Pdg_modulo_speseHome)getHome(usercontext, Pdg_modulo_speseBulk.class);		
			pdg_modulo_spese.setPrev_ass_imp_int(calcolaImporto(usercontext,speseHome.calcolaPrevisioneAssestataAnnoPrecedente(usercontext,pdg_modulo_spese,anno_precedente,NaturaBulk.TIPO_NATURA_FONTI_INTERNE)));
			pdg_modulo_spese.setPrev_ass_imp_est(calcolaImporto(usercontext,speseHome.calcolaPrevisioneAssestataAnnoPrecedente(usercontext,pdg_modulo_spese,anno_precedente,NaturaBulk.TIPO_NATURA_FONTI_ESTERNE)));
		} catch (IntrospectionException e) {
			throw handleException(e);
		} catch (PersistencyException e) {
			throw handleException(e);
		}							
		return testata;
	}
	private OggettoBulk calcolaPrevisioneAssestata(UserContext usercontext,OggettoBulk oggettobulk,Integer anno_precedente)throws ComponentException {
		Pdg_modulo_costiBulk testata = (Pdg_modulo_costiBulk)oggettobulk;
		for (java.util.Iterator dettagli = testata.getDettagliSpese().iterator();dettagli.hasNext();){
			Pdg_modulo_speseBulk pdg_modulo_spese = (Pdg_modulo_speseBulk)dettagli.next();
			calcolaPrevisioneAssestataRowByRow(usercontext,testata,pdg_modulo_spese,anno_precedente);
		}
		return testata;
	}	

	public OggettoBulk cercaPdgEsercizio(UserContext usercontext,Pdg_moduloBulk pdg_modulo) throws ComponentException {
		try {
			Pdg_esercizioBulk pdg_esercizio = new Pdg_esercizioBulk(CNRUserContext.getEsercizio(usercontext),pdg_modulo.getCd_centro_responsabilita());
			Pdg_esercizioHome esHome = (Pdg_esercizioHome)getHome(usercontext, pdg_esercizio);
			pdg_esercizio = (Pdg_esercizioBulk) esHome.findByPrimaryKey(pdg_esercizio);

			return pdg_esercizio;

		} catch (PersistencyException e) {
			throw handleException(e);
		}							
	}
	public Pdg_modulo_speseBulk getPdgModuloSpeseBulk(it.cnr.jada.UserContext userContext, V_cons_pdgp_pdgg_speBulk consPdg) throws ComponentException {
		try {
			Pdg_modulo_speseHome pdgSpehome = (Pdg_modulo_speseHome)getHome(userContext, Pdg_modulo_speseBulk.class);
			Pdg_modulo_speseBulk pdgSpeBulk = (Pdg_modulo_speseBulk)pdgSpehome.findByPrimaryKey(new Pdg_modulo_speseBulk(consPdg.getEsercizio(),consPdg.getCd_centro_responsabilita(),consPdg.getPg_progetto(),consPdg.getId_classificazione(),consPdg.getCd_cds_area(),consPdg.getPg_dettaglio()));
			return pdgSpeBulk;
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
	public boolean soggettaLimite(UserContext usercontext,Pdg_modulo_speseBulk pdg_modulo_spese,String fonte)throws ComponentException {
		try {
			if (pdg_modulo_spese!=null && pdg_modulo_spese.getId_classificazione()!=null){
				SQLBuilder sql = ((Elemento_voceHome)getHome(usercontext, Elemento_voceBulk.class)).createSQLBuilder();
				sql.addSQLClause("AND","ID_CLASSIFICAZIONE" ,SQLBuilder.EQUALS,pdg_modulo_spese.getId_classificazione());
				sql.addSQLClause("AND","FL_LIMITE_SPESA",SQLBuilder.EQUALS,"Y");
				if (sql.executeExistsQuery(getConnection(usercontext))){
					Elemento_voceHome home_voce =(Elemento_voceHome)getHome(usercontext,Elemento_voceBulk.class);
					java.util.List voci=home_voce.fetchAll(sql);
					for(Iterator i=voci.iterator();i.hasNext();){
						Elemento_voceBulk voceBulk = (Elemento_voceBulk)i.next();
						SQLBuilder sql2 = ((LimiteSpesaHome)getHome(usercontext, LimiteSpesaBulk.class)).createSQLBuilder();
						sql2.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,voceBulk.getEsercizio());
						sql2.addSQLClause("AND","TI_APPARTENENZA",SQLBuilder.EQUALS,voceBulk.getTi_appartenenza());
						sql2.addSQLClause("AND","TI_GESTIONE",SQLBuilder.EQUALS,voceBulk.getTi_gestione());
						sql2.addSQLClause("AND","CD_ELEMENTO_VOCE",SQLBuilder.EQUALS,voceBulk.getCd_elemento_voce());
						sql2.openParenthesis("AND");
						sql2.addSQLClause("AND","FONTE",SQLBuilder.EQUALS,fonte);
						sql2.addSQLClause("OR","FONTE",SQLBuilder.EQUALS,LimiteSpesaBulk.FONTE_INTERNA_E_ESTERNA);
						sql2.closeParenthesis();
						if(sql2.executeExistsQuery(getConnection(usercontext)))
								return true;
					}
				}
			}
			return false;
		} catch(Throwable e) {
			throw handleException(e);
		}	
	}
	public SQLBuilder selectCofogByClause(UserContext userContext, Pdg_modulo_speseBulk dettaglio, CofogBulk cofog, CompoundFindClause clauses) 
			throws ComponentException, EJBException, RemoteException
	{		
		    CdsBulk cds = Utility.createParametriEnteComponentSession().getCds(userContext,CNRUserContext.getCd_cds(userContext));
			SQLBuilder sql = getHome(userContext, CofogBulk.class).createSQLBuilder();
			sql.addClause( clauses );	
			sql.addTableToHeader("PARAMETRI_CNR"); 
			sql.addSQLClause("AND","PARAMETRI_CNR.ESERCIZIO",SQLBuilder.EQUALS,dettaglio.getEsercizio());
			sql.addSQLJoin("COFOG.NR_LIVELLO","PARAMETRI_CNR.LIVELLO_PDG_COFOG");
			if(cds.getCd_tipo_unita().compareTo(Tipo_unita_organizzativaHome.TIPO_UO_SAC)==0){
				sql.openParenthesis("AND");
				sql.addSQLClause("AND", "COFOG.FL_ACCENTRATO", sql.EQUALS, "Y");
				sql.addSQLClause("OR", "COFOG.FL_DECENTRATO", sql.EQUALS, "Y");
				sql.closeParenthesis();
			} 
			else{
				sql.addSQLClause("AND", "COFOG.FL_DECENTRATO", sql.EQUALS, "Y");
			}
		    sql.openParenthesis("AND");
			sql.addSQLClause("AND", "COFOG.DT_CANCELLAZIONE", sql.ISNULL, null);
			sql.addSQLClause("OR","COFOG.DT_CANCELLAZIONE",sql.GREATER,it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
			sql.closeParenthesis(); 
			return sql;
	}
	public SQLBuilder selectPdgMissioneByClause (UserContext userContext, Pdg_modulo_speseBulk dettaglio, Pdg_missioneBulk pdgMissione, CompoundFindClause clause) throws ComponentException, PersistencyException, EJBException, RemoteException {
		Pdg_missioneHome pdgMissionehome = (Pdg_missioneHome)getHome(userContext, Pdg_missioneBulk.class);
		SQLBuilder sql = pdgMissionehome.createSQLBuilder();
		CdsBulk cds = Utility.createParametriEnteComponentSession().getCds(userContext,CNRUserContext.getCd_cds(userContext));
		Ass_pdg_missione_tipo_uoHome asshome = (Ass_pdg_missione_tipo_uoHome)getHome(userContext, Ass_pdg_missione_tipo_uoBulk.class);
		SQLBuilder sqlExists = asshome.createSQLBuilder();    	
		sqlExists.addSQLJoin("ASS_PDG_MISSIONE_TIPO_UO.CD_MISSIONE","PDG_MISSIONE.CD_MISSIONE");
		if (dettaglio!=null && cds.getCd_tipo_unita()!=null)
			sqlExists.addSQLClause(FindClause.AND, "ASS_PDG_MISSIONE_TIPO_UO.CD_TIPO_UNITA",SQLBuilder.EQUALS,cds.getCd_tipo_unita());
		else
			sqlExists.addSQLClause(FindClause.AND, "1!=1"); //Condizione inserita per far fallire la query
		sql.addSQLExistsClause(FindClause.AND, sqlExists);
	    sql.openParenthesis("AND");
		sql.openParenthesis("AND");
		Pdg_modulo_speseHome spesehome = (Pdg_modulo_speseHome)getHome(userContext, Pdg_modulo_speseBulk.class);
		
		SQLBuilder sqlNotExistsprogMis = spesehome.createSQLBuilder();    	
		sqlNotExistsprogMis.addSQLClause(FindClause.AND, "PDG_MODULO_SPESE.PG_PROGETTO",SQLBuilder.EQUALS,dettaglio.getPg_progetto());
		sqlNotExistsprogMis.addSQLClause(FindClause.AND, "PDG_MODULO_SPESE.ESERCIZIO",SQLBuilder.EQUALS,dettaglio.getEsercizio());
		sqlNotExistsprogMis.addSQLJoin("PDG_MODULO_SPESE.CD_MISSIONE",SQLBuilder.NOT_EQUALS,"PDG_MISSIONE.CD_MISSIONE");
		sql.addSQLNotExistsClause(FindClause.AND, sqlNotExistsprogMis);
		
		sql.closeParenthesis();
		sql.openParenthesis("OR");
		SQLBuilder sqlNotExistsprog = spesehome.createSQLBuilder();    	
		sqlNotExistsprog.addSQLClause(FindClause.AND, "PDG_MODULO_SPESE.PG_PROGETTO",SQLBuilder.EQUALS,dettaglio.getPg_progetto());
		sqlNotExistsprog.addSQLClause(FindClause.AND, "PDG_MODULO_SPESE.ESERCIZIO",SQLBuilder.EQUALS,dettaglio.getEsercizio());
		sql.addSQLNotExistsClause(FindClause.AND, sqlNotExistsprog);
		
		sql.closeParenthesis();
		sql.closeParenthesis();  
		
		if (clause != null) 
			sql.addClause(clause);
		return sql;
	}

	@Override
	protected void validaCreaModificaConBulk(UserContext usercontext,
			OggettoBulk oggettobulk) throws ComponentException {
		Pdg_modulo_costiBulk bulk =null;
		Pdg_modulo_speseBulk spesebulk =null;
		try {
		Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(usercontext, CNRUserContext.getEsercizio(usercontext)); 
		if (oggettobulk instanceof Pdg_modulo_costiBulk){
			 bulk= (Pdg_modulo_costiBulk) oggettobulk;
			for (Iterator i=bulk.getDettagliSpese().iterator();i.hasNext();){
				 Pdg_modulo_speseBulk pdg_modulo_spese = (Pdg_modulo_speseBulk)i.next();
				 if (pdg_modulo_spese!=null && parCnr.isCofogObbligatorio() && (pdg_modulo_spese.getCofog()==null||pdg_modulo_spese.getCd_cofog()==null)) 
					 throw new ApplicationException("Non è possibile inserire la spesa senza indicare la classificazione Cofog.");
				 if (pdg_modulo_spese!=null && parCnr.getFl_nuovo_pdg() && (pdg_modulo_spese.getPdgMissione()==null||pdg_modulo_spese.getCd_missione()==null)) 
					 throw new ApplicationException("Non è possibile inserire la spesa senza indicare la missione.");
				 if (pdg_modulo_spese.getClassificazione()!=null){
					 if(pdg_modulo_spese.isPrevAnnoSucObb() && pdg_modulo_spese.getIm_spese_a2()==null)
						 throw new ApplicationException("Non è possibile inserire la spesa senza indicare la previsione dell'anno successivo.");
					 else
						 if(!pdg_modulo_spese.isPrevAnnoSucObb() && pdg_modulo_spese.getIm_spese_a2()==null)
							 pdg_modulo_spese.setIm_spese_a2(BigDecimal.ZERO);
				 }
			}
		}
		Utility.createSaldoComponentSession().checkDispPianoEconomicoProgetto(usercontext, bulk);
		super.validaCreaModificaConBulk(usercontext, oggettobulk);
		} catch (RemoteException e) {
			throw handleException(e);
		}
	}

	public SQLBuilder selectVoce_piano_economicoByClause(UserContext userContext, Pdg_modulo_speseBulk dettaglio, Voce_piano_economico_prgBulk vocePiano, CompoundFindClause clause) throws ComponentException, PersistencyException {
		Voce_piano_economico_prgHome vocePianoHome = (Voce_piano_economico_prgHome)getHome(userContext, Voce_piano_economico_prgBulk.class);
		Integer esercizio = Optional.ofNullable(dettaglio).flatMap(el->Optional.ofNullable(el.getEsercizio())).orElse(null);
		Integer pgProgetto = Optional.ofNullable(dettaglio).flatMap(el->Optional.ofNullable(el.getPg_progetto())).orElse(null);
		Integer idClassificazione = Optional.ofNullable(dettaglio).flatMap(el->Optional.ofNullable(el.getId_classificazione())).orElse(null);
		SQLBuilder sql = vocePianoHome.selectVocePianoEconomicoPrgList(esercizio, pgProgetto, idClassificazione);

		if (clause != null) 
			sql.addClause(clause);
		return sql;
	}
}
