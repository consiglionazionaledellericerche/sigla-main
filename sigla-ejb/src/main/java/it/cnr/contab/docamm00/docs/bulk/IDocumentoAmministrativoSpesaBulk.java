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

package it.cnr.contab.docamm00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (4/17/2002 5:49:25 PM)
 * @author: Roberto Peli
 */
public interface IDocumentoAmministrativoSpesaBulk extends IDocumentoAmministrativoBulk {
/**
 * Insert the method's description here.
 * Creation date: (5/8/2002 10:45:21 AM)
 * @return java.lang.String
 */
String getDescrizione_spesa();
/* 
 * Getter dell'attributo dt_a_competenza_coge
 */
public java.sql.Timestamp getDt_a_competenza_coge();
/* 
 * Getter dell'attributo dt_da_competenza_coge
 */
public java.sql.Timestamp getDt_da_competenza_coge();
/**
 * Insert the method's description here.
 * Creation date: (5/8/2002 10:42:33 AM)
 * @return java.math.BigDecimal
 */
java.math.BigDecimal getImporto_netto_spesa();
/**
 * Insert the method's description here.
 * Creation date: (5/8/2002 10:42:33 AM)
 * @return java.math.BigDecimal
 */
java.math.BigDecimal getImporto_spesa();
/**
 * Insert the method's description here.
 * Creation date: (5/8/2002 10:42:04 AM)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo_spesa();
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:33:58 PM)
 * @return java.lang.Integer
 */
void setCd_cds(String newCd_cds);
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:33:58 PM)
 * @return java.lang.Integer
 */
void setCd_tipo_doc_amm(String newCd_tipo_doc_amm);
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:33:58 PM)
 * @return java.lang.Integer
 */
void setCd_uo(String newCd_uo);
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:33:58 PM)
 * @return java.lang.Integer
 */
void setEsercizio(Integer newEsercizio);
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:33:58 PM)
 * @return java.lang.Integer
 */
void setPg_doc_amm(Long newPg);

}
