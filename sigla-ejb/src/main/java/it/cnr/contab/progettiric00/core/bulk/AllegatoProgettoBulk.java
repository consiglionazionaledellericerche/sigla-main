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

package it.cnr.contab.progettiric00.core.bulk;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.cnr.contab.progettiric00.enumeration.AllegatoProgettoRimodulazioneType;
import it.cnr.contab.progettiric00.enumeration.AllegatoProgettoType;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoTypeBulk;
import it.cnr.jada.bulk.ValidationException;
import org.apache.commons.lang.StringUtils;

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
	public boolean isValutazioneUtilizzoRisorse() {
		return Optional.ofNullable(getObjectType()).map(el->el.equals(AllegatoProgettoType.VALUTAZIONE_UTILIZZO_RISORSE.value())).orElse(Boolean.FALSE);
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
			if (this.isValutazioneUtilizzoRisorse())
				name.add("VALR");
			if (this.isGenerico()) {
				name.add("GEN");

				name.add(
						Optional.ofNullable(this.getNome())
								.filter(el -> !el.isEmpty())
								.filter(el -> el.indexOf("GEN") >= 0)
								.map(el -> el.substring(el.length() - 3, el.length()))
								.orElseGet(() -> {
									return Optional.ofNullable(this.getProgetto())
											.flatMap(el -> Optional.ofNullable(el.getArchivioAllegati()))
											.map(el -> el.stream())
											.orElse(Stream.empty())
											.filter(AllegatoProgettoBulk.class::isInstance)
											.map(AllegatoProgettoBulk.class::cast)
											.filter(AllegatoProgettoBulk::isGenerico)
											.filter(el -> Optional.ofNullable(el.getNome()).isPresent())
											.map(AllegatoProgettoBulk::getNome)
											.map(el -> el.substring(el.length() - 3, el.length()))
											.filter(el -> {
												try {
													Integer.valueOf(el);
													return true;
												} catch (NumberFormatException e) {
													return false;
												}
											})
											.map(Integer::valueOf)
											.max(Comparator.comparing(Integer::valueOf))
											.map(el -> el + 1)
											.map(el -> StringUtils.leftPad(el.toString(), 3, "0"))
											.orElse("001");
								}));
			}
			this.setNome(name.toString());
		}
	}
}