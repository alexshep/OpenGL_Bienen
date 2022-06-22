package dep;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.io.*;

import static org.lwjgl.opengl.GL11.*;

// dep.POGL = "Primitives of OpenGL"
public class POGL {
	private POGL() {
	}

	private static Torus torus;

	public static void clearBackgroundWithColor(float r, float g, float b, float a) {
		glClearColor(r, g, b, a);
		glClear(GL_COLOR_BUFFER_BIT);
	}



	public static void renderKreis(float x, float y, float step, float radius) {
		glBegin(GL_TRIANGLE_FAN);
		glVertex2f(x, y);
		for (int angle = 0; angle < 360; angle += step)
			glVertex2f(x + (float) Math.sin(angle) * radius, y + (float) Math.cos(angle) * radius);
		glEnd();
	}


	public static void createTorus(double x, double y, double z, double r, double thickness, double steps) {
		torus = new Torus(x, y, z, r, thickness, steps);
	}

	public static void renderTorus() {
		if (torus == null)
			createTorus(0, 0, 0, 1, 0.3, 20.0f);

		torus.render();
	}


	public static Model loadModelVierecke(File file) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Model model = new Model();
		String line;
		String[] lineElements;
		float x, y, z;
		Vector4f vertexIndices = null;
		Vector4f texCoordsIndices = null;
		Vector4f normalIndices = null;

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("v ")) {
				lineElements = line.split(" ");
				x = Float.valueOf(lineElements[1]);
				y = Float.valueOf(lineElements[2]);
				z = Float.valueOf(lineElements[3]);
				model.vertices.add(new Vector3f(x, y, z));
			} else if (line.startsWith("vn ")) { 
				lineElements = line.split(" ");
				x = Float.valueOf(lineElements[1]);
				y = Float.valueOf(lineElements[2]);
				z = Float.valueOf(lineElements[3]);
				model.normals.add(new Vector3f(x, y, z));
			} else if (line.startsWith("vt ")) {
				lineElements = line.split(" ");
				x = Float.valueOf(lineElements[1]);
				y = Float.valueOf(lineElements[2]);
				model.texCoords.add(new Vector2f(x, y));
			} else if (line.startsWith("f ")) {
				vertexIndices = null;
				texCoordsIndices = null;
				normalIndices = null;

				lineElements = line.split(" ");
				if (line.contains("/") && lineElements[1].split("/").length > 1) {
					vertexIndices = new Vector4f(
							Float.valueOf(lineElements[1].split("/")[0]),
							Float.valueOf(lineElements[2].split("/")[0]), 
							Float.valueOf(lineElements[3].split("/")[0]),
							Float.valueOf(lineElements[4].split("/")[0]));

					texCoordsIndices = new Vector4f(
							Float.valueOf(line.split(" ")[1].split("/")[1]),
							Float.valueOf(lineElements[2].split("/")[1]), 
							Float.valueOf(lineElements[3].split("/")[1]),
							Float.valueOf(lineElements[4].split("/")[1]));

					if (lineElements[1].split("/").length == 3) {
						normalIndices = new Vector4f(
								Float.valueOf(lineElements[1].split("/")[2]),
								Float.valueOf(lineElements[2].split("/")[2]),
								Float.valueOf(lineElements[3].split("/")[2]),
								Float.valueOf(lineElements[4].split("/")[2]));
					}
				} else {
					// nur vier Vertices für ein Viereck vorhanden
					vertexIndices = new Vector4f(
							Float.valueOf(lineElements[1]), 
							Float.valueOf(lineElements[2]),
							Float.valueOf(lineElements[3]),
							Float.valueOf(lineElements[4]));
				}
				model.facesQuads.add(new FaceQuad(vertexIndices, texCoordsIndices, normalIndices));
			}
		}
		reader.close();
		return model;
	}


	
	public static void renderObjectVierecke(Model model) {
		glBegin(GL_QUADS);
		for (FaceQuad face : model.facesQuads) {
			if (face.normal != null) {
				Vector3f n1 = model.normals.get((int) face.normal.x - 1);
				glNormal3f(n1.x, n1.y, n1.z);
			}
			if (face.texCoords != null) {
				Vector2f t1 = model.texCoords.get((int) face.texCoords.x - 1);
				glTexCoord2f(t1.x, t1.y);
			}
			Vector3f v1 = model.vertices.get((int) face.vertex.x - 1);
			glVertex3f(v1.x, v1.y, v1.z);

			if (face.normal != null) {
				Vector3f n2 = model.normals.get((int) face.normal.y - 1);
				glNormal3f(n2.x, n2.y, n2.z);
			}
			if (face.texCoords != null) {
				Vector2f t2 = model.texCoords.get((int) face.texCoords.y - 1);
				glTexCoord2f(t2.x, t2.y);
			}
			Vector3f v2 = model.vertices.get((int) face.vertex.y - 1);
			glVertex3f(v2.x, v2.y, v2.z);

			if (face.normal != null) {
				Vector3f n3 = model.normals.get((int) face.normal.z - 1);
				glNormal3f(n3.x, n3.y, n3.z);
			}
			if (face.texCoords != null) {
				Vector2f t3 = model.texCoords.get((int) face.texCoords.z - 1);
				glTexCoord2f(t3.x, t3.y);
			}
			Vector3f v3 = model.vertices.get((int) face.vertex.z - 1);
			glVertex3f(v3.x, v3.y, v3.z);

			if (face.normal != null) {
				Vector3f n4 = model.normals.get((int) face.normal.w - 1);
				glNormal3f(n4.x, n4.y, n4.z);
			}
			if (face.texCoords != null) {
				Vector2f t4 = model.texCoords.get((int) face.texCoords.w - 1);
				glTexCoord2f(t4.x, t4.y);
			}
			Vector3f v4 = model.vertices.get((int) face.vertex.w - 1);
			glVertex3f(v4.x, v4.y, v4.z);

		}
		glEnd();
		glPopMatrix();
	}
	
	public static void renderObjectWithPath(float x, float y, float r, float g, float b, float a, int radius, Weg2DDynamisch path) {
		for (int j = path.getSize()-1; j >= 0 ; j--) {
			float anteil = 1-((float)j/path.getSize());
			glColor4f(r*anteil, g*anteil, b*anteil, 1f);
			renderKreis((float)path.getElement(j).x, (float)path.getElement(j).y, 5, radius);
		}

		glColor4f(r, g, b, a);
		renderKreis(x, y, 5, radius);
	}
	
	public static void renderPfeil(float x, float y, int off, float winkel, int size) {
		glLoadIdentity();
		glTranslated(x, y, 0);
		
		glRotatef(winkel, 0, 0, 1);
		glTranslated(off, 0, 0);
		glScaled(size, size, size);
		
		glBegin(GL_LINES);
		glVertex3d(  0f,  0f, 0);
		glVertex3d(-off/15., 0, 0);
		glEnd();

        glBegin(GL_TRIANGLES);
        glVertex3d(  0f,  .2f, 0);
        glVertex3d(  0f, -.2f, 0);
        glVertex3d( .5f,   0f, 0);
        glEnd();    		
	}
	
	public static void renderObjectWithForces(float x, float y, int radius, Vektor2D velocity, Vektor2D acceleration) {
		glLoadIdentity();
		glTranslated(x, y, 0);
		
		glColor4f(1, 1, 1, 1);
		renderKreis(0, 0, 5, radius);
		glColor4f(0, 0, 0, 1);
		renderKreis(0, 0, 5, radius-2);
		
		// *****************************************************************
		// Visualisierung der Geschwindigkeit
		// der Wert off soll die Geschwindigkeit durch einen größeren Abstand visualisieren
		int off = radius + 1 + (int)(velocity.length()/5);
		double winkel = LineareAlgebra.angleDegree(velocity, new Vektor2D(1,0));
		
		// da immer der kleinere Winkel zwischen den Vektoren geliefert wird, müssen
		// wir etwas korrigieren
		if (velocity.y<0)
			winkel = 180 + (180-winkel);

		glColor4f(1, 1, 0, 1);
		renderPfeil(x, y, off, (float)winkel, 15);
		// *****************************************************************
		
		// *****************************************************************
		// Visualisierung der Beschleunigung
		off = radius + 1 + (int)(acceleration.length()/10);
		winkel = LineareAlgebra.angleDegree(acceleration, new Vektor2D(1,0));
		if (acceleration.y<0)
			winkel = 180 + (180-winkel);

		glColor4f(1, 0, 0, 1);
		renderPfeil(x, y, off, (float)winkel, 15);
		// *****************************************************************
	}
	
	public static void renderSwarmObjectWithForces(float x, float y, int radius, Vektor2D velocity, Vektor2D acceleration) {
		glLoadIdentity();
		glTranslated(x, y, 0);
		
		glColor4f(1, 1, 1, 1);
		renderKreis(0, 0, 5, radius);
		glColor4f(0, 0, 0, 1);
		renderKreis(0, 0, 5, radius-2);
		
		// *****************************************************************
		// Visualisierung der Geschwindigkeit
		// der Wert off soll die Geschwindigkeit durch einen größeren Abstand visualisieren
		int off = radius + 1 + (int)(velocity.length()/5);
		double winkel = LineareAlgebra.angleDegree(velocity, new Vektor2D(1,0));
		
		// da immer der kleinere Winkel zwischen den Vektoren geliefert wird, müssen
		// wir etwas korrigieren
		if (velocity.y<0)
			winkel = 180 + (180-winkel);

		glColor4f(1, 1, 0, 1);
		renderPfeil(x, y, off, (float)winkel, 15);
		// *****************************************************************
		
		// *****************************************************************
		// Visualisierung der Beschleunigung
		off = radius + 1 + (int)(acceleration.length()/10);
		winkel = LineareAlgebra.angleDegree(acceleration, new Vektor2D(1,0));
		if (acceleration.y<0)
			winkel = 180 + (180-winkel);

		glColor4f(1, 0, 0, 1);
		renderPfeil(x, y, off, (float)winkel, 15);
		// *****************************************************************
	}
	


	public static void renderModelWithForces(float x, float y, int radius, Vektor2D velocity, Vektor2D acceleration, Model object) {
		glTranslated((float) x/1000, (float) y/1000, 0);
		//System.out.println(position.x+"   "+position.y);
		glRotatef( 90, 1, 0, 0);
		glScaled(1./object.size, 1./object.size, 1./object.size);
		glScaled(1./5, 1./5, 1./5);
		glColor3d(0.1, .3 , 0.1);

		//glColor4f(0.5f, 1, 0.3f, 0.7f);
		//renderKreis(0, 0, 5, radius);
		renderObjectVierecke(object);


//		// *****************************************************************
//		// Visualisierung der Geschwindigkeit
//		// der Wert off soll die Geschwindigkeit durch einen größeren Abstand visualisieren
//		int off = radius + 1 + (int)(velocity.length()/5);
//		double winkel = LineareAlgebra.angleDegree(velocity, new Vektor2D(1,0));
//
//		// da immer der kleinere Winkel zwischen den Vektoren geliefert wird, müssen
//		// wir etwas korrigieren
//		if (velocity.y<0)
//			winkel = 180 + (180-winkel);
//
//		glColor4f(1, 1, 0, 1);
//		renderPfeil(x, y, off, (float)winkel, 15);
//		// *****************************************************************
//
//		// *****************************************************************
//		// Visualisierung der Beschleunigung
//		off = radius + 1 + (int)(acceleration.length()/10);
//		winkel = LineareAlgebra.angleDegree(acceleration, new Vektor2D(1,0));
//		if (acceleration.y<0)
//			winkel = 180 + (180-winkel);
//
//		glColor4f(1, 0, 0, 1);
//		renderPfeil(x, y, off, (float)winkel, 15);
//		// *****************************************************************
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
