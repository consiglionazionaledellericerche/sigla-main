/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_incarico_attivitaBase extends Ass_incarico_attivitaKey implements Keyed {
//    CD_TIPO_LIMITE DECIMAL(5,0) NOT NULL
	private java.lang.String cd_tipo_limite;
 
//    STATO CHAR(1) NOT NULL
	private java.lang.String stato;
 
	public Ass_incarico_attivitaBase() {
		super();
	}
	public Ass_incarico_attivitaBase(java.lang.Integer esercizio, java.lang.String cd_tipo_incarico, java.lang.String cd_tipo_attivita, java.lang.String tipo_natura) {
		super(esercizio, cd_tipo_incarico, cd_tipo_attivita, tipo_natura);
	}
	public java.lang.String getCd_tipo_limite() {
		return cd_tipo_limite;
	}
	public void setCd_tipo_limite(java.lang.String cd_tipo_limite)  {
		this.cd_tipo_limite=cd_tipo_limite;
	}
	public java.lang.String getStato() {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
}