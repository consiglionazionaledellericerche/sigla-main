package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Voce_f_res_presBase extends Voce_f_res_presKey implements Keyed {
	// IM_RESIDUI_PRESUNTI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_residui_presunti;

public Voce_f_res_presBase() {
	super();
}
public Voce_f_res_presBase(java.lang.String cd_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_voce,esercizio,ti_appartenenza,ti_gestione);
}
/* 
 * Getter dell'attributo im_residui_presunti
 */
public java.math.BigDecimal getIm_residui_presunti() {
	return im_residui_presunti;
}
/* 
 * Setter dell'attributo im_residui_presunti
 */
public void setIm_residui_presunti(java.math.BigDecimal im_residui_presunti) {
	this.im_residui_presunti = im_residui_presunti;
}
}
