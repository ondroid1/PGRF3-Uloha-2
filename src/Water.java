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

public class Water {

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
}
