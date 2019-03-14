package Objects.GameObjects;

import Init.CameraID;
import Init.Game;
import Init.Window;
import Objects.GameObjects.Properties.Drawable;
import Objects.GameObjects.Properties.Physics;
import Objects.Utility.Maths.Maths;
import Objects.Utility.Maths.Vector2D;
import Objects.Utility.ObjectList;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class Asteroid extends GameObject implements Physics {
    private Vector2D velocity, resultantForce;
    private int mass;

    public Asteroid() {
        super(ObjectID.NA);
        position = new Vector2D(Maths.randomInt(-Window.gameWidth*2, Window.gameWidth*2), Maths.randomInt(-Window.gameWidth*2, Window.gameWidth*2));
        midPos = new Vector2D(position.x+width/2, position.y+height/2);

        this.width = (float)Maths.randomInt(Window.gameWidth/50,Window.gameWidth/10);
        this.height = 1+width-1;

        velocity = new Vector2D();
        mass = (int)width*2;

        resultantForce = new Vector2D();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void update() {
        collisions(velocity);

        Vector2D dragForceVector = new Vector2D(velocity.getUnitVector().scale(-1*velocity.mag()*0.9));
        applyForce(dragForceVector);
        Vector2D acceleration = new Vector2D(resultantForce.x/mass,resultantForce.y/mass);
        velocity = velocity.add(acceleration);
        position = position.add(velocity);
        midPos = midPos.add(velocity);

        resultantForce = resultantForce.set(0, 0);
    }

    @Override
    public void applyForce(Vector2D force) {
        resultantForce = resultantForce.add(force);
    }

    @Override
    public void render(Graphics2D g2d) {
        Drawable drawable = (g2)->{
            g2.setColor(Color.DARK_GRAY);
            g2.fillOval((int)position.x, (int)position.y, (int)width, (int)height);

//            g2.setColor(Color.red);
//            g2.drawRect((int)getSquareBounds().getX(), (int)getSquareBounds().getY(), (int)getSquareBounds().getWidth(), (int)getSquareBounds().getHeight());
//            for(Line2D line : getPolyBounds()) {
//                g2.drawLine((int)line.getX1(), (int)line.getY1(), (int)line.getX2(), (int)line.getY2());
//            }
        };

        renderToCamera(drawable, g2d, Game.getInstance().cameraMap.get(CameraID.game));
    }

    @Override
    public Rectangle2D getSquareBounds() {
        return new Rectangle2D.Double(position.x, position.y, width, height);
    }

    @Override
    public ObjectList<Line2D> getPolyBounds() {
        ObjectList<Line2D> returnList = new ObjectList<>();
        returnList.add(new Line2D.Double(position.x+width/2, position.y, position.x+width/2+width/3, position.y+height/5));
        returnList.add(new Line2D.Double(position.x+width/2+width/3, position.y+height/5, position.x+width, position.y+height/2));
        returnList.add(new Line2D.Double(position.x+width, position.y+height/2, position.x+width/2+width/3, position.y+height/2+height/3));
        returnList.add(new Line2D.Double(position.x+width/2+width/3, position.y+height/2+height/3, position.x+width/2, position.y+height));
        returnList.add(new Line2D.Double(position.x+width/2, position.y+height, position.x+width/5, position.y+height/2+height/3));
        returnList.add(new Line2D.Double(position.x+width/5, position.y+height/2+height/3, position.x, position.y+height/2));
        returnList.add(new Line2D.Double(position.x, position.y+height/2, position.x+width/5, position.y+height/5));
        returnList.add(new Line2D.Double(position.x+width/5, position.y+height/5, position.x+width/2, position.y));
        return returnList;
    }
}
