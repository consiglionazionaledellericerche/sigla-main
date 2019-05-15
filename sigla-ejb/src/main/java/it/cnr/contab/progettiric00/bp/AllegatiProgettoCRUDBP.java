package it.cnr.contab.progettiric00.bp;

import java.util.Optional;

import it.cnr.contab.progettiric00.core.bulk.AllegatoProgettoBulk;
import it.cnr.contab.util00.bp.AllegatiTypeCRUDBP;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoTypeBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;

public abstract class AllegatiProgettoCRUDBP<T extends AllegatoGenericoTypeBulk, K extends AllegatoParentBulk> extends AllegatiTypeCRUDBP<T,K> {
    private static final long serialVersionUID = 1L;

    public AllegatiProgettoCRUDBP() {
        super();
    }

    public AllegatiProgettoCRUDBP(String s) {
        super(s);
    }

    @Override
    protected Boolean isPossibileCancellazione(AllegatoGenericoBulk allegato) {
    	if (Optional.ofNullable(allegato).map(AllegatoProgettoBulk.class::isInstance).orElse(Boolean.FALSE))
    		return super.isPossibileCancellazione(allegato);
    	return Boolean.FALSE;
    }
    
    @Override
    protected Boolean isPossibileModifica(AllegatoGenericoBulk allegato) {
    	if (Optional.ofNullable(allegato).map(AllegatoProgettoBulk.class::isInstance).orElse(Boolean.FALSE))
    		return super.isPossibileModifica(allegato);
    	return Boolean.FALSE;
    }
}
