/*
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.progettiric00.core.bulk;

import java.math.BigDecimal;
import java.util.Optional;

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
	
	public java.math.BigDecimal getAssestatoFinanziamento() {
		return Optional.ofNullable(getStanziamentoFin()).orElse(BigDecimal.ZERO)
				.add(Optional.ofNullable(getVariapiuFin()).orElse(BigDecimal.ZERO))
				.subtract(Optional.ofNullable(getVariamenoFin()).orElse(BigDecimal.ZERO))
				.add(Optional.ofNullable(getTrasferitoFinanziamento()).orElse(BigDecimal.ZERO));
	}

	public java.math.BigDecimal getTrasferitoFinanziamento() {
		return Optional.ofNullable(this.getSaldoTrasferimentoFinanziamento())
				 	   .filter(el->el.compareTo(BigDecimal.ZERO)<0)
				 	   .map(BigDecimal::abs)
				 	   .orElse(BigDecimal.ZERO);
	}
	
	public java.math.BigDecimal getAssestatoCofinanziamento() {
		return Optional.ofNullable(getStanziamentoCofin()).orElse(BigDecimal.ZERO)
				.add(Optional.ofNullable(getVariapiuCofin()).orElse(BigDecimal.ZERO))
				.subtract(Optional.ofNullable(getVariamenoCofin()).orElse(BigDecimal.ZERO))
				.add(Optional.ofNullable(getTrasferitoCofinanziamento()).orElse(BigDecimal.ZERO));
	}

	public java.math.BigDecimal getTrasferitoCofinanziamento() {
		return Optional.ofNullable(this.getSaldoTrasferimentoCofinanziamento())
				 	   .filter(el->el.compareTo(BigDecimal.ZERO)<0)
				 	   .map(BigDecimal::abs)
				 	   .orElse(BigDecimal.ZERO);
	}
	
	public java.math.BigDecimal getAssestato() {
		return Optional.ofNullable(getAssestatoFinanziamento()).orElse(BigDecimal.ZERO)
				.add(Optional.ofNullable(getAssestatoCofinanziamento()).orElse(BigDecimal.ZERO));
	}

	public java.math.BigDecimal getTrasferito() {
		return Optional.ofNullable(getTrasferitoFinanziamento()).orElse(BigDecimal.ZERO)
				.add(Optional.ofNullable(getTrasferitoCofinanziamento()).orElse(BigDecimal.ZERO));
	}
	
	public java.math.BigDecimal getSaldoTrasferimentoFinanziamento() {
		return Optional.ofNullable(getTrasfpiuFin()).orElse(BigDecimal.ZERO)
				.subtract(Optional.ofNullable(getTrasfmenoFin()).orElse(BigDecimal.ZERO));
	}

	public java.math.BigDecimal getSaldoTrasferimentoCofinanziamento() {
		return Optional.ofNullable(getTrasfpiuCofin()).orElse(BigDecimal.ZERO)
				.subtract(Optional.ofNullable(getTrasfmenoCofin()).orElse(BigDecimal.ZERO));
	}
}