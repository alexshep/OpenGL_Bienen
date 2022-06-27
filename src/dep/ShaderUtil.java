package dep;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;


public class ShaderUtil {
	private static ByteBuffer infoBuffer = BufferUtils.createByteBuffer(1024);
	private static IntBuffer errorBuffer = BufferUtils.createIntBuffer(1);
	

	public static void testShaderProgram(int shader) {
		errorBuffer.rewind();
		glGetProgram(shader, GL_LINK_STATUS, errorBuffer);
		System.out.println(errorBuffer.get(0)==GL_TRUE?"OK":"ERROR");
		int error = errorBuffer.get(0);
		errorBuffer.put(0,1024);
		glGetProgramInfoLog(shader, errorBuffer, infoBuffer);		
		if (error!=GL_TRUE) 
		{
			byte bytes[] = new byte[1024];
			infoBuffer.get(bytes).rewind();
			System.err.println(new String(bytes, 0, errorBuffer.get(0)));
		}
	}
}
