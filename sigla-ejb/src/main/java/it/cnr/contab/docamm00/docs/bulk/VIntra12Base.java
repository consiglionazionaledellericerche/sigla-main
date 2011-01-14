/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/05/2010
 */
package it.cnr.contab.docamm00.docs.bulk;
import java.math.BigDecimal;

import it.cnr.jada.persistency.Keyed;
public class VIntra12Base extends VIntra12Key implements Keyed {
//    DS_TIPO_SEZIONALE VARCHAR(50) NOT NULL
	private java.lang.String dsTipoSezionale;
 
//    IMPONIBILE DECIMAL(22,0)
	private BigDecimal imponibile;
 
//    IVA DECIMAL(22,0)
	private BigDecimal iva;
 
	public VIntra12Base() {
		super();
	}
	public VIntra12Base(java.lang.String cdTipoSezionale,java.lang.String cdBeneServizio,Integer esercizio,Integer mese,Boolean flExtra,Boolean flIntra) {
		super(cdTipoSezionale,cdBeneServizio,esercizio,mese,flExtra,flIntra);
	}
	public java.lang.String getDsTipoSezionale() {
		return dsTipoSezionale;
	}
	public void setDsTipoSezionale(java.lang.String dsTipoSezionale) {
		this.dsTipoSezionale = dsTipoSezionale;
	}
	public BigDecimal getImponibile() {
		return imponibile;
	}
	public void setImponibile(BigDecimal imponibile) {
		this.imponibile = imponibile;
	}
	public BigDecimal getIva() {
		return iva;
	}
	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}
}