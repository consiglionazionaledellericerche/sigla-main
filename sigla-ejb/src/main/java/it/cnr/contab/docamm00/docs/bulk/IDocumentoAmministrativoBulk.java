package it.cnr.contab.docamm00.docs.bulk;

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
public String getManagerName();
/**
 * Insert the method's description here.
 * Creation date: (12/17/2001 12:55:46 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable
 */
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
