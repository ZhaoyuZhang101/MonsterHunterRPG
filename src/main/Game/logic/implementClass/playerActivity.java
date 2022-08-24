package Game.logic.implementClass;

public enum playerActivity {
    run("run"), idle("idle"), jump("jump"), fall("fall"), attack("attack");
    public final String name;
    playerActivity(String name) {
        this.name = name;
    }
}
