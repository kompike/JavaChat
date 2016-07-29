package com.javaclasses.chat.model.entity.tinytype;

/**
 * Tiny type for chat message text color
 */
public class TextColor {

    private final String color;

    public TextColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextColor textColor = (TextColor) o;

        return color.equals(textColor.color);

    }

    @Override
    public int hashCode() {
        return color.hashCode();
    }

    @Override
    public String toString() {
        return color;
    }
}
