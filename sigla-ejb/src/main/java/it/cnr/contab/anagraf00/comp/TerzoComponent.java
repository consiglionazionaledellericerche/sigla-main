package it.cnr.contab.anagraf00.comp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoHome;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.RapportoBulk;
import it.cnr.contab.anagraf00.core.bulk.TelefonoBulk;
import it.cnr.contab.anagraf00.core.bulk.Termini_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.anagraf00.core.bulk.V_terzo_anagrafico_sipBulk;
import it.cnr.contab.anagraf00.core.bulk.V_terzo_anagrafico_sipHome;
import it.cnr.contab.anagraf00.core.bulk.V_terzo_sipBulk;
import it.cnr.contab.anagraf00.core.bulk.V_terzo_sipHome;
import it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.missioni00.docs.bulk.AnticipoBulk;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.ReferentialIntegrityException;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CRUDBP;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Questa classe svolge le operazioni fondamentali di lettura, scrittura e filtro dei dati
 * immessi o richiesti dall'utente. In oltre sovrintende alla gestione e creazione dati a cui
 * l'utente stesso non ha libero accesso e/o non gli sono trasparenti.
 */

public class TerzoComponent extends UtilitaAnagraficaComponent implements ICRUDMgr, ITerzoMgr {

public  TerzoComponent() {
}

public RemoteIterator cercaBanchePerTerzoCessionario(UserContext userContext,Modalita_pagamentoBulk modalita_pagamento) throws ComponentException, it.cnr.jada.persistency.PersistencyException {

	SQLBuilder sql = getHome(userContext, BancaBulk.class).createSQLBuilder();
	sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, modalita_pagamento.getTerzo_delegato().getCd_terzo());
	sql.addSQLClause("AND", "TI_PAGAMENTO", sql.EQUALS, modalita_pagamento.getRif_modalita_pagamento().getTi_pagamento());
	sql.addSQLClause("AND", "FL_CANCELLATO", sql.EQUALS, "N");


	return iterator(userContext, sql, BancaBulk.class, null);
}
public RemoteIterator cercaModalita_pagamento_disponibiliByClause(UserContext userContext,TerzoBulk terzo) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
	try {
		return iterator(
			userContext,
			selectModalita_pagamento_disponibiliByClause(userContext,terzo),
			Rif_modalita_pagamentoBulk.class,
			null);
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}

public TerzoBulk cercaTerzoPerUnitaOrganizzativa(UserContext userContext,Unita_organizzativaBulk unita_organizzativa) throws ComponentException {
	try {
		SQLBuilder sql = getHome(userContext,TerzoBulk.class).createSQLBuilder();
		sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS,unita_organizzativa.getCd_unita_organizzativa());
		sql.addSQLClause("AND", "(DT_CANC >= SYSDATE OR DT_CANC IS NULL)");
		sql.addSQLClause("AND", "(DT_FINE_RAPPORTO >= SYSDATE OR DT_FINE_RAPPORTO IS NULL)");
		
		it.cnr.jada.persistency.Broker broker = getHome(userContext,TerzoBulk.class).createBroker(sql);
		if (!broker.next()) return null;
		TerzoBulk terzo = (TerzoBulk)broker.fetch(TerzoBulk.class); 
		// eliminato controllo terzo unico per UO 
		//if (broker.next()) 
			//throw new ApplicationException("Esistono più terzi associati a questa Unità Organizzativa");
		return terzo;
	} catch(it.cnr.jada.persistency.PersistencyException ex) {
		throw new ComponentException(ex);
	}

}

	/**
	 * Viene sottomessa la richiesta di cancellazione dell'<code>OggettoBulk</code> passato in
	 * ingresso. Se viene rilevata una <code>ReferentialIntegrityException</code> si effettua
	 * l'assegnazione, in cascata, della data di fine rapporto, per l'elemento e i sui "figli".
	 *
	 * Nome: Eliminare un oggetto anagrafico;
	 * Pre:  Effettuare l'eliminazione dell'oggetto anagrafico;
	 * Post: Se l'anagrafica ha ancora dei riferimenti anziche effettuare una cancellazione fisica si procede a impostare
	 *       la data di fine rapporto per tutti gli elementi associati e l'anagrafica stessa.
	 *
	 * @param bulk <code>OggettoBulk</code> da eliminare.
	 */

	public void eliminaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		try {
			try {
				TerzoBulk terzo = (TerzoBulk)bulk;
				terzo.setCrudStatus(bulk.TO_BE_UPDATED);
				terzo.setDt_fine_rapporto(getHome(userContext,terzo).getServerDate());
				makeBulkPersistent(userContext,bulk);
			} catch(Throwable e) {
				throw handleException(bulk,e);
			}
			
			//makeBulkPersistent(userContext,bulk);
		//} catch (it.cnr.jada.persistency.sql.ReferentialIntegrityException rie) {
		/*	Angelo 03/01/05 Se ci sono dei dettagli non imposto la data di fine
			try {
				TerzoBulk terzo = (TerzoBulk)bulk;
				terzo.setCrudStatus(bulk.TO_BE_UPDATED);
				terzo.setDt_fine_rapporto(getHome(userContext,terzo).getServerDate());
				makeBulkPersistent(userContext,bulk);
			} catch(Throwable e) {
				throw handleException(bulk,rie);
			}*/
			//throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");
		} catch (Throwable e) {
			throw handleException(bulk,e);
		}
	}
	public AnagraficoBulk getAnagEnte(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
		try {
			it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configurazione = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession",it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
			AnagraficoBulk ente = (AnagraficoBulk)getHome(userContext,AnagraficoBulk.class).findByPrimaryKey(
				new AnagraficoBulk(
					new Integer(
						configurazione.getIm01(userContext,new Integer(0),null,"COSTANTI","CODICE_ANAG_ENTE").toString()
					)
				)
			);
			return ente;
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,OggettoBulk bulk) throws ComponentException {
	try {
		TerzoBulk terzo = (TerzoBulk)super.inizializzaBulkPerInserimento(userContext,bulk);
		TerzoHome home = (TerzoHome)getHome(userContext,terzo);
		terzo.setFlSbloccoFatturaElettronica(false);
		terzo.setRif_termini_pagamento_disponibili(new BulkList(home.findRif_termini_pagamento_disponibili(terzo)));
		terzo.setTi_terzo(TerzoBulk.ENTRAMBI);
		return terzo;
	} catch(Throwable e) {
		throw handleException(e);
	}
}
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws ComponentException {
	try {
		TerzoBulk terzo = (TerzoBulk)super.inizializzaBulkPerModifica(userContext,bulk);
		
		if (terzo.getCd_unita_organizzativa() != null) {
			terzo.setUnita_organizzativa((it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk)getHome(userContext,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class).findByPrimaryKey(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaKey(terzo.getCd_unita_organizzativa())));
			if (terzo.getUnita_organizzativa() == null) {
				terzo.setUnita_organizzativa(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk());
				terzo.getUnita_organizzativa().setCd_unita_organizzativa(terzo.getCd_unita_organizzativa());
				terzo.getUnita_organizzativa().setCrudStatus(terzo.NORMAL);
			}
		}

		TerzoHome home = (TerzoHome)getHome(userContext,terzo);
		AnagraficoHome anagraficoHome = (AnagraficoHome)getHome(userContext, terzo.getAnagrafico());
		
		terzo.setContatti(new BulkList(home.findContatti(terzo)));
		terzo.setBanche(new BulkList(home.findBanca(terzo)));
		terzo.setTelefoni(new BulkList(home.findTelefoni(terzo,TelefonoBulk.TEL)));
		terzo.setEmail(new BulkList(home.findTelefoni(terzo,TelefonoBulk.EMAIL)));
		terzo.setPec(new BulkList(home.findTelefoni(terzo,TelefonoBulk.PEC)));
		terzo.setFax(new BulkList(home.findTelefoni(terzo,TelefonoBulk.FAX)));
		terzo.setTermini_pagamento(new BulkList(home.findTermini_pagamento(terzo)));
		terzo.setModalita_pagamento(new BulkList(home.findModalita_pagamento(terzo)));

		initializeKeysAndOptionsInto(userContext,terzo);

		for (Iterator i=terzo.getBanche().iterator();i.hasNext();)
			initializeKeysAndOptionsInto(userContext,(OggettoBulk)i.next());
	
		terzo.setRif_termini_pagamento_disponibili(new BulkList(home.findRif_termini_pagamento_disponibili(terzo)));

		terzo.setTerzo_speciale(isTerzoSpeciale(userContext,terzo));
		// terzo non modificabile(Telecom) sfruttato lo stesso flag dei terzi che rappresentano le UO
		if (terzo.getAnagrafico()!= null && terzo.getAnagrafico().isSpeciale())
			terzo.setTerzo_speciale(true);
		terzo.setDipendente(anagraficoHome.findRapportoDipendenteFor(terzo.getAnagrafico()));

		getHomeCache(userContext).fetchAll(userContext);

		return terzo;
	} catch(Exception e) {
		throw handleException(e);
	}
}
public TerzoBulk inizializzaTerzoPerUnitaOrganizzativa(UserContext userContext,Unita_organizzativaBulk unita_organizzativa) throws ComponentException {
	TerzoBulk terzo = new TerzoBulk();
	terzo.setUnita_organizzativa(unita_organizzativa);
	terzo.setAnagrafico(getAnagEnte(userContext));
	terzo.setDenominazione_sede(unita_organizzativa.getDs_unita_organizzativa());

	// Imposto il Tipo del Terzo come ENTRAMBI, di default
	//	Questo mi permette, nel caso di una UO che non ha Terzi, di visualizzare i raidoButton relativi al terzo
	terzo.setTi_terzo(TerzoBulk.ENTRAMBI);
	
	return terzo;
}

public Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
	TerzoBulk terzo = (TerzoBulk)bulk;
	SQLBuilder sql = getHome(userContext, terzo,"V_TERZO_CF_PI").createSQLBuilder();
	sql.addClause(bulk.buildFindClauses(true));
	//SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses,bulk);
	if (terzo.getCd_anag() == null) {
		sql.addTableToHeader("ANAGRAFICO");
		sql.addSQLJoin("V_TERZO_CF_PI.CD_ANAG","ANAGRAFICO.CD_ANAG");
		sql.addSQLClause("AND","ANAGRAFICO.TI_ENTITA",sql.NOT_EQUALS,AnagraficoBulk.STRUT_CNR);
		sql.addClause(clauses);
	} else{
		sql.addClause("AND","cd_anag",sql.EQUALS,terzo.getCd_anag());
		sql.addClause(clauses);
	}
	return sql;
}

public SQLBuilder selectAbi_cabByClause(UserContext userContext,BancaBulk banca, AbicabBulk abiCab, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
	
	SQLBuilder sql = getHome(userContext,AbicabBulk.class).createSQLBuilder();
	sql.addClause(clauses);
	//if (banca.getCd_terzo_delegato() != null){
		//sql.addSQLClause("AND", )
	//}
		
	return sql;
}

	public SQLBuilder selectComune_fiscaleByClause(UserContext userContext,
												AnagraficoBulk anag,
												ComuneBulk comune,
												CompoundFindClause clause)
	throws ComponentException, it.cnr.jada.persistency.PersistencyException
	{
		if (clause == null) clause = comune.buildFindClauses(null);

		SQLBuilder sql = getHome(userContext, comune).createSQLBuilder();
		if(clause != null) sql.addClause(clause);

		if (NazioneBulk.EXTRA_CEE.equals( anag.getTi_italiano_estero() ))
			sql.addSQLClause( "AND", "TI_ITALIANO_ESTERO", sql.NOT_EQUALS, NazioneBulk.ITALIA);
		sql.openParenthesis("AND");
		sql.addSQLClause("AND", "DT_CANC", sql.ISNULL, null);
		sql.addSQLClause("OR","DT_CANC",sql.GREATER,it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
		sql.closeParenthesis();
		return sql;
	}


public SQLBuilder selectModalita_pagamento_disponibiliByClause(UserContext userContext,TerzoBulk terzo) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
	
	SQLBuilder sql = getHome(userContext,Rif_modalita_pagamentoBulk.class).createSQLBuilder();
	if (terzo.getAnagrafico() != null && terzo.getAnagrafico().isStrutturaCNR())
		sql.addSQLClause("AND", "FL_PER_CESSIONE", sql.EQUALS, "N");
	sql.addSQLClause("AND", "FL_CANCELLATO", sql.EQUALS, "N");
		
	//SQLBuilder sql2 = getHome(userContext,Modalita_pagamentoBulk.class).createSQLBuilder();
	//sql2.addClause("AND","cd_terzo",sql.EQUALS,terzo.getCd_terzo());
	//sql2.addSQLJoin("MODALITA_PAGAMENTO.CD_MODALITA_PAG","RIF_MODALITA_PAGAMENTO.CD_MODALITA_PAG");
	//sql.addSQLNotExistsClause("AND",sql2);

	return sql;
}


public SQLBuilder selectTerzo_delegatoByClause(UserContext userContext,Modalita_pagamentoBulk mod_pagamento, TerzoBulk terzo, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
	
	SQLBuilder sql = getHome(userContext,TerzoBulk.class,"V_TERZO_CF_PI").createSQLBuilder();
	sql.addClause(clauses);

	if (mod_pagamento != null && mod_pagamento.getTerzo() != null)
		sql.addSQLClause("AND", "CD_TERZO", sql.NOT_EQUALS, mod_pagamento.getTerzo().getCd_terzo());
	
	
	return sql;
}

	/**
	 * Imposta il comune della sede di un terzo.
	 *
	 * Nome: Gestione comune sede;
	 * Pre:  Ricerca del comune e acricamenti dei cap relativi;
	 * Post: Viene assegnato il comune e lanciato l'aggornamento dell'elenco dei cap associati.
	 *
	 * @param anagrafico <code>AnagraficoBulk</code> a cui abbartiene il terzo.
	 * @param terzo <code>TerzoBulk</code> su cui va impostato il comunedella sede.
	 * @param comune il <code>ComuneBulk</code> del comune da impostare.
	 *
	 * @return AnagraficoBulk <code>AnagraficoBulk</code> completo.
	 */

	public TerzoBulk setComune_sede(UserContext userContext,TerzoBulk terzo, it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune) throws it.cnr.jada.comp.ComponentException {
		terzo.setComune_sede(comune);
		terzo.setCaps_comune(null);
		super.initializeKeysAndOptionsInto(userContext,terzo);
		if (comune != null)
			terzo.setCap_comune_sede(comune.getCd_cap());
		else
			terzo.setCap_comune_sede(null);
		return terzo;
	}
/**
 * Insert the method's description here.
 * Creation date: (11/11/2002 14.12.02)
 * @param param it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
private void validaCreaModificaBanche(TerzoBulk terzo) throws ComponentException{

 
	try{ 
		BulkList banche = terzo.getBanche();
		boolean isCcd = false;
		boolean isBancaItalia = false;

		if (banche != null) {
			for (java.util.Iterator i = banche.iterator(); i.hasNext();) {
				BancaBulk banca = (BancaBulk) i.next();
				boolean deleted = banca.getFl_cancellato()==null?false:banca.getFl_cancellato().booleanValue();
	            
				if (!deleted){
					if (banca.getFl_cc_cds() != null && banca.getFl_cc_cds().booleanValue() && isCcd) {
						throw new ApplicationException("Attenzione: è possibile definire solamente una Banca come CCD.\nControllare le Banche indicate per le Modalità di Pagamento di tipo Bancario, (B).");
					} else if (banca.getFl_cc_cds().booleanValue()){
						isCcd = true;
					}

					if (banca.getTi_pagamento() != null && banca.getTi_pagamento().equals(Rif_modalita_pagamentoBulk.BANCA_ITALIA)){
						if (isBancaItalia){
							throw new ApplicationException("Attenzione: è possibile definire solamente una Banca come Banca d'Italia.\nControllare le Banche indicate per le Modalità di Pagamento di tipo (I).");
						} else {
							isBancaItalia = true;
						}
					}
		            

					// Rich. 695: Borriello Gennaro - 15.01.2004
					// Per le banche che NON sono da ORIGINE STIPENDI
					/* Se TI_PAGAMENTO è di tipo 'B':
					 *	- che il campo CIN sia stato valorizzato;
					 *	- il numero di conto corrente DEVE essere al massimo di 12 caratteri:
					 *		se l'utente inserisce un numero di lunghezza maggiore viene visualizzato
					 *		un messaggio di errore. Se la lunghezza è minore, vengono aggiunti tanti
					 *		zeri quanti ne servono per arrivare a 12 carat.
					*/
					if (!banca.isOrigineStipendi() && banca.getTi_pagamento() != null && banca.getTi_pagamento().equals(Rif_modalita_pagamentoBulk.BANCARIO) && !banca.getFl_cancellato()){
					 if (banca.getNazione_iban()!=null && banca.getNazione_iban().getCd_iso().equals("IT") ) {
						// Controllo su campo CIN
						if (banca.getCin() == null || banca.getCin().trim().equals("")){
							throw new ApplicationException("Attenzione: il campo CIN è obbligatorio per i Tipi Pagamento B.");
						}


						if (!Character.isLetterOrDigit(banca.getCin().charAt(0))){
							throw new ApplicationException("Attenzione: il campo CIN non è valido.");
						}

						// Controllo su num. C/C
						String cc = banca.getNumero_conto().trim();
						if (cc.length()>12){
							throw new ApplicationException("Attenzione: il numero di conto corrente può essere al massimo di 12 caratteri per i Tipi Pagamento B.");
						}

						if (cc.length()<12){
							StringBuffer buf = new StringBuffer("000000000000");
							buf.replace(buf.length()-cc.length(), buf.length(),cc);
							cc = buf.toString();			            				
						}
			            
						banca.setNumero_conto(cc);
					 }
					}
				}	            

			}
		}
	} catch (Throwable t){
		throw handleException(terzo, t);
	}
}
protected void validaCreaModificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try {
		super.validaCreaModificaConBulk(userContext,bulk);

		TerzoBulk terzo = (TerzoBulk)bulk;

		// Controllo che il terzo non sia un terzo speciale
		if (terzo.getCd_terzo() != null && isTerzoSpeciale(userContext,terzo))
			throw new ApplicationException("Terzo non modificabile (terzo speciale)");
			
		// se anagrafica di tipo Struttura CNR aggiorno nome_unita_organizzativa
		if (terzo.getUnita_organizzativa() != null && 
			terzo.getAnagrafico().isStrutturaCNR())
			terzo.setNome_unita_organizzativa(terzo.getUnita_organizzativa().getDs_unita_organizzativa());

		// verifica valorizzazione Comune Sede
		if(terzo.getComune_sede() == null)
			throw new it.cnr.jada.comp.ApplicationException("Comune sede è obbligatorio");

		// verifica che esistano Banche associate al tipo di modalità di pagamento delle modalità di pagamento */		
		for (java.util.Iterator i = terzo.getModalita_pagamento().iterator();i.hasNext();) {
			Modalita_pagamentoBulk modalita_pagamento = (Modalita_pagamentoBulk)i.next();
			if (terzo.getBanche(modalita_pagamento).isEmpty()) {
				modalita_pagamento.setToBeDeleted();
				i.remove();
			}
		}

		java.sql.Timestamp dt_odierna = getHome(userContext,terzo).getServerDate();

		TerzoBulk terzo_bck = (TerzoBulk)getHome(userContext,terzo).findByPrimaryKey(terzo);
		if (terzo_bck != null) {
			if (terzo.getDt_fine_rapporto() == null) {
				if (terzo_bck.getDt_fine_rapporto() != null)
					throw new ApplicationException("Non è possibile togliere la data di fine rapporto di un terzo.");
			} else if (!terzo.getDt_fine_rapporto().equals(terzo_bck.getDt_fine_rapporto())) {
				if (terzo.getDt_fine_rapporto().before(dt_odierna))
					throw new ApplicationException("La data di fine rapporto del terzo non può essere anteriore alla data odierna");
				AnagraficoBulk anagrafico_bck = (AnagraficoBulk)getHome(userContext,terzo.getAnagrafico()).findByPrimaryKey(terzo.getAnagrafico());
				if (anagrafico_bck.getDt_fine_rapporto() != null && anagrafico_bck.getDt_fine_rapporto().before(terzo.getDt_fine_rapporto()))
					throw new ApplicationException("La data di fine rapporto del terzo non può essere posteriore alla data di fine rapporto dell'anagrafico");
			}
		}

		if (terzo.getCrudStatus() == OggettoBulk.TO_BE_CREATED){
			// Controlla che la UO specificata non sia già utilizzata					
			if(terzo.getAnagrafico() != null && terzo.getAnagrafico().isStrutturaCNR())
			 validaUnitaOrganizzativa(userContext, terzo);
		}

		/* Se l'utente non ha specificato una data di fine rapporto, 
			eredita quella eventuale dell'Anagrafica
		*/
		if (terzo.getDt_fine_rapporto() == null 
				&& terzo.getAnagrafico() != null 
				&&  terzo.getAnagrafico().getDt_fine_rapporto() != null){
			terzo.setDt_fine_rapporto(terzo.getAnagrafico().getDt_fine_rapporto());
		}

		validaCreaModificaBanche(terzo);

//		if (terzo.getAnagrafico() != null && terzo.getAnagrafico().isPersonaGiuridica() && !terzo.getAnagrafico().isEntePubblico() && terzo.getAnagrafico().getDataAvvioFattElettr() != null ){
//			if (terzo.getAnagrafico().getDataAvvioFattElettr().before(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate())) {
//				if (terzo.getCodiceDestinatarioFatt() == null && !terzo.esistePecFatturazioneElettronica() && !terzo.getFlSbloccoFatturaElettronica()){
//					throw new ApplicationException("Dato che in anagrafico è avviata la fatturazione elettronica è necessario indicare il codice destinatario fattura o la pec");
//				}
//			}
//		}
				
		if (terzo.inseriteDiverseMailFatturazioneElettronica()){
			throw new ApplicationException("Non è possibile indicare più e-mail per la fatturazione elettronica");
		}
		if (terzo.inseriteDiversePecFatturazioneElettronica()){
			throw new ApplicationException("Non è possibile indicare più PEC per la fatturazione elettronica");
		}
			
	} catch(Throwable e) {
		throw handleException(e);
	}
}

	
	public Date getSystemDate() {
		Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	Date data = cal.getTime();			    	
    	return data;
	}
	
	public void validaModificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try {
		boolean hasAccesso = ((it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession")).controllaAccesso(userContext, TerzoBulk.ACCESSO_PER_CANCELLAZIONE_BANCA);
		TerzoBulk terzo = (TerzoBulk)bulk;
		for (java.util.Iterator it = terzo.getBanche().deleteIterator();it.hasNext();){
			BancaBulk banca = (BancaBulk)it.next();
			try {
				makeBulkPersistent(userContext, banca);
			} catch(ReferentialIntegrityException ex){
				if (hasAccesso||banca.getCd_terzo_delegato()!=null){
					banca.setFl_cancellato(new Boolean(true));
					banca.setCrudStatus(OggettoBulk.TO_BE_UPDATED);
					// (19/06/2002 14:49:07) CNRADM
					//v.add(banca);
				} else
				throw new ApplicationException("Attenzione: non è possibile cancellare fisicamente le banche selezionate.\nL'Utente non ha l'autorizzazione per la cancellazione logica.");
			}
		}

		// Controlla che la UO specificata non sia già utilizzata
		//validaUnitaOrganizzativa(userContext, terzo);
		super.validaModificaConBulk(userContext,bulk);
	} catch (java.rmi.RemoteException re){
		throw handleException(re);
	} catch (javax.ejb.EJBException ejbe){
		throw handleException(ejbe);
	} catch(it.cnr.jada.persistency.PersistencyException e){
		throw handleException(e);
	}
}
/**
 * Controlla che l'Unità Organizzativa indicata per il Terzo non sia già stata utilizzata:
 *	infatti, non è possibile associare una UO a più terzi.
 *
 * Creation date: (22/08/2002 12.10.44)
 * Author: Gennaro Borriello
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param terzo il <code>TerzoBulk</code> da controllare
 */
private void validaUnitaOrganizzativa(UserContext userContext,TerzoBulk terzo) throws it.cnr.jada.comp.ComponentException {
	try {

		if(terzo.getCd_unita_organizzativa() == null)
			throw new ApplicationException("Attenzione: Unità Organizzativa non specificata");		
		SQLBuilder sql = getHome(userContext, TerzoBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, terzo.getCd_unita_organizzativa());
		//if (sql.executeExistsQuery(getConnection(userContext)))
			//throw new ApplicationException("Attenzione: l'Unità Organizzativa selezionata è già stata utilizzata.");		
		
	} catch(Throwable e) {
		throw handleException(e);
	}
}
public void verificaIntegritaBanche(UserContext userContext,TerzoBulk terzo) throws it.cnr.jada.comp.ComponentException {	
	try {
		boolean hasAccesso = ((it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession")).controllaAccesso(userContext, "CFGANAGCFCOREDELBANM");
		for (java.util.Iterator it = terzo.getBanche().deleteIterator();it.hasNext();){
			BancaBulk banca = (BancaBulk)it.next();
			
			/* Se la Banca ha ORIGINE = 'S', (è stata creata dalla migrazioen per un dipendente)
				viene lanciata una eccezione di tipo <code>ReferentialIntegrityException</code>.
			*/
			//if (banca.getOrigine().equals(BancaBulk.ORIGINE_STIPENDI))
				//throw new ReferentialIntegrityException();
			try {
				deleteBulk(userContext, banca);
			} catch(ReferentialIntegrityException ex){
				if (hasAccesso){
					banca.setFl_cancellato(new Boolean(true));
					banca.setCrudStatus(OggettoBulk.TO_BE_UPDATED);
					// (19/06/2002 14:49:07) CNRADM
					//v.add(banca);
				} else throw new ApplicationException("Attenzione: l'Utente non ha l'autorizzazione a compiere questa operazione");
			}
		}
	} catch (java.rmi.RemoteException re){
		throw handleException(re);
	} catch (javax.ejb.EJBException ejbe){
		throw handleException(ejbe);
	} catch(it.cnr.jada.persistency.PersistencyException e){
		throw handleException(e);
	} 
}
/*Angelo 03/01/2005*/
public OggettoBulk modificaConBulk (UserContext aUC,OggettoBulk bulk) 
	throws ComponentException
{	
   
	TerzoBulk terBulk = (TerzoBulk)bulk;
	/* ***************************************************** *
	 * ***************************************************** *
	 * ** Se la data di fine rapporto è stata valorizzata ** *
	 * ** controllo i dettagli del Terzo                  ** *
	 * ***************************************************** *
	 * ***************************************************** */
	Timestamp today = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
	if (terBulk.getDt_fine_rapporto()!=null && !terBulk.getDt_fine_rapporto().after(today)) 
	{		
		/*Accertamento*/
		SQLBuilder sql = getHome(aUC, it.cnr.contab.doccont00.core.bulk.AccertamentoBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;        
        
		/*Anticipo*/
		sql = getHome(aUC, AnticipoBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;
		
		/*Banca*/
		sql = getHome(aUC, BancaBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;
		
		/*Compenso*/
		sql = getHome(aUC, it.cnr.contab.compensi00.docs.bulk.CompensoBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;
			
		/*Conguaglio*/
		sql = getHome(aUC, it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;
			
		/*Documento_Generico_Riga*/
		sql = getHome(aUC, Documento_generico_rigaBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;
		
		/*Fattura_Attiva*/
		sql = getHome(aUC, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;	
		
		/*Fattura_Passiva*/
		sql = getHome(aUC, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;		
		
		/*Fondo_Assegnatario*/
		sql = getHome(aUC, it.cnr.contab.fondiric00.core.bulk.Fondo_assegnatarioBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_RESPONSABILE_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;	
		
		/*Fondo_Attivita_Vincolata*/
		sql = getHome(aUC, it.cnr.contab.fondiric00.core.bulk.Fondo_attivita_vincolataBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_RESPONSABILE_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;
		
		/*Fondo_Economale*/
		sql = getHome(aUC, it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;		
		
		/*Fondo_Spesa*/
		sql = getHome(aUC, it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;		
		
		/*Inventario_Beni*/
		sql = getHome(aUC, it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_ASSEGNATARIO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;
		
		/*Inventario_Consegnatario*/
		sql = getHome(aUC, it.cnr.contab.inventario00.tabrif.bulk.Inventario_consegnatarioBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_CONSEGNATARIO", sql.EQUALS, terBulk.getCd_terzo() );
		sql.addSQLClause("OR", "CD_DELEGATO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;	
		
		/*Linea_Attivita*/
		sql = getHome(aUC, it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_RESPONSABILE_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;					
		
		/*Mandato_Riga*/
		sql = getHome(aUC, it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;		
		
		/*Mandato_Terzo*/
		sql = getHome(aUC, it.cnr.contab.doccont00.core.bulk.Mandato_terzoBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;
		
		/*Minicarriera*/
		sql = getHome(aUC, it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;	
		
		/*Missione*/
		sql = getHome(aUC, it.cnr.contab.missioni00.docs.bulk.MissioneBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;					
		
		/*Modalita_Pagamento*/
		sql = getHome(aUC, Modalita_pagamentoBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;
		
		/*Obbligazione*/
		sql = getHome(aUC, it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;	
		
		/*Progetto*/
		sql = getHome(aUC, it.cnr.contab.progettiric00.core.bulk.ProgettoBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_RESPONSABILE_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;
		
		/*Progetto_Finanziatore*/
		sql = getHome(aUC, it.cnr.contab.progettiric00.core.bulk.Progetto_finanziatoreBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_FINANZIATORE_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;		
		
		/*Progetto_Partner_Esterno*/
		sql = getHome(aUC, it.cnr.contab.progettiric00.core.bulk.Progetto_partner_esternoBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_PARTNER_ESTERNO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;		
		
		/*Reversale_Riga*/
		sql = getHome(aUC, it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;		
		
		/*Reversale_Terzo*/
		sql = getHome(aUC, it.cnr.contab.doccont00.core.bulk.Reversale_terzoBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;
		
		/*Tabella Rimborso*/
		sql = getHome(aUC, it.cnr.contab.missioni00.docs.bulk.RimborsoBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;
		
		/*Termini_Pagamento*/
		sql = getHome(aUC, Termini_pagamentoBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, terBulk.getCd_terzo() );
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
		
		sql=null;
		
		/*Unita_Organizzativa*/
		sql = getHome(aUC, Unita_organizzativaBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "CD_RESPONSABILE", sql.EQUALS, terBulk.getCd_terzo() );
		sql.addSQLClause("OR", "CD_RESPONSABILE_AMM", sql.EQUALS, terBulk.getCd_terzo() );		
		try 
		{
		  if (sql.executeCountQuery(getConnection(aUC))>0)							
			throw new ApplicationException("Impossibile cancellare l'anagrafica perchè risulta utilizzata nei documenti contabili o amministrativi.");						 
		} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}								
	}
    
	super.modificaConBulk(aUC,bulk);    
	return bulk;
}
public java.util.List findListaTerziSIP(UserContext userContext,String query,String dominio,String tipoterzo,String tipoRicerca)throws ComponentException{
	try {	
		V_terzo_anagrafico_sipHome home = (V_terzo_anagrafico_sipHome)getHome(userContext, V_terzo_anagrafico_sipBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		if (dominio.equalsIgnoreCase("cd_terzo"))
			sql.addSQLClause("AND","CD_TERZO",SQLBuilder.EQUALS,query);
		else if (dominio.equalsIgnoreCase("denominazione")){
			if (tipoterzo.equalsIgnoreCase("fisica")){
				sql.openParenthesis("AND");
				sql.addSQLClause("AND","TI_ENTITA",SQLBuilder.EQUALS,AnagraficoBulk.FISICA);
				sql.addSQLClause("OR","TI_ENTITA",SQLBuilder.EQUALS,AnagraficoBulk.DIVERSI);
				sql.closeParenthesis();
				sql.openParenthesis("AND");
				for(StringTokenizer stringtokenizer = new StringTokenizer(query, " "); stringtokenizer.hasMoreElements();){
					String queryDetail = stringtokenizer.nextToken();
					if ((tipoRicerca != null && tipoRicerca.equalsIgnoreCase("selettiva"))|| tipoRicerca == null){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail)))
							sql.addSQLClause("AND","COGNOME",SQLBuilder.CONTAINS,queryDetail);
						else{
							sql.openParenthesis("AND");
							sql.addSQLClause("OR","COGNOME",SQLBuilder.CONTAINS,queryDetail);
							sql.addSQLClause("OR","COGNOME",SQLBuilder.CONTAINS,RemoveAccent.convert(queryDetail));
							sql.closeParenthesis();
						}	
					}else if (tipoRicerca.equalsIgnoreCase("puntuale")){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail))){
							sql.openParenthesis("AND");
							  sql.addSQLClause("AND","UPPER(COGNOME)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							  sql.addSQLClause("OR","COGNOME",SQLBuilder.STARTSWITH,queryDetail+" ");
							  sql.addSQLClause("OR","COGNOME",SQLBuilder.ENDSWITH," "+queryDetail);
							sql.closeParenthesis();  
						}else{
							sql.openParenthesis("AND");
							  sql.openParenthesis("AND");
							    sql.addSQLClause("OR","UPPER(COGNOME)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							    sql.addSQLClause("OR","UPPER(COGNOME)",SQLBuilder.EQUALS,RemoveAccent.convert(queryDetail).toUpperCase());
							  sql.closeParenthesis();
							  sql.openParenthesis("OR");							  
							    sql.addSQLClause("OR","COGNOME",SQLBuilder.STARTSWITH,queryDetail+" ");
							    sql.addSQLClause("OR","COGNOME",SQLBuilder.STARTSWITH,RemoveAccent.convert(queryDetail)+" ");
							  sql.closeParenthesis();  
							  sql.openParenthesis("OR");
							    sql.addSQLClause("OR","COGNOME",SQLBuilder.ENDSWITH," "+queryDetail);
							    sql.addSQLClause("OR","COGNOME",SQLBuilder.ENDSWITH," "+RemoveAccent.convert(queryDetail));
							  sql.closeParenthesis();  
							sql.closeParenthesis();  
						}
					}
				}
				sql.closeParenthesis();
				sql.addOrderBy("UPPER(COGNOME)");
				sql.addOrderBy("UPPER(NOME)");
			}else if (tipoterzo.equalsIgnoreCase("giuridica")){
				sql.openParenthesis("AND");
				sql.addSQLClause("AND","TI_ENTITA",SQLBuilder.EQUALS,AnagraficoBulk.GIURIDICA);
				sql.addSQLClause("OR","TI_ENTITA",SQLBuilder.EQUALS,AnagraficoBulk.DIVERSI);
				sql.addSQLClause("OR","TI_ENTITA",SQLBuilder.EQUALS,AnagraficoBulk.STRUT_CNR);
				sql.closeParenthesis();
				sql.openParenthesis("AND");
				for(StringTokenizer stringtokenizer = new StringTokenizer(query, " "); stringtokenizer.hasMoreElements();){
					String queryDetail = stringtokenizer.nextToken();
					if ((tipoRicerca != null && tipoRicerca.equalsIgnoreCase("selettiva"))|| tipoRicerca == null){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail)))
							sql.addSQLClause("AND","DENOMINAZIONE_SEDE",SQLBuilder.CONTAINS,queryDetail);
						else{
							sql.openParenthesis("AND");
							sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.CONTAINS,queryDetail);
							sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.CONTAINS,RemoveAccent.convert(queryDetail));
							sql.closeParenthesis();
						}	
					}else if (tipoRicerca.equalsIgnoreCase("puntuale")){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail))){
							sql.openParenthesis("AND");
							  sql.addSQLClause("AND","UPPER(DENOMINAZIONE_SEDE)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							  sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.STARTSWITH,queryDetail+" ");
							  sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.ENDSWITH," "+queryDetail);
							sql.closeParenthesis();  
						}else{
							sql.openParenthesis("AND");
							  sql.openParenthesis("AND");
							    sql.addSQLClause("OR","UPPER(DENOMINAZIONE_SEDE)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							    sql.addSQLClause("OR","UPPER(DENOMINAZIONE_SEDE)",SQLBuilder.EQUALS,RemoveAccent.convert(queryDetail).toUpperCase());
							  sql.closeParenthesis();
							  sql.openParenthesis("OR");							  
							    sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.STARTSWITH,queryDetail+" ");
							    sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.STARTSWITH,RemoveAccent.convert(queryDetail)+" ");
							  sql.closeParenthesis();  
							  sql.openParenthesis("OR");
							    sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.ENDSWITH," "+queryDetail);
							    sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.ENDSWITH," "+RemoveAccent.convert(queryDetail));
							  sql.closeParenthesis();  
							sql.closeParenthesis();  
						}
					}
				}
				sql.closeParenthesis();
				sql.addOrderBy("UPPER(DENOMINAZIONE_SEDE)");
			}
		}
		return home.fetchAll(sql);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
public java.util.List findListaTerziSIP_rendicontazione(UserContext userContext,String query,String dominio,String tipoterzo,String tipoRicerca,Timestamp data_inizio,Timestamp data_fine,String Dip)throws ComponentException{
	try {	
		V_terzo_sipHome home = (V_terzo_sipHome)getHome(userContext, V_terzo_sipBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		if(data_inizio!=null && data_fine !=null){
			GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
			data_da.setTime(data_inizio);
			GregorianCalendar data_a = (GregorianCalendar) GregorianCalendar.getInstance();
			data_a.setTime(data_fine);
			int month1 = data_da.get(java.util.GregorianCalendar.MONTH)+1;
			int year1 = data_da.get(java.util.GregorianCalendar.YEAR);
			int month2 = data_a.get(java.util.GregorianCalendar.MONTH)+1;
			int year2 = data_a.get(java.util.GregorianCalendar.YEAR);
			sql.openParenthesis("AND");
			sql.addSQLClause("AND","DATA_CESSAZIONE",SQLBuilder.ISNULL,null);
			sql.addSQLClause("OR","DATA_CESSAZIONE",SQLBuilder.GREATER_EQUALS,data_inizio);
			sql.closeParenthesis();
			 
			sql.openParenthesis("AND");
			sql.addSQLClause("AND","ANNO_MESE",SQLBuilder.GREATER_EQUALS,new java.math.BigDecimal(year1).toString()+(new java.math.BigDecimal(month1).toString().length()==1?"0".concat(new java.math.BigDecimal(month1).toString()):new java.math.BigDecimal(month1).toString()));
			sql.addSQLClause("AND","ANNO_MESE",SQLBuilder.LESS_EQUALS,new java.math.BigDecimal(year2).toString()+(new java.math.BigDecimal(month2).toString().length()==1?"0".concat(new java.math.BigDecimal(month2).toString()):new java.math.BigDecimal(month2).toString()));
			sql.addSQLClause("OR","ANNO_MESE",SQLBuilder.ISNULL,null);
			sql.closeParenthesis();
			sql.addSQLClause("AND","DT_INI_VALIDITA",SQLBuilder.LESS_EQUALS,data_fine);
			sql.addSQLClause("AND","DT_FIN_VALIDITA",SQLBuilder.GREATER_EQUALS,data_inizio);
		}
		if (dominio.equalsIgnoreCase("cd_terzo"))
			sql.addSQLClause("AND","CD_TERZO",SQLBuilder.EQUALS,query);
		else if(dominio.equalsIgnoreCase("matricola")){
 			sql.addSQLClause("AND","MATRICOLA",SQLBuilder.EQUALS,new Long(query));
		}
		else if (dominio.equalsIgnoreCase("denominazione")){
			if (tipoterzo.equalsIgnoreCase("fisica")){
				sql.openParenthesis("AND");
				sql.addSQLClause("AND","TI_ENTITA",SQLBuilder.EQUALS,AnagraficoBulk.FISICA);
				sql.addSQLClause("OR","TI_ENTITA",SQLBuilder.EQUALS,AnagraficoBulk.DIVERSI);
				sql.closeParenthesis();
				sql.openParenthesis("AND");
				for(StringTokenizer stringtokenizer = new StringTokenizer(query, " "); stringtokenizer.hasMoreElements();){
					String queryDetail = stringtokenizer.nextToken();
					if ((tipoRicerca != null && tipoRicerca.equalsIgnoreCase("selettiva"))|| tipoRicerca == null){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail)))
							sql.addSQLClause("AND","COGNOME",SQLBuilder.CONTAINS,queryDetail);
						else{
							sql.openParenthesis("AND");
							sql.addSQLClause("OR","COGNOME",SQLBuilder.CONTAINS,queryDetail);
							sql.addSQLClause("OR","COGNOME",SQLBuilder.CONTAINS,RemoveAccent.convert(queryDetail));
							sql.closeParenthesis();
						}	
					}else if (tipoRicerca.equalsIgnoreCase("puntuale")){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail))){
							sql.openParenthesis("AND");
							  sql.addSQLClause("AND","UPPER(COGNOME)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							  sql.addSQLClause("OR","COGNOME",SQLBuilder.STARTSWITH,queryDetail+" ");
							  sql.addSQLClause("OR","COGNOME",SQLBuilder.ENDSWITH," "+queryDetail);
							sql.closeParenthesis();  
						}else{
							sql.openParenthesis("AND");
							  sql.openParenthesis("AND");
							    sql.addSQLClause("OR","UPPER(COGNOME)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							    sql.addSQLClause("OR","UPPER(COGNOME)",SQLBuilder.EQUALS,RemoveAccent.convert(queryDetail).toUpperCase());
							  sql.closeParenthesis();
							  sql.openParenthesis("OR");							  
							    sql.addSQLClause("OR","COGNOME",SQLBuilder.STARTSWITH,queryDetail+" ");
							    sql.addSQLClause("OR","COGNOME",SQLBuilder.STARTSWITH,RemoveAccent.convert(queryDetail)+" ");
							  sql.closeParenthesis();  
							  sql.openParenthesis("OR");
							    sql.addSQLClause("OR","COGNOME",SQLBuilder.ENDSWITH," "+queryDetail);
							    sql.addSQLClause("OR","COGNOME",SQLBuilder.ENDSWITH," "+RemoveAccent.convert(queryDetail));
							  sql.closeParenthesis();  
							sql.closeParenthesis();  
						}
					}
				}
				sql.closeParenthesis();
				sql.addOrderBy("COGNOME");
				sql.addOrderBy("NOME");
			}else if (tipoterzo.equalsIgnoreCase("giuridica")){
				sql.openParenthesis("AND");
				sql.addSQLClause("AND","TI_ENTITA",SQLBuilder.EQUALS,AnagraficoBulk.GIURIDICA);
				sql.addSQLClause("OR","TI_ENTITA",SQLBuilder.EQUALS,AnagraficoBulk.DIVERSI);
				sql.closeParenthesis();
				sql.openParenthesis("AND");
				for(StringTokenizer stringtokenizer = new StringTokenizer(query, " "); stringtokenizer.hasMoreElements();){
					String queryDetail = stringtokenizer.nextToken();
					if ((tipoRicerca != null && tipoRicerca.equalsIgnoreCase("selettiva"))|| tipoRicerca == null){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail)))
							sql.addSQLClause("AND","DENOMINAZIONE_SEDE",SQLBuilder.CONTAINS,queryDetail);
						else{
							sql.openParenthesis("AND");
							sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.CONTAINS,queryDetail);
							sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.CONTAINS,RemoveAccent.convert(queryDetail));
							sql.closeParenthesis();
						}	
					}else if (tipoRicerca.equalsIgnoreCase("puntuale")){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail))){
							sql.openParenthesis("AND");
							  sql.addSQLClause("AND","UPPER(DENOMINAZIONE_SEDE)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							  sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.STARTSWITH,queryDetail+" ");
							  sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.ENDSWITH," "+queryDetail);
							sql.closeParenthesis();  
						}else{
							sql.openParenthesis("AND");
							  sql.openParenthesis("AND");
							    sql.addSQLClause("OR","UPPER(DENOMINAZIONE_SEDE)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							    sql.addSQLClause("OR","UPPER(DENOMINAZIONE_SEDE)",SQLBuilder.EQUALS,RemoveAccent.convert(queryDetail).toUpperCase());
							  sql.closeParenthesis();
							  sql.openParenthesis("OR");							  
							    sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.STARTSWITH,queryDetail+" ");
							    sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.STARTSWITH,RemoveAccent.convert(queryDetail)+" ");
							  sql.closeParenthesis();  
							  sql.openParenthesis("OR");
							    sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.ENDSWITH," "+queryDetail);
							    sql.addSQLClause("OR","DENOMINAZIONE_SEDE",SQLBuilder.ENDSWITH," "+RemoveAccent.convert(queryDetail));
							  sql.closeParenthesis();  
							sql.closeParenthesis();  
						}
					}
				}
				sql.closeParenthesis();
				sql.addOrderBy("DENOMINAZIONE_SEDE");
			}
		
		}
		  if(Dip!=null && Dip.toUpperCase().compareTo("S")==0)
			sql.addSQLClause("AND","MATRICOLA",SQLBuilder.ISNOTNULL,null);
		  else if(Dip!=null && Dip.toUpperCase().compareTo("N")==0)
				sql.addSQLClause("AND","MATRICOLA",SQLBuilder.ISNULL,null);
		return home.fetchAll(sql);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
public List findNazioniIban(UserContext userContext,BancaBulk bulk) throws it.cnr.jada.comp.ComponentException {
	List lista;
	try {
			NazioneHome home = (NazioneHome)getHome(userContext,NazioneBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addClause("AND", "fl_iban", SQLBuilder.EQUALS, Boolean.TRUE);
			sql.addClause("AND", "struttura_iban", SQLBuilder.ISNOTNULL, null);

			sql.addOrderBy("cd_iso");
			Broker broker = home.createBroker(sql);
			lista = home.fetchAll(broker);
			broker.close();
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	} catch(Exception e) {
		throw handleException(e);
	}
	return lista;	
}
	@SuppressWarnings("unchecked")
	public TerzoBulk completaTerzo(UserContext userContext, TerzoBulk terzo) throws it.cnr.jada.comp.ComponentException {
		try {
			terzo.setDipendente(((AnagraficoHome)getHome(userContext, AnagraficoBulk.class)).findRapportoDipendenteFor(terzo.getAnagrafico()));
			return terzo;
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}

}
