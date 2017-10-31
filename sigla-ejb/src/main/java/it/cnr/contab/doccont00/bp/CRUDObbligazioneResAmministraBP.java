package it.cnr.contab.doccont00.bp;

public class CRUDObbligazioneResAmministraBP extends CRUDObbligazioneResBP {
	private static final long serialVersionUID = 1L;

	public CRUDObbligazioneResAmministraBP() {
		super();
	}

	public CRUDObbligazioneResAmministraBP(String function) {
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
