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

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2016
 */
package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.si.spring.storage.StorageObject;

import java.util.Dictionary;

public class VDocammElettroniciAttiviBulk extends VDocammElettroniciAttiviBase {
	public final static String DA_PREDISPORRE_ALLA_FIRMA = "P";
	public final static String DA_FIRMARE = "F";
	public final static String FIRMATI = "S";
	
	public final static String FATT_ELETT_ALLA_FIRMA = "FIR";
	public final static String FATT_ELETT_PREDISPOSTA_FIRMA = "PRE";
	public final static String FATT_ELETT_INVIATA_SDI = "INV";
	public final static String FATT_ELETT_SCARTATA_DA_SDI = "SCA";
	public final static String FATT_ELETT_CONSEGNATA_SDI = "COS";
	public final static String FATT_ELETT_AVVISO_NOTIFICA_INVIO_MAIL = "AVV";
	public final static String FATT_ELETT_MANCATA_CONSEGNA = "MAC";
	public final static String FATT_ELETT_NON_RECAPITABILE = "NRE";
	public final static String FATT_ELETT_CONSEGNATA_DESTINATARIO = "CON";
	public final static String FATT_ELETT_ACCETTATA_DESTINATARIO = "ACC";
	public final static String FATT_ELETT_RIFIUTATA_DESTINATARIO = "RIF";
	public final static String FATT_ELETT_DECORRENZA_TERMINI = "DEC";
	public final static String FATT_ELETT_FIRMATA_NC = "FIN";

	private String statoFattElett;
	private StorageObject storageObject;

	public final static Dictionary statoFattureElettronicheKeys;
	public final static Dictionary statoInvioSdiKeys;

	public final static Dictionary tipoDocammKeys;

	static {
		statoFattureElettronicheKeys = new it.cnr.jada.util.OrderedHashtable();
		statoFattureElettronicheKeys.put(DA_FIRMARE, "Da Firmare");
		statoFattureElettronicheKeys.put(DA_PREDISPORRE_ALLA_FIRMA, "Da Predisporre alla firma");
		statoFattureElettronicheKeys.put(FIRMATI, "Firmati");

		statoInvioSdiKeys = new it.cnr.jada.util.OrderedHashtable();
		statoInvioSdiKeys.put(FATT_ELETT_INVIATA_SDI, "Inviata a SDI");
		statoInvioSdiKeys.put(FATT_ELETT_ALLA_FIRMA, "Alla Firma");
		statoInvioSdiKeys.put(FATT_ELETT_PREDISPOSTA_FIRMA, "Predisposta alla Firma");
		statoInvioSdiKeys.put(FATT_ELETT_SCARTATA_DA_SDI, "Scartata da SDI");
		statoInvioSdiKeys.put(FATT_ELETT_CONSEGNATA_SDI, "Consegnata SDI");
		statoInvioSdiKeys.put(FATT_ELETT_AVVISO_NOTIFICA_INVIO_MAIL, "Consegnata a SDI e Inviato Avviso notifica e-mail");
		statoInvioSdiKeys.put(FATT_ELETT_MANCATA_CONSEGNA, "Mancata consegna");
		statoInvioSdiKeys.put(FATT_ELETT_NON_RECAPITABILE, "Non recapitabile");
		statoInvioSdiKeys.put(FATT_ELETT_CONSEGNATA_DESTINATARIO, "Consegnata al destinatario");
		statoInvioSdiKeys.put(FATT_ELETT_ACCETTATA_DESTINATARIO, "Accettata dal destinatario");
		statoInvioSdiKeys.put(FATT_ELETT_RIFIUTATA_DESTINATARIO, "Rifiutata dal destinatario");
		statoInvioSdiKeys.put(FATT_ELETT_DECORRENZA_TERMINI, "Decorrenza termini accettazione/rifiuto da parte del destinatario");
		statoInvioSdiKeys.put(FATT_ELETT_FIRMATA_NC, "Firmata(solo per le note di credito)");

		tipoDocammKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoDocammKeys.put(Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA, "Fattura Attiva");
		tipoDocammKeys.put(Numerazione_doc_ammBulk.TIPO_AUTOFATTURA, "Autofattura");
	}

	private String collegamentoDocumentale;

	public VDocammElettroniciAttiviBulk() {
		super();
	}

	public VDocammElettroniciAttiviBulk(String tipoDocamm, String cd_cds, String cd_unita_organizzativa, Integer esercizio, Long pg_docamm) {
		super(tipoDocamm,cd_cds,cd_unita_organizzativa,esercizio,pg_docamm);
	}

	public String getStatoFattElett() {
		return statoFattElett;
	}

	public void setStatoFattElett(String statoFattElett) {
		this.statoFattElett = statoFattElett;
	}

	public StorageObject getStorageObject() {
		return storageObject;
	}

	public void setStorageObject(StorageObject storageObject) {
		this.storageObject = storageObject;
	}

	public Dictionary getStatoFattureElettronicheKeys() {
		return statoFattureElettronicheKeys;
	}

	public static Dictionary getStatoInvioSdiKeys() {
		return statoInvioSdiKeys;
	}

	public static Dictionary getTipoDocammKeys() {
		return tipoDocammKeys;
	}

	public String getCollegamentoDocumentale() {
		return collegamentoDocumentale;
	}

	public void setCollegamentoDocumentale(String collegamentoDocumentale) {
		this.collegamentoDocumentale = collegamentoDocumentale;
	}
	public boolean isAutofattura() {
		return Numerazione_doc_ammBulk.TIPO_AUTOFATTURA.equals(this.getTipoDocamm());
	}

	public boolean isFatturaAttiva() {
		return Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA.equals(this.getTipoDocamm());
	}
}