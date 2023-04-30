package fr.mazure.homepagemanager.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import fr.mazure.homepagemanager.utils.ExitHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.control.TableCell;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;


/**
 * Table cell to display HTML
 *
 * @param <S> Class of the object displayed in the column
 */
public class HtmlTableCell<S> extends TableCell<S, String> {

    private final StackPane _stackPane;
    private final WebView _webView;
    private String _htmlContent;

    /**
     * Constructor
     */
    public HtmlTableCell() {
        _stackPane = new StackPane();
        _webView = new WebView();
        CookieHandler.setDefault(null);
        _stackPane.getChildren().add(_webView);
        setGraphic(_stackPane);
        _webView.getEngine().setUserStyleSheetLocation("data:,body { font: " + Font.getDefault().getSize() + "px '"+ Font.getDefault().getName() + "'; }");
        _webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            @Override
            public void changed(final ObservableValue<? extends State> ov, final State oldState, final State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    final EventListener listener = new EventListener() {
                        @Override
                        public void handleEvent(final Event ev) {
                            if (ev.getType().equals("click")) {
                                final String href = ((Element)ev.getTarget()).getAttribute("href");
                                _webView.getEngine().loadContent(_htmlContent); // kludge to avoid the WebView to navigate to the clicked link
                                try {
                                    Desktop.getDesktop().browse(new URI(href));
                                } catch (final IOException | URISyntaxException e) {
                                    ExitHelper.exit(e);
                                }
                                _webView.getEngine().executeScript("history.back()");
                            }
                        }
                    };
                    final Document doc = _webView.getEngine().getDocument();
                    final NodeList nodeList = doc.getElementsByTagName("a");
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        ((EventTarget)nodeList.item(i)).addEventListener("click", listener, false);
                    }
                }
            }
        });
    }

    @Override
    protected void updateItem(final String item, final boolean empty) {
        super.updateItem(item, empty);
        if (empty || (item == null)) {
            setText(null);
            setGraphic(null);
        } else {
            _htmlContent = workaroundWebViewEmojiBug(item);
            _webView.setPrefHeight(-1);   // <- Absolute must at this position (before calling the Javascript)
            _webView.getEngine().setUserStyleSheetLocation("data:,body { font: " + Font.getDefault().getSize() + "px '"+ Font.getDefault().getName() + "'; }");
            _webView.getEngine().loadContent(_htmlContent);
            setText(null);
            setGraphic(_stackPane);
            _webView.getEngine().documentProperty().addListener((final ObservableValue<? extends Document>obj, final Document prev, final Document newv) -> {
                final Integer heightText = (Integer)_webView.getEngine().executeScript(
                        "Math.max( document.body.scrollHeight , document.body.offsetHeight, document.documentElement.clientHeight, document.documentElement.scrollHeight, document.documentElement.offsetHeight);"
                );
                final int height = heightText.intValue() + 10;
                //System.out.println("document.body.scrollHeight = " + getJavaScriptIntegerValue("document.body.scrollHeight"));
                //System.out.println("document.body.offsetHeight = " + getJavaScriptIntegerValue("document.body.offsetHeight"));
                //System.out.println("document.documentElement.clientHeight = " + getJavaScriptIntegerValue("document.documentElement.clientHeight"));
                //System.out.println("document.documentElement.scrollHeight = " + getJavaScriptIntegerValue("document.documentElement.scrollHeight"));
                //System.out.println("document.documentElement.offsetHeight = " + getJavaScriptIntegerValue("document.documentElement.offsetHeight"));
                _webView.setPrefHeight(height);
                this.setPrefHeight(height);
            });
        }
    }

    //private int getJavaScriptIntegerValue(final String name) {
    //    final Integer heightText = (Integer)_webView.getEngine().executeScript(name);
    //    return heightText.intValue();
    //}

    private static String workaroundWebViewEmojiBug(final String str) {
        return str.codePoints()
                  .mapToObj(c -> Character.isBmpCodePoint(c) ? Character.toString(c)
                                                             : ("&#" + c + ";"))
                  .collect(Collectors.joining());
    }
}
