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

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cnr.contab.coepcoan00.core.bulk.IDocumentoCogeBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;

/**
 * Insert the type's description here.
 * Creation date: (12/13/2001 4:02:04 PM)
 *
 * @author: Roberto Peli
 */
public interface IDocumentoAmministrativoBulk extends IDocumentoCogeBulk {

    String NON_RIPORTATO = "N";
    String PARZIALMENTE_RIPORTATO = "Q";
    String COMPLETAMENTE_RIPORTATO = "R";

    String RIPORTA_AVANTI = "RIPORTA_AVANTI";
    String RIPORTA_INDIETRO = "RIPORTA_INDIETRO";

    String LIQ = "LIQ";
    String SOSP = "SOSP";
    String NOLIQ = "NOLIQ";

    String ATTLIQ = "ATTLIQ";
    String CONT = "CONT";
    String ATTNC = "ATTNC";
    String NCRED = "NCRED";


    void addToDettagliCancellati(IDocumentoAmministrativoRigaBulk dettaglio);

    void addToDocumentiContabiliCancellati(IScadenzaDocumentoContabileBulk dettaglio);

    /**
     * Insert the method's description here.
     * Creation date: (12/17/2001 12:55:46 PM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
	AccertamentiTable getAccertamentiHash();

    /**
     * Insert the method's description here.
     * Creation date: (4/23/2002 3:33:58 PM)
     *
     * @return java.lang.Integer
     */
    String getCd_cds();

    /**
     * Insert the method's description here.
     * Creation date: (4/23/2002 3:33:58 PM)
     *
     * @return java.lang.Integer
     */
    String getCd_tipo_doc_amm();

    /**
     * Insert the method's description here.
     * Creation date: (4/23/2002 3:33:58 PM)
     *
     * @return java.lang.Integer
     */
    String getCd_uo();

    /**
     * Insert the method's description here.
     * Creation date: (12/17/2001 12:55:46 PM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
    @JsonIgnore
	Class getChildClass();

    /**
     * Insert the method's description here.
     * Creation date: (12/17/2001 12:55:46 PM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
	java.util.List getChildren();

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @return java.util.Vector
     */
	java.util.Vector getDettagliCancellati();

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:28:51 PM)
     *
     * @param newDettagliCancellati java.util.Vector
     */
	void setDettagliCancellati(java.util.Vector newDettagliCancellati);

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:31:04 PM)
     *
     * @return java.util.Vector
     */
	java.util.Vector getDocumentiContabiliCancellati();

    /**
     * Insert the method's description here.
     * Creation date: (2/15/2002 2:31:04 PM)
     *
     * @param newDocumentiContabiliCancellati java.util.Vector
     */
	void setDocumentiContabiliCancellati(java.util.Vector newDocumentiContabiliCancellati);

    Class getDocumentoAmministrativoClassForDelete();

    Class getDocumentoContabileClassForDelete();

    /**
     * Insert the method's description here.
     * Creation date: (4/23/2002 3:33:58 PM)
     *
     * @return java.lang.Integer
     */
    Integer getEsercizio();

    java.math.BigDecimal getImportoSignForDelete(java.math.BigDecimal importo);

    /**
     * Insert the method's description here.
     * Creation date: (12/17/2001 12:55:46 PM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
    @JsonIgnore
	String getManagerName();

    /**
     * Insert the method's description here.
     * Creation date: (12/17/2001 12:55:46 PM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
    @JsonIgnore
	String getManagerOptions();

    /**
     * Insert the method's description here.
     * Creation date: (12/17/2001 12:55:46 PM)
     *
     * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
     */
	ObbligazioniTable getObbligazioniHash();

    /**
     * Insert the method's description here.
     * Creation date: (4/23/2002 3:33:58 PM)
     *
     * @return java.lang.Integer
     */
    Long getPg_doc_amm();

    java.lang.String getRiportata();

    java.lang.String getRiportataInScrivania();

    boolean isDeleting();

    boolean isEditable();

    boolean isRiportata();

    boolean isRiportataInScrivania();

    int removeFromDettagliCancellati(IDocumentoAmministrativoRigaBulk dettaglio);

    int removeFromDocumentiContabiliCancellati(IScadenzaDocumentoContabileBulk dettaglio);

    void setIsDeleting(boolean deletingStatus);

    TipoDocumentoEnum getTipoDocumentoEnum();
}
