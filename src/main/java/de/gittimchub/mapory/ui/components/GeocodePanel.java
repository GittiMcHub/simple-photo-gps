package de.gittimchub.mapory.ui.components;

import de.gittimchub.mapory.image.Geotagging;
import de.gittimchub.mapory.ui.SelectionRegistry;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class GeocodePanel extends JPanel {

    JLabel lbLatLon;
    JTextField tfLatLon;
    JButton btGeocode;

    public GeocodePanel(){
        lbLatLon = new JLabel("Format: Latitude , Longitude");
        tfLatLon = new JTextField();
        btGeocode = new JButton("Geocode");
        btGeocode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(String selectedFilePath : SelectionRegistry.getInstance().getFiles().stream().map(File::getAbsolutePath).collect(Collectors.toList())){
                    String[] split = tfLatLon.getText().split(",");
                    System.out.println( tfLatLon.getText());
                    System.out.println("GEOCODEPANEL | " + selectedFilePath);
                    if(split.length == 2){
                        try {
                            double lat = Double.parseDouble(split[0].trim());
                            double lon = Double.parseDouble(split[1].trim());
                            System.out.println("GEOCODEPANEL | wirting: " + lat + "/" + lon);
                            Geotagging.updateGeoTag(selectedFilePath,lat, lon);
                        } catch (IOException | ImageReadException | ImageWriteException ioException) {
                            ioException.printStackTrace();
                        }
                        SelectionRegistry.getInstance().removeAll();
                    }
                }
            }
        });

        this.setLayout(new GridLayout(1,3));
        this.add(lbLatLon);
        this.add(tfLatLon);
        this.add(btGeocode);
    }


}
