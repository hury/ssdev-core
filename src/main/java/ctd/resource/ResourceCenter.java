package ctd.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;

public class ResourceCenter implements ResourceLoaderAware {
	private static boolean devMode = false;
	private static PathMatchingResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
	private static RemoteResourceLoader remoteLoader;
	
	public static Resource load(String path) throws IOException{
		Resource r = loader.getResource(ResourceUtils.CLASSPATH_URL_PREFIX + path);
		if(r.exists()){
			return r;
		}
		r = loader.getResource(path);
		if(r.exists()){
			return r;
		}
		
		if(remoteLoader != null){
			return remoteLoader.load(path,!devMode);
		}
		else{
			throw new FileNotFoundException("file not found:" + path);
		}
	}
	
	public static Resource load(String pathPrefix,String path) throws FileNotFoundException{
		Resource r =  loader.getResource(pathPrefix + path);
		if(r.exists()){
			return r;
		}
		else{
			throw new FileNotFoundException("file not found:" + path);
		}
	}
	
	public static Resource loadWithoutExistsCheck(String pathPrefix,String path){
		return loader.getResource(pathPrefix + path);
	}
	
	public void setDevMode(boolean status){
		devMode = status;
	}
	
	public static boolean isDevMode(){
		return devMode;
	}
	
	public static InputStream getInputStream(String path) throws IOException{
		return getInputStream(load(path));
	}
	
	public static InputStream getInputStream(Resource r) throws IOException{
		InputStream input = null;
		if(devMode){
			URL url = r.getURL();
			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			input = connection.getInputStream();
		}
		else{
			input = r.getInputStream();
		}
		return input;
	}
	
	public static void write(Resource r,OutputStream output) throws IOException{
		InputStream input = null;
		try{
			input = getInputStream(r);
			IOUtils.copy(input, output);
		}
		finally{
			if(input != null){
				input.close();
			}
		}
	}
	
	@Override
	public void setResourceLoader(ResourceLoader appContextLoader) {
		loader = new PathMatchingResourcePatternResolver(appContextLoader);
	}
	
	public void setRemoteResourceLoader(RemoteResourceLoader loader){
		remoteLoader = loader;
	}

	
}
