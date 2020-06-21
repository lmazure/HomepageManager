package data.nodechecker.checker.nodeChecker;


import org.w3c.dom.Element;
import org.w3c.dom.Node;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.xmlparsing.NodeType;

public class ModifierKeyChecker extends NodeChecker {

	final static InclusionTagSelector s_selector = new InclusionTagSelector( new NodeType[] {
			NodeType.MODIFIERKEY
			} );

	@Override
	public TagSelector getTagSelector() {
		return s_selector;
	}

	@Override
	public NodeRule[] getRules() {
		final NodeRule a[]= new NodeRule[2];
		a[0] = new NodeRule() { @Override
		public CheckStatus checkElement(final Element e) { return checkModifierKeyString(e);}
		                    @Override
							public String getDescription() { return "the MODIFIERKEY is correct"; } };
		a[1] = new NodeRule() { @Override
		public CheckStatus checkElement(final Element e) { return checkModifierKeyOrder(e);}
							@Override
							public String getDescription() { return "MODIFIERKEYs are in the correct order"; } };

        return a;
	}

	private CheckStatus checkModifierKeyString(final Element e) {
		
		final String str = e.getAttribute("ID");
		
		if (str.equals("Ctrl")) return null;
		if (str.equals("Alt")) return null;
		if (str.equals("Shift")) return null;
		if (str.equals("SysRq")) return null;
        if (str.equals("Windows")) return null;

		return new CheckStatus("Illegal MODIFIERKEY ("+ str + ")");
	}
	
	private CheckStatus checkModifierKeyOrder(final Element e) {
		
		final Node next = e.getNextSibling();
		if ( !next.getNodeName().equals("MODIFIERKEY")) return null;

		final String str = e.getAttribute("ID");
		final String strNext = ((Element)next).getAttribute("ID");

		if (modifier1CanPreceedModifier2(str, strNext)) return null;	

		return new CheckStatus("MODIFIERKEY "+ strNext + " cannot follow MODIFIERKEY " + str);
	}

	private static boolean modifier1CanPreceedModifier2(
		final String modifier1,
		final String modifier2) {

		if (modifier1.equals("Ctrl")) return true;
		
		if (modifier1.equals("Alt")) {
			if (modifier2.equals("Ctrl")) return false;		
			return true;
		}
		if (modifier1.equals("Shift")) {
			if (modifier2.equals("Ctrl")) return false;		
			if (modifier2.equals("Alt")) return false;		
			return true;
		}
		if (modifier1.equals("SysRq"))  {
			if (modifier2.equals("Ctrl")) return false;		
			if (modifier2.equals("Alt")) return false;		
			if (modifier2.equals("Shift")) return false;		
			return true;
		}

		return false;
	}
}
