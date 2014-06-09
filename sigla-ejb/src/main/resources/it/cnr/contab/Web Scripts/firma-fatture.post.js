script:{
	var json = jsonUtils.toObject(requestbody.content),
	    nodeRefSource = json.nodeRefSource,
	    nodoDoc = search.findNode(nodeRefSource),
	    username = json.username,
	    password = json.password,
	    otp = json.otp, mimetypeDoc, nameDoc, service, estensione, nameDocFirmato;
	mimetypeDoc = nodoDoc.properties.content.mimetype;
	nameDoc = nodoDoc.name;
	service = cnrutils.getBean('mimetypeService');
	estensione = "." + service.getExtension(nodoDoc.mimetype);
	if (nodoDoc.name.indexOf(estensione) === -1) {
	  nameDoc = nameDoc + estensione;
	}
	nameDocFirmato = nameDoc + ".p7m";
	try{
		arubaSign.pkcs7SignV2(username, password, otp, nodoDoc.nodeRef, nodoDoc.nodeRef);
	} catch(ex) {
        status.code = 500;
        status.message = ex.javaException.message;
		status.redirect = true;
		break script;
	}  
	nodoDoc.name = nameDocFirmato;
	nodoDoc.mimetype = "application/p7m";
	nodoDoc.removeAspect("sigla_fatture_attachment:fattura_elettronica_xml_ante_firma");
	nodoDoc.addAspect("sigla_fatture_attachment:fattura_elettronica_xml_post_firma");
	nodoDoc.save();
	model.nodeRef = nodoDoc.nodeRef.toString();
}