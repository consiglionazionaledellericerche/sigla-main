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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
public class AccertamentoCdsBulk extends AccertamentoBulk {
public AccertamentoCdsBulk() {
	super();
	initialize();
}
public AccertamentoCdsBulk(String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_accertamento) {
	super(cd_cds, esercizio, esercizio_originale, pg_accertamento);
	initialize();	
}
private void initialize () {
	setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_ACR_SIST );
	setFl_pgiro( new Boolean( false ));
	
}
}
