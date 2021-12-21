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

/*
 * Created on Mar 30, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.comp;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.V_anagrafico_terzoBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk;
import it.cnr.contab.doccont00.core.AccertamentoWizard;
import it.cnr.contab.doccont00.core.ObbligazioneWizard;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk;
import it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ReversaleAutomaticaComponent extends ReversaleComponent {
	public OggettoBulk creaReversaleAutomatica(UserContext userContext,OggettoBulk bulk) throws ComponentException
	{
		ReversaleAutomaticaWizardBulk wizard = (ReversaleAutomaticaWizardBulk) bulk;
		if (wizard.isAutomatismoDaAccertamenti())
			return creaReversaleAutomaticaDaAccertamenti(userContext, wizard, wizard.getAccertamentiSelezionatiColl());
		else if (wizard.isAutomatismoDaDocumentiAttivi())
			return creaReversaleAutomaticaDaDocAttivi(userContext, wizard, wizard.getDocAttiviSelezionatiColl());
		return bulk;
	}

	private OggettoBulk creaReversaleAutomaticaDaAccertamenti (UserContext userContext, ReversaleAutomaticaWizardBulk wizard, Collection<AccertamentoWizard> accertamentiColl) throws ComponentException {
		try {
			wizard.getModelloDocumento().setEsercizio(Optional.ofNullable(wizard.getEsercizio()).orElse(wizard.getEsercizio()));
			wizard.getModelloDocumento().setCd_cds(Optional.ofNullable(wizard.getCd_cds()).orElse(wizard.getCd_cds()));
			wizard.getModelloDocumento().setCd_unita_organizzativa(Optional.ofNullable(wizard.getCd_unita_organizzativa()).orElse(wizard.getCd_unita_organizzativa()));
			wizard.getModelloDocumento().setCd_cds_origine(Optional.ofNullable(wizard.getCd_cds_origine()).orElse(wizard.getCd_cds_origine()));
			wizard.getModelloDocumento().setCd_uo_origine(Optional.ofNullable(wizard.getCd_uo_origine()).orElse(wizard.getCd_uo_origine()));
			wizard.getModelloDocumento().setData_registrazione(Optional.ofNullable(wizard.getModelloDocumento().getData_registrazione()).orElse(wizard.getDt_emissione()));
			wizard.getModelloDocumento().setDt_da_competenza_coge(Optional.ofNullable(wizard.getModelloDocumento().getDt_da_competenza_coge()).orElse(wizard.getDt_emissione()));
			wizard.getModelloDocumento().setDt_a_competenza_coge(Optional.ofNullable(wizard.getModelloDocumento().getDt_a_competenza_coge()).orElse(wizard.getDt_emissione()));
			wizard.getModelloDocumento().setUser(Optional.ofNullable(wizard.getModelloDocumento().getUser()).orElse(wizard.getUser()));
			wizard.getModelloDocumento().setTerzoWizardBulk(Optional.ofNullable(wizard.getModelloDocumento().getTerzoWizardBulk()).orElse(wizard.getTerzo()));
			wizard.getModelloDocumento().setModalitaPagamentoWizardBulk(Optional.ofNullable(wizard.getModelloDocumento().getModalitaPagamentoWizardBulk()).orElse(wizard.getModalita_pagamento()));
			wizard.getModelloDocumento().setBancaWizardBulk(Optional.ofNullable(wizard.getModelloDocumento().getBancaWizardBulk()).orElse(wizard.getBanca()));

			//Metto sugli accertamenti il terzo unico se è stato indicato a livello di reversale
			accertamentiColl.stream().forEach(el->{
				el.setTerzoWizardBulk(Optional.ofNullable(wizard.getTerzo()).orElse(el.getTerzoWizardBulk()));
				el.setModalitaPagamentoWizardBulk(Optional.ofNullable(wizard.getModalita_pagamento()).orElse(el.getModalitaPagamentoWizardBulk()));
				el.setBancaWizardBulk(Optional.ofNullable(wizard.getBanca()).orElse(el.getBancaWizardBulk()));
			});

			Documento_genericoBulk documentoGenericoBulk = Utility.createDocumentoGenericoComponentSession().creaDocumentoGenericoDaAccertamenti(userContext, wizard.getModelloDocumento(), accertamentiColl);

			wizard.setTi_automatismo(ReversaleAutomaticaWizardBulk.AUTOMATISMO_DA_DOCATTIVI);
			wizard.setDocAttiviSelezionatiColl(((ReversaleAutomaticaWizardHome)getHome(userContext, ReversaleAutomaticaWizardBulk.class)).findDocAttivi(documentoGenericoBulk));
			//Rimetto sugli oggetti documentiPassiviWizard la descrizione delle righe mandato impostate sugli impegni
			wizard.getDocAttiviSelezionatiColl().stream().forEach(docattivo->{
				accertamentiColl.stream()
						.filter(el->el.getAccertamentoScadenzarioBulk().getEsercizio().equals(docattivo.getEsercizio_accertamento()))
						.filter(el->el.getAccertamentoScadenzarioBulk().getEsercizio_originale().equals(docattivo.getEsercizio_ori_accertamento()))
						.filter(el->el.getAccertamentoScadenzarioBulk().getCd_cds().equals(docattivo.getCd_cds_accertamento()))
						.filter(el->el.getAccertamentoScadenzarioBulk().getPg_accertamento().equals(docattivo.getPg_accertamento()))
						.filter(el->el.getAccertamentoScadenzarioBulk().getPg_accertamento_scadenzario().equals(docattivo.getPg_accertamento_scadenzario()))
						.forEach(accertamento->docattivo.setDescrizioneRigaReversaleWizard(accertamento.getDescrizioneRigaReversaleWizard()));
			});

			getHomeCache(userContext).fetchAll(userContext);

			return this.creaReversaleAutomatica(userContext, wizard);
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	private OggettoBulk creaReversaleAutomaticaDaDocAttivi(UserContext userContext, ReversaleAutomaticaWizardBulk wizard, Collection<V_doc_attivo_accertamento_wizardBulk> docAttiviColl) throws ComponentException {
		try	{
			Map<Integer, Map<Optional<String>, Map<Optional<Long>, List<V_doc_attivo_accertamento_wizardBulk>>>> mapTerzo =
					docAttiviColl.stream().collect(Collectors.groupingBy(V_doc_attivo_accertamento_wizardBulk::getCd_terzo,
							Collectors.groupingBy(p->Optional.ofNullable(p.getCd_modalita_pag()),
									Collectors.groupingBy(q->Optional.ofNullable(q.getPg_banca())))));

			mapTerzo.keySet().stream().forEach(aCdTerzo -> {
				mapTerzo.get(aCdTerzo).keySet().forEach(aCdModalitaPag -> {
					mapTerzo.get(aCdTerzo).get(aCdModalitaPag).keySet().forEach(aPgBanca -> {
						try {
							List docAttiviCompetenzaColl = new ArrayList();
							List docAttiviResiduiColl = new ArrayList();
							mapTerzo.get(aCdTerzo).get(aCdModalitaPag).get(aPgBanca).forEach(docTerzo->{
								try {
									Accertamento_scadenzarioBulk as = (Accertamento_scadenzarioBulk)
											getHome(userContext, Accertamento_scadenzarioBulk.class).findAndLock(new Accertamento_scadenzarioBulk(docTerzo.getCd_cds(), docTerzo.getEsercizio(), docTerzo.getEsercizio_ori_accertamento(), docTerzo.getPg_accertamento(), docTerzo.getPg_accertamento_scadenzario()));

									if (as.getIm_scadenza().compareTo(docTerzo.getIm_scadenza()) != 0 ||
											as.getIm_associato_doc_contabile().compareTo(docTerzo.getIm_associato_doc_contabile()) != 0)
										throw new ApplicationException("Operazione non possibile! E' stata utilizzata da un altro utente la scadenza nr." + docTerzo.getPg_accertamento_scadenzario() + " dell'accertamento " + docTerzo.getEsercizio_ori_accertamento() + "/" + docTerzo.getPg_accertamento());

									if (docTerzo.isCompetenza() || wizard.isFlGeneraReversaleUnica())
										docAttiviCompetenzaColl.add(docTerzo);
									else
										docAttiviResiduiColl.add(docTerzo);
								} catch (Exception e) {
									throw new ApplicationRuntimeException(e);
								}
							});

							ReversaleBulk reversaleCompetenza, reversaleResiduo;
							if ( !docAttiviCompetenzaColl.isEmpty() ) {
								reversaleCompetenza = creaReversaleAutomatica( userContext, wizard, MandatoBulk.TIPO_COMPETENZA );
								reversaleCompetenza.setReversale_terzo( creaReversaleTerzo( reversaleCompetenza, cercaTerzo(userContext, aCdTerzo), wizard.getReversale_terzo().getTipoBollo() ) );
								reversaleCompetenza = aggiungiDocAttivi(userContext, reversaleCompetenza, docAttiviCompetenzaColl );

								reversaleCompetenza.refreshImporto();
								verificaReversale( userContext, reversaleCompetenza, Boolean.TRUE );
								super.creaConBulk( userContext, reversaleCompetenza);
								aggiornaStatoFattura( userContext, reversaleCompetenza, INSERIMENTO_REVERSALE_ACTION );
								wizard.getReversaliColl().add( reversaleCompetenza );
							}
							if ( !docAttiviResiduiColl.isEmpty() )	{
								reversaleResiduo = creaReversaleAutomatica( userContext, wizard, MandatoBulk.TIPO_RESIDUO );
								reversaleResiduo.setReversale_terzo( creaReversaleTerzo( reversaleResiduo, cercaTerzo(userContext, aCdTerzo), wizard.getReversale_terzo().getTipoBollo() ) );
								reversaleResiduo = aggiungiDocAttivi(userContext, reversaleResiduo, docAttiviResiduiColl );

								reversaleResiduo.refreshImporto();
								verificaReversale( userContext, reversaleResiduo, Boolean.TRUE );
								super.creaConBulk( userContext, reversaleResiduo);
								aggiornaStatoFattura( userContext, reversaleResiduo, INSERIMENTO_REVERSALE_ACTION );
								wizard.getReversaliColl().add( reversaleResiduo );
							}
						} catch (Exception e) {
							throw new DetailedRuntimeException(e);
						}
					});
				});
			});
			return wizard;
		} catch ( Exception e ) {
			throw handleException(e);
		}
	}
	
	private ReversaleBulk creaReversaleAutomatica (UserContext userContext, ReversaleAutomaticaWizardBulk wizard, String ti_competenza_residuo ) throws ComponentException
	{
		try
		{
			ReversaleIBulk reversale = new ReversaleIBulk();
			reversale.setToBeCreated();
			reversale.setUser( wizard.getUser() );
			reversale.setEsercizio( wizard.getEsercizio());
			reversale.setCds( wizard.getCds());
			reversale.setUnita_organizzativa( wizard.getUnita_organizzativa());
			reversale.setCd_cds_origine(Optional.ofNullable(wizard.getCd_cds_origine()).orElse(((CNRUserContext) userContext).getCd_cds()));
			reversale.setCd_uo_origine(Optional.ofNullable(wizard.getCd_uo_origine()).orElse(((CNRUserContext) userContext).getCd_unita_organizzativa()));
			reversale.setStato( ReversaleAutomaticaWizardBulk.STATO_REVERSALE_EMESSO);
			reversale.setDt_emissione( wizard.getDt_emissione());
			reversale.setIm_reversale(new BigDecimal(0));
			reversale.setIm_incassato(new BigDecimal(0));

			reversale.setDs_reversale(Optional.ofNullable(wizard.getDs_reversale()).orElse("Reversale automatica da "+(wizard.isAutomatismoDaAccertamenti()?"accertamento":"documenti attivi")));

			reversale.setTi_reversale(ReversaleAutomaticaWizardBulk.TIPO_INCASSO);
			reversale.setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_REV);
			reversale.setTi_competenza_residuo( ti_competenza_residuo );
			reversale.setStato_trasmissione( wizard.STATO_TRASMISSIONE_NON_INSERITO);
			reversale.setStato_coge( MandatoBulk.STATO_COGE_N);

			return reversale;
		}
		catch ( Exception e )
		{
			throw handleException( e )	;
		}	
	}

	private Reversale_terzoBulk creaReversaleTerzo (ReversaleBulk reversale, TerzoBulk terzo, Tipo_bolloBulk bollo ) throws ComponentException
	{
		Reversale_terzoBulk mTerzo = new Reversale_terzoBulk();

		if (!(reversale instanceof ReversaleAutomaticaWizardBulk))
			mTerzo.setToBeCreated();
		
		mTerzo.setUser( reversale.getUser() );
		mTerzo.setReversaleI(new ReversaleIBulk(reversale.getCd_cds(),reversale.getEsercizio(),reversale.getPg_reversale()));
				
		//imposto il terzo
		mTerzo.setTerzo( terzo );

		//imposto il tipo bollo di default
		mTerzo.setTipoBollo( bollo );
		return mTerzo;
	}

	private Reversale_terzoBulk creaReversaleTerzo (UserContext userContext, ReversaleIBulk reversale, TerzoBulk terzo ) throws ComponentException
	{
		try
		{
			return creaReversaleTerzo(reversale, terzo, ((Tipo_bolloHome)getHome( userContext, Tipo_bolloBulk.class )).findTipoBolloDefault(Tipo_bolloBulk.TIPO_SPESA));
		}
		catch ( Exception e )
		{
			throw handleException( e )	;
		}	
	}

	public List findBancaOptions (UserContext userContext,ReversaleAutomaticaWizardBulk reversale) throws PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
	{
		if ( reversale.getReversale_terzo() != null )
		{
			if ( reversale.getModalita_pagamentoOptions() != null && reversale.getModalita_pagamento().getCd_modalita_pag() == null)
				reversale.setModalita_pagamento( (Modalita_pagamentoBulk) reversale.getModalita_pagamentoOptions().get(0));

			SQLBuilder sql = getHome( userContext, BancaBulk.class ).createSQLBuilder();
			sql.addClause(FindClause.AND, "cd_terzo", SQLBuilder.EQUALS, reversale.getReversale_terzo().getCd_terzo() );
			sql.addSQLClause(FindClause.AND, "BANCA.CD_TERZO_DELEGATO", SQLBuilder.ISNULL, null);
			sql.addSQLClause(FindClause.AND, "BANCA.FL_CANCELLATO", SQLBuilder.EQUALS, "N");
			sql.addOrderBy("FL_CC_CDS DESC");		
			if (reversale.getModalita_pagamento() != null && reversale.getModalita_pagamento().getCd_modalita_pag() != null )
			{
				SQLBuilder sql2 = getHome( userContext, Modalita_pagamentoBulk.class ).createSQLBuilder();
				sql2.setHeader( "SELECT DISTINCT TI_PAGAMENTO " );
				sql2.addTableToHeader( "rif_modalita_pagamento" );
				sql2.addSQLClause( FindClause.AND, "modalita_pagamento.cd_terzo", SQLBuilder.EQUALS, reversale.getReversale_terzo().getCd_terzo() );
				sql2.addSQLClause( FindClause.AND, "modalita_pagamento.cd_modalita_pag", SQLBuilder.EQUALS, reversale.getModalita_pagamento().getCd_modalita_pag() );
				sql2.addSQLJoin( "modalita_pagamento.cd_modalita_pag", "rif_modalita_pagamento.cd_modalita_pag" );
				sql2.addSQLClause(FindClause.AND, "MODALITA_PAGAMENTO.CD_TERZO_DELEGATO", SQLBuilder.ISNULL, null);
				
				sql.addSQLClause( FindClause.AND, "TI_PAGAMENTO" , SQLBuilder.EQUALS, sql2 );
			}	
			List result = getHome( userContext, BancaBulk.class ).fetchAll( sql );
			if ( result.size() == 0 )
				throw new ApplicationException("Non esistono coordinate bancarie per il terzo " + reversale.getReversale_terzo().getCd_terzo());
			return result;	
		}
		else
			return null;	
	}

	public List findModalita_pagamentoOptions (UserContext userContext, ReversaleAutomaticaWizardBulk reversale) throws PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
	{
		if ( reversale.getReversale_terzo() != null )
		{
			SQLBuilder sql = getHome( userContext, Modalita_pagamentoBulk.class ).createSQLBuilder();
			sql.addTableToHeader( "RIF_MODALITA_PAGAMENTO");
			sql.addSQLJoin("MODALITA_PAGAMENTO.CD_MODALITA_PAG","RIF_MODALITA_PAGAMENTO.CD_MODALITA_PAG" );
			sql.addClause( FindClause.AND, "cd_terzo", SQLBuilder.EQUALS, reversale.getReversale_terzo().getCd_terzo() );
			sql.addClause( FindClause.AND, "cd_terzo_delegato", SQLBuilder.ISNULL, null );
			List result =  getHome( userContext, Modalita_pagamentoBulk.class ).fetchAll( sql );
			if ( result.size() == 0 )
				throw new ApplicationException("Non esistono modalità di pagamento per il terzo " + reversale.getReversale_terzo().getCd_terzo());
			return result;	

		}
		else
			return null;	
	}

	public OggettoBulk inizializzaBulkPerInserimento (UserContext aUC,OggettoBulk bulk) throws ComponentException
	{
		try
		{
			ReversaleAutomaticaWizardBulk reversale = (ReversaleAutomaticaWizardBulk) bulk;
			reversale.setCds( (CdsBulk)getHome( aUC, CdsBulk.class).findByPrimaryKey( new CdsBulk(CNRUserContext.getCd_cds(aUC))));
			reversale.setUnita_organizzativa( (Unita_organizzativaBulk)getHome( aUC, Unita_organizzativaBulk.class).findByPrimaryKey( new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(aUC))));

			bulk = super.inizializzaBulkPerInserimento( aUC, bulk );			

			return bulk;
		}
		catch ( Exception e )
		{
			throw handleException( bulk, e );
		}	
	}

	public OggettoBulk inizializzaMappaAutomatismo (UserContext aUC, OggettoBulk bulk) throws ComponentException
	{
		try
		{
			ReversaleAutomaticaWizardBulk reversale = (ReversaleAutomaticaWizardBulk)bulk;
			reversale.getReversaliColl().clear();
			if (reversale.isAutomatismoDaAccertamenti()) {
				if (reversale.getFind_doc_attivi().getCd_terzo() == null &&
					reversale.getFind_doc_attivi().getCd_precedente() == null &&
					reversale.getFind_doc_attivi().getCognome() == null &&
					reversale.getFind_doc_attivi().getRagione_sociale() == null &&
					reversale.getFind_doc_attivi().getNome()  == null &&
					reversale.getFind_doc_attivi().getPartita_iva()  == null &&
					reversale.getFind_doc_attivi().getCodice_fiscale()  == null)
					throw new ApplicationException( "Attenzione! Deve essere specificato almeno un campo dell'anagrafica." );
				else 
				{
					reversale.getAccertamentiSelezionatiColl().clear();
					reversale.getAccertamentiColl().clear();
					reversale.setReversale_terzo(creaReversaleTerzo( aUC, reversale, cercaTerzo( aUC, reversale ) ) );
					reversale = listaAccertamentiTerzo( aUC, reversale );
					reversale.setModalita_pagamento((Modalita_pagamentoBulk)findModalita_pagamentoOptions( aUC, reversale).get(0));
					reversale.setBanca((BancaBulk)findBancaOptions(aUC, reversale).get(0));
					reversale.getModelloDocumento().setDt_da_competenza_coge(reversale.getDt_emissione());
					reversale.getModelloDocumento().setDt_a_competenza_coge(reversale.getDt_emissione());
					initializeKeysAndOptionsInto(aUC, bulk);
				}
			}
			else if (reversale.isAutomatismoDaDocumentiAttivi())
			{
				reversale.getDocAttiviColl().clear();

				/*
				 * Necessario per caricare il bollo di default 
				 */
				reversale.setReversale_terzo( creaReversaleTerzo( aUC, reversale, null ) );
				bulk = listaDocAttivi( aUC, reversale );
			}

			return bulk;
		}
		catch ( Exception e )
		{
			throw handleException( bulk, e );
		}	
	}

	public ReversaleAutomaticaWizardBulk listaAccertamentiTerzo (UserContext aUC, ReversaleAutomaticaWizardBulk reversale) throws ComponentException
	{
		try
		{
			Collection result = ((ReversaleAutomaticaWizardHome)getHome( aUC, reversale.getClass())).findAccertamenti( reversale );
			reversale.setAccertamentiColl(result);
			int size = reversale.getAccertamentiColl().size();
			if ( size == 0 )
				throw new ApplicationException( "La ricerca degli Impegni non ha fornito alcun risultato.");
			return reversale;
		}
		catch ( PersistencyException e )
		{
			throw handleException( reversale, e );
		}
		catch ( it.cnr.jada.persistency.IntrospectionException e )
		{
			throw handleException( reversale, e );
		}

	}

	private TerzoBulk cercaTerzo (UserContext aUC, ReversaleAutomaticaWizardBulk wizard) throws ComponentException
	{
		try
		{
			Collection result = ((ReversaleAutomaticaWizardHome)getHome( aUC, wizard.getClass())).findTerzi( wizard );
			if ( result.size() == 0 )
				throw new ApplicationException( "La ricerca dei Terzi non ha fornito alcun risultato.");
			if ( result.size() > 1 )
				throw new ApplicationException( "Esiste piu' di un terzo che soddisfa i criteri di ricerca.");

			return (TerzoBulk)getHome( aUC, TerzoBulk.class).findByPrimaryKey( new TerzoBulk(((V_anagrafico_terzoBulk)result.iterator().next()).getCd_terzo()));				
		}
		catch ( PersistencyException e )
		{
			throw handleException( wizard, e );
		}
		catch ( it.cnr.jada.persistency.IntrospectionException e )
		{
			throw handleException( wizard, e );
		}
	}
	private TerzoBulk cercaTerzo (UserContext aUC, Integer cd_terzo) throws ComponentException
	{
		try
		{
			return (TerzoBulk)getHome( aUC, TerzoBulk.class).findByPrimaryKey( new TerzoBulk(cd_terzo));				
		}
		catch ( PersistencyException e )
		{
			throw handleException( e );
		}
	}
}
