package particle_version13_focus_on_swarm;

import dep.LineareAlgebra;
import dep.Vektor2D;
import org.lwjgl.input.Mouse;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Steuerungsverhalten {
	public Vektor2D acceleration;
	private Random zuf = ThreadLocalRandom.current();

	public Steuerungsverhalten() {
		acceleration = new Vektor2D(0, 0);
	}

	public void resetAcceleration() {
		acceleration.mult(0);
	}

	public Vektor2D mousePosition() {
		return new Vektor2D(Mouse.getX(), Mouse.getY());
	}
	
	public Vektor2D forceMousePosition(Vektor2D currentPosition) {
		Vektor2D mousePosition = mousePosition();
		mousePosition.sub(currentPosition);
		mousePosition.normalize();
		return mousePosition;
	}


	public Vektor2D separation(Agent me, double dist) {
		Vektor2D steeringForce = new Vektor2D(0, 0);
		for (int i = 0; i < me.objektManager.getAgentSize(); i++) {
			if (me.id == i)
				continue;

			BasisObjekt bObj = me.objektManager.getAgent(i);
			if (bObj instanceof Agent) {
				Agent bObjF = (Agent)bObj;
				if (LineareAlgebra.euklDistanz(me.position, bObjF.position) < dist)
					steeringForce.add(LineareAlgebra.sub(me.position, bObjF.position));
			}
		}
		LineareAlgebra.normalize(steeringForce);
		return steeringForce;
	}

	public Vektor2D alignment(Agent me, double dist) {
		Vektor2D steeringForce = new Vektor2D(0, 0);
		for (int i = 0; i < me.objektManager.getAgentSize(); i++) {
			if (me.id == i)
				continue;

			BasisObjekt bObj = me.objektManager.getAgent(i);
			if (bObj instanceof Agent) {
				Agent bObjF = (Agent)bObj;
				if (LineareAlgebra.euklDistanz(me.position, bObjF.position) < dist)
					steeringForce.add(bObjF.velocity);
			}
		}

		LineareAlgebra.normalize(steeringForce);
		return steeringForce;
	}

	public Vektor2D cohesion(Agent me, double dist) {
		Vektor2D steeringForce = new Vektor2D(0, 0);
		for (int i = 0; i < me.objektManager.getAgentSize(); i++) {
			if (me.id == i)
				continue;

			BasisObjekt bObj = me.objektManager.getAgent(i);
			if (bObj instanceof Agent) {
				Agent bObjF = (Agent)bObj;
				if (LineareAlgebra.euklDistanz(me.position, bObjF.position) < dist)
					steeringForce.add(LineareAlgebra.sub(bObjF.position, me.position));
			}
		}

		LineareAlgebra.normalize(steeringForce);
		return steeringForce;
	}
}
