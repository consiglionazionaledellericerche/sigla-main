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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

import it.cnr.contab.progettiric00.enumeration.AllegatoProgettoRimodulazioneType;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoTypeBulk;
import it.cnr.jada.bulk.ValidationException;

public class AllegatoProgettoRimodulazioneBulk extends AllegatoGenericoTypeBulk {
	private static final long serialVersionUID = 1L;

	private Progetto_rimodulazioneBulk rimodulazione;

    public final static Map<String,String> ti_allegatoKeys = Arrays.asList(AllegatoProgettoRimodulazioneType.values())
            .stream()
            .collect(Collectors.toMap(
            		AllegatoProgettoRimodulazioneType::value,
            		AllegatoProgettoRimodulazioneType::label,
                    (oldValue, newValue) -> oldValue,
                    Hashtable::new
            ));

	public final static Map<String,String> ti_allegato_insertKeys = Arrays.asList(AllegatoProgettoRimodulazioneType.values())
			.stream()
			.filter(el->!el.value().equals(AllegatoProgettoRimodulazioneType.AUTOMATICO.value()))
			.collect(Collectors.toMap(
					AllegatoProgettoRimodulazioneType::value,
					AllegatoProgettoRimodulazioneType::label,
					(oldValue, newValue) -> oldValue,
					Hashtable::new
			));

    public AllegatoProgettoRimodulazioneBulk() {
		super();
	}

	public AllegatoProgettoRimodulazioneBulk(String storageKey) {
		super(storageKey);
	}

	public boolean isTypeEnabled(){
		return !isToBeCreated();
	}
	
	public boolean isRimodulazione() {
		return Optional.ofNullable(getObjectType()).map(el->el.equals(AllegatoProgettoRimodulazioneType.RIMODULAZIONE.value())).orElse(Boolean.FALSE);
	}
	
	public boolean isProroga() {
		return Optional.ofNullable(getObjectType()).map(el->el.equals(AllegatoProgettoRimodulazioneType.PROROGA.value())).orElse(Boolean.FALSE);
	}

	public boolean isStampaAutomatica() {
		return Optional.ofNullable(getObjectType()).map(el->el.equals(AllegatoProgettoRimodulazioneType.AUTOMATICO.value())).orElse(Boolean.FALSE);
	}

	public boolean isGenerico() {
		return Optional.ofNullable(getObjectType()).map(el->el.equals(AllegatoProgettoRimodulazioneType.GENERICO.value())).orElse(Boolean.FALSE);
	}
	
	public Progetto_rimodulazioneBulk getRimodulazione() {
		return rimodulazione;
	}
	
	public void setRimodulazione(Progetto_rimodulazioneBulk rimodulazione) {
		this.rimodulazione = rimodulazione;
	}
	
	@Override
	public String parseFilename(String file) {
		return null;
	}

	public String constructNomeFile() {
		StringJoiner name = new StringJoiner("-");
		Optional.ofNullable(this.getRimodulazione())
				.flatMap(el->Optional.ofNullable(el.getProgetto()))
				.flatMap(el->Optional.ofNullable(el.getPg_progetto()))
				.ifPresent(el->name.add("PRG" + el));
		Optional.ofNullable(this.getRimodulazione()).flatMap(el->Optional.ofNullable(el.getPg_rimodulazione()))
				.map(el->StringUtils.leftPad(el.toString(), 3, "0"))
				.ifPresent(el->name.add("RIM" + el));
		if (this.isRimodulazione())	
			name.add("ATT");
		if (this.isProroga())	
			name.add("PRG");
		if (this.isStampaAutomatica())	
			name.add("AUT");
		if (this.isGenerico()) {
			name.add("GEN");

		name.add(
			Optional.ofNullable(this.getNome())
					.filter(el->!el.isEmpty())
					.filter(el->el.indexOf("GEN")>=0)
					.map(el->el.substring(el.length() - 3, el.length()))
					.orElseGet(()->{
						return Optional.ofNullable(this.getRimodulazione())
						    .flatMap(el->Optional.ofNullable(el.getArchivioAllegati()))
							.map(el->el.stream())
							.orElse(Stream.empty())
							.filter(AllegatoProgettoRimodulazioneBulk.class::isInstance)
							.map(AllegatoProgettoRimodulazioneBulk.class::cast)
							.filter(AllegatoProgettoRimodulazioneBulk::isGenerico)
							.filter(el->Optional.ofNullable(el.getNome()).isPresent())
							.map(AllegatoProgettoRimodulazioneBulk::getNome)
							.map(el->el.substring(el.length() - 3, el.length()))
							.filter(el->{
								try {
									Integer.valueOf(el);
									return true;
								}catch (NumberFormatException e){
									return false;
								}
							})
							.map(Integer::valueOf)
							.max(Comparator.comparing(Integer::valueOf))
							.map(el->el+1)
							.map(el->StringUtils.leftPad(el.toString(), 3, "0"))
							.orElse("001");
					}));
		}
		return name.toString();
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();
		if (getObjectType() == null)
			throw new ValidationException("Attenzione: selezionare il tipo di File da caricare.");
		else if (this.isToBeCreated() || this.getNome()==null || this.getNome().isEmpty())
			this.setNome(this.constructNomeFile());
	}
}