/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/05/2007
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class AssVoceCatgrpInventBase extends AssVoceCatgrpInventKey implements Keyed {
	private Integer ordine;
	private java.sql.Timestamp dtCancellazione;
	private java.lang.Boolean flDefault;

	public AssVoceCatgrpInventBase() {
		super();
	}
	public AssVoceCatgrpInventBase(java.lang.Integer esercizio, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce, java.lang.String cd_categoria_gruppo) {
		super(esercizio, ti_appartenenza, ti_gestione, cd_elemento_voce, cd_categoria_gruppo);
	}
	public Integer getOrdine() {
		return ordine;
	}
	public void setOrdine(Integer ordine) {
		this.ordine = ordine;
	}
	public java.sql.Timestamp getDtCancellazione() {
		return dtCancellazione;
	}
	public void setDtCancellazione(java.sql.Timestamp dtCancellazione) {
		this.dtCancellazione = dtCancellazione;
	}
	public java.lang.Boolean getFlDefault() {
		return flDefault;
	}
	public void setFlDefault(java.lang.Boolean flDefault) {
		this.flDefault = flDefault;
	}
}