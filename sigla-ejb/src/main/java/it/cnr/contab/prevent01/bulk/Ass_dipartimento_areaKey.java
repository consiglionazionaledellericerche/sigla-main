/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 20/01/2007
 */
package it.cnr.contab.prevent01.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Ass_dipartimento_areaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_dipartimento;
	private java.lang.String cd_cds_area;
	public Ass_dipartimento_areaKey() {
		super();
	}
	public Ass_dipartimento_areaKey(java.lang.Integer esercizio, java.lang.String cd_dipartimento, java.lang.String cd_cds_area) {
		super();
		this.esercizio=esercizio;
		this.cd_dipartimento=cd_dipartimento;
		this.cd_cds_area=cd_cds_area;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ass_dipartimento_areaKey)) return false;
		Ass_dipartimento_areaKey k = (Ass_dipartimento_areaKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_dipartimento(), k.getCd_dipartimento())) return false;
		if (!compareKey(getCd_cds_area(), k.getCd_cds_area())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_dipartimento());
		i = i + calculateKeyHashCode(getCd_cds_area());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setCd_dipartimento(java.lang.String cd_dipartimento)  {
		this.cd_dipartimento=cd_dipartimento;
	}
	public java.lang.String getCd_dipartimento() {
		return cd_dipartimento;
	}
	public void setCd_cds_area(java.lang.String cd_cds_area)  {
		this.cd_cds_area=cd_cds_area;
	}
	public java.lang.String getCd_cds_area() {
		return cd_cds_area;
	}
}