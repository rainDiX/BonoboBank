package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

import blockChain.BlockChain;

public class BlockChainView extends JPanel {

    private BlockChain blockchain;

    // Prendre de preference des valeurs pair
    private static final int BLOCKPERLINE = 16;
    private static final int BLOCKHEIGHT = 40;
    private static final int BLOCKWIDTH = 40;
    private static final int LINKHEIGHT = 8;
    private static final int LINKWIDTH = 16;
    
    // Décallage par rapport au bord de la vue
    private static final int XOFFSET = 30;
    private static final int YOFFSET = 20;

    public BlockChainView(BlockChain blockchain) {
        super();
        this.blockchain = blockchain;
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        int blockCount = 0;

        if (blockchain != null) {
            blockCount = blockchain.getSize();
        }

        // g2d.setPaint(new Color(13, 152, 186));
        g2d.setPaint(new Color(150, 150, 150));
        int x = 0;
        int y = 0;
        for (int i = 0; i < blockCount; ++i) {
            g2d.fillRect(XOFFSET + (BLOCKWIDTH + LINKWIDTH) * x, YOFFSET + (BLOCKHEIGHT + LINKWIDTH) * y, BLOCKWIDTH, BLOCKHEIGHT);
            if (i % BLOCKPERLINE == (BLOCKPERLINE - 1)) {
                // On a dessiné N blocs, on descends
                g2d.setPaint(new Color(200, 200, 200));
                g2d.fillRect(XOFFSET + (BLOCKWIDTH + LINKWIDTH) * x + (BLOCKHEIGHT / 2 - LINKHEIGHT / 2),
                        YOFFSET + (BLOCKHEIGHT + LINKWIDTH) * y + BLOCKHEIGHT, LINKHEIGHT, LINKWIDTH);
                g2d.setPaint(new Color(150, 150, 150));
                ++y;
                if (y % 2 == 0) {
                    x = -1;
                } else {
                    x = BLOCKPERLINE;
                }
            } else {
                g2d.setPaint(new Color(200, 200, 200));
                if (y % 2 == 0) {
                    g2d.fillRect(XOFFSET + (BLOCKWIDTH + LINKWIDTH) * x + BLOCKWIDTH,
                            YOFFSET + (BLOCKHEIGHT + LINKWIDTH) * y + (BLOCKHEIGHT / 2 - LINKHEIGHT / 2), LINKWIDTH,
                            LINKHEIGHT);
                } else {
                    g2d.fillRect(XOFFSET + (BLOCKWIDTH + LINKWIDTH) * x - LINKWIDTH,
                            YOFFSET + (BLOCKHEIGHT + LINKWIDTH) * y + (BLOCKHEIGHT / 2 - LINKHEIGHT / 2), LINKWIDTH,
                            LINKHEIGHT);
                }
                g2d.setPaint(new Color(150, 150, 150));
            }
            // on dessine en Z
            if (y % 2 == 0) {
                ++x;
            } else {
                --x;
            }
        }

    }

    public void loadNewBlockChain(BlockChain bc) {
        this.blockchain = bc;
        this.paintComponent(this.getGraphics());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

}