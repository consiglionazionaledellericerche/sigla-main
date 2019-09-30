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
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;

public class TipoOperazioneOrdBulk extends TipoOperazioneOrdBase {
    public final static String OPERAZIONE_VALIDAZIONE_RICHIESTA= "VR1";
    public final static String OPERAZIONE_RICHIESTA= "RIC";
    public final static String EVASIONE_ORDINE= "RIS";
    public final static String OPERAZIONE_ORDINE= "ORD";
    public final static String OPERAZIONE_MAGAZZINO= "MAG";
    public final static String OPERAZIONE_APPROVAZIONE_ORDINE= "VO1";
    public final static String OPERAZIONE_FIRMA_ORDINE= "VO2";
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_OPERAZIONE_ORD
	 **/
	public TipoOperazioneOrdBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_OPERAZIONE_ORD
	 **/
	public TipoOperazioneOrdBulk(java.lang.String cdTipoOperazione) {
		super(cdTipoOperazione);
	}
}