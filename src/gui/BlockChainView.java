package gui;

import java.awt.Color;
import java.awt.Dimension;
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

    private int blockSelected = -1;

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

        g2d.setPaint(new Color(150, 150, 150));
        int x = 0;
        int y = 0;
        for (int i = 0; i < blockCount; ++i) {
            g2d.fillRect(XOFFSET + (BLOCKWIDTH + LINKWIDTH) * x, YOFFSET + (BLOCKHEIGHT + LINKWIDTH) * y, BLOCKWIDTH,
                    BLOCKHEIGHT);
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
        this.blockSelected = -1;
        if (bc != null) {
            int lines = 1 + (bc.getSize() / BLOCKPERLINE);
            int width = 2 * XOFFSET + BLOCKWIDTH * BLOCKPERLINE + BLOCKPERLINE * LINKWIDTH;
            int height = 2 * YOFFSET + BLOCKHEIGHT * lines + lines * LINKWIDTH;
            setPreferredSize(new Dimension(width, height));
        }
        this.paintComponent(this.getGraphics());
    }

    private int blockAtPosition(int x, int y) {
        int xblk, yblk;
        int i = 0;
        boolean in = x > XOFFSET + i * (BLOCKWIDTH + LINKWIDTH)
                && x < XOFFSET + i * (BLOCKWIDTH + LINKWIDTH) + BLOCKWIDTH;
        while (i < BLOCKPERLINE && !in) {
            ++i;
            in = x > XOFFSET + i * (BLOCKWIDTH + LINKWIDTH)
                    && x < XOFFSET + i * (BLOCKWIDTH + LINKWIDTH) + BLOCKWIDTH;
        }
        if (!in) {
            return -1;
        }
        xblk = i;
        i = 0;
        in = y > YOFFSET + i * (BLOCKHEIGHT + LINKWIDTH) 
                && y < YOFFSET + i * (BLOCKHEIGHT + LINKWIDTH) + BLOCKHEIGHT;
        while (i < blockchain.getSize() && !in) {
            ++i;
            in = y > YOFFSET + i * (BLOCKHEIGHT + LINKWIDTH)
                    && y < YOFFSET + i * (BLOCKHEIGHT + LINKWIDTH) + BLOCKHEIGHT;
        }
        if (!in) {
            return -1;
        }
        yblk = i;
        int index = yblk * BLOCKPERLINE + xblk;
        if (index >= blockchain.getSize()) {
            return -1;
        }
        return index;
    }

    private void paintBlock(int index, Color c) {
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
        g2d.setPaint(c);

        int x = index % 16;
        int y = index / 16;
        g2d.fillRect(XOFFSET + (BLOCKWIDTH + LINKWIDTH) * x, YOFFSET + (BLOCKHEIGHT + LINKWIDTH) * y, BLOCKWIDTH,
                BLOCKHEIGHT);

    }

    private void selectBlock(int index) {
        if (blockSelected != -1) {
            paintBlock(blockSelected, new Color(150, 150, 150));
        }
        blockSelected = index;
        paintBlock(index, new Color(13, 152, 186));
    }

    public void clickBlock(int x, int y) {
        int blockIndex = blockAtPosition(x, y);
        if (blockIndex != -1) {
            if (blockIndex == blockSelected) {
                BlockView bv = new BlockView(blockchain.getBlockAtIndex(blockIndex));
                bv.setVisible(true);
            } else {
                selectBlock(blockIndex);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

}