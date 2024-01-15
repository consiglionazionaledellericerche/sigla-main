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

package it.cnr.contab.docamm00.fatturapa.bulk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.cnr.contab.docamm00.storage.StorageDocAmmAspect;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.UserContext;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;

public class AllegatoFatturaBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;

	public static OrderedHashtable aspectNamesKeys = new OrderedHashtable(),
			aspectNamesDecorrenzaTerminiKeys;

	public static final String P_SIGLA_FATTURE_ATTACHMENT_DURC = "P:sigla_fatture_attachment:durc";
	public static final String P_SIGLA_FATTURE_ATTACHMENT_TACCIABILITA = "P:sigla_fatture_attachment:tacciabilita";
	public static final String P_SIGLA_FATTURE_ATTACHMENT_PRESTAZIONE_RESA = "P:sigla_fatture_attachment:prestazione_resa";
	public static final String P_SIGLA_FATTURE_ATTACHMENT_ALTRO = "P:sigla_fatture_attachment:altro";

	static {
		aspectNamesKeys.put(P_SIGLA_FATTURE_ATTACHMENT_DURC,"DURC");
		aspectNamesKeys.put(P_SIGLA_FATTURE_ATTACHMENT_TACCIABILITA,"Tracciabilità");
		aspectNamesKeys.put(P_SIGLA_FATTURE_ATTACHMENT_PRESTAZIONE_RESA,"Prestazione Resa");
		aspectNamesKeys.put(P_SIGLA_FATTURE_ATTACHMENT_ALTRO,"Altro");
		aspectNamesKeys.put(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_COMUNICAZIONE_NON_REGISTRABILITA.value(),"Comunicazione di non registrabilità");

		aspectNamesDecorrenzaTerminiKeys = (OrderedHashtable) aspectNamesKeys.clone();
		aspectNamesDecorrenzaTerminiKeys.put(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_COMUNICAZIONE_NON_REGISTRABILITA.value(),"Comunicazione di non registrabilità");
	}
	private String aspectName;
	private Date dataCancellazione;

	public AllegatoFatturaBulk() {
		super();
	}

	public AllegatoFatturaBulk(String storageKey) {
		super(storageKey);
	}

	public String getAspectName() {
		return aspectName;
	}
	public void setAspectName(String aspectName) {
		this.aspectName = aspectName;
	}

	@StorageProperty(name="cmis:secondaryObjectTypeIds")
	public List<String> getAspect() {
		List<String> results = new ArrayList<String>();
		results.add("P:cm:titled");
		results.add(getAspectName());
		return results;
	}
	@StoragePolicy(
			name = "P:sigla_commons_aspect:utente_applicativo_sigla",
			property = @StorageProperty(name = "sigla_commons_aspect:utente_applicativo")
	)
	private String utenteSIGLA;

	public static OrderedHashtable getAspectnameskeys() {
		return aspectNamesKeys;
	}

	public static OrderedHashtable getAspectnamesDecorrenzaTerminikeys() {
		return aspectNamesDecorrenzaTerminiKeys;
	}
	@Override
	public void validate() throws ValidationException {
		if (getAspectName() == null) {
			throw new ValidationException("Attenzione: selezionare la tipologia di File!");
		}
		super.validate();
	}

	public String getUtenteSIGLA() {
		return utenteSIGLA;
	}

	public void setUtenteSIGLA(String utenteSIGLA) {
		this.utenteSIGLA = utenteSIGLA;
	}

	@Override
	public void complete(UserContext userContext) {
		setUtenteSIGLA(CNRUserContext.getUser(userContext));
		super.complete(userContext);
	}
	@StoragePolicy(name="P:sigla_commons_aspect:cancellato_logicamente", property=@StorageProperty(name="sigla_commons_aspect:data_cancellazione"))
	public Date getDataCancellazione() {
		return dataCancellazione;
	}

	public void setDataCancellazione(Date dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}

}