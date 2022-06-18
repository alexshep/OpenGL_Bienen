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


	public Agent(Vektor2D position, Vektor2D velocity, int radius, float r, float g, float b) {
		super(position, velocity);
		this.id = ++objCounter;

		loadObjectVierecke("res/TorusQuads.obj");
		object.size = 1.5f;

		setMass(1);
		setMaxSpeed(100);
		setMaxTurnRate(15);
		setSwarmDistanz(100);

		setWegHistorie(new Weg2DDynamisch(20));
	}

	public void setObjektManager(ObjektManager objektManager) {
		this.objektManager = objektManager;
	}

	@Override
	public void render() {
		//POGL.renderSwarmObjectWithForces((float) position.x, (float) position.y, 10, velocity, getLastAcceleration());

		POGL.renderModelWithForces((float) position.x, (float) position.y, 10, velocity, getLastAcceleration(), object);
	}

	//eigener Bullshit

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
