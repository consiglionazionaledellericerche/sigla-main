/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 24/09/2007
 */
package it.cnr.contab.incarichi00.bulk;

public class Incarichi_repertorio_archivioBulk extends Incarichi_repertorio_archivioKey {
	private Incarichi_repertorioBulk incarichi_repertorio;

	static {
		tipo_archivioKeys.put(TIPO_GENERICO,"Allegato generico");
		tipo_archivioKeys.put(TIPO_CONTRATTO,"Contratto stipulato");
		tipo_archivioKeys.put(TIPO_DECRETO_DI_NOMINA,"Decreto di nomina");
		tipo_archivioKeys.put(TIPO_ATTO_ESITO_CONTROLLO,"Esito Controllo Corte Conti");
	}

	public Incarichi_repertorio_archivioBulk() {
		super();
	}
	public Incarichi_repertorio_archivioBulk(java.lang.Integer esercizio, java.lang.Long pg_repertorio, java.lang.Long progressivo_riga) {
		super(esercizio, pg_repertorio, progressivo_riga);
		setIncarichi_repertorio(new Incarichi_repertorioBulk(esercizio,pg_repertorio));
	}
	public Incarichi_repertorioBulk getIncarichi_repertorio() {
		return incarichi_repertorio;
	}
	public void setIncarichi_repertorio(
			Incarichi_repertorioBulk incarichi_repertorio) {
		this.incarichi_repertorio = incarichi_repertorio;
	}
	public Integer getEsercizio() {
		if (this.getIncarichi_repertorio() == null)
			return null;
		return this.getIncarichi_repertorio().getEsercizio();
	}
	public void setEsercizio(Integer esercizio) {
		if (this.getIncarichi_repertorio() != null)
			this.getIncarichi_repertorio().setEsercizio(esercizio);
	}	
	public Long getPg_repertorio() {
		if (this.getIncarichi_repertorio() == null)
			return null;
		return this.getIncarichi_repertorio().getPg_repertorio();
	}
	public void setPg_repertorio(Long pg_archivio) {
		if (this.getIncarichi_repertorio() != null)
			this.getIncarichi_repertorio().setPg_repertorio(pg_archivio);
	}	
	public Integer getFaseProcesso() {
		if (getIncarichi_repertorio() == null ||
	        getIncarichi_repertorio().getIncarichi_procedura() == null ||
	        getIncarichi_repertorio().getIncarichi_procedura().getFaseProcesso() == null)
			return null;

		if (getIncarichi_repertorio().getIncarichi_procedura().getFaseProcesso().equals(Incarichi_proceduraBulk.FASE_DEFINITIVA) &&
			getIncarichi_repertorio().isIncaricoInviatoCorteConti())
			return Incarichi_proceduraBulk.FASE_INVIO_CORTE_CONTI;

		return getIncarichi_repertorio().getIncarichi_procedura().getFaseProcesso();
	}
}