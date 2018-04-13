package it.cnr.contab.docamm00.comp;

import java.rmi.RemoteException;
import java.util.GregorianCalendar;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileHome;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.ejb.ProgressiviAmmComponentSession;
import it.cnr.contab.docamm00.tabrif.bulk.SezionaleBulk;

import java.io.Serializable;

import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.docs.bulk.AutofatturaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.LoggableStatement;

public class AutoFatturaComponent 
	extends CRUDComponent 
	implements IAutoFatturaMgr,
				ICRUDMgr,
				Cloneable,
				Serializable {

/**
 * AutoFatturaComponent constructor comment.
 */
public AutoFatturaComponent() {

	/*Default constructor*/
}
//^^@@
/** 
  *  Calcolo totali di fattura.
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      Aggiorna lo stato dei documenti amministrativi associati al mandato.
  *  Inserimento del mandato.
  *    PreCondition:
  *      Si sta inserendo o modificando il mandato.
  *    PostCondition:
  *      Aggiorna lo stato dei documenti amministrativi associati al mandato con stato P o L.
  *  Annullamento del mandato.
  *    PreCondition:
  *      Si sta annullando il mandato.
  *    PostCondition:
  *      Aggiorna lo stato dei documenti amministrativi associati al mandato con stato A.
  *  Cancellazione del mandato.
  *    PreCondition:
  *      Si sta cancellando il mandato.
  *    PostCondition:
  *      Aggiorna lo stato dei documenti amministrativi associati al mandato con stato Q.
  *  Si verifica errore.
  *    PreCondition:
  *      Condizione di errore.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio dettagliato.
 */
//^^@@

public void aggiornaStatoDocumentiAmministrativi(
								it.cnr.jada.UserContext userContext, 
								String cd_cds, 
								String cd_unita_organizzativa, 
								String tipo_documento, 
								Integer esercizio, 
								Long progressivo, 
								String action) 
								throws it.cnr.jada.comp.ComponentException {
}

private void assegnaProgressivo(UserContext userContext,AutofatturaBulk autofattura) throws ComponentException {

	try {
		// Assegno un nuovo progressivo alla fattura
		ProgressiviAmmComponentSession progressiviSession = (ProgressiviAmmComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_ProgressiviAmmComponentSession", ProgressiviAmmComponentSession.class);
		Numerazione_doc_ammBulk numerazione = new Numerazione_doc_ammBulk(autofattura);
		autofattura.setPg_autofattura(progressiviSession.getNextPG(userContext, numerazione));
	} catch (Throwable t) {
		throw handleException(autofattura, t);
	}
}
//^^@@
/** 
  *  Tutti I controli superati.
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      Viene restituita l'autofattura inalterata.
 */
//^^@@

public IDocumentoAmministrativoBulk calcoloConsuntivi (UserContext aUC, IDocumentoAmministrativoBulk documentoAmministrativo) throws ComponentException {

	return documentoAmministrativo;
}
private void callVerifyDataRegistrazione(
	UserContext userContext, 
	AutofatturaBulk autofattura)
	throws  ComponentException {

	LoggableStatement cs = null;
	try	{
		cs = new LoggableStatement(getConnection(userContext), 
			"{ call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
			"CNRCTB100.chkDtRegistrazPerIva(?, ?, ?, ?, ?) }",false,this.getClass());
		cs.setString(1, autofattura.getCd_cds_origine());
		cs.setString(2, autofattura.getCd_uo_origine());
		cs.setInt(3, autofattura.getEsercizio().intValue());
		cs.setString(4, autofattura.getCd_tipo_sezionale());
		cs.setTimestamp(5, autofattura.getDt_registrazione());
		
		cs.executeQuery();
		
	} catch (Throwable e) {
		throw handleException(autofattura, e);
	} finally {
		try {
			if (cs != null) cs.close();
		} catch (java.sql.SQLException e) {
			throw handleException(autofattura, e);
		}
 	}
}
//^^@@
/** 
  *  Creazione di un'autofattura
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      l'autofattura viene creata
  *  Si verifica errore.
  *    PreCondition:
  *      Condizione di errore.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio dettagliato.
  *  Validazione autofattura.
  *    PreCondition:
  *      Non viene superata la validazione.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio relativo alla validazione non verificata.
  *  Assegnazione di un progressivo
  *    PreCondition:
  *      Non è possibile assegnare un progressivo per l'autofattura
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio relativo.
 */
//^^@@

public OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	return creaConBulk(userContext, bulk, null);
}
/**
 * creaConBulk method comment.
 */
public it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status) throws it.cnr.jada.comp.ComponentException {

	AutofatturaBulk autofattura = (AutofatturaBulk)bulk;
	assegnaProgressivo(userContext, autofattura);
	
	validaAutofattura(userContext, autofattura);

	autofattura.setUser(userContext.getUser());
	return super.creaConBulk(userContext, autofattura);
}
//^^@@
/** 
  *  Cancellazione di un'autofattura
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      l'autofattura viene cancellata
  *  Si verifica errore.
  *    PreCondition:
  *      Condizione di errore.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio dettagliato.
 */
//^^@@

private void eliminaAutofattura(UserContext userContext,AutofatturaBulk autofattura) throws it.cnr.jada.comp.ComponentException {

	if (!autofattura.STATO_CONTABILIZZATO.equalsIgnoreCase(autofattura.getStato_cofi()) &&
		!autofattura.STATO_PARZIALE.equalsIgnoreCase(autofattura.getStato_cofi()))
		throw new it.cnr.jada.comp.ApplicationException("L'autofattura per la fattura passiva \"" + autofattura.getFattura_passiva().getDs_fattura_passiva() + "\" è già stata pagata. Operazione annullata.");

	//A seguito dell'errore segnalato 569 (dovuto alla richiesta 423) il controllo
	//successivo viene demandato a isStampataSuRegistroIVA della FATTURA PASSIVA.

	//String statoIVA = getStatoIVA(userContext, autofattura);
	//if (AutofatturaBulk.STATO_IVA_B.equalsIgnoreCase(statoIVA) || 
		//AutofatturaBulk.STATO_IVA_C.equalsIgnoreCase(statoIVA)) {
			//String descr = (autofattura.getFattura_passiva().getDs_fattura_passiva() == null) ?
										//autofattura.getFattura_passiva().getPg_fattura_passiva().toString() :
										//autofattura.getFattura_passiva().getDs_fattura_passiva();
			//throw new it.cnr.jada.comp.ApplicationException("L'autofattura per la fattura passiva \"" + descr + "\" ha uno stato IVA diverso da \"A\". Operazione annullata.");
	//}
}
//^^@@
/** 
  *  Cancellazione di un'autofattura
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      l'autofattura viene cancellata
  *  Si verifica errore.
  *    PreCondition:
  *      Condizione di errore.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio dettagliato.
 */
//^^@@

public void eliminaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	eliminaAutofattura(userContext, (AutofatturaBulk)bulk);

	super.eliminaConBulk(userContext, bulk);
}
//^^@@
/** 
  *  estrazione sezionali validi
  *    PreCondition:
  *      Nessun errore riscontrato.
  *    PostCondition:
  *      E' stato estratto il vettore dei sezionali corrispondente al tipo  sezionale richiesto.
  *  fattura di tipo non valido
  *    PreCondition:
  *      E' stata selezionata una fattura di tipo non valido.
  *    PostCondition:
  *      Viene inviato il messaggio: "Il tipo di fattura selezionato non è valido".
  *  Tipo sezionale autofattura
  *    PreCondition:
  *      Il tipo sezionale ricercato non è di tipo autofattura
  *    PostCondition:
  *      Non viene aggiunto al vettore.
  *  Tipo sezionale per autofatture intra UE
  *    PreCondition:
  *      Il tipo sezionale ricercato non è contemporaneamente di tipo autofattura e tipo intra UE
  *    PostCondition:
  *      Viene ricercato il tipo sezionale generico per autofattura.
  *  Tipo sezionale per fatture S. Marino senza iva
  *    PreCondition:
  *      Il tipo sezionale ricercato non è contemporaneamente di tipo autofattura e tipo S. Marino senza iva
  *    PostCondition:
  *      Viene ricercato il tipo sezionale generico per autofattura.
  *  Ricerca del tipo sezionale generico per autofatture
  *    PreCondition:
  *      Il tipo sezionale per autofattura non è definito
  *    PostCondition:
  *      Non viene aggiunto al vettore.
  *  Tipo fatture commerciali
  *    PreCondition:
  *      Il tipo di sezionale non è commerciale
  *    PostCondition:
  *      Non viene aggiunto al vettore.
  *  Tipo fatture vendita
  *    PreCondition:
  *      Il tipo di sezionale non è definito per la vendita
  *    PostCondition:
  *      Non viene aggiunto al vettore.
  *  Tipo della fattura
  *    PreCondition:
  *      Il tipo della fattura non è 'autofattura'
  *    PostCondition:
  *      Non viene restituito alcun elemento
  *  Esercizio del tipo sezionale dell'autofattura
  *    PreCondition:
  *      Il tipo sezionale non è definito per l'esercizio corrente
  *    PostCondition:
  *      Non viene aggiunto al vettore.
  *  Unità Organizzativa del tipo sezionale dell'autofattura
  *    PreCondition:
  *      Il tipo sezionale non è definito per la UO corrente
  *    PostCondition:
  *      Non viene aggiunto al vettore.
  */
//^^@@

public java.util.Vector estraeSezionali (UserContext aUC, AutofatturaBulk autofattura) throws ComponentException {

	try {
		if (autofattura == null || autofattura.getTi_istituz_commerc() == null)
			return null;
		return new java.util.Vector(findSezionali(aUC, autofattura, false));
	} catch (Throwable t) {
		throw handleException(autofattura, t);
	}
}
public java.util.Vector estraeSezionali (UserContext aUC, AutofatturaBulk autofattura,boolean obbIta) throws ComponentException {

	try {
		if (autofattura == null || autofattura.getTi_istituz_commerc() == null)
			return null;
		return new java.util.Vector(findSezionali(aUC, autofattura, obbIta));
	} catch (Throwable t) {
		throw handleException(autofattura, t);
	}
}
public java.util.Collection findSezionali(UserContext aUC, AutofatturaBulk autofattura,boolean obbIta) 
	throws ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	
	if (autofattura.getTi_istituz_commerc() == null) return null;
	
	java.util.Vector options = new java.util.Vector();
	String[][] autofatturaClause = { { "TIPO_SEZIONALE.FL_AUTOFATTURA", "Y", "AND" } };
	//String[][] defaultOption = new String[][] { autofatturaClause };
	
	if (autofattura.getFl_intra_ue().booleanValue())
		options.add(new String[][] { { "TIPO_SEZIONALE.FL_INTRA_UE","Y", "AND" } });
	else if (autofattura.getFl_extra_ue().booleanValue())
		options.add(new String[][] { { "TIPO_SEZIONALE.FL_EXTRA_UE","Y", "AND" } });
	else if (autofattura.getFl_san_marino_con_iva().booleanValue())
		options.add(new String[][] { { "TIPO_SEZIONALE.FL_SAN_MARINO_CON_IVA","Y", "AND" } });
	else if (autofattura.getFl_san_marino_senza_iva().booleanValue())
		options.add(new String[][] { { "TIPO_SEZIONALE.FL_SAN_MARINO_SENZA_IVA","Y", "AND" } });
	else
		if(!obbIta)
			options.add(new String[][] { { "TIPO_SEZIONALE.FL_ORDINARIO","Y", "AND" } });

	if (autofattura.getFl_split_payment()!=null && autofattura.getFl_split_payment().booleanValue())
		options.add(new String[][] { { "TIPO_SEZIONALE.FL_SPLIT_PAYMENT","Y", "AND" } });
	else
		options.add(autofatturaClause);

	//********************************
	//Il seguente if è stato richiesto da Paolo espressamente su richiesta CINECA
	if (autofattura.isAutofatturaNeeded() && autofattura.isAutofatturaDiBeni())
		options.add(new String[][] {
							{ "TIPO_SEZIONALE.TI_BENE_SERVIZIO", autofattura.getTi_bene_servizio(), "AND" }
						});
	else
		options.add(new String[][] {
						{ "TIPO_SEZIONALE.TI_BENE_SERVIZIO", "*", "AND" } //,
//						{ "TIPO_SEZIONALE.TI_BENE_SERVIZIO", autofattura.getTi_bene_servizio(), "OR" }
					});
	  Fattura_passivaBulk fatturaPassiva=(Fattura_passivaBulk) findByPrimaryKey(aUC, autofattura.getFattura_passiva());
	
	  if ((fatturaPassiva.isCommerciale()) && 
          	(fatturaPassiva.getFl_split_payment()==null ||
          	(fatturaPassiva.getFl_split_payment()!=null && !fatturaPassiva.getFl_split_payment().booleanValue())) && 
          	fatturaPassiva.getData_protocollo()!=null ){
         		Configurazione_cnrBulk conf = getLimitiRitardoDetraibile(aUC, fatturaPassiva);
           		if(fatturaPassiva.getDt_registrazione().after(conf.getDt01()) && fatturaPassiva.getDt_registrazione().before(conf.getDt02()))
           			options.add(new String[][]{{"TIPO_SEZIONALE.FL_REG_TARDIVA", "Y", "AND"}});
           		else
                	options.add(new String[][]{{"TIPO_SEZIONALE.FL_REG_TARDIVA", "N", "AND"}});

      }else{
      		options.add(new String[][]{{"TIPO_SEZIONALE.FL_REG_TARDIVA", "N", "AND"}});
      }
	  
	//********************************
	//java.util.Collection result = 
	return ((it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleHome)getHome(aUC,it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk.class)).findTipiSezionali(
												autofattura.getEsercizio(),
												autofattura.getCd_uo_origine(),
												autofattura.getTi_istituz_commerc(),
												it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk.VENDITE,
												autofattura.getTi_fattura(),
												options);
	//if (result == null || result.isEmpty())
		//result = ((it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleHome)getHome(aUC,it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk.class)).findTipiSezionali(
												//autofattura.getEsercizio(),
												//autofattura.getCd_uo_origine(),
												//autofattura.getTi_istituz_commerc(),
												//it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk.VENDITE,
												//autofattura.getTi_fattura(),
												//defaultOption);
	//return result;
		
}

private String getStatoIVA(UserContext userContext, AutofatturaBulk autofattura)
	throws ComponentException {

	return (autofattura.getProtocollo_iva() == null ||
			autofattura.getProtocollo_iva_generale() == null) ?
				"A" : "B";
}
//^^@@
/** 
  *  Modifica di un'autofattura
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      l'autofattura viene aggiornata
  *  Si verifica errore.
  *    PreCondition:
  *      Condizione di errore.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio dettagliato.
 */
//^^@@

public OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	return modificaConBulk(userContext, bulk, null);
}
/**
 * modificaConBulk method comment.
 */
public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status) throws it.cnr.jada.comp.ComponentException {

	try {
		validaModificaConBulk(userContext,bulk);
		return eseguiModificaConBulk(userContext,bulk);
	} catch (Throwable e) {
		throw handleException(e);
	}
}

/** 
  *  Normale.
  *    PreCondition:
  *      Viene richiesta la visualizzazione del consuntivo fattura.
  *    PostCondition:
  *      Vegono restituiti i dettagli fattura raggruppati per codice IVA.
 */
public void protocolla(it.cnr.jada.UserContext param0, java.sql.Timestamp param1, java.lang.Long param2) throws it.cnr.jada.comp.ComponentException {}
/**
 * riportaAvanti method comment.
 */
public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaAvanti(it.cnr.jada.UserContext userContext, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk docAmm, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status) throws it.cnr.jada.comp.ComponentException {
	return null;
}
/**
 * riportaIndietro method comment.
 */
public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaIndietro(it.cnr.jada.UserContext userContext, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk docAmm) throws it.cnr.jada.comp.ComponentException {
	return null;
}
/**
 * Annulla le modifiche apportate al compenso e ritorna al savepoint impostato in precedenza
 *
 * Pre-post-conditions:
 *
 * Nome: Rollback to savePoint
 * Pre:  Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata 
 * Post: Tutte le modifiche effettuate sul compenso vengono annullate, mentre rimangono valide le
 *       modifiche apportate al doc. amministrativo che ha aperto il compenso
 * @param	uc	lo UserContext che ha generato la richiesta
 */	
public void rollbackToSavePoint(UserContext userContext, String savePointName) throws ComponentException {

	try {
		rollbackToSavepoint(userContext, savePointName);
	} catch (java.sql.SQLException e) {
		if (e.getErrorCode() != 1086)
			throw handleException(e);
	}
}
/**
 * Imposta un savepoint che consente di salvare le modifiche apportate al doc. amministrativo fino a quel momento
 * in modo che se gli aggiornamenti apportati al compenso non venissero confermati (rollback), comunque non verrebbero persi
 * anche quelli del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Imposta savePoint
 * Pre:  Una richiesta di impostare un savepoint e' stata generata 
 * Post: Un savepoint e' stato impostato in modo che le modifiche apportate al doc. amministrativo vengono consolidate
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 */	
public void setSavePoint(UserContext userContext, String savePointName) throws ComponentException {

	try {
		setSavepoint(userContext, savePointName);
	} catch (java.sql.SQLException e) {
		throw handleException(e);
	}
}
//^^@@
/** 
  *  Aggiornamento di un dettaglio di documento amministrativo
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      Il dettaglio viene aggiornato
  *  Si verifica errore.
  *    PreCondition:
  *      Condizione di errore.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio dettagliato.
 */
//^^@@

public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk update(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk param1) throws it.cnr.jada.comp.ComponentException {
	return null;
}
public IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(
	it.cnr.jada.UserContext userContext, 
	IScadenzaDocumentoContabileBulk scadenza)
	throws it.cnr.jada.comp.ComponentException {

	try {
		((IScadenzaDocumentoContabileHome)getHome(userContext, scadenza.getClass())).aggiornaImportoAssociatoADocAmm(userContext,scadenza);
	} catch (it.cnr.jada.persistency.PersistencyException exc) {
		throw handleException((OggettoBulk)scadenza, exc);
	} catch (it.cnr.jada.bulk.BusyResourceException exc) {
		throw handleException((OggettoBulk)scadenza, exc);
	} catch (it.cnr.jada.bulk.OutdatedResourceException exc) {
		throw handleException((OggettoBulk)scadenza, exc);
	}

	return scadenza;
}
//^^@@
/** 
  *  validazione autofattura
  *    PreCondition:
  *      Non esiste il sezionale per l'autofattura legato al sezionale fattura di origine
  *    PostCondition:
  *      Viene inviato il messaggio "non è stato definito un sezionale per le autofatture e il tipo sezionale"
 */
//^^@@

public void validaAutofattura(UserContext aUC,AutofatturaBulk autofattura) throws ComponentException {

	if (!verificaEsistenzaSezionalePer(aUC, autofattura))
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è stato definito un sezionale per le autofatture e il tipo sezionale \"" + autofattura.getTipo_sezionale().getDs_tipo_sezionale() + "\"!");

	//Verifica la validità della data di registrazione rispetto all'ultima
	//data di stampa registri IVA
	callVerifyDataRegistrazione(aUC, autofattura);
}

private boolean verificaEsistenzaSezionalePer(
	UserContext userContext, 
	AutofatturaBulk autofattura)
	throws ComponentException {

	if (autofattura != null && autofattura.getTipo_sezionale() != null) {

		SezionaleBulk key = new SezionaleBulk(
									autofattura.getCd_cds_origine(), 
									autofattura.getTipo_sezionale().getCd_tipo_sezionale(),
									autofattura.getCd_uo_origine(), 
									autofattura.getEsercizio(), 
									autofattura.getTi_fattura());
		try {
			return getHome(userContext, it.cnr.contab.docamm00.tabrif.bulk.SezionaleBulk.class).findByPrimaryKey(key) != null;
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(autofattura, e);
		}
	}
	return false;
}
/**
 * verificaStatoEsercizio method comment.
 */
public boolean verificaStatoEsercizio(it.cnr.jada.UserContext userContext, it.cnr.contab.config00.esercizio.bulk.EsercizioBulk anEsercizio) throws it.cnr.jada.comp.ComponentException {
	return false;
}
public Configurazione_cnrBulk getLimitiRitardoDetraibile(UserContext userContext, Fattura_passivaBulk fattura) throws ComponentException {

    try {

        Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
                .createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
        GregorianCalendar tsGregorian = (GregorianCalendar) GregorianCalendar.getInstance();
       	tsGregorian.setTime(fattura.getData_protocollo());
        
       	Configurazione_cnrBulk conf =sess.getConfigurazione(userContext, tsGregorian.get(GregorianCalendar.YEAR), null,
                Configurazione_cnrBulk.PK_FATTURA_PASSIVA, Configurazione_cnrBulk.SK_LIMITE_REG_TARDIVA);
       	if (conf !=null && conf.getDt01()!=null && conf.getDt02()!=null)
       		return conf;
       	else
       		throw new ApplicationException("Configurazione registrazione tardiva mancante.");
      
    } catch (javax.ejb.EJBException ex) {
        throw handleException(ex);
    } catch (RemoteException ex) {
        throw handleException(ex);
    }
}
}
