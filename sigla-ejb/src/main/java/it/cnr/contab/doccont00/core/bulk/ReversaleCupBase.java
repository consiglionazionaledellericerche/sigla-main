/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 09/09/2010
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class ReversaleCupBase extends ReversaleCupKey implements Keyed {
//    IMPORTO DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal importo;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: Reversale_CUP
	 **/
	public ReversaleCupBase() {
		super();
	}
	public ReversaleCupBase(java.lang.String cdCds, java.lang.Integer esercizio, java.lang.Long pgReversale, java.lang.Integer esercizioAccertamento, java.lang.Integer esercizioOriAccertamento, java.lang.Long pgAccertamento, java.lang.Long pgAccertamentoScadenzario, java.lang.String cdCdsDocAmm, java.lang.String cdUoDocAmm, java.lang.Integer esercizioDocAmm, java.lang.String cdTipoDocumentoAmm, java.lang.Long pgDocAmm, java.lang.String cdCup) {
		super(cdCds, esercizio, pgReversale, esercizioAccertamento, esercizioOriAccertamento, pgAccertamento, pgAccertamentoScadenzario, cdCdsDocAmm, cdUoDocAmm, esercizioDocAmm, cdTipoDocumentoAmm, pgDocAmm, cdCup);
	}
	public java.math.BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(java.math.BigDecimal importo) {
		this.importo = importo;
	}
	
}