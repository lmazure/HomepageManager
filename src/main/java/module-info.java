module fr.mazure.homepagemanager {
    requires javafx.controls;
    requires java.xml;
    requires java.desktop;
    requires javafx.web;
    requires jdk.httpserver;
    requires jaudiotagger;
    requires org.json;

    exports fr.mazure.homepagemanager.ui to javafx.graphics;
}
