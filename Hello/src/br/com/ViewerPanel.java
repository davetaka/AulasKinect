package br.com;


// ViewerPanel.java
// Andrew Davison, August 2011, ad@fivedots.psu.ac.th
// Version 1; copy to parent directory to use with OpenNIViewer.java

/* Based on OpenNI's SimpleViewer example
     Initialize OpenNI with SAMPLE_XML_FILE;
     Display a grayscale depthmap (darker means further away, although black
     means "too close" for a depth value to be calculated).
*/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.nio.ShortBuffer;
import java.text.DecimalFormat;

import javax.swing.JPanel;

import org.openni.Context;
import org.openni.DepthGenerator;
import org.openni.DepthMetaData;
import org.openni.GeneralException;
import org.openni.OutArg;
import org.openni.ScriptNode;
import org.openni.StatusException;




public class ViewerPanel extends JPanel implements Runnable
{
  private static final int MAX_DEPTH_SIZE = 10000;  

  private static final String SAMPLE_XML_FILE = "c:/Teste/SamplesConfig.xml";

  // image vars
  private byte[] imgbytes;
  private BufferedImage image = null;    // for displaying the depth image
  private int imWidth, imHeight;
  private float histogram[];   // for the depth values
  private int maxDepth = 0;    // largest depth value

  private volatile boolean isRunning;
  
  // used for the average ms processing information
  private int imageCount = 0;
  private long totalTime = 0;
  private DecimalFormat df;
  private Font msgFont;

  // OpenNI
  private Context context;
  private DepthMetaData depthMD;


  public ViewerPanel()
  {
    setBackground(Color.WHITE);

    df = new DecimalFormat("0.#");  // 1 dp
    msgFont = new Font("SansSerif", Font.BOLD, 18);

    try {
      OutArg<ScriptNode> scriptNode = new OutArg<ScriptNode>();
      context = Context.createFromXmlFile(SAMPLE_XML_FILE, scriptNode);

      DepthGenerator depthGen = DepthGenerator.create(context);
      depthMD = depthGen.getMetaData();
           // use depth metadata to access depth info (avoids bug with DepthGenerator)
    }
    catch (GeneralException e) {
      System.out.println(e);
      System.exit(1);
    }

    histogram = new float[MAX_DEPTH_SIZE];

    imWidth = depthMD.getFullXRes();
    imHeight = depthMD.getFullYRes();
    System.out.println("Image dimensions (" + imWidth + ", " +
                                              imHeight + ")");

    // create empty image object of correct size and type
    imgbytes = new byte[imWidth * imHeight];
    image = new BufferedImage(imWidth, imHeight, BufferedImage.TYPE_BYTE_GRAY);

    new Thread(this).start();   // start updating the panel's image
  } // end of ViewerPanel()


  public Dimension getPreferredSize()
  { return new Dimension(imWidth, imHeight); }


  public void run()
  /* update and display the depth image whenever the context
     is updated.
  */
  {
    isRunning = true;
    while (isRunning) {
      try {
        context.waitAnyUpdateAll();
      }
      catch(StatusException e)
      {  System.out.println(e); 
         System.exit(1);
      }
      long startTime = System.currentTimeMillis();
      //updateDepthImage();
      imageCount++;
      totalTime += (System.currentTimeMillis() - startTime);
      repaint();
    }

    // close down
    try {
      context.stopGeneratingAll();
    }
    catch (StatusException e) {}
    context.release();
    System.exit(0);
  }  // end of run()


  public void closeDown()
  {  isRunning = false;  } 


  private void updateDepthImage()
  /* build a new histogram of depth grayscales. and convert it to
     image pixels  */
  {
    ShortBuffer depthBuf = depthMD.getData().createShortBuffer();
    calcHistogram(depthBuf);   // convert depths to grayscales
    depthBuf.rewind();

    // store grayscale at correct (1D) position in imgbytes[] pixel array
    while (depthBuf.remaining() > 0) {
      int pos = depthBuf.position();
      short depth = depthBuf.get();
      imgbytes[pos] = (byte) histogram[depth];    // values will be 0-255
    }
  }  // end of updateDepthImage()



  private void calcHistogram(ShortBuffer depthBuf)
  // convert depths to grayscales
  {
    // reset histogram[]
    for (int i = 0; i <= maxDepth; i++)
      histogram[i] = 0;

    // record number of different depths in histogram[];
    // each depth (an integer mm value) is used as an index into the array
    int numPoints = 0;
    maxDepth = 0;
    while (depthBuf.remaining() > 0) {
      short depthVal = depthBuf.get();
      if (depthVal > maxDepth)
        maxDepth = depthVal;
      if ((depthVal != 0)  && (depthVal < MAX_DEPTH_SIZE)){    // skip histogram[0]
        histogram[depthVal]++;
        numPoints++;
      }
    }
    // System.out.println("No. of numPoints: " + numPoints);
    // System.out.println("Maximum depth: " + maxDepth);

    // convert into a cummulative depth count (skipping histogram[0])
    for (int i = 1; i <= maxDepth; i++)
      histogram[i] += histogram[i - 1];

    /* convert cummulative depth into grayscales (0-255)
        - darker means further away, although black
          means "too close" for a depth value to be calculated).
    */
    if (numPoints > 0) {
      for (int i = 1; i <= maxDepth; i++)   // skip histogram[0]
        histogram[i] = (int) (256 * (1.0f - (histogram[i] / (float) numPoints)));
    }
  }  // end of calcHistogram()





  public void paintComponent(Graphics g)
  // Draw the depth image and statistics info
  { 
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    // convert image pixel array into an image
    DataBufferByte dataBuffer = new DataBufferByte(imgbytes, imWidth * imHeight);
    Raster raster = Raster.createPackedRaster(dataBuffer, imWidth, imHeight, 8, null);
    image.setData(raster);
    if (image != null)
      g2.drawImage(image, 0, 0, this);

    
  }

} // end of ViewerPanel class

