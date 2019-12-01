package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;


import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Laurent
 *
 */
public class ProtectionFromURLChecker extends NodeChecker {

	final static InclusionTagSelector s_selector = new InclusionTagSelector( new String[] {
	        NodeChecker.X
			} );
	
	@Override
	public TagSelector getTagSelector() {
		return s_selector;
	}

	@Override
	public NodeRule[] getRules() {
		final NodeRule a[]= new NodeRule[1];
		a[0] = new NodeRule() { @Override
		public CheckStatus checkElement(final Element e) { return checkProtection(e);}
		                    @Override
							public String getDescription() { return "given the URL, the protection is incorrect"; } };
		return a;
	}

	private CheckStatus checkProtection(final Element e) {

		String url="";
		String protection="";
		
		final NodeList children = e.getChildNodes();
		for (int j=0; j<children.getLength(); j++) {
			final Node child = children.item(j);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName()=="A") {
					url=child.getTextContent();
				}
			}
		}
		
		final Node statusAttribute = e.getAttributeNode("protection");
		if ( statusAttribute != null ) {
			protection = statusAttribute.getTextContent();
		}

		if (url.contains("auntminnie.com/") && !protection.equals("free_registration"))
		   return new CheckStatus("\""+url+"\" should be flagged as 'free_registration'");

		return null;
	}
}
