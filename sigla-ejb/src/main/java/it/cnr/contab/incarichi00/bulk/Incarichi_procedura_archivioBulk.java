/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;

import it.cnr.contab.incarichi00.bulk.storage.StorageFileProcedura;
import it.cnr.si.spring.storage.bulk.StorageFile;
import it.cnr.contab.incarichi00.bulk.storage.StorageFileProceduraBando;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.contab.util.Utility;

import java.io.IOException;

public class Incarichi_procedura_archivioBulk extends Incarichi_procedura_archivioKey{
	private Incarichi_proceduraBulk incarichi_procedura;

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
	public StorageFile getCMISFile() throws IOException{
		StorageFileProcedura cmisFile = null;
		if (this.isBando()) {
			if (this.getFile()==null)
				cmisFile = new StorageFileProceduraBando(this);
			else 
				cmisFile = new StorageFileProceduraBando(this.getFile(), this.getNome_file(), this);
		} else {
			if (this.getFile()==null)
				cmisFile = new StorageFileProcedura(this);
			else 
				cmisFile = new StorageFileProcedura(this.getFile(), this.getNome_file(), this);
		}
		return cmisFile;
	}
	public StorageFile getCMISFile(StorageObject storageObject) {
		if (this.isBando())
			return new StorageFileProceduraBando(storageObject, this);
		else
			return new StorageFileProcedura(storageObject, this);
	}

	public String constructCMISNomeFile() {
		StringBuffer nomeFile = new StringBuffer();
		nomeFile = nomeFile.append(super.constructCMISNomeFile());
		nomeFile = nomeFile.append("-"+this.getIncarichi_procedura().getCd_unita_organizzativa());
		nomeFile = nomeFile.append("-"+this.getIncarichi_procedura().getEsercizio().toString()+Utility.lpad(this.getIncarichi_procedura().getPg_procedura().toString(),9,'0'));
		nomeFile = nomeFile.append("."+(this.getNome_file()!=null?this.getNome_file():Incarichi_archivioBulk.getTipo_archivioKeys().get(this.getTipo_archivio()).toString()+".txt"));
		return nomeFile.toString();
	}
}