package it.cnr.contab.doccont00.comp;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsHome;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.config00.latt.bulk.CostantiTi_gestione;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Ass_evold_evnewBulk;
import it.cnr.contab.config00.pdcfin.bulk.Ass_evold_evnewHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneHome;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneHome;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrHome;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.prevent00.bulk.Pdg_vincoloBulk;
import it.cnr.contab.prevent00.bulk.Pdg_vincoloHome;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaHome;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_costiBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_costiHome;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_other_fieldBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoHome;
import it.cnr.contab.progettiric00.core.bulk.TipoFinanziamentoBulk;
import it.cnr.contab.progettiric00.core.bulk.V_saldi_piano_econom_progettoBulk;
import it.cnr.contab.progettiric00.core.bulk.V_saldi_piano_econom_progettoHome;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrBulk;
import it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrHome;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.ObjectNotFoundException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class SaldoComponent extends it.cnr.jada.comp.GenericComponent implements ISaldoMgr,Cloneable,Serializable
{
	private class CtrlPianoEcoDett {
		private String tipoNatura;
		private boolean isUoArea;
		private boolean isCdrPersonale;
		private boolean isVoceSpeciale;
		private BigDecimal importo = BigDecimal.ZERO;
		
		private final static String TIPO_REIMPIEGO = "RIM";
		private final static String TIPO_FONTE_INTERNA = "FIN";
		private final static String TIPO_FONTE_ESTERNA = "FES";
		
		public String getTipoNatura() {
			return tipoNatura;
		}
		public void setTipoNatura(String tipoNatura) {
			this.tipoNatura = tipoNatura;
		}
		public boolean isUoArea() {
			return isUoArea;
		}
		public void setUoArea(boolean isUoArea) {
			this.isUoArea = isUoArea;
		}
		public boolean isCdrPersonale() {
			return isCdrPersonale;
		}
		public void setCdrPersonale(boolean isCdrPersonale) {
			this.isCdrPersonale = isCdrPersonale;
		}
		public boolean isVoceSpeciale() {
			return isVoceSpeciale;
		}
		public void setVoceSpeciale(boolean isVoceSpeciale) {
			this.isVoceSpeciale = isVoceSpeciale;
		}
		public BigDecimal getImporto() {
			return importo;
		}
		public void setImporto(BigDecimal importo) {
			this.importo = importo;
		}
		public boolean isNaturaReimpiego(){
			return TIPO_REIMPIEGO.equals(getTipoNatura());
		}
		public boolean isNaturaFonteEsterna(){
			return TIPO_FONTE_ESTERNA.equals(getTipoNatura());
		}
		public boolean isNaturaFonteInterna(){
			return TIPO_FONTE_INTERNA.equals(getTipoNatura());
		}
	}
	private class CtrlPianoEco {
		public CtrlPianoEco(ProgettoBulk progetto) {
			super();
			this.progetto = progetto;
		}
		private ProgettoBulk progetto;

		private List<CtrlPianoEcoDett> dett = new ArrayList<CtrlPianoEcoDett>();
		
		public ProgettoBulk getProgetto() {
			return progetto;
		}
		public void setProgetto(ProgettoBulk progetto) {
			this.progetto = progetto;
		}
		public List<CtrlPianoEcoDett> getDett() {
			return dett;
		}
		public void setDett(List<CtrlPianoEcoDett> dett) {
			this.dett = dett;
		}
		public BigDecimal getImpPositivi() {
			return this.getImpPositivi(dett.stream());
		}
		public BigDecimal getImpNegativi() {
			return this.getImpNegativi(dett.stream());
		}
		public BigDecimal getImpPositiviArea() {
			return this.getImpPositivi(dett.stream().filter(CtrlPianoEcoDett::isUoArea));
		}
		public BigDecimal getImpNegativiArea() {
			return this.getImpNegativi(dett.stream().filter(CtrlPianoEcoDett::isUoArea));
		}
		public BigDecimal getImpPositiviAreaNaturaReimpiego() {
			return this.getImpPositivi(dett.stream().filter(CtrlPianoEcoDett::isUoArea).filter(el->el.isNaturaReimpiego()));
		}
		public BigDecimal getImpNegativiAreaNaturaReimpiego() {
			return this.getImpNegativi(dett.stream().filter(CtrlPianoEcoDett::isUoArea).filter(el->el.isNaturaReimpiego()));
		}
		public BigDecimal getImpPositiviAreaNaturaFonteEsterna() {
			return this.getImpPositivi(dett.stream().filter(CtrlPianoEcoDett::isUoArea).filter(el->el.isNaturaFonteEsterna()));
		}
		public BigDecimal getImpNegativiAreaNaturaFonteEsterna() {
			return this.getImpNegativi(dett.stream().filter(CtrlPianoEcoDett::isUoArea).filter(el->el.isNaturaFonteEsterna()));
		}
		public BigDecimal getImpPositiviAreaNaturaFonteInterna() {
			return this.getImpPositivi(dett.stream().filter(CtrlPianoEcoDett::isUoArea).filter(el->el.isNaturaFonteInterna()));
		}
		public BigDecimal getImpNegativiAreaNaturaFonteInterna() {
			return this.getImpNegativi(dett.stream().filter(CtrlPianoEcoDett::isUoArea).filter(el->el.isNaturaFonteInterna()));
		}
		public BigDecimal getImpPositiviCdrPersonale() {
			return this.getImpPositivi(dett.stream().filter(CtrlPianoEcoDett::isCdrPersonale));
		}
		public BigDecimal getImpNegativiCdrPersonale() {
			return this.getImpNegativi(dett.stream().filter(CtrlPianoEcoDett::isCdrPersonale));
		}
		public BigDecimal getImpPositiviNaturaReimpiego() {
			return this.getImpPositivi(dett.stream().filter(CtrlPianoEcoDett::isNaturaReimpiego));
		}
		public BigDecimal getImpNegativiNaturaReimpiego() {
			return this.getImpNegativi(dett.stream().filter(CtrlPianoEcoDett::isNaturaReimpiego));
		}
		public BigDecimal getImpPositiviVoceSpeciale() {
			return this.getImpPositivi(dett.stream().filter(CtrlPianoEcoDett::isVoceSpeciale));
		}
		public BigDecimal getImpNegativiVoceSpeciale() {
			return this.getImpNegativi(dett.stream().filter(CtrlPianoEcoDett::isVoceSpeciale));
		}
		public Timestamp getDtScadenza() {
			return Optional.ofNullable(
					Optional.ofNullable(progetto.getOtherField().getDtProroga()).orElse(progetto.getOtherField().getDtFine()))
					.orElse(null);
		}
		public boolean isScaduto(Timestamp dataRiferimento) {
			return Optional.ofNullable(this.getDtScadenza()).map(dt->dt.before(dataRiferimento)).orElse(Boolean.FALSE);
		}
		private BigDecimal getImpPositivi(Stream<CtrlPianoEcoDett> stream){
			return stream.filter(el->el.getImporto().compareTo(BigDecimal.ZERO)>0)
					.map(CtrlPianoEcoDett::getImporto).reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
		}
		private BigDecimal getImpNegativi(Stream<CtrlPianoEcoDett> stream){
			return stream.filter(el->el.getImporto().compareTo(BigDecimal.ZERO)<0)
					.map(CtrlPianoEcoDett::getImporto).reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO).abs();
		}
	}


//@@>> setComponentContext

/** 
  *  aggiornamento importo relativo a mandati e reversali
  *    PreCondition:
  *      E' stata cancellato un mandato o creata/cancellata una reversale 
  *    PostCondition:
  *      Viene aggiornato l'importo associato a mandati e reversali della voce del piano (di competenza o residuo) interessata dal mandato o
  *      dalla reversale senza eseguire il controllo di disponibilità di cassa
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param cd_cds il codice del Cds per cui aggiornare i saldi  
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
  * @param ti_competenza_residuo identifica il tipo di voce (di competenza o residuo) da aggiornare
  
*/
public Voce_f_saldi_cmpBulk aggiornaMandatiReversali(UserContext userContext, Voce_fBulk voce, String cd_cds, BigDecimal importo, String ti_competenza_residuo  ) throws ComponentException
{
	return aggiornaMandatiReversali( userContext, voce, cd_cds, importo, ti_competenza_residuo, false );
}	
/** 
  *  creazione mandato
  *    PreCondition:
  *      E' stata creato un nuovo mandato e viene superato il controllo di
  *      di disponibilità di cassa (metodo checkDisponabilitaCassaMandati)
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza del mandato l'importo associato a mandati e reversali 
  *      della voce del piano (di competenza o residuo) interessata dal mandato 
  *  creazione mandato - errore
  *    PreCondition:
  *      E' stata creato un nuovo mandato e non viene superato il controllo di
  *      di disponibilità di cassa (metodo checkDisponabilitaCassaMandati)
  *    PostCondition:
  *      Viene segnalato con un errore l'impossibilità di emettere il mandato
  *  annullamento mandato
  *    PreCondition:
  *      E' stata annullato un mandato 
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza del mandato l'importo associato a mandati e reversali 
  *      della voce del piano (di competenza o residuo) interessata dal mandato
  *  creazione/annullamento reversale
  *    PreCondition:
  *      E' stata creata una nuova reversale o e' stata annullata una reversale già emessa 
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza del mandato l'importo associato a mandati e reversali 
  *      della voce del piano (di competenza o residuo) interessata dalla reversale
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param cd_cds il codice del Cds per cui aggiornare i saldi  
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
  * @param ti_competenza_residuo identifica il tipo di voce (di competenza o residuo) da aggiornare
  * @param checkDisponibilitaCassa  valore booleano che indica se eseguire la verifica della disponibilità di cassa sulla
  *        voce del piano
*/

public Voce_f_saldi_cmpBulk aggiornaMandatiReversali(UserContext userContext, Voce_fBulk voce, String cd_cds, BigDecimal importo, String ti_competenza_residuo, boolean checkDisponibilitaCassa  ) throws ComponentException
{
	try
	{
		Voce_f_saldi_cmpBulk saldo;
		if ( checkDisponibilitaCassa)
			saldo = checkDisponabilitaCassaMandati( userContext, voce, cd_cds, importo,ti_competenza_residuo );
		else
			saldo = findAndLock( userContext, cd_cds, voce, ti_competenza_residuo );
        if (saldo != null){
			saldo.setIm_mandati_reversali( saldo.getIm_mandati_reversali().add( importo.setScale(2, importo.ROUND_HALF_UP) ));
			saldo.setUser( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser());
			updateBulk( userContext, saldo );
		}		
		return saldo;
	}
	catch 	(Exception e )
	{
		throw handleException(  e );
	}	
}	
/** 
  *  aggiornamento importo relativo a obbligazioni/accertamento
  *    PreCondition:
  *      E' stata creato/modificato importo/cancellato un accertamento oppure e' stata
  *      cancellata un'obbligazione o e' stato diminuito l'importo del dettaglio di una scadenza dell'obbligazione
  *    PostCondition:
  *      Viene aggiornato l'importo associato a obbligazioni/accertamenti della voce del piano (di competenza o residuo) 
  *      interessata dall'accertamento o dai dettagli delle scadenze dell'obbligazione
  *      senza eseguire il controllo di disponibilità di cassa
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param cd_cds il codice del Cds per cui aggiornare i saldi  
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
  * @param ti_competenza_residuo identifica il tipo di voce (di competenza o residuo) da aggiornare
*/

public Voce_f_saldi_cmpBulk aggiornaObbligazioniAccertamenti(UserContext userContext, Voce_fBulk voce, String cd_cds, BigDecimal importo, String ti_competenza_residuo ) throws ComponentException
{
	return aggiornaObbligazioniAccertamenti( userContext, voce, cd_cds, importo, ti_competenza_residuo, false );
}	
/** 
  *  creazione obbligazione/modifica importo obbligazione
  *    PreCondition:
  *      E' stato creato un nuovo dettaglio di scadenza di obbligazione o ne e' stato incrementato l'importo
  *      e viene superato il controllo di
  *      di disponibilità di cassa (metodo checkDisponabilitaCassaObbligazione)
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza dell'obbligazione l'importo associato a obbligazione e accertamenti 
  *      della voce del piano (di competenza o residuo) interessata dal dettaglio della scadenza di obbligazione
  *  creazione obbligazione/modifica importo obbligazione - errore
  *    PreCondition:
  *      E' stato creato un nuovo dettaglio di scadenza di obbligazione o ne e' stato incrementato l'importo
  *      e non viene superato il controllo di
  *      di disponibilità di cassa (metodo checkDisponabilitaCassaObbligazione)
  *    PostCondition:
  *      Viene segnalato con un errore l'impossibilità di creare/aggiornare l'obbligazione
  *  eliminazione obbligazione
  *    PreCondition:
  *      E' stata eliminato un dettaglio di scadenza di obbligazione
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza dell'obbligazione l'importo associato a obbligazione e accertamenti 
  *      della voce del piano (di competenza o residuo) interessata dal dettaglio della scadenza di obbligazione  
  *  creazione/modifica/eliminazione accertamento
  *    PreCondition:
  *      E' stata creato un nuovo accertamento oppure e' stato modificato l'importo di un accertamento oppure e' stato
  *      cancellato un accertamento
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza dell'accertamento l'importo associato a obbligazione e accertamenti 
  *      della voce del piano (di competenza o residuo) interessata dall'accertamento
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param cd_cds il codice del Cds per cui aggiornare i saldi  
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
  * @param ti_competenza_residuo identifica il tipo di voce (di competenza o residuo) da aggiornare
  * @param checkDisponibilitaCassa  valore booleano che indica se eseguire la verifica della disponibilità di cassa sulla
  *        voce del piano
  * 
*/

public Voce_f_saldi_cmpBulk aggiornaObbligazioniAccertamenti(UserContext userContext, Voce_fBulk voce, String cd_cds, BigDecimal importo, String ti_competenza_residuo, boolean checkDisponibilitaCassa  ) throws ComponentException
{
	try
	{
		Voce_f_saldi_cmpBulk saldo;
		if ( checkDisponibilitaCassa )
			saldo = checkDisponabilitaCassaObbligazioni( userContext, voce, cd_cds, importo, ti_competenza_residuo );
		else
			saldo = findAndLock( userContext, cd_cds, voce, ti_competenza_residuo );
		if (saldo != null){			
			importo = importo.setScale(2, importo.ROUND_HALF_UP);
			saldo.setIm_obblig_imp_acr( saldo.getIm_obblig_imp_acr().add( importo) );
			saldo.setUser( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser());
	
			//se si tratta di un residuo devo aggiornare anche le variazioni più o meno
			if ( ti_competenza_residuo.equals( saldo.TIPO_RESIDUO ))
				if ( importo.compareTo( new BigDecimal(0)) > 0 )
					saldo.setVariazioni_piu( saldo.getVariazioni_piu().add( importo));
				else
					saldo.setVariazioni_meno( saldo.getVariazioni_meno().subtract( importo));
					
			updateBulk( userContext, saldo );
		}			
		importo = importo.setScale(2, importo.ROUND_HALF_UP);
		return saldo;
	}
	catch 	(Exception e )
	{
		throw handleException(  e );
	}	

}	
/** 
  *  riscontro mandato
  *    PreCondition:
  *      E' stata riscontrato un mandato 
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza del mandato l'importo associato a pagamenti/incassi
  *      della voce del piano (di competenza o residuo) interessata dal mandato 
  *  riscontro reversale
  *    PreCondition:
  *      E' stata riscontrata una reversale
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza della reversale l'importo associato a pagamenti/incassi
  *      della voce del piano (di competenza o residuo) interessata dalla reversale
  *  annullamento riscontro mandato
  *    PreCondition:
  *      E' stata annullato il riscontro di un mandato 
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza del mandato l'importo associato a pagamenti/incassi
  *      della voce del piano (di competenza o residuo) interessata dal mandato 
  *  annullamento riscontro reversale
  *    PreCondition:
  *      E' stato annullato il riscontro di una reversale
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza della reversale l'importo associato a pagamenti/incassi
  *      della voce del piano (di competenza o residuo) interessata dalla reversale
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param cd_cds il codice del Cds per cui aggiornare i saldi  
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
  * @param ti_competenza_residuo identifica il tipo di voce (di competenza o residuo) da aggiornare
*/

public Voce_f_saldi_cmpBulk aggiornaPagamentiIncassi(UserContext userContext, Voce_fBulk voce, String cd_cds, BigDecimal importo, String ti_competenza_residuo ) throws ComponentException
{
	try
	{
		Voce_f_saldi_cmpBulk saldo = findAndLock( userContext, cd_cds, voce, ti_competenza_residuo );
		if (saldo != null){
			saldo.setIm_pagamenti_incassi( saldo.getIm_pagamenti_incassi().add(importo.setScale(2, importo.ROUND_HALF_UP) ));
			saldo.setUser( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser());
			updateBulk( userContext, saldo );
		}			
		return saldo;
	}
	catch 	(Exception e )
	{
		throw handleException(  e );
	}	

}	
/** 
  *  verifica disponibilità di cassa - errore
  *    PreCondition:
  *      La somma dello stanziamento iniziale della voce del piano di competenza o residuo + variazioni in positivo 
  *      - variazione in negativo - importo 
  *      dei mandati già emessi e' inferiore all'importo del mandato che l'utente vuole emettere
  *    PostCondition:
  *      Una segnalazione di errore comunica il problema all'utente e non consente il salvataggio del mandato
  *  verifica disponibilità di cassa - ok
  *    PreCondition:
  *      La somma dello stanziamento iniziale della voce del piano di competenza o residuo + variazioni in positivo 
  *      - variazione in negativo - importo 
  *      dei mandati già emessi e' superiore o uguale all'importo del mandato che l'utente vuole emettere  
  *    PostCondition:
  *      Il mandato supera la validazione di Cassa ed e' pertanto possibile proseguire con il suo salvataggio
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui effettuare la verifica di disponibilità di cassa
  * @param cd_cds il codice del Cds per cui effettuare la verifica di disponibilità di cassa
  * @param importo l'importo (positivo o negativo) per cui effettuare la verifica di disponibilità di cassa
  * @param ti_competenza_residuo identifica il tipo di voce (di competenza o residuo) per cui effettuare la verifica di disponibilità di cassa
*/
public Voce_f_saldi_cmpBulk checkDisponabilitaCassaMandati(UserContext userContext, Voce_fBulk voce, String cd_cds, BigDecimal importo, String ti_competenza_residuo ) throws ComponentException
{
	try
	{
		Voce_f_saldi_cmpBulk saldo = findAndLock( userContext, cd_cds, voce, ti_competenza_residuo );
		/*if (saldo != null){		
			// check per capitoli di competenza
			if ( saldo.getTi_competenza_residuo().equals( MandatoBulk.TIPO_COMPETENZA ))
			{	
				if ( saldo.getIm_stanz_iniziale_a1().add( 
						saldo.getVariazioni_piu()).subtract(
							saldo.getVariazioni_meno()).subtract(
								saldo.getIm_mandati_reversali()).subtract( importo ).compareTo( new BigDecimal(0)) < 0 )
					throw handleException( new ApplicationException("La disponibilità di cassa relativa all'assunzione di mandati è stata superata per CDS: " + cd_cds + " voce: " + voce.getCd_voce() + " - Competenza"));
			}	
			else
			// check per capitoli a residuo
			{
				if ( saldo.getIm_obblig_imp_acr().subtract(
	 				  	saldo.getIm_mandati_reversali()).subtract( importo ).compareTo( new BigDecimal(0)) < 0 )
					throw handleException( new ApplicationException("La disponibilità di cassa relativa all'assunzione di mandati è stata superata per CDS: " + cd_cds + " voce: " + voce.getCd_voce() + " - Residuo" ));
				
			}
		}*/
		return saldo;
	}
	catch 	(Exception e )
	{
		throw handleException(  e );
	}
}	
/** 
  *  verifica disponibilità di cassa - errore
  *    PreCondition:
  *      La somma dello stanziamento iniziale della voce del piano di competenza + variazioni in positivo 
  *      - variazione in negativo - importo 
  *      delle obbligazioni già emesse e' inferiore all'importo dell'obbligazione che l'utente vuole emettere
  *    PostCondition:
  *      Una segnalazione di errore comunica il problema all'utente, lasciondogli comunque la possibilità di forzare questo
  *      controllo e di salvare l'obbligazione
  *  verifica disponibilità di cassa - ok
  *    PreCondition:
  *      La somma dello stanziamento iniziale della voce del piano di competenza + variazioni in positivo 
  *      - variazione in negativo - importo 
  *      delle obbligazioni già emesse e' superiore o uguale all'importo dell'obbligazione che l'utente vuole emettere
  *    PostCondition:
  *      L'obbligazione supera la validazione di Cassa ed e' pertanto possibile proseguire con il suo salvataggio
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui effettuare la verifica di disponibilità di cassa
  * @param cd_cds il codice del Cds per cui effettuare la verifica di disponibilità di cassa
  * @param importo l'importo (positivo o negativo) per cui effettuare la verifica di disponibilità di cassa
  *  
*/

public Voce_f_saldi_cmpBulk checkDisponabilitaCassaObbligazioni(UserContext userContext, Voce_fBulk voce, String cd_cds, BigDecimal importo, String ti_competenza_residuo ) throws ComponentException
{
	try
	{
		Voce_f_saldi_cmpBulk saldo = find( userContext, cd_cds, voce, ti_competenza_residuo );
		/**
		 * @author mspasiano
		 * @since 03.01.2006
		 * @see remmato controllo di cassa
		 */
		/*if ( saldo.getIm_stanz_iniziale_a1().add( 
				saldo.getVariazioni_piu()).subtract(
					saldo.getVariazioni_meno()).subtract(
						saldo.getIm_obblig_imp_acr()).subtract( importo ).compareTo( new BigDecimal(0)) < 0 )
			throw handleException( new CheckDisponibilitaCassaFailed("L'importo dei dettagli inseriti supera la disponiblità di cassa relativa al capitolo e al CdS."));
		*/	 
		return saldo;
	}
	catch 	(Exception e )
	{
		throw handleException(  e );
	}	
}	
/** 
  *  ricerca e inserisce un lock
  *    PreCondition:
  *      E' necessario apportare delle modifiche ai saldi di una voce del piano dei conti per un cds
  *    PostCondition:
  *      La voce del piano dei conti di tipo competenza o residuo per il cds specificato
  *      viene ricercata e viene inserito un lock
  *  errore - non trovato
  *    PreCondition:
  *      E' necessario apportare delle modifiche ai saldi di una voce del piano dei conti ma questa voce non e'
  *      presente nella tabella dei saldi
  *    PostCondition:
  *      Una segnalazione di errore comunica all'utente che la voce non e' presente nei saldi
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param cd_cds il codice del Cds per cui effettuare la ricerca del saldo
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui effettuare la ricerca del saldo
  * @param ti_competenza_residuo identifica il tipo di saldo (di competenza o residuo) per cui effettuare la ricerca
  *   
*/

private it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk find(UserContext userContext, String cd_cds, Voce_fBulk voce, String ti_competenza_residuo ) throws PersistencyException, ComponentException
{
	try
	{
		return (Voce_f_saldi_cmpBulk) getHome( userContext,Voce_f_saldi_cmpBulk.class ).findByPrimaryKey( new Voce_f_saldi_cmpBulk( cd_cds, voce.getCd_voce(), voce.getEsercizio(), voce.getTi_appartenenza(), ti_competenza_residuo, voce.getTi_gestione()));
	}
	catch ( it.cnr.jada.persistency.ObjectNotFoundException e )
	{
		if (!((Parametri_cnrBulk)getHome(userContext,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)))).getFl_regolamento_2006().booleanValue())
			throw handleException( new ApplicationException("Non e' presente il saldo per Esercizio: " + voce.getEsercizio() +
															" CDS: " + cd_cds + " Voce: " + voce.getCd_voce() ));
		else
			return null;								                    
	}	
	catch ( Exception e )
	{
		throw handleException( e );
	}

}	
/** 
  *  ricerca e inserisce un lock
  *    PreCondition:
  *      E' necessario apportare delle modifiche ai saldi di una voce del piano dei conti per un cds
  *    PostCondition:
  *      La voce del piano dei conti di tipo competenza o residuo per il cds specificato
  *      viene ricercata e viene inserito un lock
  *  errore - non trovato
  *    PreCondition:
  *      E' necessario apportare delle modifiche ai saldi di una voce del piano dei conti ma questa voce non e'
  *      presente nella tabella dei saldi
  *    PostCondition:
  *      Una segnalazione di errore comunica all'utente che la voce non e' presente nei saldi
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param cd_cds il codice del Cds per cui effettuare la ricerca del saldo
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui effettuare la ricerca del saldo
  * @param ti_competenza_residuo identifica il tipo di saldo (di competenza o residuo) per cui effettuare la ricerca
*/

private it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk findAndLock(UserContext userContext, String cd_cds, Voce_fBulk voce, String ti_competenza_residuo ) throws PersistencyException, ComponentException
{
	try
	{
		return (Voce_f_saldi_cmpBulk) getHome( userContext,Voce_f_saldi_cmpBulk.class ).findAndLock( new Voce_f_saldi_cmpBulk( cd_cds, voce.getCd_voce(), voce.getEsercizio(), voce.getTi_appartenenza(), ti_competenza_residuo, voce.getTi_gestione()));
	}
	catch ( it.cnr.jada.persistency.ObjectNotFoundException e )
	{
		if (!((Parametri_cnrBulk)getHome(userContext,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)))).getFl_regolamento_2006().booleanValue())
		    throw handleException( new ApplicationException("Non e' presente il saldo per Esercizio: " + voce.getEsercizio() +
										                    " CDS: " + cd_cds + " Voce: " + voce.getCd_voce() ));
		else
		    return null;								                    
	}	
	catch ( Exception e )
	{
		throw handleException( e );
	}

}	
/** 
  *  ricerca
  *    PreCondition:
  *      E' necessario apportare delle modifiche ai saldi di una voce del piano dei conti per un cdr/linea
  *    PostCondition:
  *      La voce del piano dei conti di tipo competenza o residuo per il cdr/linea specificato
  *      viene ricercata e viene inserito un lock
  *  errore - non trovato
  *    PreCondition:
  *      E' necessario apportare delle modifiche ai saldi di una voce del piano dei conti ma questa voce non e'
  *      presente nella tabella dei saldi
  *    PostCondition:
  *      Una segnalazione di errore comunica all'utente che la voce non e' presente nei saldi
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param cd_cdr il codice del CDR per cui effettuare la ricerca del saldo
  * @param cd_linea il codice del Workpackage per cui effettuare la ricerca del saldo  
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui effettuare la ricerca del saldo
  *   
*/
private it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk find(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce, Integer esercizio_res) throws ComponentException
{
	try
	{
		return (Voce_f_saldi_cdr_lineaBulk) getHome( userContext,Voce_f_saldi_cdr_lineaBulk.class ).findByPrimaryKey( new Voce_f_saldi_cdr_lineaBulk( voce.getEsercizio(), esercizio_res, cd_cdr, cd_linea_attivita, voce.getTi_appartenenza(), voce.getTi_gestione(),voce.getCd_voce()));
	}
	catch ( it.cnr.jada.persistency.ObjectNotFoundException e )
	{
		return null;
	}	
	catch ( Exception e )
	{
		throw handleException( e );
	}

}
private it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk find(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce) throws ComponentException
{
	return find(userContext,cd_cdr,cd_linea_attivita,voce,voce.getEsercizio());
}	
/** 
  *  ricerca e inserisce un lock
  *    PreCondition:
  *      E' necessario apportare delle modifiche ai saldi di una voce del piano dei conti per un cdr/linea
  *    PostCondition:
  *      La voce del piano dei conti di tipo competenza o residuo per il cdr/linea specificato
  *      viene ricercata e viene inserito un lock
  *  errore - non trovato
  *    PreCondition:
  *      E' necessario apportare delle modifiche ai saldi di una voce del piano dei conti ma questa voce non e'
  *      presente nella tabella dei saldi
  *    PostCondition:
  *      Una segnalazione di errore comunica all'utente che la voce non e' presente nei saldi
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param cd_cdr il codice del CDR per cui effettuare la ricerca del saldo
  * @param cd_linea il codice del Workpackage per cui effettuare la ricerca del saldo  
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui effettuare la ricerca del saldo
  *   
*/
private it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk findAndLock(UserContext userContext, Integer esercizio, Integer esercizio_res, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce) throws ComponentException{
	try
	{
		return (Voce_f_saldi_cdr_lineaBulk) getHome( userContext,Voce_f_saldi_cdr_lineaBulk.class ).findAndLock( new Voce_f_saldi_cdr_lineaBulk( esercizio, esercizio_res, cd_cdr, cd_linea_attivita, voce.getTi_appartenenza(), voce.getTi_gestione(),voce.getCd_voce()));
	}
	catch ( it.cnr.jada.persistency.ObjectNotFoundException e )
	{
		return null;
	}	
	catch ( Exception e )
	{
		throw handleException( e );
	}
	
}

private it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk findAndLock(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce) throws ComponentException
{
	return findAndLock(userContext,voce.getEsercizio(),voce.getEsercizio(), cd_cdr,cd_linea_attivita,voce);
}
/** 
  *  aggiornamento importo relativo a mandati e reversali
  *    PreCondition:
  *      E' stata cancellato un mandato o creata/cancellata una reversale 
  *    PostCondition:
  *      Viene aggiornato l'importo associato a mandati e reversali della voce del piano di competenza o residuo interessata dal mandato o
  *      dalla reversale senza eseguire il controllo di disponibilità di cassa
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param cd_cdr il codice del CDR per cui effettuare la ricerca del saldo
  * @param cd_linea il codice del Workpackage per cui effettuare la ricerca del saldo
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param esercizio_res l'anno del residuo
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
  *  
*/
public Voce_f_saldi_cdr_lineaBulk aggiornaMandatiReversali(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce, Integer esercizio_res, BigDecimal importo, String tipo_residuo) throws ComponentException
{
	return aggiornaMandatiReversali( userContext, cd_cdr, cd_linea_attivita, voce, esercizio_res, importo, tipo_residuo, false );
}	
/** 
  *  creazione mandato
  *    PreCondition:
  *      E' stata creato un nuovo mandato e viene superato il controllo di
  *      di disponibilità di cassa (metodo checkDisponabilitaCassaMandati)
  *    PostCondition:
  *      Viene aggiornato per cdr/linea di appartenenza del mandato l'importo associato a mandati e reversali 
  *      della voce del piano (di competenza o residuo) interessata dal mandato 
  *  creazione mandato - errore
  *    PreCondition:
  *      E' stata creato un nuovo mandato e non viene superato il controllo di
  *      di disponibilità di cassa (metodo checkDisponabilitaCassaMandati)
  *    PostCondition:
  *      Viene segnalato con un errore l'impossibilità di emettere il mandato
  *  annullamento mandato
  *    PreCondition:
  *      E' stata annullato un mandato 
  *    PostCondition:
  *      Viene aggiornato per la coppia cdr/linea di appartenenza delle righe del mandato l'importo associato a mandati e reversali 
  *      della voce del piano (di competenza o residuo) interessata dal mandato
  *
  *  creazione/annullamento reversale
  *    PreCondition:
  *      E' stata creata una nuova reversale o e' stata annullata una reversale già emessa 
  *    PostCondition:
  *      Viene aggiornato per la coppia cdr/linea di appartenenza delle righe della reversale l'importo associato a mandati e reversali 
  *      della voce del piano (di competenza o residuo) interessata dalla reversale
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param cd_cdr il codice del CDR per cui effettuare la ricerca del saldo
  * @param cd_linea il codice del Workpackage per cui effettuare la ricerca del saldo
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param esercizio_res l'anno del residuo
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
  * @param checkDisponibilitaCassa  valore booleano che indica se eseguire la verifica della disponibilità di cassa sulla
  *        voce del piano
*/

public Voce_f_saldi_cdr_lineaBulk aggiornaMandatiReversali(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce, Integer esercizio_res, BigDecimal importo, String tipo_residuo, boolean checkDisponibilitaCassa  ) throws ComponentException
{
	try
	{
		Voce_f_saldi_cdr_lineaBulk saldo, saldoCassa;
		if ( checkDisponibilitaCassa)
			//Eseguo il controllo sempre sulla competenza
			saldoCassa = checkDisponabilitaCassaMandati( userContext, cd_cdr, cd_linea_attivita, voce, importo );		

		if (voce.getEsercizio().compareTo(esercizio_res) != 0)		
			saldo = findAndLock( userContext, voce.getEsercizio(), esercizio_res, cd_cdr, cd_linea_attivita, voce);
		else
			saldo = findAndLock( userContext, cd_cdr, cd_linea_attivita, voce);
		if (saldo == null && 
		    ((Parametri_cnrBulk)getHome(userContext,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)))).getFl_regolamento_2006().booleanValue()) {
			throw handleException( new ApplicationException("Non e' presente il saldo per Esercizio: " + voce.getEsercizio() +
													   " CDR: " + cd_cdr + " GAE: " + cd_linea_attivita + " Voce: " + voce.getCd_voce() ));
		}
		if (saldo != null){
			if (tipo_residuo.equals(Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO))
			  saldo.setIm_mandati_reversali_imp( saldo.getIm_mandati_reversali_imp().add( importo.setScale(2, importo.ROUND_HALF_UP) ));
			else if (tipo_residuo.equals(Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO)||tipo_residuo.equals(Voce_f_saldi_cdr_lineaBulk.TIPO_COMPETENZA))
			  saldo.setIm_mandati_reversali_pro( saldo.getIm_mandati_reversali_pro().add( importo.setScale(2, importo.ROUND_HALF_UP) ));
			saldo.setUser( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser());
			saldo.setToBeUpdated();
			updateBulk( userContext, saldo );
		}		
		return saldo;
	}
	catch 	(Exception e )
	{
		throw handleException(  e );
	}	
}
/** 
  *  verifica disponibilità di cassa - errore
  *    PreCondition:
  *      La somma dello stanziamento iniziale della voce del piano di competenza o residuo + variazioni in positivo 
  *      - variazione in negativo - importo 
  *      dei mandati già emessi e' inferiore all'importo del mandato che l'utente vuole emettere
  *    PostCondition:
  *      Una segnalazione di errore comunica il problema all'utente e non consente il salvataggio del mandato
  *  verifica disponibilità di cassa - ok
  *    PreCondition:
  *      La somma dello stanziamento iniziale della voce del piano di competenza o residuo + variazioni in positivo 
  *      - variazione in negativo - importo 
  *      dei mandati già emessi e' superiore o uguale all'importo del mandato che l'utente vuole emettere  
  *    PostCondition:
  *      Il mandato supera la validazione di Cassa ed e' pertanto possibile proseguire con il suo salvataggio
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui effettuare la verifica di disponibilità di cassa
  * @param cd_cds il codice del Cds per cui effettuare la verifica di disponibilità di cassa
  * @param importo l'importo (positivo o negativo) per cui effettuare la verifica di disponibilità di cassa
  * @param ti_competenza_residuo identifica il tipo di voce (di competenza o residuo) per cui effettuare la verifica di disponibilità di cassa
*/
public Voce_f_saldi_cdr_lineaBulk checkDisponabilitaCassaMandati(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce, BigDecimal importo ) throws ComponentException
{
	try
	{
		Voce_f_saldi_cdr_lineaBulk saldo = findAndLock( userContext,cd_cdr,cd_linea_attivita, voce );
		/*if (saldo != null) { //Se la riga non viene recuperata non viene effettuato alcun aggiornamento	
			// check per capitoli di competenza
			if ( saldo.getIm_stanz_iniziale_cassa().add( 
				 saldo.getVariazioni_piu_cassa()).subtract(
				 saldo.getVariazioni_meno_cassa()).subtract(
				 saldo.getIm_mandati_reversali_pro()).subtract( importo ).compareTo( new BigDecimal(0)) < 0 )				 
				throw handleException( new ApplicationException("La disponibilità di cassa relativa all'assunzione di mandati è stata superata per CDR: " + cd_cdr +  " GAE: " + cd_linea_attivita +" voce: " + voce.getCd_voce() + " - Competenza"));
		}*/		
		return saldo;
	}
	catch 	(Exception e )
	{
		throw handleException(  e );
	}	
}
public String checkDispObbligazioniAccertamenti(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce, Integer esercizio_res, String tipo_imp, String tipo_messaggio) throws ComponentException
{
	try
	{
		Voce_f_saldi_cdr_lineaBulk saldo;
		String messaggio = null;
		String aCapo = tipo_messaggio.equals(Parametri_cdsBulk.BLOCCO_IMPEGNI_ERROR)?"\n":"<BR>";
		saldo = findAndLock( userContext,voce.getEsercizio(), esercizio_res,cd_cdr,cd_linea_attivita, voce);
		BigDecimal diff = Utility.ZERO;
		if (saldo != null) {	
		   if (tipo_imp.equals(Voce_f_saldi_cdr_lineaBulk.TIPO_COMPETENZA)){
			  diff = saldo.getAssestato().subtract(saldo.getIm_obbl_acc_comp());
		   }else if (tipo_imp.equals(Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO)){
			  diff = saldo.getDispAdImpResiduoImproprio();
		   }   	 
		   if(diff != null && diff.compareTo(Utility.ZERO) == -1){
			 messaggio = "L'importo relativo al CDR "+cd_cdr+" G.A.E. "+cd_linea_attivita+" Voce "+voce.getCd_voce()+ aCapo +
                         "supera la disponibilità di " + new it.cnr.contab.util.EuroFormat().format(diff.abs())+".";
		   }
		   //aggiungo i vincoli
		   Pdg_vincoloHome home = (Pdg_vincoloHome)getHome(userContext, Pdg_vincoloBulk.class);
		   List<Pdg_vincoloBulk> listVincoli = home.cercaDettagliVincolati(saldo);
		   BigDecimal impVincolo = listVincoli.stream().map(e->e.getIm_vincolo()).reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
		   if (impVincolo.compareTo(BigDecimal.ZERO)!=0 && diff.subtract(impVincolo).compareTo(BigDecimal.ZERO)==-1)
			   messaggio = "L'importo relativo al CDR "+cd_cdr+" G.A.E. "+cd_linea_attivita+" Voce "+voce.getCd_voce()+ aCapo +
			   "supera la disponibilità di " + new it.cnr.contab.util.EuroFormat().format(diff.subtract(impVincolo).abs())+" in conseguenza della presenza "
			   		+ "di vincoli di spesa per un importo di " + new it.cnr.contab.util.EuroFormat().format(impVincolo.abs())+".";
		}
		return messaggio;
	}
	catch 	(Exception e )
	{
		throw handleException(  e );
	}	
}
/** 
  *  aggiornamento importo relativo a obbligazioni/accertamento
  *    PreCondition:
  *      E' stata creato/modificato importo/cancellato un accertamento oppure e' stata
  *      cancellata un'obbligazione o e' stato diminuito l'importo del dettaglio di una scadenza dell'obbligazione
  *    PostCondition:
  *      Viene aggiornato l'importo associato a obbligazioni/accertamenti della voce del piano (di competenza o residuo) 
  *      interessata dall'accertamento o dai dettagli delle scadenze dell'obbligazione
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param cd_cdr il codice del CDR per cui effettuare la ricerca del saldo
  * @param cd_linea il codice del Workpackage per cui effettuare la ricerca del saldo
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param esercizio_res l'anno del residuo
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
*/

public Voce_f_saldi_cdr_lineaBulk aggiornaObbligazioniAccertamenti(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce, Integer esercizio_res, String tipo_residuo, BigDecimal importo, String tipo_doc_cont) throws ComponentException
{
	try
	{
		Voce_f_saldi_cdr_lineaBulk saldo;
		saldo = findAndLock( userContext,voce.getEsercizio(), esercizio_res, cd_cdr,cd_linea_attivita, voce);
		if (saldo == null){
   	        Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk)getHome(userContext,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(voce.getEsercizio()));
            if (parametriCnr==null || !parametriCnr.getFl_nuovo_pdg())
               voce = (Voce_fBulk)getHome(userContext,Voce_fBulk.class).findByPrimaryKey(
																  new Voce_fBulk(voce.getCd_voce(),voce.getEsercizio(),voce.getTi_appartenenza(),voce.getTi_gestione())
																  );
            Elemento_voceBulk elemento_voce = (Elemento_voceBulk)getHome(userContext,Elemento_voceBulk.class).findByPrimaryKey(
			                                                      new Elemento_voceBulk(voce.getCd_elemento_voce(),voce.getEsercizio(),voce.getTi_appartenenza(),voce.getTi_gestione())
			                                                      );
            if (elemento_voce == null)
			  throw new ApplicationException("Elemento voce non trovato per la Voce: "+ voce.getCd_voce());
			WorkpackageBulk workpackage = (WorkpackageBulk)getHome(userContext,WorkpackageBulk.class).findByPrimaryKey(
											new WorkpackageBulk(cd_cdr,cd_linea_attivita)
											);           
			
			if ((voce.getTi_gestione().equals(CostantiTi_gestione.TI_GESTIONE_SPESE) && 
			     tipo_residuo.equals(Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_PROPRIO)||
			    (elemento_voce.getFl_limite_ass_obblig()!= null && !elemento_voce.getFl_limite_ass_obblig().booleanValue()&&
			     workpackage.getFl_limite_ass_obblig() != null && !workpackage.getFl_limite_ass_obblig().booleanValue()))
			     || voce.getTi_gestione().equals(CostantiTi_gestione.TI_GESTIONE_ENTRATE)){
			    	
			    saldo = new Voce_f_saldi_cdr_lineaBulk( voce.getEsercizio(), esercizio_res, cd_cdr, cd_linea_attivita, voce.getTi_appartenenza(), voce.getTi_gestione(),voce.getCd_voce());
			    saldo.setCd_elemento_voce(elemento_voce.getCd_elemento_voce());
			    saldo.inizializzaSommeAZero();
			    saldo.setToBeCreated();
			    insertBulk(userContext, saldo);	
			}
			if (saldo == null) {
				throw handleException( new ApplicationException("Disponibilità inesistente per Esercizio: " + voce.getEsercizio() +
														   " CDR: " + cd_cdr + " G.A.E.: " + cd_linea_attivita + " Voce: " + voce.getCd_voce() ));
			}
		}
		if (saldo != null) {	
			importo = importo.setScale(2, importo.ROUND_HALF_UP);
			if (voce.getEsercizio().compareTo(esercizio_res) == 0){
			   saldo.setIm_obbl_acc_comp( saldo.getIm_obbl_acc_comp().add( importo) );
			}else{
				if (tipo_residuo.equals(Voce_f_saldi_cdr_lineaBulk.TIPO_RESIDUO_IMPROPRIO)){
					saldo.setIm_obbl_res_imp( saldo.getIm_obbl_res_imp().add( importo) );   
				}else{
					saldo.setIm_obbl_res_pro( saldo.getIm_obbl_res_pro().add( importo) );
				}
			}
			saldo.setUser( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser());
			saldo.setToBeUpdated();
			updateBulk( userContext, saldo );
		}
		/**
		 * @author mspasiano
		 * Aggiorno i saldi negli anni successivi aperti
		 */
		if ((voce.getTi_gestione().equals(CostantiTi_gestione.TI_GESTIONE_SPESE)) &&
				!(tipo_doc_cont.equalsIgnoreCase(Numerazione_doc_contBulk.TIPO_IMP) || 
				  tipo_doc_cont.equalsIgnoreCase(Numerazione_doc_contBulk.TIPO_IMP_RES)))
		   aggiornaSaldiAnniSuccessivi(userContext, cd_cdr, cd_linea_attivita, voce, esercizio_res, importo, saldo);
		return saldo;
	}
	catch 	(Exception e )
	{
		throw handleException(  e );
	}	

}
public Voce_f_saldi_cdr_lineaBulk aggiornaVariazioneStanziamento(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce, Integer esercizio_res, String tipo_residuo, BigDecimal importo) throws ComponentException
{
	return aggiornaVariazioneStanziamento(userContext, cd_cdr, cd_linea_attivita, voce, esercizio_res, tipo_residuo, importo, false);
}
public Voce_f_saldi_cdr_lineaBulk aggiornaVariazioneStanziamento(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce, Integer esercizio_res, String tipo_residuo, BigDecimal importo, Boolean sottraiImportoDaVariazioneEsistente) throws ComponentException
{
	try
	{
		Voce_f_saldi_cdr_lineaBulk saldo;
		saldo = findAndLock( userContext,voce.getEsercizio(), esercizio_res, cd_cdr,cd_linea_attivita, voce);
		if (saldo == null){
			  Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk)getHome(userContext,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(voce.getEsercizio()));
			     if (parametriCnr==null || !parametriCnr.getFl_nuovo_pdg())
			    	 voce = (Voce_fBulk)getHome(userContext,Voce_fBulk.class).findByPrimaryKey(
																  new Voce_fBulk(voce.getCd_voce(),voce.getEsercizio(),voce.getTi_appartenenza(),voce.getTi_gestione())
																  );
			Elemento_voceBulk elemento_voce = (Elemento_voceBulk)getHome(userContext,Elemento_voceBulk.class).findByPrimaryKey(
																  new Elemento_voceBulk(voce.getCd_elemento_voce(),voce.getEsercizio(),voce.getTi_appartenenza(),voce.getTi_gestione())
																  );
			if (elemento_voce == null)
			  throw new ApplicationException("Elemento voce non trovato per la Voce: "+ voce.getCd_voce());
					    	
			saldo = new Voce_f_saldi_cdr_lineaBulk( voce.getEsercizio(), esercizio_res, cd_cdr, cd_linea_attivita, voce.getTi_appartenenza(), voce.getTi_gestione(),voce.getCd_voce());
			saldo.setCd_elemento_voce(elemento_voce.getCd_elemento_voce());
			saldo.inizializzaSommeAZero();
			saldo.setToBeCreated();
			insertBulk(userContext, saldo);	
		}
		importo = importo.setScale(2, importo.ROUND_HALF_UP);
		if (sottraiImportoDaVariazioneEsistente){
			if(importo.compareTo(Utility.ZERO)==1)
				  saldo.setVariazioni_meno(saldo.getVariazioni_meno().subtract(importo.abs()));
				else  
				  saldo.setVariazioni_piu(saldo.getVariazioni_piu().subtract(importo));
		} else {
			if(importo.compareTo(Utility.ZERO)==1)
				  saldo.setVariazioni_piu(saldo.getVariazioni_piu().add(importo));
				else  
				  saldo.setVariazioni_meno(saldo.getVariazioni_meno().add(importo.abs()));
		}
		  
		saldo.setUser( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser());
		saldo.setToBeUpdated();
		updateBulk( userContext, saldo );
		
		/**
		 * @author mspasiano
		 * Aggiorno i saldi negli anni successivi aperti
		 */
		if ((voce.getTi_gestione().equals(CostantiTi_gestione.TI_GESTIONE_SPESE)))
			aggiornaSaldiAnniSuccessivi(userContext, cd_cdr, cd_linea_attivita, voce, esercizio_res, importo.negate(), saldo);
		return saldo;
	}
	catch 	(Exception e )
	{
		throw handleException(  e );
	}	

}
public String getMessaggioSfondamentoDisponibilita(UserContext userContext, Voce_f_saldi_cdr_lineaBulk saldo) throws ComponentException
{
	try {
		//Voce_f_saldi_cdr_lineaBulk saldoNew = findAndLock( userContext,saldo.getEsercizio(),saldo.getEsercizio_res(), saldo.getCd_centro_responsabilita(),saldo.getCd_linea_attivita(), saldo.getVoce());
		Voce_f_saldi_cdr_lineaBulk saldoNew = (Voce_f_saldi_cdr_lineaBulk) getHome( userContext,Voce_f_saldi_cdr_lineaBulk.class ).findAndLock( saldo);
		if (saldoNew == null) 
			return "";
	
		//Voce_fBulk voce = (Voce_fBulk)getHome(userContext,Voce_fBulk.class).findByPrimaryKey(new Voce_fBulk(saldoNew.getCd_voce(), saldoNew.getEsercizio(), saldoNew.getTi_appartenenza(), saldoNew.getTi_gestione()));                	
		Elemento_voceBulk elemento_voce = (Elemento_voceBulk)getHome(userContext,Elemento_voceBulk.class).findByPrimaryKey(
															  new Elemento_voceBulk(saldoNew.getCd_elemento_voce(),saldo.getEsercizio(),saldo.getTi_appartenenza(),saldo.getTi_gestione()));
		WorkpackageBulk workpackage = (WorkpackageBulk)getHome(userContext,WorkpackageBulk.class).findByPrimaryKey(
										new WorkpackageBulk(saldo.getCd_centro_responsabilita(),saldo.getCd_linea_attivita()));           
															  
		getHomeCache(userContext).fetchAll(userContext);																		  
	
		if (!((elemento_voce.getFl_partita_giro() != null && 
			   elemento_voce.getFl_partita_giro().booleanValue()) ||
			  (elemento_voce.getFl_limite_ass_obblig()!= null && !elemento_voce.getFl_limite_ass_obblig().booleanValue() &&
		       workpackage.getFl_limite_ass_obblig()!= null && !workpackage.getFl_limite_ass_obblig().booleanValue()))) {
			if (saldoNew.getEsercizio().compareTo(saldoNew.getEsercizio_res())!=0 &&
				saldoNew.getDispAdImpResiduoImproprio().compareTo(Utility.ZERO) < 0)
				return "Impossibile effettuare l'operazione !\n"+
	                   "Nell'esercizio "+saldo.getEsercizio()+
	                   " e per il CdR "+saldo.getCd_centro_responsabilita()+", "+
	                   " Voce "+saldoNew.getCd_voce()+
	                   " e GAE "+saldo.getCd_linea_attivita()+" lo stanziamento Residuo Improprio "+
	                   " diventerebbe negativo ("+new it.cnr.contab.util.EuroFormat().format(saldoNew.getDispAdImpResiduoImproprio().abs())+")";
			if (saldoNew.getEsercizio().compareTo(saldoNew.getEsercizio_res())==0 &&
				saldoNew.getDispAdImpCompetenza().compareTo(Utility.ZERO) < 0)
				return "Impossibile effettuare l'operazione !\n"+
	                   "Nell'esercizio "+saldo.getEsercizio()+
	                   " e per il CdR "+saldo.getCd_centro_responsabilita()+", "+
	                   " Voce "+saldoNew.getCd_voce()+
	                   " e GAE "+saldo.getCd_linea_attivita()+" lo stanziamento di Competenza "+
	                   " diventerebbe negativo ("+new it.cnr.contab.util.EuroFormat().format(saldoNew.getDispAdImpCompetenza().abs())+")";
		}
		return "";
	} catch ( it.cnr.jada.persistency.ObjectNotFoundException e ) {
		return "Impossibile effettuare l'operazione! Non è stata trovata la riga di saldo da aggiornare!";
	} catch (PersistencyException e) {
		throw new ComponentException(e);
	} catch ( Exception e ) {
		throw handleException( e );
	}
}
public void aggiornaSaldiAnniSuccessivi(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce, Integer esercizio_res, BigDecimal importo, Voce_f_saldi_cdr_lineaBulk saldoOld) throws ComponentException
{
	Voce_f_saldi_cdr_lineaBulk saldoNew;
		try {
			Ass_evold_evnewHome ass_evold_evnewHome = (Ass_evold_evnewHome) getHome( userContext, Ass_evold_evnewBulk.class);
			CdrHome cdrHome = (CdrHome)getHome(userContext,CdrBulk.class);
			CdrBulk cdr = (CdrBulk)cdrHome.findByPrimaryKey(new CdrBulk(cd_cdr));
			getHomeCache(userContext).fetchAll(userContext,cdrHome);
			if (((Parametri_cdsHome)getHome(userContext,Parametri_cdsBulk.class)).isRibaltato(userContext,cdr.getCd_cds())){
				//RECUPERO L'ELEMENTO DELL'ANNO IN CORSO
				for (Iterator esercizi = ((EsercizioHome)getHome(userContext,EsercizioBulk.class)).findEserciziSuccessivi(new EsercizioBulk(CNRUserContext.getCd_cds(userContext),CNRUserContext.getEsercizio(userContext))).iterator();esercizi.hasNext();){
					EsercizioBulk esercizio = (EsercizioBulk)esercizi.next();
					
					String codiceVoce = voce.getCd_voce(), codiceVoceForSaldoNew = null;
					//recupero la voce di ribaltamento
					Elemento_voceBulk elemento_voce = null;
					if (voce instanceof Voce_fBulk) {
						if (!((Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class)).isNuovoPdg(esercizio.getEsercizio())) {
							voce = (Voce_fBulk)getHome(userContext,Voce_fBulk.class).findByPrimaryKey(
									  new Voce_fBulk(voce.getCd_voce(),esercizio.getEsercizio(),saldoOld.getTi_appartenenza(),saldoOld.getTi_gestione())
									  );
							
							getHomeCache(userContext).fetchAll(userContext);

							if (voce == null)
								throw new ApplicationException("La voce: "+ codiceVoce +" non è presente nell'esercizio: "+esercizio.getEsercizio());
							
							saldoNew = findAndLock( userContext,esercizio.getEsercizio(), esercizio_res, cd_cdr,cd_linea_attivita, voce);
							codiceVoceForSaldoNew = voce.getCd_voce();
							
							elemento_voce = (Elemento_voceBulk)getHome(userContext,Elemento_voceBulk.class).findByPrimaryKey(
									  new Elemento_voceBulk(voce.getCd_elemento_voce(),esercizio.getEsercizio(),voce.getTi_appartenenza(),voce.getTi_gestione())
									  );
						} else {
							//recupero la voce di ribaltamento
//							Voce_fBulk voceOld = (Voce_fBulk)getHome(userContext,Voce_fBulk.class).findByPrimaryKey(
//									  new Voce_fBulk(voce.getCd_voce(),CNRUserContext.getEsercizio(userContext),saldoOld.getTi_appartenenza(),saldoOld.getTi_gestione())
//						 			  );
//
//							getHomeCache(userContext).fetchAll(userContext);
//
//							if (voceOld == null)
//								throw new ApplicationException("La voce: "+ voce.getCd_voce() +" non è presente nell'esercizio: "+CNRUserContext.getEsercizio(userContext));
							
							Elemento_voceBulk elementoVoceOld = (Elemento_voceBulk)getHome(userContext,Elemento_voceBulk.class).findByPrimaryKey(
									  new Elemento_voceBulk(saldoOld.getCd_voce(),CNRUserContext.getEsercizio(userContext),saldoOld.getTi_appartenenza(),saldoOld.getTi_gestione())
									  );
							
							if (elementoVoceOld == null)
							  throw new ApplicationException("Elemento voce non trovato per la Voce: "+ saldoOld.getCd_voce() +" nell'esercizio: "+CNRUserContext.getEsercizio(userContext));

							//cerco la voce del nuovo anno
							List listVociNew = ass_evold_evnewHome.findAssElementoVoceNewList(elementoVoceOld);
							if (!listVociNew.isEmpty()) {
								if (listVociNew.size()>1)
									throw new ApplicationException("Trovate nella tabella di associazione Vecchie/Nuove Voci più elementi voce nel nuovo anno per la Voce: "+ elementoVoceOld.getCd_voce() +" nell'esercizio: "+CNRUserContext.getEsercizio(userContext));
								elemento_voce = (Elemento_voceBulk)listVociNew.get(0);
							} else {
								elemento_voce = (Elemento_voceBulk)getHome(userContext,Elemento_voceBulk.class).findByPrimaryKey(
									  new Elemento_voceBulk(elementoVoceOld.getCd_elemento_voce(),esercizio.getEsercizio(),elementoVoceOld.getTi_appartenenza(),elementoVoceOld.getTi_gestione())
								  );
							}
							
							if (elemento_voce == null || elemento_voce.getEsercizio().compareTo(esercizio.getEsercizio())!=0)
								  throw new ApplicationException("Elemento voce non trovato o associato ad una voce di anno differente rispetto a quello di ribaltamento per la Voce: "+ saldoOld.getCd_voce() +" nell'esercizio: "+CNRUserContext.getEsercizio(userContext));

							saldoNew = findAndLock( userContext,esercizio.getEsercizio(), esercizio_res, cd_cdr,cd_linea_attivita, elemento_voce);
							codiceVoceForSaldoNew = elemento_voce.getCd_voce();
						}
					} else {
						//recupero la voce di ribaltamento
						Elemento_voceBulk elementoVoceOld = (Elemento_voceBulk)getHome(userContext,Elemento_voceBulk.class).findByPrimaryKey(
								  new Elemento_voceBulk(voce.getCd_elemento_voce(),CNRUserContext.getEsercizio(userContext),voce.getTi_appartenenza(),voce.getTi_gestione())
								  );

						if (elementoVoceOld == null)
							  throw new ApplicationException("Elemento voce non trovato per la Voce: "+ voce.getCd_voce() +" nell'esercizio: "+CNRUserContext.getEsercizio(userContext));

						getHomeCache(userContext).fetchAll(userContext);

						//cerco la voce del nuovo anno
						List listVociNew = ass_evold_evnewHome.findAssElementoVoceNewList(elementoVoceOld);
						if (!listVociNew.isEmpty()) {
							if (listVociNew.size()>1)
								throw new ApplicationException("Trovate nella tabella di associazione Vecchie/Nuove Voci più elementi voce nel nuovo anno per la Voce: "+ elementoVoceOld.getCd_voce() +" nell'esercizio: "+CNRUserContext.getEsercizio(userContext));
							elemento_voce = (Elemento_voceBulk)listVociNew.get(0);
						} else {
							elemento_voce = (Elemento_voceBulk)getHome(userContext,Elemento_voceBulk.class).findByPrimaryKey(
									  new Elemento_voceBulk(elementoVoceOld.getCd_elemento_voce(),esercizio.getEsercizio(),elementoVoceOld.getTi_appartenenza(),elementoVoceOld.getTi_gestione())
									  );
						}

						if (elemento_voce == null || elemento_voce.getEsercizio().compareTo(esercizio.getEsercizio())!=0)
							  throw new ApplicationException("Elemento voce non trovato o associato ad una voce di anno differente rispetto a quello di ribaltamento per la Voce: "+ elementoVoceOld.getCd_voce() +" nell'esercizio: "+CNRUserContext.getEsercizio(userContext));

						saldoNew = findAndLock( userContext,esercizio.getEsercizio(), esercizio_res, cd_cdr,cd_linea_attivita, elemento_voce);
						codiceVoceForSaldoNew = elemento_voce.getCd_voce();
					}
					getHomeCache(userContext).fetchAll(userContext);
					
					WorkpackageBulk workpackage = (WorkpackageBulk)getHome(userContext,WorkpackageBulk.class).findByPrimaryKey(
													new WorkpackageBulk(cd_cdr,cd_linea_attivita)
													);
					 // Obbligatorio cofog sulle GAE 
					Parametri_cnrBulk par = (Parametri_cnrBulk)getHome(userContext,Parametri_cnrBulk.class).findByPrimaryKey( new Parametri_cnrBulk(esercizio.getEsercizio()));
					if (par != null && par.getLivello_pdg_cofog()!=0)
						if( (workpackage.getTi_gestione().compareTo(CostantiTi_gestione.TI_GESTIONE_SPESE)==0) && workpackage.getCd_cofog()==null)
							throw new ApplicationException("Non è possibile utilizzare GAE di spesa in cui non è indicata la classificazione Cofog.");
																		  
					getHomeCache(userContext).fetchAll(userContext);																		  

					if (saldoNew == null){
						if (elemento_voce == null)
						  throw new ApplicationException("Elemento voce non trovato per la Voce: "+ voce.getCd_voce() +" nell'esercizio: "+esercizio.getEsercizio());

						saldoNew = new Voce_f_saldi_cdr_lineaBulk( esercizio.getEsercizio(), esercizio_res, cd_cdr, cd_linea_attivita, voce.getTi_appartenenza(), voce.getTi_gestione(),codiceVoceForSaldoNew);
						saldoNew.setCd_elemento_voce(elemento_voce.getCd_elemento_voce());
						saldoNew.inizializzaSommeAZero();
						saldoNew.setToBeCreated();
						insertBulk(userContext, saldoNew);	                					
					}
					if (saldoNew != null){				
							saldoNew.setIm_stanz_res_improprio(saldoNew.getIm_stanz_res_improprio().subtract(importo));

							//calcolo i vincoli
							Pdg_vincoloHome home = (Pdg_vincoloHome)getHome(userContext, Pdg_vincoloBulk.class);
							List<Pdg_vincoloBulk> listVincoli = home.cercaDettagliVincolati(saldoNew);
							BigDecimal impVincolo = listVincoli.stream().map(e->e.getIm_vincolo()).reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
							BigDecimal diff = saldoNew.getDispAdImpResiduoImproprio().subtract(impVincolo);

							if (diff.compareTo(Utility.ZERO) < 0){
								if (voce.getTi_gestione().equalsIgnoreCase(Voce_f_saldi_cdr_lineaBulk.TIPO_GESTIONE_SPESA)){
								    if (!((elemento_voce.getFl_partita_giro() != null && 
								           elemento_voce.getFl_partita_giro().booleanValue()) ||
								     (elemento_voce.getFl_limite_ass_obblig()!= null && !elemento_voce.getFl_limite_ass_obblig().booleanValue() &&
									  workpackage.getFl_limite_ass_obblig()!= null && !workpackage.getFl_limite_ass_obblig().booleanValue()))){
										StringBuilder messaggio = new StringBuilder("Impossibile effettuare l'operazione !\n"+
											        "Nell'esercizio "+esercizio.getEsercizio()+
											        " e per il CdR "+cd_cdr+", "+
											        " Voce "+voce.getCd_voce()+
											        " e GAE "+cd_linea_attivita+" lo stanziamento Residuo Improprio "+
											        " diventerebbe negativo ("+new it.cnr.contab.util.EuroFormat().format(diff.abs())+")");
										if (impVincolo.compareTo(BigDecimal.ZERO)>0)
											messaggio.append(" in conseguenza della presenza di vincoli di spesa per un importo di " + 
													new it.cnr.contab.util.EuroFormat().format(impVincolo.abs()));
										throw new ApplicationException(messaggio.toString());
								    }
								}	                                                           
							}						  
							saldoNew.setToBeUpdated();
							updateBulk( userContext, saldoNew );						
					}
				}
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		}
}
/** 
  *  riscontro mandato
  *    PreCondition:
  *      E' stata riscontrato un mandato 
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza del mandato l'importo associato a pagamenti/incassi
  *      della voce del piano (di competenza o residuo) interessata dal mandato 
  *  riscontro reversale
  *    PreCondition:
  *      E' stata riscontrata una reversale
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza della reversale l'importo associato a pagamenti/incassi
  *      della voce del piano (di competenza o residuo) interessata dalla reversale
  *  annullamento riscontro mandato
  *    PreCondition:
  *      E' stata annullato il riscontro di un mandato 
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza del mandato l'importo associato a pagamenti/incassi
  *      della voce del piano (di competenza o residuo) interessata dal mandato 
  *  annullamento riscontro reversale
  *    PreCondition:
  *      E' stato annullato il riscontro di una reversale
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza della reversale l'importo associato a pagamenti/incassi
  *      della voce del piano (di competenza o residuo) interessata dalla reversale
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param cd_cdr il codice del CDR per cui effettuare la ricerca del saldo
  * @param cd_linea il codice del Workpackage per cui effettuare la ricerca del saldo
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param esercizio_res l'anno del residuo
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
*/

public Voce_f_saldi_cdr_lineaBulk aggiornaPagamentiIncassi(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce, Integer esercizio_res, BigDecimal importo ) throws ComponentException
{
	try
	{
		Voce_f_saldi_cdr_lineaBulk saldo = findAndLock( userContext,voce.getEsercizio(), esercizio_res, cd_cdr,cd_linea_attivita, voce);
		if (saldo == null && 
			((Parametri_cnrBulk)getHome(userContext,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)))).getFl_regolamento_2006().booleanValue()) {
			throw handleException( new ApplicationException("Non e' presente il saldo per Esercizio: " + voce.getEsercizio() +
													   " CDR: " + cd_cdr + " GAE: " + cd_linea_attivita + " Voce: " + voce.getCd_voce() ));
		}		
		if (saldo != null) {
			saldo.setIm_pagamenti_incassi( saldo.getIm_pagamenti_incassi().add(importo.setScale(2, importo.ROUND_HALF_UP) ));
			saldo.setUser( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser());
			saldo.setToBeUpdated();
			updateBulk( userContext, saldo );
		}	
		return saldo;
	}
	catch 	(Exception e )
	{
		throw handleException(  e );
	}	

}
/** 
 *  calcola il saldo (disponibilità ad impegnare) per creare residui propri o impropri
 *  come sommatoria dei saldi di tutti gli anni residui presenti 
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param esercizio l'anno di competenza
 * @param cd_cdr il codice del CDR per cui effettuare la ricerca del saldo dei residui
 * @param cd_linea il codice del Workpackage per cui effettuare la ricerca del saldo dei residui
 * @param cd_voce il codice della voce del piano per cui ricercare il saldo dei residui 
*/
public java.math.BigDecimal getTotaleSaldoResidui(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce) throws ComponentException
{
	try
	{
		java.math.BigDecimal saldoResiduo = new java.math.BigDecimal(0);
		Voce_f_saldi_cdr_lineaBulk comp = find( userContext, cd_cdr, cd_linea_attivita, voce);
		if (comp != null) {
			List residui = ((Voce_f_saldi_cdr_lineaHome)getHome( userContext,Voce_f_saldi_cdr_lineaBulk.class )).cercaDettagliResidui( comp );

			for (Iterator i = residui.iterator(); i.hasNext();) {
				Voce_f_saldi_cdr_lineaBulk residuo = (Voce_f_saldi_cdr_lineaBulk)i.next();
				saldoResiduo = saldoResiduo.add(residuo.getDispAdImpResiduoImproprio());
			}
		}
		return saldoResiduo;
	}
	catch 	(Exception e )
	{
		throw handleException(  e );
	}	
}
public Voce_f_saldi_cdr_lineaBulk aggiornaImpegniResiduiPropri(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce, Integer esercizio_res, BigDecimal importo) throws ComponentException
{
	try
	{
		Voce_f_saldi_cdr_lineaBulk saldo;
		saldo = findAndLock( userContext,voce.getEsercizio(), esercizio_res, cd_cdr,cd_linea_attivita, voce);
		if (saldo == null){
			  throw new ApplicationException("Saldo non trovato per la Voce/CdR/GAE: "+ voce.getCd_voce()+"/"+cd_cdr+"/"+cd_linea_attivita);
		}

		//calcolo i vincoli
		Pdg_vincoloHome home = (Pdg_vincoloHome)getHome(userContext, Pdg_vincoloBulk.class);
		List<Pdg_vincoloBulk> listVincoli = home.cercaDettagliVincolati(saldo);
		BigDecimal impVincolo = listVincoli.stream().map(e->e.getIm_vincolo()).reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

		importo = importo.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal diff = saldo.getAssestatoResiduoImproprio().subtract(saldo.getIm_obbl_res_imp()).subtract(importo).subtract(impVincolo);

		if (diff.compareTo(Utility.ZERO)<0) {
			StringBuilder messaggio = new StringBuilder("Impossibile effettuare l'operazione !\n"+
			        "Nell'esercizio "+saldo.getEsercizio()+
			        " e per il CdR "+saldo.getCd_centro_responsabilita()+", "+
			        " Voce "+voce.getCd_voce()+
			        " e GAE "+saldo.getCd_linea_attivita()+" lo stanziamento Residuo Improprio "+
			        " diventerebbe negativo ("+new it.cnr.contab.util.EuroFormat().format(diff.abs())+")");
			if (impVincolo.compareTo(BigDecimal.ZERO)>0)
				messaggio.append(" in conseguenza della presenza di vincoli di spesa per un importo di " + 
						new it.cnr.contab.util.EuroFormat().format(impVincolo.abs()));
			throw new ApplicationException(messaggio.toString());
		}

		if (importo.compareTo(Utility.ZERO)>0)
			saldo.setVar_piu_obbl_res_pro( saldo.getVar_piu_obbl_res_pro().add( importo.abs()) );
		else
			saldo.setVar_meno_obbl_res_pro( saldo.getVar_meno_obbl_res_pro().add( importo.abs()) );   
		saldo.setUser( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser());
		saldo.setToBeUpdated();

		aggiornaSaldiAnniSuccessivi(userContext,
				cd_cdr,
				cd_linea_attivita,
				voce,
				esercizio_res,
				importo,
				saldo);

		updateBulk( userContext, saldo );
		return saldo;
	}
	catch 	(Exception e )
	{
		throw handleException(  e );
	}	

}
public Voce_f_saldi_cdr_lineaBulk aggiornaAccertamentiResiduiPropri(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce, Integer esercizio_res, BigDecimal importo) throws ComponentException
{
	try
	{
		Voce_f_saldi_cdr_lineaBulk saldo;
		saldo = findAndLock( userContext,voce.getEsercizio(), esercizio_res, cd_cdr,cd_linea_attivita, voce);
		if (saldo == null){
			  throw new ApplicationException("Saldo non trovato per la Voce/CdR/GAE: "+ voce.getCd_voce()+"/"+cd_cdr+"/"+cd_linea_attivita);
		}
		importo = importo.setScale(2, importo.ROUND_HALF_UP);
		if (importo.compareTo(Utility.ZERO)>0)
			saldo.setVar_piu_obbl_res_pro( saldo.getVar_piu_obbl_res_pro().add( importo.abs()) );
		else
			saldo.setVar_meno_obbl_res_pro( saldo.getVar_meno_obbl_res_pro().add( importo.abs()) );   
		saldo.setUser( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser());
		saldo.setToBeUpdated();
		updateBulk( userContext, saldo );
		return saldo;
	}
	catch 	(Exception e )
	{
		throw handleException(  e );
	}	

}
	/**
	 * Metodo che verifica la congruenza delle disponibilità sul piano economico del progetto
	 * 
	 * @param userContext
	 * @param moduloCosti
	 * @param isFromChangeStato indica che la modifica è stata richiesta in fase di cambio stato progetto 
	 * @throws ComponentException
	 */
    public void checkDispPianoEconomicoProgetto(UserContext userContext, Pdg_modulo_costiBulk moduloCosti, boolean isFromChangeStato) throws ComponentException
    {
        try {
   	   		it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configSession = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
   	   		BigDecimal annoFrom = configSession.getIm01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_PROGETTO_PIANO_ECONOMICO);
   	   		if (Optional.ofNullable(annoFrom).map(BigDecimal::intValue).filter(el->el.compareTo(moduloCosti.getEsercizio())<=0).isPresent()) {
	            List<Progetto_piano_economicoBulk> pianoEconomicoList = (List<Progetto_piano_economicoBulk>)((Progetto_piano_economicoHome)getHome(userContext,Progetto_piano_economicoBulk.class)).findProgettoPianoEconomicoList(moduloCosti.getPg_progetto());
	
	            List<Pdg_modulo_speseBulk> speseListDB = (List<Pdg_modulo_speseBulk>)((Pdg_modulo_costiHome)getHome(userContext,Pdg_modulo_costiBulk.class)).findPdgModuloSpeseDettagli(userContext, moduloCosti);
	            List<Pdg_modulo_speseBulk> speseList = (List<Pdg_modulo_speseBulk>)moduloCosti.getDettagliSpese();
	
	            pianoEconomicoList.stream()
	                    .filter(e->e.getFl_ctrl_disp() && (e.getEsercizio_piano().equals(0) || e.getEsercizio_piano().equals(moduloCosti.getEsercizio())))
	                    .forEach(e->{
	                        try {
	                            Progetto_piano_economicoBulk bulk = null;
	
	                            Progetto_piano_economicoBulk bulkToFind = new Progetto_piano_economicoBulk();
	                            bulkToFind.setVoce_piano_economico(e.getVoce_piano_economico());
	                            bulkToFind.setPg_progetto(e.getPg_progetto());
	                            bulkToFind.setEsercizio_piano(e.getEsercizio_piano());
	                            try {
	                                bulk = (Progetto_piano_economicoBulk) getHome( userContext,Progetto_piano_economicoBulk.class ).findAndLock(bulkToFind);
	                            } catch (ObjectNotFoundException ex) {
	                            }
	
	                            if (bulk!=null && bulk.getFl_ctrl_disp()) {
	                                V_saldi_piano_econom_progettoBulk saldo = ((V_saldi_piano_econom_progettoHome)getHome( userContext,V_saldi_piano_econom_progettoBulk.class )).
	                                        cercaSaldoPianoEconomico(bulk, "S");
	
	                                BigDecimal dispResiduaFin = saldo.getDispResiduaFinanziamento();
	
	                                if (!isFromChangeStato) {
		                                dispResiduaFin = dispResiduaFin.add(
		                                        speseListDB.stream()
		                                        		.filter(x->Optional.ofNullable(x.getVoce_piano_economico()).isPresent())
		                                                .filter(x->x.getVoce_piano_economico().equalsByPrimaryKey(e.getVoce_piano_economico()))
		                                                .map(el->Utility.nvl(el.getIm_spese_gest_decentrata_est()))
		                                                .collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)));
		
		                                dispResiduaFin = dispResiduaFin.subtract(
		                                        speseList.stream()
		                                        		.filter(x->Optional.ofNullable(x.getVoce_piano_economico()).isPresent())
		                                                .filter(x->x.getVoce_piano_economico().equalsByPrimaryKey(e.getVoce_piano_economico()))
		                                                .map(el->Utility.nvl(el.getIm_spese_gest_decentrata_est()))
		                                                .collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)));
	                                }
	                                
	                                if (dispResiduaFin.compareTo(BigDecimal.ZERO)<0)
	                                    throw new ApplicationException(
	                                            "Impossibile effettuare l'operazione !\n"+
	                                            "L'importo indicato in previsione per le fonti decentrate esterne supera di "+
	                                            new it.cnr.contab.util.EuroFormat().format(dispResiduaFin.abs())+
	                                            "  l'importo finanziato indicato sul piano economico "+e.getCd_voce_piano()+
	                                            " del progetto "+(e.getEsercizio_piano().equals(0)?"":"per l'esercizio "+e.getEsercizio_piano())+
	                                            ".");
	
	                                if (isFromChangeStato &&
	                                		Optional.ofNullable(moduloCosti)
	                                		.flatMap(el->Optional.ofNullable(el.getPdg_modulo()))
	                                		.flatMap(el->Optional.ofNullable(el.getProgetto()))
	                                		.flatMap(el->Optional.ofNullable(el.getOtherField()))
	                                		.flatMap(el->Optional.ofNullable(el.getTipoFinanziamento()))
	                                		.map(TipoFinanziamentoBulk::getFlAllPrevFin)
	                                		.orElse(Boolean.TRUE) &&
	                                    	dispResiduaFin.compareTo(BigDecimal.ZERO)!=0) {
	                                	Voce_piano_economico_prgBulk vocePianoEconomico = (Voce_piano_economico_prgBulk)((Voce_piano_economico_prgHome)getHome(userContext, Voce_piano_economico_prgBulk.class)).findByPrimaryKey(e.getVoce_piano_economico());
	                                	if (Optional.ofNullable(vocePianoEconomico)
	                                		.map(Voce_piano_economico_prgBulk::getFlAllPrevFin)
	                                		.orElse(Boolean.TRUE))
		                                    throw new ApplicationException(
		                                            "Impossibile effettuare l'operazione !\n"+
		                                                    "L'importo totale indicato in previsione per le fonti decentrate esterne non corrisponde " +
		                                                    "  all'importo finanziato indicato sul piano economico "+e.getCd_voce_piano()+
		                                                    " del progetto "+(e.getEsercizio_piano().equals(0)?"":"per l'esercizio "+e.getEsercizio_piano())+
		                                                    "(diff: "+new it.cnr.contab.util.EuroFormat().format(dispResiduaFin)+").");
	            					}
	                                		
	                                BigDecimal dispResiduaCofin = saldo.getDispResiduaCofinanziamento();
	
	                                if (!isFromChangeStato) {
		                                dispResiduaCofin = dispResiduaCofin.add(
		                                        speseListDB.stream()
		                                        		.filter(x->Optional.ofNullable(x.getVoce_piano_economico()).isPresent())
		                                                .filter(x->x.getVoce_piano_economico().equalsByPrimaryKey(e.getVoce_piano_economico()))
		                                                .map(el->Utility.nvl(el.getIm_spese_gest_decentrata_int()))
		                                                .collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)));
		
		                                dispResiduaCofin = dispResiduaCofin.subtract(
		                                        speseList.stream()
		                                        		.filter(x->Optional.ofNullable(x.getVoce_piano_economico()).isPresent())
		                                                .filter(x->x.getVoce_piano_economico().equalsByPrimaryKey(e.getVoce_piano_economico()))
		                                                .map(el->Utility.nvl(el.getIm_spese_gest_decentrata_int()))
		                                                .collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)));
	                                }
	                                
	                                if (dispResiduaCofin.compareTo(BigDecimal.ZERO)<0)
	                                    throw new ApplicationException(
	                                            "Impossibile effettuare l'operazione !\n"+
	                                            "L'importo indicato in previsione per le fonti decentrate interne supera di "+
	                                            new it.cnr.contab.util.EuroFormat().format(dispResiduaCofin.abs())+
	                                            "  l'importo cofinanziato indicato sul piano economico "+e.getCd_voce_piano()+
	                                            " del progetto "+(e.getEsercizio_piano().equals(0)?"":"per l'esercizio "+e.getEsercizio_piano())+
	                                            ".");
	                            }
	                        }
	                        catch (Exception ex )
	                        {
	                            throw new RuntimeException(  ex );
	                        }
	                    });
   	   		}
        }
        catch 	(Exception e )
        {
            if (e instanceof RuntimeException)
                throw handleException(  e.getCause() );
            throw handleException(  e );
        }
    }
    public String getMessaggioSfondamentoPianoEconomico(UserContext userContext, Pdg_variazioneBulk pdgVariazione) throws ComponentException{
        return getMessaggioSfondamentoPianoEconomico(userContext, pdgVariazione, false);
    }
    public void checkDispPianoEconomicoProgetto(UserContext userContext, Pdg_variazioneBulk pdgVariazione) throws ComponentException
    {
        String message = getMessaggioSfondamentoPianoEconomico(userContext, pdgVariazione, true);
        if (message!=null && message.length()>0)
            throw new ApplicationException(
                    "Impossibile effettuare l'operazione !\n"+message);
    }
    private String getMessaggioSfondamentoPianoEconomico(UserContext userContext, Pdg_variazioneBulk pdgVariazione, boolean locked) throws ComponentException{
   		String messaggio = "";
        try {
        	Configurazione_cnrComponentSession configSession = (Configurazione_cnrComponentSession)Utility.createConfigurazioneCnrComponentSession();
			String cdNaturaReimpiego = Utility.createConfigurazioneCnrComponentSession().getVal01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_NATURA_REIMPIEGO);
			BigDecimal annoFrom = configSession.getIm01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_PROGETTO_PIANO_ECONOMICO);

			if (Optional.ofNullable(annoFrom).map(BigDecimal::intValue).filter(el->el.compareTo(pdgVariazione.getEsercizio())<=0).isPresent()) {
	            Pdg_variazioneHome detHome = (Pdg_variazioneHome)getHome(userContext,Pdg_variazioneBulk.class);
	
	            for (java.util.Iterator dett = detHome.findDettagliSpesaVariazioneGestionale(pdgVariazione).iterator();dett.hasNext();){
	                Pdg_variazione_riga_gestBulk rigaVar = (Pdg_variazione_riga_gestBulk)dett.next();
	
	                WorkpackageBulk linea_attivita = (WorkpackageBulk)((it.cnr.contab.config00.ejb.Linea_attivitaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
	                        "CNRCONFIG00_EJB_Linea_attivitaComponentSession", it.cnr.contab.config00.ejb.Linea_attivitaComponentSession.class)
	                ).inizializzaBulkPerModifica(userContext, rigaVar.getLinea_attivita());

					boolean isNaturaReimpiego = Optional.ofNullable(cdNaturaReimpiego).map(el->el.equals(linea_attivita.getNatura().getCd_natura()))
							.orElse(Boolean.FALSE);

					//recupero il progetto per verificare se è scaduto
					ProgettoHome home = (ProgettoHome)getHome(userContext, ProgettoBulk.class);
					home.setFetchPolicy("it.cnr.contab.progettiric00.comp.ProgettoRicercaComponent.find");
					ProgettoBulk progetto = (ProgettoBulk)home.findByPrimaryKey(linea_attivita.getProgetto2016());
					getHomeCache(userContext).fetchAll(userContext);
					
					if (progetto.isPianoEconomicoRequired()) {
		                List<Progetto_piano_economicoBulk> pianoEconomicoList = (List<Progetto_piano_economicoBulk>)((Progetto_piano_economicoHome)getHome(userContext,Progetto_piano_economicoBulk.class)).findProgettoPianoEconomicoList(pdgVariazione.getEsercizio(), linea_attivita.getProgetto2016().getPg_progetto(), rigaVar.getElemento_voce());
		                if (pianoEconomicoList==null || pianoEconomicoList.isEmpty())
                            messaggio = messaggio +
                            "Non risulta essere stato imputato alcun valore nel piano economico del progetto per la Voce " + 
                            	rigaVar.getCd_elemento_voce() + " e GAE " + rigaVar.getCd_linea_attivita() + ".\n";
		                	
		                for (Progetto_piano_economicoBulk e : pianoEconomicoList) {
		                    if (e.getFl_ctrl_disp() &&
		                            (e.getEsercizio_piano().equals(0) || e.getEsercizio_piano().equals(rigaVar.getEsercizio()))) {
		                        try {
		                            if (locked) {
		                                Progetto_piano_economicoBulk bulkToFind = new Progetto_piano_economicoBulk();
		                                bulkToFind.setVoce_piano_economico(e.getVoce_piano_economico());
		                                bulkToFind.setPg_progetto(e.getPg_progetto());
		                                bulkToFind.setEsercizio_piano(e.getEsercizio_piano());
		                                try {
		                                    bulkToFind = (Progetto_piano_economicoBulk) getHome( userContext,Progetto_piano_economicoBulk.class ).findAndLock(bulkToFind);
		                                } catch (ObjectNotFoundException ex) {
		                                }
		                            }

		                            V_saldi_piano_econom_progettoBulk saldo = ((V_saldi_piano_econom_progettoHome)getHome( userContext,V_saldi_piano_econom_progettoBulk.class )).
		                                    cercaSaldoPianoEconomico(e, "S");
		
		                            if (!isNaturaReimpiego) {
			                            BigDecimal imVariazioneFin = Utility.nvl(rigaVar.getIm_spese_gest_decentrata_est());
			                            BigDecimal dispResiduaFin = saldo.getDispResiduaFinanziamento().subtract(imVariazioneFin);
			                            if (dispResiduaFin.compareTo(BigDecimal.ZERO)<0) {
			                                if (messaggio!=null && messaggio.length()>0)
			                                    messaggio = messaggio+ "\n";
			                                messaggio = messaggio +
			                                        "La disponibilità quota finanziata del piano economico "+e.getCd_voce_piano()+
			                                        " associato al progetto"+(e.getEsercizio_piano().equals(0)?"":" per l'esercizio "+e.getEsercizio_piano())+
			                                        " per la Voce " + rigaVar.getCd_elemento_voce() + " e GAE " + rigaVar.getCd_linea_attivita() +
			                                        " non è sufficiente a coprire la variazione che risulta di " +
			                                        new it.cnr.contab.util.EuroFormat().format(imVariazioneFin) + ".\n";
			                            }
		                            }		
		                            
		                            BigDecimal imVariazioneCofin = Utility.nvl(rigaVar.getIm_spese_gest_decentrata_int());
		                            if (isNaturaReimpiego)
		                            	imVariazioneCofin = imVariazioneCofin.add(Utility.nvl(rigaVar.getIm_spese_gest_decentrata_est()));
		                            BigDecimal dispResiduaCofin = saldo.getDispResiduaCofinanziamento().subtract(imVariazioneCofin);
		                            if (dispResiduaCofin.compareTo(BigDecimal.ZERO)<0) {
		                                if (messaggio!=null && messaggio.length()>0)
		                                    messaggio = messaggio+ "\n";
		                                messaggio = messaggio +
		                                        "La disponibilità quota cofinanziata del piano economico "+e.getCd_voce_piano()+
		                                        " associato al progetto"+(e.getEsercizio_piano().equals(0)?"":" per l'esercizio "+e.getEsercizio_piano())+
		                                        " per la Voce " + rigaVar.getCd_elemento_voce() + " e GAE " + rigaVar.getCd_linea_attivita() +
		                                        " non è sufficiente a coprire la variazione che risulta di " +
		                                        new it.cnr.contab.util.EuroFormat().format(imVariazioneCofin) + ".\n";
		                            }
		                        }
		                        catch (Exception ex )
		                        {
		                            throw new RuntimeException(  ex );
		                        }
		                    }
		                }
					}
	            }
	   		}
        }catch (PersistencyException e) {
            throw new ComponentException(e);
        }catch (RemoteException e) {
            throw new ComponentException(e);
        } catch (EJBException e) {
            throw new ComponentException(e);
        }
        return messaggio;
    }

	public void checkPdgPianoEconomico(UserContext userContext, Var_stanz_resBulk variazione) throws ComponentException{
		try {
			if (Utility.createParametriEnteComponentSession().isProgettoPianoEconomicoEnabled(userContext, CNRUserContext.getEsercizio(userContext))) {
				List<CtrlPianoEco> listCtrlPianoEco = new ArrayList<CtrlPianoEco>();

				String cdNaturaReimpiego = Utility.createConfigurazioneCnrComponentSession().getVal01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_NATURA_REIMPIEGO);
				String cdVoceSpeciale = Utility.createConfigurazioneCnrComponentSession().getVal01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_ELEMENTO_VOCE_SPECIALE, Configurazione_cnrBulk.SK_TEMPO_IND_SU_PROGETTI_FINANZIATI);
				
				String cdrPersonale = Optional.ofNullable(((ObbligazioneHome)getHome(userContext, ObbligazioneBulk.class)).recupero_cdr_speciale_stipendi())
						.orElseThrow(() -> new ComponentException("Non è possibile individuare il codice CDR del Personale."));
				CdrBulk cdrPersonaleBulk = (CdrBulk)getHome(userContext, CdrBulk.class).findByPrimaryKey(new CdrBulk(cdrPersonale));

				Ass_var_stanz_res_cdrHome ass_cdrHome = (Ass_var_stanz_res_cdrHome)getHome(userContext,Ass_var_stanz_res_cdrBulk.class);
				java.util.Collection<Var_stanz_res_rigaBulk> dettagliSpesa = ass_cdrHome.findDettagliSpesa(variazione);
	
				for (Var_stanz_res_rigaBulk varStanzResRiga : dettagliSpesa) {
					//verifico se si tratta di area
					CdrBulk cdrBulk = (CdrBulk)getHome(userContext, CdrBulk.class).findByPrimaryKey(new CdrBulk(varStanzResRiga.getCd_cdr()));
					Unita_organizzativaBulk uoBulk = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdrBulk.getCd_unita_organizzativa()));
					boolean isUoArea = uoBulk.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_AREA);
					
					//verifico se si tratta di cdr Personale
					boolean isDettPersonale = uoBulk.getCd_unita_organizzativa().equals(cdrPersonaleBulk.getCd_unita_organizzativa());
					if (!isDettPersonale) {
						//verifico se si tratta di voce accentrata verso il personale
						Elemento_voceBulk voce = (Elemento_voceBulk)getHome(userContext, Elemento_voceBulk.class).findByPrimaryKey(varStanzResRiga.getElemento_voce());
						Classificazione_vociBulk classif = (Classificazione_vociBulk)getHome(userContext, Classificazione_vociBulk.class).findByPrimaryKey(new Classificazione_vociBulk(voce.getId_classificazione()));
						isDettPersonale = classif.getFl_accentrato()&&cdrPersonale.equals(classif.getCdr_accentratore());
					}

					//recupero la GAE
					BulkHome lattHome = getHome(userContext, WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA");
					SQLBuilder sql = lattHome.createSQLBuilder();
	
					sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,varStanzResRiga.getEsercizio());
					sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,varStanzResRiga.getCd_cdr());
					sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA",SQLBuilder.EQUALS,varStanzResRiga.getCd_linea_attivita());
						
					List<WorkpackageBulk> listGAE = lattHome.fetchAll(sql);
					
					if (listGAE.isEmpty() || listGAE.size()>1)
						throw new ApplicationException("Errore in fase di ricerca linea_attivita "+varStanzResRiga.getEsercizio()+"/"+varStanzResRiga.getCd_cdr()+"/"+varStanzResRiga.getCd_linea_attivita()+".");
					
					WorkpackageBulk linea = listGAE.get(0);

					//recupero il progetto per verificare se è scaduto
					ProgettoHome home = (ProgettoHome)getHome(userContext, ProgettoBulk.class);
					home.setFetchPolicy("it.cnr.contab.progettiric00.comp.ProgettoRicercaComponent.find");
					ProgettoBulk progetto = (ProgettoBulk)home.findByPrimaryKey(userContext, linea.getProgetto());
					getHomeCache(userContext).fetchAll(userContext);
					
					//effettuo controlli sulla validità del progetto
					Optional.of(progetto.getOtherField())
							.filter(el->el.isStatoApprovato()||el.isStatoChiuso())
							.orElseThrow(()->new ApplicationException("Attenzione! Il progetto "+progetto.getCd_progetto()
									+ " non risulta in stato approvato o chiuso. Variazione non consentita!"));
						
					if (progetto.getOtherField().isDatePianoEconomicoRequired()) {
						Optional.ofNullable(progetto.getOtherField().getDtInizio())
						 	    .orElseThrow(()->new ApplicationException("Attenzione! GAE "+linea.getCd_linea_attivita()+" non selezionabile. "
													+ "La data inizio del progetto non risulta impostata."));

						Optional.ofNullable(progetto.getOtherField().getDtFine())
					    .orElseThrow(()->new ApplicationException("Attenzione! GAE "+linea.getCd_linea_attivita()+" non selezionabile. "
											+ "La data fine del progetto non risulta impostata."));
						
						Optional.of(progetto.getOtherField().getDtInizio())
				 	    		.filter(dt->!dt.after(variazione.getDt_chiusura()))
								.orElseThrow(()->new ApplicationException("Attenzione! GAE "+linea.getCd_linea_attivita()+" non selezionabile. "
										+ "La data inizio ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(progetto.getOtherField().getDtInizio())
										+ ") del progetto "+progetto.getCd_progetto()+" associato è successiva "
										+ "rispetto alla data di chiusura della variazione ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(variazione.getDt_chiusura())+")."));
					}

					//recupero il record se presente altrimenti ne creo uno nuovo
					CtrlPianoEco pianoEco = listCtrlPianoEco.stream()
							.filter(el->el.getProgetto().getPg_progetto().equals(progetto.getPg_progetto()))
							.findFirst()
							.orElse(new CtrlPianoEco(progetto));

					//creo il dettaglio
					CtrlPianoEcoDett dett = new CtrlPianoEcoDett();
					dett.setImporto(varStanzResRiga.getIm_variazione());
					dett.setCdrPersonale(isDettPersonale);
					dett.setUoArea(isUoArea);

					if (Optional.ofNullable(cdNaturaReimpiego).map(el->el.equals(linea.getNatura().getCd_natura())).orElse(Boolean.FALSE))
						dett.setTipoNatura(CtrlPianoEcoDett.TIPO_REIMPIEGO);
					else if (linea.getNatura().isFonteEsterna())
						dett.setTipoNatura(CtrlPianoEcoDett.TIPO_FONTE_ESTERNA);
					else
						dett.setTipoNatura(CtrlPianoEcoDett.TIPO_FONTE_INTERNA);
						
					dett.setVoceSpeciale(Optional.ofNullable(cdVoceSpeciale).map(el->el.equals(linea.getNatura().getCd_natura()))
													.orElse(Boolean.FALSE));

					pianoEco.getDett().add(dett);
					listCtrlPianoEco.add(pianoEco);
				}
				controllaPdgPianoEconomico(userContext, listCtrlPianoEco, variazione.getDt_chiusura(), cdVoceSpeciale);
			}
        } catch (DetailedRuntimeException _ex) {
            throw new ApplicationException(_ex.getMessage());
        } catch (PersistencyException|RemoteException|IntrospectionException e) {
			throw new ComponentException(e);
		}
	}

	public void checkPdgPianoEconomico(UserContext userContext, Pdg_variazioneBulk variazione) throws ComponentException{
		try {
			if (Utility.createParametriEnteComponentSession().isProgettoPianoEconomicoEnabled(userContext, CNRUserContext.getEsercizio(userContext))) {
				List<CtrlPianoEco> listCtrlPianoEco = new ArrayList<CtrlPianoEco>();

				String cdNaturaReimpiego = Utility.createConfigurazioneCnrComponentSession().getVal01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_NATURA_REIMPIEGO);
				String cdVoceSpeciale = Utility.createConfigurazioneCnrComponentSession().getVal01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_ELEMENTO_VOCE_SPECIALE, Configurazione_cnrBulk.SK_TEMPO_IND_SU_PROGETTI_FINANZIATI);

				String cdrPersonale = Optional.ofNullable(((ObbligazioneHome)getHome(userContext, ObbligazioneBulk.class)).recupero_cdr_speciale_stipendi())
						.orElseThrow(() -> new ComponentException("Non è possibile individuare il codice CDR del Personale."));
				CdrBulk cdrPersonaleBulk = (CdrBulk)getHome(userContext, CdrBulk.class).findByPrimaryKey(new CdrBulk(cdrPersonale));

				Ass_pdg_variazione_cdrHome ass_cdrHome = (Ass_pdg_variazione_cdrHome)getHome(userContext,Ass_pdg_variazione_cdrBulk.class);
				java.util.Collection<Pdg_variazione_riga_gestBulk> dettagliSpesa = ass_cdrHome.findDettagliSpesa(variazione);
	
				for (Pdg_variazione_riga_gestBulk varStanzRiga : dettagliSpesa) {
					//verifico se si tratta di area
					CdrBulk cdrBulk = (CdrBulk)getHome(userContext, CdrBulk.class).findByPrimaryKey(new CdrBulk(varStanzRiga.getCd_cdr_assegnatario()));
					Unita_organizzativaBulk uoBulk = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdrBulk.getCd_unita_organizzativa()));
					boolean isUoArea = uoBulk.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_AREA);
					
					//verifico se si tratta di cdr Personale
					boolean isDettPersonale = uoBulk.getCd_unita_organizzativa().equals(cdrPersonaleBulk.getCd_unita_organizzativa());
					if (!isDettPersonale) {
						//verifico se si tratta di voce accentrata verso il personale
						Elemento_voceBulk voce = (Elemento_voceBulk)getHome(userContext, Elemento_voceBulk.class).findByPrimaryKey(varStanzRiga.getElemento_voce());
						Classificazione_vociBulk classif = (Classificazione_vociBulk)getHome(userContext, Classificazione_vociBulk.class).findByPrimaryKey(new Classificazione_vociBulk(voce.getId_classificazione()));
						isDettPersonale = classif.getFl_accentrato()&&cdrPersonale.equals(classif.getCdr_accentratore());
					}

					//recupero la GAE
					BulkHome lattHome = getHome(userContext, WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA");
					SQLBuilder sql = lattHome.createSQLBuilder();
	
					sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,varStanzRiga.getEsercizio());
					sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,varStanzRiga.getCd_cdr_assegnatario());
					sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA",SQLBuilder.EQUALS,varStanzRiga.getCd_linea_attivita());
						
					List<WorkpackageBulk> listGAE = lattHome.fetchAll(sql);

					if (listGAE.isEmpty() || listGAE.size()>1)
						throw new ApplicationException("Errore in fase di ricerca linea_attivita "+varStanzRiga.getEsercizio()+"/"+varStanzRiga.getCd_centro_responsabilita()+"/"+varStanzRiga.getCd_linea_attivita()+".");

					WorkpackageBulk linea = listGAE.get(0);

					//recupero il progetto per verificare se è scaduto
					ProgettoHome home = (ProgettoHome)getHome(userContext, ProgettoBulk.class);
					home.setFetchPolicy("it.cnr.contab.progettiric00.comp.ProgettoRicercaComponent.find");
					ProgettoBulk progetto = (ProgettoBulk)home.findByPrimaryKey(userContext, linea.getProgetto());
					getHomeCache(userContext).fetchAll(userContext);
					
					//effettuo controlli sulla validità del progetto
					Optional.of(progetto.getOtherField())
							.filter(el->el.isStatoApprovato()||el.isStatoChiuso())
							.orElseThrow(()->new ApplicationException("Attenzione! Il progetto "+progetto.getCd_progetto()
									+ " non risulta in stato approvato o chiuso. Variazione non consentita!"));
						
					if (progetto.getOtherField().isDatePianoEconomicoRequired()) {
						Optional.ofNullable(progetto.getOtherField().getDtInizio())
						 	    .orElseThrow(()->new ApplicationException("Attenzione! GAE "+linea.getCd_linea_attivita()+" non selezionabile. "
													+ "La data inizio del progetto non risulta impostata."));

						Optional.ofNullable(progetto.getOtherField().getDtFine())
					    .orElseThrow(()->new ApplicationException("Attenzione! GAE "+linea.getCd_linea_attivita()+" non selezionabile. "
											+ "La data fine del progetto non risulta impostata."));
						
						Optional.of(progetto.getOtherField().getDtInizio())
				 	    		.filter(dt->!dt.after(variazione.getDt_chiusura()))
								.orElseThrow(()->new ApplicationException("Attenzione! GAE "+linea.getCd_linea_attivita()+" non selezionabile. "
										+ "La data inizio ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(progetto.getOtherField().getDtInizio())
										+ ") del progetto "+progetto.getCd_progetto()+" associato è successiva "
										+ "rispetto alla data di chiusura della variazione ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(variazione.getDt_chiusura())+")."));
					}
						
					//recupero il record se presente altrimenti ne creo uno nuovo
					CtrlPianoEco pianoEco = listCtrlPianoEco.stream()
							.filter(el->el.getProgetto().getPg_progetto().equals(progetto.getPg_progetto()))
							.findFirst()
							.orElse(new CtrlPianoEco(progetto));

					//creo il dettaglio
					CtrlPianoEcoDett dett = new CtrlPianoEcoDett();
					dett.setImporto(varStanzRiga.getIm_variazione());
					dett.setCdrPersonale(isDettPersonale);
					dett.setUoArea(isUoArea);

					if (Optional.ofNullable(cdNaturaReimpiego).map(el->el.equals(linea.getNatura().getCd_natura())).orElse(Boolean.FALSE)) {
						dett.setTipoNatura(CtrlPianoEcoDett.TIPO_REIMPIEGO);
						if (!variazione.getTipo_variazione().isStorno()) 
							throw new ApplicationException("Attenzione! Risultano movimentazioni sulla GAE "
							    + linea.getCd_linea_attivita() + " con natura 6 - 'Reimpiego di risorse' "
								+ " consentito solo per operazioni di storno. Operazioni non possibile.");
					} else if (linea.getNatura().isFonteEsterna())
						dett.setTipoNatura(CtrlPianoEcoDett.TIPO_FONTE_ESTERNA);
					else
						dett.setTipoNatura(CtrlPianoEcoDett.TIPO_FONTE_INTERNA);
						
					dett.setVoceSpeciale(Optional.ofNullable(cdVoceSpeciale).map(el->el.equals(linea.getNatura().getCd_natura()))
													.orElse(Boolean.FALSE));

					pianoEco.getDett().add(dett);
					listCtrlPianoEco.add(pianoEco);
				}
				controllaPdgPianoEconomico(userContext, listCtrlPianoEco, variazione.getDt_chiusura(), cdVoceSpeciale);
			}
        } catch (DetailedRuntimeException _ex) {
            throw new ApplicationException(_ex.getMessage());
        } catch (PersistencyException|RemoteException|IntrospectionException e) {
			throw new ComponentException(e);
		}
	}

	private void controllaPdgPianoEconomico(UserContext userContext, List<CtrlPianoEco> listCtrlPianoEco, Timestamp dataChiusura, String cdVoceSpeciale) throws ComponentException{
		//CONTROLLI SU SINGOLO PROGETTO
		/**
		 * 1. se un progetto è scaduto non è possibile attribuire fondi
		 */
		listCtrlPianoEco.stream()
			.filter(el->el.isScaduto(dataChiusura))
			.filter(el->el.getImpPositivi().compareTo(BigDecimal.ZERO)>0)
			.findFirst().ifPresent(el->{
				throw new DetailedRuntimeException("Attenzione! Non è possibile attribuire fondi al progetto "+
						el.getProgetto().getCd_progetto()+
						" in quanto scaduto ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(el.getDtScadenza()) +
						") rispetto alla data di chiusura della variazione ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(dataChiusura)+").");});
		
		/**
		 * 2. se un progetto è attivo è possibile sottrarre fondi a GAE di natura 6 solo prelevandoli dallo stesso progetto 
		 *    da GAE di natura 6
		 */
		listCtrlPianoEco.stream()
			.filter(el->!el.isScaduto(dataChiusura))
			.filter(el->el.getImpNegativiNaturaReimpiego().compareTo(BigDecimal.ZERO)>0)
			.filter(el->el.getImpNegativiNaturaReimpiego().compareTo(el.getImpPositiviNaturaReimpiego())!=0)
			.findFirst().ifPresent(el->{
				throw new DetailedRuntimeException("Attenzione! Sono stati prelevati fondi dal progetto "+
						el.getProgetto().getCd_progetto()+"(" + 
						new it.cnr.contab.util.EuroFormat().format(el.getImpNegativiNaturaReimpiego()) +
						") da GAE di natura 6 - 'Reimpiego di risorse' non compensati da un'equivalente " +
						"assegnazione nell'ambito dello stesso progetto e della stessa natura ("+
						new it.cnr.contab.util.EuroFormat().format(el.getImpPositiviNaturaReimpiego()) + ")");});

		/**
		 * 3. se un progetto è aperto è possibile attribuire somme su GAE non di natura 6 solo se stornate dallo stesso progetto 
		 * 	  (regola non valida per progetti di Aree e CdrPersonale)
		 */
		listCtrlPianoEco.stream()
			.filter(el->!el.isScaduto(dataChiusura))
			.filter(el->el.getImpPositivi().subtract(el.getImpPositiviNaturaReimpiego())
						  .subtract(el.getImpPositiviArea().subtract(el.getImpPositiviAreaNaturaReimpiego()))
						  .subtract(el.getImpPositiviCdrPersonale())
						  .compareTo(BigDecimal.ZERO)>0)
			.filter(el->el.getImpPositivi().subtract(el.getImpPositiviNaturaReimpiego())
						  .subtract(el.getImpPositiviArea().subtract(el.getImpPositiviAreaNaturaReimpiego()))
						  .subtract(el.getImpPositiviCdrPersonale())
					      .compareTo(el.getImpNegativi().subtract(el.getImpNegativiNaturaReimpiego())
					    		     .subtract(el.getImpNegativiArea().subtract(el.getImpNegativiAreaNaturaReimpiego()))
					    		     .subtract(el.getImpNegativiCdrPersonale()))>0)
			.findFirst().ifPresent(el->{
			throw new DetailedRuntimeException("Attenzione! Sono stati attribuiti fondi al progetto "+
					el.getProgetto().getCd_progetto()+"(" + 
					new it.cnr.contab.util.EuroFormat().format(el.getImpPositivi().subtract(el.getImpPositiviNaturaReimpiego())
							  .subtract(el.getImpPositiviArea().subtract(el.getImpPositiviAreaNaturaReimpiego()))
							  .subtract(el.getImpPositiviCdrPersonale())) +
					") non compensati da un equivalente prelievo nell'ambito dello stesso progetto ("+
					new it.cnr.contab.util.EuroFormat().format(el.getImpNegativi().subtract(el.getImpNegativiNaturaReimpiego())
			    		     .subtract(el.getImpNegativiArea().subtract(el.getImpNegativiAreaNaturaReimpiego()))
			    		     .subtract(el.getImpNegativiCdrPersonale())) + ")");});

		/**
		 * 4. se un progetto è aperto e vengono sottratte somme ad un'area queste devono essere riassegnate 
		 *    allo stesso progetto e alla stessa area
		 */
		listCtrlPianoEco.stream()
			.filter(el->!el.isScaduto(dataChiusura))
			.filter(el->el.getImpNegativiArea().compareTo(BigDecimal.ZERO)>0)
			.filter(el->el.getImpNegativiArea().compareTo(el.getImpPositiviArea())>0)
			.findFirst().ifPresent(el->{
			throw new DetailedRuntimeException("Attenzione! Sono stati prelevati dall'area fondi dal progetto "+
					el.getProgetto().getCd_progetto()+"(" + 
					new it.cnr.contab.util.EuroFormat().format(el.getImpNegativiArea()) +
					") non compensati da un equivalente assegnazione nell'ambito dello stesso progetto e della stessa area ("+
					new it.cnr.contab.util.EuroFormat().format(el.getImpPositiviArea()) + ")");});

		/**
		 * 5. se un progetto è aperto e vengono sottratte somme al CDR Personale queste devono essere riassegnate 
		 *    allo stesso progetto e alla stesso CDR
		 */
		listCtrlPianoEco.stream()
			.filter(el->!el.isScaduto(dataChiusura))
			.filter(el->el.getImpNegativiCdrPersonale().compareTo(BigDecimal.ZERO)>0)
			.filter(el->el.getImpNegativiCdrPersonale().compareTo(el.getImpPositiviCdrPersonale())>0)
			.findFirst().ifPresent(el->{
			throw new DetailedRuntimeException("Attenzione! Sono stati prelevati dal CDR Personale fondi dal progetto "+
					el.getProgetto().getCd_progetto()+"(" + 
					new it.cnr.contab.util.EuroFormat().format(el.getImpNegativiCdrPersonale()) +
					") non compensati da un equivalente assegnazione nell'ambito dello stesso progetto e della stessa area ("+
					new it.cnr.contab.util.EuroFormat().format(el.getImpPositiviCdrPersonale()) + ")");});

		//CONTROLLI SUL TOTALE PROGETTI
		BigDecimal impNegativiPrgScaduti = listCtrlPianoEco.stream()
				.filter(el->el.isScaduto(dataChiusura))
				.filter(el->el.getImpNegativi().compareTo(BigDecimal.ZERO)>0)
				.map(CtrlPianoEco::getImpNegativi)
				.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
		
		{
		/**
		 * 6. se un progetto è scaduto se vengono sottratti importi devono essere girati a GaeNatura6 o al CDRPersonale
		 */
			BigDecimal impPositiviCashFund = listCtrlPianoEco.stream()
					.filter(el->!el.isScaduto(dataChiusura))
					.map(CtrlPianoEco::getDett)
					.flatMap(List::stream)
					.filter(el->el.isNaturaReimpiego()||el.isCdrPersonale())
					.filter(el->el.getImporto().compareTo(BigDecimal.ZERO)>0)
					.map(CtrlPianoEcoDett::getImporto)
					.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
			
			if (impNegativiPrgScaduti.compareTo(BigDecimal.ZERO)>0 && impNegativiPrgScaduti.compareTo(impPositiviCashFund)>0)
				throw new ApplicationException("Attenzione! Risultano prelievi da progetti scaduti"
						+ " per un importo di "	+ new it.cnr.contab.util.EuroFormat().format(impNegativiPrgScaduti)
						+ " che non risultano totalmente coperti da variazioni a favore"
						+ " di GAE di natura 6 - 'Reimpiego di risorse' o del CDR Personale ("
						+ new it.cnr.contab.util.EuroFormat().format(impPositiviCashFund)+").");
		}
		{
		/**
		 * 7. se un progetto è attivo se vengono sottratti importi su GAE natura 6 queste devono essere girate ad Aree di uguale Natura
		 */
			BigDecimal impSaldoPrgAttiviNaturaReimpiego = listCtrlPianoEco.stream()
					.filter(el->!el.isScaduto(dataChiusura))
					.map(CtrlPianoEco::getDett)
					.flatMap(List::stream)
					.filter(el->!el.isUoArea())
					.filter(el->!el.isCdrPersonale())
					.filter(el->el.isNaturaReimpiego())
					.map(CtrlPianoEcoDett::getImporto)
					.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

			if (impSaldoPrgAttiviNaturaReimpiego.compareTo(BigDecimal.ZERO)<0) {
				//Vuol dire che ho ridotto progetti attivi sulla natura 6 per cui deve essere bilanciato solo con Aree
				BigDecimal impSaldoPrgAttiviAreeNaturaReimpiego = listCtrlPianoEco.stream()
						.filter(el->!el.isScaduto(dataChiusura))
						.map(CtrlPianoEco::getDett)
						.flatMap(List::stream)
						.filter(el->el.isUoArea())
						.filter(el->el.isNaturaReimpiego())
						.map(CtrlPianoEcoDett::getImporto)
						.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

				if (impSaldoPrgAttiviAreeNaturaReimpiego.compareTo(BigDecimal.ZERO)<0 ||
						impSaldoPrgAttiviAreeNaturaReimpiego.abs().compareTo(impSaldoPrgAttiviNaturaReimpiego.abs())!=0)
					throw new ApplicationException("Attenzione! Risultano prelievi da progetti attivi"
							+ " per un importo di "	+ new it.cnr.contab.util.EuroFormat().format(impSaldoPrgAttiviNaturaReimpiego.abs())
							+ " su GAE di natura 6 che non risultano totalmente coperti da variazioni a favore"
							+ " di Aree su GAE di natura 6 ("
							+ new it.cnr.contab.util.EuroFormat().format(impSaldoPrgAttiviAreeNaturaReimpiego.abs())+").");						
			}
		}
		{
		/**
		 * 8. se un progetto è attivo se vengono sottratti importi su GAE natura FES queste devono essere girate ad Aree di uguale Natura o 
		 *    al CDR Personale
		 */
			BigDecimal impSaldoPrgAttiviFonteEsterna = listCtrlPianoEco.stream()
					.filter(el->!el.isScaduto(dataChiusura))
					.map(CtrlPianoEco::getDett)
					.flatMap(List::stream)
					.filter(el->!el.isUoArea())
					.filter(el->!el.isCdrPersonale())
					.filter(el->el.isNaturaFonteEsterna())
					.map(CtrlPianoEcoDett::getImporto)
					.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

			if (impSaldoPrgAttiviFonteEsterna.compareTo(BigDecimal.ZERO)<0) {
				//Vuol dire che ho ridotto progetti attivi sulle fonti esterne per cui deve essere bilanciato solo con Aree di uguale natura o
				// con CDR Personale
				BigDecimal impSaldoPrgAttiviCashFund = listCtrlPianoEco.stream()
						.filter(el->!el.isScaduto(dataChiusura))
						.map(CtrlPianoEco::getDett)
						.flatMap(List::stream)
						.filter(el->el.isUoArea()||el.isCdrPersonale())
						.filter(el->el.isUoArea()?el.isNaturaFonteEsterna():Boolean.TRUE)
						.map(CtrlPianoEcoDett::getImporto)
						.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

				if (impSaldoPrgAttiviCashFund.compareTo(BigDecimal.ZERO)<0 ||
						impSaldoPrgAttiviCashFund.abs().compareTo(impSaldoPrgAttiviFonteEsterna.abs())!=0)
					throw new ApplicationException("Attenzione! Risultano prelievi da progetti attivi"
							+ " per un importo di "	+ new it.cnr.contab.util.EuroFormat().format(impSaldoPrgAttiviFonteEsterna.abs())
							+ " su GAE Fonte Esterna che non risultano totalmente coperti da variazioni a favore"
							+ " di Aree su GAE Fonte Esterna o CDR Personale ("
							+ new it.cnr.contab.util.EuroFormat().format(impSaldoPrgAttiviCashFund.abs())+").");						
			}
		}
		BigDecimal impPositiviNaturaReimpiego = listCtrlPianoEco.stream()
				.filter(el->el.getImpPositiviNaturaReimpiego().compareTo(BigDecimal.ZERO)>0)
				.map(CtrlPianoEco::getImpPositiviNaturaReimpiego)
				.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
		
		{
			/**
			 * 2. è possibile attribuire fondi ad un progetto di natura 6 solo se ne vengono sottratti equivalenti da:
			 * 		a. un progetto scaduto
			 * 		b. dalla voce speciale (11048)
			 */
			if (impPositiviNaturaReimpiego.compareTo(BigDecimal.ZERO)>0) {
				BigDecimal impNegativiVoceSpecialePrgInCorso = listCtrlPianoEco.stream()
						.filter(el->!el.isScaduto(dataChiusura))
						.filter(el->el.getImpNegativiVoceSpeciale().compareTo(BigDecimal.ZERO)>0)
						.map(CtrlPianoEco::getImpNegativiVoceSpeciale)
						.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
				boolean existsPrgScaduti = impNegativiPrgScaduti.compareTo(BigDecimal.ZERO)>0;
				boolean existsVoceSpeciale = impNegativiVoceSpecialePrgInCorso.compareTo(BigDecimal.ZERO)>0;
				if (impPositiviNaturaReimpiego.compareTo(impNegativiPrgScaduti.add(impNegativiVoceSpecialePrgInCorso))!=0)
					throw new ApplicationException("Attenzione! Risultano trasferimenti a GAE di natura 6 - 'Reimpiego di risorse' "
							+ " per un importo di "	+ new it.cnr.contab.util.EuroFormat().format(impPositiviNaturaReimpiego)
							+ " che non corrisponde all'importo prelevato da"
							+ (existsPrgScaduti?" progetti scaduti ":"")
							+ (existsPrgScaduti&&existsVoceSpeciale?"e da":"")
							+ (existsVoceSpeciale?"lla Voce "+cdVoceSpeciale:"")
							+" ("
							+ new it.cnr.contab.util.EuroFormat().format(impNegativiPrgScaduti)+").");
			}
		}
		{
			/**
			 * 5. non è possibile attribuire fondi alla voce speciale (11048)
			 */
			BigDecimal impPositiviVoceSpeciale = listCtrlPianoEco.stream()
					.filter(el->el.getImpPositiviVoceSpeciale().compareTo(BigDecimal.ZERO)>0)
					.map(CtrlPianoEco::getImpPositiviVoceSpeciale)
					.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
			
			if (impPositiviVoceSpeciale.compareTo(BigDecimal.ZERO)>0)
				throw new ApplicationException("Attenzione! Non è possibile attribuire fondi alla voce "
						+ cdVoceSpeciale + " ("
						+ new it.cnr.contab.util.EuroFormat().format(impPositiviVoceSpeciale)+").");
		}
		{
			/**
			 * 6. se vengono spostate somme dalla voce speciale (11048) devono essere girate a GaeNatura6
			 */
			BigDecimal impNegativiVoceSpeciale = listCtrlPianoEco.stream()
					.filter(el->el.getImpNegativiVoceSpeciale().compareTo(BigDecimal.ZERO)>0)
					.map(CtrlPianoEco::getImpNegativiVoceSpeciale)
					.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
				if (impNegativiVoceSpeciale.compareTo(BigDecimal.ZERO)>0 && impNegativiVoceSpeciale.compareTo(impPositiviNaturaReimpiego)>0)
				throw new ApplicationException("Attenzione! Risultano prelievi dalla voce " + cdVoceSpeciale
						+ " per un importo di "	+ new it.cnr.contab.util.EuroFormat().format(impNegativiVoceSpeciale)
						+ " che non risultano totalmente coperto da variazioni a favore"
						+ " di GAE di natura 6 - 'Reimpiego di risorse' ("
						+ new it.cnr.contab.util.EuroFormat().format(impPositiviNaturaReimpiego)+").");
		}
	}
}
