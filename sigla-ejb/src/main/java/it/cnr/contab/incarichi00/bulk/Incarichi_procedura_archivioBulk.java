/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;
import java.io.File;
import java.sql.Timestamp;
import java.util.StringTokenizer;

import it.cnr.jada.persistency.Persister;
public class Incarichi_procedura_archivioBulk extends Incarichi_procedura_archivioKey{
	private Incarichi_proceduraBulk incarichi_procedura;

	static {
		tipo_archivioKeys.put(TIPO_BANDO,"Avviso da pubblicare");
//		tipo_archivioKeys.put(TIPO_DA_PUBBLICARE,"Allegato da pubblicare");
		tipo_archivioKeys.put(TIPO_GENERICO,"Allegato generico");
		tipo_archivioKeys.put(TIPO_CONTRATTO,"Contratto stipulato");
		tipo_archivioKeys.put(TIPO_DECISIONE_A_CONTRATTARE,"Decisione a contrattare");
		tipo_archivioKeys.put(TIPO_DECRETO_DI_NOMINA,"Decreto di nomina");
	}

	public Incarichi_procedura_archivioBulk() {
		super();
	}
	public Incarichi_procedura_archivioBulk(java.lang.Integer esercizio, java.lang.Long pg_procedura, java.lang.Long progressivo_riga) {
		super(esercizio, pg_procedura, progressivo_riga);
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
	public void setPg_procedura(Long pg_archivio) {
		if (this.getIncarichi_procedura() != null)
			this.getIncarichi_procedura().setPg_procedura(pg_archivio);
	}	
	public Integer getFaseProcesso() {
		if (getIncarichi_procedura() == null ||
			getIncarichi_procedura().getFaseProcesso() == null)
			return null;

		if (getIncarichi_procedura().getFaseProcesso().equals(Incarichi_proceduraBulk.FASE_DEFINITIVA) &&
			getIncarichi_procedura().isProceduraInviataCorteConti())
			return Incarichi_proceduraBulk.FASE_INVIO_CORTE_CONTI;
		return getIncarichi_procedura().getFaseProcesso();
	}
}