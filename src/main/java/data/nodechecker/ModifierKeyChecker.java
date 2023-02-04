package data.nodechecker;

import java.util.Optional;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import data.nodechecker.tagselection.InclusionTagSelector;
import utils.XmlHelper;
import utils.xmlparsing.ElementType;

/**
*
*/
public class ModifierKeyChecker extends NodeChecker {

    private static final String s_WINDOWS = "Windows";
    private static final String s_SYS_RQ = "SysRq";
    private static final String s_SHIFT = "Shift";
    private static final String s_ALT = "Alt";
    private static final String s_CTRL = "Ctrl";

    private final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.MODIFIERKEY
            });

    /**
    * constructor
    */
    public ModifierKeyChecker() {
        super(s_selector,
                ModifierKeyChecker::checkModifierKeyString, "the MODIFIERKEY is incorrect",
                ModifierKeyChecker::checkModifierKeyOrder, "MODIFIERKEYs are not in the correct order");
    }

    private static CheckStatus checkModifierKeyString(final Element e) {

        final String str = e.getAttribute("id");

        if (str.equals(s_CTRL)) {
            return null;
        }
        if (str.equals(s_ALT)) {
            return null;
        }
        if (str.equals(s_SHIFT)) {
            return null;
        }
        if (str.equals(s_SYS_RQ)) {
            return null;
        }
        if (str.equals(s_WINDOWS)) {
            return null;
        }

        return new CheckStatus("IllegalModifierKey", "Illegal MODIFIERKEY (" + str + ")", Optional.empty());
    }

    private static CheckStatus checkModifierKeyOrder(final Element e) {

        final Node next = e.getNextSibling();
        if (!XmlHelper.isOfType(next, ElementType.MODIFIERKEY)) {
            return null;
        }

        final String str = e.getAttribute("id");
        final String strNext = ((Element)next).getAttribute("id");

        if (modifier1CanPreceedModifier2(str, strNext)) {
            return null;
        }

        return new CheckStatus("IllegalModifierKeyOrder", "MODIFIERKEY " + strNext + " cannot follow MODIFIERKEY " + str, Optional.empty());
    }

    private static boolean modifier1CanPreceedModifier2(
        final String modifier1,
        final String modifier2) {

        if (modifier1.equals(s_CTRL)) {
            return true;
        }

        if (modifier1.equals(s_ALT)) {
            if (modifier2.equals(s_CTRL)) {
                return false;
            }
            return true;
        }
        if (modifier1.equals(s_SHIFT)) {
            if (modifier2.equals(s_CTRL)) {
                return false;
            }
            if (modifier2.equals(s_ALT)) {
                return false;
            }
            return true;
        }
        if (modifier1.equals(s_SYS_RQ) || modifier1.equals(s_WINDOWS)) {
            if (modifier2.equals(s_CTRL)) {
                return false;
            }
            if (modifier2.equals(s_ALT)) {
                return false;
            }
            if (modifier2.equals(s_SHIFT)) {
                return false;
            }
            return true;
        }

        return false;
    }
}
