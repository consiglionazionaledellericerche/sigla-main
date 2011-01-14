package it.cnr.contab.docamm00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (12/13/2001 4:02:04 PM)
 * @author: Roberto Peli
 */
public interface IDocumentoAmministrativoRigaBulk {

	public final static String NON_RIPORTATO = "N";
	public final static String RIPORTATO = "R";
/**
 * Insert the method's description here.
 * Creation date: (12/14/2001 2:38:27 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
 */
public IDocumentoAmministrativoRigaBulk getAssociatedDetail();
/**
 * Insert the method's description here.
 * Creation date: (12/14/2001 2:38:27 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
 */
public IDocumentoAmministrativoBulk getFather();
public java.math.BigDecimal getIm_diponibile_nc();
/**
 * Insert the method's description here.
 * Creation date: (12/14/2001 2:38:27 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
 */
public java.math.BigDecimal getIm_imponibile();
/**
 * Insert the method's description here.
 * Creation date: (12/14/2001 2:38:27 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
 */
public java.math.BigDecimal getIm_iva();
/**
 * Insert the method's description here.
 * Creation date: (12/14/2001 2:38:27 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
 */
public IDocumentoAmministrativoRigaBulk getOriginalDetail();
/**
 * Insert the method's description here.
 * Creation date: (12/14/2001 2:38:27 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
 */
public it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk getScadenzaDocumentoContabile();
/**
 * Insert the method's description here.
 * Creation date: (12/14/2001 2:38:27 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
 */
public it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk getVoce_iva();
/**
 * Insert the method's description here.
 * Creation date: (12/14/2001 2:38:27 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
 */
public boolean isDirectlyLinkedToDC();
public boolean isRiportata();
public void setIm_diponibile_nc(java.math.BigDecimal im_diponibile_nc);
}
