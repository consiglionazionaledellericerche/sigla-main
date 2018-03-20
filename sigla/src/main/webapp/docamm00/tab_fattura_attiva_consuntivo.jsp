<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<% CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP)BusinessProcess.getBusinessProcess(request);
	Fattura_attivaBulk fatturaAttiva = (Fattura_attivaBulk)bp.getModel(); %>
	<% bp.getConsuntivoController().writeHTMLTable(pageContext,"consuntivoSet",false,false,false,"100%","100px"); %>
  
   <div class="Group card">

   <table>	  	
      <tr>
      	<% bp.getController().writeFormField(out,"im_totale_imponibile");%>
      	<% bp.getController().writeFormField(out,"im_totale_iva");%>
   		<% bp.getController().writeFormField(out,"im_totale_fattura");%>
      </tr>
    </table>
    
   </div>