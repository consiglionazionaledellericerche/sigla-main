/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;
public class Incarichi_procedura_noteBulk extends Incarichi_procedura_noteBase {
	private Incarichi_proceduraBulk incarichi_procedura = new Incarichi_proceduraBulk();

	public Incarichi_procedura_noteBulk() {
		super();
	}
	public Incarichi_procedura_noteBulk(java.lang.Integer esercizio, java.lang.Long pg_procedura, java.lang.Long pg_nota) {
		super(esercizio, pg_procedura, pg_nota);
		setIncarichi_procedura(new Incarichi_proceduraBulk(esercizio,pg_procedura));
	}

	public Incarichi_proceduraBulk getIncarichi_procedura() {
		return incarichi_procedura;
	}
	public void setIncarichi_procedura(
			Incarichi_proceduraBulk incarichi_procedura) {
		this.incarichi_procedura = incarichi_procedura;
	}
	public Integer getEsercizio() {
		if (this.getIncarichi_procedura() == null)
			return null;
		return this.getIncarichi_procedura().getEsercizio();
	}
	public void setEsercizio(Integer esercizio) {
		if (this.getIncarichi_procedura() != null)
			this.getIncarichi_procedura().setEsercizio(esercizio);
	}	
	public Long getPg_procedura() {
		if (this.getIncarichi_procedura() == null)
			return null;
		return this.getIncarichi_procedura().getPg_procedura();
	}
	public void setPg_procedura(Long pg_procedura) {
		if (this.getIncarichi_procedura() != null)
			this.getIncarichi_procedura().setPg_procedura(pg_procedura);
	}	
}