script:{
	var json = jsonUtils.toObject(requestbody.content),
	    nodes = [],
	    node,
	    apparence = json.apparence,
	    username = json.username,
	    password = json.password,
	    otp = json.otp;
		if (json.nodes) {
		  for (i = 0; i < json.nodes.size(); i++) {
		    nodes.push(json.nodes.get(i));
		    node = search.findNode(json.nodes.get(i));
		  }
		}
		try{
			arubaSign.pdfsignatureV2Multiple(username, password, otp, nodes,
	      		"https://gestdoc.cnr.it/alfresco/d/a/workspace/SpacesStore/d676d0ff-101f-4b01-8743-e54769bcb004/firma-digitale-ArubaSign-icona.png",
	      		apparence.leftx, apparence.lefty, apparence.location, apparence.page, apparence.reason, apparence.rightx, apparence.righty, apparence.testo);
		} catch(ex) {
        	status.code = 500;
        	status.message = ex.javaException.message;
			status.redirect = true;
			break script;
		}
}