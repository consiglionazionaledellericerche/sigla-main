/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 09/09/2010
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class MandatoCupBase extends MandatoCupKey implements Keyed {
//    IMPORTO DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal importo;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MANDATO_CUP
	 **/
	public MandatoCupBase() {
		super();
	}
	public MandatoCupBase(java.lang.String cdCds, java.lang.Integer esercizio, java.lang.Long pgMandato, java.lang.Integer esercizioObbligazione, java.lang.Integer esercizioOriObbligazione, java.lang.Long pgObbligazione, java.lang.Long pgObbligazioneScadenzario, java.lang.String cdCdsDocAmm, java.lang.String cdUoDocAmm, java.lang.Integer esercizioDocAmm, java.lang.String cdTipoDocumentoAmm, java.lang.Long pgDocAmm, java.lang.String cdCup) {
		super(cdCds, esercizio, pgMandato, esercizioObbligazione, esercizioOriObbligazione, pgObbligazione, pgObbligazioneScadenzario, cdCdsDocAmm, cdUoDocAmm, esercizioDocAmm, cdTipoDocumentoAmm, pgDocAmm, cdCup);
	}
	public java.math.BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(java.math.BigDecimal importo) {
		this.importo = importo;
	}
	
}