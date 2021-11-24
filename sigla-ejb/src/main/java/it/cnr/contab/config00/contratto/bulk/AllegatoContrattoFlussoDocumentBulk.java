/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.config00.contratto.bulk;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.bulk.StorageTypeName;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

public class AllegatoContrattoFlussoDocumentBulk extends OggettoBulk implements StorageTypeName {
	private static final long serialVersionUID = 1L;
	private ContrattoBulk contrattoBulk;
	//allegato da webService
	private byte[] bytes;
	private File file;
	private String contentType;
	private String nome;
	private String titolo;
	private String descrizione;
	private String type;
	private Boolean urp;
	private Boolean trasparenza;
	private String label;

	private long contentlength;
	private String nodeId;
	private String name;

	public static final OrderedHashtable  ti_allegatoFlussoKeys =  new it.cnr.jada.util.OrderedHashtable();

//	final public static String CONTRATTO = "P:sigla_contratti_aspect:contratto";
//	final public static String PROGETTO = "P:sigla_contratti_aspect:progetto";
//	final public static String CAPITOLATO = "P:sigla_contratti_aspect:capitolato";
//	final public static String GENERICO = "P:sigla_contratti_aspect:allegato_generico";
	final public static String ALLEGATO_FLUSSO = "P:sigla_contratti_aspect:doc_flusso_allegato";
	final public static String CONTRATTO_FLUSSO = "P:sigla_contratti_aspect:doc_flusso_contratto";
	final public static String AGGIUDICAZIONE_FLUSSO = "P:sigla_contratti_aspect:doc_flusso_provvedimento_aggiudicazione";
	final public static String REVOCA_FLUSSO = "P:sigla_contratti_aspect:doc_flusso_provvedimento_di_revoca";
	final public static String RICHIESTA_ACQUISTO_FLUSSO = "P:sigla_contratti_aspect:doc_flusso_richiesta_di_acquisto";
	final public static String AVVISO_POST_INFORMAZIONE_FLUSSO = "P:sigla_contratti_aspect:doc_flusso_avviso_post_informazione";
	final public static String STIPULA_FLUSSO = "P:sigla_contratti_aspect:doc_flusso_stipula";
	final public static String ELENCO_VERBALI_FLUSSO = "P:sigla_contratti_aspect:doc_flusso_elenco_verbali";
	final public static String NOMINA_COMMISSIONE_FLUSSO = "P:sigla_contratti_aspect:doc_flusso_provvedimento_nomina_commissione";
	final public static String AMMESSI_ESCLUSI_FLUSSO = "P:sigla_contratti_aspect:doc_flusso_provvedimento_ammessi_esclusi";
	final public static String LETTERA_INVITO_FLUSSO = "P:sigla_contratti_aspect:doc_flusso_lettera_invito";
	final public static String BANDO_AVVISI_FLUSSO = "P:sigla_contratti_aspect:doc_flusso_bando_avvisi";
	final public static String MODIFICHE_VARIANTI_FLUSSO = "P:sigla_contratti_aspect:doc_flusso_modifiche_varianti_art106";
	final public static String DECISIONE_CONTRATTARE_FLUSSO = "P:sigla_contratti_aspect:doc_flusso_decisione_contrattare";

//	static {
//		ti_allegatoKeys.put(CONTRATTO,"Contratto");
//		ti_allegatoKeys.put(PROGETTO,"Progetto");
//		ti_allegatoKeys.put(CAPITOLATO,"Capitolato");
//		ti_allegatoKeys.put(GENERICO,"Generico");
//	}
//
	static {
		ti_allegatoFlussoKeys.put(DECISIONE_CONTRATTARE_FLUSSO,"Decisione a Contrattare");
//		ti_allegatoFlussoKeys.put(PROGETTO,"Progetto");
//		ti_allegatoFlussoKeys.put(CAPITOLATO,"Capitolato");
//		ti_allegatoFlussoKeys.put(GENERICO,"Generico");
		ti_allegatoFlussoKeys.put(ALLEGATO_FLUSSO,"Varie");
		ti_allegatoFlussoKeys.put(CONTRATTO_FLUSSO,"Contratto");
		ti_allegatoFlussoKeys.put(AGGIUDICAZIONE_FLUSSO,"Aggiudicazione");
		ti_allegatoFlussoKeys.put(REVOCA_FLUSSO,"Revoca");
		ti_allegatoFlussoKeys.put(RICHIESTA_ACQUISTO_FLUSSO,"Richiesta di Acquisto");
		ti_allegatoFlussoKeys.put(AVVISO_POST_INFORMAZIONE_FLUSSO,"Avviso Post informazione");
		ti_allegatoFlussoKeys.put(STIPULA_FLUSSO,"Stipula");
		ti_allegatoFlussoKeys.put(ELENCO_VERBALI_FLUSSO,"Elenco Verbali");
		ti_allegatoFlussoKeys.put(NOMINA_COMMISSIONE_FLUSSO,"Nomina Commissione");
		ti_allegatoFlussoKeys.put(AMMESSI_ESCLUSI_FLUSSO,"Elenco Ammessi Esclusi");
		ti_allegatoFlussoKeys.put(LETTERA_INVITO_FLUSSO,"Lettera Invito");
		ti_allegatoFlussoKeys.put(BANDO_AVVISI_FLUSSO,"Bando Avvisi");
		ti_allegatoFlussoKeys.put(MODIFICHE_VARIANTI_FLUSSO,"Modifiche Varianti art. 106");
	}

	public final java.util.Dictionary getTi_allegatoFlussoKeys() {
		return ti_allegatoFlussoKeys;
	}

	public AllegatoContrattoFlussoDocumentBulk() {
		super();
	}

	public static AllegatoContrattoFlussoDocumentBulk construct(StorageObject storageObject) {
		return new AllegatoContrattoFlussoDocumentBulk(storageObject);
	}

	public AllegatoContrattoFlussoDocumentBulk(StorageObject storageObject){
		super();
		nodeId = storageObject.getKey();
		name = storageObject.getPropertyValue(StoragePropertyNames.NAME.value());
		contentlength = Optional.ofNullable(storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()))
				.map(bigInteger -> bigInteger.longValue())
				.orElse(Long.valueOf(0));
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

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

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

//	@StoragePolicy(name="P:sigla_contratti_aspect:link", property=@StorageProperty(name="sigla_contratti_aspect_link:url"))
//	public String getLink() {
//		return link;
//	}
//
//	public void setLink(String link) {
//		this.link = link;
//	}
//
	public boolean isTypeEnabled(){
		return !isToBeCreated();
	}

	public boolean isContentStreamPresent(){
		return isNodePresent() && contentlength > 0;
	}

	@StorageProperty(name="cmis:name")
	public String getDocumentName() {
		StringBuffer name = new StringBuffer();
		String tipo = getType();
		name.append("-").append(contrattoBulk.getUnita_organizzativa().getCd_unita_organizzativa());
		name.append("-").append(contrattoBulk.getEsercizio()).append(contrattoBulk.getStato()).append(Utility.lpad(contrattoBulk.getPg_contratto(), 9, '0'));
		name.append(".").append(getNome()==null?"Progetto.link":getNome());
		return name.toString();
	}
	public boolean isTipoAllegatoDuplicabile(){
		if (getType() == null){
			return true;
		}
		return 	 getType().equals(ALLEGATO_FLUSSO);

	}
	public boolean isTipoContratto(){
		if (getType() == null){
			return false;
		}
		return 	 getType().equals(CONTRATTO_FLUSSO) || getType().equals(STIPULA_FLUSSO);
	}

	@StoragePolicy(name="P:sigla_commons_aspect:flusso_pubblicazione", property=@StorageProperty(name="sigla_commons_aspect:pubblicazione_urp"))
	public Boolean getUrp() {
		return urp;
	}

	public void setUrp(Boolean urp) {
		this.urp = urp;
	}

	@StoragePolicy(name="P:sigla_commons_aspect:flusso_pubblicazione", property=@StorageProperty(name="sigla_commons_aspect:pubblicazione_trasparenza"))
	public Boolean getTrasparenza() {
		return trasparenza;
	}

	public void setTrasparenza(Boolean trasparenza) {
		this.trasparenza = trasparenza;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@StorageProperty(name="cmis:secondaryObjectTypeIds")
	public List<String> getAspect() {
		List<String> results = new ArrayList<String>();
		results.add(getType());
		return results;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean existBytes(){
		if ( file==null && bytes==null)
			return Boolean.FALSE;
		return Boolean.TRUE;

	}
	public InputStream getInputStream()  {
		if ( file!=null) {
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		if ( bytes!=null)
			return  new ByteArrayInputStream(bytes);
		return null;
	}
}
