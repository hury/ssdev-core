package ctd.account.tenant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import ctd.resource.ResourceCenter;

public class TenantHomeUtils {
	private final static String HOME = "tenantProfiles/";
	
	public static String getHomePath(String tenantId){
		StringBuilder sb = new StringBuilder(HOME);
		sb.append(tenantId.replaceAll("\\.", "/")).append("/");
		return sb.toString();
	}
	
	private static File createHome(String tenantId) throws IOException{
		Resource r = ResourceCenter.loadWithoutExistsCheck(ResourceUtils.CLASSPATH_URL_PREFIX, HOME);
		if(!r.exists()){
			throw new FileNotFoundException("tenantProfiles HOME not exists.");
		}
		File root = r.getFile();
		if(root.isDirectory()){
			File tenantHomeFile = new File(r.getFile(),tenantId.replaceAll("\\.", "/") + "/");
			tenantHomeFile.mkdirs();
			return tenantHomeFile;
		}
		else{
			throw new FileNotFoundException("tenantProfiles HOME is not a directory.");
		}
	}
	
	public static String getHomeAbsolutePath(String tenantId) throws IOException{
		String path = getHomePath(tenantId);
		Resource r = ResourceCenter.loadWithoutExistsCheck(ResourceUtils.CLASSPATH_URL_PREFIX, path);
		File file = null;
		if(!r.exists()){
			file = createHome(tenantId);
		}
		else{
			file = r.getFile();
		}
		return file.getAbsolutePath();
	}
	
	public static Resource getHomeResource(String tenantId) throws IOException{
		String path = getHomeAbsolutePath(tenantId);
		return new FileSystemResource(path);
	}
	
	public static File getRelativeDirectory(String tenantId,String directory) throws IOException{
		String tenantHomePath = getHomeAbsolutePath(tenantId);
		File file = new File(tenantHomePath,directory);
		if(!file.exists()){
			file.mkdirs();
		}
		if(!file.isDirectory()){
			throw new IOException("path[" + directory + "] must be a directory");
		}
		return file;
	}
	
	public static void copyFile(String tenantId,String parentDirectory,File sourceFile) throws IOException{
		File directory = getRelativeDirectory(tenantId,parentDirectory);
		FileUtils.copyFileToDirectory(sourceFile, directory, true);
	}
	
	public static void copyFile(String tenantId,String parentDirectory,String filename,InputStream input) throws IOException{
		File directory = getRelativeDirectory(tenantId,parentDirectory);
		FileUtils.copyInputStreamToFile(input, new File(directory,filename));
	}

}
