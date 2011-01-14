package it.cnr.contab.incarichi00.comp;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoHome;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoHome;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.contratto.bulk.Procedure_amministrativeBulk;
import it.cnr.contab.config00.contratto.bulk.Tipo_atto_amministrativoBulk;
import it.cnr.contab.config00.file.bulk.Gruppo_fileBulk;
import it.cnr.contab.config00.file.bulk.Gruppo_fileHome;
import it.cnr.contab.config00.file.bulk.Tipo_fileBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaHome;
import it.cnr.contab.doccont00.comp.DateServices;
import it.cnr.contab.incarichi00.bulk.Ass_incarico_uoBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraHome;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_annoBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_archivioHome;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_richiestaBulk;
import it.cnr.contab.incarichi00.ejb.RepertorioLimitiComponentSession;
import it.cnr.contab.incarichi00.tabrif.bulk.Incarichi_parametriBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Incarichi_parametri_configBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Incarichi_parametri_configHome;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_attivitaBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_incaricoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;

public class IncarichiProceduraComponent extends CRUDComponent {
	private final static int INSERIMENTO = 1;
	private final static int MODIFICA    = 2;
	private final static int CANCELLAZIONE = 3;	

	public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			oggettobulk =  super.inizializzaBulkPerInserimento(usercontext, oggettobulk);
			
			if (oggettobulk instanceof Incarichi_proceduraBulk) {

				Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)oggettobulk; 

				procedura.setEsercizio(CNRUserContext.getEsercizio(usercontext));
				procedura.setStato(Incarichi_proceduraBulk.STATO_PROVVISORIO);
				procedura.setFl_pubblica_contratto(Boolean.FALSE);
				procedura.setFl_meramente_occasionale(Boolean.FALSE);
				procedura.setFl_art51(Boolean.FALSE);
				procedura.setFl_sbloccato(Boolean.FALSE);
				procedura.setNr_contratti(1);
				
				if (procedura.getIncarichi_richiesta()==null || procedura.getIncarichi_richiesta().getPg_richiesta()==null){  
					procedura.setCds( (CdsBulk)getHome(usercontext, CdsBulk.class).findByPrimaryKey(new CdsBulk(CNRUserContext.getCd_cds(usercontext))) );
					procedura.setUnita_organizzativa( (Unita_organizzativaBulk)getHome(usercontext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext))));
				}

				procedura = (Incarichi_proceduraBulk)caricaSedeUnitaOrganizzativa(usercontext, procedura);
				getHomeCache(usercontext).fetchAll(usercontext);
			}
			return oggettobulk;
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}
	
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			oggettobulk =  super.inizializzaBulkPerModifica(usercontext, oggettobulk);
			if (oggettobulk instanceof Incarichi_proceduraBulk) {
				Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)oggettobulk;
				
				Incarichi_proceduraHome procHome = (Incarichi_proceduraHome) getHome( usercontext, Incarichi_proceduraBulk.class );
				procedura.setIncarichi_procedura_annoColl( new BulkList( procHome.findIncarichi_procedura_annoList(usercontext, procedura ) ));
				procedura.setArchivioAllegati( new BulkList( procHome.findArchivioAllegati( procedura ) ));
				procedura.setIncarichi_procedura_noteColl( new BulkList( procHome.findIncarichi_procedura_noteList(usercontext, procedura ) ));

				BulkList listIncarichi =  new BulkList( procHome.findIncarichi_repertorioList(usercontext, procedura ) );
				BulkList listIncarichiClone = new BulkList(); 
				for (Iterator i=listIncarichi.iterator();i.hasNext();) {
					Incarichi_repertorioBulk incarico = ((Incarichi_repertorioBulk)i.next());
					Incarichi_repertorioBulk incaricoClone = (Incarichi_repertorioBulk)Utility.createIncarichiRepertorioComponentSession().inizializzaBulkPerModifica(usercontext, incarico);
					incaricoClone.setIncarichi_procedura(procedura);
					listIncarichiClone.add(incaricoClone);
				}
				procedura.setIncarichi_repertorioColl( listIncarichiClone );

				for (Iterator i=listIncarichiClone.iterator();i.hasNext();){
					for (Iterator y=((Incarichi_repertorioBulk)i.next()).getIncarichi_repertorio_annoColl().iterator();y.hasNext();){
						Incarichi_repertorio_annoBulk repAnno = (Incarichi_repertorio_annoBulk)y.next();
						if (!repAnno.getCompensiColl().isEmpty()){
							for (Iterator z=procedura.getIncarichi_procedura_annoColl().iterator();z.hasNext();){
								Incarichi_procedura_annoBulk procAnno = (Incarichi_procedura_annoBulk)z.next();
								if (procAnno.getEsercizio_limite().equals(repAnno.getEsercizio_limite()))
									procAnno.getCompensiColl().addAll(repAnno.getCompensiColl());
							}
						}
					}
				}
				if (procedura.getNr_contratti()!=null && procedura.getNr_contratti().compareTo(1)==0) {
					procedura.getArchivioAllegatiMI().addAll(procedura.getArchivioAllegati());
					if (procedura.getIncarichi_repertorioValidiColl().size()==1)
						procedura.getArchivioAllegatiMI().addAll(((Incarichi_repertorioBulk)procedura.getIncarichi_repertorioValidiColl().get(0)).getArchivioAllegati());
				}
				getHomeCache(usercontext).fetchAll(usercontext);
			}
			return oggettobulk;
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}
	public OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			oggettobulk =  super.inizializzaBulkPerRicerca(usercontext, oggettobulk);
			if (oggettobulk instanceof Incarichi_proceduraBulk) {
				Incarichi_proceduraBulk incarico = (Incarichi_proceduraBulk)oggettobulk; 
		  	  	incarico.setEsercizio(CNRUserContext.getEsercizio(usercontext));

		  	  	Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk)getHome(usercontext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(usercontext))));

		  	  	SQLBuilder sqlUoExists = getHome(usercontext, Ass_incarico_uoBulk.class).createSQLBuilder();
				sqlUoExists.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(usercontext));

				if (!sqlUoExists.executeExistsQuery(connection)) {
					boolean isUoEnte = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0;
					boolean isUoSac  = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC)==0;
				
					if (!isUoEnte)
						incarico.setCds( (CdsBulk)getHome(usercontext, CdsBulk.class).findByPrimaryKey(new CdsBulk(CNRUserContext.getCd_cds(usercontext))) );
					if (isUoSac)
						incarico.setUnita_organizzativa( uoScrivania );
				}
			}
			return oggettobulk;
		}catch( Exception e ){
			throw handleException( e );
		}		
	}
	protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext))));
		boolean isUoEnte = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0;
		boolean isUoSac  = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC)==0;
		
		SQLBuilder sqlCdsExists = getHome(userContext, Incarichi_repertorioBulk.class).createSQLBuilder();
		sqlCdsExists.resetColumns();
		sqlCdsExists.addColumn("1");
		sqlCdsExists.addSQLJoin("INCARICHI_REPERTORIO.ESERCIZIO_PROCEDURA", "INCARICHI_PROCEDURA.ESERCIZIO");
		sqlCdsExists.addSQLJoin("INCARICHI_REPERTORIO.PG_PROCEDURA", "INCARICHI_PROCEDURA.PG_PROCEDURA");

		sqlCdsExists.addTableToHeader("ASS_INCARICO_UO");
		sqlCdsExists.addSQLJoin("ASS_INCARICO_UO.ESERCIZIO","INCARICHI_REPERTORIO.ESERCIZIO");
		sqlCdsExists.addSQLJoin("ASS_INCARICO_UO.PG_REPERTORIO","INCARICHI_REPERTORIO.PG_REPERTORIO");

		if (isUoSac)
			sqlCdsExists.addSQLClause(FindClause.AND, "ASS_INCARICO_UO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
		else {
			sqlCdsExists.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA");
			sqlCdsExists.addSQLClause(FindClause.AND, "V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
			sqlCdsExists.addSQLJoin("V_STRUTTURA_ORGANIZZATIVA.CD_ROOT","ASS_INCARICO_UO.CD_UNITA_ORGANIZZATIVA");
			sqlCdsExists.addSQLClause(FindClause.AND, "V_STRUTTURA_ORGANIZZATIVA.CD_CDS", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
		}

		SQLBuilder sql = (SQLBuilder) super.select( userContext, clauses, bulk );
		if (!isUoEnte) {
			sql.openParenthesis(FindClause.AND);
				sql.openParenthesis(FindClause.OR);
					sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
					if (isUoSac)
						sql.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
				sql.closeParenthesis();
				sql.addSQLExistsClause(FindClause.OR,sqlCdsExists);
			sql.closeParenthesis();
		} else if (isUoSac) {
			sql.openParenthesis(FindClause.AND);
				sql.addClause(FindClause.OR, "cd_unita_organizzativa", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
				sql.addSQLExistsClause(FindClause.OR,sqlCdsExists);
			sql.closeParenthesis();
		}
		if (((Incarichi_proceduraBulk)bulk).getV_terzoForSearch()!=null &&
			((Incarichi_proceduraBulk)bulk).getV_terzoForSearch().getTerzo()!=null &&
			((Incarichi_proceduraBulk)bulk).getV_terzoForSearch().getTerzo().getCd_terzo()!=null) {
			SQLBuilder sqlExists = getHome(userContext, Incarichi_repertorioBulk.class).createSQLBuilder();
			sqlExists.addSQLJoin( "INCARICHI_REPERTORIO.ESERCIZIO_PROCEDURA", "INCARICHI_PROCEDURA.ESERCIZIO");
			sqlExists.addSQLJoin( "INCARICHI_REPERTORIO.PG_PROCEDURA", "INCARICHI_PROCEDURA.PG_PROCEDURA");
			sqlExists.addClause(FindClause.AND, "cd_terzo", SQLBuilder.EQUALS, ((Incarichi_proceduraBulk)bulk).getV_terzoForSearch().getTerzo().getCd_terzo());
			sql.addSQLExistsClause(FindClause.AND, sqlExists);
		}
		sql.addOrderBy("pg_procedura");
		return sql;
	}
	
	protected java.util.GregorianCalendar getGregorianCalendar() {
		java.util.GregorianCalendar gc = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
		
		gc.set(java.util.Calendar.HOUR, 0);
		gc.set(java.util.Calendar.MINUTE, 0);
		gc.set(java.util.Calendar.SECOND, 0);
		gc.set(java.util.Calendar.MILLISECOND, 0);
		gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
		
		return gc;
	}
	public it.cnr.jada.persistency.sql.SQLBuilder selectIncarichi_procedura_padreByClause(UserContext userContext, Incarichi_proceduraBulk procedura, Incarichi_proceduraBulk proceduraPadre, it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException{
		it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(userContext, proceduraPadre).createSQLBuilder();

		Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext))));
		boolean isUoEnte = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0;
		boolean isUoSac  = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC)==0;
		
		if (!isUoEnte)
			sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
		if (isUoSac)
			sql.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));

		sql.addClause(FindClause.AND,"cd_cds",SQLBuilder.EQUALS, procedura.getCd_cds());
		sql.addClause(FindClause.AND,"cd_unita_organizzativa",SQLBuilder.EQUALS, procedura.getCd_unita_organizzativa());

		if (procedura != null && procedura.getEsercizio()!=null && procedura.getPg_procedura()!=null) {
			sql.openParenthesis("AND");
			sql.addClause(FindClause.OR,"esercizio",SQLBuilder.NOT_EQUALS, procedura.getEsercizio());
			sql.addClause(FindClause.OR,"pg_procedura",SQLBuilder.NOT_EQUALS, procedura.getPg_procedura());
			sql.closeParenthesis();
		}
		sql.addClause(clauses);
		return sql;
	}
	public it.cnr.jada.persistency.sql.SQLBuilder selectUnita_organizzativaByClause(UserContext userContext, Incarichi_proceduraBulk procedura, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sqlStruttura = getHome(userContext, V_struttura_organizzativaBulk.class).createSQLBuilder();
		sqlStruttura.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		sqlStruttura.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, procedura.getCd_cds());
		sqlStruttura.addClause(FindClause.AND, "cd_tipo_livello", SQLBuilder.EQUALS, V_struttura_organizzativaHome.LIVELLO_UO);
		sqlStruttura.addSQLJoin( "V_STRUTTURA_ORGANIZZATIVA.CD_ROOT", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");

		Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext))));
		boolean isUoEnte = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0;
		boolean isUoSac  = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC)==0;
		
		if (!isUoEnte)
			sqlStruttura.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
		if (isUoSac)
			sqlStruttura.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));

		SQLBuilder sql = getHome(userContext, uo.getClass()).createSQLBuilder();
		sql.addSQLExistsClause(FindClause.AND, sqlStruttura);
		sql.addClause( clauses );
		return sql;
	}
	/**
	 * Pre:  Ricerca Tipo Provvedimento
	 * Post: Limitazione ai tipi non annullati
	 */
	public SQLBuilder selectAttoByClause (UserContext userContext, OggettoBulk bulk, Tipo_atto_amministrativoBulk tipo_atto_amministrativo,CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		  clause = tipo_atto_amministrativo.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, tipo_atto_amministrativo).createSQLBuilder();
		sql.addSQLClause(FindClause.AND, "FL_CANCELLATO", SQLBuilder.EQUALS, "N");
		if (clause != null) 
		  sql.addClause(clause);
		return sql;
	}
	/**
	 * Pre:  Ricerca Tipo Attività
	 * Post: Limitazione ai tipi non annullati
	 */
	public SQLBuilder selectTipo_attivitaByClause (UserContext userContext, OggettoBulk bulk, Tipo_attivitaBulk tipo_attivita, CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		    clause = tipo_attivita.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, tipo_attivita).createSQLBuilder();
		sql.addSQLClause(FindClause.AND, "FL_CANCELLATO", SQLBuilder.EQUALS, "N");
		if (clause != null) 
		  sql.addClause(clause);
		sql.addOrderBy("CD_TIPO_ATTIVITA");
		return sql;
	}
	/**
	 * Pre:  Ricerca Tipo Incarico
	 * Post: Limitazione ai tipi non annullati
	 */
	public SQLBuilder selectTipo_incaricoByClause (UserContext userContext, OggettoBulk bulk, Tipo_incaricoBulk tipo_incarico, CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		    clause = tipo_incarico.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, tipo_incarico).createSQLBuilder();
		sql.addSQLClause(FindClause.AND, "FL_CANCELLATO", SQLBuilder.EQUALS, "N");
		if (clause != null) 
		  sql.addClause(clause);
		sql.addOrderBy("CD_TIPO_INCARICO");

    	if (bulk.getCrudStatus()!=OggettoBulk.UNDEFINED && !bulk.isToBeCreated() &&
   			!((Incarichi_proceduraBulk)bulk).isProceduraProvvisoria() && 
   			!((Incarichi_proceduraBulk)bulk).isUtenteCollegatoUoEnte() &&  !((Incarichi_proceduraBulk)bulk).isUtenteCollegatoSuperUtente()) {
			try{
				Incarichi_proceduraHome procTempHome= (Incarichi_proceduraHome)getTempHome(userContext, Incarichi_proceduraBulk.class);
				Incarichi_proceduraBulk proceduraDB = (Incarichi_proceduraBulk)procTempHome.findByPrimaryKey((Incarichi_proceduraBulk)bulk);
	
				getTempHomeCache(userContext).fetchAll(userContext);
				if (!proceduraDB.getTipo_incarico().getTipoRapporto().getFl_inquadramento().booleanValue()) {
					sql.addTableToHeader("TIPO_RAPPORTO");
					sql.addSQLJoin("TIPO_INCARICO.CD_TIPO_RAPPORTO", "TIPO_RAPPORTO.CD_TIPO_RAPPORTO");
					sql.addSQLClause(FindClause.AND, "TIPO_RAPPORTO.FL_INQUADRAMENTO",SQLBuilder.EQUALS,"N");
				}
				else
					sql.addClause(FindClause.AND, "cd_tipo_incarico", SQLBuilder.EQUALS, proceduraDB.getCd_tipo_incarico());

			} catch (Throwable e) {
				throw handleException(bulk, e);
			}	
		}
    	else if (((Incarichi_proceduraBulk)bulk).getProcedura_amministrativa()!=null && 
        	 	 ((Incarichi_proceduraBulk)bulk).getProcedura_amministrativa().isMeramenteOccasionaleRequired()) {	
    		sql.addTableToHeader("TIPO_RAPPORTO");
    		sql.addSQLJoin("TIPO_INCARICO.CD_TIPO_RAPPORTO", "TIPO_RAPPORTO.CD_TIPO_RAPPORTO");
    		sql.addSQLClause(FindClause.AND, "TIPO_RAPPORTO.FL_INQUADRAMENTO",SQLBuilder.EQUALS,"N");
        }
		return sql;
	}
	/**
	 * Pre:  Ricerca Procedura amministrativa
	 * Post: Limitazione ai tipi non annullati
	 */
	public SQLBuilder selectProcedura_amministrativaByClause (UserContext userContext, OggettoBulk bulk, Procedure_amministrativeBulk procedura_amministrativa,CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		  clause = procedura_amministrativa.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, procedura_amministrativa).createSQLBuilder();

		sql.openParenthesis(FindClause.AND);
		sql.addClause(FindClause.OR, "ti_proc_amm", SQLBuilder.EQUALS, Procedure_amministrativeBulk.TIPO_AFFIDAMENTO_INCARICHI);
		sql.addClause(FindClause.OR, "ti_proc_amm", SQLBuilder.EQUALS, Procedure_amministrativeBulk.TIPO_GENERICA);
		sql.closeParenthesis();

		sql.addClause(FindClause.AND, "fl_cancellato", SQLBuilder.EQUALS, Boolean.FALSE);

		if (clause != null) 
		  sql.addClause(clause);
		return sql;
	}	

	private void validaProceduraIncarico(UserContext aUC,Incarichi_proceduraBulk procedura) throws ComponentException {
		/*
		 *Recupero l'eventuale nuova parametrizzazione 
		 */
		Incarichi_parametriBulk parametri = getIncarichiParametri(aUC, procedura);
		
		if (procedura.getProcedura_amministrativa().getFl_ricerca_incarico().booleanValue())
			if (procedura.getIncarichi_richiesta()==null||procedura.getIncarichi_richiesta().getPg_richiesta()==null)
				throw handleException( new ApplicationException( "La procedura amministrativa inserita richiede l'indicazione della \"Ricerca Professionalità Interna\"!") );

		if (parametri!=null && parametri.getRicerca_interna()!=null && parametri.getRicerca_interna().equals("Y"))
			if (procedura.getIncarichi_richiesta()==null||procedura.getIncarichi_richiesta().getPg_richiesta()==null)
				throw handleException( new ApplicationException( "Per il tipo di incarico registrato è richiesta l'indicazione della \"Ricerca Professionalità Interna\"!") );
			
		if (parametri==null || parametri.getAllega_decisione_ctr()==null || !parametri.getAllega_decisione_ctr().equals("N")) {
			if (procedura.getAtto()==null || procedura.getAtto().getCd_tipo_atto()==null)
				throw handleException( new ApplicationException( "Il campo \"Decisione a contrattare\" non può essere vuoto.") );
			if (procedura.getDs_atto()==null)
				throw handleException( new ApplicationException( "Il campo \"Estremi della decisione a contrattare\" non può essere vuoto.") );
			if (procedura.getTerzo_resp()==null || procedura.getTerzo_resp().getCd_terzo()==null)
				throw handleException( new ApplicationException( "Il campo \"Responsabile del procedimento\" non può essere vuoto.") );
		}

		if (procedura.getProcedura_amministrativa().isMeramenteOccasionaleRequired() && !procedura.getFl_meramente_occasionale().booleanValue())
			throw handleException( new ApplicationException( "La procedura amministrativa inserita richiede l'inserimento di incarichi solo per \"Collaboratori Meramente Occasionali\"!") );

		/*
		 * Il flag ART.51 deve essere valorizzato solo se:
		 * - la procedura amministrativa non è dipo INC3 (SENZA VERIFICA INTERNA E SENZA PROCEDURA COMPARATIVA)
		 * - il tipo di rapporto è COCOCO
		 */
		if (procedura.getFl_art51().booleanValue() && 
			!(procedura.getTipo_incarico()!=null && 
			  procedura.getTipo_incarico().getTipoRapporto().getFl_inquadramento().booleanValue() &&
			 !procedura.isProceduraDaPubblicare() && 
			 !(procedura.getProcedura_amministrativa()!=null &&
			   procedura.getProcedura_amministrativa().getFl_ricerca_incarico()!=null &&   	   
			   procedura.getProcedura_amministrativa().getFl_ricerca_incarico().booleanValue())))
			throw handleException( new ApplicationException( "Il flag art.51 può essere valorizzato solo per \"Collaboratori coordinati e continuativi\" e per procedure amministrative che non richiedono né la verifica di professionalità interna né la procedura comparativa!"));

		if (procedura.getFl_meramente_occasionale().booleanValue()) {
			try {
				Configurazione_cnrBulk config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( aUC, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LIMITE_COLL_MERAMENTE_OCCASIONALI, null );
	
				if ( (config == null || config.getIm01() == null) && (parametri==null || parametri.getImporto_limite_merocc() == null) )
					throw new ApplicationException("Manca la definizione dell'importo limite per i \"Collaboratori Meramente Occasionali\"!");

				BigDecimal limite; 
				if (config == null || config.getIm01() == null) 
					limite = parametri.getImporto_limite_merocc();
				else if (parametri==null || parametri.getImporto_limite_merocc() == null)
					limite = config.getIm01();
				else if (parametri.getImporto_limite_merocc().compareTo(config.getIm01()) == -1)
					limite = parametri.getImporto_limite_merocc();
				else 
					limite = config.getIm01();
					
				if (procedura.getImporto_lordo().compareTo(limite)==1)
					throw new ApplicationException("Non è possibile conferire incarichi per \"Collaboratori Meramente Occasionali\" di importo superiore al limite consentito di " + 
							new it.cnr.contab.util.EuroFormat().format(config.getIm01()) + "!");
					
		   }catch (RemoteException e) {
			   throw new ComponentException(e);
		   } catch (EJBException e) {
			   throw new ComponentException(e);
		   }
		}

		Integer totaleContrattiAttivati = 0;
		Integer totaleContrattiAttivabili = 0;
		try{
			if (procedura.getIncarichi_richiesta()!=null&&procedura.getIncarichi_richiesta().getPg_richiesta()!=null)
				procedura.getIncarichi_richiesta().setIncarichi_proceduraColl(new BulkList(((Incarichi_proceduraHome)getHome(aUC, Incarichi_proceduraBulk.class)).findIncarichiProcedura(procedura.getIncarichi_richiesta())));
		}
		catch (Throwable e) 
		{
			throw handleException(e);
		}
			
		if (procedura.getIncarichi_richiesta()!=null&&procedura.getIncarichi_richiesta().getIncarichi_proceduraColl()!=null) {
			for (Iterator<Incarichi_proceduraBulk> i=procedura.getIncarichi_richiesta().getIncarichi_proceduraColl().iterator();i.hasNext();){
				Incarichi_proceduraBulk proceduraColl = i.next();
				if (!proceduraColl.equalsByPrimaryKey(procedura))
					totaleContrattiAttivati = totaleContrattiAttivati + proceduraColl.getNr_contratti();
			}
			totaleContrattiAttivabili = procedura.getIncarichi_richiesta().getNrRisorseNonTrovate() - totaleContrattiAttivati;
			if (totaleContrattiAttivabili < procedura.getNr_contratti())
				throw handleException( new ApplicationException( "Il numero di contratti attivabili non può essere superiore al numero massimo ancora consentito dalla Verifica di professionalità interna associata ("+totaleContrattiAttivabili.toString()+")!") );
		}					
			
		int contaBandi = 0;
		int contaDecisioniAContrattare = 0;
		for (Iterator<Incarichi_procedura_archivioBulk> i = procedura.getArchivioAllegati().iterator(); i.hasNext(); ) {
			Incarichi_procedura_archivioBulk archivio = i.next();
			if (archivio.isBando())
				contaBandi = contaBandi+1; 
			if (archivio.isDecisioneAContrattare())
				contaDecisioniAContrattare = contaDecisioniAContrattare+1; 
		}
		
		if (contaBandi>1)
			throw handleException( new ApplicationException( "Alla procedura di conferimento incarico può essere allegato al massimo un \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_BANDO).toString()+"\"!") );

		if (contaDecisioniAContrattare>1)
			throw handleException( new ApplicationException( "Alla procedura di conferimento incarico può essere allegato al massimo una \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_DECISIONE_A_CONTRATTARE).toString()+"\"'!") );
		
		for (Iterator<Incarichi_repertorioBulk> i = procedura.getIncarichi_repertorioColl().iterator(); i.hasNext(); ) {
			Incarichi_repertorioBulk incarico = i.next();
		
			if (incarico.getTerzo()==null || incarico.getTerzo().getCd_terzo()==null)
				throw handleException( new ApplicationException(incarico.getPg_repertorio()!=null?"Contratto "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - completare le informazioni relative al terzo!":"Completare le informazioni relative al terzo!") );
			else if (incarico.getTerzo()!=null || incarico.getTerzo().getCd_terzo()!=null) {
				if (incarico.getDt_inizio_validita()!=null && incarico.getDt_fine_validita()!=null) {
					if (incarico.getTipo_rapporto()==null)
						throw handleException( new ApplicationException( "Completare le informazioni relative al tipo rapporto da applicare al terzo selezionato (cod: "+incarico.getTerzo().getCd_terzo()+")!") );
					if (incarico.getTi_istituz_commerc()==null) 
						throw handleException( new ApplicationException( "Completare le informazioni relative al tipo compenso da applicare al terzo selezionato (cod: "+incarico.getTerzo().getCd_terzo()+")!") );
				}
				
				int contaContratti = 0;
	
				for ( Iterator y = incarico.getArchivioAllegati().iterator(); y.hasNext(); ) {
					Incarichi_repertorio_archivioBulk archivio = (Incarichi_repertorio_archivioBulk)y.next();
					if (archivio.isContratto())
						contaContratti = contaContratti+1; 
				}
			
				if (contaContratti>1)
					if (incarico.getV_terzo()!=null && incarico.getV_terzo().getCognome()!=null && incarico.getV_terzo().getNome()!=null)
						throw handleException( new ApplicationException( "Al terzo \""+incarico.getV_terzo().getCognome()+" "+incarico.getV_terzo().getNome()+"\" associato alla procedura di conferimento incarico può essere allegato al massimo un \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_CONTRATTO).toString()+"\"!") );
					else
						throw handleException( new ApplicationException( "Al terzo (cod: "+incarico.getTerzo().getCd_terzo()+") allegato alla procedura di conferimento incarico può essere allegato al massimo un \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_CONTRATTO).toString()+"\"!") );
	
				if (incarico.getIm_complessivo_ripartito().compareTo(incarico.getImporto_complessivo_incarico())!=0)
					if (incarico.getV_terzo()!=null && incarico.getV_terzo().getCognome()!=null && incarico.getV_terzo().getNome()!=null)
						throw handleException( new ApplicationException( "Il totale ripartito per anno per il terzo \""+incarico.getV_terzo().getCognome()+" "+incarico.getV_terzo().getNome()+"\" non coincide con la spesa complessiva relativa al singolo contratto indicata in testata!") );
					else
						throw handleException( new ApplicationException( "Il totale ripartito per anno per il terzo cod: "+incarico.getTerzo().getCd_terzo()+" non coincide con la spesa complessiva relativa al singolo contratto indicata in testata!") );
			}
			
			if (incarico.isIncaricoDefinitivo() || incarico.isIncaricoInviatoCorteConti() || incarico.isIncaricoChiuso()) { 
				if (incarico.getContratto()==null) {
					if (parametri==null || parametri.getAllega_contratto()==null || parametri.getAllega_contratto().equals("Y")) { 
						if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
							//Istanzio la classe per riempire tipo_archivioKeys
							new Incarichi_procedura_archivioBulk(); 
						}
		
						if (incarico.getV_terzo()!=null && incarico.getV_terzo().getCognome()!=null && incarico.getV_terzo().getNome()!=null)
							throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto del terzo \""+incarico.getV_terzo().getCognome()+" "+incarico.getV_terzo().getNome()+"\" un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_CONTRATTO).toString()+"\".");
						else
							throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_CONTRATTO).toString()+"\".");
					}
				}
			}
		}

		if (procedura.getIm_complessivo_ripartito().compareTo(procedura.getImporto_complessivo_procedura())!=0)
			throw handleException( new ApplicationException( "Il totale ripartito per anno non coincide con la spesa complessiva indicata in testata!") );
	}

	private void aggiornaImportiRepertorioLimiti(UserContext userContext, Incarichi_proceduraBulk procedura, int azione) throws ComponentException 
	{
		try{
			RepertorioLimitiComponentSession comp = Utility.createRepertorioLimitiComponentSession();
			
			Incarichi_proceduraHome procTempHome= (Incarichi_proceduraHome)getTempHome(userContext, Incarichi_proceduraBulk.class);
			Incarichi_proceduraBulk proceduraDB = (Incarichi_proceduraBulk)procTempHome.findByPrimaryKey(procedura);
			if (azione!=INSERIMENTO) {
				for (Iterator i=procTempHome.findIncarichi_procedura_annoList(userContext, procedura ).iterator();i.hasNext();) {
					Incarichi_procedura_annoBulk dettDB = (Incarichi_procedura_annoBulk)i.next();
					if (azione==CANCELLAZIONE ||
						!procedura.getIncarichi_procedura_annoColl().containsByPrimaryKey(dettDB) ||
						!proceduraDB.getCd_tipo_attivita().equals(procedura.getCd_tipo_attivita()) ||
						!proceduraDB.getCd_tipo_incarico().equals(procedura.getCd_tipo_incarico()) ||
						!proceduraDB.getTipo_natura().equals(procedura.getTipo_natura())){

						if (calcolaUtilizzato(userContext, dettDB).compareTo(Utility.ZERO)!=0)
							throw new ApplicationException("Eliminazione importi per l'anno " + dettDB.getEsercizio_limite() + 
									" non possibile!\nL'importo risulta già utilizzato.");

						if (dettDB.getImporto_complessivo().compareTo(Utility.ZERO) != 0)
							comp.aggiornaRepertorioLimiti(userContext, dettDB.getEsercizio_limite(), 
									proceduraDB.getCd_tipo_incarico(), 
									proceduraDB.getCd_tipo_attivita(), 
									proceduraDB.getTipo_natura(),
	                                dettDB.getImporto_complessivo().negate());
					}
				}
			}
			if (azione!=CANCELLAZIONE) {
				for (Iterator i=procedura.getIncarichi_procedura_annoColl().iterator();i.hasNext();) {
					Incarichi_procedura_annoBulk dett = (Incarichi_procedura_annoBulk)i.next();
	
					BigDecimal importo = new BigDecimal(0);
	
					if (azione==INSERIMENTO)
						importo = dett.getImporto_complessivo();
					else
					{
						Incarichi_procedura_annoBulk dettTemp = (Incarichi_procedura_annoBulk)getTempHome(userContext, Incarichi_procedura_annoBulk.class).findByPrimaryKey(dett);
						if (dettTemp==null || 
							!proceduraDB.getCd_tipo_attivita().equals(procedura.getCd_tipo_attivita()) ||
							!proceduraDB.getCd_tipo_incarico().equals(procedura.getCd_tipo_incarico()) ||
							!proceduraDB.getTipo_natura().equals(procedura.getTipo_natura()))
							importo = dett.getImporto_complessivo();
						else
							importo = dett.getImporto_complessivo().subtract(dettTemp.getImporto_complessivo());

						if (dett.getImporto_complessivo().compareTo(calcolaUtilizzato(userContext, dett))==-1)
							throw new ApplicationException("Modifica importi per l'anno " + dett.getEsercizio_limite() + 
									" non possibile!\nL'importo deve essere almeno pari all'importo utilizzato.");
					}
					
					if (importo.compareTo(Utility.ZERO) != 0)
						comp.aggiornaRepertorioLimiti(userContext, dett.getEsercizio_limite(), 
																   procedura.getCd_tipo_incarico(), 
																   procedura.getCd_tipo_attivita(), 
																   procedura.getTipo_natura(),
								                                   importo);
				}
			}
		} catch (Throwable e) {
			throw handleException(procedura, e);
		}	
	}
	public BigDecimal calcolaUtilizzato(UserContext userContext, Incarichi_procedura_annoBulk procedura_anno) throws ComponentException
	{
		BigDecimal utilizzato=new BigDecimal(0);
		for (Iterator i =procedura_anno.getIncarichi_procedura().getIncarichi_repertorioColl().iterator();i.hasNext();){
			Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)i.next();
			if (!incarico.isToBeCreated()){
				for (Iterator x = incarico.getIncarichi_repertorio_annoColl().iterator();x.hasNext();){
					Incarichi_repertorio_annoBulk incarico_anno = (Incarichi_repertorio_annoBulk)x.next();
					if (!incarico_anno.isToBeCreated())
						if (incarico_anno.getEsercizio_limite().compareTo(procedura_anno.getEsercizio_limite())==0)
							utilizzato = utilizzato.add(calcolaUtilizzato(userContext, incarico_anno));
				}
			}
		}
		return utilizzato;
	}

	public BigDecimal calcolaUtilizzato(UserContext userContext, Incarichi_repertorio_annoBulk incarico_anno) throws ComponentException
	{
		BigDecimal utilizzato=new BigDecimal(0);
		CompensoHome cHome = (CompensoHome)getHome(userContext, CompensoBulk.class);
		Iterator listacomp_incarico;
		try {
			listacomp_incarico = cHome.findCompensoIncaricoList(userContext,incarico_anno).iterator();

			for (Iterator x =listacomp_incarico;x.hasNext();){
				CompensoBulk dett =(CompensoBulk)x.next();
				getHomeCache(userContext).fetchAll(userContext);
				utilizzato = utilizzato.add(dett.getIm_totale_compenso());
			}
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(e);
		} catch (it.cnr.jada.persistency.IntrospectionException e) {
			throw handleException(e);
		}
		return utilizzato;
	}

	/** 
	  *  Archivia i file indicati dall'utente 
	  *
	  * @param  userContext
	  * @param  incarico    Bulk della richiesta incarico di cui si vuole effettuare l'archiviazione
	  * @param  tipo	 	Tipo di consultazione effettuata E/S/C/R
	  * @param  file	 	File da archiviare
	  * @return	void
	 */
	public void archiviaAllegati(UserContext userContext, Incarichi_proceduraBulk procedura, String tipo, File file) throws ComponentException{
		Incarichi_procedura_archivioHome archiveHome = (Incarichi_procedura_archivioHome)getHome(userContext,Incarichi_procedura_archivioBulk.class);
		Incarichi_procedura_archivioBulk archive = new Incarichi_procedura_archivioBulk();
        archive.setIncarichi_procedura(procedura);
        archive.setTipo_archivio(tipo);
        archive.setToBeCreated();
        super.creaConBulk(userContext, archive);         		
		try {
			oracle.sql.BLOB blob = (oracle.sql.BLOB)archiveHome.getSQLBlob(archive,"BDATA");
			java.io.InputStream in = new java.io.BufferedInputStream(new FileInputStream(file));
			byte[] byteArr = new byte[1024];
			java.io.OutputStream os = new java.io.BufferedOutputStream(blob.getBinaryOutputStream());
			int len;			
			while ((len = in.read(byteArr))>0){
			   os.write(byteArr,0,len);
			}
			os.close();
			in.close();
		} catch (PersistencyException e) {
			throw new ComponentException(e);	
		} catch (FileNotFoundException e) {
			throw new ComponentException(e);	
		} catch (IOException e) {
			throw new ComponentException(e);	
		} catch (SQLException e) {
			throw new ComponentException(e);	
		}
	}
	
	private void archiviaAllegati(UserContext userContext, Incarichi_proceduraBulk procedura) throws ComponentException{
		List listFileAllegabili = null;
		if (procedura.getProcedura_amministrativa()!=null && procedura.getProcedura_amministrativa().getCd_gruppo_file()!=null) {
			try {
				listFileAllegabili = ((Gruppo_fileHome)getHome(userContext,Gruppo_fileBulk.class)).findTipo_file_associati(procedura.getProcedura_amministrativa().getGruppo_file());
			} catch (PersistencyException e) {
				throw new ComponentException(e);	
			} catch (IntrospectionException e) {
				throw new ComponentException(e);	
			}
		}
		
		BulkList listFileDaArchiviare = new BulkList();
		listFileDaArchiviare.addAll(procedura.getArchivioAllegati());
		for (Iterator<Incarichi_repertorioBulk> i = procedura.getIncarichi_repertorioColl().iterator();i.hasNext();){
			Incarichi_repertorioBulk incarico = i.next();
			listFileDaArchiviare.addAll(incarico.getArchivioAllegati());
			listFileDaArchiviare.addAll(incarico.getIncarichi_repertorio_varColl());
		}

		for (Iterator i = listFileDaArchiviare.iterator(); i.hasNext();) {
			Incarichi_archivioBulk allegato = (Incarichi_archivioBulk)i.next();
			if (!(allegato.getFile() == null || allegato.getFile().getName().equals(""))) {
				if (listFileAllegabili != null && !listFileAllegabili.isEmpty()){
					String nomeFile = allegato.getFile().getName();
					String estensioneFile = nomeFile.substring(nomeFile.lastIndexOf(".")+1);
					String estensioniValide = null;
					
					boolean valido = false;
					for ( Iterator j = listFileAllegabili.iterator(); j.hasNext(); ) {
						Tipo_fileBulk tipo_file = (Tipo_fileBulk)j.next();
						if (estensioneFile.equalsIgnoreCase(tipo_file.getEstensione_file())) {
							valido = true;
							break;
						}
						estensioniValide = estensioniValide==null?"\""+tipo_file.getEstensione_file()+"\"":estensioniValide.concat(", \""+tipo_file.getEstensione_file()+"\"");
					}
					if (!valido)
						throw new ApplicationException( "File non valido!\nI formati dei file consentiti sono "+estensioniValide+".");
				}
				allegato.setToBeUpdated();
				BulkHome archiveHome = getHome(userContext,allegato.getClass());
				try {
					oracle.sql.BLOB blob = (oracle.sql.BLOB)archiveHome.getSQLBlob(allegato,"BDATA");
					java.io.InputStream in = new java.io.BufferedInputStream(new FileInputStream(allegato.getFile()));
					byte[] byteArr = new byte[1024];
					java.io.OutputStream os = new java.io.BufferedOutputStream(blob.getBinaryOutputStream());
					int len;			
					while ((len = in.read(byteArr))>0){
						os.write(byteArr,0,len);
					}
					os.close();
					in.close();
				} catch (PersistencyException e) {
					throw new ComponentException(e);	
				} catch (FileNotFoundException e) {
					throw new ComponentException(e);	
				} catch (IOException e) {
					throw new ComponentException(e);	
				} catch (SQLException e) {
					throw new ComponentException(e);	
				}
			}
		}
	}
	public void eliminaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException
	{
		try
		{
			Incarichi_proceduraBulk procedura;
			if ( bulk instanceof Incarichi_proceduraBulk )
			{
				procedura = (Incarichi_proceduraBulk) getHome( aUC, Incarichi_proceduraBulk.class).findByPrimaryKey( bulk );
				if ( procedura == null )
					throw new ApplicationException( "L'incarico e' stato cancellato" );

				procedura = (Incarichi_proceduraBulk)inizializzaBulkPerModifica(aUC, procedura);
	
				if ( procedura.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_INCARICO)!=1)
				{
					procedura.setToBeDeleted();
	
					for ( Iterator i = procedura.getIncarichi_procedura_annoColl().iterator(); i.hasNext(); )
						((OggettoBulk) i.next()).setToBeDeleted();

					for ( Iterator i = procedura.getIncarichi_repertorioColl().iterator(); i.hasNext(); ) {
						Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk) i.next();
						incarico.setToBeDeleted();
						for ( Iterator y = incarico.getIncarichi_repertorio_annoColl().iterator(); y.hasNext(); )
							((OggettoBulk) y.next()).setToBeDeleted();
						for ( Iterator y = incarico.getArchivioAllegati().iterator(); y.hasNext(); )
							((OggettoBulk) y.next()).setToBeDeleted();
					}

					aggiornaImportiRepertorioLimiti(aUC, procedura, CANCELLAZIONE);
					
					for ( Iterator i = procedura.getArchivioAllegati().iterator(); i.hasNext(); )
						((OggettoBulk) i.next()).setToBeDeleted();

					makeBulkPersistent( aUC, procedura );					
				}
				// si tratta di un annullamento
				else if ( procedura.getStato().equals(Incarichi_proceduraBulk.STATO_PUBBLICATA))
				{
					procedura = (Incarichi_proceduraBulk)stornaProceduraIncaricoPubblicata(aUC, procedura);
				}
				else if ( procedura.getStato().equals(Incarichi_proceduraBulk.STATO_DEFINITIVO))
				{
					if (procedura.getImporto_utilizzato().compareTo(Utility.ZERO)==1)
						procedura = (Incarichi_proceduraBulk)chiudiProceduraIncaricoPubblicata(aUC, procedura);
					else
						procedura = (Incarichi_proceduraBulk)stornaProceduraIncaricoPubblicata(aUC, procedura);
				}
				else
					throw handleException( new it.cnr.jada.comp.ApplicationException( "Lo stato della procedura di conferimento incarico non ne consente la cancellazione/storno")); 
			}		
		}
		catch ( it.cnr.jada.persistency.PersistencyException e)
		{
			throw handleException( bulk, e );
		}		
	}
	public OggettoBulk stornaProceduraIncaricoPubblicata(UserContext aUC, OggettoBulk bulk) throws ComponentException {
		try {
			if (bulk instanceof Incarichi_proceduraBulk){
				Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) getHome( aUC, Incarichi_proceduraBulk.class).findByPrimaryKey( bulk );
				if ( procedura == null )
					throw new ApplicationException( "La procedura di conferimento incarico e' stata cancellata" );
	
				procedura = (Incarichi_proceduraBulk)inizializzaBulkPerModifica(aUC, procedura);
		
				/* Qui bisogna verificare se gli importi associati all'incarico non siano già stati, anche in parte, associati ad impegni/compensi
			  	if (esisteimpegno o incarico)
		          	throw new ApplicationException("L'incarico risulta già utilizzato su impegni/compensi. Impossibile eliminarlo.");
				 */
				/* Qui bisogna ritornare le dipsonibilità sulla tabella degli 
		      	for ( Iterator i = obbligazione.getObbligazione_scadenzarioColl().iterator(); i.hasNext(); )
			        if ( ((Obbligazione_scadenzarioBulk)i.next()).getIm_associato_doc_amm().compareTo( new BigDecimal(0)) > 0 )
		            throw new ApplicationException("Impossibile stornare impegni collegati a spese del fondo economale o a documenti amministrativi");
				*/
				procedura.setStato(Incarichi_proceduraBulk.STATO_ANNULLATO);
				procedura.setDt_cancellazione( DateServices.getDt_valida(aUC));
				procedura.setToBeUpdated();
	
				aggiornaImportiRepertorioLimiti(aUC, procedura, CANCELLAZIONE);
				
				makeBulkPersistent( aUC, procedura);
				
				return procedura;
			}
			return bulk;
	    } catch (Exception e) {
	        throw handleException(e);
	    }
	}
	public it.cnr.jada.persistency.sql.SQLBuilder selectIncarichi_richiestaByClause(UserContext userContext, Incarichi_proceduraBulk procedura, Incarichi_richiestaBulk richiesta, it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws ComponentException {
		it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(userContext, Incarichi_richiestaBulk.class).createSQLBuilder();
		sql.addClause(FindClause.AND,"cd_cds",SQLBuilder.EQUALS, procedura.getCd_cds());
		sql.addClause(FindClause.AND,"cd_unita_organizzativa",SQLBuilder.EQUALS, procedura.getCd_unita_organizzativa());
		//Escludo quelle scadute
		sql.addClause(FindClause.AND,"data_scadenza", SQLBuilder.GREATER_EQUALS, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
		sql.addClause(clauses);
		
		sql.openParenthesis(FindClause.AND);
		sql.addClause(FindClause.OR,"nr_risorse_trovate_no", SQLBuilder.GREATER, Utility.ZERO);
		sql.addClause(FindClause.OR,"nr_risorse_trovate_na",SQLBuilder.GREATER, Utility.ZERO);
		sql.closeParenthesis();

		it.cnr.jada.persistency.sql.SQLBuilder sqlExist = getHome(userContext, Incarichi_richiestaBulk.class).createSQLBuilder();
		sqlExist.setFromClause(new StringBuffer("INCARICHI_RICHIESTA A"));
		sqlExist.resetColumns();
		sqlExist.addColumn("A.ESERCIZIO, A.PG_RICHIESTA, A.NR_RISORSE_TROVATE_NO, A.NR_RISORSE_TROVATE_NA");
		sqlExist.addSQLJoin("A.ESERCIZIO", "INCARICHI_RICHIESTA.ESERCIZIO");
		sqlExist.addSQLJoin("A.PG_RICHIESTA", "INCARICHI_RICHIESTA.PG_RICHIESTA");
		sqlExist.addSQLGroupBy("A.ESERCIZIO, A.PG_RICHIESTA, A.NR_RISORSE_TROVATE_NO, A.NR_RISORSE_TROVATE_NA");

		it.cnr.jada.persistency.sql.SQLBuilder sqlCount = getHome(userContext, Incarichi_proceduraBulk.class).createSQLBuilder();
		sqlCount.resetColumns();
		sqlCount.addColumn("NVL(SUM(INCARICHI_PROCEDURA.NR_CONTRATTI), 0)");

		sqlCount.addSQLJoin("INCARICHI_PROCEDURA.ESERCIZIO_RICHIESTA","A.ESERCIZIO");
		sqlCount.addSQLJoin("INCARICHI_PROCEDURA.PG_RICHIESTA","A.PG_RICHIESTA");
		
		sqlCount.addClause(FindClause.AND, "stato", SQLBuilder.NOT_EQUALS,Incarichi_proceduraBulk.STATO_ANNULLATO);
		sqlCount.addClause(FindClause.AND, "stato", SQLBuilder.NOT_EQUALS,Incarichi_proceduraBulk.STATO_RESPINTO);
		
		//Escludo quelle scadute
		sqlCount.openParenthesis(FindClause.AND_NOT);
		sqlCount.addClause(FindClause.AND, "stato", SQLBuilder.EQUALS,Incarichi_proceduraBulk.STATO_PUBBLICATA);
		sqlCount.addClause(FindClause.AND, "dt_scadenza", SQLBuilder.LESS, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
		sqlCount.closeParenthesis();

		if (procedura != null && procedura.getEsercizio() != null && procedura.getPg_procedura() != null) {
			sqlCount.openParenthesis(FindClause.AND);
			sqlCount.addClause(FindClause.OR,"esercizio",SQLBuilder.NOT_EQUALS, procedura.getEsercizio());
			sqlCount.addClause(FindClause.OR,"pg_procedura",SQLBuilder.NOT_EQUALS, procedura.getPg_procedura());
			sqlCount.closeParenthesis();
		}

		sqlExist.addSQLHavingClause(FindClause.AND, "(A.NR_RISORSE_TROVATE_NO + A.NR_RISORSE_TROVATE_NA)", SQLBuilder.GREATER, sqlCount);

		sql.addSQLExistsClause(FindClause.AND, sqlExist);
		
		return sql;
	}
	public OggettoBulk pubblicaSulSito(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			if (oggettobulk instanceof Incarichi_proceduraBulk) {
				Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)oggettobulk;

				if (procedura.getProcedura_amministrativa() != null &&
					procedura.getProcedura_amministrativa().getIncarico_ric_giorni_pubbl() != null &&
					procedura.getProcedura_amministrativa().getIncarico_ric_giorni_pubbl().compareTo(new Integer(0)) == 1){
					java.util.GregorianCalendar gc_data_pubblicazione = getGregorianCalendar();
					gc_data_pubblicazione.setTime(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
					procedura.setDt_pubblicazione(new Timestamp(gc_data_pubblicazione.getTime().getTime()));
		
					java.util.GregorianCalendar gc_data_fine_pubblicazione = getGregorianCalendar();
					gc_data_fine_pubblicazione.setTime(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
					gc_data_fine_pubblicazione.add(java.util.Calendar.DAY_OF_YEAR,procedura.getProcedura_amministrativa().getIncarico_ric_giorni_pubbl());
					procedura.setDt_fine_pubblicazione(new Timestamp(gc_data_fine_pubblicazione.getTime().getTime()));
				}	
				if (procedura.getProcedura_amministrativa() != null &&
					procedura.getProcedura_amministrativa().getIncarico_ric_giorni_scad() != null &&
					procedura.getProcedura_amministrativa().getIncarico_ric_giorni_scad().compareTo(new Integer(0)) == 1){
					java.util.GregorianCalendar gc_data_scadenza = getGregorianCalendar();
					gc_data_scadenza.setTime(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
					gc_data_scadenza.add(java.util.Calendar.DAY_OF_YEAR,procedura.getProcedura_amministrativa().getIncarico_ric_giorni_pubbl()+procedura.getProcedura_amministrativa().getIncarico_ric_giorni_scad());
					procedura.setDt_scadenza(new Timestamp(gc_data_scadenza.getTime().getTime()));
				}
				
				procedura.setStato(Incarichi_proceduraBulk.STATO_PUBBLICATA);
			}
			updateBulk(usercontext, oggettobulk);
			return oggettobulk;
		}
		catch(Throwable throwable)
        {
            throw handleException(throwable);
        }
	}
	protected OggettoBulk eseguiCreaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		if (oggettobulk instanceof Incarichi_proceduraBulk)
			aggiornaImportiRepertorioLimiti(usercontext, (Incarichi_proceduraBulk)oggettobulk, INSERIMENTO);
		oggettobulk = super.eseguiCreaConBulk(usercontext, oggettobulk);
		if (oggettobulk instanceof Incarichi_proceduraBulk)
			archiviaAllegati(usercontext, (Incarichi_proceduraBulk)oggettobulk);
		return oggettobulk;
	}
	protected OggettoBulk eseguiModificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		if (oggettobulk instanceof Incarichi_proceduraBulk) {
			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)oggettobulk;
			aggiornaImportiRepertorioLimiti(usercontext, procedura, procedura.isProceduraAnnullata()?CANCELLAZIONE:MODIFICA);
			if (procedura.isProceduraDefinitiva())
				procedura.setStato(Incarichi_proceduraBulk.STATO_DEFINITIVO);
			else if (procedura.getStato().equals(Incarichi_proceduraBulk.STATO_DEFINITIVO))
				procedura.setStato(Incarichi_proceduraBulk.STATO_PUBBLICATA);
		}				
		oggettobulk = super.eseguiModificaConBulk(usercontext, oggettobulk);
		if (oggettobulk instanceof Incarichi_proceduraBulk)
			archiviaAllegati(usercontext, (Incarichi_proceduraBulk)oggettobulk);
		return oggettobulk;
	}
	protected void validaCreaModificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		super.validaCreaModificaConBulk(usercontext, oggettobulk);
		if (oggettobulk instanceof Incarichi_proceduraBulk) {
			Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)oggettobulk;

			if (procedura.getProcedura_amministrativa()==null ||
				!procedura.getProcedura_amministrativa().isMeramenteOccasionaleEnabled())
				procedura.setFl_meramente_occasionale(Boolean.FALSE);
				
			if (procedura.getFl_meramente_occasionale().booleanValue())
				procedura.setFl_pubblica_contratto(Boolean.FALSE);
			else
				procedura.setFl_pubblica_contratto(procedura.getTipo_attivita().getFl_pubblica_contratto());

			for (Iterator i=procedura.getIncarichi_repertorioColl().iterator();i.hasNext();){
				Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)i.next();
				incarico.setImporto_lordo(procedura.getImporto_lordo());
				incarico.setImporto_complessivo(procedura.getImporto_complessivo());

				if (procedura.getFl_meramente_occasionale().booleanValue())
					incarico.setFl_pubblica_contratto(Boolean.FALSE);
				else
					incarico.setFl_pubblica_contratto(procedura.getTipo_attivita().getFl_pubblica_contratto());
				
			}
		}
		if (oggettobulk instanceof Incarichi_proceduraBulk)
			validaProceduraIncarico(usercontext, (Incarichi_proceduraBulk)oggettobulk);
	}
	public OggettoBulk chiudiProceduraIncaricoPubblicata(UserContext aUC, OggettoBulk bulk) throws ComponentException {
		try {
			if (bulk instanceof Incarichi_proceduraBulk){
				Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) getHome( aUC, Incarichi_proceduraBulk.class).findByPrimaryKey( bulk );
			
				if ( procedura == null )
					throw new ApplicationException( "La procedura di conferimento incarico e' stata cancellata" );
	
				procedura = (Incarichi_proceduraBulk)inizializzaBulkPerModifica(aUC, procedura);
		
				for (Iterator i=procedura.getIncarichi_procedura_annoColl().iterator();i.hasNext();) {
					Incarichi_procedura_annoBulk dett = (Incarichi_procedura_annoBulk)i.next();

					if (dett.getImporto_complessivo().compareTo(dett.getImporto_utilizzato())==1) 
						dett.setImporto_complessivo(dett.getImporto_utilizzato());
					
					dett.setToBeUpdated();
				}
					
				procedura.setStato(Incarichi_proceduraBulk.STATO_CHIUSO);
				procedura.setDt_cancellazione( DateServices.getDt_valida(aUC));
				procedura.setToBeUpdated();
	
				aggiornaImportiRepertorioLimiti(aUC, procedura, MODIFICA);
				
				makeBulkPersistent( aUC, procedura);
				
				return procedura;
			}
			return bulk;
	    } catch (Exception e) {
	        throw handleException(e);
	    }
	}
	public it.cnr.jada.persistency.sql.SQLBuilder selectV_terzoByClause(UserContext userContext, Incarichi_repertorioBulk incarico, V_terzo_per_compensoBulk contraente, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		try
		{
			V_terzo_per_compensoHome home = (V_terzo_per_compensoHome)getHome(userContext,V_terzo_per_compensoBulk.class,"DISTINCT_TERZO");
			return home.selectVTerzo(Tipo_rapportoBulk.ALTRO, incarico.getV_terzo().getCd_terzo(), incarico.getDt_inizio_validita(), incarico.getDt_inizio_validita(), clauses);
		}
		catch(it.cnr.jada.persistency.PersistencyException ex){throw handleException(ex);}
	}

	/*Metodo che restituisce la disponibilità di importi non utilizzati per l'attivazione di contratti*/
	public OggettoBulk concludiProceduraIncaricoPubblicata(UserContext aUC, OggettoBulk bulk) throws ComponentException {
		try {
			if (bulk instanceof Incarichi_proceduraBulk){
				Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) getHome( aUC, Incarichi_proceduraBulk.class).findByPrimaryKey( bulk );

				if ( procedura == null )
					throw new ApplicationException( "La procedura di conferimento incarico e' stata cancellata." );
	
				procedura = (Incarichi_proceduraBulk)inizializzaBulkPerModifica(aUC, procedura);
		
				if (procedura.getIncarichi_repertorioValidiColl().size()<procedura.getNr_contratti()) {
					procedura.setNr_contratti_iniziale(procedura.getNr_contratti());
					procedura.setNr_contratti(procedura.getIncarichi_repertorioValidiColl().size());

				    BigDecimal totImportodaSottrarre = procedura.getImporto_complessivo().multiply(new BigDecimal(procedura.getNr_contratti_iniziale()-procedura.getNr_contratti()));;

				    if (totImportodaSottrarre.compareTo(Utility.ZERO)==1) {
 				    	for (Iterator i=procedura.getIncarichi_procedura_annoColl().iterator();i.hasNext();) {
 				    		Incarichi_procedura_annoBulk dett = (Incarichi_procedura_annoBulk)i.next();

 				    		if (dett.getImporto_complessivo().compareTo(dett.getImporto_ripartito())==1) {
 				    			BigDecimal impRecuperabile = dett.getImporto_complessivo().subtract(dett.getImporto_ripartito());
 				    			if (impRecuperabile.compareTo(totImportodaSottrarre)!=1){
 				    				totImportodaSottrarre = totImportodaSottrarre.subtract(impRecuperabile);
 				    				dett.setImporto_complessivo(dett.getImporto_ripartito());
 				    				dett.setToBeUpdated();
 				    			} else {
 				    				dett.setImporto_complessivo(dett.getImporto_complessivo().subtract(totImportodaSottrarre));
 				    				dett.setToBeUpdated();
 									totImportodaSottrarre = Utility.ZERO;
 				    				break;
 				    			}
 				    		}
 				    	}
 				    	
 				    	if (totImportodaSottrarre.compareTo(Utility.ZERO)==1)
 	 						throw new ApplicationException( "Operazione non possibile. L'importo da ridurre risulta già essere stato utilizzato." );
 				    }		
				}
					
		    	Integer contadef=0, containv=0;
				for (Iterator i=procedura.getIncarichi_repertorioValidiColl().iterator();i.hasNext();) {
					Incarichi_repertorioBulk incaricoColl = (Incarichi_repertorioBulk)i.next();
		    		if ( incaricoColl.isIncaricoDefinitivo() || incaricoColl.isIncaricoChiuso() ) 
		    			contadef=contadef+1;
		    		if ( incaricoColl.isIncaricoInviatoCorteConti() ) 
		    			containv=containv+1;
				}
				if (procedura.getNr_contratti().equals(contadef)) {
					if (!procedura.isProceduraDefinitiva()) 
						procedura.setStato(Incarichi_proceduraBulk.STATO_DEFINITIVO);
				}
				else if (procedura.getNr_contratti().equals(contadef+containv)) {
					if (!procedura.isProceduraInviataCorteConti()) 
						procedura.setStato(Incarichi_proceduraBulk.STATO_INVIATO);
				}

				procedura.setToBeUpdated();

				aggiornaImportiRepertorioLimiti(aUC, procedura, MODIFICA);
				
				makeBulkPersistent( aUC, procedura);
				
				return procedura;
			}
			return bulk;
	    } catch (Exception e) {
	        throw handleException(e);
	    }
	}
	/*Metodo che restituisce la disponibilità di importi non utilizzati per l'attivazione di contratti*/
	public OggettoBulk concludiIncaricoPubblicato(UserContext aUC, OggettoBulk bulk, Incarichi_repertorioBulk incarico) throws ComponentException {
		try {
			if (bulk instanceof Incarichi_proceduraBulk){
				Utility.createIncarichiRepertorioComponentSession().chiudiIncaricoPubblicato(aUC, incarico); 

				Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk) getHome( aUC, Incarichi_proceduraBulk.class).findByPrimaryKey( bulk );

				if ( procedura == null )
					throw new ApplicationException( "La procedura di conferimento incarico e' stata cancellata." );
	
				procedura = (Incarichi_proceduraBulk)inizializzaBulkPerModifica(aUC, procedura);
				
				if ( procedura.getIncarichi_repertorioValidiColl().isEmpty() || incarico == null ||
					 !procedura.getIncarichi_repertorioColl().containsByPrimaryKey(incarico))
					throw new ApplicationException( "L'incarico da concludere non risulta associato alla procedura di conferimento indicata." );
				else if ( !procedura.getIncarichi_repertorioValidiColl().containsByPrimaryKey(incarico))
					throw new ApplicationException( "L'incarico da concludere risulta avere uno stato non compatibile per la sua conclusione." );

/*				boolean isEsitoControlloNegativo = incarico.isIncaricoInviatoCorteConti() && incarico.getEsito_corte_conti()!=null &&
												   incarico.getEsito_corte_conti().equals(Incarichi_repertorioBulk.ESITO_ILLEGITTIMO);

				if (isEsitoControlloNegativo && incarico.getAttoEsitoControllo()==null) {
					if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
						//Istanzio la classe per riempire tipo_archivioKeys
						new Incarichi_procedura_archivioBulk(); 
					}

					if (incarico.getV_terzo()!=null && incarico.getV_terzo().getCognome()!=null && incarico.getV_terzo().getNome()!=null)
						throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto del terzo \""+incarico.getV_terzo().getCognome()+" "+incarico.getV_terzo().getNome()+"\" un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_ATTO_ESITO_CONTROLLO).toString()+"\".");
					else
						throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_ATTO_ESITO_CONTROLLO).toString()+"\".");
				}

				BigDecimal totVariazione = BigDecimal.ZERO;
				for (Iterator i=incarico.getIncarichi_repertorio_annoColl().iterator();i.hasNext();) {
					Incarichi_repertorio_annoBulk incAnno = (Incarichi_repertorio_annoBulk)i.next();

					if (incAnno.getImporto_complessivo().compareTo(incAnno.getImporto_utilizzato())==1){
						for (Iterator y=procedura.getIncarichi_procedura_annoColl().iterator();y.hasNext();) {
							Incarichi_procedura_annoBulk procAnno = (Incarichi_procedura_annoBulk)y.next();
							if (procAnno.getEsercizio_limite().equals(incAnno.getEsercizio_limite())) {
								procAnno.setImporto_complessivo(procAnno.getImporto_complessivo().subtract(incAnno.getImporto_complessivo().subtract(incAnno.getImporto_utilizzato())));
								procAnno.setToBeUpdated();
							}
						}
						totVariazione = totVariazione.add(incAnno.getImporto_complessivo().subtract(incAnno.getImporto_utilizzato()).negate());
						incAnno.setImporto_complessivo(incAnno.getImporto_utilizzato());
						incAnno.setToBeUpdated();
					}
				}
					
				if (totVariazione.compareTo(BigDecimal.ZERO)!=0){
					Incarichi_repertorio_varBulk variazione = new Incarichi_repertorio_varBulk();
					incarico.addToIncarichi_repertorio_varColl(variazione);
					variazione.setDt_variazione(DateServices.getDt_valida(aUC));
					if (isEsitoControlloNegativo)
						variazione.setDs_variazione("Variazione automatica generata in fase di annullamento incarico per esito negativo del controllo di legittima' da parte della Corte dei Conti.");
					else
						variazione.setDs_variazione("Variazione automatica generata in fase di chiusura incarico.");
					variazione.setTipo_variazione(Incarichi_repertorio_varBulk.TIPO_VARIAZIONE_GENERICA);
					variazione.setImporto_complessivo(totVariazione);
					variazione.setStato(Incarichi_repertorio_varBulk.STATO_VALIDO);
					variazione.setToBeCreated();
				}

				incarico.setStato(Incarichi_repertorioBulk.STATO_CHIUSO);
				incarico.setDt_cancellazione( DateServices.getDt_valida(aUC));
				incarico.setToBeUpdated();

				makeBulkPersistent( aUC, incarico);
*/				
		    	Integer contadef=0, containv=0;
				for (Iterator i=procedura.getIncarichi_repertorioValidiColl().iterator();i.hasNext();) {
					Incarichi_repertorioBulk incaricoColl = (Incarichi_repertorioBulk)i.next();
		    		if ( incaricoColl.isIncaricoDefinitivo() || incaricoColl.isIncaricoChiuso() ) 
		    			contadef=contadef+1;
		    		if ( incaricoColl.isIncaricoInviatoCorteConti() ) 
		    			containv=containv+1;
				}
				if (procedura.getNr_contratti().equals(contadef)) {
					if (!procedura.isProceduraDefinitiva()) 
						procedura.setStato(Incarichi_proceduraBulk.STATO_DEFINITIVO);
				}
				else if (procedura.getNr_contratti().equals(contadef+containv)) {
					if (!procedura.isProceduraInviataCorteConti()) 
						procedura.setStato(Incarichi_proceduraBulk.STATO_INVIATO);
				}

				procedura.setToBeUpdated();

				aggiornaImportiRepertorioLimiti(aUC, procedura, MODIFICA);
				
				makeBulkPersistent( aUC, procedura);
				
				return procedura;
			}
			return bulk;
	    } catch (Exception e) {
	        throw handleException(e);
	    }
	}
	public it.cnr.jada.persistency.sql.SQLBuilder selectV_terzoForSearchByClause(UserContext userContext, Incarichi_proceduraBulk procedura, V_terzo_per_compensoBulk contraente, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		try
		{
			V_terzo_per_compensoHome home = (V_terzo_per_compensoHome)getHome(userContext,V_terzo_per_compensoBulk.class,"DISTINCT_TERZO");
			return home.selectVTerzo(Tipo_rapportoBulk.ALTRO, procedura.getV_terzoForSearch().getCd_terzo(), procedura.getDt_registrazione(), procedura.getDt_registrazione(), clauses);
		}
		catch(it.cnr.jada.persistency.PersistencyException ex){throw handleException(ex);}
	}
	public OggettoBulk caricaSedeUnitaOrganizzativa(UserContext userContext, OggettoBulk bulk) throws ComponentException 
	{
		try {
			if (bulk instanceof Incarichi_proceduraBulk){
				if (bulk!=null && 
					((Incarichi_proceduraBulk)bulk).getUnita_organizzativa()!=null && 
					((Incarichi_proceduraBulk)bulk).getUnita_organizzativa().getCd_unita_organizzativa()!=null) { 	
					TerzoBulk terzo = Utility.createTerzoComponentSession().cercaTerzoPerUnitaOrganizzativa(userContext, ((Incarichi_proceduraBulk)bulk).getUnita_organizzativa());
					String indirizzo = "";
			
					if (terzo != null) {
						if (terzo.getVia_sede() != null)
							indirizzo = indirizzo + terzo.getVia_sede();
						if (terzo.getNumero_civico_sede() != null)
							indirizzo = indirizzo +  ", " + terzo.getNumero_civico_sede();
			
						((Incarichi_proceduraBulk)bulk).setIndirizzo_unita_organizzativa(indirizzo);
						((Incarichi_proceduraBulk)bulk).setCitta((ComuneBulk)getHome(userContext, ComuneBulk.class).findByPrimaryKey(terzo.getComune_sede()));
					}
				}
			}
	    } catch (Exception e) {
	        throw handleException(e);
	    }
		return bulk;
	}

	public Incarichi_parametriBulk getIncarichiParametri(UserContext aUC, Incarichi_proceduraBulk procedura) throws ComponentException {
		if (procedura==null || 
			procedura.getProcedura_amministrativa()==null || procedura.getProcedura_amministrativa().getCd_proc_amm()==null ||
			procedura.getTipo_attivita()==null || procedura.getTipo_attivita().getCd_tipo_attivita()==null ||
			procedura.getTipo_incarico()==null || procedura.getTipo_incarico().getCd_tipo_incarico()==null ||
			procedura.getTipo_natura()==null)
			return null;
		
		try {
			Incarichi_parametri_configHome home = (Incarichi_parametri_configHome)getHome(aUC, Incarichi_parametri_configBulk.class);
	
			it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
	
			sql.openParenthesis(FindClause.AND);
				sql.addClause(FindClause.OR, "cd_proc_amm", SQLBuilder.EQUALS, procedura.getCd_proc_amm());
				sql.addClause(FindClause.OR, "cd_proc_amm", SQLBuilder.ISNULL, null);
			sql.closeParenthesis();
			sql.openParenthesis(FindClause.AND);
				sql.addClause(FindClause.OR, "cd_tipo_attivita", SQLBuilder.EQUALS, procedura.getCd_tipo_attivita());
				sql.addClause(FindClause.OR, "cd_tipo_attivita", SQLBuilder.ISNULL, null);
			sql.closeParenthesis();
				sql.openParenthesis(FindClause.AND);
				sql.addClause(FindClause.OR, "cd_tipo_incarico", SQLBuilder.EQUALS, procedura.getCd_tipo_incarico());
				sql.addClause(FindClause.OR, "cd_tipo_incarico", SQLBuilder.ISNULL, null);
			sql.closeParenthesis();
			sql.openParenthesis(FindClause.AND);
				sql.addClause(FindClause.OR, "tipo_natura", SQLBuilder.EQUALS, procedura.getTipo_natura());
				sql.addClause(FindClause.OR, "tipo_natura", SQLBuilder.ISNULL, null);
			sql.closeParenthesis();
			sql.openParenthesis(FindClause.AND);
				sql.addClause(FindClause.OR, "meramente_occasionale", SQLBuilder.EQUALS, procedura.getFl_meramente_occasionale()?"Y":"N");
				sql.addClause(FindClause.OR, "meramente_occasionale", SQLBuilder.ISNULL, null);
			sql.closeParenthesis();
			sql.openParenthesis(FindClause.AND);
				sql.addClause(FindClause.OR, "art51", SQLBuilder.EQUALS, procedura.getFl_art51()?"Y":"N");
				sql.addClause(FindClause.OR, "art51", SQLBuilder.ISNULL, null);
			sql.closeParenthesis();

			java.util.List list = home.fetchAll(sql);
			
			if (!list.isEmpty()) {
				Incarichi_parametriBulk parametriDefinitivi = new Incarichi_parametriBulk();

				for (Iterator i = list.iterator(); i.hasNext();) {
					Incarichi_parametri_configBulk config = (Incarichi_parametri_configBulk) i.next();
					Incarichi_parametriBulk parametri = (Incarichi_parametriBulk)getHome(aUC, config.getIncarichi_parametri()).findByPrimaryKey(config.getIncarichi_parametri());

					if (config.getIncarichi_parametri()!=null) {
						if (!(parametriDefinitivi.getAllega_contratto()!=null && parametriDefinitivi.getAllega_contratto().equals("Y")) &&
							parametri.getAllega_contratto()!=null)
							parametriDefinitivi.setAllega_contratto(parametri.getAllega_contratto());
						if (!(parametriDefinitivi.getAllega_decisione_ctr()!=null && parametriDefinitivi.getAllega_decisione_ctr().equals("Y")) &&
						    parametri.getAllega_decisione_ctr()!=null)
							parametriDefinitivi.setAllega_decisione_ctr(parametri.getAllega_decisione_ctr());
						if (!(parametriDefinitivi.getAllega_decreto_nomina()!=null && parametriDefinitivi.getAllega_decreto_nomina().equals("Y")) &&
						    parametri.getAllega_decreto_nomina()!=null)
							parametriDefinitivi.setAllega_decreto_nomina(parametri.getAllega_decreto_nomina());
						if (!(parametriDefinitivi.getMeramente_occasionale()!=null && parametriDefinitivi.getMeramente_occasionale().equals("Y")) &&
						    parametri.getMeramente_occasionale()!=null)
							parametriDefinitivi.setMeramente_occasionale(parametri.getMeramente_occasionale());
						if (!(parametriDefinitivi.getPubblica_contratto()!=null && parametriDefinitivi.getPubblica_contratto().equals("Y")) &&
							parametri.getPubblica_contratto()!=null)
							parametriDefinitivi.setPubblica_contratto(parametri.getPubblica_contratto());
						if (!(parametriDefinitivi.getRicerca_interna()!=null && parametriDefinitivi.getRicerca_interna().equals("Y")) &&
						    parametri.getRicerca_interna()!=null)
							parametriDefinitivi.setRicerca_interna(parametri.getRicerca_interna());
						if (!(parametriDefinitivi.getLimite_dt_stipula()!=null && parametriDefinitivi.getLimite_dt_stipula().equals("Y")) &&
						    parametri.getLimite_dt_stipula()!=null)
							parametriDefinitivi.setLimite_dt_stipula(parametri.getLimite_dt_stipula());

						if (parametri.getCd_gruppo_file()!=null) 
							parametriDefinitivi.getGruppoFileList().add(parametri.getCd_gruppo_file());
						if (parametri.getImporto_limite_merocc()!=null) {
							if (parametriDefinitivi.getImporto_limite_merocc()==null ||
								parametri.getImporto_limite_merocc().compareTo(parametriDefinitivi.getImporto_limite_merocc())==-1)
								parametriDefinitivi.setImporto_limite_merocc(parametri.getImporto_limite_merocc());
						}
						if (parametri.getIncarico_ric_giorni_pubbl()!=null) {
							if (parametriDefinitivi.getIncarico_ric_giorni_pubbl()==null ||
								parametri.getIncarico_ric_giorni_pubbl().compareTo(parametriDefinitivi.getIncarico_ric_giorni_pubbl())==-1)
								parametriDefinitivi.setIncarico_ric_giorni_pubbl(parametri.getIncarico_ric_giorni_pubbl());
						}
						if (parametri.getIncarico_ric_giorni_scad()!=null) {
							if (parametriDefinitivi.getIncarico_ric_giorni_scad()==null ||
								parametri.getIncarico_ric_giorni_scad().compareTo(parametriDefinitivi.getIncarico_ric_giorni_scad())==-1)
								parametriDefinitivi.setIncarico_ric_giorni_scad(parametri.getIncarico_ric_giorni_scad());
						}
						if (parametri.getGiorni_limite_dt_stipula()!=null) {
							if (parametriDefinitivi.getGiorni_limite_dt_stipula()==null ||
								parametri.getGiorni_limite_dt_stipula().compareTo(parametriDefinitivi.getGiorni_limite_dt_stipula())==-1)
								parametriDefinitivi.setGiorni_limite_dt_stipula(parametri.getGiorni_limite_dt_stipula());
						}
					}
				}
				return parametriDefinitivi;
			}
			return null;	
		} catch (PersistencyException e) {
	        throw handleException(e);
		}
	}
}
