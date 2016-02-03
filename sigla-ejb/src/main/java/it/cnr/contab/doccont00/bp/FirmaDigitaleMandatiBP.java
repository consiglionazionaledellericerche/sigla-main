package it.cnr.contab.doccont00.bp;

import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleIBulk;
import it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.AbilitatoFirma;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SimpleFindClause;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.pdfbox.util.PDFMergerUtility;
/**
 * 
 * @author mspasiano
 * @date 30-11-2015
 * 
 *
 */
public class FirmaDigitaleMandatiBP extends AbstractFirmaDigitaleDocContBP {
	private static final long serialVersionUID = 1L;

	public FirmaDigitaleMandatiBP() {
		super();
	}

	public FirmaDigitaleMandatiBP(String s) {
		super(s);
	}

	@Override
	public void openIterator(ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			Unita_organizzativaBulk uoScrivania = CNRUserInfo.getUnita_organizzativa(actioncontext);			
			CompoundFindClause compoundfindclause = new CompoundFindClause();
			compoundfindclause.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS,
					((CNRUserContext) actioncontext.getUserContext()).getEsercizio());			
			if (uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)!=0) {
				compoundfindclause.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS,
						((CNRUserContext) actioncontext.getUserContext()).getCd_cds());
				if (!uoScrivania.isUoCds())
					compoundfindclause.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS,
						((CNRUserContext) actioncontext.getUserContext()).getCd_unita_organizzativa());				
			}
			compoundfindclause.addClause(FindClause.AND, "ti_documento_cont", SQLBuilder.NOT_EQUALS,
					MandatoBulk.TIPO_REGOLARIZZAZIONE);
			compoundfindclause.addClause(FindClause.AND, "ti_documento_cont", SQLBuilder.NOT_EQUALS,
					ReversaleIBulk.TIPO_INCASSO);
			compoundfindclause.addClause(FindClause.AND, "stato", SQLBuilder.NOT_EQUALS,
					MandatoBulk.STATO_MANDATO_ANNULLATO);

			SimpleFindClause simpleFindClause = new SimpleFindClause();
			simpleFindClause.setLogicalOperator(FindClause.AND);
			simpleFindClause.setSqlClause("pg_documento_cont = pg_documento_cont_padre");

			SimpleFindClause collegatiFindClause = new SimpleFindClause();
			collegatiFindClause.setLogicalOperator(FindClause.AND);
			collegatiFindClause.setSqlClause("pg_documento_cont != pg_documento_cont_padre" + 
					" and cd_tipo_documento_cont = 'MAN' and cd_tipo_documento_cont_padre = 'MAN'");	
			compoundfindclause = CompoundFindClause.and(compoundfindclause, CompoundFindClause.or(simpleFindClause, collegatiFindClause));
			setBaseclause(compoundfindclause);
			
			EJBCommonServices.closeRemoteIterator(getIterator());
			setIterator(actioncontext, find(actioncontext, compoundfindclause, getModel()));
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		}
	}
	@Override
	protected void aggiornaStato(ActionContext actioncontext, String stato, StatoTrasmissione...bulks) throws ComponentException, RemoteException {
		EJBCommonServices.closeRemoteIterator(getIterator());
		for (StatoTrasmissione v_mandato_reversaleBulk : bulks) {
			if (v_mandato_reversaleBulk.getCd_tipo_documento_cont().equalsIgnoreCase(Numerazione_doc_contBulk.TIPO_MAN)) {				
				MandatoIBulk mandato = new MandatoIBulk(v_mandato_reversaleBulk.getCd_cds(), v_mandato_reversaleBulk.getEsercizio(), v_mandato_reversaleBulk.getPg_documento_cont());
				mandato = (MandatoIBulk) getComponentSession().findByPrimaryKey(actioncontext.getUserContext(), mandato);
				mandato.setStato_trasmissione(stato);
				if (stato.equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA))
					mandato.setDt_firma(EJBCommonServices.getServerTimestamp());
				else
					mandato.setDt_firma(null);					
				mandato.setToBeUpdated();
				getComponentSession().modificaConBulk(actioncontext.getUserContext(), mandato);
			} else {
				ReversaleIBulk reversale = new ReversaleIBulk(v_mandato_reversaleBulk.getCd_cds(), v_mandato_reversaleBulk.getEsercizio(), v_mandato_reversaleBulk.getPg_documento_cont());
				reversale = (ReversaleIBulk) getComponentSession().findByPrimaryKey(actioncontext.getUserContext(), reversale);
				reversale.setStato_trasmissione(stato);
				if (stato.equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA))
					reversale.setDt_firma(EJBCommonServices.getServerTimestamp());
				else
					reversale.setDt_firma(null);					
				reversale.setToBeUpdated();
				getComponentSession().modificaConBulk(actioncontext.getUserContext(), reversale);					
			}			
		}		
	}
	
	@Override
	@SuppressWarnings({"unchecked" })
	public void predisponiPerLaFirma(ActionContext actioncontext) throws BusinessProcessException{
		try {
			List<StatoTrasmissione> selectedElements = getSelectedElements(actioncontext);
			if (selectedElements == null || selectedElements.isEmpty())
					throw new ApplicationException("Selezionare almeno un elemento!");
			Format dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			String message = "";
			EJBCommonServices.closeRemoteIterator(getIterator());			
			addSomethingToSelectedElements(actioncontext, selectedElements);
			for (StatoTrasmissione statoTrasmissione : selectedElements) {
				V_mandato_reversaleBulk v_mandato_reversaleBulk = (V_mandato_reversaleBulk)statoTrasmissione;
				if (v_mandato_reversaleBulk.isMandato()) {
					if (!Utility.createMandatoComponentSession().isCollegamentoSiopeCompleto(
							actioncontext.getUserContext(),new MandatoIBulk(v_mandato_reversaleBulk.getCd_cds(),v_mandato_reversaleBulk.getEsercizio(),v_mandato_reversaleBulk.getPg_documento_cont()))) {
						message += "\nIl mandato n."+ v_mandato_reversaleBulk.getPg_documento_cont()+ " non risulta associato completamente a codici Siope, pertanto è stato escluso dalla selezione.";
						continue;
					}
				} else if (v_mandato_reversaleBulk.isReversale()) {
					if (!Utility.createReversaleComponentSession().isCollegamentoSiopeCompleto(
							actioncontext.getUserContext(),new ReversaleIBulk(v_mandato_reversaleBulk.getCd_cds(),v_mandato_reversaleBulk.getEsercizio(),v_mandato_reversaleBulk.getPg_documento_cont()))) {
						message += "\nLa reversale n."+ v_mandato_reversaleBulk.getPg_documento_cont()+ " non risulta associata completamente a codici Siope, pertanto è stata esclusa dalla selezione.";
						continue;						
					}					
				}				
				predisponi(actioncontext, v_mandato_reversaleBulk, dateFormat);
			}
			setMessage("Predisposizione effettuata correttamente." + message);
		} catch (ApplicationException e) {
			setMessage(e.getMessage());
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (IOException e) {
			throw handleException(e);
		}
	}
	private void predisponi(ActionContext actioncontext, V_mandato_reversaleBulk v_mandato_reversaleBulk, Format dateFormat) throws ComponentException, IOException {
		Print_spoolerBulk print = new Print_spoolerBulk();
		print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
		print.setFlEmail(false);
		if (v_mandato_reversaleBulk.getCd_tipo_documento_cont().equalsIgnoreCase(Numerazione_doc_contBulk.TIPO_MAN)) {
			print.setReport("/doccont/doccont/vpg_man_rev_ass.jasper");
			print.setNomeFile("Mandato n. "
					+ v_mandato_reversaleBulk.getPg_documento_cont() + ".pdf");
		} else {
			print.setReport("/doccont/doccont/vpg_reversale.jasper");
			print.setNomeFile("Reversale n. "
					+ v_mandato_reversaleBulk.getPg_documento_cont() + ".pdf");					
		}
		print.setUtcr(actioncontext.getUserContext().getUser());
		print.addParam("aCd_cds", v_mandato_reversaleBulk.getCd_cds(), String.class);
		print.addParam("aCd_terzo", "%", String.class);
		print.addParam("aEs", v_mandato_reversaleBulk.getEsercizio().intValue(), Integer.class);
		print.addParam("aPg_a", v_mandato_reversaleBulk.getPg_documento_cont().longValue(), Long.class);
		print.addParam("aPg_da", v_mandato_reversaleBulk.getPg_documento_cont().longValue(), Long.class);					
		print.addParam("aDt_da", DateUtils.firstDateOfTheYear(1970), Date.class, dateFormat);
		print.addParam("aDt_a", DateUtils.firstDateOfTheYear(3000), Date.class, dateFormat);
		
		Report report = SpringUtil.getBean("printService",
				PrintService.class).executeReport(actioncontext.getUserContext(),
				print);
		CMISPath cmisPath = v_mandato_reversaleBulk.getCMISPath(cmisService);
		v_mandato_reversaleBulk.setStato_trasmissione(MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO);
		Document node = cmisService.restoreSimpleDocument(v_mandato_reversaleBulk, report.getInputStream(), report.getContentType(), report.getName(), cmisPath);
		cmisService.makeVersionable(node);
		aggiornaStato(actioncontext, MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO, v_mandato_reversaleBulk);		
	}
	
	@Override
	protected AbilitatoFirma getAbilitatoFirma() {
		return AbilitatoFirma.DOCCONT;
	}

	@Override
	protected String getTitoloFirma() {
		return "firma per il controllo di ragioneria\ndel mandato e delle reversali collegate";
	}
	@Override
	protected void addSomethingToSelectedElements(ActionContext actioncontext, List<StatoTrasmissione> selectelElements) throws BusinessProcessException {
		DistintaCassiereComponentSession distintaCassiereComponentSession = Utility.createDistintaCassiereComponentSession();
		List<V_mandato_reversaleBulk> adds = new ArrayList<V_mandato_reversaleBulk>();
		for (StatoTrasmissione statoTrasmissione : selectelElements) {
			try {
				adds.addAll(distintaCassiereComponentSession.findMandatiCollegati(actioncontext.getUserContext(), (V_mandato_reversaleBulk) statoTrasmissione));
			} catch (ComponentException e) {
				throw handleException(e);
			} catch (RemoteException e) {
				throw handleException(e);			
			}
		}
		for (V_mandato_reversaleBulk v_mandato_reversaleBulk : adds) {
			if (!selectelElements.contains(v_mandato_reversaleBulk))
				selectelElements.add(v_mandato_reversaleBulk);
		}		
	}
	
	@Override
	protected void executeSign(ActionContext actioncontext, List<StatoTrasmissione> selectelElements, FirmaOTPBulk firmaOTPBulk) throws Exception{
		String message = "";
		for (Iterator<StatoTrasmissione> iterator = selectelElements.iterator(); iterator.hasNext();) {
			try {
				StatoTrasmissione statoTrasmissione = iterator.next();
				if (statoTrasmissione.getCd_tipo_documento_cont().equalsIgnoreCase(Numerazione_doc_contBulk.TIPO_MAN)){
					CMISPath cmisPath = statoTrasmissione.getCMISPath(cmisService);
					Folder folderMandato = (Folder) cmisService.getNodeByPath(cmisPath);					
					List<Rif_modalita_pagamentoBulk> result = Utility.createMandatoComponentSession().findModPagObbligatorieAssociateAlMandato(actioncontext.getUserContext(), 
							(V_mandato_reversaleBulk) statoTrasmissione);
					Set<String> childs = new HashSet<String>();
					for (Rif_modalita_pagamentoBulk rif_modalita_pagamentoBulk : result) {
						Set<String> allegati = documentiContabiliService.getAllegatoForModPag(folderMandato, rif_modalita_pagamentoBulk.getCd_modalita_pag());
						if (allegati.isEmpty())
							throw new ApplicationException("\nAl mandato n."+ statoTrasmissione.getPg_documento_cont()+ " non risulta allegato il documento con tipologia [" + 
											rif_modalita_pagamentoBulk.getDs_modalita_pag() + "]" +
											", pertanto è stato escluso dalla selezione.");
						childs.addAll(allegati);
					}
					//Eseguo il merge dei documenti
					if (!childs.isEmpty()) {
						PDFMergerUtility ut = new PDFMergerUtility();
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						ut.setDestinationStream(out);
						ut.addSource(documentiContabiliService.getStreamDocumento((V_mandato_reversaleBulk) statoTrasmissione));
						for (String documentId : childs) {
							ut.addSource(((Document)documentiContabiliService.getNodeByNodeRef(documentId)).getContentStream().getStream());						
						}
						try {
							ut.mergeDocuments();
						} catch (IOException _ex) {
							throw new ApplicationException("\nAl mandato n."+ statoTrasmissione.getPg_documento_cont()+ " risulta allegato un documento non in formato PDF" + 
									", pertanto è stato escluso dalla selezione.");						
						}
						cmisService.restoreSimpleDocument((V_mandato_reversaleBulk)statoTrasmissione, new ByteArrayInputStream(out.toByteArray()), "application/pdf", "Mandato n. "
								+ statoTrasmissione.getPg_documento_cont() + ".pdf", cmisPath);						
					}
				}
			} catch(ApplicationException _ex) {
				iterator.remove();
				message += _ex.getMessage();
			}
		}
		super.executeSign(actioncontext, selectelElements, firmaOTPBulk);
		setMessage("Firma effettuata correttamente." + message);
	}
}