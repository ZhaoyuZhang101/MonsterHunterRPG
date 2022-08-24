package Game.logic.implementClass;

public class CollisionBox {
    private final float height;
    private final float width;

    public CollisionBox(float height, float width){
        this.height = height;
        this.width = width;
    }
    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    @Override
    public String toString() {
        return "CollisionBox{" +
                "height=" + height +
                ", width=" + width +
                '}';
    }
}
