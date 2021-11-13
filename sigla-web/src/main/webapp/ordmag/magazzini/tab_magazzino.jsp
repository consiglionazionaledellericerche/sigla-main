<%@ page pageEncoding="UTF-8"
	import = "it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.anag00.MagazzinoBulk,
		it.cnr.contab.ordmag.anag00.*"
%>

<% SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
MagazzinoBulk ordine = (MagazzinoBulk)bp.getModel();
%>
<div class="Group card p-2 m-1">
	<table class="w-100" cellpadding="2">
         <tr>
            <td>
            <% bp.getController().writeFormField(out, "cdMagazzino");%>
            </td>
         </tr>
         <tr>
            <td>
            <% bp.getController().writeFormField(out, "dsMagazzino");%>
            </td>
         </tr>

	    <tr>
	        <td>
	        <% bp.getController().writeFormField(out, "findUnitaOperativaOrd");%>
	        </td>
	    </tr>
		<tr>				
			<td>
				<% bp.getController().writeFormField(out, "findLuogoConsegnaMag");%>
			</td>
		</tr>
		<tr>
            <td>
                <% bp.getController().writeFormField(out, "dtUltValRim");%>
            </td>
         </tr>
         <tr>
            <td>
                <% bp.getController().writeFormField(out, "esercizioValRim");%>
            </td>
          </tr>
        <tr>
            <td>
                 <% bp.getController().writeFormField(out, "metodoValRim");%>
            </td>
        </tr>
		<tr>
            <td>
                <% bp.getController().writeFormField(out, "findRaggrMagazzinoRim");%>
            </td>
        </tr>
        <tr>
            <td>
                <% bp.getController().writeFormField(out, "dtUltValSca");%>
            </td>
         </tr>
         <tr>
            <td>
                <% bp.getController().writeFormField(out, "esercizioValSca");%>
            </td>
         </tr>
         <tr>
            <td>
                 <% bp.getController().writeFormField(out, "metodoValSca");%>
            </td>
        </tr>
       <tr>
			<td>
				<% bp.getController().writeFormField(out, "findRaggrMagazzinoSca");%>
			</td>
		</tr>
		<tr>
            <td>
                <% bp.getController().writeFormField(out, "abilTuttiBeniServ");%>
            </td>
        </tr>
			<tr>
				<td><% bp.getController().writeFormField( out, "tipoGestione"); %></td>
			</tr>

		<tr>
        	<td>
                <% bp.getController().writeFormField(out, "dtCancellazione");%>
            </td>
        </tr>


	</table>
</div> 
