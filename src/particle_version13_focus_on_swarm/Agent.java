package particle_version13_focus_on_swarm;

import dep.Model;
import dep.POGL;
import dep.Vektor2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Agent extends BewegendesObjekt {
	private static int objCounter = 0;
	public ObjektManager objektManager;
	Model object = null;
	boolean isALive;


	public Agent(Vektor2D position, Vektor2D velocity, int radius, float r, float g, float b) {
		super(position, velocity);
		this.id = ++objCounter;
		isALive = true;

		loadObjectVierecke("res/Quappe_tex.obj");
		object.size = 0.015f;

		setMass(0.5);
		setMaxSpeed(200);
		setMaxTurnRate(100);
		setSwarmDistanz(40);

		setWegHistorie(new Weg2DDynamisch(20));
	}



	public void setObjektManager(ObjektManager objektManager) {
		this.objektManager = objektManager;
	}

	@Override
	public void render() {
		POGL.renderModelWithForces((float) position.x, (float) position.y, 10, velocity, getLastAcceleration(), object);
	}



	public Boolean getAgentStatus() {return isALive;}

	public void killAgent() {
		isALive = false;
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
}