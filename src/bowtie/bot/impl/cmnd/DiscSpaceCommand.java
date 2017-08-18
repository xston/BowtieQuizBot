package bowtie.bot.impl.cmnd;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.CodeSource;
import java.util.concurrent.atomic.AtomicLong;

import bowtie.bot.cons.Colors;
import bowtie.bot.obj.Bot;
import bowtie.bot.obj.Command;
import bowtie.core.Main;
import bowtie.evnt.impl.CommandEvent;

/**
 * @author &#8904
 *
 */
public class DiscSpaceCommand extends Command{
	private Bot bot;
	
	/**
	 * @param validExpressions
	 * @param permission
	 */
	public DiscSpaceCommand(String[] validExpressions, int permission, Bot bot) {
		super(validExpressions, permission);
		this.bot = bot;
	}

	/**
	 * @see bowtie.bot.obj.Command#execute(bowtie.evnt.impl.CommandEvent)
	 */
	@Override
	public void execute(CommandEvent event) {
		String jarFolderSize = null;
		String questionFolderSize = null;
		String logFolderSize = null;
		String dbFolderSize = null;
		try{
			String jarParentPath = getJarParentFile().getAbsolutePath();
			jarFolderSize = formatSize(size(new File(jarParentPath).toPath()));
			dbFolderSize = formatSize(size(new File(jarParentPath+"/db").toPath()));
			questionFolderSize = formatSize(size(new File(jarParentPath+"/questions").toPath()));
			logFolderSize = formatSize(size(new File(jarParentPath+"/logs").toPath()));
		}catch(Exception e){
			e.printStackTrace();
		}
		bot.sendMessage("```Total: "+jarFolderSize+"\n\n"
				+"Database: "+dbFolderSize+"\n"
				+"Questions: "+questionFolderSize+"\n"
				+"Logs: "+logFolderSize+"```", event.getMessage().getChannel(), Colors.PURPLE);
	}
	
	public File getJarParentFile(){
		try{
			CodeSource codeSource = getClass().getProtectionDomain().getCodeSource();
			File jarFile;
			if (codeSource.getLocation() != null){
			    jarFile = new File(codeSource.getLocation().toURI());
			}else{
			    String path = getClass().getResource(getClass().getSimpleName() + ".class").getPath();
			    String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
			    jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
			    jarFile = new File(jarFilePath);
			} 
			return jarFile.getParentFile();  
		}catch(Exception e){
			Main.log.print(e);
		}
		return null;
	}
	
	public long size(Path path){
	    final AtomicLong size = new AtomicLong(0);
	    try{
	        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
	            @Override
	            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
	                size.addAndGet(attrs.size());
	                return FileVisitResult.CONTINUE;
	            }

	            @Override
	            public FileVisitResult visitFileFailed(Path file, IOException exc){
	                return FileVisitResult.CONTINUE;
	            }

	            @Override
	            public FileVisitResult postVisitDirectory(Path dir, IOException exc){
	                return FileVisitResult.CONTINUE;
	            }
	        });
	    }catch (IOException e){
	    	Main.log.print(e);
	    }
	    return size.get();
	}
	
	private String formatSize(long size){
		String[] units = {"b", "kb", "mb", "gb"};
		float actSize = (float)size;
		String unit = units[0];
		for(int i = 0; i < 4; i++){
			if(actSize >= 1000){
				actSize /= 1000;
				unit = units[i+1];
			}
		}
		return String.format("%.2f", actSize)+" "+unit;
	}
}