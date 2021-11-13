package it.cnr.contab.web.rest.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class ContrattoMaggioliDTOBulk extends ContrattoDtoBulk implements Serializable {

    @NotNull
   AttachmentFile contrattoFile;

    List<AttachmentContratto> attachments;

    public List<AttachmentContratto> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentContratto> attachments) {
        this.attachments = attachments;
    }

    public AttachmentFile getContrattoFile() {
        return contrattoFile;
    }

    public void setContrattoFile(AttachmentFile contrattoFile) {
        this.contrattoFile = contrattoFile;
    }
}
