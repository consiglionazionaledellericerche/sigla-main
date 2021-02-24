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

package it.cnr.contab.progettiric00.core.bulk;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.util.OrderedHashtable;

public class Progetto_piano_economicoBulk extends Progetto_piano_economicoBase {
	private static final long serialVersionUID = 1L;

	private Voce_piano_economico_prgBulk voce_piano_economico;
	private ProgettoBulk progetto;
	private Progetto_rimodulazioneBulk progettoRimodulazione;
	private BulkList<Ass_progetto_piaeco_voceBulk> vociBilancioAssociate = new BulkList<Ass_progetto_piaeco_voceBulk>();

	private java.math.BigDecimal imSpesaFinanziatoRimodulato;
	private java.math.BigDecimal imSpesaCofinanziatoRimodulato;
	
	private java.math.BigDecimal imSpesaFinanziatoRimodulatoPreDelete;
	private java.math.BigDecimal imSpesaCofinanziatoRimodulatoPreDelete;
	private boolean detailDerivato;

	//Indica il numero di anni precedenti per il quale è possibile caricare un piano economico rispetto alla data di inizio del progetto
	public final static Integer ANNIPRE_PIANOECONOMICO = 14;

	public Progetto_piano_economicoBulk() {
		super();
	}

	public Progetto_piano_economicoBulk(java.lang.Integer pg_progetto, java.lang.String cd_unita_organizzativa, java.lang.String cd_voce_piano, java.lang.Integer esercizio_piano) {
		super(pg_progetto, cd_unita_organizzativa, cd_voce_piano, esercizio_piano);
	}
	
	public Voce_piano_economico_prgBulk getVoce_piano_economico() {
		return voce_piano_economico;
	}
	
	public void setVoce_piano_economico(Voce_piano_economico_prgBulk voce_piano_economico) {
		this.voce_piano_economico = voce_piano_economico;
	}
	
	public java.lang.String getCd_unita_organizzativa() {
		it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk voce_piano_economico = this.getVoce_piano_economico();
		if (voce_piano_economico == null)
			return null;
		return voce_piano_economico.getCd_unita_organizzativa();
	}

	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.getVoce_piano_economico().setCd_unita_organizzativa(cd_unita_organizzativa);
	}
	
	@Override
	public String getCd_voce_piano() {
		it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk voce_piano_economico = this.getVoce_piano_economico();
		if (voce_piano_economico == null)
			return null;
		return voce_piano_economico.getCd_voce_piano();
	}
	
	@Override
	public void setCd_voce_piano(String cd_voce_piano) {
		this.getVoce_piano_economico().setCd_voce_piano(cd_voce_piano);
	}
	
	public ProgettoBulk getProgetto() {
		return progetto;
	}
	
	public void setProgetto(ProgettoBulk progetto) {
		this.progetto = progetto;
	}

	public Progetto_rimodulazioneBulk getProgettoRimodulazione() {
		return progettoRimodulazione;
	}
	
	public void setProgettoRimodulazione(Progetto_rimodulazioneBulk progettoRimodulazione) {
		this.progettoRimodulazione = progettoRimodulazione;
	}
	
	public V_saldi_piano_econom_progettoBulk getSaldoEntrata() {
		V_saldi_piano_econom_progettoBulk saldoEntrata = new V_saldi_piano_econom_progettoBulk();
		Supplier<Stream<V_saldi_voce_progettoBulk>> saldi = () -> this.getVociBilancioAssociate().stream()
													  .filter(el->!el.isDetailRimodulatoEliminato())
													  .filter(el->Optional.ofNullable(el.getSaldoEntrata()).isPresent())
													  .flatMap(el->Stream.of(el.getSaldoEntrata()));
		saldoEntrata.setStanziamentoFin(saldi.get().map(el->Optional.ofNullable(el.getStanziamentoFin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoEntrata.setVariapiuFin(saldi.get().map(el->Optional.ofNullable(el.getVariapiuFin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoEntrata.setVariamenoFin(saldi.get().map(el->Optional.ofNullable(el.getVariamenoFin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoEntrata.setTrasfpiuFin(saldi.get().map(el->Optional.ofNullable(el.getTrasfpiuFin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoEntrata.setTrasfmenoFin(saldi.get().map(el->Optional.ofNullable(el.getTrasfmenoFin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));

		saldoEntrata.setStanziamentoCofin(saldi.get().map(el->Optional.ofNullable(el.getStanziamentoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoEntrata.setVariapiuCofin(saldi.get().map(el->Optional.ofNullable(el.getVariapiuCofin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoEntrata.setVariamenoCofin(saldi.get().map(el->Optional.ofNullable(el.getVariamenoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoEntrata.setTrasfpiuCofin(saldi.get().map(el->Optional.ofNullable(el.getTrasfpiuCofin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoEntrata.setTrasfmenoCofin(saldi.get().map(el->Optional.ofNullable(el.getTrasfmenoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		
		saldoEntrata.setImpaccFin(saldi.get().map(el->Optional.ofNullable(el.getImpaccFin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoEntrata.setImpaccCofin(saldi.get().map(el->Optional.ofNullable(el.getImpaccCofin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoEntrata.setManrisFin(saldi.get().map(el->Optional.ofNullable(el.getManrisFin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoEntrata.setManrisCofin(saldi.get().map(el->Optional.ofNullable(el.getManrisCofin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		return saldoEntrata;
	}
	
	public V_saldi_piano_econom_progettoBulk getSaldoSpesa() {
		V_saldi_piano_econom_progettoBulk saldoSpesa = new V_saldi_piano_econom_progettoBulk();
		Supplier<Stream<V_saldi_voce_progettoBulk>> saldi = () -> this.getVociBilancioAssociate().stream()
													  .filter(el->!el.isDetailRimodulatoEliminato())
				  									  .filter(el->Optional.ofNullable(el.getSaldoSpesa()).isPresent())
													  .flatMap(el->Stream.of(el.getSaldoSpesa()));
		saldoSpesa.setStanziamentoFin(saldi.get().map(el->Optional.ofNullable(el.getStanziamentoFin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoSpesa.setVariapiuFin(saldi.get().map(el->Optional.ofNullable(el.getVariapiuFin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoSpesa.setVariamenoFin(saldi.get().map(el->Optional.ofNullable(el.getVariamenoFin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoSpesa.setTrasfpiuFin(saldi.get().map(el->Optional.ofNullable(el.getTrasfpiuFin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoSpesa.setTrasfmenoFin(saldi.get().map(el->Optional.ofNullable(el.getTrasfmenoFin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));

		saldoSpesa.setStanziamentoCofin(saldi.get().map(el->Optional.ofNullable(el.getStanziamentoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoSpesa.setVariapiuCofin(saldi.get().map(el->Optional.ofNullable(el.getVariapiuCofin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoSpesa.setVariamenoCofin(saldi.get().map(el->Optional.ofNullable(el.getVariamenoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoSpesa.setTrasfpiuCofin(saldi.get().map(el->Optional.ofNullable(el.getTrasfpiuCofin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoSpesa.setTrasfmenoCofin(saldi.get().map(el->Optional.ofNullable(el.getTrasfmenoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		
		saldoSpesa.setImpaccFin(saldi.get().map(el->Optional.ofNullable(el.getImpaccFin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoSpesa.setImpaccCofin(saldi.get().map(el->Optional.ofNullable(el.getImpaccCofin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoSpesa.setManrisFin(saldi.get().map(el->Optional.ofNullable(el.getManrisFin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoSpesa.setManrisCofin(saldi.get().map(el->Optional.ofNullable(el.getManrisCofin()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		return saldoSpesa;
	}
	
	public java.math.BigDecimal getImTotaleSpesa() {
		return Optional.ofNullable(getIm_spesa_finanziato()).orElse(BigDecimal.ZERO)
				.add(Optional.ofNullable(getIm_spesa_cofinanziato()).orElse(BigDecimal.ZERO));
	}
	
	public java.math.BigDecimal getImTotaleSpesaRimodulato() {
		return Optional.ofNullable(getImSpesaFinanziatoRimodulato()).orElse(BigDecimal.ZERO)
				.add(Optional.ofNullable(getImSpesaCofinanziatoRimodulato()).orElse(BigDecimal.ZERO));
	}
	
	public BulkList<Ass_progetto_piaeco_voceBulk> getVociBilancioAssociate() {
		return vociBilancioAssociate;
	}
	
	public void setVociBilancioAssociate(BulkList<Ass_progetto_piaeco_voceBulk> vociBilancioAssociate) {
		this.vociBilancioAssociate = vociBilancioAssociate;
	}
	
	public int addToVociBilancioAssociate(Ass_progetto_piaeco_voceBulk dett) {
		dett.setProgetto_piano_economico( this );
		vociBilancioAssociate.add(dett);
		return vociBilancioAssociate.size()-1;
	}

	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {vociBilancioAssociate};
	}

	public Ass_progetto_piaeco_voceBulk removeFromVociBilancioAssociate(int index) {
		Ass_progetto_piaeco_voceBulk dett = (Ass_progetto_piaeco_voceBulk)vociBilancioAssociate.remove(index);
		return dett;
	}

	public boolean isROVocePiano() {
		return !Optional.ofNullable(this.getEsercizio_piano()).isPresent() ||
				Optional.ofNullable(this.getVociBilancioAssociate())
				.map(el->!el.isEmpty())
				.orElse(Boolean.TRUE);
	}

	/**
	 * Ritorna la lista degli altri anni valorizzabili sul piano economico del progetto
	 * La lista degli anni restituita è l'insieme di tutti gli anni compresi tra:
	 * 1) 14 anni precedenti all'anno di inizio del progetto
	 * 2) Anno fine/proroga del progetto
	 *
	 * Dalla lista viene eliminato l'anno corrente che non fa parte dell'insieme "altri anni".
	 *
	 * @return la lista degli altri anni valorizzabili sul piano economico del progetto
	 */
	public it.cnr.jada.util.OrderedHashtable getAnniList() {
		OrderedHashtable list = new OrderedHashtable();
		Optional<ProgettoBulk> optProgetto = Optional.ofNullable(this.getProgetto());
		Integer annoInizio = Optional.ofNullable(this.getProgettoRimodulazione()).map(el->el.getAnnoInizioForPianoEconomico())
				.orElse(optProgetto.map(ProgettoBulk::getAnnoInizioForPianoEconomico).orElse(0));
		Integer annoFine = Optional.ofNullable(this.getProgettoRimodulazione()).map(el->el.getAnnoFineRimodulato())
				.orElse(optProgetto.map(ProgettoBulk::getAnnoFineOf).orElse(9999));
		for (int i=annoFine;i>=annoInizio;i--)
			list.put(new Integer(i), new Integer(i));
		if (this.getEsercizio_piano()!=null && list.get(this.getEsercizio_piano())==null)
			list.put(this.getEsercizio_piano(), this.getEsercizio_piano());
		list.remove(this.getProgetto().getEsercizio());
		Optional.ofNullable(this.getProgetto())
				.flatMap(el->Optional.ofNullable(el.getPdgModuli()))
				.map(el->el.stream())
				.orElse(Stream.empty())
				.filter(el->Optional.ofNullable(el.getStato()).map(stato->!stato.equals(Pdg_moduloBulk.STATO_AC)).orElse(Boolean.FALSE))
				.forEach(el->list.remove(el));
		return list;
	}

	public boolean isROEsercizio_piano() {
		return Optional.ofNullable(this.getVoce_piano_economico()).isPresent();
	}
			
	public java.math.BigDecimal getDispResiduaFinanziamento() {
		return Optional.ofNullable(this.getIm_spesa_finanziato()).orElse(BigDecimal.ZERO)
				.subtract(Optional.ofNullable(this.getSaldoSpesa())
								  .flatMap(el->Optional.ofNullable(el.getAssestatoFinanziamento()))
								  .orElse(BigDecimal.ZERO));
	}

	public java.math.BigDecimal getDispResiduaCofinanziamento() {
		return Optional.ofNullable(this.getIm_spesa_cofinanziato()).orElse(BigDecimal.ZERO)
				.subtract(Optional.ofNullable(this.getSaldoSpesa())
								  .flatMap(el->Optional.ofNullable(el.getAssestatoCofinanziamento()))
						          .orElse(BigDecimal.ZERO));
	}

	public java.math.BigDecimal getDispResidua() {
		return this.getDispResiduaFinanziamento().add(this.getDispResiduaCofinanziamento());
	}
	
	public boolean isROProgettoPianoEconomico() {
		return Optional.ofNullable(this.getProgetto())
					   .map(ProgettoBulk::isROProgettoForStato)
					   .orElse(Boolean.FALSE) ||
				Optional.ofNullable(this.getProgetto())
					   .flatMap(el->Optional.ofNullable(el.getPdgModuli()))
					   .map(el->el.stream())
					   .orElse(Stream.empty())
					   .filter(el->el.getEsercizio().equals(this.getEsercizio_piano()))
					   .filter(el->!el.getStato().equals(Pdg_moduloBulk.STATO_AC))
					   .findAny().isPresent();
	}

	public boolean isROImSpesaFinanziato() {
		return this.isROProgettoPianoEconomico() ||
				this.isAnnoPianoEconomicoMinoreAnnoInizio();
	}

	public boolean isAnnoPianoEconomicoMinoreAnnoInizio() {
		Optional<ProgettoBulk> optProgetto = Optional.ofNullable(this.getProgetto());
		Integer annoInizio = Optional.ofNullable(this.getProgettoRimodulazione()).map(el->el.getAnnoInizioRimodulato())
				.orElse(optProgetto.map(ProgettoBulk::getAnnoInizioOf).orElse(0));
		return Optional.ofNullable(this.getEsercizio_piano())
						.map(el->el.compareTo(annoInizio)<0)
						.orElse(Boolean.TRUE);
	}

	public java.math.BigDecimal getImSpesaFinanziatoRimodulato() {
		return imSpesaFinanziatoRimodulato;
	}

	public void setImSpesaFinanziatoRimodulato(java.math.BigDecimal imSpesaFinanziatoRimodulato) {
		this.imSpesaFinanziatoRimodulato = imSpesaFinanziatoRimodulato;
	}

	public java.math.BigDecimal getImSpesaCofinanziatoRimodulato() {
		return imSpesaCofinanziatoRimodulato;
	}

	public void setImSpesaCofinanziatoRimodulato(java.math.BigDecimal imSpesaCofinanziatoRimodulato) {
		this.imSpesaCofinanziatoRimodulato = imSpesaCofinanziatoRimodulato;
	}
	
	public java.math.BigDecimal getDispResiduaFinanziamentoRimodulato() {
		return Optional.ofNullable(this.getImSpesaFinanziatoRimodulato()).orElse(BigDecimal.ZERO)
				.subtract(this.getImAssestatoSpesaFinanziatoRimodulato());
	}

	public java.math.BigDecimal getDispResiduaCofinanziamentoRimodulato() {
		return Optional.ofNullable(this.getImSpesaCofinanziatoRimodulato()).orElse(BigDecimal.ZERO)
				.subtract(this.getImAssestatoSpesaCofinanziatoRimodulato());
	}

	public java.math.BigDecimal getDispResiduaRimodulato() {
		return this.getDispResiduaFinanziamentoRimodulato().add(this.getDispResiduaCofinanziamentoRimodulato());
	}

	/**
	 * Ritorna il totale assestato spesa di tipo finanziamento delle voci associate inclusa la rimodulazione proposta dall'utente
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getImAssestatoSpesaFinanziatoRimodulato() {
		return this.getSaldoSpesa().getAssestatoFinanziamento()
				.add(this.getVociBilancioAssociate().stream()
				  		 .filter(el->!el.isDetailRimodulatoEliminato())
					     .filter(el->Optional.ofNullable(el.getImVarFinanziatoRimodulato()).isPresent())
					     .map(Ass_progetto_piaeco_voceBulk::getImVarFinanziatoRimodulato)
					     .reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO));
	}
	
	/**
	 * Ritorna il totale assestato spesa di tipo cofinanziamento delle voci associate inclusa la rimodulazione proposta dall'utente
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getImAssestatoSpesaCofinanziatoRimodulato() {
		return this.getSaldoSpesa().getAssestatoCofinanziamento()
				.add(this.getVociBilancioAssociate().stream()
				  		 .filter(el->!el.isDetailRimodulatoEliminato())
					     .filter(el->Optional.ofNullable(el.getImVarCofinanziatoRimodulato()).isPresent())
					     .map(Ass_progetto_piaeco_voceBulk::getImVarCofinanziatoRimodulato)
					     .reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO));
	}

	public boolean isDetailRimodulato(){
		return Optional.ofNullable(this.getIm_spesa_finanziato()).orElse(BigDecimal.ZERO)
				.compareTo(Optional.ofNullable(this.getImSpesaFinanziatoRimodulato()).orElse(BigDecimal.ZERO))!=0 ||
				Optional.ofNullable(this.getIm_spesa_cofinanziato()).orElse(BigDecimal.ZERO)
				.compareTo(Optional.ofNullable(this.getImSpesaCofinanziatoRimodulato()).orElse(BigDecimal.ZERO))!=0;
				
	}
	
	/**
	 * Campo utilizzato solo in fase di rimodulazione.
	 * Indica che il dettaglio è stato eliminato, a seguito di rimodulazione.
	 * 
	 * @return boolean
	 */
	public boolean isDetailRimodulatoEliminato(){
		return this.isDetailRimodulato() && 
				Optional.ofNullable(this.getImSpesaFinanziatoRimodulato()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)==0 &&
				Optional.ofNullable(this.getImSpesaCofinanziatoRimodulato()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO)==0;
	}

	public java.math.BigDecimal getImSpesaFinanziatoRimodulatoPreDelete() {
		return imSpesaFinanziatoRimodulatoPreDelete;
	}

	public void setImSpesaFinanziatoRimodulatoPreDelete(java.math.BigDecimal imSpesaFinanziatoRimodulatoPreDelete) {
		this.imSpesaFinanziatoRimodulatoPreDelete = imSpesaFinanziatoRimodulatoPreDelete;
	}

	public java.math.BigDecimal getImSpesaCofinanziatoRimodulatoPreDelete() {
		return imSpesaCofinanziatoRimodulatoPreDelete;
	}

	public void setImSpesaCofinanziatoRimodulatoPreDelete(java.math.BigDecimal imSpesaCofinanziatoRimodulatoPreDelete) {
		this.imSpesaCofinanziatoRimodulatoPreDelete = imSpesaCofinanziatoRimodulatoPreDelete;
	}
	
	public void setDetailDerivato(boolean detailDerivato) {
		this.detailDerivato = detailDerivato;
	}
	
	/**
	 * Indica se il record nella rimodulazione è stato creato a seguito di copia dal piano economico
	 * Serve per gestire la rimodulazione
	 * @return boolean
	 */
	public boolean isDetailDerivato() {
		return detailDerivato;
	}
	
	public String getMessageAnomaliaDetailRimodulato() {
		StringJoiner anomalia = new StringJoiner("\r\r");
		if (this.isEsercizioPianoAttivo()) {
			Optional.ofNullable(this.getDispResiduaFinanziamentoRimodulato())
				.filter(el->el.compareTo(BigDecimal.ZERO)<0)
				.ifPresent(el->anomalia.add("QUOTA FINANZIATA:" +
					"\rAssegnata (" + new it.cnr.contab.util.EuroFormat().format(this.getImSpesaFinanziatoRimodulato())+")"+
					" - Assestato (" + new it.cnr.contab.util.EuroFormat().format(this.getImAssestatoSpesaFinanziatoRimodulato())+")"+
					" = " + new it.cnr.contab.util.EuroFormat().format(el)+" - Valore negativo non consentito."));
	
			Optional.ofNullable(this.getDispResiduaCofinanziamentoRimodulato())
				.filter(el->el.compareTo(BigDecimal.ZERO)<0)
				.ifPresent(el->anomalia.add("QUOTA COFINANZIATA:" +
					"\rAssegnata (" + new it.cnr.contab.util.EuroFormat().format(this.getImSpesaCofinanziatoRimodulato())+")"+
					" - Assestato (" + new it.cnr.contab.util.EuroFormat().format(this.getImAssestatoSpesaCofinanziatoRimodulato())+")"+
					" = " + new it.cnr.contab.util.EuroFormat().format(el)+" - Valore negativo non consentito."));

		}
		return Optional.ofNullable(anomalia).filter(el->el.length()>0).map(StringJoiner::toString).orElse(null);
	}
	
	public boolean isROFieldRimodulazione() {
		return isDetailRimodulatoEliminato() ||
				Optional.ofNullable(this.getProgettoRimodulazione()).map(Progetto_rimodulazioneBulk::isROFieldRimodulazione).orElse(Boolean.TRUE);
	}

	public boolean isROImSpesaFinanziatoRimodulatoAA() {
		return isROFieldRimodulazione() ||
				this.isAnnoPianoEconomicoMinoreAnnoInizio();
	}

	/**
	 * Indica se l'anno del piano economico è all'interno del periodo di validità comreso
	 * tra l'anno di attivazione del piano economico e l'ultimo anno contabile attivo
	 * @return
	 */
	public boolean isEsercizioPianoAttivo() {
		return Optional.ofNullable(this.getEsercizio_piano()).map(esePpe->{
			return Optional.ofNullable(this.getProgettoRimodulazione())
					.flatMap(el->Optional.ofNullable(el.getAnnoFromPianoEconomico()))
					.map(eseFrom->eseFrom.compareTo(esePpe)<=0)
					.orElse(Boolean.TRUE) &&
					Optional.ofNullable(this.getProgettoRimodulazione())
					.flatMap(el->Optional.ofNullable(el.getLastEsercizioAperto()))
					.map(lastEse->lastEse.compareTo(esePpe)>=0)
					.orElse(Boolean.TRUE);
		}).orElse(Boolean.TRUE);
	}
	
	@Override
	public Object clone() {
		Progetto_piano_economicoBulk ppeClone = (Progetto_piano_economicoBulk)super.clone();
		ppeClone.setVociBilancioAssociate(new BulkList<>(ppeClone.getVociBilancioAssociate().stream().map(Ass_progetto_piaeco_voceBulk::clone).collect(Collectors.toList())));
		return ppeClone;
	}
}