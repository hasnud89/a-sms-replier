package cn.edu.hit.voidmain.asmsreplier.replier.interfaces;

/**
 * implements the Observer pattern
 * @author voidmain
 *
 */
public interface IReplyChangeSubject {
	public void addReplyObserver(IReplyChangeObserver observer);
	public void removeReplyObserver(IReplyChangeObserver observer);
	
	public void replyChanged();
}
