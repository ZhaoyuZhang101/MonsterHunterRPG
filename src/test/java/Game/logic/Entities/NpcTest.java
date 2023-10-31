package Game.logic.Entities;

import Game.logic.Entities.lives.NPC.BaseNPC;
import Game.logic.implementClass.CollisionBox;
import Game.logic.implementClass.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NpcTest {
    @Test
    public void dialogTest() throws IOException {
        List<String> dialogs = new ArrayList<>();
        dialogs.add("test dialog 1");
        dialogs.add("test dialog 2");
        BaseNPC npc = new BaseNPC(new CollisionBox(1, 1), 1, 1, 1, 1, new HashSet<>());
        assertTrue(dialogs.contains(npc.getRandomDialog()), "random dialog should return one in the list");
    }
}
