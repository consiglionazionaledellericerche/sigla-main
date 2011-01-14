package it.cnr.contab.util;

/**
 * Insert the type's description here.
 * Creation date: (10/10/2002 16:32:06)
 * @author: CNRADM
 */
public class PositivePercentFormat extends PercentFormat {
/**
 * PositivePercentFormat constructor comment.
 */
public PositivePercentFormat() {
	super();
}
public Object parseObject(String source) throws java.text.ParseException {
	java.math.BigDecimal percent = (java.math.BigDecimal)super.parseObject(source);
	if (percent == null) return null;
	if (percent.signum() < 0)
		throw new it.cnr.jada.bulk.ValidationParseException("Sono ammesse solo percentuali positive",0);
	return percent;
}
}
