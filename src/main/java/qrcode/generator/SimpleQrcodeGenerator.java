package qrcode.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


/**
 * 
 * @author Samson N.
 *
 */
public class SimpleQrcodeGenerator {
	
	public static void main(String[] args) {
       	var data = "Petit Tutoriel sur la réalisation des QR Code Par Samson (en Python ou en meme Java)";

        // encode &  write in a file
        try {
			BitMatrix bitMatrix = generateMatrix(data, 400);
			var outputFileName = "E:/qrcode.png";
	        writeImage(outputFileName, "png", bitMatrix);
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
       
        
    }

	/**
	 * 
	 * @param data
	 * @param size
	 * @return
	 * @throws WriterException
	 */
    private static BitMatrix generateMatrix(String data, int size) throws WriterException {
        BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size);
        return bitMatrix;
    }
    
    /**
     * 
     * @param outputFileName
     * @param imageFormat
     * @param bitMatrix
     * @throws Exception
     */
    private static void writeImage(String outputFileName, String imageFormat, BitMatrix bitMatrix) throws Exception  {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFileName));
        MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, fileOutputStream);
        fileOutputStream.close();
    }

}
