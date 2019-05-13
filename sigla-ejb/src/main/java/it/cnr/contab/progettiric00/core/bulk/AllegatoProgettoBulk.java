package it.cnr.contab.progettiric00.core.bulk;

import java.util.Optional;
import java.util.StringJoiner;

import it.cnr.contab.util00.bulk.storage.AllegatoGenericoTypeBulk;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

public class AllegatoProgettoBulk extends AllegatoGenericoTypeBulk {
	private static final long serialVersionUID = 1L;
	private ProgettoBulk progetto;

	private static final java.util.Dictionary ti_allegatoKeys =  new it.cnr.jada.util.OrderedHashtable();

	final public static String PROVV_COSTITUZIONE = "D:sigla_progetti_attachment:provvedimento_costituzione";
	final public static String RICHIESTA_ANTICIPO = "D:sigla_progetti_attachment:richiesta_anticipo";
	final public static String RENDICONTAZIONE = "D:sigla_progetti_attachment:rendicontazione";
	final public static String STRALCIO = "D:sigla_progetti_attachment:stralcio";
	final public static String CONTRODEDUZIONE = "D:sigla_progetti_attachment:controdeduzioni";
	final public static String FINAL_STATEMENT_PAYMENT = "D:sigla_progetti_attachment:final_statement_payment";
	final public static String GENERICO = "D:sigla_progetti_attachment:allegato_generico";

	static {
		ti_allegatoKeys.put(PROVV_COSTITUZIONE,"Provvedimento di Costituzione");
		ti_allegatoKeys.put(RICHIESTA_ANTICIPO,"Richiesta di Anticipo");
		ti_allegatoKeys.put(RENDICONTAZIONE,"Rendicontazione");
		ti_allegatoKeys.put(STRALCIO,"Stralcio");
		ti_allegatoKeys.put(CONTRODEDUZIONE,"Controdeduzione");
		ti_allegatoKeys.put(FINAL_STATEMENT_PAYMENT,"Final Statement Payment");
		ti_allegatoKeys.put(GENERICO,"Allegato Generico");
	}

	public final java.util.Dictionary getTi_allegatoKeys() {
		return ti_allegatoKeys;
	}
	
	public AllegatoProgettoBulk() {
		super();
	}

	public static AllegatoProgettoBulk construct(StorageObject storageObject) {
		return new AllegatoProgettoBulk(storageObject);
	}

	public AllegatoProgettoBulk(String storageKey) {
		super(storageKey);
	}

	public AllegatoProgettoBulk(StorageObject storageObject){
		super();
		setContentType(storageObject.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()));
	}

	public boolean isTypeEnabled(){
		return !isToBeCreated();
	}
	
	public boolean isProvvedimentoCostituzione() {
		return Optional.ofNullable(getContentType()).map(el->el.equals(PROVV_COSTITUZIONE)).orElse(Boolean.FALSE);
	}

	public boolean isRichiestaAnticipo() {
		return Optional.ofNullable(getContentType()).map(el->el.equals(RICHIESTA_ANTICIPO)).orElse(Boolean.FALSE);
	}

	public boolean isRendicontazione() {
		return Optional.ofNullable(getContentType()).map(el->el.equals(RENDICONTAZIONE)).orElse(Boolean.FALSE);
	}

	public boolean isStralcio() {
		return Optional.ofNullable(getContentType()).map(el->el.equals(STRALCIO)).orElse(Boolean.FALSE);
	}

	public boolean isControdeduzione() {
		return Optional.ofNullable(getContentType()).map(el->el.equals(CONTRODEDUZIONE)).orElse(Boolean.FALSE);
	}
	
	public boolean isFinalStatementPayment() {
		return Optional.ofNullable(getContentType()).map(el->el.equals(FINAL_STATEMENT_PAYMENT)).orElse(Boolean.FALSE);
	}
	
	public boolean isGenerico() {
		return Optional.ofNullable(getContentType()).map(el->el.equals(GENERICO)).orElse(Boolean.FALSE);
	}
	
	public ProgettoBulk getProgetto() {
		return progetto;
	}
	
	public void setProgetto(ProgettoBulk progetto) {
		this.progetto = progetto;
	}
	
	@Override
	public String parseFilename(String file) {
		StringJoiner name = new StringJoiner("-");
		Optional.ofNullable(this.getProgetto()).flatMap(el->Optional.ofNullable(el.getPg_progetto()))
				.ifPresent(el->name.add("PRG" + el));
		if (this.isProvvedimentoCostituzione())	
			name.add("COST");
		if (this.isRichiestaAnticipo())	
			name.add("ANT");
		if (this.isRendicontazione())	
			name.add("REND");
		if (this.isStralcio())	
			name.add("STRC");
		if (this.isControdeduzione())	
			name.add("CTRD");
		if (this.isFinalStatementPayment())	
			name.add("FSP");
		if (this.isGenerico())	
			name.add("GEN");
		name.add(super.parseFilename(file));
		return name.toString();
	}
}