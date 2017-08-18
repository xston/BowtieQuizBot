package bowtie.bot.obj;

import java.util.ArrayList;
import java.util.List;

import bowtie.bot.intf.CommandHandler;
import bowtie.evnt.impl.CommandEvent;


/**
 * @author &#8904
 *
 */
public abstract class Command{
	private List<String> validExpressions;
	private final int permission;
	private boolean onCooldown = false;
	
	public Command(String[] validExpressions, int permission){
		this.validExpressions = new ArrayList<String>();
		for(String command : validExpressions){
			this.validExpressions.add(command);
		}
		this.permission = permission;
	}
	
	public Command(List<String> validExpressions, int permission){
		this.validExpressions = validExpressions;
		this.permission = permission;
	}
	
	/**
	 * Checks if the given String is a valid command expression.
	 * 
	 * @param expression
	 * @return
	 */
	public boolean isValidExpression(String expression){
		if(validExpressions.contains(expression)){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the given permission level is high enough to execute this command.
	 * 
	 * @param permission
	 * @return
	 */
	public boolean isValidPermission(int permission){
		if(permission >= this.permission){
			return true;
		}
		return false;
	}
	
	public void setOnCooldown(boolean onCooldown){
		this.onCooldown = onCooldown;
	}
	
	public boolean isOnCooldown(){
		return onCooldown;
	}
	
	/**
	 * Defines the action that should be performed when this 
	 * command is called.
	 * 
	 * @param event The {@link CommandEvent} dispatched by the {@link CommandHandler}.
	 */
	public abstract void execute(CommandEvent event);
}