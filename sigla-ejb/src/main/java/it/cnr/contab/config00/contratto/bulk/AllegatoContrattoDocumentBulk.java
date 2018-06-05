package it.cnr.contab.config00.contratto.bulk;

import it.cnr.si.spring.storage.bulk.StorageTypeName;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.OggettoBulk;

import java.io.File;
import java.math.BigInteger;

public class AllegatoContrattoDocumentBulk extends OggettoBulk implements StorageTypeName {
	private static final long serialVersionUID = 1L;
	private ContrattoBulk contrattoBulk;
	private File file;
	private String contentType;
	private String nome;
	private String titolo;
	private String descrizione;
	private String type;
	private String link;

	private long contentlength;
	private String nodeId;
	private String name;

	private static final java.util.Dictionary ti_allegatoKeys =  new it.cnr.jada.util.OrderedHashtable();

	final public static String CONTRATTO = "D:sigla_contratti_attachment:contratto";
	final public static String PROGETTO = "D:sigla_contratti_attachment:progetto";
	final public static String CAPITOLATO = "D:sigla_contratti_attachment:capitolato";
	final public static String GENERICO = "D:sigla_contratti_attachment:allegato_generico";

	static {
		ti_allegatoKeys.put(CONTRATTO,"Contratto");
		ti_allegatoKeys.put(PROGETTO,"Progetto");
		ti_allegatoKeys.put(CAPITOLATO,"Capitolato");
		ti_allegatoKeys.put(GENERICO,"Generico");
	}

	public final java.util.Dictionary getTi_allegatoKeys() {
		return ti_allegatoKeys;
	}

	public AllegatoContrattoDocumentBulk() {
		super();
	}

	public static AllegatoContrattoDocumentBulk construct(StorageObject storageObject){
		return new AllegatoContrattoDocumentBulk(storageObject);
	}

	public AllegatoContrattoDocumentBulk(StorageObject storageObject) {
		super();
		contentlength = storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).longValue();
		nodeId = storageObject.getKey();
		name = storageObject.getPropertyValue(StoragePropertyNames.NAME.value());
	}

	public String getName() {
		return name;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public boolean isNodePresent(){
		return nodeId != null;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	@StorageProperty(name="sigla_contratti_attachment:original_name")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	@StoragePolicy(name="P:cm:titled", property=@StorageProperty(name="cm:title"))
	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	@StoragePolicy(name="P:cm:titled", property=@StorageProperty(name="cm:description"))
	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ContrattoBulk getContrattoBulk() {
		return contrattoBulk;
	}

	public void setContrattoBulk(ContrattoBulk contrattoBulk) {
		this.contrattoBulk = contrattoBulk;
	}

	public String getTypeName() {
		return getType();
	}

	@StoragePolicy(name="P:sigla_contratti_aspect:link", property=@StorageProperty(name="sigla_contratti_aspect_link:url"))
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public boolean isTypeEnabled(){
		return !isToBeCreated();
	}

	public boolean isContentStreamPresent(){
		return isNodePresent() && contentlength > 0;
	}

	@StorageProperty(name="cmis:name")
	public String getDocumentName() {
		StringBuffer name = new StringBuffer();
		if (getType().equals(CONTRATTO))
			name.append("CTR");
		else if (getType().equals(PROGETTO))
			name.append("PRG");
		else if (getType().equals(CAPITOLATO))
			name.append("CAP");
		else if (getType().equals(GENERICO))
			name.append("GEN");
		name.append("-").append(contrattoBulk.getUnita_organizzativa().getCd_unita_organizzativa());
		name.append("-").append(contrattoBulk.getEsercizio()).append(contrattoBulk.getStato()).append(Utility.lpad(contrattoBulk.getPg_contratto(), 9, '0'));
		name.append(".").append(getNome()==null?"Progetto.link":getNome());
		return name.toString();
	}
}