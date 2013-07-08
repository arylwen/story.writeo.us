package us.writeo.novel.persistence;

import us.writeo.novel.model.*;

public interface SceneProcessor
{
    public void process(Scene currentScene, Object extra);
}
