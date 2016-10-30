package alicjawozniak.service;

import alicjawozniak.model.ImportedFont;
import alicjawozniak.repository.ImportedFontRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Created by alicja on 29.10.16.
 */
@Service
public class GenerateService {

    @Autowired
    private ImportedFontRepository importedFontRepository;

    private static String backgroundDir = "/home/alicja/Desktop/projekty_src/posterGenerator/backgrounds";
    private static String fontDir = "/home/alicja/Desktop/projekty_src/posterGenerator/fonts";
    private static String generatedDir = "/home/alicja/Desktop/projekty_src/posterGenerator/generated";

    private static int backgroundCount = 52;
    private static int fontCount = 18;


    public File generateImage(String text) {
        File file = null;
        try {
            BufferedImage bufferedImage = getRandomBackground();
            drawString1(bufferedImage, text);
            file = new File(generatedDir, "generated" + LocalDateTime.now() + ".png");
            ImageIO.write(bufferedImage, "PNG", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private BufferedImage getRandomBackground() {
        Random random = new Random();
        int number = random.nextInt(backgroundCount);
        File file = new File(backgroundDir, "" + number);

        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getRandomFont() {
        Random random = new Random();
        int number = random.nextInt(fontCount);
        ImportedFont importedFont = importedFontRepository.findOneByNumber(number);
        System.out.print("used font: " + importedFont.getFontName() + "\n");
        return importedFont.getFontName();
    }


    @PostConstruct
    private void loadFontsFromFiles() {
        for (int i = 0; i < fontCount; i++) {
            try {
                Font font = Font.createFont(Font.PLAIN, new File(fontDir, "" + i));
                GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
                graphicsEnvironment.registerFont(font);
                ImportedFont importedFont = new ImportedFont();
                importedFont.setFontName(font.getFontName());
                importedFont.setNumber(i);
                importedFontRepository.save(importedFont);

            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
        }
    }


    private Graphics2D drawString1(BufferedImage bufferedImage, String text) {
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

//        int textHeight = imageHeight;
//        while (textHeight>imageHeight-40){
//            textHeight = 0;
//            for (int i=0; i<fontSizes.length; i++){
//                fontSizes[i]--;
//                fonts[i] = new Font(fonts[i].getFontName(), Font.PLAIN, fontSizes[i]);
//            }
//            for (int i=0; i<fonts.length; i++){
//                textHeight+=fonts[i].getStringBounds(words[i], graphics2D.getFontRenderContext()).getHeight();
//            }
//        }

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
}
