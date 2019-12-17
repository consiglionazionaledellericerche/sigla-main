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
 * Creation date: (3/22/2002 2:48:52 PM)
 * @author: Roberto Peli
 */
public interface IDocumentoAmministrativoGenericoBulk {
    /**
     * Insert the method's description here.
     * Creation date: (3/22/2002 2:52:05 PM)
     * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
     */
    IDocumentoAmministrativoBulk getSpecializedInstance();

    /**
     *
     * @param cd_cds_origine
     */
    void setCd_cds_origine(java.lang.String cd_cds_origine);

    /**
     *
     * @param cd_uo_origine
     */
    void setCd_uo_origine(java.lang.String cd_uo_origine);
}
