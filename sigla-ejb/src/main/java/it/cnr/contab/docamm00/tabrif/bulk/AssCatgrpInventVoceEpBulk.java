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

package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.contab.config00.pdcep.bulk.ContoBulk;
import it.cnr.contab.config00.pdcep.bulk.Voce_epBase;
import it.cnr.contab.config00.pdcep.bulk.Voce_epBulk;
import it.cnr.contab.config00.pdcep.cla.bulk.V_classificazione_voci_epBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;

import java.util.Hashtable;

/**
 * Classe che eredita le caratteristiche della superclasse <code>OggettoBulk<code>.
 * Contiene tutte le variabili e i metodi che sono comuni alle sue sottoclassi.
 * Gestisce i dati relativi alla tabella Voce_ep.
 */
public class AssCatgrpInventVoceEpBulk extends AssCatgrpInventVoceEpBase {
	protected ContoBulk conto = new ContoBulk();
	private Categoria_gruppo_inventBulk categoria_gruppo_invent = new Categoria_gruppo_inventBulk();

public AssCatgrpInventVoceEpBulk() {
	super();
}
public AssCatgrpInventVoceEpBulk(String cd_voce_ep, Integer esercizio, java.lang.String cd_categoria_gruppo) {
	super(cd_voce_ep,esercizio,cd_categoria_gruppo);
}

	public ContoBulk getConto() {
		return conto;
	}

	public void setConto(ContoBulk conto) {
		this.conto = conto;
	}

	public Categoria_gruppo_inventBulk getCategoria_gruppo_invent() {
		return categoria_gruppo_invent;
	}

	public void setCategoria_gruppo_invent(Categoria_gruppo_inventBulk categoria_gruppo_invent) {
		this.categoria_gruppo_invent = categoria_gruppo_invent;
	}
	public java.lang.String getCd_categoria_gruppo() {
		Categoria_gruppo_inventBulk categoria_gruppo_inventBulk = this.getCategoria_gruppo_invent();
		if (categoria_gruppo_inventBulk == null)
			return null;
		return categoria_gruppo_inventBulk.getCd_categoria_gruppo();
	}

	public void setCd_categoria_gruppo(java.lang.String cd_categoria_gruppo) {
		this.getCategoria_gruppo_invent().setCd_categoria_gruppo(cd_categoria_gruppo);
	}
	public java.lang.String getCd_voce_ep() {
		it.cnr.contab.config00.pdcep.bulk.ContoBulk conto = this.getConto();
		if (conto == null)
			return null;
		return conto.getCd_voce_ep();
	}
	public void setCd_voce_ep(java.lang.String cd_voce_ep) {
		this.getConto().setCd_voce_ep(cd_voce_ep);
	}



}
