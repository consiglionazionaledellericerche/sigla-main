/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 24/09/2007
 */
package it.cnr.contab.incarichi00.bulk;

import it.cnr.si.spring.storage.bulk.StorageFile;
import it.cnr.contab.incarichi00.bulk.storage.StorageFileAssegniRicerca;
import it.cnr.contab.incarichi00.bulk.storage.StorageFileBorseStudio;
import it.cnr.contab.incarichi00.bulk.storage.StorageFileIncarichi;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.contab.util.Utility;

import java.io.IOException;

public class Incarichi_repertorio_archivioBulk extends Incarichi_repertorio_archivioKey {
	private Incarichi_repertorioBulk incarichi_repertorio;

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
	public Integer getEsercizioProcedura() {
		if (this.getIncarichi_repertorio() == null)
			return null;
		return this.getIncarichi_repertorio().getEsercizio_procedura();
	}
	public Long getPg_procedura() {
		if (this.getIncarichi_repertorio() == null)
			return null;
		return this.getIncarichi_repertorio().getPg_procedura();
	}
	public StorageFile getCMISFile() throws IOException{
		StorageFile storageFile =null;
		if (this.getIncarichi_repertorio()!=null && 
			this.getIncarichi_repertorio().getIncarichi_procedura()!=null) {
			if (this.getIncarichi_repertorio().getIncarichi_procedura().isProceduraForBorseStudio()) {
				if (this.getFile()==null)
					storageFile = new StorageFileBorseStudio(this);
				else
					storageFile = new StorageFileBorseStudio(this.getFile(), this.getNome_file(), this);
			} else if (this.getIncarichi_repertorio().getIncarichi_procedura().isProceduraForAssegniRicerca()) {
				if (this.getFile()==null)
					storageFile = new StorageFileAssegniRicerca(this);
				else
					storageFile = new StorageFileAssegniRicerca(this.getFile(), this.getNome_file(), this);
			}
		}
		if (storageFile ==null) {
			if (this.getFile()==null)
				storageFile = new StorageFileIncarichi(this);
			else
				storageFile = new StorageFileIncarichi(this.getFile(), this.getNome_file(), this);
		}
		return storageFile;
	}
	public StorageFile getCMISFile(StorageObject storageObject) {
		if (this.getIncarichi_repertorio()!=null && 
			this.getIncarichi_repertorio().getIncarichi_procedura()!=null) {
			if (this.getIncarichi_repertorio().getIncarichi_procedura().isProceduraForBorseStudio())
				return new StorageFileBorseStudio(storageObject, this);
			else if (this.getIncarichi_repertorio().getIncarichi_procedura().isProceduraForAssegniRicerca())
				return new StorageFileAssegniRicerca(storageObject, this);
		}
		return new StorageFileIncarichi(storageObject, this);
	}
	public String constructCMISNomeFile() {
		StringBuffer nomeFile = new StringBuffer();
		nomeFile = nomeFile.append(super.constructCMISNomeFile());
		nomeFile = nomeFile.append("-"+this.getIncarichi_repertorio().getCd_unita_organizzativa());
		nomeFile = nomeFile.append("-"+this.getEsercizio().toString()+Utility.lpad(this.getPg_repertorio().toString(),9,'0'));
		nomeFile = nomeFile.append("."+(this.getNome_file()!=null?this.getNome_file():Incarichi_archivioBulk.getTipo_archivioKeys().get(this.getTipo_archivio()).toString()+".txt"));
		return nomeFile.toString();
	}
}