package ch.openech.mj.toolkit;



public interface FlowField extends IComponent {

	public void add(IComponent component);

	public void addGap();
	
	public void removeAll();

	public void setEnabled(boolean enabled);
	
}