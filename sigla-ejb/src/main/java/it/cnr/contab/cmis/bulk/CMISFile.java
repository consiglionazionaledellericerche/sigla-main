package it.cnr.contab.cmis.bulk;


import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.annotation.CMISType;
import it.cnr.contab.spring.config.StorageObject;
import it.cnr.contab.spring.config.StoragePropertyNames;
import it.cnr.jada.bulk.OggettoBulk;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;

@CMISType(name="cmis:document")
public class CMISFile extends OggettoBulk {
	private static final long serialVersionUID = -1775673719677028944L;

    private String contentType;
    private String fileName;
    private byte[] bytes;
    
	private String description;
    private String title;
    private String author;
    
	private StorageObject storageObject;

    public CMISFile() {
		super();
	}

	/**
     * Constructs a CMISFile
     *
     * @param bytes the File object on the server which holds the uploaded contents of the file
     * @param contentType the content type of the file declared by the browser during upload
     * @param originalName the name of the file as declared by the user&apos;s browser
     */
    public CMISFile(byte[] bytes, String contentType, String originalName) {
    	this.bytes = bytes;
        this.contentType = contentType;
        this.fileName = originalName;
        this.storageObject = null;
    }

    public CMISFile(InputStream inputStream, String contentType, String originalName) {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
        	try {
                int c;
	            while ((c = inputStream.read()) != -1) {
	                baos.write(c);
	            }
			} finally {
	        	inputStream.close();
	        }
        } catch (IOException e) {
        }        	
    	this.bytes = baos.toByteArray();
        this.contentType = contentType;
        this.fileName = originalName;
        this.storageObject = null;
    }

    public CMISFile(StorageObject storageObject) {
		this.storageObject = storageObject;
        this.contentType = storageObject.<String>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value());
        this.fileName = storageObject.<String>getPropertyValue(StoragePropertyNames.NAME.value());
        this.author = storageObject.<String>getPropertyValue(StoragePropertyNames.AUTHOR.value());
        this.description = storageObject.<String>getPropertyValue(StoragePropertyNames.DESCRIPTION.value());
        this.title = storageObject.<String>getPropertyValue(StoragePropertyNames.TITLE.value());
	}
	
    public CMISFile(File file, String contentType, String originalName) throws IOException{
		this(new FileInputStream(file), contentType!=null?contentType:new MimetypesFileTypeMap().getContentType(file), originalName);
    }

    public CMISFile(File file, String originalName) throws IOException{
		this(new FileInputStream(file), new MimetypesFileTypeMap().getContentType(file), originalName);
    }

    public StorageObject getStorageObject() {
		return storageObject;
	}
	
	public void setStorageObject(StorageObject storageObject) {
		this.storageObject = storageObject;
	}

	/**
     * Returns the name of the file that the user selected and uploaded (this is not necessarily
     * the name that the underlying file is now stored on the server using).
     */
	@CMISProperty(name="cmis:name")
    public String getFileName() {
        return fileName;
    }

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
     * Returns the content type of the file that the user selected and uploaded.
     */
	public String getContentType() {
        return contentType;
    }
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

    /**
     * Gets an input stream to read from the file uploaded
     */
    public byte[] getBytes() {
		return bytes;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@CMISPolicy(name="P:cm:titled", property=@CMISProperty(name="cm:description"))
	public String getDescription() {
		return description;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@CMISPolicy(name="P:cm:titled", property=@CMISProperty(name="cm:title"))
	public String getTitle() {
		return title;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@CMISPolicy(name="P:cm:author", property=@CMISProperty(name="cm:author"))
	public String getAuthor() {
		return author;
	}

	public InputStream getInputStream() throws IOException {
		if (getBytes()!=null && getBytes().length>0)
			return new ByteArrayInputStream(getBytes());
		return null;
	}

	public String getCMISParentPath(){
		return null;
	}

	public String getCMISAlternativeParentPath(){
		return null;
	}
}
