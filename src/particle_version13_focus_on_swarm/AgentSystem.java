package particle_version13_focus_on_swarm;

import dep.LWJGLBasisFenster;
import dep.Vektor2D;
import org.lwjgl.opengl.Display;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import static org.lwjgl.opengl.GL11.*;

public class AgentSystem extends LWJGLBasisFenster {
	private ObjektManager agentenSpielwiese;
	private double runningAverageFrameTime = 1/60, avgRatio = 0.75;
	private long last = System.nanoTime();


	public AgentSystem(String title, int width, int height) {
        super(title, width, height);
		WIDTH = width;
		HEIGHT = height;
		initDisplay();
		agentenSpielwiese = ObjektManager.getExemplar();
		erzeugeAgenten(1);
	}


	private void erzeugeAgenten(int anz) {
		Random rand = ThreadLocalRandom.current();

		for (int i = 0; i < anz; i++) {
			Agent agent = new Agent(
					new Vektor2D(rand.nextInt(WIDTH), rand.nextInt(HEIGHT)), //Integer zwischen 0 und max Bildhöhe/-breite
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
		//glEnable(GL_DEPTH_TEST);

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


			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//			//glClear löst das Problem, dass beim Neuzeichnen die alten Darstellungen nicht gelöscht werden
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
			glLoadIdentity();
			glMatrixMode(GL_MODELVIEW);
			glDisable(GL_DEPTH_TEST);



			for (int i = 1; i <= agentenSpielwiese.getAgentSize(); i++) {
				Agent aktAgent = agentenSpielwiese.getAgent(i);
								
				aktAgent.render();
				aktAgent.update(diff);
			}
			Display.update();
		}
	}

	public static void main(String[] args) {
       new AgentSystem("CGV2 Beleg",
             1600, 900).start();
	}
}