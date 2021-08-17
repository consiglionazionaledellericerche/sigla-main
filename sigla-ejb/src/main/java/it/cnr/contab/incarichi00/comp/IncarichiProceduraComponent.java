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
import it.cnr.contab.incarichi00.bulk.*;
import it.cnr.contab.incarichi00.bulk.storage.*;
import it.cnr.contab.incarichi00.ejb.IncarichiRepertorioComponentSession;
import it.cnr.contab.incarichi00.ejb.RepertorioLimitiComponentSession;
import it.cnr.contab.incarichi00.service.ContrattiService;
import it.cnr.contab.incarichi00.storage.StorageContrattiAspect;
import it.cnr.contab.incarichi00.tabrif.bulk.*;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.SIGLAGroups;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
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
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.bulk.StorageFile;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;

public class IncarichiProceduraComponent extends CRUDComponent {
	private transient static final Logger logger = LoggerFactory.getLogger(IncarichiProceduraComponent.class);
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
				procedura.setFl_applicazione_norma(Incarichi_proceduraBulk.APPLICAZIONE_NORMA_YES);
				procedura.setFl_applicazione_norma_orig(Incarichi_proceduraBulk.APPLICAZIONE_NORMA_YES);
				procedura.setFl_migrata_to_cmis(Boolean.TRUE);
				
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
				procedura.setTipo_prestazione_orig(procedura.getTipo_prestazione());
				procedura.setTipo_norma_perla_orig(procedura.getTipo_norma_perla());
				procedura.setTipo_attivita_fp_orig(procedura.getTipo_attivita_fp());
				if (procedura.getTipologie_norma_perla()==null) 
					procedura.setFl_applicazione_norma_orig(Incarichi_proceduraBulk.APPLICAZIONE_NORMA_YES);
				
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
				if (procedura.getTipo_attivita_fp()!=null && procedura.getTipo_attivita_fp().getCd_tipo_attivita()!=null) {
					procedura.setTipo_attivita_fp1(procedura.getTipo_attivita_fp().getTipo_attivita_fp_padre());
					procedura.setTipo_attivita_fp0(procedura.getTipo_attivita_fp1().getTipo_attivita_fp_padre());
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
		SQLBuilder sql = (SQLBuilder) this.selectBase(userContext, clauses, bulk);

		sql.addTableToHeader("TIPO_ATTIVITA");
		sql.addSQLJoin("INCARICHI_PROCEDURA.CD_TIPO_ATTIVITA","TIPO_ATTIVITA.CD_TIPO_ATTIVITA");
		sql.addSQLClause(FindClause.AND, "TIPO_ATTIVITA.TIPO_ASSOCIAZIONE", SQLBuilder.EQUALS, Tipo_attivitaBulk.ASS_INCARICHI);
	
		sql.addTableToHeader("TIPO_INCARICO");
		sql.addSQLJoin("INCARICHI_PROCEDURA.CD_TIPO_INCARICO","TIPO_INCARICO.CD_TIPO_INCARICO");
		sql.addSQLClause(FindClause.AND, "TIPO_INCARICO.TIPO_ASSOCIAZIONE", SQLBuilder.EQUALS, Tipo_incaricoBulk.ASS_INCARICHI);

		return sql;
	}
	
	protected Query selectBase(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
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

		Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)bulk;
		boolean searchTerzo = procedura.getV_terzoForSearch()!=null && procedura.getV_terzoForSearch().getTerzo()!=null &&
				 			  procedura.getV_terzoForSearch().getTerzo().getCd_terzo()!=null;
		boolean searchIncarico = procedura.getIncaricoRepertorioForSearch()!=null &&
							  	 procedura.getIncaricoRepertorioForSearch().getEsercizio()!=null &&
							  	 procedura.getIncaricoRepertorioForSearch().getPg_repertorio()!=null;

		if (searchTerzo || searchIncarico) {
			SQLBuilder sqlExists = getHome(userContext, Incarichi_repertorioBulk.class).createSQLBuilder();
			sqlExists.addSQLJoin( "INCARICHI_REPERTORIO.ESERCIZIO_PROCEDURA", "INCARICHI_PROCEDURA.ESERCIZIO");
			sqlExists.addSQLJoin( "INCARICHI_REPERTORIO.PG_PROCEDURA", "INCARICHI_PROCEDURA.PG_PROCEDURA");
			if (searchTerzo)
				sqlExists.addClause(FindClause.AND, "cd_terzo", SQLBuilder.EQUALS, procedura.getV_terzoForSearch().getTerzo().getCd_terzo());
			if (searchIncarico) {
				sqlExists.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, procedura.getIncaricoRepertorioForSearch().getEsercizio());
				sqlExists.addClause(FindClause.AND, "pg_repertorio", SQLBuilder.EQUALS, procedura.getIncaricoRepertorioForSearch().getPg_repertorio());
			}
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
	protected it.cnr.jada.persistency.sql.SQLBuilder selectBaseIncarichi_procedura_padreByClause(UserContext userContext, Incarichi_proceduraBulk procedura, Incarichi_proceduraBulk proceduraPadre, it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException{
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
	public it.cnr.jada.persistency.sql.SQLBuilder selectIncarichi_procedura_padreByClause(UserContext userContext, Incarichi_proceduraBulk procedura, Incarichi_proceduraBulk proceduraPadre, it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException{
		SQLBuilder sql = selectBaseIncarichi_procedura_padreByClause(userContext, procedura, proceduraPadre, clauses);
		sql.addTableToHeader("TIPO_ATTIVITA");
		sql.addSQLJoin("INCARICHI_PROCEDURA.CD_TIPO_ATTIVITA","TIPO_ATTIVITA.CD_TIPO_ATTIVITA");
		sql.addSQLClause(FindClause.AND, "TIPO_ATTIVITA.TIPO_ASSOCIAZIONE", SQLBuilder.EQUALS, Tipo_attivitaBulk.ASS_INCARICHI);

		sql.addTableToHeader("TIPO_INCARICO");
		sql.addSQLJoin("INCARICHI_PROCEDURA.CD_TIPO_INCARICO","TIPO_INCARICO.CD_TIPO_INCARICO");
		sql.addSQLClause(FindClause.AND, "TIPO_INCARICO.TIPO_ASSOCIAZIONE", SQLBuilder.EQUALS, Tipo_incaricoBulk.ASS_INCARICHI);
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
		sql.addClause(FindClause.AND, "tipo_associazione", SQLBuilder.EQUALS, Tipo_incaricoBulk.ASS_INCARICHI);
		sql.addClause(FindClause.AND, "fl_cancellato", SQLBuilder.EQUALS, Boolean.FALSE);
		if (clause != null) 
		  sql.addClause(clause);
		sql.addOrderBy("CD_TIPO_ATTIVITA");
		return sql;
	}
	public SQLBuilder selectBaseTipo_incaricoByClause (UserContext userContext, OggettoBulk bulk, Tipo_incaricoBulk tipo_incarico, CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		    clause = tipo_incarico.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, tipo_incarico).createSQLBuilder();
		sql.addClause(FindClause.AND, "tipo_associazione", SQLBuilder.EQUALS, Tipo_incaricoBulk.ASS_INCARICHI);
		sql.addClause(FindClause.AND, "fl_cancellato", SQLBuilder.EQUALS, Boolean.FALSE);
		if (clause != null) 
		  sql.addClause(clause);
		sql.addOrderBy("CD_TIPO_INCARICO");
		return sql;
	}
	/**
	 * Pre:  Ricerca Tipo Incarico
	 * Post: Limitazione ai tipi non annullati
	 */
	public SQLBuilder selectTipo_incaricoByClause (UserContext userContext, OggettoBulk bulk, Tipo_incaricoBulk tipo_incarico, CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		SQLBuilder sql = selectBaseTipo_incaricoByClause(userContext, bulk, tipo_incarico, clause);

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

	public SQLBuilder selectProcedura_amministrativa_beneficiarioByClause (UserContext userContext, OggettoBulk bulk, Procedure_amministrativeBulk procedura_amministrativa_beneficario,CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		  clause = procedura_amministrativa_beneficario.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, procedura_amministrativa_beneficario).createSQLBuilder();

		sql.addClause(FindClause.AND, "ti_proc_amm", SQLBuilder.EQUALS, Procedure_amministrativeBulk.TIPO_INDIVIDUAZIONE_BENEFICIARIO);
		sql.addClause(FindClause.AND, "fl_cancellato", SQLBuilder.EQUALS, Boolean.FALSE);

		if (clause != null) 
		  sql.addClause(clause);
		return sql;
	}	

	private void validaProceduraIncarico(UserContext aUC,Incarichi_proceduraBulk procedura) throws ComponentException {
		Incarichi_proceduraBulk incaricoOld = null;
		try {
			if (!procedura.isToBeCreated())
				incaricoOld = (Incarichi_proceduraBulk) getTempHome(aUC, procedura.getClass()).findByPrimaryKey(procedura);
		} catch (PersistencyException e) {
		}

		if (procedura.getTerzo_resp()==null || procedura.getTerzo_resp().getCd_terzo()==null)
			throw handleException( new ApplicationException( "Il campo \"Responsabile del procedimento\" non può essere vuoto.") );
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
		
			if (incarico.getFl_inviato_corte_conti() && procedura.getTipo_prestazione()!=null && 
				procedura.getTipo_prestazione().getTipo_classificazione().equals("LEGGE"))
				throw handleException( new ApplicationException((incarico.getPg_repertorio()!=null?"Contratto "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - ":"")+"Deselezionare il flag \"Invio alla corte dei conti\" in quanto non previsto su incarichi per prestazioni previsti da norme di legge!") );
			else if (incarico.getTerzo()==null || incarico.getTerzo().getCd_terzo()==null)
				throw handleException( new ApplicationException(incarico.getPg_repertorio()!=null?"Contratto "+incarico.getEsercizio()+"/"+incarico.getPg_repertorio()+" - completare le informazioni relative al terzo!":"Completare le informazioni relative al terzo!") );
			else if (incarico.getTerzo()!=null || incarico.getTerzo().getCd_terzo()!=null) {
				if (incarico.getTerzo().getAnagrafico()!=null && !incarico.getTerzo().getAnagrafico().isPersonaFisica())
					throw handleException( new ApplicationException( "Il terzo selezionato (cod: "+incarico.getTerzo().getCd_terzo()+") non risulta essere una persona fisica!") );
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
				if (incarico.getCurriculumVincitore()==null) {
					if (parametri!=null && parametri.getAllega_curriculum_vitae()!=null && parametri.getAllega_curriculum_vitae().equals("Y")) { 
						if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
							//Istanzio la classe per riempire tipo_archivioKeys
							new Incarichi_procedura_archivioBulk(); 
						}
	
						if (incarico.getV_terzo()!=null && incarico.getV_terzo().getCognome()!=null && incarico.getV_terzo().getNome()!=null)
							throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto del terzo \""+incarico.getV_terzo().getCognome()+" "+incarico.getV_terzo().getNome()+"\" un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_CURRICULUM_VINCITORE).toString()+"\".");
						else
							throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_CURRICULUM_VINCITORE).toString()+"\".");
					}
				}
				//Il documento viene richiesto solo in fase di salvataggio definitivo della procedura.
				//In caso di semplice cambio importi per anno non deve scattare il controllo, dato che per incarichi vecchi
				//tale documento non esiste.
				if (incarico.getConflittoInteressi()==null &&
						!Optional.ofNullable(incaricoOld).map(Incarichi_proceduraBulk::isProceduraDefinitiva).orElse(Boolean.FALSE)) {
					if (parametri!=null && parametri.getAllega_conflitto_interesse()!=null && parametri.getAllega_conflitto_interesse().equals("Y")) {
						if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
							//Istanzio la classe per riempire tipo_archivioKeys
							new Incarichi_procedura_archivioBulk();
						}

						if (incarico.getV_terzo()!=null && incarico.getV_terzo().getCognome()!=null && incarico.getV_terzo().getNome()!=null)
							throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto del terzo \""+incarico.getV_terzo().getCognome()+" "+incarico.getV_terzo().getNome()+"\" un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_CONFLITTO_INTERESSI).toString()+"\".");
						else
							throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_CONFLITTO_INTERESSI).toString()+"\".");
					}
				}
				//Il documento viene richiesto solo in fase di salvataggio definitivo della procedura.
				//In caso di semplice cambio importi per anno non deve scattare il controllo, dato che per incarichi vecchi
				//tale documento non esiste.
				if (incarico.getAttestazioneDirettore()==null &&
						!Optional.ofNullable(incaricoOld).map(Incarichi_proceduraBulk::isProceduraDefinitiva).orElse(Boolean.FALSE)) {
					if (parametri!=null && parametri.getAllega_attestazione_direttore()!=null && parametri.getAllega_attestazione_direttore().equals("Y")) {
						if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
							//Istanzio la classe per riempire tipo_archivioKeys
							new Incarichi_procedura_archivioBulk();
						}

						if (incarico.getV_terzo()!=null && incarico.getV_terzo().getCognome()!=null && incarico.getV_terzo().getNome()!=null)
							throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto del terzo \""+incarico.getV_terzo().getCognome()+" "+incarico.getV_terzo().getNome()+"\" un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_ATTESTAZIONE_DIRETTORE).toString()+"\".");
						else
							throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_ATTESTAZIONE_DIRETTORE).toString()+"\".");
					}
				}
				if (incarico.getDecretoDiNomina()==null) {
					if (parametri!=null && parametri.getAllega_decreto_nomina()!=null && parametri.getAllega_decreto_nomina().equals("Y")) { 
						if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
							//Istanzio la classe per riempire tipo_archivioKeys
							new Incarichi_procedura_archivioBulk(); 
						}

						if (incarico.getV_terzo()!=null && incarico.getV_terzo().getCognome()!=null && incarico.getV_terzo().getNome()!=null)
							throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto del terzo \""+incarico.getV_terzo().getCognome()+" "+incarico.getV_terzo().getNome()+"\" un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_DECRETO_DI_NOMINA).toString()+"\".");
						else
							throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_DECRETO_DI_NOMINA).toString()+"\".");
					}
				}
				if (incarico.getFl_inviato_corte_conti() && incarico.getEsito_corte_conti()!=null && incarico.getAttoEsitoControllo()==null) {
					if (Incarichi_procedura_archivioBulk.tipo_archivioKeys.isEmpty()) {
						//Istanzio la classe per riempire tipo_archivioKeys
						new Incarichi_procedura_archivioBulk(); 
					}

					if (incarico.getV_terzo()!=null && incarico.getV_terzo().getCognome()!=null && incarico.getV_terzo().getNome()!=null)
						throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto del terzo \""+incarico.getV_terzo().getCognome()+" "+incarico.getV_terzo().getNome()+"\" un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_ATTO_ESITO_CONTROLLO).toString()+"\".");
					else
						throw new it.cnr.jada.comp.ApplicationException("Allegare al contratto un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_ATTO_ESITO_CONTROLLO).toString()+"\".");
				}
			    if (procedura.getDecisioneAContrattare()==null) {
					if (parametri==null || parametri.getAllega_decisione_ctr()==null || parametri.getAllega_decisione_ctr().equals("Y")) 
						throw new it.cnr.jada.comp.ApplicationException("Allegare alla \"Procedura di conferimento incarico\" un file di tipo \""+Incarichi_procedura_archivioBulk.tipo_archivioKeys.get(Incarichi_procedura_archivioBulk.TIPO_DECISIONE_A_CONTRATTARE).toString()+"\".");
			    }
			    //Controllo che sia stata inserita la dichiarazione del contraente....almeno quella del primo anno di validità dell'incarico 
			    if (procedura.isDichiarazioneContraenteRequired()) {
				    boolean existRapportoAnnoStipula = false;
					GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
					data_da.setTime(incarico.getDt_stipula());
					if (incarico.getIncarichi_repertorio_rappColl()!=null && !incarico.getIncarichi_repertorio_rappColl().isEmpty()) {
						for (Iterator y = incarico.getIncarichi_repertorio_rappColl().iterator(); y.hasNext();) {
							Incarichi_repertorio_rappBulk rapporto = (Incarichi_repertorio_rappBulk) y.next();
							if (!rapporto.isAnnullato() && rapporto.getAnno_competenza().equals(data_da.get(java.util.Calendar.YEAR)))
								existRapportoAnnoStipula = true;
						}
					}
					if (!existRapportoAnnoStipula) {
						if (incarico.getV_terzo()!=null && incarico.getV_terzo().getCognome()!=null && incarico.getV_terzo().getNome()!=null)
							throw new it.cnr.jada.comp.ApplicationException("Inserire la dichiarazione dell'anno "+data_da.get(java.util.Calendar.YEAR)+", anno di stipula del contratto, resa dal terzo \""+incarico.getV_terzo().getCognome()+" "+incarico.getV_terzo().getNome()+"\" sui rapporti stipulati con altri enti.");
						else
							throw new it.cnr.jada.comp.ApplicationException("Inserire la dichiarazione dell'anno "+data_da.get(java.util.Calendar.YEAR)+", anno di stipula del contratto, resa dal terzo sui rapporti stipulati con altri enti.");
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

	public void archiviaAllegati(UserContext userContext, Incarichi_proceduraBulk procedura) throws ComponentException{
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
		
		BulkList<Incarichi_archivioBulk> listArchiviFile = new BulkList<Incarichi_archivioBulk>();
		listArchiviFile.addAll(procedura.getArchivioAllegati());
		for (Iterator<Incarichi_repertorioBulk> i = procedura.getIncarichi_repertorioColl().iterator();i.hasNext();){
			Incarichi_repertorioBulk incarico = i.next();
			listArchiviFile.addAll(incarico.getArchivioAllegati());
			listArchiviFile.addAll(incarico.getIncarichi_repertorio_varColl());
			listArchiviFile.addAll(incarico.getIncarichi_repertorio_rappColl());
		}

		for (Iterator<Incarichi_archivioBulk> i = listArchiviFile.iterator(); i.hasNext();) {
			Incarichi_archivioBulk allegato = i.next();
			if (!(allegato.getFile() == null || allegato.getFile().getName().equals(""))) {
				String nomeFile = allegato.getFile().getName();
				String estensioneFile = nomeFile.substring(nomeFile.lastIndexOf(".")+1);
				if (listFileAllegabili != null && !listFileAllegabili.isEmpty()){
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
				if (!estensioneFile.equalsIgnoreCase("pdf")) {
					if (allegato.isCurriculumVincitore())
						throw new ApplicationException("File non valido!\nIl formato del file consentito per il Curriculum Vitae è il pdf.");
					if (allegato.isAggiornamentoCurriculumVincitore())
						throw new ApplicationException("File non valido!\nIl formato del file consentito per l'aggiornamento del Curriculum Vitae è il pdf.");
					if (allegato.isAllegatoGenerico() && allegato instanceof Incarichi_repertorio_rappBulk)
						throw new ApplicationException("File non valido!\nIl formato del file consentito per la Dichiarazione Altri Rapporti è il pdf.");
				}
			}
		}
		archiviaAllegatiCMIS(userContext, listArchiviFile, procedura);
	}

	private void archiviaAllegatiCMIS(UserContext userContext, BulkList<Incarichi_archivioBulk> listArchiviFile, Incarichi_proceduraBulk procedura) throws ComponentException{
		List<StorageFile> storageFileCreate = new ArrayList<StorageFile>();
		List<StorageFile> storageFileAnnullati = new ArrayList<StorageFile>();
		ContrattiService contrattiService = SpringUtil.getBean(ContrattiService.class);
		try {
			for (Iterator<Incarichi_archivioBulk> i = listArchiviFile.iterator(); i.hasNext();) {
				Incarichi_archivioBulk allegato = i.next();
	
				StorageFile storageFile = null;
				if (allegato.getCms_node_ref()==null)
					try{
						storageFile = allegato.getCMISFile();
					} catch (IOException e) {
						throw new ApplicationException("CMIS - Errore nella registrazione degli allegati (" + e.getMessage() + ")");
					}
				else 
					storageFile = allegato.getCMISFile(contrattiService.getStorageObjectBykey(allegato.getCms_node_ref()));

				if (Optional.ofNullable(storageFile).isPresent()) {
					//E' previsto solo l'inserimento ma non l'aggiornamento
					if (allegato.getCms_node_ref()==null || allegato.isAnnullato()) {
						String path = storageFile.getStorageParentPath();
						String alternativePath = null;
						if (allegato.getNome_file()!=null)
							alternativePath = storageFile.getStorageAlternativeParentPath();
						
						if (allegato.getCms_node_ref()==null) {
							if (storageFile.getInputStream()==null) {
								if (Optional.of(allegato).filter(Incarichi_repertorio_varBulk.class::isInstance)
										.map(Incarichi_repertorio_varBulk.class::cast)
										.filter(el -> !el.isVariazioneIntegrazioneIncarico())
										.isPresent())
									continue;
								else
									throw new ApplicationException("Errore nella registrazione degli allegati - Non è stato possibile recuperare InputStream");
							}

							try {
								StorageObject storageObject =
                                        contrattiService.restoreSimpleDocument(
												storageFile,
												storageFile.getInputStream(),
												storageFile.getContentType(),
												storageFile.getFileName(),
												path, true);
								storageFile.setStorageObject(storageObject);
								storageFileCreate.add(storageFile);
								if (alternativePath!=null)
									try{
										contrattiService.copyNode(storageObject, contrattiService.getStorageObjectByPath(alternativePath, true));
									} catch (StorageException e) {
									}
								allegato.setCms_node_ref(storageObject.getKey());
								allegato.setToBeUpdated();
								updateBulk(userContext, allegato);
							} catch (StorageException e) {
							    if (e.getType().equals(StorageException.Type.CONSTRAINT_VIOLATED))
                                    throw new ApplicationException("CMIS - File ["+ storageFile.getFileName()+"] già presente o non completo di tutte le proprietà obbligatorie. Inserimento non possibile!");
                                throw new ApplicationException("CMIS - Errore nella registrazione degli allegati (" + e.getMessage() + ")");
							}
						} 
						if (allegato.isAnnullato()) {
							StorageObject storageObject = storageFile.getStorageObject();
							if (storageObject!=null && !storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value())
                                    .contains(StorageContrattiAspect.SIGLA_CONTRATTI_STATO_ANNULLATO.value())) {
								String cmisFileName = storageFile.getFileName();
								String cmisFileEstensione = cmisFileName.substring(cmisFileName.lastIndexOf(".")+1);
								storageFile.setFileName(cmisFileName.replace("."+cmisFileEstensione, "-ANNULLATO."+cmisFileEstensione));
								Boolean CMISAggiornato=Boolean.FALSE;
								int numFile=0;
								do {
									try {
										contrattiService.updateProperties(storageFile, storageObject);
                                        List<String> aspects = storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
                                        aspects.add(StorageContrattiAspect.SIGLA_CONTRATTI_STATO_ANNULLATO.value());
                                        contrattiService.updateProperties(Collections.singletonMap(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), aspects), storageObject);
										storageFile.setStorageObject(contrattiService.getStorageObjectBykey(storageObject.getKey()));
										storageFileAnnullati.add(storageFile);
										CMISAggiornato=Boolean.TRUE;
									} catch (Exception e) {
										numFile++;
										storageFile.setFileName(cmisFileName.replace("."+cmisFileEstensione, "-ANNULLATO"+numFile+"."+cmisFileEstensione));
									}
								} while (!CMISAggiornato && numFile<=100);
							}
						}
					} else {
						contrattiService.updateProperties(storageFile, storageFile.getStorageObject());
					}
					if (allegato!=null) {
						if (procedura.isProceduraAnnullata() || procedura.isProceduraProvvisoria() || 
								!(allegato.isBando() || allegato.isCurriculumVincitore() || allegato.isAggiornamentoCurriculumVincitore() || allegato.isConflittoInteressi()))
							contrattiService.setInheritedPermission(storageFile.getStorageObject(), false);
						else if (allegato.isBando())
							contrattiService.setInheritedPermission(storageFile.getStorageObject(), true);
						else if (procedura.isProceduraDefinitiva() && (allegato.isCurriculumVincitore() || allegato.isAggiornamentoCurriculumVincitore() || allegato.isConflittoInteressi()))
							contrattiService.setInheritedPermission(storageFile.getStorageObject(), true);
						else
							contrattiService.setInheritedPermission(storageFile.getStorageObject(), false);
					}
				}
				if (allegato!=null && allegato.getFile()!=null)
					allegato.getFile().delete();
			}
		} catch (Exception e){
			//Codice per riallineare il documentale allo stato precedente rispetto alle modifiche
			for (StorageFile storageFile : storageFileCreate)
				contrattiService.delete(storageFile.getStorageObject());
			for (StorageFile storageFile : storageFileAnnullati) {
				String cmisFileName = storageFile.getFileName();
				String cmisFileEstensione = cmisFileName.substring(cmisFileName.lastIndexOf(".")+1);
				String stringToDelete = cmisFileName.substring(cmisFileName.indexOf("-ANNULLATO"));
				storageFile.setFileName(cmisFileName.replace(stringToDelete, "."+cmisFileEstensione));
				contrattiService.updateProperties(storageFile, storageFile.getStorageObject());

				List<String> aspects = storageFile.getStorageObject().<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
                aspects.remove(StorageContrattiAspect.SIGLA_CONTRATTI_STATO_ANNULLATO.value());
                contrattiService.updateProperties(Collections.singletonMap(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), aspects), storageFile.getStorageObject());
			}
			throw handleException(e);
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
				removeConsumerToEveryone(aUC, procedura);
	
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
				addConsumerToEveryone(usercontext, procedura);
			}
			updateBulk(usercontext, oggettobulk);
			if (oggettobulk instanceof Incarichi_proceduraBulk) {
				//lanciato per aggiornare le proprietà sul documentale
				archiviaAllegati(usercontext, (Incarichi_proceduraBulk)oggettobulk);
			}
			return oggettobulk;
		}
		catch(Throwable throwable)
        {
            throw handleException(throwable);
        }
	}
	public OggettoBulk annullaPubblicazioneSulSito(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			if (oggettobulk instanceof Incarichi_proceduraBulk) {
				Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)oggettobulk;
				procedura.setStato(Incarichi_proceduraBulk.STATO_ANNULLATO);
				procedura.setDt_cancellazione( DateServices.getDt_valida(usercontext));
				updateBulk(usercontext, procedura);
				archiviaAllegati(usercontext, procedura);
				removeConsumerToEveryone(usercontext, procedura);
			}
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
		if (oggettobulk instanceof Incarichi_proceduraBulk) {
			archiviaAllegati(usercontext, (Incarichi_proceduraBulk) oggettobulk);
			comunicaPerla(usercontext, (Incarichi_proceduraBulk) oggettobulk);
		}

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
						if (!(parametriDefinitivi.getAllega_curriculum_vitae()!=null && parametriDefinitivi.getAllega_curriculum_vitae().equals("Y")) &&
						    parametri.getAllega_curriculum_vitae()!=null)
							parametriDefinitivi.setAllega_curriculum_vitae(parametri.getAllega_curriculum_vitae());
						if (!(parametriDefinitivi.getAllega_dich_contraente()!=null && parametriDefinitivi.getAllega_dich_contraente().equals("Y")) &&
							parametri.getAllega_dich_contraente()!=null)
							parametriDefinitivi.setAllega_dich_contraente(parametri.getAllega_dich_contraente());
						if (!(parametriDefinitivi.getAllega_progetto()!=null && parametriDefinitivi.getAllega_progetto().equals("Y")) &&
							parametri.getAllega_progetto()!=null)
							parametriDefinitivi.setAllega_progetto(parametri.getAllega_progetto());
						if (!(parametriDefinitivi.getIndica_url_progetto()!=null && parametriDefinitivi.getIndica_url_progetto().equals("Y")) &&
							parametri.getIndica_url_progetto()!=null)
							parametriDefinitivi.setIndica_url_progetto(parametri.getIndica_url_progetto());
						if (!(parametriDefinitivi.getFl_invio_fp()!=null && parametriDefinitivi.getFl_invio_fp().equals("Y")) &&
							parametri.getFl_invio_fp()!=null)
							parametriDefinitivi.setFl_invio_fp(parametri.getFl_invio_fp());
						if (!(parametriDefinitivi.getAllega_conflitto_interesse()!=null && parametriDefinitivi.getAllega_conflitto_interesse().equals("Y")) &&
							parametri.getAllega_conflitto_interesse()!=null)
							parametriDefinitivi.setAllega_conflitto_interesse(parametri.getAllega_conflitto_interesse());
						if (!(parametriDefinitivi.getAllega_attestazione_direttore()!=null && parametriDefinitivi.getAllega_attestazione_direttore().equals("Y")) &&
								parametri.getAllega_attestazione_direttore()!=null)
							parametriDefinitivi.setAllega_attestazione_direttore(parametri.getAllega_attestazione_direttore());
					}
				}
				return parametriDefinitivi;
			}
			return null;	
		} catch (PersistencyException e) {
	        throw handleException(e);
		}
	}

	public SQLBuilder selectTipo_attivita_fp0ByClause (UserContext userContext, OggettoBulk bulk, Tipo_attivita_fpBulk tipo_attivita_fp, CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		    clause = tipo_attivita_fp.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, tipo_attivita_fp).createSQLBuilder();
		sql.addSQLClause(FindClause.AND, "LIVELLO", SQLBuilder.EQUALS, new Integer(0));
		sql.addSQLClause(FindClause.AND, "FL_CANCELLATO", SQLBuilder.EQUALS, "N");
		if (bulk instanceof Incarichi_proceduraBulk && ((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp1()!=null &&
			((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp1().getCd_tipo_attivita_padre()!=null) {
			sql.addSQLClause(FindClause.AND, "CD_TIPO_ATTIVITA", SQLBuilder.EQUALS, ((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp1().getCd_tipo_attivita_padre());
		}else if (bulk instanceof Incarichi_proceduraBulk && ((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp()!=null &&
			((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp().getCd_tipo_attivita()!=null) {
			sql.addSQLClause(FindClause.AND, "LIVELLO", SQLBuilder.EQUALS, new Integer(-1));
		}
		if (clause != null) 
		  sql.addClause(clause);
		sql.addOrderBy("CD_TIPO_ATTIVITA");
		return sql;
	}

	public SQLBuilder selectTipo_attivita_fp1ByClause (UserContext userContext, OggettoBulk bulk, Tipo_attivita_fpBulk tipo_attivita_fp, CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		    clause = tipo_attivita_fp.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, tipo_attivita_fp).createSQLBuilder();
		if (bulk instanceof Incarichi_proceduraBulk && ((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp0()!=null &&
			((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp0().getCd_tipo_attivita()!=null) {
			sql.addSQLClause(FindClause.AND, "LIVELLO", SQLBuilder.EQUALS, new Integer(1));
			sql.addSQLClause(FindClause.AND, "CD_TIPO_ATTIVITA_PADRE", SQLBuilder.EQUALS, ((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp0().getCd_tipo_attivita());
			sql.addSQLClause(FindClause.AND, "FL_CANCELLATO", SQLBuilder.EQUALS, "N");
			if (bulk instanceof Incarichi_proceduraBulk && ((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp()!=null &&
				((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp().getCd_tipo_attivita_padre()!=null) {
				sql.addSQLClause(FindClause.AND, "CD_TIPO_ATTIVITA", SQLBuilder.EQUALS, ((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp().getCd_tipo_attivita_padre());
			}		
		}else if (bulk instanceof Incarichi_proceduraBulk && ((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp()!=null &&
			((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp().getCd_tipo_attivita_padre()!=null) {
			sql.addSQLClause(FindClause.AND, "CD_TIPO_ATTIVITA", SQLBuilder.EQUALS, ((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp().getCd_tipo_attivita_padre());
		} else {
			sql.addSQLClause(FindClause.AND, "LIVELLO", SQLBuilder.EQUALS, new Integer(-1));
		}
		if (clause != null) 
		  sql.addClause(clause);
		sql.addOrderBy("CD_TIPO_ATTIVITA");
		return sql;
	}

	public SQLBuilder selectTipo_attivita_fpByClause (UserContext userContext, OggettoBulk bulk, Tipo_attivita_fpBulk tipo_attivita_fp, CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		    clause = tipo_attivita_fp.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, tipo_attivita_fp).createSQLBuilder();
		if (bulk instanceof Incarichi_proceduraBulk && ((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp1()!=null &&
			((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp1().getCd_tipo_attivita()!=null) {
			sql.addSQLClause(FindClause.AND, "LIVELLO", SQLBuilder.EQUALS, new Integer(2));
			sql.addSQLClause(FindClause.AND, "CD_TIPO_ATTIVITA_PADRE", SQLBuilder.EQUALS, ((Incarichi_proceduraBulk)bulk).getTipo_attivita_fp1().getCd_tipo_attivita());
			sql.addSQLClause(FindClause.AND, "FL_CANCELLATO", SQLBuilder.EQUALS, "N");

		} else {
			sql.addSQLClause(FindClause.AND, "LIVELLO", SQLBuilder.EQUALS, new Integer(-1));
		}
		if (clause != null) 
		  sql.addClause(clause);
		sql.addOrderBy("CD_TIPO_ATTIVITA");
		return sql;
	}
	public java.util.Collection findTipologie_norma_perla(UserContext userContext, Incarichi_proceduraBulk procedura) throws ComponentException{
		try{
			Tipo_norma_perlaHome tnpHome = (Tipo_norma_perlaHome)getHome(userContext, Tipo_norma_perlaBulk.class);
			SQLBuilder sql = tnpHome.createSQLBuilder();

			sql.addClause(FindClause.AND, "tipo_associazione", SQLBuilder.EQUALS, Tipo_norma_perlaBulk.ASS_INCARICHI);
			sql.addOrderBy("cd_tipo_norma");
			return tnpHome.fetchAll(sql);
		}catch (it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(procedura, ex);
		}
	}
	public void addConsumerToEveryone(UserContext userContext, Incarichi_proceduraBulk incarico_procedura) throws ComponentException{
		ContrattiService contrattiService = SpringUtil.getBean(ContrattiService.class);
        Optional.ofNullable(contrattiService.getStorageObjectByPath(incarico_procedura.getCMISFolder().getCMISPath()))
                .ifPresent(storageObject -> contrattiService.addConsumerToEveryone(storageObject));
	}

	public void removeConsumerToEveryone(UserContext userContext, Incarichi_proceduraBulk incarico_procedura) throws ComponentException{
        ContrattiService contrattiService = SpringUtil.getBean(ContrattiService.class);
        Optional.ofNullable(contrattiService.getStorageObjectByPath(incarico_procedura.getCMISFolder().getCMISPath()))
                .ifPresent(storageObject -> contrattiService.removeConsumerToEveryone(storageObject));
	}

	public void salvaDefinitivoCMIS(UserContext userContext, Incarichi_proceduraBulk incarico_procedura) throws ComponentException{
		List<StorageObject> nodeAddAspect = new ArrayList<StorageObject>();
		ContrattiService contrattiService = SpringUtil.getBean(ContrattiService.class);
		try{
		    Optional.ofNullable(contrattiService.getStorageObjectByPath(incarico_procedura.
					getCMISFolder().getCMISPath()))
                    .filter(storageObject ->
                            storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()).contains(
                                    StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value()
                            )
                    ).ifPresent(storageObject -> {
                List<String> aspects = storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
                aspects.add(StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
                contrattiService.updateProperties(Collections.singletonMap(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), aspects), storageObject);
            });
			BulkList listArchiviFile = new BulkList();
			listArchiviFile.addAll(incarico_procedura.getArchivioAllegati());
			
			for (Iterator i = listArchiviFile.iterator(); i.hasNext();) {
				Incarichi_archivioBulk allegato = (Incarichi_archivioBulk)i.next();
				if (allegato.getCms_node_ref()!=null) {
					Optional<StorageObject> optStorage = Optional.ofNullable(contrattiService.getStorageObjectBykey(allegato.getCms_node_ref()));
					if (optStorage.isPresent()) {
						contrattiService.addConsumer(optStorage.get(), SIGLAGroups.GROUP_CONTRATTI.name());
						contrattiService.addConsumer(optStorage.get(), SIGLAGroups.GROUP_INCARICHI.name());
					}
					optStorage.filter(storageObject ->
                                    !storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()).contains(
                                            StorageContrattiAspect.SIGLA_CONTRATTI_STATO_ANNULLATO.value()
                                    ) && !storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()).contains(
                                            StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value()
                                    )
                            	).ifPresent(storageObject -> {
                        contrattiService.addAspect(storageObject, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
                    });
					if (allegato!=null && optStorage.isPresent()) {
						if (incarico_procedura.isProceduraAnnullata() || incarico_procedura.isProceduraProvvisoria() || 
								!(allegato.isBando() || allegato.isCurriculumVincitore() || allegato.isAggiornamentoCurriculumVincitore() || allegato.isConflittoInteressi()))
							contrattiService.setInheritedPermission(optStorage.get(), false);
						else if (allegato.isBando())
							contrattiService.setInheritedPermission(optStorage.get(), true);
						else if (incarico_procedura.isProceduraDefinitiva() && (allegato.isCurriculumVincitore() || allegato.isAggiornamentoCurriculumVincitore() || allegato.isConflittoInteressi()))
							contrattiService.setInheritedPermission(optStorage.get(), true);
						else
							contrattiService.setInheritedPermission(optStorage.get(), false);
					}				
				}
			}
		} catch( StorageException e ) {
			//Codice per riallineare il documentale allo stato precedente rispetto alle modifiche
			for (StorageObject storageObject : nodeAddAspect) {
			    contrattiService.removeAspect(storageObject, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
            }
			throw new ApplicationException(e.getMessage());
		}		
	}
	public void annullaDefinitivoCMIS(UserContext userContext, Incarichi_proceduraBulk incarico_procedura) throws ComponentException{
		List<StorageObject> nodeRemoveAspect = new ArrayList<StorageObject>();
		List<StorageObject> nodeRemoveConsumer = new ArrayList<StorageObject>();
		ContrattiService contrattiService = SpringUtil.getBean(ContrattiService.class);
		try{
			incarico_procedura = (Incarichi_proceduraBulk)inizializzaBulkPerModifica(userContext, incarico_procedura);
			StorageObject nodeProcedura = contrattiService.getStorageObjectByPath(incarico_procedura.getCMISFolder().getCMISPath());
			if (nodeProcedura!=null && contrattiService.hasAspect(nodeProcedura, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value())){
				contrattiService.removeAspect(nodeProcedura, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
				nodeRemoveAspect.add(nodeProcedura);
				if (incarico_procedura.isProceduraProvvisoria()){
					contrattiService.removeConsumerToEveryone(nodeProcedura);
					nodeRemoveConsumer.add(nodeProcedura);
				}
			}
			BulkList listArchiviFile = new BulkList();
			listArchiviFile.addAll(incarico_procedura.getArchivioAllegati());
			
			for (Iterator i = listArchiviFile.iterator(); i.hasNext();) {
				Incarichi_archivioBulk allegato = (Incarichi_archivioBulk)i.next();
				if (allegato.getCms_node_ref()!=null) {
					StorageObject nodeAllegato = contrattiService.getStorageObjectBykey(allegato.getCms_node_ref());
					if (nodeAllegato!=null && contrattiService.hasAspect(nodeAllegato, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value())){
						contrattiService.removeAspect(nodeAllegato, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
						nodeRemoveAspect.add(nodeAllegato);
					}
					if (allegato!=null && nodeAllegato!=null) {
						if (incarico_procedura.isProceduraAnnullata() || incarico_procedura.isProceduraProvvisoria() || 
								!(allegato.isBando() || allegato.isCurriculumVincitore() || allegato.isAggiornamentoCurriculumVincitore() || allegato.isConflittoInteressi()))
							contrattiService.setInheritedPermission(nodeAllegato, false);
						else if (allegato.isBando())
							contrattiService.setInheritedPermission(nodeAllegato, true);
						else if (incarico_procedura.isProceduraDefinitiva() && (allegato.isCurriculumVincitore() || allegato.isAggiornamentoCurriculumVincitore() || allegato.isConflittoInteressi()))
							contrattiService.setInheritedPermission(nodeAllegato, true);
						else
							contrattiService.setInheritedPermission(nodeAllegato, false);
					}				
				}
			}
		} catch( Exception e ) {
			//Codice per riallineare il documentale allo stato precedente rispetto alle modifiche
			for (StorageObject storageObject : nodeRemoveAspect)
				contrattiService.addAspect(storageObject, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
			for (StorageObject storageObject : nodeRemoveConsumer)
				contrattiService.addConsumerToEveryone(storageObject);
			throw new ApplicationException(e.getMessage());
		}		
	}

	public List getIncarichiForMigrateFromDBToCMIS(UserContext userContext, Integer esercizio, Long procedura) throws ComponentException{
		try{
			Incarichi_proceduraHome procHome = (Incarichi_proceduraHome)getHome(userContext, Incarichi_proceduraBulk.class);
			SQLBuilder sql = procHome.createSQLBuilder();

			sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, esercizio);
			if (procedura!=null) 
				sql.addClause(FindClause.AND, "pg_procedura", SQLBuilder.EQUALS, procedura);
			
			sql.addClause(FindClause.AND, "fl_migrata_to_cmis", SQLBuilder.EQUALS, Boolean.FALSE);
			
			sql.addOrderBy("PG_PROCEDURA DESC");
			return procHome.fetchAll(sql);
		} catch (PersistencyException e) {
			throw new ApplicationException("Errore in fase di ricerca procedure conferimento incarichi");			
		}
	}

	public void migrateAllegatiFromDBToCMIS(UserContext userContext, Incarichi_proceduraBulk procedura) throws ComponentException{
		throw new NotImplementedException();
	}
	
	public List getIncarichiForMergeWithCMIS(UserContext userContext, Integer esercizio, Long procedura) throws ComponentException{
		try{
			Incarichi_proceduraHome procHome = (Incarichi_proceduraHome)getHome(userContext, Incarichi_proceduraBulk.class);
			SQLBuilder sql = procHome.createSQLBuilder();

			sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, esercizio);
			if (procedura!=null) 
				sql.addClause(FindClause.AND, "pg_procedura", SQLBuilder.EQUALS, procedura);
			
			sql.addOrderBy("PG_PROCEDURA DESC");
			return procHome.fetchAll(sql);
		} catch (PersistencyException e) {
			throw new ApplicationException("Errore in fase di ricerca procedure conferimento incarichi");			
		}
	}

	public List<String> mergeAllegatiWithCMIS(UserContext userContext, Incarichi_proceduraBulk procedura) throws ComponentException{
		List<String> listError = new ArrayList<String>();

		IncarichiRepertorioComponentSession incaricoSession = Utility.createIncarichiRepertorioComponentSession();
			
		ContrattiService contrattiService = SpringUtil.getBean("contrattiService", ContrattiService.class);
		procedura = (Incarichi_proceduraBulk)inizializzaBulkPerModifica(userContext, procedura);
		
		boolean existIncaricoDefinitivo = false; 

		for (Iterator<Incarichi_repertorioBulk> iterator = procedura.getIncarichi_repertorioColl().iterator(); iterator.hasNext();) {
			Incarichi_repertorioBulk incarico = iterator.next();

			boolean isIncaricoDefinitivo = incarico.isIncaricoDefinitivo() || incarico.isIncaricoChiuso(); 

			//Controlli su Cartella INCARICHI
			StorageFolderContrattiModel cmisFolderIncarico = incarico.getCMISFolder();
			StorageObject nodeFolderIncarico = contrattiService.getStorageObjectByPath(cmisFolderIncarico.getCMISPath());
			if (!cmisFolderIncarico.isEqualsTo(nodeFolderIncarico, listError)) {
				contrattiService.updateProperties(cmisFolderIncarico, nodeFolderIncarico);
				listError.add("AGGIORNAMENTO EFFETTUATO: Incarico "+incarico.getEsercizio().toString()+"/"+incarico.getPg_repertorio().toString()+" - Disallineamento dati ");
			}
			if (isIncaricoDefinitivo) {
				existIncaricoDefinitivo = true;
                listError.add("AGGIORNAMENTO EFFETTUATO: Incarico "+incarico.getEsercizio().toString()+"/"+incarico.getPg_repertorio().toString()+" - addConsumerToEveryone");
                contrattiService.addConsumerToEveryone(nodeFolderIncarico);
				if (!contrattiService.hasAspect(nodeFolderIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value())) {
					listError.add("AGGIORNAMENTO EFFETTUATO: Incarico "+incarico.getEsercizio().toString()+"/"+incarico.getPg_repertorio().toString()+" - addAspect SIGLA_CONTRATTI_STATO_DEFINITIVO");
					contrattiService.addAspect(nodeFolderIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
				}
			} else {
                listError.add("AGGIORNAMENTO EFFETTUATO: Incarico "+incarico.getEsercizio().toString()+"/"+incarico.getPg_repertorio().toString()+" - removeConsumerToEveryone");
                contrattiService.removeConsumerToEveryone(nodeFolderIncarico);
				if (contrattiService.hasAspect(nodeFolderIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value())) {
					listError.add("AGGIORNAMENTO EFFETTUATO: Incarico "+incarico.getEsercizio().toString()+"/"+incarico.getPg_repertorio().toString()+" - removeAspect SIGLA_CONTRATTI_STATO_DEFINITIVO");
					contrattiService.removeAspect(nodeFolderIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
				}
			}

			if (isIncaricoDefinitivo && incarico.getFl_pubblica_contratto().equals(Boolean.TRUE)) {
				if (!contrattiService.hasAspect(nodeFolderIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_PUBBLICATO.value())) {
					listError.add("AGGIORNAMENTO EFFETTUATO: Incarico "+incarico.getEsercizio().toString()+"/"+incarico.getPg_repertorio().toString()+" - addAspect SIGLA_CONTRATTI_PUBBLICATO");
					contrattiService.addAspect(nodeFolderIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_PUBBLICATO.value());
				}
			} else {
				if (contrattiService.hasAspect(nodeFolderIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_PUBBLICATO.value())) {
					listError.add("AGGIORNAMENTO EFFETTUATO: Incarico "+incarico.getEsercizio().toString()+"/"+incarico.getPg_repertorio().toString()+" - removeAspect SIGLA_CONTRATTI_PUBBLICATO");
					contrattiService.removeAspect(nodeFolderIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_PUBBLICATO.value());
				}
			}

			//Controlli su Allegati Cartella INCARICHI
			for (Iterator<Incarichi_archivioBulk> iterator2 = incarico.getArchivioAllegati().iterator(); iterator2.hasNext();) {
				Incarichi_archivioBulk archivio = iterator2.next();
				if (archivio.getCms_node_ref()==null)
					listError.add("ERRORE GRAVE: Allegato Incarico alla procedura "+procedura.getEsercizio().toString()+"/"+procedura.getPg_procedura().toString()+" - Manca Archiviazione");
				else {
					//Controlli su Allegati INCARICHI
					StorageObject nodeArchivioIncarico = contrattiService.getStorageObjectBykey(archivio.getCms_node_ref());
					StorageFile storageFile = (StorageFile)archivio.getCMISFile(nodeArchivioIncarico);
					boolean makeUpdateProperties = false;
					if (storageFile.getContentType().equals("application/octet-stream")) {
						String newContentType = new MimetypesFileTypeMap().getContentType(storageFile.getFileName());
						if (!newContentType.equals("application/octet-stream")) {
							makeUpdateProperties = true;
							storageFile.setContentType(newContentType);
						}
					}
					if (storageFile instanceof StorageFileIncarichi) {
						if (!((StorageFileIncarichi) storageFile).isEqualsTo(nodeArchivioIncarico, listError) || makeUpdateProperties) {
							contrattiService.updateProperties(storageFile, nodeArchivioIncarico);
							listError.add("AGGIORNAMENTO EFFETTUATO: Allegato Incarico "+((StorageFileIncarichi) storageFile).getEsercizioIncarico().toString()+"/"+((StorageFileIncarichi) storageFile).getPgIncarico().toString()+" - Disallineamento dati ");
						}
					} else if (storageFile instanceof StorageFileAssegniRicerca) {
						if (!((StorageFileAssegniRicerca) storageFile).isEqualsTo(nodeArchivioIncarico, listError) || makeUpdateProperties) {
							contrattiService.updateProperties(storageFile, nodeArchivioIncarico);
							listError.add("AGGIORNAMENTO EFFETTUATO: Allegato Incarico StorageFileAssegniRicerca "+((StorageFileAssegniRicerca) storageFile).getEsercizioIncarico().toString()+"/"+((StorageFileAssegniRicerca) storageFile).getPgIncarico().toString()+" - Disallineamento dati ");
						}
					} else if (storageFile instanceof StorageFileBorseStudio) {
						if (!((StorageFileBorseStudio) storageFile).isEqualsTo(nodeArchivioIncarico, listError) || makeUpdateProperties) {
							contrattiService.updateProperties(storageFile, nodeArchivioIncarico);
							listError.add("AGGIORNAMENTO EFFETTUATO: Allegato Incarico StorageFileBorseStudio "+((StorageFileBorseStudio) storageFile).getEsercizioIncarico().toString()+"/"+((StorageFileBorseStudio) storageFile).getPgIncarico().toString()+" - Disallineamento dati ");
						}
					}

					if (contrattiService.hasAspect(nodeArchivioIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value())){
						listError.add("AGGIORNAMENTO EFFETTUATO: Allegato Incarico "+incarico.getEsercizio().toString()+"/"+incarico.getPg_repertorio().toString()+" - removeAspect SIGLA_CONTRATTI_STATO_DEFINITIVO");
						contrattiService.removeAspect(nodeFolderIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
					}

					if (archivio.isAnnullato()) {
						if (!contrattiService.hasAspect(nodeArchivioIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_ANNULLATO.value())) {
							listError.add("AGGIORNAMENTO EFFETTUATO: Allegato Incarico "+incarico.getEsercizio().toString()+"/"+incarico.getPg_repertorio().toString()+" - addAspect SIGLA_CONTRATTI_STATO_ANNULLATO");
							contrattiService.addAspect(nodeArchivioIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_ANNULLATO.value());
						}
					} else {
						if (contrattiService.hasAspect(nodeArchivioIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_ANNULLATO.value())) {
							listError.add("AGGIORNAMENTO EFFETTUATO: Allegato Incarico "+incarico.getEsercizio().toString()+"/"+incarico.getPg_repertorio().toString()+" - removeAspect SIGLA_CONTRATTI_STATO_ANNULLATO");
							contrattiService.removeAspect(nodeArchivioIncarico, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_ANNULLATO.value());
						}
					}
				}
			}
		}
		
		//Controlli su Cartella PROCEDURA
		StorageFolderProcedura storageFolderProcedura = procedura.getCMISFolder();
		StorageObject nodeFolderProcedura = contrattiService.getStorageObjectByPath(storageFolderProcedura.getCMISPath());
		if (!storageFolderProcedura.isEqualsTo(nodeFolderProcedura, listError)) {
			contrattiService.updateProperties(storageFolderProcedura, nodeFolderProcedura);
			listError.add("AGGIORNAMENTO EFFETTUATO: Procedura "+procedura.getEsercizio().toString()+"/"+procedura.getPg_procedura().toString()+" - Disallineamento dati ");
		}

		boolean isProceduraDefinitiva = procedura.isProceduraDefinitiva() || procedura.isProceduraChiusa(); 
		
		if (isProceduraDefinitiva || existIncaricoDefinitivo) {
            listError.add("AGGIORNAMENTO EFFETTUATO: Procedura "+procedura.getEsercizio().toString()+"/"+procedura.getPg_procedura().toString()+" - addConsumerToEveryone");
            contrattiService.addConsumerToEveryone(nodeFolderProcedura);
			if (procedura.isProceduraDefinitiva() && !contrattiService.hasAspect(nodeFolderProcedura, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value())) {
				listError.add("AGGIORNAMENTO EFFETTUATO: Procedura "+procedura.getEsercizio().toString()+"/"+procedura.getPg_procedura().toString()+" - addAspect SIGLA_CONTRATTI_STATO_DEFINITIVO");
				contrattiService.addAspect(nodeFolderProcedura, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
			}
		} else {
            listError.add("AGGIORNAMENTO EFFETTUATO: Procedura "+procedura.getEsercizio().toString()+"/"+procedura.getPg_procedura().toString()+" - removeConsumerToEveryone");
            contrattiService.removeConsumerToEveryone(nodeFolderProcedura);
			if (contrattiService.hasAspect(nodeFolderProcedura, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value())) {
				listError.add("AGGIORNAMENTO EFFETTUATO: Procedura "+procedura.getEsercizio().toString()+"/"+procedura.getPg_procedura().toString()+" - removeAspect SIGLA_CONTRATTI_STATO_DEFINITIVO");
				contrattiService.removeAspect(nodeFolderProcedura, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
			}
		}
		
		//Controlli su Allegati Cartella PROCEDURA
		for (Iterator<Incarichi_archivioBulk> iterator = procedura.getArchivioAllegati().iterator(); iterator.hasNext();) {
			Incarichi_procedura_archivioBulk archivio = (Incarichi_procedura_archivioBulk)iterator.next();
			if (archivio.getCms_node_ref()==null)
				listError.add("ERRORE GRAVE: Allegato Procedura "+procedura.getEsercizio().toString()+"/"+procedura.getPg_procedura().toString()+"/"+archivio.getProgressivo_riga()+" - Manca Archiviazione");
			else {
				//Controlli su Allegati PROCEDURA
				StorageObject nodeArchivioProcedura = contrattiService.getStorageObjectBykey(archivio.getCms_node_ref());
				StorageFileProcedura cmisFileProcedura = (StorageFileProcedura)archivio.getCMISFile(nodeArchivioProcedura);
				boolean makeUpdateProperties = false;
				if (cmisFileProcedura.getContentType().equals("application/octet-stream")) {
					String newContentType = new MimetypesFileTypeMap().getContentType(cmisFileProcedura.getFileName());
					if (!newContentType.equals("application/octet-stream")) {
						makeUpdateProperties = true;
						cmisFileProcedura.setContentType(newContentType);
					}
				}
				if (!cmisFileProcedura.isEqualsTo(nodeArchivioProcedura, listError) || makeUpdateProperties) {
					contrattiService.updateProperties(cmisFileProcedura, nodeArchivioProcedura);
					listError.add("AGGIORNAMENTO EFFETTUATO: Allegato Procedura "+procedura.getEsercizio().toString()+"/"+procedura.getPg_procedura().toString()+"/"+archivio.getProgressivo_riga()+" - Disallineamento dati ");
				}

				if (archivio.isBando() && !procedura.isProceduraProvvisoria() && procedura.getDt_pubblicazione()!=null) {
                    listError.add("AGGIORNAMENTO EFFETTUATO: Allegato Procedura "+procedura.getEsercizio().toString()+"/"+procedura.getPg_procedura().toString()+" - addConsumerToEveryone");
                    contrattiService.addConsumerToEveryone(nodeArchivioProcedura);
					if (!contrattiService.hasAspect(nodeArchivioProcedura, StorageContrattiAspect.SIGLA_CONTRATTI_PUBBLICATO.value())) {
						listError.add("AGGIORNAMENTO EFFETTUATO: Allegato Procedura "+procedura.getEsercizio().toString()+"/"+procedura.getPg_procedura().toString()+" - addAspect SIGLA_CONTRATTI_PUBBLICATO");
						contrattiService.addAspect(nodeArchivioProcedura, StorageContrattiAspect.SIGLA_CONTRATTI_PUBBLICATO.value());
					}
				} else {
                    listError.add("AGGIORNAMENTO EFFETTUATO: Allegato Procedura "+procedura.getEsercizio().toString()+"/"+procedura.getPg_procedura().toString()+" - removeConsumerToEveryone");
                    contrattiService.removeConsumerToEveryone(nodeArchivioProcedura);
					if (contrattiService.hasAspect(nodeArchivioProcedura, StorageContrattiAspect.SIGLA_CONTRATTI_PUBBLICATO.value())) {
						listError.add("AGGIORNAMENTO EFFETTUATO: Allegato Procedura "+procedura.getEsercizio().toString()+"/"+procedura.getPg_procedura().toString()+" - removeAspect SIGLA_CONTRATTI_PUBBLICATO");
						contrattiService.removeAspect(nodeArchivioProcedura, StorageContrattiAspect.SIGLA_CONTRATTI_PUBBLICATO.value());
					}
				}

				if (contrattiService.hasAspect(nodeArchivioProcedura, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value())) {
					listError.add("AGGIORNAMENTO EFFETTUATO: Allegato Procedura "+procedura.getEsercizio().toString()+"/"+procedura.getPg_procedura().toString()+" - removeAspect SIGLA_CONTRATTI_STATO_DEFINITIVO");
					contrattiService.removeAspect(nodeArchivioProcedura, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_DEFINITIVO.value());
				}
				
				if (archivio.isAnnullato()) {
					if (!contrattiService.hasAspect(nodeArchivioProcedura, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_ANNULLATO.value())) {
						listError.add("AGGIORNAMENTO EFFETTUATO: Allegato Procedura "+procedura.getEsercizio().toString()+"/"+procedura.getPg_procedura().toString()+" - addAspect SIGLA_CONTRATTI_STATO_ANNULLATO");
						contrattiService.addAspect(nodeArchivioProcedura, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_ANNULLATO.value());
					}
				} else {
					if (contrattiService.hasAspect(nodeArchivioProcedura, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_ANNULLATO.value())) {
						listError.add("AGGIORNAMENTO EFFETTUATO: Allegato Procedura "+procedura.getEsercizio().toString()+"/"+procedura.getPg_procedura().toString()+" - removeAspect SIGLA_CONTRATTI_STATO_ANNULLATO");
						contrattiService.removeAspect(nodeArchivioProcedura, StorageContrattiAspect.SIGLA_CONTRATTI_STATO_ANNULLATO.value());
					}
				}
			}
		}
		return listError;
	}
	public it.cnr.jada.persistency.sql.SQLBuilder selectBaseIncaricoRepertorioForSearchByClause(UserContext userContext, Incarichi_proceduraBulk procedura, Incarichi_repertorioBulk incarico, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		if (clauses == null) 
			clauses = incarico.buildFindClauses(null);

		Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext))));
		boolean isUoEnte = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0;
		boolean isUoSac  = uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC)==0;
		
		SQLBuilder sqlCdsExists = getHome(userContext, Ass_incarico_uoBulk.class).createSQLBuilder();
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

		SQLBuilder sql = getHome(userContext, Incarichi_repertorioBulk.class).createSQLBuilder();
		if (!isUoEnte) {
			sql.openParenthesis(FindClause.AND);
				sql.openParenthesis(FindClause.OR);
					sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
					if (isUoSac)
						sql.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
				sql.closeParenthesis();
			sql.closeParenthesis();
		} else if (isUoSac) {
			sql.openParenthesis(FindClause.AND);
				sql.addClause(FindClause.OR, "cd_unita_organizzativa", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
				sql.addSQLExistsClause(FindClause.OR,sqlCdsExists);
			sql.closeParenthesis();
		}
		if (clauses != null) 
			sql.addClause(clauses);
		sql.addOrderBy("ESERCIZIO, PG_REPERTORIO");		
		return sql;
	}
	public it.cnr.jada.persistency.sql.SQLBuilder selectIncaricoRepertorioForSearchByClause(UserContext userContext, Incarichi_proceduraBulk procedura, Incarichi_repertorioBulk incarico, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = selectBaseIncaricoRepertorioForSearchByClause(userContext, procedura, incarico, clauses);

		sql.addTableToHeader("INCARICHI_PROCEDURA");
		sql.addSQLJoin("INCARICHI_REPERTORIO.ESERCIZIO_PROCEDURA", "INCARICHI_PROCEDURA.ESERCIZIO");
		sql.addSQLJoin("INCARICHI_REPERTORIO.PG_PROCEDURA", "INCARICHI_PROCEDURA.PG_PROCEDURA");

		sql.addTableToHeader("TIPO_ATTIVITA");
		sql.addSQLJoin("INCARICHI_PROCEDURA.CD_TIPO_ATTIVITA","TIPO_ATTIVITA.CD_TIPO_ATTIVITA");
		sql.addSQLClause(FindClause.AND, "TIPO_ATTIVITA.TIPO_ASSOCIAZIONE", SQLBuilder.EQUALS, Tipo_attivitaBulk.ASS_INCARICHI);
	
		sql.addTableToHeader("TIPO_INCARICO");
		sql.addSQLJoin("INCARICHI_PROCEDURA.CD_TIPO_INCARICO","TIPO_INCARICO.CD_TIPO_INCARICO");
		sql.addSQLClause(FindClause.AND, "TIPO_INCARICO.TIPO_ASSOCIAZIONE", SQLBuilder.EQUALS, Tipo_incaricoBulk.ASS_INCARICHI);

		return sql;
	}

	private void comunicaPerla(UserContext userContext, Incarichi_proceduraBulk procedura) throws ComponentException {
		try {
			IncarichiRepertorioComponentSession incRepComponent = Utility.createIncarichiRepertorioComponentSession();
			for (Iterator i=procedura.getIncarichi_repertorioColl().iterator();i.hasNext();) {
				Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk) i.next();
				incRepComponent.comunicaPerla(userContext, incarico);
			}
		} catch (Exception e){
			throw handleException(e);
		}
	}
}
