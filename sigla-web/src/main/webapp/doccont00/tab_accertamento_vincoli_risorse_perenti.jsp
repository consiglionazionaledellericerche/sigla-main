<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.doccont00.bp.*,it.cnr.contab.doccont00.action.*, it.cnr.contab.doccont00.core.bulk.*,
			it.cnr.contab.doccont00.core.bulk.*"
%>
<%  
	CRUDAccertamentoBP bp = (CRUDAccertamentoBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = bp.getPdgVincoliPerenti();
   	controller.writeHTMLTable(pageContext,"default",true,false,true,"100%","200px"); %>

<div class="Group" style="width:100%;padding:0px">
	<table class="Panel">
  	  	<tr>
  	  		<td><% controller.writeFormLabel(out,"findVariazioneResidua");%></td>
		  	<td colspan="3"><% controller.writeFormInput(out,"findVariazioneResidua"); %></td>
		</tr>
  	  	<tr>
		  	<td><% controller.writeFormLabel(out,"im_vincolo");%></td>
		  	<td colspan="3"><% controller.writeFormInput(out,"im_vincolo"); %></td>
	    </tr>
	</table>
</div>