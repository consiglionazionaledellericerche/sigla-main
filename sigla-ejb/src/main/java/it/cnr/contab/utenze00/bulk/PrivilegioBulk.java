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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/03/2008
 */
package it.cnr.contab.utenze00.bulk;
public class PrivilegioBulk extends PrivilegioBase {
	final public static String ABILITA_FIRMA_FATTURA_ELETTRONICA = "FIFAEL";
	final public static String ABILITA_CANCELLAZIONE_MISSIONE_GEMIS = "CANCMI";
	final public static String ABILITA_APPROVA_BILANCIO = "APPBIL";
	final public static String ABILITA_AGGIORNA_INVENTARIO = "AGGINV";
	final public static String ABILITA_INVENTARIO_UFFICIALE = "INVUFF";
	final public static String ABILITA_GESTIONE_ISTAT_SIOPE = "INSSIO";
	final public static String ABILITA_SUPERVISORE = "SUPVIS";
	final public static String ABILITA_ELENCO_CF = "ELENCF";
	final public static String ABILITA_F24EP = "INSF24";
	final public static String ABILITA_FATTURA_ATTIVA = "FATATT";
	final public static String ABILITA_PUBBLICAZIONE_SITO = "PUBSIT";
	final public static String ABILITA_FUNZIONI_INCARICHI = "FUNINC";
	final public static String ABILITA_FUNZIONI_DIRETTORE = "DIRIST";
	final public static String ABILITA_FUNZIONI_SUPERUTENTE_INCARICHI = "SUPINC";
	final public static String ABILITA_SOSPCORI = "INSSOS";
	final public static String ABILITA_VARIAZIONI = "MODVAR";
	final public static String ABILITA_ALL_TRATT = "ALLTRA";
	final public static String ABILITA_AUTORIZZA_DIARIA = "DIARIA";
	final public static String ABILITA_REVERSALE_INCASSO = "REVINC";
	final public static String ABILITA_SBLOCCO_IMPEGNO = "SBLIMP";

	public static final String TIPO_RISERVATO_CNR 	= "C";
	public static final String TIPO_PUBBLICO 		= "D";

	public PrivilegioBulk() {
		super();
	}
	public PrivilegioBulk(java.lang.String cd_privilegio) {
		super(cd_privilegio);
	}
}