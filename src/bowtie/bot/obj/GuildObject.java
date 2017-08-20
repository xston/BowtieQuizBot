package bowtie.bot.obj;

import java.util.ArrayList;
import java.util.List;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

/**
 * Abstract class which represents a Discord guild.
 * 
 * @author &#8904
 */
public abstract class GuildObject {
	/** A list containing the users in this guild which have permission to
	 *  command the bot. */
	private List<IUser> masters;
	
	/** The {@link IGuild} object this instance is representing. */
	private final IGuild guild;
	
	/**
	 * Creates a new {@link GuildObject} instance.
	 * 
	 * @param guild The guild this object should represent.
	 */
	public GuildObject(IGuild guild){
		this.guild = guild;
		masters = new ArrayList<IUser>();
	}

	/**
	 * Gets the String ID of the represented {@link #guild}.
	 * 
	 * @return The String ID of this instances {@link #guild}.
	 */
	public String getStringID(){
		return guild.getStringID();
	}
	
	/**
	 * Gets the represented {@link #guild}.
	 * 
	 * @return This instances {@link #guild}.
	 */
	public IGuild getGuild(){
		return guild;
	}
	
	/**
	 * Gets this instances {@link #masters}.
	 * 
	 * @return This instances {@link #masters}.
	 */
	public List<IUser> getMasters(){
		return masters;
	}

	/**
	 * Sets this instances {@link #masters}.
	 * 
	 * @param masters The {@link List} which should be set as this
	 * instances {@link #masters}.
	 */
	public void setMasters(List<IUser> masters){
		this.masters = masters;
	}
	
	/**
	 * Adds the given user to this instances {@link #masters} if it is not 
	 * yet contained.
	 * 
	 * @param master The {@link IUser} that should be added.
	 * @return true if the user was not yet contained and successfully added.
	 */
	public boolean addMaster(IUser master){
		if(masters.contains(master)){
			return false;
		}
		masters.add(master);
		return true;
	}
	
	/**
	 * Removes the given {@link IUser} from this instances {@link #masters}.
	 * 
	 * @param master The {@link IUser} that should be removed.
	 * @return true if the given user was contained and successfully removed.
	 */
	public boolean removeMaster(IUser master){
		if(!masters.contains(master)){
			return false;
		}
		masters.remove(master);
		return true;
	}
	
	/**
	 * Checks if the given user is a master in this guild.
	 * 
	 * @param user The {@link IUser} whichs master permissions should be checked.
	 * @return true if the given user is contained in this instances {@link #masters}.
	 */
	public boolean isMaster(IUser user){
		return masters.contains(user);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(this.getStringID().equals(((GuildObject)obj).getStringID())){
			return true;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		return false;
	}
}