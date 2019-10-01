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

public class Ass_obb_acr_pgiroBulk extends Ass_obb_acr_pgiroBase {
	public final static String TIPO_ENTRATA = "E";
	public final static String TIPO_SPESA 	= "S";
	private ImpegnoPGiroBulk impegno;
	private AccertamentoPGiroBulk accertamento;

public Ass_obb_acr_pgiroBulk() {
	super();
}
public Ass_obb_acr_pgiroBulk(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Integer esercizio_ori_accertamento,java.lang.Long pg_accertamento,java.lang.Integer esercizio_ori_obbligazione,java.lang.Long pg_obbligazione) {
	super(cd_cds,esercizio,esercizio_ori_accertamento,pg_accertamento,esercizio_ori_obbligazione,pg_obbligazione);
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk
 */
public AccertamentoPGiroBulk getAccertamento() {
	return accertamento;
}
/**
 * @return it.cnr.contab.doccont00.core.bulk.ImpegnoPGiroBulk
 */
public ImpegnoPGiroBulk getImpegno() {
	return impegno;
}
/**
 * @param newAccertamento it.cnr.contab.doccont00.core.bulk.AccertamentoPGiroBulk
 */
public void setAccertamento(AccertamentoPGiroBulk newAccertamento) {
	accertamento = newAccertamento;
}
/**
 * @param newImpegno it.cnr.contab.doccont00.core.bulk.ImpegnoPGiroBulk
 */
public void setImpegno(ImpegnoPGiroBulk newImpegno) {
	impegno = newImpegno;
}
}
