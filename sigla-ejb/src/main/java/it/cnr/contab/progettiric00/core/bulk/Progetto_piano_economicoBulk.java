package it.cnr.contab.progettiric00.core.bulk;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.util.OrderedHashtable;

public class Progetto_piano_economicoBulk extends Progetto_piano_economicoBase {
	private Voce_piano_economico_prgBulk voce_piano_economico;
	private ProgettoBulk progetto;
	private BulkList<Ass_progetto_piaeco_voceBulk> vociBilancioAssociate = new BulkList();

	private java.math.BigDecimal imSpesaFinanziatoRimodulato;
	private java.math.BigDecimal imSpesaCofinanziatoRimodulato;
	
	private java.math.BigDecimal imSpesaFinanziatoRimodulatoPreDelete;
	private java.math.BigDecimal imSpesaCofinanziatoRimodulatoPreDelete;

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

	public V_saldi_piano_econom_progettoBulk getSaldoEntrata() {
		V_saldi_piano_econom_progettoBulk saldoEntrata = new V_saldi_piano_econom_progettoBulk();
		Supplier<Stream<V_saldi_voce_progettoBulk>> saldi = () -> this.getVociBilancioAssociate().stream()
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
		
		saldoEntrata.setImpacc(saldi.get().map(el->Optional.ofNullable(el.getImpacc()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoEntrata.setManris(saldi.get().map(el->Optional.ofNullable(el.getManris()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		return saldoEntrata;
	}
	
	public V_saldi_piano_econom_progettoBulk getSaldoSpesa() {
		V_saldi_piano_econom_progettoBulk saldoSpesa = new V_saldi_piano_econom_progettoBulk();
		Supplier<Stream<V_saldi_voce_progettoBulk>> saldi = () -> this.getVociBilancioAssociate().stream()
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
		
		saldoSpesa.setImpacc(saldi.get().map(el->Optional.ofNullable(el.getImpacc()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		saldoSpesa.setManris(saldi.get().map(el->Optional.ofNullable(el.getManris()).orElse(BigDecimal.ZERO)).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));

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
				this.isROProgettoPianoEconomico() ||
				Optional.ofNullable(this.getVociBilancioAssociate())
				.map(el->!el.isEmpty())
				.orElse(Boolean.TRUE);
	}
	
	public it.cnr.jada.util.OrderedHashtable getAnniList() {
		OrderedHashtable list = new OrderedHashtable();
		for (int i=this.getProgetto().getAnnoFineOf().intValue();i>=this.getProgetto().getAnnoInizioOf();i--)
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
					   .flatMap(el->Optional.ofNullable(el.getPdgModuli()))
					   .map(el->el.stream())
					   .orElse(Stream.empty())
					   .filter(el->el.getEsercizio().equals(this.getEsercizio_piano()))
					   .filter(el->!el.getStato().equals(Pdg_moduloBulk.STATO_AC))
					   .findAny().isPresent();
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
				.subtract(Optional.ofNullable(this.getSaldoSpesa())
								  .flatMap(el->Optional.ofNullable(el.getAssestatoFinanziamento()))
								  .orElse(BigDecimal.ZERO));
	}

	public java.math.BigDecimal getDispResiduaCofinanziamentoRimodulato() {
		return Optional.ofNullable(this.getImSpesaCofinanziatoRimodulato()).orElse(BigDecimal.ZERO)
				.subtract(Optional.ofNullable(this.getSaldoSpesa())
								  .flatMap(el->Optional.ofNullable(el.getAssestatoCofinanziamento()))
						          .orElse(BigDecimal.ZERO));
	}

	public java.math.BigDecimal getDispResiduaRimodulato() {
		return this.getDispResiduaFinanziamentoRimodulato().add(this.getDispResiduaCofinanziamentoRimodulato());
	}
	
	public boolean isDetailRimodulato(){
		return this.getIm_spesa_finanziato().compareTo(this.getImSpesaFinanziatoRimodulato())!=0 ||
				this.getIm_spesa_cofinanziato().compareTo(this.getImSpesaCofinanziatoRimodulato())!=0;
				
	}

	public boolean isDetailRimodulatoEliminato(){
		return this.isDetailRimodulato() && 
			   this.getImSpesaFinanziatoRimodulato().compareTo(BigDecimal.ZERO)==0 &&
  		       this.getImSpesaCofinanziatoRimodulato().compareTo(BigDecimal.ZERO)==0;
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
}