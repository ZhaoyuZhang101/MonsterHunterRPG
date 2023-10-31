package Game.logic.Background;

import Game.logic.Engine.SceneCreator;
import Game.logic.implementClass.TimeLineController;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @className Background
 * @Description TODO
 * @Author zhang
 * @DATE 2022/9/11 11:38
 **/
public class Background extends Parent {
    public ArrayList<ImageView> images = new ArrayList<>();
    public int layer;
    public Stage stage;
    double SceneH;
    SceneCreator root;
    private double SceneW;
    private double scrollValue = 0;
    private final ArrayList<Double> layoutXList = new ArrayList<>();
    public TimeLineController timeLineController;
    public Background(Stage stage, SceneCreator root) {
        this.root = root;
        this.stage = stage;
        timeLineController = new TimeLineController() {
            @Override
            public void tick() throws IOException {
                super.tick();
                ticks();
            }
        };
    }

    public void updateHW() {
        this.SceneH = this.stage.getScene().getHeight();
        this.SceneW = this.stage.getScene().getWidth();
    }

    public void setImage(ImageView image) {
        this.updateHW();
        this.images.add(image);

    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
    public void setRoot(SceneCreator root) {
        this.root = root;
    }

    public ArrayList<ImageView> getImage() {
        return images;
    }

    public void moveLeft() {
        this.scrollValue = (this.scrollValue + ((this.SceneH/1000)*this.layer))%(this.SceneH * 1.5);
    }

    public ImageView preset_background(Image image) {
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(this.SceneH);
        imageView.setFitWidth(this.SceneH * 1.5);
        return imageView;
    }

    public void moveRight() {
        this.scrollValue = (this.scrollValue - ((this.SceneH/1000)*this.layer))%(this.SceneH * 1.5);
    }

    /**
     * 背景循环逻辑（已经完成，换背景的话只需要单纯的替换图片路径就好）
     */
    public void ticks() {
        this.updateHW();
        for (ImageView image: images) {
            getChildren().remove(image);
        }
        layoutXList.clear();
        if (images.size()>0) {
            int backgroundNumber = (int) Math.ceil(this.SceneW / images.get(0).getFitWidth());
            Image new_image = images.get(0).getImage();
            images.clear();
            for (int i = 0; i < backgroundNumber; i++) {
                ImageView imageView = preset_background(new_image);
                imageView.setLayoutX(getLayoutX() + (i * imageView.getFitWidth()) + this.scrollValue);
                this.images.add(imageView);
                getChildren().add(imageView);
                layoutXList.add(imageView.getLayoutX());
            }
            if (layoutXList.get(0) > 0) {
                ImageView imageView = preset_background(new_image);
                this.images.add(0, imageView);
                getChildren().add(imageView);
                imageView.setLayoutX(getLayoutX() - this.SceneH * 1.5 + this.scrollValue);
                layoutXList.add(0, imageView.getLayoutX());
            }
            if (layoutXList.get(layoutXList.size()-1) + this.SceneH * 1.5 < this.SceneW) {
                ImageView imageView = preset_background(new_image);
                this.images.add(imageView);
                getChildren().add(imageView);
                imageView.setLayoutX(getLayoutX() + ((images.size()-1) * this.SceneH * 1.5) + this.scrollValue);
                layoutXList.add(imageView.getLayoutX());
            }
        }
    }
}
