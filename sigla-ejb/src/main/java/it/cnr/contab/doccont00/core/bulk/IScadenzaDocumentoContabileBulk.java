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

public interface IScadenzaDocumentoContabileBulk {
/* 
 * Setter dell'attributo im_associato_doc_amm
 */
public String getDs_scadenza();
/**
 * @return it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
IDocumentoContabileBulk getFather();
/* 
 * Getter dell'attributo im_associato_doc_amm
 */
public java.math.BigDecimal getIm_associato_doc_amm();
/**
 * @return it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
java.math.BigDecimal getIm_scadenza();
/* 
 * Setter dell'attributo im_associato_doc_amm
 */
public void setIm_associato_doc_amm(java.math.BigDecimal im_associato_doc_amm);
}
