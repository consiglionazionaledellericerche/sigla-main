package it.cnr.contab.web.rest.model;

public class AttachmentContratto extends AttachmentFile {

    EnumTypeAttachmentContratti typeAttachment;

    public EnumTypeAttachmentContratti getTypeAttachment() {
        return typeAttachment;
    }

    public void setTypeAttachment(EnumTypeAttachmentContratti typeAttachment) {
        this.typeAttachment = typeAttachment;
    }
}
