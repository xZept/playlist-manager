
package com.zept.practicetool.zapper;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author Allen James Laxamana
 */
public class FontImport {
    String[] fontPaths = {
        "src/main/java/com/zept/practicetool/fonts/FreeSans.ttf",
        "src/main/java/com/zept/practicetool/fonts/FreeSansBold.ttf"

    };

    public Font registerFonts(int i) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(fontPaths[i]));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font;
        } catch (IOException | FontFormatException e) {
            System.out.println(e);
            return new Font("Roboto",Font.BOLD,11);
        }
    }
}
