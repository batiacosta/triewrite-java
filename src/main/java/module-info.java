module com.example.triewrite {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.fxmisc.richtext;
    requires junit;


    opens com.example.triewrite to javafx.fxml;
    exports com.example.triewrite;
    exports triewrite;
}