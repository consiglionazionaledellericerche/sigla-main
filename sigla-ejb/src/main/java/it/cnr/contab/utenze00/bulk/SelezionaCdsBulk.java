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

package it.cnr.contab.utenze00.bulk;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;

public class SelezionaCdsBulk extends it.cnr.jada.bulk.OggettoBulk {

	private CdsBulk cds;

	private Unita_organizzativaBulk uo;

	private CdrBulk cdr;

public SelezionaCdsBulk() {
	super();
}

public CdrBulk getCdr() {
	return cdr;
}

public void setCdr(CdrBulk cdr) {
	this.cdr = cdr;
}

public CdsBulk getCds() {
	return cds;
}

public void setCds(CdsBulk cds) {
	this.cds = cds;
}

public Unita_organizzativaBulk getUo() {
	return uo;
}

public void setUo(Unita_organizzativaBulk uo) {
	this.uo = uo;
}
}
