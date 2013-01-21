package it.cnr.contab.incarichi00.bulk.cmis;

import it.cnr.cmisdl.model.Node;
import it.cnr.contab.cmis.CMISTypeName;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_archivioBulk;
import it.cnr.contab.incarichi00.cmis.CMISContrattiAttachment;

import java.io.File;
import java.io.IOException;

public class CMISFileProceduraBando extends CMISFileProcedura implements CMISTypeName{
	private static final long serialVersionUID = -1775673719677028944L;

	public CMISFileProceduraBando(File file, String originalName, Incarichi_procedura_archivioBulk incaricoProceduraArchivio) throws IOException {
		super(file, originalName, incaricoProceduraArchivio);
	}

	public CMISFileProceduraBando(Incarichi_procedura_archivioBulk incaricoProceduraArchivio) throws IOException {
		super(incaricoProceduraArchivio);
	}

	public CMISFileProceduraBando(Node node, Incarichi_procedura_archivioBulk incaricoProceduraArchivio) {
		super(node, incaricoProceduraArchivio);
	}

	public String getTypeName() {
    	return CMISContrattiAttachment.SIGLA_CONTRATTI_ATTACHMENT_BANDO.value();
	}

	@CMISProperty(name="sigla_contratti_attachment:data_inizio", converterBeanName="cmis.converter.timestampToCalendarConverter")
	public java.sql.Timestamp getDataInizioPubblicazione() {
		if (this.getIncaricoProceduraArchivio()==null||getIncaricoProceduraArchivio().getIncarichi_procedura()==null)
			return null;
		return this.getIncaricoProceduraArchivio().getIncarichi_procedura().getDt_pubblicazione();
	}

	@CMISProperty(name="sigla_contratti_attachment:data_fine", converterBeanName="cmis.converter.timestampToCalendarConverter")
	public java.sql.Timestamp getDataFinePubblicazione() {
		if (this.getIncaricoProceduraArchivio()==null||getIncaricoProceduraArchivio().getIncarichi_procedura()==null)
			return null;
		return this.getIncaricoProceduraArchivio().getIncarichi_procedura().getDt_fine_pubblicazione();
	}
}
