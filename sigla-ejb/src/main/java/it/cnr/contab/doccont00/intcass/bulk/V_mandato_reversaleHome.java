package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoHome;
import it.cnr.contab.doccont00.core.bulk.Mandato_terzoBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleIBulk;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.doccont00.core.bulk.Sospeso_det_etrBulk;
import it.cnr.contab.doccont00.core.bulk.Sospeso_det_uscBulk;
import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.Collection;
import java.util.List;

public class V_mandato_reversaleHome extends BulkHome {
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
			throw new ApplicationException("Attenzione! Esiste più di un Mandato/Reversale  per il dettaglio distinta");
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
	public Collection findDocContabiliAnnullatiDaRitrasmettere( Distinta_cassiereBulk distinta, boolean annulliTuttaSac ) throws PersistencyException
	{
		SQLBuilder sql = createSQLBuilder();
		sql.addClause( "AND", "esercizio", sql.EQUALS, distinta.getEsercizio());	
		sql.addClause( "AND", "cd_cds", sql.EQUALS, distinta.getCd_cds());
		//solo per la 000.407, devono essere ritrasmessi i doc. di tutta la SAC 
		if (!annulliTuttaSac)
		   sql.addClause( "AND", "cd_unita_organizzativa", sql.EQUALS, distinta.getCd_unita_organizzativa());
		sql.addSQLClause( "AND", "dt_annullamento > dt_trasmissione" );
		sql.addClause( "AND", "dt_ritrasmissione", sql.ISNULL, null );	
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
		sql.addClause( "AND", "stato_trasmissione", sql.NOT_EQUALS, ReversaleIBulk.STATO_TRASMISSIONE_TRASMESSO);
	
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
			List<String> nodeRefs;
			try {
				if (!bulk.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO)){
					nodeRefs = documentiContabiliService.getNodeRefDocumento(bulk);
					if (nodeRefs != null && !nodeRefs.isEmpty())
						bulk.setDocumento("<button class='Button' style='width:60px;' onclick='cancelBubble(event); if (disableDblClick()) "+
							"doVisualizzaSingoloDocumento("+bulk.getEsercizio()+",\""+bulk.getCd_cds()+"\","+bulk.getPg_documento_cont()+",\""+bulk.getCd_tipo_documento_cont()+"\"); return false' "+
							"onMouseOver='mouseOver(this)' onMouseOut='mouseOut(this)' onMouseDown='mouseDown(this)' onMouseUp='mouseUp(this)' "+
							"title='Visualizza Documento Contabile'><img align='middle' class='Button' src='img/application-pdf.png'></button>");			
				}		
			} catch (ApplicationException _ex) {				
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
}