package dep;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.io.*;

import static org.lwjgl.opengl.GL11.*;

// dep.POGL = "Primitives of OpenGL"
public class POGL {
	private POGL() {
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


	public static void renderModelWithForces(float x, float y, int radius, Vektor2D velocity, Vektor2D acceleration, Model object) {
		glLoadIdentity();
		glTranslated((x/Display.getWidth())*2-1, (y/Display.getHeight())*2-1, 0);
		glRotatef( 90, 1, 0, 0);
		glScaled(object.size, object.size, object.size);
		glColor3d(0.1, .3 , 0.5);
		renderObjectVierecke(object);
	}

	public static void renderMouseModel(float x, float y, Model object) {
		glLoadIdentity();
		glTranslated((x / Display.getWidth()) * 2 - 1, (y / Display.getHeight()) * 2 - 1, 0);
		//System.out.println(position.x+"   "+position.y);
		glRotatef(90, 1, 0.5f, 0);
		glScaled(object.size, object.size, object.size);
		glColor3d(0.1, .3, 0.5);
		renderObjectVierecke(object);
	}
}
