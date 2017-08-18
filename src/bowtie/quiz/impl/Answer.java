package bowtie.quiz.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author &#8904
 *
 */
public class Answer{
	private List<String> variations;
	private boolean isBonus = false;
	
	public Answer(String[] variations){
		this(variations, false);
	}
	
	public Answer(String[] variations, boolean isBonus){
		this.variations = new ArrayList<String>();
		for(String variation : variations){
			this.variations.add(variation.trim());
		}
		this.isBonus = isBonus;
	}
	
	public Answer(List<String> variations){
		this(variations, false);
	}
	
	public Answer(List<String> variations, boolean isBonus){
		this.variations = variations;
		this.isBonus = isBonus;
	}
	
	public boolean isBonusAnswer(){
		return isBonus;
	}
	
	public void setIsBonusAnswer(boolean isBonus){
		this.isBonus = isBonus;
	}
	
	public List<String> getVariations(){
		return variations;
	}
	
	/**
	 * Returns true if the given object is a String which is contained in {@link #variations} or 
	 * if it is an {@link Answer} with an identical {@link #variations} list.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o){
		if(o instanceof String){
			if(variations.contains(o)){
				return true;
			}
		}
		if(o instanceof Answer){
			if(this.variations.equals(((Answer) o).getVariations())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString(){
		return isBonus+" "+variations.toString();
	}
}