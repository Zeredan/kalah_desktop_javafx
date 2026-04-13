package com.last_battle.kalah.feature;

import com.last_battle.kalah.core.ui.base.ViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GameVsBotVM extends ViewModel {

    private final IntegerProperty difficultyLevel = new SimpleIntegerProperty(1);
    private final IntegerProperty pitsCount = new SimpleIntegerProperty(6);
    private final IntegerProperty stonesCount = new SimpleIntegerProperty(6);

    public IntegerProperty difficultyLevelProperty() {
        return difficultyLevel;
    }

    public int getDifficultyLevel() {
        return difficultyLevel.get();
    }

    public void increaseDifficulty() {
        if (difficultyLevel.get() < 2) {
            difficultyLevel.set(difficultyLevel.get() + 1);
        }
    }

    public void decreaseDifficulty() {
        if (difficultyLevel.get() > 0) {
            difficultyLevel.set(difficultyLevel.get() - 1);
        }
    }

    public StringProperty difficultyNameProperty() {
        SimpleStringProperty name = new SimpleStringProperty();
        name.bind(Bindings.createStringBinding(() -> {
            String level = String.valueOf(difficultyLevel.get());
            return switch (level) {
                case "0" -> "easy";
                case "1" -> "medium";
                default -> "hard";
            };
        }, difficultyLevel));
        return name;
    }

    public IntegerProperty pitsCountProperty() {
        return pitsCount;
    }

    public void increasePits() {
        if (pitsCount.get() < 12) {
            pitsCount.set(pitsCount.get() + 1);
        }
    }

    public void decreasePits() {
        if (pitsCount.get() > 3) {
            pitsCount.set(pitsCount.get() - 1);
        }
    }

    public IntegerProperty stonesCountProperty() {
        return stonesCount;
    }

    public void increaseStones() {
        if (stonesCount.get() < 12) {
            stonesCount.set(stonesCount.get() + 1);
        }
    }

    public void decreaseStones() {
        if (stonesCount.get() > 1) {
            stonesCount.set(stonesCount.get() - 1);
        }
    }
}