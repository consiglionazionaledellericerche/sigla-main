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

package it.cnr.contab.inventario00.docs.bulk;

import java.util.Dictionary;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.util.enumeration.TipoIVA;

/**
 * Insert the type's description here.
 * Creation date: (21/07/2004 16.55.43)
 * @author: Gennaro Borriello
 */
public class Stampa_registro_inventarioVBulk extends it.cnr.jada.bulk.OggettoBulk {
	// Il Cds di scrivania
	private String cd_cds;

	// nr_inventario Da
	private java.lang.Long nrInventarioFrom;

	// nr_inventario A
	private java.lang.Long nrInventarioTo;

	// Categoria
	private Categoria_gruppo_inventBulk categoriaForPrint;

	// Gruppo
	private Categoria_gruppo_inventBulk gruppoForPrint;
	
	// Data da
	private java.sql.Timestamp dataInizio;

	// Data a
	private java.sql.Timestamp dataFine;

	// Uo di stampa
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint;

	private boolean uOForPrintEnabled;

	// CDS di stampa
	private CdsBulk cdsForPrint;

	protected Tipo_carico_scaricoBulk tipoMovimento;
	
	//  Tipo_carico_scarico di stampa
	private java.util.Collection tipoMovimenti;
	private CdsBulk cdsEnte;
	private Boolean fl_ufficiale= new Boolean(false);
	private Boolean fl_solo_totali = new Boolean(false);
	private String ti_commerciale_istituzionale;	
	public final static java.util.Dictionary ISTITUZIONALE_COMMERCIALE;
	static {
		
		ISTITUZIONALE_COMMERCIALE = new it.cnr.jada.util.OrderedHashtable();
		for (TipoIVA tipoIVA : TipoIVA.values()) {
			ISTITUZIONALE_COMMERCIALE.put(tipoIVA.value(), tipoIVA.label());
		}
	};
/**
 * Stampa_registro_inventarioVBulk constructor comment.
 */
public Stampa_registro_inventarioVBulk() {
	super();
}
public CdsBulk getCdsEnte() {
	return cdsEnte;
}
public void setCdsEnte(CdsBulk cdsEnte) {
	this.cdsEnte = cdsEnte;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @return it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk
 */
public it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk getCategoriaForPrint() {
	return categoriaForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @return java.lang.String
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 22/12/2004
 * Aggiunto per permettere la stampa di tutti
 * i CDS e non solo del 999.000 se presente in
 * scrivania 999 
 * 
 * 27/12/2004
 * Aggiunto qui il controllo precedentemente
 * effettuato in Inventario_beniComponent.
 * In questo modo a video il CDS di appartentenza è 
 * comunque visualizzato
 * */
public java.lang.String getCdCdsForPrint() {
	if (cd_cds==null ||cd_cds.equals(cdsEnte.getCd_unita_organizzativa()) )
	    return "*";	
	return cd_cds;	
}

/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdCategoriaForPrint() {

	if (getCategoriaForPrint()==null)
		return "*";
	if (getCategoriaForPrint().getCd_categoria_gruppo()==null)
		return "*";

	return getCategoriaForPrint().getCd_categoria_gruppo().toString();
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdGruppoForPrint() {

	if (getGruppoForPrint()==null)
		return "*";
	if (getGruppoForPrint().getCd_categoria_gruppo()==null)
		return "*";

	return getGruppoForPrint().getCd_categoria_gruppo().toString();
}
/**
 * Insert the method's description here.
 * Creation date: (20/01/2003 16.50.12)
 * @return java.sql.Timestamp
 */
public String getCdUOCRForPrint() {

	if (getUoForPrint()==null)
		return "*";
	if (getUoForPrint().getCd_unita_organizzativa()==null)
		return "*";

	return getUoForPrint().getCd_unita_organizzativa().toString();
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataFine() {
	return dataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDataInizio() {
	return dataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @return it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk
 */
public it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk getGruppoForPrint() {
	return gruppoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @return java.lang.Long
 */
public java.lang.Long getNrInventarioFrom() {
	return nrInventarioFrom;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @return java.lang.Long
 */
public java.lang.Long getNrInventarioTo() {
	return nrInventarioTo;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoForPrint() {
	return uoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROCategoria() {
	return getCategoriaForPrint()==null || getCategoriaForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROGruppo() {
	return getGruppoForPrint()==null || getGruppoForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROUoForPrint() {
	return getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @return boolean
 */
public boolean isUOForPrintEnabled() {
	return uOForPrintEnabled;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @param newCategoriaForPrint it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk
 */
public void setCategoriaForPrint(it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk newCategoriaForPrint) {
	categoriaForPrint = newCategoriaForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @param newCd_cds java.lang.String
 */
public void setCd_cds(java.lang.String newCd_cds) {
	cd_cds = newCd_cds;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @param newDataFine java.sql.Timestamp
 */
public void setDataFine(java.sql.Timestamp newDataFine) {
	dataFine = newDataFine;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @param newDataInizio java.sql.Timestamp
 */
public void setDataInizio(java.sql.Timestamp newDataInizio) {
	dataInizio = newDataInizio;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @param newGruppoForPrint it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk
 */
public void setGruppoForPrint(it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk newGruppoForPrint) {
	gruppoForPrint = newGruppoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @param newNrInventarioFrom java.lang.Long
 */
public void setNrInventarioFrom(java.lang.Long newNrInventarioFrom) {
	nrInventarioFrom = newNrInventarioFrom;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @param newNrInventarioTo java.lang.Long
 */
public void setNrInventarioTo(java.lang.Long newNrInventarioTo) {
	nrInventarioTo = newNrInventarioTo;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @param newUoForPrint it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUoForPrint) {
	uoForPrint = newUoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (21/07/2004 17.02.54)
 * @param newUOForPrintEnabled boolean
 */
public void setUOForPrintEnabled(boolean newUOForPrintEnabled) {
	uOForPrintEnabled = newUOForPrintEnabled;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws it.cnr.jada.bulk.ValidationException {
}
	/**
	 * @return
	 */
	public CdsBulk getCdsForPrint() {
		return cdsForPrint;
	}

	/**
	 * @param bulk
	 */
	public void setCdsForPrint(CdsBulk newCdsForPrint) {
		cdsForPrint = newCdsForPrint;
	}

	/**
	 * @return
	 */
	public java.util.Collection getTipoMovimenti() {
		return tipoMovimenti;
	}

	/**
	 * @return
	 */
	public Tipo_carico_scaricoBulk getTipoMovimento() {
		return tipoMovimento;
	}

	/**
	 * @param collection
	 */
	public void setTipoMovimenti(java.util.Collection collection) {
		tipoMovimenti = collection;
	}

	/**
	 * @param bulk
	 */
	public void setTipoMovimento(Tipo_carico_scaricoBulk bulk) {
		tipoMovimento = bulk;
	}

	/**
	 * @return
	 */
	public String getCd_tipo_movimentoForPrint() {
		if (getTipoMovimento() != null)
		  return getTipoMovimento().getCd_tipo_carico_scarico();
		else  
		  return "*";
	}

	/**
	 * @return
	 */
	public Boolean getFl_solo_totali() {
		return fl_solo_totali;
	}

	/**
	 * @param boolean1
	 */
	public void setFl_solo_totali(Boolean boolean1) {
		fl_solo_totali = boolean1;
	}
	public Dictionary getTi_istituzionale_commercialeKeys() {

		return ISTITUZIONALE_COMMERCIALE;
	}
	public String getTi_commerciale_istituzionale() {
		return ti_commerciale_istituzionale;
	}

	public void setTi_commerciale_istituzionale(String string) {
		ti_commerciale_istituzionale = string;
	}
	public Boolean getFl_ufficiale() {
		return fl_ufficiale;
	}
	public void setFl_ufficiale(Boolean fl_ufficiale) {
		this.fl_ufficiale = fl_ufficiale;
	}

}
