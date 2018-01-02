package it.cnr.contab.ordmag.ordini.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import it.cnr.contab.util.Utility;

public class ImportoOrdine implements Serializable {
    BigDecimal imponibile;
	BigDecimal importoIva;
	BigDecimal importoIvaDetraibile;
	BigDecimal importoIvaInd;
	BigDecimal arrAliIva;
	public BigDecimal getImponibile() {
		return imponibile;
	}
	public void setImponibile(BigDecimal imponibile) {
		this.imponibile = imponibile;
	}
	public BigDecimal getImportoIva() {
		return importoIva;
	}
	public void setImportoIva(BigDecimal importoIva) {
		this.importoIva = importoIva;
	}
	public BigDecimal getImportoIvaInd() {
		return importoIvaInd;
	}
	public void setImportoIvaInd(BigDecimal importoIvaInd) {
		this.importoIvaInd = importoIvaInd;
	}
	public BigDecimal getTotale() {
		return Utility.nvl(getImponibile()).add(Utility.nvl(getImportoIva())).add(Utility.nvl(getArrAliIva()));
	}
	public BigDecimal getArrAliIva() {
		return arrAliIva;
	}
	public void setArrAliIva(BigDecimal arrAliIva) {
		this.arrAliIva = arrAliIva;
	}
	public BigDecimal getImportoIvaDetraibile() {
		return importoIvaDetraibile;
	}
	public void setImportoIvaDetraibile(BigDecimal importoIvaDetraibile) {
		this.importoIvaDetraibile = importoIvaDetraibile;
	}
}
