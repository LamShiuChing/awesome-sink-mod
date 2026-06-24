import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * Procedural placeholder textures (run once via `java build-tools/TexGen.java`).
 * Crude but custom and easy to replace with real art later.
 */
public class TexGen {
    static final String BASE = "src/main/resources/assets/awesomesink/textures/";

    public static void main(String[] args) throws Exception {
        write(sink(), BASE + "block/awesome_sink.png");
        write(shop(), BASE + "block/awesome_shop.png");
        write(coupon(), BASE + "item/coupon.png");
        write(gui(), BASE + "gui/awesome_machine.png");
        System.out.println("textures written");
    }

    static void write(BufferedImage img, String path) throws Exception {
        File f = new File(path);
        f.getParentFile().mkdirs();
        ImageIO.write(img, "PNG", f);
    }

    static void fill(BufferedImage img, int x, int y, int w, int h, int argb) {
        for (int j = y; j < y + h; j++)
            for (int i = x; i < x + w; i++)
                if (i >= 0 && j >= 0 && i < img.getWidth() && j < img.getHeight())
                    img.setRGB(i, j, argb);
    }

    static BufferedImage img(int w, int h) {
        return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }

    // 16x16 brushed-metal block with a dark central sink funnel + rivets.
    static BufferedImage sink() {
        BufferedImage b = img(16, 16);
        fill(b, 0, 0, 16, 16, 0xFF6E6E72);
        for (int y = 0; y < 16; y++) {
            int shade = 0xFF000000 | (0x66 + (y % 2) * 0x0A) * 0x010101;
            fill(b, 0, y, 16, 1, blend(0xFF6E6E72, shade, 0.12f));
        }
        fill(b, 0, 0, 16, 1, 0xFF9A9AA0);
        fill(b, 0, 15, 16, 1, 0xFF3A3A40);
        fill(b, 3, 3, 10, 10, 0xFF45454C);
        fill(b, 5, 5, 6, 6, 0xFF26262B);
        fill(b, 7, 7, 2, 2, 0xFF101013);
        int[][] rivets = {{1, 1}, {14, 1}, {1, 14}, {14, 14}};
        for (int[] r : rivets) fill(b, r[0], r[1], 1, 1, 0xFFB8B8C0);
        return b;
    }

    // 16x16 teal vending machine with a window and a coupon-gold coin.
    static BufferedImage shop() {
        BufferedImage b = img(16, 16);
        fill(b, 0, 0, 16, 16, 0xFF2A9D8F);
        fill(b, 0, 0, 16, 1, 0xFF4FC2B3);
        fill(b, 0, 15, 16, 1, 0xFF1C6F65);
        fill(b, 2, 2, 12, 8, 0xFF1B3A38);
        fill(b, 3, 3, 10, 6, 0xFF8FE3DC);
        fill(b, 6, 11, 4, 4, 0xFFE9C46A);
        fill(b, 7, 12, 2, 2, 0xFFB8860B);
        return b;
    }

    // 16x16 gold ticket coupon with notches and a star.
    static BufferedImage coupon() {
        BufferedImage b = img(16, 16);
        fill(b, 2, 4, 12, 8, 0xFFE9C46A);
        fill(b, 2, 4, 12, 1, 0xFFF4D88B);
        fill(b, 2, 11, 12, 1, 0xFFC79B3B);
        fill(b, 1, 7, 1, 2, 0x00000000);
        fill(b, 14, 7, 1, 2, 0x00000000);
        fill(b, 7, 6, 2, 1, 0xFFB8860B);
        fill(b, 6, 7, 4, 1, 0xFFB8860B);
        fill(b, 7, 8, 2, 2, 0xFFB8860B);
        return b;
    }

    // 256x256 canvas, 176x166 vanilla-style machine GUI in the top-left.
    static BufferedImage gui() {
        BufferedImage b = img(256, 256);
        fill(b, 0, 0, 176, 166, 0xFFC6C6C6);
        fill(b, 0, 0, 176, 1, 0xFFFFFFFF);
        fill(b, 0, 0, 1, 166, 0xFFFFFFFF);
        fill(b, 0, 165, 176, 1, 0xFF555555);
        fill(b, 175, 0, 1, 166, 0xFF555555);
        slot(b, 56, 35);   // machine input
        slot(b, 116, 35);  // machine output
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                slot(b, 8 + col * 18, 84 + row * 18);
        for (int col = 0; col < 9; col++) slot(b, 8 + col * 18, 142);
        return b;
    }

    static void slot(BufferedImage b, int x, int y) {
        fill(b, x - 1, y - 1, 18, 18, 0xFF373737);
        fill(b, x, y, 17, 17, 0xFFC6C6C6);
        fill(b, x, y, 16, 16, 0xFF8B8B8B);
        fill(b, x, y, 16, 1, 0xFF373737);
        fill(b, x, y, 1, 16, 0xFF373737);
    }

    static int blend(int a, int c, float t) {
        int ar = (a >> 16) & 0xFF, ag = (a >> 8) & 0xFF, ab = a & 0xFF;
        int cr = (c >> 16) & 0xFF, cg = (c >> 8) & 0xFF, cb = c & 0xFF;
        int r = Math.round(ar * (1 - t) + cr * t);
        int g = Math.round(ag * (1 - t) + cg * t);
        int bl = Math.round(ab * (1 - t) + cb * t);
        return 0xFF000000 | (r << 16) | (g << 8) | bl;
    }
}
