package it.cnr.contab.web.rest.model;

public class OtherAttachments extends AttachmentFile {

    EnumTypeAttachmentFile typeAttachment;

    public EnumTypeAttachmentFile getTypeAttachment() {
        return typeAttachment;
    }

    public void setTypeAttachment(EnumTypeAttachmentFile typeAttachment) {
        this.typeAttachment = typeAttachment;
    }
}
