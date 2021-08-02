package it.cnr.contab.web.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.cnr.si.spring.storage.MimeTypes;
import org.springframework.util.MimeType;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class AttachmentFile implements Serializable {
    String nomeFile;

    MimeTypes mimeTypes;

    byte[] bytes;

    public String getNomeFile() {
        return nomeFile;
    }

    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    public MimeTypes getMimeTypes() {
        return mimeTypes;
    }

    public void setMimeTypes(MimeTypes mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
