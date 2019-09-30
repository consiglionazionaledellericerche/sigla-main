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

public class MandatoAccreditamento_terzoBulk extends Mandato_terzoBulk {
	protected MandatoAccreditamentoBulk mandatoAccreditamento;

	
public MandatoAccreditamento_terzoBulk() {
	super();
}
public MandatoAccreditamento_terzoBulk( MandatoBulk mandato, Mandato_terzoBulk terzo)
{
	super( mandato, terzo );
}
public MandatoAccreditamento_terzoBulk(String cd_cds, Integer cd_terzo, Integer esercizio, Long pg_mandato) {
	super(cd_cds, cd_terzo, esercizio, pg_mandato);
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk
 */
public MandatoBulk getMandato() {
	return mandatoAccreditamento;
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk
 */
public MandatoAccreditamentoBulk getMandatoAccreditamento() {
	return mandatoAccreditamento;
}
/**
 * @param newMandatoI it.cnr.contab.doccont00.core.bulk.MandatoIBulk
 */
public void setMandato(MandatoBulk newMandato) {
	setMandatoAccreditamento( (MandatoAccreditamentoBulk) newMandato);;
}
/**
 * @param newMandatoAccreditamento it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk
 */
public void setMandatoAccreditamento(MandatoAccreditamentoBulk newMandatoAccreditamento) {
	mandatoAccreditamento = newMandatoAccreditamento;
}
}
