import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import common.GridFactory;
import oglutils.*;
import transforms.Camera;
import transforms.Mat4;
import transforms.Mat4PerspRH;
import transforms.Vec3D;

import java.awt.event.*;

/**
 * GLSL sample:<br/>
 * Read and compile shader from files "/shader/glsl01/start.*" using ShaderUtils
 * class in oglutils package (older GLSL syntax can be seen in
 * "/shader/glsl01/startForOlderGLSL")<br/>
 * Manage (create, bind, draw) vertex and index buffers using OGLBuffers class
 * in oglutils package<br/>
 * Requires JOGL 2.3.0 or newer
 *
 * @author PGRF FIM UHK
 * @version 2.0
 * @since 2015-09-05
 */
public class Renderer implements GLEventListener, MouseListener,
		MouseMotionListener, KeyListener {

	private final static int DEFAULT_GRID_SIDE_SIZE = 10;

	private String vertexShaderFileName = "/start.vert", fragmentShaderFileName  = "/start.frag";

	int width, height;

	OGLBuffers buffers;
	OGLTextRenderer textRenderer;
	OGLTexture2D texture;

	int shaderProgram, locView, locProj;

	private Mat4 proj;
	private Camera camera;

	// přepínače
	private boolean showPolygonLines = true;
	private int gridSize = DEFAULT_GRID_SIDE_SIZE;


	@Override
	public void init(GLAutoDrawable glDrawable) {
		GL2GL3 gl = glDrawable.getGL().getGL2GL3();
		OGLUtils.shaderCheck(gl);
		OGLUtils.printOGLparameters(gl);

		textRenderer = new OGLTextRenderer(gl, glDrawable.getSurfaceWidth(), glDrawable.getSurfaceHeight());

		texture = new OGLTexture2D(gl, "/water2.jpg");

		shaderProgram = ShaderUtils.loadProgram(gl, vertexShaderFileName, fragmentShaderFileName,
				null,null,null,null);

		buffers = GridFactory.generateGrid(gl, gridSize, gridSize, true);

		camera = new Camera()
				.withPosition(new Vec3D(10,10, 5))
				.addAzimuth(5 / 4. * Math.PI)
				.addZenith(-1 / 5. * Math.PI)
				.withFirstPerson(false)
				.withRadius(5);

		locProj = gl.glGetUniformLocation(shaderProgram, "proj");
		locView = gl.glGetUniformLocation(shaderProgram, "view");
	}

	@Override
	public void display(GLAutoDrawable glDrawable) {
		GL2GL3 gl = glDrawable.getGL().getGL2GL3();

		//gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		gl.glClear(GL2GL3.GL_COLOR_BUFFER_BIT | GL2GL3.GL_DEPTH_BUFFER_BIT);

		gl.glUseProgram(shaderProgram);

		gl.glUniformMatrix4fv(locView, 1, false, camera.getViewMatrix().floatArray(), 0);
		gl.glUniformMatrix4fv(locProj, 1, false, proj.floatArray(), 0);

		if (showPolygonLines){
			gl.glPolygonMode(GL2GL3.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
		} else{
			gl.glPolygonMode(GL2GL3.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
		}

		texture.bind(shaderProgram, "textureID", 0);

		// bind and draw
		buffers.draw(GL2GL3.GL_TRIANGLE_STRIP, shaderProgram);

		textRenderer.drawStr2D(20, height - 40, "Ovládání:");
		textRenderer.drawStr2D(20, height - 120, "+-: Zvětšit nebo zmenšit plochu");
		textRenderer.drawStr2D(20, height - 80, "q: Zobrazit hladinu jako plochu nebo v čarách");
		textRenderer.drawStr2D(20, height - 160, "w: Zapnout / vypnout vlnění");
		textRenderer.drawStr2D(20, height - 200, "e: Zapnout / vypnout osvětlení");
		textRenderer.drawStr2D(width - 300, 20, " (c) PGRF UHK");
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
						int height) {
		this.width = width;
		this.height = height;
		proj = new Mat4PerspRH(Math.PI / 4, height / (double) width, 0.01, 1000.0);

		textRenderer.updateSize(width, height);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_PLUS: // zvětšit plochu
			case KeyEvent.VK_ADD:
				gridSize++;
				break;
			case KeyEvent.VK_MINUS: // zmenšit plochu
			case KeyEvent.VK_SUBTRACT:
				gridSize--;
				break;
			case KeyEvent.VK_Q: // čáry nebo plocha
				showPolygonLines = !showPolygonLines;
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void dispose(GLAutoDrawable glDrawable) {
		GL2GL3 gl = glDrawable.getGL().getGL2GL3();
		gl.glDeleteProgram(shaderProgram);
	}

}