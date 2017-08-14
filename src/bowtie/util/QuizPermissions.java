package bowtie.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import bowtie.bot.Bot;
import bowtie.bot.cons.BotConstants;
import bowtie.bot.impl.QuizBot;
import bowtie.bot.obj.GuildObject;

/**
 * @author &#8904
 *
 */
public final class QuizPermissions {
	private static Bot bot;
	
	/**
	 * Sets the {@link Bot} which guilds should be checked for permissions.
	 * 
	 * @param bot
	 */
	public static void setBot(Bot bot){
		QuizPermissions.bot = bot;
	}
	
	/**
	 * Gets the permission level of the given user on that guild.
	 * 
	 * @param user
	 * @param guild
	 * @return 
	 * <ul>
	 * 		<li>{@link BotConstants#CREATOR_PERMISSION}</li>
	 * 		<li>{@link BotConstants#MASTER_PERMISSION}</li>
	 *		<li>{@link BotConstants#USER_PERMISSION}</li>
	 * </ul>
	 */
	public static int getPermissionLevel(IUser user, GuildObject guild){
		if(bot.isCreator(user)){
			return BotConstants.CREATOR_PERMISSION;
		}else if(guild.isMaster(user)){
			return BotConstants.MASTER_PERMISSION;
		}
		return BotConstants.USER_PERMISSION;
	}
	
	/**
	 * Gets the permission level of the given user. 
	 * <p>
	 * <b>Note</b> that this will return {@link BotConstants#MASTER_PERMISSION} 
	 * if the user is a master on any registered guild. This method should 
	 * only be used for private message handling.
	 * </p>
	 * 
	 * @param user
	 * @return 
	 * <ul>
	 * 		<li>{@link BotConstants#CREATOR_PERMISSION}</li>
	 * 		<li>{@link BotConstants#MASTER_PERMISSION}</li>
	 *		<li>{@link BotConstants#USER_PERMISSION}</li>
	 * </ul>
	 */
	public static int getPermissionLevel(IUser user){
		if(bot.isCreator(user)){
			return BotConstants.CREATOR_PERMISSION;
		}else if(((QuizBot)bot).isMaster(user)){
			return BotConstants.MASTER_PERMISSION;
		}
		return BotConstants.USER_PERMISSION;
	}
	
	public static List<Permissions> getMissingPermissions(EnumSet<Permissions> permissionsHave, EnumSet<Permissions> permissionsNeeded){
		List<Permissions> missing = new ArrayList<Permissions>();
		if(permissionsHave.containsAll(permissionsNeeded)){
			return missing;
		}
		for(Permissions perm : permissionsNeeded){
			if(!permissionsHave.contains(perm)){
				missing.add(perm);
			}
		}
		return missing;
	}
	
	public static List<Permissions> getMissingPermissions(EnumSet<Permissions> permissionsHave, int permissionsNeeded){
		return getMissingPermissions(permissionsHave, Permissions.getAllowedPermissionsForNumber(permissionsNeeded));
	}
}