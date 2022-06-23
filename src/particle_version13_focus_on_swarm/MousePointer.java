package particle_version13_focus_on_swarm;

import dep.Model;
import dep.POGL;
import dep.Vektor2D;
import org.lwjgl.input.Mouse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MousePointer extends BewegendesObjekt {
    Model object = null;


    public MousePointer(Vektor2D position, Vektor2D velocity, int radius, float r, float g, float b) {
        super(position, velocity);
        loadObjectVierecke("res/kescher.obj");
        object.size = 0.05f;
    }

    //@Override
    public void render() {
        Vektor2D mousepos = new Vektor2D(Mouse.getX(), Mouse.getY());
        POGL.renderMouseModel((float) mousepos.x, (float) mousepos.y, object);
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
