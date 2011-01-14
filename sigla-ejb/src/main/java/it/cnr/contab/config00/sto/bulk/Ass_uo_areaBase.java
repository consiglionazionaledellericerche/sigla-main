/*
* Created by Generator 1.0
* Date 12/09/2005
*/
package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.persistency.Keyed;
public class Ass_uo_areaBase extends Ass_uo_areaKey implements Keyed {
//    FL_PRESIDENTE_AREA CHAR(1) NOT NULL
	private java.lang.Boolean fl_presidente_area;
 
	public Ass_uo_areaBase() {
		super();
	}
	public Ass_uo_areaBase(java.lang.Integer esercizio, java.lang.String cd_unita_area, java.lang.String cd_unita_organizzativa) {
		super(esercizio, cd_unita_area, cd_unita_organizzativa);
	}
	public java.lang.Boolean getFl_presidente_area () {
		return fl_presidente_area;
	}
	public void setFl_presidente_area(java.lang.Boolean fl_presidente_area)  {
		this.fl_presidente_area=fl_presidente_area;
	}
}