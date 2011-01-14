/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 10/09/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Incarichi_repertorio_annoBase extends Incarichi_repertorio_annoKey implements Keyed {
//    IMPORTO_INIZIALE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_iniziale;
 
//    IMPORTO_COMPLESSIVO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_complessivo;

	public Incarichi_repertorio_annoBase() {
		super();
	}
	public Incarichi_repertorio_annoBase(java.lang.Integer esercizio, java.lang.Long pg_repertorio, java.lang.Integer esercizio_limite) {
		super(esercizio, pg_repertorio, esercizio_limite);
	}
	public java.math.BigDecimal getImporto_iniziale() {
		return importo_iniziale;
	}
	public void setImporto_iniziale(java.math.BigDecimal importo_iniziale)  {
		this.importo_iniziale=importo_iniziale;
	}
	public java.math.BigDecimal getImporto_complessivo() {
		return importo_complessivo;
	}
	public void setImporto_complessivo(java.math.BigDecimal importo_complessivo)  {
		this.importo_complessivo=importo_complessivo;
	}
}