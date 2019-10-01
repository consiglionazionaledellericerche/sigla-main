package it.cnr.contab.progettiric00.core.bulk;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import it.cnr.contab.progettiric00.enumeration.AllegatoProgettoType;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoTypeBulk;
import it.cnr.jada.bulk.ValidationException;

public class AllegatoProgettoBulk extends AllegatoGenericoTypeBulk {
	private static final long serialVersionUID = 1L;
	private ProgettoBulk progetto;

    public final static Map<String,String> ti_allegatoKeys = Arrays.asList(AllegatoProgettoType.values())
            .stream()
            .collect(Collectors.toMap(
            		AllegatoProgettoType::value,
            		AllegatoProgettoType::label,
                    (oldValue, newValue) -> oldValue,
                    Hashtable::new
            ));

	public AllegatoProgettoBulk() {
		super();
	}

	public AllegatoProgettoBulk(String storageKey) {
		super(storageKey);
	}

	public boolean isTypeEnabled(){
		return !isToBeCreated();
	}
	
	public boolean isProvvedimentoCostituzione() {
		return Optional.ofNullable(getObjectType()).map(el->el.equals(AllegatoProgettoType.PROVV_COSTITUZIONE.value())).orElse(Boolean.FALSE);
	}

	public boolean isRichiestaAnticipo() {
		return Optional.ofNullable(getObjectType()).map(el->el.equals(AllegatoProgettoType.RICHIESTA_ANTICIPO.value())).orElse(Boolean.FALSE);
	}

	public boolean isRendicontazione() {
		return Optional.ofNullable(getObjectType()).map(el->el.equals(AllegatoProgettoType.RENDICONTAZIONE.value())).orElse(Boolean.FALSE);
	}

	public boolean isStralcio() {
		return Optional.ofNullable(getObjectType()).map(el->el.equals(AllegatoProgettoType.STRALCIO.value())).orElse(Boolean.FALSE);
	}

	public boolean isControdeduzione() {
		return Optional.ofNullable(getObjectType()).map(el->el.equals(AllegatoProgettoType.CONTRODEDUZIONE.value())).orElse(Boolean.FALSE);
	}
	
	public boolean isFinalStatementPayment() {
		return Optional.ofNullable(getObjectType()).map(el->el.equals(AllegatoProgettoType.FINAL_STATEMENT_PAYMENT.value())).orElse(Boolean.FALSE);
	}
	
	public boolean isGenerico() {
		return Optional.ofNullable(getObjectType()).map(el->el.equals(AllegatoProgettoType.GENERICO.value())).orElse(Boolean.FALSE);
	}
	
	public ProgettoBulk getProgetto() {
		return progetto;
	}
	
	public void setProgetto(ProgettoBulk progetto) {
		this.progetto = progetto;
	}
	
	@Override
	public String parseFilename(String file) {
		return null;
	}
	
	@Override
	public void validate() throws ValidationException {
		super.validate();
		if (getObjectType() == null)
			throw new ValidationException("Attenzione: selezionare il tipo di File da caricare.");
		else if (this.isToBeCreated() || this.getNome()==null || this.getNome().isEmpty()) {
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
			this.setNome(name.toString());
		}
	}
}