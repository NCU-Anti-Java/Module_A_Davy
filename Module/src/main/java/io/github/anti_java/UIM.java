package io.github.anti_java;

import io.github.anti_java.interfaces.Application;
import io.github.anti_java.interfaces.DynamicObjectModule;
import io.github.anti_java.interfaces.TCPClient;
import io.github.anti_java.interfaces.UIModule;
import io.github.anti_java.struct.UIEvent;

/**
 * Created by Davy on 2015/12/17.
 */
public class UIM implements UIModule {
    private final Application mApplication;

    public UIM(Application application) {
        mApplication = application;

        application.getUIEventListener().registerDispatcher(this);
    }

    @Override
    public void triggerEvent(UIEvent event) {
        switch (event.getType()) {
            case KEYBOARD: {
                final TCPClient tcpClient = mApplication.getTCPClient();
                final DynamicObjectModule dynamicObjectModule = mApplication.getDynamicObjectModule();
                final UIEvent.Key key = (UIEvent.Key) event.getData();

                switch (key) {
                    case LEFT: {
                        tcpClient.inputMoves(Global.TURNWEST);
                        break;
                    }
                    case RIGHT: {
                        tcpClient.inputMoves(Global.TURNEAST);
                        break;
                    }
                    case UP: {
                        tcpClient.inputMoves(Global.TURNNORTH);
                        break;
                    }
                    case DOWN: {
                        tcpClient.inputMoves(Global.TURNSOUTH);
                        break;
                    }
                    case SPACE: {
                        dynamicObjectModule.keyGETPressed();
                        break;
                    }
                }

                break;
            }
        }
    }
}
