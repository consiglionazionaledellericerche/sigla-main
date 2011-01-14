/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 10/09/2007
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Incarichi_procedura_noteBase extends Incarichi_procedura_noteKey implements Keyed {
	//  NOTA VARCHAR(2000)
	private java.lang.String nota;

	public Incarichi_procedura_noteBase() {
		super();
	}
	public Incarichi_procedura_noteBase(java.lang.Integer esercizio, java.lang.Long pg_repertorio, java.lang.Long pg_nota) {
		super(esercizio, pg_repertorio, pg_nota);
	}
	public java.lang.String getNota() {
		return nota;
	}
	public void setNota(java.lang.String nota) {
		this.nota = nota;
	}
}