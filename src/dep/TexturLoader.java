package dep;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class TexturLoader {

	
	public static int generateId()	
	{
		return glGenTextures();
	}
	
	public static int loadTexture(String pfad)	throws IOException
	{
		int texid= generateId();
		 
	
		BufferedImage img=ImageIO.read(new File(pfad));
		
		 int[]pixels=new int [img.getHeight()*img.getWidth()];
		 img.getRGB(0,0, img.getHeight(),img.getWidth(),pixels,0,img.getWidth());
		
		 ByteBuffer buffer= BufferUtils.createByteBuffer(img.getHeight()*img.getWidth()*4);
		 
		 int pixel=0;
		 for (int y=0;y<img.getHeight();y++)
		 {
			 for (int x=0;x<img.getWidth();x++)
			 {
				  pixel=pixels[y*img.getHeight()+x];
				  buffer.put((byte) ((byte) (pixel>>16) & 0xFF));
				  buffer.put((byte) ((pixel>>8) & 0xFF));
				  buffer.put((byte) (pixel & 0xFF));
				  buffer.put((byte) ((pixel>>24) & 0xFF));
			 }
		 }
		 buffer.flip();
		 
		 glBindTexture(GL_TEXTURE_2D,texid);
		 glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
		 glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
		 glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
		 glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
		 glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA8,img.getWidth(),img.getHeight(),0,GL_RGBA,GL_UNSIGNED_BYTE,buffer);
		 
		 
		 glBindTexture(GL_TEXTURE_2D,0);
		
		 return texid; 
	}
	
}
