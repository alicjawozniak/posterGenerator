package alicjawozniak.service;

import alicjawozniak.model.ImportedFont;
import alicjawozniak.repository.ImportedFontRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.text.AttributedString;
import java.util.Random;

/**
 * Created by alicja on 31.10.16.
 */
@Service
public class DrawStringService {

    @Autowired
    private ImportedFontRepository importedFontRepository;

    public final static int fontCount = 18;

    private String getRandomFont() {
        Random random = new Random();
        int number = random.nextInt(fontCount);
        ImportedFont importedFont = importedFontRepository.findOneByNumber(number);
        System.out.print("used font: " + importedFont.getFontName() + "\n");
        return importedFont.getFontName();
    }

    public Graphics2D drawString1(BufferedImage bufferedImage, String text) {
        Graphics2D graphics2D = bufferedImage.createGraphics();
        int imageHeight = bufferedImage.getHeight();
        int imageWidth = bufferedImage.getWidth();

        String[] words = text.split(" ");
        Font[] fonts = new Font[words.length];

        int textWidth = imageWidth / 2;

        int[] fontSizes = new int[words.length];
        for (int i = 0; i < words.length; i++) {
            fontSizes[i] = textWidth / words[i].length();
            fonts[i] = new Font(getRandomFont(), Font.PLAIN, fontSizes[i]);
        }
        for (int i = 0; i < words.length; i++) {
            while (fonts[i].getStringBounds(words[i], graphics2D.getFontRenderContext()).getWidth() < textWidth) {
                fontSizes[i]++;
                fonts[i] = new Font(fonts[i].getFontName(), Font.PLAIN, fontSizes[i]);
            }
        }

        int textHeight = imageHeight;
        while (textHeight > imageHeight - 40) {
            textHeight = 0;
            textWidth -= 30;
            for (int i = 0; i < words.length; i++) {
                fontSizes[i] = textWidth / words[i].length();
                fonts[i] = new Font(fonts[i].getFontName(), Font.PLAIN, fontSizes[i]);
            }
            for (int i = 0; i < words.length; i++) {
                while (fonts[i].getStringBounds(words[i], graphics2D.getFontRenderContext()).getWidth() < textWidth) {
                    fontSizes[i]++;
                    fonts[i] = new Font(fonts[i].getFontName(), Font.PLAIN, fontSizes[i]);
                }
            }
            for (int i = 0; i < fonts.length; i++) {
                textHeight += fonts[i].getStringBounds(words[i], graphics2D.getFontRenderContext()).getHeight();
            }
        }

        int currentX = imageWidth / 4;
        int currentY = 20 + (int) fonts[0].getStringBounds(words[0], graphics2D.getFontRenderContext()).getHeight();
        for (int i = 0; i < words.length; i++) {
            AttributedString attributedString = new AttributedString(words[i]);
            attributedString.addAttribute(TextAttribute.FONT, fonts[i], 0, words[i].length());
            graphics2D.drawString(attributedString.getIterator(), currentX, currentY);
            if (i + 1 < words.length) {
                currentY += fonts[i + 1].getStringBounds(words[i + 1], graphics2D.getFontRenderContext()).getHeight();
            }
        }


        return graphics2D;
    }

    public void drawString2(BufferedImage bufferedImage, String text) {
    }

}
