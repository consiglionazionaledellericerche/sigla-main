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

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.coepcoan00.core.bulk.IDocumentoCogeBulk;

/**
 * Insert the type's description here.
 * Creation date: (10/07/2002 12.22.24)
 * @author: Simonetta Costa
 */
public interface IManRevBulk extends IDocumentoCogeBulk {
    String getCd_cds();

    String getCd_tipo_documento_cont();

    Integer getEsercizio();

    Long getPg_manrev();

    Long getPg_documento_cont();

    TerzoBulk getTerzo();

    boolean isAnnullato();
}
