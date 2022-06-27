package dep;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import java.nio.ByteBuffer;

public class FrameBuffers {

	protected static final int REFLECTION_WIDTH = 1920;
	private static final int REFLECTION_HEIGHT = 800;

	private int FrameBuffer;
	private int Texture;


	public FrameBuffers() {//call when loading the game
		initialiseFrameBuffer();
	}

	public void bindFrameBuffer() {//call before rendering to this FBO
		bindFrameBuffer(FrameBuffer,REFLECTION_WIDTH,REFLECTION_HEIGHT);
	}

	
	public void unbindCurrentFrameBuffer() {//call to switch to default frame buffer
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	public int getTexture() {//get the resulting texture
		return Texture;
	}


	private void initialiseFrameBuffer() {
		FrameBuffer = createFrameBuffer();
		Texture = createTextureAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);
		
		unbindCurrentFrameBuffer();
	}
	

	
	private void bindFrameBuffer(int frameBuffer, int width, int height){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, width, height);
	}


	private int createFrameBuffer() {
		int frameBuffer = GL30.glGenFramebuffers();
		//generate name for frame buffer
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		//create the framebuffer
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		//indicate that we will always render to color attachment 0
		
		
		return frameBuffer;
	}

	private int createTextureAttachment( int width, int height) {
		int texture = GL11.glGenTextures();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height,
				0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
				texture, 0);
		return texture;
	}
}

