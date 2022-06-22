package particle_version13_focus_on_swarm;

import dep.LWJGLBasisFenster;
import dep.Vektor2D;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.awt.geom.Line2D;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL11.*;

public class AgentSystem extends LWJGLBasisFenster {
	private ObjektManager agentenSpielwiese;
	private double runningAverageFrameTime = 1/60, avgRatio = 0.75;
	private long last = System.nanoTime();




	Vektor2D currentMousePositionPress;
	Vektor2D currentMousePositionRelease;
	int mouseDown = 0;


	public AgentSystem(String title, int width, int height) {
        super(title, width, height);
		initDisplay();
		agentenSpielwiese = ObjektManager.getExemplar();
		erzeugeAgenten(20);
	}


	private void erzeugeAgenten(int anz) {
		Random rand = ThreadLocalRandom.current();

		for (int i = 0; i < anz; i++) {
			Agent agent = new Agent(
					new Vektor2D(rand.nextInt(WIDTH/8), rand.nextInt(HEIGHT/8)), //Integer zwischen 0 und max Bildhöhe/-breite
					new Vektor2D(rand.nextFloat()*1, rand.nextFloat()*1), 10, 1f, 1f, 1f);
			agent.setVerhalten(new VerhaltenAgent(agent));
			agent.setObjektManager(agentenSpielwiese);
			agentenSpielwiese.registrierePartikel(agent);
		}
	}

	public int getCurrFPS() {
		return (int) (1 / runningAverageFrameTime);
	}

	@Override
	public void renderLoop() {
		glEnable(GL_DEPTH_TEST);

		while (!Display.isCloseRequested()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			long now = System.nanoTime();
			double diff = (now - last) / 1e9;
			runningAverageFrameTime = avgRatio * runningAverageFrameTime + (1 - avgRatio) * diff;
			last = now;

			glClearColor(0.95f, 0.65f, 0.75f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT);

			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, WIDTH, HEIGHT, 0, 0, 1);
			glMatrixMode(GL_MODELVIEW);
			glDisable(GL_DEPTH_TEST);


			for (int i = 1; i <= agentenSpielwiese.getAgentSize(); i++) {
				Agent aktAgent = agentenSpielwiese.getAgent(i);
								
				aktAgent.render();
				aktAgent.update(diff);
			}
			updateMouse();
			Display.update();
		}
	}



	public Vektor2D mousePosition() {
		return new Vektor2D(Mouse.getX(), Display.getDisplayMode().getHeight() - Mouse.getY());
	}

	public void updateMouse() {

		if (mouseDown == 0) {
			if (Mouse.isButtonDown(0) == true) {
				mouseDown = 1;
				currentMousePositionPress = mousePosition();
			}
		} else if (mouseDown == 1) {
			if (Mouse.isButtonDown(0) == false) {
				mouseDown = 0;
				currentMousePositionRelease = mousePosition();
				checkCollisions(currentMousePositionPress,currentMousePositionRelease);
			}
		}
	}

	public void checkCollisions(Vektor2D press, Vektor2D release){
		Line2D.Double check = new Line2D.Double();
		check.x1 = press.x;
		check.y1 = press.y;
		check.x2 = release.x;
		check.y2 = release.y;



		//Das funktioniert so überhaupt nicht. Ziel war es, für die aktuellen Positionen der Agenten zu prüfen, ob sie
		//auf der gezogenen Linie liegen. Allerdings finde ich keine Möglichkeit, die Position des Agenten zu ermitteln
		//und in einem Punkt zu speichern.

		for (int i = 1; i <= agentenSpielwiese.getAgentSize(); i++) {
//			Point2D agentlocation = new Point2D() {
//				setLocation(double position.x, position.y) {
//				}
//			//}
			//if (check.contains(agentlocation) {
				//Agent aktAgent = agentenSpielwiese.getAgent(i);
				//agentenSpielwiese.entfernePartikel(aktAgent);
			//}


			//agent.setPosition(new Vektor2D(-100, -100));


		}

	}


	public static void main(String[] args) {
       new AgentSystem("CGV2 Beleg",
             1600, 900).start();
	}
}
