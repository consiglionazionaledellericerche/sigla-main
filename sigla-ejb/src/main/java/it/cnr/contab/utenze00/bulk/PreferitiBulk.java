/*
 * Created by BulkGenerator 1.5 [30/07/2008]
 * Date 17/10/2008
 */
package it.cnr.contab.utenze00.bulk;

import java.util.Dictionary;

import it.cnr.jada.bulk.annotation.BulkInfoAnnotation;
import it.cnr.jada.bulk.annotation.ColumnSetAnnotation;
import it.cnr.jada.bulk.annotation.FieldPropertyAnnotation;
import it.cnr.jada.bulk.annotation.FormAnnotation;
import it.cnr.jada.bulk.annotation.FreeSearchSetAnnotation;
import it.cnr.jada.bulk.annotation.Layout;
import it.cnr.jada.bulk.annotation.TypeProperty;
@SuppressWarnings("unchecked")
@BulkInfoAnnotation(shortDescription="Preferiti", 
		longDescription="Preferiti",
		form = {
			@FormAnnotation(value = {
				@FieldPropertyAnnotation(name="descrizione", type=TypeProperty.FormFieldProperty), 
				@FieldPropertyAnnotation(name="url_icona", type=TypeProperty.FormFieldProperty)}),
			@FormAnnotation(name= "bootstrap", value = {
					@FieldPropertyAnnotation(name="descrizione", type=TypeProperty.FormFieldProperty)})
		},
		columnSet=@ColumnSetAnnotation(value={
				@FieldPropertyAnnotation(name="descrizione", type=TypeProperty.ColumnFieldProperty),
				@FieldPropertyAnnotation(name="url_icona", type=TypeProperty.ColumnFieldProperty)}),
		freeSearchSet=@FreeSearchSetAnnotation(value={
				@FieldPropertyAnnotation(name="descrizione", type=TypeProperty.FindFieldProperty),
				@FieldPropertyAnnotation(name="url_icona", type=TypeProperty.FindFieldProperty)})		
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

}