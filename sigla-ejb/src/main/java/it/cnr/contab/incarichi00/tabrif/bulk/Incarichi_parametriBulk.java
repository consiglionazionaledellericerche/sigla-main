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

package it.cnr.contab.incarichi00.tabrif.bulk;

import it.cnr.contab.config00.file.bulk.Gruppo_fileBulk;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.annotation.*;

@BulkInfoAnnotation(shortDescription="Tabella di parametrizzazione incarichi", 
			longDescription="Tabella di parametrizzazione incarichi",
			form={@FormAnnotation(value = {
					@FieldPropertyAnnotation(name="cd_parametri",type=TypeProperty.FormFieldProperty), 
					@FieldPropertyAnnotation(name="fl_pubblica_contratto",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="fl_allega_contratto",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="fl_allega_decisione_ctr",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="fl_allega_decreto_nomina",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="fl_ricerca_interna",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="fl_meramente_occasionale",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="importo_limite_merocc",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="incarico_ric_giorni_pubbl",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="incarico_ric_giorni_scad",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="cd_gruppo_file",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="dacr",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="utcr",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="duva",type=TypeProperty.FormFieldProperty),
					@FieldPropertyAnnotation(name="utuv",type=TypeProperty.FormFieldProperty)}),
				@FormAnnotation(name="searchtool",value={@FieldPropertyAnnotation(name="idSessione",type=TypeProperty.FormFieldProperty)})},
			columnSet=@ColumnSetAnnotation(value={
					@FieldPropertyAnnotation(name="cd_parametri",type=TypeProperty.ColumnFieldProperty), 
					@FieldPropertyAnnotation(name="fl_pubblica_contratto",type=TypeProperty.ColumnFieldProperty),
					@FieldPropertyAnnotation(name="fl_allega_contratto",type=TypeProperty.ColumnFieldProperty),
					@FieldPropertyAnnotation(name="fl_allega_decisione_ctr",type=TypeProperty.ColumnFieldProperty),
					@FieldPropertyAnnotation(name="fl_allega_decreto_nomina",type=TypeProperty.ColumnFieldProperty),
					@FieldPropertyAnnotation(name="fl_ricerca_interna",type=TypeProperty.ColumnFieldProperty),
					@FieldPropertyAnnotation(name="fl_meramente_occasionale",type=TypeProperty.ColumnFieldProperty),
					@FieldPropertyAnnotation(name="importo_limite_merocc",type=TypeProperty.ColumnFieldProperty),
					@FieldPropertyAnnotation(name="incarico_ric_giorni_pubbl",type=TypeProperty.ColumnFieldProperty),
					@FieldPropertyAnnotation(name="incarico_ric_giorni_scad",type=TypeProperty.ColumnFieldProperty),
					@FieldPropertyAnnotation(name="cd_gruppo_file",type=TypeProperty.ColumnFieldProperty),
					@FieldPropertyAnnotation(name="dacr", type=TypeProperty.ColumnFieldProperty),
					@FieldPropertyAnnotation(name="duva", type=TypeProperty.ColumnFieldProperty)}),
			freeSearchSet=@FreeSearchSetAnnotation(value={
					@FieldPropertyAnnotation(name="cd_parametri",type=TypeProperty.FindFieldProperty), 
					@FieldPropertyAnnotation(name="fl_pubblica_contratto",type=TypeProperty.FindFieldProperty),
					@FieldPropertyAnnotation(name="fl_allega_contratto",type=TypeProperty.FindFieldProperty),
					@FieldPropertyAnnotation(name="fl_allega_decisione_ctr",type=TypeProperty.FindFieldProperty),
					@FieldPropertyAnnotation(name="fl_allega_decreto_nomina",type=TypeProperty.FindFieldProperty),
					@FieldPropertyAnnotation(name="fl_ricerca_interna",type=TypeProperty.FindFieldProperty),
					@FieldPropertyAnnotation(name="fl_meramente_occasionale",type=TypeProperty.FindFieldProperty),
					@FieldPropertyAnnotation(name="importo_limite_merocc",type=TypeProperty.FindFieldProperty),
					@FieldPropertyAnnotation(name="incarico_ric_giorni_pubbl",type=TypeProperty.FindFieldProperty),
					@FieldPropertyAnnotation(name="incarico_ric_giorni_scad",type=TypeProperty.FindFieldProperty),
					@FieldPropertyAnnotation(name="cd_gruppo_file",type=TypeProperty.FindFieldProperty),
					@FieldPropertyAnnotation(name="dacr", type=TypeProperty.FindFieldProperty),
					@FieldPropertyAnnotation(name="duva", type=TypeProperty.FindFieldProperty)})		
		)
public class Incarichi_parametriBulk extends Incarichi_parametriBase {
	private Gruppo_fileBulk gruppo_file = new Gruppo_fileBulk();

	private BulkList gruppoFileList = new BulkList();

	public Incarichi_parametriBulk() {
		super();
	}

	public Incarichi_parametriBulk(java.lang.String cd_parametri) {
		super(cd_parametri);
	}

	public Gruppo_fileBulk getGruppo_file() {
		return gruppo_file;
	}
	public void setGruppo_file(Gruppo_fileBulk gruppo_file) {
		this.gruppo_file = gruppo_file;
	}

	@Override
	public String getCd_gruppo_file() {
		if (getGruppo_file()==null)
			return null;
		return getGruppo_file().getCd_gruppo_file();
	}
	@Override
	public void setCd_gruppo_file(String cd_gruppo_file) {
		if (getGruppo_file()!=null)
			getGruppo_file().setCd_gruppo_file(cd_gruppo_file);
	}

	public BulkList getGruppoFileList() {
		return gruppoFileList;
	}
	public void setGruppoFileList(BulkList gruppoFileList) {
		this.gruppoFileList = gruppoFileList;
	}
}