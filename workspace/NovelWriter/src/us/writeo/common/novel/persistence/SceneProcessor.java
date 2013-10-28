package us.writeo.common.novel.persistence;

import us.writeo.common.novel.model.*;

public interface SceneProcessor
{
    public void process(Scene currentScene, Object extra);
}
