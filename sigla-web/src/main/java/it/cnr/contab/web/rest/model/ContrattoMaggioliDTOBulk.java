package it.cnr.contab.web.rest.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class ContrattoMaggioliDTOBulk extends ContrattoDtoBulk implements Serializable {
    @NotNull
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
        AttachmentFile contrattoFile;

    List<OtherAttachments> otherFiles;

    public List<OtherAttachments> getOtherFiles() {
        return otherFiles;
    }

    public void setOtherFiles(List<OtherAttachments> otherFiles) {
        this.otherFiles = otherFiles;
    }

    public AttachmentFile getContrattoFile() {
        return contrattoFile;
    }

    public void setContrattoFile(AttachmentFile contrattoFile) {
        this.contrattoFile = contrattoFile;
    }
}
