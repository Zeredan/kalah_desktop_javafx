package com.last_battle.kalah;

import javafx.application.Application;
import javafx.stage.Stage;

public class KalahApplication extends Application {
    private Object mainNavigationRoot;


    @Override
    public void start(Stage stage) throws Exception {
        mainNavigationRoot = new MainNavigationRoot(stage);
    }
}
