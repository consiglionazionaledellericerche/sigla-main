package it.cnr.contab.config00.contratto.bulk;

import it.cnr.cmisdl.model.Node;
import it.cnr.contab.cmis.CMISTypeName;
import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.service.CMISService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.OggettoBulk;

import java.io.File;
import java.math.BigInteger;
import java.util.StringTokenizer;
public class AllegatoContrattoDocumentBulk extends OggettoBulk implements CMISTypeName{
	private static final long serialVersionUID = 1L;
	private ContrattoBulk contrattoBulk;
	private File file;
	private String contentType;
	private String nome;
	private String titolo;
	private String descrizione;
	private String type;
	private String link;
	
	private BigInteger contentlength;
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

	public static AllegatoContrattoDocumentBulk construct(Node node){
		return new AllegatoContrattoDocumentBulk(node);
	}
	
	public AllegatoContrattoDocumentBulk(Node node) {
		super();
		contentlength = node.getContentLength();
		nodeId = node.getId();
		name = node.getName();
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

	public String parseFilename(String file) {
		StringTokenizer fileName = new StringTokenizer(file,"\\",false);
		String newFileName = null;
		
		while (fileName.hasMoreTokens()){
			newFileName = fileName.nextToken();   	
		}
		CMISService cmisService = SpringUtil.getBean("cmisService",
				CMISService.class);		

		if (newFileName != null){
			return cmisService.sanitizeFilename(newFileName);
		}
		return cmisService.sanitizeFilename(file);
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
	@CMISProperty(name="sigla_contratti_attachment:original_name")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	@CMISPolicy(name="P:cm:titled", property=@CMISProperty(name="cm:title"))
	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	@CMISPolicy(name="P:cm:titled", property=@CMISProperty(name="cm:description"))
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
	
	@CMISPolicy(name="P:sigla_contratti_aspect:link", property=@CMISProperty(name="sigla_contratti_aspect_link:url"))	
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
		return isNodePresent() && contentlength.compareTo(BigInteger.ZERO) == 1;
	}

	@CMISProperty(name="cmis:name")
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
		name.append("-").append(contrattoBulk.getEsercizio()).append(Utility.lpad(contrattoBulk.getPg_contratto(), 9, '0'));
		name.append(".").append(getNome()==null?"Progetto.link":getNome());
		return name.toString();
	}
}
