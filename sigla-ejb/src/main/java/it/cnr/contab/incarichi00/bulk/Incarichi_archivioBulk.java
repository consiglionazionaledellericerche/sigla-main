/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 03/04/2008
 */
package it.cnr.contab.incarichi00.bulk;
import java.io.File;
import java.sql.Timestamp;
import java.util.StringTokenizer;

import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.Persister;
public abstract class Incarichi_archivioBulk extends Incarichi_archivioBase {
	public static final java.util.Dictionary tipo_archivioKeys = new it.cnr.jada.util.OrderedHashtable();
	public static final java.util.Dictionary statoKeys = new it.cnr.jada.util.OrderedHashtable();

	private java.lang.String blob;
	private File file;
 
	final public static String TIPO_BANDO = "B";
	final public static String TIPO_DA_PUBBLICARE = "P";
	final public static String TIPO_GENERICO = "G";
	final public static String TIPO_CONTRATTO = "C";
	final public static String TIPO_ALLEGATO_CONTRATTO = "A";
	final public static String TIPO_DECISIONE_A_CONTRATTARE = "D";
	final public static String TIPO_DECRETO_DI_NOMINA = "N";
	final public static String TIPO_ATTO_ESITO_CONTROLLO = "E";	

	final public static String DEFAULT_NOME_FILE = "Allegato";	
	final public static String DEFAULT_ESTENSIONE = "xls";	
	
	final public static String STATO_PROVVISORIO = "P";
	final public static String STATO_DEFINITIVO = "D";
	final public static String STATO_VALIDO = "V";
	final public static String STATO_ANNULLATO = "A";

	static {
		statoKeys.put(STATO_PROVVISORIO,"Provvisorio");
		statoKeys.put(STATO_DEFINITIVO,"Definitivo");
		statoKeys.put(STATO_VALIDO,"Valido");
		statoKeys.put(STATO_ANNULLATO,"Annullato");
	}

	public Incarichi_archivioBulk() {
		super();
	}
	public java.lang.String getBlob () {
		return blob;
	}
	public void setBlob(java.lang.String blob)  {
		this.blob=blob;
	}
	public static java.util.Dictionary getTipo_archivioKeys() {
		return tipo_archivioKeys;
	}
	public String getNomeFile() {
		if (getDacr() != null || getTipo_archivio() != null) {
		  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("ddMMyyyy");
		  return getTipo_archivioKeys().get(getTipo_archivio()) + " " + sdf.format(getDacr()) + "." + DEFAULT_ESTENSIONE; 
		}
		else
		  return DEFAULT_NOME_FILE + "." + DEFAULT_ESTENSIONE; 
	}

	public Timestamp getData_creazione() {
		return getDacr();
	}

	/**
	 * Metodo che elimina il path dal nome del file
	 *
	 * @return Il nome del file senza path
	 * 
	 * @param Il nome completo di path
	 */
	public static String parseFilename(String file) {
		StringTokenizer fileName = new StringTokenizer(file,"\\",false);
		String newFileName = null;
		
		while (fileName.hasMoreTokens()){
			newFileName = fileName.nextToken();   	
		}

		if (newFileName != null)
			return newFileName;
		
		return file;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public boolean isROAttivaFileBlob() {
		return this == null;
	}
	public boolean isBando() {
		return isAllegatoValido() && getTipo_archivio() != null && getTipo_archivio().equals(TIPO_BANDO);
	}
	public boolean isAllegatoDaPubblicare() {
		return isAllegatoValido() && getTipo_archivio() != null && getTipo_archivio().equals(TIPO_DA_PUBBLICARE);
	}
	public boolean isAllegatoGenerico() {
		return isAllegatoValido() && getTipo_archivio() != null && getTipo_archivio().equals(TIPO_GENERICO);
	}
	public boolean isContratto() {
		return isAllegatoValido() && getTipo_archivio() != null && getTipo_archivio().equals(TIPO_CONTRATTO);
	}
	public boolean isDecisioneAContrattare() {
		return isAllegatoValido() && getTipo_archivio() != null && getTipo_archivio().equals(TIPO_DECISIONE_A_CONTRATTARE);
	}
	public boolean isDecretoDiNomina() {
		return isAllegatoValido() && getTipo_archivio() != null && getTipo_archivio().equals(TIPO_DECRETO_DI_NOMINA);
	}
	public boolean isAttoEsitoControllo() {
		return isAllegatoValido() && getTipo_archivio() != null && getTipo_archivio().equals(TIPO_ATTO_ESITO_CONTROLLO);
	}
	public abstract String getDownloadUrl();
	public void insertingUsing(Persister persister, UserContext userContext) {
		if (getStato()==null)
			setStato(STATO_VALIDO);
		super.insertingUsing(persister, userContext);
	}
	public boolean isAllegatoValido() {
		return isToBeCreated() || isValido() || isProvvisorio() || isDefinitivo();
	}
	public Integer getFaseProcesso() {
		return null;
	}
	public boolean isFileRequired(){
		return true;
	}
	public boolean isValido(){
		return getStato()!=null && getStato().equals(STATO_VALIDO);
	}
	public boolean isProvvisorio(){
		return getStato()!=null && getStato().equals(STATO_PROVVISORIO);
	}
	public boolean isDefinitivo(){
		return getStato()!=null && getStato().equals(STATO_DEFINITIVO);
	}
	public boolean isAnnullato(){
		return getStato()!=null && getStato().equals(STATO_ANNULLATO);
	}
	
}