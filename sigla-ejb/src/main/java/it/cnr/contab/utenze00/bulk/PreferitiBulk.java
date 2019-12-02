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
 * Created by BulkGenerator 1.5 [30/07/2008]
 * Date 17/10/2008
 */
package it.cnr.contab.utenze00.bulk;

import java.util.Dictionary;
import java.util.Optional;

import it.cnr.jada.bulk.annotation.*;

@SuppressWarnings("unchecked")
@BulkInfoAnnotation(shortDescription="Preferiti", 
		longDescription="Preferiti",
		form={
			@FormAnnotation(value = {
			        @FieldPropertyAnnotation(name="assBpAccesso", type=TypeProperty.FormFieldProperty),
				    @FieldPropertyAnnotation(name="descrizione", type=TypeProperty.FormFieldProperty),
				    @FieldPropertyAnnotation(name="url_icona", type=TypeProperty.FormFieldProperty)}),
                @FormAnnotation(name = "bootstrap", value={
                        @FieldPropertyAnnotation(name="assBpAccesso", type=TypeProperty.FormFieldProperty),
                        @FieldPropertyAnnotation(name="descrizione", type=TypeProperty.FormFieldProperty, inputCssClass = "w-100")
                })
		},
		columnSet={
			@ColumnSetAnnotation(value={
				    @FieldPropertyAnnotation(name="descrizione", type=TypeProperty.ColumnFieldProperty),
                    @FieldPropertyAnnotation(name="url_icona", type=TypeProperty.ColumnFieldProperty)
			}),
			@ColumnSetAnnotation(name = "bootstrap", value={
			        @FieldPropertyAnnotation(name="descrizione", type=TypeProperty.ColumnFieldProperty)
			})
		},
		freeSearchSet= {
			@FreeSearchSetAnnotation(value = {
				@FieldPropertyAnnotation(name = "descrizione", type = TypeProperty.FindFieldProperty),
				@FieldPropertyAnnotation(name = "url_icona", type = TypeProperty.FindFieldProperty)}),
				@FreeSearchSetAnnotation(name = "bootstrap", value=@FieldPropertyAnnotation(name = "descrizione", type = TypeProperty.FindFieldProperty))
		}
	)
public class PreferitiBulk extends PreferitiBase {
	public final static Dictionary iconeKeys;
	public final static String LINK1 = "img/link1.gif";
	public final static String LINK2 = "img/link2.gif";
	public final static String LINK3 = "img/link3.gif";
	public final static String LINK4 = "img/link4.gif";
	public final static String LINK5 = "img/link5.gif";

	static{
		iconeKeys = new it.cnr.jada.util.OrderedHashtable();
		iconeKeys.put(LINK1, "<img src='"+LINK1+"'>");
		iconeKeys.put(LINK2, "<img src='"+LINK2+"'>");
		iconeKeys.put(LINK3, "<img src='"+LINK3+"'>");
		iconeKeys.put(LINK4, "<img src='"+LINK4+"'>");
		iconeKeys.put(LINK5, "<img src='"+LINK5+"'>");	
	}
    @FieldPropertyAnnotation(
            name="assBpAccesso",
            inputType= InputType.SEARCHTOOL,
            formName = "accesso",
            columnSet = "accesso",
            freeSearchSet = "accesso",
            nullable=false,
            enabledOnSearch=true,
            label="Accesso")
	private AssBpAccessoBulk assBpAccessoBulk;
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Table name: PREFERITI
	 **/
	public PreferitiBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Table name: PREFERITI
	 **/
	public PreferitiBulk(java.lang.String cd_utente, java.lang.String business_process, java.lang.String ti_funzione) {
		super(cd_utente, business_process, ti_funzione);
	}
	public static Dictionary getIconeKeys() {
		return iconeKeys;
	}

	public AssBpAccessoBulk getAssBpAccessoBulk() {
		return assBpAccessoBulk;
	}

	public void setAssBpAccessoBulk(AssBpAccessoBulk assBpAccessoBulk) {
		this.assBpAccessoBulk = assBpAccessoBulk;
		Optional.ofNullable(assBpAccessoBulk)
				.ifPresent(assBpAccesso -> {
					setTi_funzione(Optional.ofNullable(assBpAccesso.getTiFunzione()).orElse("C"));
					setBusiness_process(assBpAccesso.getBusinessProcess());
				});
	}

}