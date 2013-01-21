package it.cnr.contab.incarichi00.bulk.cmis;

import it.cnr.cmisdl.model.Node;
import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.incarichi00.bulk.Incarichi_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_rappBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_varBulk;

import java.io.File;
import java.io.IOException;

public class CMISFileAssegniRicerca extends CMISFileIncarichi {
	private static final long serialVersionUID = -1775673719677028944L;

	public CMISFileAssegniRicerca(Incarichi_repertorio_archivioBulk incaricoRepertorioArchivio) throws IOException{
		super(incaricoRepertorioArchivio);
	}

	public CMISFileAssegniRicerca(Incarichi_repertorio_rappBulk incaricoRepertorioRapp) throws IOException{
		super(incaricoRepertorioRapp);
	}

	public CMISFileAssegniRicerca(Incarichi_repertorio_varBulk incaricoRepertorioVar) throws IOException{
		super(incaricoRepertorioVar);
	}

	public CMISFileAssegniRicerca(File file, String originalName, Incarichi_repertorio_archivioBulk incaricoRepertorioArchivio) throws IOException {
		super(file, originalName, incaricoRepertorioArchivio);
	}

	public CMISFileAssegniRicerca(File file, String originalName, Incarichi_repertorio_rappBulk incaricoRepertorioRapp) throws IOException {
		super(file, originalName, incaricoRepertorioRapp);
	}

	public CMISFileAssegniRicerca(File file, String originalName, Incarichi_repertorio_varBulk incaricoRepertorioVar) throws IOException {
		super(file, originalName, incaricoRepertorioVar);
	}

	public CMISFileAssegniRicerca(Node node, Incarichi_repertorio_archivioBulk incaricoRepertorioArchivio) {
		super(node, incaricoRepertorioArchivio);
	}

	public CMISFileAssegniRicerca(Node node, Incarichi_repertorio_rappBulk incaricoRepertorioRapp) {
		super(node, incaricoRepertorioRapp);
	}

	public CMISFileAssegniRicerca(Node node, Incarichi_repertorio_varBulk incaricoRepertorioVar) {
		super(node, incaricoRepertorioVar);
	}

	@CMISPolicy(name="P:sigla_contratti_aspect:assegni_ricerca", property=@CMISProperty(name="sigla_contratti_aspect_assegni_ricerca:esercizio"))
    public Integer getEsercizioIncarico() {
		return super.getEsercizioIncarico();
    }

	@CMISPolicy(name="P:sigla_contratti_aspect:assegni_ricerca", property=@CMISProperty(name="sigla_contratti_aspect_assegni_ricerca:progressivo", converterBeanName="cmis.converter.longToIntegerConverter"))
    public Long getPgIncarico() {
		return super.getPgIncarico();
    }

	@SuppressWarnings("unused")
	private void initCMISField(Integer esercizio, Long progressivo){
	    this.setAuthor(getIncaricoArchivio().getUtcr());
		this.setTitle((String)Incarichi_archivioBulk.getTipo_archivioKeys().get(getIncaricoArchivio().getTipo_archivio()));
		this.setDescription((String)Incarichi_archivioBulk.getTipo_archivioKeys().get(getIncaricoArchivio().getTipo_archivio()).toString()+
					" - Assegno di Ricerca nr."+esercizio+"/"+progressivo);
	}
}
