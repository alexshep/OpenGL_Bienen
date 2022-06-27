package particle_version13_focus_on_swarm;

import dep.POGL;
import dep.Vektor2D;

public class Agent extends BewegendesObjekt {
	private static int objCounter = 0;
	public ObjektManager objektManager;

	boolean isALive;


	public Agent(Vektor2D position, Vektor2D velocity, int radius, float r, float g, float b) {
		super(position, velocity);
		this.id = ++objCounter;
		isALive = true;

		setMass(0.3);
		setMaxSpeed(300);
		setMaxTurnRate(150);
		setSwarmDistanz(30);

		setWegHistorie(new Weg2DDynamisch(20));
	}

	public void setObjektManager(ObjektManager objektManager) {
		this.objektManager = objektManager;
	}

	public Boolean getAgentStatus() {return isALive;}

	public void killAgent() {
		isALive = false;
	}

	@Override
	public void render() {
		POGL.renderSwarmObjectWithoutForcesllllmao((float) position.x, (float) position.y, 10, velocity, getLastAcceleration());
	}
}
