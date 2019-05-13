package it.cnr.contab.progettiric00.core.bulk;

import java.util.Optional;
import java.util.StringJoiner;

import it.cnr.contab.util00.bulk.storage.AllegatoGenericoTypeBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.si.spring.storage.StorageObject;

public class AllegatoProgettoRimodulazioneBulk extends AllegatoGenericoTypeBulk {
	private static final long serialVersionUID = 1L;

	private Progetto_rimodulazioneBulk rimodulazione;

	private static final java.util.Dictionary ti_allegatoKeys =  new it.cnr.jada.util.OrderedHashtable();

	final public static String RIMODULAZIONE = "D:sigla_progetti_attachment:rimodulazione_attestato";
	final public static String PROROGA = "D:sigla_progetti_attachment:rimodulazione_proroga";
	final public static String GENERICO = "D:sigla_progetti_attachment:rimodulazione_allegato_generico";

	static {
		ti_allegatoKeys.put(RIMODULAZIONE,"Rimodulazione");
		ti_allegatoKeys.put(PROROGA,"Proroga");
		ti_allegatoKeys.put(GENERICO,"Allegato Generico");
	}

	public final java.util.Dictionary getTi_allegatoKeys() {
		return ti_allegatoKeys;
	}

	public AllegatoProgettoRimodulazioneBulk() {
		super();
	}

	public static AllegatoProgettoRimodulazioneBulk construct(StorageObject storageObject) {
		return new AllegatoProgettoRimodulazioneBulk(storageObject);
	}

	public AllegatoProgettoRimodulazioneBulk(String storageKey) {
		super(storageKey);
	}

	public AllegatoProgettoRimodulazioneBulk(StorageObject storageObject){
		super(storageObject);
	}

	public boolean isTypeEnabled(){
		return !isToBeCreated();
	}
	
	public boolean isRimodulazione() {
		return Optional.ofNullable(getObjectType()).map(el->el.equals(RIMODULAZIONE)).orElse(Boolean.FALSE);
	}
	
	public boolean isProroga() {
		return Optional.ofNullable(getObjectType()).map(el->el.equals(PROROGA)).orElse(Boolean.FALSE);
	}

	public boolean isGenerico() {
		return Optional.ofNullable(getObjectType()).map(el->el.equals(GENERICO)).orElse(Boolean.FALSE);
	}
	
	public Progetto_rimodulazioneBulk getRimodulazione() {
		return rimodulazione;
	}
	
	public void setRimodulazione(Progetto_rimodulazioneBulk rimodulazione) {
		this.rimodulazione = rimodulazione;
	}
	
	@Override
	public String parseFilename(String file) {
		StringJoiner name = new StringJoiner("-");
		Optional.ofNullable(this.getRimodulazione())
				.flatMap(el->Optional.ofNullable(el.getProgetto()))
				.flatMap(el->Optional.ofNullable(el.getPg_progetto()))
				.ifPresent(el->name.add("PRG" + el));
		Optional.ofNullable(this.getRimodulazione()).flatMap(el->Optional.ofNullable(el.getPg_rimodulazione()))
				.ifPresent(el->name.add("RIM" + el));
		if (this.isRimodulazione())	
			name.add("ATT");
		if (this.isProroga())	
			name.add("PRG");
		if (this.isGenerico())	
			name.add("GEN");
		name.add(super.parseFilename(file));
		return name.toString();
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();
		if (getNome() == null ) {
			if (getObjectType() == null)
				throw new ValidationException("Attenzione: selezionare il tipo di File da caricare.");
		}
	}
}