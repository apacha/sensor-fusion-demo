package org.hitlabnz.sensor_fusion_demo;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Intent;
import android.hardware.SensorManager;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import org.hitlabnz.sensorfusionlib.orientationProvider.*;
import org.hitlabnz.sensorfusionlib.representation.*;

public class Compass3D extends ApplicationAdapter {
    private final String TAG = Compass3D.class.getName();
    private final SensorSelectionActivity activity;
    private final SensorManager sensorManager;
    private Environment lights;
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Model model;
    private ModelInstance instance;
    private Texture north, east, south, west, zenith, nadir;
    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage;
    private Label infoLabel;
    private final MatrixF4x4 providerRotationMatrix;
    private float[] rotationMatrix;
    private final float[] modMat;
    private final float[] eulerAngles;
    private String currentOrientationProvider;
    private FreeTypeFontGenerator generator;

    // We have 6 different orientation providers suitable for 3D compass,
    // but Calibrated Gyroscope cannot be used in a compass application
    // to correctly display the compass
    private final int numberOfProviders = 6;
    private int currentProviderIndex = 0;

    public Compass3D(SensorSelectionActivity parent) {
        activity = parent;
        sensorManager = (SensorManager) activity.getSystemService(SENSOR_SERVICE);
        currentOrientationProvider = activity.getString(R.string.title_section1);
        providerRotationMatrix = new MatrixF4x4();
        rotationMatrix = new float[16];
        modMat = new float[16];
        eulerAngles = new float[3];
    }

    @Override
    public void create() {
        // prepare the environment, create a point light source
        lights = new Environment().add(new PointLight().set(Color.WHITE, Vector3.Zero, 75f));
        lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.1f, 0.1f, 0.1f, 1));

        modelBatch = new ModelBatch();

        // create & setup camera
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Vector3.Zero); // put the camera to the center of the 3D coordinate system
        camera.lookAt(Vector3.Z); // look at North direction first
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        // setup materials for 3D components, load textures from files
        north = new Texture(Gdx.files.internal("North.png"));
        Material matNorth = new Material(TextureAttribute.createDiffuse(north));

        south = new Texture(Gdx.files.internal("South.png"));
        Material matSouth = new Material(TextureAttribute.createDiffuse(south));

        east = new Texture(Gdx.files.internal("East.png"));
        Material matEast = new Material(TextureAttribute.createDiffuse(east));

        west = new Texture(Gdx.files.internal("West.png"));
        Material matWest = new Material(TextureAttribute.createDiffuse(west));

        zenith = new Texture(Gdx.files.internal("Zenith.png"));
        Material matZenith = new Material(TextureAttribute.createDiffuse(zenith));

        nadir = new Texture(Gdx.files.internal("Nadir.png"));
        Material matNadir = new Material(TextureAttribute.createDiffuse(nadir));

        Node node = new Node();

        ModelBuilder modelBuilder = new ModelBuilder();

        // create the 3D compass model components: 6 simple boxes with assigned materials
        Model boxEast = modelBuilder.createBox(5, 5, 5, GL20.GL_TRIANGLES, matEast, Usage.Position | Usage.Normal | Usage.TextureCoordinates);
        Model boxWest = modelBuilder.createBox(5, 5, 5, GL20.GL_TRIANGLES, matWest, Usage.Position | Usage.Normal | Usage.TextureCoordinates);
        Model boxZenith = modelBuilder.createBox(5, 5, 5, GL20.GL_TRIANGLES, matZenith, Usage.Position | Usage.Normal | Usage.TextureCoordinates);
        Model boxNadir = modelBuilder.createBox(5, 5, 5, GL20.GL_TRIANGLES, matNadir, Usage.Position | Usage.Normal | Usage.TextureCoordinates);
        Model boxNorth = modelBuilder.createBox(5, 5, 5, GL20.GL_TRIANGLES, matNorth, Usage.Position | Usage.Normal | Usage.TextureCoordinates);
        Model boxSouth = modelBuilder.createBox(5, 5, 5, GL20.GL_TRIANGLES, matSouth, Usage.Position | Usage.Normal | Usage.TextureCoordinates);

        // create our model, the 3D compass and put it to the center of the 3D coordinate system
        modelBuilder.begin();
        node = modelBuilder.node("axes", new Model());  // this is just a dummy node

        node = modelBuilder.node("boxEast", boxEast);
        node.translation.set(-10, 0, 0);
        node.rotation.setFromAxis(Vector3.Z, 90);

        node = modelBuilder.node("boxWest", boxWest);
        node.translation.set(10, 0, 0);
        node.rotation.setFromAxis(Vector3.Z, 90);

        node = modelBuilder.node("boxNadir", boxNadir);
        node.translation.set(0, -10, 0);

        node = modelBuilder.node("boxZenith", boxZenith);
        node.translation.set(0, 10, 0);

        node = modelBuilder.node("boxSouth", boxSouth);
        node.translation.set(0, 0, -10);
        node.rotation.setFromAxis(Vector3.Z, 90);

        node = modelBuilder.node("boxNorth", boxNorth);
        node.translation.set(0, 0, 10);
        node.rotation.setFromAxis(Vector3.Z, 90);

        model = modelBuilder.end();
        Gdx.app.log(TAG, "Nodes: " + model.nodes.size);

        // place our model to the center of the 3D world
        instance = new ModelInstance(model, Vector3.Zero);

        // setup all the stuff needed for displaying texts
        batch = new SpriteBatch();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("CascadiaMono.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        int fontSizeInDp = 20;
        int fontSizeInPixels = (int) (fontSizeInDp * activity.getContext().getResources().getDisplayMetrics().density);
        parameter.size = fontSizeInPixels;
        font = generator.generateFont(parameter);

        // for displaying static texts
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // create a new style for the below buttons
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(createDrawable(Color.BLACK), createDrawable(Color.DARK_GRAY), null, font);

        TextButton previousProviderButton = new TextButton(
                activity.getString(R.string.action_previous_provider),
                buttonStyle
        );
        previousProviderButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentProviderIndex = (currentProviderIndex - 1 + numberOfProviders) % numberOfProviders;
                currentOrientationProvider = setCurrentOrientationProvider(currentProviderIndex);
            }
        });

        TextButton nextProviderButton = new TextButton(
                activity.getString(R.string.action_next_provider),
                buttonStyle
        );
        nextProviderButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentProviderIndex = (currentProviderIndex + 1) % numberOfProviders;
                currentOrientationProvider = setCurrentOrientationProvider(currentProviderIndex);
            }
        });


        TextButton aboutButton = new TextButton(
                activity.getString(R.string.action_about),
                buttonStyle
        );
        aboutButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Intent intent = new Intent(activity, AboutActivity.class);
                activity.startActivity(intent);
            }
        });

        // the table will be our parent container for all UI elements
        Table container = new Table().top().left();
        // container.debug();      // Turn on all debug lines (table, cell, and widget).
        container.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        container.setFillParent(true);

        stage.addActor(container);

        // create text label to display current FPS
        infoLabel = new Label("FPS:", new LabelStyle(font, Color.WHITE));
        // add all UI elements to the table, each into a new row
        container.add(infoLabel).top().left().pad(16f);

        Table buttonGroup = new Table();
        buttonGroup.add(previousProviderButton).height(fontSizeInPixels * 2);
        buttonGroup.add(nextProviderButton).height(fontSizeInPixels * 2).padLeft(fontSizeInPixels);

        container.row();
        container.add(buttonGroup).bottom().expand();
        container.row();
        container.add(aboutButton).bottom().expandX().height(fontSizeInPixels * 2)
                .padTop(fontSizeInPixels)
                .padBottom(fontSizeInPixels);

        // write some logs
        Gdx.app.log(TAG, "App created!");
        Gdx.app.log(TAG, "" + Gdx.app.getType());
        Gdx.app.log(TAG, "GDX version: " + Version.VERSION);
    }

    @Override
    public void resize(int width, int height) {
        // should not happen on Android using the current settings,
        // manifest file - hardcoded screen portrait mode,
        // but anyway, on resize we update the camera viewport
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        // clear screen
        ScreenUtils.clear(Color.BLACK, true);

        // render the model - our 3D compass
        modelBatch.begin(camera);
        modelBatch.render(instance, lights);
        modelBatch.end();

        // render some text information
        batch.begin();
        String information = "FPS: " + Gdx.graphics.getFramesPerSecond() +
                "\nGDX version: " + Version.VERSION +
                "\n" +
                activity.getString(R.string.title_orientation_provider) +
                "\n" + currentOrientationProvider;
        infoLabel.setText(information);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        batch.end();

        // Rotate virtual camera based on device orientation
        // get rotation matrix from the current orientation provider
        activity.getOrientationProvider().getRotationMatrix(providerRotationMatrix);
        rotationMatrix = providerRotationMatrix.getMatrix();

        // TODO: Handle Display Rotation using WindowManager.DefaultDisplay.Rotation
        // Currently the activity is hardcoded set to portrait mode.
        // When the device is held with screen facing to the user,
        // the sensors coordinate system is different than the world coordinate system and
        // therefore needs to be remapped to show the 3D compass correctly.
        SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, modMat);
        SensorManager.getOrientation(modMat, eulerAngles);

        float Azimuth = (eulerAngles[0] * MathUtils.radiansToDegrees + 360f) % 360f; // Azimuth (Yaw, rotation around the world's Y-axis)
        float Pitch = eulerAngles[1] * MathUtils.radiansToDegrees; // Altitude (rotation around the world's X-axis)
        float Roll = eulerAngles[2] * MathUtils.radiansToDegrees; // Camera roll (rotation around the current "LookAt" direction vector)

        //Gdx.app.log(TAG, MessageFormat.format("Azimuth: {0,number,#.###}; Pitch: {1,number,#.###}; Roll: {2,number,#.###}", Azimuth, Pitch, Roll));

        // Transform the Forward vector (Vector3.Z) using a quaternion got from Azimuth/Pitch angles to a LookAt vector
        Quaternion rotCam = new Quaternion().setEulerAngles(-Azimuth, Pitch, 0); // We don't process Roll angle here!
        Vector3 newDir = new Vector3(Vector3.Z).mul(rotCam).nor();
        camera.lookAt(newDir);

        // Transform the camera UP vector (Vector3.Y) using a quaternion got from previous LookAt vector and Roll angle
        Quaternion rotUp = new Quaternion(newDir, Roll);
        Vector3 newUp = new Vector3(Vector3.Y).mul(rotUp).nor();
        camera.up.set(newUp);

        // apply the above changes on camera
        camera.update();
    }

    @Override
    public void dispose() {
        // cleanup all the stuff created in "create"
        modelBatch.dispose();
        model.dispose();
        north.dispose();
        south.dispose();
        east.dispose();
        west.dispose();
        zenith.dispose();
        nadir.dispose();
        batch.dispose();
        font.dispose();
        stage.dispose();
        generator.dispose();
    }

    private String setCurrentOrientationProvider(int providerIndex) {
        OrientationProvider orientationProvider = new ImprovedOrientationSensor1Provider(sensorManager);
        String result = "";
        switch (providerIndex) {
            case 0:
                result = activity.getString(R.string.title_section1);
                orientationProvider = new ImprovedOrientationSensor1Provider(sensorManager);
                break;
            case 1:
                result = activity.getString(R.string.title_section2);
                orientationProvider = new ImprovedOrientationSensor2Provider(sensorManager);
                break;
            case 2:
                result = activity.getString(R.string.title_section3);
                orientationProvider = new RotationVectorProvider(sensorManager);
                break;
            case 3:
                result = activity.getString(R.string.title_section5);
                orientationProvider = new GravityCompassProvider(sensorManager);
                break;
            case 4:
                result = activity.getString(R.string.title_section6);
                orientationProvider = new AccelerometerCompassProvider(sensorManager);
                break;
            case 5:
                result = activity.getString(R.string.title_section4);
                orientationProvider = new CalibratedGyroscopeProvider(sensorManager);
                break;
        }
        orientationProvider.start();
        activity.setOrientationProvider(orientationProvider);
        return result;
    }

    private TextureRegionDrawable createDrawable(Color color) {
        Pixmap pxm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pxm.setColor(color);
        pxm.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(pxm)));
    }
}
