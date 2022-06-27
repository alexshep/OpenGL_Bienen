package particle_version13_focus_on_swarm;

import dep.*;
import org.lwjgl.opengl.Display;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

public class AgentSystem extends LWJGLBasisFenster {
    private ObjektManager agentenSpielwiese;
    private double runningAverageFrameTime = 1 / 60, avgRatio = 0.75;
    private long last = System.nanoTime();
    private MousePointer mouse;
    private int ShaderProgramm;
    private int uniform_fragShader_tex1;
	private static String fragShaderCode = ""
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


    private void prepareShader() {
        ShaderProgramm = glCreateProgram();

        int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragShader, fragShaderCode);
        glCompileShader(fragShader);
        System.out.println(glGetShaderInfoLog(fragShader, 1024));
        glAttachShader(ShaderProgramm, fragShader);

        glLinkProgram(ShaderProgramm);
        uniform_fragShader_tex1 = glGetUniformLocation(ShaderProgramm, "tex1");
        glUseProgram(ShaderProgramm);

        ShaderUtil.testShaderProgram(ShaderProgramm);
    }

    @Override
    public void renderLoop() {
        prepareShader();
        FrameBuffer fb = new FrameBuffer();
        FrameBuffer fbHintergrund = new FrameBuffer();


        int tex_Sand = 0;
        int tex_Biene = 0;
        int tex_buffer = 0;
        int tex_Hintergrund = 0;

        //Texturen laden
        try {
            tex_Sand = TexturLoader.loadTexture("res/Wiese.png");
            tex_Biene = TexturLoader.loadTexture("res/Biene.png");
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


            //Hintergrund auf ein Quad rendern, was den gesamten Bildschirm füllt

            fbHintergrund.bindFrameBuffer();

            glLoadIdentity();
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glBindTexture(GL_TEXTURE_2D, tex_Sand);
            POGL.renderWiese();

            fbHintergrund.unbindCurrentFrameBuffer();

            //Hintergrund jetzt als Textur in der passenden Größe verfügbar
            tex_Hintergrund = fbHintergrund.getTexture();


            long now = System.nanoTime();
            double diff = (now - last) / 1e9;
            runningAverageFrameTime = avgRatio * runningAverageFrameTime + (1 - avgRatio) * diff;
            last = now;

            fb.bindFrameBuffer();

            glLoadIdentity();

            glUseProgram(ShaderProgramm);

            //Hintergrund in Szene einbringen
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, tex_Hintergrund);

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


            //Agenten mit Textur rendern

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glActiveTexture(GL_TEXTURE1);

            for (int i = 1; i <= agentenSpielwiese.getAgentSize(); i++) {
                //absolut keine Ahnung, warum hier nichts geht
                glBindTexture(GL_TEXTURE_2D, 0);
                glBindTexture(GL_TEXTURE_2D, tex_Biene);
                //glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WIDTH, HEIGHT, 0, GL_RGB, GL_UNSIGNED_BYTE, null);
                //glGenerateMipmap(GL_TEXTURE_2D);
                //glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
                Agent aktAgent = agentenSpielwiese.getAgent(i);
                aktAgent.render();
                aktAgent.update(diff);
            }

            glBindTexture(GL_TEXTURE_2D, tex_Biene);
            mouse.render();

			fb.unbindCurrentFrameBuffer();// naus mit de framebuffer

            glBindTexture(GL_TEXTURE_2D, 0);

            //gesamte Szene in einem Texturbuffer speichern

            tex_buffer = fb.getTexture();

            glActiveTexture(GL_TEXTURE0); // activate texture unit 0
            glBindTexture(GL_TEXTURE_2D, tex_buffer); // bind texture
            glUniform1i(uniform_fragShader_tex1, 0); // inform the shader to use texture unit 0

            glLoadIdentity();

            glTranslated(Display.getWidth() / 2, Display.getHeight() / 2, 0);
            glRotatef(180, 1, 0, 0);

            //gesamte Szene aus dem Texturbuffer auf ein Quad rendern

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