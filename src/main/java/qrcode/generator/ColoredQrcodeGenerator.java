package qrcode.generator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

/**
 * 
 * @author Samson N.
 *
 */
public class ColoredQrcodeGenerator {

	public static void main(String[] args) {
		String data = "Je suis Samson et je fais des QR Code en Java et en couleur.";
		 
		 
		// encode &  write in a file
	        try {
	        	ByteMatrix matrix = generateMatrix(data);
				var outputFileName = "E:/qrcode.png";
		        writeImage(outputFileName, "png", matrix, 400);
			} catch (Exception e) {
				e.printStackTrace();
			}

	}
	
	/**
	 * 
	 * @param data
	 * @return
	 * @throws WriterException
	 */
	 private static ByteMatrix generateMatrix(final String data) throws WriterException {
	        QRCode qr = new QRCode();
	        qr = Encoder.encode(data, ErrorCorrectionLevel.Q);
	        
	        ByteMatrix matrix = qr.getMatrix();
	        return matrix;
	    }
	 
	 /**
	  * 
	  * @param outputFileName
	  * @param imageFormat
	  * @param matrix
	  * @param size
	  */
	 private static void writeImage(String outputFileName, String imageFormat, ByteMatrix matrix, final int size) {

	        // Java 2D Traitement de Area
	        // Futurs modules
	        Area a = new Area();
		 
		 	Area module = new Area(new Rectangle.Float(0, 0, 1, 1));
	        
	        /*Area module = new Area(new Rectangle2D.Float(0.05f, 0.05f, 0.9f, 0.9f)); // --> GridQrcodeGenerator */
	     
	        /*Area module = new Area(new RoundRectangle2D.Float(0, 0, 0.9f, 0.9f, 1f, 1f)); // --> RoundedQrcodeGenerator */
		 	

	        // Deplacement du module
	        AffineTransform at = new AffineTransform(); 
	        int width = matrix.getWidth();
	        for (int i = 0; i < width; i++) {
	            for (int j = 0; j < width; j++) {
	                if (matrix.get(j, i) == 1) {
	                    // Ajout du module
	                    a.add(module);
	                }
	                // Decalage a droite
	                at.setToTranslation(1, 0); 
	                module.transform(at);
	            }

	            // Ligne suivante
	            at.setToTranslation(-width, 1); 
	            module.transform(at);
	        }

	        // Agrandissement de l'Area pour le remplissage de l'image
	        double ratio = size / (double) width;

	        // Quietzone : 4 modules de bordures autour du QR Code (zone vide pour bien identifier le code dans la page imprimee)
	        double adjustment = width / (double) (width + 8);
	        ratio = ratio * adjustment;
	        at.setToTranslation(4, 4); 
	        a.transform(at);

	        // On agrandit le tour a la taille souhaitee.
	        at.setToScale(ratio, ratio); 
	        a.transform(at);
	        
	     // Java 2D Traitement l'image
	        BufferedImage im = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
	        Graphics2D g = (Graphics2D) im.getGraphics();

	        // Modules verts
	        Color vert = new Color(0xFFA32E);
	        g.setPaint(vert);

	        g.setBackground(new Color(0xFFFFFF));

	        // Fond blanc
	        g.clearRect(0, 0, size, size);

	        // Remplissage des modules
	        g.fill(a);
	        
	        
	        
	        /*
	     // --> TransColoredQrcodeGenerator
		 	// Color couleur1 = new Color(0xFFFFFF);
	        // g.setPaint(couleur1);
	        Color couleur1 = new Color(0xAFC828);
	        Color couleur2 = new Color(0x606640);
	        // Debut et fin du gradient
	        float[] fractions = { 0.0f, 1.0f }; 
	        Color[] colors = { couleur1, couleur2 };
	        g.setPaint(new RadialGradientPaint(size / 2, size / 2, size / 2, fractions, colors));
	        
	        */
	        
	        
	     // Ecriture sur le disque
	        File f = new File(outputFileName);
	        f.setWritable(true);
	        try {
				ImageIO.write(im, imageFormat, f);
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	 
	 /*
	  * Une des particularités des QR codes est qu'ils restent lisibles même lorsqu'ils sont déchirés ou abîmés, un peu 
	  * comme un CD qui reste utilisable lorsqu'il est rayé. Pour cela, la « norme » des QR codes implique que les données 
	  * soient répliquées. Elle définit quatre niveaux de réplication : L (7 %), M (15 %), Q (25 %) et H (30 %).
		Bien entendu, plus le niveau de réplication est élevé et plus gros devra être le QR code généré pour tout contenir.
	Par défaut, Zxing génère des QR codes avec le niveau « L » (low). Dans l'exemple j'ai utilisé le niveau « Q » (quality)
	 et on constate donc une plus grande densité des modules. Pour que ça saute aux yeux, j'ai utilisé du « bleu mauve » pour
	  cette version.
	  
	  */

}
