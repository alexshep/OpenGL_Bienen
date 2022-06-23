package particle_version13_focus_on_swarm;

import dep.*;
import org.lwjgl.opengl.Display;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.*;

public class AgentSystem extends LWJGLBasisFenster {
    private ObjektManager agentenSpielwiese;
    private double runningAverageFrameTime = 1 / 60, avgRatio = 0.75;
    private long last = System.nanoTime();
    private MousePointer mouse;
    private int ShaderProgramm;
    private int uniform_fragShader_tex1;
	private static String fragShaderSource = ""
			+ "uniform sampler2D tex1;"
			+ "void main() { "
			+ "   gl_FragColor =  texture2D(tex1, gl_TexCoord[0].st); " + "}";


    public AgentSystem(String title, int width, int height) {
        super(title, width, height);
        WIDTH = width;
        HEIGHT = height;
        initDisplay();
        agentenSpielwiese = ObjektManager.getExemplar();
        erzeugeAgenten(20);
        erzeugeMausPointer();
    }

    public static void main(String[] args) {
        new AgentSystem("CGV2 Beleg",
                1600, 900).start();
    }

    private void erzeugeAgenten(int anz) {
        Random rand = ThreadLocalRandom.current();

        for (int i = 0; i < anz; i++) {
            Agent agent = new Agent(
                    new Vektor2D(rand.nextInt(WIDTH), rand.nextInt(HEIGHT)), //Integer zwischen 0 und max Bildhöhe/-breite
                    new Vektor2D(rand.nextFloat() * 1, rand.nextFloat() * 1), 10, 1f, 1f, 1f);
            agent.setVerhalten(new VerhaltenAgent(agent));
            agent.setObjektManager(agentenSpielwiese);
            agentenSpielwiese.registrierePartikel(agent);
        }
    }

    private void erzeugeMausPointer() {
        Random rand = ThreadLocalRandom.current();

        mouse = new MousePointer(
                new Vektor2D(rand.nextInt(WIDTH), rand.nextInt(HEIGHT)), new Vektor2D(rand.nextFloat() * 1, rand.nextFloat() * 1), 10, 0.5f, 0.2f, 0.4f);
    }

    public int getCurrFPS() {
        return (int) (1 / runningAverageFrameTime);
    }

    private void prepareShader() {
        ShaderProgramm = glCreateProgram();

        int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragShader, fragShaderSource);
        glCompileShader(fragShader);
        System.out.println(glGetShaderInfoLog(fragShader, 1024));
        glAttachShader(ShaderProgramm, fragShader);

        glLinkProgram(ShaderProgramm);
        uniform_fragShader_tex1 = glGetUniformLocation(ShaderProgramm, "tex1");
        glUseProgram(ShaderProgramm);

        ShaderUtilities.testShaderProgram(ShaderProgramm);
    }

    @Override
    public void renderLoop() {
        //glEnable(GL_DEPTH_TEST);

/*		while (!Display.isCloseRequested()) {
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
			mouse.render();
			Display.update();
		}*/

        prepareShader();
        FrameBuffers fbos = new FrameBuffers();
        FrameBuffers fbosBoden = new FrameBuffers();
        FrameBuffers fbosprim = new FrameBuffers();
        int tex_QUAPPE = 0;
        int tex_FISCH = 0;
        int tex_Futter = 0;
        int tex_Stein = 0;
        int tex_Pflanze = 0;
        int tex_Sand = 0;
        int tex_buffer = 0;
        int tex_BodenPrim = 0;
        int tex_BodenModel = 0;

        // einladen der Texturen
        try {
            tex_Sand = TexturLoader.loadTexture("res/Sand.png");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH, HEIGHT, 0, -100, 100);
        glMatrixMode(GL_MODELVIEW);
        glDisable(GL_DEPTH_TEST);

        while (!Display.isCloseRequested()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            fbosBoden.bindFrameBuffer();

            glLoadIdentity();
            glBindTexture(GL_TEXTURE_2D, tex_Sand);
            POGL.renderTeich();

            fbosBoden.unbindCurrentFrameBuffer();


            tex_BodenModel = fbosBoden.getTexture();


            fbosprim.bindFrameBuffer();

            glLoadIdentity();
            glBindTexture(GL_TEXTURE_2D, tex_Sand);
            POGL.renderTeich();


            long now = System.nanoTime();
            double diff = (now - last) / 1e9;
            runningAverageFrameTime = avgRatio * runningAverageFrameTime + (1 - avgRatio) * diff;
            last = now;


            fbos.bindFrameBuffer(); // Framebuffer hinzu
            glLoadIdentity();

            glUseProgram(ShaderProgramm);

            //Teich

            glBindTexture(GL_TEXTURE_2D, tex_BodenModel);


            glTranslated(Display.getWidth() / 2, Display.getHeight() / 2, -20);
            glRotatef(180, 1, 0, 0);

            glBegin(GL_QUADS);
            glTexCoord2f(0, 0);
            glVertex3f(-Display.getWidth() / 2, -Display.getHeight() / 2, 0.0f);
            glTexCoord2f(1, 0);
            glVertex3f(Display.getWidth() / 2, -Display.getHeight() / 2, 0.0f);
            glTexCoord2f(1, 1);
            glVertex3f(Display.getWidth() / 2, Display.getHeight() / 2, 0.0f);
            glTexCoord2f(0, 1);
            glVertex3f(-Display.getWidth() / 2, Display.getHeight() / 2, 0.0f);
            glEnd();


            for (int i = 1; i <= agentenSpielwiese.getAgentSize(); i++) {
                Agent aktAgent = agentenSpielwiese.getAgent(i);

                aktAgent.render();
                aktAgent.update(diff);
            }

			mouse.render();


			fbos.unbindCurrentFrameBuffer();// framebuffer weg
            glBindTexture(GL_TEXTURE_2D, 0);// fertig mit texturen

            tex_buffer = fbos.getTexture();


            glActiveTexture(GL_TEXTURE0); // activate texture unit 0
            glBindTexture(GL_TEXTURE_2D, tex_buffer); // bind texture
            glUniform1i(uniform_fragShader_tex1, 0); // inform the shader to use texture unit 0

            glLoadIdentity();

            glTranslated(Display.getWidth() / 2, Display.getHeight() / 2, 0);
            glRotatef(180, 1, 0, 0);

            glBegin(GL_QUADS);
            glTexCoord2f(0, 0);
            glVertex3f(-Display.getWidth() / 2, -Display.getHeight() / 2, 0.0f);
            glTexCoord2f(1, 0);
            glVertex3f(Display.getWidth() / 2, -Display.getHeight() / 2, 0.0f);
            glTexCoord2f(1, 1);
            glVertex3f(Display.getWidth() / 2, Display.getHeight() / 2, 0.0f);
            glTexCoord2f(0, 1);
            glVertex3f(-Display.getWidth() / 2, Display.getHeight() / 2, 0.0f);
            glEnd();

            glUseProgram(0);
            Display.update();

        }
    }
}