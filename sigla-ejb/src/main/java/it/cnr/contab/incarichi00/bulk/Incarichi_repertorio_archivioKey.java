/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Incarichi_repertorio_archivioKey extends Incarichi_archivioBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.Long pg_repertorio;
	private java.lang.Long progressivo_riga;
	public Incarichi_repertorio_archivioKey() {
		super();
	}
	public Incarichi_repertorio_archivioKey(java.lang.Integer esercizio, java.lang.Long pg_repertorio, java.lang.Long progressivo_riga) {
		super();
		this.esercizio=esercizio;
		this.pg_repertorio=pg_repertorio;
		this.progressivo_riga=progressivo_riga;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Incarichi_repertorio_archivioKey)) return false;
		Incarichi_repertorio_archivioKey k = (Incarichi_repertorio_archivioKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_repertorio(), k.getPg_repertorio())) return false;
		if (!compareKey(getProgressivo_riga(), k.getProgressivo_riga())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_repertorio());
		i = i + calculateKeyHashCode(getProgressivo_riga());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setPg_repertorio(java.lang.Long pg_repertorio)  {
		this.pg_repertorio=pg_repertorio;
	}
	public java.lang.Long getPg_repertorio() {
		return pg_repertorio;
	}
	public void setProgressivo_riga(java.lang.Long progressivo_riga)  {
		this.progressivo_riga=progressivo_riga;
	}
	public java.lang.Long getProgressivo_riga() {
		return progressivo_riga;
	}
	public String getDownloadUrl()
	{
		if(this == null ||this.getNome_file() == null)
			return null;
		StringBuffer stringbuffer = new StringBuffer("download_incarichi/fileIncaricoAllegato");
		stringbuffer.append("/");
		stringbuffer.append(this.getEsercizio());
		stringbuffer.append("/");
		stringbuffer.append(this.getPg_repertorio());	
		stringbuffer.append("/");
		stringbuffer.append(this.getProgressivo_riga());	
		stringbuffer.append("/");
		stringbuffer.append(this.getNome_file());	
		return stringbuffer.toString();
	}
}