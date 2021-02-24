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

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 15/12/2010
 */
package it.cnr.contab.config00.pdcfin.bulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;

import java.math.BigDecimal;
import java.util.Optional;

public class LimiteSpesaClassBulk extends LimiteSpesaClassBase {
	private V_classificazione_vociBulk v_classificazione_voci;
	private CdsBulk cds;
	private java.math.BigDecimal imAssestatoAssunto;

	public LimiteSpesaClassBulk() {
		super();
	}

	public LimiteSpesaClassBulk(Integer id_classificazione, String cdCds) {
		super(id_classificazione, cdCds);
		setV_classificazione_voci( new V_classificazione_vociBulk(id_classificazione) );
		setCds( new CdsBulk(cdCds) );
	}

	public V_classificazione_vociBulk getV_classificazione_voci() {
		return v_classificazione_voci;
	}

	public void setV_classificazione_voci(V_classificazione_vociBulk v_classificazione_voci) {
		this.v_classificazione_voci = v_classificazione_voci;
	}

	@Override
	public Integer getId_classificazione() {
		return Optional.ofNullable(this.getV_classificazione_voci())
				.map(V_classificazione_vociBulk::getId_classificazione)
				.orElse(null);
	}

	@Override
	public void setId_classificazione(Integer id_classificazione) {
		Optional.ofNullable(this.getV_classificazione_voci()).ifPresent(el->el.setId_classificazione(id_classificazione));
	}

	public CdsBulk getCds() {
		return cds;
	}

	public void setCds(CdsBulk cds) {
		this.cds = cds;
	}

	public String getCd_cds() {
		return Optional.ofNullable(this.getCds())
				.map(CdsBulk::getCd_unita_organizzativa)
				.orElse(null);
	}

	public void setCd_cds(String cdCds)  {
		Optional.ofNullable(this.getCds()).ifPresent(el->el.setCd_unita_organizzativa(cdCds));
	}

	@Override
	public OggettoBulk initializeForInsert(CRUDBP crudbp, ActionContext actioncontext) {
		this.setIm_limite_assestato(BigDecimal.ZERO);
		this.setV_classificazione_voci(new V_classificazione_vociBulk());
		return super.initializeForInsert(crudbp, actioncontext);
	}

	public BigDecimal getImAssestatoAssunto() {
		return imAssestatoAssunto;
	}

	public void setImAssestatoAssunto(BigDecimal imAssestatoAssunto) {
		this.imAssestatoAssunto = imAssestatoAssunto;
	}

	public boolean isUtilizzato(){
		return Optional.ofNullable(this.getImAssestatoAssunto()).map(el->el.compareTo(BigDecimal.ZERO)>0).orElse(Boolean.FALSE);
	}
}