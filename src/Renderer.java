import com.jogamp.opengl.*;
import common.GridFactory;
import oglutils.*;
import transforms.Camera;
import transforms.Mat4;
import transforms.Mat4PerspRH;
import transforms.Vec3D;

import java.awt.event.*;

/**
 * WaterWavesSimulation Waves Simulationion in Open GL
 * Requires JOGL 2.3.0 or newer
 *
 * @author Ondřej Stieber / PGRF FIM UHK
 * @version 2.0
 * @since 2015-09-05
 */
public class Renderer implements GLEventListener, MouseListener,
		MouseMotionListener, KeyListener {

	private final static int DEFAULT_GRID_SIDE_SIZE = 100;
	private final static String vertexShaderFileName = "/start.vert", fragmentShaderFileName  = "/start.frag";


	int width, height;

	OGLBuffers buffers;
	OGLTextRenderer textRenderer;
	OGLTexture2D waveHeightTexture;
	OGLTexture2D[] mainTextures;
	OGLTexture2D.Viewer textureViewer;

	int shaderProgram, uniformLocationView, uniformLocationProj, uniformLocationShowWaves, uniformLocationShowTexture,
			uniformLocationTimeTime, uniformLocationTexture, uniformLocationWaveHeightTexture;


	private Mat4 projectionMatrix;
	private Camera camera;
	private float time = 0;
	private int selectedMainTextureIndex = 0;
	boolean updateGridBuffers;

	// přepínače
	private boolean showTexture = false;
	private int showVawes = 0;
	private int gridSize = DEFAULT_GRID_SIDE_SIZE;


	@Override
	public void init(GLAutoDrawable glDrawable) {
		GL2GL3 gl = glDrawable.getGL().getGL2GL3();
		OGLUtils.shaderCheck(gl);
		OGLUtils.printOGLparameters(gl);

		textRenderer = new OGLTextRenderer(gl, glDrawable.getSurfaceWidth(), glDrawable.getSurfaceHeight());
		waveHeightTexture = new OGLTexture2D(gl, "/wave-height-texture.png");
		mainTextures = new OGLTexture2D[3];
		mainTextures[0] = new OGLTexture2D(gl, "/water-surface-texture-0.jpg");
		mainTextures[1] = new OGLTexture2D(gl, "/water-surface-texture-1.jpg");
		mainTextures[2] = new OGLTexture2D(gl, "/water-surface-texture-2.jpg");
		textureViewer = new OGLTexture2D.Viewer(gl);

		shaderProgram = ShaderUtils.loadProgram(gl, vertexShaderFileName, fragmentShaderFileName,
				null,null,null,null);

		setVertexAndIndexBuffers(gl);

		camera = new Camera()
				.withPosition(new Vec3D(10, 10, 5))
				.addAzimuth(5 / 4. * Math.PI)
				.addZenith(-1 / 5. * Math.PI)
				.withFirstPerson(false)
				.withRadius(2);

		uniformLocationProj = gl.glGetUniformLocation(shaderProgram, "projectionMatrix");
		uniformLocationView = gl.glGetUniformLocation(shaderProgram, "viewMatrix");
		uniformLocationTimeTime = gl.glGetUniformLocation(shaderProgram, "time");
		uniformLocationShowWaves = gl.glGetUniformLocation(shaderProgram, "showWaves");
		uniformLocationShowTexture = gl.glGetUniformLocation(shaderProgram, "showTexture");
		uniformLocationTexture = gl.glGetUniformLocation(shaderProgram, "mainTexture");
		uniformLocationWaveHeightTexture = gl.glGetUniformLocation(shaderProgram, "waveHeightTexture");
	}

	@Override
	public void display(GLAutoDrawable glDrawable) {
		GL2 gl = glDrawable.getGL().getGL2();

		if (updateGridBuffers) {
			setVertexAndIndexBuffers(gl);
			updateGridBuffers = false;
		}

		gl.glClear(GL2GL3.GL_COLOR_BUFFER_BIT | GL2GL3.GL_DEPTH_BUFFER_BIT);

		gl.glUseProgram(shaderProgram);

		if (showVawes == 1) {
			time += 0.005;
		}

		gl.glUniform1f(uniformLocationTimeTime, time);
		gl.glUniform1i(uniformLocationShowWaves, showVawes);
		gl.glUniform1i(uniformLocationShowTexture, showTexture ? 1 : 0);
		gl.glUniformMatrix4fv(uniformLocationView, 1, false, camera.getViewMatrix().floatArray(), 0);
		gl.glUniformMatrix4fv(uniformLocationProj, 1, false, projectionMatrix.floatArray(), 0);

		if (showTexture) {
			gl.glPolygonMode(GL2GL3.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
		} else {
			gl.glPolygonMode(GL2GL3.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
		}

		// textura pro výpočet výšky vln
		waveHeightTexture.bind(shaderProgram, "waveHeightTextureID", 0);

		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);

		// textura pro obarvení vln
		if (showTexture) {
			mainTextures[selectedMainTextureIndex].bind(shaderProgram, "mainTextureID", 1);
		}

		// bind and draw
		buffers.draw(GL2GL3.GL_TRIANGLE_STRIP, shaderProgram);

		// zobrazení hlavní textury ve vlastním rámečku
		if (showTexture) {
			textureViewer.view(mainTextures[selectedMainTextureIndex], -1, -1, 0.5);
		}

		textRenderer.drawStr2D(20, height - 40, "Ovládání:");
		textRenderer.drawStr2D(20, height - 120, "+-: Zvětšit nebo zmenšit hustotu rastru");
		textRenderer.drawStr2D(20, height - 160, "q: Zobrazit texturu");
		textRenderer.drawStr2D(20, height - 200, "w: Změnit texturu");
		textRenderer.drawStr2D(20, height - 240, "e: Zapnout / vypnout simulaci vln");
		textRenderer.drawStr2D(width - 300, 20, " (c) PGRF UHK");
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
						int height) {
		this.width = width;
		this.height = height;
		projectionMatrix = new Mat4PerspRH(Math.PI / 4, height / (double) width, 0.01, 1000.0);

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
			case KeyEvent.VK_PLUS: // zvětšit rastr
			case KeyEvent.VK_ADD:
				gridSize++;
				updateGridBuffers = true;
				break;
			case KeyEvent.VK_MINUS: // zmenšit rastr
			case KeyEvent.VK_SUBTRACT:
				if (gridSize > 2) {
					gridSize--;
					updateGridBuffers = true;
				}
				break;
			case KeyEvent.VK_Q: // zobrazit / skrýt texturu
				showTexture = !showTexture;
				updateGridBuffers = true;
				break;
			case KeyEvent.VK_W: // změnit texturu
				if (selectedMainTextureIndex >= mainTextures.length - 1) {
					selectedMainTextureIndex = 0;
				} else {
					selectedMainTextureIndex++;
				}
				break;

			case KeyEvent.VK_E: // zastavit / spustit vlny
				showVawes = showVawes == 1 ? 0 : 1;
				time = 0;
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

	private void setVertexAndIndexBuffers(GL2GL3 gl) {
		buffers = GridFactory.generateGrid(gl, gridSize, gridSize, true);
	}
}