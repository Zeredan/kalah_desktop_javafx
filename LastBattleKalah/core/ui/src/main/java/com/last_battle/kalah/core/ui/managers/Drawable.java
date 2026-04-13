package com.last_battle.kalah.core.ui.managers;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

public class Drawable {
    private static final Map<String, Image> cache = new HashMap<>();

    public static Image get(String name) {

        Image cachedImage = cache.get(name);

        if (cachedImage != null) {
            return cachedImage;
        }

        String path = "/drawable/" + name;

        var resource = Drawable.class.getResourceAsStream(path);

        if (resource == null) {
            return createEmptyImage();
        }

        Image newImage = new Image(resource);

        cache.put(name, newImage);

        return newImage;
    }

    private static Image createEmptyImage() {
        return new Image(Drawable.class.getResourceAsStream("/drawable/star.png"));
    }
}