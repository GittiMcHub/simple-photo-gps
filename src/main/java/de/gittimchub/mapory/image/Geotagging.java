package de.gittimchub.mapory.image;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Geotagging {

    public static final String TEMP_SUFFIX = ".tmp";

    public static double[] getCoordinates(String path) throws ImageReadException, IOException {
        File jpeg = new File(path);
        final ImageMetadata metadata = Imaging.getMetadata(jpeg);
        final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
        TiffImageMetadata.GPSInfo gps = jpegMetadata.getExif().getGPS();
        double latitudeAsDegreesNorth = gps.getLatitudeAsDegreesNorth();
        double longitudeAsDegreesEast = gps.getLongitudeAsDegreesEast();
        return new double[]{latitudeAsDegreesNorth,longitudeAsDegreesEast};
    }

    public static void updateGeoTag(String path, double latitude, double longitude) throws IOException, ImageReadException, ImageWriteException {

        File jpeg = new File(path);

        TiffOutputSet outputSet = null;

        final ImageMetadata metadata = Imaging.getMetadata(jpeg);
        final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
        if (jpegMetadata != null) {
            // note that exif might be null if no Exif metadata is found.
            final TiffImageMetadata exif = jpegMetadata.getExif();
            if (exif != null) {
                outputSet = exif.getOutputSet();
            }
        }
        if (outputSet == null) {
            outputSet = new TiffOutputSet();
        }
        outputSet.setGPSInDegrees(longitude,latitude);

        FileOutputStream fos = new FileOutputStream(path + TEMP_SUFFIX);
        OutputStream os = new BufferedOutputStream(fos);

        new ExifRewriter().updateExifMetadataLossless(jpeg,os, outputSet);

        Files.move(new File(path + TEMP_SUFFIX).toPath(), jpeg.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

}
