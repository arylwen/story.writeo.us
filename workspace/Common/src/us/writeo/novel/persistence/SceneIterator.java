package us.writeo.novel.persistence;

public interface SceneIterator
{
    public void setProcessor(SceneProcessor processor, Object args);
	public void iterate(SceneProcessor processor, Object args);
}
