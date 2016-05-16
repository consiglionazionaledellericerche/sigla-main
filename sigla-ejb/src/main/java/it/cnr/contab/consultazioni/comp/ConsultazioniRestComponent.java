package it.cnr.contab.consultazioni.comp;

import java.util.Enumeration;
import java.util.Iterator;

import it.cnr.contab.anagraf00.core.bulk.InquadramentoBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.VConsObbligazioniBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.VConsObbligazioniGaeBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoGestBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoGestHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.AssBpAccessoBulk;
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
					SimpleFindClause clause = (SimpleFindClause) e.nextElement();
					int operator = clause.getOperator();
					if (clause.getPropertyName() != null && clause.getPropertyName().equals("cd_anag") && 
							operator == 8192){
						trovataCondizioneCdAnagrafica = true;
					}
				}
				if (!trovataCondizioneCdAnagrafica){
					throw new ComponentException("Non e' possibile richiamare il servizio REST degli inquadramenti senza la condizione del codice anagrafico.");
				}
			}
		} else if (oggettobulk instanceof WorkpackageBulk){
			if(!isUtenteEnte(userContext)){ 
				WorkpackageHome home = (WorkpackageHome) getHome(userContext, oggettobulk);
				SQLBuilder sqlExists = home.createSQLBuilder();
				CdrBulk cdrUtente = cdrFromUserContext(userContext);
				String uo_scrivania = CNRUserContext.getCd_unita_organizzativa(userContext);
				if (cdrUtente.getLivello().compareTo(CdrHome.CDR_PRIMO_LIVELLO)==0)
				{
					sql.addTableToHeader("V_CDR_VALIDO");
					sql.addSQLJoin("LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA","V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA");
					sql.addSQLClause("AND", "V_CDR_VALIDO.ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
						sql.openParenthesis("AND");
						sql.addSQLClause("AND", "V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdrUtente.getCd_centro_responsabilita());
						sql.addSQLClause("OR", "V_CDR_VALIDO.CD_CDR_AFFERENZA",sql.EQUALS,cdrUtente.getCd_centro_responsabilita());
						sql.closeParenthesis();
				}else{
					sql.addTableToHeader("V_CDR_VALIDO");
					sql.addSQLJoin("LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA","V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA");
					sql.addSQLClause("AND", "V_CDR_VALIDO.ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
					sql.openParenthesis("AND");
					sql.addSQLClause("AND", "V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdrUtente.getCd_centro_responsabilita());
					sql.addSQLClause("OR", "V_CDR_VALIDO.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,uo_scrivania);
					sql.closeParenthesis();
				}
				((SQLBuilder)sql).addSQLExistsClause(FindClause.AND, sqlExists);
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
}