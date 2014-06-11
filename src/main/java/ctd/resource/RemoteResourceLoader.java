package ctd.resource;

import java.io.IOException;

import org.springframework.core.io.Resource;

public interface RemoteResourceLoader {
	public Resource load(String path,boolean cache) throws IOException;
	public Resource load(String path) throws IOException;
}
