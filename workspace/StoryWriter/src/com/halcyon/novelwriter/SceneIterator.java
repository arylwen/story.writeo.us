package com.halcyon.novelwriter;

import com.halcyon.novelwriter.model.*;

public interface SceneIterator
{
    public void setProcessor(SceneProcessor processor, Object args);
	public void iterate(SceneProcessor processor, Object args);
}
