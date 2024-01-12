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

package it.cnr.contab.doccont00.comp;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ejb.EJBException;import it.cnr.contab.config00.bulk.*;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.config00.latt.bulk.CostantiTi_gestione;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_variazioniBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneHome;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrHome;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestHome;
import it.cnr.contab.pdg01.bulk.Tipo_variazioneBulk;
import it.cnr.contab.prevent00.bulk.*;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_costiBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.progettiric00.core.bulk.*;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrBulk;
import it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrHome;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resHome;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaHome;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.ObjectNotFoundException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class SaldoComponent extends it.cnr.jada.comp.GenericComponent implements ISaldoMgr,Cloneable,Serializable
{
	private static class CtrlDispPianoEco {
		public CtrlDispPianoEco(ProgettoBulk progetto, Progetto_piano_economicoBulk progettoPianoEconomico) {
			super();
			this.progetto = progetto;
			this.progettoPianoEconomico = progettoPianoEconomico;
			this.impFinanziato = BigDecimal.ZERO;
			this.impCofinanziato = BigDecimal.ZERO;
		}
		private ProgettoBulk progetto;
		private Progetto_piano_economicoBulk progettoPianoEconomico;
		private BigDecimal impFinanziato;
		private BigDecimal impCofinanziato;
		public ProgettoBulk getProgetto() {
			return progetto;
		}
		public void setProgetto(ProgettoBulk progetto) {
			this.progetto = progetto;
		}
		public Progetto_piano_economicoBulk getProgettoPianoEconomico() {
			return progettoPianoEconomico;
		}
		public void setProgettoPianoEconomico(Progetto_piano_economicoBulk progettoPianoEconomico) {
			this.progettoPianoEconomico = progettoPianoEconomico;
		}
		public BigDecimal getImpFinanziato() {
			return impFinanziato;
		}
		public void setImpFinanziato(BigDecimal impFinanziato) {
			this.impFinanziato = impFinanziato;
		}
		public BigDecimal getImpCofinanziato() {
			return impCofinanziato;
		}
		public void setImpCofinanziato(BigDecimal impCofinanziato) {
			this.impCofinanziato = impCofinanziato;
		}
	}
	private static class CtrlPianoEcoDett {
		private String tipoNatura;
		private boolean isUoArea;
		private boolean isCdrPersonale;
		private boolean isUoRagioneria;
		private boolean isUoFiscale;
		private boolean isVoceSpeciale;
		private Elemento_voceBulk elementoVoce;
		private String tipoDett;
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
		public boolean isUoRagioneria() { return isUoRagioneria; }
		public void setUoRagioneria(boolean isUoRagioneria) { this.isUoRagioneria = isUoRagioneria; }
		public boolean isUoFiscale() {
			return isUoFiscale;
		}
		public void setUoFiscale(boolean uoFiscale) {
			isUoFiscale = uoFiscale;
		}
		public boolean isVoceSpeciale() {
			return isVoceSpeciale;
		}
		public void setVoceSpeciale(boolean isVoceSpeciale) {
			this.isVoceSpeciale = isVoceSpeciale;
		}
		public Elemento_voceBulk getElementoVoce() {
			return elementoVoce;
		}
		public void setElementoVoce(Elemento_voceBulk elementoVoce) {
			this.elementoVoce = elementoVoce;
		}
		public String getTipoDett() {
			return tipoDett;
		}
		public void setTipoDett(String tipoDett) {
			this.tipoDett = tipoDett;
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
		public boolean isTipoEntrata(){
			return Elemento_voceHome.GESTIONE_ENTRATE.equals(getTipoDett());
		}
		public boolean isTipoSpesa(){
			return Elemento_voceHome.GESTIONE_SPESE.equals(getTipoDett());
		}
	}
	private static class CtrlPianoEco {
		public CtrlPianoEco(ProgettoBulk progetto) {
			super();
			this.progetto = progetto;
		}
		private ProgettoBulk progetto;
		private Progetto_rimodulazioneBulk rimodulazione;

		private List<CtrlPianoEcoDett> dett = new ArrayList<>();
		
		public ProgettoBulk getProgetto() {
			return progetto;
		}
		public void setProgetto(ProgettoBulk progetto) {
			this.progetto = progetto;
		}
		public Progetto_rimodulazioneBulk getRimodulazione() {
			return rimodulazione;
		}
		public void setRimodulazione(Progetto_rimodulazioneBulk rimodulazione) {
			this.rimodulazione = rimodulazione;
		}
		public List<CtrlPianoEcoDett> getDett() {
			return dett;
		}
		public void setDett(List<CtrlPianoEcoDett> dett) {
			this.dett = dett;
		}
		public BigDecimal getImpSpesaPositivi() {
			return this.getImpSpesaPositivi(dett.stream());
		}
		public BigDecimal getImpSpesaNegativi() {
			return this.getImpSpesaNegativi(dett.stream());
		}
		public BigDecimal getImpSpesaPositiviArea() {
			return this.getImpSpesaPositivi(dett.stream().filter(CtrlPianoEcoDett::isUoArea));
		}
		public BigDecimal getImpSpesaNegativiArea() {
			return this.getImpSpesaNegativi(dett.stream().filter(CtrlPianoEcoDett::isUoArea));
		}
		public BigDecimal getImpSpesaPositiviAreaNaturaReimpiego() {
			return this.getImpSpesaPositivi(dett.stream().filter(CtrlPianoEcoDett::isUoArea).filter(CtrlPianoEcoDett::isNaturaReimpiego));
		}
		public BigDecimal getImpSpesaNegativiAreaNaturaReimpiego() {
			return this.getImpSpesaNegativi(dett.stream().filter(CtrlPianoEcoDett::isUoArea).filter(CtrlPianoEcoDett::isNaturaReimpiego));
		}
		public BigDecimal getImpSpesaPositiviRagioneria() {
			return this.getImpSpesaPositivi(dett.stream().filter(CtrlPianoEcoDett::isUoRagioneria));
		}
		public BigDecimal getImpSpesaNegativiRagioneria() {
			return this.getImpSpesaNegativi(dett.stream().filter(CtrlPianoEcoDett::isUoRagioneria));
		}
		public BigDecimal getImpSpesaPositiviUoFiscale() {
			return this.getImpSpesaPositivi(dett.stream().filter(CtrlPianoEcoDett::isUoFiscale));
		}
		public BigDecimal getImpSpesaNegativiUoFiscale() {
			return this.getImpSpesaNegativi(dett.stream().filter(CtrlPianoEcoDett::isUoFiscale));
		}
		public BigDecimal getImpSpesaPositiviCdrPersonale() {
			return this.getImpSpesaPositivi(dett.stream().filter(CtrlPianoEcoDett::isCdrPersonale));
		}
		public BigDecimal getImpSpesaNegativiCdrPersonale() {
			return this.getImpSpesaNegativi(dett.stream().filter(CtrlPianoEcoDett::isCdrPersonale));
		}
		public BigDecimal getImpSpesaPositiviNaturaReimpiego() {
			return this.getImpSpesaPositivi(dett.stream().filter(CtrlPianoEcoDett::isNaturaReimpiego));
		}
		public BigDecimal getImpSpesaNegativiNaturaReimpiego() {
			return this.getImpSpesaNegativi(dett.stream().filter(CtrlPianoEcoDett::isNaturaReimpiego));
		}
		public BigDecimal getImpSpesaPositiviVoceSpeciale() {
			return this.getImpSpesaPositivi(dett.stream().filter(CtrlPianoEcoDett::isVoceSpeciale));
		}
		public BigDecimal getImpSpesaNegativiVoceSpeciale() {
			return this.getImpSpesaNegativi(dett.stream().filter(CtrlPianoEcoDett::isVoceSpeciale));
		}
		public BigDecimal getImpEntrataPositivi(){
			return dett.stream().filter(CtrlPianoEcoDett::isTipoEntrata).map(CtrlPianoEcoDett::getImporto)
					.filter(importo -> importo.compareTo(BigDecimal.ZERO)>0).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
		}
		public BigDecimal getImpEntrataNegativi(){
			return dett.stream().filter(CtrlPianoEcoDett::isTipoEntrata).map(CtrlPianoEcoDett::getImporto)
					.filter(importo -> importo.compareTo(BigDecimal.ZERO)<0).reduce(BigDecimal::add).orElse(BigDecimal.ZERO).abs();
		}
		public Timestamp getDtScadenza() {
			return Optional.ofNullable(progetto.getOtherField().getDtProroga()).orElse(progetto.getOtherField().getDtFine());
		}
		public Timestamp getDtScadenzaRimodulata() {
			return Optional.ofNullable(this.getRimodulazione())
					.flatMap(el->Optional.ofNullable(Optional.ofNullable(el.getDtProroga()).orElse(el.getDtFine())))
					.orElse(null);
		}
		public boolean isScaduto(Timestamp dataRiferimento) {
			return Optional.ofNullable(Optional.ofNullable(this.getDtScadenzaRimodulata()).orElse(this.getDtScadenza()))
					.map(dt->dt.before(dataRiferimento)).orElse(Boolean.FALSE);
		}
		private BigDecimal getImpSpesaPositivi(Stream<CtrlPianoEcoDett> stream){
			return stream.filter(CtrlPianoEcoDett::isTipoSpesa).map(CtrlPianoEcoDett::getImporto)
					.filter(importo -> importo.compareTo(BigDecimal.ZERO)>0).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
		}
		private BigDecimal getImpSpesaNegativi(Stream<CtrlPianoEcoDett> stream){
			return stream.filter(CtrlPianoEcoDett::isTipoSpesa).map(CtrlPianoEcoDett::getImporto)
					.filter(importo -> importo.compareTo(BigDecimal.ZERO)<0).reduce(BigDecimal::add).orElse(BigDecimal.ZERO).abs();
		}
		/**
		 * Ritorna l'importo positivo della spesa al netto degli importi di Natura Reimpiego, Area e Cdr Personale
		 * Calcolo:
		 * 		Totale Importi Positivi
		 *    - Totale Importi Positivi di Natura Reimpiego
		 *    - Totale Importi Positivi di Area al netto della Natura Reimpiego (il cui valore è ricompreso al punto precedente)
		 *    - Totale Importi Positivi di CDR Personale
		 *    - Totale Importi Positivi Voce Speciale 11048 al netto della quota compresa in Aree o Personale (il cui valore è ricompreso nei punti precedente)
		 */
		public BigDecimal getImpSpesaPositiviNetti() {
			return this.getImpSpesaPositivi().subtract(this.getImpSpesaPositiviNaturaReimpiego())
					  .subtract(this.getImpSpesaPositiviArea().subtract(this.getImpSpesaPositiviAreaNaturaReimpiego()))
					  .subtract(this.getImpSpesaPositiviCdrPersonale())
					  .subtract(this.getImpSpesaPositivi(dett.stream()
								 .filter(CtrlPianoEcoDett::isVoceSpeciale)
								 .filter(el->!el.isCdrPersonale())
								 .filter(el->!el.isUoArea())));
		}

		/**
		 * Ritorna l'importo negativo della spesa al netto degli importi di Natura Reimpiego, Area e Cdr Personale
		 * Calcolo:
		 * 		Totale Importi Negativi
		 *    - Totale Importi Negativi di Natura Reimpiego
		 *    - Totale Importi Negativi di Area al netto della Natura Reimpiego (il cui valore è ricompreso al punto precedente)
		 *    - Totale Importi Negativi di CDR Personale
		 *    - Totale Importi Negativi Voce Speciale 11048 al netto della quota compresa in Aree o Personale (il cui valore è ricompreso nei punti precedente)
		 */
		public BigDecimal getImpSpesaNegativiNetti() {
			return this.getImpSpesaNegativi().subtract(this.getImpSpesaNegativiNaturaReimpiego())
					  .subtract(this.getImpSpesaNegativiArea().subtract(this.getImpSpesaNegativiAreaNaturaReimpiego()))
					  .subtract(this.getImpSpesaNegativiCdrPersonale())
					  .subtract(this.getImpSpesaNegativi(dett.stream()
							  								 .filter(CtrlPianoEcoDett::isVoceSpeciale)
							  								 .filter(el->!el.isCdrPersonale())
							  								 .filter(el->!el.isUoArea())));
		}
	}

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
			saldo.setUser( userContext.getUser());
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
			saldo.setUser( userContext.getUser());
	
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
			saldo.setUser( userContext.getUser());
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
  * @param cd_linea_attivita il codice del Workpackage per cui effettuare la ricerca del saldo
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui effettuare la ricerca del saldo
  * @param esercizio_res esercizio residuo per cui ricercare il saldo
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
  * @param esercizio esercizio per cui effettuare la ricerca del saldo
  * @param esercizio_res esercizio residuo per cui effettuare la ricerca del saldo
  * @param cd_cdr il codice del CDR per cui effettuare la ricerca del saldo
  * @param cd_linea_attivita il codice del Workpackage per cui effettuare la ricerca del saldo
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
  * @param cd_linea_attivita il codice del Workpackage per cui effettuare la ricerca del saldo
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param esercizio_res l'anno del residuo
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
  * @param tipo_residuo il tipo di residuo (proprio o improprio)
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
  * @param cd_linea_attivita il codice del Workpackage per cui effettuare la ricerca del saldo
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param esercizio_res l'anno del residuo
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
  * @param tipo_residuo il tipo di residuo (proprio o improprio)
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
  * @param cd_cdr il codice del CDR per cui effettuare la ricerca del saldo
  * @param cd_linea_attivita il codice del Workpackage per cui effettuare la ricerca del saldo
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui effettuare la verifica di disponibilità di cassa
  * @param importo l'importo (positivo o negativo) per cui effettuare la verifica di disponibilità di cassa
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
  * @param cd_linea_attivita il codice del Workpackage per cui effettuare la ricerca del saldo
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param esercizio_res l'anno del residuo
  * @param tipo_residuo il tipo di residuo (proprio o improprio)
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
  * @param tipo_doc_cont il tipo di documento contabile
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
  * @param cd_linea_attivita il codice del Workpackage per cui effettuare la ricerca del saldo
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
 * @param cd_cdr il codice del CDR per cui effettuare la ricerca del saldo dei residui
 * @param cd_linea_attivita il codice del Workpackage per cui effettuare la ricerca del saldo dei residui
 * @param voce la voce del piano per cui ricercare il saldo dei residui
*/
public java.math.BigDecimal getTotaleSaldoResidui(UserContext userContext, String cd_cdr, String cd_linea_attivita, IVoceBilancioBulk voce) throws ComponentException
{
	try
	{
		Pdg_vincoloHome home = (Pdg_vincoloHome)getHome(userContext, Pdg_vincoloBulk.class);
		java.math.BigDecimal saldoResiduo = new java.math.BigDecimal(0);
		Voce_f_saldi_cdr_lineaBulk comp = find( userContext, cd_cdr, cd_linea_attivita, voce);
		if (comp != null) {
			List residui = ((Voce_f_saldi_cdr_lineaHome)getHome( userContext,Voce_f_saldi_cdr_lineaBulk.class )).cercaDettagliResidui( comp );

			for (Iterator i = residui.iterator(); i.hasNext();) {
				Voce_f_saldi_cdr_lineaBulk residuo = (Voce_f_saldi_cdr_lineaBulk)i.next();
				saldoResiduo = saldoResiduo.add(residuo.getDispAdImpResiduoImproprio());

				//calcolo i vincoli e li sottraggo in quanto quota riservata
				List<Pdg_vincoloBulk> listVincoli = home.cercaDettagliVincolati(residuo);
				BigDecimal impVincolo = listVincoli.stream().map(e->e.getIm_vincolo()).reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
				saldoResiduo = saldoResiduo.subtract(impVincolo);
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
   	   		    Progetto_piano_economicoHome ppeHome = (Progetto_piano_economicoHome)getHome(userContext,Progetto_piano_economicoBulk.class);
   	   			List<Progetto_piano_economicoBulk> pianoEconomicoList = (List<Progetto_piano_economicoBulk>)ppeHome.findProgettoPianoEconomicoList(moduloCosti.getPg_progetto());
	
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
	
	                                if (!isFromChangeStato)
		                                dispResiduaFin = dispResiduaFin.subtract(
		                                        speseList.stream()
		                                        		.filter(x->Optional.ofNullable(x.getVoce_piano_economico()).isPresent())
		                                                .filter(x->x.getVoce_piano_economico().equalsByPrimaryKey(e.getVoce_piano_economico()))
		                                                .map(el->Utility.nvl(el.getIm_spese_gest_decentrata_est()))
		                                                .collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)));

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
	
	                                if (!isFromChangeStato)
		                                dispResiduaCofin = dispResiduaCofin.subtract(
		                                        speseList.stream()
		                                        		.filter(x->Optional.ofNullable(x.getVoce_piano_economico()).isPresent())
		                                                .filter(x->x.getVoce_piano_economico().equalsByPrimaryKey(e.getVoce_piano_economico()))
		                                                .map(el->Utility.nvl(el.getIm_spese_gest_decentrata_int()))
		                                                .collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)));

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
    
    //Controllo che restituisce errore.
	//Se la variazione passa a definitivo controllo che gli importi inseriti in variazione non superino la disponibilità residua.
	//Se la variazione passa ad approvato controllo solo che il piano economico non sia sfondato sul voci del piano economico movimentate dalla variazione
	public void checkDispPianoEconomicoProgetto(UserContext userContext, Pdg_variazioneBulk pdgVariazione) throws ComponentException
	{
		String message = getMessaggioSfondamentoPianoEconomico(userContext, pdgVariazione, true);
		if (message!=null && message.length()>0)
			throw new ApplicationException(
					"Impossibile effettuare l'operazione !\n"+message);
	}

	//Controllo che restituisce errore.
	//Se la variazione passa a definitivo controllo che gli importi inseriti in variazione non superino la disponibilità residua.
	//Se la variazione passa ad approvato controllo solo che il piano economico non sia sfondato sul voci del piano economico movimentate dalla variazione
	public void checkDispPianoEconomicoProgetto(UserContext userContext, Var_stanz_resBulk variazione) throws ComponentException
	{
		String message = getMessaggioSfondamentoPianoEconomico(userContext, variazione, true);
		if (message!=null && message.length()>0)
			throw new ApplicationException(
					"Impossibile effettuare l'operazione !\n"+message);
	}

    //Controllo che restituisce errore.
    //Se la variazione passa a definitivo controllo che gli importi inseriti in variazione non superino la disponibilità residua.
    //Se la variazione passa ad approvato controllo solo che il piano economico non sia sfondato sul voci del piano economico movimentate dalla variazione
    private String getMessaggioSfondamentoPianoEconomico(UserContext userContext, OggettoBulk variazione, boolean locked) throws ComponentException{
		StringJoiner messaggio = new StringJoiner("\n\n");

    	boolean isVariazioneCompetenza = Optional.ofNullable(variazione).map(Pdg_variazioneBulk.class::isInstance).orElse(Boolean.FALSE);
		boolean isVariazioneResidua = Optional.ofNullable(variazione).map(Var_stanz_resBulk.class::isInstance).orElse(Boolean.FALSE);

		if (!isVariazioneCompetenza && !isVariazioneResidua)
			throw new ApplicationException("Errore nella call del metodo getMessaggioSfondamentoPianoEconomico. Contattare il CED.");

        try {
        	Configurazione_cnrComponentSession configSession = Utility.createConfigurazioneCnrComponentSession();
			String cdNaturaReimpiego = configSession.getVal01(userContext, 0, null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_NATURA_REIMPIEGO);
			BigDecimal annoFrom = configSession.getIm01(userContext, 0, null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_PROGETTO_PIANO_ECONOMICO);

			Integer esercizioVariazione = Optional.ofNullable(isVariazioneCompetenza?Optional.of(variazione).map(Pdg_variazioneBulk.class::cast).map(Pdg_variazioneBulk::getEsercizio).orElse(null)
																				    :Optional.of(variazione).map(Var_stanz_resBulk.class::cast).map(Var_stanz_resBulk::getEsercizio_residuo).orElse(null))
					                              .orElseThrow(()->new ApplicationException("Esercizio Variazione non individuata."));

			if (Optional.ofNullable(annoFrom).map(BigDecimal::intValue).filter(el->el.compareTo(esercizioVariazione)<=0).isPresent()) {
				List<CtrlDispPianoEco> listCtrlDispPianoEcoEtr = new ArrayList<>();

				Pdg_variazioneHome detHome = (Pdg_variazioneHome)getHome(userContext,Pdg_variazioneBulk.class);
				Var_stanz_resHome varResHome = (Var_stanz_resHome)getHome(userContext,Var_stanz_resBulk.class);
				Progetto_piano_economicoHome ppeHome = (Progetto_piano_economicoHome)getHome(userContext,Progetto_piano_economicoBulk.class);

				Optional<Progetto_rimodulazioneBulk> optProgettoRimodulazioneVariazione =
						isVariazioneCompetenza?Optional.of(variazione).map(Pdg_variazioneBulk.class::cast).map(Pdg_variazioneBulk::getProgettoRimodulazione)
											  :Optional.of(variazione).map(Var_stanz_resBulk.class::cast).map(Var_stanz_resBulk::getProgettoRimodulazione);

				ProgettoBulk progettoRimodulato =  null;
				if (optProgettoRimodulazioneVariazione.isPresent()) {
					Progetto_rimodulazioneHome prgHome = (Progetto_rimodulazioneHome)getHome(userContext, Progetto_rimodulazioneBulk.class);
					Progetto_rimodulazioneBulk rimodulazione = prgHome.rebuildRimodulazione(userContext, optProgettoRimodulazioneVariazione.get());
					progettoRimodulato = (ProgettoBulk)prgHome.getProgettoRimodulato(rimodulazione).clone();
				}

				if (isVariazioneCompetenza) {
					for (java.util.Iterator dett = detHome.findDettagliEntrataVariazioneGestionale((Pdg_variazioneBulk)variazione).iterator(); dett.hasNext(); ) {
						Pdg_variazione_riga_gestBulk rigaVar = (Pdg_variazione_riga_gestBulk) dett.next();

						WorkpackageBulk latt = ((WorkpackageHome) getHome(userContext, WorkpackageBulk.class)).searchGAECompleta(userContext, CNRUserContext.getEsercizio(userContext),
								rigaVar.getLinea_attivita().getCd_centro_responsabilita(), rigaVar.getLinea_attivita().getCd_linea_attivita());
						ProgettoBulk progetto = latt.getProgetto();

						if (Optional.ofNullable(progettoRimodulato).filter(prgRim -> !prgRim.getPg_progetto().equals(progetto.getPg_progetto())).isPresent())
							messaggio.add("Progetto " + progetto.getCd_progetto() + ": " +
									"movimentazione non possibile in quanto la variazione è di tipo rimodulazione di altro progetto (" +
									progettoRimodulato.getCd_progetto() + ").");

						BigDecimal imVariazioneFin = Utility.nvl(rigaVar.getIm_entrata());

						//recupero il record se presente altrimenti ne creo uno nuovo
						CtrlDispPianoEco dispPianoEco = listCtrlDispPianoEcoEtr.stream()
								.filter(el -> el.getProgetto().getPg_progetto().equals(progetto.getPg_progetto()))
								.findFirst()
								.orElse(new CtrlDispPianoEco(progetto, null));

						dispPianoEco.setImpFinanziato(dispPianoEco.getImpFinanziato().add(imVariazioneFin));

						if (!listCtrlDispPianoEcoEtr.contains(dispPianoEco))
							listCtrlDispPianoEcoEtr.add(dispPianoEco);
					}

					for (CtrlDispPianoEco ctrlDispPianoEco : listCtrlDispPianoEcoEtr) {
						ProgettoBulk progetto = ctrlDispPianoEco.getProgetto();
						BigDecimal totFinanziato;

						List<Progetto_piano_economicoBulk> pianoEconomicoList = null;
						if (Optional.ofNullable(progettoRimodulato).isPresent())
							pianoEconomicoList = progettoRimodulato.getAllDetailsProgettoPianoEconomico();
						else
							pianoEconomicoList = (List<Progetto_piano_economicoBulk>) ppeHome.findProgettoPianoEconomicoList(progetto.getPg_progetto());

						// Il controllo puntuale sul piano economico deve partire se:
						// 1) sul progetto esiste un piano economico per l'anno della variazione.
						// 2) l'anno della variazione è gestita (<=annoFrom)
						// 3) l'anno della variazione rientra nel periodo di validità del progetto, ovvero esiste almeno un dettaglio per l'anno della variazione stessa
						// In caso contrario viene controllato solo l'importo complessivo del progetto
						boolean ctrlFinanziamentoAnnuale = progetto.isPianoEconomicoRequired() &&
								(Optional.ofNullable(progetto)
										 .flatMap(prg -> Optional.ofNullable(prg.getOtherField()))
										 .filter(of -> of.getAnnoInizio() <= esercizioVariazione)
										 .filter(of -> of.getAnnoFine() >= esercizioVariazione)
										 .isPresent() ||
								 Optional.ofNullable(pianoEconomicoList).orElse(new ArrayList<>()).stream()
										 .filter(el -> el.getEsercizio_piano().equals(esercizioVariazione)).findAny().isPresent());

						if (ctrlFinanziamentoAnnuale) {
							totFinanziato = pianoEconomicoList.stream()
									.filter(el -> el.getEsercizio_piano().equals(esercizioVariazione))
									.map(Progetto_piano_economicoBulk::getIm_spesa_finanziato)
									.reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO);
						} else {
							totFinanziato = Optional.ofNullable(progetto.getOtherField())
									.map(Progetto_other_fieldBulk::getImFinanziato).orElse(BigDecimal.ZERO);
						}

						//Controllo che la quota finanziata sia almeno pari alle entrate del progetto
						BigDecimal assestatoEtrPrg = this.getStanziamentoAssestatoProgetto(userContext, progetto, Elemento_voceHome.GESTIONE_ENTRATE,
								(ctrlFinanziamentoAnnuale ? esercizioVariazione: null), null, null);

						if (totFinanziato.compareTo(assestatoEtrPrg.add(ctrlDispPianoEco.getImpFinanziato())) < 0)
							messaggio.add("Progetto " + progetto.getCd_progetto() + ": " +
									(ctrlFinanziamentoAnnuale ? "per l'esercizio " + esercizioVariazione + " " : "") +
									"l'assestato entrate " + (ctrlFinanziamentoAnnuale ? "" : "totale ") + "(" +
									new it.cnr.contab.util.EuroFormat().format(assestatoEtrPrg.add(ctrlDispPianoEco.getImpFinanziato())) +
									") non può essere superiore alla quota finanziata (" +
									new it.cnr.contab.util.EuroFormat().format(totFinanziato) + ").");

						if (progetto.getOtherField().getTipoFinanziamento().getFlAssociaContratto() && !progetto.getOtherField().getFlControlliDisabled()) {
							//Recupero la lista dei contratti attivi collegati al progetto
							ProgettoHome progettoHome = (ProgettoHome) getHome(userContext, ProgettoBulk.class);
							java.util.Collection<ContrattoBulk> contrattiAssociati = progettoHome.findContratti(progetto.getPg_progetto());

							BigDecimal impContrattiAttivi = contrattiAssociati.stream()
									.filter(el -> el.isAttivo() || el.isAttivo_e_Passivo())
									.filter(el -> !el.getDt_stipula().after(((Pdg_variazioneBulk)variazione).getDt_chiusura()))
									.map(ContrattoBulk::getIm_contratto_attivo)
									.reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO);

							if (impContrattiAttivi.compareTo(assestatoEtrPrg.add(ctrlDispPianoEco.getImpFinanziato())) < 0)
								messaggio.add("Progetto " + progetto.getCd_progetto() + ": " +
										(ctrlFinanziamentoAnnuale ? "per l'esercizio " + esercizioVariazione + " " : "") +
										"l'assestato entrate " + (ctrlFinanziamentoAnnuale ? "" : "totale ") + "(" +
										new it.cnr.contab.util.EuroFormat().format(assestatoEtrPrg.add(ctrlDispPianoEco.getImpFinanziato())) +
										") non può essere superiore alla somma dei contratti associati al progetto (" +
										new it.cnr.contab.util.EuroFormat().format(impContrattiAttivi) +
										") stipulati in data precedente rispetto alla data di chiusura della variazione (" +
										new java.text.SimpleDateFormat("dd/MM/yyyy").format(((Pdg_variazioneBulk)variazione).getDt_chiusura()) + ").");
						}
					}
				}

				List<CtrlDispPianoEco> listCtrlDispPianoEco = new ArrayList<CtrlDispPianoEco>();

				if (isVariazioneCompetenza) {
					for (java.util.Iterator dett = detHome.findDettagliSpesaVariazioneGestionale((Pdg_variazioneBulk) variazione).iterator(); dett.hasNext(); ) {
						Pdg_variazione_riga_gestBulk rigaVar = (Pdg_variazione_riga_gestBulk) dett.next();

						WorkpackageBulk latt = ((WorkpackageHome) getHome(userContext, WorkpackageBulk.class)).searchGAECompleta(userContext, CNRUserContext.getEsercizio(userContext),
								rigaVar.getLinea_attivita().getCd_centro_responsabilita(), rigaVar.getLinea_attivita().getCd_linea_attivita());
						ProgettoBulk progetto = latt.getProgetto();

						if (Optional.ofNullable(progettoRimodulato).filter(prgRim -> !prgRim.getPg_progetto().equals(progetto.getPg_progetto())).isPresent())
							messaggio.add("Progetto " + progetto.getCd_progetto() + ": " +
									"movimentazione non possibile in quanto la variazione è di tipo rimodulazione di altro progetto (" +
									progettoRimodulato.getCd_progetto() + ").");

						//Il progetto viene controllato solo se non è scaduto (anno fine <= anno variazione) o, se scaduto,
						// la variazione non è di tipo Maggiori Entrate/Spese nell'ambito del CDS
						if (Optional.ofNullable(progetto.getOtherField().getAnnoFine())
								.filter(annoFine -> annoFine.compareTo(esercizioVariazione) >= 0).isPresent() ||
								!Tipo_variazioneBulk.VARIAZIONE_POSITIVA_STESSO_ISTITUTO.equals(((Pdg_variazioneBulk) variazione).getTipologia())) {
							boolean isNaturaReimpiego = Optional.ofNullable(cdNaturaReimpiego).map(el -> el.equals(latt.getCd_natura()))
									.orElse(Boolean.FALSE);

							BigDecimal imSpeseInterne = Utility.nvl(rigaVar.getIm_spese_gest_decentrata_int()).compareTo(BigDecimal.ZERO) != 0
									? rigaVar.getIm_spese_gest_decentrata_int() : Utility.nvl(rigaVar.getIm_spese_gest_accentrata_int());
							BigDecimal imSpeseEsterne = Utility.nvl(rigaVar.getIm_spese_gest_decentrata_est()).compareTo(BigDecimal.ZERO) != 0
									? rigaVar.getIm_spese_gest_decentrata_est() : Utility.nvl(rigaVar.getIm_spese_gest_accentrata_est());

							BigDecimal imVariazioneFin = BigDecimal.ZERO;
							BigDecimal imVariazioneCofin = imSpeseInterne;
							if (isNaturaReimpiego)
								imVariazioneCofin = imVariazioneCofin.add(imSpeseEsterne);
							else
								imVariazioneFin = imSpeseEsterne;

							List<Progetto_piano_economicoBulk> pianoEconomicoList = null;
							if (Optional.ofNullable(progettoRimodulato).isPresent())
								pianoEconomicoList = progettoRimodulato.getAllDetailsProgettoPianoEconomico().stream()
										.filter(ppe -> {
											return ppe.getVociBilancioAssociate().stream()
													.filter(ppeVoc -> ppeVoc.getEsercizio_voce().equals(rigaVar.getElemento_voce().getEsercizio()))
													.filter(ppeVoc -> ppeVoc.getTi_appartenenza().equals(rigaVar.getElemento_voce().getTi_appartenenza()))
													.filter(ppeVoc -> ppeVoc.getTi_gestione().equals(rigaVar.getElemento_voce().getTi_gestione()))
													.filter(ppeVoc -> ppeVoc.getCd_elemento_voce().equals(rigaVar.getElemento_voce().getCd_elemento_voce()))
													.findFirst().isPresent();
										}).collect(Collectors.toList());
							else
								pianoEconomicoList = (List<Progetto_piano_economicoBulk>) ppeHome.findProgettoPianoEconomicoList(esercizioVariazione, progetto.getPg_progetto(), rigaVar.getElemento_voce());

							// Il controllo puntuale sul piano economico deve partire se:
							// 1) sul progetto esiste un piano economico per l'anno della variazione.
							// 2) l'anno della variazione è gestita (<=annoFrom)
							// 3) l'anno della variazione rientra nel periodo di validità del progetto, ovvero esiste almeno un dettaglio per l'anno della variazione stessa
							// In caso contrario viene controllato solo l'importo complessivo del progetto
							boolean ctrlFinanziamentoAnnuale = progetto.isPianoEconomicoRequired() &&
									(Optional.ofNullable(progetto)
											.flatMap(prg -> Optional.ofNullable(prg.getOtherField()))
											.filter(of -> of.getAnnoInizio() <= esercizioVariazione)
											.filter(of -> of.getAnnoFine() >= esercizioVariazione)
											.isPresent() ||
									 Optional.ofNullable(pianoEconomicoList).orElse(new ArrayList<>()).stream()
											.filter(el -> el.getEsercizio_piano().equals(esercizioVariazione)).findAny().isPresent());

							if (ctrlFinanziamentoAnnuale) {
								if (pianoEconomicoList == null || pianoEconomicoList.isEmpty()) {
									//messaggio che non esce per rimodulazione progetto in quanto controllo effettuato in fase di approvaziomne ultima variazione
									if (!Optional.ofNullable(progettoRimodulato).isPresent())
										messaggio.add("Non risulta essere stato imputato alcun valore nel piano economico del progetto " + progetto.getCd_progetto() +
												" per la Voce " + rigaVar.getCd_elemento_voce() + ".");
								} else if (pianoEconomicoList.size() > 1)
									messaggio.add("La Voce " + rigaVar.getCd_elemento_voce() + " risulta associata a più voci di piano economico del progetto " +
											progetto.getCd_progetto() + ".");
								else {
									Progetto_piano_economicoBulk progettoPianoEconomico = pianoEconomicoList.get(0);

									if (progettoPianoEconomico.getFl_ctrl_disp() &&
										(progettoPianoEconomico.getEsercizio_piano().equals(0) ||
													progettoPianoEconomico.getEsercizio_piano().equals(rigaVar.getEsercizio()))) {
										//recupero il record se presente altrimenti ne creo uno nuovo
										CtrlDispPianoEco dispPianoEco = listCtrlDispPianoEco.stream()
												.filter(el -> el.getProgetto().getPg_progetto().equals(progetto.getPg_progetto()))
												.filter(el -> el.getProgettoPianoEconomico().getPg_progetto().equals(progetto.getPg_progetto()))
												.filter(el -> el.getProgettoPianoEconomico().getCd_unita_organizzativa().equals(progettoPianoEconomico.getCd_unita_organizzativa()))
												.filter(el -> el.getProgettoPianoEconomico().getCd_voce_piano().equals(progettoPianoEconomico.getCd_voce_piano()))
												.findFirst()
												.orElse(new CtrlDispPianoEco(progetto, progettoPianoEconomico));

										dispPianoEco.setImpFinanziato(dispPianoEco.getImpFinanziato().add(imVariazioneFin));
										dispPianoEco.setImpCofinanziato(dispPianoEco.getImpCofinanziato().add(imVariazioneCofin));

										if (!listCtrlDispPianoEco.contains(dispPianoEco))
											listCtrlDispPianoEco.add(dispPianoEco);
									}
								}
							} else {
								//recupero il record se presente altrimenti ne creo uno nuovo
								CtrlDispPianoEco dispPianoEco = listCtrlDispPianoEco.stream()
										.filter(el -> el.getProgetto().getPg_progetto().equals(progetto.getPg_progetto()))
										.filter(el -> !Optional.ofNullable(el.getProgettoPianoEconomico()).isPresent())
										.findFirst()
										.orElse(new CtrlDispPianoEco(progetto, null));

								dispPianoEco.setImpFinanziato(dispPianoEco.getImpFinanziato().add(imVariazioneFin));
								dispPianoEco.setImpCofinanziato(dispPianoEco.getImpCofinanziato().add(imVariazioneCofin));

								if (!listCtrlDispPianoEco.contains(dispPianoEco))
									listCtrlDispPianoEco.add(dispPianoEco);
							}
						}
					}
				} else {
					for (java.util.Iterator dett = varResHome.findAllVariazioniRiga((Var_stanz_resBulk) variazione).iterator(); dett.hasNext(); ) {
						Var_stanz_res_rigaBulk rigaVar = (Var_stanz_res_rigaBulk) dett.next();

						WorkpackageBulk latt = ((WorkpackageHome) getHome(userContext, WorkpackageBulk.class)).searchGAECompleta(userContext, CNRUserContext.getEsercizio(userContext),
								rigaVar.getLinea_di_attivita().getCd_centro_responsabilita(), rigaVar.getLinea_di_attivita().getCd_linea_attivita());
						ProgettoBulk progetto = latt.getProgetto();

						if (Optional.ofNullable(progettoRimodulato).filter(prgRim -> !prgRim.getPg_progetto().equals(progetto.getPg_progetto())).isPresent())
							messaggio.add("Progetto " + progetto.getCd_progetto() + ": " +
									"movimentazione non possibile in quanto la variazione è di tipo rimodulazione di altro progetto (" +
									progettoRimodulato.getCd_progetto() + ").");

						boolean isNaturaReimpiego = Optional.ofNullable(cdNaturaReimpiego).map(el -> el.equals(latt.getCd_natura()))
								.orElse(Boolean.FALSE);

						BigDecimal imVariazioneFin = BigDecimal.ZERO;
						BigDecimal imVariazioneCofin = BigDecimal.ZERO;

						if (Optional.ofNullable(latt.getNatura()).map(NaturaBulk::isFonteInterna).orElse(Boolean.FALSE) || isNaturaReimpiego)
							imVariazioneCofin = Utility.nvl(rigaVar.getIm_variazione());
						else if (Optional.ofNullable(latt.getNatura()).map(NaturaBulk::isFonteEsterna).orElse(Boolean.FALSE))
							imVariazioneFin = Utility.nvl(rigaVar.getIm_variazione());

						List<Progetto_piano_economicoBulk> pianoEconomicoList = null;
						if (Optional.ofNullable(progettoRimodulato).isPresent())
							pianoEconomicoList = progettoRimodulato.getAllDetailsProgettoPianoEconomico().stream()
									.filter(ppe -> {
										return ppe.getVociBilancioAssociate().stream()
												.filter(ppeVoc -> ppeVoc.getEsercizio_voce().equals(rigaVar.getEsercizio_res()))
												.filter(ppeVoc -> ppeVoc.getTi_appartenenza().equals(rigaVar.getElemento_voce().getTi_appartenenza()))
												.filter(ppeVoc -> ppeVoc.getTi_gestione().equals(rigaVar.getElemento_voce().getTi_gestione()))
												.filter(ppeVoc -> ppeVoc.getCd_elemento_voce().equals(rigaVar.getElemento_voce().getCd_elemento_voce()))
												.findFirst().isPresent();
									}).collect(Collectors.toList());
						else
							pianoEconomicoList = (List<Progetto_piano_economicoBulk>) ppeHome.findProgettoPianoEconomicoList(esercizioVariazione, progetto.getPg_progetto(), rigaVar.getElemento_voce());

						// Il controllo puntuale sul piano economico deve partire se:
						// 1) sul progetto esiste un piano economico per l'anno della variazione.
						// 2) l'anno della variazione è gestita (<=annoFrom)
						// 3) l'anno della variazione rientra nel periodo di validità del progetto, ovvero esiste almeno un dettaglio per l'anno della variazione stessa
						// In caso contrario viene controllato solo l'importo complessivo del progetto
						boolean ctrlFinanziamentoAnnuale = progetto.isPianoEconomicoRequired() &&
								(Optional.ofNullable(progetto)
										.flatMap(prg -> Optional.ofNullable(prg.getOtherField()))
										.filter(of -> of.getAnnoInizio() <= esercizioVariazione)
										.filter(of -> of.getAnnoFine() >= esercizioVariazione)
										.isPresent() ||
								 Optional.ofNullable(pianoEconomicoList).orElse(new ArrayList<>()).stream()
										.filter(el -> el.getEsercizio_piano().equals(esercizioVariazione)).findAny().isPresent());

						if (ctrlFinanziamentoAnnuale) {
							if (pianoEconomicoList == null || pianoEconomicoList.isEmpty()) {
								//messaggio che non esce per rimodulazione progetto in quanto controllo effettuato in fase di approvaziomne ultima variazione
								if (!Optional.ofNullable(progettoRimodulato).isPresent())
									messaggio.add("Non risulta essere stato imputato alcun valore nel piano economico del progetto " + progetto.getCd_progetto() +
											" per la Voce " + rigaVar.getCd_elemento_voce() + ".");
							} else if (pianoEconomicoList.size() > 1)
								messaggio.add("La Voce " + rigaVar.getCd_elemento_voce() + " risulta associata a più voci di piano economico del progetto " +
										progetto.getCd_progetto() + ".");
							else {
								Progetto_piano_economicoBulk progettoPianoEconomico = pianoEconomicoList.get(0);

								if (progettoPianoEconomico.getFl_ctrl_disp() &&
										(progettoPianoEconomico.getEsercizio_piano().equals(0) ||
												progettoPianoEconomico.getEsercizio_piano().equals(esercizioVariazione))) {
									//recupero il record se presente altrimenti ne creo uno nuovo
									CtrlDispPianoEco dispPianoEco = listCtrlDispPianoEco.stream()
											.filter(el -> el.getProgetto().getPg_progetto().equals(progetto.getPg_progetto()))
											.filter(el -> Optional.ofNullable(el.getProgettoPianoEconomico()).isPresent())
											.filter(el -> el.getProgettoPianoEconomico().getPg_progetto().equals(progetto.getPg_progetto()))
											.filter(el -> el.getProgettoPianoEconomico().getCd_unita_organizzativa().equals(progettoPianoEconomico.getCd_unita_organizzativa()))
											.filter(el -> el.getProgettoPianoEconomico().getCd_voce_piano().equals(progettoPianoEconomico.getCd_voce_piano()))
											.findFirst()
											.orElse(new CtrlDispPianoEco(progetto, progettoPianoEconomico));

									dispPianoEco.setImpFinanziato(dispPianoEco.getImpFinanziato().add(imVariazioneFin));
									dispPianoEco.setImpCofinanziato(dispPianoEco.getImpCofinanziato().add(imVariazioneCofin));

									if (!listCtrlDispPianoEco.contains(dispPianoEco))
										listCtrlDispPianoEco.add(dispPianoEco);
								}
							}
						} else {
							//recupero il record se presente altrimenti ne creo uno nuovo
							CtrlDispPianoEco dispPianoEco = listCtrlDispPianoEco.stream()
									.filter(el -> el.getProgetto().getPg_progetto().equals(progetto.getPg_progetto()))
									.filter(el -> !Optional.ofNullable(el.getProgettoPianoEconomico()).isPresent())
									.findFirst()
									.orElse(new CtrlDispPianoEco(progetto, null));

							dispPianoEco.setImpFinanziato(dispPianoEco.getImpFinanziato().add(imVariazioneFin));
							dispPianoEco.setImpCofinanziato(dispPianoEco.getImpCofinanziato().add(imVariazioneCofin));

							if (!listCtrlDispPianoEco.contains(dispPianoEco))
								listCtrlDispPianoEco.add(dispPianoEco);
						}
					}
				}

				for (CtrlDispPianoEco ctrlDispPianoEco : listCtrlDispPianoEco) {
					Progetto_piano_economicoBulk ppe = ctrlDispPianoEco.getProgettoPianoEconomico();
					if (Optional.ofNullable(ppe).isPresent()) {
						try {
							if (locked) {
								Progetto_piano_economicoBulk bulkToFind = new Progetto_piano_economicoBulk();
								bulkToFind.setVoce_piano_economico(ppe.getVoce_piano_economico());
								bulkToFind.setPg_progetto(ppe.getPg_progetto());
								bulkToFind.setEsercizio_piano(ppe.getEsercizio_piano());
								try {
									bulkToFind = (Progetto_piano_economicoBulk) ppeHome.findAndLock(bulkToFind);
								} catch (ObjectNotFoundException ex) {}
							}

							if (Optional.ofNullable(progettoRimodulato).isPresent()) {
								//Controllo quota FINANZIATA
								BigDecimal imRimodulatoFin = progettoRimodulato.getAllDetailsProgettoPianoEconomico().stream()
										.filter(ppeRim->ppeRim.getEsercizio_piano().equals(ppe.getEsercizio_piano()))
										.filter(ppeRim->ppeRim.getCd_voce_piano().equals(ppe.getCd_voce_piano()))
										.filter(ppeRim->ppeRim.getCd_unita_organizzativa().equals(ppe.getCd_unita_organizzativa()))
										.map(Progetto_piano_economicoBulk::getIm_spesa_finanziato)
										.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

								BigDecimal imStanziatoFin = progettoRimodulato.getAllDetailsProgettoPianoEconomico().stream()
										.filter(ppeRim->ppeRim.getEsercizio_piano().equals(ppe.getEsercizio_piano()))
										.filter(ppeRim->ppeRim.getCd_voce_piano().equals(ppe.getCd_voce_piano()))
										.filter(ppeRim->ppeRim.getCd_unita_organizzativa().equals(ppe.getCd_unita_organizzativa()))
										.filter(ppeRim->Optional.ofNullable(ppeRim.getVociBilancioAssociate()).isPresent())
										.flatMap(ppeRim->ppeRim.getVociBilancioAssociate().stream())
										.filter(ppeRim->Optional.ofNullable(ppeRim.getSaldoSpesa()).isPresent())
										.map(Ass_progetto_piaeco_voceBulk::getSaldoSpesa)
										.map(el->el.getAssestatoFinanziamento())
										.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

								//Se l'importo stanziato è superiore a quello rimodulato, l'importo della variazione deve essere negativa al fine di riportare
								// lo stanziato a quadrare con il rimodulato
								if (imStanziatoFin.compareTo(imRimodulatoFin)>0) {
									if (ctrlDispPianoEco.getImpFinanziato().compareTo(BigDecimal.ZERO)>0)
										messaggio.add("La variazione della quota finanziata stanziata del piano economico "+ppe.getCd_voce_piano()+
												" ("+new it.cnr.contab.util.EuroFormat().format(ctrlDispPianoEco.getImpFinanziato()) +
												") risulta essere positiva laddove la variazione richiesta dalla rimodulazione del progetto "+
												ctrlDispPianoEco.getProgetto().getCd_progetto() + " deve essere solo negativa.");
									//La variazione negativa non deve essere superiore a quanto richiesto dalla rimodulazione
									//Es. se imRimodulato=1000 e imStanziato=2000 la variazione negativa non deve essere superiore a -1000
									if (imRimodulatoFin.subtract(imStanziatoFin).subtract(ctrlDispPianoEco.getImpFinanziato()).compareTo(BigDecimal.ZERO)>0)
										 messaggio.add("La variazione della quota finanziata stanziata del piano economico "+ppe.getCd_voce_piano()+
												" ("+new it.cnr.contab.util.EuroFormat().format(ctrlDispPianoEco.getImpFinanziato()) +
												") risulta essere superiore alla variazione richiesta dalla rimodulazione del progetto "+
												ctrlDispPianoEco.getProgetto().getCd_progetto() +
												" ("+new it.cnr.contab.util.EuroFormat().format(imRimodulatoFin.subtract(imStanziatoFin)) + ").");
								} else {
									//La variazione non deve superare la disponibilità residua data dalla differenza tra imRimodulato e imStanziato
									if (imRimodulatoFin.subtract(imStanziatoFin).subtract(ctrlDispPianoEco.getImpFinanziato()).compareTo(BigDecimal.ZERO)<0)
										 messaggio.add("La disponibilità rimodulata della quota finanziata del piano economico "+ppe.getCd_voce_piano()+
												  " associato al progetto " + ctrlDispPianoEco.getProgetto().getCd_progetto() +
												  (ppe.getEsercizio_piano().equals(0)?"":" per l'esercizio "+ppe.getEsercizio_piano())+
												  " ("+new it.cnr.contab.util.EuroFormat().format(imRimodulatoFin.subtract(imStanziatoFin))+")"+
												  " non è sufficiente a coprire la variazione (" +
												  new it.cnr.contab.util.EuroFormat().format(ctrlDispPianoEco.getImpFinanziato()) + ").");
								}

								//Controllo quota COFINANZIATA
								BigDecimal imRimodulatoCofin = progettoRimodulato.getAllDetailsProgettoPianoEconomico().stream()
										.filter(ppeRim->ppeRim.getEsercizio_piano().equals(ppe.getEsercizio_piano()))
										.filter(ppeRim->ppeRim.getCd_voce_piano().equals(ppe.getCd_voce_piano()))
										.filter(ppeRim->ppeRim.getCd_unita_organizzativa().equals(ppe.getCd_unita_organizzativa()))
										.map(Progetto_piano_economicoBulk::getIm_spesa_cofinanziato)
										.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

								BigDecimal imStanziatoCofin = progettoRimodulato.getAllDetailsProgettoPianoEconomico().stream()
										.filter(ppeRim->ppeRim.getEsercizio_piano().equals(ppe.getEsercizio_piano()))
										.filter(ppeRim->ppeRim.getCd_voce_piano().equals(ppe.getCd_voce_piano()))
										.filter(ppeRim->ppeRim.getCd_unita_organizzativa().equals(ppe.getCd_unita_organizzativa()))
										.filter(ppeRim->Optional.ofNullable(ppeRim.getVociBilancioAssociate()).isPresent())
										.flatMap(ppeRim->ppeRim.getVociBilancioAssociate().stream())
										.filter(ppeRim->Optional.ofNullable(ppeRim.getSaldoSpesa()).isPresent())
										.map(Ass_progetto_piaeco_voceBulk::getSaldoSpesa)
										.map(el->el.getAssestatoCofinanziamento())
										.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

								//Se l'importo stanziato è superiore a quello rimodulato, l'importo della variazione deve essere negativa al fine di riportare lo stanziato a quadrare con il rimodulato
								if (imStanziatoCofin.compareTo(imRimodulatoCofin)>0) {
									if (ctrlDispPianoEco.getImpCofinanziato().compareTo(BigDecimal.ZERO)>0)
										messaggio.add("La variazione della quota cofinanziata stanziata del piano economico "+ppe.getCd_voce_piano()+
												" ("+new it.cnr.contab.util.EuroFormat().format(ctrlDispPianoEco.getImpCofinanziato()) +
												") risulta essere positiva laddove la variazione richiesta dalla rimodulazione del progetto "+
												ctrlDispPianoEco.getProgetto().getCd_progetto() + " deve essere solo negativa.");

									//La variazione negativa non deve essere superiore a quanto richiesto dalla rimodulazione
									//Es. se imRimodulato=1000 e imStanziato=2000 la variazione negativa non deve essere superiore a -1000
									if (imRimodulatoCofin.subtract(imStanziatoCofin).subtract(ctrlDispPianoEco.getImpCofinanziato()).compareTo(BigDecimal.ZERO)>0)
										messaggio.add("La variazione della quota cofinanziata stanziata del piano economico "+ppe.getCd_voce_piano()+
												" ("+new it.cnr.contab.util.EuroFormat().format(ctrlDispPianoEco.getImpCofinanziato()) +
												") risulta essere superiore alla variazione richiesta dalla rimodulazione del progetto "+
												ctrlDispPianoEco.getProgetto().getCd_progetto() +
												" ("+new it.cnr.contab.util.EuroFormat().format(imRimodulatoCofin.subtract(imStanziatoCofin)) + ").");
								} else {
									//La variazione non deve superare la disponibilità residua data dalla differenza tra imRimodulato e imStanzato
									if (imRimodulatoCofin.subtract(imStanziatoCofin).subtract(ctrlDispPianoEco.getImpCofinanziato()).compareTo(BigDecimal.ZERO)<0)
										messaggio.add("La disponibilità rimodulata della quota cofinanziata del piano economico "+ppe.getCd_voce_piano()+
												" associato al progetto " + ctrlDispPianoEco.getProgetto().getCd_progetto() +
												(ppe.getEsercizio_piano().equals(0)?"":" per l'esercizio "+ppe.getEsercizio_piano())+
												" ("+new it.cnr.contab.util.EuroFormat().format(imRimodulatoCofin.subtract(imStanziatoCofin))+")"+
												" non è sufficiente a coprire la variazione (" +
												new it.cnr.contab.util.EuroFormat().format(ctrlDispPianoEco.getImpCofinanziato()) + ").");
								}
							} else {
								V_saldi_piano_econom_progettoBulk saldo = ((V_saldi_piano_econom_progettoHome)getHome( userContext,V_saldi_piano_econom_progettoBulk.class )).
										cercaSaldoPianoEconomico(ppe, "S");

								BigDecimal dispResiduaFin = Optional.ofNullable(saldo)
										.map(V_saldi_piano_econom_progettoBulk::getDispResiduaFinanziamento)
										.orElse(BigDecimal.ZERO)
										.subtract(ctrlDispPianoEco.getImpFinanziato());
								if (dispResiduaFin.compareTo(BigDecimal.ZERO)<0)
									messaggio.add("La disponibilità quota finanziata del piano economico "+ppe.getCd_voce_piano()+
											" associato al progetto " + ctrlDispPianoEco.getProgetto().getCd_progetto() +
											(ppe.getEsercizio_piano().equals(0)?"":" per l'esercizio "+ppe.getEsercizio_piano())+
											" ("+new it.cnr.contab.util.EuroFormat().format(Optional.ofNullable(saldo)
											.map(V_saldi_piano_econom_progettoBulk::getDispResiduaFinanziamento)
											.orElse(BigDecimal.ZERO))+")"+
											" non è sufficiente a coprire la variazione (" +
											new it.cnr.contab.util.EuroFormat().format(ctrlDispPianoEco.getImpFinanziato()) + ").");

								BigDecimal dispResiduaCofin = Optional.ofNullable(saldo)
										.map(V_saldi_piano_econom_progettoBulk::getDispResiduaCofinanziamento)
										.orElse(BigDecimal.ZERO)
										.subtract(ctrlDispPianoEco.getImpCofinanziato());
								if (dispResiduaCofin.compareTo(BigDecimal.ZERO)<0)
									messaggio.add("La disponibilità quota cofinanziata del piano economico "+ppe.getCd_voce_piano()+
											" associato al progetto " + ctrlDispPianoEco.getProgetto().getCd_progetto() +
											(ppe.getEsercizio_piano().equals(0)?"":" per l'esercizio "+ppe.getEsercizio_piano())+
											" ("+new it.cnr.contab.util.EuroFormat().format(saldo.getDispResiduaCofinanziamento())+")"+
											" non è sufficiente a coprire la variazione (" +
											new it.cnr.contab.util.EuroFormat().format(ctrlDispPianoEco.getImpCofinanziato()) + ").");
							}
						}
						catch (Exception ex ){
							throw new RuntimeException(  ex );
						}
					} else {
						ProgettoBulk prg = ctrlDispPianoEco.getProgetto();
						{
							BigDecimal totFinanziato = BigDecimal.ZERO;
							if (Optional.ofNullable(progettoRimodulato).isPresent())
								totFinanziato = progettoRimodulato.getImFinanziato();
							else
								totFinanziato = prg.getImFinanziato();

							BigDecimal assestatoSpePrgFes = this.getStanziamentoAssestatoProgetto(userContext, prg, Elemento_voceHome.GESTIONE_SPESE,
									null, null, Progetto_other_fieldHome.TI_IMPORTO_FINANZIATO);

							if (totFinanziato.compareTo(assestatoSpePrgFes.add(ctrlDispPianoEco.getImpFinanziato()))<0)
							   messaggio.add("Progetto " + prg.getCd_progetto() + ": l'assestato totale spese 'fonti esterne' ("+
									   new it.cnr.contab.util.EuroFormat().format(assestatoSpePrgFes.add(ctrlDispPianoEco.getImpFinanziato())) +
									   ") non può essere superiore alla quota finanziata (" +
									   new it.cnr.contab.util.EuroFormat().format(totFinanziato) + ").");
						}
						{
							BigDecimal totCofinanziato = BigDecimal.ZERO;
							if (Optional.ofNullable(progettoRimodulato).isPresent())
								totCofinanziato = progettoRimodulato.getImCofinanziato();
							else
								totCofinanziato = prg.getImCofinanziato();

							BigDecimal assestatoSpePrgReimpiego = this.getStanziamentoAssestatoProgetto(userContext, prg, Elemento_voceHome.GESTIONE_SPESE,
									null, null, Progetto_other_fieldHome.TI_IMPORTO_COFINANZIATO);

							if (totCofinanziato.compareTo(assestatoSpePrgReimpiego.add(ctrlDispPianoEco.getImpCofinanziato()))<0)
							   messaggio.add("Progetto " + prg.getCd_progetto() + ": l'assestato totale spese 'fonti interne' e 'natura reimpiego' ("+
										 new it.cnr.contab.util.EuroFormat().format(assestatoSpePrgReimpiego.add(ctrlDispPianoEco.getImpCofinanziato())) +
										 ") non può essere superiore alla quota cofinanziata (" +
										 new it.cnr.contab.util.EuroFormat().format(totCofinanziato) + ").");
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
        return messaggio.toString();
    }

	public void checkPdgPianoEconomico(UserContext userContext, Var_stanz_resBulk variazione) throws ComponentException{
		try {
			if (Utility.createParametriEnteComponentSession().isProgettoPianoEconomicoEnabled(userContext, CNRUserContext.getEsercizio(userContext))) {
				Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).
						findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));
				/*
				 * non effettuo alcun controllo se è collegata la UO Ente e la variazione è fatta dalla UO Ente
				 */
				if (uoScrivania.isUoEnte() && variazione.getCentroDiResponsabilita().getUnita_padre().isUoEnte())
					return;

				/*
				 * non effettuo alcun controllo se si tratta di variazione IVA
				 */
				PersistentHome homeLiqVar = getHome(userContext, Liquidazione_iva_variazioniBulk.class);
				SQLBuilder sqlLiqVar = homeLiqVar.createSQLBuilder();
				sqlLiqVar.addClause(FindClause.AND, "esercizio_variazione_res", SQLBuilder.EQUALS, variazione.getEsercizio());
				sqlLiqVar.addClause(FindClause.AND, "pg_variazione_res", SQLBuilder.EQUALS, variazione.getPg_variazione());
				if (sqlLiqVar.executeCountQuery(getConnection(userContext)) > 0)
					return;
				
				List<CtrlPianoEco> listCtrlPianoEco = new ArrayList<CtrlPianoEco>();

				it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configSession = Utility.createConfigurazioneCnrComponentSession();

				String cdNaturaReimpiego = configSession.getVal01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_NATURA_REIMPIEGO);
				String cdVoceSpeciale = configSession.getVal01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_ELEMENTO_VOCE_SPECIALE, Configurazione_cnrBulk.SK_TEMPO_IND_SU_PROGETTI_FINANZIATI);
				
				String cdrPersonale = Optional.ofNullable(((Configurazione_cnrHome)getHome(userContext,Configurazione_cnrBulk.class)).getCdrPersonale(CNRUserContext.getEsercizio(userContext)))
						.orElseThrow(() -> new ComponentException("Non è possibile individuare il codice CDR del Personale per l'esercizio "+CNRUserContext.getEsercizio(userContext)+"."));
				CdrBulk cdrPersonaleBulk = (CdrBulk)getHome(userContext, CdrBulk.class).findByPrimaryKey(new CdrBulk(cdrPersonale));

				String uoRagioneria = ((Configurazione_cnrHome)getHome(userContext,Configurazione_cnrBulk.class)).getUoRagioneria(CNRUserContext.getEsercizio(userContext));
				String uoFiscale = ((Configurazione_cnrHome)getHome(userContext,Configurazione_cnrBulk.class)).getUoFiscale(CNRUserContext.getEsercizio(userContext));

				Ass_var_stanz_res_cdrHome ass_cdrHome = (Ass_var_stanz_res_cdrHome)getHome(userContext,Ass_var_stanz_res_cdrBulk.class);
				java.util.Collection<Var_stanz_res_rigaBulk> dettagliSpesa = ass_cdrHome.findDettagliSpesa(variazione);
	
				for (Var_stanz_res_rigaBulk varStanzResRiga : dettagliSpesa) {
					//verifico se si tratta di area
					CdrBulk cdrBulk = (CdrBulk)getHome(userContext, CdrBulk.class).findByPrimaryKey(new CdrBulk(varStanzResRiga.getCd_cdr()));
					Unita_organizzativaBulk uoBulk = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdrBulk.getCd_unita_organizzativa()));
					boolean isUoArea = uoBulk.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_AREA);
					
					//verifico se si tratta di voce accentrata verso il personale
					Elemento_voceBulk voce = (Elemento_voceBulk)getHome(userContext, Elemento_voceBulk.class).findByPrimaryKey(varStanzResRiga.getElemento_voce());
					Classificazione_vociBulk classif = (Classificazione_vociBulk)getHome(userContext, Classificazione_vociBulk.class).findByPrimaryKey(new Classificazione_vociBulk(voce.getId_classificazione()));
					boolean isDettPersonale = classif.getFl_accentrato()&&cdrPersonale.equals(classif.getCdr_accentratore());

					boolean isUoRagioneria = uoBulk.getCd_unita_organizzativa().equals(uoRagioneria);
					boolean isUoFiscale = uoBulk.getCd_unita_organizzativa().equals(uoFiscale);

					//recupero la GAE
					WorkpackageBulk linea = ((WorkpackageHome)getHome(userContext, WorkpackageBulk.class)).searchGAECompleta(userContext,
							varStanzResRiga.getEsercizio(), varStanzResRiga.getCd_cdr(), varStanzResRiga.getCd_linea_attivita());

					Optional.ofNullable(linea)
					.orElseThrow(()->new ApplicationException("Errore in fase di ricerca linea_attivita "+varStanzResRiga.getEsercizio()+"/"+varStanzResRiga.getCd_cdr()+"/"+varStanzResRiga.getCd_linea_attivita()+"."));

					ProgettoBulk progetto = linea.getProgetto();

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
						
						//Nelle variazioni controllare la più piccola data tra data inizio progetto e data stipula contratto definitivo
						ProgettoHome progettoHome = (ProgettoHome)getHome(userContext, ProgettoBulk.class);
						java.util.Collection<ContrattoBulk> contratti = progettoHome.findContratti(progetto.getPg_progetto());

						Optional<ContrattoBulk> optContratto = 
								contratti.stream().filter(el->el.isAttivo()||el.isAttivo_e_Passivo())
								 .min((p1, p2) -> p1.getDt_stipula().compareTo(p2.getDt_stipula()))
				    			 .filter(el->el.getDt_stipula().before(progetto.getOtherField().getDtInizio()));
						
						if (optContratto.isPresent())
							optContratto
			 	    			.filter(ctr->ctr.getDt_stipula().after(variazione.getDt_chiusura()))
			 	    			.ifPresent(ctr->{
			 	    				throw new ApplicationRuntimeException(
			 	    						"Attenzione! GAE "+linea.getCd_linea_attivita()+" non selezionabile. "
  										  + "La data stipula ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(ctr.getDt_stipula())
										  + ") del primo contratto " + ctr.getEsercizio()+"/"+ctr.getStato()+"/"+ctr.getPg_contratto()
				    				   	  + " associato al progetto "+progetto.getCd_progetto()+" è successiva "
										  + "rispetto alla data di chiusura della variazione ("+
				    				   	  new java.text.SimpleDateFormat("dd/MM/yyyy").format(variazione.getDt_chiusura())+").");
				    			  });
						else
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

					if (variazione.isVariazioneRimodulazioneProgetto()) {
						if (variazione.getProgettoRimodulazione().getPg_progetto().compareTo(progetto.getPg_progetto()) != 0)
							throw new ApplicationException("Attenzione! Nella variazione residua " + variazione.getEsercizio() + "/" + variazione.getPg_variazione() + " risulta movimentato un progetto differente rispetto a quello della " +
									"rimodulazione associata. Operazione non possibile!");
						pianoEco.setRimodulazione(variazione.getProgettoRimodulazione());
					}

					//creo il dettaglio
					CtrlPianoEcoDett dett = new CtrlPianoEcoDett();
					dett.setTipoDett(varStanzResRiga.getTi_gestione());
					dett.setImporto(varStanzResRiga.getIm_variazione());
					dett.setCdrPersonale(isDettPersonale);
					dett.setUoArea(isUoArea);
					dett.setUoRagioneria(isUoRagioneria);
					dett.setUoFiscale(isUoFiscale);
					dett.setElementoVoce(varStanzResRiga.getElemento_voce());

					if (Optional.ofNullable(cdNaturaReimpiego).map(el->el.equals(linea.getNatura().getCd_natura())).orElse(Boolean.FALSE))
						dett.setTipoNatura(CtrlPianoEcoDett.TIPO_REIMPIEGO);
					else if (linea.getNatura().isFonteEsterna())
						dett.setTipoNatura(CtrlPianoEcoDett.TIPO_FONTE_ESTERNA);
					else
						dett.setTipoNatura(CtrlPianoEcoDett.TIPO_FONTE_INTERNA);
						
					dett.setVoceSpeciale(Optional.ofNullable(cdVoceSpeciale).map(el->el.equals(varStanzResRiga.getCd_elemento_voce()))
							.orElse(Boolean.FALSE));

					pianoEco.getDett().add(dett);
					if (!listCtrlPianoEco.contains(pianoEco))
						listCtrlPianoEco.add(pianoEco);
				}
				controllaPdgPianoEconomico(userContext, variazione, listCtrlPianoEco, cdVoceSpeciale, cdrPersonaleBulk);
			}
        } catch (DetailedRuntimeException _ex) {
            throw new ApplicationException(_ex.getMessage());
        } catch (PersistencyException|RemoteException|IntrospectionException|SQLException e) {
			throw new ComponentException(e);
		}
	}

	public void checkPdgPianoEconomico(UserContext userContext, Pdg_variazioneBulk variazione) throws ComponentException{
		try {
			if (Utility.createParametriEnteComponentSession().isProgettoPianoEconomicoEnabled(userContext, CNRUserContext.getEsercizio(userContext))) {
				Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).
						findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));
				/*
				 * non effettuo alcun controllo se è collegata la UO Ente e la variazione è fatta dalla UO Ente
				 * oppure
				 * la variazione è generata automaticamente (es. in fase di emissione obbligazione)
				 */
				if (variazione.getCentro_responsabilita().getUnita_padre().isUoEnte() &&
						(uoScrivania.isUoEnte() || Pdg_variazioneBulk.MOTIVAZIONE_VARIAZIONE_AUTOMATICA.equals(variazione.getTiMotivazioneVariazione())))
					return;

				/*
				 * non effettuo alcun controllo se si tratta di variazione IVA
				 */
				PersistentHome homeLiqVar = getHome(userContext, Liquidazione_iva_variazioniBulk.class);
				SQLBuilder sqlLiqVar = homeLiqVar.createSQLBuilder();
				sqlLiqVar.addClause(FindClause.AND, "esercizio_variazione_comp", SQLBuilder.EQUALS, variazione.getEsercizio());
				sqlLiqVar.addClause(FindClause.AND, "pg_variazione_comp", SQLBuilder.EQUALS, variazione.getPg_variazione_pdg());
				if (sqlLiqVar.executeCountQuery(getConnection(userContext)) > 0)
					return;

				it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configSession = Utility.createConfigurazioneCnrComponentSession();

				List<CtrlPianoEco> listCtrlPianoEco = new ArrayList<CtrlPianoEco>();

				String cdNaturaReimpiego = configSession.getVal01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_NATURA_REIMPIEGO);
				String cdVoceSpeciale = configSession.getVal01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_ELEMENTO_VOCE_SPECIALE, Configurazione_cnrBulk.SK_TEMPO_IND_SU_PROGETTI_FINANZIATI);

				String cdrPersonale = Optional.ofNullable(((Configurazione_cnrHome)getHome(userContext,Configurazione_cnrBulk.class)).getCdrPersonale(CNRUserContext.getEsercizio(userContext)))
						.orElseThrow(() -> new ComponentException("Non è possibile individuare il codice CDR del Personale."));
				CdrBulk cdrPersonaleBulk = (CdrBulk)getHome(userContext, CdrBulk.class).findByPrimaryKey(new CdrBulk(cdrPersonale));

				String uoRagioneria = ((Configurazione_cnrHome)getHome(userContext,Configurazione_cnrBulk.class)).getUoRagioneria(CNRUserContext.getEsercizio(userContext));
				String uoFiscale = ((Configurazione_cnrHome)getHome(userContext,Configurazione_cnrBulk.class)).getUoFiscale(CNRUserContext.getEsercizio(userContext));

				Ass_pdg_variazione_cdrHome ass_cdrHome = (Ass_pdg_variazione_cdrHome)getHome(userContext,Ass_pdg_variazione_cdrBulk.class);
				java.util.Collection<Pdg_variazione_riga_gestBulk> dettagliVariazione = ass_cdrHome.findDettagli(variazione);
	
				for (Pdg_variazione_riga_gestBulk varStanzRiga : dettagliVariazione) {
					//verifico se si tratta di area
					CdrBulk cdrBulk = (CdrBulk)getHome(userContext, CdrBulk.class).findByPrimaryKey(new CdrBulk(varStanzRiga.getCd_cdr_assegnatario()));
					Unita_organizzativaBulk uoBulk = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdrBulk.getCd_unita_organizzativa()));
					boolean isUoArea = uoBulk.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_AREA);
					
					//verifico se si tratta di voce accentrata verso il personale
					Elemento_voceBulk voce = (Elemento_voceBulk)getHome(userContext, Elemento_voceBulk.class).findByPrimaryKey(varStanzRiga.getElemento_voce());
					Classificazione_vociBulk classif = (Classificazione_vociBulk)getHome(userContext, Classificazione_vociBulk.class).findByPrimaryKey(new Classificazione_vociBulk(voce.getId_classificazione()));
					boolean isDettPersonale = classif.getFl_accentrato()&&cdrPersonale.equals(classif.getCdr_accentratore());

					boolean isUoRagioneria = uoBulk.getCd_unita_organizzativa().equals(uoRagioneria);
					boolean isUoFiscale = uoBulk.getCd_unita_organizzativa().equals(uoFiscale);

					//recupero la GAE
					WorkpackageBulk linea = ((WorkpackageHome)getHome(userContext, WorkpackageBulk.class)).searchGAECompleta(userContext,
							varStanzRiga.getEsercizio(), varStanzRiga.getCd_cdr_assegnatario(), varStanzRiga.getCd_linea_attivita());

					Optional.ofNullable(linea)
					.orElseThrow(()->new ApplicationException("Errore in fase di ricerca linea_attivita "+varStanzRiga.getEsercizio()+"/"+varStanzRiga.getCd_centro_responsabilita()+"/"+varStanzRiga.getCd_linea_attivita()+"."));

					ProgettoBulk progetto = linea.getProgetto();
					
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
						
						//Nelle variazioni controllare la più piccola data tra data inizio progetto e data stipula contratto definitivo
						//Recupero la lista dei contratti attivi collegati al progetto
						ProgettoHome progettoHome = (ProgettoHome)getHome(userContext, ProgettoBulk.class);
						java.util.Collection<ContrattoBulk> contrattiAssociati = progettoHome.findContratti(progetto.getPg_progetto());

						Optional<ContrattoBulk> optContratto = 
								contrattiAssociati.stream().filter(el->el.isAttivo()||el.isAttivo_e_Passivo())
								 .min((p1, p2) -> p1.getDt_stipula().compareTo(p2.getDt_stipula()))
				    			 .filter(el->el.getDt_stipula().before(progetto.getOtherField().getDtInizio()));
						
						if (optContratto.isPresent())
							optContratto
			 	    			.filter(ctr->ctr.getDt_stipula().after(variazione.getDt_chiusura()))
			 	    			.ifPresent(ctr->{
			 	    				throw new ApplicationRuntimeException(
			 	    						"Attenzione! GAE "+linea.getCd_linea_attivita()+" non selezionabile. "
  										  + "La data stipula ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(ctr.getDt_stipula())
										  + ") del primo contratto " + ctr.getEsercizio()+"/"+ctr.getStato()+"/"+ctr.getPg_contratto()
				    				   	  + " associato al progetto "+progetto.getCd_progetto()+" è successiva "
										  + "rispetto alla data di chiusura della variazione ("+
				    				   	  new java.text.SimpleDateFormat("dd/MM/yyyy").format(variazione.getDt_chiusura())+").");
				    			  });
						else
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

					if (variazione.isVariazioneRimodulazioneProgetto()) {
						if (variazione.getProgettoRimodulazione().getPg_progetto().compareTo(progetto.getPg_progetto()) != 0)
							throw new ApplicationException("Attenzione! Nella variazione " + variazione.getEsercizio() + "/" + variazione.getPg_variazione_pdg() + " risulta movimentato un progetto differente rispetto a quello della " +
									"rimodulazione associata. Operazione non possibile!");
						pianoEco.setRimodulazione(variazione.getProgettoRimodulazione());
					}

					//creo il dettaglio
					CtrlPianoEcoDett dett = new CtrlPianoEcoDett();
					dett.setTipoDett(varStanzRiga.getTi_gestione());
					dett.setImporto(varStanzRiga.getIm_variazione());
					dett.setCdrPersonale(isDettPersonale);
					dett.setUoArea(isUoArea);
					dett.setUoRagioneria(isUoRagioneria);
					dett.setUoFiscale(isUoFiscale);

					dett.setElementoVoce(varStanzRiga.getElemento_voce());
					
					if (Optional.ofNullable(cdNaturaReimpiego).map(el->el.equals(linea.getNatura().getCd_natura())).orElse(Boolean.FALSE)) {
						dett.setTipoNatura(CtrlPianoEcoDett.TIPO_REIMPIEGO);
						if (!variazione.getTipo_variazione().isStorno()) 
							throw new ApplicationException("Attenzione! Risultano movimentazioni sulla GAE "
							    + linea.getCd_linea_attivita() + " con natura 6 - 'Reimpiego di risorse' "
								+ " consentito solo per operazioni di storno. Operazione non possibile.");
					} else if (linea.getNatura().isFonteEsterna())
						dett.setTipoNatura(CtrlPianoEcoDett.TIPO_FONTE_ESTERNA);
					else
						dett.setTipoNatura(CtrlPianoEcoDett.TIPO_FONTE_INTERNA);
						
					dett.setVoceSpeciale(Optional.ofNullable(cdVoceSpeciale).map(el->el.equals(varStanzRiga.getCd_elemento_voce()))
													.orElse(Boolean.FALSE));

					pianoEco.getDett().add(dett);
					if (!listCtrlPianoEco.contains(pianoEco))
						listCtrlPianoEco.add(pianoEco);
				}
				
				controllaPdgPianoEconomico(userContext, variazione, listCtrlPianoEco, cdVoceSpeciale, cdrPersonaleBulk);
			}
        } catch (DetailedRuntimeException _ex) {
            throw new ApplicationException(_ex.getMessage());
        } catch (PersistencyException|RemoteException|IntrospectionException|SQLException e) {
			throw new ComponentException(e);
		}
	}

	private void controllaPdgPianoEconomico(UserContext userContext, OggettoBulk variazione, List<CtrlPianoEco> listCtrlPianoEco, String cdVoceSpeciale, CdrBulk cdrPersonaleBulk) throws ComponentException{
		try {
			boolean isAttivaGestioneTrasferimenti = Utility.createParametriEnteComponentSession().getParametriEnte(userContext).getFl_variazioni_trasferimento();
		
			boolean isVariazionePersonale = Optional.of(variazione)
					.filter(Pdg_variazioneBulk.class::isInstance)
					.map(Pdg_variazioneBulk.class::cast)
					.map(Pdg_variazioneBulk::isMotivazioneVariazionePersonale)
					.orElse(Boolean.FALSE) ||
					Optional.of(variazione)
					.filter(Var_stanz_resBulk.class::isInstance)
					.map(Var_stanz_resBulk.class::cast)
					.map(Var_stanz_resBulk::isMotivazioneVariazionePersonale)
					.orElse(Boolean.FALSE);
			
			boolean isVariazioneArea = Optional.of(variazione)
					.filter(Pdg_variazioneBulk.class::isInstance)
					.map(Pdg_variazioneBulk.class::cast)
					.map(Pdg_variazioneBulk::isMotivazioneTrasferimentoArea)
					.orElse(Boolean.FALSE) ||
					Optional.of(variazione)
					.filter(Var_stanz_resBulk.class::isInstance)
					.map(Var_stanz_resBulk.class::cast)
					.map(Var_stanz_resBulk::isMotivazioneTrasferimentoArea)
					.orElse(Boolean.FALSE);

			boolean isVariazioneRagioneria = Optional.of(variazione)
					.filter(Pdg_variazioneBulk.class::isInstance)
					.map(Pdg_variazioneBulk.class::cast)
					.map(Pdg_variazioneBulk::isMotivazioneTrasferimentoRagioneria)
					.orElse(Boolean.FALSE) ||
					Optional.of(variazione)
					.filter(Var_stanz_resBulk.class::isInstance)
					.map(Var_stanz_resBulk.class::cast)
					.map(Var_stanz_resBulk::isMotivazioneTrasferimentoRagioneria)
					.orElse(Boolean.FALSE);

			boolean isVariazioneFiscale = Optional.of(variazione)
					.filter(Pdg_variazioneBulk.class::isInstance)
					.map(Pdg_variazioneBulk.class::cast)
					.map(Pdg_variazioneBulk::isMotivazioneTrasferimentoFiscale)
					.orElse(Boolean.FALSE) ||
					Optional.of(variazione)
							.filter(Var_stanz_resBulk.class::isInstance)
							.map(Var_stanz_resBulk.class::cast)
							.map(Var_stanz_resBulk::isMotivazioneTrasferimentoFiscale)
							.orElse(Boolean.FALSE);

			boolean isVariazioneTrasferimentoEsigenzeFinanziarie = Optional.of(variazione)
					.filter(Pdg_variazioneBulk.class::isInstance)
					.map(Pdg_variazioneBulk.class::cast)
					.map(Pdg_variazioneBulk::isMotivazioneTrasferimentoEsigenzeFinanziarie)
					.orElse(Boolean.FALSE) ||
					Optional.of(variazione)
					.filter(Var_stanz_resBulk.class::isInstance)
					.map(Var_stanz_resBulk.class::cast)
					.map(Var_stanz_resBulk::isMotivazioneTrasferimentoEsigenzeFinanziarie)
					.orElse(Boolean.FALSE);

			boolean isCDRAreaVariazione = Optional.of(variazione)
					.filter(Pdg_variazioneBulk.class::isInstance)
					.map(Pdg_variazioneBulk.class::cast)
					.map(Pdg_variazioneBulk::getCentro_responsabilita)
					.map(CdrBulk::getUnita_padre)
					.map(Unita_organizzativaBulk::isUoArea)
					.orElse(Boolean.FALSE) ||
					Optional.of(variazione)
					.filter(Var_stanz_resBulk.class::isInstance)
					.map(Var_stanz_resBulk.class::cast)
					.map(Var_stanz_resBulk::getCentroDiResponsabilita)
					.map(CdrBulk::getUnita_padre)
					.map(Unita_organizzativaBulk::isUoArea)
					.orElse(Boolean.FALSE);

			String uoRagioneria = ((Configurazione_cnrHome)getHome(userContext,Configurazione_cnrBulk.class)).getUoRagioneria(CNRUserContext.getEsercizio(userContext));

			boolean isCDRUoRagioneria = Optional.of(variazione)
					.filter(Pdg_variazioneBulk.class::isInstance)
					.map(Pdg_variazioneBulk.class::cast)
					.map(Pdg_variazioneBulk::getCentro_responsabilita)
					.map(CdrBulk::getUnita_padre)
					.map(Unita_organizzativaBulk::getCd_unita_organizzativa)
					.map(uo->uo.equals(uoRagioneria))
					.orElse(Boolean.FALSE) ||
					Optional.of(variazione)
							.filter(Var_stanz_resBulk.class::isInstance)
							.map(Var_stanz_resBulk.class::cast)
							.map(Var_stanz_resBulk::getCentroDiResponsabilita)
							.map(CdrBulk::getUnita_padre)
							.map(Unita_organizzativaBulk::getCd_unita_organizzativa)
							.map(uo->uo.equals(uoRagioneria))
							.orElse(Boolean.FALSE);

			String uoFiscale = ((Configurazione_cnrHome)getHome(userContext,Configurazione_cnrBulk.class)).getUoFiscale(CNRUserContext.getEsercizio(userContext));

			boolean isCDRUoFiscale = Optional.of(variazione)
					.filter(Pdg_variazioneBulk.class::isInstance)
					.map(Pdg_variazioneBulk.class::cast)
					.map(Pdg_variazioneBulk::getCentro_responsabilita)
					.map(CdrBulk::getUnita_padre)
					.map(Unita_organizzativaBulk::getCd_unita_organizzativa)
					.map(uo->uo.equals(uoFiscale))
					.orElse(Boolean.FALSE) ||
					Optional.of(variazione)
							.filter(Var_stanz_resBulk.class::isInstance)
							.map(Var_stanz_resBulk.class::cast)
							.map(Var_stanz_resBulk::getCentroDiResponsabilita)
							.map(CdrBulk::getUnita_padre)
							.map(Unita_organizzativaBulk::getCd_unita_organizzativa)
							.map(uo->uo.equals(uoFiscale))
							.orElse(Boolean.FALSE);

			boolean isCDRPersonaleVariazione = Optional.of(variazione)
					.filter(Pdg_variazioneBulk.class::isInstance)
					.map(Pdg_variazioneBulk.class::cast)
					.map(Pdg_variazioneBulk::getCentro_responsabilita)
					.map(CdrBulk::getUnita_padre)
					.map(Unita_organizzativaBulk::getCd_unita_organizzativa)
					.map(cdUo -> cdUo.equals(cdrPersonaleBulk.getCd_unita_organizzativa()))
					.orElse(Boolean.FALSE) ||
					Optional.of(variazione)
					.filter(Var_stanz_resBulk.class::isInstance)
					.map(Var_stanz_resBulk.class::cast)
					.map(Var_stanz_resBulk::getCentroDiResponsabilita)
					.map(CdrBulk::getUnita_padre)
					.map(Unita_organizzativaBulk::getCd_unita_organizzativa)
					.map(cdUo -> cdUo.equals(cdrPersonaleBulk.getCd_unita_organizzativa()))
					.orElse(Boolean.FALSE);

			//se è una variazione di competenza per maggiori entrate/spese controllo solo che non siano stati sottratti erroneamente fondi a progetti
			boolean isVariazioneCompetenzaMaggioreEntrateSpese = Optional.of(variazione)
					.filter(Pdg_variazioneBulk.class::isInstance)
					.map(Pdg_variazioneBulk.class::cast)
					.map(Pdg_variazioneBulk::getTipo_variazione)
					.map(Tipo_variazioneBulk::isVariazioneMaggioriEntrateSpese)
					.orElse(Boolean.FALSE);

			//se è una variazione di competenza per minori entrate/spese controllo solo che non siano stati assegnati erroneamente fondi a progetti
			boolean isVariazioneCompetenzaMinoriEntrateSpese = Optional.of(variazione)
					.filter(Pdg_variazioneBulk.class::isInstance)
					.map(Pdg_variazioneBulk.class::cast)
					.map(Pdg_variazioneBulk::getTipo_variazione)
					.map(Tipo_variazioneBulk::isVariazioneMinoriEntrateSpese)
					.orElse(Boolean.FALSE);

			//se è una variazione di competenza per minori entrate/spese controllo solo che non siano stati assegnati erroneamente fondi a progetti
			boolean isVariazioneStornoSpese = Optional.of(variazione)
					.filter(Pdg_variazioneBulk.class::isInstance)
					.map(Pdg_variazioneBulk.class::cast)
					.map(Pdg_variazioneBulk::getTipo_variazione)
					.map(Tipo_variazioneBulk::isStornoSpesa)
					.orElse(Boolean.FALSE)  ||
					Optional.of(variazione)
							.filter(Var_stanz_resBulk.class::isInstance)
							.map(Var_stanz_resBulk.class::cast)
							.map(Var_stanz_resBulk::isVariazioneStorno)
							.orElse(Boolean.FALSE);

			boolean isVariazioneMonoProgetto = listCtrlPianoEco.stream().map(CtrlPianoEco::getProgetto)
					.map(ProgettoBulk::getPg_progetto).distinct().count()==1;

			if (isAttivaGestioneTrasferimenti) {
				//se non è una variazione di personale non possono essere movimentate voci del personale
				if (isVariazionePersonale) {
					if (isCDRPersonaleVariazione) {
						listCtrlPianoEco.stream()
							.filter(el->el.getImpSpesaPositiviCdrPersonale().compareTo(BigDecimal.ZERO)>0)
							.findFirst().ifPresent(el->{
								throw new DetailedRuntimeException("Attenzione! In una variazione di restituzione di 'Trasferimenti per personale' "
										+ "non è possibile assegnare fondi a voci accentrate del personale.");
						});
						
						listCtrlPianoEco.stream()
							.filter(el->el.getImpSpesaNegativiCdrPersonale().compareTo(BigDecimal.ZERO)>0)
							.findFirst().orElseThrow(()->
								new DetailedRuntimeException("Attenzione! In una variazione di restituzione di 'Trasferimenti per personale' "
										+ "è necessario sottrarre fondi a voci accentrate del personale."));
	
						listCtrlPianoEco.stream()
							.filter(el->el.getImpSpesaNegativi().subtract(el.getImpSpesaNegativiCdrPersonale()).compareTo(BigDecimal.ZERO)>0)
							.findFirst().ifPresent(el->{
								throw new DetailedRuntimeException("Attenzione! In una variazione di restituzione 'Trasferimenti per personale' "
										+ "non è possibile sottrarre fondi a CDR non qualificati come CDR Personale.");
						});
					} else {
						listCtrlPianoEco.stream()
							.filter(el->el.getImpSpesaPositiviCdrPersonale().compareTo(BigDecimal.ZERO)>0)
							.findFirst().orElseThrow(()->
								new DetailedRuntimeException("Attenzione! In una variazione di tipo 'Trasferimenti per personale' è necessario "
										+ "assegnare fondi ad almeno una voce accentrata del personale."));
			
						listCtrlPianoEco.stream()
							.filter(el->el.getImpSpesaNegativiCdrPersonale().compareTo(BigDecimal.ZERO)>0)
							.findFirst().ifPresent(el->{
								throw new DetailedRuntimeException("Attenzione! In una variazione di tipo 'Trasferimenti per personale' non è possibile "
										+ "sottrarre fondi a voci accentrate del personale.");
						});
	
						listCtrlPianoEco.stream()
							.filter(el->el.getImpSpesaPositivi().subtract(el.getImpSpesaPositiviCdrPersonale()).compareTo(BigDecimal.ZERO)>0)
							.findFirst().ifPresent(el->{
								throw new DetailedRuntimeException("Attenzione! In una variazione di tipo 'Trasferimenti per personale' non è possibile "
										+ "assegnare fondi a voci non accentrate del personale.");
						});
					}
				} else if (!isCDRPersonaleVariazione) { 
					//Il controllo non vale se la variazione viene fatta dal CDR Personale in quanto per essa le voci sono qualificate come accentrate
					listCtrlPianoEco.stream()
						.filter(el->el.getImpSpesaPositiviCdrPersonale().compareTo(BigDecimal.ZERO)!=0 ||
									el.getImpSpesaNegativiCdrPersonale().compareTo(BigDecimal.ZERO)!=0)
						.findFirst().ifPresent(el->{
							throw new DetailedRuntimeException("Attenzione! Non è possibile movimentare voci accentrate del personale "
									+ "in una variazione non effettuata per 'Trasferimenti per personale'.");
					});
				}

			 	if (isVariazioneArea) {
					if (isCDRAreaVariazione)
						//24/02/2022 Su indicazione di Sabrina Miceli l'unico modo di un'area di ricerca di trasferire fondi ad altri CDR è con variazione generica.
						//E' stato pertanto chiesto di bloccare la possibilità per un'area di ricerca di effettuare variazioni di tipo Area
						throw new DetailedRuntimeException("Attenzione! Non è possibile per un'Area di Ricerca predisporre variazione di tipo 'Trasferimenti da Aree di Ricerca.");
					else {
						listCtrlPianoEco.stream()
							.filter(el->el.getImpSpesaPositiviArea().compareTo(BigDecimal.ZERO)>0)
							.findFirst().orElseThrow(()->
								new DetailedRuntimeException("Attenzione! In una variazione di tipo 'Trasferimenti ad Aree di Ricerca' è necessario "
										+ "assegnare fondi ad almeno una Area di Ricerca."));
			
						listCtrlPianoEco.stream()
							.filter(el->el.getImpSpesaNegativiArea().compareTo(BigDecimal.ZERO)>0)
							.findFirst().ifPresent(el->{
								throw new DetailedRuntimeException("Attenzione! In una variazione di tipo 'Trasferimenti ad Aree di Ricerca' non è possibile "
										+ "sottrarre fondi ad Aree di Ricerca.");
						});

						listCtrlPianoEco.stream()
							.filter(el->el.getImpSpesaPositivi().subtract(el.getImpSpesaPositiviArea()).compareTo(BigDecimal.ZERO)>0)
							.findFirst().ifPresent(el->{
								throw new DetailedRuntimeException("Attenzione! In una variazione di tipo 'Trasferimenti ad Aree di Ricerca' non è possibile "
										+ "assegnare fondi a CDR non qualificati come Aree di Ricerca.");
						});
					}
				} else if (!isCDRAreaVariazione) {
					//L'area può sempre ricevere somme su un progetto cui partecipa se le stesse provengono dallo stesso progetto
					//Questa condizione è garantita dal monoprogetto e da storno spese
					//Indicazioni avute da Sabrina Miceli il 10/11/2021
					if (!isVariazioneMonoProgetto || !isVariazioneStornoSpese) {
						//Il controllo non vale se la variazione viene fatta dal CDR Area
						listCtrlPianoEco.stream()
								.filter(el->el.getImpSpesaPositiviArea().compareTo(BigDecimal.ZERO)!=0 ||
										el.getImpSpesaNegativiArea().compareTo(BigDecimal.ZERO)!=0)
								.findFirst().ifPresent(el->{
							throw new DetailedRuntimeException("Attenzione! Non è possibile movimentare voci su Aree di Ricerca "
									+ "in una variazione non effettuata per 'Trasferimenti ad Aree di Ricerca'.");
						});
					}
				}

				if (isVariazioneRagioneria) {
					if (isCDRUoRagioneria) {
						listCtrlPianoEco.stream()
								.filter(el->el.getImpSpesaPositiviRagioneria().compareTo(BigDecimal.ZERO)>0)
								.findFirst().ifPresent(el->{
							throw new DetailedRuntimeException("Attenzione! In una variazione generata dall'Ufficio Ragioneria di tipo 'Trasferimento alla Ragioneria' non è possibile "
									+ "assegnare fondi alla Ragioneria (UO: "+uoRagioneria+").");
						});

						listCtrlPianoEco.stream()
								.filter(el->el.getImpSpesaNegativiRagioneria().compareTo(BigDecimal.ZERO)>0)
								.findFirst().orElseThrow(()->
								new DetailedRuntimeException("Attenzione! In una variazione generata dall'Ufficio Ragioneria di tipo 'Trasferimento alla Ragioneria' è necessario "
										+ "sottrarre fondi alla Ragioneria (UO: "+uoRagioneria+")."));

						listCtrlPianoEco.stream()
								.filter(el->el.getImpSpesaNegativi().subtract(el.getImpSpesaNegativiRagioneria()).compareTo(BigDecimal.ZERO)>0)
								.findFirst().ifPresent(el->{
							throw new DetailedRuntimeException("Attenzione! In una variazione generata dall'Ufficio Ragioneria di tipo 'Trasferimento alla Ragioneria' non è possibile "
									+ "sottrarre fondi a CDR non qualificati come Ragioneria.");
						});
					} else {
						listCtrlPianoEco.stream()
								.filter(el->el.getImpSpesaPositiviRagioneria().compareTo(BigDecimal.ZERO)>0)
								.findFirst().orElseThrow(()->
								new DetailedRuntimeException("Attenzione! In una variazione di tipo 'Trasferimento alla Ragioneria' è necessario "
										+ "assegnare fondi alla Ragioneria (UO: "+uoRagioneria+")."));

						listCtrlPianoEco.stream()
								.filter(el->el.getImpSpesaNegativiRagioneria().compareTo(BigDecimal.ZERO)>0)
								.findFirst().ifPresent(el->{
							throw new DetailedRuntimeException("Attenzione! In una variazione di tipo 'Trasferimento alla Ragioneria' non è possibile "
									+ "sottrarre fondi alla Ragioneria (UO: "+uoRagioneria+").");
						});

						listCtrlPianoEco.stream()
								.filter(el->el.getImpSpesaPositivi().subtract(el.getImpSpesaPositiviRagioneria()).compareTo(BigDecimal.ZERO)>0)
								.findFirst().ifPresent(el->{
							throw new DetailedRuntimeException("Attenzione! In una variazione di tipo 'Trasferimento alla Ragioneria' non è possibile "
									+ "assegnare fondi a CDR non qualificati come Ragioneria.");
						});
					}
				}

				if (isVariazioneFiscale) {
					if (isCDRUoFiscale) {
						listCtrlPianoEco.stream()
								.filter(el->el.getImpSpesaPositiviUoFiscale().compareTo(BigDecimal.ZERO)>0)
								.findFirst().ifPresent(el->{
									throw new DetailedRuntimeException("Attenzione! In una variazione generata dall'Ufficio Fiscale di tipo 'Trasferimento Fiscale' non è possibile "
											+ "assegnare fondi all'Ufficio Fiscale (UO: "+uoFiscale+").");
								});

						listCtrlPianoEco.stream()
								.filter(el->el.getImpSpesaNegativiUoFiscale().compareTo(BigDecimal.ZERO)>0)
								.findFirst().orElseThrow(()->
										new DetailedRuntimeException("Attenzione! In una variazione generata dall'Ufficio Fiscale di tipo 'Trasferimento Fiscale' è necessario "
												+ "sottrarre fondi all'Ufficio Fiscale' (UO: "+uoFiscale+")."));

						listCtrlPianoEco.stream()
								.filter(el->el.getImpSpesaNegativi().subtract(el.getImpSpesaNegativiUoFiscale()).compareTo(BigDecimal.ZERO)>0)
								.findFirst().ifPresent(el->{
									throw new DetailedRuntimeException("Attenzione! In una variazione generata dall'Ufficio Fiscale di tipo 'Trasferimento Fiscale' non è possibile "
											+ "sottrarre fondi a CDR non qualificati come Ufficio Fiscale.");
								});
					} else {
						listCtrlPianoEco.stream()
								.filter(el->el.getImpSpesaPositiviUoFiscale().compareTo(BigDecimal.ZERO)>0)
								.findFirst().orElseThrow(()->
										new DetailedRuntimeException("Attenzione! In una variazione di tipo 'Trasferimento Fiscale' è necessario "
												+ "assegnare fondi all'Ufficio Fiscale' (UO: "+uoFiscale+")."));

						listCtrlPianoEco.stream()
								.filter(el->el.getImpSpesaNegativiUoFiscale().compareTo(BigDecimal.ZERO)>0)
								.findFirst().ifPresent(el->{
									throw new DetailedRuntimeException("Attenzione! In una variazione di tipo 'Trasferimento Fiscale' non è possibile "
											+ "sottrarre fondi all'Ufficio Fiscale (UO: "+uoFiscale+").");
								});

						listCtrlPianoEco.stream()
								.filter(el->el.getImpSpesaPositivi().subtract(el.getImpSpesaPositiviUoFiscale()).compareTo(BigDecimal.ZERO)>0)
								.findFirst().ifPresent(el->{
									throw new DetailedRuntimeException("Attenzione! In una variazione di tipo 'Trasferimento Fiscale' non è possibile "
											+ "assegnare fondi a CDR non qualificati come Ufficio Fiscale.");
								});
					}
				}
			} else if (!isCDRUoRagioneria) {
				//Il controllo non vale se la variazione viene fatta dal CDR Ragioneria
				listCtrlPianoEco.stream()
						.filter(el->el.getImpSpesaPositiviRagioneria().compareTo(BigDecimal.ZERO)!=0 ||
								el.getImpSpesaNegativiRagioneria().compareTo(BigDecimal.ZERO)!=0)
						.findFirst().ifPresent(el->{
					throw new DetailedRuntimeException("Attenzione! Non è possibile movimentare voci sulla Ragioneria (Uo: "+uoRagioneria+") "
							+ "in una variazione non effettuata per 'Trasferimento alla Ragioneria'.");
				});
			} else if (!isCDRUoFiscale) {
				//Il controllo non vale se la variazione viene fatta dal CDR Fiscale
				listCtrlPianoEco.stream()
						.filter(el->el.getImpSpesaPositiviUoFiscale().compareTo(BigDecimal.ZERO)!=0 ||
								el.getImpSpesaNegativiUoFiscale().compareTo(BigDecimal.ZERO)!=0)
						.findFirst().ifPresent(el->{
							throw new DetailedRuntimeException("Attenzione! Non è possibile movimentare voci sull'Ufficio Fiscale (Uo: "+uoFiscale+") "
									+ "in una variazione non effettuata per 'Trasferimento Fiscale'.");
						});
			}

			if (isVariazioneTrasferimentoEsigenzeFinanziarie) {
				listCtrlPianoEco.stream()
						.filter(el->!el.getProgetto().getOtherField().getTipoFinanziamento().getFlTrasfQuoteProgettiAttivi())
						.filter(el->!el.getProgetto().getOtherField().getTipoFinanziamento().getFlRiceviQuoteProgettiAttivi())
						.findFirst().ifPresent(el->{
							throw new DetailedRuntimeException("Attenzione! In una variazione di tipo 'Trasferimenti per Esigenze Finanziarie' non è possibile "
									+ "movimentare progetti la cui tipologia di finanziamento non prevede trasferimento/acquisizione di fondi da progetti attivi (Progetto non valido: "+el.getProgetto().getCd_progetto()+").");
						});

				listCtrlPianoEco.stream()
						.filter(el->!el.getProgetto().getOtherField().getTipoFinanziamento().getFlRiceviQuoteProgettiAttivi())
						.filter(el->el.getImpSpesaPositivi().compareTo(BigDecimal.ZERO)!=0)
						.findFirst().ifPresent(el->{
							throw new DetailedRuntimeException("Attenzione! Non è possibile assegnare fondi a progetti la cui tipologia di finanziamento non prevede " +
									"acquisizione di fondi da progetti attivi (Progetto non valido: "+el.getProgetto().getCd_progetto()+").");
						});

				listCtrlPianoEco.stream()
						.filter(el->!el.getProgetto().getOtherField().getTipoFinanziamento().getFlTrasfQuoteProgettiAttivi())
						.filter(el->el.getImpSpesaNegativi().compareTo(BigDecimal.ZERO)!=0)
						.findFirst().ifPresent(el->{
							throw new DetailedRuntimeException("Attenzione! Non è possibile sottrarre fondi a progetti la cui tipologia di finanziamento non prevede " +
									"trasferimento di fondi da progetti attivi (Progetto non valido: "+el.getProgetto().getCd_progetto()+").");
						});
			}

			BigDecimal impSpesaPositiviVoceSpeciale = listCtrlPianoEco.stream()
					.filter(el->el.getImpSpesaPositiviVoceSpeciale().compareTo(BigDecimal.ZERO)>0)
					.map(CtrlPianoEco::getImpSpesaPositiviVoceSpeciale)
					.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
	
			BigDecimal impSpesaPositiviNaturaReimpiego = listCtrlPianoEco.stream()
					.filter(el->el.getImpSpesaPositiviNaturaReimpiego().compareTo(BigDecimal.ZERO)>0)
					.map(CtrlPianoEco::getImpSpesaPositiviNaturaReimpiego)
					.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

			BigDecimal impSpesaNegativiNaturaReimpiego = listCtrlPianoEco.stream()
					.filter(el->el.getImpSpesaNegativiNaturaReimpiego().compareTo(BigDecimal.ZERO)>0)
					.map(CtrlPianoEco::getImpSpesaNegativiNaturaReimpiego)
					.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

			if (isVariazioneCompetenzaMaggioreEntrateSpese || isVariazioneCompetenzaMinoriEntrateSpese) {
				CdrBulk cdrVariazioneBulk = Optional.of(variazione)
						.filter(Pdg_variazioneBulk.class::isInstance)
						.map(Pdg_variazioneBulk.class::cast)
						.map(Pdg_variazioneBulk::getCentro_responsabilita)
						.orElseGet(()->Optional.of(variazione)
										.filter(Var_stanz_resBulk.class::isInstance)
										.map(Var_stanz_resBulk.class::cast)
										.map(Var_stanz_resBulk::getCentroDiResponsabilita)
										.orElseThrow(()->new DetailedRuntimeException("Attenzione! Operazione non possibile in "
												+ "quanto non è stato possibile individuare il CDR della variazione|")));

				Unita_organizzativaBulk uoVariazioneBulk = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdrVariazioneBulk.getCd_unita_organizzativa()));
				//verifico se si tratta di cdr Personale
				boolean isCDRVariazionePersonale = uoVariazioneBulk.getCd_unita_organizzativa().equals(this.getCDRPersonale(userContext).getCd_unita_organizzativa());

				//se è una variazione per maggiori/minori entrate/spese non è possibile movimentare voci accentrate del personale
				if (!isCDRVariazionePersonale)
					listCtrlPianoEco.stream()
						.filter(el->el.getImpSpesaPositiviCdrPersonale().compareTo(BigDecimal.ZERO)!=0 ||
									el.getImpSpesaNegativiCdrPersonale().compareTo(BigDecimal.ZERO)!=0)
						.findFirst().ifPresent(el->{
							throw new DetailedRuntimeException("Attenzione! Non è possibile movimentare voci accentrate del personale "
									+ "in una variazione per maggiori/minori spese.");
					});
	
				listCtrlPianoEco.stream()
					.filter(el->el.getImpEntrataPositivi().subtract(el.getImpEntrataNegativi())
								.compareTo(el.getImpSpesaPositivi().subtract(el.getImpSpesaNegativi()))!=0)
					.findFirst().ifPresent(el->{
					throw new DetailedRuntimeException("Attenzione! Il saldo entrata ("
							+ new it.cnr.contab.util.EuroFormat().format(el.getImpEntrataPositivi().subtract(el.getImpEntrataNegativi()))
							+ ") non corrisponde al saldo spesa ("
							+ new it.cnr.contab.util.EuroFormat().format(el.getImpSpesaPositivi().subtract(el.getImpSpesaNegativi()))
							+ ") per il progetto "+el.getProgetto().getCd_progetto()+".");
				});
				
				if (isVariazioneCompetenzaMaggioreEntrateSpese) {
					listCtrlPianoEco.stream()
						.filter(el->el.getImpEntrataNegativi().compareTo(BigDecimal.ZERO)>0 ||
									el.getImpSpesaNegativi().compareTo(BigDecimal.ZERO)>0)
						.findFirst().ifPresent(el->{
						throw new DetailedRuntimeException("Attenzione! Non è possibile sottrarre fondi al progetto "+
								el.getProgetto().getCd_progetto()+" in quanto la variazione è di tipo 'Maggiori Entrate/Spese'.");
						});
					
					if (impSpesaPositiviNaturaReimpiego.compareTo(BigDecimal.ZERO)>0 || impSpesaNegativiNaturaReimpiego.compareTo(BigDecimal.ZERO)>0)
						throw new ApplicationException("Attenzione! Risultano movimenti su GAE di natura 6 - 'Reimpiego di risorse' "
									+ " non consentiti in quanto la variazione è di tipo 'Maggiori Entrate/Spese'.");
				} else {
					listCtrlPianoEco.stream()
						.filter(el->el.getImpEntrataPositivi().compareTo(BigDecimal.ZERO)>0 ||
									el.getImpSpesaPositivi().compareTo(BigDecimal.ZERO)>0)
						.findFirst().ifPresent(el->{
						throw new DetailedRuntimeException("Attenzione! Non è possibile attribuire fondi al progetto "+
								el.getProgetto().getCd_progetto()+" in quanto la variazione è di tipo 'Minori Entrate/Spese'.");
						});
				}
			} else if (isVariazioneStornoSpese){ 
				/**
				 * 1. non è possibile attribuire fondi alla voce speciale (11048)
				 */
				if (impSpesaPositiviVoceSpeciale.compareTo(BigDecimal.ZERO)>0)
					throw new ApplicationException("Attenzione! Non è possibile attribuire fondi alla voce "
							+ cdVoceSpeciale + " ("
							+ new it.cnr.contab.util.EuroFormat().format(impSpesaPositiviVoceSpeciale)+") "
							+ "in quanto la variazione non è di tipo 'Maggiori Entrate/Spese'.");
				
				//Controlli su tutte le altre tipologie di variazioni
				Timestamp dataChiusura = Optional.of(variazione)
						.filter(Pdg_variazioneBulk.class::isInstance)
						.map(Pdg_variazioneBulk.class::cast)
						.map(Pdg_variazioneBulk::getDt_chiusura)
						.orElseGet(()->Optional.of(variazione)
										.filter(Var_stanz_resBulk.class::isInstance)
										.map(Var_stanz_resBulk.class::cast)
										.map(Var_stanz_resBulk::getDt_chiusura)
										.orElseThrow(()->new DetailedRuntimeException("Attenzione! Operazione non possibile in "
												+ "quanto non risulta ancora impostata la data chiusura della variazione.")));


				if (isVariazioneTrasferimentoEsigenzeFinanziarie) {
					listCtrlPianoEco.stream()
							.filter(el->el.isScaduto(dataChiusura))
							.filter(el->el.getProgetto().getOtherField().getTipoFinanziamento().getFlTrasfQuoteProgettiAttivi())
							.filter(el->el.getImpSpesaNegativi().compareTo(BigDecimal.ZERO) != 0)
							.findFirst().ifPresent(el -> {
								throw new DetailedRuntimeException("Attenzione! In una variazione di tipo 'Trasferimenti per Esigenze Finanziarie' non è possibile " +
										"trasferire fondi da progetti scaduti (Progetto non valido: " + el.getProgetto().getCd_progetto() + ").");
							});
				}

					//CONTROLLI SU SINGOLO PROGETTO
				//Controlli non attivi per variazioni maggiori entrate/spese che non possono avere importi negativi essendo già stato fatto
				//questo controllo prima
				/*
				  10. se un progetto è scaduto non è possibile attribuire fondi
				 */
				listCtrlPianoEco.stream()
					.filter(el->el.isScaduto(dataChiusura))
					.filter(el->el.getImpSpesaPositivi().compareTo(BigDecimal.ZERO)>0)
					.findFirst().ifPresent(el->{
						throw new DetailedRuntimeException("Attenzione! Non è possibile attribuire fondi al progetto "+
								el.getProgetto().getCd_progetto()+
								" in quanto scaduto ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(el.getDtScadenza()) +
								") rispetto alla data di chiusura della variazione ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(dataChiusura)+").");});

				/*
				  20. se un progetto è attivo è possibile sottrarre fondi a GAE di natura 6 solo assegnandoli a GAE di natura 6
				     dello stesso progetto (regola non valida per trasferimento ad Aree o Ragioneria o Fiscale o per esigenze finanziarie)
			  	  10/06/2022 - regola non valida per variazioni di tipo Trasferimento Finanziario - documento analisi cda
				 */
				if (!isVariazioneArea && !isVariazioneRagioneria && !isVariazioneFiscale && !isVariazioneTrasferimentoEsigenzeFinanziarie)
					listCtrlPianoEco.stream()
						.filter(el->!el.isScaduto(dataChiusura))
						.filter(el->el.getImpSpesaNegativiNaturaReimpiego().compareTo(BigDecimal.ZERO)>0)
						.filter(el->el.getImpSpesaNegativiNaturaReimpiego().compareTo(el.getImpSpesaPositiviNaturaReimpiego())!=0)
						.findFirst().ifPresent(el->{
							throw new DetailedRuntimeException("Attenzione! Sono stati sottratti fondi dal progetto "+
									el.getProgetto().getCd_progetto()+"(" +
									new it.cnr.contab.util.EuroFormat().format(el.getImpSpesaNegativiNaturaReimpiego()) +
									") da GAE di natura 6 - 'Reimpiego di risorse' non compensati da un equivalente " +
									"assegnazione nell'ambito dello stesso progetto e della stessa natura ("+
									new it.cnr.contab.util.EuroFormat().format(el.getImpSpesaPositiviNaturaReimpiego()) + ")");});
		
				/*
				  30. se un progetto è aperto è possibile attribuire somme su GAE non di natura 6 solo se stornate dallo stesso progetto
				  	  (regola non valida per progetti di Aree, CdrPersonale, Ragioneria e Fiscale)
				  	  24/02/2022 - regola valida anche per progetti di Aree su indicazione di Sabrina Miceli
  				  	  10/06/2022 - regola non valida per variazioni di tipo Trasferimento Finanziario - documento analisi cda
				 */
				if (!isVariazioneArea && !isVariazioneRagioneria && !isVariazioneFiscale && !(isVariazionePersonale && variazione instanceof Var_stanz_resBulk) && !isVariazioneTrasferimentoEsigenzeFinanziarie) {
					boolean addSpesePersonale = !isAttivaGestioneTrasferimenti||isVariazionePersonale;
					listCtrlPianoEco.stream()
						.filter(el->!el.isScaduto(dataChiusura))
						.filter(el->el.getImpSpesaPositiviNetti()
								  .add(el.getImpSpesaPositiviArea().subtract(el.getImpSpesaPositiviAreaNaturaReimpiego()))
								  .add(addSpesePersonale?el.getImpSpesaPositiviCdrPersonale():BigDecimal.ZERO)
								  .compareTo(BigDecimal.ZERO)>0)
						.filter(el->el.getImpSpesaPositiviNetti()
									  .add(el.getImpSpesaPositiviArea().subtract(el.getImpSpesaPositiviAreaNaturaReimpiego()))
									  .add(addSpesePersonale?el.getImpSpesaPositiviCdrPersonale():BigDecimal.ZERO)
										  .compareTo(el.getImpSpesaNegativiNetti()
												   .add(el.getImpSpesaNegativiArea().subtract(el.getImpSpesaNegativiAreaNaturaReimpiego()))
											  	   .add(addSpesePersonale?el.getImpSpesaNegativiCdrPersonale():BigDecimal.ZERO))>0)
						.findFirst().ifPresent(el->{
						throw new DetailedRuntimeException("Attenzione! Sono stati attribuiti fondi al progetto "+
								el.getProgetto().getCd_progetto()+" (" + 
								new it.cnr.contab.util.EuroFormat().format(
										el.getImpSpesaPositiviNetti()
										  .add(el.getImpSpesaPositiviArea().subtract(el.getImpSpesaPositiviAreaNaturaReimpiego()))
										  .add(addSpesePersonale?el.getImpSpesaPositiviCdrPersonale():BigDecimal.ZERO)) +
								") non compensati da un equivalente prelievo nell'ambito dello stesso progetto ("+
								new it.cnr.contab.util.EuroFormat().format(
										el.getImpSpesaNegativiNetti()
										  .add(el.getImpSpesaNegativiArea().subtract(el.getImpSpesaNegativiAreaNaturaReimpiego()))
										  .add(addSpesePersonale?el.getImpSpesaNegativiCdrPersonale():BigDecimal.ZERO)) + ")");});
		
					/*
					  31. se un progetto è aperto è possibile sottrarre somme su GAE non di natura 6 solo se assegnate allo stesso progetto
					  	  (regola non valida per progetti di Aree, CdrPersonale e Ragioneria)

					      N.B.: la sottrazione dalla voce speciale è consentita purchè sia compensata da trasferimenti a GAE di natura 6
					      controllo effettuato al punto 90
					 */
					listCtrlPianoEco.stream()
						.filter(el->!el.isScaduto(dataChiusura))
						.filter(el->el.getImpSpesaNegativiNetti()
								      .add(el.getImpSpesaNegativiArea().subtract(el.getImpSpesaNegativiAreaNaturaReimpiego()))
									  .add(addSpesePersonale?el.getImpSpesaNegativiCdrPersonale():BigDecimal.ZERO)
									  .compareTo(BigDecimal.ZERO)>0)
						.filter(el->el.getImpSpesaNegativiNetti()
									  .add(el.getImpSpesaNegativiArea().subtract(el.getImpSpesaNegativiAreaNaturaReimpiego()))
								      .add(addSpesePersonale?el.getImpSpesaNegativiCdrPersonale():BigDecimal.ZERO)
									  .compareTo(el.getImpSpesaPositiviNetti()
 											       .add(el.getImpSpesaPositiviArea().subtract(el.getImpSpesaPositiviAreaNaturaReimpiego()))
											  	   .add(addSpesePersonale?el.getImpSpesaPositiviCdrPersonale():BigDecimal.ZERO))>0)
						.findFirst().ifPresent(el->{
						throw new DetailedRuntimeException("Attenzione! Sono stati sottratti fondi al progetto "+
								el.getProgetto().getCd_progetto()+" (" + 
								new it.cnr.contab.util.EuroFormat().format(
										el.getImpSpesaNegativiNetti()
										  .add(el.getImpSpesaNegativiArea().subtract(el.getImpSpesaNegativiAreaNaturaReimpiego()))
										  .add(addSpesePersonale?el.getImpSpesaNegativiCdrPersonale():BigDecimal.ZERO)) +
								") non compensati da un equivalente assegnazione nell'ambito dello stesso progetto ("+
								new it.cnr.contab.util.EuroFormat().format(
										el.getImpSpesaPositiviNetti()
										  .add(el.getImpSpesaPositiviArea().subtract(el.getImpSpesaPositiviAreaNaturaReimpiego()))
										  .add(addSpesePersonale?el.getImpSpesaPositiviCdrPersonale():BigDecimal.ZERO)) + ")");});
				}
				
				/**
				 * 40. se un progetto è aperto e vengono sottratte somme ad un'area queste devono essere riassegnate 
				 *    allo stesso progetto e alla stessa area
				 *    (regola non valida per Trasferimenti a Ragioneria e Fiscale)
				 *    08/03/2022 Controllo eliminato su richiesta di Sabrina Miceli (Segnalazione Helpdesk 107720)
				if (!isVariazioneRagioneria && !isVariazioneFiscale) {
					listCtrlPianoEco.stream()
							.filter(el -> !el.isScaduto(dataChiusura))
							.filter(el -> el.getImpSpesaNegativiArea().compareTo(BigDecimal.ZERO) > 0)
							.filter(el -> el.getImpSpesaNegativiArea().compareTo(el.getImpSpesaPositiviArea()) > 0)
							.findFirst().ifPresent(el -> {
						throw new DetailedRuntimeException("Attenzione! Sono stati prelevati dall'area fondi dal progetto " +
								el.getProgetto().getCd_progetto() + " (" +
								new it.cnr.contab.util.EuroFormat().format(el.getImpSpesaNegativiArea()) +
								") non compensati da un equivalente assegnazione nell'ambito dello stesso progetto e della stessa area (" +
								new it.cnr.contab.util.EuroFormat().format(el.getImpSpesaPositiviArea()) + ")");
					});
				}
			 	*/

				/**
				 * 50. se un progetto è aperto e vengono sottratte somme al CDR Personale queste devono essere riassegnate 
				 *    allo stesso progetto e alla stesso CDR
				 */
				listCtrlPianoEco.stream()
					.filter(el->!el.isScaduto(dataChiusura))
					.filter(el->el.getImpSpesaNegativiCdrPersonale().compareTo(BigDecimal.ZERO)>0)
					.filter(el->el.getImpSpesaNegativiCdrPersonale().compareTo(el.getImpSpesaPositiviCdrPersonale())>0)
					.findFirst().ifPresent(el->{
					throw new DetailedRuntimeException("Attenzione! Sono stati prelevati dal CDR Personale fondi dal progetto "+
							el.getProgetto().getCd_progetto()+" (" + 
							new it.cnr.contab.util.EuroFormat().format(el.getImpSpesaNegativiCdrPersonale()) +
							") non compensati da un equivalente assegnazione nell'ambito dello stesso progetto e della stessa area ("+
							new it.cnr.contab.util.EuroFormat().format(el.getImpSpesaPositiviCdrPersonale()) + ")");});

				/**
				 * 50.1 se un progetto è aperto e vengono sottratte somme al CDR Ragioneria queste devono essere riassegnate
				 *    allo stesso progetto e alla stesso CDR
				 */
				listCtrlPianoEco.stream()
						.filter(el->!el.isScaduto(dataChiusura))
						.filter(el->el.getImpSpesaNegativiRagioneria().compareTo(BigDecimal.ZERO)>0)
						.filter(el->el.getImpSpesaNegativiRagioneria().compareTo(el.getImpSpesaPositiviRagioneria())>0)
						.findFirst().ifPresent(el->{
					throw new DetailedRuntimeException("Attenzione! Sono stati prelevati dal CDR Ragioneria fondi dal progetto "+
							el.getProgetto().getCd_progetto()+" (" +
							new it.cnr.contab.util.EuroFormat().format(el.getImpSpesaNegativiRagioneria()) +
							") non compensati da un equivalente assegnazione nell'ambito dello stesso progetto e della stessa area ("+
							new it.cnr.contab.util.EuroFormat().format(el.getImpSpesaPositiviRagioneria()) + ")");});

				/**
				 * 50.1 se un progetto è aperto e vengono sottratte somme al CDR Fiscale queste devono essere riassegnate
				 *    allo stesso progetto e alla stesso CDR
				 */
				listCtrlPianoEco.stream()
						.filter(el->!el.isScaduto(dataChiusura))
						.filter(el->el.getImpSpesaNegativiUoFiscale().compareTo(BigDecimal.ZERO)>0)
						.filter(el->el.getImpSpesaNegativiUoFiscale().compareTo(el.getImpSpesaPositiviUoFiscale())>0)
						.findFirst().ifPresent(el->{
							throw new DetailedRuntimeException("Attenzione! Sono stati prelevati dal CDR Fiscale fondi dal progetto "+
									el.getProgetto().getCd_progetto()+" (" +
									new it.cnr.contab.util.EuroFormat().format(el.getImpSpesaNegativiUoFiscale()) +
									") non compensati da un equivalente assegnazione nell'ambito dello stesso progetto e della stessa area ("+
									new it.cnr.contab.util.EuroFormat().format(el.getImpSpesaPositiviUoFiscale()) + ")");});

				//CONTROLLI SUL TOTALE PROGETTI
				BigDecimal impNegativiPrgScaduti = listCtrlPianoEco.stream()
						.filter(el->el.isScaduto(dataChiusura))
						.filter(el->el.getImpSpesaNegativi().compareTo(BigDecimal.ZERO)>0)
						.map(CtrlPianoEco::getImpSpesaNegativi)
						.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
				
				{
					/**
					 * 60. se un progetto è scaduto se vengono sottratti importi devono essere girati a GaeNatura6 o al CDRPersonale o alla Uo Ragioneria o alla Uo Fiscale
					 */
					BigDecimal impPositiviCashFund = listCtrlPianoEco.stream()
							.filter(el->!el.isScaduto(dataChiusura))
							.map(CtrlPianoEco::getDett)
							.flatMap(List::stream)
							.filter(el->el.isNaturaReimpiego()||el.isCdrPersonale()||el.isUoRagioneria()||el.isUoFiscale())
							.filter(el->el.getImporto().compareTo(BigDecimal.ZERO)>0)
							.map(CtrlPianoEcoDett::getImporto)
							.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
					
					if (impNegativiPrgScaduti.compareTo(BigDecimal.ZERO)>0 && impNegativiPrgScaduti.compareTo(impPositiviCashFund)>0)
						throw new ApplicationException("Attenzione! Risultano prelievi da progetti scaduti"
								+ " per un importo di "	+ new it.cnr.contab.util.EuroFormat().format(impNegativiPrgScaduti)
								+ " che non risultano totalmente coperti da variazioni a favore"
								+ " di GAE di natura 6 - 'Reimpiego di risorse' o del CDR Personale o del CDR Ragioneria o del CDR Fiscale ("
								+ new it.cnr.contab.util.EuroFormat().format(impPositiviCashFund)+").");
				}
				{
					/**
					 * 70. se un progetto è attivo se vengono sottratti importi su GAE natura 6 queste devono essere girate ad Aree di uguale Natura
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

				BigDecimal impSaldoPrgAttiviFonteEsterna = listCtrlPianoEco.stream()
						.filter(el->!el.isScaduto(dataChiusura))
						.map(CtrlPianoEco::getDett)
						.flatMap(List::stream)
						.filter(el->!el.isUoArea())
						.filter(el->!el.isCdrPersonale())
						.filter(el->!el.isVoceSpeciale())
						.filter(el->!el.isUoRagioneria())
						.filter(el->!el.isUoFiscale())
						.filter(el->el.isNaturaFonteEsterna())
						.map(CtrlPianoEcoDett::getImporto)
						.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

				{
					/**
					 * 80. se un progetto è attivo se vengono sottratti importi su GAE natura FES queste devono essere girate ad Aree di uguale Natura o
					 *    al CDR Personale o alla UO Ragioneria o alla UO Fiscale su GAE Natura 6 se variazione multiprogetto o su GAE natura FES se variazione monoprogetto
					 */
					if (impSaldoPrgAttiviFonteEsterna.compareTo(BigDecimal.ZERO)<0) {
						//Vuol dire che ho ridotto progetti attivi sulle fonti esterne per cui deve essere bilanciato solo con Aree di uguale natura o
						// con CDR Personale
						BigDecimal impSaldoPrgAttiviCashFund = listCtrlPianoEco.stream()
								.filter(el->!el.isScaduto(dataChiusura))
								.map(CtrlPianoEco::getDett)
								.flatMap(List::stream)
								.filter(el->el.isUoArea()||el.isCdrPersonale()||el.isUoRagioneria()||el.isUoFiscale())
								.filter(el->el.isUoArea()?el.isNaturaFonteEsterna():Boolean.TRUE)
								.filter(el->{
									if (el.isUoRagioneria()||el.isUoFiscale()) {
										if (!isVariazioneMonoProgetto)
											return el.isNaturaReimpiego();
										else
											return el.isNaturaFonteEsterna();
									}
									return Boolean.TRUE;
								})
								.map(CtrlPianoEcoDett::getImporto)
								.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
		
						if (impSaldoPrgAttiviCashFund.compareTo(BigDecimal.ZERO)<0 ||
								impSaldoPrgAttiviCashFund.abs().compareTo(impSaldoPrgAttiviFonteEsterna.abs())<0)
							throw new ApplicationException("Attenzione! Risultano prelievi da progetti attivi"
									+ " per un importo di "	+ new it.cnr.contab.util.EuroFormat().format(impSaldoPrgAttiviFonteEsterna.abs())
									+ " su GAE Fonte Esterna che non risultano totalmente coperti da variazioni a favore"
									+ " di Aree su GAE Fonte Esterna o CDR Personale o Uo Ragioneria o Uo Fiscale su GAE "
									+ (isVariazioneMonoProgetto?"Fonte Esterna":"di natura 6") + " ("
									+ new it.cnr.contab.util.EuroFormat().format(impSaldoPrgAttiviCashFund.abs())+").");						
					}
				}
				{
					/**
					 * 90. in un progetto non scaduto è possibile prelevare fondi dalla voce speciale (11048) solo se assegnati a GAE di natura 6 dello stesso progetto
					 * 	(regola non valida per trasferimenti ad Aree)
					 */
					if (!isVariazioneArea) {
						listCtrlPianoEco.stream()
								.filter(el -> !el.isScaduto(dataChiusura))
								.filter(el -> el.getImpSpesaNegativiVoceSpeciale().compareTo(BigDecimal.ZERO) > 0)
								.filter(el -> el.getImpSpesaPositiviNaturaReimpiego().subtract(el.getImpSpesaNegativiNaturaReimpiego()).compareTo(el.getImpSpesaNegativiVoceSpeciale()) != 0)
								.findFirst().ifPresent(el -> {
							throw new DetailedRuntimeException("Attenzione! Sono stati prelevati fondi dalla voce speciale " + cdVoceSpeciale +" del progetto " +
									el.getProgetto().getCd_progetto() + " (" +
									new it.cnr.contab.util.EuroFormat().format(el.getImpSpesaNegativiVoceSpeciale()) +
									") non compensati da un equivalente assegnazione nell'ambito dello stesso progetto " +
									"su GAE di natura 6 - 'Reimpiego di risorse' ("+
									new it.cnr.contab.util.EuroFormat().format(el.getImpSpesaPositiviNaturaReimpiego().subtract(el.getImpSpesaNegativiNaturaReimpiego())) + ")");
						});

					}

					/**
					 * 90.1 è possibile attribuire fondi ad un progetto di natura 6 solo se ne vengono sottratti equivalenti da:
					 * 		a. un progetto scaduto
					 * 		b. dalla voce speciale (11048) sullo stesso progetto
					 * 		c. da una GAE di natura 6 sullo stesso progetto
					 * 	(regola non valida per trasferimenti ad Aree, Ragioneria e Fiscale)
					 */
					if (!isVariazioneArea && !isVariazioneRagioneria && !isVariazioneFiscale) {
						if (!isVariazioneTrasferimentoEsigenzeFinanziarie) {
							listCtrlPianoEco.stream()
									.filter(el -> !el.isScaduto(dataChiusura))
									.filter(el -> el.getImpSpesaNegativiNaturaReimpiego().compareTo(BigDecimal.ZERO) > 0)
									.filter(el -> el.getImpSpesaNegativiNaturaReimpiego().compareTo(el.getImpSpesaPositiviNaturaReimpiego()) != 0)
									.findFirst().ifPresent(el -> {
								throw new DetailedRuntimeException("Attenzione! Sono stati prelevati fondi dal progetto " +
										el.getProgetto().getCd_progetto() + " (" +
										new it.cnr.contab.util.EuroFormat().format(el.getImpSpesaNegativiNaturaReimpiego()) +
										") da GAE di natura 6 - 'Reimpiego di risorse' non compensati da un equivalente " +
										"assegnazione nell'ambito dello stesso progetto e della stessa natura (" +
										new it.cnr.contab.util.EuroFormat().format(el.getImpSpesaPositiviNaturaReimpiego()) + ")");
							});

							BigDecimal saldoPositivoNaturaReimpiego = listCtrlPianoEco.stream()
									.filter(el -> el.getImpSpesaPositiviNaturaReimpiego().subtract(el.getImpSpesaNegativiNaturaReimpiego()).compareTo(BigDecimal.ZERO) > 0)
									.map(el -> el.getImpSpesaPositiviNaturaReimpiego().subtract(el.getImpSpesaNegativiNaturaReimpiego()))
									.reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO);

							if (saldoPositivoNaturaReimpiego.compareTo(BigDecimal.ZERO) > 0) {
								BigDecimal impNegativiVoceSpecialePrgInCorso = listCtrlPianoEco.stream()
										.filter(el -> !el.isScaduto(dataChiusura))
										.filter(el -> el.getImpSpesaNegativiVoceSpeciale().compareTo(BigDecimal.ZERO) > 0)
										.map(CtrlPianoEco::getImpSpesaNegativiVoceSpeciale)
										.reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO);
								if (saldoPositivoNaturaReimpiego.compareTo(impNegativiPrgScaduti.add(impNegativiVoceSpecialePrgInCorso)) != 0)
									throw new ApplicationException("Attenzione! Risultano trasferimenti a GAE di natura 6 - 'Reimpiego di risorse' "
											+ " per un importo di " + new it.cnr.contab.util.EuroFormat().format(saldoPositivoNaturaReimpiego)
											+ " che non corrisponde all'importo prelevato da progetti scaduti e/o dalla voce " + cdVoceSpeciale
											+ " (" + new it.cnr.contab.util.EuroFormat().format(impNegativiPrgScaduti) + ").");
							}
						}
					} else {
						/**
						 * 90. in una variazione di area/ragioneria/fiscale se vengono sottratti importi su GAE natura 6 queste devono essere girate ad Aree o Ragioneria o Fiscale di uguale Natura
						 */
						BigDecimal impSaldoNaturaReimpiego = listCtrlPianoEco.stream()
								.map(CtrlPianoEco::getDett)
								.flatMap(List::stream)
								.filter(el->!el.isUoArea())
								.filter(el->el.isNaturaReimpiego())
								.map(CtrlPianoEcoDett::getImporto)
								.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

						if (impSaldoNaturaReimpiego.compareTo(BigDecimal.ZERO)<0) {
							//Vuol dire che ho ridotto su GAE Natura 6 per cui deve essere bilanciato solo con Aree/Ragioneria/Fiscale di uguale natura
							BigDecimal impSaldoNaturaReimpiegoAreaRagioneriaFiscale = listCtrlPianoEco.stream()
									.map(CtrlPianoEco::getDett)
									.flatMap(List::stream)
									.filter(el->isVariazioneArea?el.isUoArea():(isVariazioneRagioneria?el.isUoRagioneria():el.isUoFiscale()))
									.filter(el->el.isNaturaReimpiego())
									.map(CtrlPianoEcoDett::getImporto)
									.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

							if (impSaldoNaturaReimpiegoAreaRagioneriaFiscale.compareTo(BigDecimal.ZERO)<0 ||
									impSaldoNaturaReimpiegoAreaRagioneriaFiscale.abs().compareTo(impSaldoNaturaReimpiego.abs())!=0)
								throw new ApplicationException("Attenzione! Risultano prelievi"
										+ " per un importo di "	+ new it.cnr.contab.util.EuroFormat().format(impSaldoNaturaReimpiego.abs())
										+ " su GAE di natura 6 - 'Reimpiego di risorse' che non risultano totalmente coperti da variazioni a favore "
										+ (isVariazioneArea?"di Aree":(isVariazioneRagioneria?"della Ragioneria":"dell'Ufficio Fiscale"))
										+ " su GAE di natura 6 - 'Reimpiego di risorse' ("
										+ new it.cnr.contab.util.EuroFormat().format(impSaldoNaturaReimpiegoAreaRagioneriaFiscale.abs())+").");
						}
					}
				}
				{
					/**
					 * 100. se vengono spostate somme dalla voce speciale (11048) devono essere girate a GaeNatura6
					 */
					BigDecimal impNegativiVoceSpeciale = listCtrlPianoEco.stream()
							.filter(el->el.getImpSpesaNegativiVoceSpeciale().compareTo(BigDecimal.ZERO)>0)
							.map(CtrlPianoEco::getImpSpesaNegativiVoceSpeciale)
							.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
					if (impNegativiVoceSpeciale.compareTo(BigDecimal.ZERO)>0 && impNegativiVoceSpeciale.compareTo(impSpesaPositiviNaturaReimpiego)>0)
						throw new ApplicationException("Attenzione! Risultano prelievi dalla voce " + cdVoceSpeciale
								+ " per un importo di "	+ new it.cnr.contab.util.EuroFormat().format(impNegativiVoceSpeciale)
								+ " che non risultano totalmente coperti da variazioni a favore"
								+ " di GAE di natura 6 - 'Reimpiego di risorse' ("
								+ new it.cnr.contab.util.EuroFormat().format(impSpesaPositiviNaturaReimpiego)+").");
				}
				{
					if (isVariazionePersonale) {
						/**
						 * 110. se vengono assegnate somme al CDR Personale devono essere prelevate da progetti scaduti e/o da progetti attivi su GAE Fonte Esterna
						 */
						BigDecimal impSaldoPrgAttiviPersonale = listCtrlPianoEco.stream()
								.filter(el->!el.isScaduto(dataChiusura))
								.map(CtrlPianoEco::getDett)
								.flatMap(List::stream)
								.filter(el->el.isCdrPersonale())
								.map(CtrlPianoEcoDett::getImporto)
								.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

						if (impSaldoPrgAttiviPersonale.compareTo(BigDecimal.ZERO)>0) {
							BigDecimal saldoCashFund = impSaldoPrgAttiviFonteEsterna.add(impNegativiPrgScaduti.negate());
							if (saldoCashFund.compareTo(BigDecimal.ZERO) > 0 || impSaldoPrgAttiviPersonale.compareTo(saldoCashFund.abs()) != 0)
								throw new ApplicationException("Attenzione! Risultano assegnazioni al CDR Personale "
										+ " per un importo di " + new it.cnr.contab.util.EuroFormat().format(impSaldoPrgAttiviPersonale)
										+ " che non risultano totalmente coperti da prelievi da progetti scaduti e/o progetti attivi su GAE Fonte Esterna' ("
										+ new it.cnr.contab.util.EuroFormat().format(saldoCashFund.abs()) + ").");
						}
					}
				}
			}
		} catch (RemoteException|PersistencyException e) {
			throw new ComponentException(e);
		}
	}
	
	public BigDecimal getStanziamentoAssestatoProgetto(UserContext userContext, ProgettoBulk progetto, String tiGestione, Integer pEsercizio, Timestamp pDataLimite, String tiImporto) throws ComponentException{
		try{
			Voce_f_saldi_cdr_lineaHome saldiHome = (Voce_f_saldi_cdr_lineaHome)getHome(userContext, Voce_f_saldi_cdr_lineaBulk.class);
			
			Integer annoDtLimite = Optional.ofNullable(pDataLimite)
									   .map(el->{
										  Calendar calDtLimite = Calendar.getInstance();
										  calDtLimite.setTime(new Date(pDataLimite.getTime()));
										  return calDtLimite.get(Calendar.YEAR)-1;
									   })
									   .orElse(null);
			BigDecimal result = BigDecimal.ZERO;
			if (Elemento_voceHome.GESTIONE_ENTRATE.equals(tiGestione)) {
		        SQLBuilder saldiEtrSQL = saldiHome.createSQLBuilder();
		        saldiEtrSQL.addSQLClause(FindClause.AND, "VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE);
		        saldiEtrSQL.addTableToHeader("V_LINEA_ATTIVITA_VALIDA");
		        saldiEtrSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", "VOCE_F_SALDI_CDR_LINEA.ESERCIZIO");
		        saldiEtrSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA");
		        saldiEtrSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA");
		        saldiEtrSQL.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", SQLBuilder.EQUALS, progetto.getPg_progetto());
		
				List<Voce_f_saldi_cdr_lineaBulk> saldiList = new it.cnr.jada.bulk.BulkList(saldiHome.fetchAll(saldiEtrSQL));
				
				result = saldiList.stream()
							 .filter(el->Optional.ofNullable(pEsercizio).map(ese->ese.equals(el.getEsercizio_res())).orElse(Boolean.TRUE))
							 .filter(el->el.getEsercizio().equals(el.getEsercizio_res()))
							 .filter(el->Optional.ofNullable(annoDtLimite).map(aaDt->aaDt.compareTo(el.getEsercizio())>=0).orElse(Boolean.TRUE))
							 .map(el->{
								 //Prendo sempre lo stanziamento di tutti gli anni
								 BigDecimal assestato = el.getIm_stanz_iniziale_a1();
								 //e tutte le variazioni, tranne quelle dell'anno della data limite se presente, 
								 //che calcolo puntualmente dopo
								 if (Optional.ofNullable(annoDtLimite)
										 	 .map(annoLimite->el.getEsercizio().compareTo(annoLimite)<0)
										 	 .orElse(Boolean.TRUE))
									 assestato = assestato.add(el.getVariazioni_piu()).subtract(el.getVariazioni_meno());
								 return assestato;
							 })
							 .reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
				
				if (Optional.ofNullable(pDataLimite).isPresent()) {
					//sommo le variazioni dell'anno della data limite 
					Pdg_variazione_riga_gestHome varHome = (Pdg_variazione_riga_gestHome)getHome(userContext, Pdg_variazione_riga_gestBulk.class);
			        SQLBuilder varEtrSQL = varHome.createSQLBuilder();
			        varEtrSQL.addSQLClause(FindClause.AND, "PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO", SQLBuilder.EQUALS, annoDtLimite);
			        varEtrSQL.addSQLClause(FindClause.AND, "PDG_VARIAZIONE_RIGA_GEST.TI_GESTIONE", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE);
			        varEtrSQL.addTableToHeader("PDG_VARIAZIONE");
			        varEtrSQL.addSQLJoin("PDG_VARIAZIONE.ESERCIZIO", "PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
			        varEtrSQL.addSQLJoin("PDG_VARIAZIONE.PG_VARIAZIONE_PDG", "PDG_VARIAZIONE_RIGA_GEST.PG_VARIAZIONE_PDG");

			        varEtrSQL.addSQLClause(FindClause.OR, "PDG_VARIAZIONE.DT_CHIUSURA", SQLBuilder.GREATER_EQUALS, pDataLimite);
			        varEtrSQL.openParenthesis(FindClause.AND);
			        varEtrSQL.addSQLClause(FindClause.OR, "PDG_VARIAZIONE.STATO", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVATA);
			        varEtrSQL.addSQLClause(FindClause.OR, "PDG_VARIAZIONE.STATO", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVAZIONE_FORMALE);
			        varEtrSQL.addSQLClause(FindClause.OR, "PDG_VARIAZIONE.STATO", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_PROPOSTA_DEFINITIVA);
			        varEtrSQL.closeParenthesis();
				        
			        varEtrSQL.addTableToHeader("V_LINEA_ATTIVITA_VALIDA");
			        varEtrSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", "PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
			        varEtrSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "PDG_VARIAZIONE_RIGA_GEST.CD_CENTRO_RESPONSABILITA");
			        varEtrSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "PDG_VARIAZIONE_RIGA_GEST.CD_LINEA_ATTIVITA");
			        varEtrSQL.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", SQLBuilder.EQUALS, progetto.getPg_progetto());
			        
					List<Pdg_variazione_riga_gestBulk> varList = new it.cnr.jada.bulk.BulkList(varHome.fetchAll(varEtrSQL));

			        BigDecimal variazioni = varList.stream()
								 .map(Pdg_variazione_riga_gestBulk::getIm_entrata)
								 .reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
				        
			        result = result.add(variazioni);
				} 
			} else if (Elemento_voceHome.GESTIONE_SPESE.equals(tiGestione)) {
				it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configSession = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
				String cdNaturaReimpiego = configSession.getVal01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_NATURA_REIMPIEGO);
				
		        SQLBuilder saldiSpeSQL = saldiHome.createSQLBuilder();
		        saldiSpeSQL.addSQLClause(FindClause.AND, "VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
		        saldiSpeSQL.addTableToHeader("V_LINEA_ATTIVITA_VALIDA");
		        saldiSpeSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", "VOCE_F_SALDI_CDR_LINEA.ESERCIZIO");
		        saldiSpeSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA");
		        saldiSpeSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA");
		        saldiSpeSQL.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", SQLBuilder.EQUALS, progetto.getPg_progetto());

		        saldiSpeSQL.addTableToHeader("NATURA");
		        saldiSpeSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_NATURA", "NATURA.CD_NATURA");

		        if (Progetto_other_fieldHome.TI_IMPORTO_FINANZIATO.equals(tiImporto)) {
			        saldiSpeSQL.addSQLClause(FindClause.AND, "NATURA.TIPO", SQLBuilder.EQUALS, NaturaBulk.TIPO_NATURA_FONTI_ESTERNE);
			        saldiSpeSQL.addSQLClause(FindClause.AND, "NATURA.CD_NATURA", SQLBuilder.NOT_EQUALS, cdNaturaReimpiego);
		        } else if (Progetto_other_fieldHome.TI_IMPORTO_COFINANZIATO.equals(tiImporto)) {
		        	saldiSpeSQL.openParenthesis(FindClause.AND);
		        	saldiSpeSQL.addSQLClause(FindClause.OR, "NATURA.TIPO", SQLBuilder.EQUALS, NaturaBulk.TIPO_NATURA_FONTI_INTERNE);
			        saldiSpeSQL.addSQLClause(FindClause.OR, "V_LINEA_ATTIVITA_VALIDA.CD_NATURA", SQLBuilder.EQUALS, cdNaturaReimpiego);
			        saldiSpeSQL.closeParenthesis();
		        }

		        List<Voce_f_saldi_cdr_lineaBulk> saldiList = new it.cnr.jada.bulk.BulkList(saldiHome.fetchAll(saldiSpeSQL));
				
				result = saldiList.stream()
							 .filter(el->Optional.ofNullable(pEsercizio).map(ese->ese.equals(el.getEsercizio_res())).orElse(Boolean.TRUE))
							 .filter(el->Optional.ofNullable(annoDtLimite).map(aaDt->aaDt.compareTo(el.getEsercizio())>=0).orElse(Boolean.TRUE))
							 .map(el->{
								 BigDecimal assestato = BigDecimal.ZERO;
								 //Prendo sempre lo stanziamento di tutti gli anni
								 if (el.getEsercizio().equals(el.getEsercizio_res()))
									 assestato = el.getIm_stanz_iniziale_a1();
								 //e tutte le variazioni, tranne quelle dell'anno della data limite se presente, 
								 //che devo calcolare puntualmente
								 if (Optional.ofNullable(annoDtLimite)
										 	 .map(annoLimite->el.getEsercizio().compareTo(annoLimite)<0)
										 	 .orElse(Boolean.TRUE)){
									 if (el.getEsercizio().equals(el.getEsercizio_res()))
										 assestato = assestato.add(el.getVariazioni_piu()).subtract(el.getVariazioni_meno());
									 else
										 assestato = assestato.add(el.getVar_piu_stanz_res_imp()).subtract(el.getVar_meno_stanz_res_imp());
								 }
								 return assestato;
							 })
							 .reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
	
				if (Optional.ofNullable(pDataLimite).isPresent()) {
					if (Optional.ofNullable(pEsercizio).map(ese->ese.equals(annoDtLimite)).orElse(Boolean.TRUE)) {
						//sommo le variazioni di competenza dell'anno della data limite 
						Pdg_variazione_riga_gestHome varHome = (Pdg_variazione_riga_gestHome)getHome(userContext, Pdg_variazione_riga_gestBulk.class);
				        SQLBuilder varSpeCompSQL = varHome.createSQLBuilder();
				        varSpeCompSQL.addSQLClause(FindClause.AND, "PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO", SQLBuilder.EQUALS, annoDtLimite);
				        varSpeCompSQL.addSQLClause(FindClause.AND, "PDG_VARIAZIONE_RIGA_GEST.TI_GESTIONE", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
				        varSpeCompSQL.addTableToHeader("PDG_VARIAZIONE");
				        varSpeCompSQL.addSQLJoin("PDG_VARIAZIONE.ESERCIZIO", "PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
				        varSpeCompSQL.addSQLJoin("PDG_VARIAZIONE.PG_VARIAZIONE_PDG", "PDG_VARIAZIONE_RIGA_GEST.PG_VARIAZIONE_PDG");

				        varSpeCompSQL.addSQLClause(FindClause.OR, "PDG_VARIAZIONE.DT_CHIUSURA", SQLBuilder.GREATER_EQUALS, pDataLimite);
				        varSpeCompSQL.openParenthesis(FindClause.AND);
				        varSpeCompSQL.addSQLClause(FindClause.OR, "PDG_VARIAZIONE.STATO", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVATA);
				        varSpeCompSQL.addSQLClause(FindClause.OR, "PDG_VARIAZIONE.STATO", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_APPROVAZIONE_FORMALE);
				        varSpeCompSQL.addSQLClause(FindClause.OR, "PDG_VARIAZIONE.STATO", SQLBuilder.EQUALS, Pdg_variazioneBulk.STATO_PROPOSTA_DEFINITIVA);
				        varSpeCompSQL.closeParenthesis();
				        
				        varSpeCompSQL.openParenthesis(FindClause.AND);
				        varSpeCompSQL.addSQLClause(FindClause.OR,"PDG_VARIAZIONE.TI_MOTIVAZIONE_VARIAZIONE",SQLBuilder.ISNULL,null);
				        varSpeCompSQL.addSQLClause(FindClause.OR,"PDG_VARIAZIONE.TI_MOTIVAZIONE_VARIAZIONE",SQLBuilder.NOT_EQUALS,Pdg_variazioneBulk.MOTIVAZIONE_TRASFERIMENTO_AREA);
				        varSpeCompSQL.addSQLClause(FindClause.OR,"PDG_VARIAZIONE_RIGA_GEST.IM_SPESE_GEST_DECENTRATA_INT",SQLBuilder.GREATER,BigDecimal.ZERO);
				        varSpeCompSQL.addSQLClause(FindClause.OR,"PDG_VARIAZIONE_RIGA_GEST.IM_SPESE_GEST_ACCENTRATA_INT",SQLBuilder.GREATER,BigDecimal.ZERO);
				        varSpeCompSQL.addSQLClause(FindClause.OR,"PDG_VARIAZIONE_RIGA_GEST.IM_SPESE_GEST_DECENTRATA_EST",SQLBuilder.GREATER,BigDecimal.ZERO);
				        varSpeCompSQL.addSQLClause(FindClause.OR,"PDG_VARIAZIONE_RIGA_GEST.IM_SPESE_GEST_DECENTRATA_EST",SQLBuilder.GREATER,BigDecimal.ZERO);
				        varSpeCompSQL.closeParenthesis();
				        		
				        varSpeCompSQL.addTableToHeader("V_LINEA_ATTIVITA_VALIDA");
				        varSpeCompSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", "PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
				        varSpeCompSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "PDG_VARIAZIONE_RIGA_GEST.CD_CENTRO_RESPONSABILITA");
				        varSpeCompSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "PDG_VARIAZIONE_RIGA_GEST.CD_LINEA_ATTIVITA");
				        varSpeCompSQL.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", SQLBuilder.EQUALS, progetto.getPg_progetto());

				        varSpeCompSQL.addTableToHeader("NATURA");
				        varSpeCompSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_NATURA", "NATURA.CD_NATURA");
	
				        if (Progetto_other_fieldHome.TI_IMPORTO_FINANZIATO.equals(tiImporto)) {
				        	varSpeCompSQL.addSQLClause(FindClause.AND, "NATURA.TIPO", SQLBuilder.EQUALS, NaturaBulk.TIPO_NATURA_FONTI_ESTERNE);
				        	varSpeCompSQL.addSQLClause(FindClause.AND, "NATURA.CD_NATURA", SQLBuilder.NOT_EQUALS, cdNaturaReimpiego);
				        } else if (Progetto_other_fieldHome.TI_IMPORTO_COFINANZIATO.equals(tiImporto)) {
				        	varSpeCompSQL.openParenthesis(FindClause.AND);
				        	varSpeCompSQL.addSQLClause(FindClause.OR, "NATURA.TIPO", SQLBuilder.EQUALS, NaturaBulk.TIPO_NATURA_FONTI_INTERNE);
				        	varSpeCompSQL.addSQLClause(FindClause.OR, "V_LINEA_ATTIVITA_VALIDA.CD_NATURA", SQLBuilder.EQUALS, cdNaturaReimpiego);
				        	varSpeCompSQL.closeParenthesis();
				        }

				        List<Pdg_variazione_riga_gestBulk> varSpeCompList = new it.cnr.jada.bulk.BulkList(varHome.fetchAll(varSpeCompSQL));

				        BigDecimal variazioniCompetenza = varSpeCompList.stream()
								 .map(el->{
									 BigDecimal resultEst = BigDecimal.ZERO;
									 if (Utility.nvl(el.getIm_spese_gest_decentrata_est()).compareTo(BigDecimal.ZERO)!=0) 
										 resultEst = Utility.nvl(el.getIm_spese_gest_decentrata_est());
									 else if (Utility.nvl(el.getIm_spese_gest_accentrata_est()).compareTo(BigDecimal.ZERO)!=0 &&
											 (el.getCd_cdr_assegnatario_clgs()!=null || el.isDettaglioScaricato()))
										 resultEst = Utility.nvl(el.getIm_spese_gest_accentrata_est());

									 if (Progetto_other_fieldHome.TI_IMPORTO_FINANZIATO.equals(tiImporto))
										 return resultEst;
									 else if (Progetto_other_fieldHome.TI_IMPORTO_COFINANZIATO.equals(tiImporto)) {
										 BigDecimal resultInt = BigDecimal.ZERO;
										 if (Utility.nvl(el.getIm_spese_gest_decentrata_int()).compareTo(BigDecimal.ZERO)!=0) 
											 resultInt = Utility.nvl(el.getIm_spese_gest_decentrata_int());
										 else if (Utility.nvl(el.getIm_spese_gest_accentrata_int()).compareTo(BigDecimal.ZERO)!=0 &&
												 (el.getCd_cdr_assegnatario_clgs()!=null || el.isDettaglioScaricato()))
											 resultInt = Utility.nvl(el.getIm_spese_gest_accentrata_int());
										 return resultEst.add(resultInt);
									 }
									 return BigDecimal.ZERO;
								 })
								 .reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
				        
				        result = result.add(variazioniCompetenza);
					}
					
					if (Optional.ofNullable(pEsercizio).map(ese->!ese.equals(annoDtLimite)).orElse(Boolean.TRUE)) {
						//sommo le variazioni di residuo dell'anno della data limite 
						Var_stanz_res_rigaHome varHome = (Var_stanz_res_rigaHome)getHome(userContext, Var_stanz_res_rigaBulk.class);
				        SQLBuilder varSpeResSQL = varHome.createSQLBuilder();
				        varSpeResSQL.addSQLClause(FindClause.AND, "VAR_STANZ_RES_RIGA.ESERCIZIO", SQLBuilder.EQUALS, annoDtLimite);
				        varSpeResSQL.addSQLClause(FindClause.AND, "VAR_STANZ_RES_RIGA.TI_GESTIONE", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
				        if (Optional.ofNullable(pEsercizio).isPresent())
					        varSpeResSQL.addSQLClause(FindClause.AND, "VAR_STANZ_RES_RIGA.ESERCIZIO_RES", SQLBuilder.EQUALS, pEsercizio);
				        varSpeResSQL.addTableToHeader("VAR_STANZ_RES");
				        varSpeResSQL.addSQLJoin("VAR_STANZ_RES.ESERCIZIO", "VAR_STANZ_RES_RIGA.ESERCIZIO");
				        varSpeResSQL.addSQLJoin("VAR_STANZ_RES.PG_VARIAZIONE", "VAR_STANZ_RES_RIGA.PG_VARIAZIONE");

				        varSpeResSQL.addSQLClause(FindClause.OR, "VAR_STANZ_RES.DT_CHIUSURA", SQLBuilder.GREATER_EQUALS, pDataLimite);
				        varSpeResSQL.openParenthesis(FindClause.AND);
				        varSpeResSQL.addSQLClause(FindClause.OR, "VAR_STANZ_RES.STATO", SQLBuilder.EQUALS, Var_stanz_resBulk.STATO_APPROVATA);
				        varSpeResSQL.addSQLClause(FindClause.OR, "VAR_STANZ_RES.STATO", SQLBuilder.EQUALS, Var_stanz_resBulk.STATO_PROPOSTA_DEFINITIVA);
				        varSpeResSQL.closeParenthesis();

				        varSpeResSQL.openParenthesis(FindClause.AND);
				        varSpeResSQL.addSQLClause(FindClause.OR,"VAR_STANZ_RES.TI_MOTIVAZIONE_VARIAZIONE",SQLBuilder.ISNULL,null);
				        varSpeResSQL.addSQLClause(FindClause.OR,"VAR_STANZ_RES.TI_MOTIVAZIONE_VARIAZIONE",SQLBuilder.NOT_EQUALS,Pdg_variazioneBulk.MOTIVAZIONE_TRASFERIMENTO_AREA);
				        varSpeResSQL.addSQLClause(FindClause.OR,"VAR_STANZ_RES_RIGA.IM_VARIAZIONE",SQLBuilder.GREATER,BigDecimal.ZERO);
				        varSpeResSQL.closeParenthesis();
				        
				        varSpeResSQL.addTableToHeader("V_LINEA_ATTIVITA_VALIDA");
				        varSpeResSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", "VAR_STANZ_RES_RIGA.ESERCIZIO");
				        varSpeResSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "VAR_STANZ_RES_RIGA.CD_CENTRO_RESPONSABILITA");
				        varSpeResSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "VAR_STANZ_RES_RIGA.CD_LINEA_ATTIVITA");
				        varSpeResSQL.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", SQLBuilder.EQUALS, progetto.getPg_progetto());

				        varSpeResSQL.addTableToHeader("NATURA");
				        varSpeResSQL.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_NATURA", "NATURA.CD_NATURA");
	
				        if (Progetto_other_fieldHome.TI_IMPORTO_FINANZIATO.equals(tiImporto)) {
				        	varSpeResSQL.addSQLClause(FindClause.AND, "NATURA.TIPO", SQLBuilder.EQUALS, NaturaBulk.TIPO_NATURA_FONTI_ESTERNE);
				        	varSpeResSQL.addSQLClause(FindClause.AND, "NATURA.CD_NATURA", SQLBuilder.NOT_EQUALS, cdNaturaReimpiego);
				        } else if (Progetto_other_fieldHome.TI_IMPORTO_COFINANZIATO.equals(tiImporto)) {
				        	varSpeResSQL.openParenthesis(FindClause.AND);
				        	varSpeResSQL.addSQLClause(FindClause.OR, "NATURA.TIPO", SQLBuilder.EQUALS, NaturaBulk.TIPO_NATURA_FONTI_INTERNE);
				        	varSpeResSQL.addSQLClause(FindClause.OR, "V_LINEA_ATTIVITA_VALIDA.CD_NATURA", SQLBuilder.EQUALS, cdNaturaReimpiego);
				        	varSpeResSQL.closeParenthesis();
				        }

				        List<Var_stanz_res_rigaBulk> varSpeResList = new it.cnr.jada.bulk.BulkList(varHome.fetchAll(varSpeResSQL));

				        BigDecimal variazioniResiduo = varSpeResList.stream()
								 .map(el->{
									 BigDecimal resultEst = BigDecimal.ZERO;
									 if (el.getVar_stanz_res().getTipologia_fin().equals(NaturaBulk.TIPO_NATURA_FONTI_ESTERNE)) 
										 resultEst = Utility.nvl(el.getIm_variazione());

									 if (Progetto_other_fieldHome.TI_IMPORTO_FINANZIATO.equals(tiImporto))
										 return resultEst;
									 else if (Progetto_other_fieldHome.TI_IMPORTO_COFINANZIATO.equals(tiImporto)) {
										 BigDecimal resultInt = BigDecimal.ZERO;
										 if (el.getVar_stanz_res().getTipologia_fin().equals(NaturaBulk.TIPO_NATURA_FONTI_INTERNE)) 
											 resultInt = Utility.nvl(el.getIm_variazione());
										 return resultEst.add(resultInt);
									 }
									 return BigDecimal.ZERO;
								 })
								 .reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
				        
				        result = result.add(variazioniResiduo);
					}
				}
			}
			return result;
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
	
	private CdrBulk getCDRPersonale(UserContext userContext) throws ComponentException{
		try {
			String cdrPersonale = Optional.ofNullable(((Configurazione_cnrHome)getHome(userContext,Configurazione_cnrBulk.class)).getCdrPersonale(CNRUserContext.getEsercizio(userContext)))
				.orElseThrow(() -> new ComponentException("Non è possibile individuare il codice CDR del Personale."));
			return (CdrBulk)getHome(userContext, CdrBulk.class).findByPrimaryKey(new CdrBulk(cdrPersonale));
		} catch(Throwable e) {
			throw handleException(e);
		}
	}

	//Controllo che restituisce errore.
	//Se la variazione passa a definitivo controllo che non siano modificate combinazioni contabili per le quali sia attivo un blocco residui
	public void checkBloccoImpegniNatfin(UserContext userContext, Var_stanz_resBulk variazione) throws ComponentException {
		try	{
			Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).
					findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));
			/*
			 * non effettuo alcun controllo se è collegata la UO Ente e la variazione è fatta dalla UO Ente
			 */
			if (uoScrivania.isUoEnte() && variazione.getCentroDiResponsabilita().getUnita_padre().isUoEnte())
				return;

			Var_stanz_resHome varResHome = (Var_stanz_resHome)getHome(userContext,Var_stanz_resBulk.class);
			for (java.util.Iterator dett = varResHome.findAllVariazioniRiga(variazione).iterator(); dett.hasNext(); ) {
				Var_stanz_res_rigaBulk rigaVar = (Var_stanz_res_rigaBulk) dett.next();
				checkBloccoImpegniNatfin(userContext, rigaVar.getLinea_di_attivita().getCd_centro_responsabilita(), rigaVar.getLinea_di_attivita().getCd_linea_attivita(),
						rigaVar.getElemento_voce(), ObbligazioneBulk.TIPO_RESIDUO_IMPROPRIO);
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);
		}
	}

	public void checkBloccoImpegniNatfin(UserContext userContext, String cdr, String cdLineaAttivita, Elemento_voceBulk elementoVoceBulk, String tipoObbligazione) throws ComponentException {
		WorkpackageBulk workpackageBulk = ((WorkpackageHome) getHome(userContext, WorkpackageBulk.class)).searchGAECompleta(userContext, CNRUserContext.getEsercizio(userContext),
					cdr, cdLineaAttivita);
		checkBloccoImpegniNatfin(userContext, workpackageBulk, elementoVoceBulk, tipoObbligazione);
	}

	public void checkBloccoImpegniNatfin(UserContext userContext, WorkpackageBulk workpackageBulk, Elemento_voceBulk elementoVoceBulk, String tipoObbligazione) throws ComponentException {
    	try {
    		if ((ObbligazioneBulk.TIPO_COMPETENZA.equals(tipoObbligazione) && elementoVoceBulk.isAttivoBloccoResiduiNatfinCompetenza()) ||
					((ObbligazioneBulk.TIPO_RESIDUO_PROPRIO.equals(tipoObbligazione) || ObbligazioneBulk.TIPO_RESIDUO_IMPROPRIO.equals(tipoObbligazione)) && elementoVoceBulk.isAttivoBloccoResiduiNatfinResidui())) {
				Parametri_cdsBulk param_cds = (Parametri_cdsBulk)(getHome(userContext, Parametri_cdsBulk.class)).findByPrimaryKey(new Parametri_cdsBulk(CNRUserContext.getCd_cds(userContext),CNRUserContext.getEsercizio(userContext)));
				if (param_cds.getFl_blocco_impegni_natfin().equals(Boolean.TRUE)) {
					Configurazione_cnrBulk configBulk = Utility.createConfigurazioneCnrComponentSession().getConfigurazione(userContext, CNRUserContext.getEsercizio(userContext), null, Configurazione_cnrBulk.PK_BLOCCO_RESIDUI, Configurazione_cnrBulk.SK_NATURA_FINANZIAMENTO);
					if (Optional.ofNullable(configBulk).isPresent()) {
						Optional<String> optNatura = Optional.ofNullable(configBulk).map(Configurazione_cnrBulk::getVal01);
						Optional<String> optCodFinanziamento = Optional.ofNullable(configBulk).map(Configurazione_cnrBulk::getVal02);
						if (optNatura.isPresent() || optCodFinanziamento.isPresent()) {
							if (optNatura.map(el -> el.equals(workpackageBulk.getNatura().getTipo())).orElse(Boolean.TRUE) &&
									optCodFinanziamento.map(el -> el.equals(workpackageBulk.getProgetto().getOtherField().getTipoFinanziamento().getCodice())).orElse(Boolean.TRUE))
								throw new ApplicationException("Non è possibile effettuare movimentazioni per il CDR/GAE/Voce (" +
										workpackageBulk.getCd_centro_responsabilita() + "/" + workpackageBulk.getCd_linea_attivita() + "/" + elementoVoceBulk.getCd_voce() +
										"), in quanto " +
										optNatura.map(el -> "GAE di natura " + el).orElse("") +
										(optNatura.isPresent() && optCodFinanziamento.isPresent() ? " e " : "") +
										optCodFinanziamento.map(el -> "Progetto di tipo " + el + " ").orElse("") + ".");
						}
					}
				}
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (RemoteException e) {
			throw new ComponentException(e);
		}
	}

	public void checkBloccoLimiteClassificazione(UserContext userContext, Pdg_variazioneBulk variazione) throws ComponentException {
		try {
			Pdg_variazioneHome detHome = (Pdg_variazioneHome)getHome(userContext,Pdg_variazioneBulk.class);
			V_classificazione_vociHome vClassVociHome = (V_classificazione_vociHome)getHome(userContext, V_classificazione_vociBulk.class);
			Elemento_voceHome elementoVoceHome = (Elemento_voceHome)getHome(userContext, Elemento_voceBulk.class);
			V_assestatoHome assHome = (V_assestatoHome)getHome(userContext,V_assestatoBulk.class);
			LimiteSpesaClassHome dettHome = (LimiteSpesaClassHome)getHome(userContext,LimiteSpesaClassBulk.class);

			//CERCO TUTTE LE CLASSIFICAZIONI CON LIMITE
			SQLBuilder sqlClassLimite = vClassVociHome.createSQLBuilder();
			sqlClassLimite.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
			sqlClassLimite.addClause(FindClause.AND, "im_limite_assestato", SQLBuilder.ISNOTNULL, null);
			sqlClassLimite.addClause(FindClause.AND, "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
			List<V_classificazione_vociBulk> listClass = vClassVociHome.fetchAll(sqlClassLimite);

			//CREO UNA MAPPA CHE PER OGNI CLASSIFICAZIONE CON LIMITE ABBIA I DATI DEI CDS E DELLE VOCI ASSOCIATE
			Map<V_classificazione_vociBulk, List<Elemento_voceBulk>> mapClassificazioni = new HashMap<V_classificazione_vociBulk, List<Elemento_voceBulk>>();
			Optional.ofNullable(listClass)
					.map(List::stream)
					.orElse(Stream.empty())
					.forEach(classLimite->{
						//verifico se la classificazione con limite appartiene a
						try {
							classLimite.setLimitiSpesaClassColl(new BulkList(dettHome.getDetailsFor(classLimite)));

							//recupero tutte le voci della classificazione con limite
							SQLBuilder sqlElementoVoce = elementoVoceHome.selectElementoVociAssociate(classLimite.getEsercizio(), classLimite.getNr_livello(), classLimite.getId_classificazione());
							sqlElementoVoce.addClause(FindClause.AND, "fl_limite_competenza", SQLBuilder.EQUALS, Boolean.TRUE);
							List<Elemento_voceBulk> listVociClass = elementoVoceHome.fetchAll(sqlElementoVoce);

							mapClassificazioni.put(classLimite,listVociClass);
						} catch (PersistencyException e) {
							throw new RuntimeException(e);
						}
					});


			//RAGGRUPPO PER CDS TUTTE LE RIGHE DI VARIAZIONE CON VOCE DI BILANCIO CON GESTIONE LIMITE ATTIVO
			Collection<Pdg_variazione_riga_gestBulk> righeVar = detHome.findDettagliSpesaVariazioneGestionale(variazione);
			getHomeCache(userContext).fetchAll(userContext);
			Map<String, List<Pdg_variazione_riga_gestBulk>> cdsMap =
					Optional.ofNullable(righeVar)
							.map(el->el.stream())
							.orElse(Stream.empty())
							.filter(riga->riga.getElemento_voce().getFl_limite_competenza().equals(Boolean.TRUE))
							.collect(Collectors.groupingBy(o->o.getCdr_assegnatario().getCd_cds()));

			cdsMap.keySet().stream().forEach(cds-> {
				mapClassificazioni.keySet().forEach(classificazione->{
					List<Elemento_voceBulk> listVociClass = mapClassificazioni.get(classificazione);
					//trovo l'importo della variazione provvisoria delle voci associate alla classificazione
					BigDecimal impCurrentVariazioneClass =
							cdsMap.get(cds).stream()
								  .filter(rigavar->{
								  		return rigavar.getLinea_attivita().getNatura().isFonteInterna();
								  })
								  .filter(rigavar->{
										return listVociClass.stream()
												.filter(voceClass->voceClass.getEsercizio().equals(rigavar.getElemento_voce().getEsercizio()))
												.filter(voceClass->voceClass.getTi_gestione().equals(rigavar.getElemento_voce().getTi_gestione()))
												.filter(voceClass->voceClass.getTi_appartenenza().equals(rigavar.getElemento_voce().getTi_appartenenza()))
												.filter(voceClass->voceClass.getCd_elemento_voce().equals(rigavar.getElemento_voce().getCd_elemento_voce()))
												.findFirst().isPresent();
								  })
								  .map(Pdg_variazione_riga_gestBulk::getIm_variazione)
								  .reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

					if (impCurrentVariazioneClass.compareTo(BigDecimal.ZERO)>0) {
						List<V_assestatoBulk> listAssestatoClass =
								listVociClass.stream()
										.filter(voceClass->voceClass.getFl_limite_competenza().equals(Boolean.TRUE))
										.flatMap(voceClass->{
											try {
												SQLBuilder sqlAssestato = assHome.createSQLBuilder();
												sqlAssestato.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, voceClass.getEsercizio());
												sqlAssestato.addClause(FindClause.AND, "esercizio_res", SQLBuilder.EQUALS, voceClass.getEsercizio());
												sqlAssestato.addClause(FindClause.AND, "ti_gestione", SQLBuilder.EQUALS, voceClass.getTi_gestione());
												sqlAssestato.addClause(FindClause.AND, "ti_appartenenza", SQLBuilder.EQUALS, voceClass.getTi_appartenenza());
												sqlAssestato.addClause(FindClause.AND, "cd_elemento_voce", SQLBuilder.EQUALS, voceClass.getCd_elemento_voce());

												sqlAssestato.addTableToHeader("NATURA");
												sqlAssestato.addSQLJoin("V_ASSESTATO.CD_NATURA","NATURA.CD_NATURA");
												sqlAssestato.addSQLClause(FindClause.AND,"NATURA.TIPO", SQLBuilder.EQUALS, NaturaBulk.TIPO_NATURA_FONTI_INTERNE);

												sqlAssestato.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "A");
												sqlAssestato.addSQLJoin("V_ASSESTATO.ESERCIZIO","A.ESERCIZIO");
												sqlAssestato.addSQLJoin("V_ASSESTATO.CD_CENTRO_RESPONSABILITA","A.CD_ROOT");
												sqlAssestato.addSQLClause(FindClause.AND,"A.CD_CDS", SQLBuilder.EQUALS, cds);

												List<V_assestatoBulk> listAssestati = assHome.fetchAll(sqlAssestato);
												//ritorna l'assestato della singola voce
												return listAssestati.stream();
											} catch (PersistencyException e) {
												throw new RuntimeException(e);
											}
										})
								.collect(Collectors.toList());

						BigDecimal impApprovatoClass = listAssestatoClass.stream().map(V_assestatoBulk::getAssestato_iniziale).reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
						BigDecimal impVariazioniDefinitiveClass = listAssestatoClass.stream().map(V_assestatoBulk::getVariazioni_definitive).reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

						//recupero il limite per il cds
						BigDecimal impLimiteClass = Optional.ofNullable(classificazione.getLimitiSpesaClassColl())
								.map(List::stream)
								.orElse(Stream.empty())
								.filter(el->el.getCd_cds().equals(cds))
								.map(LimiteSpesaClassBulk::getIm_limite_assestato)
								.findFirst()
								.orElse(BigDecimal.ZERO);

						/*
							Il controllo viene effettuato su:
								assestato (dato dagli stanziamenti e le variazioni approvate)
								+variazioni definitive
								+variazione provvisoria corrente che sta diventando definitiva
						 */
						if (impApprovatoClass.add(impVariazioniDefinitiveClass).compareTo(impLimiteClass)>0)
							throw new ApplicationRuntimeException("Operazione non possibile!\nLa quota stanziata dal CDS "+cds+
									" per la classificazione '"+classificazione.getCd_classificazione()+" - "+classificazione.getDs_classificazione()+
									"',  di euro "+ new it.cnr.contab.util.EuroFormat().format(impApprovatoClass.add(impVariazioniDefinitiveClass))+
									"\n(ottenuta sommando alla quota approvata di euro "+new it.cnr.contab.util.EuroFormat().format(impApprovatoClass)+" la quota di altre variazioni definitive " +
									"in attesa di approvazione di euro "+new it.cnr.contab.util.EuroFormat().format(impVariazioniDefinitiveClass.subtract(impCurrentVariazioneClass))+ " e la quota " +
									"della corrente variazione di euro "+new it.cnr.contab.util.EuroFormat().format(impCurrentVariazioneClass)+")\nsupererebbe l'importo " +
									"limite stabilito di euro "+new it.cnr.contab.util.EuroFormat().format(impLimiteClass)+".");
					}
				});
			});
		} catch (DetailedRuntimeException _ex) {
			throw new ApplicationException(_ex.getMessage());
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
	}
}