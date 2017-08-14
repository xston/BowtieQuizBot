package bowtie.util;

import java.util.TimeZone;

import sx.blah.discord.handle.obj.IGuild;

/**
 * @author &#8904
 *
 */
public final class Region{
	/**
	 * Gets the {@link TimeZone} of the given {@link IGuild} based on its region.
	 * 
	 * @param guild
	 * @return
	 */
	public static TimeZone getTimeZone(IGuild guild){
		String regionName = guild.getRegion().getName();
		switch(regionName){
		case "Central Europe":
			return TimeZone.getTimeZone("CET");
		case "Western Europe":
			return TimeZone.getTimeZone("WET");
		case "Brazil":
			return TimeZone.getTimeZone("ACT");
		case "Hong Kong":
			return TimeZone.getTimeZone("Asia/Hong_Kong");
		case "Russia":
			return TimeZone.getTimeZone("Europe/Moscow");
		case "Singapore":
			return TimeZone.getTimeZone("Asia/Singapore");
		case "Sydney":
			return TimeZone.getTimeZone("Australia/Sydney");
		case "US Central":
			return TimeZone.getTimeZone("CST");
		case "US East":
			return TimeZone.getTimeZone("EST");
		case "US West":
			return TimeZone.getTimeZone("PST");
		case "US South":
			return TimeZone.getTimeZone("EST");
		default:
			return TimeZone.getTimeZone("UTC");
		}
	}
}