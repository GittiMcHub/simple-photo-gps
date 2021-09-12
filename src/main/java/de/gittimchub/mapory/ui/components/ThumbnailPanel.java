package de.gittimchub.mapory.ui.components;

import de.gittimchub.mapory.ui.SelectionRegistry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.rmi.registry.Registry;

public class ThumbnailPanel extends JPanel {

    public static final Dimension size = new Dimension(120,120);



    public ThumbnailPanel(String path) throws IOException {
        super();

        this.updatePanel(path);
    }

    public void updatePanel(String path) throws MalformedURLException {
        File param = new File(path);
        if(param.isDirectory()){
            System.out.println("Directory..." + param.getName());
            File[] files = param.listFiles();

            if(files.length<=5){
                this.setLayout(new GridLayout(1,4));
            } else {
                this.setLayout(new GridLayout((int)(files.length / 4),4));
            }


            for(File f : files){
                if(f.isFile() && (f.getName().toLowerCase().endsWith(".jpg") || f.getName().toLowerCase().endsWith(".jpeg"))){
                    ImageIcon icon = createImageIcon(f.getPath(), f.getName());
                    JPanel thumbPanel = new JPanel();
                    thumbPanel.setLayout(new GridLayout(2,1));
                    thumbPanel.add(new JLabel(icon));
                    thumbPanel.add(new JLabel(f.getName()));
                    thumbPanel.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            SelectionRegistry.getInstance().add(f);
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {

                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {

                        }

                        @Override
                        public void mouseExited(MouseEvent e) {

                        }
                    });
                    this.add(thumbPanel);

                }
            }
        }
        this.revalidate();
        this.repaint();
    }



    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createImageIcon(String path,
                                        String description) throws MalformedURLException {
        java.net.URL imgURL = Paths.get(path).toUri().toURL();
        if (imgURL != null) {
            ImageIcon imageIcon = new ImageIcon(imgURL, description);
            return new ImageIcon(imageIcon.getImage().getScaledInstance((int) size.getWidth(),(int) size.getHeight(), Image.SCALE_DEFAULT));
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}
