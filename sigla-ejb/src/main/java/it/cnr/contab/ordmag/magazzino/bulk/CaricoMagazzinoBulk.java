/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.DateUtils;

public class CaricoMagazzinoBulk extends MovimentiMagazzinoBulk{
	private static final long serialVersionUID = 1L;

	private TerzoBulk terzo = new TerzoBulk();
	
	private List<CaricoMagazzinoRigaBulk> caricoMagazzinoRigaColl = new BulkList<>();

	public CaricoMagazzinoBulk() {
		super();
	}

	public int addToCaricoMagazzinoRigaColl( CaricoMagazzinoRigaBulk bulk ) {
		bulk.setTerzo(this.getTerzo());
		bulk.setMovimentiMagazzinoBulk(this);
		getCaricoMagazzinoRigaColl().add(bulk);
		return getCaricoMagazzinoRigaColl().size()-1;
	}

	public CaricoMagazzinoRigaBulk removeFromCaricoMagazzinoRigaColl(int index) {
		return (CaricoMagazzinoRigaBulk)getCaricoMagazzinoRigaColl().remove(index);
	}
	
	public boolean isROTestata(){
		return Optional.ofNullable(this.getCaricoMagazzinoRigaColl())
				.map(e->!e.isEmpty())
				.orElse(Boolean.FALSE);
	}

	public void validaDate() throws ValidationException {
		if (this.getDataCompetenza()!=null && 
				DateUtils.truncate(this.getDataCompetenza()).after(DateUtils.truncate(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()))) 
			throw new ValidationException( "La \"Data di competenza\" dello scarico non deve essere successiva alla data odierna!");
	}


	public TerzoBulk getTerzo() {
		return terzo;
	}

	public void setTerzo(TerzoBulk terzo) {
		this.terzo = terzo;
	}

	public List<CaricoMagazzinoRigaBulk> getCaricoMagazzinoRigaColl() {
		return caricoMagazzinoRigaColl;
	}

	public void setCaricoMagazzinoRigaColl(List<CaricoMagazzinoRigaBulk> caricoMagazzinoRigaColl) {
		this.caricoMagazzinoRigaColl = caricoMagazzinoRigaColl;
	}
}