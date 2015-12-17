package io.github.anti_java;

import io.github.anti_java.interfaces.*;
import io.github.anti_java.struct.IPAddress;
import io.github.anti_java.struct.Position;
import io.github.anti_java.struct.UIEvent;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Davy on 2015/12/17.
 */

public class UIMTest {
    class StubApplication implements Application {
        public final StubTCPClient tcpClient = new StubTCPClient();
        public final StubUIEventListener uiEventListener = new StubUIEventListener();
        public final StubDynamicObjectModule dynamicObjectModule = new StubDynamicObjectModule();

        public StubApplication() {}

        @Override
        public TCPClient getTCPClient() { return tcpClient; }

        @Override
        public UIEventListener getUIEventListener() { return uiEventListener; }

        @Override
        public DynamicObjectModule getDynamicObjectModule() { return dynamicObjectModule; }
    }

    class StubTCPClient implements TCPClient {
        public Integer lastInputMoves = null;
        @Override
        public boolean connectServer(IPAddress serverip) { return false; }

        @Override
        public void inputMoves(int MoveCode) {
            lastInputMoves = MoveCode;
        }
    }

    class StubUIEventListener implements UIEventListener {
        public UIEventDispatcher uiEventDispatcher = null;
        @Override
        public void registerDispatcher(UIEventDispatcher dispatcher) {
            uiEventDispatcher = dispatcher;
        }
    }

    class StubDynamicObjectModule implements DynamicObjectModule {
        public boolean keyGETPressedCalled = false;

        @Override
        public void addVirtualCharacter(int clientno) {}

        @Override
        public void addItem(String name, int index, boolean shared) {}

        @Override
        public void updateItem(int index, boolean shared, int owner) {}

        @Override
        public void updateVirtualCharacter(int clientno, int dir, int speed, int x, int y) {}

        @Override
        public List<Object> getAllDynamicObjects() { return null; }

        @Override
        public Position getVirtualCharacterXY() { return null; }

        @Override
        public void keyGETPressed() {
            keyGETPressedCalled = true;
        }
    }

    static final List<Pair<UIEvent.Key, Integer>> stubKeyEvents = new ArrayList<>();
    static {
        stubKeyEvents.add(new Pair<>(UIEvent.Key.UP, Global.TURNNORTH));
        stubKeyEvents.add(new Pair<>(UIEvent.Key.DOWN, Global.TURNSOUTH));
        stubKeyEvents.add(new Pair<>(UIEvent.Key.LEFT, Global.TURNWEST));
        stubKeyEvents.add(new Pair<>(UIEvent.Key.RIGHT, Global.TURNEAST));
    }

    @Test
    public void successRegisterDispatcherOnCreate() {
        final StubApplication application = new StubApplication();
        final UIM uim = new UIM(application);

        Assert.assertEquals(uim, application.uiEventListener.uiEventDispatcher);
    }

    @Test
    public void successTriggerDirectionKeyEvent() {
        final StubApplication application = new StubApplication();
        final StubTCPClient tcpClient = application.tcpClient;
        final UIM uim = new UIM(application);

        for (Pair<UIEvent.Key, Integer> p : stubKeyEvents) {
            tcpClient.lastInputMoves = null;

            uim.triggerEvent(new UIEvent(UIEvent.Type.KEYBOARD, p.getKey()));

            Assert.assertEquals(tcpClient.lastInputMoves, p.getValue());
        }
    }

    @Test
    public void successTriggerGetKeyEvent() {
        final StubApplication application = new StubApplication();
        final StubDynamicObjectModule dynamicObjectModule = application.dynamicObjectModule;
        final UIM uim = new UIM(application);
        dynamicObjectModule.keyGETPressedCalled = false;

        uim.triggerEvent(new UIEvent(UIEvent.Type.KEYBOARD, UIEvent.Key.SPACE));

        Assert.assertTrue(dynamicObjectModule.keyGETPressedCalled);
    }
}
