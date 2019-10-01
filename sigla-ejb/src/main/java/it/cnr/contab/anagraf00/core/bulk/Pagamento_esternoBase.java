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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Pagamento_esternoBase extends Pagamento_esternoKey implements Keyed {

	// DT_PAGAMENTO TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_pagamento;

	// IM_PAGAMENTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_pagamento;

	// IM_SPESE DECIMAL(15,2) NULL
	private java.math.BigDecimal im_spese;

	// DS_PAGAMENTO VARCHAR2(300) NOT NULL
	private java.lang.String ds_pagamento;

	// CD_TIPO_RAPPORTO VARCHAR2(10) NOT NULL
	private java.lang.String cd_tipo_rapporto;

public Pagamento_esternoBase() {
	super();
}
public Pagamento_esternoBase(java.lang.Integer cd_anag,java.lang.Integer pg_pagamento) {
	super(cd_anag,pg_pagamento);
}

	/**
	 * Returns the ds_pagamento.
	 * @return java.lang.String
	 */
	public java.lang.String getDs_pagamento() {
		return ds_pagamento;
	}

	/**
	 * Returns the dt_pagamento.
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getDt_pagamento() {
		return dt_pagamento;
	}

	/**
	 * Returns the im_pagamento.
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getIm_pagamento() {
		return im_pagamento;
	}

	/**
	 * Returns the im_spese.
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getIm_spese() {
		return im_spese;
	}

	/**
	 * Sets the ds_pagamento.
	 * @param ds_pagamento The ds_pagamento to set
	 */
	public void setDs_pagamento(java.lang.String ds_pagamento) {
		this.ds_pagamento = ds_pagamento;
	}

	/**
	 * Sets the dt_pagamento.
	 * @param dt_pagamento The dt_pagamento to set
	 */
	public void setDt_pagamento(java.sql.Timestamp dt_pagamento) {
		this.dt_pagamento = dt_pagamento;
	}

	/**
	 * Sets the im_pagamento.
	 * @param im_pagamento The im_pagamento to set
	 */
	public void setIm_pagamento(java.math.BigDecimal im_pagamento) {
		this.im_pagamento = im_pagamento;
	}

	/**
	 * Sets the im_spese.
	 * @param im_spese The im_spese to set
	 */
	public void setIm_spese(java.math.BigDecimal im_spese) {
		this.im_spese = im_spese;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_tipo_rapporto() {
		return cd_tipo_rapporto;
	}

	/**
	 * @param string
	 */
	public void setCd_tipo_rapporto(java.lang.String string) {
		cd_tipo_rapporto = string;
	}

}
