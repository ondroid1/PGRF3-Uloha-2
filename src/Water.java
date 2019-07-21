import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import common.Constants;
import common.GridFactory;
import oglutils.OGLBuffers;
import oglutils.ShaderUtils;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Water  implements GLEventListener {

    private static final int FPS = 60; // animator's target frames per second
    private static final Dimension DIMENSION = new Dimension(1280, 720);
    private static final String WINDOW_TITLE = "PGRF3 - Ondřej Stieber - Simulace vodní hladiny";

    OGLBuffers buffers;
    int shaderProgram;

    public static void main(String[] args) {

        try {
            Frame frame = new Frame(Constants.FRAME_TITLE);
            frame.setSize(DIMENSION);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);

            // setup OpenGL version
            GLProfile profile = GLProfile.getMaximum(true);
            GLCapabilities capabilities = new GLCapabilities(profile);

            // The canvas is the widget that's drawn in the JFrame
            GLCanvas canvas = new GLCanvas(capabilities);
            Renderer ren = new Renderer();
            canvas.addGLEventListener(ren);
            canvas.addMouseListener(ren);
            canvas.addMouseMotionListener(ren);
            canvas.addKeyListener(ren);
            canvas.setSize(DIMENSION);

            frame.add(canvas);

            final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    new Thread(() -> {
                        if (animator.isStarted()) animator.stop();
                        System.exit(0);
                    }).start();
                }
            });
            frame.setTitle(WINDOW_TITLE);
            frame.pack();
            frame.setVisible(true);
            animator.start(); // start the animation loop

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void init(GLAutoDrawable glAutoDrawable) {

        GL2GL3 gl = glAutoDrawable.getGL().getGL2GL3();

        shaderProgram = ShaderUtils.loadProgram(gl, "shaders/start.vert", "shaders/start.frag",
                null,null,null,null);

        buffers = GridFactory.generateGrid(gl, 20, 20);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {

        GL2GL3 gl = glAutoDrawable.getGL().getGL2GL3();

        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        gl.glClear(GL2GL3.GL_COLOR_BUFFER_BIT | GL2GL3.GL_DEPTH_BUFFER_BIT);

        gl.glUseProgram(shaderProgram);

//        gl.glUniformMatrix4fv(locView, 1, false, camera.getViewMatrix().floatArray(), 0);
//        gl.glUniformMatrix4fv(locProj, 1, false, proj.floatArray(), 0);

        gl.glPolygonMode(GL2GL3.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);

        // bind and draw
        buffers.draw(GL2GL3.GL_TRIANGLE_STRIP, shaderProgram);
        //gl.glDrawElements(GL2GL3.GL_TRIANGLE_STRIP, buffers.getIndexCount(), GL2GL3.GL_UNSIGNED_INT, 0);

//        textRenderer.drawStr2D(3, height - 20, this.getClass().getName());
//        textRenderer.drawStr2D(width - 90, 3, " (c) PGRF UHK");

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int width, int height) {

    }


}
