package dep;

import static org.lwjgl.opengl.GL11.*;

// dep.POGL = "Primitives of OpenGL"
public class POGL {
	private POGL() {
	}


	public static void renderKreis(float x, float y, float step, float radius) {
		glBegin(GL_TRIANGLE_FAN);
		glVertex2f(x, y);
		for (int angle = 0; angle < 360; angle += step)
			glVertex2f(x + (float) Math.sin(angle) * radius, y + (float) Math.cos(angle) * radius);
		glEnd();
	}


	public static void renderSwarmObjectWithoutForcesllllmao(float x, float y, int radius, Vektor2D velocity, Vektor2D acceleration) {
		glLoadIdentity();
		glTranslated(x, y, 0);

		glColor4f(1, 1, 1, 1);
		renderKreis(0, 0, 5, radius);
		glColor4f(0, 0, 0, 1);
		renderKreis(0, 0, 5, radius-2);
	}



}
