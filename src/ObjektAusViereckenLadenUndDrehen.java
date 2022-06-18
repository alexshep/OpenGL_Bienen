import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class ObjektAusViereckenLadenUndDrehen extends LWJGLBasisFenster {
	Model object = null;
	
	public ObjektAusViereckenLadenUndDrehen(String fileName, float size) {
		JFrame f = new JFrame();
		Canvas c = new Canvas();
		f.add(c);
		f.setBounds(100, 100, 600, 600);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		loadObjectVierecke(fileName);		
		object.size = size;

		initDisplay(c);
	}

	public boolean loadObjectVierecke(String fileName) {
		try {
			object = POGL.loadModelVierecke(new File(fileName));
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public void renderLoop() {
	      //glPolygonMode(GL_FRONT_AND_BACK, GL_POINT);
	      glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	      //glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	      
	      long start = System.nanoTime();
	      while (!Display.isCloseRequested()) {
	         double t = (System.nanoTime() - start) / 1e9;
	         POGL.clearBackgroundWithColor(1, 1, 1, 1);

	         glLoadIdentity();
	         glFrustum(-1, 1, -1, 1, 4, 10);
	         glTranslated(0, 0, -5);
	         glRotatef((float) t * 100, 1, 0, 0);
	         glRotatef((float) t * 100, 0, 1, 0);
	         glScaled(1./object.size, 1./object.size, 1./object.size);
	         glScaled(1./5, 1./5, 1./5);
	         glColor3d(0, .7 + 0.3 * Math.sin(t), 0);
	         POGL.renderObjectVierecke(object);			
	         Display.update();
	      }
	}

	public static void main(String[] args) throws LWJGLException {
		//new ObjektAusViereckenLadenUndDrehen("src/kapitel01/loesungen/CubeQuads.obj", 0.5f).start();		//Würfel
		new ObjektAusViereckenLadenUndDrehen("res/TorusQuads.obj", 1.1f/*5*/).start();		//Element aus CGV1 Projekt
		//new ObjektAusViereckenLadenUndDrehen("src/kapitel01/loesungen/TorusQuads.obj", 40/*5*/).start();		//Torus
	}
}
