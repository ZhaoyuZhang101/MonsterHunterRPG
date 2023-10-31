package Game.logic.implementClass;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;

public abstract class TimeLineController {
    public Timeline timeline;
    public TimeLineController() {
        initTimeLine();
    }
    public void initTimeLine() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyframe = new KeyFrame(Duration.millis(10), actionEvent -> {
            try {
                tick();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        timeline.getKeyFrames().add(keyframe);
        timeline.play();
    }
    public void tick() throws IOException {}
    public void stop() {
        timeline.stop();
    }
}
