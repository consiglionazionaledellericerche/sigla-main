package it.cnr.contab.docamm00.bp;

import it.cnr.jada.util.action.SimpleDetailCRUDController;

public interface FatturaPassivaElettronicaBP {
	public SimpleDetailCRUDController getCrudDocEleAllegatiColl();
	public String getNomeFileAllegato();
	public String getPath();
}
