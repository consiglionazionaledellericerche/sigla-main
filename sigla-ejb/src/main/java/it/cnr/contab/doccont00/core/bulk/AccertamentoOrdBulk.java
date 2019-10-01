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

import it.cnr.jada.bulk.OggettoBulk;

public class AccertamentoOrdBulk extends AccertamentoBulk {
public AccertamentoOrdBulk() 
{
	super();
}
public AccertamentoOrdBulk(String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_accertamento) 
{
	super(cd_cds, esercizio, esercizio_originale, pg_accertamento);
}
/**
 * Inizializza l'Oggetto Bulk.
 * @param bp Il Business Process in uso
 * @param context Il contesto dell'azione
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context)
{
	super.initialize(bp, context);
	
//	setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_ACR );
	setFl_pgiro( new Boolean( false ));

	return this;
}
public boolean isInitialized()
{
	//return getLinee_attivitaColl() != null && getLinee_attivitaColl().size() != 0;
	return getLineeAttivitaColl() != null && getLineeAttivitaColl().size() != 0;
}
}
