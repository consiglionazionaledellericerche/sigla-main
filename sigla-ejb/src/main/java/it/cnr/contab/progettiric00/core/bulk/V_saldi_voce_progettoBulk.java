/*
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.progettiric00.core.bulk;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class V_saldi_voce_progettoBulk extends V_saldi_voce_progettoBase {
	private static final long serialVersionUID = 5918224310476589096L;

	public V_saldi_voce_progettoBulk() {
		super();
	}
	
	/*
	 * Ritorna l'assestato della quota finanziata calcolata da:
	 * Stanziamento + Variazioni Positive - Variazioni Negative + Quote Ricevute
	 */
	public java.math.BigDecimal getAssestatoFinanziamento() {
		return Optional.ofNullable(getStanziamentoFin()).orElse(BigDecimal.ZERO)
				.add(Optional.ofNullable(getVariapiuFin()).orElse(BigDecimal.ZERO))
				.subtract(Optional.ofNullable(getVariamenoFin()).orElse(BigDecimal.ZERO))
				.add(Optional.ofNullable(getTrasferitoFinanziamento()).orElse(BigDecimal.ZERO));
	}

	/*
	 * Ritorna la quota trasferita della quota finanziata calcolata come valore netto di:
	 * Quote Trasferite - Quote Ricevute
	 */
	public java.math.BigDecimal getTrasferitoFinanziamento() {
		return Optional.ofNullable(this.getSaldoTrasferimentoFinanziamento())
				 	   .filter(el->el.compareTo(BigDecimal.ZERO)<0)
				 	   .map(BigDecimal::abs)
				 	   .orElse(BigDecimal.ZERO);
	}

	/*
	 * Ritorna la quota utilizzata dell'assestato della quota finanziata calcolata da:
	 * Assestato - Quota Impegnata - Quota Trasferita
	 */
	public java.math.BigDecimal getUtilizzatoAssestatoFinanziamento() {
		return this.getImpaccFin().add(this.getTrasferitoFinanziamento());
	}
	

	/*
	 * Ritorna la quota di assestato di tipo finanziamento non utilizzato disponibile per nuovi impegni/trasferimenti
	 */
	public java.math.BigDecimal getDispAssestatoFinanziamento() {
		return getAssestatoFinanziamento().subtract(this.getUtilizzatoAssestatoFinanziamento());
	}

	/*
	 * Ritorna l'assestato della quota cofinanziata calcolata da:
	 * Stanziamento + Variazioni Positive - Variazioni Negative + Quote Ricevute
	 */
	public java.math.BigDecimal getAssestatoCofinanziamento() {
		return Optional.ofNullable(getStanziamentoCofin()).orElse(BigDecimal.ZERO)
				.add(Optional.ofNullable(getVariapiuCofin()).orElse(BigDecimal.ZERO))
				.subtract(Optional.ofNullable(getVariamenoCofin()).orElse(BigDecimal.ZERO))
				.add(Optional.ofNullable(getTrasferitoCofinanziamento()).orElse(BigDecimal.ZERO));
	}

	/*
	 * Ritorna la quota trasferita della quota cofinanziata calcolata come valore netto di:
	 * Quote Trasferite - Quote Ricevute
	 */
	public java.math.BigDecimal getTrasferitoCofinanziamento() {
		return Optional.ofNullable(this.getSaldoTrasferimentoCofinanziamento())
				 	   .filter(el->el.compareTo(BigDecimal.ZERO)<0)
				 	   .map(BigDecimal::abs)
				 	   .orElse(BigDecimal.ZERO);
	}
	
	/*
	 * Ritorna la quota utilizzata dell'assestato della quota cofinanziata calcolata da:
	 * Assestato - Quota Impegnata - Quota Trasferita
	 */
	public java.math.BigDecimal getUtilizzatoAssestatoCofinanziamento() {
		return this.getImpaccCofin().add(this.getTrasferitoCofinanziamento());
	}

	/*
	 * Ritorna la quota di assestato di tipo cofinanziamento non utilizzato e 
	 * disponibile per nuovi impegni/trasferimenti
	 */
	public java.math.BigDecimal getDispAssestatoCofinanziamento() {
		return this.getAssestatoCofinanziamento().subtract(this.getUtilizzatoAssestatoCofinanziamento());
	}

	/*
	 * Ritorna l'assestato totale calcolata da:
	 * assestato della quota finanziata + assestato della quota cofinanziata
	 */
	public java.math.BigDecimal getAssestato() {
		return Optional.ofNullable(getAssestatoFinanziamento()).orElse(BigDecimal.ZERO)
				.add(Optional.ofNullable(getAssestatoCofinanziamento()).orElse(BigDecimal.ZERO));
	}

	/*
	 * Ritorna la quota totale trasferita calcolata come valore di:
	 * Quote Trasferite Finanziate - Quote Trasferite Cofinanziate
	 */
	public java.math.BigDecimal getTrasferito() {
		return Optional.ofNullable(getTrasferitoFinanziamento()).orElse(BigDecimal.ZERO)
				.add(Optional.ofNullable(getTrasferitoCofinanziamento()).orElse(BigDecimal.ZERO));
	}
	
	/*
	 * Ritorna il saldo delle quote finanziate trasferite calcolate come:
	 * Quota Trasferita - Quota Ricevuta
	 */
	public java.math.BigDecimal getSaldoTrasferimentoFinanziamento() {
		return Optional.ofNullable(getTrasfpiuFin()).orElse(BigDecimal.ZERO)
				.subtract(Optional.ofNullable(getTrasfmenoFin()).orElse(BigDecimal.ZERO));
	}

	/*
	 * Ritorna il saldo delle quote cofinanziate trasferite calcolate come:
	 * Quota Trasferita - Quota Ricevuta
	 */
	public java.math.BigDecimal getSaldoTrasferimentoCofinanziamento() {
		return Optional.ofNullable(getTrasfpiuCofin()).orElse(BigDecimal.ZERO)
				.subtract(Optional.ofNullable(getTrasfmenoCofin()).orElse(BigDecimal.ZERO));
	}
	
	/*
	 * Ritorna il totale impegni/accertamenti ottenuto come:
	 * Totale Impegni/Accertamenti Quota Finanziata + Totale Impegni/Accertamenti Quota Cofinanziata
	 */
	public BigDecimal getImpacc() {
		return this.getImpaccFin().add(this.getImpaccCofin());
	}

	/*
	 * Ritorna il totale mandati/reversali ottenuto come:
	 * Totale Mandati/Reversali Quota Finanziata + Totale Mandati/Reversali Quota Cofinanziata
	 */
	public BigDecimal getManris() {
		return this.getManrisFin().add(this.getManrisCofin());
	}

	/*
	 * Ritorna a fronte di tanti dettagli V_saldi_voce_progettoBulk presenti nella listSaldi
	 * un unico V_saldi_voce_progettoBulk che risulta la sommatoria degli stessi
	 */
	public static V_saldi_voce_progettoBulk getSaldoAccorpato(List<V_saldi_piano_econom_progettoBulk> listSaldi) {
		V_saldi_voce_progettoBulk saldoAccorpato = new V_saldi_voce_progettoBulk();
		saldoAccorpato.setStanziamentoFin(listSaldi.stream().map(el->Optional.ofNullable(el.getStanziamentoFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
		saldoAccorpato.setVariapiuFin(listSaldi.stream().map(el->Optional.ofNullable(el.getVariapiuFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
		saldoAccorpato.setVariamenoFin(listSaldi.stream().map(el->Optional.ofNullable(el.getVariamenoFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
		saldoAccorpato.setTrasfpiuFin(listSaldi.stream().map(el->Optional.ofNullable(el.getTrasfpiuFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
		saldoAccorpato.setTrasfmenoFin(listSaldi.stream().map(el->Optional.ofNullable(el.getTrasfmenoFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
		saldoAccorpato.setStanziamentoCofin(listSaldi.stream().map(el->Optional.ofNullable(el.getStanziamentoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
		saldoAccorpato.setVariapiuCofin(listSaldi.stream().map(el->Optional.ofNullable(el.getVariapiuCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
		saldoAccorpato.setVariamenoCofin(listSaldi.stream().map(el->Optional.ofNullable(el.getVariamenoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
		saldoAccorpato.setTrasfpiuCofin(listSaldi.stream().map(el->Optional.ofNullable(el.getTrasfpiuCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
		saldoAccorpato.setTrasfmenoCofin(listSaldi.stream().map(el->Optional.ofNullable(el.getTrasfmenoCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
		saldoAccorpato.setImpaccFin(listSaldi.stream().map(el->Optional.ofNullable(el.getImpaccFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
		saldoAccorpato.setManrisFin(listSaldi.stream().map(el->Optional.ofNullable(el.getManrisFin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
		saldoAccorpato.setImpaccCofin(listSaldi.stream().map(el->Optional.ofNullable(el.getImpaccCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
		saldoAccorpato.setManrisCofin(listSaldi.stream().map(el->Optional.ofNullable(el.getManrisCofin()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO));
		return saldoAccorpato;
	}
}