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

import it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk;
/**
 * Insert the type's description here.
 * Creation date: (26/07/2004 16.04.17)
 * @author: Gennaro Borriello
 */
public class Trasferimento_inventarioBulk extends it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk {

	private Tipo_carico_scaricoBulk tipoMovimentoCarico;
	private java.util.Collection tipoMovimentiCarico;

	private Tipo_carico_scaricoBulk tipoMovimentoScarico;
	private java.util.Collection tipoMovimentiScarico;

	// Unità Organizzativa Destinazione
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo_destinazione;

	// Inventario Destinazione
	private it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk inventario_destinazione;

	// Opzione che indica se trasferire tutti i beni dell'Inventario
	private Boolean fl_scarica_tutti = new Boolean(false);

	// Indica se si sta facendo una operazione di trasferimento verso altro Inventario oppure
	//	all'interno dello stesso.
	private boolean trasferimentoExtraInv;	
	private boolean trasferimentoIntraInv;
	
	private boolean fl_cambio_categoria =false;

/**
 * Trasferimento_inventarioBulk constructor comment.
 */
public Trasferimento_inventarioBulk() {
	super();
}
/**
 * Trasferimento_inventarioBulk constructor comment.
 * @param esercizio java.lang.Integer
 * @param pg_buono_c_s java.lang.Long
 * @param pg_inventario java.lang.Long
 * @param ti_documento java.lang.String
 */
public Trasferimento_inventarioBulk(Integer esercizio, Long pg_buono_c_s, Long pg_inventario, String ti_documento) {
	super( pg_inventario, ti_documento,esercizio, pg_buono_c_s);
}
/**
 * Insert the method's description here.
 * Creation date: (28/07/2004 17.55.56)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_scarica_tutti() {
	return fl_scarica_tutti;
}
/**
 * Insert the method's description here.
 * Creation date: (27/07/2004 12.17.39)
 * @return it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk
 */
public it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk getInventario_destinazione() {
	return inventario_destinazione;
}
/**
 * Insert the method's description here.
 * Creation date: (26/07/2004 16.10.04)
 * @return java.util.Collection
 */
public java.util.Collection getTipoMovimentiCarico() {
	return tipoMovimentiCarico;
}
/**
 * Insert the method's description here.
 * Creation date: (26/07/2004 16.10.04)
 * @return java.util.Collection
 */
public java.util.Collection getTipoMovimentiScarico() {
	return tipoMovimentiScarico;
}
/**
 * Insert the method's description here.
 * Creation date: (26/07/2004 16.10.04)
 * @return it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk
 */
public it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk getTipoMovimentoCarico() {
	return tipoMovimentoCarico;
}
/**
 * Insert the method's description here.
 * Creation date: (26/07/2004 16.10.04)
 * @return it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk
 */
public it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk getTipoMovimentoScarico() {
	return tipoMovimentoScarico;
}
/**
 * Insert the method's description here.
 * Creation date: (27/07/2004 12.17.39)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUo_destinazione() {
	return uo_destinazione;
}
/**
 * Insert the method's description here.
 * Creation date: (20/12/2002 10.47.40)
 * @param newCdUOEmittente java.lang.String
 */
public boolean isROUoDestinazione() {
	return getUo_destinazione()==null || getUo_destinazione().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (30/07/2004 16.41.44)
 * @return boolean
 */
public boolean isTrasferimentoExtraInv() {
	return trasferimentoExtraInv;
}
/**
 * Insert the method's description here.
 * Creation date: (30/07/2004 16.41.44)
 * @return boolean
 */
public boolean isTrasferimentoIntraInv() {
	return trasferimentoIntraInv;
}
/**
 * Insert the method's description here.
 * Creation date: (28/07/2004 17.55.56)
 * @param newFl_scarica_tutti java.lang.Boolean
 */
public void setFl_scarica_tutti(java.lang.Boolean newFl_scarica_tutti) {
	fl_scarica_tutti = newFl_scarica_tutti;
}
/**
 * Insert the method's description here.
 * Creation date: (27/07/2004 12.17.39)
 * @param newInventario_destinazione it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk
 */
public void setInventario_destinazione(it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk newInventario_destinazione) {
	inventario_destinazione = newInventario_destinazione;
}
/**
 * Insert the method's description here.
 * Creation date: (26/07/2004 16.10.04)
 * @param newTipoMovimentiCarico java.util.Collection
 */
public void setTipoMovimentiCarico(java.util.Collection newTipoMovimentiCarico) {
	tipoMovimentiCarico = newTipoMovimentiCarico;
}
/**
 * Insert the method's description here.
 * Creation date: (26/07/2004 16.10.04)
 * @param newTipoMovimentiScarico java.util.Collection
 */
public void setTipoMovimentiScarico(java.util.Collection newTipoMovimentiScarico) {
	tipoMovimentiScarico = newTipoMovimentiScarico;
}
/**
 * Insert the method's description here.
 * Creation date: (26/07/2004 16.10.04)
 * @param newTipoMovimentoCarico it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk
 */
public void setTipoMovimentoCarico(it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk newTipoMovimentoCarico) {
	tipoMovimentoCarico = newTipoMovimentoCarico;
}
/**
 * Insert the method's description here.
 * Creation date: (26/07/2004 16.10.04)
 * @param newTipoMovimentoScarico it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk
 */
public void setTipoMovimentoScarico(it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk newTipoMovimentoScarico) {
	tipoMovimentoScarico = newTipoMovimentoScarico;
}
/**
 * Insert the method's description here.
 * Creation date: (30/07/2004 16.41.44)
 * @param newTrasferimentoExtraInv boolean
 */
public void setTrasferimentoExtraInv(boolean newTrasferimentoExtraInv) {
	trasferimentoExtraInv = newTrasferimentoExtraInv;
}
/**
 * Insert the method's description here.
 * Creation date: (30/07/2004 16.41.44)
 * @param newTrasferimentoIntraInv boolean
 */
public void setTrasferimentoIntraInv(boolean newTrasferimentoIntraInv) {
	trasferimentoIntraInv = newTrasferimentoIntraInv;
}
/**
 * Insert the method's description here.
 * Creation date: (27/07/2004 12.17.39)
 * @param newUo_destinazione it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUo_destinazione(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUo_destinazione) {
	uo_destinazione = newUo_destinazione;
}
public boolean isFl_cambio_categoria() {
	return fl_cambio_categoria;
}
public void setFl_cambio_categoria(boolean fl_cambio_categoria) {
	this.fl_cambio_categoria = fl_cambio_categoria;
}
}
