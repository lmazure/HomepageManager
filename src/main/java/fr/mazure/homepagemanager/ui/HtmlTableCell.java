package fr.mazure.homepagemanager.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
import javafx.scene.web.WebView;


/**
 * Table cell to display HTML
 *
 * @param <S> Class of the object displayed in the column
 */
public class HtmlTableCell<S> extends TableCell<S, String> {

    private final StackPane stackPane;
    private final WebView webView;
    private String itemRecord;

    /**
     * Constructor
     */
    public HtmlTableCell() {
        stackPane = new StackPane();
        webView = new WebView();
        stackPane.getChildren().add(webView);
        setGraphic(stackPane);
        webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            @Override
            public void changed(final ObservableValue ov, final State oldState, final State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    final EventListener listener = new EventListener() {
                        @Override
                        public void handleEvent(final Event ev) {
                            if (ev.getType().equals("click")) {
                                final String href = ((Element)ev.getTarget()).getAttribute("href");
                                webView.getEngine().loadContent(itemRecord); // kludge to avoid the WebView to navigate to the clicked link
                                try {
                                    Desktop.getDesktop().browse(new URI(href));
                                } catch (final IOException | URISyntaxException e) {
                                    ExitHelper.exit(e);
                                }
                                webView.getEngine().executeScript("history.back()");
                            }
                        }
                    }; 
                    final Document doc = webView.getEngine().getDocument();
                    final NodeList nodeList = doc.getElementsByTagName("a");
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        ((EventTarget)nodeList.item(i)).addEventListener("click", listener, false);
                    }
                }
            }
        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || (item == null)) {
            setText(null);
            setGraphic(null);
        } else {
            webView.getEngine().loadContent(item);
            setText(null);
            setGraphic(stackPane);
            itemRecord = item;
        }
    }
}
