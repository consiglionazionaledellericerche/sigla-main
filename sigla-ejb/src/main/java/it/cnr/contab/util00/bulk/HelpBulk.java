/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/02/2020
 */
package it.cnr.contab.util00.bulk;

import it.cnr.jada.util.OrderedHashtable;

import java.util.Dictionary;

public class HelpBulk extends HelpBase {

	public final static Dictionary bpKeys = new OrderedHashtable();
    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Table name: HELP_LKT
     **/
    public HelpBulk() {
        super();
    }

    /**
     * Created by BulkGenerator 2.0 [07/12/2009]
     * Table name: HELP_LKT
     **/
    public HelpBulk(java.lang.Integer id) {
        super(id);
    }


    public HelpBulk bpName(java.lang.String bpName) {
        setBpName(bpName);
        return this;
    }

    public HelpBulk page(java.lang.String page) {
        setPage(page);
        return this;
    }

	public static Dictionary getBpKeys() {
		return bpKeys;
	}
}