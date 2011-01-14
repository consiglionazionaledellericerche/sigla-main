/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.persistency.Keyed;
public class Repertorio_limitiBase extends Repertorio_limitiKey implements Keyed {
//    IMPORTO_LIMITE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_limite;
 
//    IMPORTO_UTILIZZATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_utilizzato;
 
//    FL_RAGGIUNTO_LIMITE CHAR(1) NOT NULL
	private java.lang.Boolean fl_raggiunto_limite;
 
	public Repertorio_limitiBase() {
		super();
	}
	public Repertorio_limitiBase(java.lang.Integer esercizio, java.lang.String cd_tipo_limite) {
		super(esercizio, cd_tipo_limite);
	}
	public java.math.BigDecimal getImporto_limite() {
		return importo_limite;
	}
	public void setImporto_limite(java.math.BigDecimal importo_limite)  {
		this.importo_limite=importo_limite;
	}
	public java.math.BigDecimal getImporto_utilizzato() {
		return importo_utilizzato;
	}
	public void setImporto_utilizzato(java.math.BigDecimal importo_utilizzato)  {
		this.importo_utilizzato=importo_utilizzato;
	}
	public java.math.BigDecimal getImporto_residuo() {
		return Utility.nvl(getImporto_limite()).subtract(Utility.nvl(getImporto_utilizzato()));
	}
	public java.lang.Boolean getFl_raggiunto_limite() {
		return fl_raggiunto_limite;
	}
	public void setFl_raggiunto_limite(java.lang.Boolean fl_raggiunto_limite)  {
		this.fl_raggiunto_limite=fl_raggiunto_limite;
	}
}