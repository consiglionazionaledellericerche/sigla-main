package it.cnr.contab.consultazioni.comp;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.ejb.EJBException;

import it.cnr.contab.anagraf00.core.bulk.InquadramentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneHome;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.consultazioni.bulk.VDocAmmAttiviBrevettiBulk;
import it.cnr.contab.docamm00.consultazioni.bulk.VDocAmmBrevettiBulk;
import it.cnr.contab.docamm00.consultazioni.bulk.VFatturaAttivaRigaBrevettiBulk;
import it.cnr.contab.docamm00.consultazioni.bulk.VFatturaAttivaRigaBrevettiHome;
import it.cnr.contab.docamm00.consultazioni.bulk.VFatturaPassivaRigaBrevettiBulk;
import it.cnr.contab.docamm00.consultazioni.bulk.VFatturaPassivaRigaBrevettiHome;
import it.cnr.contab.doccont00.consultazioni.bulk.VConsObbligazioniBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.VConsObbligazioniGaeBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaHome;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.missioni00.docs.bulk.RimborsoBulk;
import it.cnr.contab.missioni00.docs.bulk.RimborsoHome;
import it.cnr.contab.missioni00.ejb.MissioneComponentSession;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_rimborso_kmBulk;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_pastoBulk;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_spesaBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoGestBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoGestHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SimpleFindClause;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsultazioniRestComponent extends CRUDComponent {

	@Override
	protected Query select(UserContext userContext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		SQLBuilder sql =  (SQLBuilder)super.select(userContext, compoundfindclause, oggettobulk);
		if (oggettobulk instanceof VConsObbligazioniGaeBulk){
			sql.addClause( "AND", "cd_uo_origine", sql.EQUALS, ((CNRUserContext)userContext).getCd_unita_organizzativa()); 
			sql.addClause( "AND", "esercizio", sql.EQUALS, ((CNRUserContext)userContext).getEsercizio());
		} else if (oggettobulk instanceof VConsObbligazioniBulk){
			sql.addClause( "AND", "cd_uo_origine", sql.EQUALS, ((CNRUserContext)userContext).getCd_unita_organizzativa()); 
			sql.addClause( "AND", "esercizio", sql.EQUALS, ((CNRUserContext)userContext).getEsercizio());
		} else if (oggettobulk instanceof Elemento_voceBulk){
			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		} else if (oggettobulk instanceof InquadramentoBulk){
			if (compoundfindclause != null && compoundfindclause.getClauses() != null){
				Boolean trovataCondizioneCdAnagrafica = false;
				Enumeration e = compoundfindclause.getClauses();
				while(e.hasMoreElements() ){
					FindClause findClause = (FindClause) e.nextElement();
					if (findClause instanceof SimpleFindClause){
						SimpleFindClause clause = (SimpleFindClause)findClause;
						int operator = clause.getOperator();
						if (clause.getPropertyName() != null && clause.getPropertyName().equals("cd_anag") && 
								operator == 8192){
							trovataCondizioneCdAnagrafica = true;
						}
					}
				}
				if (!trovataCondizioneCdAnagrafica){
					throw new ComponentException("Non e' possibile richiamare il servizio REST degli inquadramenti senza la condizione del codice anagrafico.");
				}
			}
		} else if (oggettobulk instanceof VDocAmmAttiviBrevettiBulk){
			if (compoundfindclause != null && compoundfindclause.getClauses() != null){
				Boolean trovataCondizioneTrovato = false;
    			CompoundFindClause newClauses = new CompoundFindClause();
    			Enumeration e = compoundfindclause.getClauses();
    			SQLBuilder sqlExists = null;
				while(e.hasMoreElements() ){
					FindClause findClause = (FindClause) e.nextElement();
					if (findClause instanceof SimpleFindClause){
						SimpleFindClause clause = (SimpleFindClause)findClause;
						int operator = clause.getOperator();
						if (clause.getPropertyName() != null && clause.getPropertyName().equals("pgTrovato") && 
								operator == 8192){
							trovataCondizioneTrovato = true;
							VFatturaAttivaRigaBrevettiHome home = (VFatturaAttivaRigaBrevettiHome) getHome(userContext, VFatturaAttivaRigaBrevettiBulk.class);
							sqlExists = home.createSQLBuilder();
							sqlExists.addSQLJoin("V_DOC_AMM_ATTIVI_BREVETTI.ESERCIZIO", "V_FATTURA_ATTIVA_RIGA_BREVETTI.ESERCIZIO");
							sqlExists.addSQLJoin("V_DOC_AMM_ATTIVI_BREVETTI.CD_CDS", "V_FATTURA_ATTIVA_RIGA_BREVETTI.CD_CDS");
							sqlExists.addSQLJoin("V_DOC_AMM_ATTIVI_BREVETTI.CD_UNITA_ORGANIZZATIVA", "V_FATTURA_ATTIVA_RIGA_BREVETTI.CD_UNITA_ORGANIZZATIVA");
							sqlExists.addSQLJoin("V_DOC_AMM_ATTIVI_BREVETTI.PG_FATTURA_ATTIVA", "V_FATTURA_ATTIVA_RIGA_BREVETTI.PG_FATTURA_ATTIVA");
							sqlExists.addSQLJoin("V_DOC_AMM_ATTIVI_BREVETTI.TIPO_FATTURA", "V_FATTURA_ATTIVA_RIGA_BREVETTI.TIPO_FATTURA");
							sqlExists.addSQLClause("AND","V_FATTURA_ATTIVA_RIGA_BREVETTI.PG_TROVATO",SQLBuilder.EQUALS, clause.getValue() );
						} else {
							newClauses.addClause(clause.getLogicalOperator(), clause.getPropertyName(), clause.getOperator(), clause.getValue());
						}
					}
				}
				if (trovataCondizioneTrovato){
					sql =  getHome(userContext, oggettobulk).selectByClause(userContext, newClauses);
					sql.addSQLExistsClause("AND", sqlExists);
				}
			}
			if ( !isUoEnte(userContext)){
				sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
				sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
			}
		} else if (oggettobulk instanceof Missione_tipo_spesaBulk){
			if (compoundfindclause != null && compoundfindclause.getClauses() != null){
				CompoundFindClause newClauses = new CompoundFindClause();
				Enumeration e = compoundfindclause.getClauses();
				Enumeration e1 = compoundfindclause.getClauses();
				SQLBuilder sqlExists = null;
				if (isCondizioneTipiSpesaMissione(e)){
					NazioneBulk nazioneBulk = null;
					Timestamp dataTappa = null;
					Long inquadramento = null;
					Boolean ammissibileConRimborso = null;

					while(e1.hasMoreElements() ){
						FindClause findClause = (FindClause) e.nextElement();
						if (findClause instanceof SimpleFindClause){
							SimpleFindClause clause = (SimpleFindClause)findClause;
							int operator = clause.getOperator();
							if (clause.getPropertyName() != null && clause.getPropertyName().equals("nazione") && 
									operator == 8192){
								NazioneHome nazionehome=(NazioneHome)getHome(userContext,NazioneBulk.class);
								Integer str = (Integer)clause.getValue();
								nazioneBulk = new NazioneBulk(new Long(str));
								nazioneBulk = (NazioneBulk)nazionehome.findByPrimaryKey(nazioneBulk); 
							}else if (clause.getPropertyName() != null && clause.getPropertyName().equals("inquadramento") && 
									operator == 8192)	{
								Integer str = (Integer)clause.getValue();
								inquadramento = new Long(str);
							}else if (clause.getPropertyName() != null && clause.getPropertyName().equals("data") && 
									operator == 8192)	{
								SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
								Date parsedDate;
								try {
									parsedDate = dateFormat.parse((String) clause.getValue());
									dataTappa = new Timestamp(parsedDate.getTime());
								} catch (ParseException e2) {
									e2.printStackTrace();
								}
							}else if (clause.getPropertyName() != null && clause.getPropertyName().equals("ammissibileRimborso") && 
									operator == 8192)	{
								Boolean str = (Boolean)clause.getValue();
								ammissibileConRimborso = str;

							} else {
								newClauses.addClause(clause.getLogicalOperator(), clause.getPropertyName(), clause.getOperator(), clause.getValue());
							}
						}
					}
					if (nazioneBulk != null && dataTappa != null && inquadramento != null && ammissibileConRimborso != null){
						try {
							sql = missioneComponent().selectTipo_spesaByClause(userContext, dataTappa, inquadramento, nazioneBulk, ammissibileConRimborso, null, new CompoundFindClause());
							sql.addPreOrderBy(" ds_ti_spesa");
						} catch (RemoteException | EJBException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					}
				}
			}
		} else if (oggettobulk instanceof Missione_rimborso_kmBulk){
			if (compoundfindclause != null && compoundfindclause.getClauses() != null){
				CompoundFindClause newClauses = new CompoundFindClause();
				Enumeration e = compoundfindclause.getClauses();
				Enumeration e1 = compoundfindclause.getClauses();
				SQLBuilder sqlExists = null;
				if (isCondizioneRimborsoKmMissione(e)){
					NazioneBulk nazioneBulk = null;
					Timestamp dataTappa = null;
					String tipoAuto = null;

					while(e1.hasMoreElements() ){
						FindClause findClause = (FindClause) e.nextElement();
						if (findClause instanceof SimpleFindClause){
							SimpleFindClause clause = (SimpleFindClause)findClause;
							int operator = clause.getOperator();
							if (clause.getPropertyName() != null && clause.getPropertyName().equals("nazione") && 
									operator == 8192){
								NazioneHome nazionehome=(NazioneHome)getHome(userContext,NazioneBulk.class);
								Integer str = (Integer)clause.getValue();
								nazioneBulk = new NazioneBulk(new Long(str));
								nazioneBulk = (NazioneBulk)nazionehome.findByPrimaryKey(nazioneBulk); 
							}else if (clause.getPropertyName() != null && clause.getPropertyName().equals("tipoAuto") && 
									operator == 8192)	{
								String str = (String)clause.getValue();
								tipoAuto = str;
							}else if (clause.getPropertyName() != null && clause.getPropertyName().equals("data") && 
									operator == 8192)	{
								SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
								Date parsedDate;
								try {
									parsedDate = dateFormat.parse((String) clause.getValue());
									dataTappa = new Timestamp(parsedDate.getTime());
								} catch (ParseException e2) {
									e2.printStackTrace();
								}
							} else {
								newClauses.addClause(clause.getLogicalOperator(), clause.getPropertyName(), clause.getOperator(), clause.getValue());
							}
						}
					}
					if (nazioneBulk != null && dataTappa != null && tipoAuto != null){
						try {
							sql = missioneComponent().selectTipo_autoByClause(userContext, dataTappa, nazioneBulk, tipoAuto, new CompoundFindClause());
						} catch (RemoteException | EJBException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					}
				}
			}
		} else if (oggettobulk instanceof Missione_tipo_pastoBulk){
			if (compoundfindclause != null && compoundfindclause.getClauses() != null){
				CompoundFindClause newClauses = new CompoundFindClause();
				Enumeration e = compoundfindclause.getClauses();
				Enumeration e1 = compoundfindclause.getClauses();
				SQLBuilder sqlExists = null;
				if (isCondizioneTipiPastoMissione(e)){
					NazioneBulk nazioneBulk = null;
					Timestamp dataTappa = null;
					Long inquadramento = null;

					while(e1.hasMoreElements() ){
						FindClause findClause = (FindClause) e.nextElement();
						if (findClause instanceof SimpleFindClause){
							SimpleFindClause clause = (SimpleFindClause)findClause;
							int operator = clause.getOperator();
							if (clause.getPropertyName() != null && clause.getPropertyName().equals("nazione") && 
									operator == 8192){
								NazioneHome nazionehome=(NazioneHome)getHome(userContext,NazioneBulk.class);
								Integer str = (Integer)clause.getValue();
								nazioneBulk = new NazioneBulk(new Long(str));
								nazioneBulk = (NazioneBulk)nazionehome.findByPrimaryKey(nazioneBulk); 
							}else if (clause.getPropertyName() != null && clause.getPropertyName().equals("inquadramento") && 
									operator == 8192)	{
								Integer str = (Integer)clause.getValue();
								inquadramento = new Long(str);
							}else if (clause.getPropertyName() != null && clause.getPropertyName().equals("data") && 
									operator == 8192)	{
								SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
								Date parsedDate;
								try {
									parsedDate = dateFormat.parse((String) clause.getValue());
									dataTappa = new Timestamp(parsedDate.getTime());
								} catch (ParseException e2) {
									e2.printStackTrace();
								}
							} else {
								newClauses.addClause(clause.getLogicalOperator(), clause.getPropertyName(), clause.getOperator(), clause.getValue());
							}
						}
					}
					if (nazioneBulk != null && dataTappa != null && inquadramento != null){
						try {
							sql = missioneComponent().selectTipo_pastoByClause(userContext, dataTappa, inquadramento, nazioneBulk, null, new CompoundFindClause());
						} catch (RemoteException | EJBException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					}
				}
			}
		} else if (oggettobulk instanceof WorkpackageBulk){
			
	        if(compoundfindclause == null){
	            if(oggettobulk != null)
	                compoundfindclause = oggettobulk.buildFindClauses(null);
	        }else{
	            compoundfindclause = CompoundFindClause.and(compoundfindclause, oggettobulk.buildFindClauses(Boolean.FALSE));
	        }
	        sql =  getHome(userContext, WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA").selectByClause(userContext, compoundfindclause);
			
			if(!isUtenteEnte(userContext)){ 
				WorkpackageHome home = (WorkpackageHome) getHome(userContext, oggettobulk);
				SQLBuilder sqlExists = home.createSQLBuilder();
				CdrBulk cdrUtente = cdrFromUserContext(userContext);
				String uo_scrivania = CNRUserContext.getCd_unita_organizzativa(userContext);
				if (cdrUtente.getLivello().compareTo(CdrHome.CDR_PRIMO_LIVELLO)==0)
				{
					sql.addTableToHeader("V_CDR_VALIDO");
					sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA","V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA");
					sql.addSQLClause("AND", "V_CDR_VALIDO.ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
					sql.openParenthesis("AND");
					sql.addSQLClause("AND", "V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdrUtente.getCd_centro_responsabilita());
					sql.addSQLClause("OR", "V_CDR_VALIDO.CD_CDR_AFFERENZA",sql.EQUALS,cdrUtente.getCd_centro_responsabilita());
					sql.closeParenthesis();
				}else{
					sql.addTableToHeader("V_CDR_VALIDO");
					sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA","V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA");
					sql.addSQLClause("AND", "V_CDR_VALIDO.ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
					sql.openParenthesis("AND");
					sql.addSQLClause("AND", "V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdrUtente.getCd_centro_responsabilita());
					sql.addSQLClause("OR", "V_CDR_VALIDO.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,uo_scrivania);
					sql.closeParenthesis();
				}
				((SQLBuilder)sql).addSQLExistsClause(FindClause.AND, sqlExists);
			} 
		} else if (oggettobulk instanceof V_mandato_reversaleBulk){
			if (compoundfindclause != null && compoundfindclause.getClauses() != null){
				Boolean trovataCondizioneSoloAnticipi = false;
    			CompoundFindClause newClauses = new CompoundFindClause();
    			Enumeration e = compoundfindclause.getClauses();
    			SQLBuilder sqlExists = null;
    			SQLBuilder sqlNotExists = null;
				while(e.hasMoreElements() ){
					FindClause findClause = (FindClause) e.nextElement();
					if (findClause instanceof SimpleFindClause){
						SimpleFindClause clause = (SimpleFindClause)findClause;
						int operator = clause.getOperator();
						if (clause.getPropertyName() != null && clause.getPropertyName().equals("soloAnticipi") && 
								operator == 8192){
							trovataCondizioneSoloAnticipi = true;
							Mandato_rigaHome home = (Mandato_rigaHome) getHome(userContext, Mandato_rigaBulk.class);
							sqlExists = home.createSQLBuilder();
							sqlExists.addTableToHeader("ANTICIPO");
							sqlExists.addSQLJoin("V_MANDATO_REVERSALE.ESERCIZIO", "MANDATO_RIGA.ESERCIZIO");
							sqlExists.addSQLJoin("V_MANDATO_REVERSALE.CD_CDS", "MANDATO_RIGA.CD_CDS");
							sqlExists.addSQLJoin("V_MANDATO_REVERSALE.PG_DOCUMENTO_CONT", "MANDATO_RIGA.PG_MANDATO");
							sqlExists.addSQLJoin("ANTICIPO.CD_CDS", "MANDATO_RIGA.CD_CDS_DOC_AMM");
							sqlExists.addSQLJoin("ANTICIPO.ESERCIZIO", "MANDATO_RIGA.ESERCIZIO_DOC_AMM");
							sqlExists.addSQLJoin("ANTICIPO.CD_UNITA_ORGANIZZATIVA", "MANDATO_RIGA.CD_UO_DOC_AMM");
							sqlExists.addSQLJoin("ANTICIPO.PG_ANTICIPO", "MANDATO_RIGA.PG_DOC_AMM");
							sqlExists.addSQLClause("AND","MANDATO_RIGA.CD_TIPO_DOCUMENTO_AMM",SQLBuilder.EQUALS, "ANTICIPO" );
							sqlExists.addSQLClause("AND","ANTICIPO.FL_ASSOCIATO_MISSIONE",SQLBuilder.EQUALS, "N" );


							RimborsoHome homeRimborso = (RimborsoHome) getHome(userContext, RimborsoBulk.class);
							sqlNotExists = homeRimborso.createSQLBuilder();
							sqlNotExists.addTableToHeader("MANDATO_RIGA");
							sqlNotExists.addSQLJoin("V_MANDATO_REVERSALE.ESERCIZIO", "MANDATO_RIGA.ESERCIZIO");
							sqlNotExists.addSQLJoin("V_MANDATO_REVERSALE.CD_CDS", "MANDATO_RIGA.CD_CDS");
							sqlNotExists.addSQLJoin("V_MANDATO_REVERSALE.PG_DOCUMENTO_CONT", "MANDATO_RIGA.PG_MANDATO");
							sqlNotExists.addSQLJoin("RIMBORSO.CD_CDS", "MANDATO_RIGA.CD_CDS_DOC_AMM");
							sqlNotExists.addSQLJoin("RIMBORSO.ESERCIZIO", "MANDATO_RIGA.ESERCIZIO_DOC_AMM");
							sqlNotExists.addSQLJoin("RIMBORSO.CD_UNITA_ORGANIZZATIVA", "MANDATO_RIGA.CD_UO_DOC_AMM");
							sqlNotExists.addSQLJoin("RIMBORSO.PG_ANTICIPO", "MANDATO_RIGA.PG_DOC_AMM");
							sqlNotExists.addSQLClause("AND","MANDATO_RIGA.CD_TIPO_DOCUMENTO_AMM",SQLBuilder.EQUALS, "ANTICIPO" );
						} else {
							newClauses.addClause(clause.getLogicalOperator(), clause.getPropertyName(), clause.getOperator(), clause.getValue());
						}
					}
				}
				if (trovataCondizioneSoloAnticipi){
					sql =  getHome(userContext, oggettobulk).selectByClause(userContext, newClauses);
					sql.addSQLExistsClause("AND", sqlExists);
					sql.addSQLNotExistsClause("AND", sqlNotExists);
				}
			}
			if ( !isUoEnte(userContext)){
				sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
				sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
			}
		} else if (oggettobulk instanceof VDocAmmBrevettiBulk){
			if (compoundfindclause != null && compoundfindclause.getClauses() != null){
				Boolean trovataCondizioneTrovato = false;
    			CompoundFindClause newClauses = new CompoundFindClause();
    			Enumeration e = compoundfindclause.getClauses();
    			SQLBuilder sqlExists = null;
    			while(e.hasMoreElements() ){
    				FindClause findClause = (FindClause) e.nextElement();
    				if (findClause instanceof SimpleFindClause){
    					SimpleFindClause clause = (SimpleFindClause)findClause;
    					int operator = clause.getOperator();
    					if (clause.getPropertyName() != null && clause.getPropertyName().equals("pgTrovato") && 
    							operator == 8192){
    						trovataCondizioneTrovato = true;
    						VFatturaPassivaRigaBrevettiHome home = (VFatturaPassivaRigaBrevettiHome) getHome(userContext, VFatturaPassivaRigaBrevettiBulk.class);
    						sqlExists = home.createSQLBuilder();
    						sqlExists.addSQLJoin("V_DOC_AMM_BREVETTI.ESERCIZIO", "V_FATTURA_PASSIV_RIGA_BREVETTI.ESERCIZIO");
    						sqlExists.addSQLJoin("V_DOC_AMM_BREVETTI.CD_CDS", "V_FATTURA_PASSIV_RIGA_BREVETTI.CD_CDS");
    						sqlExists.addSQLJoin("V_DOC_AMM_BREVETTI.CD_UNITA_ORGANIZZATIVA", "V_FATTURA_PASSIV_RIGA_BREVETTI.CD_UNITA_ORGANIZZATIVA");
    						sqlExists.addSQLJoin("V_DOC_AMM_BREVETTI.PG_FATTURA_PASSIVA", "V_FATTURA_PASSIV_RIGA_BREVETTI.PG_FATTURA_PASSIVA");
    						sqlExists.addSQLJoin("V_DOC_AMM_BREVETTI.TIPO_FATTURA_COMPENSO", "V_FATTURA_PASSIV_RIGA_BREVETTI.TIPO_FATTURA_COMPENSO");
    						sqlExists.addSQLClause("AND","V_FATTURA_PASSIV_RIGA_BREVETTI.PG_TROVATO",SQLBuilder.EQUALS, clause.getValue() );
    					} else {
    						newClauses.addClause(clause.getLogicalOperator(), clause.getPropertyName(), clause.getOperator(), clause.getValue());
    					}
    				}
				}
				if (trovataCondizioneTrovato){
					sql =  getHome(userContext, oggettobulk).selectByClause(userContext, newClauses);
					sql.addSQLExistsClause("AND", sqlExists);
				}
			}
			if ( !isUoEnte(userContext)){
				sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
				sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
			}
		} else if (oggettobulk instanceof V_mandato_reversaleBulk){

			if ( !isUoEnte(userContext)){
				sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
				sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
			}
		} else if (oggettobulk instanceof ProgettoGestBulk){
			ProgettoGestHome home = (ProgettoGestHome) getHome(userContext, oggettobulk);
			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
			if ( !isUoEnte(userContext)){
				sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
			}
		}
		
		return sql;
	}

	private Boolean isCondizioneTipiSpesaMissione(Enumeration e) {
		while(e.hasMoreElements() ){
				SimpleFindClause clause = (SimpleFindClause) e.nextElement();
				int operator = clause.getOperator();
				if (clause.getPropertyName() != null && clause.getPropertyName().equals("condizioneTipiSpesaMissione") ){
					return true;
				}
			}
		return false;
	}

	private Boolean isCondizioneTipiPastoMissione(Enumeration e) {
		while(e.hasMoreElements() ){
				SimpleFindClause clause = (SimpleFindClause) e.nextElement();
				int operator = clause.getOperator();
				if (clause.getPropertyName() != null && clause.getPropertyName().equals("condizioneTipiPastoMissione") ){
					return true;
				}
			}
		return false;
	}

	private Boolean isCondizioneRimborsoKmMissione(Enumeration e) {
		while(e.hasMoreElements() ){
				SimpleFindClause clause = (SimpleFindClause) e.nextElement();
				int operator = clause.getOperator();
				if (clause.getPropertyName() != null && clause.getPropertyName().equals("condizioneRimborsoKmMissione") ){
					return true;
				}
			}
		return false;
	}

	private Boolean isUoEnte(UserContext userContext) throws PersistencyException, ComponentException{
		Unita_organizzativa_enteBulk uoEnte = getUoEnte(userContext);
		if ( ((CNRUserContext)userContext).getCd_unita_organizzativa().equals ( uoEnte.getCd_unita_organizzativa() )){
			return true;
		}
		return false;
	}
	
	private Unita_organizzativa_enteBulk getUoEnte(UserContext userContext)
			throws PersistencyException, ComponentException {
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0);
		return uoEnte;
	}	

	private CdrBulk cdrFromUserContext(UserContext userContext) throws ComponentException {
		try {
			it.cnr.contab.utenze00.bulk.UtenteBulk user = new it.cnr.contab.utenze00.bulk.UtenteBulk(userContext.getUser() );
			user = (it.cnr.contab.utenze00.bulk.UtenteBulk)getHome(userContext, user).findByPrimaryKey(user);

			CdrBulk cdr = new CdrBulk( it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext) );
			return (CdrBulk)getHome(userContext, cdr).findByPrimaryKey(cdr);

		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw new ComponentException(e);
		}
	}

	private Unita_organizzativaBulk uoFromUserContext(UserContext userContext) throws ComponentException {
		try {
			it.cnr.contab.utenze00.bulk.UtenteBulk user = new it.cnr.contab.utenze00.bulk.UtenteBulk( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser() );
			user = (it.cnr.contab.utenze00.bulk.UtenteBulk)getHome(userContext, user).findByPrimaryKey(user);

			Unita_organizzativaBulk uo = new Unita_organizzativaBulk( it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext) );
			return (Unita_organizzativaBulk)getHome(userContext, uo).findByPrimaryKey(uo);

		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw new ComponentException(e);
		}
	}
	
	private boolean isCdrEnte(UserContext userContext,CdrBulk cdr) throws ComponentException {
		try {
			getHome(userContext,cdr.getUnita_padre()).findByPrimaryKey(cdr.getUnita_padre());
			return cdr.isCdrAC();
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
	private boolean isUtenteEnte(UserContext userContext) throws ComponentException {
			return isCdrEnte(userContext,cdrFromUserContext(userContext));
	}	
	private MissioneComponentSession missioneComponent() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (MissioneComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRMISSIONI00_EJB_MissioneComponentSession");
	}

}