package data.nodechecker.checker.nodeChecker;


import org.w3c.dom.Element;
import org.w3c.dom.Node;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.XMLHelper;
import utils.xmlparsing.NodeType;

public class ModifierKeyChecker extends NodeChecker {

    private static final String s_WINDOWS = "Windows";
    private static final String s_SYS_RQ = "SysRq";
    private static final String s_SHIFT = "Shift";
    private static final String s_ALT = "Alt";
    private static final String s_CTRL = "Ctrl";

    final static InclusionTagSelector s_selector = new InclusionTagSelector(new NodeType[] {
            NodeType.MODIFIERKEY
            });

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

        if (str.equals(s_CTRL)) return null;
        if (str.equals(s_ALT)) return null;
        if (str.equals(s_SHIFT)) return null;
        if (str.equals(s_SYS_RQ)) return null;
        if (str.equals(s_WINDOWS)) return null;

        return new CheckStatus("Illegal MODIFIERKEY (" + str + ")");
    }

    private CheckStatus checkModifierKeyOrder(final Element e) {

        final Node next = e.getNextSibling();
        if (!XMLHelper.isOfType(next, NodeType.MODIFIERKEY)) return null;

        final String str = e.getAttribute("ID");
        final String strNext = ((Element)next).getAttribute("ID");

        if (modifier1CanPreceedModifier2(str, strNext)) return null;

        return new CheckStatus("MODIFIERKEY " + strNext + " cannot follow MODIFIERKEY " + str);
    }

    private static boolean modifier1CanPreceedModifier2(
        final String modifier1,
        final String modifier2) {

        if (modifier1.equals(s_CTRL)) return true;

        if (modifier1.equals(s_ALT)) {
            if (modifier2.equals(s_CTRL)) return false;
            return true;
        }
        if (modifier1.equals(s_SHIFT)) {
            if (modifier2.equals(s_CTRL)) return false;
            if (modifier2.equals(s_ALT)) return false;
            return true;
        }
        if (modifier1.equals(s_SYS_RQ) || modifier1.equals(s_WINDOWS)) {
            if (modifier2.equals(s_CTRL)) return false;
            if (modifier2.equals(s_ALT)) return false;
            if (modifier2.equals(s_SHIFT)) return false;
            return true;
        }

        return false;
    }
}
