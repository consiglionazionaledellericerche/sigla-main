package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Incarichi_repertorio_rappBase extends Incarichi_repertorio_rappKey implements Keyed {
 
	//  ANNO_COMPETENZA NUMBER(4) NOT NULL
	private java.lang.Integer anno_competenza;

	//  DT_DICHIARAZIONE TIMESTAMP(7)  NOT NULL
	private java.sql.Timestamp dt_dichiarazione;

	//  FL_ALTRI_RAPPORTI CHAR(1) NOT NULL
	private java.lang.Boolean fl_altri_rapporti;

	public Incarichi_repertorio_rappBase() {
		super();
	}
	
	public Incarichi_repertorio_rappBase(java.lang.Integer esercizio, java.lang.Long pg_repertorio, java.lang.Long progressivo_riga) {
		super(esercizio, pg_repertorio, progressivo_riga);
	}
	
	public java.lang.Integer getAnno_competenza() {
		return anno_competenza;
	}
	public void setAnno_competenza(java.lang.Integer anno_competenza) {
		this.anno_competenza = anno_competenza;
	}

	public java.sql.Timestamp getDt_dichiarazione() {
		return dt_dichiarazione;
	}
	public void setDt_dichiarazione(java.sql.Timestamp dt_dichiarazione) {
		this.dt_dichiarazione = dt_dichiarazione;
	}
	public java.lang.Boolean getFl_altri_rapporti() {
		return fl_altri_rapporti;
	}
	public void setFl_altri_rapporti(java.lang.Boolean fl_altri_rapporti) {
		this.fl_altri_rapporti = fl_altri_rapporti;
	}
}