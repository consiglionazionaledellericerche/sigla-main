package it.cnr.contab.doccont00.bp;

public class CRUDAccertamentoResiduoAmministraBP extends
		CRUDAccertamentoResiduoBP {
	
	public CRUDAccertamentoResiduoAmministraBP() {
		super();
	}

	public CRUDAccertamentoResiduoAmministraBP(String function) {
		super(function);
	}

	@Override
	public boolean isStatoVisibile() {
		return true;
	}
	
	@Override
	public boolean isROStato() {
		return false;
	}
}
