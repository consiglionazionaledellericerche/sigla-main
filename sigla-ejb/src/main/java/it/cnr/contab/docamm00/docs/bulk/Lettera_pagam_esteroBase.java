package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Lettera_pagam_esteroBase extends Lettera_pagam_esteroKey implements Keyed {
	// CD_SOSPESO VARCHAR(20)
	private java.lang.String cd_sospeso;

	// DT_REGISTRAZIONE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_registrazione;

	// IM_COMMISSIONI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_commissioni;

	// IM_PAGAMENTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_pagamento;

	// TI_ENTRATA_SPESA CHAR(1)
	private java.lang.String ti_entrata_spesa;

	// TI_SOSPESO_RISCONTRO CHAR(1)
	private java.lang.String ti_sospeso_riscontro;

public Lettera_pagam_esteroBase() {
	super();
}
public Lettera_pagam_esteroBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_lettera) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_lettera);
}
/* 
 * Getter dell'attributo cd_sospeso
 */
public java.lang.String getCd_sospeso() {
	return cd_sospeso;
}
/* 
 * Getter dell'attributo dt_registrazione
 */
public java.sql.Timestamp getDt_registrazione() {
	return dt_registrazione;
}
/* 
 * Getter dell'attributo im_commissioni
 */
public java.math.BigDecimal getIm_commissioni() {
	return im_commissioni;
}
/* 
 * Getter dell'attributo im_pagamento
 */
public java.math.BigDecimal getIm_pagamento() {
	return im_pagamento;
}
/* 
 * Getter dell'attributo ti_entrata_spesa
 */
public java.lang.String getTi_entrata_spesa() {
	return ti_entrata_spesa;
}
/* 
 * Getter dell'attributo ti_sospeso_riscontro
 */
public java.lang.String getTi_sospeso_riscontro() {
	return ti_sospeso_riscontro;
}
/* 
 * Setter dell'attributo cd_sospeso
 */
public void setCd_sospeso(java.lang.String cd_sospeso) {
	this.cd_sospeso = cd_sospeso;
}
/* 
 * Setter dell'attributo dt_registrazione
 */
public void setDt_registrazione(java.sql.Timestamp dt_registrazione) {
	this.dt_registrazione = dt_registrazione;
}
/* 
 * Setter dell'attributo im_commissioni
 */
public void setIm_commissioni(java.math.BigDecimal im_commissioni) {
	this.im_commissioni = im_commissioni;
}
/* 
 * Setter dell'attributo im_pagamento
 */
public void setIm_pagamento(java.math.BigDecimal im_pagamento) {
	this.im_pagamento = im_pagamento;
}
/* 
 * Setter dell'attributo ti_entrata_spesa
 */
public void setTi_entrata_spesa(java.lang.String ti_entrata_spesa) {
	this.ti_entrata_spesa = ti_entrata_spesa;
}
/* 
 * Setter dell'attributo ti_sospeso_riscontro
 */
public void setTi_sospeso_riscontro(java.lang.String ti_sospeso_riscontro) {
	this.ti_sospeso_riscontro = ti_sospeso_riscontro;
}
}
