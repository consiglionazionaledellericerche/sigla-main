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
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
/**
 * Insert the type's description here.
 * Creation date: (12/13/2001 4:02:04 PM)
 * @author: Roberto Peli
 */
public interface IDocumentoAmministrativoBulk {

	public final static String NON_RIPORTATO = "N";
	public final static String PARZIALMENTE_RIPORTATO = "Q";
	public final static String COMPLETAMENTE_RIPORTATO = "R";

	public final static String RIPORTA_AVANTI = "RIPORTA_AVANTI";
	public final static String RIPORTA_INDIETRO = "RIPORTA_INDIETRO";
	
	public final static String LIQ = "LIQ";
	public final static String SOSP = "SOSP";
	public final static String NOLIQ = "NOLIQ";
		
	public final static String ATTLIQ = "ATTLIQ";
	public final static String CONT = "CONT";
	public final static String ATTNC = "ATTNC";
	public final static String NCRED= "NCRED";
	
	
public void addToDettagliCancellati(IDocumentoAmministrativoRigaBulk dettaglio);
public void addToDocumentiContabiliCancellati(IScadenzaDocumentoContabileBulk dettaglio);
/**
 * Insert the method's description here.
 * Creation date: (12/17/2001 12:55:46 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
 */
public AccertamentiTable getAccertamentiHash();
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:33:58 PM)
 * @return java.lang.Integer
 */
String getCd_cds();
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:33:58 PM)
 * @return java.lang.Integer
 */
String getCd_tipo_doc_amm();
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:33:58 PM)
 * @return java.lang.Integer
 */
String getCd_uo();
/**
 * Insert the method's description here.
 * Creation date: (12/17/2001 12:55:46 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
 */
@JsonIgnore
public Class getChildClass();
/**
 * Insert the method's description here.
 * Creation date: (12/17/2001 12:55:46 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
 */
public java.util.List getChildren();
/**
 * Insert the method's description here.
 * Creation date: (2/15/2002 2:28:51 PM)
 * @return java.util.Vector
 */
public java.util.Vector getDettagliCancellati();
/**
 * Insert the method's description here.
 * Creation date: (2/15/2002 2:31:04 PM)
 * @return java.util.Vector
 */
public java.util.Vector getDocumentiContabiliCancellati();
public Class getDocumentoAmministrativoClassForDelete();
public Class getDocumentoContabileClassForDelete();
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:33:58 PM)
 * @return java.lang.Integer
 */
Integer getEsercizio();
public java.math.BigDecimal getImportoSignForDelete(java.math.BigDecimal importo);
/**
 * Insert the method's description here.
 * Creation date: (12/17/2001 12:55:46 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
 */
@JsonIgnore
public String getManagerName();
/**
 * Insert the method's description here.
 * Creation date: (12/17/2001 12:55:46 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
 */
@JsonIgnore
public String getManagerOptions();
/**
 * Insert the method's description here.
 * Creation date: (12/17/2001 12:55:46 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
 */
public ObbligazioniTable getObbligazioniHash();
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 3:33:58 PM)
 * @return java.lang.Integer
 */
Long getPg_doc_amm();
public java.lang.String getRiportata();
public java.lang.String getRiportataInScrivania();
public boolean isDeleting();
public boolean isEditable();
public boolean isRiportata();
public boolean isRiportataInScrivania();
public int removeFromDettagliCancellati(IDocumentoAmministrativoRigaBulk dettaglio);
public int removeFromDocumentiContabiliCancellati(IScadenzaDocumentoContabileBulk dettaglio);
/**
 * Insert the method's description here.
 * Creation date: (2/15/2002 2:28:51 PM)
 * @param newDettagliCancellati java.util.Vector
 */
public void setDettagliCancellati(java.util.Vector newDettagliCancellati);
/**
 * Insert the method's description here.
 * Creation date: (2/15/2002 2:31:04 PM)
 * @param newDocumentiContabiliCancellati java.util.Vector
 */
public void setDocumentiContabiliCancellati(java.util.Vector newDocumentiContabiliCancellati);
public void setIsDeleting(boolean deletingStatus);
}
