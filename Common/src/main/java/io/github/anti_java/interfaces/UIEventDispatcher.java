package io.github.anti_java.interfaces;

import io.github.anti_java.struct.UIEvent;

/**
 * Created by Davy on 2015/12/17.
 */
public interface UIEventDispatcher {
    void triggerEvent(UIEvent event);
}
