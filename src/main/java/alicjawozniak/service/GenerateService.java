package alicjawozniak.service;

import alicjawozniak.model.ImportedFont;
import alicjawozniak.repository.ImportedFontRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Created by alicja on 29.10.16.
 */
@Service
public class GenerateService {

    @Autowired
    private ImportedFontRepository importedFontRepository;

    @Autowired
    private DrawStringService drawStringService;

    private static String backgroundDir = "/home/alicja/Desktop/projekty_src/posterGenerator/backgrounds";
    private static String fontDir = "/home/alicja/Desktop/projekty_src/posterGenerator/fonts";
    private static String generatedDir = "/home/alicja/Desktop/projekty_src/posterGenerator/generated";

    private static int backgroundCount = 52;


    public File generateImage(String text) {
        File file = null;
        try {
            BufferedImage bufferedImage = getRandomBackground();
            drawStringService.drawString1(bufferedImage, text);
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


    @PostConstruct
    private void loadFontsFromFiles() {
        for (int i = 0; i < DrawStringService.fontCount; i++) {
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

}
