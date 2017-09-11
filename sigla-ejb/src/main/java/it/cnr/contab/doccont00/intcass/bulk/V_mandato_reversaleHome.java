package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.consultazioni.bulk.ConsultazioniRestHome;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.contab.missioni00.docs.bulk.RimborsoBulk;
import it.cnr.contab.missioni00.docs.bulk.RimborsoHome;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.*;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

public class V_mandato_reversaleHome extends BulkHome implements ConsultazioniRestHome {
	private DocumentiContabiliService documentiContabiliService;

	public V_mandato_reversaleHome(java.sql.Connection conn) {
		super(V_mandato_reversaleBulk.class,conn);
		documentiContabiliService = SpringUtil.getBean("documentiContabiliService",
				DocumentiContabiliService.class);					
	}
	public V_mandato_reversaleHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(V_mandato_reversaleBulk.class,conn,persistentCache);
		documentiContabiliService = SpringUtil.getBean("documentiContabiliService",
				DocumentiContabiliService.class);					
	}
	public V_mandato_reversaleBulk findDocContabile( Distinta_cassiere_detBulk dettaglio ) throws PersistencyException, ApplicationException
	{
		SQLBuilder sql = createSQLBuilder();
		if ( dettaglio.getPg_mandato() != null )
		{
			sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);
			sql.addClause( "AND", "pg_documento_cont", sql.EQUALS, dettaglio.getPg_mandato());
		}	
		else if ( dettaglio.getPg_reversale() != null )
		{
			sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_REV);
			sql.addClause( "AND", "pg_documento_cont", sql.EQUALS, dettaglio.getPg_reversale());
		}	
		else
			return null;	
		sql.addClause( "AND", "esercizio", sql.EQUALS, dettaglio.getEsercizio());	
		sql.addClause( "AND", "cd_cds", sql.EQUALS, dettaglio.getCd_cds());
		sql.addClause( "AND", "cd_unita_organizzativa", sql.EQUALS, dettaglio.getCd_unita_organizzativa());
		List result = fetchAll( sql );
		if ( result.size() == 0 )
			throw new ApplicationException("Attenzione! Mandato/Reversale non trovato per il dettaglio distinta");
		if ( result.size() > 1 )
			throw new ApplicationException("Attenzione! Esiste pi� di un Mandato/Reversale  per il dettaglio distinta");
		return (V_mandato_reversaleBulk) result.get(0);	
				
	}
	public V_mandato_reversaleBulk findDocContabilePadre( V_mandato_reversaleBulk docContabile ) throws PersistencyException
	{
	
		SQLBuilder sql = createSQLBuilder();
		sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, docContabile.getCd_tipo_documento_cont_padre());
		sql.addClause( "AND", "pg_documento_cont", sql.EQUALS, docContabile.getPg_documento_cont_padre());
		sql.addClause( "AND", "esercizio", sql.EQUALS, docContabile.getEsercizio());	
		sql.addClause( "AND", "cd_cds", sql.EQUALS, docContabile.getCd_cds());
		sql.addClause( "AND", "cd_unita_organizzativa", sql.EQUALS, docContabile.getCd_unita_organizzativa());
		List result =  fetchAll( sql );
		if ( result != null && result.size() > 0 )
			return (V_mandato_reversaleBulk) result.get(0);
		return null;	
	}
public Collection findDocContabiliAnnullatiDaRitrasmettere( Distinta_cassiereBulk distinta, boolean annulliTuttaSac ,boolean tesoreria) throws PersistencyException
	{
		SQLBuilder sql = createSQLBuilder();
	sql.addClause( "AND", "esercizio", sql.EQUALS, distinta.getEsercizio());
	if(!tesoreria){
		sql.addClause( "AND", "cd_cds", sql.EQUALS, distinta.getCd_cds());
		if (!annulliTuttaSac){	
	//solo per la 000.407, devono essere ritrasmessi i doc. di tutta la SAC 	
			sql.addClause( "AND", "cd_unita_organizzativa", sql.EQUALS, distinta.getCd_unita_organizzativa());
		}
	}
		sql.addSQLClause( "AND", "dt_annullamento > dt_trasmissione" );
		sql.addClause( "AND", "dt_ritrasmissione", sql.ISNULL, null );
		if(tesoreria)
			sql.addClause( "AND", "stato_trasmissione",  sql.EQUALS, MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA );	
		return fetchAll( sql );
				
	}
	
	/* ritorna la collezione dei mandati figli */
	public List<V_mandato_reversaleBulk> findMandatiCollegati( V_mandato_reversaleBulk docContabile ) throws PersistencyException
	{	
		SQLBuilder sql = createSQLBuilder();
		sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);
		sql.addClause( "AND", "pg_documento_cont_padre", sql.EQUALS, docContabile.getPg_documento_cont());
		sql.addClause( "AND", "cd_tipo_documento_cont_padre", sql.EQUALS, docContabile.getCd_tipo_documento_cont());
		sql.openParenthesis( "AND");
		sql.addClause( "AND", "pg_documento_cont", sql.NOT_EQUALS, docContabile.getPg_documento_cont());
		sql.addClause( "OR", "cd_tipo_documento_cont", sql.NOT_EQUALS, docContabile.getCd_tipo_documento_cont());
		sql.closeParenthesis();
		sql.addClause( "AND", "esercizio", sql.EQUALS, docContabile.getEsercizio());	
		sql.addClause( "AND", "cd_cds", sql.EQUALS, docContabile.getCd_cds());
		sql.addClause( "AND", "cd_unita_organizzativa", sql.EQUALS, docContabile.getCd_unita_organizzativa());
		return fetchAll( sql );
	}

	/* ritorna la collezione dei mandati figli */
	public List<V_mandato_reversaleBulk> findReversaliCollegate( V_mandato_reversaleBulk docContabile ) throws PersistencyException
	{	
		SQLBuilder sql = createSQLBuilder();
		sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_REV);
		sql.addClause( "AND", "pg_documento_cont_padre", sql.EQUALS, docContabile.getPg_documento_cont());
		sql.addClause( "AND", "cd_tipo_documento_cont_padre", sql.EQUALS, docContabile.getCd_tipo_documento_cont());
		sql.openParenthesis( "AND");
		sql.addClause( "AND", "pg_documento_cont", sql.NOT_EQUALS, docContabile.getPg_documento_cont());
		sql.addClause( "OR", "cd_tipo_documento_cont", sql.NOT_EQUALS, docContabile.getCd_tipo_documento_cont());
		sql.closeParenthesis();
		sql.addClause( "AND", "esercizio", sql.EQUALS, docContabile.getEsercizio());	
		sql.addClause( "AND", "cd_cds", sql.EQUALS, docContabile.getCd_cds());
		sql.addClause( "AND", "cd_unita_organizzativa", sql.EQUALS, docContabile.getCd_unita_organizzativa());
		return fetchAll( sql );
	}
	
	/* ritorna la collezione dei documenti contabili figli */
	public Collection findDocContabiliCollegati( V_mandato_reversaleBulk docContabile ) throws PersistencyException
	{
	
		SQLBuilder sql = createSQLBuilder();
		/*???????
		if ( docContabile.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_MAN ) )
			sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_REV);
		else if ( docContabile.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_REV ) )
			sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);
		*/	
		sql.addClause( "AND", "pg_documento_cont_padre", sql.EQUALS, docContabile.getPg_documento_cont());
		sql.addClause( "AND", "cd_tipo_documento_cont_padre", sql.EQUALS, docContabile.getCd_tipo_documento_cont());
		sql.openParenthesis( "AND");
		sql.addClause( "AND", "pg_documento_cont", sql.NOT_EQUALS, docContabile.getPg_documento_cont());
		sql.addClause( "OR", "cd_tipo_documento_cont", sql.NOT_EQUALS, docContabile.getCd_tipo_documento_cont());
		sql.closeParenthesis();
		sql.addClause( "AND", "esercizio", sql.EQUALS, docContabile.getEsercizio());	
		sql.addClause( "AND", "cd_cds", sql.EQUALS, docContabile.getCd_cds());
		sql.addClause( "AND", "cd_unita_organizzativa", sql.EQUALS, docContabile.getCd_unita_organizzativa());
		return fetchAll( sql );
	}
	/* dato un documento contabile figlio restituisce la lista dei fratelli */
	public Collection findDocContabiliCollegatiEccetto( V_mandato_reversaleBulk docContabileDaEscludere ) throws PersistencyException
	{
	
		SQLBuilder sql = createSQLBuilder();
		/*
		if ( docContabileDaEscludere.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_REV ) )
			sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_REV);
		else if ( docContabileDaEscludere.getCd_tipo_documento_cont().equals( Numerazione_doc_contBulk.TIPO_MAN ) )
			sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);
		*/	
		sql.addClause( "AND", "pg_documento_cont_padre", sql.EQUALS, docContabileDaEscludere.getPg_documento_cont_padre());
		sql.addClause( "AND", "cd_tipo_documento_cont_padre", sql.EQUALS, docContabileDaEscludere.getCd_tipo_documento_cont_padre());
		sql.openParenthesis( "AND");
		sql.addClause( "AND", "pg_documento_cont", sql.NOT_EQUALS, docContabileDaEscludere.getPg_documento_cont());
		sql.addClause( "OR", "cd_tipo_documento_cont", sql.NOT_EQUALS, docContabileDaEscludere.getCd_tipo_documento_cont());
		sql.closeParenthesis();
		sql.addClause( "AND", "pg_documento_cont", sql.NOT_EQUALS, docContabileDaEscludere.getPg_documento_cont());	
		sql.addClause( "AND", "esercizio", sql.EQUALS, docContabileDaEscludere.getEsercizio());	
		sql.addClause( "AND", "cd_cds", sql.EQUALS, docContabileDaEscludere.getCd_cds());
		sql.addClause( "AND", "cd_unita_organizzativa", sql.EQUALS, docContabileDaEscludere.getCd_unita_organizzativa());
		return fetchAll( sql );
	}
	public List findReversaliAssociabili(it.cnr.jada.UserContext userContext,MandatoBulk mandato) throws PersistencyException, IntrospectionException{
	
		SQLBuilder sql = createSQLBuilder();
		sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_REV);
	
		sql.addClause( "AND", "cd_cds", sql.EQUALS, mandato.getCd_cds());
		sql.addClause( "AND", "esercizio", sql.EQUALS, mandato.getEsercizio());	
		sql.addClause( "AND", "cd_unita_organizzativa", sql.EQUALS, mandato.getCd_unita_organizzativa());
	
		sql.addClause( "AND", "cd_cds_origine", sql.EQUALS, mandato.getCd_cds_origine());
		sql.addClause( "AND", "cd_uo_origine", sql.EQUALS, mandato.getCd_uo_origine());
		
		sql.addClause( "AND", "stato", sql.EQUALS, ReversaleIBulk.STATO_REVERSALE_EMESSO);
		sql.addClause( "AND", "stato_trasmissione", sql.EQUALS, ReversaleIBulk.STATO_TRASMISSIONE_NON_INSERITO);
	
		MandatoHome home = (MandatoHome)getHomeCache().getHome(mandato.getClass());
		Mandato_terzoBulk terzo = home.findMandato_terzo(userContext,mandato);
		sql.addClause( "AND", "cd_terzo", sql.EQUALS, terzo.getCd_terzo());
	
		return fetchAll(sql);
	}
	/**
	 * Metodo per cercare i riscontri associati alla reversale selezionata.
	 *
	 * @return result i riscontri associati alla reversale
	 *
	 */
	public Collection findRiscontriDiEntrata( V_mandato_reversaleBulk v_man_rev ) throws PersistencyException, IntrospectionException
	{
		PersistentHome home = getHomeCache().getHome( SospesoBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addTableToHeader("SOSPESO_DET_ETR");	
		sql.addSQLClause( "AND", "SOSPESO_DET_ETR.esercizio", sql.EQUALS, v_man_rev.getEsercizio());	
		sql.addSQLClause( "AND", "SOSPESO_DET_ETR.cd_cds", sql.EQUALS, v_man_rev.getCd_cds());
		sql.addSQLClause( "AND", "SOSPESO_DET_ETR.pg_reversale", sql.EQUALS, v_man_rev.getPg_documento_cont());
		sql.addSQLClause( "AND", "SOSPESO_DET_ETR.ti_sospeso_riscontro", sql.EQUALS, SospesoBulk.TI_RISCONTRO);
		sql.addSQLClause( "AND", "SOSPESO_DET_ETR.ti_entrata_spesa", sql.EQUALS, SospesoBulk.TIPO_ENTRATA);
		sql.addSQLClause( "AND", "SOSPESO_DET_ETR.STATO", sql.EQUALS, Sospeso_det_etrBulk.STATO_DEFAULT);	
	
		sql.addSQLJoin( "SOSPESO_DET_ETR.ESERCIZIO","SOSPESO.ESERCIZIO");
		sql.addSQLJoin( "SOSPESO_DET_ETR.CD_CDS", "SOSPESO.CD_CDS");
		sql.addSQLJoin( "SOSPESO_DET_ETR.CD_SOSPESO", "SOSPESO.CD_SOSPESO");	
		sql.addSQLJoin( "SOSPESO_DET_ETR.TI_ENTRATA_SPESA", "SOSPESO.TI_ENTRATA_SPESA");
		sql.addSQLJoin( "SOSPESO_DET_ETR.TI_SOSPESO_RISCONTRO", "SOSPESO.TI_SOSPESO_RISCONTRO");
		
		return home.fetchAll( sql);
	}
	/**
	 * Metodo per cercare i riscontri associati al mandato selezionato.
	 *
	 * @return result i riscontri associati al mandato
	 *
	 */
	public Collection findRiscontriDiSpesa( V_mandato_reversaleBulk v_man_rev ) throws PersistencyException, IntrospectionException
	{
		PersistentHome home = getHomeCache().getHome( SospesoBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addTableToHeader("SOSPESO_DET_USC");	
		sql.addSQLClause( "AND", "SOSPESO_DET_USC.esercizio", sql.EQUALS, v_man_rev.getEsercizio());	
		sql.addSQLClause( "AND", "SOSPESO_DET_USC.cd_cds", sql.EQUALS, v_man_rev.getCd_cds());
		sql.addSQLClause( "AND", "SOSPESO_DET_USC.pg_mandato", sql.EQUALS, v_man_rev.getPg_documento_cont());
		sql.addSQLClause( "AND", "SOSPESO_DET_USC.ti_sospeso_riscontro", sql.EQUALS, SospesoBulk.TI_RISCONTRO);
		sql.addSQLClause( "AND", "SOSPESO_DET_USC.ti_entrata_spesa", sql.EQUALS, SospesoBulk.TIPO_SPESA);
		sql.addSQLClause( "AND", "SOSPESO_DET_USC.STATO", sql.EQUALS, Sospeso_det_uscBulk.STATO_DEFAULT);	
	
		sql.addSQLJoin( "SOSPESO_DET_USC.ESERCIZIO","SOSPESO.ESERCIZIO");
		sql.addSQLJoin( "SOSPESO_DET_USC.CD_CDS", "SOSPESO.CD_CDS");
		sql.addSQLJoin( "SOSPESO_DET_USC.CD_SOSPESO", "SOSPESO.CD_SOSPESO");	
		sql.addSQLJoin( "SOSPESO_DET_USC.TI_ENTRATA_SPESA", "SOSPESO.TI_ENTRATA_SPESA");
		sql.addSQLJoin( "SOSPESO_DET_USC.TI_SOSPESO_RISCONTRO", "SOSPESO.TI_SOSPESO_RISCONTRO");
		
		return home.fetchAll( sql);
	}	
	
	@Override
	public Persistent completeBulkRowByRow(UserContext userContext, Persistent persistent) throws PersistencyException {
		persistent =  super.completeBulkRowByRow(userContext, persistent);
		if (persistent instanceof V_mandato_reversaleBulk){
			V_mandato_reversaleBulk bulk = (V_mandato_reversaleBulk)persistent;
			if (!bulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO)){
				if (((CNRUserContext)userContext).isFromBootstrap()) {
					bulk.setDocumento("<a class='btn btn-link' onclick='"+
							"doVisualizzaSingoloDocumento("+bulk.getEsercizio()+",\""+bulk.getCd_cds()+"\",\""+bulk.getCd_unita_organizzativa()+"\","+bulk.getPg_documento_cont()+",\""+bulk.getCd_tipo_documento_cont()+"\");' "+
							"title='Visualizza Documento Contabile'><i class='fa fa-fw fa-2x fa-file-pdf-o text-danger' aria-hidden='true'></i></a>");															
				} else {				
					bulk.setDocumento("<button class='Button' style='width:60px;' onclick='cancelBubble(event); if (disableDblClick()) "+
							"doVisualizzaSingoloDocumento("+bulk.getEsercizio()+",\""+bulk.getCd_cds()+"\",\""+bulk.getCd_unita_organizzativa()+"\","+bulk.getPg_documento_cont()+",\""+bulk.getCd_tipo_documento_cont()+"\"); return false' "+
						"onMouseOver='mouseOver(this)' onMouseOut='mouseOut(this)' onMouseDown='mouseDown(this)' onMouseUp='mouseUp(this)' "+
						"title='Visualizza Documento Contabile'><img align='middle' class='Button' src='img/application-pdf.png'></button>");			
				}
			}		
			if (bulk.isReversaleTrasferimento()){
				SQLBuilder sql  = getHomeCache().getHome(Distinta_cassiere_detBulk.class).createSQLBuilder();
				sql.addClause( "AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );
				sql.addClause( "AND", "cd_cds", SQLBuilder.EQUALS, bulk.getCd_cds() );
				sql.addClause( "AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, bulk.getCd_unita_organizzativa() );
				sql.addClause( "AND", "pg_reversale", SQLBuilder.EQUALS, bulk.getPg_documento_cont() );
				sql.addOrderBy("PG_DISTINTA DESC");
	
				java.util.List list = getHomeCache().getHome(Distinta_cassiere_detBulk.class).fetchAll(sql);
				if (!list.isEmpty())
					bulk.setPg_distinta(((Distinta_cassiere_detBulk)list.get(0)).getPg_distinta());
				return (Persistent)bulk;
			}
		}
		return persistent;
	}

	@Override
	public SQLBuilder restSelect(UserContext userContext, SQLBuilder sql, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
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
							operator == SQLBuilder.EQUALS){
						trovataCondizioneSoloAnticipi = true;
						Mandato_rigaHome home = (Mandato_rigaHome) getHomeCache().getHome(Mandato_rigaBulk.class);
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


						RimborsoHome homeRimborso = (RimborsoHome) getHomeCache().getHome(RimborsoBulk.class);
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
				sql = selectByClause(userContext, newClauses);
				sql.addSQLExistsClause("AND", sqlExists);
				sql.addSQLNotExistsClause("AND", sqlNotExists);
			}
		}
		if ( !isUoEnte(userContext)){
			sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
			sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
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
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHomeCache().getHome(Unita_organizzativa_enteBulk.class ).findAll().get(0);
		return uoEnte;
	}
}