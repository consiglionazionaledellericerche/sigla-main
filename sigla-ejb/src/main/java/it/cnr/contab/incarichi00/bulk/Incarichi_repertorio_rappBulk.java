package it.cnr.contab.incarichi00.bulk;

import it.cnr.cmisdl.model.Node;
import it.cnr.contab.cmis.bulk.CMISFile;
import it.cnr.contab.incarichi00.bulk.cmis.CMISFileAssegniRicerca;
import it.cnr.contab.incarichi00.bulk.cmis.CMISFileBorseStudio;
import it.cnr.contab.incarichi00.bulk.cmis.CMISFileIncarichi;
import it.cnr.contab.util.Utility;

import java.io.IOException;

public class Incarichi_repertorio_rappBulk extends Incarichi_repertorio_rappBase {
	private Incarichi_repertorioBulk incarichi_repertorio = new Incarichi_repertorioBulk();

	public Incarichi_repertorio_rappBulk() {
		super();
	}
	public Incarichi_repertorio_rappBulk(java.lang.Integer esercizio, java.lang.Long pg_repertorio, java.lang.Long progressivo_riga) {
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
	public void setPg_repertorio(Long pg_repertorio) {
		if (this.getIncarichi_repertorio() != null)
			this.getIncarichi_repertorio().setPg_repertorio(pg_repertorio);
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
	public CMISFile getCMISFile() throws IOException{
		CMISFile cmisFile=null;
		if (this.getIncarichi_repertorio()!=null && 
			this.getIncarichi_repertorio().getIncarichi_procedura()!=null) {
			if (this.getIncarichi_repertorio().getIncarichi_procedura().isProceduraForBorseStudio()) {
				if (this.getFile()==null)
					cmisFile = new CMISFileBorseStudio(this);
				else
					cmisFile = new CMISFileBorseStudio(this.getFile(), this.getNome_file(), this);
			} else if (this.getIncarichi_repertorio().getIncarichi_procedura().isProceduraForAssegniRicerca()) {
				if (this.getFile()==null)
					cmisFile = new CMISFileAssegniRicerca(this);
				else
					cmisFile = new CMISFileAssegniRicerca(this.getFile(), this.getNome_file(), this);
			}
		}
		if (cmisFile==null) {
			if (this.getFile()==null)
				cmisFile = new CMISFileIncarichi(this);
			else
				cmisFile = new CMISFileIncarichi(this.getFile(), this.getNome_file(), this);
		}
		return cmisFile;
	}
	public CMISFile getCMISFile(Node node) {
		if (this.getIncarichi_repertorio()!=null && 
			this.getIncarichi_repertorio().getIncarichi_procedura()!=null) {
			if (this.getIncarichi_repertorio().getIncarichi_procedura().isProceduraForBorseStudio())
				return new CMISFileBorseStudio(node, this);
			else if (this.getIncarichi_repertorio().getIncarichi_procedura().isProceduraForAssegniRicerca())
				return new CMISFileAssegniRicerca(node, this);
		}
		return new CMISFileIncarichi(node, this);
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
