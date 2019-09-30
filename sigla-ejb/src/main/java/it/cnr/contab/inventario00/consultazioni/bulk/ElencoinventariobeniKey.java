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
* Creted by Generator 1.0
* Date 03/03/2005
*/
package it.cnr.contab.inventario00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class ElencoinventariobeniKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_unita_organizzativa;
	private java.lang.String cd_categoria_gruppo;
	private java.lang.Long nr_inventario;
	private java.lang.Long progressivo;
	private java.lang.String ds_bene;
	public ElencoinventariobeniKey() {
		super();
	}
	public ElencoinventariobeniKey(java.lang.String cd_unita_organizzativa, java.lang.String cd_categoria_gruppo, java.lang.Long nr_inventario, java.lang.Long progressivo, java.lang.String ds_bene, java.sql.Timestamp data_registrazione) {
		super();
		this.cd_unita_organizzativa=cd_unita_organizzativa;
		this.cd_categoria_gruppo=cd_categoria_gruppo;
		this.nr_inventario=nr_inventario;
		this.progressivo=progressivo;
		this.ds_bene=ds_bene;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof ElencoinventariobeniKey)) return false;
		ElencoinventariobeniKey k = (ElencoinventariobeniKey) o;
		if (!compareKey(getCd_unita_organizzativa(), k.getCd_unita_organizzativa())) return false;
		if (!compareKey(getCd_categoria_gruppo(), k.getCd_categoria_gruppo())) return false;
		if (!compareKey(getNr_inventario(), k.getNr_inventario())) return false;
		if (!compareKey(getProgressivo(), k.getProgressivo())) return false;
		if (!compareKey(getDs_bene(), k.getDs_bene())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_unita_organizzativa());
		i = i + calculateKeyHashCode(getCd_categoria_gruppo());
		i = i + calculateKeyHashCode(getNr_inventario());
		i = i + calculateKeyHashCode(getProgressivo());
		i = i + calculateKeyHashCode(getDs_bene());
		return i;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.String getCd_unita_organizzativa () {
		return cd_unita_organizzativa;
	}
	public void setCd_categoria_gruppo(java.lang.String cd_categoria_gruppo)  {
		this.cd_categoria_gruppo=cd_categoria_gruppo;
	}
	public java.lang.String getCd_categoria_gruppo () {
		return cd_categoria_gruppo;
	}
	public void setNr_inventario(java.lang.Long nr_inventario)  {
		this.nr_inventario=nr_inventario;
	}
	public java.lang.Long getNr_inventario () {
		return nr_inventario;
	}
	public void setProgressivo(java.lang.Long progressivo)  {
		this.progressivo=progressivo;
	}
	public java.lang.Long getProgressivo () {
		return progressivo;
	}
	public void setDs_bene(java.lang.String ds_bene)  {
		this.ds_bene=ds_bene;
	}
	public java.lang.String getDs_bene () {
		return ds_bene;
	}
}